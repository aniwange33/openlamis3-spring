/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.report;

import org.fhi360.lamis.controller.report.ReportParameterDTO;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.builder.LaboratoryListBuilder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

@Component
public class LaboratoryReports {


    private String reportTitle;


    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private List<Map<String, Object>> reportList;


    String reportingDateBegin;
    String reportingDateEnd;

    public List<Map<String, Object>> process(ReportParameterDTO dto, String description, Integer labtestId, Long facilityId) {
        reportTitle = "Patients Laboratory Result - " + description;
        reportingDateBegin = DateUtil.formatDateString(dto.getReportingDateBegin(),
                "MM/dd/yyyy", "yyyy-MM-dd");
        reportingDateEnd = DateUtil.formatDateString(dto.getReportingDateEnd(),
                "MM/dd/yyyy", "yyyy-MM-dd");
        query = "SELECT laboratory.*, patient.hospital_num, patient.surname, patient.other_names, patient.gender, " +
                "patient.date_birth, DATEDIFF(YEAR, patient.date_birth, CURDATE()) AS age, patient.current_status, " +
                "patient.date_current_status, patient.date_started, labtest.measureab, labtest.measurepc FROM " +
                "laboratory JOIN patient ON laboratory.patient_id = patient.patient_id JOIN labtest ON " +
                "laboratory.labtest_id = labtest.labtest_id WHERE laboratory.facility_id = " + facilityId +
                " AND laboratory.labtest_id = " + labtestId + " AND laboratory.date_reported >= '" +
                reportingDateBegin + "' AND laboratory.date_reported <= '" + reportingDateEnd + "' ORDER BY " +
                "laboratory.date_reported";
        return generateReportList(query);
    }

    public List<Map<String, Object>> generateReportList(String query) {
        reportList = new ArrayList<>();
        try {
            // fetch the required records from the database
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            reportList = new LaboratoryListBuilder().buildLabResultQueryList(resultSet);
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return reportList;
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
            query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility JOIN lga ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE facility_id = " + facilityId;
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
