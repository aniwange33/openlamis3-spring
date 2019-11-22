/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.chart;

import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author user10
 */
@Component
public class NewlyEnrolledChartService {
    private JDBCUtil jdbcUtil;

    public NewlyEnrolledChartService() {

    }

    public Map<String, Object> getChartData(long ipId, long stateId, long lgaId, long facilityId, String dataElementId, String categoryFemaleIds, String categoryMaleIds, Date reportingDateBegin) {

        Map<String, Object> map = new HashMap<String, Object>();
        String strDate = DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd");
        java.sql.Date reportingDate = java.sql.Date.valueOf(strDate);
        // map.put("month", month);
        // map.put("year", year);

        try {
            String query = "SELECT SUM(value) AS value FROM indicatorvalue WHERE data_element_id = " + Integer.parseInt(dataElementId) + " AND category_id IN (" + categoryFemaleIds + ") AND report_date = '" + reportingDate + "'";
            if (stateId != 0) query = query + " AND state_id = " + stateId;
            if (lgaId != 0) query = query + " AND lga_id = " + lgaId;
            if (facilityId != 0) query = query + " AND facility_id = " + facilityId;

            int female = 0;

            jdbcUtil = new JDBCUtil();
            ResultSet resultSet = execute(query);
            if (resultSet != null) {
                if (resultSet.next()) {
                    female = resultSet.getInt("value");
                }
            }
            map.put("female", female);


            query = "SELECT SUM(value) AS value FROM indicatorvalue WHERE data_element_id = " + Integer.parseInt(dataElementId) + " AND category_id IN (" + categoryMaleIds + ") AND report_date = '" + reportingDate + "'";
            if (stateId != 0) query = query + " AND state_id = " + stateId;
            if (lgaId != 0) query = query + " AND lga_id = " + lgaId;
            if (facilityId != 0) query = query + " AND facility_id = " + facilityId;

            int male = 0;

            resultSet = execute(query);
            if (resultSet != null) {
                if (resultSet.next()) {
                    male = resultSet.getInt("value");
                }
            }
            map.put("male", male);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return map;
    }

    public Map<String, Object> getChartDataByMonth(long ipId, long stateId, long lgaId, long facilityId, String dataElementId, String categoryFemaleIds, String categoryMaleIds, int month, int year) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("month", month);
        map.put("year", year);

        try {
            String query = "SELECT SUM(value) AS value FROM indicatorvalue WHERE data_element_id = " + Integer.parseInt(dataElementId) + " AND category_id IN (" + categoryFemaleIds + ")  AND MONTH(report_date) = " + month + " AND YEAR(report_date) = " + year;
            if (stateId != 0) query = query + " AND state_id = " + stateId;
            if (lgaId != 0) query = query + " AND lga_id = " + lgaId;
            if (facilityId != 0) query = query + " AND facility_id = " + facilityId;

            int female = 0;

            jdbcUtil = new JDBCUtil();
            ResultSet resultSet = execute(query);
            if (resultSet != null) {
                if (resultSet.next()) {
                    female = resultSet.getInt("value");
                }
            }
            map.put("female", female);


            query = "SELECT SUM(value) AS value FROM indicatorvalue WHERE data_element_id = " + Integer.parseInt(dataElementId) + " AND category_id IN (" + categoryMaleIds + ")  AND MONTH(report_date) = " + month + " AND YEAR(report_date) = " + year;
            if (stateId != 0) query = query + " AND state_id = " + stateId;
            if (lgaId != 0) query = query + " AND lga_id = " + lgaId;
            if (facilityId != 0) query = query + " AND facility_id = " + facilityId;

            int male = 0;

            resultSet = execute(query);
            if (resultSet != null) {
                if (resultSet.next()) {
                    male = resultSet.getInt("value");
                }
            }
            map.put("male", male);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return map;
    }

    private ResultSet execute(String query) {
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return rs;
    }
}
