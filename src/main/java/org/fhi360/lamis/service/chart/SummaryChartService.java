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
public class SummaryChartService {
    private JDBCUtil jdbcUtil;

    public SummaryChartService() {

    }

    public ArrayList<Map<String, Object>> getChartData(long ipId, long stateId, long lgaId, long facilityId, String dataElementIds, String categoryFemaleIds, String categoryMaleIds, Date reportingDateBegin, Date reportingDateEnd) {
        ArrayList<Map<String, Object>> analysisList = new ArrayList<Map<String, Object>>();
        String strDate = DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd");
        java.sql.Date reportingDate = java.sql.Date.valueOf(strDate);
        strDate = DateUtil.parseDateToString(reportingDateEnd, "yyyy-MM-dd");
        java.sql.Date reportingDate2 = java.sql.Date.valueOf(strDate);

        String[] ids = dataElementIds.split(",");
        for (String dataElementId : ids) {
            Map<String, Object> map = new HashMap<String, Object>();

            try {
                String query = "SELECT SUM(value) AS value FROM indicatorvalue WHERE data_element_id = " + Integer.parseInt(dataElementId) + " AND category_id IN (" + categoryFemaleIds + ") AND report_date BETWEEN '" + reportingDate + "' AND '" + reportingDate2 + "'";
                if (stateId != 0) query = query + " AND state_id = " + stateId;
                if (lgaId != 0) query = query + " AND lga_id = " + lgaId;
                if (facilityId != 0) query = query + " AND facility_id = " + facilityId;

                int female = 0;

                ResultSet resultSet = execute(query);
                if (resultSet != null) {
                    if (resultSet.next()) {
                        female = resultSet.getInt("value");
                    }
                }
                System.out.println(".....female:" + female);
                map.put("female", female);


                query = "SELECT SUM(value) AS value FROM indicatorvalue WHERE data_element_id = " + dataElementId + " AND category_id IN (" + categoryMaleIds + ") AND report_date BETWEEN '" + reportingDate + "' AND '" + reportingDate2 + "'";
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
                System.out.println(".....male:" + male);
                map.put("male", male);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            analysisList.add(map);
        }
        return analysisList;
    }

    public ArrayList<Map<String, Object>> getChartDataByMonth(long ipId, long stateId, long lgaId, long facilityId, String dataElementIds, String categoryFemaleIds, String categoryMaleIds, Date reportingDateBegin, Date reportingDateEnd) {
        ArrayList<Map<String, Object>> analysisList = new ArrayList<Map<String, Object>>();
        String strDate = DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd");
        java.sql.Date reportingDate = java.sql.Date.valueOf(strDate);
        strDate = DateUtil.parseDateToString(reportingDateEnd, "yyyy-MM-dd");
        java.sql.Date reportingDate2 = java.sql.Date.valueOf(strDate);

        String[] ids = dataElementIds.split(",");
        for (String dataElementId : ids) {
            Map<String, Object> map = new HashMap<String, Object>();

            try {
                String query = "SELECT SUM(value) AS value FROM indicatorvalue WHERE data_element_id = " + Integer.parseInt(dataElementId) + " AND category_id IN (" + categoryFemaleIds + ") AND report_date BETWEEN '" + reportingDate + "' AND '" + reportingDate2 + "'";
                if (stateId != 0) query = query + " AND state_id = " + stateId;
                if (lgaId != 0) query = query + " AND lga_id = " + lgaId;
                if (facilityId != 0) query = query + " AND facility_id = " + facilityId;

                int female = 0;

                ResultSet resultSet = execute(query);
                if (resultSet != null) {
                    if (resultSet.next()) {
                        female = resultSet.getInt("value");
                    }
                }
                System.out.println(".....female:" + female);
                map.put("female", female);


                query = "SELECT SUM(value) AS value FROM indicatorvalue WHERE data_element_id = " + dataElementId + " AND category_id IN (" + categoryMaleIds + ") AND report_date BETWEEN '" + reportingDate + "' AND '" + reportingDate2 + "'";
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
                System.out.println(".....male:" + male);
                map.put("male", male);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            analysisList.add(map);
        }
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
