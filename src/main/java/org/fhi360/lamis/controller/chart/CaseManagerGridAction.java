
package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.controller.mapstruct.PatientMapper;
import org.fhi360.lamis.model.CaseManager;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.PatientDTO;
import org.fhi360.lamis.model.repositories.CaseManagerRepository;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.PatientNumberNormalizer;
import org.fhi360.lamis.utility.Scrambler;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;


@RestController
@RequestMapping("/chart/case-manager")
@Api(tags = "CaseManager Grid Chart", description = " ")
public class CaseManagerGridAction {
    private final CaseManagerRepository caseManagerRepository;
    private final FacilityRepository facilityRepository;
    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private ArrayList<Map<String, String>> caseManagerList = new ArrayList<Map<String, String>>();
    private ArrayList<Map<String, String>> caseManagerClientsList = new ArrayList<Map<String, String>>();
    private ArrayList<Map<String, String>> clientSearchList = new ArrayList<Map<String, String>>();
    private Map<String, String> clientsStatusCountMap = new TreeMap<String, String>();
    private Map<String, String> clientsCategoryCountMap = new TreeMap<String, String>();
    private Map<String, String> clientsMap = new TreeMap<String, String>();


    public CaseManagerGridAction(CaseManagerRepository caseManagerRepository, FacilityRepository facilityRepository,
                                 JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate, PatientRepository patientRepository, PatientMapper patientMapper) {
        this.caseManagerRepository = caseManagerRepository;
        this.facilityRepository = facilityRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    @GetMapping("/grid")
    public ResponseEntity caseManagerGrid(HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        Facility facility = facilityRepository.getOne(facilityId);
        List<CaseManager> caseManagers = caseManagerRepository.findByFacilityOrderByFullName(facility);
        Map<String, Object> response = new HashMap<>();
        response.put("casemanagerList", caseManagers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/clients")
    public ResponseEntity caseManagerClientsGrid(@RequestParam("casemanagerId") Long id) {
        CaseManager caseManager = caseManagerRepository.getOne(id);
        List<Patient> patients = patientRepository.findByCaseManager(caseManager);
        List<PatientDTO> dtos = patientMapper.patientToDto(patients);
        Map<String, Object> response = new HashMap<>();
        response.put("clientSearchList", dtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/init-client-search")
    public ResponseEntity initClientSearch(HttpSession session) {
        //Create a temporary Table to hold values.
        Map<String, String> clientsMap = new HashMap<>();
        try {
            if (!checkDatabaseTable()) {
                String client_drop = "DROP VIEW clients IF EXISTS";
                transactionTemplate.execute(status -> {
                    jdbcTemplate.execute(client_drop);
                    return null;
                });

                Long facilityId = (Long) session.getAttribute("id");

                //(
                String client = " CREATE VIEW IF NOT EXISTS clients (facility_id bigint, patient_id bigint, " +
                        "hospital_num varchar(25), surname varchar(45), other_names varchar(75), gender varchar(7), " +
                        "state varchar(75), lga varchar(150), date_birth date, age int(11), age_unit varchar(30)," +
                        " address varchar(100), date_started date, current_viral_load double, current_cd4 double, " +
                        "current_cd4p double, current_status varchar(75), status int(11), casemanager_id bigint, " +
                        "pregnant int(11), breastfeeding int(11))";
                //String client = " CREATE VIEW IF NOT EXISTS clients (facility_id INT, patient_id INT, hospital_num TEXT, surname TEXT, other_names TEXT, gender TEXT, date_birth DATE, address TEXT, date_started DATE, current_viral_load INT, current_cd4 INT, current_status TEXT, status INT)";

                transactionTemplate.execute(status -> {
                    jdbcTemplate.execute(client);
                    return null;
                });

                //ACTIVE CLIENTS
                String active_clients = "INSERT INTO clients (facility_id, patient_id, hospital_num, surname, " +
                        "other_names, gender, state, lga, date_birth, age, age_unit, address, date_started, " +
                        "current_viral_load, current_cd4, current_cd4p, current_status, status, " +
                        "casemanager_id, pregnant, breastfeeding)"
                        + "SELECT facility_id, patient_id, hospital_num, surname, other_names, gender, state, lga, " +
                        "date_birth, age, age_unit, address, date_started, last_viral_load, last_cd4, last_cd4p, " +
                        "current_status, 0, casemanager_id, 0, 0 FROM patient WHERE current_status NOT IN" +
                        " ('Known Death', 'ART Transfer Out', 'Pre-ART Transfer Out') AND facility_id =" + facilityId;
                transactionTemplate.execute(status -> {
                    jdbcTemplate.execute(active_clients);
                    return null;
                });

                //Update pregnant and breastfeeding...
                pregnantWomen(session);
                breastfeedingWomen(session);

                String query_pregnant = "UPDATE clients SET pregnant = 1 where patient_id IN (SELECT " +
                        "patient_id from pregnants)";

                transactionTemplate.execute(status -> {
                    jdbcTemplate.update(query_pregnant);
                    return null;
                });

                String query_breastfeeding = "UPDATE clients SET breastfeeding = 1 where patient_id IN (SELECT patient_id from breastfeeding)";

                transactionTemplate.execute(status -> {
                    jdbcTemplate.update(query_breastfeeding);
                    return null;
                });

                //GROUP VARIOUS CLIENTS BY STATUS
                String query_client_status = "SELECT * FROM clients";

                jdbcTemplate.query(query_client_status, (ResultSet resultSet1) -> {
                    //Iterate through the result set...
                    while (resultSet1.next()) {
                        long patientId = resultSet1.getLong("patient_id");

                        String currentStatus = resultSet1.getString("current_status");
                        String patient_id = String.valueOf(resultSet1.getLong("patient_id"));
                        String currentViralLoad = resultSet1.getObject("current_viral_load") == null ? "" :
                                resultSet1.getDouble("current_viral_load") == 0.0 ? "" :
                                        Double.toString(resultSet1.getDouble("current_viral_load"));
                        String currentCd4 = resultSet1.getObject("current_cd4") == null ? "0" :
                                resultSet1.getDouble("current_cd4") == 0.0 ? "0" :
                                        Double.toString(resultSet1.getDouble("current_cd4"));
                        String currentCd4p = resultSet1.getObject("current_cd4p") == null ? "0" :
                                resultSet1.getDouble("current_cd4p") == 0.0 ? "0" :
                                        Double.toString(resultSet1.getDouble("current_cd4p"));
                        if (!currentStatus.equals("HIV+ non ART") && !currentStatus.equals("Pre-ART Transfer In")) {
                            String dateStarted = resultSet1.getObject("date_started") == null ? "" :
                                    DateUtil.parseDateToString(resultSet1.getDate("date_started"), "yyyy-MM-dd");
                            if (!dateStarted.equals("")) {
                                LocalDate startDate = LocalDate.parse(dateStarted);
                                LocalDate today = LocalDate.now();
                                Period intervalPeriod = Period.between(startDate, today);
                                if (intervalPeriod.getYears() >= 1) { //START WORKING ON STABLE OR UNSTABLE
                                    if (currentStatus.equals("Stopped Treatment") && currentStatus.equals("Lost to Follow Up")) {
                                        String internal_query = "UPDATE clients SET status = " +
                                                Constants.CaseManager.UNSTABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                        transactionTemplate.execute((ts) -> {
                                            jdbcTemplate.update(internal_query);
                                            return null;
                                        });

                                    } else {
                                        //Check for the stablilty or unstablility...
                                        if (preceedingOis(patientId) == 0) { //-- Preceeding Ois
                                            if (clinicVisits(patientId) >= 5) { //-- Clinic Visits
                                                if (!currentViralLoad.equals("")) { //-- Viral Load
                                                    if (Double.parseDouble(currentViralLoad) < 1000) {
                                                        String internal_query = "UPDATE clients SET status = " +
                                                                Constants.CaseManager.STABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                                        transactionTemplate.execute((ts) -> {
                                                            jdbcTemplate.update(internal_query);
                                                            return null;
                                                        });

                                                    } else {
                                                        String internal_query = "UPDATE clients SET status = " + Constants.CaseManager.UNSTABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                                        transactionTemplate.execute((ts) -> {
                                                            jdbcTemplate.update(internal_query);
                                                            return null;
                                                        });
                                                    }
                                                } else {
                                                    if (!currentCd4.equals("0")) {
                                                        if (Double.parseDouble(currentCd4) > 250) {
                                                            String internal_query = "UPDATE clients SET status = " +
                                                                    Constants.CaseManager.STABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                                            transactionTemplate.execute((ts) -> {
                                                                jdbcTemplate.update(internal_query);
                                                                return null;
                                                            });
                                                        } else {
                                                            String internal_query = "UPDATE clients SET status = " +
                                                                    Constants.CaseManager.UNSTABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                                            transactionTemplate.execute((ts) -> {
                                                                jdbcTemplate.update(internal_query);
                                                                return null;
                                                            });
                                                        }
                                                    } else {
                                                        if (!currentCd4p.equals("0")) {
                                                            if (Double.parseDouble(currentCd4p) > 250) {
                                                                String internal_query = "UPDATE clients SET status = " +
                                                                        Constants.CaseManager.STABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                                                transactionTemplate.execute((ts) -> {
                                                                    jdbcTemplate.update(internal_query);
                                                                    return null;
                                                                });
                                                            } else {
                                                                String internal_query = "UPDATE clients SET status = " +
                                                                        Constants.CaseManager.UNSTABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                                                transactionTemplate.execute((ts) -> {
                                                                    jdbcTemplate.update(internal_query);
                                                                    return null;
                                                                });
                                                            }
                                                        } else {
                                                            String internal_query = "UPDATE clients SET status = " +
                                                                    Constants.CaseManager.UNSTABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                                            transactionTemplate.execute((ts) -> {
                                                                jdbcTemplate.update(internal_query);
                                                                return null;
                                                            });
                                                        }
                                                    }
                                                }
                                            } else {//UNSTABLE
                                                String internal_query = "UPDATE clients SET status = " +
                                                        Constants.CaseManager.UNSTABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                                transactionTemplate.execute((ts) -> {
                                                    jdbcTemplate.update(internal_query);
                                                    return null;
                                                });
                                            }
                                        } else {//UNSTABLE
                                            String internal_query = "UPDATE clients SET status = " +
                                                    Constants.CaseManager.UNSTABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                            transactionTemplate.execute((ts) -> {
                                                jdbcTemplate.update(internal_query);
                                                return null;
                                            });
                                        }
                                    }
                                } else { //UNSTABLE NOT ONE YEAR = 3
                                    if (currentStatus.equals("Stopped Treatment") && currentStatus.equals("Lost to Follow Up")) {
                                        String internal_query = "UPDATE clients SET status = " +
                                                Constants.CaseManager.UNSTABLE_NOT_ONE_YEAR + " WHERE patient_id = " + patientId;
                                        transactionTemplate.execute((ts) -> {
                                            jdbcTemplate.update(internal_query);
                                            return null;
                                        });
                                    } else {
                                        String internal_query = "UPDATE clients SET status = " +
                                                Constants.CaseManager.UNSTABLE_NOT_ONE_YEAR + " WHERE patient_id = " + patientId;
                                        transactionTemplate.execute((ts) -> {
                                            jdbcTemplate.update(internal_query);
                                            return null;
                                        });
                                    }
                                }
                            } else { //UNSTABLE NOT ONE YEAR = 3
                                String internal_query = "UPDATE clients SET status = " +
                                        Constants.CaseManager.UNSTABLE_NOT_ONE_YEAR + " WHERE patient_id = " + patientId;
                                transactionTemplate.execute((ts) -> {
                                    jdbcTemplate.update(internal_query);
                                    return null;
                                });
                            }
                        } else {
                            //Pre-ART = 4
                            String internal_query = "UPDATE clients SET status = " +
                                    Constants.CaseManager.PRE_ART + " WHERE patient_id = " + patientId;
                            transactionTemplate.execute((ts) -> {
                                jdbcTemplate.update(internal_query);
                                return null;
                            });
                        }

                    }
                    return null;
                });
                clientsMap.put("Clients", "success");
            } else {
                clientsMap.put("Clients", "success");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.ok(clientsMap);
    }

    private boolean checkDatabaseTable() throws Exception {
        boolean isConnected = false;
        DatabaseMetaData dbm = jdbcTemplate.getDataSource().getConnection().getMetaData();
        // check if "employee" table is there
        ResultSet tables = dbm.getTables(null, null, "CLIENTS", null);
        // Table exists
        // Table does not exist
        isConnected = tables.next();

        return isConnected;
    }

    @GetMapping("/search-grid")
    public void clientSearchGrid(@RequestParam("rows") Integer rows, @RequestParam("page") Integer page, @RequestParam("start") Integer start,
                                 @RequestParam(required = false, value = "gender") String gender,
                                 @RequestParam(required = false, value = "ageGroup") String ageGroup,
                                 @RequestParam(required = false, value = "state") String state,
                                 @RequestParam(required = false, value = "lga") String lga,
                                 @RequestParam(required = false, value = "pregnancyStatus") String pregnancyStatus,
                                 @RequestParam(required = false, value = "categoryId") String categoryId,
                                 @RequestParam(required = false, value = "showAssigned") Boolean showAssigned,
                                 HttpSession session) {

        Map<String, Object> pagerParams = null;
        String seive_query = "";

        try {
            //Initilaize the Client Search
            //initClientSearch();

            if (checkDatabaseTable()) {
                pagerParams = new PaginationUtil().paginateGrid(page, rows, "clients");

                int numberOfRows = rows;
                System.out.println("Retriving clients......");

                //Call for Grid..
                Long facilityId = (Long) session.getAttribute("id");
                String query = "SELECT clients * FROM clients WHERE facility_id = " + facilityId;

                System.out.println("Retriving clients   2......");
                if (!StringUtils.equals(gender, "--All--")) {
                    query += " AND gender = '" + gender + "'";
                }
                //TODO: Arrange Age Group:
                if (!StringUtils.equals(ageGroup, "0")) {
                    String[] ageRange = ageGroup.split("-");
                    query += " AND TIMESTAMPDIFF(YEAR, date_birth, CURDATE()) >= " + Integer.parseInt(ageRange[0]) +
                            " AND TIMESTAMPDIFF(YEAR, date_birth, CURDATE()) <= " + Integer.parseInt(ageRange[1]);
                }
                if (!StringUtils.isEmpty(state)) {
                    query += " AND state = '" + state + "'";
                }
                if (!StringUtils.isEmpty(lga)) {
                    query += " AND lga = '" + lga + "'";
                }
                //Pregnancy Status
                if (!StringUtils.equals(pregnancyStatus, "--All--")) {
                    if (Integer.parseInt(pregnancyStatus) == 1) {
                        query += " AND pregnant = 1";
                    } else if (Integer.parseInt(pregnancyStatus) == 2) {
                        query += " AND breastfeeding = 1";
                    }
                }

                if (!StringUtils.isEmpty(categoryId)) {
                    if (categoryId.equals("0")) {
                        query += " AND (status = " + Constants.CaseManager.STABLE_ONE_YEAR + " OR status = " +
                                Constants.CaseManager.UNSTABLE_NOT_ONE_YEAR + " OR status = " +
                                Constants.CaseManager.UNSTABLE_ONE_YEAR + " OR status = " +
                                Constants.CaseManager.PRE_ART + ")";
                    } else if (categoryId.equals("1")) {
                        query += " AND status = " + Constants.CaseManager.STABLE_ONE_YEAR;
                    } else if (categoryId.equals("2")) {
                        query += " AND status = " + Constants.CaseManager.UNSTABLE_NOT_ONE_YEAR;
                    } else if (categoryId.equals("3")) {
                        query += " AND status = " + Constants.CaseManager.UNSTABLE_ONE_YEAR;
                    } else if (categoryId.equals("4")) {
                        query += " AND status = " + Constants.CaseManager.PRE_ART;
                    }
                }

                if (!showAssigned == false) {
                    if (Boolean.valueOf(showAssigned == true)) {
                        query += " AND (casemanager_id IS NOT NULL AND casemanager_id > 0)";
                    } else {
                        query += " AND (casemanager_id IS NULL OR casemanager_id = 0) ";
                    }
                }
                System.out.println("Retriving clients    3......");

                seive_query = query.replace("clients.*", "status, COUNT (clients.patient_id) AS count");
                seive_query += " GROUP BY status ORDER BY status ASC";

                System.out.println("Sieve Query is: " + seive_query);
                session.setAttribute("query", seive_query);
                query += " ORDER BY hospital_num, address ASC LIMIT " + start + " , " + numberOfRows;
                System.out.println("Retriving clients   4......" + query);

                jdbcTemplate.query(query, (ResultSet resultSet) -> {
//                    new CaseManagerListBuilder().buildClientSearchList(resultSet);
//                    clientSearchList = new CaseManagerListBuilder().retrieveClientSearchList();
                    //System.out.println("Retriving clients......" + clientSearchList);
                });
                System.out.println("Retriving clients   5......");
            }
        } catch (Exception exception) {
            //   resultSet = null;
            //jdbcUtil.disconnectFromDatabase();  //disconnect from database
            exception.printStackTrace();
        }

//        page = (Integer) pagerParams.get("page");
//        currpage = (Integer) pagerParams.get("page");
//        totalpages = (Integer) pagerParams.get("totalpages");
//        totalrecords = (Integer) pagerParams.get("totalrecords");

        //   return SUCCESS;
    }

    public String getAssignedClientsTreatmentStatus(@RequestParam("id") Long facilityId,
                                                    @RequestParam("casemanagerId") Long casemanagerId) {
        try {

            if (casemanagerId != null) {
                casemanagerId = casemanagerId;

                String internalQuery = "SELECT DISTINCT status as treatment_status, COUNT(status) as treatment_status_count FROM clients WHERE facility_id = " + facilityId + " AND casemanager_id = " + casemanagerId + " GROUP BY status oRDER BY status ASC";
                jdbcTemplate.query(internalQuery, (ResultSet internalResultSet) -> {
                    while (internalResultSet.next()) {
                        Integer treatmentStatus = internalResultSet.getInt("treatment_status");
                        Integer treatmentStatusCount = internalResultSet.getInt("treatment_status_count");
                        switch (treatmentStatus) {
                            case 1:
                                clientsStatusCountMap.put("stable", Integer.toString(treatmentStatusCount));
                                break;
                            case 2:
                                clientsStatusCountMap.put("unstable_less", Integer.toString(treatmentStatusCount));
                                break;
                            case 3:
                                clientsStatusCountMap.put("unstable_more", Integer.toString(treatmentStatusCount));
                                break;
                            case 4:
                                clientsStatusCountMap.put("preart", Integer.toString(treatmentStatusCount));
                                break;
                            default:
                                break;
                        }
                    }
                    return null;
                });

                //System.out.println("The data is: "+clientsStatusCountMap);
            }
        } catch (Exception exception) {
            // resultSet = null;
            //jdbcUtil.disconnectFromDatabase();  //disconnect from database
            exception.printStackTrace();
            return "ERROR";
        }

        return "SUCCESSS";
    }

    public String clientGrid(HttpSession session,
                             @RequestParam("page") int page,
                             @RequestParam("rows") int rows,
                             @RequestParam("name") String name1,
                             @RequestParam("gender") String gender
    ) {
        long facilityId = (Long) session.getAttribute("id");
        Scrambler scrambler = new Scrambler();
        Map<String, Object> pagerParams = new PaginationUtil().paginateGrid(page, rows, "clients");
        int start = (Integer) pagerParams.get("start");
        String query = "";
        int numberOfRows = rows;
        try {

            if (name1 != null) {
                String name = scrambler.scrambleCharacters(name1);
                if (name == null || name.isEmpty()) {
                    query = "SELECT * FROM clients WHERE facility_id = " + facilityId + " ORDER BY surname ASC LIMIT " + start + " , " + numberOfRows;
                    if (gender.equalsIgnoreCase("female")) {
                        query = "SELECT * FROM clients WHERE facility_id = " + facilityId + " AND gender = 'Female' ORDER BY surname ASC LIMIT " + start + " , " + numberOfRows;
                    }
                } else {
                    query = "SELECT * FROM clients WHERE facility_id = " + facilityId + " AND surname LIKE '" + name + "%' OR other_names LIKE '" + name + "%' ORDER BY surname ASC LIMIT " + start + " , " + numberOfRows;
                    if (gender.equalsIgnoreCase("female")) {
                        query = "SELECT * FROM clients WHERE facility_id = " + facilityId + " AND gender = 'Female' AND surname LIKE '" + name + "%' OR other_names LIKE '" + name + "%' ORDER BY surname ASC LIMIT " + start + " , " + numberOfRows;
                    }
                }
            } else {
                query = "SELECT * FROM clients WHERE facility_id = " + facilityId + " ORDER BY surname ASC LIMIT " + start + " , " + numberOfRows;
                if (gender.equalsIgnoreCase("female")) {
                    query = "SELECT * FROM clients WHERE facility_id = " + facilityId + " AND gender = 'Female' ORDER BY surname ASC LIMIT " + start + " , " + numberOfRows;
                }
            }

            jdbcTemplate.query(query, (ResultSet resultSet) -> {
                while (resultSet.next()) {
                    String patientId = Long.toString(resultSet.getLong("patient_id"));
                    String facilityId1 = Long.toString(resultSet.getLong("facility_id"));
                    String hospitalNum = resultSet.getString("hospital_num");
                    String surname = scrambler.unscrambleCharacters(resultSet.getString("surname"));

                    surname = StringUtils.upperCase(surname);
                    String otherNames = scrambler.unscrambleCharacters(resultSet.getString("other_names")) ;

                    otherNames = StringUtils.capitalize(otherNames);
                    String gender1 = resultSet.getString("gender");
                    String dateBirth = resultSet.getObject("date_birth") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_birth"), "MM/dd/yyyy");
                    String address = resultSet.getObject("address") == null ? "" : resultSet.getString("address");
                    address = scrambler.unscrambleCharacters(address);
                    address = StringUtils.capitalize(address);
                    String dateStarted = resultSet.getObject("date_started") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_started"), "MM/dd/yyyy");
                    String currentViralLoad = resultSet.getString("current_viral_load");
                    String currentCd4 = resultSet.getString("current_cd4");
                    String currentCd4p = resultSet.getString("current_cd4p");
                    String currentStatus = resultSet.getString("current_status");
                    String status = resultSet.getString("status");

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("patientId", patientId);
                    map.put("facilityId", facilityId1);
                    map.put("hospitalNum", hospitalNum);
                    map.put("name", surname.concat(" " + otherNames));
                    map.put("fullName", String.valueOf(patientId));
                    map.put("otherNames", otherNames);
                    map.put("gender", gender);
                    map.put("dateBirth", dateBirth);
                    map.put("address", address);
                    map.put("dateStarted", dateStarted);
                    map.put("currentViralLoad", currentViralLoad);
                    map.put("currentCd4", currentCd4 != null ? currentCd4 : currentCd4p);
                    map.put("currentStatus", currentStatus);
                    //if(status == 1)

                    map.put("status", status);
                    clientSearchList.add(map);
                }
                session.setAttribute("clientSearchList", clientSearchList);
            });

        } catch (Exception exception) {
            //resultSet = null;
            //jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }

        return "SUCCESS";
    }

    @GetMapping("clientCridByNumber")
    public String clientGridByNumber(HttpSession session, @RequestParam("hospitalNum") String hospitalNum1, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        long facilityId = (Long) session.getAttribute("id");

        Map<String, Object> pagerParams = new PaginationUtil().paginateGrid(page, rows, "clients");
        int start = (Integer) pagerParams.get("start");
        int numberOfRows = rows;
        String query = "";
        try {
            // fetch the required records from the database

            if (hospitalNum1 == null || hospitalNum1.isEmpty()) {
                query = "SELECT * FROM clients WHERE facility_id = " + facilityId + " ORDER BY current_status ASC LIMIT " + start + " , " + numberOfRows;
            } else {
                query = "SELECT * FROM clients WHERE facility_id = " + facilityId + " AND TRIM(LEADING '0' FROM hospital_num) LIKE '" + PatientNumberNormalizer.unpadNumber(hospitalNum1) + "%' ORDER BY current_status ASC LIMIT " + start + " , " + numberOfRows;
            }
            Scrambler scrambler = new Scrambler();
            jdbcTemplate.query(query, (ResultSet resultSet) -> {
                while (resultSet.next()) {
                    String patientId = Long.toString(resultSet.getLong("patient_id"));
                    String facilityId1 = Long.toString(resultSet.getLong("facility_id"));
                    String hospitalNum = resultSet.getString("hospital_num");
                    String surname = scrambler.unscrambleCharacters(resultSet.getString("surname"));

                    surname = StringUtils.upperCase(surname);
                    String otherNames = scrambler.unscrambleCharacters(resultSet.getString("other_names"));

                    otherNames = StringUtils.capitalize(otherNames);
                    String gender1 = resultSet.getString("gender");
                    String dateBirth = resultSet.getObject("date_birth") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_birth"), "MM/dd/yyyy");
                    String address = resultSet.getObject("address") == null ? "" : resultSet.getString("address");
                    address = scrambler.unscrambleCharacters(address);
                    address = StringUtils.capitalize(address);
                    String dateStarted = resultSet.getObject("date_started") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_started"), "MM/dd/yyyy");
                    String currentViralLoad = resultSet.getString("current_viral_load");
                    String currentCd4 = resultSet.getString("current_cd4");
                    String currentCd4p = resultSet.getString("current_cd4p");
                    String currentStatus = resultSet.getString("current_status");
                    String status = resultSet.getString("status");

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("patientId", patientId);
                    map.put("facilityId", facilityId1);
                    map.put("hospitalNum", hospitalNum);
                    map.put("name", surname.concat(" " + otherNames));
                    map.put("fullName", String.valueOf(patientId));
                    map.put("otherNames", otherNames);
                    map.put("gender", gender1);
                    map.put("dateBirth", dateBirth);
                    map.put("address", address);
                    map.put("dateStarted", dateStarted);
                    map.put("currentViralLoad", currentViralLoad);
                    map.put("currentCd4", currentCd4 != null ? currentCd4 : currentCd4p);
                    map.put("currentStatus", currentStatus);
                    //if(status == 1)

                    map.put("status", status);
                    clientSearchList.add(map);
                }
                session.setAttribute("clientSearchList", clientSearchList);
                return null;
            });

        } catch (Exception exception) {
            //resultSet = null;
            //jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }

        return "SUCCESS";
    }

    double baselineCd4 = 0.0;

    private double baselineCd4(long patientId) {

        String query = "SELECT cd4 FROM clinic WHERE patient_id = " + patientId + " AND commence = 1";
        jdbcTemplate.query(query, (ResultSet resultSet) -> {
            while (resultSet.next()) {
                String baselineCd4Str = resultSet.getObject("cd4") == null ? "0" : resultSet.getDouble("cd4") == 0.0 ? "0" : Double.toString(resultSet.getDouble("cd4"));
                if (!baselineCd4Str.equals("0")) {
                    baselineCd4 = Double.parseDouble(baselineCd4Str);
                }
            }
            return null;
        });

        return baselineCd4;
    }

    private void pregnantWomen(HttpSession session) {

        try {
            String client_drop = "DROP VIEW pregnants IF EXISTS";
            transactionTemplate.execute(status -> {
                jdbcTemplate.execute(client_drop);
                return null;
            });

            long facilityId = (Long) session.getAttribute("id");

            String pregnants = " CREATE VIEW IF NOT EXISTS pregnants (patient_id bigint, facility_id int," +
                    " date_visit date)";
            transactionTemplate.execute(status -> {
                jdbcTemplate.execute(pregnants);
                return null;
            });

            String pregnant_clients = "INSERT INTO pregnants (patient_id, facility_id, date_visit)" +
                    " SELECT DISTINCT patient_id, facility_id, date_visit FROM clinic WHERE pregnant = 1 " +
                    "AND facility_id =" + facilityId + " ORDER BY date_visit DESC";
            transactionTemplate.execute(status -> {
                jdbcTemplate.update(pregnant_clients);
                return null;
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void breastfeedingWomen(HttpSession session) {
        String client_drop = "DROP VIEW breastfeeding IF EXISTS";
        jdbcTemplate.execute(client_drop);
        transactionTemplate.execute(status -> {
            jdbcTemplate.execute(client_drop);
            return null;
        });
        Long facilityId = (Long) session.getAttribute("id");

        String client = " CREATE VIEW IF NOT EXISTS breastfeeding (patient_id bigint, facility_id bigint, " +
                "date_visit date)";

        jdbcTemplate.execute(client_drop);
        transactionTemplate.execute(status -> {
            jdbcTemplate.execute(client);
            return null;
        });
        String active_clients = "INSERT INTO breastfeeding ( patient_id, facility_id, date_visit)"
                + "SELECT DISTINCT patient_id, facility_id, date_visit FROM clinic WHERE " +
                "breastfeeding = 1 AND facility_id =" + facilityId + " ORDER BY date_visit DESC";

        jdbcTemplate.execute(client_drop);
        transactionTemplate.execute(status -> {
            jdbcTemplate.update(active_clients);
            return null;
        });
    }

    private int clinicVisitCount = 0;

    private int clinicVisits(long patientId) {

        String query = "SELECT count(*) AS count FROM clinic WHERE patient_id = " + patientId + " " +
                "AND date_visit >= DATE_ADD(CURDATE(), INTERVAL -12 MONTH) AND date_visit <= CURDATE()";

        jdbcTemplate.query(query, ((resultSet) -> {
            while (resultSet.next()) {
                clinicVisitCount = resultSet.getInt("count");
            }
            return null;
        }));

        return clinicVisitCount;
    }

    private int preceedingOis(long patientId) {
        String query = "SELECT count(*) AS count FROM clinic WHERE patient_id = " + patientId + " AND date_visit >= " +
                "DATE_ADD(CURDATE(), INTERVAL -16 MONTH) AND date_visit <= CURDATE() AND oi_ids IS NOT NULL AND oi_ids != ''";
        jdbcTemplate.query(query, ((resultSet) -> {
            while (resultSet.next()) {
                clinicVisitCount = resultSet.getInt("count");
            }
            return null;
        }));

        return clinicVisitCount;
    }

    boolean recentTxNew = false;

    private boolean isRecentTxNew(long patientId) {

        String query = "SELECT patient_id FROM patient WHERE patient_id = " + patientId + " " +
                "AND TIMESTAMPDIFF(MONTH, date_started, CURDATE()) < 12";

        jdbcTemplate.query(query, ((resultSet) -> {
            while (resultSet.next()) {
                recentTxNew = true;
            }
            return null;
        }));

        return recentTxNew;
    }

    //    private int countStatus(Integer option){
//
//        int counts = 0;
//        String query = "SELECT COUNT(status) AS count FROM clients WHERE status = "+option;
//        JDBCUtil jdbcUtil = null;
//        try{
//            jdbcUtil = new JDBCUtil();
//            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                counts = resultSet.getInt("count");
//                if(option == 1)
//                    clientsCategoryCountMap.put("stable", Integer.toString(counts));
//                else if(option == 2)
//                    clientsCategoryCountMap.put("unstable", Integer.toString(counts));
//                else if(option == 3)
//                    clientsCategoryCountMap.put("tx_new", Integer.toString(counts));
//                else if(option == 4)
//                    clientsCategoryCountMap.put("preart", Integer.toString(counts));
//            }
//        }catch(Exception ex){
//            jdbcUtil.disconnectFromDatabase();
//            ex.printStackTrace();
//        }
//
//        return counts;
//    }
    public String countCategories(HttpSession session) {
       long facilityId = (Long) session.getAttribute("id");

        //String internalQuery = "SELECT DISTINCT status as option, COUNT(status) AS count FROM clients WHERE facility_id = "+id+" GROUP BY status ORDER BY status ASC";
        if (session.getAttribute("query") != null) {
         String   final_seive = (String) session.getAttribute("query");
            jdbcTemplate.query(final_seive, ((internalResultSet) -> {
                while (internalResultSet.next()) {
                    Integer defaultCount = 0;
                    Integer count = internalResultSet.getInt("count");
                    Integer option = internalResultSet.getInt("status");
                    //System.out.println("Data is: "+count+" and option is: "+option);
                    if (option == 1) {
                        clientsCategoryCountMap.put("stable", Integer.toString(count));
                    } else {
                        if (!clientsCategoryCountMap.containsKey("stable")) {
                            clientsCategoryCountMap.put("stable", Integer.toString(defaultCount));
                        }
                    }
                    if (option == 2) {
                        clientsCategoryCountMap.put("unstable_less", Integer.toString(count));
                    } else {
                        if (!clientsCategoryCountMap.containsKey("unstable_less")) {
                            clientsCategoryCountMap.put("unstable_less", Integer.toString(defaultCount));
                        }
                    }
                    if (option == 3) {
                        clientsCategoryCountMap.put("unstable_more", Integer.toString(count));
                    } else {
                        if (!clientsCategoryCountMap.containsKey("unstable_more")) {
                            clientsCategoryCountMap.put("unstable_more", Integer.toString(defaultCount));
                        }
                    }
                    if (option == 4) {
                        clientsCategoryCountMap.put("preart", Integer.toString(count));
                    } else {
                        if (!clientsCategoryCountMap.containsKey("preart")) {
                            clientsCategoryCountMap.put("preart", Integer.toString(defaultCount));
                        }
                    }
                }
                return null;
            }));

        }
        return "SUCCESS";
    }


}

