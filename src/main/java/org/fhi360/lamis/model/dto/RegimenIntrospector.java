/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model.dto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import org.fhi360.lamis.model.RegimenHistory;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;

/**
 *
 * @author user1
 */
public class RegimenIntrospector {
    private static JDBCUtil jdbcUtil;

    public RegimenIntrospector() {
    }
    
    public static boolean substitutedOrSwitched(String regimen1, String regimen2) {
        boolean changed = false; 
        try {
            String query = "SELECT DISTINCT composition, description FROM regimen WHERE description = '" + regimen1 + "' OR description = '" + regimen2 + "'";
            jdbcUtil = new JDBCUtil();
            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            String composition1 = "";
            String composition2 = "";
            while (rs.next()) {
                if(rs.getString("description").equals(regimen1)) composition1 = rs.getString("composition");                
                if(rs.getString("description").equals(regimen2)) composition2 = rs.getString("composition");                
            }
            if(!composition1.equals(composition2)) {
                changed = true;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }          
        return changed;
    }

    public static boolean substitutedOrSwitched(long regimenId1, long regimenId2) {
        boolean changed = false; 
        try {
            String query = "SELECT DISTINCT composition, regimen_id FROM regimen WHERE regimen_id = " + regimenId1 + " OR regimen_id = " + regimenId2;
            jdbcUtil = new JDBCUtil();
            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            String composition1 = "";
            String composition2 = "";
            while (rs.next()) {
                if(rs.getLong("regimen_id") == regimenId1) composition1 = rs.getString("composition");                
                if(rs.getLong("regimen_id") == regimenId2) composition2 = rs.getString("composition");                
            }
            if(!composition1.equals(composition2)) {
                changed = true;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }          
        return changed;
    }

    //This method returns the details of the first time this regimen was dispensed to the patient
    /*public static RegimenHistory getRegimenHistory(long id, long regimentypeId, long id) {
       String regimenType = RegimenJDBC.getRegimenType(regimentypeId);
        String regimen = RegimenJDBC.getRegimen(id);
        return getRegimenHistory(id, regimenType, regimen);
    }
    */
    /*public static Regimenhistory getRegimenHistory(long id, String regimenType, String regimen) {
        Regimenhistory regimenhistory = new Regimenhistory();
        try {
            String query = "SELECT * FROM regimenhistory WHERE patient_id = " + id + " AND regimenType = '" + regimenType + "' AND regimen = '" + regimen + "' ORDER BY date_visit ASC LIMIT 1";
            jdbcUtil = new JDBCUtil();
            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {              
                regimenhistory.setDateVisit(rs.getDate("date_visit"));
                regimenhistory.setReasonSwitchedSubs(rs.getString("reason_switched_subs"));
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }          
        return regimenhistory;    
    }*/
        
    public static Date getDateRegimenEnded(long patientId, long regimenId) {
        Date dateRegimenEnded = null;
        try {
            String query = "SELECT date_visit, duration FROM pharmacy WHERE patient_id = " + patientId + " AND regimen_id = " + regimenId + " ORDER BY date_visit DESC LIMIT 1";
            jdbcUtil = new JDBCUtil();
            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                dateRegimenEnded = DateUtil.addDay(rs.getDate("date_visit"), (int) rs.getDouble("duration"));
            }  //Math.round(rs.getDouble("duration"));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }          
        return dateRegimenEnded;        
    } 

    public static Date getDateRegimenStarted(long patientId, long regimenId) {
        Date dateRegimenEnded = null;
        try {
            String query = "SELECT date_visit, duration FROM pharmacy WHERE patient_id = " + patientId + " AND regimen_id = " + regimenId + " ORDER BY date_visit ASC LIMIT 1";
            jdbcUtil = new JDBCUtil();
            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                dateRegimenEnded = rs.getDate("date_visit");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }          
        return dateRegimenEnded;        
    } 

    public static String resolveRegimen(String regimensys) {       
        String regimen = "";
        try {
            String query = "SELECT regimen FROM regimenresolver WHERE regimensys = '" + regimensys + "'";
            jdbcUtil = new JDBCUtil();
            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                regimen = rs.getString("regimen");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }          
        return regimen;
    }
    
    public static boolean isARV(String id) {
        if(id.equals("1") || id.equals("2") || id.equals("3") || id.equals("4") || id.equals("14")) return true;
        return false;
    }

   public static boolean isARV(int id) {
        if((id >=1 && id <= 4) || id == 14) return true;
        return false;
    }

    private static ResultSet executeQuery(String query) {
       ResultSet resultSet = null;
       try {
            jdbcUtil = new JDBCUtil();
            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
       }
       catch (Exception exception) {
            exception.printStackTrace();
       }          
       return resultSet;
   }   

}
