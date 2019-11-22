/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller.chart;


import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.model.*;
import org.fhi360.lamis.model.dto.PrescriptionDTO;
import org.fhi360.lamis.model.repositories.PrescriptionRepository;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author user10
 */
@RestController
@RequestMapping("/api/prescription")
public class PrescriptionGridAction {
    private final JdbcTemplate jdbcTemplate;
    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionGridAction(JdbcTemplate jdbcTemplate, PrescriptionRepository prescriptionRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.prescriptionRepository = prescriptionRepository;
    }


    @GetMapping("getRegimenForSelectedType")
    public ResponseEntity getRegimenForSelectedType(@RequestParam(required = false, name = "regimentypeId") Long regimentypeId,
                                                    @RequestParam(required = false) String regimenIds) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            if (regimentypeId != null) {
                String query = "SELECT * FROM regimen WHERE regimentype_id = ? ORDER BY description";
                result = jdbcTemplate.queryForList(query, regimentypeId);
            } else if (regimenIds != null) {
                String query = "SELECT * FROM regimen WHERE regimen_id IN (" + regimenIds + ") ORDER BY description";
                result = jdbcTemplate.queryForList(query, regimentypeId);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/saveSelectedPrescriptions")
    public String saveSelectedPrescriptions(@RequestParam("regimenMap")
                                                    String regimen_map,
                                            @RequestParam("id") Long patientId,
                                            @RequestParam("dateVisit") String dateVisit,
                                            @RequestParam("labtestIds") List<String> labtest_ids,
                                            @RequestParam("id") Long userId) {
        Long facilityId = 2L;
        String[] regimen_ids;
        try {
            regimen_ids = StringUtils.trimToEmpty(regimen_map).split(",");
            String prescriptionType;
            List<Prescription> prescriptions = new ArrayList<>();

            //Save the Selected drugs...
            for (int i = 0; i < regimen_ids.length; i += 3) {
                prescriptionType = "drug";
                Prescription p = new Prescription();
                //Check If that prescription exists for the given date and update if yes...
                PrescriptionDTO prescription = null; //getPrescriptionsByDate(id, id, dateVisit, "drug", Integer.parseInt(regimen_ids[i + 1]));
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                p.setPatient(patient);
                p.setFacilityId(facilityId);
                p.setPrescriptionType(prescriptionType);
                RegimenType regimenType = new RegimenType();
                regimenType.setId(Long.parseLong(regimen_ids[i]));
                p.setRegimenType(regimenType);
                Regimen regimen = new Regimen();
                regimen.setId(Long.parseLong(regimen_ids[i + 1]));
                p.setRegimen(regimen);
                p.setTimeStamp(LocalDateTime.now());
                User userId1 = new User();
                userId1.setId(Long.parseLong(regimen_ids[i + 2]));
                p.setUser(userId1);
                p.setDuration(Integer.parseInt(regimen_ids[i + 2]));
                p.setStatus(Constants.Prescription.PRESCRIBED);
                p.setDateVisit(DateUtil.asLocalDate(DateUtil.parseStringToSqlDate(dateVisit, "MM/dd/yyyy")));
                prescriptions.add(p);
            }
            if (labtest_ids != null) {
                for (String labtest_id : labtest_ids) {
                    prescriptionType = "labtest";
                    Prescription p = new Prescription();
                    PrescriptionDTO prescription = null;//getPrescriptionsByDate(id, id, dateVisit, "labtest", Integer.parseInt(labtest_id));
                    Patient patient = new Patient();
                    patient.setPatientId(patientId);
                    p.setPatient(patient);
                    p.setFacilityId(facilityId);
                    p.setPrescriptionType(prescriptionType);
                    LabTest labTest = new LabTest();
                    labTest.setLabtestId(Long.parseLong(labtest_id));
                    p.setLabTest(labTest);
                    p.setTimeStamp(LocalDateTime.now());
                    User userId1 = new User();
                    userId1.setId(userId);
                    p.setDateVisit(DateUtil.asLocalDate(DateUtil.parseStringToSqlDate(dateVisit, "MM/dd/yyyy")));
                    p.setStatus(Constants.Prescription.PRESCRIBED);
                    System.out.println("P in LAB is: " + p);
                    prescriptions.add(p);
                }
            }
            System.out.println(prescriptions);
            prescriptions.forEach(prescriptionRepository::save);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "SUCCESS";
    }

    @GetMapping("/drugs-by-date/{patientId}")
    public ResponseEntity getDrugsByDate(@PathVariable("patientId") Long patientId,
                                         @RequestParam("dateVisit") String dateVisit,
                                         HttpSession session) {
        List<Map<String, Object>> result = new ArrayList<>();
        String prescriptionType = "drug";
        //Long id = (Long) session.getAttribute("id");
        Long facilityId = 2L;
        if (patientId != null) {
            String query = "SELECT * FROM prescription WHERE patient_id = ? " +
                    "AND date_visit = ? AND prescription_type = ?";
            result = jdbcTemplate.queryForList(query, patientId,
                    DateUtil.parseStringToSqlDate(dateVisit, "MM/dd/yyyy"), prescriptionType);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("drugList", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-date")
    public ResponseEntity getPrescriptionsByDate(@RequestParam(required = false, name = "id") Long patientId,
                                                 @RequestParam("dateVisit") String dateVisit,
                                                 @RequestParam("type") String type,
                                                 @RequestParam("idTofind") Integer idTofind,
                                                 HttpSession session) {
        List<Map<String, Object>> result = new ArrayList<>();
        Long facilityId = (Long) session.getAttribute("id");
        if (patientId != null) {
            String query = "SELECT * FROM prescription WHERE facility_id = ? AND patient_id = ? AND date_visit = ? AND prescription_type = ?";
            if (type.equals("drug"))
                query += " AND regimen_id = ?";
            else if (type.equals("labtest"))
                query += " AND labtest_id = ?";
            result = jdbcTemplate.queryForList(query, facilityId, patientId,
                    DateUtil.parseStringToSqlDate(dateVisit, "MM/dd/yyyy"), type, idTofind);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/lab-tests-by-date/{patientId}")
    public ResponseEntity getLabTestsByDate(@PathVariable("patientId") String patientId1,
                                            @RequestParam("dateVisit") String dateVisit) {
        List<Map<String, Object>> result;
        Long patientId = Long.valueOf(patientId1);
        String query = "SELECT * FROM prescription WHERE patient_id = ? AND date_visit = ? AND prescription_type = ?";
        result = jdbcTemplate.queryForList(query, patientId,
                DateUtil.parseStringToSqlDate(dateVisit, "MM/dd/yyyy"), "labtest");
        Map<String, Object> response = new HashMap<>();
        response.put("labtestList", result);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/retrieveDrugPrescriptionByPatientId")
    public String retrieveDrugPrescriptionByPatientId(@RequestParam("id") String patientId,
                                                      HttpSession session
    ) {
        ArrayList<Integer> regimen_ids = new ArrayList<>();
        try {
            Long facilityId = (Long) session.getAttribute("id");
            String prescriptionType = "drug";
            JDBCUtil jdbcUtil = new JDBCUtil();
            String query = "SELECT regimentype_id, regimen_id, duration FROM prescription WHERE patient_id = ? AND facility_id = ? AND prescription_type = ? AND status = ?";
            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
            //    preparedStatement.setLong(1, Long.parseLong(patient_id));
            preparedStatement.setLong(2, facilityId);
            preparedStatement.setString(3, prescriptionType);
            preparedStatement.setInt(4, Constants.Prescription.PRESCRIBED);
            ResultSet resultSet = preparedStatement.executeQuery();

            // loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String regimen_id = Long.toString(resultSet.getInt("regimen_id"));  //Double.toString(double) if it is a double
                String regimenTypeId = Long.toString(resultSet.getInt("regimentype_id"));
                String duration = Long.toString(resultSet.getInt("duration"));
                regimen_ids.add(Integer.parseInt(regimen_id));
                Map<String, String> prescriptionMap = new HashMap<>();
                prescriptionMap.put("regimenTypeId", regimenTypeId);
                prescriptionMap.put("regimenId", regimen_id);
                prescriptionMap.put("duration", duration);
                //   prescriptionList.add(prescriptionMap);
            }
            boolean done = false;// buildPrescriptionDispenser(regimen_ids.toString());
            if (done) return "SUCCESS";
        } catch (Exception exception) {
            //jdbcUtil.disconnectFromDatabase();  //disconnect from database
            exception.printStackTrace();
            return "ERROR";
        }
        return "SUCCESS";
    }
//
//    private boolean buildPrescriptionDispenser(String regimen_ids) {
//
//        boolean done = false;
//        try {
//
//            //obtain a JDBC connect and execute query
//            JDBCUtil jdbcUtil = new JDBCUtil();
//            String query = "SELECT drug.name, drug.strength, drug.morning, drug.afternoon, drug.evening, regimendrug.regimendrug_id, regimendrug.regimen_id, regimendrug.drug_id, regimen.regimentype_id "
//                    + " FROM drug JOIN regimendrug ON regimendrug.drug_id = drug.drug_id JOIN regimen ON regimendrug.regimen_id = regimen.regimen_id WHERE regimendrug.regimen_id IN (" + regimen_ids.replace("[", "").replace("]", "") + ")";
//            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
//            System.out.println(query);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            //loop through resultSet for each row and put into Map
//            while (resultSet.next()) {
//                String regimentype_id = Long.toString(resultSet.getLong("regimentype_id"));
//                String regimendrugId = Long.toString(resultSet.getLong("regimendrug_id"));
//                String regimen_id = Long.toString(resultSet.getLong("regimen_id"));
//                String drugId = Long.toString(resultSet.getLong("drug_id"));
//                String description = resultSet.getString("name") + " " + resultSet.getString("strength");
//                String morning = Double.toString(resultSet.getDouble("morning"));
//                String afternoon = Double.toString(resultSet.getDouble("afternoon"));
//                String evening = Double.toString(resultSet.getDouble("evening"));
//                String duration = "0";
//                String quantity = "0.0";
//
//                Map<String, String> map = new HashMap<>();
//                map.put("regimentypeId", regimentype_id);
//                map.put("regimendrugId", regimendrugId);
//                map.put("id", regimen_id);
//                map.put("drugId", drugId);
//                map.put("description", description);
//                map.put("morning", morning);
//                map.put("afternoon", afternoon);
//                map.put("evening", evening);
//                map.put("duration", duration);
//                map.put("quantity", quantity);
//                dispenserList.add(map);
//                prescribedList.add(map);
//            }
//            session.setAttribute("fromPrescription", "true");
//            session.setAttribute("dispenserList", dispenserList);
//            session.setAttribute("prescribedList", prescribedList);
////            System.out.println("Prescribed List is "+prescribedList);
////            System.out.println("Dispenser List is "+dispenserList);
//            done = true;
//            resultSet = null;
//        } catch (Exception exception) {
//            resultSet = null;
//            jdbcUtil.disconnectFromDatabase();  //disconnect from database
//            exception.printStackTrace();
//            done = false;
//        }
//
//        return done;
//    }
//
//    public String retrieveLabTestPrescriptionByPatientId() {
//        ArrayList<String> labtest_ids = new ArrayList<>();
//        try {
//            id = (Long) session.getAttribute("id");
//            String patient_id = request.getParameter("id");
//            String prescriptionType = "labtest";
//
//            JDBCUtil jdbcUtil = new JDBCUtil();
//            String query = "SELECT labtest_id FROM prescription WHERE patient_id = ? AND facility_id = ? AND prescription_type = ? AND status = ?";
//            //System.out.println(query);
//            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
//            preparedStatement.setLong(1, Long.parseLong(patient_id));
//            preparedStatement.setLong(2, id);
//            preparedStatement.setString(3, prescriptionType);
//            preparedStatement.setInt(4, Constants.Prescription.PRESCRIBED);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            // loop through resultSet for each row and put into Map
//            while (resultSet.next()) {
//                String labTestId = Long.toString(resultSet.getInt("labtest_id"));
//                labtest_ids.add((labTestId));
//                labtestPrescriptionList.add(labTestId);
//            }
//            buildLabTests(labtest_ids.toString());
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//        return "SUCCESS";
//    }
//
//
//    private void buildLabTests(String labtest_ids) {
//        try {
//            // obtain a JDBC connect and execute query
//            jdbcUtil = new JDBCUtil();
//            // fetch the details of selected lab test
//            query = "SELECT labtest_id, description, measureab, measurepc FROM labtest WHERE labtest_id IN (" + labtest_ids.replace("[", "").replace("]", "") + ")";
//            preparedStatement = jdbcUtil.getStatement(query);
//            resultSet = preparedStatement.executeQuery();
//
//            // loop through resultSet for each row and put into Map
//            while (resultSet.next()) {
//                String labtestId = Long.toString(resultSet.getInt("labtest_id"));
//                String description = resultSet.getString("description");
//                String resultab = "";
//                String measureab = resultSet.getString("measureab");
//                String resultpc = "";
//                String measurepc = resultSet.getString("measurepc");
//                String comment = "";
//
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("labtestId", labtestId);
//                map.put("description", description);
//                map.put("resultab", resultab);
//                map.put("measureab", measureab);
//                map.put("resultpc", resultpc);
//                map.put("measurepc", measurepc);
//                map.put("comment", comment);
//                labresultList.add(map);
//                labPrescribedList.add(map);
//            }
//            session.setAttribute("fromLabTest", "true");
//            session.setAttribute("labresultList", labresultList);
//            session.setAttribute("labPrescribedList", labPrescribedList);
//            resultSet = null;
//        } catch (Exception exception) {
//            resultSet = null;
//            jdbcUtil.disconnectFromDatabase();  //disconnect from database
//            exception.printStackTrace();
//        }
//    }


}
