/*
 * To change this template, choose Tools | Templates
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

import org.fhi360.lamis.utility.DateUtil;

public class PartnerInformationListBuilder {
    private HttpServletRequest request;
    private HttpSession session;

    private ArrayList<Map<String, String>> partnerinformationList = new ArrayList<Map<String, String>>();

    public PartnerInformationListBuilder() {
    }

    public void buildPartnerinformationList(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String partnerinformationId = resultSet.getObject("partnerinformation_id") == null ? "" : Long.toString(resultSet.getLong("partnerinformation_id"));
            String patientId = resultSet.getObject("patient_id") == null ? "" : Long.toString(resultSet.getLong("patient_id"));
            String facilityId = resultSet.getObject("facility_id") == null ? "" : Long.toString(resultSet.getLong("facility_id"));
            String dateVisit = resultSet.getObject("date_visit") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_visit"), "MM/dd/yyyy");
            String partnerNotification = resultSet.getObject("partner_notification") == null ? "" : resultSet.getString("partner_notification");
            String partnerHivStatus = resultSet.getObject("partner_hiv_status") == null ? "" : resultSet.getString("partner_hiv_status");
            String partnerReferred = resultSet.getObject("partner_referred") == null ? "" : resultSet.getString("partner_referred");

            Map<String, String> map = new HashMap<String, String>();
            map.put("partnerinformationId", partnerinformationId);
            map.put("patientId", patientId);
            map.put("facilityId", facilityId);
            map.put("dateVisit", dateVisit);
            map.put("partnerNotification", partnerNotification);
            map.put("partnerHivStatus", partnerHivStatus);
            map.put("partnerReferred", partnerReferred);
            partnerinformationList.add(map);
        }
        session.setAttribute("partnerinformationList", partnerinformationList);
    }

    public ArrayList<Map<String, String>> retrievePartnerinformationList() {
        // retrieve the partnerinformation record store in session attribute
        if (session.getAttribute("partnerinformationList") != null) {
            partnerinformationList = (ArrayList) session.getAttribute("partnerinformationList");
        }
        return partnerinformationList;
    }

    public void clearPartnerinformationList() {
        partnerinformationList = retrievePartnerinformationList();
        partnerinformationList.clear();
        session.setAttribute("partnerinformationList", partnerinformationList);
    }

}
