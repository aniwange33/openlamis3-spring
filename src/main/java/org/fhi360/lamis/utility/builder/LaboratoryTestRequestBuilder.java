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
public class LaboratoryTestRequestBuilder {
    
    private HttpServletRequest request;
    private HttpSession session;
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private ArrayList<Map<String, String>> labRequestList = new ArrayList<Map<String, String>>();
    private ArrayList<Map<String, String>> labRequestEditList = new ArrayList<Map<String, String>>();
    private ResultSet rs;
    
    public LaboratoryTestRequestBuilder() {

    }
    
    
    public void buildLabRequestList(ResultSet resultSet) throws SQLException{
        try {
            // loop through resultSet for each row and put into Map

            resultSet.beforeFirst();
            int i = 1;
            while (resultSet.next()) { 
                String dateColelcted = (resultSet.getDate("date_collected") == null)? "" : dateFormat.format(resultSet.getDate("date_collected"));
                String dateSent = (resultSet.getDate("date_sent") == null)? "" : dateFormat.format(resultSet.getDate("date_sent"));
                String sampleNature = (resultSet.getString("specimen_type") == null)? "" : resultSet.getString("specimen_type");
                String testOrdered = (resultSet.getString("test_ordered") == null)? "" : resultSet.getString("test_ordered");           
                
                Map<String, String> map = new HashMap<String, String>();    
                map.put("sn", ""+i);
                map.put("dateColelcted", dateColelcted);
                map.put("dateSent", dateSent);
                map.put("sampleNature", sampleNature); 
                map.put("testOrdered", testOrdered);
                
                labRequestList.add(map);
                
                i++;
            }            
            session.setAttribute("labRequestList", labRequestList);   
            resultSet = null;
            labRequestList = null;
        }
        catch (SQLException sqlException) {
            resultSet = null;
            throw sqlException;  
        }
    }
    
     public ArrayList<Map<String, String>> retrieveLabRequestList() {
        // retrieve the status record store in session attribute
        if(session.getAttribute("labRequestList") != null) {
            labRequestList = (ArrayList) session.getAttribute("labRequestList");                        
        }
        return labRequestList;
    }
     
      public void buildLabRequestEditList(ResultSet resultSet) throws SQLException{
        try {
            // loop through resultSet for each row and put into Map

            resultSet.beforeFirst();
            int i = 1;
            while (resultSet.next()) { 
                String labtestId = Long.toString(resultSet.getLong("labtest_id")) == null ? "" : Long.toString(resultSet.getLong("labtest_id")); 
                String description = resultSet.getString("description") == null ? "" : resultSet.getString("description");          
                
                Map<String, String> map = new HashMap<String, String>();    
                map.put("sn", ""+i);
                map.put("labtestId", labtestId);
                map.put("description", description);;
                
                labRequestEditList.add(map);
                
                i++;
            }            
            session.setAttribute("labRequestEditList", labRequestEditList);   
            resultSet = null;
            labRequestEditList = null;
        }
        catch (SQLException sqlException) {
            resultSet = null;
            throw sqlException;  
        }
    }
    
     public ArrayList<Map<String, String>> retrieveLabRequestEditList() {
        // retrieve the status record store in session attribute
        if(session.getAttribute("labRequestEditList") != null) {
            labRequestEditList = (ArrayList) session.getAttribute("labRequestEditList");                        
        }
        return labRequestEditList;
    }
}
