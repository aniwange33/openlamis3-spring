/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.chart;
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.utility.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PatientGridAction {
    private static final Logger LOG = LoggerFactory.getLogger(PatientGridAction.class);
    private final JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);
    private final TransactionTemplate transactionTemplate = ContextProvider.getBean(TransactionTemplate.class);

    private HttpServletRequest request;
    private HttpSession session;
    private String query;
    private final PatientRepository patientRepository;

    private ArrayList<Map<String, String>> patientList = new ArrayList<>();
    private Map<String, Map<String, String>> sortedMaps = new TreeMap<>();

    public PatientGridAction(HttpServletRequest request, HttpSession session, PatientRepository patientRepository) {
        this.request = request;
        this.session = session;
        this.patientRepository = patientRepository;
    }

    @GetMapping("patientGrid")
    public String patientGrid(@RequestParam("id") Long facilityId, @RequestParam("hospitalNum") String hospitalNum, @RequestParam("rows") int rows, @RequestParam("page") int page) {
        Scrambler scrambler = new Scrambler();
        Map<String, Object> pagerParams = new PaginationUtil().paginateGrid(page, rows, "patient");
        int start = (Integer) pagerParams.get("start");
        int numberOfRows = rows;
        if (request.getParameterMap().containsKey("name")) {
            String name = scrambler.scrambleCharacters(request.getParameter("name"));
            name = name.toUpperCase();
            if (name.isEmpty()) {
                query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b inner " +
                        "join patient x on x.id_uuid = b.patient_id  where x.patient_id = p.patient_id and " +
                        "x.facility_id = p.facility_id) as biometric"
                        + " FROM patient p WHERE facility_id = " + facilityId + " ORDER BY surname ASC LIMIT " +
                        start + " , " + numberOfRows;
                if (request.getParameterMap().containsKey("female"))
                    query = "SELECT p.*, (select case when count(*) > 0 " +
                            "then true else false end from biometric b inner join patient x on x.id_uuid = b.patient_id  " +
                            "where x.patient_id = p.patient_id and x.facility_id = p.facility_id) as biometric FROM " +
                            "patient p WHERE facility_id = " + facilityId + " AND gender = 'Female' ORDER BY surname ASC " +
                            "LIMIT " + start + " , " + numberOfRows;
            } else {
                query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b inner " +
                        "join patient x on x.id_uuid = b.patient_id  where x.patient_id = p.patient_id and " +
                        "x.facility_id = p.facility_id) as biometric FROM patient p WHERE facility_id = " +
                        facilityId + " AND UPPER(surname) LIKE '" + name + "%' OR UPPER(other_names) LIKE '" +
                        name + "%' OR UPPER(CONCAT(surname, ' ', other_names)) LIKE '" + name + "%'  OR " +
                        "UPPER(CONCAT(other_names, ' ', surname)) LIKE '" + name + "%'  ORDER BY surname " +
                        "ASC LIMIT " + start + " , " + numberOfRows;

                if (request.getParameterMap().containsKey("female")) query = "SELECT p.*, (select case when count(*) " +
                        "> 0 then true else false end from biometric b inner join patient x on x.id_uuid =" +
                        " b.patient_id  where x.patient_id = p.patient_id and x.facility_id = p.facility_id) as " +
                        "biometric FROM patient p WHERE facility_id = " + facilityId + " AND gender = 'Female' " +
                        "AND UPPER(surname) LIKE '" + name + "%' OR UPPER(other_names) LIKE '" + name + "%' " +
                        "OR UPPER(CONCAT(surname, ' ', other_names)) LIKE '" + name + "%'  OR UPPER(CONCAT(" +
                        "other_names, ' ', surname)) LIKE '" + name + "%' ORDER BY surname ASC LIMIT " + start +
                        " , " + numberOfRows;
                if (request.getParameterMap().containsKey("unsuppressed")) query = "SELECT p.*, (select case when " +
                        "count(*) > 0 then true else false end from biometric b inner join patient x on " +
                        "x.id_uuid = b.patient_id  where x.patient_id = p.patient_id and x.facility_id = " +
                        "p.facility_id) as biometric FROM patient p WHERE facility_id = " + facilityId +
                        " AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND last_viral_load" +
                        " >=1000 AND UPPER(surname) LIKE '" + name + "%' OR UPPER(other_names) LIKE '" + name +
                        "%' OR UPPER(CONCAT(surname, ' ', other_names)) LIKE '" + name + "%'  OR UPPER(CONCAT(" +
                        "other_names, ' ', surname)) LIKE '" + name + "%' ORDER BY surname ASC LIMIT " + start + " ," +
                        " " + numberOfRows;
            }

        } else {
            query = "SELECT p.*,(select case when count(*) > 0 then true else false end from biometric b " +
                    "inner join patient x on x.id_uuid = b.patient_id  where x.patient_id = p.patient_id " +
                    "and x.facility_id = p.facility_id) as biometric FROM patient p WHERE facility_id = " +
                    facilityId + " ORDER BY surname ASC LIMIT " + start + " , " + numberOfRows;
            if (request.getParameterMap().containsKey("female")) query = "SELECT p.*,(select case when " +
                    "count(*) > 0 then true else false end from biometric b inner join patient x on x.id_uuid = " +
                    "b.patient_id  where x.patient_id = p.patient_id and x.facility_id = p.facility_id) as " +
                    "biometric FROM patient p WHERE facility_id = " + facilityId + " AND gender = 'Female' " +
                    "ORDER BY surname ASC LIMIT " + start + " , " + numberOfRows;
            if (request.getParameterMap().containsKey("unsuppressed")) query = "SELECT p.*,(select case when " +
                    "count(*) > 0 then true else false end from biometric b inner join patient x on x.id_uuid = " +
                    "b.patient_id  where x.patient_id = p.patient_id and x.facility_id = p.facility_id) as " +
                    "biometric FROM patient p WHERE facility_id = " + facilityId + " AND current_status IN " +
                    "('ART Start', 'ART Restart', 'ART Transfer In') AND last_viral_load >=1000 ORDER " +
                    "BY surname ASC LIMIT " + start + " , " + numberOfRows;
        }
        LOG.info("Query 1: {}", query);
        jdbcTemplate.query(query, resultSet -> {

            while (resultSet.next()) {
                String patientId = Long.toString(resultSet.getLong("patient_id"));
                String facilityId1 = Long.toString(resultSet.getLong("facility_id"));
                String hospitalNum1 = resultSet.getString("hospital_num");
                String uniqueId = resultSet.getObject("unique_id") == null ? "" : resultSet.getString("unique_id");
                String surname = resultSet.getObject("surname") == null ? "" : resultSet.getString("surname");

                surname = scrambler.unscrambleCharacters(surname);
                surname = StringUtils.upperCase(surname);
                String otherNames = resultSet.getObject("other_names") == null ? "" : resultSet.getString("other_names");
                otherNames = scrambler.unscrambleCharacters(otherNames);
                otherNames = StringUtils.capitalize(otherNames);
                String gender = resultSet.getObject("gender") == null ? "" : resultSet.getString("gender");
                String timeHivDiagnosis = resultSet.getObject("time_hiv_diagnosis") == null ? "" : resultSet.getString("time_hiv_diagnosis");
                String maritalStatus = resultSet.getObject("marital_status") == null ? "" : resultSet.getString("marital_status");
                String dateBirth = resultSet.getObject("date_birth") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_birth"), "MM/dd/yyyy");
                String age = resultSet.getObject("age") == null ? "" : resultSet.getInt("age") == 0 ? "" : Integer.toString(resultSet.getInt("age"));
                String ageUnit = resultSet.getObject("age_unit") == null ? "" : resultSet.getString("age_unit");
                String address = resultSet.getObject("address") == null ? "" : resultSet.getString("address");
                address = scrambler.unscrambleCharacters(address);
                address = StringUtils.capitalize(address);
                String phone = resultSet.getObject("phone") == null ? "" : resultSet.getString("phone");
                phone = scrambler.unscrambleNumbers(phone);
                String education = resultSet.getObject("education") == null ? "" : resultSet.getString("education");
                String occupation = resultSet.getObject("occupation") == null ? "" : resultSet.getString("occupation");
                String state = resultSet.getObject("state") == null ? "" : resultSet.getString("state");
                String lga = resultSet.getObject("lga") == null ? "" : resultSet.getString("lga");
                String nextKin = resultSet.getObject("next_kin") == null ? "" : resultSet.getString("next_kin");
                nextKin = scrambler.unscrambleCharacters(nextKin);
                nextKin = StringUtils.capitalize(nextKin);
                String addressKin = resultSet.getObject("address_kin") == null ? "" : resultSet.getString("address_kin");
                addressKin = scrambler.unscrambleCharacters(addressKin);
                addressKin = StringUtils.capitalize(addressKin);
                String phoneKin = resultSet.getObject("phone_kin") == null ? "" : resultSet.getString("phone_kin");
                phoneKin = scrambler.unscrambleNumbers(phoneKin);
                String relationKin = resultSet.getObject("relation_kin") == null ? "" : resultSet.getString("relation_kin");
                String entryPoint = resultSet.getObject("entry_point") == null ? "" : resultSet.getString("entry_point");
                String dateConfirmedHiv = resultSet.getObject("date_confirmed_hiv") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_confirmed_hiv"), "MM/dd/yyyy");
                String pregnant = resultSet.getObject("pregnant") == null ? "" : Integer.toString(resultSet.getInt("pregnant"));
                String breastfeeding = resultSet.getObject("breastfeeding") == null ? "" : Integer.toString(resultSet.getInt("breastfeeding"));
                String dateRegistration = resultSet.getObject("date_registration") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_registration"), "MM/dd/yyyy");
                String statusRegistration = resultSet.getObject("status_registration") == null ? "" : resultSet.getString("status_registration");
                String enrollmentSetting = resultSet.getObject("enrollment_setting") == null ? "" : resultSet.getString("enrollment_setting");
                String currentStatus = resultSet.getObject("current_status") == null ? "" : resultSet.getString("current_status");
                String sourceReferral = resultSet.getObject("source_referral") == null ? "" : resultSet.getString("source_referral");
                String tbStatus = resultSet.getObject("tb_status") == null ? "" : resultSet.getString("tb_status");
                String dateCurrentStatus = resultSet.getObject("date_current_status") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_current_status"), "MM/dd/yyyy");
                String dateStarted = resultSet.getObject("date_started") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_started"), "MM/dd/yyyy");
                String dateLastClinic = resultSet.getObject("date_last_clinic") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_clinic"), "MM/dd/yyyy");
                String dateLastRefill = resultSet.getObject("date_last_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_refill"), "MM/dd/yyyy");
                String dateNextClinic = resultSet.getObject("date_next_clinic") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_clinic"), "MM/dd/yyyy");
                String dateNextRefill = resultSet.getObject("date_next_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_refill"), "MM/dd/yyyy");
                String dateEnrolledPmtct = resultSet.getObject("date_enrolled_pmtct") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_enrolled_pmtct"), "MM/dd/yyyy");
                String lastViralLoad = "";
                String dateLastViralLoad = resultSet.getObject("date_last_viral_load") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_viral_load"), "MM/dd/yyyy");
                if (!dateLastViralLoad.equalsIgnoreCase("")) {
                    lastViralLoad = resultSet.getObject("last_viral_load") == null ? "" : resultSet.getDouble("last_viral_load") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_viral_load"));
                }
                String lastCd4 = "";
                String lastCd4p = "";
                String dateLastCd4 = resultSet.getObject("date_last_cd4") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_cd4"), "MM/dd/yyyy");
                if (!dateLastCd4.equalsIgnoreCase("")) {
                    lastCd4 = resultSet.getObject("last_cd4") == null ? "" : resultSet.getDouble("last_cd4") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_cd4"));
                    lastCd4p = resultSet.getObject("last_cd4p") == null ? "" : resultSet.getDouble("last_cd4p") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_cd4p"));
                }

                String lastClinicStage = resultSet.getObject("last_clinic_stage") == null ? "" : resultSet.getString("last_clinic_stage");
                String regimentype = resultSet.getObject("regimenType") == null ? "" : resultSet.getString("regimenType");
                String viralLoadType = resultSet.getObject("viral_load_type") == null ? "" : resultSet.getString("viral_load_type");
                String regimen = resultSet.getObject("regimen") == null ? "" : resultSet.getString("regimen");
                String lastRefillSetting = resultSet.getObject("last_refill_setting") == null ? "" : resultSet.getString("last_refill_setting");
                String lastRefillDuration = resultSet.getObject("last_refill_duration") == null ? "" : resultSet.getDouble("last_refill_duration") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_refill_duration"));
                String sendMessage = resultSet.getObject("send_message") == null ? "" : Integer.toString(resultSet.getInt("send_message"));
                String timeStamp = resultSet.getObject("time_stamp") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("time_stamp"), "MM/dd/yyyy");

                String communitypharmId = resultSet.getObject("communitypharm_id") == null ? "" : Integer.toString(resultSet.getInt("communitypharm_id"));
                String casemanagerId = resultSet.getObject("casemanager_id") == null ? "" : Integer.toString(resultSet.getInt("casemanager_id"));

                Boolean biometric = resultSet.getObject("biometric") == null ? false : resultSet.getBoolean("biometric");

                // create an array from object properties
                Map<String, String> map = new HashMap<>();
                map.put("patientId", patientId);
                map.put("facilityId", String.valueOf(facilityId));
                map.put("hospitalNum", hospitalNum);
                map.put("uniqueId", uniqueId);
                map.put("surname", surname);
                map.put("otherNames", otherNames);
                map.put("name", surname + ' ' + otherNames);
                map.put("gender", gender);
                map.put("dateBirth", dateBirth);
                map.put("age", age);
                map.put("ageUnit", ageUnit);
                map.put("maritalStatus", maritalStatus);
                map.put("address", address);
                map.put("phone", phone);
                map.put("education", education);
                map.put("occupation", occupation);
                map.put("state", state);
                map.put("lga", lga);
                map.put("nextKin", nextKin);
                map.put("addressKin", addressKin);
                map.put("phoneKin", phoneKin);
                map.put("relationKin", relationKin);
                map.put("entryPoint", entryPoint);
                map.put("dateConfirmedHiv", dateConfirmedHiv);
                map.put("pregnant", pregnant);
                map.put("breastfeeding", breastfeeding);
                map.put("dateRegistration", dateRegistration);
                map.put("statusRegistration", statusRegistration);
                map.put("enrollmentSetting", enrollmentSetting);
                map.put("currentStatus", currentStatus);
                map.put("dateCurrentStatus", dateCurrentStatus);
                map.put("dateEnrolledPmtct", dateEnrolledPmtct);
                map.put("sourceReferral", sourceReferral);
                map.put("timeHivDiagnosis", timeHivDiagnosis);
                map.put("dateStarted", dateStarted);
                map.put("dateLastClinic", dateLastClinic);
                map.put("dateLastRefill", dateLastRefill);
                map.put("dateNextClinic", dateNextClinic);
                map.put("dateNextRefill", dateNextRefill);
                map.put("dateLastCd4", dateLastCd4);
                map.put("dateLastViralLoad", dateLastViralLoad);
                map.put("lastViralLoad", lastViralLoad);
                map.put("lastCd4", lastCd4);
                map.put("lastCd4p", lastCd4p);
                map.put("lastClinicStage", lastClinicStage);
                map.put("regimentype", regimentype);
                map.put("regimen", regimen);
                map.put("lastRefillSetting", lastRefillSetting);
                map.put("lastRefillDuration", lastRefillDuration);
                map.put("sendMessage", sendMessage);
                map.put("timeStamp", timeStamp);
                map.put("tbStatus", tbStatus);

                map.put("biometric", biometric.toString());
                map.put("communitypharmId", communitypharmId);
                map.put("casemanagerId", casemanagerId);
                //Check if this patient ARV refill has been devolved to a community pharmacy
                map.put("devolve", communitypharmId.isEmpty() ? "0" : "1");

                //Check if this patient ARV refill has been devolved to a community pharmacy
                Patient patient = new Patient();
                patient.setPatientId(resultSet.getLong("patient_id"));
                boolean dueViralLoad = patientRepository.dueViralLoad(patient);
                map.put("dueViralLoad", dueViralLoad ? "1" : "0");
                map.put("sel", "0");
                map.put("viralLoadType", viralLoadType);
                sortedMaps.put(surname + otherNames, map);
            }
            for (Map.Entry<String, Map<String, String>> entry : sortedMaps.entrySet()) {
                patientList.add(entry.getValue());
            }

            return null;
        });

