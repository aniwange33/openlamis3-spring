/**
 * @author AALOZIE
 */

package org.fhi360.lamis.report;

import org.fhi360.lamis.controller.report.ReportParameterDTO;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

@Component
public class SpecimenReports {
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private final JdbcTemplate jdbcTemplate;

    public SpecimenReports(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> eidRegister(String month, String year, Long facilityId) {
        List<Map<String, Object>> reportList = new ArrayList<>();
        int reportingMonth = DateUtil.getMonth(month);
        int reportingYear = Integer.parseInt(year);
        String reportingDateBegin = DateUtil.parseDateToString(DateUtil
                .getFirstDateOfMonth(reportingYear, reportingMonth), "yyyy-MM-dd");
        String reportingDateEnd = DateUtil.parseDateToString(DateUtil
                .getLastDateOfMonth(reportingYear, reportingMonth), "yyyy-MM-dd");
        String query = "SELECT specimen_type, labno, date_received, date_collected, DATEDIFF(DAY, date_collected," +
                " date_received) AS transit_time, date_assay, date_reported, date_dispatched, DATEDIFF(DAY, " +
                "date_received, date_dispatched) AS turnaround_time, "
                + " hospital_num, result, final_result, surname, other_names, gender, date_birth, age, age_unit FROM " +
                "specimen WHERE facility_id = " + facilityId + " AND MONTH(date_received) = " + reportingMonth +
                " AND YEAR(date_received) = " + reportingYear + " ORDER BY date_received";
        return generateReportList(query);
    }

    public List<Map<String, Object>> specimenSummary(String reportingMonth, String reportingYear, Long facilityId) {
        List<Map<String, Object>> reportList = new ArrayList<>();
        String reportTitle = "PCR LABORATORY SUMMARY";
        ResultSet resultSet;
        try {
            // fetch the required records from the database
            jdbcUtil = new JDBCUtil();
            String query = "SELECT specimen.treatment_unit_id, facility.name FROM specimen JOIN facility ON " +
                    "specimen.treatment_unit_id = facility.facility_id WHERE facility_id = " + facilityId +
                    " AND MONTH(date_collected) = " + reportingMonth + " AND YEAR(date_collected) = " + reportingYear;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                long treatmentUnitId = resultSet.getLong("treatment_unit_id");
                String treatmentUnit = resultSet.getString("name");
                query = "SELECT COUNT(*) AS count FROM specimen WHERE treatment_unit_id = " + treatmentUnitId +
                        " AND MONTH(date_collected) = " + reportingMonth + " AND YEAR(date_collected) = " +
                        reportingYear;
                int sampleCollected = getCount(query);

                query = "SELECT COUNT(*) AS count FROM specimen WHERE treatment_unit_id = " + treatmentUnitId +
                        " AND MONTH(date_dispatched) = " + reportingMonth + " AND YEAR(date_dispatched) = " +
                        reportingYear;
                int resultSent = getCount(query);

                // create an array from object properties 
                Map<String, Object> map = new HashMap<>();
                map.put("sampleCollected", sampleCollected);
                map.put("resultSent", resultSent);
                map.put("treatmentUnit", treatmentUnit);
                reportList.add(map);
            }
            resultSet = null;
        } catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return reportList;
    }

    public List<Map<String, Object>> generateReportList(String query) {
        return jdbcTemplate.queryForList(query);
    }

    public Map<String, Object> getReportParameters(ReportParameterDTO dto, long facilityId) {
        Map<String, Object> parameterMap = new HashMap<>();
        String reportTitle = "PCR LABORATORY REGISTER";
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
            String query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, " +
                    "facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility JOIN lga " +
                    "ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id " +
                    "WHERE facility_id = " + facilityId;
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

    private int getCount(String query) {
        int count = 0;
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            resultSet = null;
        } catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return count;
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
