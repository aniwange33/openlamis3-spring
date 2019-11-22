/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.nigqual;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.Scrambler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 *
 * @author user1
 */
@Component
public class NigQualReports {
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Boolean viewIdentifier;
    private Scrambler scrambler;

    private ArrayList<Map<String, Object>> reportList;
    private HashMap parameterMap;
    HttpSession session;
    public NigQualReports() {
        this.scrambler = new Scrambler();

    }   
    
    public ArrayList<Map<String, Object>> nigqualReport(String reviewPeriodId1, String portalId1,  String thermaticArea,long facilityId ){
        long reviewPeriodId = Long.parseLong(reviewPeriodId1);
        long portalId = Long.parseLong(portalId1);
        reportList = new ArrayList<>();
        try {
            jdbcUtil = new JDBCUtil();
            query = "SELECT DISTINCT patient.patient_id, patient.unique_id, patient.hospital_num, patient.surname, patient.other_names, patient.gender, patient.age, patient.age_unit, patient.current_status, patient.date_registration, patient.date_last_clinic, patient.date_last_refill FROM patient "
                    + " JOIN nigqual ON patient.facility_id = nigqual.facility_id AND patient.patient_id = nigqual.patient_id WHERE patient.facility_id = " + facilityId + " AND nigqual.portal_id = " + portalId + " AND nigqual.review_period_id = " + reviewPeriodId + " AND nigqual.thermatic_area = '" + thermaticArea + "'"; 
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) { 
                String patientId = Long.toString(resultSet.getLong("patient_id")); 
                String hospitalNum = resultSet.getString("hospital_num");
                String uniqueId = resultSet.getString("unique_id") == null ? "" : resultSet.getString("unique_id");
                String surname = resultSet.getString("surname") == null ? "" : resultSet.getString("surname");
                surname = (viewIdentifier)? scrambler.unscrambleCharacters(surname) : surname;
                surname = StringUtils.upperCase(surname);                
                String otherNames = resultSet.getString("other_names") == null ? "" : resultSet.getString("other_names");
                otherNames = (viewIdentifier)? scrambler.unscrambleCharacters(otherNames) : otherNames;
                otherNames = StringUtils.capitalize(otherNames);
                String gender = resultSet.getString("gender") == null ? "" : resultSet.getString("gender");                
                String age = (resultSet.getInt("age") == 0)? "" : Integer.toString(resultSet.getInt("age"));
                String ageUnit = resultSet.getString("age_unit") == null ? "" : resultSet.getString("age_unit");
                String currentStatus = resultSet.getString("current_status");
                String dateRegistration = (resultSet.getDate("date_registration") == null)? "" : DateUtil.parseDateToString(resultSet.getDate("date_registration"), "MM/dd/yyyy");                
                String dateLastClinic = (resultSet.getDate("date_last_clinic") == null)? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_clinic"), "MM/dd/yyyy");                
                String dateLastRefill = (resultSet.getDate("date_last_refill") == null)? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_refill"), "MM/dd/yyyy");                
                if(!age.trim().isEmpty()) age = age + " - " + ageUnit ;

                // create an array from object properties 
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("patientId", patientId);
                map.put("facilityId", Long.toString(facilityId));
                map.put("hospitalNum", hospitalNum);
                map.put("uniqueId", uniqueId);
                map.put("name", surname + ' ' + otherNames);
                map.put("gender", gender);
                map.put("age", age);
                map.put("currentStatus", currentStatus);
                map.put("dateRegistration", dateRegistration);
                map.put("dateLastClinic", dateLastClinic);
                map.put("dateLastRefill", dateLastRefill);
                reportList.add(map);
            }
            resultSet = null;
        }
        catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return reportList;
    }
    
    public HashMap getReportParameters(String thermaticArea ,  String reportingDateBegin,String reportingDateEnd) {
        long facilityId = (Long) session.getAttribute("id");

        String reportTitle = thermaticArea.equalsIgnoreCase("AD")? "Adult ART Audit (RNL) - " + reportingDateBegin + " to " + reportingDateEnd  : thermaticArea.equalsIgnoreCase("PD")? "Pediatrics ART Audit (RNL) - " + reportingDateBegin + " to " + reportingDateEnd : "PMTCT Audit (RNL) - " + reportingDateBegin + " to " + reportingDateEnd;        
        
        parameterMap = new HashMap();        
        parameterMap.put("reportTitle", reportTitle);  
        try {
            // fetch the required records from the database
            jdbcUtil = new JDBCUtil();
            query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility JOIN lga ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE facility_id = " + facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next()) {
                parameterMap.put("facilityName", resultSet.getString("name"));  
                parameterMap.put("lga", resultSet.getString("lga"));            
                parameterMap.put("state", resultSet.getString("state")); 
            }
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }                
        return parameterMap;        
    }
}
