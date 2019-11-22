/**
 *
 * @author aalozie
 *//*


package org.fhi360.lamis.utility.builder;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.Scrambler;

public class EncounterListBuilder {
    private HttpServletRequest request;
    private HttpSession session;
    private ArrayList<Map<String, String>> encounterList = new ArrayList<Map<String, String>>();
  
    private Boolean viewIdentifier;
    private Scrambler scrambler;
  
    public EncounterListBuilder() {
        this.scrambler = new Scrambler();
        */
/*if(ServletActionContext.getRequest().getSession().getAttribute("viewIdentifier") != null) {
            this.viewIdentifier = (Boolean) session.getAttribute("viewIdentifier");                        
        }*//*

    }
        
    public void buildEncounterList(ResultSet resultSet) throws SQLException{
        try {
            // loop through resultSet for each row and put into Map
            resultSet.beforeFirst();
            while (resultSet.next()) {
                Patient patient = PatientDAO.find(resultSet.getLong("patient_id"));
                String surname = (viewIdentifier)? scrambler.unscrambleCharacters(patient.getSurname()) : patient.getSurname();
                surname = StringUtils.upperCase(surname);                
                String otherNames = (viewIdentifier)? scrambler.unscrambleCharacters(patient.getOtherNames()) : patient.getOtherNames();
                otherNames = StringUtils.capitalize(otherNames);

                Facility facility = FacilityDAO.find(resultSet.getLong("facility_id")); 
                Communitypharm communitypharm = CommunitypharmDAO.find(resultSet.getLong("communitypharm_id"));
                String dateVisit = resultSet.getObject("date_visit") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_visit"), "MM/dd/yyyy");                
                String question1 = resultSet.getObject("question1") == null ? "" : resultSet.getString("question1");
                String question2 = resultSet.getObject("question2") == null ? "" : resultSet.getString("question2");
                String question3 = resultSet.getObject("question3") == null ? "" : resultSet.getString("question3");
                String question4 = resultSet.getObject("question4") == null ? "" : resultSet.getString("question4");
                String question5 = resultSet.getObject("question5") == null ? "" : resultSet.getString("question5");
                String question6 = resultSet.getObject("question6") == null ? "" : resultSet.getString("question6");
                String question7 = resultSet.getObject("question7") == null ? "" : resultSet.getString("question7");
                String regimen1 = resultSet.getObject("regimen1") == null ? "" : resultSet.getString("regimen1");
                String regimen2 = resultSet.getObject("regimen2") == null ? "" : resultSet.getString("regimen2");
                String regimen3 = resultSet.getObject("regimen3") == null ? "" : resultSet.getString("regimen3");
                String regimen4 = resultSet.getObject("regimen4") == null ? "" : resultSet.getString("regimen4");
                String duration1 = resultSet.getObject("duration1") == null ? "" : Integer.toString(resultSet.getInt("duration1"));
                String duration2 = resultSet.getObject("duration2") == null ? "" : Integer.toString(resultSet.getInt("duration2"));
                String duration3 = resultSet.getObject("duration3") == null ? "" : Integer.toString(resultSet.getInt("duration3"));
                String duration4 = resultSet.getObject("duration4") == null ? "" : Integer.toString(resultSet.getInt("duration4"));
                String prescribed1 = resultSet.getObject("prescribed1") == null ? "" : Integer.toString(resultSet.getInt("prescribed1"));
                String prescribed2 = resultSet.getObject("prescribed2") == null ? "" : Integer.toString(resultSet.getInt("prescribed2"));
                String prescribed3 = resultSet.getObject("prescribed3") == null ? "" : Integer.toString(resultSet.getInt("prescribed3"));
                String prescribed4 = resultSet.getObject("prescribed4") == null ? "" : Integer.toString(resultSet.getInt("prescribed4"));
                String dispensed1 = resultSet.getObject("dispensed1") == null ? "" : Integer.toString(resultSet.getInt("dispensed1"));
                String dispensed2 = resultSet.getObject("dispensed2") == null ? "" : Integer.toString(resultSet.getInt("dispensed2"));
                String dispensed3 = resultSet.getObject("dispensed3") == null ? "" : Integer.toString(resultSet.getInt("dispensed3"));
                String dispensed4 = resultSet.getObject("dispensed4") == null ? "" : Integer.toString(resultSet.getInt("dispensed4"));
                String nextRefill = resultSet.getObject("next_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("next_refill"), "MM/dd/yyyy");                
                String nextAppointment = resultSet.getObject("next_appointment") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("next_appointment"), "MM/dd/yyyy");                
                
                System.out.println("Surname:"+surname);
                Map<String, String> map = new HashMap<String, String>();                
                map.put("facilityName", facility.getName());
                map.put("hospitalNum", patient.getHospitalNum());
                map.put("name", surname + " " + otherNames);
                map.put("pharmacy", communitypharm.getPharmacy());
                map.put("dateVisit", dateVisit);
                map.put("question1", question1);
                map.put("question2", question2);
                map.put("question3", question3);
                map.put("question4", question4);
                map.put("question5", question5);
                map.put("question6", question6);
                map.put("question7", question7);
                map.put("regimen1", regimen1);
                map.put("regimen2", regimen2);
                map.put("regimen3", regimen3);
                map.put("regimen4", regimen4);
                map.put("duration1", duration1);
                map.put("duration2", duration2);
                map.put("duration3", duration3);
                map.put("duration4", duration4);
                map.put("prescribed1", prescribed1);
                map.put("prescribed2", prescribed2);
                map.put("prescribed3", prescribed3);
                map.put("prescribed4", prescribed4);
                map.put("dispensed1", dispensed1);
                map.put("dispensed2", dispensed2);
                map.put("dispensed3", dispensed3);
                map.put("dispensed4", dispensed4);
                map.put("next_refill", nextRefill);
                map.put("next_appointment", nextAppointment);
                encounterList.add(map);
            }            
            session.setAttribute("encounterList", encounterList);   
            resultSet = null;
            encounterList = null;
        }
        catch (SQLException sqlException) {
            resultSet = null;
            throw sqlException;  
        }
    }

    public ArrayList<Map<String, String>> retrieveEncounterList() {
        // retrieve record store in session attribute
        if(session.getAttribute("encounterList") != null) {
            encounterList = (ArrayList) session.getAttribute("encounterList");                        
        }
        return encounterList;
    }            
    
}
*/
