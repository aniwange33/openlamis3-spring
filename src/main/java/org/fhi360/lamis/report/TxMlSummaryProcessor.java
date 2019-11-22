/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.report;

import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author user10
 */
@Component
public class TxMlSummaryProcessor {
    private String reportingDateEnd;

    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private long facilityId;
    //private static final Log log = LogFactory.getLog(ArtSummaryProcessor.class);
    
    private int agem1, agem2, agem3, agem4, agem5, agem6, agem7, agem8, agem9, agem10, agem11, agem12;;
    private int agef1, agef2, agef3, agef4, agef5, agef6, agef7, agef8, agef9, agef10, agef11, agef12;


    public TxMlSummaryProcessor() {
    }
    
    public Map<String, Object> process(String month, String year, Long facilityId) {

        Map<String, Object> parameterMap = new HashMap<>();
        int reportingMonth = DateUtil.getMonth(month);
        int reportingYear = Integer.parseInt(year);
        String reportingDateBegin = dateFormat.format(DateUtil.getFirstDateOfMonth(reportingYear, reportingMonth));
        reportingDateEnd = dateFormat.format(DateUtil.getLastDateOfMonth(reportingYear, reportingMonth));

        ResultSet resultSet;
        try {
            jdbcUtil = new JDBCUtil();

            ResultSet rs;
            System.out.println("Computing TXMl 1.....");
            //TxMl 1
            //Total number of people living with HIV known to have died during the month
            initVariables();

            executeUpdate("DROP INDEX IF EXISTS idx_client");                       
            executeUpdate("DROP TABLE IF EXISTS client");        
            executeUpdate("CREATE TEMPORARY TABLE client AS SELECT * FROM patient WHERE facility_id = " + facilityId + " AND outcome = '" + Constants.TxMlStatus.TX_ML_DIED + "' AND YEAR(date_current_status) = " + reportingYear + " AND MONTH(date_current_status) = " + reportingMonth);
            executeUpdate("CREATE INDEX idx_client ON client(patient_id)");

            String query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM client";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("txml1m1", Integer.toString(agem1));
            parameterMap.put("txml1f1", Integer.toString(agef1));
            parameterMap.put("txml1t1", Integer.toString(agem1 + agef1));

            parameterMap.put("txml1m2", Integer.toString(agem2));
            parameterMap.put("txml1f2", Integer.toString(agef2));
            parameterMap.put("txml1t2", Integer.toString(agem2 + agef2));

            parameterMap.put("txml1m3", Integer.toString(agem3));
            parameterMap.put("txml1f3", Integer.toString(agef3));
            parameterMap.put("txml1t3", Integer.toString(agem3 + agef3));

            parameterMap.put("txml1m4", Integer.toString(agem4));
            parameterMap.put("txml1f4", Integer.toString(agef4));
            parameterMap.put("txml1t4", Integer.toString(agem4 + agef4));

            parameterMap.put("txml1m5", Integer.toString(agem5));
            parameterMap.put("txml1f5", Integer.toString(agef5));
            parameterMap.put("txml1t5", Integer.toString(agem5 + agef5));

            parameterMap.put("txml1m6", Integer.toString(agem6));
            parameterMap.put("txml1f6", Integer.toString(agef6));
            parameterMap.put("txml1t6", Integer.toString(agem6 + agef6));

            parameterMap.put("txml1m7", Integer.toString(agem7));
            parameterMap.put("txml1f7", Integer.toString(agef7));
            parameterMap.put("txml1t7", Integer.toString(agem7 + agef7));

            parameterMap.put("txml1m8", Integer.toString(agem8));
            parameterMap.put("txml1f8", Integer.toString(agef8));
            parameterMap.put("txml1t8", Integer.toString(agem8 + agef8));

            parameterMap.put("txml1m9", Integer.toString(agem9));
            parameterMap.put("txml1f9", Integer.toString(agef9));
            parameterMap.put("txml1t9", Integer.toString(agem9 + agef9));

            parameterMap.put("txml1m10", Integer.toString(agem10));
            parameterMap.put("txml1f10", Integer.toString(agef10));
            parameterMap.put("txml1t10", Integer.toString(agem10 + agef10));

            parameterMap.put("txml1m11", Integer.toString(agem11));
            parameterMap.put("txml1f11", Integer.toString(agef11));
            parameterMap.put("txml1t11", Integer.toString(agem11 + agef11));
            
            parameterMap.put("txml1m12", Integer.toString(agem12));
            parameterMap.put("txml1f12", Integer.toString(agef12));
            parameterMap.put("txml1t12", Integer.toString(agem12 + agef12));

            System.out.println("Computing TXML 1A.....");
            //TxMl 1A
            //Total number of people living with HIV known to have died during the month (by TB)
            initVariables();
            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM client WHERE cause_death = '" + Constants.CauseDeath.DEATH_TB + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("txml1am1", Integer.toString(agem1));
            parameterMap.put("txml1af1", Integer.toString(agef1));
            parameterMap.put("txml1at1", Integer.toString(agem1 + agef1));

            parameterMap.put("txml1am2", Integer.toString(agem2));
            parameterMap.put("txml1af2", Integer.toString(agef2));
            parameterMap.put("txml1at2", Integer.toString(agem2 + agef2));

            parameterMap.put("txml1am3", Integer.toString(agem3));
            parameterMap.put("txml1af3", Integer.toString(agef3));
            parameterMap.put("txml1at3", Integer.toString(agem3 + agef3));

            parameterMap.put("txml1am4", Integer.toString(agem4));
            parameterMap.put("txml1af4", Integer.toString(agef4));
            parameterMap.put("txml1at4", Integer.toString(agem4 + agef4));

            parameterMap.put("txml1am5", Integer.toString(agem5));
            parameterMap.put("txml1af5", Integer.toString(agef5));
            parameterMap.put("txml1at5", Integer.toString(agem5 + agef5));

            parameterMap.put("txml1am6", Integer.toString(agem6));
            parameterMap.put("txml1af6", Integer.toString(agef6));
            parameterMap.put("txml1at6", Integer.toString(agem6 + agef6));

            parameterMap.put("txml1am7", Integer.toString(agem7));
            parameterMap.put("txml1af7", Integer.toString(agef7));
            parameterMap.put("txml1at7", Integer.toString(agem7 + agef7));

            parameterMap.put("txml1am8", Integer.toString(agem8));
            parameterMap.put("txml1af8", Integer.toString(agef8));
            parameterMap.put("txml1at8", Integer.toString(agem8 + agef8));

            parameterMap.put("txml1am9", Integer.toString(agem9));
            parameterMap.put("txml1af9", Integer.toString(agef9));
            parameterMap.put("txml1at9", Integer.toString(agem9 + agef9));

            parameterMap.put("txml1am10", Integer.toString(agem10));
            parameterMap.put("txml1af10", Integer.toString(agef10));
            parameterMap.put("txml1at10", Integer.toString(agem10 + agef10));

            parameterMap.put("txml1am11", Integer.toString(agem11));
            parameterMap.put("txml1af11", Integer.toString(agef11));
            parameterMap.put("txml1at11", Integer.toString(agem11 + agef11));
            
            parameterMap.put("txml1am12", Integer.toString(agem12));
            parameterMap.put("txml1af12", Integer.toString(agef12));
            parameterMap.put("txml1at12", Integer.toString(agem12 + agef12));


            System.out.println("Computing TXML 1B.....");
            //TXML 1B
            //Number of People living with HIV who are lost to follow up during the month (by Cancer)
            initVariables();
            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM client WHERE cause_death = '" + Constants.CauseDeath.DEATH_CANCER + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("txml1bm1", Integer.toString(agem1));
            parameterMap.put("txml1bf1", Integer.toString(agef1));
            parameterMap.put("txml1bt1", Integer.toString(agem1 + agef1));

            parameterMap.put("txml1bm2", Integer.toString(agem2));
            parameterMap.put("txml1bf2", Integer.toString(agef2));
            parameterMap.put("txml1bt2", Integer.toString(agem2 + agef2));

            parameterMap.put("txml1bm3", Integer.toString(agem3));
            parameterMap.put("txml1bf3", Integer.toString(agef3));
            parameterMap.put("txml1bt3", Integer.toString(agem3 + agef3));

            parameterMap.put("txml1bm4", Integer.toString(agem4));
            parameterMap.put("txml1bf4", Integer.toString(agef4));
            parameterMap.put("txml1bt4", Integer.toString(agem4 + agef4));

            parameterMap.put("txml1bm5", Integer.toString(agem5));
            parameterMap.put("txml1bf5", Integer.toString(agef5));
            parameterMap.put("txml1bt5", Integer.toString(agem5 + agef5));

            parameterMap.put("txml1bm6", Integer.toString(agem6));
            parameterMap.put("txml1bf6", Integer.toString(agef6));
            parameterMap.put("txml1bt6", Integer.toString(agem6 + agef6));

            parameterMap.put("txml1bm7", Integer.toString(agem7));
            parameterMap.put("txml1bf7", Integer.toString(agef7));
            parameterMap.put("txml1bt7", Integer.toString(agem7 + agef7));

            parameterMap.put("txml1bm8", Integer.toString(agem8));
            parameterMap.put("txml1bf8", Integer.toString(agef8));
            parameterMap.put("txml1bt8", Integer.toString(agem8 + agef8));

            parameterMap.put("txml1bm9", Integer.toString(agem9));
            parameterMap.put("txml1bf9", Integer.toString(agef9));
            parameterMap.put("txml1bt9", Integer.toString(agem9 + agef9));

            parameterMap.put("txml1bm10", Integer.toString(agem10));
            parameterMap.put("txml1bf10", Integer.toString(agef10));
            parameterMap.put("txml1bt10", Integer.toString(agem10 + agef10));

            parameterMap.put("txml1bm11", Integer.toString(agem11));
            parameterMap.put("txml1bf11", Integer.toString(agef11));
            parameterMap.put("txml1bt11", Integer.toString(agem11 + agef11));
            
            parameterMap.put("txml1bm12", Integer.toString(agem12));
            parameterMap.put("txml1bf12", Integer.toString(agef12));
            parameterMap.put("txml1bt12", Integer.toString(agem12 + agef12));

            System.out.println("Computing TXML 1C.....");
            //TXML 1C
            //Number of People living with HIV who are lost to follow up during the month (by Infections)
            initVariables();
            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM client WHERE cause_death = '" + Constants.CauseDeath.DEATH_INFECTION + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("txml1cm1", Integer.toString(agem1));
            parameterMap.put("txml1cf1", Integer.toString(agef1));
            parameterMap.put("txml1ct1", Integer.toString(agem1 + agef1));

            parameterMap.put("txml1cm2", Integer.toString(agem2));
            parameterMap.put("txml1cf2", Integer.toString(agef2));
            parameterMap.put("txml1ct2", Integer.toString(agem2 + agef2));

            parameterMap.put("txml1cm3", Integer.toString(agem3));
            parameterMap.put("txml1cf3", Integer.toString(agef3));
            parameterMap.put("txml1ct3", Integer.toString(agem3 + agef3));

            parameterMap.put("txml1cm4", Integer.toString(agem4));
            parameterMap.put("txml1cf4", Integer.toString(agef4));
            parameterMap.put("txml1ct4", Integer.toString(agem4 + agef4));

            parameterMap.put("txml1cm5", Integer.toString(agem5));
            parameterMap.put("txml1cf5", Integer.toString(agef5));
            parameterMap.put("txml1ct5", Integer.toString(agem5 + agef5));

            parameterMap.put("txml1cm6", Integer.toString(agem6));
            parameterMap.put("txml1cf6", Integer.toString(agef6));
            parameterMap.put("txml1ct6", Integer.toString(agem6 + agef6));

            parameterMap.put("txml1cm7", Integer.toString(agem7));
            parameterMap.put("txml1cf7", Integer.toString(agef7));
            parameterMap.put("txml1ct7", Integer.toString(agem7 + agef7));

            parameterMap.put("txml1cm8", Integer.toString(agem8));
            parameterMap.put("txml1cf8", Integer.toString(agef8));
            parameterMap.put("txml1ct8", Integer.toString(agem8 + agef8));

            parameterMap.put("txml1cm9", Integer.toString(agem9));
            parameterMap.put("txml1cf9", Integer.toString(agef9));
            parameterMap.put("txml1ct9", Integer.toString(agem9 + agef9));

            parameterMap.put("txml1cm10", Integer.toString(agem10));
            parameterMap.put("txml1cf10", Integer.toString(agef10));
            parameterMap.put("txml1ct10", Integer.toString(agem10 + agef10));

            parameterMap.put("txml1cm11", Integer.toString(agem11));
            parameterMap.put("txml1cf11", Integer.toString(agef11));
            parameterMap.put("txml1ct11", Integer.toString(agem11 + agef11));
            
            parameterMap.put("txml1cm12", Integer.toString(agem12));
            parameterMap.put("txml1cf12", Integer.toString(agef12));
            parameterMap.put("txml1ct12", Integer.toString(agem12 + agef12));


            System.out.println("Computing TXML 1D.....");
            //TXML 1D
            //Number of People living with HIV who are lost to follow up during the month (by Condition)
            initVariables();
            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM client WHERE cause_death = '" + Constants.CauseDeath.DEATH_CONDITION + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("txml1dm1", Integer.toString(agem1));
            parameterMap.put("txml1df1", Integer.toString(agef1));
            parameterMap.put("txml1dt1", Integer.toString(agem1 + agef1));

            parameterMap.put("txml1dm2", Integer.toString(agem2));
            parameterMap.put("txml1df2", Integer.toString(agef2));
            parameterMap.put("txml1dt2", Integer.toString(agem2 + agef2));

            parameterMap.put("txml1dm3", Integer.toString(agem3));
            parameterMap.put("txml1df3", Integer.toString(agef3));
            parameterMap.put("txml1dt3", Integer.toString(agem3 + agef3));

            parameterMap.put("txml1dm4", Integer.toString(agem4));
            parameterMap.put("txml1df4", Integer.toString(agef4));
            parameterMap.put("txml1dt4", Integer.toString(agem4 + agef4));

            parameterMap.put("txml1dm5", Integer.toString(agem5));
            parameterMap.put("txml1df5", Integer.toString(agef5));
            parameterMap.put("txml1dt5", Integer.toString(agem5 + agef5));

            parameterMap.put("txml1dm6", Integer.toString(agem6));
            parameterMap.put("txml1df6", Integer.toString(agef6));
            parameterMap.put("txml1dt6", Integer.toString(agem6 + agef6));

            parameterMap.put("txml1dm7", Integer.toString(agem7));
            parameterMap.put("txml1df7", Integer.toString(agef7));
            parameterMap.put("txml1dt7", Integer.toString(agem7 + agef7));

            parameterMap.put("txml1dm8", Integer.toString(agem8));
            parameterMap.put("txml1df8", Integer.toString(agef8));
            parameterMap.put("txml1dt8", Integer.toString(agem8 + agef8));
            
            parameterMap.put("txml1dm9", Integer.toString(agem9));
            parameterMap.put("txml1df9", Integer.toString(agef9));
            parameterMap.put("txml1dt9", Integer.toString(agem9 + agef9));

            parameterMap.put("txml1dm10", Integer.toString(agem10));
            parameterMap.put("txml1df10", Integer.toString(agef10));
            parameterMap.put("txml1dt10", Integer.toString(agem10 + agef10));

            parameterMap.put("txml1dm11", Integer.toString(agem11));
            parameterMap.put("txml1df11", Integer.toString(agef11));
            parameterMap.put("txml1dt11", Integer.toString(agem11 + agef11));
            
            parameterMap.put("txml1dm12", Integer.toString(agem12));
            parameterMap.put("txml1df12", Integer.toString(agef12));
            parameterMap.put("txml1dt12", Integer.toString(agem12 + agef12));
            

            System.out.println("Computing TXML 1E.....");
            //TXML 1E
            //Number of People living with HIV who are lost to follow up during the month (by Natural)
            initVariables();
            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM client WHERE cause_death = '" + Constants.CauseDeath.DEATH_NATURAL + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("txml1em1", Integer.toString(agem1));
            parameterMap.put("txml1ef1", Integer.toString(agef1));
            parameterMap.put("txml1et1", Integer.toString(agem1 + agef1));

            parameterMap.put("txml1em2", Integer.toString(agem2));
            parameterMap.put("txml1ef2", Integer.toString(agef2));
            parameterMap.put("txml1et2", Integer.toString(agem2 + agef2));

            parameterMap.put("txml1em3", Integer.toString(agem3));
            parameterMap.put("txml1ef3", Integer.toString(agef3));
            parameterMap.put("txml1et3", Integer.toString(agem3 + agef3));

            parameterMap.put("txml1em4", Integer.toString(agem4));
            parameterMap.put("txml1ef4", Integer.toString(agef4));
            parameterMap.put("txml1et4", Integer.toString(agem4 + agef4));

            parameterMap.put("txml1em5", Integer.toString(agem5));
            parameterMap.put("txml1ef5", Integer.toString(agef5));
            parameterMap.put("txml1et5", Integer.toString(agem5 + agef5));

            parameterMap.put("txml1em6", Integer.toString(agem6));
            parameterMap.put("txml1ef6", Integer.toString(agef6));
            parameterMap.put("txml1et6", Integer.toString(agem6 + agef6));

            parameterMap.put("txml1em7", Integer.toString(agem7));
            parameterMap.put("txml1ef7", Integer.toString(agef7));
            parameterMap.put("txml1et7", Integer.toString(agem7 + agef7));

            parameterMap.put("txml1em8", Integer.toString(agem8));
            parameterMap.put("txml1ef8", Integer.toString(agef8));
            parameterMap.put("txml1et8", Integer.toString(agem8 + agef8));
            
            parameterMap.put("txml1em9", Integer.toString(agem9));
            parameterMap.put("txml1ef9", Integer.toString(agef9));
            parameterMap.put("txml1et9", Integer.toString(agem9 + agef9));

            parameterMap.put("txml1em10", Integer.toString(agem10));
            parameterMap.put("txml1ef10", Integer.toString(agef10));
            parameterMap.put("txml1et10", Integer.toString(agem10 + agef10));

            parameterMap.put("txml1em11", Integer.toString(agem11));
            parameterMap.put("txml1ef11", Integer.toString(agef11));
            parameterMap.put("txml1et11", Integer.toString(agem11 + agef11));
            
            parameterMap.put("txml1em12", Integer.toString(agem12));
            parameterMap.put("txml1ef12", Integer.toString(agef12));
            parameterMap.put("txml1et12", Integer.toString(agem12 + agef12));
            

            System.out.println("Computing TXML 1F.....");
            //TXML 1F
            //Number of People living with HIV who are lost to follow up during the month (by non Natural)
            initVariables();
            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM client WHERE cause_death = '" + Constants.CauseDeath.DEATH_NON_NATURAL + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("txml1fm1", Integer.toString(agem1));
            parameterMap.put("txml1ff1", Integer.toString(agef1));
            parameterMap.put("txml1ft1", Integer.toString(agem1 + agef1));

            parameterMap.put("txml1fm2", Integer.toString(agem2));
            parameterMap.put("txml1ff2", Integer.toString(agef2));
            parameterMap.put("txml1ft2", Integer.toString(agem2 + agef2));

            parameterMap.put("txml1fm3", Integer.toString(agem3));
            parameterMap.put("txml1ff3", Integer.toString(agef3));
            parameterMap.put("txml1ft3", Integer.toString(agem3 + agef3));

            parameterMap.put("txml1fm4", Integer.toString(agem4));
            parameterMap.put("txml1ff4", Integer.toString(agef4));
            parameterMap.put("txml1ft4", Integer.toString(agem4 + agef4));

            parameterMap.put("txml1fm5", Integer.toString(agem5));
            parameterMap.put("txml1ff5", Integer.toString(agef5));
            parameterMap.put("txml1ft5", Integer.toString(agem5 + agef5));

            parameterMap.put("txml1fm6", Integer.toString(agem6));
            parameterMap.put("txml1ff6", Integer.toString(agef6));
            parameterMap.put("txml1ft6", Integer.toString(agem6 + agef6));

            parameterMap.put("txml1fm7", Integer.toString(agem7));
            parameterMap.put("txml1ff7", Integer.toString(agef7));
            parameterMap.put("txml1ft7", Integer.toString(agem7 + agef7));

            parameterMap.put("txml1fm8", Integer.toString(agem8));
            parameterMap.put("txml1ff8", Integer.toString(agef8));
            parameterMap.put("txml1ft8", Integer.toString(agem8 + agef8));
            
            parameterMap.put("txml1fm9", Integer.toString(agem9));
            parameterMap.put("txml1ff9", Integer.toString(agef9));
            parameterMap.put("txml1ft9", Integer.toString(agem9 + agef9));

            parameterMap.put("txml1fm10", Integer.toString(agem10));
            parameterMap.put("txml1ff10", Integer.toString(agef10));
            parameterMap.put("txml1ft10", Integer.toString(agem10 + agef10));

            parameterMap.put("txml1fm11", Integer.toString(agem11));
            parameterMap.put("txml1ff11", Integer.toString(agef11));
            parameterMap.put("txml1ft11", Integer.toString(agem11 + agef11));
            
            parameterMap.put("txml1fm12", Integer.toString(agem12));
            parameterMap.put("txml1ff12", Integer.toString(agef12));
            parameterMap.put("txml1ft12", Integer.toString(agem12 + agef12));

            System.out.println("Computing TXMl 2.....");
            //TxMl 2
            //Previously Undocumented Patient Transfer (Confirmed) this month
            initVariables();

            executeUpdate("DROP INDEX IF EXISTS idx_client");                       
            executeUpdate("DROP TABLE IF EXISTS client");        
            executeUpdate("CREATE TEMPORARY TABLE client AS SELECT * FROM patient WHERE facility_id = " + facilityId + " AND YEAR(date_tracked) = " + reportingYear + " AND MONTH(date_tracked) = " + reportingMonth);
            executeUpdate("CREATE INDEX idx_client ON client(patient_id)");

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM client WHERE outcome = '" + Constants.TxMlStatus.TX_ML_TRANSFER + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("txml2m1", Integer.toString(agem1));
            parameterMap.put("txml2f1", Integer.toString(agef1));
            parameterMap.put("txml2t1", Integer.toString(agem1 + agef1));

            parameterMap.put("txml2m2", Integer.toString(agem2));
            parameterMap.put("txml2f2", Integer.toString(agef2));
            parameterMap.put("txml2t2", Integer.toString(agem2 + agef2));

            parameterMap.put("txml2m3", Integer.toString(agem3));
            parameterMap.put("txml2f3", Integer.toString(agef3));
            parameterMap.put("txml2t3", Integer.toString(agem3 + agef3));

            parameterMap.put("txml2m4", Integer.toString(agem4));
            parameterMap.put("txml2f4", Integer.toString(agef4));
            parameterMap.put("txml2t4", Integer.toString(agem4 + agef4));

            parameterMap.put("txml2m5", Integer.toString(agem5));
            parameterMap.put("txml2f5", Integer.toString(agef5));
            parameterMap.put("txml2t5", Integer.toString(agem5 + agef5));

            parameterMap.put("txml2m6", Integer.toString(agem6));
            parameterMap.put("txml2f6", Integer.toString(agef6));
            parameterMap.put("txml2t6", Integer.toString(agem6 + agef6));

            parameterMap.put("txml2m7", Integer.toString(agem7));
            parameterMap.put("txml2f7", Integer.toString(agef7));
            parameterMap.put("txml2t7", Integer.toString(agem7 + agef7));

            parameterMap.put("txml2m8", Integer.toString(agem8));
            parameterMap.put("txml2f8", Integer.toString(agef8));
            parameterMap.put("txml2t8", Integer.toString(agem8 + agef8));

            parameterMap.put("txml2m9", Integer.toString(agem9));
            parameterMap.put("txml2f9", Integer.toString(agef9));
            parameterMap.put("txml2t9", Integer.toString(agem9 + agef9));

            parameterMap.put("txml2m10", Integer.toString(agem10));
            parameterMap.put("txml2f10", Integer.toString(agef10));
            parameterMap.put("txml2t10", Integer.toString(agem10 + agef10));

            parameterMap.put("txml2m11", Integer.toString(agem11));
            parameterMap.put("txml2f11", Integer.toString(agef11));
            parameterMap.put("txml2t11", Integer.toString(agem11 + agef11));
            
            parameterMap.put("txml2m12", Integer.toString(agem12));
            parameterMap.put("txml2f12", Integer.toString(agef12));
            parameterMap.put("txml2t12", Integer.toString(agem12 + agef12));
            
            System.out.println("Computing TXMl 3.....");
            //TxMl 3
            //Traced Patient (Unable to locate)
            initVariables();

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM client WHERE outcome = '" + Constants.TxMlStatus.TX_ML_TRACED + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("txml3m1", Integer.toString(agem1));
            parameterMap.put("txml3f1", Integer.toString(agef1));
            parameterMap.put("txml3t1", Integer.toString(agem1 + agef1));

            parameterMap.put("txml3m2", Integer.toString(agem2));
            parameterMap.put("txml3f2", Integer.toString(agef2));
            parameterMap.put("txml3t2", Integer.toString(agem2 + agef2));

            parameterMap.put("txml3m3", Integer.toString(agem3));
            parameterMap.put("txml3f3", Integer.toString(agef3));
            parameterMap.put("txml3t3", Integer.toString(agem3 + agef3));

            parameterMap.put("txml3m4", Integer.toString(agem4));
            parameterMap.put("txml3f4", Integer.toString(agef4));
            parameterMap.put("txml3t4", Integer.toString(agem4 + agef4));

            parameterMap.put("txml3m5", Integer.toString(agem5));
            parameterMap.put("txml3f5", Integer.toString(agef5));
            parameterMap.put("txml3t5", Integer.toString(agem5 + agef5));

            parameterMap.put("txml3m6", Integer.toString(agem6));
            parameterMap.put("txml3f6", Integer.toString(agef6));
            parameterMap.put("txml3t6", Integer.toString(agem6 + agef6));

            parameterMap.put("txml3m7", Integer.toString(agem7));
            parameterMap.put("txml3f7", Integer.toString(agef7));
            parameterMap.put("txml3t7", Integer.toString(agem7 + agef7));

            parameterMap.put("txml3m8", Integer.toString(agem8));
            parameterMap.put("txml3f8", Integer.toString(agef8));
            parameterMap.put("txml3t8", Integer.toString(agem8 + agef8));

            parameterMap.put("txml3m9", Integer.toString(agem9));
            parameterMap.put("txml3f9", Integer.toString(agef9));
            parameterMap.put("txml3t9", Integer.toString(agem9 + agef9));

            parameterMap.put("txml3m10", Integer.toString(agem10));
            parameterMap.put("txml3f10", Integer.toString(agef10));
            parameterMap.put("txml3t10", Integer.toString(agem10 + agef10));

            parameterMap.put("txml3m11", Integer.toString(agem11));
            parameterMap.put("txml3f11", Integer.toString(agef11));
            parameterMap.put("txml3t11", Integer.toString(agem11 + agef11));
            
            parameterMap.put("txml3m12", Integer.toString(agem12));
            parameterMap.put("txml3f12", Integer.toString(agef12));
            parameterMap.put("txml3t12", Integer.toString(agem12 + agef12));

            System.out.println("Computing TXMl 4.....");
            //TxMl 4
            //Did Not Attempt to Trace Patient
            initVariables();

            query = "SELECT DISTINCT patient_id, gender, date_birth, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM client WHERE ((current_status IN (" + Constants.ClientStatus.NON_ART+ ") AND DATEDIFF(DAY, date_next_refill, CURDATE()) >= 1) OR (current_status IN ('Lost to Follow Up', 'Stopped Treatment') AND DATEDIFF(DAY, agreed_date, CURDATE()) >= 1)) AND date_started IS NOT NULL AND (outcome IS NULL OR outcome = '')"; 
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("txml4m1", Integer.toString(agem1));
            parameterMap.put("txml4f1", Integer.toString(agef1));
            parameterMap.put("txml4t1", Integer.toString(agem1 + agef1));

            parameterMap.put("txml4m2", Integer.toString(agem2));
            parameterMap.put("txml4f2", Integer.toString(agef2));
            parameterMap.put("txml4t2", Integer.toString(agem2 + agef2));

            parameterMap.put("txml4m3", Integer.toString(agem3));
            parameterMap.put("txml4f3", Integer.toString(agef3));
            parameterMap.put("txml4t3", Integer.toString(agem3 + agef3));

            parameterMap.put("txml4m4", Integer.toString(agem4));
            parameterMap.put("txml4f4", Integer.toString(agef4));
            parameterMap.put("txml4t4", Integer.toString(agem4 + agef4));

            parameterMap.put("txml4m5", Integer.toString(agem5));
            parameterMap.put("txml4f5", Integer.toString(agef5));
            parameterMap.put("txml4t5", Integer.toString(agem5 + agef5));

            parameterMap.put("txml4m6", Integer.toString(agem6));
            parameterMap.put("txml4f6", Integer.toString(agef6));
            parameterMap.put("txml4t6", Integer.toString(agem6 + agef6));

            parameterMap.put("txml4m7", Integer.toString(agem7));
            parameterMap.put("txml4f7", Integer.toString(agef7));
            parameterMap.put("txml4t7", Integer.toString(agem7 + agef7));

            parameterMap.put("txml4m8", Integer.toString(agem8));
            parameterMap.put("txml4f8", Integer.toString(agef8));
            parameterMap.put("txml4t8", Integer.toString(agem8 + agef8));

            parameterMap.put("txml4m9", Integer.toString(agem9));
            parameterMap.put("txml4f9", Integer.toString(agef9));
            parameterMap.put("txml4t9", Integer.toString(agem9 + agef9));

            parameterMap.put("txml4m10", Integer.toString(agem10));
            parameterMap.put("txml4f10", Integer.toString(agef10));
            parameterMap.put("txml4t10", Integer.toString(agem10 + agef10));

            parameterMap.put("txml4m11", Integer.toString(agem11));
            parameterMap.put("txml4f11", Integer.toString(agef11));
            parameterMap.put("txml4t11", Integer.toString(agem11 + agef11));
            
            parameterMap.put("txml4m12", Integer.toString(agem12));
            parameterMap.put("txml4f12", Integer.toString(agef12));
            parameterMap.put("txml4t12", Integer.toString(agem12 + agef12));

            
            System.out.println("Adding headers.....");
            //Include reproting period & facility details into report header 
            parameterMap.put("reportingMonth", month);
            parameterMap.put("reportingYear", year);

            query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility JOIN lga ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE facility_id = " + facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                parameterMap.put("facilityName", rs.getString("name"));
                parameterMap.put("facilityType", "");
                parameterMap.put("lga", rs.getString("lga"));
                parameterMap.put("state", rs.getString("state"));
                parameterMap.put("level", "");
            }
        } catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return parameterMap;
    }

    private void disaggregate(String gender, int age) {
        if (gender.trim().equalsIgnoreCase("Male")) {
            if (age < 1) {
                agem1++;
            } else {
                if (age >= 1 && age <= 4) {
                    agem2++;
                } else {
                    if (age >= 5 && age <= 9) {
                        agem3++;
                    } else {
                        if (age >= 10 && age <= 14) {
                            agem4++;
                        } else {
                            if (age >= 15 && age <= 19) {
                                agem5++;
                            } else {
                                if (age >= 20 && age <= 24) {
                                    agem6++;
                                } else {
                                    if (age >= 25 && age <= 29) {
                                        agem7++;
                                    } else {
                                        if (age >= 30 && age <= 34) {
                                            agem8++;
                                        } else {
                                            if (age >= 35 && age <= 39) {
                                                agem9++;
                                            } else {
                                                if (age >= 40 && age <= 44) {
                                                    agem10++;
                                                } else {
                                                    if (age >= 45 && age <= 49) {
                                                        agem11++;
                                                    } else {
                                                        if (age >= 50) {
                                                            agem12++;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (age < 1) {
                agef1++;
            } else {
                if (age >= 1 && age <= 4) {
                    agef2++;
                } else {
                    if (age >= 5 && age <= 9) {
                        agef3++;
                    } else {
                        if (age >= 10 && age <= 14) {
                            agef4++;
                        } else {
                            if (age >= 15 && age <= 19) {
                                agef5++;
                            } else {
                                if (age >= 20 && age <= 24) {
                                    agef6++;
                                } else {
                                    if (age >= 25 && age <= 29) {
                                        agef7++;
                                    } else {
                                        if (age >= 30 && age <= 34) {
                                            agef8++;
                                        } else {
                                            if (age >= 35 && age <= 39) {
                                                agef9++;
                                            } else {
                                                if (age >= 40 && age <= 44) {
                                                    agef10++;
                                                } else {
                                                    if (age >= 45 && age <= 49) {
                                                        agef11++;
                                                    } else {
                                                        if (age >= 50) {
                                                            agef12++;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return rs;
    }

    private void executeUpdate(String query) {
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private boolean found(String query) {
        boolean found = false;
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                found = true;
            }
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return found;
    }

    private void initVariables() {
        agem1 = 0;
        agem2 = 0;
        agem3 = 0;
        agem4 = 0;
        agem5 = 0;
        agem6 = 0;
        agem7 = 0;
        agem8 = 0;
        agem9 = 0;
        agem10 = 0;
        agem11 = 0;
        agem12 = 0;
        
        agef1 = 0;
        agef2 = 0;
        agef3 = 0;
        agef4 = 0;
        agef5 = 0;
        agef6 = 0;
        agef7 = 0;
        agef8 = 0;
        agef9 = 0;
        agef10 = 0;
        agef11 = 0;
        agef12 = 0;
    }

}
