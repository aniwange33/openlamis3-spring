/**
 * @author user1
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

public class ChildfollowupListBuilder {
    private HttpServletRequest request;
    private HttpSession session;

    private ArrayList<Map<String, String>> childfollowupList = new ArrayList<Map<String, String>>();

    public ChildfollowupListBuilder() {
    }

    public void buildChildfollowupList(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String childfollowupId = resultSet.getObject("childfollowup_id") == null ? "" : Long.toString(resultSet.getLong("childfollowup_id"));
            String childId = resultSet.getObject("child_id") == null ? "" : Long.toString(resultSet.getLong("child_id"));
            String facilityId = resultSet.getObject("facility_id") == null ? "" : Long.toString(resultSet.getLong("facility_id"));
            String dateVisit = resultSet.getObject("date_visit") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_visit"), "MM/dd/yyyy");
            String ageVisit = resultSet.getObject("age_visit") == null ? "" : Integer.toString(resultSet.getInt("age_visit"));
            String dateNvpInitiated = resultSet.getObject("date_nvp_initiated") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_nvp_initiated"), "MM/dd/yyyy");
            String ageNvpInitiated = resultSet.getObject("age_nvp_initiated") == null ? "" : Integer.toString(resultSet.getInt("age_nvp_initiated"));
            String dateCotrimInitiated = resultSet.getObject("date_cotrim_initiated") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_cotrim_initiated"), "MM/dd/yyyy");
            String dateRapidTest = resultSet.getObject("date_rapid_test") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_rapid_test"), "MM/dd/yyyy");
            String ageCotrimInitiated = resultSet.getObject("age_cotrim_initiated") == null ? "" : Integer.toString(resultSet.getInt("age_cotrim_initiated"));
            String bodyWeight = resultSet.getObject("body_weight") == null ? "" : Double.toString(resultSet.getDouble("body_weight"));
            String height = resultSet.getObject("height") == null ? "" : Double.toString(resultSet.getDouble("height"));
            String feeding = resultSet.getObject("feeding") == null ? "" : resultSet.getString("feeding");
            String arv = resultSet.getObject("arv") == null ? "" : resultSet.getString("arv");
            String arvType = resultSet.getObject("arv_type") == null ? "" : resultSet.getString("arv_type");
            String arvTiming = resultSet.getObject("arv_timing") == null ? "" : resultSet.getString("arv_timing");
            String cotrim = resultSet.getObject("cotrim") == null ? "" : resultSet.getString("cotrim");
            String dateSampleCollected = resultSet.getObject("date_sample_collected") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_sample_collected"), "MM/dd/yyyy");
            String reasonPcr = resultSet.getObject("reason_pcr") == null ? "" : resultSet.getString("reason_pcr");
            String dateSampleSent = resultSet.getObject("date_sample_sent") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_sample_sent"), "MM/dd/yyyy");
            String datePcrResult = resultSet.getObject("date_pcr_result") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_pcr_result"), "MM/dd/yyyy");
            String pcrResult = resultSet.getObject("pcr_result") == null ? "" : resultSet.getString("pcr_result");
            String rapidTest = resultSet.getObject("rapid_test") == null ? "" : resultSet.getString("rapid_test");
            String rapidTestResult = resultSet.getObject("rapid_test_result") == null ? "" : resultSet.getString("rapid_test_result");
            String caregiverGivenResult = resultSet.getObject("caregiver_given_result") == null ? "" : resultSet.getString("caregiver_given_result");
            String childOutcome = resultSet.getObject("child_outcome") == null ? "" : resultSet.getString("child_outcome");
            String referred = resultSet.getObject("referred") == null ? "" : resultSet.getString("referred");
            String dateNextVisit = resultSet.getObject("date_next_visit") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_visit"), "MM/dd/yyyy");
            String dateLinkedToArt = resultSet.getObject("date_linked_to_art") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_linked_to_art"), "MM/dd/yyyy");

            Map<String, String> map = new HashMap<String, String>();
            map.put("childfollowupId", childfollowupId);
            map.put("childId", childId);
            map.put("facilityId", facilityId);
            map.put("dateVisit", dateVisit);
            map.put("ageVisit", ageVisit);
            map.put("dateNvpInitiated", dateNvpInitiated);
            map.put("ageNvpInitiated", ageNvpInitiated);
            map.put("dateCotrimInitiated", dateCotrimInitiated);
            map.put("ageCotrimInitiated", ageCotrimInitiated);
            map.put("bodyWeight", bodyWeight);
            map.put("height", height);
            map.put("feeding", feeding);
            map.put("arv", arv);
            map.put("arvType", arvType);
            map.put("dateRapidTest", dateRapidTest);
            map.put("arvTiming", arvTiming);
            map.put("cotrim", cotrim);
            map.put("dateSampleCollected", dateSampleCollected);
            map.put("reasonPcr", reasonPcr);
            map.put("dateSampleSent", dateSampleSent);
            map.put("datePcrResult", datePcrResult);
            map.put("pcrResult", pcrResult);
            map.put("rapidTest", rapidTest);
            map.put("rapidTestResult", rapidTestResult);
            map.put("caregiverGivenResult", caregiverGivenResult);
            map.put("childOutcome", childOutcome);
            map.put("referred", referred);
            map.put("dateNextVisit", dateNextVisit);
            map.put("dateLinkedToArt", dateLinkedToArt);
            childfollowupList.add(map);
        }
        session.setAttribute("childfollowupList", childfollowupList);
    }

    public ArrayList<Map<String, String>> retrieveChildfollowupList() {
        // retrieve the childfollowup record store in session attribute
        if (session.getAttribute("childfollowupList") != null) {
            childfollowupList = (ArrayList) session.getAttribute("childfollowupList");
        }
        return childfollowupList;
    }

    public void clearChildfollowupList() {
        childfollowupList = retrieveChildfollowupList();
        childfollowupList.clear();
        session.setAttribute("childfollowupList", childfollowupList);
    }

}
