/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility.builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.Scrambler;

/**
 *
 * @author user10
 */
public class EacListBuilder {
    private HttpServletRequest request;
    private HttpSession session;
    private Boolean viewIdentifier;
    private Scrambler scrambler;

    private ArrayList<Map<String, String>> eacList = new ArrayList<Map<String, String>>();
    
    public EacListBuilder() {
        this.scrambler = new Scrambler();
        /*if(ServletActionContext.getRequest().getSession().getAttribute("viewIdentifier") != null) {
            this.viewIdentifier = (Boolean) session.getAttribute("viewIdentifier");                        
        }*/
    }
    
           
    public void buildEacList(ResultSet resultSet) throws SQLException{
        try {
            // loop through resultSet for each row and put into Map
            resultSet.beforeFirst();
            while (resultSet.next()) { 
                System.out.println("retrieving.......data");
                String patientId = Long.toString(resultSet.getLong("patient_id")); 
                String facilityId = Long.toString(resultSet.getLong("facility_id")); 
                String eacId = Long.toString(resultSet.getLong("eac_id")); 
                String dateEac1 = resultSet.getObject("date_eac1") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_eac1"), "MM/dd/yyyy");
                String dateEac2 = resultSet.getObject("date_eac2") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_eac2"), "MM/dd/yyyy");
                String dateEac3 = resultSet.getObject("date_eac3") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_eac3"), "MM/dd/yyyy");
                String dateSampleCollected = resultSet.getObject("date_sample_collected") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_sample_collected"), "MM/dd/yyyy");
                String notes = resultSet.getObject("notes") == null ? "" : resultSet.getString("notes");
                String lastViralLoad = "";
                String dateLastViralLoad = resultSet.getObject("date_last_viral_load") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_viral_load"), "MM/dd/yyyy");                
                if(!dateLastViralLoad.equalsIgnoreCase("")) {
                    lastViralLoad = resultSet.getObject("last_viral_load") == null ? "" :  resultSet.getDouble("last_viral_load") == 0.0 ? "0" : Double.toString(resultSet.getDouble("last_viral_load")); 
                }

                // create an array from object properties 
                Map<String, String> map = new HashMap<String, String>();
                map.put("eacId", eacId);
                map.put("patientId", patientId);
                map.put("facilityId", facilityId);
                map.put("dateEac1", dateEac1);
                map.put("dateEac2", dateEac2);
                map.put("dateEac3", dateEac3);
                map.put("dateSampleCollected", dateSampleCollected);
                map.put("notes", notes);
                map.put("dateLastViralLoad", dateLastViralLoad); 
                map.put("lastViralLoad", lastViralLoad); 
                eacList.add(map);
            }
            session.setAttribute("eacList", eacList);  
            resultSet = null;
            eacList = null;
        }
        catch (SQLException sqlException) {
            resultSet = null;
            throw sqlException;  
        }            
    }
   
    public ArrayList<Map<String, String>> retrieveEacList() {
        if(session.getAttribute("eacList") != null) {
            eacList = (ArrayList) session.getAttribute("eacList");                        
        }
        return eacList;
    }   

    public void clearEacList() {
        eacList = retrieveEacList();
        eacList.clear();
        session.setAttribute("eacList", eacList); 
    }
    
}
