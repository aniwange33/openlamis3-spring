/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility.builder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.Scrambler;

/**
 *
 * @author user10
 */
public class ValidationListBuilder {
    
    private HttpServletRequest request;
    private HttpSession session;
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private ArrayList<Map<String, String>> validationList = new ArrayList<Map<String, String>>();
    private ArrayList<Map<String, String>> entityList = new ArrayList<Map<String, String>>();
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet rs;
    
    public ValidationListBuilder() {
    }
        
    public void buildValidationList(ResultSet resultSet) throws SQLException{
        try {
            // loop through resultSet for each row and put into Map
            resultSet.beforeFirst();
            while (resultSet.next()) {
                String id = Integer.toString(resultSet.getInt("id"));
                String entity = resultSet.getString("entity"); 
                String entity_count = Long.toString(resultSet.getLong("entity_count"));
                
                Map<String, String> map = new HashMap<String, String>();   
                map.put("id", id);
                map.put("entity", entity);
                map.put("entityCount", entity_count);
                validationList.add(map);
            }            
            session.setAttribute("validationList", validationList);   
            resultSet = null;
            validationList = null;
        }
        catch (SQLException sqlException) {
            resultSet = null;
            throw sqlException;  
        }
    }

    public ArrayList<Map<String, String>> retrieveValidationList() {
        // retrieve the status record store in session attribute
        if(session.getAttribute("validationList") != null) {
            validationList = (ArrayList) session.getAttribute("validationList");                        
        }
        return validationList;
    }        


    public void buildEntityList(ResultSet resultSet) throws SQLException{
        try {
            // loop through resultSet for each row and put into Map
            
            //Needed
            long facilityId = (Long) session.getAttribute("id");
            int entity_id = (int)session.getAttribute("entity_id");

            resultSet.beforeFirst();
            int i = 1;
            while (resultSet.next()) { 
                String dateRegistration = (resultSet.getDate("report_date") == null)? "" : dateFormat.format(resultSet.getDate("report_date"));
                String statusRegistration = (resultSet.getString("current_status") == null)? "" : resultSet.getString("current_status");
                String surname = (resultSet.getString("surname") == null)? "" : resultSet.getString("surname");
                String otherNames = (resultSet.getString("other_names") == null)? "" : resultSet.getString("other_names");
                String hospitalNum = (resultSet.getString("hospital_num") == null)? "" : resultSet.getString("hospital_num");
                String patientId = (resultSet.getString("patient_id") == null)? "" : resultSet.getString("patient_id");
                String fullName = (resultSet.getString("fullname") == null)? "" : resultSet.getString("fullname");
                
                //get the data from the validated table and mark as validated or not...
                DateFormat sDF = new SimpleDateFormat("dd/MM/yyyy");
                String record_id = hospitalNum+"#"+sDF.format(resultSet.getDate("report_date"));
               // System.out.println(id+record_id+id);
                String query = "SELECT patient_id FROM validated WHERE facility_id = '"+facilityId+"' AND patient_id = '"+patientId+"' AND record_id = '"+record_id+"'";
                if(entity_id == 1) query += "AND table_validated = 'patient'";
                else if(entity_id == 2) query += "AND table_validated = 'clinic'";
                else if(entity_id == 3) query += "AND table_validated = 'pharmacy'";
                else if(entity_id == 4) query += "AND table_validated = 'laboratory'";
                
                try{
                    jdbcUtil = new JDBCUtil();
                    preparedStatement = jdbcUtil.getStatement(query);
                    rs = preparedStatement.executeQuery();                   
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
                Map<String, String> map = new HashMap<String, String>();    
                String name = new Scrambler().unscrambleCharacters(surname).toUpperCase()+" "+new Scrambler().unscrambleCharacters(otherNames).toUpperCase();
                map.put("sn", ""+i);
                map.put("name", name);
                map.put("dateRegistration", dateRegistration);
                map.put("statusRegistration", statusRegistration);
                map.put("hospitalNum", hospitalNum); 
                map.put("patientId", patientId); 
                map.put("fullName", fullName); 
                if(rs.next()){   
                    map.put("validated", "true");
                }else{
                    //System.out.println("false");
                    map.put("validated", "false");
                }
                entityList.add(map);
                
                i++;
            }            
            session.setAttribute("entityList", entityList);   
            resultSet = null;
            entityList = null;
        }
        catch (SQLException sqlException) {
            resultSet = null;
            throw sqlException;  
        }
    }    
    
    public ArrayList<Map<String, String>> retrieveEntityList() {
        // retrieve the status record store in session attribute
        if(session.getAttribute("entityList") != null) {
            entityList = (ArrayList) session.getAttribute("entityList");                        
        }
        return entityList;
    }
    
}
