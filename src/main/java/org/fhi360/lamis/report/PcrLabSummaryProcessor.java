/**
 *
 * @author AALOZIE
 */

package org.fhi360.lamis.report;

import org.fhi360.lamis.controller.report.ReportParameterDTO;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PcrLabSummaryProcessor {

    private String query;
   private JDBCUtil jdbcUtil;
   private PreparedStatement preparedStatement;

    public PcrLabSummaryProcessor() {
    }

    public synchronized List<Map<String, Object>> process(ReportParameterDTO dto, Long facilityId) {
        List<Map<String, Object>> reportList = new ArrayList<>();
        int reportingMonth = DateUtil.getMonth(dto.getReportingMonth());
        int reportingYear = Integer.parseInt(dto.getReportingYear());
        String reportingDateBegin = DateUtil.parseDateToString(DateUtil
                .getFirstDateOfMonth(reportingYear, reportingMonth), "yyyy-MM-dd");
        String reportingDateEnd = DateUtil.parseDateToString(DateUtil
                .getLastDateOfMonth(reportingYear, reportingMonth), "yyyy-MM-dd");
        
        query = "SELECT COUNT(*) AS count FROM specimen WHERE facility_id = " + facilityId +
                " AND MONTH(date_received) = " + reportingMonth + " AND YEAR(date_received) = " + reportingYear;
        int samplesReceived = getCount(query);                
        
        query = "SELECT COUNT(*) AS count FROM specimen WHERE facility_id = " + facilityId +
                " AND MONTH(date_assay) = " + reportingMonth + " AND YEAR(date_assay) = " + reportingYear;
        int testDone = getCount(query); 
        
        query = "SELECT COUNT(*) AS count FROM specimen WHERE facility_id = " + facilityId +
                " AND MONTH(date_dispatched) = " + reportingMonth + " AND YEAR(date_dispatched) = " + reportingYear +
                " AND result = 'Negative'";
        int resultSentNegative = getCount(query); 

        query = "SELECT COUNT(*) AS count FROM specimen WHERE facility_id = " + facilityId +
                " AND MONTH(date_dispatched) = " + reportingMonth + " AND YEAR(date_dispatched) = " + reportingYear +
                " AND result = 'Positive'";
        int resultSentPositive = getCount(query); 

        query = "SELECT COUNT(*) AS count FROM specimen WHERE facility_id = " + facilityId +
                " AND MONTH(date_dispatched) = " + reportingMonth + " AND YEAR(date_dispatched) = " + reportingYear +
                " AND result IN('Indeterminate', 'Fail', 'Invalid')";
        int resultSentIndeterminate = getCount(query); 

        query = "SELECT COUNT(*) AS count FROM specimen WHERE facility_id = " + facilityId +
                " AND MONTH(date_dispatched) = " + reportingMonth + " AND YEAR(date_dispatched) = " + reportingYear +
                " AND DATEDIFF(DAY, date_received, date_assay) > 10";
        int testAboveThreshold = getCount(query); 

        // create an array from object properties 
        Map<String, Object> map = new HashMap<>();
        map.put("samplesReceived", samplesReceived);
        map.put("testDone", testDone);
        map.put("resultSentNegative", resultSentNegative);
        map.put("resultSentPositive", resultSentPositive);
        map.put("resultSentIndeterminate", resultSentIndeterminate);
        map.put("resultSentTotal", resultSentNegative + resultSentPositive + resultSentIndeterminate);
        map.put("testAboveThreshold", testAboveThreshold);
        reportList.add(map);        
        return reportList;
    }

    public Map<String, Object> getReportParameters(ReportParameterDTO dto, Long facilityId) {
        Map<String, Object> parameterMap = new HashMap<>();
        String reportingPeriodBegin = dto.getReportingMonthBegin() + " " + dto.getReportingYearBegin();
        String reportingPeriodEnd = dto.getReportingMonthEnd() + " " + dto.getReportingYearEnd();
        parameterMap.put("reportingPeriodBegin", reportingPeriodBegin);
        parameterMap.put("reportingPeriodEnd", reportingPeriodEnd);
        ResultSet resultSet;
        
        try{
            // fetch the required records from the database
            jdbcUtil = new JDBCUtil();
            query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, " +
                    "facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility JOIN lga " +
                    "ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id " +
                    "WHERE facility_id = " + facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next()) {
                parameterMap.put("facilityName", resultSet.getString("name"));  
                parameterMap.put("lga", resultSet.getString("lga"));            
                parameterMap.put("state", resultSet.getString("state")); 
            }
            resultSet = null;                        
        }
        catch (Exception exception) {
            resultSet = null;                        
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }                
        return parameterMap;        
    }
 
    private int getCount(String query) {
       int count  = 0;
        ResultSet resultSet;
        try {
            jdbcUtil = new JDBCUtil();            
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                count = resultSet.getInt("count");
            }
            resultSet = null;                        
        }
        catch (Exception exception) {
            resultSet = null;                        
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return count;
    }      
    
}