//        page = (Integer) pagerParams.get("page");
//        currpage = (Integer) pagerParams.get("page");
//        totalpages = (Integer) pagerParams.get("totalpages");
//        totalrecords = (Integer) pagerParams.get("totalrecords");
        return "SUCCESS";
    }

    @GetMapping("patientDrugDispenseGrid")
    public String patientDrugDispenseGrid(@RequestParam("id") long facilityId,
                                          @RequestParam("rows") int rows, @RequestParam("page") int page,
                                          @RequestParam("hospitalNum") String hospitalNum) {

        Scrambler scrambler = new Scrambler();
        Map<String, Object> pagerParams = new PaginationUtil().paginateGrid(page
                , rows, "patient");
        int start = (Integer) pagerParams.get("start");
        int numberOfRows =rows;
        if (request.getParameterMap().containsKey("name")) {
            String name = scrambler.scrambleCharacters(request.getParameter("name"));
            if (name == null || name.isEmpty()) {
                query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b " +
                        "inner join patient x on x.id_uuid = b.patient_id  where x.patient_id = p.patient_id " +
                        "and x.facility_id = p.facility_id) as biometric FROM patient p JOIN prescription pr " +
                        "ON p.patient_id = pr.patient_id WHERE p.facility_id = " + facilityId + " " +
                        "AND pr.status = " + Constants.Prescription.PRESCRIBED + " AND prescription_type = 'drug' " +
                        "ORDER BY p.surname LIMIT " + start + " , " + numberOfRows;
            } else {
                query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b " +
                        "inner join patient x on x.id_uuid = b.patient_id  where x.patient_id = p.patient_id " +
                        "and x.facility_id = p.facility_id) as biometric FROM patient p JOIN prescription pr " +
                        "ON p.patient_id = pr.patient_id WHERE p.facility_id = " + facilityId + " AND p.surname " +
                        "LIKE '" + name + "%' OR p.other_names LIKE '" + name + "%' AND pr.status = " +
                        Constants.Prescription.PRESCRIBED + " AND prescription_type = 'drug' ORDER BY p.surname " +
                        "LIMIT " + start + " , " + numberOfRows;
            }
        } else {
            query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b " +
                    "inner join patient x on x.id_uuid = b.patient_id  where x.patient_id = p.patient_id and " +
                    "x.facility_id = p.facility_id) as biometric FROM patient p JOIN prescription pr " +
                    "ON p.patient_id = pr.patient_id WHERE p.facility_id = " + facilityId + " AND pr.status = " +
                    Constants.Prescription.PRESCRIBED + " AND prescription_type = 'drug' " +
                    "ORDER BY p.surname LIMIT " + start + " , " + numberOfRows;
        }
        jdbcTemplate.query(query, resultSet -> {

            while (resultSet.next()) {
                String patientId = Long.toString(resultSet.getLong("patient_id"));
                String facilityId1 = Long.toString(resultSet.getLong("facility_id"));
                String hospitalNum1 = resultSet.getString("hospital_num");
                String uniqueId = resultSet.getObject("unique_id") == null ? "" : resultSet.getString("unique_id");
                String surname = resultSet.getObject("surname") == null ? "" : resultSet.getString("surname");

                surname = scrambler.unscrambleCharacters(surname);
                surname = StringUtils.upperCase(surname);
                String otherNames = resultSet.getObject("other_names") == null ? "" : resultSet.getString("other_names");
                otherNames = scrambler.unscrambleCharacters(otherNames);
                otherNames = StringUtils.capitalize(otherNames);
                String gender = resultSet.getObject("gender") == null ? "" : resultSet.getString("gender");
                String timeHivDiagnosis = resultSet.getObject("time_hiv_diagnosis") == null ? "" : resultSet.getString("time_hiv_diagnosis");
                String maritalStatus = resultSet.getObject("marital_status") == null ? "" : resultSet.getString("marital_status");
                String dateBirth = resultSet.getObject("date_birth") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_birth"), "MM/dd/yyyy");
                String age = resultSet.getObject("age") == null ? "" : resultSet.getInt("age") == 0 ? "" : Integer.toString(resultSet.getInt("age"));
                String ageUnit = resultSet.getObject("age_unit") == null ? "" : resultSet.getString("age_unit");
                String address = resultSet.getObject("address") == null ? "" : resultSet.getString("address");
                address = scrambler.unscrambleCharacters(address);
                address = StringUtils.capitalize(address);
                String phone = resultSet.getObject("phone") == null ? "" : resultSet.getString("phone");
                phone = scrambler.unscrambleNumbers(phone);
                String education = resultSet.getObject("education") == null ? "" : resultSet.getString("education");
                String occupation = resultSet.getObject("occupation") == null ? "" : resultSet.getString("occupation");
                String state = resultSet.getObject("state") == null ? "" : resultSet.getString("state");
                String lga = resultSet.getObject("lga") == null ? "" : resultSet.getString("lga");
                String nextKin = resultSet.getObject("next_kin") == null ? "" : resultSet.getString("next_kin");
                nextKin = scrambler.unscrambleCharacters(nextKin);
                nextKin = StringUtils.capitalize(nextKin);
                String addressKin = resultSet.getObject("address_kin") == null ? "" : resultSet.getString("address_kin");
                addressKin = scrambler.unscrambleCharacters(addressKin);
                addressKin = StringUtils.capitalize(addressKin);
                String phoneKin = resultSet.getObject("phone_kin") == null ? "" : resultSet.getString("phone_kin");
                phoneKin = scrambler.unscrambleNumbers(phoneKin);
                String relationKin = resultSet.getObject("relation_kin") == null ? "" : resultSet.getString("relation_kin");
                String entryPoint = resultSet.getObject("entry_point") == null ? "" : resultSet.getString("entry_point");
                String dateConfirmedHiv = resultSet.getObject("date_confirmed_hiv") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_confirmed_hiv"), "MM/dd/yyyy");
                String pregnant = resultSet.getObject("pregnant") == null ? "" : Integer.toString(resultSet.getInt("pregnant"));
                String breastfeeding = resultSet.getObject("breastfeeding") == null ? "" : Integer.toString(resultSet.getInt("breastfeeding"));
                String dateRegistration = resultSet.getObject("date_registration") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_registration"), "MM/dd/yyyy");
                String statusRegistration = resultSet.getObject("status_registration") == null ? "" : resultSet.getString("status_registration");
                String enrollmentSetting = resultSet.getObject("enrollment_setting") == null ? "" : resultSet.getString("enrollment_setting");
                String currentStatus = resultSet.getObject("current_status") == null ? "" : resultSet.getString("current_status");
                String sourceReferral = resultSet.getObject("source_referral") == null ? "" : resultSet.getString("source_referral");
                String tbStatus = resultSet.getObject("tb_status") == null ? "" : resultSet.getString("tb_status");
                String dateCurrentStatus = resultSet.getObject("date_current_status") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_current_status"), "MM/dd/yyyy");
                String dateStarted = resultSet.getObject("date_started") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_started"), "MM/dd/yyyy");
                String dateLastClinic = resultSet.getObject("date_last_clinic") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_clinic"), "MM/dd/yyyy");
                String dateLastRefill = resultSet.getObject("date_last_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_refill"), "MM/dd/yyyy");
                String dateNextClinic = resultSet.getObject("date_next_clinic") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_clinic"), "MM/dd/yyyy");
                String dateNextRefill = resultSet.getObject("date_next_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_refill"), "MM/dd/yyyy");
                String dateEnrolledPmtct = resultSet.getObject("date_enrolled_pmtct") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_enrolled_pmtct"), "MM/dd/yyyy");
                String lastViralLoad = "";
                String dateLastViralLoad = resultSet.getObject("date_last_viral_load") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_viral_load"), "MM/dd/yyyy");
                if (!dateLastViralLoad.equalsIgnoreCase("")) {
                    lastViralLoad = resultSet.getObject("last_viral_load") == null ? "" : resultSet.getDouble("last_viral_load") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_viral_load"));
                }
                String lastCd4 = "";
                String lastCd4p = "";
                String dateLastCd4 = resultSet.getObject("date_last_cd4") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_cd4"), "MM/dd/yyyy");
                if (!dateLastCd4.equalsIgnoreCase("")) {
                    lastCd4 = resultSet.getObject("last_cd4") == null ? "" : resultSet.getDouble("last_cd4") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_cd4"));
                    lastCd4p = resultSet.getObject("last_cd4p") == null ? "" : resultSet.getDouble("last_cd4p") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_cd4p"));
                }

                String lastClinicStage = resultSet.getObject("last_clinic_stage") == null ? "" : resultSet.getString("last_clinic_stage");
                String regimentype = resultSet.getObject("regimenType") == null ? "" : resultSet.getString("regimenType");
                String viralLoadType = resultSet.getObject("viral_load_type") == null ? "" : resultSet.getString("viral_load_type");
                String regimen = resultSet.getObject("regimen") == null ? "" : resultSet.getString("regimen");
                String lastRefillSetting = resultSet.getObject("last_refill_setting") == null ? "" : resultSet.getString("last_refill_setting");
                String lastRefillDuration = resultSet.getObject("last_refill_duration") == null ? "" : resultSet.getDouble("last_refill_duration") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_refill_duration"));
                String sendMessage = resultSet.getObject("send_message") == null ? "" : Integer.toString(resultSet.getInt("send_message"));
                String timeStamp = resultSet.getObject("time_stamp") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("time_stamp"), "MM/dd/yyyy");

                String communitypharmId = resultSet.getObject("communitypharm_id") == null ? "" : Integer.toString(resultSet.getInt("communitypharm_id"));
                String casemanagerId = resultSet.getObject("casemanager_id") == null ? "" : Integer.toString(resultSet.getInt("casemanager_id"));

                Boolean biometric = resultSet.getObject("biometric") == null ? false : resultSet.getBoolean("biometric");

                // create an array from object properties
                Map<String, String> map = new HashMap<>();
                map.put("patientId", patientId);
                map.put("facilityId", String.valueOf(facilityId));
                map.put("hospitalNum", hospitalNum);
                map.put("uniqueId", uniqueId);
                map.put("surname", surname);
                map.put("otherNames", otherNames);
                map.put("name", surname + ' ' + otherNames);
                map.put("gender", gender);
                map.put("dateBirth", dateBirth);
                map.put("age", age);
                map.put("ageUnit", ageUnit);
                map.put("maritalStatus", maritalStatus);
                map.put("address", address);
                map.put("phone", phone);
                map.put("education", education);
                map.put("occupation", occupation);
                map.put("state", state);
                map.put("lga", lga);
                map.put("nextKin", nextKin);
                map.put("addressKin", addressKin);
                map.put("phoneKin", phoneKin);
                map.put("relationKin", relationKin);
                map.put("entryPoint", entryPoint);
                map.put("dateConfirmedHiv", dateConfirmedHiv);
                map.put("pregnant", pregnant);
                map.put("breastfeeding", breastfeeding);
                map.put("dateRegistration", dateRegistration);
                map.put("statusRegistration", statusRegistration);
                map.put("enrollmentSetting", enrollmentSetting);
                map.put("currentStatus", currentStatus);
                map.put("dateCurrentStatus", dateCurrentStatus);
                map.put("dateEnrolledPmtct", dateEnrolledPmtct);
                map.put("sourceReferral", sourceReferral);
                map.put("timeHivDiagnosis", timeHivDiagnosis);
                map.put("dateStarted", dateStarted);
                map.put("dateLastClinic", dateLastClinic);
                map.put("dateLastRefill", dateLastRefill);
                map.put("dateNextClinic", dateNextClinic);
                map.put("dateNextRefill", dateNextRefill);
                map.put("dateLastCd4", dateLastCd4);
                map.put("dateLastViralLoad", dateLastViralLoad);
                map.put("lastViralLoad", lastViralLoad);
                map.put("lastCd4", lastCd4);
                map.put("lastCd4p", lastCd4p);
                map.put("lastClinicStage", lastClinicStage);
                map.put("regimentype", regimentype);
                map.put("regimen", regimen);
                map.put("lastRefillSetting", lastRefillSetting);
                map.put("lastRefillDuration", lastRefillDuration);
                map.put("sendMessage", sendMessage);
                map.put("timeStamp", timeStamp);
                map.put("tbStatus", tbStatus);

                map.put("biometric", biometric.toString());
                map.put("communitypharmId", communitypharmId);
                map.put("casemanagerId", casemanagerId);
                //Check if this patient ARV refill has been devolved to a community pharmacy
                map.put("devolve", communitypharmId.isEmpty() ? "0" : "1");

                //Check if this patient ARV refill has been devolved to a community pharmacy
                Patient patient = new Patient();
                patient.setPatientId(resultSet.getLong("patient_id"));
                boolean dueViralLoad = patientRepository.dueViralLoad(patient);
                map.put("dueViralLoad", dueViralLoad ? "1" : "0");
                map.put("sel", "0");
                map.put("viralLoadType", viralLoadType);
                sortedMaps.put(surname + otherNames, map);
            }
            for (Map.Entry<String, Map<String, String>> entry : sortedMaps.entrySet()) {
                patientList.add(entry.getValue());
            }

            return null;
        });


