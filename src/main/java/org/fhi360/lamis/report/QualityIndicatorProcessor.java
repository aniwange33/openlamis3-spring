/**
 *
 * @author AALOZIE
 */

package org.fhi360.lamis.report;

import org.fhi360.lamis.controller.report.ReportParameterDTO;
import org.fhi360.lamis.report.indicator.QualityIndicators;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class QualityIndicatorProcessor {
    private int [][] value = new int[12][2];

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    //private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private long facilityId;
    
    public QualityIndicatorProcessor() {
    } 
    
    public synchronized List<Map<String, Object>> process(ReportParameterDTO dto, Long facilityId){
        List<Map<String, Object>> reportList = new ArrayList<>();
        int reportingMonthBegin = DateUtil.getMonth(dto.getReportingMonthBegin());
        int reportingYearBegin = Integer.parseInt(dto.getReportingYearBegin());
        int reportingMonthEnd = DateUtil.getMonth(dto.getReportingMonthEnd());
        int reportingYearEnd = Integer.parseInt(dto.getReportingYearEnd());
        String reportingDateEnd = DateUtil.parseDateToString(DateUtil
                .getLastDateOfMonth(reportingYearEnd, reportingMonthEnd), "yyyy-MM-dd");
        ResultSet resultSet;
        String[] indicator = new QualityIndicators().initialize();
        
        try {
            jdbcUtil = new JDBCUtil();
            //Indicator 1 = Percentage of eligible patients placed on ART within the review period
            //denominator - Number of non-ART patients with CD4+ count < 350 or WHO stage 3 or 4 or TB within the review period
            //create a temporary table of date of the latest status change on or before the last day of reporting month 
            String reportingDateBegin = DateUtil.parseDateToString(DateUtil
                    .getFirstDateOfMonth(reportingYearBegin, reportingMonthBegin), "yyyy-MM-dd");
            executeUpdate("DROP TABLE IF EXISTS currentstatus");        
            query = "CREATE TEMPORARY TABLE currentstatus AS SELECT DISTINCT patient_id, MAX(date_current_status) AS " +
                    "date_status FROM statushistory WHERE facility_id = " + facilityId + " AND date_current_status <= '" +
                    reportingDateEnd + "' GROUP BY patient_id";
            executeUpdate(query);            
            executeUpdate("DROP TABLE IF EXISTS ps");        
            query = "CREATE TEMPORARY TABLE ps AS SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, " +
                    "patient.date_birth, '" + reportingDateBegin + "') AS age, patient.date_registration, " +
                    "patient.status_registration, patient.date_started, statushistory.current_status, " +
                    "currentstatus.date_status FROM patient JOIN statushistory ON patient.patient_id = " +
                    "statushistory.patient_id JOIN currentstatus ON patient.patient_id = currentstatus.patient_id " +
                    "WHERE patient.facility_id = " + facilityId + " AND statushistory.facility_id = " +
                    facilityId + " AND statushistory.patient_id = currentstatus.patient_id AND " +
                    "statushistory.date_current_status = currentstatus.date_status";
            executeUpdate(query);            
            
            executeUpdate("DROP TABLE IF EXISTS eligible");        
            query = "CREATE TEMPORARY TABLE eligible AS SELECT DISTINCT patient_id FROM clinic WHERE facility_id = " +
                    facilityId + " AND patient_id IN (SELECT patient_id FROM ps WHERE age > 15 AND current_status IN " +
                    "('HIV+ non ART', 'Pre-ART Transfer In') AND date_status <= '" + reportingDateBegin + "') " +
                    "AND (clinic_stage = 'Stage III' OR clinic_stage = 'Stage IV' OR (cd4 > 0.0 AND cd4 < 350)) " +
                    "AND date_visit <= '" + reportingDateEnd + "'";
            executeUpdate(query);
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM eligible";
            value[0][0] = getCount(query);
            
            //numerator - Number of non-ART patients with CD4+ count < 350 or WHO stage 3 or 4 or TB within the review period who initiated ART
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM ps WHERE patient_id IN (SELECT patient_id FROM" +
                    " eligible) AND current_status IN ('ART Start', 'ART Restart') AND date_status <= '" +
                    reportingDateEnd + "'";
            value[0][1] = getCount(query);
            
            //Indicator 2 = Percentage of Pre -ART patients whose first visit within the review period is less than or equal to 6 months after their last visit
            //denominator - Number of Pre ART patients with at least one clinical visit in the 6 months prior to the review period
            reportingDateBegin = DateUtil.parseDateToString(DateUtil.addMonth(DateUtil
                    .getLastDateOfMonth(reportingYearEnd, reportingMonthEnd), -6), "yyyy-MM-dd");
            executeUpdate("DROP TABLE IF EXISTS currentstatus");        
            query = "CREATE TEMPORARY TABLE currentstatus AS SELECT DISTINCT patient_id, MAX(date_current_status) AS " +
                    "date_status FROM statushistory WHERE facility_id = " + facilityId + " AND date_current_status <= '" +
                    reportingDateEnd + "' GROUP BY patient_id";
            executeUpdate(query);            
            executeUpdate("DROP TABLE IF EXISTS ps");        
            query = "CREATE TEMPORARY TABLE ps AS SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, " +
                    "patient.date_birth, '" + reportingDateBegin + "') AS age, patient.date_registration, " +
                    "patient.status_registration, patient.date_started, statushistory.current_status, " +
                    "currentstatus.date_status FROM patient JOIN statushistory ON patient.patient_id = " +
                    "statushistory.patient_id JOIN currentstatus ON patient.patient_id = currentstatus.patient_id " +
                    "WHERE patient.facility_id = " + facilityId + " AND statushistory.facility_id = " +
                    facilityId + " AND statushistory.patient_id = currentstatus.patient_id AND " +
                    "statushistory.date_current_status = currentstatus.date_status";
            executeUpdate(query);            
            
            executeUpdate("DROP TABLE IF EXISTS visit");        
            query = "CREATE TEMPORARY TABLE visit AS SELECT DISTINCT patient_id FROM clinic WHERE facility_id = " +
                    facilityId + " AND patient_id IN (SELECT patient_id FROM ps WHERE age > 15 AND current_status IN " +
                    "('HIV+ non ART', 'Pre-ART Transfer In')) AND date_visit < '" + reportingDateBegin + "'";
            executeUpdate(query);
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM visit";
            value[1][0] = getCount(query);
            
            reportingDateBegin = DateUtil.parseDateToString(DateUtil
                    .getFirstDateOfMonth(reportingYearBegin, reportingMonthBegin), "yyyy-MM-dd");
            //numerator - Number of pre ART patients with at least one clinic visit in the six months of the review period. Exclusion: documentation of transferred out patients and known deaths
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM clinic WHERE facility_id = " + facilityId +
                    " AND patient_id IN (SELECT patient_id FROM visit) AND date_visit >= '" + reportingDateBegin +
                    "' AND date_visit <= '" + reportingDateEnd + "'";
            value[1][1] = getCount(query);

            //Indicator 3 = Percentage of ART patients who had at least one documented adherence assessment during the last 3 months
            //denominator - Number of ART patients who had a drug pick up  in the last 3 months
            reportingDateBegin = DateUtil.parseDateToString(DateUtil.addMonth(DateUtil
                    .getLastDateOfMonth(reportingYearEnd, reportingMonthEnd), -3), "yyyy-MM-dd");
            executeUpdate("DROP TABLE IF EXISTS ps");        
            query = "CREATE TEMPORARY TABLE ps AS SELECT patient_id FROM patient WHERE facility_id = " + facilityId +
                    " AND DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') > 15";
            executeUpdate(query);
            
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM pharmacy WHERE facility_id = " + facilityId +
                    " AND patient_id IN (SELECT patient_id FROM ps)  AND date_visit >= '" + reportingDateBegin +
                    "' AND date_visit <= '" + reportingDateEnd + "' AND regimentype_id IN (1, 2, 3, 4)";
            value[2][0] = getCount(query);
            //numerator - Number of ART patients who had a drug pick up and had a documented adherence assessment during the last 3 months
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM pharmacy WHERE facility_id = " + facilityId +
                    " AND patient_id IN (SELECT patient_id FROM ps) AND date_visit >= '" + reportingDateBegin +
                    "' AND date_visit <= '" + reportingDateEnd + "' AND regimentype_id IN (1, 2, 3, 4) AND adherence = 1";
            value[2][1] = getCount(query);

            //Indicator 4 = Percentage of  HIV patients with at least one clinic visit in the review period who were clinically screened for TB
            //denominator - Number of  HIV patients with at least one clinical visit during the last 6 months. Exclusion: patients on TB treatment
            reportingDateBegin = DateUtil.parseDateToString(DateUtil.addMonth(DateUtil
                    .getLastDateOfMonth(reportingYearEnd, reportingMonthEnd), -6), "yyyy-MM-dd");
            executeUpdate("DROP TABLE IF EXISTS ps");        
            query = "CREATE TEMPORARY TABLE ps AS SELECT patient.patient_id, patient.date_started FROM patient JOIN " +
                    "clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id = " + facilityId +
                    " AND DATEDIFF(YEAR, patient.date_birth, '" + reportingDateBegin + "') > 15 AND clinic.facility_id = " +
                    facilityId;
            executeUpdate(query);
            
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM clinic WHERE facility_id = " + facilityId + "" +
                    " AND patient_id IN (SELECT patient_id FROM ps) AND date_visit >= '" + reportingDateBegin +
                    "' AND date_visit <= '" + reportingDateEnd + "'";
            value[3][0] = getCount(query);
            //numerator - Number of  HIV patients with at least one clinical visit during the last 6 months who were screened for clinical symptoms (cough, fever, night sweats and weight loss)
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM clinic WHERE facility_id = " + facilityId +
                    " AND patient_id IN (SELECT patient_id FROM ps) AND date_visit >= '" + reportingDateBegin +
                    "' AND date_visit <= '" + reportingDateEnd + "' AND tb_status != '' AND tb_status IS NOT NULL";
            value[3][1] = getCount(query);
            
            //Indicator 5 = Percentage of patients with at least one clinical visit within the last 6 months who have a CD4 test result
            //denominator - Number of patients with at least one clinical visit within the last 6 months       
            executeUpdate("DROP TABLE IF EXISTS ps");        
            query = "CREATE TEMPORARY TABLE ps AS SELECT patient_id FROM patient WHERE facility_id = " + facilityId +
                    " AND DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') > 15";
            executeUpdate(query);
            executeUpdate("DROP TABLE IF EXISTS visit");        
            query = "CREATE TEMPORARY TABLE visit AS SELECT DISTINCT patient_id FROM clinic WHERE facility_id = " +
                    facilityId + " AND patient_id IN (SELECT patient_id FROM ps) AND date_visit >= '" +
                    reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "'";
            executeUpdate(query);
            
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM visit";
            value[4][0] = getCount(query);
            //numerator - Number of patients with at least one clinical visit within the last 6 months who have a documented CD4 count 
            query = "SELECT COUNT(DISTINCT visit.patient_id) AS count FROM visit JOIN laboratory ON visit.patient_id = " +
                    "laboratory.patient_id WHERE laboratory.facility_id = " + facilityId + " AND " +
                    "laboratory.date_reported >= '" + reportingDateBegin + "' AND laboratory.date_reported <= '" +
                    reportingDateEnd + "' AND laboratory.labtest_id = 1";
            value[4][1] = getCount(query);
        
            //Indicator 6 = Percentage of ART patients with at least one clinical visit within the last 6 months that have all the relevant laboratory tests conducted
            //denominator - Number of ART patients with at least one clinical visit within the last 6 months
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM visit";
            value[5][0] = getCount(query);
            
            //numerator - Number of ART patients with at least one clinical visit within the last 6 months who had all of the following lab tests: Creatinine, Haemoglobin, ALT
            query = "SELECT COUNT(DISTINCT visit.patient_id) AS count FROM visit JOIN laboratory ON visit.patient_id = " +
                    "laboratory.patient_id WHERE laboratory.facility_id = " + facilityId + " AND " +
                    "laboratory.date_reported >= '" + reportingDateBegin + "' AND laboratory.date_reported <= '" +
                    reportingDateEnd + "' AND laboratory.labtest_id = 12 AND laboratory.labtest_id = 8 AND " +
                    "laboratory.labtest_id = 15";
            value[5][1] = getCount(query);
                    
            //Indicator 7 = Percentage of patients with at least one CD4 count < 350 cells or confirmed TB in the past 6 months that received CPT
            //denominator - Number of patients with at least one CD4 count < 350 cells or confirmed TB in the past 6 months
            executeUpdate("DROP TABLE IF EXISTS ps");        
            query = "CREATE TEMPORARY TABLE ps AS SELECT patient_id FROM patient WHERE facility_id = " + facilityId +
                    " AND DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') > 15";
            executeUpdate(query);
            executeUpdate("DROP TABLE IF EXISTS visit");        
            query = "CREATE TEMPORARY TABLE visit AS SELECT DISTINCT patient_id FROM clinic WHERE facility_id = " +
                    facilityId + " AND patient_id IN (SELECT patient_id FROM ps) AND date_visit >= '" +
                    reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "' AND ((cd4 > 0.0 AND cd4 < 350) " +
                    "OR tb_status IN ('Currently on TB treatment', 'TB positive not on TB drugs'))";
            executeUpdate(query);
            
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM visit";
            value[6][0] = getCount(query);
            //numerator - Number of patients on CPT in the last 6 months
            query = "SELECT COUNT(DISTINCT visit.patient_id) AS count FROM visit JOIN pharmacy ON visit.patient_id = " +
                    "pharmacy.patient_id WHERE pharmacy.facility_id = " + facilityId + " AND pharmacy.date_visit >= '" +
                    reportingDateBegin + "' AND pharmacy.date_visit <= '" + reportingDateEnd + "' AND " +
                    "pharmacy.regimentype_id = 8";
            value[6][1] = getCount(query);
            
            //Indicator 8 = Percentage of HIV infected children 0-23 months commenced on ART in the past 6 months
            //denominator - Number of HIV infected a) children 0-18 months with positive DNA PCR OR b) children 18-23 months of age with positive rapid test OR c) children 0-23 months of age with presumptive clinical diagnosis in the past 6 months
            //create a temporary table of date of the latest status change on or before the last day of reporting month 
            executeUpdate("DROP TABLE IF EXISTS currentstatus");        
            query = "CREATE TEMPORARY TABLE currentstatus AS SELECT DISTINCT patient_id, MAX(date_current_status) AS " +
                    "date_status FROM statushistory WHERE facility_id = " + facilityId + " AND date_current_status <= '" +
                    reportingDateEnd + "' GROUP BY patient_id";
            executeUpdate(query);            
            executeUpdate("DROP TABLE IF EXISTS ps");        
            query = "CREATE TEMPORARY TABLE ps AS SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, " +
                    "patient.date_birth, '" + reportingDateBegin + "') AS age, patient.date_registration, " +
                    "patient.status_registration, patient.date_started, statushistory.current_status, " +
                    "currentstatus.date_status FROM patient JOIN statushistory ON patient.patient_id = " +
                    "statushistory.patient_id JOIN currentstatus ON patient.patient_id = currentstatus.patient_id " +
                    "WHERE patient.facility_id = " + facilityId + " AND statushistory.facility_id = " + facilityId +
                    " AND statushistory.patient_id = currentstatus.patient_id AND statushistory.date_current_status = " +
                    "currentstatus.date_status";
            executeUpdate(query);            
            
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM ps WHERE age < 2 AND current_status IN " +
                    "('HIV+ non ART', 'Pre-ART Transfer In') OR (current_status IN ('ART Start', 'ART Restart') AND " +
                    "date_started >= '" + reportingDateBegin + "' AND date_started <= '" + reportingDateEnd + "')";
            value[7][0] = getCount(query);
            //numerator - Number of children 0-23 months started on ART in the past 6 months
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM ps WHERE age < 2 AND current_status IN " +
                    "('ART Start', 'ART Restart')";
            value[7][1] = getCount(query);
            
            //Indicator 9 = Percentage of eligible HIV infected children 24 and 59 months commenced on ART in the past 6 months
            //denominator - Number of HIV infected children 24 and 59 months with WHO clinical stages 3, 4 or CD4% < 25 in the past 6 months
            executeUpdate("DROP TABLE IF EXISTS eligible");        
            query = "CREATE TEMPORARY TABLE eligible AS SELECT DISTINCT patient_id FROM clinic WHERE facility_id = " +
                    facilityId + " AND patient_id IN (SELECT patient_id FROM ps WHERE age >= 2 AND age < 5 AND " +
                    "current_status IN ('HIV+ non ART', 'Pre-ART Transfer In') AND date_status <= '" +
                    reportingDateBegin + "') AND (clinic_stage = 'Stage III' OR clinic_stage = 'Stage IV' OR " +
                    "(cd4p > 0.0 AND cd4p < 25)) AND date_visit <= '" + reportingDateEnd + "'";
            executeUpdate(query);
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM eligible";
            value[8][0] = getCount(query);
            
            //numerator - Number of HIV infected children 24 and 59 months started on ART in the past 6 months
            //query = "SELECT COUNT(DISTINCT patient_id) AS count FROM ps WHERE patient_id IN (SELECT patient_id FROM eligible) AND current_status IN ('ART Start', 'ART Restart') AND date_status <= '" + reportingDateEnd + "'";            
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM ps WHERE patient_id IN (SELECT patient_id FROM " +
                    "eligible) AND date_started >= '" + reportingDateBegin + "' AND date_started <= '" +
                    reportingDateEnd + "'";
            value[8][1] = getCount(query);

            //Indicator 10 = Percentage of eligible HIV infected children 5 to < 15 years commenced on ART in the past 6 months
            //denominator - Number of HIV infected children 5 to < 15 years with WHO clinical stages 3, 4 or CD4 count < 350 in the past 6 months
            executeUpdate("DROP TABLE IF EXISTS eligible");        
            query = "CREATE TEMPORARY TABLE eligible AS SELECT DISTINCT patient_id FROM clinic WHERE facility_id = " +
                    facilityId + " AND patient_id IN (SELECT patient_id FROM ps WHERE age >= 5 AND age < 15 AND " +
                    "current_status IN ('HIV+ non ART', 'Pre-ART Transfer In') AND date_status <= '" +
                    reportingDateBegin + "') AND (clinic_stage = 'Stage III' OR clinic_stage = 'Stage IV' OR " +
                    "(cd4 > 0.0 AND cd4 < 350)) AND date_visit <= '" + reportingDateEnd + "'";
            executeUpdate(query);
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM eligible";
            value[9][0] = getCount(query);
            
            //numerator - Number of HIV infected children 24 and 59 months started on ART in the past 6 months
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM ps WHERE patient_id IN (SELECT patient_id " +
                    "FROM eligible) AND date_started >= '" + reportingDateBegin + "' AND date_started <= '" +
                    reportingDateEnd + "'";
            value[9][1] = getCount(query);
            
            //Indicator 11 = Percentage of HIV infected children < 15 years on ART with a visit in the last 3 months who have had at least one adherence assessment
            //denominator - Number of HIV infected children < 15 years on ART with at least one clinical visit in the last 3 months of review period
            reportingDateBegin = DateUtil.parseDateToString(DateUtil.addMonth(DateUtil
                    .getLastDateOfMonth(reportingYearEnd, reportingMonthEnd), -3), "yyyy-MM-dd");
            executeUpdate("DROP TABLE IF EXISTS ps");        
            query = "CREATE TEMPORARY TABLE ps AS SELECT patient_id FROM patient WHERE facility_id = " + facilityId +
                    " AND DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') < 15 AND date_started IS NOT NULL";
            executeUpdate(query);
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM clinic WHERE facility_id = " + facilityId +
                    " AND patient_id IN (SELECT patient_id FROM ps) AND date_visit >= '" + reportingDateBegin +
                    "' AND date_visit <= '" + reportingDateEnd + "'";
            value[10][0] = getCount(query);
            //numerator - Number of HIV infected children < 15 year on ART with at least one clinical visit in the last 3 months who have had at least one documented adherence assessment
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM clinic WHERE facility_id = " + facilityId +
                    " AND patient_id IN (SELECT patient_id FROM ps) AND date_visit >= '" + reportingDateBegin +
                    "' AND date_visit <= '" + reportingDateEnd + "' AND adherence_level !='' AND adherence_level IS NOT NULL";
            value[10][1] = getCount(query);
        
            //Indicator 12 = Percentage of HIV positive children < 15 years who have had at least one clinical visit in the last 3 months
            //denominator - Total number of HIV positive children < 15 years of age enrolled in care
            reportingDateBegin = DateUtil.parseDateToString(DateUtil.addMonth(DateUtil
                    .getLastDateOfMonth(reportingYearEnd, reportingMonthEnd), -3), "yyyy-MM-dd");
            executeUpdate("DROP TABLE IF EXISTS ps");        
            query = "CREATE TEMPORARY TABLE ps AS SELECT patient_id FROM patient WHERE facility_id = " + facilityId +
                    " AND DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') < 15";
            executeUpdate(query);
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM ps"; 
            value[11][0] = getCount(query);
            
            //numerator - Number of HIV positive children < 15 years of age enrolled in care who had at least one clinical visit in the last 3 months
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM clinic WHERE facility_id = " + facilityId +
                    " AND patient_id IN (SELECT patient_id FROM ps) AND date_visit >= '" + reportingDateBegin +
                    "' AND date_visit <= '" + reportingDateEnd + "'";
            value[11][1] = getCount(query);
                        
            //Populating indicator values
            for(int i = 0; i < indicator.length; i++) {
                DecimalFormat decimalFormat = new DecimalFormat("###.##");
                double d1 = Double.valueOf(value[i][0]);
                double d2 = Double.valueOf(value[i][1]);
                String percentage = d2 > 0.0? decimalFormat.format((d2/d1)*100) : "0.0";

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("sno", Integer.toString(i+1));
                map.put("indicator", indicator[i]);
                map.put("percentage", percentage);
                map.put("proportion", Integer.toString(value[i][1])+"/"+Integer.toString(value[i][0]));
                reportList.add(map);
            }
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }          
        return reportList;
    }

    public Map<String, Object> getReportParameters(ReportParameterDTO dto, Long facilityId){
        Map<String, Object> parameterMap = new HashMap<>();
        String reportingPeriodBegin = dto.getReportingMonthBegin() + " " + dto.getReportingYearBegin();
        String reportingPeriodEnd = dto.getReportingMonthEnd() + " " + dto.getReportingYearEnd();
        parameterMap.put("reportingPeriodBegin",  reportingPeriodBegin);
        parameterMap.put("reportingPeriodEnd", reportingPeriodEnd);
        ResultSet resultSet;

        try {
            jdbcUtil = new JDBCUtil();            
            query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility JOIN lga ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE facility_id = " + facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next()) {
                parameterMap.put("facilityName", resultSet.getString("name"));  
                parameterMap.put("lga", resultSet.getString("lga"));            
                parameterMap.put("state", resultSet.getString("state")); 
            }
            resultSet = null;                        
        }
        catch (Exception exception) {
            resultSet = null;                        
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }         
        return parameterMap;
    }
    
    private void executeUpdate(String query) {
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }        
    }        
    
    private int getCount(String query) {
       int count  = 0;
       ResultSet resultSet;
       try {
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                count = resultSet.getInt("count");
            }
            resultSet = null;                        
        }
        catch (Exception exception) {
            resultSet = null;                        
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return count;
    }      
}
