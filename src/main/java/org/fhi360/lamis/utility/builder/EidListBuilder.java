/**
 * @author aalozie
 *//*


package org.fhi360.lamis.utility.builder;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.model.Eid;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.Scrambler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class EidListBuilder {
    private HttpServletRequest request;
    private HttpSession session;
    private Boolean viewIdentifier;
    private Scrambler scrambler;
    private ArrayList<Map<String, String>> eidList = new ArrayList<Map<String, String>>();

    public EidListBuilder() {
        */
/*this.scrambler = new Scrambler();
        if (ServletActionContext.getRequest().getSession().getAttribute("viewIdentifier") != null) {
            this.viewIdentifier = (Boolean) session.getAttribute("viewIdentifier");
        }*//*

    }

    public void buildEidList(Eid eid) {
        String eidId = Long.toString(eid.getEidId());
        String id = Long.toString(eid.getId());
        String labno = eid.getLabno() == null ? "" : eid.getLabno();
        String motherName = eid.getMotherName() == null ? "" : eid.getMotherName();
        motherName = (viewIdentifier) ? scrambler.unscrambleCharacters(motherName) : motherName;
        motherName = StringUtils.upperCase(motherName);
        String motherAddress = eid.getMotherAddress() == null ? "" : eid.getMotherAddress();
        motherAddress = (viewIdentifier) ? scrambler.unscrambleCharacters(motherAddress) : motherAddress;
        motherAddress = StringUtils.capitalize(motherAddress);
        String motherPhone = eid.getMotherPhone() == null ? "" : eid.getMotherPhone();
        motherPhone = (viewIdentifier) ? scrambler.unscrambleNumbers(motherPhone) : motherPhone;
        String senderName = eid.getSenderName() == null ? "" : eid.getSenderName();
        String senderAddress = eid.getSenderAddress() == null ? "" : eid.getSenderAddress();
        String senderDesignation = eid.getSenderDesignation() == null ? "" : eid.getSenderDesignation();
        String senderPhone = eid.getSenderPhone() == null ? "" : eid.getSenderPhone();
        String reasonPcr = eid.getReasonPcr() == null ? "" : eid.getReasonPcr();
        String rapidTestDone = eid.getRapidTestDone() == null ? "" : eid.getRapidTestDone();
        String dateRapidTest = eid.getDateRapidTest() == null ? "" : DateUtil.parseDateToString(eid.getDateRapidTest(), "MM/dd/yyyy");
        String rapidTestResult = eid.getRapidTestResult() == null ? "" : eid.getRapidTestResult();
        String motherArtReceived = eid.getMotherArtReceived() == null ? "" : eid.getMotherArtReceived();
        String motherProphylaxReceived = eid.getMotherProphylaxReceived() == null ? "" : eid.getMotherProphylaxReceived();
        String childProphylaxReceived = eid.getChildProphylaxReceived() == null ? "" : eid.getChildProphylaxReceived();
        String breastfedEver = eid.getBreastfedEver() == null ? "" : eid.getBreastfedEver();
        String feedingMethod = eid.getFeedingMethod() == null ? "" : eid.getFeedingMethod();
        String breastfedNow = eid.getBreastfedNow() == null ? "" : eid.getBreastfedNow();
        String feedingCessationAge = (eid.getFeedingCessationAge() == null) ? "" : Integer.toString(eid.getFeedingCessationAge());
        String cotrim = eid.getCotrim() == null ? "" : eid.getCotrim();
        String nextAppointment = (eid.getNextAppointment() == null) ? "" : DateUtil.parseDateToString(eid.getNextAppointment(), "MM/dd/yyyy");
        String timeStamp = (eid.getTimeStamp() == null) ? "" : DateUtil.parseDateToString(eid.getTimeStamp(), "MM/dd/yyyy");

        // create an array from object properties 
        Map<String, String> map = new TreeMap<String, String>();
        map.put("eidId", eidId);
        map.put("id", id);
        map.put("labno", labno);
        map.put("motherName", motherName);
        map.put("motherAddress", motherAddress);
        map.put("motherPhone", motherPhone);
        map.put("senderName", senderName);
        map.put("senderAddress", senderAddress);
        map.put("senderDesignation", senderDesignation);
        map.put("senderPhone", senderPhone);
        map.put("reasonPcr", reasonPcr);
        map.put("rapidTestDone", rapidTestDone);
        map.put("dateRapidTest", dateRapidTest);
        map.put("rapidTestResult", rapidTestResult);
        map.put("motherArtReceived", motherArtReceived);
        map.put("motherProphylaxReceived", motherProphylaxReceived);
        map.put("childProphylaxReceived", childProphylaxReceived);
        map.put("breastfedEver", breastfedEver);
        map.put("feedingMethod", feedingMethod);
        map.put("breastfedNow", breastfedNow);
        map.put("feedingCessationAge", feedingCessationAge);
        map.put("cotrim", cotrim);
        map.put("nextAppointment", nextAppointment);
        map.put("timeStamp", timeStamp);
        eidList.add(map);
        session.setAttribute("eidList", eidList);
    }

    public void buildEidList(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String eidId = Long.toString(resultSet.getLong("eid_id"));
            String id = Long.toString(resultSet.getLong("facility_id"));
            String labno = resultSet.getObject("labno") == null ? "" : resultSet.getString("labno");
            String motherName = resultSet.getObject("mother_name") == null ? "" : resultSet.getString("mother_name");
            motherName = (viewIdentifier) ? scrambler.unscrambleCharacters(motherName) : motherName;
            motherName = StringUtils.upperCase(motherName);
            String motherAddress = resultSet.getObject("mother_address") == null ? "" : resultSet.getString("mother_address");
            motherAddress = (viewIdentifier) ? scrambler.unscrambleCharacters(motherAddress) : motherAddress;
            motherAddress = StringUtils.capitalize(motherAddress);
            String motherPhone = resultSet.getObject("mother_phone") == null ? "" : resultSet.getString("mother_phone");
            motherPhone = (viewIdentifier) ? scrambler.unscrambleNumbers(motherPhone) : motherPhone;
            String senderName = resultSet.getObject("sender_name") == null ? "" : resultSet.getString("sender_name");
            String senderDesignation = resultSet.getObject("sender_designation") == null ? "" : resultSet.getString("sender_designation");
            String senderPhone = resultSet.getObject("sender_phone") == null ? "" : resultSet.getString("sender_phone");
            String senderAddress = resultSet.getObject("sender_address") == null ? "" : resultSet.getString("sender_address");
            String reasonPcr = resultSet.getObject("reason_pcr") == null ? "" : resultSet.getString("reason_pcr");
            String rapidTestDone = resultSet.getObject("rapid_test_done") == null ? "" : resultSet.getString("rapid_test_done");
            String dateRapidTest = resultSet.getString("date_rapid_test") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_rapid_test"), "MM/dd/yyyy");
            String rapidTestResult = resultSet.getObject("rapid_test_result") == null ? "" : resultSet.getString("rapid_test_result");
            String motherArtReceived = resultSet.getObject("mother_art_received") == null ? "" : resultSet.getString("mother_art_received");
            String motherProphylaxReceived = resultSet.getObject("mother_prophylax_received") == null ? "" : resultSet.getString("mother_prophylax_received");
            String childProphylaxReceived = resultSet.getObject("child_prophylax_received") == null ? "" : resultSet.getString("child_prophylax_received");
            String breastfedEver = resultSet.getObject("breastfed_ever") == null ? "" : resultSet.getString("breastfed_ever");
            String feedingMethod = resultSet.getObject("feeding_method") == null ? "" : resultSet.getString("feeding_method");
            String breastfedNow = resultSet.getObject("breastfed_now") == null ? "" : resultSet.getString("breastfed_now");
            String feedingCessationAge = resultSet.getObject("feeding_cessation_age") == null ? "" : Integer.toString(resultSet.getInt("feeding_cessation_age"));
            String cotrim = resultSet.getObject("cotrim") == null ? "" : resultSet.getString("cotrim");
            String nextAppointment = resultSet.getObject("next_appointment") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("next_appointment"), "MM/dd/yyyy");
            String timeStamp = resultSet.getObject("time_stamp") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("time_stamp"), "MM/dd/yyyy");

            // create an array from object properties
            Map<String, String> map = new TreeMap<String, String>();
            map.put("eidId", eidId);
            map.put("id", id);
            map.put("labno", labno);
            map.put("motherName", motherName);
            map.put("motherAddress", motherAddress);
            map.put("motherPhone", motherPhone);
            map.put("senderName", senderName);
            map.put("senderAddress", senderAddress);
            map.put("senderDesignation", senderDesignation);
            map.put("senderPhone", senderPhone);
            map.put("reasonPcr", reasonPcr);
            map.put("rapidTestDone", rapidTestDone);
            map.put("dateRapidTest", dateRapidTest);
            map.put("rapidTestResult", rapidTestResult);
            map.put("motherArtReceived", motherArtReceived);
            map.put("motherProphylaxReceived", motherProphylaxReceived);
            map.put("childProphylaxReceived", childProphylaxReceived);
            map.put("breastfedEver", breastfedEver);
            map.put("feedingMethod", feedingMethod);
            map.put("breastfedNow", breastfedNow);
            map.put("feedingCessationAge", feedingCessationAge);
            map.put("cotrim", cotrim);
            map.put("nextAppointment", nextAppointment);
            map.put("timeStamp", timeStamp);
            eidList.add(map);
        }
        session.setAttribute("eidList", eidList);
    }

    public ArrayList<Map<String, String>> retrieveEidList() {
        // retrieve the eid record store in session attribute
        if (session.getAttribute("eidList") != null) {
            eidList = (ArrayList) session.getAttribute("eidList");
        }
        return eidList;
    }

    public void clearEidList() {
        eidList = retrieveEidList();
        eidList.clear();
        session.setAttribute("eidList", eidList);
    }

}
*/
