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
public class TbStatusChartService {
    private JDBCUtil jdbcUtil;

    public TbStatusChartService() {

    }

    public Map<String, Object> getChartData(long ipId, long stateId, long lgaId, long facilityId, String dataElementIds, String categoryFemaleIds, String categoryMaleIds, Date reportingDateBegin) {

        Map<String, Object> map = new HashMap<String, Object>();
        String strDate = DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd");
        java.sql.Date reportingDate = java.sql.Date.valueOf(strDate);
        //map.put("month", month);
        //map.put("year", year);

        String query = "SELECT SUM(value) AS value, data_element_id  FROM indicatorvalue WHERE data_element_id IN ( " + dataElementIds + ") AND report_date = '" + reportingDate + "'";
        if (stateId != 0) query = query + " AND state_id = " + stateId;
        if (lgaId != 0) query = query + " AND lga_id = " + lgaId;
        if (facilityId != 0) query = query + " AND facility_id = " + facilityId;

        query = query + " GROUP BY data_element_id";

        try {
            ResultSet resultSet = execute(query);
            if (resultSet != null) {
                while (resultSet.next()) {
                    int dataElementId = resultSet.getInt("data_element_id");
                    if (dataElementId == 44) {
                        map.put("value1", resultSet.getInt("value"));
                        //System.out.println(".....clinic visit:"+resultSet.getInt("value"));
                    } else {
                        map.put("value2", resultSet.getInt("value"));
                        //System.out.println(".....documented TB:"+resultSet.getInt("value"));
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return map;
    }

    public Map<String, Object> getChartDataByMonth(long ipId, long stateId, long lgaId, long facilityId, String dataElementIds, String categoryFemaleIds, String categoryMaleIds, int month, int year) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("month", month);
        map.put("year", year);

        String query = "SELECT SUM(value) AS value, data_element_id  FROM indicatorvalue WHERE data_element_id IN ( " + dataElementIds + ") AND MONTH(report_date) = " + month + " AND YEAR(report_date) = " + year;
        if (stateId != 0) query = query + " AND state_id = " + stateId;
        if (lgaId != 0) query = query + " AND lga_id = " + lgaId;
        if (facilityId != 0) query = query + " AND facility_id = " + facilityId;

        query = query + " GROUP BY data_element_id";

        try {
            ResultSet resultSet = execute(query);
            if (resultSet != null) {
                while (resultSet.next()) {
                    int dataElementId = resultSet.getInt("data_element_id");
                    if (dataElementId == 44) {
                        map.put("value1", resultSet.getInt("value"));
                        //System.out.println(".....clinic visit:"+resultSet.getInt("value"));
                    } else {
                        map.put("value2", resultSet.getInt("value"));
                        //System.out.println(".....documented TB:"+resultSet.getInt("value"));
                    }
                }
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
