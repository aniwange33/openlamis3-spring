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

public class MaternalfollowupListBuilder {
    private HttpServletRequest request;
    private HttpSession session;

    private ArrayList<Map<String, String>> maternalfollowupList = new ArrayList<Map<String, String>>();

    public MaternalfollowupListBuilder() {
    }

    public void buildMaternalfollowupList(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String maternalfollowupId = resultSet.getObject("maternalfollowup_id") == null ? "" : Long.toString(resultSet.getLong("maternalfollowup_id"));
            String patientId = resultSet.getObject("patient_id") == null ? "" : Long.toString(resultSet.getLong("patient_id"));
            String facilityId = resultSet.getObject("facility_id") == null ? "" : Long.toString(resultSet.getLong("facility_id"));
            String ancId = resultSet.getObject("anc_id") == null ? "" : Long.toString(resultSet.getLong("anc_id"));
            String dateVisit = resultSet.getObject("date_visit") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_visit"), "MM/dd/yyyy");
            String bodyWeight = resultSet.getObject("body_weight") == null ? "" : Double.toString(resultSet.getDouble("body_weight"));
            String bp = resultSet.getObject("bp") == null ? "" : resultSet.getString("bp");
            String fundalHeight = resultSet.getObject("fundal_height") == null ? "" : Double.toString(resultSet.getDouble("fundal_height"));
            String fetalPresentation = resultSet.getObject("fetal_presentation") == null ? "" : resultSet.getString("fetal_presentation");
            String syphilisTested = resultSet.getObject("syphilis_tested") == null ? "" : resultSet.getString("syphilis_tested");
            String tbStatus = resultSet.getObject("tb_status") == null ? "" : resultSet.getString("tb_status");
            String visitStatus = resultSet.getObject("visit_status") == null ? "" : resultSet.getString("visit_status");
            String syphilisTestResult = resultSet.getObject("syphilis_test_result") == null ? "" : resultSet.getString("syphilis_test_result");
            String gestationalAge = resultSet.getObject("gestational_age") == null ? "" : resultSet.getString("gestational_age");
            String viralLoadCollected = resultSet.getObject("viral_load_collected") == null ? "" : resultSet.getString("viral_load_collected");
            String cd4Ordered = resultSet.getObject("cd4_ordered") == null ? "" : resultSet.getString("cd4_ordered");
            String cd4 = resultSet.getObject("cd4") == null ? "" : Double.toString(resultSet.getDouble("cd4"));
            String counselNutrition = resultSet.getObject("counsel_nutrition") == null ? "" : Integer.toString(resultSet.getInt("counsel_nutrition"));
            String counselFeeding = resultSet.getObject("counsel_feeding") == null ? "" : Integer.toString(resultSet.getInt("counsel_feeding"));
            String counselFamilyPlanning = resultSet.getObject("counsel_family_planning") == null ? "" : Integer.toString(resultSet.getInt("counsel_family_planning"));
            String familyPlanningMethod = resultSet.getObject("family_planning_method") == null ? "" : resultSet.getString("family_planning_method");
            String referred = resultSet.getObject("referred") == null ? "" : resultSet.getString("referred");
            String partnerHivStatus = "";
            try {
                partnerHivStatus = resultSet.getObject("partner_hiv_status") == null ? "" : resultSet.getString("partner_hiv_status");
            } catch (SQLException sqlException) {
            }

            String dateNextVisit = resultSet.getObject("date_next_visit") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_visit"), "MM/dd/yyyy");
            String dateSampleCollected = resultSet.getObject("date_sample_collected") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_sample_collected"), "MM/dd/yyyy");
            String dateConfirmedHiv = resultSet.getObject("date_confirmed_hiv") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_confirmed_hiv"), "MM/dd/yyyy");
            String timeHivDiagnosis = resultSet.getObject("time_hiv_diagnosis") == null ? "" : resultSet.getString("time_hiv_diagnosis");
            String typeOfVisit = resultSet.getObject("type_of_visit") == null ? "" : resultSet.getString("type_of_visit");
            String arvRegimenPast = resultSet.getObject("arv_regimen_past") == null ? "" : resultSet.getString("arv_regimen_past");
            String arvRegimenCurrent = resultSet.getObject("arv_regimen_current") == null ? "" : resultSet.getString("arv_regimen_current");
            String dateArvRegimenCurrent = resultSet.getObject("date_arv_regimen_current") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_arv_regimen_current"), "MM/dd/yyyy");
            String screenPostPartum = resultSet.getObject("screen_post_partum") == null ? "" : Integer.toString(resultSet.getInt("screen_post_partum"));

            Map<String, String> map = new HashMap<String, String>();
            map.put("maternalfollowupId", maternalfollowupId);
            map.put("patientId", patientId);
            map.put("facilityId", facilityId);
            map.put("ancId", ancId);
            map.put("dateVisit", dateVisit);
            map.put("bodyWeight", bodyWeight);
            map.put("bp", bp);
            map.put("fundalHeight", fundalHeight);
            map.put("fetalPresentation", fetalPresentation);
            map.put("syphilisTested", syphilisTested);
            map.put("syphilisTestResult", syphilisTestResult);
            map.put("viralLoadCollected", viralLoadCollected);
            map.put("dateSampleCollected", dateSampleCollected);
            map.put("cd4Ordered", cd4Ordered);
            map.put("cd4", cd4);
            map.put("counselNutrition", counselNutrition);
            map.put("tbStatus", tbStatus);
            map.put("visitStatus", visitStatus);
            map.put("counselFeeding", counselFeeding);
            map.put("counselFamilyPlanning", counselFamilyPlanning);
            map.put("familyPlanningMethod", familyPlanningMethod);
            map.put("referred", referred);
            map.put("dateNextVisit", dateNextVisit);
            map.put("typeOfVisit", typeOfVisit);
            map.put("gestationalAge", gestationalAge + " weeks");
            map.put("partnerHivStatus", partnerHivStatus);
            map.put("dateConfirmedHiv", dateConfirmedHiv);
            map.put("timeHivDiagnosis", timeHivDiagnosis);
            map.put("arvRegimenPast", arvRegimenPast);
            map.put("arvRegimenCurrent", arvRegimenCurrent);
            map.put("dateArvRegimenCurrent", dateArvRegimenCurrent);
            map.put("screenPostPartum", screenPostPartum);
            maternalfollowupList.add(map);
        }
        session.setAttribute("maternalfollowupList", maternalfollowupList);
    }

    public ArrayList<Map<String, String>> retrieveMaternalfollowupList() {
        // retrieve the maternalfollowup record store in session attribute
        if (session.getAttribute("maternalfollowupList") != null) {
            maternalfollowupList = (ArrayList) session.getAttribute("maternalfollowupList");
        }
        return maternalfollowupList;
    }

    public void clearMaternalfollowupList() {
        maternalfollowupList = retrieveMaternalfollowupList();
        maternalfollowupList.clear();
        session.setAttribute("maternalfollowupList", maternalfollowupList);
    }

}
