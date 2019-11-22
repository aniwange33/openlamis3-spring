/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.radet;

;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.model.*;
import org.fhi360.lamis.model.repositories.*;
import org.fhi360.lamis.service.ViralLoadMontiorService;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.Scrambler;
import org.springframework.stereotype.Component;

/**
 * @author user1
 */
@Component
public class ClientDataUpdateAction {
    private HttpServletRequest request;
    private HttpSession session;
    private long facilityId;
    private long userId;
    private String query;
    private Patient patient;
    private Clinic clinic;
    private Pharmacy pharmacy;
    private Laboratory laboratory;
    private StatusHistory statushistory;
    private RegimenHistory regimenhistory;
    private long patientId;

    private RegimenType regimentype;
    private String regimen;

    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;

    private final RegimenRepository regimenRepository = ContextProvider.getBean(RegimenRepository.class);
    private final PatientRepository patientRepository = ContextProvider.getBean(PatientRepository.class);
    private final ClinicRepository clinicRepository = ContextProvider.getBean(ClinicRepository.class);
    private final PharmacyRepository pharmacyRepository = ContextProvider.getBean(PharmacyRepository.class);
    private final StatusHistoryRepository statusHistoryRepository = ContextProvider.getBean(StatusHistoryRepository.class);
    private final  RegimenHistoryRepository regimenHistoryRepository = ContextProvider.getBean(RegimenHistoryRepository.class);
    private  final  LaboratoryRepository laboratoryRepository = ContextProvider.getBean(LaboratoryRepository.class);
    private Map<String, String> clientDto = new HashMap<>();
    private String status;

    public ClientDataUpdateAction() {
    }


