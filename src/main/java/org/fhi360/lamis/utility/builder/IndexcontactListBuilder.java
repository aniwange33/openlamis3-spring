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
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.Scrambler;

/**
 *
 * @author user10
 */
public class IndexcontactListBuilder {

    private HttpServletRequest request;
    private HttpSession session;
    private Boolean viewIdentifier;
    private Scrambler scrambler;

    private ArrayList<Map<String, String>> indexcontactList = new ArrayList<>();

    public IndexcontactListBuilder() {
        this.scrambler = new Scrambler();
        /*if (ServletActionContext.getRequest().getSession().getAttribute("viewIdentifier") != null) {
            this.viewIdentifier = (Boolean) session.getAttribute("viewIdentifier");
        }*/
    }

    public void buildIndexcontactList(ResultSet resultSet) throws SQLException {
        try {
            // loop through resultSet for each row and put into Map
            resultSet.beforeFirst();
            while (resultSet.next()) {
                String indexcontactId = Long.toString(resultSet.getLong("indexcontact_id"));
                String htsId = Long.toString(resultSet.getLong("hts_id"));
                String facilityId = Long.toString(resultSet.getLong("facility_id"));

                String clientCode = resultSet.getObject("client_code") == null ? "" : resultSet.getString("client_code");
                String contactType = resultSet.getObject("contact_type") == null ? "" : resultSet.getString("contact_type");
                String indexContactCode = resultSet.getObject("index_contact_code") == null ? "" : resultSet.getString("index_contact_code");

                String surname = resultSet.getObject("surname") == null ? "" : resultSet.getString("surname");
                String otherNames = resultSet.getObject("other_names") == null ? "" : resultSet.getString("other_names");
                surname = (viewIdentifier) ? scrambler.unscrambleCharacters(surname) : surname;
                surname = StringUtils.upperCase(surname);
                otherNames = (viewIdentifier) ? scrambler.unscrambleCharacters(otherNames) : otherNames;
                otherNames = StringUtils.capitalize(otherNames);

                String age = resultSet.getObject("age") == null ? "" : resultSet.getString("age");
                String gender = resultSet.getObject("gender") == null ? "" : resultSet.getString("gender");
                String address = resultSet.getObject("address") == null ? "" : resultSet.getString("address");
                String phone = resultSet.getObject("phone") == null ? "" : resultSet.getString("phone");
                String relationship = resultSet.getObject("relationship") == null ? "" : resultSet.getString("relationship");
                String gbv = resultSet.getObject("gbv") == null ? "" : resultSet.getString("gbv");
                String durationPartner = resultSet.getObject("duration_partner") == null ? "" : resultSet.getString("duration_partner");
               String phoneTracking = resultSet.getObject("phone_tracking") == null ? "" : resultSet.getString("phone_tracking");
                String homeTracking = resultSet.getObject("home_tracking") == null ? "" : resultSet.getString("home_tracking");
                String outcome = resultSet.getObject("outcome") == null ? "" : resultSet.getString("outcome");

                String dateHivTest = resultSet.getObject("date_hiv_test") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_hiv_test"), "MM/dd/yyyy");
                String hivStatus = resultSet.getObject("hiv_status") == null ? "" : resultSet.getString("hiv_status");
                String linkCare = resultSet.getObject("link_care") == null ? "" : resultSet.getString("link_care");
                String partnerNotification = resultSet.getObject("partner_notification") == null ? "" : resultSet.getString("partner_notification");
                String modeNotification = resultSet.getObject("mode_notification") == null ? "" : resultSet.getString("mode_notification");

                String serviceProvided = resultSet.getObject("service_provided") == null ? "" : resultSet.getString("service_provided");
                String deviceconfigId = resultSet.getObject("deviceconfig_id") == null ? "" : resultSet.getString("deviceconfig_id");
                String timeStamp = resultSet.getObject("time_stamp") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("time_stamp"), "MM/dd/yyyy");
                String idUUID = resultSet.getObject("id_UUID") == null ? "" : resultSet.getString("id_UUID");

                // create an array from object properties 
                Map<String, String> map = new HashMap<>();
                map.put("clientCode", clientCode);
                map.put("indexcontactId", indexcontactId);
                map.put("htsId", htsId);
                map.put("facilityId", facilityId);
                map.put("contactType", contactType);
                map.put("indexContactCode", indexContactCode);
                map.put("age", age);
                map.put("address", address);
                map.put("phone", phone);
                map.put("relationship", relationship);
                map.put("surname", surname);
                map.put("otherNames", otherNames);
                map.put("name", surname + ' ' + otherNames);
                map.put("gender", gender);
                map.put("gbv", gbv);
                map.put("durationPartner", durationPartner);
                map.put("phoneTracking", phoneTracking);
                map.put("homeTracking", homeTracking);
                map.put("outcome", outcome);
                map.put("dateHivTest", dateHivTest);
                map.put("hivStatus", hivStatus);
                map.put("linkCare", linkCare);
                map.put("partnerNotification", partnerNotification);
                map.put("modeNotification", modeNotification);

                map.put("serviceProvided", serviceProvided);
                map.put("deviceconfigId", deviceconfigId);
                map.put("timeStamp", timeStamp);
                map.put("idUUID", idUUID);

                indexcontactList.add(map);

            }

            session.setAttribute("indexcontactList", indexcontactList);
            resultSet = null;
            indexcontactList = null;
        } catch (SQLException sqlException) {
            resultSet = null;
            throw sqlException;
        }
    }

    public ArrayList<Map<String, String>> retrieveIndexcontactList() {
        if (session.getAttribute("indexcontactList") != null) {
            indexcontactList = (ArrayList) session.getAttribute("indexcontactList");
        }
        return indexcontactList;
    }

    public void clearIndexcontactList() {
        indexcontactList = retrieveIndexcontactList();
        indexcontactList.clear();
        session.setAttribute("indexcontactList", indexcontactList);
    }

}
