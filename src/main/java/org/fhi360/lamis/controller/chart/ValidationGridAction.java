/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.utility.Scrambler;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/chart/validationGridAction")
@Api(tags = "ValidationGridAction Chart", description = " ")
public class ValidationGridAction {
    private int year, month;
    private final JdbcTemplate jdbcTemplate;
    long facilityId;
    private ArrayList<Map<String, String>> validationList = new ArrayList<Map<String, String>>();
    private ArrayList<Map<String, String>> entityList = new ArrayList<Map<String, String>>();
    private int entity_id;

    public ValidationGridAction(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    String query = "";

    @GetMapping("/validationGrid")
    public void validationGrid(HttpSession session) {
        facilityId = (Long) session.getAttribute("id");
        facilityId = (Long) session.getAttribute("id");
        String general_temp_drop = "DROP VIEW general_temp IF EXISTS";
        jdbcTemplate.execute(general_temp_drop);


        String general_temp = " CREATE VIEW IF NOT EXISTS general_temp (id INT, entity TEXT, entity_count INTEGER)";
        jdbcTemplate.execute(general_temp);


        //ENROLMENTS
        String query_temp_reg = "INSERT INTO general_temp(id, entity, entity_count) "
                + "SELECT 1, 'Number of enrolment records this month', COUNT(DISTINCT patient.patient_id) AS reg_count FROM patient WHERE YEAR(patient.time_stamp) = '" + year + "' AND MONTH(patient.time_stamp) = '" + month + "' AND patient.facility_id = '" + facilityId + "'";

        jdbcTemplate.execute(query_temp_reg);

        //CLINIC
        String query_temp_clinic = "INSERT INTO general_temp(id, entity, entity_count) "
                + "SELECT 2, 'Number of clinic Records this month', COUNT(DISTINCT clinic.patient_id) AS reg_count FROM clinic WHERE YEAR(clinic.time_stamp) = '" + year + "' AND MONTH(clinic.time_stamp) = '" + month + "' AND clinic.facility_id = '" + facilityId + "'";

        jdbcTemplate.execute(query_temp_clinic);
        //PHARMACY
        String query_temp_pharmacy = "INSERT INTO general_temp(id, entity, entity_count) "
                + "SELECT 3, 'Number of pharmacy records this month', COUNT(DISTINCT CONCAT(pharmacy.date_visit, " +
                "patient.patient_id)) AS reg_count FROM pharmacy JOIN patient ON pharmacy.patient_id =" +
                " patient.patient_id WHERE YEAR(pharmacy.time_stamp) = '" + year + "' AND " +
                "MONTH(pharmacy.time_stamp) = '" + month + "' AND pharmacy.facility_id = '" + facilityId + "'";

        jdbcTemplate.execute(query_temp_pharmacy);

        //LABORATORY
        String query_temp_laboratory = "INSERT INTO general_temp(id, entity, entity_count) "
                + "SELECT 4, 'Number of laboratory records this month', COUNT(DISTINCT CONCAT(laboratory.date_reported, patient.patient_id)) AS reg_count FROM laboratory JOIN patient ON laboratory.patient_id = patient.patient_id WHERE YEAR(laboratory.time_stamp) = '" + year + "' AND MONTH(laboratory.time_stamp) = '" + month + "' AND laboratory.facility_id = '" + facilityId + "'";


        jdbcTemplate.execute(query_temp_laboratory);


        //The global table...
        query = "SELECT * from general_temp";

        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(query);
        while (resultSet.next()) {
            String id = Integer.toString(resultSet.getInt("stateId"));
            String entity = resultSet.getString("entity");
            String entity_count = Long.toString(resultSet.getLong("entity_count"));
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", id);
            map.put("entity", entity);
            map.put("entityCount", entity_count);
            validationList.add(map);
        }

        session.setAttribute("validationList", validationList);

    }

    @GetMapping("/entityGrid")
    public void entityGrid(HttpSession session, @Param("entity") String entity) {

        //Get the entity selected and sample from it.
        String entity_year = (String) session.getAttribute("year");
        String entity_month = (String) session.getAttribute("month");
        entity_id = Integer.parseInt(entity);

        if (session.getAttribute("entity_id") != null) {
            if ((int) session.getAttribute("entity_id") != entity_id)
                session.setAttribute("entity_id", entity_id);
        } else {
            session.setAttribute("entity_id", entity_id);
        }

        //System.out.println("Entity ID in session is: "+session.getAttribute("entity_id"));
        try {
            //Get the Enrolment Uploads...
            if (entity_id == 1) {
                query = "SELECT patient_id, surname, other_names, hospital_num, date_current_status AS report_date, current_status, fullname FROM patient JOIN user ON user.user_id = patient.user_id WHERE YEAR(patient.time_stamp) = '" + entity_year + "' AND MONTH(patient.time_stamp) = '" + entity_month + "' AND patient.facility_id = '" + facilityId + "'";

            }
            //Get the Clinic Uploads...
            else if (entity_id == 2) {
                query = "SELECT patient.surname AS surname, patient.patient_id AS patient_id, patient.other_names AS other_names, patient.hospital_num AS hospital_num, clinic.date_visit AS report_date, patient.current_status AS current_status, fullname FROM patient JOIN clinic ON patient.patient_id = clinic.patient_id JOIN user ON user.user_id = clinic.user_id WHERE YEAR(clinic.time_stamp) = '" + entity_year + "' AND MONTH(clinic.time_stamp) = '" + entity_month + "' AND clinic.facility_id = '" + facilityId + "'";

            }
            //Get the Pharmacy Uploads...
            else if (entity_id == 3) {
                query = "SELECT DISTINCT pharmacy.date_visit, patient.patient_id AS patient_id, patient.surname AS surname, patient.other_names AS other_names, patient.hospital_num AS hospital_num, pharmacy.date_visit AS report_date, patient.current_status AS current_status, fullname FROM patient JOIN pharmacy ON patient.patient_id = pharmacy.patient_id JOIN user ON user.user_id = pharmacy.user_id WHERE YEAR(pharmacy.time_stamp) = '" + entity_year + "' AND MONTH(pharmacy.time_stamp) = '" + entity_month + "' AND pharmacy.facility_id = '" + facilityId + "'";

            }
            //Get the Laboratory Uploads...
            else if (entity_id == 4) {
                query = "SELECT DISTINCT laboratory.date_reported, patient.patient_id AS patient_id, patient.surname AS surname, patient.other_names AS other_names, patient.hospital_num AS hospital_num, laboratory.date_reported AS report_date, patient.current_status AS current_status, fullname FROM patient JOIN laboratory ON patient.patient_id = laboratory.patient_id JOIN user ON user.user_id = laboratory.user_id WHERE YEAR(laboratory.time_stamp) = '" + entity_year + "' AND MONTH(laboratory.time_stamp) = '" + entity_month + "' AND laboratory.facility_id = '" + facilityId + "'";

            }
            SqlRowSet resultSet = jdbcTemplate.queryForRowSet(query);
            int i = 1;
            while (resultSet.next()) {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String dateRegistration = (resultSet.getDate("report_date") == null) ? "" : dateFormat.format(resultSet.getDate("report_date"));
                String statusRegistration = (resultSet.getString("current_status") == null) ? "" : resultSet.getString("current_status");
                String surname = (resultSet.getString("surname") == null) ? "" : resultSet.getString("surname");
                String otherNames = (resultSet.getString("other_names") == null) ? "" : resultSet.getString("other_names");
                String hospitalNum = (resultSet.getString("hospital_num") == null) ? "" : resultSet.getString("hospital_num");
                String patientId = (resultSet.getString("patient_id") == null) ? "" : resultSet.getString("patient_id");
                String fullName = (resultSet.getString("fullname") == null) ? "" : resultSet.getString("fullname");

                //get the data from the validated table and mark as validated or not...
                DateFormat sDF = new SimpleDateFormat("dd/MM/yyyy");
                String record_id = hospitalNum + "#" + sDF.format(resultSet.getDate("report_date"));
                // System.out.println(id+record_id+id);
                String query = "SELECT patient_id FROM validated WHERE facility_id = '" + facilityId + "' AND patient_id = '" + patientId + "' AND record_id = '" + record_id + "'";
                if (entity_id == 1) query += "AND table_validated = 'patient'";
                else if (entity_id == 2) query += "AND table_validated = 'clinic'";
                else if (entity_id == 3) query += "AND table_validated = 'pharmacy'";
                else if (entity_id == 4) query += "AND table_validated = 'laboratory'";
                SqlRowSet rs = jdbcTemplate.queryForRowSet(query);
                Map<String, String> map = new HashMap<>();
                String name = new Scrambler().unscrambleCharacters(surname).toUpperCase() + " " + new Scrambler().unscrambleCharacters(otherNames).toUpperCase();
                map.put("sn", "" + i);
                map.put("name", name);
                map.put("dateRegistration", dateRegistration);
                map.put("statusRegistration", statusRegistration);
                map.put("hospitalNum", hospitalNum);
                map.put("patientId", patientId);
                map.put("fullName", fullName);
                if (rs.next()) {
                    map.put("validated", "true");
                } else {
                    //System.out.println("false");
                    map.put("validated", "false");
                }
                entityList.add(map);

                i++;
            }
            session.setAttribute("entityList", entityList);
        } catch (Exception exception) {

        }

    }
}
