/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*

package org.fhi360.lamis.utility;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;

*/
/**
 *
 * @author user1
 *//*

public class LabNumberNormalizer {
    private static String query;
    private static JDBCUtil jdbcUtil;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    
    public static String getLabno() {
        long id = 0;//(Long) ServletActionContext.getRequest().getSession().getAttribute("id");
        String labno = "";
        String zeros = "";
        String lastno = "";
        String year = "";
        int MAX_LENGTH = 5;
        try {
            jdbcUtil = new JDBCUtil();  
            query = "SELECT * FROM labno WHERE facility_id = " + id + " AND year = YEAR(CURDATE())";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                lastno = Integer.toString(resultSet.getInt("lastno") + 1);
                year = Integer.toString(resultSet.getInt("year"));
                if(lastno.length() < MAX_LENGTH) {
                    for(int i = 0; i < MAX_LENGTH-lastno.length(); i++) {
                        zeros = zeros + "0";  
                    }
                }
                labno = zeros+lastno+"/"+year.substring(2, 4);
            }
            else {
                query = "INSERT INTO labno(facility_id, year, lastno, time_stamp) VALUES(" + id + ", YEAR(CURDATE()), 0, NOW())";
                preparedStatement = jdbcUtil.getStatement(query);
                preparedStatement.executeUpdate(); 
                year = Integer.toString(new GregorianCalendar().get(Calendar.YEAR));
                labno = "00001/"+year.substring(2, 4);
            }
            resultSet = null;
        }
        catch (Exception exception) {
            resultSet = null;                        
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }                  
        return labno;
    }
   
    public static void updateLabno() {
        long id = (Long) ServletActionContext.getRequest().getSession().getAttribute("id");
        try {
            jdbcUtil = new JDBCUtil();  
            query = "UPDATE labno SET lastno = lastno+1, time_stamp = NOW() WHERE facility_id = " + id + " AND year = YEAR(CURDATE())";
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();                
        }
        catch (Exception exception) {
            resultSet = null;                        
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }                         
    }

    public static void updateLabno(long id, int year, int lastno) {
        try {
            jdbcUtil = new JDBCUtil(); 
            query = "SELECT * FROM labno WHERE facility_id = " + id + " AND year = " + year;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                query = "UPDATE labno SET lastno = " + lastno + ", time_stamp = NOW() WHERE facility_id = " + id + " AND year = " + year;
            }
            else {
                query = "INSERT INTO labno(facility_id, year, lastno, time_stamp) VALUES(" + id + ", " + year + "," + lastno + ", NOW())";
            }
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();                
        }
        catch (Exception exception) {
            resultSet = null;                        
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }                         
    }
    
    public static String normalize(String labno) {
        labno = StringUtils.trimToEmpty(labno);
        String zeros = "";
        int MAX_LENGTH = 8;
        if(labno.length() < MAX_LENGTH) {
            for(int i = 0; i < MAX_LENGTH-labno.length(); i++) {
                zeros = zeros + "0";  
            }
        }
        labno = labno.replace("O", "0");
        labno = labno.replace("o", "0");
        labno = labno.replace("\\", "/");
        labno = labno.replace("-", "/");
        labno = labno.replace("_", "/");
        return zeros+labno;
    }

}
*/
