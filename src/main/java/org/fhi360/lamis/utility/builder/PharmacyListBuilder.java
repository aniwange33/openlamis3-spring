/**
 *
 * @author aalozie
 */

package org.fhi360.lamis.utility.builder;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.fhi360.lamis.utility.DateUtil;

public class PharmacyListBuilder {
    private HttpServletRequest request;
    private HttpSession session;

    private ArrayList<Map<String, String>> pharmacyList = new ArrayList<Map<String, String>>();
    private ArrayList<Map<String, String>> dispenserList = new ArrayList<Map<String, String>>();
	private ArrayList<Map<String, String>> regimenList = new ArrayList<Map<String, String>>();
    private ArrayList<Map<String, String>> prescribedList = new ArrayList<Map<String, String>>();																						  
    
    public PharmacyListBuilder() {
    }
    
    public void buildPharmacyList(ResultSet resultSet) throws SQLException{ 
        String hospitalNum = "";
        if(request.getParameterMap().containsKey("hospitalNum")) {
            hospitalNum = request.getParameter("hospitalNum");             
        }
        String dateLastRefill = "";
        if(request.getParameterMap().containsKey("dateLastRefill")) {
            dateLastRefill = request.getParameter("dateLastRefill"); 
        }
        try {
            // loop through resultSet for each row and put into Map
            resultSet.beforeFirst();
            while (resultSet.next()) { 
                String pharmacyId = Long.toString(resultSet.getLong("pharmacy_id")); 
                String patientId = Long.toString(resultSet.getLong("patient_id")); 
                String facilityId = Long.toString(resultSet.getLong("facility_id")); 
                String dateVisit = resultSet.getObject("date_visit") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_visit"), "MM/dd/yyyy");
                String regimentypeId = Long.toString(resultSet.getLong("regimentype_id"));                
                String regimendrugId = Long.toString(resultSet.getLong("regimendrug_id"));
                String regimenId = Long.toString(resultSet.getLong("regimen_id"));
                String dmocType = resultSet.getString("dmoc_type");
                String drugId = Long.toString(resultSet.getLong("drug_id"));
                String description = resultSet.getString("name")+ " "+ resultSet.getString("strength");
                String morning = Double.toString(resultSet.getDouble("morning"));                 
                String afternoon = Double.toString(resultSet.getDouble("afternoon"));                 
                String evening = Double.toString(resultSet.getDouble("evening")); 
                String duration = Integer.toString(resultSet.getInt("duration"));                               
                String quantity = Double.toString((resultSet.getDouble("morning")+resultSet.getDouble("afternoon")+resultSet.getDouble("evening"))*resultSet.getInt("duration"));
                String adrScreened = resultSet.getString("adr_screened") == null ? "" : resultSet.getString("adr_screened");                 
                String adrIds = resultSet.getString("adr_ids") == null ? "" : resultSet.getString("adr_ids");                 
                String prescripError = Integer.toString(resultSet.getInt("prescrip_error"));                 
                String adherence = Integer.toString(resultSet.getInt("adherence"));                 
                String nextAppointment = resultSet.getDate("next_appointment") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("next_appointment"), "MM/dd/yyyy");
                
                Map<String, String> map = new HashMap<String, String>();                
                map.put("pharmacyId", pharmacyId);
                map.put("patientId", patientId);
                map.put("hospitalNum", hospitalNum);
                map.put("facilityId", facilityId);
                map.put("dateLastRefill", dateLastRefill);
                map.put("dateVisit", dateVisit);
                map.put("duration", duration);
                map.put("adrScreened", adrScreened);
                map.put("adrIds", adrIds);
                map.put("dmocType", dmocType);
                map.put("prescripError", prescripError);
                map.put("adherence", adherence);
                map.put("nextAppointment", nextAppointment);                
                map.put("regimentypeId", regimentypeId);
                map.put("regimenId", regimenId);
                pharmacyList.add(map);

                Map<String, String> map1 = new HashMap<>();
                map1.put("regimentypeId", regimentypeId);
                map1.put("regimendrugId", regimendrugId);
                map1.put("regimenId", regimenId);
                map1.put("drugId", drugId);
                map1.put("description", description);
                map1.put("morning", morning);                
                map1.put("afternoon", afternoon);                
                map1.put("evening", evening);                
                map1.put("duration", duration);                
                map1.put("quantity", quantity);                
                dispenserList.add(map1);                                
            }            
            session.setAttribute("pharmacyList", pharmacyList);   
            session.setAttribute("dispenserList", dispenserList);   
			session.setAttribute("fromPrescription", "false");												  
            resultSet = null;
            pharmacyList = null;
            dispenserList = null;
        }
        catch (SQLException sqlException) {
            resultSet = null;
            throw sqlException;  
        }
    }

    public void buildDispenserList(ResultSet resultSet) throws SQLException{
        try {
            // loop through resultSet for each row and put into Map
            resultSet.beforeFirst();
            while (resultSet.next()) {
                String regimentypeId = Long.toString(resultSet.getLong("regimentype_id")); 
                String regimendrugId = Long.toString(resultSet.getLong("regimendrug_id")); 
                String regimenId = Long.toString(resultSet.getLong("regimen_id")); 
                String drugId = Long.toString(resultSet.getLong("drug_id")); 
                String description = resultSet.getString("name")+ " "+ resultSet.getString("strength");
                String morning = Double.toString(resultSet.getDouble("morning"));                 
                String afternoon = Double.toString(resultSet.getDouble("afternoon"));                 
                String evening = Double.toString(resultSet.getDouble("evening"));                 
                String duration = "0";                 
                String quantity = "0.0";                 

                Map<String, String> map = new HashMap<String, String>();                
                map.put("regimentypeId", regimentypeId);
                map.put("regimendrugId", regimendrugId);
                map.put("regimenId", regimenId);
                map.put("drugId", drugId);
                map.put("description", description);
                map.put("morning", morning);                
                map.put("afternoon", afternoon);                
                map.put("evening", evening);                
                map.put("duration", duration);                
                map.put("quantity", quantity);                
                dispenserList.add(map);
            }                    
            session.setAttribute("dispenserList", dispenserList);
            resultSet = null;
            dispenserList = null;
        }
        catch (SQLException sqlException) {
            resultSet = null;
            throw sqlException;  
        }
    }
    
	public void buildRegimenList(ResultSet resultSet, String selectedRegimen) throws Exception{
        try {
            // loop through resultSet for each row and put into Map
            resultSet.beforeFirst();
            
            while (resultSet.next()) {
                String regimentypeId = Long.toString(resultSet.getLong("regimentype_id"));  
                String regimenId = Long.toString(resultSet.getLong("regimen_id"));
                String description = resultSet.getString("description");

                Map<String, String> map = new HashMap<String, String>();                
                map.put("regimentypeId", regimentypeId);
                map.put("regimenId", regimenId);
                map.put("description", description);  
                map.put("selectedRegimen", selectedRegimen);  
                regimenList.add(map);           
            }                    
            session.setAttribute("regimenList", regimenList);
            resultSet = null;
            regimenList = null;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            resultSet = null;
            throw ex;  
        }
    }
    
   
    
    public ArrayList<Map<String, String>> retrievePharmacyList() {
        // retrieve the pharmacy record store in session attribute
        if(session.getAttribute("pharmacyList") != null) {
            pharmacyList = (ArrayList) session.getAttribute("pharmacyList");                        
        }
        return pharmacyList;
    }        

    public ArrayList<Map<String, String>> retrieveDispenserList() {
        // retrieve the pharmacy record store in session attribute
        if(session.getAttribute("dispenserList") != null) {
            dispenserList = (ArrayList) session.getAttribute("dispenserList");                        
        }
        return dispenserList;
    }            
    public ArrayList<Map<String, String>> retrievePrescribedList() {
        // retrieve the pharmacy record store in session attribute
        if(session.getAttribute("prescribedList") != null) {
            prescribedList = (ArrayList) session.getAttribute("prescribedList");                        
        }
        return prescribedList;
    }
    
    public ArrayList<Map<String, String>> retrieveRegimenList() {
        // retrieve the pharmacy record store in session attribute
        if(session.getAttribute("regimenList") != null) {
            regimenList = (ArrayList) session.getAttribute("regimenList");                        
        }
        return regimenList;
    }
    

    public void clearPharmacyList() {
        pharmacyList = retrievePharmacyList();
        pharmacyList.clear();
        session.setAttribute("pharmacyList", pharmacyList); 
        dispenserList = retrieveDispenserList();
        dispenserList.clear();
        session.setAttribute("dispenserList", dispenserList); 
		regimenList = retrieveRegimenList();
        regimenList.clear();
        session.setAttribute("regimenList", regimenList); 
        prescribedList = retrievePrescribedList();
        prescribedList.clear();
        session.setAttribute("prescribedList", prescribedList);	
    } 
    
}