    public String update() {
        Scrambler scrambler = new Scrambler();

        try {
            //Save patient object
            String uniqueId = clientDto.get("uniqueId");
            String surname = scrambler.scrambleCharacters(clientDto.get("surname"));
            String otherNames = scrambler.scrambleCharacters(clientDto.get("otherNames"));
            int age = clientDto.get("age").trim().equals("") ? 0 : Integer.parseInt(clientDto.get("age"));
            String ageUnit = clientDto.get("ageUnit");
            Date dateRegistration = DateUtil.parseStringToDate(clientDto.get("dateRegistration"), "MM/dd/yyyy");
            Date dateBirth = null;
            if (clientDto.get("dateBirth").isEmpty()) {
                dateBirth = DateUtil.addYearMonthDay(dateRegistration, -age, ageUnit);
            } else {
                dateBirth = DateUtil.parseStringToDate(clientDto.get("dateBirth"), "MM/dd/yyyy");
            }
            String gender = clientDto.get("gender");
            String statusRegistration = clientDto.get("statusRegistration");
            String enrollmentSetting = clientDto.get("enrollmentSetting");
            Date dateStarted = DateUtil.parseStringToDate(clientDto.get("dateStarted"), "MM/dd/yyyy");
            String currentStatus = clientDto.get("currentStatus");
            Date dateCurrentStatus = DateUtil.parseStringToDate(clientDto.get("dateCurrentStatus"), "MM/dd/yyyy");
            String clinicStage = clientDto.get("clinicStage");
            String funcStatus = clientDto.get("funcStatus");
            Double cd4 = clientDto.get("cd4").trim().equals("") ? null : Double.parseDouble(clientDto.get("cd4"));
            Double cd4p = clientDto.get("cd4p").trim().equals("") ? null : Double.parseDouble(clientDto.get("cd4p"));
            Date dateLastRefill = DateUtil.parseStringToDate(clientDto.get("dateLastRefill"), "MM/dd/yyyy");
            String regimentypeStart = clientDto.get("regimentypeStart");
            String regimenStart = clientDto.get("regimenStart");
            /*

        session = request.getSession();
        id = (Long) session.getAttribute("id");
        userId = (Long) session.getAttribute("userId");
        try {
            jdbcUtil = new JDBCUtil();
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
             */
            RegimenType regimenType = new RegimenType();
            regimenType.setId(Long.parseLong(clientDto.get("regimentypeId")));
            regimentype = regimenRepository.findByRegimenType1(regimenType).getRegimenType();
            regimen = regimenRepository.getOne(Long.parseLong(clientDto.get("id"))).getId() + "";
            int duration = clientDto.get("duration").trim().equals("") ? 0 : Integer.parseInt(clientDto.get("duration"));
            Date dateCurrentViralLoad = DateUtil.parseStringToDate(clientDto.get("dateCurrentViralLoad"), "MM/dd/yyyy");
            Double viralLoad = clientDto.get("viralLoad").trim().equals("") ? null : Double.parseDouble(clientDto.get("viralLoad"));
            String viralLoadIndication = clientDto.get("viralLoadIndication");
            //Determine date of next appointment
            Date dateNextRefill = null;
            if (dateLastRefill != null) {
                dateNextRefill = DateUtil.addDay(dateLastRefill, duration);
            }

            Facility facility = new Facility();
            facility.setId(facilityId);
            patientId = Long.parseLong(clientDto.get("id"));
            patient = patientRepository.getOne(patientId);// PatientDAO.find(id);
            patient.setUniqueId(uniqueId);
            patient.setSurname(surname);
            patient.setOtherNames(otherNames);
            patient.setAge(age);
            patient.setAgeUnit(ageUnit);

            patient.setDateBirth(DateUtil.asLocalDate(dateBirth));
            patient.setGender(gender);
            patient.setStatusRegistration(statusRegistration);
            patient.setDateRegistration(DateUtil.asLocalDate(dateRegistration));
            patient.setEnrollmentSetting(enrollmentSetting);
            patient.setCurrentStatus(currentStatus);
            patient.setDateCurrentStatus(DateUtil.asLocalDate(dateCurrentStatus));
            patient.setDateStarted(DateUtil.asLocalDate(dateStarted));
            if (!clientDto.get("dateLastRefill").isEmpty()) {
                patient.setDateLastRefill((DateUtil.asLocalDate(dateLastRefill)));
                patient.setDateNextRefill(DateUtil.asLocalDate(dateNextRefill));
                patient.setRegimentype(regimentype.getDescription());
                patient.setRegimen(regimen);
                patient.setLastRefillDuration(duration);
            }
            if (!clientDto.get("dateCurrentViralLoad").isEmpty()) {
                patient.setDateLastViralLoad(DateUtil.asLocalDate(dateCurrentViralLoad));
                patient.setLastViralLoad(viralLoad);
            }
            User user = new User();
            user.setId(userId);
            patient.setUser(user);
            patient.setTimeStamp(LocalDateTime.now());
            patientRepository.save(patient);

            //Update ART commencement record if exist or save a new clinic record
            if (!clientDto.get("dateStarted").isEmpty()) {
                query = "SELECT clinic_id AS id FROM clinic WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND commence = 1";
                long id = getId(query);
                if (id != 0L) {
                    query = "UPDATE clinic SET date_visit = '" + DateUtil.parseDateToString(dateStarted, "yyyy-MM-dd") + "', cd4 = " + cd4 + ", cd4p = " + cd4p + ", regimenType = '" + regimentypeStart + "', regimen = '" + regimenStart + "', clinic_stage = '" + clinicStage + "', func_status = '" + funcStatus + "' WHERE clinic_id = " + id;
                    executeUpdate(query);
                } else {
                    saveClinic();
                }
            }

            //Save a new pharmacy record if not exist and log in regimen history
            query = "SELECT pharmacy_id AS id FROM pharmacy WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND regimentype_id IN (1, 2, 3, 4, 14) AND date_visit = '" + DateUtil.parseDateToString(dateLastRefill, "yyyy-MM-dd") + "'";
            if (getId(query) == 0L) {
                if (!clientDto.get("dateLastRefill").isEmpty() && !clientDto.get("id").isEmpty() && !clientDto.get("duration").isEmpty()) {
                    savePharmacy();
                    //   DefaulterAttributeUpdater.nullifyTrackingOutcome(id, id, clientDto.get("dateLastRefill"));

                    //Log dispensed ARV in regimenhistory
                    query = "SELECT history_id AS id FROM regimenhistory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND regimenType = '" + regimentype + "' AND regimen = '" + regimen + "'";
                    if (getId(query) == 0L) {
                        saveRegimenhistory();
                    }
                }
            }

            //Save a new viral load record if not exist
            if (!clientDto.get("dateCurrentViralLoad").isEmpty()) {
                query = "SELECT laboratory_id AS id FROM laboratory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND labtest_id = 16 AND date_reported = '" + DateUtil.parseDateToString(dateCurrentViralLoad, "yyyy-MM-dd") + "'";
                long id = getId(query);
                if (id != 0L) {
                    query = "UPDATE laboratory SET resultab = '" + clientDto.get("viralLoad") + "' AND comment = '" + viralLoadIndication + "' WHERE laboratory_id = " + id;
                    executeUpdate(query);
                } else {
                    saveLaboratory();
                }
            }

            if (!clientDto.get("dateCurrentStatus").isEmpty() && !clientDto.get("currentStatus").isEmpty()) {
                query = "SELECT history_id AS id FROM statushistory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_current_status = '" + DateUtil.parseDateToString(dateCurrentStatus, "yyyy-MM-dd") + "'";
                long id = getId(query);
                if (id != 0L) {
                    query = "UPDATE statushistory SET current_status = '" + currentStatus + "' WHERE history_id = " + id;
                    executeUpdate(query);
                } else {
                    saveStatusHistory();
                }
            }
            new ViralLoadMontiorService().updateViralLoadDue(Long.parseLong(clientDto.get("id")), DateUtil.parseDateToString(dateStarted, "yyyy-MM-dd"));
            updateClientList(session);
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();
        }
        return "SUCCESS";
    }

