/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.Scrambler;

import javax.servlet.http.HttpSession;

/**
 *
 * @author user10
 */
public class Deduplicator {
    private String query; 
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private long facilityId;
    private Boolean viewIdentifier;
    private Scrambler scrambler;

    
    private ArrayList<Map<String, String>> duplicateList = new ArrayList<Map<String, String>>(); 

    public Deduplicator() {
        this.scrambler = new Scrambler();
//        if(ServletActionContext.getRequest().getSession().getAttribute("viewIdentifier") != null) {
//            this.viewIdentifier = (Boolean) ServletActionContext.getRequest().getSession().getAttribute("viewIdentifier");
//        }
    }
    
    public void duplicates(HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        removeNullHospitalNum();
        try {
            executeUpdate("DROP TABLE IF EXISTS duplicate"); 
            executeUpdate("CREATE TEMPORARY TABLE duplicate (patient_id bigint, hospital_num varchar(25), unique_id varchar(25), surname varchar(45), other_names varchar(75), gender varchar(7), date_birth date, address varchar(100), current_status varchar(75))"); 
            
            query = "SELECT hospital_num, COUNT(hospital_num) AS num FROM patient WHERE facility_id = " + facilityId + " GROUP BY hospital_num";
            resultSet = executeQuery(query);
            while(resultSet.next()) {
                String hospitalNum = resultSet.getString("hospital_num");
                int num = resultSet.getInt("num");
                if(num > 1) {
                   // System.out.println("Duplicate...."+hospitalNum);
                    executeUpdate("INSERT INTO duplicate (patient_id, hospital_num, unique_id, surname, other_names, gender, date_birth, address, current_status) SELECT patient_id, hospital_num, unique_id, surname, other_names, gender, date_birth, address, current_status FROM patient WHERE facility_id = " + facilityId + " AND hospital_num = '" + hospitalNum + "'");
                }
            }
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }
    
     public void updateDuplicates(long patientId, String hospitalNum, String uniqueId, long facilityId){
        System.out.println("Update begins");
        try{
            String query  = "UPDATE patient SET hospital_num = '" + hospitalNum + "' unique_id = '" + uniqueId + "' WHERE facility_id =  " + facilityId + " AND  patient_id = " + patientId;
            executeUpdate(query);
            System.out.println("Record updated......" + patientId+ " Hospital Number: ....." + hospitalNum);
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  
        }    
        
    }
     
    public void deleteDuplicates(long patientId, long facilityId){
        try{
            String query  = "DELETE FROM patient WHERE facility_id =  " + facilityId + " AND  patient_id = " + patientId;
            executeUpdate(query);
            System.out.println("Record deleted......" + patientId);
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  
        }    
        
    }
    
//    public ArrayList<Map<String, String>> getDuplicates() { 
//        try {
//            query = "SELECT * FROM patient WHERE patient_id IN (SELECT patient_id FROM duplicate) ORDER BY hospital_num";
//            resultSet = executeQuery(query);
//            new PatientListBuilder().buildPatientList(resultSet);        
//            duplicateList = new PatientListBuilder().retrievePatientList(); 
//        }
//        catch (Exception exception) {
//            jdbcUtil.disconnectFromDatabase();  //disconnect from database
//        }
//        return duplicateList;           
//    }

    private void removeNullHospitalNum() {
        System.out.println("Removing null numbers......");
        try {
            resultSet = executeQuery("SELECT patient_id FROM patient WHERE (hospital_num IS NULL OR hospital_num = '') AND facility_id = " + facilityId);
            while(resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                //new DeleteService(jdbcTemplate).deletePatient(id, id);
            }                    
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  
        }          
    }

    public ArrayList<Map<String, String>> getDuplicates() { 
        try {
            resultSet = executeQuery("SELECT * FROM duplicate ORDER BY hospital_num");
            while(resultSet.next()) {
                String patientId = Long.toString(resultSet.getLong("patient_id")); 
                String hospitalNum = resultSet.getString("hospital_num");
                String uniqueId = resultSet.getString("unique_id");
                String surname = resultSet.getObject("surname") == null ? "" : resultSet.getString("surname");
                surname = (viewIdentifier)? scrambler.unscrambleCharacters(surname) : surname;
                surname = StringUtils.upperCase(surname);                
                String otherNames = resultSet.getObject("other_names") == null ? "" : resultSet.getString("other_names");
                otherNames = (viewIdentifier)? scrambler.unscrambleCharacters(otherNames) : otherNames;
                otherNames = StringUtils.capitalize(otherNames);
                String gender = resultSet.getObject("gender") == null ? "" : resultSet.getString("gender");                
                String dateBirth = resultSet.getObject("date_birth") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_birth"), "MM/dd/yyyy");
                String address = resultSet.getObject("address") == null ? "" : resultSet.getString("address");
                address = (viewIdentifier)? scrambler.unscrambleCharacters(address) : address;
                address = StringUtils.capitalize(address);                
                String currentStatus = resultSet.getObject("current_status") == null ? "" : resultSet.getString("current_status");

                // create an array from object properties 
                Map<String, String> map = new TreeMap<String, String>();
                map.put("patientId", patientId);
                map.put("hospitalNum", hospitalNum);
                map.put("uniqueId", uniqueId);
                map.put("surname", surname);
                map.put("otherNames", otherNames);
                map.put("name", surname +' '+ otherNames);
                map.put("gender", gender);
                map.put("dateBirth", dateBirth);
                map.put("address", address);
                map.put("currentStatus", currentStatus);
                
                int count = 0;
                ResultSet rs = executeQuery("SELECT COUNT(DISTINCT date_visit) AS count FROM clinic WHERE patient_id = " + resultSet.getLong("patient_id"));
                if(rs.next()) count = count + rs.getInt("count");
                rs = executeQuery("SELECT COUNT(DISTINCT date_visit) AS count FROM pharmacy WHERE patient_id = " + resultSet.getLong("patient_id"));
                if(rs.next()) count = count + rs.getInt("count");
                rs = executeQuery("SELECT COUNT(DISTINCT date_reported) AS count FROM laboratory WHERE patient_id = " + resultSet.getLong("patient_id"));
                if(rs.next()) count = count + rs.getInt("count");
                map.put("count", Integer.toString(count));
                map.put("sel", "0");
                duplicateList.add(map);                 
            }                    
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }                
        return duplicateList; 
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
    
    public ResultSet executeQuery(String query) {
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

}
