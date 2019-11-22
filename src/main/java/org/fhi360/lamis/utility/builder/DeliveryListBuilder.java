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

public class DeliveryListBuilder {
    private HttpServletRequest request;
    private HttpSession session;

    private ArrayList<Map<String, String>> deliveryList = new ArrayList<Map<String, String>>();

    public DeliveryListBuilder() {
    }

    public void buildDeliveryList(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String deliveryId = resultSet.getObject("delivery_id") == null ? "" : Long.toString(resultSet.getLong("delivery_id"));
            String patientId = resultSet.getObject("patient_id") == null ? "" : Long.toString(resultSet.getLong("patient_id"));
            String facilityId = resultSet.getObject("facility_id") == null ? "" : Long.toString(resultSet.getLong("facility_id"));
            String ancId = resultSet.getObject("anc_id") == null ? "" : Long.toString(resultSet.getLong("anc_id"));
            String bookingStatus = resultSet.getObject("booking_status") == null ? "" : Integer.toString(resultSet.getInt("booking_status"));
            String dateDelivery = resultSet.getObject("date_delivery") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_delivery"), "MM/dd/yyyy");
            String romDeliveryInterval = resultSet.getObject("rom_delivery_interval") == null ? "" : resultSet.getString("rom_delivery_interval");
            String modeDelivery = resultSet.getObject("mode_delivery") == null ? "" : resultSet.getString("mode_delivery");
            String episiotomy = resultSet.getObject("episiotomy") == null ? "" : resultSet.getString("episiotomy");
            String vaginalTear = resultSet.getObject("vaginal_tear") == null ? "" : resultSet.getString("vaginal_tear");
            String maternalOutcome = resultSet.getObject("maternal_outcome") == null ? "" : resultSet.getString("maternal_outcome");
            String timeHivDiagnosis = resultSet.getObject("time_hiv_diagnosis") == null ? "" : resultSet.getString("time_hiv_diagnosis");
            String sourceReferral = resultSet.getObject("source_referral") == null ? "" : resultSet.getString("source_referral");
            String hepatitisBStatus = resultSet.getObject("hepatitisb_status") == null ? "" : resultSet.getString("hepatitisb_status");
            String hepatitisCStatus = resultSet.getObject("hepatitisc_status") == null ? "" : resultSet.getString("hepatitisc_status");
            String gestationalAge = resultSet.getObject("gestational_age") == null ? "" : String.valueOf(resultSet.getInt("gestational_age"));
            String screenPostPartum = resultSet.getObject("screen_post_partum") == null ? "" : Integer.toString(resultSet.getInt("screen_post_partum"));
            String arvRegimenPast = resultSet.getObject("arv_regimen_past") == null ? "" : resultSet.getString("arv_regimen_past");
            String arvRegimenCurrent = resultSet.getObject("arv_regimen_current") == null ? "" : resultSet.getString("arv_regimen_current");
            String clinicStage = resultSet.getObject("clinic_stage") == null ? "" : resultSet.getString("clinic_stage");
            String cd4Ordered = resultSet.getObject("cd4_ordered") == null ? "" : resultSet.getString("cd4_ordered");
            String cd4 = resultSet.getObject("cd4") == null ? "" : Double.toString(resultSet.getDouble("cd4"));
            String dateArvRegimenCurrent = resultSet.getObject("date_arv_regimen_current") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_arv_regimen_current"), "MM/dd/yyyy");
            String dateConfirmedHiv = resultSet.getObject("date_confirmed_hiv") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_confirmed_hiv"), "MM/dd/yyyy");

            Map<String, String> map = new HashMap<String, String>();
            map.put("deliveryId", deliveryId);
            map.put("patientId", patientId);
            map.put("facilityId", facilityId);
            map.put("ancId", ancId);
            map.put("bookingStatus", bookingStatus);
            map.put("dateDelivery", dateDelivery);
            map.put("romDeliveryInterval", romDeliveryInterval);
            map.put("modeDelivery", modeDelivery);
            map.put("episiotomy", episiotomy);
            map.put("vaginalTear", vaginalTear);
            map.put("maternalOutcome", maternalOutcome);
            map.put("timeHivDiagnosis", timeHivDiagnosis);
            map.put("screenPostPartum", screenPostPartum);
            map.put("sourceReferral", sourceReferral);
            map.put("hepatitisBStatus", hepatitisBStatus);
            map.put("hepatitisCStatus", hepatitisCStatus);
            map.put("gestationalAge", gestationalAge);
            map.put("arvRegimenPast", arvRegimenPast);
            map.put("arvRegimenCurrent", arvRegimenCurrent);
            map.put("clinicStage", clinicStage);
            map.put("cd4Ordered", cd4Ordered);
            map.put("cd4", cd4);
            map.put("dateArvRegimenCurrent", dateArvRegimenCurrent);
            map.put("dateConfirmedHiv", dateConfirmedHiv);
            deliveryList.add(map);
        }
        session.setAttribute("deliveryList", deliveryList);
    }

    public ArrayList<Map<String, String>> retrieveDeliveryList() {
        // retrieve the delivery record store in session attribute
        if (session.getAttribute("deliveryList") != null) {
            deliveryList = (ArrayList) session.getAttribute("deliveryList");
        }
        return deliveryList;
    }

    public void clearDeliveryList() {
        deliveryList = retrieveDeliveryList();
        deliveryList.clear();
        session.setAttribute("deliveryList", deliveryList);
    }

}