    private void saveClinic() {
        clinic.setPatient(patient);
        clinic.setFacilityId(facilityId);
        Date date = DateUtil.parseStringToDate(clientDto.get("dateStarted"), "MM/dd/yyyy");
        clinic.setDateVisit(DateUtil.asLocalDate(date));
        clinic.setCd4(clientDto.get("cd4").trim().equals("") ? null : Double.parseDouble(clientDto.get("cd4")));
        clinic.setCd4p(clientDto.get("cd4p").trim().equals("") ? null : Double.parseDouble(clientDto.get("cd4p")));
        clinic.setClinicStage(clientDto.get("clinicStage"));
        clinic.setFuncStatus(clientDto.get("funcStatus"));
        clinic.setRegimenType(clientDto.get("regimentypeStart"));
        clinic.setRegimen(clientDto.get("regimenStart"));
        clinic.setCommence(true);
        User user = new User();
        user.setId(userId);
        clinic.setUser(user);
        clinic.setTimeStamp(LocalDateTime.now());
        clinicRepository.save(clinic);
    }

    private void savePharmacy() {
        pharmacy.setPatient(patient);
        pharmacy.setFacilityId(facilityId);
        Date date = DateUtil.parseStringToDate(clientDto.get("dateLastRefill"), "MM/dd/yyyy");
        pharmacy.setDateVisit(DateUtil.asLocalDate(date));
        pharmacy.setDuration(clientDto.get("duration").trim().equals("") ? 0 : Integer.parseInt(clientDto.get("duration")));
        RegimenType regimenType = new RegimenType();
        regimenType.setId(Long.parseLong(clientDto.get("regimentypeId")));
        pharmacy.setRegimenType(regimenType);
        Regimen regimen = new Regimen();
        regimen.setId(Long.parseLong(clientDto.get("id")));
        pharmacy.setRegimen(regimen);
        User user = new User();
        user.setId(userId);
        pharmacy.setUser(user);
        pharmacy.setTimeStamp(LocalDateTime.now());

        query = "SELECT drug.name, drug.strength, drug.morning, drug.afternoon, drug.evening, regimendrug.regimendrug_id, regimendrug.regimen_id, regimendrug.drug_id, regimen.regimentype_id "
                + " FROM drug JOIN regimendrug ON regimendrug.drug_id = drug.drug_id JOIN regimen ON regimendrug.regimen_id = regimen.regimen_id WHERE regimen.regimen_id = " + clientDto.get("id");
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                pharmacy.setMorning(rs.getDouble("morning"));
                pharmacy.setAfternoon(rs.getDouble("afternoon"));
                pharmacy.setEvening(rs.getDouble("evening"));
                pharmacy.setRegimendrugId(rs.getLong("regimendrug_id"));
                pharmacyRepository.save(pharmacy);
            }
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private void saveStatusHistory() {
        statushistory.setPatient(patient);
        statushistory.setFacilityId(facilityId);
        statushistory.setCurrentStatus(clientDto.get("currentStatus"));
        Date date = DateUtil.parseStringToDate(clientDto.get("dateCurrentStatus"), "MM/dd/yyyy");
        statushistory.setDateCurrentStatus(DateUtil.asLocalDate(date));
        statushistory.setTimeStamp(LocalDateTime.now());
        statusHistoryRepository.save(statushistory);
    }

    private void saveRegimenhistory() {
        regimenhistory.setPatient(patient);
        regimenhistory.setFacilityId(facilityId);
        Date date =DateUtil.parseStringToDate(clientDto.get("dateLastRefill"), "MM/dd/yyyy");
        regimenhistory.setDateVisit(DateUtil.asLocalDate(date));
        RegimenType regimenType = new RegimenType();
        regimenhistory.setRegimenType(regimentype.getDescription());
        regimenhistory.setRegimen(regimen);
        regimenhistory.setTimeStamp(LocalDateTime.now());
        regimenHistoryRepository.save(regimenhistory);
    }

