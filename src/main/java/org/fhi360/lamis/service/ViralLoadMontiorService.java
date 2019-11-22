/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.StringUtil;
import org.springframework.stereotype.Component;

/**
 *
 * @author user10
 */
@Component
public class ViralLoadMontiorService {
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    

    //Update Viral Load Due.
    public void updateViralLoadDue(long patientId){
        try {
            query = "SELECT date_started FROM patient WHERE patient_id = " + patientId + " AND date_started IS NOT NULL";                
            resultSet = executeQuery(query);                
            if(resultSet.next()) {
                updateViralLoadDue(patientId, DateUtil.parseDateToString(resultSet.getDate("date_started"), "yyyy-MM-dd"));
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            jdbcUtil.disconnectFromDatabase(); 
        }
    }
    
    public void updateViralLoadDue(long patientId, String dateStarted) {
        ArrayList<String> viralLoads;
        ArrayList<String> viralLoadDates;
        String viralLoadType;
        String updateQuery;
        try {
            viralLoads = new ArrayList<>();
            viralLoadDates = new ArrayList<>();
            viralLoadType = Constants.TypeVL.VL_BASELINE;
            updateQuery = "UPDATE patient SET viral_load_due_date = DATEADD('MONTH', 6, '" + dateStarted + "'), viral_load_type = '" + viralLoadType + "', time_stamp = NOW() WHERE patient_id = " + patientId;

            query = "SELECT resultab, resultpc, date_reported FROM laboratory WHERE patient_id = " + patientId + " AND labtest_id = 16 AND DATEDIFF(MONTH, '" + dateStarted + "', date_reported) >= 6 ORDER BY date_reported DESC";
            resultSet = executeQuery(query);                
            //int i = 0;
            while(resultSet.next()){
                //i++;
                //System.out.println("The Record Count is: "+i);
                String resultab = resultSet.getString("resultab");
                String resultpc = resultSet.getString("resultpc");
                String vlDate = (resultSet.getDate("date_reported") == null)? "" : DateUtil.parseDateToString(resultSet.getDate("date_reported"), "yyyy-MM-dd");

                if(resultab != null && resultab != "") {
                    if(!StringUtil.isInteger(resultab)) resultab = "0";
                    viralLoads.add(resultab);
                    viralLoadDates.add(vlDate);
                } 
                else {
                    if(resultpc != null && resultpc != "") {
                        if(!StringUtil.isInteger(resultpc)) resultpc = "0";
                        viralLoads.add(resultpc);
                        viralLoadDates.add(vlDate);
                    }
                }

            }
            Integer vlSize = viralLoads.size();

            if(vlSize == 1) {
                //This is the baseline...set the date to 6 months or three months  
                if(Double.parseDouble(viralLoads.get(0)) < 1000){
                    viralLoadType = Constants.TypeVL.VL_SECOND;
                    updateQuery = "UPDATE patient SET viral_load_due_date = DATEADD('MONTH', 6, '" + viralLoadDates.get(0) + "'), viral_load_type = '" + viralLoadType + "', time_stamp = NOW() WHERE patient_id = " + patientId;
                } 
                else {
                    viralLoadType = Constants.TypeVL.VL_REPEAT;
                    updateQuery = "UPDATE patient SET viral_load_due_date = DATEADD('MONTH', 3, '" + viralLoadDates.get(0) + "'), viral_load_type = '" + viralLoadType + "', time_stamp = NOW() WHERE patient_id = " + patientId;
                } 
            }
            else if(vlSize == 2){  
                if(Double.parseDouble(viralLoads.get(0)) < 1000) {
                    viralLoadType = Constants.TypeVL.VL_ROUTINE;
                    updateQuery = "UPDATE patient SET viral_load_due_date = DATEADD('MONTH', 12, '" + viralLoadDates.get(0) + "'), viral_load_type = '" + viralLoadType + "', time_stamp = NOW() WHERE patient_id = " + patientId;
                } 
                else {
                    viralLoadType = Constants.TypeVL.VL_REPEAT;
                    updateQuery = "UPDATE patient SET viral_load_due_date = DATEADD('MONTH', 3, '" + viralLoadDates.get(0) + "'), viral_load_type = '" + viralLoadType + "', time_stamp = NOW() WHERE patient_id = " + patientId;
                }                                                 
            }
            else if(vlSize > 2){

                if(Double.parseDouble(viralLoads.get(0)) < 1000) {
                    viralLoadType = Constants.TypeVL.VL_ROUTINE;
                    updateQuery = "UPDATE patient SET viral_load_due_date = DATEADD('MONTH', 12, '" + viralLoadDates.get(0) + "'), viral_load_type = '" + viralLoadType + "', time_stamp = NOW() WHERE patient_id = " + patientId;
                }
                else {
                    viralLoadType = Constants.TypeVL.VL_REPEAT;
                    updateQuery = "UPDATE patient SET viral_load_due_date = DATEADD('MONTH', 3, '" + viralLoadDates.get(0) + "'), viral_load_type = '" + viralLoadType + "', time_stamp = NOW() WHERE patient_id = " + patientId;
                }
            }
            executeUpdate(updateQuery);
        }
        catch(Exception ex){
             jdbcUtil.disconnectFromDatabase();
            ex.printStackTrace();
        }
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
    
    private ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try {
            jdbcUtil = new JDBCUtil();  
            preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        } 
        return rs;
    }            

    private boolean isInteger(String s) {
        int radix = 10;
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }    
    
}