//        page = (Integer) pagerParams.get("page");
//        currpage = (Integer) pagerParams.get("page");
//        totalpages = (Integer) pagerParams.get("totalpages");
//        totalrecords = (Integer) pagerParams.get("totalrecords");
        return"SUCCESS";
}

    @GetMapping("patientLabtestDispenseGrid")
    public String patientLabtestDispenseGrid(@RequestParam("id") long facilityId,
                                             @RequestParam("hospitalNum") String hospitalNum,
                                             @RequestParam("row") int rows,
                                             @RequestParam("page") int page) {

        Scrambler scrambler = new Scrambler();
        Map<String, Object> pagerParams = new PaginationUtil().paginateGrid(page, rows, "patient");
        int start = (Integer) pagerParams.get("start");
        int numberOfRows = rows;
        if (request.getParameterMap().containsKey("name")) {
            String name = scrambler.scrambleCharacters(request.getParameter("name"));
            if (name == null || name.isEmpty()) {
                query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b " +
                        "inner join patient x on x.id_uuid = b.patient_id  where x.patient_id = p.patient_id " +
                        "and x.facility_id = p.facility_id) as biometric FROM patient p JOIN prescription pr " +
                        "ON p.patient_id = pr.patient_id WHERE p.facility_id = " + facilityId + " " +
                        "AND pr.status = " + Constants.Prescription.PRESCRIBED + " AND prescription_type = " +
                        "'labtest' ORDER BY p.surname LIMIT " + start + " , " + numberOfRows;
            } else {
                query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b " +
                        "inner join patient x on x.id_uuid = b.patient_id  where x.patient_id = p.patient_id and " +
                        "x.facility_id = p.facility_id) as biometric FROM patient p JOIN prescription pr ON " +
                        "p.patient_id = pr.patient_id WHERE p.facility_id = " + facilityId + " AND p.surname " +
                        "LIKE '" + name + "%' OR p.other_names LIKE '" + name + "%' AND pr.status = " +
                        Constants.Prescription.PRESCRIBED + " AND prescription_type = 'labtest' " +
                        "ORDER BY p.surname LIMIT " + start + " , " + numberOfRows;
            }
        } else {
            query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b " +
                    "inner join patient x on x.id_uuid = b.patient_id  where x.patient_id = p.patient_id and " +
                    "x.facility_id = p.facility_id) as biometric FROM patient p JOIN prescription pr ON " +
                    "p.patient_id = pr.patient_id WHERE p.facility_id = " + facilityId + " AND pr.status = " +
                    Constants.Prescription.PRESCRIBED + " AND prescription_type = 'labtest' " +
                    "ORDER BY p.surname LIMIT " + start + " , " + numberOfRows;
        }
        jdbcTemplate.query(query, resultSet -> {


            while (resultSet.next()) {
                String patientId = Long.toString(resultSet.getLong("patient_id"));
                String facilityId1 = Long.toString(resultSet.getLong("facility_id"));
                String hospitalNum1 = resultSet.getString("hospital_num");
                String uniqueId = resultSet.getObject("unique_id") == null ? "" : resultSet.getString("unique_id");
                String surname = resultSet.getObject("surname") == null ? "" : resultSet.getString("surname");

                surname = scrambler.unscrambleCharacters(surname);
                surname = StringUtils.upperCase(surname);
                String otherNames = resultSet.getObject("other_names") == null ? "" : resultSet.getString("other_names");
                otherNames = scrambler.unscrambleCharacters(otherNames);
                otherNames = StringUtils.capitalize(otherNames);
                String gender = resultSet.getObject("gender") == null ? "" : resultSet.getString("gender");
                String timeHivDiagnosis = resultSet.getObject("time_hiv_diagnosis") == null ? "" : resultSet.getString("time_hiv_diagnosis");
                String maritalStatus = resultSet.getObject("marital_status") == null ? "" : resultSet.getString("marital_status");
                String dateBirth = resultSet.getObject("date_birth") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_birth"), "MM/dd/yyyy");
                String age = resultSet.getObject("age") == null ? "" : resultSet.getInt("age") == 0 ? "" : Integer.toString(resultSet.getInt("age"));
                String ageUnit = resultSet.getObject("age_unit") == null ? "" : resultSet.getString("age_unit");
                String address = resultSet.getObject("address") == null ? "" : resultSet.getString("address");
                address = scrambler.unscrambleCharacters(address);
                address = StringUtils.capitalize(address);
                String phone = resultSet.getObject("phone") == null ? "" : resultSet.getString("phone");
                phone = scrambler.unscrambleNumbers(phone);
                String education = resultSet.getObject("education") == null ? "" : resultSet.getString("education");
                String occupation = resultSet.getObject("occupation") == null ? "" : resultSet.getString("occupation");
                String state = resultSet.getObject("state") == null ? "" : resultSet.getString("state");
                String lga = resultSet.getObject("lga") == null ? "" : resultSet.getString("lga");
                String nextKin = resultSet.getObject("next_kin") == null ? "" : resultSet.getString("next_kin");
                nextKin = scrambler.unscrambleCharacters(nextKin);
                nextKin = StringUtils.capitalize(nextKin);
                String addressKin = resultSet.getObject("address_kin") == null ? "" : resultSet.getString("address_kin");
                addressKin = scrambler.unscrambleCharacters(addressKin);
                addressKin = StringUtils.capitalize(addressKin);
                String phoneKin = resultSet.getObject("phone_kin") == null ? "" : resultSet.getString("phone_kin");
                phoneKin = scrambler.unscrambleNumbers(phoneKin);
                String relationKin = resultSet.getObject("relation_kin") == null ? "" : resultSet.getString("relation_kin");
                String entryPoint = resultSet.getObject("entry_point") == null ? "" : resultSet.getString("entry_point");
                String dateConfirmedHiv = resultSet.getObject("date_confirmed_hiv") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_confirmed_hiv"), "MM/dd/yyyy");
                String pregnant = resultSet.getObject("pregnant") == null ? "" : Integer.toString(resultSet.getInt("pregnant"));
                String breastfeeding = resultSet.getObject("breastfeeding") == null ? "" : Integer.toString(resultSet.getInt("breastfeeding"));
                String dateRegistration = resultSet.getObject("date_registration") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_registration"), "MM/dd/yyyy");
                String statusRegistration = resultSet.getObject("status_registration") == null ? "" : resultSet.getString("status_registration");
                String enrollmentSetting = resultSet.getObject("enrollment_setting") == null ? "" : resultSet.getString("enrollment_setting");
                String currentStatus = resultSet.getObject("current_status") == null ? "" : resultSet.getString("current_status");
                String sourceReferral = resultSet.getObject("source_referral") == null ? "" : resultSet.getString("source_referral");
                String tbStatus = resultSet.getObject("tb_status") == null ? "" : resultSet.getString("tb_status");
                String dateCurrentStatus = resultSet.getObject("date_current_status") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_current_status"), "MM/dd/yyyy");
                String dateStarted = resultSet.getObject("date_started") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_started"), "MM/dd/yyyy");
                String dateLastClinic = resultSet.getObject("date_last_clinic") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_clinic"), "MM/dd/yyyy");
                String dateLastRefill = resultSet.getObject("date_last_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_refill"), "MM/dd/yyyy");
                String dateNextClinic = resultSet.getObject("date_next_clinic") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_clinic"), "MM/dd/yyyy");
                String dateNextRefill = resultSet.getObject("date_next_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_refill"), "MM/dd/yyyy");
                String dateEnrolledPmtct = resultSet.getObject("date_enrolled_pmtct") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_enrolled_pmtct"), "MM/dd/yyyy");
                String lastViralLoad = "";
                String dateLastViralLoad = resultSet.getObject("date_last_viral_load") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_viral_load"), "MM/dd/yyyy");
                if (!dateLastViralLoad.equalsIgnoreCase("")) {
                    lastViralLoad = resultSet.getObject("last_viral_load") == null ? "" : resultSet.getDouble("last_viral_load") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_viral_load"));
                }
                String lastCd4 = "";
                String lastCd4p = "";
                String dateLastCd4 = resultSet.getObject("date_last_cd4") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_cd4"), "MM/dd/yyyy");
                if (!dateLastCd4.equalsIgnoreCase("")) {
                    lastCd4 = resultSet.getObject("last_cd4") == null ? "" : resultSet.getDouble("last_cd4") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_cd4"));
                    lastCd4p = resultSet.getObject("last_cd4p") == null ? "" : resultSet.getDouble("last_cd4p") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_cd4p"));
                }

                String lastClinicStage = resultSet.getObject("last_clinic_stage") == null ? "" : resultSet.getString("last_clinic_stage");
                String regimentype = resultSet.getObject("regimenType") == null ? "" : resultSet.getString("regimenType");
                String viralLoadType = resultSet.getObject("viral_load_type") == null ? "" : resultSet.getString("viral_load_type");
                String regimen = resultSet.getObject("regimen") == null ? "" : resultSet.getString("regimen");
                String lastRefillSetting = resultSet.getObject("last_refill_setting") == null ? "" : resultSet.getString("last_refill_setting");
                String lastRefillDuration = resultSet.getObject("last_refill_duration") == null ? "" : resultSet.getDouble("last_refill_duration") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_refill_duration"));
                String sendMessage = resultSet.getObject("send_message") == null ? "" : Integer.toString(resultSet.getInt("send_message"));
                String timeStamp = resultSet.getObject("time_stamp") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("time_stamp"), "MM/dd/yyyy");

                String communitypharmId = resultSet.getObject("communitypharm_id") == null ? "" : Integer.toString(resultSet.getInt("communitypharm_id"));
                String casemanagerId = resultSet.getObject("casemanager_id") == null ? "" : Integer.toString(resultSet.getInt("casemanager_id"));

                Boolean biometric = resultSet.getObject("biometric") == null ? false : resultSet.getBoolean("biometric");

                // create an array from object properties
                Map<String, String> map = new HashMap<>();
                map.put("patientId", patientId);
                map.put("facilityId", String.valueOf(facilityId));
                map.put("hospitalNum", hospitalNum);
                map.put("uniqueId", uniqueId);
                map.put("surname", surname);
                map.put("otherNames", otherNames);
                map.put("name", surname + ' ' + otherNames);
                map.put("gender", gender);
                map.put("dateBirth", dateBirth);
                map.put("age", age);
                map.put("ageUnit", ageUnit);
                map.put("maritalStatus", maritalStatus);
                map.put("address", address);
                map.put("phone", phone);
                map.put("education", education);
                map.put("occupation", occupation);
                map.put("state", state);
                map.put("lga", lga);
                map.put("nextKin", nextKin);
                map.put("addressKin", addressKin);
                map.put("phoneKin", phoneKin);
                map.put("relationKin", relationKin);
                map.put("entryPoint", entryPoint);
                map.put("dateConfirmedHiv", dateConfirmedHiv);
                map.put("pregnant", pregnant);
                map.put("breastfeeding", breastfeeding);
                map.put("dateRegistration", dateRegistration);
                map.put("statusRegistration", statusRegistration);
                map.put("enrollmentSetting", enrollmentSetting);
                map.put("currentStatus", currentStatus);
                map.put("dateCurrentStatus", dateCurrentStatus);
                map.put("dateEnrolledPmtct", dateEnrolledPmtct);
                map.put("sourceReferral", sourceReferral);
                map.put("timeHivDiagnosis", timeHivDiagnosis);
                map.put("dateStarted", dateStarted);
                map.put("dateLastClinic", dateLastClinic);
                map.put("dateLastRefill", dateLastRefill);
                map.put("dateNextClinic", dateNextClinic);
                map.put("dateNextRefill", dateNextRefill);
                map.put("dateLastCd4", dateLastCd4);
                map.put("dateLastViralLoad", dateLastViralLoad);
                map.put("lastViralLoad", lastViralLoad);
                map.put("lastCd4", lastCd4);
                map.put("lastCd4p", lastCd4p);
                map.put("lastClinicStage", lastClinicStage);
                map.put("regimentype", regimentype);
                map.put("regimen", regimen);
                map.put("lastRefillSetting", lastRefillSetting);
                map.put("lastRefillDuration", lastRefillDuration);
                map.put("sendMessage", sendMessage);
                map.put("timeStamp", timeStamp);
                map.put("tbStatus", tbStatus);

                map.put("biometric", biometric.toString());
                map.put("communitypharmId", communitypharmId);
                map.put("casemanagerId", casemanagerId);
                //Check if this patient ARV refill has been devolved to a community pharmacy
                map.put("devolve", communitypharmId.isEmpty() ? "0" : "1");

                //Check if this patient ARV refill has been devolved to a community pharmacy
                Patient patient = new Patient();
                patient.setPatientId(resultSet.getLong("patient_id"));
                boolean dueViralLoad = patientRepository.dueViralLoad(patient);
                map.put("dueViralLoad", dueViralLoad ? "1" : "0");
                map.put("sel", "0");
                map.put("viralLoadType", viralLoadType);
                sortedMaps.put(surname + otherNames, map);
            }
            for (Map.Entry<String, Map<String, String>> entry : sortedMaps.entrySet()) {
                patientList.add(entry.getValue());
            }

            return null;
        });

