/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility.builder;

/**
 * @author user1
 */

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.Scrambler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevolveListBuilder {
    private HttpServletRequest request;
    private HttpSession session;
    private Boolean viewIdentifier;
    private Scrambler scrambler;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;


    private List<Map<String, String>> devolveList = new ArrayList<>();
    private ArrayList<Map<String, String>> patientList = new ArrayList<Map<String, String>>();

    public DevolveListBuilder() {
        this.scrambler = new Scrambler();
        /*if (ServletActionContext.getRequest().getSession().getAttribute("viewIdentifier") != null) {
            this.viewIdentifier = (Boolean) session.getAttribute("viewIdentifier");
        }*/
    }

    public void buildDevolveDetailsList(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String patientId = Long.toString(resultSet.getLong("patient_id"));
            String hospitalNum = resultSet.getString("hospital_num");
            String uniqueId = resultSet.getString("unique_id") == null ? "" : resultSet.getString("unique_id");
            String surname = (viewIdentifier) ? scrambler.unscrambleCharacters(resultSet.getString("surname")) : resultSet.getString("surname");
            surname = StringUtils.upperCase(surname);
            String otherNames = (viewIdentifier) ? scrambler.unscrambleCharacters(resultSet.getString("other_names")) : resultSet.getString("other_names");
            otherNames = StringUtils.capitalize(otherNames);
            String gender = resultSet.getString("gender");
            String age = Integer.toString(resultSet.getInt("age"));
            System.out.println("Surname...." + surname);

            //Devolvement information
            String devolveId = Long.toString(resultSet.getLong("devolve_id"));
            String dateDevolved = resultSet.getObject("date_devolved") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_devolved"), "MM/dd/yyyy");
            String typeDmoc = resultSet.getObject("type_dmoc") == null ? "" : resultSet.getString("type_dmoc");
            String viralLoadAssessed = resultSet.getObject("viral_load_assessed") == null ? "" : resultSet.getString("viral_load_assessed");
            String lastViralLoad = resultSet.getObject("last_viral_load") == null ? "" : Double.toString(resultSet.getDouble("last_viral_load"));
            String dateLastViralLoad = resultSet.getObject("date_last_viral_load") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_viral_load"), "MM/dd/yyyy");
            String cd4Assessed = resultSet.getObject("cd4_assessed") == null ? "" : resultSet.getString("cd4_assessed");
            String lastCd4 = resultSet.getObject("last_cd4") == null ? "" : Double.toString(resultSet.getDouble("last_cd4"));
            String dateLastCd4 = resultSet.getObject("date_last_cd4") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_cd4"), "MM/dd/yyyy");
            String lastClinicStage = resultSet.getObject("last_clinic_stage") == null ? "" : resultSet.getString("last_clinic_stage");
            String dateLastClinicStage = resultSet.getObject("date_last_clinic_stage") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_clinic_stage"), "MM/dd/yyyy");
            String regimentype = resultSet.getObject("regimenType") == null ? "" : resultSet.getString("regimenType");
            String regimen = resultSet.getObject("regimen") == null ? "" : resultSet.getString("regimen");
            String arvDispensed = resultSet.getObject("arv_dispensed") == null ? "" : resultSet.getString("arv_dispensed");
            String dateNextClinic = resultSet.getObject("date_next_clinic") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_clinic"), "MM/dd/yyyy");
            String dateNextRefill = resultSet.getObject("date_next_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_refill"), "MM/dd/yyyy");
            String dateLastClinic = resultSet.getObject("date_last_clinic") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_clinic"), "MM/dd/yyyy");
            String dateLastRefill = resultSet.getObject("date_last_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_refill"), "MM/dd/yyyy");
            String notes = resultSet.getString("notes") == null ? "" : resultSet.getString("notes");
            System.out.println("Last CD4...." + lastCd4);

            //ART Commencement details
            String dateStarted = "";
            String clinicStageCommence = "";
            String cd4 = "";
            String cd4p = "";
            String cd4Commence = "";
            String regimentypeCommence = "";
            String regimenCommence = "";

            String query = "SELECT * FROM clinic WHERE patient_id = " + resultSet.getLong("patient_id") + " AND commence = 1";
            ResultSet rs = executeQuery(query);
            if (rs.next()) {
                dateStarted = rs.getObject("date_visit") == null ? "" : DateUtil.parseDateToString(rs.getDate("date_visit"), "MM/dd/yyyy");
                clinicStageCommence = rs.getObject("clinic_stage_commence") == null ? "" : rs.getString("clinic_stage_commence");
                cd4 = rs.getObject("cd4_commence") == null ? "" : rs.getDouble("cd4_commence") == 0.0 ? "" : Double.toString(rs.getDouble("cd4_commence"));
                cd4p = rs.getObject("cd4p_commence") == null ? "" : rs.getDouble("cd4p_commence") == 0.0 ? "" : Double.toString(rs.getDouble("cd4p_commence"));
                cd4Commence = cd4.equals("") ? cd4p : "";
                regimentypeCommence = rs.getObject("regimentype_commence") == null ? "" : rs.getString("regimentype_commence");
                regimenCommence = rs.getObject("regimentype_commence") == null ? "" : rs.getString("regimen_commence");
            }

            //Community pharmacy details
            String stateId = "";
            String lgaId = "";
            String pharmacy = "";
            String address = "";
            String phone = "";

            String communitypharmId = Long.toString(resultSet.getLong("communitypharm_id"));
            query = "SELECT * FROM communitypharm WHERE communitypharm_id = " + resultSet.getLong("communitypharm_id");
            rs = executeQuery(query);
            if (rs.next()) {
                stateId = Long.toString(rs.getLong("state_id"));
                lgaId = Long.toString(rs.getLong("lga_id"));
                pharmacy = rs.getObject("pharmacy") == null ? "" : rs.getString("pharmacy");
                address = rs.getObject("address") == null ? "" : rs.getString("address");
                phone = rs.getObject("phone") == null ? "" : rs.getString("phone");

            }

            Map<String, String> map = new HashMap<String, String>();
            map.put("patientId", patientId);
            map.put("hospitalNum", hospitalNum);
            map.put("uniqueId", uniqueId);
            map.put("surname", surname);
            map.put("otherNames", otherNames);
            map.put("name", surname + ' ' + otherNames);
            map.put("gender", gender);
            map.put("age", age);

            map.put("devolveId", devolveId);
            map.put("dateDevolved", dateDevolved);
            map.put("typeDmoc", typeDmoc);
            map.put("viralLoadAssessed", viralLoadAssessed);
            map.put("lastViralLoad", lastViralLoad);
            map.put("dateLastViralLoad", dateLastViralLoad);
            map.put("cd4Assessed", cd4Assessed);
            map.put("lastCd4", lastCd4);
            map.put("dateLastCd4", dateLastCd4);
            map.put("lastClinicStage", lastClinicStage);
            map.put("dateLastClinicStage", dateLastClinicStage);
            map.put("regimentype", regimentype);
            map.put("regimen", regimen);
            map.put("arvDispensed", arvDispensed);
            map.put("dateNextClinic", dateNextClinic);
            map.put("dateNextRefill", dateNextRefill);
            map.put("dateLastClinic", dateLastClinic);
            map.put("dateLastRefill", dateLastRefill);
            map.put("notes", notes);

            map.put("dateStarted", dateStarted);
            map.put("clinicStageCommence", clinicStageCommence);
            map.put("cd4Commence", cd4Commence);
            map.put("regimentypeCommence", regimentypeCommence);
            map.put("regimenCommence", regimenCommence);

            map.put("communitypharmId", communitypharmId);
            map.put("pharmacy", pharmacy);
            map.put("address", address);
            map.put("phone", phone);
            map.put("stateId", stateId);
            map.put("lgaId", lgaId);
            devolveList.add(map);
        }
        session.setAttribute("devolveList", devolveList);
    }

    public void buildDevolveList(ResultSet resultSet) throws SQLException {
        try {
            resultSet.beforeFirst();
            while (resultSet.next()) {

                //Devolvement information
                String devolveId = Long.toString(resultSet.getLong("devolve_id"));
                String dateDevolved = resultSet.getObject("date_devolved") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_devolved"), "MM/dd/yyyy");
                String typeDmoc = resultSet.getObject("type_dmoc") == null ? "" : resultSet.getString("type_dmoc");
                String viralLoadAssessed = resultSet.getObject("viral_load_assessed") == null ? "" : resultSet.getString("viral_load_assessed");
                String lastViralLoad = resultSet.getObject("last_viral_load") == null ? "" : Double.toString(resultSet.getDouble("last_viral_load"));
                String dateLastViralLoad = resultSet.getObject("date_last_viral_load") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_viral_load"), "MM/dd/yyyy");
                String cd4Assessed = resultSet.getObject("cd4_assessed") == null ? "" : resultSet.getString("cd4_assessed");
                String lastCd4 = resultSet.getObject("last_cd4") == null ? "" : Double.toString(resultSet.getDouble("last_cd4"));
                String dateLastCd4 = resultSet.getObject("date_last_cd4") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_cd4"), "MM/dd/yyyy");
                String lastClinicStage = resultSet.getObject("last_clinic_stage") == null ? "" : resultSet.getString("last_clinic_stage");
                String dateLastClinicStage = resultSet.getObject("date_last_clinic_stage") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_clinic_stage"), "MM/dd/yyyy");
                String regimentype = resultSet.getObject("regimenType") == null ? "" : resultSet.getString("regimenType");
                String regimen = resultSet.getObject("regimen") == null ? "" : resultSet.getString("regimen");
                String arvDispensed = resultSet.getObject("arv_dispensed") == null ? "" : resultSet.getString("arv_dispensed");
                String dateNextClinic = resultSet.getObject("date_next_clinic") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_clinic"), "MM/dd/yyyy");
                String dateNextRefill = resultSet.getObject("date_next_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_refill"), "MM/dd/yyyy");
                String dateLastClinic = resultSet.getObject("date_last_clinic") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_clinic"), "MM/dd/yyyy");
                String dateLastRefill = resultSet.getObject("date_last_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_refill"), "MM/dd/yyyy");
                String notes = resultSet.getObject("notes") == null ? "" : resultSet.getString("notes");


                //Community pharmacy details
                String stateId = "";
                String lgaId = "";
                String pharmacy = "";
                String address = "";
                String phone = "";

                String communitypharmId = Long.toString(resultSet.getLong("communitypharm_id"));
                String query = "SELECT * FROM communitypharm WHERE communitypharm_id = " + resultSet.getLong("communitypharm_id");
                ResultSet rs = executeQuery(query);
                if (rs.next()) {
                    stateId = Long.toString(rs.getLong("state_id"));
                    lgaId = Long.toString(rs.getLong("lga_id"));
                    pharmacy = rs.getObject("pharmacy") == null ? "" : rs.getString("pharmacy");
                    address = rs.getObject("address") == null ? "" : rs.getString("address");
                    phone = rs.getObject("phone") == null ? "" : rs.getString("phone");

                }

                Map<String, String> map = new HashMap<String, String>();
                map.put("devolveId", devolveId);
                map.put("dateDevolved", dateDevolved);
                map.put("typeDmoc", typeDmoc);
                map.put("viralLoadAssessed", viralLoadAssessed);
                map.put("lastViralLoad", lastViralLoad);
                map.put("dateLastViralLoad", dateLastViralLoad);
                map.put("cd4Assessed", cd4Assessed);
                map.put("lastCd4", lastCd4);
                map.put("dateLastCd4", dateLastCd4);
                map.put("lastClinicStage", lastClinicStage);
                map.put("dateLastClinicStage", dateLastClinicStage);
                map.put("regimentype", regimentype);
                map.put("regimen", regimen);
                map.put("arvDispensed", arvDispensed);
                map.put("dateNextClinic", dateNextClinic);
                map.put("dateNextRefill", dateNextRefill);
                map.put("dateLastClinic", dateLastClinic);
                map.put("dateLastRefill", dateLastRefill);
                map.put("notes", notes);

                map.put("communitypharmId", communitypharmId);
                map.put("pharmacy", pharmacy);
                map.put("addressPharm", address);
                map.put("phonePharm", phone);
                map.put("stateId", stateId);
                map.put("lgaId", lgaId);

                devolveList.add(map);
            }
            session.setAttribute("devolveList", devolveList);
            resultSet = null;
            devolveList = null;
        } catch (SQLException sqlException) {
            resultSet = null;
            throw sqlException;
        }
    }

    public void buildPatientList(ResultSet resultSet) throws SQLException {
        try {
            resultSet.beforeFirst();
            while (resultSet.next()) {
                String patientId = Long.toString(resultSet.getLong("patient_id"));
                String facilityId = Long.toString(resultSet.getLong("facility_id"));
                String hospitalNum = resultSet.getString("hospital_num");
                String uniqueId = resultSet.getObject("unique_id") == null ? "" : resultSet.getString("unique_id");
                String surname = resultSet.getObject("surname") == null ? "" : resultSet.getString("surname");
                surname = (viewIdentifier) ? scrambler.unscrambleCharacters(surname) : surname;
                surname = StringUtils.upperCase(surname);
                String otherNames = resultSet.getObject("other_names") == null ? "" : resultSet.getString("other_names");
                otherNames = (viewIdentifier) ? scrambler.unscrambleCharacters(otherNames) : otherNames;
                otherNames = StringUtils.capitalize(otherNames);
                String gender = resultSet.getObject("gender") == null ? "" : resultSet.getString("gender");
                String dateBirth = resultSet.getObject("date_birth") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_birth"), "MM/dd/yyyy");
                String age = resultSet.getObject("age") == null ? "" : resultSet.getInt("age") == 0 ? "" : Integer.toString(resultSet.getInt("age"));
                String ageUnit = resultSet.getObject("age_unit") == null ? "" : resultSet.getString("age_unit");
                String address = resultSet.getObject("address") == null ? "" : resultSet.getString("address");
                address = (viewIdentifier) ? scrambler.unscrambleCharacters(address) : address;
                address = StringUtils.capitalize(address);

                String lastViralLoad = resultSet.getObject("last_viral_load") == null ? "" : Double.toString(resultSet.getDouble("last_viral_load"));
                String dateLastViralLoad = resultSet.getObject("date_last_viral_load") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_viral_load"), "MM/dd/yyyy");
                String cd4 = resultSet.getObject("last_cd4") == null ? "" : Double.toString(resultSet.getDouble("last_cd4"));
                String cd4p = resultSet.getObject("last_cd4p") == null ? "" : Double.toString(resultSet.getDouble("last_cd4p"));
                String lastCd4 = cd4;
                String dateLastCd4 = resultSet.getObject("date_last_cd4") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_cd4"), "MM/dd/yyyy");
                String lastClinicStage = resultSet.getObject("last_clinic_stage") == null ? "" : resultSet.getString("last_clinic_stage");
                String regimentype = resultSet.getObject("regimenType") == null ? "" : resultSet.getString("regimenType");
                String regimen = resultSet.getObject("regimen") == null ? "" : resultSet.getString("regimen");
                String dateLastClinic = resultSet.getObject("date_last_clinic") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_clinic"), "MM/dd/yyyy");
                String dateLastRefill = resultSet.getObject("date_last_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_last_refill"), "MM/dd/yyyy");
                String dateNextClinic = resultSet.getObject("date_next_clinic") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_clinic"), "MM/dd/yyyy");
                String dateNextRefill = resultSet.getObject("date_next_refill") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_next_refill"), "MM/dd/yyyy");
                System.out.println("Done....");

                //ART Commencement details                
                String dateStarted = (resultSet.getDate("date_visit") == null) ? "" : DateUtil.parseDateToString(resultSet.getDate("date_visit"), "MM/dd/yyyy");
                String clinicStageCommence = resultSet.getString("clinic_stage_commence");
                String cd4Commence = resultSet.getDouble("cd4_commence") == 0.0 ? Double.toString(resultSet.getDouble("cd4p_commence")) : Double.toString(resultSet.getDouble("cd4_commence"));
                String regimentypeCommence = resultSet.getString("regimentype_commence") == null ? "" : resultSet.getString("regimentype_commence");
                String regimenCommence = resultSet.getString("regimentype_commence") == null ? "" : resultSet.getString("regimen_commence");

                // create an array from object properties 
                Map<String, String> map = new HashMap<String, String>();
                map.put("patientId", patientId);
                map.put("facilityId", facilityId);
                map.put("hospitalNum", hospitalNum);
                map.put("uniqueId", uniqueId);
                map.put("surname", surname);
                map.put("otherNames", otherNames);
                map.put("gender", gender);
                map.put("dateBirth", dateBirth);
                map.put("age", age);
                map.put("ageUnit", ageUnit);
                map.put("address", address);

                map.put("lastViralLoad", lastViralLoad);
                map.put("dateLastViralLoad", dateLastViralLoad);
                map.put("lastCd4", lastCd4);
                map.put("dateLastCd4", dateLastCd4);
                map.put("lastClinicStage", lastClinicStage);
                map.put("dateLastClinicStage", dateLastClinic);
                map.put("regimentype", regimentype);
                map.put("regimen", regimen);
                map.put("dateLastClinic", dateLastClinic);
                map.put("dateLastRefill", dateLastRefill);
                map.put("dateNextClinic", dateNextClinic);
                map.put("dateNextRefill", dateNextRefill);
                map.put("dateStarted", dateStarted);
                map.put("clinicStageCommence", clinicStageCommence);
                map.put("cd4Commence", cd4Commence);
                map.put("regimentypeCommence", regimentypeCommence);
                map.put("regimenCommence", regimenCommence);
                patientList.add(map);
            }
            session.setAttribute("patientList", patientList);
            resultSet = null;
            patientList = null;
        } catch (SQLException sqlException) {
            resultSet = null;
            throw sqlException;
        }
    }

    public List<Map<String, String>> retrieveDevolveList() {
        if (session.getAttribute("devolveList") != null) {
            devolveList = (ArrayList) session.getAttribute("devolveList");
        }
        return devolveList;
    }

    public void clearDevolveList() {
        devolveList = retrieveDevolveList();
        devolveList.clear();
        session.setAttribute("devolveList", devolveList);
    }


    private ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return rs;
    }


}
