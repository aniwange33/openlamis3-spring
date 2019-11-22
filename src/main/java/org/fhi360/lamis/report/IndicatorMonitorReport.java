/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;

/**
 *
 * @author user10
 */
public class IndicatorMonitorReport {
    
    private HttpServletRequest request;
    private HttpSession session;
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    private ArrayList<Map<String, Object>> reportList;
    private HashMap parameterMap;
    
    public List<Integer> getTld(long facilityId, long stateId, String reportingDateBegin){
        List<Integer> tldValues = new ArrayList<>();
        
        query = "DROP TABLE IF EXISTS TEMP1";
        executeUpdate(query);
        //Current on treatment
       Date reportDate = DateUtil.parseStringToSqlDate(reportingDateBegin, "MM/dd/yyyy");
       query = "CREATE TEMPORARY TABLE temp1 AS SELECT DISTINCT patient_id, current_status, gender, regimen, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age FROM patient WHERE facility_id = " + facilityId + " AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND date_started IS NOT NULL AND date_current_status <= '" + reportDate + "' ORDER BY current_status"; 
       executeUpdate(query);
       
       //All clients on TLD
       query = "SELECT COUNT(*) AS count FROM temp1 WHERE regimen LIKE '%DTG%'";
       int tldTotal = getCount(query);
       tldValues.add(tldTotal);
       
       //Male on TLD above 10yrs
       query = "SELECT COUNT(*) AS count FROM temp1 WHERE age > 10 AND gender ='Male'";
       int male = getCount(query);
       
       //Female on TLD above 10yrs
       query = "SELECT COUNT(*) AS count FROM temp1 WHERE age > 10 AND gender ='Female'"; 
       int female = getCount(query);
       
       //Female on TLD beteen 15 and 49 
       query = "SELECT COUNT(*) AS count FROM temp1 WHERE age > 15 AND age <49 AND gender ='Female'"; 
       int fertile = getCount(query);
       
       query = "SELECT percentage AS rate FROM tpr WHERE state_id = " + stateId; 
       double rate = getRate(query);
       
       female = (int) (female - (fertile * rate));
       
        int tldExpected = male + female;
        tldValues.add(tldExpected);
        int percentage = 0;
        if(tldExpected > 0 && tldTotal > 0)
           percentage = (tldTotal * 100 + (tldExpected >> 1)) / tldExpected;
        tldValues.add(percentage);           
       return tldValues;
    }
    
    public List<Integer> getLpv(long facilityId, String reportingDateBegin){
        
       List<Integer> lpvValues = new ArrayList<>();
       query = "DROP TABLE IF EXISTS TEMP2";
       executeUpdate(query);
       
       Date reportDate = DateUtil.parseStringToSqlDate(reportingDateBegin, "MM/dd/yyyy");
       
       query = "CREATE TEMPORARY TABLE temp2 AS SELECT DISTINCT patient_id, gender, current_status, regimen, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age FROM patient WHERE facility_id = " + facilityId + " AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')  AND date_started IS NOT NULL AND date_current_status <= '" + reportDate + "' ORDER BY current_status"; 
       executeUpdate(query);

       query = "SELECT COUNT(*) AS count FROM temp2 WHERE regimen LIKE '%LPV/r%' AND age < 3"; 
       int lpvTotal = getCount(query);
       lpvValues.add(lpvTotal);
        
       query = "SELECT COUNT(*) AS count FROM temp2 WHERE age < 3"; 
       int lpvCurrent = getCount(query);
       lpvValues.add(lpvCurrent);
       
       int percentage = 0;
       if(lpvTotal > 0 && lpvCurrent > 0)
            percentage = (lpvTotal * 100 + (lpvCurrent >> 1)) / lpvCurrent;
       lpvValues.add(percentage);

       return lpvValues;
    }


    public List<Integer> getDmoc(long facilityId, String reportingDateBegin){

       List<Integer> dmocFields = new ArrayList<>();

       query = "DROP TABLE IF EXISTS TEMP3";
       executeUpdate(query);
       Date reportDate = DateUtil.parseStringToSqlDate(reportingDateBegin, "MM/dd/yyyy");
       query = "CREATE TEMPORARY TABLE temp3 AS SELECT DISTINCT patient_id, current_status FROM patient WHERE facility_id = " + facilityId + " AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')  AND date_started IS NOT NULL AND date_current_status <= '" + reportDate + "' ORDER BY current_status"; 
       executeUpdate(query); 
       
       query = "SELECT COUNT(*) as count FROM temp3";
       int tx_curr = getCount(query);

       query = "SELECT COUNT(*) AS count FROM devolve WHERE patient_id IN (SELECT patient_id FROM temp3) AND type_dmoc = 'CPARP'"; 
       int cparp = getCount(query);
       dmocFields.add(cparp);
       
       query = "SELECT COUNT(*) AS count FROM devolve WHERE patient_id IN (SELECT patient_id FROM temp3) AND type_dmoc = 'CARC'"; 
       int carc = getCount(query);
       dmocFields.add(carc);
       
       query = "SELECT COUNT(*) AS count FROM devolve WHERE patient_id IN (SELECT patient_id FROM temp3) AND type_dmoc = 'MMS'"; 
       int mms = getCount(query);
       dmocFields.add(mms);
       
       query = "SELECT COUNT(*) AS count FROM devolve WHERE patient_id IN (SELECT patient_id FROM temp3) AND type_dmoc = 'MMD'"; 
       int mmd = getCount(query);
       dmocFields.add(mmd);
       
       query = "SELECT COUNT(*) AS count FROM temp3";
       int current = getCount(query);
       dmocFields.add(current);
       
       int expected = (70 * tx_curr)/100;
       dmocFields.add(expected);
       
        int dmocTotal = cparp + carc + mms + mmd;

        int percentage = 0;
        if(dmocTotal > 0 && expected > 0)
           percentage = (dmocTotal * 100 + (expected >> 1)) / expected;
        dmocFields.add(percentage);
       
       return dmocFields;
    }
    