//        page = (Integer) pagerParams.get("page");
//        currpage = (Integer) pagerParams.get("page");
//        totalpages = (Integer) pagerParams.get("totalpages");
//        totalrecords = (Integer) pagerParams.get("totalrecords");
        return "SUCCESS";
    }

    @GetMapping("/patientGridSearch")
    public String patientGridSearch(@RequestParam("hospitalNum") String hospitalNum,
                                    @RequestParam("id") long facilityId, @RequestParam("rows") int rows, @RequestParam("page") int page) {

        Scrambler scrambler = new Scrambler();

        Map<String, Object> pagerParams = new PaginationUtil().paginateGrid(page, rows, "patient");
        int start = (Integer) pagerParams.get("start");
        int numberOfRows = rows;
        if (request.getParameterMap().containsKey("name")) {
            String name = scrambler.scrambleCharacters(request.getParameter("name"));
            if (name == null || name.isEmpty()) {
                query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b " +
                        "inner join patient x on x.id_uuid = b.patient_id  where x.patient_id = p.patient_id " +
                        "and x.facility_id = p.facility_id) as biometric FROM patient p WHERE facility_id = " +
                        facilityId + " ORDER BY time_stamp DESC LIMIT " + start + " , " + numberOfRows;
                if (request.getParameterMap().containsKey("female"))
                    query = "SELECT p.*, (select case when count(*) > 0 then true else false end from " +
                            "biometric b inner join patient x on x.id_uuid = b.patient_id  where x.patient_id = " +
                            "p.patient_id and x.facility_id = p.facility_id) as biometric FROM patient p " +
                            "WHERE facility_id = " + facilityId + " AND gender = 'Female' ORDER BY " +
                            "time_stamp DESC LIMIT " + start + " , " + numberOfRows;
            } else {
                query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b " +
                        "inner join patient x on x.id_uuid = b.patient_id  where x.patient_id = p.patient_id " +
                        "and x.facility_id = p.facility_id) as biometric FROM patient p WHERE facility_id = " +
                        facilityId + " AND surname LIKE '" + name + "%' OR other_names LIKE '" + name +
                        "%' ORDER BY current_status LIMIT " + start + " , " + numberOfRows;
                if (request.getParameterMap().containsKey("female"))
                    query = "SELECT p.*, (select case when count(*) > 0 then true else false end from " +
                            "biometric b inner join patient x on x.id_uuid = b.patient_id  where x.patient_id = " +
                            "p.patient_id and x.facility_id = p.facility_id) as biometric FROM patient p " +
                            "WHERE facility_id = " + facilityId + " AND gender = 'Female' AND surname " +
                            "LIKE '" + name + "%' OR other_names LIKE '" + name + "%' ORDER BY " +
                            "current_status LIMIT " + start + " , " + numberOfRows;
            }

        } else {
            query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b " +
                    "inner join patient x on x.id_uuid = b.patient_id  where x.patient_id = p.patient_id " +
                    "and x.facility_id = p.facility_id) as biometric FROM patient p WHERE facility_id = " +
                    facilityId + " ORDER BY time_stamp DESC LIMIT " + start + " , " + numberOfRows;
            if (request.getParameterMap().containsKey("female"))
                query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b " +
                        "inner join patient x on x.id_uuid = b.patient_id  where x.patient_id = p.patient_id " +
                        "and x.facility_id = p.facility_id) as biometric FROM patient p WHERE facility_id = " +
                        facilityId + " AND gender = 'Female' ORDER BY time_stamp DESC LIMIT " + start + " , " +
                        numberOfRows;
        }
        LOG.info("Query: {}", query);
        jdbcTemplate.query(query, resultSet -> {


            while (resultSet.next()) {
                String patientId = Long.toString(resultSet.getLong("patient_id"));
                String facilityId1 = Long.toString(resultSet.getLong("facility_id"));
                String hospitalNum1 = resultSet.getString("hospital_num");
                String uniqueId = resultSet.getObject("unique_id") == null ? "" : resultSet.getString("unique_id");
                String surname = resultSet.getObject("surname") == null ? "" : resultSet.getString("surname");

                surname = scrambler.unscrambleCharacters(surname);
                surname = StringUtils.upperCase(surname);
                String otherNames = resultSet.getObject("other_names") == null ? "" : resultSet.getString("other_names");
                otherNames = scrambler.unscrambleCharacters(otherNames);
                otherNames = StringUtils.capitalize(otherNames);
                String gender = resultSet.getObject("gender") == null ? "" : resultSet.getString("gender");
                String timeHivDiagnosis = resultSet.getObject("time_hiv_diagnosis") == null ? "" : resultSet.getString("time_hiv_diagnosis");
                String maritalStatus = resultSet.getObject("marital_status") == null ? "" : resultSet.getString("marital_status");
                String dateBirth = resultSet.getObject("date_birth") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_birth"), "MM/dd/yyyy");
                String age = resultSet.getObject("age") == null ? "" : resultSet.getInt("age") == 0 ? "" : Integer.toString(resultSet.getInt("age"));
                String ageUnit = resultSet.getObject("age_unit") == null ? "" : resultSet.getString("age_unit");
                String address = resultSet.getObject("address") == null ? "" : resultSet.getString("address");
                address = scrambler.unscrambleCharacters(address);
                address = StringUtils.capitalize(address);
                String phone = resultSet.getObject("phone") == null ? "" : resultSet.getString("phone");
                phone = scrambler.unscrambleNumbers(phone);
                String education = resultSet.getObject("education") == null ? "" : resultSet.getString("education");
                String occupation = resultSet.getObject("occupation") == null ? "" : resultSet.getString("occupation");
                String state = resultSet.getObject("state") == null ? "" : resultSet.getString("state");
                String lga = resultSet.getObject("lga") == null ? "" : resultSet.getString("lga");
                String nextKin = resultSet.getObject("next_kin") == null ? "" : resultSet.getString("next_kin");
                nextKin = scrambler.unscrambleCharacters(nextKin);
                nextKin = StringUtils.capitalize(nextKin);
                String addressKin = resultSet.getObject("address_kin") == null ? "" : resultSet.getString("address_kin");
                addressKin = scrambler.unscrambleCharacters(addressKin);
                addressKin = StringUtils.capitalize(addressKin);
                String phoneKin = resultSet.getObject("phone_kin") == null ? "" : resultSet.getString("phone_kin");
                phoneKin = scrambler.unscrambleNumbers(phoneKin);
                String relationKin = resultSet.getObject("relation_kin") == null ? "" : resultSet.getString("relation_kin");
                String entryPoint = resultSet.getObject("entry_point") == null ? "" : resultSet.getString("entry_point");
                String dateConfirmedHiv = resultSet.getObject("date_confirmed_hiv") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_confirmed_hiv"), "MM/dd/yyyy");
                String pregnant = resultSet.getObject("pregnant") == null ? "" : Integer.toString(resultSet.getInt("pregnant"));
                String breastfeeding = resultSet.getObject("breastfeeding") == null ? "" : Integer.toString(resultSet.getInt("breastfeeding"));
                String dateRegistration = resultSet.getObject("date_registration") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_registration"), "MM/dd/yyyy");
                String statusRegistration = resultSet.getObject("status_registration") == null ? "" : resultSet.getString("status_registration");
                String enrollmentSetting = resultSet.getObject("enrollment_setting") == null ? "" : resultSet.getString("enrollment_setting");
                String currentStatus = resultSet.getObject("current_status") == null ? "" : resultSet.getString("current_status");
                String sourceReferral = resultSet.getObject("source_referral") == null ? "" : resultSet.getString("source_referral");
                String tbStatus = resultSet.getObject("tb_status") == null ? "" : resultSet.getString("tb_status");
                String dateCurrentStatus = resultSet.getObject("date_current_status") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_current_status"), "MM/dd/yyyy");
                String dateStarted = resultSet.getObject("date_started") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_started"), "MM/dd/yyyy");
                String dateLastClinic = resultSet.getObject("date_last_clinic") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_clinic"), "MM/dd/yyyy");
                String dateLastRefill = resultSet.getObject("date_last_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_refill"), "MM/dd/yyyy");
                String dateNextClinic = resultSet.getObject("date_next_clinic") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_clinic"), "MM/dd/yyyy");
                String dateNextRefill = resultSet.getObject("date_next_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_refill"), "MM/dd/yyyy");
                String dateEnrolledPmtct = resultSet.getObject("date_enrolled_pmtct") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_enrolled_pmtct"), "MM/dd/yyyy");
                String lastViralLoad = "";
                String dateLastViralLoad = resultSet.getObject("date_last_viral_load") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_viral_load"), "MM/dd/yyyy");
                if (!dateLastViralLoad.equalsIgnoreCase("")) {
                    lastViralLoad = resultSet.getObject("last_viral_load") == null ? "" : resultSet.getDouble("last_viral_load") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_viral_load"));
                }
                String lastCd4 = "";
                String lastCd4p = "";
                String dateLastCd4 = resultSet.getObject("date_last_cd4") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_cd4"), "MM/dd/yyyy");
                if (!dateLastCd4.equalsIgnoreCase("")) {
                    lastCd4 = resultSet.getObject("last_cd4") == null ? "" : resultSet.getDouble("last_cd4") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_cd4"));
                    lastCd4p = resultSet.getObject("last_cd4p") == null ? "" : resultSet.getDouble("last_cd4p") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_cd4p"));
                }

                String lastClinicStage = resultSet.getObject("last_clinic_stage") == null ? "" : resultSet.getString("last_clinic_stage");
                String regimentype = resultSet.getObject("regimenType") == null ? "" : resultSet.getString("regimenType");
                String viralLoadType = resultSet.getObject("viral_load_type") == null ? "" : resultSet.getString("viral_load_type");
                String regimen = resultSet.getObject("regimen") == null ? "" : resultSet.getString("regimen");
                String lastRefillSetting = resultSet.getObject("last_refill_setting") == null ? "" : resultSet.getString("last_refill_setting");
                String lastRefillDuration = resultSet.getObject("last_refill_duration") == null ? "" : resultSet.getDouble("last_refill_duration") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_refill_duration"));
                String sendMessage = resultSet.getObject("send_message") == null ? "" : Integer.toString(resultSet.getInt("send_message"));
                String timeStamp = resultSet.getObject("time_stamp") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("time_stamp"), "MM/dd/yyyy");

                String communitypharmId = resultSet.getObject("communitypharm_id") == null ? "" : Integer.toString(resultSet.getInt("communitypharm_id"));
                String casemanagerId = resultSet.getObject("casemanager_id") == null ? "" : Integer.toString(resultSet.getInt("casemanager_id"));

                Boolean biometric = resultSet.getObject("biometric") == null ? false : resultSet.getBoolean("biometric");

                // create an array from object properties
                Map<String, String> map = new HashMap<>();
                map.put("patientId", patientId);
                map.put("facilityId", String.valueOf(facilityId));
                map.put("hospitalNum", hospitalNum);
                map.put("uniqueId", uniqueId);
                map.put("surname", surname);
                map.put("otherNames", otherNames);
                map.put("name", surname + ' ' + otherNames);
                map.put("gender", gender);
                map.put("dateBirth", dateBirth);
                map.put("age", age);
                map.put("ageUnit", ageUnit);
                map.put("maritalStatus", maritalStatus);
                map.put("address", address);
                map.put("phone", phone);
                map.put("education", education);
                map.put("occupation", occupation);
                map.put("state", state);
                map.put("lga", lga);
                map.put("nextKin", nextKin);
                map.put("addressKin", addressKin);
                map.put("phoneKin", phoneKin);
                map.put("relationKin", relationKin);
                map.put("entryPoint", entryPoint);
                map.put("dateConfirmedHiv", dateConfirmedHiv);
                map.put("pregnant", pregnant);
                map.put("breastfeeding", breastfeeding);
                map.put("dateRegistration", dateRegistration);
                map.put("statusRegistration", statusRegistration);
                map.put("enrollmentSetting", enrollmentSetting);
                map.put("currentStatus", currentStatus);
                map.put("dateCurrentStatus", dateCurrentStatus);
                map.put("dateEnrolledPmtct", dateEnrolledPmtct);
                map.put("sourceReferral", sourceReferral);
                map.put("timeHivDiagnosis", timeHivDiagnosis);
                map.put("dateStarted", dateStarted);
                map.put("dateLastClinic", dateLastClinic);
                map.put("dateLastRefill", dateLastRefill);
                map.put("dateNextClinic", dateNextClinic);
                map.put("dateNextRefill", dateNextRefill);
                map.put("dateLastCd4", dateLastCd4);
                map.put("dateLastViralLoad", dateLastViralLoad);
                map.put("lastViralLoad", lastViralLoad);
                map.put("lastCd4", lastCd4);
                map.put("lastCd4p", lastCd4p);
                map.put("lastClinicStage", lastClinicStage);
                map.put("regimentype", regimentype);
                map.put("regimen", regimen);
                map.put("lastRefillSetting", lastRefillSetting);
                map.put("lastRefillDuration", lastRefillDuration);
                map.put("sendMessage", sendMessage);
                map.put("timeStamp", timeStamp);
                map.put("tbStatus", tbStatus);

                map.put("biometric", biometric.toString());
                map.put("communitypharmId", communitypharmId);
                map.put("casemanagerId", casemanagerId);
                //Check if this patient ARV refill has been devolved to a community pharmacy
                map.put("devolve", communitypharmId.isEmpty() ? "0" : "1");

                //Check if this patient ARV refill has been devolved to a community pharmacy
                Patient patient = new Patient();
                patient.setPatientId(resultSet.getLong("patient_id"));
                boolean dueViralLoad = patientRepository.dueViralLoad(patient);
                map.put("dueViralLoad", dueViralLoad ? "1" : "0");
                map.put("sel", "0");
                map.put("viralLoadType", viralLoadType);
                sortedMaps.put(surname + otherNames, map);
            }
            for (Map.Entry<String, Map<String, String>> entry : sortedMaps.entrySet()) {
                patientList.add(entry.getValue());
            }

            return null;
        });

