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
public class HtsOnArtChartService {
    private JDBCUtil jdbcUtil;

    public HtsOnArtChartService() {

    }

    public Map<String, String> getChartData(long ipId, long stateId, long lgaId, long facilityId, Date reportingDateBegin) {

        Map<String, String> map = new HashMap<String, String>();
        String strDate = DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd");
        java.sql.Date reportingDate = java.sql.Date.valueOf(strDate);

        String query = "SELECT * FROM hts WHERE UPPER(hiv_test_result) = 'POSITIVE' AND date_visit = '" + reportingDate + "'";
        if (stateId != 0) query = query + " AND state_id = " + stateId;
        if (lgaId != 0) query = query + " AND lga_id = " + lgaId;
        if (facilityId != 0) query = query + " AND facility_id = " + facilityId;

        int initiated = 0;
        int positive = 0;
        try {
            ResultSet resultSet = execute(query);
            if (resultSet != null) {
                while (resultSet.next()) {
                    positive++;

                    String artReferred = resultSet.getString("date_started");
                    if (artReferred != null && !artReferred.isEmpty()) {
                        initiated++;
                    }
                }
                map.put("value1", Integer.toString(positive)); // positive
                map.put("value2", Integer.toString(initiated)); // initiated on ART
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return map;
    }

    public Map<String, String> getChartDataByMonth(long ipId, long stateId, long lgaId, long facilityId, int month, int year) {

        Map<String, String> map = new HashMap<String, String>();

        // map.put("month", month);
        // map.put("year", year);

        String query = "SELECT * FROM hts WHERE UPPER(hiv_test_result) = 'POSITIVE' AND MONTH(date_visit) = " + month + " AND YEAR(date_visit) = " + year;
        if (stateId != 0) query = query + " AND state_id = " + stateId;
        if (lgaId != 0) query = query + " AND lga_id = " + lgaId;
        if (facilityId != 0) query = query + " AND facility_id = " + facilityId;

        int initiated = 0;
        int positive = 0;
        try {
            ResultSet resultSet = execute(query);
            if (resultSet != null) {
                while (resultSet.next()) {
                    positive++;

                    String artReferred = resultSet.getString("date_started");
                    if (artReferred != null && !artReferred.isEmpty()) {
                        initiated++;
                    }
                }
                map.put("value1", Integer.toString(positive)); // positive
                map.put("value2", Integer.toString(initiated)); // initiated on ART
            }
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
