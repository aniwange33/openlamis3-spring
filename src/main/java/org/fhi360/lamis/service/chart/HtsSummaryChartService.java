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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author user10
 */
@Component
public class HtsSummaryChartService {
    private JDBCUtil jdbcUtil;

    public HtsSummaryChartService() {

    }

    public ArrayList<Map<String, Object>> getChartData(long ipId, long stateId, long lgaId, long facilityId, Date reportingDateBegin, Date reportingDateEnd) {
        ArrayList<Map<String, Object>> analysisList = new ArrayList<Map<String, Object>>();
        String strDate = DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd");
        java.sql.Date reportingDate = java.sql.Date.valueOf(strDate);
        strDate = DateUtil.parseDateToString(reportingDateEnd, "yyyy-MM-dd");
        java.sql.Date reportingDate2 = java.sql.Date.valueOf(strDate);

        Map<String, Object> map = new HashMap<String, Object>();

        try {
            String query = "SELECT * FROM hts WHERE date_visit BETWEEN '" + reportingDate + "' AND '" + reportingDate2 + "'";
            if (stateId != 0) query = query + " AND state_id = " + stateId;
            if (lgaId != 0) query = query + " AND lga_id = " + lgaId;
            if (facilityId != 0) query = query + " AND facility_id = " + facilityId;

            int tested = 0;
            int positive = 0;
            int initiated = 0;

            ResultSet resultSet = execute(query);
            if (resultSet != null) {
                while (resultSet.next()) {
                    tested++;

                    String hivResult = resultSet.getString("hiv_test_result");
                    if (hivResult.equalsIgnoreCase("Positive"))
                        positive++;

                    String artReferred = resultSet.getString("date_started");
                    if (artReferred != null && !artReferred.isEmpty())
                        initiated++;
                }
            }

            map.put("tested", tested);
            map.put("positive", positive);
            map.put("initiated", initiated);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        analysisList.add(map);

        return analysisList;
    }

    public ArrayList<Map<String, Object>> getChartDataByMonth(long ipId, long stateId, long lgaId, long facilityId, Date reportingDateBegin, Date reportingDateEnd) {
        ArrayList<Map<String, Object>> analysisList = new ArrayList<Map<String, Object>>();
        String strDate = DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd");
        System.out.println("Date: " + strDate);
        java.sql.Date reportingDate = java.sql.Date.valueOf(strDate);
        strDate = DateUtil.parseDateToString(reportingDateEnd, "yyyy-MM-dd");
        System.out.println("Date2: " + strDate);
        java.sql.Date reportingDate2 = java.sql.Date.valueOf(strDate);

        Map<String, Object> map = new HashMap<String, Object>();

        try {
            String query = "SELECT * FROM hts WHERE date_visit BETWEEN '" + reportingDate + "' AND '" + reportingDate2 + "'";
            if (stateId != 0) query = query + " AND state_id = " + stateId;
            if (lgaId != 0) query = query + " AND lga_id = " + lgaId;
            if (facilityId != 0) query = query + " AND facility_id = " + facilityId;

            int tested = 0;
            int positive = 0;
            int initiated = 0;

            ResultSet resultSet = execute(query);
            if (resultSet != null) {
                while (resultSet.next()) {
                    tested++;

                    String hivResult = resultSet.getString("hiv_test_result");
                    if (hivResult.equalsIgnoreCase("Positive"))
                        positive++;

                    String artReferred = resultSet.getString("date_started");
                    if (artReferred != null && !artReferred.isEmpty())
                        initiated++;
                }
            }
            map.put("tested", tested);
            map.put("positive", positive);
            map.put("initiated", initiated);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        analysisList.add(map);

        return analysisList;
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
