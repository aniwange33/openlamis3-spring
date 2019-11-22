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
public class CurrentOnArtChartService {
    private JDBCUtil jdbcUtil;

    public CurrentOnArtChartService() {

    }

    public Map<String, Object> getChartData(long ipId, long stateId, long lgaId, long facilityId,
                                            String dataElementId, int categoryFemaleId, int categoryMaleId,
                                            Date reportingDateBegin, Date reportingDateEnd) {

        Map<String, Object> map = new HashMap<String, Object>();
        String strDate = DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd");
        java.sql.Date reportingDate = java.sql.Date.valueOf(strDate);
        strDate = DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd");
        java.sql.Date reportingDate2 = java.sql.Date.valueOf(strDate);

        String query = "SELECT SUM(value) AS value, category_id FROM indicatorvalue WHERE data_element_id = " +
                Integer.parseInt(dataElementId) + " AND (category_id = " + categoryFemaleId + " OR category_id = " +
                categoryMaleId + ") AND report_date BETWEEN '" + reportingDate + "'  AND  '" + reportingDate2 + "'";
        if (stateId != 0) {
            query = query + " AND state_id = " + stateId;
        }
        if (lgaId != 0) {
            query = query + " AND lga_id = " + lgaId;
        }
        if (facilityId != 0) {
            query = query + " AND facility_id = " + facilityId;
        }

        query = query + " GROUP BY category_id";

        try {
            ResultSet resultSet = execute(query);
            if (resultSet != null) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("category_id");
                    if (id == categoryMaleId) {
                        map.put("value1", resultSet.getInt("value"));
                    } else {
                        map.put("value2", resultSet.getInt("value") * -1);
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return map;
    }

    public Map<String, Object> getChartDataByMonth(long ipId, long stateId, long lgaId, long facilityId,
                                                   String dataElementId, int categoryFemaleId, int categoryMaleId,
                                                   Date reportingDateBegin, Date reportingDateEnd) {

        Map<String, Object> map = new HashMap<String, Object>();
        String strDate = DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd");
        java.sql.Date reportingDate = java.sql.Date.valueOf(strDate);
        strDate = DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd");
        java.sql.Date reportingDate2 = java.sql.Date.valueOf(strDate);

        String query = "SELECT SUM(value) AS value, category_id FROM indicatorvalue WHERE data_element_id = " +
                Integer.parseInt(dataElementId) + " AND (category_id = " + categoryFemaleId + " OR category_id = " +
                categoryMaleId + ")  AND report_date BETWEEN '" + reportingDate + "'  AND  '" + reportingDate2 + "'";
        if (stateId != 0) {
            query = query + " AND state_id = " + stateId;
        }
        if (lgaId != 0) {
            query = query + " AND lga_id = " + lgaId;
        }
        if (facilityId != 0) {
            query = query + " AND facility_id = " + facilityId;
        }

        query = query + " GROUP BY category_id";

        try {
            ResultSet resultSet = execute(query);
            if (resultSet != null) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("category_id");
                    if (id == categoryMaleId) {
                        map.put("value1", resultSet.getInt("value"));
                    } else {
                        map.put("value2", resultSet.getInt("value") * -1);
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
