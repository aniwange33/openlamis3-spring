/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility.builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author User10
 */
public class BiometricReportListBuilder {

    private static final DecimalFormat FORMATTER = new DecimalFormat("#,###");

    public static List<Map<String, Object>> buildEnrollmentList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        rs.beforeFirst();
        int count = 1;
        while (rs.next()) {
            Map<String, Object> map = new HashMap<>();
            map.put("sn", FORMATTER.format(count++));
            String name = rs.getString("other_names") + " " + rs.getString("surname");
            map.put("name", name);
            map.put("unique_id", rs.getString("unique_id"));
            map.put("gender", rs.getString("gender"));
            map.put("age", rs.getInt("age") + " " + rs.getString("age_unit"));
            map.put("address", rs.getString("address"));
            map.put("phone", rs.getString("phone"));
            map.put("current_status", rs.getString("current_status"));
            result.add(map);
        }
        return result;
    }
    
    public static List<Map<String, Object>> buildDuplicatesList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        rs.beforeFirst();
        int count = 1;
        while (rs.next()) {
            Map<String, Object> map = new HashMap<>();
            map.put("sn", FORMATTER.format(count++));
            String name = rs.getString("other_names") + " " + rs.getString("surname");
            map.put("name", name);
            map.put("unique_id", rs.getString("unique_id"));
            map.put("gender", rs.getString("gender"));
            map.put("age", rs.getInt("age") + " " + rs.getString("age_unit"));
            map.put("address", rs.getString("address"));
            map.put("phone", rs.getString("phone"));
            map.put("current_status", rs.getString("current_status"));
            result.add(map);
        }
        return result;
    }
}
