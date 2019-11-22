/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility.builder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.Scrambler;

/**
 *
 * @author user10
 */
public class ViralLoadListBuilder {
    
    private HttpServletRequest request;
    private HttpSession session;
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private DateFormat selectDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ArrayList<Map<String, String>> viralLoadList = new ArrayList<Map<String, String>>();
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet rs;
    private Scrambler scrambler = new Scrambler();
    
    public ViralLoadListBuilder() {

    }
        
    public void buildViralLoadList(ResultSet resultSet) throws SQLException{
        try {
            Integer sn;
           if(session.getAttribute("currpage") != null) {
                sn = ((Integer) session.getAttribute("currpage") * 100) + 1;                        
            }else{
                sn = 1;
            }
            
            // loop through resultSet for each row and put into Map
            resultSet.beforeFirst();
            while (resultSet.next()) { //patient_id, facility_id, hospital_num, surname, other_names, phone, date_started
                String dateStarted = (resultSet.getDate("date_started") == null)? "" : dateFormat.format(resultSet.getDate("date_started"));
                String facilityId = (resultSet.getString("facility_id") == null)? "" : resultSet.getString("facility_id");
                String surname = (resultSet.getString("surname") == null)? "" : resultSet.getString("surname");
                String otherNames = (resultSet.getString("other_names") == null)? "" : resultSet.getString("other_names");
                String hospitalNum = (resultSet.getString("hospital_num") == null)? "" : resultSet.getString("hospital_num");
                String patientId = (resultSet.getString("patient_id") == null)? "" : resultSet.getString("patient_id");
                String phone = (resultSet.getString("phone") == null)? "" : resultSet.getString("phone");
                               
                Map<String, String> map = new HashMap<String, String>();               
                String name = scrambler.unscrambleCharacters(surname).toUpperCase()+" "+scrambler.unscrambleCharacters(otherNames).toUpperCase();
                map.put("sn", String.valueOf(sn));
                map.put("patientId", patientId);
                map.put("facilityId", facilityId);
                map.put("hospitalNum", hospitalNum);
                map.put("name", name);
                map.put("phone", scrambler.unscrambleNumbers(phone));
                map.put("dateStarted", dateStarted);
                map.put("eac", "");
                map.put("repeatVl", "");
                String query;
                
                //For the VL Baseline...
                if(dateStarted != null && !dateStarted.equals("")){
                    dateStarted = selectDateFormat.format(resultSet.getDate("date_started"));
                    //System.out.println("Date Started = "+dateStarted);
                    query = "SELECT resultab, date_reported FROM laboratory WHERE labtest_id = 16 AND date_reported >= '"+dateStarted+"' AND patient_id = '"+patientId+"' ORDER BY date_reported ASC LIMIT 1";
                
                    jdbcUtil = new JDBCUtil();
                    preparedStatement = jdbcUtil.getStatement(query);
                    rs = preparedStatement.executeQuery();

                    //Fetch the Baseline conditions...
                    if(rs.next()){
                        String baselineVl = (rs.getString("resultab") == null)? "" : rs.getString("resultab");
                        //System.out.println("VL Count Baseline: "+baselineVl);
                        map.put("baselineVl", baselineVl);
                        map.put("dateBaselineVl", dateFormat.format(rs.getDate("date_reported")));
                    }else{
                        map.put("baselineVl", "N/A");
                        map.put("dateBaselineVl", "N/A");
                    }

                    //For the Surpressed and UnSurpressed...
                    query = "SELECT resultab, date_reported FROM laboratory WHERE labtest_id = 16 AND date_reported >= '"+dateStarted+"' AND patient_id = '"+patientId+"' ORDER BY date_reported DESC LIMIT 1";

                    jdbcUtil = new JDBCUtil();
                    preparedStatement = jdbcUtil.getStatement(query);
                    rs = preparedStatement.executeQuery();

                    //Fetch the Eligibility conditions...
                    if(rs.next()){
                        String vlCount = rs.getString("resultab");
                        if(vlCount != null && !vlCount.equals("")){
                            map.put("currentVl", rs.getString("resultab"));
                            map.put("dateCurrentVl", dateFormat.format(rs.getDate("date_reported")));
                            if(vlCount.contains("<")) vlCount = vlCount.substring(1);
                            if(Integer.parseInt(vlCount) > 1000) map.put("status", "Unsuppressed");
                            else if(Integer.parseInt(vlCount) < 1000) map.put("status", "Suppressed");
                        }else{

                            //Eligible Not Done..
                            query = "SELECT patient_id FROM patient WHERE TIMESTAMPDIFF(MONTH, date_started, CURDATE()) > 6 AND patient_id = '"+patientId+"'";
                            jdbcUtil = new JDBCUtil();
                            preparedStatement = jdbcUtil.getStatement(query);
                            rs = preparedStatement.executeQuery();

                            if(rs.next()){
                                map.put("currentVl", "N/A");
                                map.put("dateCurrentVl", "N/A");
                                map.put("status", "Eligible not Done");
                            }
                        }
                    }else{
                         //Eligible Not Done..
                        query = "SELECT patient_id FROM patient WHERE TIMESTAMPDIFF(MONTH, date_started, CURDATE()) > 6 AND patient_id = '"+patientId+"'";
                        jdbcUtil = new JDBCUtil();
                        preparedStatement = jdbcUtil.getStatement(query);
                        rs = preparedStatement.executeQuery();

                        if(rs.next()){
                            map.put("currentVl", "N/A");
                            map.put("dateCurrentVl", "N/A");
                            map.put("status", "Eligible not Done");
                        }else{
                            map.put("currentVl", "N/A");
                            map.put("dateCurrentVl", "N/A");
                            map.put("status", "Not Eligible");
                        }
                    }
                }else{
                    map.put("currentVl", "N/A");
                    map.put("dateBaselineVl", "N/A");
                    map.put("dateCurrentVl", "N/A");
                    map.put("status", " N/A");
                    map.put("baselineVl", "N/A");
                }
                                    
                viralLoadList.add(map);
                
                sn++;
                //session.setAttribute("sn", sn); 
            }            
            session.setAttribute("viralLoadList", viralLoadList);   
            resultSet = null;
            rs = null;
            viralLoadList = null;
        }
        catch (Exception ex) {
            resultSet = null;
            ex.printStackTrace();
        }
    }

    public ArrayList<Map<String, String>> retrieveViralLoadList() {
        // retrieve the status record store in session attribute
        if(session.getAttribute("viralLoadList") != null) {
            viralLoadList = (ArrayList) session.getAttribute("viralLoadList");                        
        }
        return viralLoadList;
    }       
    
}