//        page = (Integer) pagerParams.get("page");
//        currpage = (Integer) pagerParams.get("page");
//        totalpages = (Integer) pagerParams.get("totalpages");
//        totalrecords = (Integer) pagerParams.get("totalrecords");
        return "SUCCESS";
    }

    @GetMapping("/patientGridByNumber")
    public void patientGridByNumber(@RequestParam("id") long facilityId, @RequestParam("hospitalNum") String hospitalNum,
                                    @RequestParam("rows") int rows, @RequestParam("page") int page) {

        Map<String, Object> pagerParams = new PaginationUtil().paginateGrid(page, rows, "patient");
        int start = (Integer) pagerParams.get("start");
        int numberOfRows = rows;
        if (hospitalNum == null || hospitalNum.isEmpty()) {
            query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b " +
                    "inner join patient x on x.id_uuid = b.patient_id  where x.patient_id = p.patient_id " +
                    "and x.facility_id = p.facility_id) as biometric FROM patient p WHERE facility_id = " +
                    facilityId + " ORDER BY current_status LIMIT " + start + " , " + numberOfRows;
        } else {
            query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b " +
                    "inner join patient x on x.id_uuid = b.patient_id  where x.patient_id = p.patient_id " +
                    "and x.facility_id = p.facility_id) as biometric FROM patient p WHERE facility_id = " +
                    facilityId + " AND TRIM(LEADING '0' FROM hospital_num) LIKE '" +
                    PatientNumberNormalizer.unpadNumber(hospitalNum) + "%' " +
                    "ORDER BY current_status LIMIT " + start + " , " + numberOfRows;
        }
        jdbcTemplate.query(query, resultSet -> {

            while (resultSet.next()) {
                String patientId = Long.toString(resultSet.getLong("patient_id"));
                String facilityId1 = Long.toString(resultSet.getLong("facility_id"));
                String hospitalNum1 = resultSet.getString("hospital_num");
                String uniqueId = resultSet.getObject("unique_id") == null ? "" : resultSet.getString("unique_id");
                String surname = resultSet.getObject("surname") == null ? "" : resultSet.getString("surname");
                Scrambler scrambler = new Scrambler();
                surname = scrambler.unscrambleCharacters(surname);
                surname = StringUtils.upperCase(surname);
                String otherNames = resultSet.getObject("other_names") == null ? "" : resultSet.getString("other_names");
                otherNames = scrambler.unscrambleCharacters(otherNames);
                otherNames = StringUtils.capitalize(otherNames);
                String gender = resultSet.getObject("gender") == null ? "" : resultSet.getString("gender");
                String timeHivDiagnosis = resultSet.getObject("time_hiv_diagnosis") == null ? "" : resultSet.getString("time_hiv_diagnosis");
                String maritalStatus = resultSet.getObject("marital_status") == null ? "" : resultSet.getString("marital_status");
                String dateBirth = resultSet.getObject("date_birth") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_birth"), "MM/dd/yyyy");
                String age = resultSet.getObject("age") == null ? "" : resultSet.getInt("age") == 0 ? "" : Integer.toString(resultSet.getInt("age"));
                String ageUnit = resultSet.getObject("age_unit") == null ? "" : resultSet.getString("age_unit");
                String address = resultSet.getObject("address") == null ? "" : resultSet.getString("address");
                address = scrambler.unscrambleCharacters(address);
                address = StringUtils.capitalize(address);
                String phone = resultSet.getObject("phone") == null ? "" : resultSet.getString("phone");
                phone = scrambler.unscrambleNumbers(phone);
                String education = resultSet.getObject("education") == null ? "" : resultSet.getString("education");
                String occupation = resultSet.getObject("occupation") == null ? "" : resultSet.getString("occupation");
                String state = resultSet.getObject("state") == null ? "" : resultSet.getString("state");
                String lga = resultSet.getObject("lga") == null ? "" : resultSet.getString("lga");
                String nextKin = resultSet.getObject("next_kin") == null ? "" : resultSet.getString("next_kin");
                nextKin = scrambler.unscrambleCharacters(nextKin);
                nextKin = StringUtils.capitalize(nextKin);
                String addressKin = resultSet.getObject("address_kin") == null ? "" : resultSet.getString("address_kin");
                addressKin = scrambler.unscrambleCharacters(addressKin);
                addressKin = StringUtils.capitalize(addressKin);
                String phoneKin = resultSet.getObject("phone_kin") == null ? "" : resultSet.getString("phone_kin");
                phoneKin = scrambler.unscrambleNumbers(phoneKin);
                String relationKin = resultSet.getObject("relation_kin") == null ? "" : resultSet.getString("relation_kin");
                String entryPoint = resultSet.getObject("entry_point") == null ? "" : resultSet.getString("entry_point");
                String dateConfirmedHiv = resultSet.getObject("date_confirmed_hiv") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_confirmed_hiv"), "MM/dd/yyyy");
                String pregnant = resultSet.getObject("pregnant") == null ? "" : Integer.toString(resultSet.getInt("pregnant"));
                String breastfeeding = resultSet.getObject("breastfeeding") == null ? "" : Integer.toString(resultSet.getInt("breastfeeding"));
                String dateRegistration = resultSet.getObject("date_registration") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_registration"), "MM/dd/yyyy");
                String statusRegistration = resultSet.getObject("status_registration") == null ? "" : resultSet.getString("status_registration");
                String enrollmentSetting = resultSet.getObject("enrollment_setting") == null ? "" : resultSet.getString("enrollment_setting");
                String currentStatus = resultSet.getObject("current_status") == null ? "" : resultSet.getString("current_status");
                String sourceReferral = resultSet.getObject("source_referral") == null ? "" : resultSet.getString("source_referral");
                String tbStatus = resultSet.getObject("tb_status") == null ? "" : resultSet.getString("tb_status");
                String dateCurrentStatus = resultSet.getObject("date_current_status") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_current_status"), "MM/dd/yyyy");
                String dateStarted = resultSet.getObject("date_started") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_started"), "MM/dd/yyyy");
                String dateLastClinic = resultSet.getObject("date_last_clinic") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_clinic"), "MM/dd/yyyy");
                String dateLastRefill = resultSet.getObject("date_last_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_refill"), "MM/dd/yyyy");
                String dateNextClinic = resultSet.getObject("date_next_clinic") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_clinic"), "MM/dd/yyyy");
                String dateNextRefill = resultSet.getObject("date_next_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_refill"), "MM/dd/yyyy");
                String dateEnrolledPmtct = resultSet.getObject("date_enrolled_pmtct") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_enrolled_pmtct"), "MM/dd/yyyy");
                String lastViralLoad = "";
                String dateLastViralLoad = resultSet.getObject("date_last_viral_load") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_viral_load"), "MM/dd/yyyy");
                if (!dateLastViralLoad.equalsIgnoreCase("")) {
                    lastViralLoad = resultSet.getObject("last_viral_load") == null ? "" : resultSet.getDouble("last_viral_load") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_viral_load"));
                }
                String lastCd4 = "";
                String lastCd4p = "";
                String dateLastCd4 = resultSet.getObject("date_last_cd4") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_cd4"), "MM/dd/yyyy");
                if (!dateLastCd4.equalsIgnoreCase("")) {
                    lastCd4 = resultSet.getObject("last_cd4") == null ? "" : resultSet.getDouble("last_cd4") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_cd4"));
                    lastCd4p = resultSet.getObject("last_cd4p") == null ? "" : resultSet.getDouble("last_cd4p") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_cd4p"));
                }

                String lastClinicStage = resultSet.getObject("last_clinic_stage") == null ? "" : resultSet.getString("last_clinic_stage");
                String regimentype = resultSet.getObject("regimenType") == null ? "" : resultSet.getString("regimenType");
                String viralLoadType = resultSet.getObject("viral_load_type") == null ? "" : resultSet.getString("viral_load_type");
                String regimen = resultSet.getObject("regimen") == null ? "" : resultSet.getString("regimen");
                String lastRefillSetting = resultSet.getObject("last_refill_setting") == null ? "" : resultSet.getString("last_refill_setting");
                String lastRefillDuration = resultSet.getObject("last_refill_duration") == null ? "" : resultSet.getDouble("last_refill_duration") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_refill_duration"));
                String sendMessage = resultSet.getObject("send_message") == null ? "" : Integer.toString(resultSet.getInt("send_message"));
                String timeStamp = resultSet.getObject("time_stamp") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("time_stamp"), "MM/dd/yyyy");

                String communitypharmId = resultSet.getObject("communitypharm_id") == null ? "" : Integer.toString(resultSet.getInt("communitypharm_id"));
                String casemanagerId = resultSet.getObject("casemanager_id") == null ? "" : Integer.toString(resultSet.getInt("casemanager_id"));

                Boolean biometric = resultSet.getObject("biometric") == null ? false : resultSet.getBoolean("biometric");

                // create an array from object properties
                Map<String, String> map = new HashMap<>();
                map.put("patientId", patientId);
                map.put("facilityId", String.valueOf(facilityId));
                map.put("hospitalNum", hospitalNum);
                map.put("uniqueId", uniqueId);
                map.put("surname", surname);
                map.put("otherNames", otherNames);
                map.put("name", surname + ' ' + otherNames);
                map.put("gender", gender);
                map.put("dateBirth", dateBirth);
                map.put("age", age);
                map.put("ageUnit", ageUnit);
                map.put("maritalStatus", maritalStatus);
                map.put("address", address);
                map.put("phone", phone);
                map.put("education", education);
                map.put("occupation", occupation);
                map.put("state", state);
                map.put("lga", lga);
                map.put("nextKin", nextKin);
                map.put("addressKin", addressKin);
                map.put("phoneKin", phoneKin);
                map.put("relationKin", relationKin);
                map.put("entryPoint", entryPoint);
                map.put("dateConfirmedHiv", dateConfirmedHiv);
                map.put("pregnant", pregnant);
                map.put("breastfeeding", breastfeeding);
                map.put("dateRegistration", dateRegistration);
                map.put("statusRegistration", statusRegistration);
                map.put("enrollmentSetting", enrollmentSetting);
                map.put("currentStatus", currentStatus);
                map.put("dateCurrentStatus", dateCurrentStatus);
                map.put("dateEnrolledPmtct", dateEnrolledPmtct);
                map.put("sourceReferral", sourceReferral);
                map.put("timeHivDiagnosis", timeHivDiagnosis);
                map.put("dateStarted", dateStarted);
                map.put("dateLastClinic", dateLastClinic);
                map.put("dateLastRefill", dateLastRefill);
                map.put("dateNextClinic", dateNextClinic);
                map.put("dateNextRefill", dateNextRefill);
                map.put("dateLastCd4", dateLastCd4);
                map.put("dateLastViralLoad", dateLastViralLoad);
                map.put("lastViralLoad", lastViralLoad);
                map.put("lastCd4", lastCd4);
                map.put("lastCd4p", lastCd4p);
                map.put("lastClinicStage", lastClinicStage);
                map.put("regimentype", regimentype);
                map.put("regimen", regimen);
                map.put("lastRefillSetting", lastRefillSetting);
                map.put("lastRefillDuration", lastRefillDuration);
                map.put("sendMessage", sendMessage);
                map.put("timeStamp", timeStamp);
                map.put("tbStatus", tbStatus);

                map.put("biometric", biometric.toString());
                map.put("communitypharmId", communitypharmId);
                map.put("casemanagerId", casemanagerId);
                //Check if this patient ARV refill has been devolved to a community pharmacy
                map.put("devolve", communitypharmId.isEmpty() ? "0" : "1");

                //Check if this patient ARV refill has been devolved to a community pharmacy
                Patient patient = new Patient();
                patient.setPatientId(resultSet.getLong("patient_id"));
                boolean dueViralLoad = patientRepository.dueViralLoad(patient);
                map.put("dueViralLoad", dueViralLoad ? "1" : "0");
                map.put("sel", "0");
                map.put("viralLoadType", viralLoadType);
                sortedMaps.put(surname + otherNames, map);
            }
            for (Map.Entry<String, Map<String, String>> entry : sortedMaps.entrySet()) {
                patientList.add(entry.getValue());
            }

            return null;
        });
//        page = (Integer) pagerParams.get("page");
//        currpage = (Integer) pagerParams.get("page");
//        totalpages = (Integer) pagerParams.get("totalpages");
//        totalrecords = (Integer) pagerParams.get("totalrecords");
        session.setAttribute("patientList", patientList);


    }
}

//                    query = "SELECT * FROM patient WHERE facility_id = " + id + " AND surname LIKE '"+name+"%' UNION ALL SELECT * FROM patient WHERE facility_id = " + id + " AND UPPER(other_names) LIKE '"+name+"%' ORDER BY current_status LIMIT " + start + " , " + numberOfRows;
