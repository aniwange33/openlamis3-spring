/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

/**
 *
 * @author user10
 */
@Component
public class TreatmentCurrentService {

    public TreatmentCurrentService() {
    }
    
    
    public boolean isPatientActive(long patientId, int daysSinceLastRefill, Date reportingDate) {
        try {
            JDBCUtil jdbcUtil = new JDBCUtil(); 

            String  query = "SELECT pharm.date_visit, pharm.duration, pharm.regimentype_id, pharm.regimen_id, regimenType.description AS regimenType, regimen.description AS regimen FROM pharm JOIN regimenType ON pharm.regimentype_id = regimenType.regimentype_id JOIN regimen ON pharm.regimen_id = regimen.regimen_id WHERE pharm.patient_id = " + patientId + " ORDER BY pharm.date_visit DESC LIMIT 1";
            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                Date dateLastRefill = resultSet.getDate("date_visit");
                int duration = resultSet.getInt("duration");
                int monthRefill = duration / 30;
                if (monthRefill <= 0) {
                    monthRefill = 1;
                }
                if (dateLastRefill != null) {
                    //If the last refill date plus refill duration plus days since last refill  in days is before the last day of the reporting date this patient is Active   
                    //or in other words if your 28 days is not after the reporting date your are LTFU
                    if(DateUtil.addYearMonthDay(dateLastRefill, duration+daysSinceLastRefill, "DAY").before(reportingDate)) {
                        return false;
                    }
                }
           }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }        
        return true;
    }


    public boolean isPatientActiveTLD(long patientId, int daysSinceLastRefill, Date reportingDate) {
        try {
            JDBCUtil jdbcUtil = new JDBCUtil(); 

            String  query = "SELECT pharm.date_visit, pharm.duration, pharm.regimentype_id, pharm.regimen_id, regimenType.description AS regimenType, regimen.description AS regimen FROM pharm JOIN regimenType ON pharm.regimentype_id = regimenType.regimentype_id JOIN regimen ON pharm.regimen_id = regimen.regimen_id WHERE pharm.patient_id = " + patientId + " ORDER BY pharm.date_visit DESC LIMIT 1";
            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                Date dateLastRefill = resultSet.getDate("date_visit");
                int duration = resultSet.getInt("duration");
                String regimen = resultSet.getString("regimen");
                int monthRefill = duration / 30;
                if (monthRefill <= 0) {
                    monthRefill = 1;
                }
                if (dateLastRefill != null) {
                    //If the last refill date plus refill duration plus days since last refill  in days is before the last day of the reporting date this patient is Active   
                    //or in other words if your 28 days is not after the reporting date your are LTFU
                    if(DateUtil.addYearMonthDay(dateLastRefill, duration+daysSinceLastRefill, "DAY").before(reportingDate)) {
                        return false;
                    }
                    else {
                        if(regimen.contains("TLD")) return true;
                    }
                }
           }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }        
        return true;
    }


}