    private void saveLaboratory() {
        laboratory.setPatient(patient);
        laboratory.setFacilityId(facilityId);
        laboratory.setLaboratoryId(16L);
        Date date = DateUtil.parseStringToDate(clientDto.get("dateCurrentViralLoad"), "MM/dd/yyyy");
        Date date1 = DateUtil.parseStringToDate(clientDto.get("dateCollected"), "MM/dd/yyyy");
        laboratory.setDateReported(DateUtil.asLocalDate(date));
        laboratory.setDateCollected(DateUtil.asLocalDate(date1));
        laboratory.setResultAB(clientDto.get("viralLoad"));
        laboratory.setComment(clientDto.get("viralLoadIndication"));
        User user = new User();
        user.setId(userId);
        laboratory.setUser(user);
        laboratory.setTimeStamp(LocalDateTime.now());
        laboratoryRepository.save(laboratory);
    }
    RadetService radetService ;
    private void updateClientList(HttpSession session) {
        ArrayList<Map<String, String>> clientList = radetService.retrieveClientList(session);

        for (int i = 0; i < clientList.size(); i++) {
            String id = (String) clientList.get(i).get("id"); // retrieve patient ID from list
            if (id.equals(clientDto.get("id"))) {
                clientList.get(i).remove("uniqueId");
                clientList.get(i).put("uniqueId", clientDto.get("uniqueId"));
                clientList.get(i).remove("surname");
                clientList.get(i).put("surname", clientDto.get("surname"));
                clientList.get(i).remove("otherNames");
                clientList.get(i).put("otherNames", clientDto.get("otherNames"));
                clientList.get(i).remove("name");
                clientList.get(i).put("name", clientDto.get("surname") + " " + clientDto.get("otherNames"));
                clientList.get(i).remove("age");
                clientList.get(i).put("age", clientDto.get("age"));
                clientList.get(i).remove("ageUnit");
                clientList.get(i).put("ageUnit", clientDto.get("ageUnit"));
                clientList.get(i).remove("dateRegistration");
                clientList.get(i).put("dateRegistration", clientDto.get("dateRegistration"));
                clientList.get(i).remove("dateBirth");
                clientList.get(i).put("dateBirth", clientDto.get("dateBirth"));
                clientList.get(i).remove("gender");
                clientList.get(i).put("gender", clientDto.get("gender"));
                clientList.get(i).remove("statusRegistration");
                clientList.get(i).put("statusRegistration", clientDto.get("statusRegistration"));
                clientList.get(i).put("enrollmentSetting", clientDto.get("enrollmentSetting"));
                clientList.get(i).remove("dateStarted");
                clientList.get(i).put("dateStarted", clientDto.get("dateStarted"));
                clientList.get(i).remove("currentStatus");
                clientList.get(i).put("currentStatus", clientDto.get("currentStatus"));
                clientList.get(i).remove("dateCurrentStatus");
                clientList.get(i).put("dateCurrentStatus", clientDto.get("dateCurrentStatus"));
                clientList.get(i).remove("clinicStage");
                clientList.get(i).put("clinicStage", clientDto.get("clinicStage"));
                clientList.get(i).remove("funcStatus");
                clientList.get(i).put("funcStatus", clientDto.get("funcStatus"));
                clientList.get(i).remove("cd4");
                clientList.get(i).put("cd4", clientDto.get("cd4"));
                clientList.get(i).remove("cd4p");
                clientList.get(i).put("cd4p", clientDto.get("cd4p"));
                clientList.get(i).remove("dateLastRefill");
                clientList.get(i).put("dateLastRefill", clientDto.get("dateLastRefill"));
                clientList.get(i).remove("duration");
                clientList.get(i).put("duration", clientDto.get("duration"));
                clientList.get(i).remove("regimenType");
                clientList.get(i).put("regimentype", regimentype.getDescription());
                clientList.get(i).remove("regimen");
                clientList.get(i).put("regimen", regimen);
                clientList.get(i).remove("regimentypeId");
                clientList.get(i).put("regimentypeId", clientDto.get("regimentypeId"));
                clientList.get(i).remove("id");
                clientList.get(i).put("regimenId", clientDto.get("id"));
                clientList.get(i).remove("regimentypeStart");
                clientList.get(i).put("regimentypeStart", clientDto.get("regimentypeStart"));
                clientList.get(i).remove("regimenStart");
                clientList.get(i).put("regimenStart", clientDto.get("regimenStart"));
                clientList.get(i).put("dateCurrentViralLoad", clientDto.get("dateCurrentViralLoad"));
                clientList.get(i).put("dateCollected", clientDto.get("dateCollected"));
                clientList.get(i).put("viralLoad", clientDto.get("viralLoad"));
                clientList.get(i).put("viralLoadIndication", clientDto.get("viralLoadIndication"));
                clientList.get(i).remove("category");
                clientList.get(i).put("category", "5");
            }
        }
        session.setAttribute("clientList", clientList);
    }

    private long getId(String query) {
        long id = 0L;
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) id = rs.getLong("id");
        } catch (Exception exception) {
            status = "error";
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return id;
    }

    private void executeUpdate(String query) {
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            status = "error";
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }


}
