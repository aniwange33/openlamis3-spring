/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility.builder;

/**
 * @author user1
 */

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.Scrambler;

import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AncListBuilder {
    private HttpSession session;
    private Boolean viewIdentifier;
    private Scrambler scrambler;
    private ArrayList<Map<String, String>> ancList = new ArrayList<Map<String, String>>();
    private ArrayList<Map<String, String>> motherList = new ArrayList<Map<String, String>>();

    public AncListBuilder() {
        /*this.session = ServletActionContext.getRequest().getSession();
        this.scrambler = new Scrambler();
        if (ServletActionContext.getRequest().getSession().getAttribute("viewIdentifier") != null) {
            this.viewIdentifier = (Boolean) session.getAttribute("viewIdentifier");
        }*/
    }

    public void buildAncList(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String ancId = resultSet.getObject("anc_id") == null ? "" : Long.toString(resultSet.getLong("anc_id"));
            String patientId = resultSet.getObject("patient_id") == null ? "" : Long.toString(resultSet.getLong("patient_id"));
            String facilityId = resultSet.getObject("facility_id") == null ? "" : Long.toString(resultSet.getLong("facility_id"));
            String ancNum = resultSet.getString("anc_num") == null ? "" : resultSet.getString("anc_num");
            String uniqueId = resultSet.getString("unique_id") == null ? "" : resultSet.getString("unique_id");
            String dateVisit = resultSet.getDate("date_visit") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_visit"), "MM/dd/yyyy");
            String dateEnrolledPmtct = resultSet.getDate("date_enrolled_pmtct") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_enrolled_pmtct"), "MM/dd/yyyy");
            String sourceReferral = resultSet.getString("source_referral") == null ? "" : resultSet.getString("source_referral");
            String lmp = resultSet.getDate("lmp") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("lmp"), "MM/dd/yyyy");
            String edd = resultSet.getDate("edd") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("edd"), "MM/dd/yyyy");
            String gestationalAge = resultSet.getObject("gestational_age") == null ? "" : Integer.toString(resultSet.getInt("gestational_age"));
            String gravida = resultSet.getObject("gravida") == null ? "" : Integer.toString(resultSet.getInt("gravida"));
            String parity = resultSet.getObject("parity") == null ? "" : Integer.toString(resultSet.getInt("parity"));
            String timeHivDiagnosis = resultSet.getString("time_hiv_diagnosis") == null ? "" : resultSet.getString("time_hiv_diagnosis");
            String arvRegimenPast = resultSet.getString("arv_regimen_past") == null ? "" : resultSet.getString("arv_regimen_past");
            String arvRegimenCurrent = resultSet.getString("arv_regimen_current") == null ? "" : resultSet.getString("arv_regimen_current");
            String dateConfirmedHiv = resultSet.getDate("date_confirmed_hiv") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_confirmed_hiv"), "MM/dd/yyyy");
            String clinicStage = resultSet.getString("clinic_stage") == null ? "" : resultSet.getString("clinic_stage");
            String hepatitisBTested = resultSet.getString("hepatitisb_tested") == null ? "" : resultSet.getString("hepatitisb_tested");
            String syphilisTested = resultSet.getString("syphilis_tested") == null ? "" : resultSet.getString("syphilis_tested");
            String syphilisTestResult = resultSet.getString("syphilis_test_result") == null ? "" : resultSet.getString("syphilis_test_result");
            String syphilisTreated = resultSet.getString("syphilis_treated") == null ? "" : resultSet.getString("syphilis_treated");
            String hepatitisBTestResult = resultSet.getString("hepatitisb_test_result") == null ? "" : resultSet.getString("hepatitisb_test_result");
            String hepatitisCTested = resultSet.getString("hepatitisc_tested") == null ? "" : resultSet.getString("hepatitisc_tested");
            String hepatitisCTestResult = resultSet.getString("hepatitisc_test_result") == null ? "" : resultSet.getString("hepatitisc_test_result");
            String funcStatus = resultSet.getString("func_status") == null ? "" : resultSet.getString("func_status");
            String bodyWeight = resultSet.getObject("body_weight") == null ? "" : Double.toString(resultSet.getDouble("body_weight"));
            String cd4Ordered = resultSet.getString("cd4_ordered") == null ? "" : resultSet.getString("cd4_ordered");
            String cd4 = resultSet.getObject("cd4") == null ? "" : Double.toString(resultSet.getDouble("cd4"));
            String dateArvRegimenCurrent = resultSet.getObject("date_arv_regimen_current") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_arv_regimen_current"), "MM/dd/yyyy");
            String dateCd4Count = resultSet.getObject("date_cd4") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_cd4"), "MM/dd/yyyy");
            String height = resultSet.getObject("height") == null ? "" : Double.toString(resultSet.getDouble("height"));
            String numberAncVisit = resultSet.getObject("number_anc_visit") == null ? "" : resultSet.getString("number_anc_visit");
            String viralLoadTestDone = resultSet.getObject("viral_load_test_done") == null ? "" : resultSet.getString("viral_load_test_done");
            String dateViralLoad = resultSet.getObject("date_viral_load") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_viral_load"), "MM/dd/yyyy");
            String dateNextAppointment = resultSet.getObject("date_next_appointment") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_appointment"), "MM/dd/yyyy");
            String viralLoadResult = resultSet.getObject("viral_load_result") == null ? "" : Double.toString(resultSet.getDouble("viral_load_result"));

            Map<String, String> map = new HashMap<String, String>();
            map.put("ancId", ancId);
            map.put("patientId", patientId);
            map.put("facilityId", facilityId);
            map.put("ancNum", ancNum);
            map.put("uniqueId", uniqueId);
            map.put("dateVisit", dateVisit);
            map.put("dateEnrolledPmtct", dateEnrolledPmtct);
            map.put("dateNextAppointment", dateNextAppointment);
            map.put("sourceReferral", sourceReferral);
            map.put("lmp", lmp);
            map.put("edd", edd);
            map.put("gestationalAge", gestationalAge);
            map.put("gravida", gravida);
            map.put("parity", parity);
            map.put("timeHivDiagnosis", timeHivDiagnosis);
            map.put("arvRegimenPast", arvRegimenPast);
            map.put("arvRegimenCurrent", arvRegimenCurrent);
            map.put("dateConfirmedHiv", dateConfirmedHiv);
            map.put("clinicStage", clinicStage);
            map.put("funcStatus", funcStatus);
            map.put("bodyWeight", bodyWeight);
            map.put("cd4Ordered", cd4Ordered);
            map.put("hepatitisBTestResult", hepatitisBTestResult);
            map.put("hepatitisBTested", hepatitisBTested);
            map.put("syphilisTested", syphilisTested);
            map.put("syphilisTestResult", syphilisTestResult);
            map.put("syphilisTreated", syphilisTreated);
            map.put("hepatitisCTestResult", hepatitisCTestResult);
            map.put("hepatitisCTested", hepatitisCTested);
            map.put("cd4", cd4);
            map.put("dateArvRegimenCurrent", dateArvRegimenCurrent);
            map.put("dateCd4Count", dateCd4Count);
            map.put("height", height);
            map.put("numberAncVisit", numberAncVisit);
            map.put("viralLoadTestDone", viralLoadTestDone);
            map.put("dateViralLoad", dateViralLoad);
            map.put("viralLoadResult", viralLoadResult);
            ancList.add(map);
        }
        session.setAttribute("ancList", ancList);
    }

    public void buildMotherList(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String motherId = resultSet.getObject("motherinformation_id") == null ? "" : Long.toString(resultSet.getLong("motherinformation_id"));
            String surname = resultSet.getObject("surname") == null ? "" : resultSet.getString("surname");
            surname = (viewIdentifier) ? scrambler.unscrambleCharacters(surname) : surname;
            surname = StringUtils.upperCase(surname);
            String otherNames = resultSet.getObject("other_names") == null ? "" : resultSet.getString("other_names");
            otherNames = (viewIdentifier) ? scrambler.unscrambleCharacters(otherNames) : otherNames;
            otherNames = StringUtils.capitalize(otherNames);
            String patientId = resultSet.getObject("patient_id") == null ? "" : Long.toString(resultSet.getLong("patient_id"));
            String facilityId = resultSet.getObject("facility_id") == null ? "" : Long.toString(resultSet.getLong("facility_id"));
            String hospitalNum = resultSet.getString("hospital_num") == null ? "" : resultSet.getString("hospital_num");
            String uniqueId = resultSet.getString("unique_id") == null ? "" : resultSet.getString("unique_id");
            String dateConfirmedHiv = resultSet.getDate("date_confirmed_hiv") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_confirmed_hiv"), "MM/dd/yyyy");
            String dateEnrolledPmtct = resultSet.getDate("date_enrolled_pmtct") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_enrolled_pmtct"), "MM/dd/yyyy");
            String timeHivDiagnosis = resultSet.getObject("time_hiv_diagnosis") == null ? "" : resultSet.getString("unique_id");
            String regimen = resultSet.getString("regimen") == null ? "" : resultSet.getString("regimen");
            String address = resultSet.getString("address") == null ? "" : resultSet.getString("address");
            address = (viewIdentifier) ? scrambler.unscrambleCharacters(address) : address;
            address = StringUtils.capitalize(address);
            String artStatus = resultSet.getString("art_status") == null ? "" : resultSet.getString("art_status");
            String inFacility = resultSet.getString("in_facility") == null ? "" : resultSet.getString("in_facility");

            Map<String, String> map = new HashMap<String, String>();
            map.put("motherId", motherId);
            map.put("patientId", patientId);
            map.put("surname", surname);
            map.put("otherNames", otherNames);
            map.put("facilityId", facilityId);
            map.put("hospitalNum", hospitalNum);
            map.put("uniqueId", uniqueId);
            map.put("dateEnrolledPmtct", dateEnrolledPmtct);
            map.put("timeHivDiagnosis", timeHivDiagnosis);
            map.put("regimen", regimen);
            map.put("dateConfirmedHiv", dateConfirmedHiv);
            map.put("address", address);
            map.put("srtStatus", artStatus);
            map.put("inFacility", inFacility);
            motherList.add(map);
        }
        session.setAttribute("motherList", motherList);
    }

    public ArrayList<Map<String, String>> retrieveAncList() {
        // retrieve the anc record store in session attribute
        try {
            if (session.getAttribute("ancList") != null) {
                ancList = (ArrayList) session.getAttribute("ancList");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ancList;
    }

    public void clearAncList() {
        ancList = retrieveAncList();
        ancList.clear();
        session.setAttribute("ancList", ancList);
    }

}
