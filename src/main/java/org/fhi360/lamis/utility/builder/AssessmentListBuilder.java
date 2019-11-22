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
public class AssessmentListBuilder {

    private HttpServletRequest request;
    private HttpSession session;
    private Boolean viewIdentifier;
    private Scrambler scrambler;

    private ArrayList<Map<String, String>> assessmentList = new ArrayList<Map<String, String>>();
    
    public AssessmentListBuilder() {
        this.scrambler = new Scrambler();
        /*if(ServletActionContext.getRequest().getSession().getAttribute("viewIdentifier") != null) {
            this.viewIdentifier = (Boolean) session.getAttribute("viewIdentifier");                        
        }*/
    }
    
           
    public void buildAssessmentList(ResultSet resultSet) throws SQLException{
        try {
            resultSet.beforeFirst();
            while (resultSet.next()) { 
                String facilityId = Long.toString(resultSet.getLong("facility_id")); 
                String assessmentId = resultSet.getObject("assessment_id") == null ? "" : resultSet.getString("assessment_id");
                String clientCode = resultSet.getObject("client_code") == null ? "" : resultSet.getString("client_code");
                String dateVisit = resultSet.getObject("date_visit") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_visit"), "MM/dd/yyyy");                

                String question1 = resultSet.getObject("question1") == null ? "" : resultSet.getString("question1");
                String question2 = resultSet.getObject("question2") == null ? "" : resultSet.getString("question2");
                String question3 = resultSet.getObject("question3") == null ? "" : resultSet.getString("question3");
                String question4 = resultSet.getObject("question4") == null ? "" : resultSet.getString("question4");
                String question5 = resultSet.getObject("question5") == null ? "" : resultSet.getString("question5");
                String question6 = resultSet.getObject("question6") == null ? "" : resultSet.getString("question6");
                String question7 = resultSet.getObject("question7") == null ? "" : resultSet.getString("question7");
                String question8 = resultSet.getObject("question8") == null ? "" : resultSet.getString("question8");
                String question9 = resultSet.getObject("question9") == null ? "" : resultSet.getString("question9");
                String question10 = resultSet.getObject("question10") == null ? "" : resultSet.getString("question10");
                String question11 = resultSet.getObject("question11") == null ? "" : resultSet.getString("question11");
                String question12 = resultSet.getObject("question12") == null ? "" : resultSet.getString("question12");

                String sti1 = resultSet.getObject("sti1") == null ? "" : resultSet.getString("sti1");
                String sti2 = resultSet.getObject("sti2") == null ? "" : resultSet.getString("sti2");
                String sti3 = resultSet.getObject("sti3") == null ? "" : resultSet.getString("sti3");
                String sti4 = resultSet.getObject("sti4") == null ? "" : resultSet.getString("sti4");
                String sti5 = resultSet.getObject("sti5") == null ? "" : resultSet.getString("sti5");
                String sti6 = resultSet.getObject("sti6") == null ? "" : resultSet.getString("sti6");
                String sti7 = resultSet.getObject("sti7") == null ? "" : resultSet.getString("sti7");
               
                // create an array from object properties 
                Map<String, String> map = new HashMap<String, String>();
                map.put("assessmentId", assessmentId);
                map.put("clientCode", clientCode );
                map.put("facilityId", facilityId);
                map.put("dateVisit", dateVisit);
                
                map.put("question1", question1);
                map.put("question2", question2);
                map.put("question3", question3);
                map.put("question4", question4);
                map.put("question5", question5);
                map.put("question6", question6);
                map.put("question7", question7);
                map.put("question8", question8);
                map.put("question9", question9);
                map.put("question10", question10);
                map.put("question11", question11);
                map.put("question12", question12);
                
                map.put("sti1", sti1);
                map.put("sti2", sti2);
                map.put("sti3", sti3);
                map.put("sti4", sti4);
                map.put("sti5", sti5);
                map.put("sti6", sti6);
                map.put("sti7", sti7);
                assessmentList.add(map);
            }
            session.setAttribute("assessmentList", assessmentList);  
            resultSet = null;
            assessmentList = null;
        }
        catch (Exception sqlException) {
            resultSet = null;
           sqlException.printStackTrace();
        }            
    }
   
    public ArrayList<Map<String, String>> retrieveAssessmentList() {
        if(session.getAttribute("assessmentList") != null) {
            assessmentList = (ArrayList) session.getAttribute("assessmentList");                        
        }
        return assessmentList;
    }   

    public void clearAssessmentList() {
        assessmentList = retrieveAssessmentList();
        assessmentList.clear();
        session.setAttribute("assessmentList", assessmentList); 
    }

    
}
