/**
 *
 * @author user1
 */

package org.fhi360.lamis.report;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.fhi360.lamis.utility.JDBCUtil;
import java.util.HashMap;
import java.util.Map;

public class StatusUpdateIndicatorProcessor {
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet, rs;
    
    public synchronized Map calculate(long facilityId, String reportingDateEnd) {
        int denominator  = 0;
        int numerator = 0;
        Map<String, Object> map = new HashMap<>();
        
        //current ART patients whos last refill was more than 3 months ago 
        executeUpdate("DROP TABLE IF EXISTS lastrefill");        
        query = "CREATE TEMPORARY TABLE lastrefill AS SELECT DISTINCT patient_id, MAX(date_visit) AS date_visit, duration FROM pharmacy WHERE facility_id = " + facilityId + " AND date_visit < '" + reportingDateEnd + "'  AND regimentype_id IN (1, 2, 3, 4, 5) GROUP BY patient_id, duration"; 
        executeUpdate(query);   

        executeUpdate("DROP TABLE IF EXISTS refill");        
        query = "CREATE TEMPORARY TABLE refill AS SELECT DISTINCT pharmacy.patient_id FROM pharmacy JOIN lastrefill ON pharmacy.patient_id = lastrefill.patient_id WHERE pharmacy.facility_id = " + facilityId + "  AND pharmacy.date_visit > lastrefill.date_visit AND (lastrefill.date_visit + lastrefill.duration + 90) <= pharmacy.date_visit";
        executeUpdate(query);
        try {
            query = "SELECT patient_id FROM lastrefill WHERE patient_id NOT IN (SELECT patient_id FROM refill)"; //selecting defaulters for refill
            jdbcUtil = new JDBCUtil();            
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()) {
                denominator  = denominator  + 1;
                long patientId = resultSet.getLong("patient_id");
                query = "SELECT patient_id FROM statushistory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND current_status IN ('Lost to Follow Up', 'Stopped Treatment', 'ART Restart', 'Known Death', 'ART Transfer Out') AND date_current_status <= '" + reportingDateEnd + "'";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                if(rs.next()) numerator = numerator + 1;
            }
            map.put("denominator", denominator );
            map.put("numerator", numerator);
        }
        catch (Exception exception) {
            resultSet = null;                        
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return map;
    }

    private void executeUpdate(String query) {
        try {
            jdbcUtil = new JDBCUtil();            
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }        
    }        
    
    private int getCount(String query) {
       int count  = 0;
       try {
            jdbcUtil = new JDBCUtil();            
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                count = resultSet.getInt("count");
            }
            resultSet = null;                        
        }
        catch (Exception exception) {
            resultSet = null;                        
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return count;
    }      
    
}
