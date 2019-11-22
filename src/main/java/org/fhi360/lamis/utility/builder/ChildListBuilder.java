/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.fhi360.lamis.utility.builder;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.model.Child;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.Scrambler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ChildListBuilder {
    private HttpServletRequest request;
    private HttpSession session;
    private Boolean viewIdentifier;
    private Scrambler scrambler;

    private ArrayList<Map<String, String>> childList = new ArrayList<>();
    private ArrayList<Map<String, String>> childListWithMother;

    private Map<String, Map<String, String>> sortedMaps = new TreeMap<String, Map<String, String>>();

    public ChildListBuilder() {
        this.scrambler = new Scrambler();
       /* if (ServletActionContext.getRequest().getSession().getAttribute("viewIdentifier") != null) {
            this.viewIdentifier = (Boolean) session.getAttribute("viewIdentifier");
        }
*/
    }

    public void buildChildList(Child child) {
        try {
            String childId = Long.toString(child.getChildId());
            String patientId = child.getPatient() != null ? Long.toString(child.getPatient().getPatientId()) : "";
            String facilityId = Long.toString(child.getFacilityId());
            String deliveryId = child.getDelivery().getDeliveryId() != null ? child.getDelivery().getDeliveryId().toString() : "";
            String ancId = child.getAnc().getAncId() != null ? child.getAnc().getAncId().toString() : "";
            String hospitalNum = child.getHospitalNumber();
            String referenceNum = child.getReferenceNum();
            String surname = (viewIdentifier) ? scrambler.unscrambleCharacters(child.getSurname()) : child.getSurname();
            surname = surname.toUpperCase();
            String arv = child.getArv();
            String hepb = child.getHepb();
            String hbv = child.getHbv();
            String registrationStatus = child.getRegistrationStatus();
            String otherNames = (viewIdentifier) ? scrambler.unscrambleCharacters(child.getOtherNames()) : child.getOtherNames();
            String dateBirth = DateUtil.parseDateToString(child.getDateBirth(), "MM/dd/yyyy");
            String gender = child.getGender();
            String bodyWeight = Double.toString(child.getBodyWeight());
            String apgarScore = child.getApgarScore() == null ? "0" : Integer.toString(child.getApgarScore());
            String status = child.getStatus();
            String motherId = Long.toString(child.getMother().getMotherinformationId());

            Map<String, String> map = new HashMap<String, String>();
            map.put("childId", childId);
            map.put("patientId", patientId);
            map.put("facilityId", facilityId);
            map.put("deliveryId", deliveryId);
            map.put("ancId", ancId);
            map.put("referenceNum", referenceNum);
            map.put("hospitalNum", hospitalNum);
            map.put("surname", surname);
            map.put("otherNames", otherNames);
            map.put("childName", surname + " " + otherNames);
            map.put("arv", arv);
            map.put("hepb", hepb);
            map.put("hbv", hbv);
            map.put("registrationStatus", registrationStatus);
            map.put("dateBirth", dateBirth);
            map.put("gender", gender);
            map.put("bodyWeight", bodyWeight);
            map.put("apgarScore", apgarScore);
            map.put("status", status);
            map.put("motherId", motherId);
            childList.add(map);
            session.setAttribute("childList", childList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void buildChildList(ResultSet resultSet, String option) throws SQLException {
        try {
            if (option.equals(Constants.Pmtct.Child.WITH_MOTHER))
                childListWithMother = new ArrayList<>();
            else
                childListWithMother = (ArrayList<Map<String, String>>) session.getAttribute("childListWithMother");
            // loop through resultSet for each row and put into Map
            resultSet.beforeFirst();
            while (resultSet.next()) {
                String childId = resultSet.getObject("child_id") == null ? "" : Long.toString(resultSet.getLong("child_id"));
                String patientId = resultSet.getObject("patient_id") == null ? "" : Long.toString(resultSet.getLong("patient_id"));
                String facilityId = resultSet.getObject("facility_id") == null ? "" : Long.toString(resultSet.getLong("facility_id"));
                String deliveryId = resultSet.getObject("delivery_id") == null ? "" : Long.toString(resultSet.getLong("delivery_id"));
                String ancId = resultSet.getObject("anc_id") == null ? "" : Long.toString(resultSet.getLong("anc_id"));
                String hospitalNum = resultSet.getObject("hospital_number") == null ? "" : resultSet.getString("hospital_number");
                String referenceNum = resultSet.getObject("reference_num") == null ? "" : resultSet.getString("reference_num");
                String registrationStatus = resultSet.getObject("registration_status") == null ? "" : resultSet.getString("registration_status");
                String surname = resultSet.getObject("surname") == null ? "" : resultSet.getString("surname");
                surname = (viewIdentifier) ? scrambler.unscrambleCharacters(surname) : surname;
                surname = StringUtils.upperCase(surname);
                String otherNames = resultSet.getObject("other_names") == null ? "" : resultSet.getString("other_names");
                otherNames = (viewIdentifier) ? scrambler.unscrambleCharacters(otherNames) : otherNames;
                String dateBirth = resultSet.getObject("date_birth") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_birth"), "MM/dd/yyyy");
                String gender = resultSet.getObject("gender") == null ? "" : resultSet.getString("gender");
                String arv = resultSet.getObject("arv") == null ? "" : resultSet.getString("arv");
                String hepb = resultSet.getObject("hepb") == null ? "" : resultSet.getString("hepb");
                String hbv = resultSet.getObject("hbv") == null ? "" : resultSet.getString("hbv");
                String bodyWeight = resultSet.getObject("body_weight") == null ? "" : Double.toString(resultSet.getDouble("body_weight"));
                String apgarScore = resultSet.getObject("apgar_score") == null ? "" : Integer.toString(resultSet.getInt("apgar_score"));
                String status = resultSet.getObject("status") == null ? "N/A" : resultSet.getString("status");
                String surnameMother = "N/A", otherNamesMother = "N/A", inFacility = "N/A";
                String motherId = "";
                if (patientId.equals("")) motherId = resultSet.getObject("mother_id") == null ?
                        "N/A" : Long.toString(resultSet.getLong("mother_id"));
                if (option.equals(Constants.Pmtct.Child.WITH_MOTHER)) {
                    surnameMother = resultSet.getObject("surname_mother") == null ? "" : resultSet.getString("surname_mother");
                    surnameMother = (viewIdentifier) ? scrambler.unscrambleCharacters(surnameMother) : surnameMother;
                    surnameMother = StringUtils.upperCase(surnameMother);
                    otherNamesMother = resultSet.getObject("other_names_mother") == null ? "" : resultSet.getString("other_names_mother");
                    otherNamesMother = (viewIdentifier) ? scrambler.unscrambleCharacters(otherNamesMother) : otherNamesMother;
                    otherNamesMother = StringUtils.capitalize(otherNamesMother);
                    inFacility = resultSet.getObject("in_facility") == null ? "N/A" : resultSet.getString("in_facility");
                }

                Map<String, String> map = new HashMap<String, String>();
                map.put("childId", childId);
                map.put("patientId", patientId);
                map.put("facilityId", facilityId);
                map.put("deliveryId", deliveryId);
                map.put("ancId", ancId);
                map.put("referenceNum", referenceNum);
                map.put("registrationStatus", registrationStatus);
                map.put("hospitalNumber", hospitalNum);
                map.put("surname", surname);
                map.put("otherNames", otherNames);
                map.put("childName", surname + " " + otherNames);
                map.put("dateBirth", dateBirth);
                map.put("gender", gender);
                map.put("arv", arv);
                map.put("hepb", hepb);
                map.put("hbv", hbv);
                map.put("bodyWeight", bodyWeight);
                map.put("apgarScore", apgarScore);
                map.put("status", status);
                map.put("nameMother", surnameMother + " " + otherNamesMother);
                map.put("inFacility", inFacility);
                map.put("motherId", motherId);
                if (!StringUtils.isEmpty(map.get("childId")))
                    childListWithMother.add(map);
            }
            childList = childListWithMother;
            session.setAttribute("childListWithMother", childListWithMother);
            session.setAttribute("childList", childList);
        } catch (SQLException sqlException) {
            throw sqlException;
        }
    }

    public void buildChildListSorted(ResultSet resultSet) throws SQLException {
        clearChildList();
        while (resultSet.next()) {
            String childId = resultSet.getObject("child_id") == null ? "" : Long.toString(resultSet.getLong("child_id"));
            String patientId = resultSet.getObject("patient_id") == null ? "" : Long.toString(resultSet.getLong("patient_id"));
            String facilityId = resultSet.getObject("facility_id") == null ? "" : Long.toString(resultSet.getLong("facility_id"));
            String deliveryId = resultSet.getObject("delivery_id") == null ? "" : Long.toString(resultSet.getLong("delivery_id"));
            String ancId = resultSet.getObject("anc_id") == null ? "" : Long.toString(resultSet.getLong("anc_id"));
            String hospitalNum = resultSet.getObject("hospital_number") == null ? "" : resultSet.getString("hospital_number");
            String referenceNum = resultSet.getObject("reference_num") == null ? "" : resultSet.getString("reference_num");
            String registrationStatus = resultSet.getObject("registration_status") == null ? "" : resultSet.getString("registration_status");
            String surname = resultSet.getObject("surname") == null ? "" : resultSet.getString("surname");
            surname = (viewIdentifier) ? scrambler.unscrambleCharacters(surname) : surname;
            surname = StringUtils.upperCase(surname);
            String otherNames = resultSet.getObject("other_names") == null ? "" : resultSet.getString("other_names");
            otherNames = (viewIdentifier) ? scrambler.unscrambleCharacters(otherNames) : otherNames;
            String dateBirth = resultSet.getObject("date_birth") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_birth"), "MM/dd/yyyy");
            String gender = resultSet.getObject("gender") == null ? "" : resultSet.getString("gender");
            String arv = resultSet.getObject("arv") == null ? "" : resultSet.getString("arv");
            String hepb = resultSet.getObject("hepb") == null ? "" : resultSet.getString("hepb");
            String hbv = resultSet.getObject("hbv") == null ? "" : resultSet.getString("hbv");
            String bodyWeight = resultSet.getObject("body_weight") == null ? "" : Double.toString(resultSet.getDouble("body_weight"));
            String apgarScore = resultSet.getObject("apgar_score") == null ? "" : Integer.toString(resultSet.getInt("apgar_score"));
            String status = resultSet.getObject("status") == null ? "N/A" : resultSet.getString("status");
            String surnameMother = "N/A", otherNamesMother = "N/A", inFacility = "N/A";
            String motherId = resultSet.getObject("mother_id") == null ?
                    "N/A" : Long.toString(resultSet.getLong("mother_id"));
            surnameMother = resultSet.getObject("surname_mother") == null ? "" : resultSet.getString("surname_mother");
            surnameMother = (viewIdentifier) ? scrambler.unscrambleCharacters(surnameMother) : surnameMother;
            surnameMother = StringUtils.upperCase(surnameMother);
            otherNamesMother = resultSet.getObject("other_names_mother") == null ? "" : resultSet.getString("other_names_mother");
            otherNamesMother = (viewIdentifier) ? scrambler.unscrambleCharacters(otherNamesMother) : otherNamesMother;
            otherNamesMother = StringUtils.capitalize(otherNamesMother);
            inFacility = resultSet.getObject("in_facility") == null ? "N/A" : resultSet.getString("in_facility");

            Map<String, String> map = new HashMap<String, String>();
            map.put("childId", childId);
            map.put("patientId", patientId);
            map.put("facilityId", facilityId);
            map.put("deliveryId", deliveryId);
            map.put("ancId", ancId);
            map.put("referenceNum", referenceNum);
            map.put("registrationStatus", registrationStatus);
            map.put("hospitalNumber", hospitalNum);
            map.put("surname", surname);
            map.put("otherNames", otherNames);
            map.put("childName", surname + " " + otherNames);
            map.put("dateBirth", dateBirth);
            map.put("gender", gender);
            map.put("arv", arv);
            map.put("hepb", hepb);
            map.put("hbv", hbv);
            map.put("bodyWeight", bodyWeight);
            map.put("apgarScore", apgarScore);
            map.put("status", status);
            map.put("nameMother", surnameMother + " " + otherNamesMother);
            map.put("inFacility", inFacility);
            map.put("motherId", motherId);
            sortedMaps.put(surname + otherNames, map);
        }
        for (Map.Entry<String, Map<String, String>> entry : sortedMaps.entrySet()) {
            childList.add(entry.getValue());
        }
        session.setAttribute("childList", childList);
    }

    public ArrayList<Map<String, String>> retrieveChildList() {
        // retrieve the child record store in session attribute
        if (session.getAttribute("childList") != null) {
            childList = (ArrayList) session.getAttribute("childList");
        }
        return childList;
    }

    public void clearChildList() {
        childList = retrieveChildList();
        childList.clear();
        session.setAttribute("childList", childList);
        session.setAttribute("childListWithMother", childListWithMother);
    }

}
