/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*

package org.fhi360.lamis.utility.builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.Scrambler;

*/
/**
 *
 * @author user10
 *//*

public class MotherInformationListBuilder {
    
     private HttpServletRequest request;
    private HttpSession session;
    private Boolean viewIdentifier;
    private Scrambler scrambler;

    private ArrayList<Map<String, String>> motherList = new ArrayList<>();
    private final Map<String, Map<String, String>> sortedMaps = new TreeMap<>();
    
    public MotherInformationListBuilder() {
        this.request = ServletActionContext.getRequest();
        this.session = ServletActionContext.getRequest().getSession();
        this.scrambler = new Scrambler();
        if(ServletActionContext.getRequest().getSession().getAttribute("viewIdentifier") != null) {
            this.viewIdentifier = (Boolean) session.getAttribute("viewIdentifier");                        
        }
    }
    
    public void buildMotherListSorted(ResultSet resultSet) throws SQLException {
        try {
            // loop through resultSet for each row and put into Map
            resultSet.beforeFirst();
            while (resultSet.next()) {
                String motherId = resultSet.getObject("motherinformation_id") != null ? 
                        Long.toString(resultSet.getLong("motherinformation_id")) : "N/A"; 
                String id = resultSet.getObject("patient_id") != null ?
                        Long.toString(resultSet.getLong("patient_id")) : "N/A"; 
                String id = Long.toString(resultSet.getLong("facility_id"));
                String hospitalNum = resultSet.getString("hospital_num") == null ? "N/A" : 
                        resultSet.getString("hospital_num");
                String uniqueId = resultSet.getObject("unique_id") == null ? "N/A" : resultSet.getString("unique_id");
                String surname = resultSet.getObject("surname") == null ? "" : resultSet.getString("surname");
                surname = (viewIdentifier)? scrambler.unscrambleCharacters(surname) : surname;
                surname = StringUtils.upperCase(surname);              
                String artStatus = resultSet.getObject("art_status") == null ? "N/A" : resultSet.getString("art_status");               
                String otherNames = resultSet.getObject("other_names") == null ? "" : resultSet.getString("other_names");
                String address = resultSet.getObject("address") == null ? "" : resultSet.getString("address");
                address = (viewIdentifier)? scrambler.unscrambleCharacters(address) : address;
                String inFacility = resultSet.getObject("in_facility") == null ? "" : resultSet.getString("in_facility");          
                otherNames = (viewIdentifier)? scrambler.unscrambleCharacters(otherNames) : otherNames;
                otherNames = StringUtils.capitalize(otherNames);
                String timeHivDiagnosis = resultSet.getObject("time_hiv_diagnosis") == null ? "" : resultSet.getString("time_hiv_diagnosis");
                String dateConfirmedHiv = resultSet.getObject("date_confirmed_hiv") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_confirmed_hiv"), "MM/dd/yyyy");
                String dateStarted = resultSet.getObject("date_started") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_started"), "MM/dd/yyyy");                
                String dateEnrolledPmtct = resultSet.getObject("date_enrolled_pmtct") == null ? "N/A" : DateUtil.parseDateToString(resultSet.getDate("date_enrolled_pmtct"), "MM/dd/yyyy"); 
                String timeStamp = resultSet.getObject("time_stamp") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("time_stamp"), "MM/dd/yyyy");                

                // create an array from object properties 
                Map<String, String> map = new HashMap<>();
                map.put("motherId", motherId);
                map.put("id", id);
                map.put("id", id);
                map.put("hospitalNum", hospitalNum);
                map.put("uniqueId", uniqueId);
                map.put("surname", surname);
                map.put("otherNames", otherNames);
                map.put("currentStatus", artStatus);
                map.put("inFacility", inFacility);
                map.put("address", address);
                map.put("name", surname + ' ' + otherNames);
                map.put("dateConfirmedHiv", dateConfirmedHiv);
                map.put("dateEnrolledPmtct", dateEnrolledPmtct); 
                map.put("timeHivDiagnosis", timeHivDiagnosis);
                map.put("dateStarted", dateStarted);
                map.put("timeStamp", timeStamp);  
                
                //Check if this patient ARV refill has been devolved to a community pharmacy
                boolean dueViralLoad = new PatientJDBC().dueViralLoad(resultSet.getLong("patient_id"));        
                map.put("dueViralLoad", dueViralLoad? "1" : "0"); 
                map.put("sel", "0");
                sortedMaps.put(surname + otherNames, map);                 
           } 
            sortedMaps.entrySet().forEach((entry) -> {
                motherList.add(entry.getValue());
            });
            resultSet = null;
            session.setAttribute("motherList", motherList); 
        }
        catch (Exception ex) {
            ex.printStackTrace();
            resultSet = null;
        }
    }
    
    public ArrayList<Map<String, String>> retrieveMotherList() {
        // retrieve the patient record store in session attribute
        if(session.getAttribute("motherList") != null) {
            motherList = (ArrayList) session.getAttribute("motherList");                        
        }
        return motherList;
    }
    
    public void clearMotherList() {
        motherList = retrieveMotherList();
        motherList.clear();
        session.setAttribute("motherList", motherList); 
    }
}
*/
