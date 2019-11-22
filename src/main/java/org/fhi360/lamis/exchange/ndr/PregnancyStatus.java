/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.ndr;

/**
 * @author user1
 */

import org.fhi360.lamis.config.ContextProvider;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

public class PregnancyStatus {

    public PregnancyStatus() {
    }

    public static Map getPregnancyStatus(long patientId) {
        String databaseName = "mysql"; //new PropertyAccessor().getDatabaseName();

        String query = "SELECT date_visit, pregnant, lmp, DATE_ADD(lmp, INTERVAL 280 DAY) AS edd FROM clinic WHERE patient_id = " + patientId + " AND date_visit >= DATE_ADD(CURDATE(), INTERVAL -9 MONTH) AND date_visit <= CURDATE()";
        if (databaseName.equals("h2"))
            query = "SELECT date_visit, pregnant, lmp, DATEADD('DAY', 280, lmp) AS edd FROM clinic WHERE patient_id = " + patientId + " AND date_visit >= DATEADD('MONTH', -9, CURDATE()) AND date_visit <= CURDATE()";

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "NK");   //If no clinic record is found the pregnancy status is Unkown
        map.put("lmp", null);
        map.put("edd", null);
        ContextProvider.getBean(JdbcTemplate.class).query(query, rs -> {
            map.put("status", "NP");  //If clinic record is found but no record with pregnancy status checked
            map.put("lmp", null);
            map.put("edd", null);
            if (rs.getInt("pregnant") == 1) {
                map.put("status", "P");
                map.put("lmp", rs.getDate("lmp"));
                map.put("edd", rs.getDate("edd"));  //EDD calcuated by adding 9 months and subtracting 7 weeks (280 days) to the LMP i.e Naegele's rule
            }
        });
        return map;
    }
}
