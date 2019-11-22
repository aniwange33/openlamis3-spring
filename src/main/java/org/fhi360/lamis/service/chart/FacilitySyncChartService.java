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
public class FacilitySyncChartService {

    private JDBCUtil jdbcUtil;

    public FacilitySyncChartService() {


    }

    public Map<String, Object> getChartData(int stateId, Date reportingDateBegin, Date reportingDateEnd) {

        Map<String, Object> map = new HashMap<String, Object>();
        String strDate = DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd");
        java.sql.Date reportingDate = java.sql.Date.valueOf(strDate);
        strDate = DateUtil.parseDateToString(reportingDateEnd, "yyyy-MM-dd");
        java.sql.Date reportingDate2 = java.sql.Date.valueOf(strDate);

        try {

            //Number of expected uploads
            String query = "SELECT COUNT(*) AS count FROM facility WHERE state_id = " + stateId + " AND active";
            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                map.put("value1", resultSet.getInt("count"));
            }

            //Number of uploads
            query = "SELECT COUNT(DISTINCT facility_id) AS count FROM synchistory WHERE upload_date BETWEEN '" + reportingDate + "' AND  '" + reportingDate2 + "' AND facility_id IN (SELECT DISTINCT facility_id FROM facility WHERE state_id = " + stateId + " AND active)";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                map.put("value2", resultSet.getInt("count"));
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return map;
    }

    public String getStateById(int stateId) {
        try {
            String query = "SELECT name FROM state WHERE state_id = " + stateId;
            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "";
    }

}
