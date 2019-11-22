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
public class CurrentOnArtDmocTypeChartService {
    private JDBCUtil jdbcUtil;

    public CurrentOnArtDmocTypeChartService() {

    }

    public Map<String, Object> getChartData(long ipId, long stateId, long lgaId, long facilityId, String dataElementIds, Date reportingDateBegin, Date reportingDateEnd) {

        Map<String, Object> map = new HashMap<String, Object>();
        String strDate = DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd");
        java.sql.Date reportingDate = java.sql.Date.valueOf(strDate);
        strDate = DateUtil.parseDateToString(reportingDateEnd, "yyyy-MM-dd");
        java.sql.Date reportingDate2 = java.sql.Date.valueOf(strDate);
        // map.put("month", month);
        // map.put("year", year);

        String query = "SELECT SUM(value) AS value, data_element_id  FROM indicatorvalue WHERE data_element_id IN ( " + dataElementIds + ") AND report_date BETWEEN '" + reportingDate + "' AND '" + reportingDate2 + "'";
        if (stateId != 0) query = query + " AND state_id = " + stateId;
        if (lgaId != 0) query = query + " AND lga_id = " + lgaId;
        if (facilityId != 0) query = query + " AND facility_id = " + facilityId;

        query = query + "  GROUP BY data_element_id";
        try {
            ResultSet resultSet = execute(query);
            if (resultSet != null) {
                while (resultSet.next()) {
                    int dataElementId = resultSet.getInt("data_element_id");
                    if (dataElementId == 51) {
                        map.put("MMS", resultSet.getInt("value"));
                        //System.out.println(".....MMS:"+resultSet.getInt("value"));
                    } else {
                        if (dataElementId == 52) {
                            map.put("MMD", resultSet.getInt("value"));
                            //System.out.println(".....MMD:"+resultSet.getInt("value"));

                        } else {
                            if (dataElementId == 53) {
                                map.put("CARC", resultSet.getInt("value"));
                                //System.out.println("....CARC:"+resultSet.getInt("value"));
                            } else {
                                map.put("CPARP", resultSet.getInt("value"));
                                // System.out.println("....CPARP:"+resultSet.getInt("value"));
                            }
                        }
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return map;
    }

    public Map<String, Object> getChartDataByMonth(long ipId, long stateId, long lgaId, long facilityId, String dataElementIds, Date reportingDateBegin, Date reportingDateEnd) {

        Map<String, Object> map = new HashMap<String, Object>();
        String strDate = DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd");
        java.sql.Date reportingDate = java.sql.Date.valueOf(strDate);
        strDate = DateUtil.parseDateToString(reportingDateEnd, "yyyy-MM-dd");
        java.sql.Date reportingDate2 = java.sql.Date.valueOf(strDate);

        //map.put("month", month);
        //map.put("year", year);

        String query = "SELECT SUM(value) AS value, data_element_id  FROM indicatorvalue WHERE data_element_id IN ( " + dataElementIds + ") AND report_date BETWEEN '" + reportingDate + "' AND '" + reportingDate2 + "'";
        if (stateId != 0) query = query + " AND state_id = " + stateId;
        if (lgaId != 0) query = query + " AND lga_id = " + lgaId;
        if (facilityId != 0) query = query + " AND facility_id = " + facilityId;

        query = query + "  GROUP BY data_element_id";
        try {
            ResultSet resultSet = execute(query);
            if (resultSet != null) {
                while (resultSet.next()) {
                    int dataElementId = resultSet.getInt("data_element_id");
                    if (dataElementId == 51) {
                        map.put("MMS", resultSet.getInt("value"));
                        //System.out.println(".....MMS:"+resultSet.getInt("value"));
                    } else {
                        if (dataElementId == 52) {
                            map.put("MMD", resultSet.getInt("value"));
                            //System.out.println(".....MMD:"+resultSet.getInt("value"));

                        } else {
                            if (dataElementId == 53) {
                                map.put("CARC", resultSet.getInt("value"));
                                //System.out.println("....CARC:"+resultSet.getInt("value"));
                            } else {
                                map.put("CPARP", resultSet.getInt("value"));
                                // System.out.println("....CPARP:"+resultSet.getInt("value"));
                            }
                        }
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
