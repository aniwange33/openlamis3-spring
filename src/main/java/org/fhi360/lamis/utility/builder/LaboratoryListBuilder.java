/**
 * @author aalozie
 */

package org.fhi360.lamis.utility.builder;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.Scrambler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LaboratoryListBuilder {
    private HttpServletRequest request;
    private HttpSession session;
    private Scrambler scrambler;
    private Boolean viewIdentifier;

    private ArrayList<Map<String, String>> laboratoryList = new ArrayList<Map<String, String>>();
    private ArrayList<Map<String, String>> labresultList = new ArrayList<Map<String, String>>();
    private ArrayList<Map<String, String>> labPrescribedList = new ArrayList<Map<String, String>>();
    private ArrayList<Map<String, Object>> reportList = new ArrayList<Map<String, Object>>();
    private ArrayList<Map<String, String>> labtestList = new ArrayList<Map<String, String>>();

    public LaboratoryListBuilder() {
        this.scrambler = new Scrambler();
        /*if (ServletActionContext.getRequest().getSession().getAttribute("viewIdentifier") != null) {
            this.viewIdentifier = (Boolean) session.getAttribute("viewIdentifier");
        }*/
    }

    public void buildLaboratoryList(ResultSet resultSet) throws SQLException {
        String hospitalNum = "";
        if (request.getParameterMap().containsKey("hospitalNum")) {
            hospitalNum = request.getParameter("hospitalNum");
        }
        String dateLastCd4 = "";
        if (request.getParameterMap().containsKey("dateLastCd4")) {
            dateLastCd4 = request.getParameter("dateLastCd4");
        }
        while (resultSet.next()) {
            String laboratoryId = Long.toString(resultSet.getLong("laboratory_id"));
            String patientId = Long.toString(resultSet.getLong("patient_id"));
            String facilityId = Long.toString(resultSet.getLong("facility_id"));
            String dateCollected = resultSet.getObject("date_collected") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_collected"), "MM/dd/yyyy");
            String dateReported = resultSet.getObject("date_reported") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_reported"), "MM/dd/yyyy");
            String labno = resultSet.getObject("labno") == null ? "" : resultSet.getString("labno");
            String labtestId = resultSet.getObject("labtest_id") == null ? "" : Long.toString(resultSet.getLong("labtest_id"));
            String description = resultSet.getObject("description") == null ? "" : resultSet.getString("description");
            String resultab = resultSet.getObject("resultab") == null ? "" : resultSet.getString("resultab");
            String resultpc = resultSet.getObject("resultpc") == null ? "" : resultSet.getString("resultpc");
            String measureab = "";
            if (contains(resultSet, "measureab")) {
                measureab = resultSet.getObject("measureab") == null ? "" : resultSet.getString("measureab");
            }
            String measurepc = "";
            if (contains(resultSet, "measurepc")) {
                measurepc = resultSet.getObject("measurepc") == null ? "" : resultSet.getString("measurepc");
            }
            String comment = "";
            if (contains(resultSet, "comment")) {
                comment = resultSet.getObject("comment") == null ? "" : resultSet.getString("comment");
            }

            Map<String, String> map = new HashMap<String, String>();
            map.put("laboratoryId", laboratoryId);
            map.put("patientId", patientId);
            map.put("facilityId", facilityId);
            map.put("hospitalNum", hospitalNum);
            map.put("dateLastCd4", dateLastCd4);
            map.put("dateCollected", dateCollected);
            map.put("dateReported", dateReported);
            map.put("labno", labno);
            map.put("labtestId", labtestId);
            map.put("description", description);
            map.put("resultab", resultab);
            map.put("resultpc", resultpc);
            laboratoryList.add(map);

            Map<String, String> map1 = new HashMap<String, String>();
            map1.put("labtestId", labtestId);
            map1.put("description", description);
            map1.put("resultab", resultab);
            map1.put("measureab", measureab);
            map1.put("resultpc", resultpc);
            map1.put("measurepc", measurepc);
            map1.put("comment", comment);
            labresultList.add(map1);
        }
        session.setAttribute("laboratoryList", laboratoryList);
        session.setAttribute("labresultList", labresultList);
    }

    public ArrayList<Map<String, Object>> buildLabResultQueryList(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String laboratoryId = Long.toString(resultSet.getLong("laboratory_id"));
            String patientId = Long.toString(resultSet.getLong("patient_id"));
            String facilityId = Long.toString(resultSet.getLong("facility_id"));
            String dateCollected = resultSet.getObject("date_collected") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_collected"), "MM/dd/yyyy");
            String dateReported = resultSet.getObject("date_reported") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_reported"), "MM/dd/yyyy");
            String labno = resultSet.getObject("labno") == null ? "" : resultSet.getString("labno");
            String labtestId = Long.toString(resultSet.getLong("labtest_id"));
            String resultab = resultSet.getObject("resultab") == null ? "" : resultSet.getString("resultab");
            String resultpc = resultSet.getObject("resultpc") == null ? "" : resultSet.getString("resultpc");
            String measureab = resultSet.getObject("measureab") == null ? "" : resultSet.getString("measureab");
            String measurepc = resultSet.getObject("measurepc") == null ? "" : resultSet.getString("measurepc");
            String result = "";
            if (resultab.equals("+") || resultpc.equals("+")) {
                result = "Positive";
            } else {
                if (resultab.equals("-") || resultpc.equals("-")) {
                    result = "Negative";
                } else {
                    result = resultab.trim() + "  " + measureab.trim();
                    if (resultab.isEmpty()) result = resultpc.trim() + "  " + measurepc.trim();
                }
            }

            String hospitalNum = resultSet.getString("hospital_num");
            String surname = resultSet.getObject("surname") == null ? "" : resultSet.getString("surname");
            surname = (viewIdentifier) ? scrambler.unscrambleCharacters(surname) : surname;
            surname = StringUtils.upperCase(surname);
            String otherNames = resultSet.getObject("other_names") == null ? "" : resultSet.getString("other_names");
            otherNames = (viewIdentifier) ? scrambler.unscrambleCharacters(otherNames) : otherNames;
            otherNames = StringUtils.capitalize(otherNames);
            String gender = resultSet.getObject("gender") == null ? "" : resultSet.getString("gender");
            String age = resultSet.getObject("age") == null ? "" : resultSet.getInt("age") == 0 ? "" : Integer.toString(resultSet.getInt("age"));
            String currentStatus = resultSet.getObject("current_status") == null ? "" : resultSet.getString("current_status");
            String dateCurrentStatus = resultSet.getObject("date_current_status") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_current_status"), "MM/dd/yyyy");
            String dateStarted = resultSet.getObject("date_started") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_started"), "MM/dd/yyyy");

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("laboratoryId", laboratoryId);
            map.put("patientId", patientId);
            map.put("facilityId", facilityId);
            map.put("dateCollected", dateCollected);
            map.put("dateReported", dateReported);
            map.put("labno", labno);
            map.put("labtestId", labtestId);
            map.put("resultab", resultab);
            map.put("resultpc", resultpc);
            map.put("result", result);

            map.put("hospitalNum", hospitalNum);
            map.put("surname", surname);
            map.put("otherNames", otherNames);
            map.put("name", surname + ' ' + otherNames);
            map.put("gender", gender);
            map.put("age", age);
            map.put("currentStatus", currentStatus);
            map.put("dateCurrentStatus", dateCurrentStatus);
            map.put("dateStarted", dateStarted);
            reportList.add(map);
        }
        return reportList;
    }

    public void buildLabTestList(ResultSet resultSet, String selectedLabtest) throws Exception {
        try {
            // loop through resultSet for each row and put into Map
            resultSet.beforeFirst();
            while (resultSet.next()) {
                String labtestId = Long.toString(resultSet.getLong("labtest_id"));
                String labtestCategoryId = Long.toString(resultSet.getLong("labtestcategory_id"));
                String description = resultSet.getString("description");

                Map<String, String> map = new HashMap<String, String>();
                map.put("labtestId", labtestId);
                map.put("labtestCategoryId", labtestCategoryId);
                map.put("description", description);
                map.put("selectedLabtest", selectedLabtest);
                labtestList.add(map);

            }
            session.setAttribute("labtestList", labtestList);
            resultSet = null;
            labtestList = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            resultSet = null;
            throw ex;
        }
    }

    public ArrayList<Map<String, String>> retrieveLaboratoryList() {
        // retrieve the laboratory record store in session attribute
        if (session.getAttribute("laboratoryList") != null) {
            laboratoryList = (ArrayList) session.getAttribute("laboratoryList");
        }
        return laboratoryList;
    }

    public ArrayList<Map<String, String>> retrieveLabresultList() {
        // retrieve the laboratory record store in session attribute
        if (session.getAttribute("labresultList") != null) {
            labresultList = (ArrayList) session.getAttribute("labresultList");
        }
        return labresultList;
    }

    public ArrayList<Map<String, String>> retrieveLabPrescribedList() {
        // retrieve the laboratory record store in session attribute
        if (session.getAttribute("labPrescribedList") != null) {
            labPrescribedList = (ArrayList) session.getAttribute("labPrescribedList");
        }
        return labPrescribedList;
    }

    public ArrayList<Map<String, String>> retrieveLabTestList() {
        // retrieve the laboratory record store in session attribute
        if (session.getAttribute("labtestList") != null) {
            labtestList = (ArrayList) session.getAttribute("labtestList");
        }
        return labtestList;
    }

    public void clearLaboratoryList() {
        laboratoryList = retrieveLaboratoryList();
        laboratoryList.clear();
        session.setAttribute("laboratoryList", laboratoryList);
        labresultList = retrieveLabresultList();
        labresultList.clear();
        session.setAttribute("labresultList", labresultList);
        labtestList = retrieveLabTestList();
        labtestList.clear();
        session.setAttribute("labtestList", labtestList);
        labPrescribedList = retrieveLabPrescribedList();
        labPrescribedList.clear();
        session.setAttribute("labPrescribedList", labPrescribedList);
    }

    private boolean contains(ResultSet rs, String column) {
        try {
            rs.findColumn(column);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}
