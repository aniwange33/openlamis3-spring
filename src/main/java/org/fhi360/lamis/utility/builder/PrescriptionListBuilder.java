/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility.builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author user10
 */
public class PrescriptionListBuilder {
    private HttpServletRequest request;
    private HttpSession session;
    
    private List<String> drugList = new ArrayList<String>();
    private List<String> labtestList = new ArrayList<String>();

    public PrescriptionListBuilder() {
    }
    
    public void buildDrugList(ResultSet rs) throws SQLException {
        try {
            // loop through resultSet for each row and put into Map
            //resultSet.beforeFirst();
            while (rs.next()) {
                String regimenId = String.valueOf(rs.getLong("regimen_id"));
                String regimenTypeId = String.valueOf(rs.getLong("regimentype_id"));
                String duration = String.valueOf(rs.getInt("duration"));
                String status = String.valueOf(rs.getInt("status"));
                drugList.add(regimenTypeId);
                drugList.add(regimenId);
                drugList.add(duration);
                drugList.add(status);
            }            
            session.setAttribute("drugList", drugList);  
            rs = null;
            drugList = null;
        }
        catch (SQLException sqlException) {
            rs = null;
            throw sqlException;  
        }
    }
    
     public void buildLabTestList(ResultSet rs) throws SQLException {
        try {
            // loop through resultSet for each row and put into Map
            //resultSet.beforeFirst();
            while (rs.next()) {
                String labtestId = String.valueOf(rs.getLong("labtest_id"));
                String status = String.valueOf(rs.getInt("status"));
                labtestList.add(labtestId);
                labtestList.add(status);
            }            
            session.setAttribute("labtestList", labtestList);  
            rs = null;
            labtestList = null;
        }
        catch (SQLException sqlException) {
            rs = null;
            throw sqlException;  
        }
    }
    
    public List<String> retrieveDrugList() {
        // retrieve the drug record store in session attribute
        if(session.getAttribute("drugList") != null) {
            drugList = (ArrayList<String>) session.getAttribute("drugList");              
        }
        return drugList;
    } 
    
    public List<String> retrieveLabTestList() {
        // retrieve the drug record store in session attribute
        if(session.getAttribute("labtestList") != null) {
            labtestList = (ArrayList<String>) session.getAttribute("labtestList");              
        }
        return labtestList;
    } 
    
    public void clearDrugMap() {
        drugList = retrieveDrugList();
        drugList.clear();
        session.setAttribute("drugList", drugList);
        labtestList = retrieveLabTestList();
        labtestList.clear();
        session.setAttribute("labtestList", labtestList);
    }

    public List<String> getDrugList() {
        return drugList;
    }

    public void setDrugList(List<String> drugList) {
        this.drugList = drugList;
    }

    public List<String> getLabtestList() {
        return labtestList;
    }

    public void setLabtestList(List<String> labtestList) {
        this.labtestList = labtestList;
    }
    
    
}