    public List<Integer> getTxNew(long facility, String reportingDateBegin){
        List<Integer> newValues = new ArrayList<>();
       //Enrolled this month
       Date reportDate = DateUtil.parseStringToSqlDate(reportingDateBegin, "MM/dd/yyyy");
       
       query = "SELECT COUNT(*) AS count FROM patient WHERE MONTH(date_started) = MONTH('" + reportDate + "') AND YEAR(date_started) = YEAR('" + reportDate + "') AND facility_id = "+facility; 
       int newInPeriod = getCount(query);
       newValues.add(newInPeriod);

       //Enrolled this month and assigned to a case manager
       query = "SELECT COUNT(*) As count FROM patient WHERE MONTH(date_started) = MONTH('" + reportDate + "') AND YEAR(date_started) = YEAR('" + reportDate + "') AND casemanager_id != 0 AND facility_id = "+facility; 
       int newAndAssigned = getCount(query);
       newValues.add(newAndAssigned);
       
        int percentage = 0;
        if(newAndAssigned > 0 && newInPeriod > 0)
            percentage = (newAndAssigned * 100 + (newInPeriod >> 1)) / newInPeriod;
        newValues.add(percentage);
        
       return newValues;
    }
    
    //TX_ML 1
    public List<Integer> getTxML1(long facility, String reportingDateBegin){
        List<Integer> txMlOneValues = new ArrayList<>();
        Date reportDate = DateUtil.parseStringToSqlDate(reportingDateBegin, "MM/dd/yyyy");
       
       query = "SELECT COUNT(*) AS count FROM patient WHERE facility_id = " + facility + " AND outcome = '" + Constants.TxMlStatus.TX_ML_DIED + "' AND YEAR(date_current_status) = YEAR('" + reportDate + "') AND MONTH(date_current_status) = MONTH('" + reportDate +"')"; 
       int txMlDied = getCount(query);
       txMlOneValues.add(txMlDied);
        
       return txMlOneValues;
    }
    
    //TX_ML 2
    public List<Integer> getTxML2(long facility, String reportingDateBegin){
        List<Integer> txMlTwoValues = new ArrayList<>();
       Date reportDate = DateUtil.parseStringToSqlDate(reportingDateBegin, "MM/dd/yyyy");
       executeUpdate("CREATE TEMPORARY TABLE client AS SELECT * FROM patient WHERE facility_id = " + facility + " AND YEAR(date_tracked) = YEAR('" + reportDate + "') AND MONTH(date_tracked) = MONTH('" + reportDate +"')");     
       
       query = "SELECT COUNT(*) AS count FROM client WHERE outcome = '" + Constants.TxMlStatus.TX_ML_TRANSFER + "'"; 
       int txMlTransfer = getCount(query);
       txMlTwoValues.add(txMlTransfer);
        
       return txMlTwoValues;
    }
    
    //TX_ML 3
    public List<Integer> getTxML3(long facility, String reportingDateBegin){
       List<Integer> txMlThreeValues = new ArrayList<>();
       query = "SELECT COUNT(*) AS count FROM client WHERE outcome = '" + Constants.TxMlStatus.TX_ML_TRACED + "'"; 
       int txMlTraced = getCount(query);
       txMlThreeValues.add(txMlTraced);
        
       return txMlThreeValues;
    }
    
    //TX_ML 4
    public List<Integer> getTxML4(long facility, String reportingDateBegin){
        List<Integer> txMlFourValues = new ArrayList<>();
        Date reportDate = DateUtil.parseStringToSqlDate(reportingDateBegin, "MM/dd/yyyy");
       
       query = "SELECT COUNT(*) AS count FROM patient WHERE date_next_refill < '" + reportDate + "'"; 
       int txMlNotTraced = getCount(query);
       txMlFourValues.add(txMlNotTraced);
        
       return txMlFourValues;
    }

    private void executeUpdate(String query) {
        JDBCUtil jdbcUtil1 = null;
        try {
            jdbcUtil1 = new JDBCUtil();
            PreparedStatement preparedStatement1 = jdbcUtil1.getStatement(query);
            preparedStatement1.executeUpdate();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            jdbcUtil1.disconnectFromDatabase();  //disconnect from database
        }        
    }        
    
    private int getCount(String query) {
       int count  = 0;
       ResultSet resultSet1;
       JDBCUtil jdbcUtil1 = null;
       try {
           jdbcUtil1 = new JDBCUtil();
            PreparedStatement preparedStatement1 = jdbcUtil1.getStatement(query);
            resultSet1 = preparedStatement1.executeQuery();
            while(resultSet1.next()) {
                count = resultSet1.getInt("count");
            }
            resultSet1 = null;                        
        }
        catch (Exception exception) {
            exception.printStackTrace();
            resultSet1 = null;                        
            jdbcUtil1.disconnectFromDatabase();  //disconnect from database
        }
        return count;
    }
    
    private double getRate(String query) {
       double rate  = 0;
       ResultSet resultSet1;
       JDBCUtil jdbcUtil1 = null;
       try {
            jdbcUtil1 = new JDBCUtil();
            PreparedStatement preparedStatement1 = jdbcUtil1.getStatement(query);
            resultSet1 = preparedStatement1.executeQuery();
            while(resultSet1.next()) {
                rate = resultSet1.getInt("rate");
            }
            resultSet1 = null;                        
        }
        catch (Exception exception) {
            resultSet1 = null;                        
            jdbcUtil1.disconnectFromDatabase();  //disconnect from database
        }
        return rate;
    }
     
}
