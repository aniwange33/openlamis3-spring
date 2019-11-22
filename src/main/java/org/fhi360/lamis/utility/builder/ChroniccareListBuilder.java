/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility.builder;

import org.fhi360.lamis.utility.DateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChroniccareListBuilder {
    private HttpServletRequest request;
    private HttpSession session;

    private ArrayList<Map<String, String>> chroniccareList = new ArrayList<Map<String, String>>();

    public ChroniccareListBuilder() {
    }
    
    public void buildChroniccareList(ResultSet resultSet) throws SQLException{ 
        try {
            // loop through resultSet for each row and put into Map
            resultSet.beforeFirst();
            while (resultSet.next()) {
                String chroniccareId = resultSet.getObject("chroniccare_id") == null ? "" : Long.toString(resultSet.getLong("chroniccare_id"));
                String patientId = resultSet.getObject("patient_id") == null ? "" : Long.toString(resultSet.getLong("patient_id"));
                String facilityId = resultSet.getObject("facility_id") == null ? "" : Long.toString(resultSet.getLong("facility_id"));
                String dateVisit = resultSet.getObject("date_visit") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_visit"), "MM/dd/yyyy");
                String clientType = resultSet.getObject("client_type") == null ? "" : resultSet.getString("client_type");
                String currentStatus = resultSet.getObject("current_status") == null ? "" : resultSet.getString("current_status");
                String pregnancyStatus = resultSet.getObject("pregnancy_status") == null ? "" : resultSet.getString("pregnancy_status");
                String clinicStage = resultSet.getObject("clinic_stage") == null ? "" : resultSet.getString("clinic_stage");     
                String lastCd4 = resultSet.getObject("last_cd4") == null ? "" : Double.toString(resultSet.getDouble("last_cd4"));
                String dateLastCd4 = resultSet.getObject("date_last_cd4") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_cd4"), "MM/dd/yyyy");
                String lastViralLoad = resultSet.getObject("last_viral_load") == null ? "" : Double.toString(resultSet.getDouble("last_viral_load"));
                String dateLastViralLoad = resultSet.getObject("date_last_viral_load") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_viral_load"), "MM/dd/yyyy");
                String eligibleCd4 = resultSet.getObject("eligible_cd4") == null ? "" : resultSet.getString("eligible_cd4");
                String eligibleViralLoad = resultSet.getObject("eligible_viral_load") == null ? "" : resultSet.getString("eligible_viral_load");
                String cotrimEligibility1 = resultSet.getObject("cotrim_eligibility1") == null ? "" : Integer.toString(resultSet.getInt("cotrim_eligibility1"));
                String cotrimEligibility2 = resultSet.getObject("cotrim_eligibility2") == null ? "" : Integer.toString(resultSet.getInt("cotrim_eligibility2"));
                String cotrimEligibility3 = resultSet.getObject("cotrim_eligibility3") == null ? "" : Integer.toString(resultSet.getInt("cotrim_eligibility3"));
                String cotrimEligibility4 = resultSet.getObject("cotrim_eligibility4") == null ? "" : Integer.toString(resultSet.getInt("cotrim_eligibility4"));
                String cotrimEligibility5 = resultSet.getObject("cotrim_eligibility5") == null ? "" : Integer.toString(resultSet.getInt("cotrim_eligibility5"));
                String ipt = resultSet.getObject("ipt") == null ? "" : resultSet.getString("ipt");
                String inh = resultSet.getObject("inh") == null ? "" : resultSet.getString("inh");
                String tbTreatment = resultSet.getObject("tb_treatment") == null ? "" : resultSet.getString("tb_treatment");
                String dateStartedTbTreatment = resultSet.getObject("date_started_tb_treatment") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_started_tb_treatment"), "MM/dd/yyyy");
                String tbReferred = resultSet.getObject("tb_referred") == null ? "" : resultSet.getString("tb_referred");
                String eligibleIpt = resultSet.getObject("eligible_ipt") == null ? "" : resultSet.getString("eligible_ipt");
                String bodyWeight = resultSet.getObject("body_weight") == null ? "" : Double.toString(resultSet.getDouble("body_weight"));
                String height = resultSet.getObject("height") == null ? "" : Double.toString(resultSet.getDouble("height"));
                String bmi = resultSet.getObject("bmi") == null ? "" : resultSet.getString("bmi");
                String muac = resultSet.getObject("muac") == null ? "" : Double.toString(resultSet.getDouble("muac"));
                String muacPediatrics = resultSet.getObject("muac_pediatrics") == null ? "" : resultSet.getString("muac_pediatrics");
                String muacPregnant = resultSet.getObject("muac_pregnant") == null ? "" : resultSet.getString("muac_pregnant");
                String supplementaryFood = resultSet.getObject("supplementary_food") == null ? "" : resultSet.getString("supplementary_food");
                String nutritionalStatusReferred = resultSet.getObject("nutritional_status_referred") == null ? "" : resultSet.getString("nutritional_status_referred");
                String gbv1 = resultSet.getObject("gbv1") == null ? "" : resultSet.getString("gbv1");
                String gbv1Referred = resultSet.getObject("gbv1_referred") == null ? "" : resultSet.getString("gbv1_referred");
                String gbv2 = resultSet.getObject("gbv2") == null ? "" : resultSet.getString("gbv2");
                String gbv2Referred = resultSet.getObject("gbv2_referred") == null ? "" : resultSet.getString("gbv2_referred");
                String hypertensive = resultSet.getObject("hypertensive") == null ? "" : resultSet.getString("hypertensive");
                String firstHypertensive = resultSet.getObject("first_hypertensive") == null ? "" : resultSet.getString("first_hypertensive");
                String bpAbove = resultSet.getObject("bp_above") == null ? "" : resultSet.getString("bp_above");
                String bpReferred = resultSet.getObject("bp_referred") == null ? "" : resultSet.getString("bp_referred");
                String diabetic = resultSet.getObject("diabetic") == null ? "" : resultSet.getString("diabetic"); 
                String firstDiabetic = resultSet.getObject("first_diabetic") == null ? "" : resultSet.getString("first_diabetic");  
                String phdp1 = resultSet.getObject("phdp1") == null ? "" : resultSet.getString("phdp1");
                String phdp1ServicesProvided = resultSet.getObject("phdp1_services_provided") == null ? "" : resultSet.getString("phdp1_services_provided");
                String phdp2 = resultSet.getObject("phdp2") == null ? "" : resultSet.getString("phdp2");
                String phdp2ServicesProvided = resultSet.getObject("phdp2_services_provided") == null ? "" : resultSet.getString("phdp2_services_provided");
                String phdp3 = resultSet.getObject("phdp3") == null ? "" : resultSet.getString("phdp3");
                String phdp3ServicesProvided = resultSet.getObject("phdp3_services_provided") == null ? "" : resultSet.getString("phdp3_services_provided");
                String phdp4 = resultSet.getObject("phdp4") == null ? "" : resultSet.getString("phdp4");
                String phdp4ServicesProvided = resultSet.getObject("phdp4_services_provided") == null ? "" : resultSet.getString("phdp4_services_provided");
                String phdp5 = resultSet.getObject("phdp5") == null ? "" : resultSet.getString("phdp5");
                String phdp5ServicesProvided = resultSet.getObject("phdp5_services_provided") == null ? "" : resultSet.getString("phdp5_services_provided");
                String phdp6 = resultSet.getObject("phdp6") == null ? "" : Integer.toString(resultSet.getInt("phdp6"));
                String phdp6ServicesProvided = resultSet.getObject("phdp6_services_provided") == null ? "" : resultSet.getString("phdp6_services_provided");
                String phdp7 = resultSet.getObject("phdp7") == null ? "" : Integer.toString(resultSet.getInt("phdp7"));
                String phdp7ServicesProvided = resultSet.getObject("phdp7_services_provided") == null ? "" : resultSet.getString("phdp7_services_provided");
                String phdp8 = resultSet.getObject("phdp8") == null ? "" : resultSet.getString("phdp8");
                String phdp8ServicesProvided = resultSet.getObject("phdp8_services_provided") == null ? "" : resultSet.getString("phdp8_services_provided");
                String phdp9ServicesProvided = resultSet.getObject("phdp9_services_provided") == null ? "" : resultSet.getString("phdp9_services_provided");
                String reproductiveIntentions1 = resultSet.getObject("reproductive_intentions1") == null ? "" : resultSet.getString("reproductive_intentions1");
                String reproductiveIntentions1Referred = resultSet.getObject("reproductive_intentions1_referred") == null ? "" : resultSet.getString("reproductive_intentions1_referred");
                String reproductiveIntentions2 = resultSet.getObject("reproductive_intentions2") == null ? "" : resultSet.getString("reproductive_intentions2");
                String reproductiveIntentions2Referred = resultSet.getObject("reproductive_intentions2_referred") == null ? "" : resultSet.getString("reproductive_intentions2_referred");
                String reproductiveIntentions3 = resultSet.getObject("reproductive_intentions3") == null ? "" : resultSet.getString("reproductive_intentions3");
                String reproductiveIntentions3Referred = resultSet.getObject("reproductive_intentions3_referred") == null ? "" : resultSet.getString("reproductive_intentions3_referred");
                String malariaPrevention1 = resultSet.getObject("malaria_prevention1") == null ? "" : resultSet.getString("malaria_prevention1");
                String malariaPrevention1Referred = resultSet.getObject("malaria_prevention1_referred") == null ? "" : resultSet.getString("malaria_prevention1_referred");
                String malariaPrevention2 = resultSet.getObject("malaria_prevention2") == null ? "" : resultSet.getString("malaria_prevention2");
                String malariaPrevention2Referred = resultSet.getObject("malaria_prevention2_referred") == null ? "" : resultSet.getString("malaria_prevention2_referred");
                String dmReferred = resultSet.getObject("dm_referred") == null ? "" : resultSet.getString("dm_referred");    
                String tbValues = resultSet.getObject("tb_values") == null ? "" : resultSet.getString("tb_values");
                String dmValues = resultSet.getObject("dm_values") == null ? "" : resultSet.getString("dm_values");
				
                Map<String, String> map = new HashMap<String, String>();                
                map.put("chroniccareId", chroniccareId);
                map.put("patientId", patientId);
                map.put("facilityId", facilityId);
                map.put("dateVisit", dateVisit);
                map.put("clientType", clientType);
                map.put("currentStatus", currentStatus);
                map.put("pregnancyStatus", pregnancyStatus);
                map.put("clinicStage", clinicStage);
                map.put("lastCd4", lastCd4);
                map.put("dateLastCd4", dateLastCd4);
                map.put("lastViralLoad", lastViralLoad);
                map.put("dateLastViralLoad", dateLastViralLoad);
                map.put("eligibleCd4", eligibleCd4);
                map.put("eligibleViralLoad", eligibleViralLoad);
                map.put("cotrimEligibility1", cotrimEligibility1);
                map.put("cotrimEligibility2", cotrimEligibility2);
                map.put("cotrimEligibility3", cotrimEligibility3);
                map.put("cotrimEligibility4", cotrimEligibility4);
                map.put("cotrimEligibility5", cotrimEligibility5);
                map.put("ipt", ipt);
                map.put("inh", inh);
                map.put("tbTreatment", tbTreatment);
                map.put("dateStartedTbTreatment", dateStartedTbTreatment);
                map.put("tbReferred", tbReferred);
                map.put("eligibleIpt", eligibleIpt);
                map.put("bodyWeight", bodyWeight);
                map.put("height", height);
                map.put("bmi", bmi);
                map.put("muac", muac);
                map.put("muacPediatrics", muacPediatrics);
                map.put("muacPregnant", muacPregnant);
                map.put("supplementaryFood", supplementaryFood);
                map.put("nutritionalStatusReferred", nutritionalStatusReferred);
                map.put("gbv1", gbv1);
                map.put("gbv1Referred", gbv1Referred);
                map.put("gbv2", gbv2);
                map.put("gbv2Referred", gbv2Referred);
                map.put("hypertensive", hypertensive);
                map.put("firstHypertensive", firstHypertensive);
                map.put("bpAbove", bpAbove);
                map.put("bpReferred", bpReferred);
                map.put("diabetic", diabetic);
                map.put("firstDiabetic", firstDiabetic);
                map.put("phdp1", phdp1);
                map.put("phdp1ServicesProvided", phdp1ServicesProvided);
                map.put("phdp2", phdp2);
                map.put("phdp2ServicesProvided", phdp2ServicesProvided);
                map.put("phdp3", phdp3);
                map.put("phdp3ServicesProvided", phdp3ServicesProvided);
                map.put("phdp4", phdp4);
                map.put("phdp4ServicesProvided", phdp4ServicesProvided);
                map.put("phdp5", phdp5);
                map.put("phdp5ServicesProvided", phdp5ServicesProvided);
                map.put("phdp6", phdp6);
                map.put("phdp6ServicesProvided", phdp6ServicesProvided);
                map.put("phdp7", phdp7);
                map.put("phdp7ServicesProvided", phdp7ServicesProvided);
                map.put("phdp8", phdp8);
                map.put("phdp8ServicesProvided", phdp8ServicesProvided);
                map.put("phdp9ServicesProvided", phdp9ServicesProvided);
                map.put("reproductiveIntentions1", reproductiveIntentions1);
                map.put("reproductiveIntentions1Referred", reproductiveIntentions1Referred);
                map.put("reproductiveIntentions2", reproductiveIntentions2);
                map.put("reproductiveIntentions2Referred", reproductiveIntentions2Referred);
                map.put("reproductiveIntentions3", reproductiveIntentions3);
                map.put("reproductiveIntentions3Referred", reproductiveIntentions3Referred);
                map.put("malariaPrevention1", malariaPrevention1);
                map.put("malariaPrevention1Referred", malariaPrevention1Referred);
                map.put("malariaPrevention2", malariaPrevention2);
                map.put("malariaPrevention2Referred", malariaPrevention2Referred);
                map.put("dmReferred", dmReferred);
                map.put("tbValues", tbValues);
                map.put("dmValues", dmValues);
                chroniccareList.add(map);
            }            
            session.setAttribute("chroniccareList", chroniccareList);
        }
        catch (SQLException sqlException) {
            throw sqlException;  
        }
    }

   public ArrayList<Map<String, String>> retrieveChroniccareList() {
        // retrieve the chroniccare record store in session attribute
        if(session.getAttribute("chroniccareList") != null) {
            chroniccareList = (ArrayList) session.getAttribute("chroniccareList");
        }
        return chroniccareList;
    }
    
    public void clearChroniccareList() {
        chroniccareList = retrieveChroniccareList();
        chroniccareList.clear();
        session.setAttribute("chroniccareList", chroniccareList); 
    }
        
}
