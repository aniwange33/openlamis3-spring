
package org.fhi360.lamis.exchange.radet;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.model.repositories.RegimenHistoryRepository;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.Scrambler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class RadetService {

    private String query;
    private Scrambler scrambler = new Scrambler();
    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;
    private String uniqueId;
    private String hospitalNum;
    private String surname;
    private String otherNames;
    private String dateBirth;
    private String age;
    private String ageUnit;
    private String gender;
    private String dateRegistration;
    private String statusRegistration;
    private String enrollmentSetting;
    private String dateStarted;
    private String currentStatus;
    private String dateCurrentStatus;
    private String clinicStage;
    private String funcStatus;
    private String cd4;
    private String cd4p;
    private String dateLastRefill;
    private String duration;
    private String regimentype;
    private String regimen;
    private String regimentypeId;
    private String regimenId;
    private String regimenStart;
    private String regimentypeStart;
    private String dateCurrentViralLoad;
    private String dateCollected;
    private String viralLoad;
    private String viralLoadIndication;
    private String sortname;
    private String category;
    private final RegimenHistoryRepository regimenHistoryRepository = ContextProvider.getBean(RegimenHistoryRepository.class);
    private final PatientRepository patientRepository = ContextProvider.getBean(PatientRepository.class);
    private ArrayList<Map<String, String>> clientList = new ArrayList<>();
    private Map<String, Map<String, String>> sortedMaps = new TreeMap<>();

    public RadetService(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    //@GetMapping
    public void buildClientList(HttpSession session) {
        long facilityId = (long) session.getAttribute("id");
        Date today = Calendar.getInstance().getTime();
        String losses = "ART Transfer Out Stopped Treatment Known Death Lost to Follow Up";

        try {
            executeUpdate("DROP VIEW IF EXISTS commence");
            executeUpdate(" CREATE VIEW commence AS SELECT * FROM clinic WHERE facility_id = " + facilityId + " AND commence = 1");

            executeUpdate("DROP VIEW IF EXISTS pharm");
            executeUpdate(" CREATE VIEW pharm AS SELECT * FROM pharmacy WHERE facility_id = " + facilityId + " AND regimentype_id IN (1, 2, 3, 4, 14)");

            query = "SELECT * FROM patient WHERE facility_id = " + facilityId + " AND date_started IS NOT NULL";
            jdbcTemplate.query(query, resultSet -> {
                while (resultSet.next()) {
                    initVariables();
                    long patientId = resultSet.getLong("patient_id");
                    hospitalNum = resultSet.getString("hospital_num");
                    uniqueId = resultSet.getString("unique_id");
                    surname = (resultSet.getString("surname") == null) ? "" : resultSet.getString("surname");
                    surname = scrambler.unscrambleCharacters(surname);
                    surname = StringUtils.upperCase(surname);
                    otherNames = (resultSet.getString("other_names") == null) ? "" : resultSet.getString("other_names");
                    otherNames = scrambler.unscrambleCharacters(otherNames);
                    otherNames = StringUtils.capitalize(otherNames);
                    dateBirth = (resultSet.getDate("date_birth") == null) ? "" :
                            DateUtil.parseDateToString(resultSet.getDate("date_birth"), "MM/dd/yyyy");
                    age = (resultSet.getInt("age") == 0) ? "" : Integer.toString(resultSet.getInt("age"));
                    ageUnit = (resultSet.getString("age_unit") == null) ? "" : resultSet.getString("age_unit");
                    gender = (resultSet.getString("gender") == null) ? "" : resultSet.getString("gender");
                    dateRegistration = (resultSet.getDate("date_registration") == null) ? "" :
                            DateUtil.parseDateToString(resultSet.getDate("date_registration"), "MM/dd/yyyy");
                    currentStatus = (resultSet.getString("current_status") == null) ? "" : resultSet.getString("current_status");
                    statusRegistration = (resultSet.getString("status_registration") == null) ? "" : resultSet.getString("status_registration");
                    enrollmentSetting = (resultSet.getString("enrollment_setting") == null) ? "" : resultSet.getString("enrollment_setting");
                    dateCurrentStatus = (resultSet.getDate("date_current_status") == null) ? "" : DateUtil.parseDateToString(resultSet.getDate("date_current_status"), "MM/dd/yyyy");
                    dateStarted = DateUtil.parseDateToString(resultSet.getDate("date_started"), "MM/dd/yyyy");

                    //Get ART Commencement data
                    query = "SELECT * FROM commence WHERE patient_id = " + patientId;
                    jdbcTemplate.query(query, clinic -> {
                        regimentypeStart = (clinic.getString("regimenType") == null) ? "" : clinic.getString("regimenType");
                        regimenStart = (clinic.getString("regimen") == null) ? "" : clinic.getString("regimen");
                        clinicStage = (clinic.getString("clinic_stage") == null) ? "" : clinic.getString("clinic_stage");
                        funcStatus = (clinic.getString("func_status") == null) ? "" : clinic.getString("func_status");
                        cd4 = (clinic.getDouble("cd4") == 0) ? "" : Double.toString(clinic.getDouble("cd4"));
                        cd4p = (clinic.getDouble("cd4p") == 0) ? "" : Double.toString(clinic.getDouble("cd4p"));
                    });

                    //Get last refill record
                    query = "SELECT DISTINCT regimentype_id, regimen_id, date_visit, duration FROM pharm WHERE patient_id = " +
                            patientId + " ORDER BY date_visit DESC LIMIT 1";
                    jdbcTemplate.query(query, pharm -> {
                        regimentypeId = Long.toString(pharm.getLong("regimentype_id"));
                        regimenId = Long.toString(pharm.getLong("regimen_id"));
                        regimentype = regimenHistoryRepository.getOne(pharm.getLong("regimentype_id")).getRegimenType();
                        regimen = regimenHistoryRepository.getOne(pharm.getLong("regimen_id")).getRegimen();
                        duration = Integer.toString(pharm.getInt("duration"));
                        dateLastRefill = (pharm.getDate("date_visit") == null) ? "" : DateUtil.parseDateToString(pharm.getDate("date_visit"), "MM/dd/yyyy");

                        //Set the default category to 5 meaning the client has no gaps in ARV refill or viral load test records
                        sortname = "E" + patientId;
                        category = "5";

                        //Check if client has no ARV refill
                        if (regimentype.trim().isEmpty() || regimen.trim().isEmpty()) {
                            sortname = "A" + patientId;
                            category = "1";
                        } else {
                            //Check if client has no regimen at start of ART
                            if (regimentypeStart.trim().isEmpty() || regimenStart.trim().isEmpty()) {
                                sortname = "B" + patientId;
                                category = "2";
                            } else {
                                if (!losses.contains(currentStatus)) {
                                    //Check if the last refill is more than 28 days ( 0r 90 days) ago from the reporting date, then this client is lost to follow up
                                    if (!dateLastRefill.isEmpty()) {
                                        if (DateUtil.addYearMonthDay(pharm.getDate("date_visit"), pharm.getInt("duration") + Constants.LTFU.PEPFAR, "DAY").before(today)) {
                                            sortname = "C" + patientId;
                                            category = "3";
                                        } else {
                                            //Check if client is due for Viral Load test
                                            Patient patient = new Patient();
                                            patient.setPatientId(patientId);
                                            boolean dueViralLoad = patientRepository.dueViralLoad(patient);
                                            if (dueViralLoad) {
                                                sortname = "D" + patientId;
                                                category = "4";
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                    // create an array from object properties
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("patientId", Long.toString(patientId));
                    map.put("uniqueId", uniqueId);
                    map.put("hospitalNum", hospitalNum);
                    map.put("name", surname + " " + otherNames);
                    map.put("surname", surname);
                    map.put("otherNames", otherNames);
                    map.put("dateBirth", dateBirth);
                    map.put("age", age);
                    map.put("ageUnit", ageUnit);
                    map.put("gender", gender);
                    map.put("dateRegistration", dateRegistration);
                    map.put("statusRegistration", statusRegistration);
                    map.put("enrollmentSetting", enrollmentSetting);
                    map.put("dateStarted", dateStarted);
                    map.put("currentStatus", currentStatus);
                    map.put("dateCurrentStatus", dateCurrentStatus);
                    map.put("clinicStage", clinicStage);
                    map.put("funcStatus", funcStatus);
                    map.put("cd4", cd4);
                    map.put("cd4p", cd4p);
                    map.put("dateLastRefill", dateLastRefill);
                    map.put("duration", duration);
                    map.put("regimentype", regimentype);
                    map.put("regimen", regimen);
                    map.put("regimentypeId", regimentypeId);
                    map.put("regimenId", regimenId);
                    map.put("regimentypeStart", regimentypeStart);
                    map.put("regimenStart", regimenStart);
                    map.put("dateCurrentViralLoad", dateCurrentViralLoad);
                    map.put("dateCollected", dateCollected);
                    map.put("viralLoad", viralLoad);
                    map.put("viralLoadIndication", viralLoadIndication);
                    map.put("category", category);
                    sortedMaps.put(sortname, map);
                }
                return null;
            });

            for (Map.Entry<String, Map<String, String>> entry : sortedMaps.entrySet()) {
                clientList.add(entry.getValue());
            }
            session.setAttribute("clientList", clientList);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public ArrayList<Map<String, String>> radetReport(String reportType, HttpSession session) {
        ArrayList<Map<String, String>> reportList = new ArrayList<>();
        clientList = retrieveClientList(session);
        for (Map<String, String> stringStringMap : clientList) {
            String id = stringStringMap.get("category");
            if (id.equals(reportType)) {
                Map<String, String> map = new HashMap<>();
                map.put("uniqueId", stringStringMap.get("uniqueId"));
                map.put("hospitalNum", stringStringMap.get("hospitalNum"));
                map.put("name", stringStringMap.get("name"));
                map.put("surname", stringStringMap.get("surname"));
                map.put("otherNames", stringStringMap.get("otherNames"));
                map.put("dateBirth", stringStringMap.get("dateBirth"));
                map.put("age", stringStringMap.get("age"));
                map.put("ageUnit", stringStringMap.get("ageUnit"));
                map.put("gender", stringStringMap.get("gender"));
                map.put("dateRegistration", stringStringMap.get("dateRegistration"));
                map.put("statusRegistration", stringStringMap.get("statusRegistration"));
                map.put("dateStarted", stringStringMap.get("dateStarted"));
                map.put("currentStatus", stringStringMap.get("currentStatus"));
                map.put("dateCurrentStatus", stringStringMap.get("dateCurrentStatus"));
                map.put("dateLastRefill", stringStringMap.get("dateLastRefill"));
                map.put("regimentype", stringStringMap.get("regimenType"));
                map.put("regimen", stringStringMap.get("regimen"));
                map.put("regimentypeStart", stringStringMap.get("regimentypeStart"));
                map.put("regimenStart", stringStringMap.get("regimenStart"));
                reportList.add(map);
            }
        }
        return reportList;
    }

    public ArrayList<Map<String, String>> retrieveClientList(HttpSession session) {
        if (session.getAttribute("clientList") != null) {
            clientList = (ArrayList) session.getAttribute("clientList");
        }
        return clientList;
    }

    public void clearClientList(HttpSession session) {
        clientList = retrieveClientList(session);
        clientList.clear();
        session.setAttribute("clientList", clientList);
    }

    private void initVariables() {
        uniqueId = "";
        hospitalNum = "";
        surname = "";
        otherNames = "";
        dateBirth = "";
        age = "";
        ageUnit = "";
        gender = "";
        dateRegistration = "";
        statusRegistration = "";
        enrollmentSetting = "";
        dateStarted = "";
        currentStatus = "";
        dateCurrentStatus = "";
        clinicStage = "";
        funcStatus = "";
        cd4 = "";
        cd4p = "";
        dateLastRefill = "";
        duration = "";
        regimentype = "";
        regimen = "";
        regimentypeId = "";
        regimenId = "";
        regimenStart = "";
        regimentypeStart = "";
        dateCurrentViralLoad = "";
        dateCollected = "";
        viralLoad = "";
        viralLoadIndication = "";
        sortname = "";
    }

    private void executeUpdate(String query) {
        transactionTemplate.execute(status -> {
            jdbcTemplate.execute(query);
            return null;
        });
    }
}
