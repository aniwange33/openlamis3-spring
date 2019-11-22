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
public class HtsListBuilder {

    private HttpServletRequest request;
    private HttpSession session;
    private Boolean viewIdentifier;
    private Scrambler scrambler;

    private ArrayList<Map<String, String>> htsList = new ArrayList<Map<String, String>>();

    public HtsListBuilder() {
    }

    public void buildHtsList(ResultSet resultSet) throws SQLException {
        try {
            // loop through resultSet for each row and put into Map
            resultSet.beforeFirst();
            while (resultSet.next()) {

                String facilityId = Long.toString(resultSet.getLong("facility_id"));
                String clientCode = resultSet.getObject("client_code") == null ? "" : resultSet.getString("client_code");
                String htsId = resultSet.getObject("hts_id") == null ? "" : Long.toString(resultSet.getLong("hts_id"));
                String surname = resultSet.getObject("surname") == null ? "" : resultSet.getString("surname");
                //surname = (viewIdentifier)? scrambler.unscrambleCharacters(surname) : surname;
                surname = StringUtils.upperCase(surname);
                String otherNames = resultSet.getObject("other_names") == null ? "" : resultSet.getString("other_names");
                //otherNames = (viewIdentifier)? scrambler.unscrambleCharacters(otherNames) : otherNames;
                otherNames = StringUtils.capitalize(otherNames);
                String gender = resultSet.getObject("gender") == null ? "" : resultSet.getString("gender");

                String dateBirth = resultSet.getObject("date_birth") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_birth"), "MM/dd/yyyy");
                String dateVisit = resultSet.getObject("date_visit") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_visit"), "MM/dd/yyyy");
                String assessmentId = resultSet.getObject("assessment_id") == null ? "" : Long.toString(resultSet.getLong("assessment_id"));

                String hospitalNum = resultSet.getObject("hospital_num") == null ? "" : resultSet.getString("hospital_num");
                String referredFrom = resultSet.getObject("referred_from") == null ? "" : resultSet.getString("referred_from");
                String testingSetting = resultSet.getObject("testing_setting") == null ? "" : resultSet.getString("testing_setting");
                String age = resultSet.getObject("age") == null ? "" : resultSet.getString("age");
                String ageUnit = resultSet.getObject("age_unit") == null ? "" : resultSet.getString("age_unit");
                String phone = resultSet.getObject("phone") == null ? "" : resultSet.getString("phone");

                String address = resultSet.getObject("address") == null ? "" : resultSet.getString("address");
                String firstTimeVisit = resultSet.getObject("first_time_visit") == null ? "" : resultSet.getString("first_time_visit");
                String stateId = resultSet.getObject("state_id") == null ? "" : Long.toString(resultSet.getLong("state_id"));
                String lgaId = resultSet.getObject("lga_id") == null ? "" : Long.toString(resultSet.getLong("lga_id"));

                String state = resultSet.getObject("state") == null ? "" : resultSet.getString("state");
                String lga = resultSet.getObject("lga") == null ? "" : resultSet.getString("lga");
                String maritalStatus = resultSet.getObject("marital_status") == null ? "" : resultSet.getString("marital_status");
                String numChildren = resultSet.getObject("num_children") == null ? "" : resultSet.getString("num_children");
                String numWives = resultSet.getObject("num_wives") == null ? "" : resultSet.getString("num_wives");
                String typeCounseling = resultSet.getObject("type_counseling") == null ? "" : resultSet.getString("type_counseling");
                String indexClient = resultSet.getObject("index_client") == null ? "" : resultSet.getString("index_client");
                String typeIndex = resultSet.getObject("type_index") == null ? "" : resultSet.getString("type_index");
              
                String indexClientCode = resultSet.getObject("index_client_code") == null ? "" : resultSet.getString("index_client_code");

                String hivTestResult = resultSet.getObject("hiv_test_result") == null ? "" : resultSet.getString("hiv_test_result");

                String knowledgeAssessment1 = resultSet.getObject("knowledge_assessment1") == null ? "" : resultSet.getString("knowledge_assessment1");
                String knowledgeAssessment2 = resultSet.getObject("knowledge_assessment2") == null ? "" : resultSet.getString("knowledge_assessment2");
                String knowledgeAssessment3 = resultSet.getObject("knowledge_assessment3") == null ? "" : resultSet.getString("knowledge_assessment3");
                String knowledgeAssessment4 = resultSet.getObject("knowledge_assessment4") == null ? "" : resultSet.getString("knowledge_assessment4");
                String knowledgeAssessment5 = resultSet.getObject("knowledge_assessment5") == null ? "" : resultSet.getString("knowledge_assessment5");
                String knowledgeAssessment6 = resultSet.getObject("knowledge_assessment6") == null ? "" : resultSet.getString("knowledge_assessment6");
                String knowledgeAssessment7 = resultSet.getObject("knowledge_assessment7") == null ? "" : resultSet.getString("knowledge_assessment7");

                String tbScreening1 = resultSet.getObject("tb_screening1") == null ? "" : resultSet.getString("tb_screening1");
                String tbScreening2 = resultSet.getObject("tb_screening2") == null ? "" : resultSet.getString("tb_screening2");
                String tbScreening3 = resultSet.getObject("tb_screening3") == null ? "" : resultSet.getString("tb_screening3");
                String tbScreening4 = resultSet.getObject("tb_screening4") == null ? "" : resultSet.getString("tb_screening4");

                String stiScreening1 = resultSet.getObject("sti_screening1") == null ? "" : resultSet.getString("sti_screening1");
                String stiScreening2 = resultSet.getObject("sti_screening2") == null ? "" : resultSet.getString("sti_screening2");
                String stiScreening3 = resultSet.getObject("sti_screening3") == null ? "" : resultSet.getString("sti_screening3");
                String stiScreening4 = resultSet.getObject("sti_screening4") == null ? "" : resultSet.getString("sti_screening4");
                String stiScreening5 = resultSet.getObject("sti_screening5") == null ? "" : resultSet.getString("sti_screening5");

                String riskAssessment1 = resultSet.getObject("risk_assessment1") == null ? "" : resultSet.getString("risk_assessment1");
                String riskAssessment2 = resultSet.getObject("risk_assessment2") == null ? "" : resultSet.getString("risk_assessment2");
                String riskAssessment3 = resultSet.getObject("risk_assessment3") == null ? "" : resultSet.getString("risk_assessment3");
                String riskAssessment4 = resultSet.getObject("risk_assessment4") == null ? "" : resultSet.getString("risk_assessment4");
                String riskAssessment5 = resultSet.getObject("risk_assessment5") == null ? "" : resultSet.getString("risk_assessment5");
                String riskAssessment6 = resultSet.getObject("risk_assessment6") == null ? "" : resultSet.getString("risk_assessment6");

                String testedHiv = resultSet.getObject("tested_hiv") == null ? "" : resultSet.getString("tested_hiv");

                String postTest1 = resultSet.getObject("post_test1") == null ? "" : resultSet.getString("post_test1");
                String postTest2 = resultSet.getObject("post_test2") == null ? "" : resultSet.getString("post_test2");
                String postTest3 = resultSet.getObject("post_test3") == null ? "" : resultSet.getString("post_test3");
                String postTest4 = resultSet.getObject("post_test4") == null ? "" : resultSet.getString("post_test4");
                String postTest5 = resultSet.getObject("post_test5") == null ? "" : resultSet.getString("post_test5");
                String postTest6 = resultSet.getObject("post_test6") == null ? "" : resultSet.getString("post_test6");
                String postTest7 = resultSet.getObject("post_test7") == null ? "" : resultSet.getString("post_test7");
                String postTest8 = resultSet.getObject("post_test8") == null ? "" : resultSet.getString("post_test8");
                String postTest9 = resultSet.getObject("post_test9") == null ? "" : resultSet.getString("post_test9");
                String postTest10 = resultSet.getObject("post_test10") == null ? "" : resultSet.getString("post_test10");
                String postTest11 = resultSet.getObject("post_test11") == null ? "" : resultSet.getString("post_test11");
                String postTest12 = resultSet.getObject("post_test12") == null ? "" : resultSet.getString("post_test12");
                String postTest13 = resultSet.getObject("post_test13") == null ? "" : resultSet.getString("post_test13");
                String postTest14 = resultSet.getObject("post_test14") == null ? "" : resultSet.getString("post_test14");

                String syphilisTestResult = resultSet.getObject("syphilis_test_result") == null ? "" : resultSet.getString("syphilis_test_result");
                String hepatitisbTestResult = resultSet.getObject("hepatitisb_test_result") == null ? "" : resultSet.getString("hepatitisb_test_result");
                String hepatitiscTestResult = resultSet.getObject("hepatitisc_test_result") == null ? "" : resultSet.getString("hepatitisc_test_result");
                String note = resultSet.getObject("note") == null ? "" : resultSet.getString("note");

                String artReferred = resultSet.getObject("art_referred") == null ? "" : resultSet.getString("art_referred");
                String tbReferred = resultSet.getObject("tb_referred") == null ? "" : resultSet.getString("tb_referred");
                String stiReferred = resultSet.getObject("sti_referred") == null ? "" : resultSet.getString("sti_referred");
                String timeStamp = resultSet.getObject("time_stamp") == null ? "" : resultSet.getString("time_stamp");
                String idUUID = resultSet.getObject("id_UUID") == null ? "" : resultSet.getString("id_UUID");

                // create an array from object properties 
                Map<String, String> map = new HashMap<>();
                map.put("htsId", htsId);
                map.put("clientCode", clientCode);
                map.put("assessmentId", assessmentId);

                map.put("facilityId", facilityId);
                map.put("hospitalNum", hospitalNum);
                map.put("referredFrom", referredFrom);
                map.put("testingSetting", testingSetting);
                map.put("dateBirth", dateBirth);
                map.put("age", age);
                map.put("ageUnit", ageUnit);
                map.put("phone", phone);
                map.put("surname", surname);
                map.put("otherNames", otherNames);
                map.put("name", surname + ' ' + otherNames);
                map.put("gender", gender);
                map.put("dateVisit", dateVisit);

                map.put("testedHiv", testedHiv);
                map.put("address", address);
                map.put("firstTimeVisit", firstTimeVisit);
                map.put("stateId", stateId);
                map.put("lgaId", lgaId);

                map.put("lga", lga);
                map.put("state", state);

                map.put("maritalStatus", maritalStatus);
                map.put("numChildren", numChildren);
                map.put("numWives", numWives);
                map.put("typeCounseling", typeCounseling);
                map.put("indexClient", indexClient);
                map.put("typeIndex", typeIndex);
                map.put("indexClientCode", indexClientCode);

                map.put("knowledgeAssessment1", knowledgeAssessment1);
                map.put("knowledgeAssessment2", knowledgeAssessment2);
                map.put("knowledgeAssessment3", knowledgeAssessment3);
                map.put("knowledgeAssessment4", knowledgeAssessment4);
                map.put("knowledgeAssessment5", knowledgeAssessment5);
                map.put("knowledgeAssessment6", knowledgeAssessment6);
                map.put("knowledgeAssessment7", knowledgeAssessment7);

                map.put("tbScreening1", tbScreening1);
                map.put("tbScreening2", tbScreening2);
                map.put("tbScreening3", tbScreening3);

                map.put("tbScreening4", tbScreening4);
                map.put("stiScreening1", stiScreening1);
                map.put("stiScreening2", stiScreening2);
                map.put("stiScreening3", stiScreening3);
                map.put("stiScreening4", stiScreening4);
                map.put("stiScreening5", stiScreening5);

                map.put("riskAssessment1", riskAssessment1);
                map.put("riskAssessment2", riskAssessment2);
                map.put("riskAssessment3", riskAssessment3);
                map.put("riskAssessment4", riskAssessment4);
                map.put("riskAssessment5", riskAssessment5);
                map.put("riskAssessment6", riskAssessment6);

                map.put("postTest1", postTest1);
                map.put("postTest2", postTest2);
                map.put("postTest3", postTest3);
                map.put("postTest4", postTest4);
                map.put("postTest5", postTest5);
                map.put("postTest6", postTest6);
                map.put("postTest7", postTest7);
                map.put("postTest8", postTest8);
                map.put("postTest9", postTest9);
                map.put("postTest10", postTest10);
                map.put("postTest11", postTest11);
                map.put("postTest12", postTest12);
                map.put("postTest13", postTest13);
                map.put("postTest14", postTest14);

                map.put("syphilisTestResult", syphilisTestResult);
                map.put("hepatitisbTestResult", hepatitisbTestResult);
                map.put("hepatitiscTestResult", hepatitiscTestResult);
                map.put("note", note);
                map.put("hivTestResult", hivTestResult);
                map.put("artReferred", artReferred);
                map.put("tbReferred", tbReferred);
                map.put("stiReferred", stiReferred);
                map.put("timeStamp", timeStamp);
                map.put("idUUID", idUUID);
                htsList.add(map);
            }
            session.setAttribute("htsList", htsList);
            resultSet = null;
            htsList = null;
        } catch (Exception sqlException) {
            resultSet = null;
            sqlException.printStackTrace();
        }
    }

    public ArrayList<Map<String, String>> retrieveHtsList() {
        if (session.getAttribute("htsList") != null) {
            htsList = (ArrayList) session.getAttribute("htsList");
        }
        return htsList;
    }

    public void clearHtsList() {
        htsList = retrieveHtsList();
        htsList.clear();
        session.setAttribute("htsList", htsList);
    }

}
