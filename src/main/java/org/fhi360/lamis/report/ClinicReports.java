/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.report;

import org.fhi360.lamis.controller.report.ReportParameterDTO;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;


@Component
public class ClinicReports {

    private String reportTitle;

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private final JdbcTemplate jdbcTemplate;


    public ClinicReports(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> eligibleForART(Long facilityId) {
        reportTitle = "Patients Eligible for ARV Initiation";
        query = "SELECT * FROM (SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname," +
                " patient.other_names, patient.gender, patient.date_birth, DATEDIFF(YEAR, date_birth, CURDATE()) " +
                "AS age, patient.phone, patient.address, patient.current_status, patient.last_cd4, patient.last_cd4p," +
                " patient.last_viral_load, patient.last_clinic_stage FROM patient WHERE patient.facility_id = " +
                facilityId + " AND patient.current_status = 'HIV+ non ART') AS ps WHERE (ps.age >= 5 AND " +
                "(ps.last_clinic_stage IN ('Stage III', 'Stage IV') OR (ps.last_cd4 > 0.0 AND ps.last_cd4 < 350))) " +
                "OR (ps.age >= 2 AND ps.age < 5 AND (ps.last_clinic_stage IN ('Stage III', 'Stage IV') OR " +
                "(ps.last_cd4p > 0.0 AND ps.last_cd4p < 25) OR (ps.last_cd4 > 0.0 AND ps.last_cd4 < 750))) OR " +
                "ps.age < 2";
        return generateReportList(query);
    }

    public List<Map<String, Object>> cd4Due(Long facilityId) {
        reportTitle = "Patients due for CD4 count Test";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, " +
                "date_birth, CURDATE()) AS age, phone, address, current_status, date_last_cd4, last_cd4, last_cd4p " +
                "FROM patient WHERE facility_id = " + facilityId + " AND current_status IN ('HIV+ non ART', " +
                "'ART Start', 'ART Restart', 'ART Transfer In', 'Pre-ART Transfer In') AND DATEDIFF(MONTH, " +
                "date_last_cd4, CURDATE()) > 3";
        return generateReportList(query);
    }

    public List<Map<String, Object>> clientsCd4Due(Long facilityId, String casemanagerId1) {
        int casemanagerId = Integer.parseInt(casemanagerId1);
        JDBCUtil internalJdbcUtil = null;
        String casemanagerName = "";

        String caseManagerQuery = "SELECT fullname from casemanager WHERE casemanager_id = " + casemanagerId;
        try {
            internalJdbcUtil = new JDBCUtil();
            PreparedStatement preparedStatement = internalJdbcUtil.getStatement(caseManagerQuery);
            ResultSet internalResultSet = preparedStatement.executeQuery();

            while (internalResultSet.next()) {
                casemanagerName = internalResultSet.getString("fullname");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            internalJdbcUtil.disconnectFromDatabase();
        }

        reportTitle = casemanagerName + "'s Clients due for CD4 count Test";

        System.out.println("The Report Title is: " + reportTitle);
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, " +
                "date_birth, CURDATE()) AS age, phone, address, current_status, date_last_cd4, last_cd4, last_cd4p " +
                "FROM patient WHERE facility_id = " + facilityId + " AND casemanager_id = " + casemanagerId +
                " AND DATEDIFF(MONTH, date_last_cd4, CURDATE()) > 3";
        return generateReportList(query);
    }

    public List<Map<String, Object>> unassignedClients(long facilityId) {
        reportTitle = "List of clients not assigned to a case manager";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, " +
                "date_birth, CURDATE()) AS age, phone, address, state, lga, date_registration, status_registration, " +
                "current_status, date_current_status, date_started, date_last_cd4, last_cd4, last_cd4p, " +
                "date_last_viral_load, last_viral_load, date_last_refill, last_refill_duration FROM patient WHERE " +
                "facility_id = " + facilityId + " AND (casemanager_id is NULL OR casemanager_id = 0) AND " +
                "current_status NOT IN ('Known Death', 'ART Transfer Out', 'Pre-ART Transfer Out')";
        query += " ORDER BY current_status";
        return generateReportList(query);

    }

    public List<Map<String, Object>> clientsViralLoadDue(Long facilityId, String casemanagerId1) {
        Integer casemanagerId = Integer.parseInt(casemanagerId1);
        JDBCUtil internalJdbcUtil = null;
        String casemanagerName = "";
        String caseManagerQuery = "SELECT fullname from casemanager WHERE casemanager_id = " + casemanagerId;
        try {
            internalJdbcUtil = new JDBCUtil();
            PreparedStatement preparedStatement = internalJdbcUtil.getStatement(caseManagerQuery);
            ResultSet internalResultSet = preparedStatement.executeQuery();

            while (internalResultSet.next()) {
                casemanagerName = internalResultSet.getString("fullname");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            internalJdbcUtil.disconnectFromDatabase();
        }

        reportTitle = casemanagerName + "'s Clients due for Viral Load Test";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, " +
                "date_birth, CURDATE()) AS age, phone, address, date_started, current_status, date_last_cd4, " +
                "last_cd4, last_cd4p, date_last_viral_load, last_viral_load FROM patient WHERE facility_id = " +
                facilityId + " AND casemanager_id = " + casemanagerId + " AND ((DATEDIFF(MONTH, date_last_viral_load, " +
                "CURDATE()) >= 6 AND viral_load_type != '" + Constants.TypeVL.VL_ROUTINE + "') OR " +
                "(date_last_viral_load IS NULL AND DATEDIFF(MONTH, date_started, CURDATE()) >= 6))";
        return generateReportList(query);
    }


    public List<Map<String, Object>> cd4LessBaseline(Long facilityId) {
        reportTitle = "Patients with current CD4 Count <= baseline value";
        query = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, " +
                "patient.gender, patient.date_birth, DATEDIFF(YEAR, patient.date_birth, CURDATE()) AS age, " +
                "patient.current_status, patient.phone, patient.address, patient.date_started, patient.last_cd4, " +
                "patient.last_cd4p, patient.date_last_cd4, clinic.cd4, clinic.cd4p FROM patient JOIN clinic ON " +
                "patient.facility_id = clinic.facility_id AND patient.patient_id = clinic.patient_id WHERE " +
                "patient.facility_id = " + facilityId + " AND clinic.facility_id = " + facilityId +
                " AND patient.current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND " +
                "(patient.last_cd4 != 0 AND patient.last_cd4p != 0) AND (patient.last_cd4 <= clinic.cd4 OR " +
                "patient.last_cd4p <= clinic.cd4p) AND clinic.commence = 1";
        return generateReportList(query);
    }

    public List<Map<String, Object>> viralLoadDue(Long facilityId) {
        reportTitle = "Patients due for Viral Load Test";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, " +
                "date_birth, CURDATE()) AS age, phone, address, date_started, current_status, date_last_cd4, last_cd4, " +
                "last_cd4p, date_last_viral_load, last_viral_load FROM patient WHERE facility_id = " + facilityId +
                " AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND (DATEDIFF(MONTH, " +
                "date_last_viral_load, CURDATE()) >= 6) OR (date_last_viral_load IS NULL AND DATEDIFF(MONTH, " +
                "date_started, CURDATE()) >= 6)";
        return generateReportList(query);
    }


    //Generate the List of Patients dur for Baseline Viral Load Test...
    public List<Map<String, Object>> baselineViralLoadDue(Long facilityId) {
        reportTitle = "Patients due for Baseline Viral Load Test";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, " +
                "date_birth, CURDATE()) AS age, phone, address, date_started, current_status, date_last_cd4, " +
                "last_cd4, last_cd4p, date_last_viral_load, last_viral_load FROM patient WHERE facility_id = " +
                facilityId + " AND viral_load_type = '" + Constants.TypeVL.VL_BASELINE + "' AND viral_load_due_date " +
                "<= CURDATE()";
        return generateReportList(query);
    }

    public List<Map<String, Object>> secondViralLoadDue(Long facilityId) {
        reportTitle = "Patients due for Second Viral Load Test";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, " +
                "date_birth, CURDATE()) AS age, phone, address, date_started, current_status, date_last_cd4, last_cd4," +
                " last_cd4p, date_last_viral_load, last_viral_load FROM patient WHERE facility_id = " +
                facilityId + " AND viral_load_type = '" + Constants.TypeVL.VL_SECOND + "' AND viral_load_due_date" +
                " <= CURDATE()";
        return generateReportList(query);
    }

    public List<Map<String, Object>> routineViralLoadDue(Long facilityId) {
        reportTitle = "Patients due for Routine Viral Load Test";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR," +
                " date_birth, CURDATE()) AS age, phone, address, date_started, current_status, date_last_cd4, " +
                "last_cd4, last_cd4p, date_last_viral_load, last_viral_load FROM patient WHERE facility_id = " +
                facilityId + " AND viral_load_type = '" + Constants.TypeVL.VL_ROUTINE + "' AND viral_load_due_date " +
                "<= CURDATE()";
        return generateReportList(query);
    }

    public List<Map<String, Object>> repeatViralLoadDue(Long facilityId) {
        reportTitle = "Patients due for Repeat Viral Load Test";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, " +
                "date_birth, CURDATE()) AS age, phone, address, date_started, current_status, date_last_cd4, last_cd4," +
                " last_cd4p, date_last_viral_load, last_viral_load FROM patient WHERE facility_id = " + facilityId +
                " AND viral_load_type = '" + Constants.TypeVL.VL_REPEAT + "' AND viral_load_due_date <= CURDATE()";
        return generateReportList(query);
    }

    public List<Map<String, Object>> viralLoadSupressed(Long facilityId) {
        reportTitle = "Patients with current Viral Load less than 1000 copies/ml";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, " +
                "date_birth, CURDATE()) AS age, phone, address, date_started, current_status, date_last_cd4, " +
                "last_cd4, last_cd4p, date_last_viral_load, last_viral_load FROM patient WHERE facility_id = " +
                facilityId + " AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND " +
                "last_viral_load < 1000";
        return generateReportList(query);
    }

    public List<Map<String, Object>> viralLoadUnsupressed(Long facilityId) {
        reportTitle = "Patients with current Viral Load more than or equal 1000 copies/ml";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, " +
                "date_birth, CURDATE()) AS age, phone, address, date_started, current_status, date_last_cd4, last_cd4, " +
                "last_cd4p, date_last_viral_load, last_viral_load FROM patient WHERE facility_id = " + facilityId +
                " AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND last_viral_load >= 1000";
        return generateReportList(query);
    }

    public List<Map<String, Object>> generateReportList(String query) {
        return jdbcTemplate.queryForList(query);
    }

    public Map<String, Object> getReportParameters(ReportParameterDTO dto, Long facilityId) {
        Map<String, Object> parameterMap = new HashMap<>();
        Calendar today = new GregorianCalendar();
        if (dto.getReportingMonth() != null) {
            parameterMap.put("reportingMonth", dto.getReportingMonth());
        } else {
            parameterMap.put("reportingMonth", DateUtil.getMonth(today.get(Calendar.MONTH) + 1));
        }
        if (dto.getReportingYear() != null) {
            parameterMap.put("reportingYear", dto.getReportingYear());
        } else {
            parameterMap.put("reportingYear", Integer.toString(today.get(Calendar.YEAR)));
        }
        if (dto.getReportingMonthBegin() != null) {
            parameterMap.put("reportingMonthBegin", dto.getReportingMonthBegin());
        }
        if (dto.getReportingMonthEnd() != null) {
            parameterMap.put("reportingMonthEnd", dto.getReportingMonthEnd());
        }
        if (dto.getReportingYearBegin() != null) {
            parameterMap.put("reportingYearBegin", dto.getReportingYearBegin());
        }
        if (dto.getReportingYearEnd() != null) {
            parameterMap.put("reportingYearEnd", dto.getReportingYearEnd());
        }
        if (dto.getReportingDateBegin() != null) {
            parameterMap.put("reportingDateBegin", dto.getReportingDateBegin());
        }
        if (dto.getReportingDateEnd() != null) {
            parameterMap.put("reportingDateEnd", dto.getReportingDateEnd());
        }
        if (dto.getReportingDate() != null) {
            parameterMap.put("reportingDate", dto.getReportingDate());
        }
        parameterMap.put("reportTitle", reportTitle);

        try {
            // fetch the required records from the database
            jdbcUtil = new JDBCUtil();
            query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, " +
                    "facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility JOIN lga " +
                    "ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE " +
                    "facility_id = " + facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                parameterMap.put("facilityName", resultSet.getString("name"));
                parameterMap.put("lga", resultSet.getString("lga"));
                parameterMap.put("state", resultSet.getString("state"));
            }
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return parameterMap;
    }

    private void executeUpdate(String query) {
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }
}
