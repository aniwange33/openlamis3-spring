/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;

/**
 *
 * @author user10
 */
public class DatabaseHandler {

    public DatabaseHandler() {
    }
    
    public static void executeUpdate(String query) {
        try {
            JDBCUtil jdbcUtil1 = new JDBCUtil();
            Statement st = jdbcUtil1.getStatement();       
            st.executeUpdate(query);
            jdbcUtil1.disconnectFromDatabase();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }        
    }
    
    public static Long getFacilityUploadForToday(long facilityId, Date dateUpload) {
        Long retrievedFacility = 0L;
        ResultSet rs;
        String query1 = "SELECT facility_id FROM synchistory WHERE facility_id = " + facilityId +" AND upload_date = '" +dateUpload+"'";
        try {
            JDBCUtil jdbcUtil1 = new JDBCUtil();
            Statement st = jdbcUtil1.getStatement();
            rs = st.executeQuery(query1);
            if(rs.next()) {
                 retrievedFacility =  rs.getLong("facility_id"); 
            }         
            jdbcUtil1.disconnectFromDatabase();
        }
        catch (Exception exception) {
           exception.printStackTrace(); //disconnect from database
        }
        return retrievedFacility;
    }
    
    public static String getFilesUploadedToday(long facilityId, Date dateUpload) {
        String retrievedFiles = "";
        ResultSet rs;
        String query1 = "SELECT tables_uploaded FROM synchistory WHERE facility_id = " + facilityId +" AND upload_date = '" +dateUpload+"'";
        try {
            JDBCUtil jdbcUtil1 = new JDBCUtil();
            Statement st = jdbcUtil1.getStatement();
            rs = st.executeQuery(query1);
            if(rs.next()) {
                 retrievedFiles =  rs.getString("tables_uploaded"); 
            }         
            jdbcUtil1.disconnectFromDatabase();
        }
        catch (Exception exception) {
           exception.printStackTrace(); //disconnect from database
        }
        return retrievedFiles;
    }
    
}
