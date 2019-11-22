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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.fhi360.lamis.utility.StringUtil.isInteger;

/**
 * @author user10
 */
@Component
public class ArtAddendumSummaryProcessor {

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //private static final Log log = LogFactory.getLog(ArtSummaryProcessor.class);

    private int agem1, agem2, agem3, agem4, agem5, agem6, agem7, agem8, agem9, agem10, agem11, agem12;
    ;
    private int agef1, agef2, agef3, agef4, agef5, agef6, agef7, agef8, agef9, agef10, agef11, agef12;


    public ArtAddendumSummaryProcessor() {
    }

    public Map<String, Object> process(String month, String year, Long facilityId) {

        Map<String, Object> parameterMap = new HashMap<>();
        int reportingMonth = DateUtil.getMonth(month);
        int reportingYear = Integer.parseInt(year);
        String reportingDateBegin = dateFormat.format(DateUtil.getFirstDateOfMonth(reportingYear, reportingMonth));
        String reportingDateEnd = dateFormat.format(DateUtil.getLastDateOfMonth(reportingYear, reportingMonth));

        ResultSet resultSet;
        try {
            jdbcUtil = new JDBCUtil();

            ResultSet rs;
            System.out.println("Computing EAC1.....");
            //EAC1
            //Number of virally unsuppressed PLHIV who received 1st EAC during the month
            initVariables();

            query = "SELECT DISTINCT p.patient_id, p.gender, DATEDIFF(YEAR, p.date_birth, '" + reportingDateBegin + "') AS age FROM patient  p JOIN eac e ON p.patient_id = e.patient_id WHERE YEAR(e.date_eac1) = " + reportingYear + " AND MONTH(e.date_eac1) = " + reportingMonth;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for EAC1
            parameterMap.put("art1m1", Integer.toString(agem1));
            parameterMap.put("art1f1", Integer.toString(agef1));
            parameterMap.put("art1t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art1m2", Integer.toString(agem2));
            parameterMap.put("art1f2", Integer.toString(agef2));
            parameterMap.put("art1t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art1m3", Integer.toString(agem3));
            parameterMap.put("art1f3", Integer.toString(agef3));
            parameterMap.put("art1t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art1m4", Integer.toString(agem4));
            parameterMap.put("art1f4", Integer.toString(agef4));
            parameterMap.put("art1t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art1m5", Integer.toString(agem5));
            parameterMap.put("art1f5", Integer.toString(agef5));
            parameterMap.put("art1t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art1m6", Integer.toString(agem6));
            parameterMap.put("art1f6", Integer.toString(agef6));
            parameterMap.put("art1t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art1m7", Integer.toString(agem7));
            parameterMap.put("art1f7", Integer.toString(agef7));
            parameterMap.put("art1t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art1m8", Integer.toString(agem8));
            parameterMap.put("art1f8", Integer.toString(agef8));
            parameterMap.put("art1t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art1m9", Integer.toString(agem9));
            parameterMap.put("art1f9", Integer.toString(agef9));
            parameterMap.put("art1t9", Integer.toString(agem9 + agef9));

            parameterMap.put("art1m10", Integer.toString(agem10));
            parameterMap.put("art1f10", Integer.toString(agef10));
            parameterMap.put("art1t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art1m11", Integer.toString(agem11));
            parameterMap.put("art1f11", Integer.toString(agef11));
            parameterMap.put("art1t11", Integer.toString(agem11 + agef11));

            parameterMap.put("art1m12", Integer.toString(agem12));
            parameterMap.put("art1f12", Integer.toString(agef12));
            parameterMap.put("art1t12", Integer.toString(agem12 + agef12));

            System.out.println("Computing EAC2.....");
            //EAC2
            //Total number of people living with HIV known to have died during the month (by TB)
            initVariables();
            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM eac WHERE YEAR(date_eac2) = " + reportingYear + " AND MONTH(date_eac2) = " + reportingMonth;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for EAC1
            parameterMap.put("art2m1", Integer.toString(agem1));
            parameterMap.put("art2f1", Integer.toString(agef1));
            parameterMap.put("art2t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art2m2", Integer.toString(agem2));
            parameterMap.put("art2f2", Integer.toString(agef2));
            parameterMap.put("art2t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art2m3", Integer.toString(agem3));
            parameterMap.put("art2f3", Integer.toString(agef3));
            parameterMap.put("art2t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art2m4", Integer.toString(agem4));
            parameterMap.put("art2f4", Integer.toString(agef4));
            parameterMap.put("art2t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art2m5", Integer.toString(agem5));
            parameterMap.put("art2f5", Integer.toString(agef5));
            parameterMap.put("art2t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art2m6", Integer.toString(agem6));
            parameterMap.put("art2f6", Integer.toString(agef6));
            parameterMap.put("art2t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art2m7", Integer.toString(agem7));
            parameterMap.put("art2f7", Integer.toString(agef7));
            parameterMap.put("art2t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art2m8", Integer.toString(agem8));
            parameterMap.put("art2f8", Integer.toString(agef8));
            parameterMap.put("art2t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art2m9", Integer.toString(agem9));
            parameterMap.put("art2f9", Integer.toString(agef9));
            parameterMap.put("art2t9", Integer.toString(agem9 + agef9));

            parameterMap.put("art2m10", Integer.toString(agem10));
            parameterMap.put("art2f10", Integer.toString(agef10));
            parameterMap.put("art2t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art2m11", Integer.toString(agem11));
            parameterMap.put("art2f11", Integer.toString(agef11));
            parameterMap.put("art2t11", Integer.toString(agem11 + agef11));

            parameterMap.put("art2m12", Integer.toString(agem12));
            parameterMap.put("art2f12", Integer.toString(agef12));
            parameterMap.put("art2t12", Integer.toString(agem12 + agef12));

            System.out.println("Computing EAC3.....");
            //EAC3
            //Number of virally unsuppressed PLHIV who received 3rd EAC during the month
            initVariables();
            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM eac WHERE YEAR(date_eac3) = " + reportingYear + " AND MONTH(date_eac3) = " + reportingMonth;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for EAC2
            parameterMap.put("art3m1", Integer.toString(agem1));
            parameterMap.put("art3f1", Integer.toString(agef1));
            parameterMap.put("art3t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art3m2", Integer.toString(agem2));
            parameterMap.put("art3f2", Integer.toString(agef2));
            parameterMap.put("art3t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art3m3", Integer.toString(agem3));
            parameterMap.put("art3f3", Integer.toString(agef3));
            parameterMap.put("art3t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art3m4", Integer.toString(agem4));
            parameterMap.put("art3f4", Integer.toString(agef4));
            parameterMap.put("art3t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art3m5", Integer.toString(agem5));
            parameterMap.put("art3f5", Integer.toString(agef5));
            parameterMap.put("art3t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art3m6", Integer.toString(agem6));
            parameterMap.put("art3f6", Integer.toString(agef6));
            parameterMap.put("art3t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art3m7", Integer.toString(agem7));
            parameterMap.put("art3f7", Integer.toString(agef7));
            parameterMap.put("art3t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art3m8", Integer.toString(agem8));
            parameterMap.put("art3f8", Integer.toString(agef8));
            parameterMap.put("art3t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art3m9", Integer.toString(agem9));
            parameterMap.put("art3f9", Integer.toString(agef9));
            parameterMap.put("art3t9", Integer.toString(agem9 + agef9));

            parameterMap.put("art3m10", Integer.toString(agem10));
            parameterMap.put("art3f10", Integer.toString(agef10));
            parameterMap.put("art3t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art3m11", Integer.toString(agem11));
            parameterMap.put("art3f11", Integer.toString(agef11));
            parameterMap.put("art3t11", Integer.toString(agem11 + agef11));

            parameterMap.put("art3m12", Integer.toString(agem12));
            parameterMap.put("art3f12", Integer.toString(agef12));
            parameterMap.put("art3t12", Integer.toString(agem12 + agef12));

            System.out.println("Computing EAC4.....");
            //EAC4
            //Number of virally unsuppressed PLHIV who had a targeted VL test done after completing 3rd EAC during the month
            initVariables();

            executeUpdate("DROP INDEX IF EXISTS idx_lab");
            executeUpdate("DROP TABLE IF EXISTS lab");
            executeUpdate("CREATE TEMPORARY TABLE lab AS SELECT * FROM laboratory WHERE facility_id = " + facilityId + " AND date_reported >= '" + reportingDateEnd + "' AND labtest_id = 16");
            executeUpdate("CREATE INDEX idx_lab ON lab(patient_id)");

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM eac WHERE YEAR(date_eac3) = " + reportingYear + " AND MONTH(date_eac3) = " + reportingMonth;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                //Check if the last viral load after the reporting month is  greater than 1000
                query = "SELECT * FROM lab WHERE patient_id = " + patientId + " ORDER BY date_reported DESC LIMIT 1";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    disaggregate(gender, age);
                }
            }

            //Populate the report parameter map with values computed for EAC4
            parameterMap.put("art4m1", Integer.toString(agem1));
            parameterMap.put("art4f1", Integer.toString(agef1));
            parameterMap.put("art4t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art4m2", Integer.toString(agem2));
            parameterMap.put("art4f2", Integer.toString(agef2));
            parameterMap.put("art4t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art4m3", Integer.toString(agem3));
            parameterMap.put("art4f3", Integer.toString(agef3));
            parameterMap.put("art4t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art4m4", Integer.toString(agem4));
            parameterMap.put("art4f4", Integer.toString(agef4));
            parameterMap.put("art4t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art4m5", Integer.toString(agem5));
            parameterMap.put("art4f5", Integer.toString(agef5));
            parameterMap.put("art4t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art4m6", Integer.toString(agem6));
            parameterMap.put("art4f6", Integer.toString(agef6));
            parameterMap.put("art4t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art4m7", Integer.toString(agem7));
            parameterMap.put("art4f7", Integer.toString(agef7));
            parameterMap.put("art4t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art4m8", Integer.toString(agem8));
            parameterMap.put("art4f8", Integer.toString(agef8));
            parameterMap.put("art4t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art4m9", Integer.toString(agem9));
            parameterMap.put("art4f9", Integer.toString(agef9));
            parameterMap.put("art4t9", Integer.toString(agem9 + agef9));

            parameterMap.put("art4m10", Integer.toString(agem10));
            parameterMap.put("art4f10", Integer.toString(agef10));
            parameterMap.put("art4t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art4m11", Integer.toString(agem11));
            parameterMap.put("art4f11", Integer.toString(agef11));
            parameterMap.put("art4t11", Integer.toString(agem11 + agef11));

            parameterMap.put("art4m12", Integer.toString(agem12));
            parameterMap.put("art4f12", Integer.toString(agef12));
            parameterMap.put("art4t12", Integer.toString(agem12 + agef12));


            System.out.println("Computing EAC5.....");
            //EAC5
            //Number of People living with HIV who are lost to follow up during the month (by Condition)
            initVariables();

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM eac WHERE YEAR(date_eac3) = " + reportingYear + " AND MONTH(date_eac3) = " + reportingMonth;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                //Check if the last viral load after the reporting month is  greater than 1000
                query = "SELECT * FROM lab WHERE patient_id = " + patientId + " ORDER BY date_reported DESC LIMIT 1";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String resultab = rs.getString("resultab");
                    if (isInteger(resultab)) {
                        if (Double.valueOf(resultab) >= 1000) {
                            disaggregate(gender, age);
                        }
                    }
                }
            }

            //Populate the report parameter map with values computed for EAC5
            parameterMap.put("art5m1", Integer.toString(agem1));
            parameterMap.put("art5f1", Integer.toString(agef1));
            parameterMap.put("art5t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art5m2", Integer.toString(agem2));
            parameterMap.put("art5f2", Integer.toString(agef2));
            parameterMap.put("art5t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art5m3", Integer.toString(agem3));
            parameterMap.put("art5f3", Integer.toString(agef3));
            parameterMap.put("art5t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art5m4", Integer.toString(agem4));
            parameterMap.put("art5f4", Integer.toString(agef4));
            parameterMap.put("art5t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art5m5", Integer.toString(agem5));
            parameterMap.put("art5f5", Integer.toString(agef5));
            parameterMap.put("art5t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art5m6", Integer.toString(agem6));
            parameterMap.put("art5f6", Integer.toString(agef6));
            parameterMap.put("art5t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art5m7", Integer.toString(agem7));
            parameterMap.put("art5f7", Integer.toString(agef7));
            parameterMap.put("art5t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art5m8", Integer.toString(agem8));
            parameterMap.put("art5f8", Integer.toString(agef8));
            parameterMap.put("art5t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art5m9", Integer.toString(agem9));
            parameterMap.put("art5f9", Integer.toString(agef9));
            parameterMap.put("art5t9", Integer.toString(agem9 + agef9));

            parameterMap.put("art5m10", Integer.toString(agem10));
            parameterMap.put("art5f10", Integer.toString(agef10));
            parameterMap.put("art5t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art5m11", Integer.toString(agem11));
            parameterMap.put("art5f11", Integer.toString(agef11));
            parameterMap.put("art5t11", Integer.toString(agem11 + agef11));

            parameterMap.put("art5m12", Integer.toString(agem12));
            parameterMap.put("art5f12", Integer.toString(agef12));
            parameterMap.put("art5t12", Integer.toString(agem12 + agef12));


            System.out.println("Computing EAC6.....");
            //EAC6
            //Number of People living with HIV who are lost to follow up during the month (by Natural)
            initVariables();
//            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM client WHERE cause_death = '" + Constants.CauseDeath.DEATH_NATURAL + "'";
//            preparedStatement = jdbcUtil.getStatement(query);
//            resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                String gender = resultSet.getString("gender");
//                int age = resultSet.getInt("age");
//                disaggregate(gender, age);
//            }

            //Populate the report parameter map with values computed for EAC6
            parameterMap.put("art6m1", Integer.toString(agem1));
            parameterMap.put("art6f1", Integer.toString(agef1));
            parameterMap.put("art6t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art6m2", Integer.toString(agem2));
            parameterMap.put("art6f2", Integer.toString(agef2));
            parameterMap.put("art6t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art6m3", Integer.toString(agem3));
            parameterMap.put("art6f3", Integer.toString(agef3));
            parameterMap.put("art6t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art6m4", Integer.toString(agem4));
            parameterMap.put("art6f4", Integer.toString(agef4));
            parameterMap.put("art6t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art6m5", Integer.toString(agem5));
            parameterMap.put("art6f5", Integer.toString(agef5));
            parameterMap.put("art6t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art6m6", Integer.toString(agem6));
            parameterMap.put("art6f6", Integer.toString(agef6));
            parameterMap.put("art6t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art6m7", Integer.toString(agem7));
            parameterMap.put("art6f7", Integer.toString(agef7));
            parameterMap.put("art6t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art6m8", Integer.toString(agem8));
            parameterMap.put("art6f8", Integer.toString(agef8));
            parameterMap.put("art6t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art6m9", Integer.toString(agem9));
            parameterMap.put("art6f9", Integer.toString(agef9));
            parameterMap.put("art6t9", Integer.toString(agem9 + agef9));

            parameterMap.put("art6m10", Integer.toString(agem10));
            parameterMap.put("art6f10", Integer.toString(agef10));
            parameterMap.put("art6t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art6m11", Integer.toString(agem11));
            parameterMap.put("art6f11", Integer.toString(agef11));
            parameterMap.put("art6t11", Integer.toString(agem11 + agef11));

            parameterMap.put("art6m12", Integer.toString(agem12));
            parameterMap.put("art6f12", Integer.toString(agef12));
            parameterMap.put("art6t12", Integer.toString(agem12 + agef12));


            System.out.println("Computing DMOC1.....");
            //DMOC1
            //Number of People living with HIV who are lost to follow up during the month (by non Natural)
            initVariables();
            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM devolve WHERE YEAR(date_devolved) = " + reportingYear + " AND MONTH(date_devolved) = " + reportingMonth;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("art7m1", Integer.toString(agem1));
            parameterMap.put("art7f1", Integer.toString(agef1));
            parameterMap.put("art7t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art7m2", Integer.toString(agem2));
            parameterMap.put("art7f2", Integer.toString(agef2));
            parameterMap.put("art7t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art7m3", Integer.toString(agem3));
            parameterMap.put("art7f3", Integer.toString(agef3));
            parameterMap.put("art7t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art7m4", Integer.toString(agem4));
            parameterMap.put("art7f4", Integer.toString(agef4));
            parameterMap.put("art7t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art7m5", Integer.toString(agem5));
            parameterMap.put("art7f5", Integer.toString(agef5));
            parameterMap.put("art7t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art7m6", Integer.toString(agem6));
            parameterMap.put("art7f6", Integer.toString(agef6));
            parameterMap.put("art7t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art7m7", Integer.toString(agem7));
            parameterMap.put("art7f7", Integer.toString(agef7));
            parameterMap.put("art7t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art7m8", Integer.toString(agem8));
            parameterMap.put("art7f8", Integer.toString(agef8));
            parameterMap.put("art7t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art7m9", Integer.toString(agem9));
            parameterMap.put("art7f9", Integer.toString(agef9));
            parameterMap.put("art7t9", Integer.toString(agem9 + agef9));

            parameterMap.put("art7m10", Integer.toString(agem10));
            parameterMap.put("art7f10", Integer.toString(agef10));
            parameterMap.put("art7t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art7m11", Integer.toString(agem11));
            parameterMap.put("art7f11", Integer.toString(agef11));
            parameterMap.put("art7t11", Integer.toString(agem11 + agef11));

            parameterMap.put("art7m12", Integer.toString(agem12));
            parameterMap.put("art7f12", Integer.toString(agef12));
            parameterMap.put("art7t12", Integer.toString(agem12 + agef12));

            System.out.println("Computing DMOC2.....");
            //TDMOC1 a
            //Previously Undocumented Patient Transfer (Confirmed) this month
            initVariables();

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM devolve WHERE YEAR(date_devolved) = " + reportingYear + " AND MONTH(date_devolved) = " + reportingMonth + " AND (type_dmoc = '" + Constants.DMOCType.MMD + " OR type_dmoc = '" + Constants.DMOCType.MMS + "')";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("art7_1m1", Integer.toString(agem1));
            parameterMap.put("art7_1f1", Integer.toString(agef1));
            parameterMap.put("art7t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art7_1m2", Integer.toString(agem2));
            parameterMap.put("art7_1f2", Integer.toString(agef2));
            parameterMap.put("art7_1t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art7_1m3", Integer.toString(agem3));
            parameterMap.put("art7_1f3", Integer.toString(agef3));
            parameterMap.put("art7_1t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art7_1m4", Integer.toString(agem4));
            parameterMap.put("art7_1f4", Integer.toString(agef4));
            parameterMap.put("art7_1t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art7_1m5", Integer.toString(agem5));
            parameterMap.put("art7_1f5", Integer.toString(agef5));
            parameterMap.put("art7_1t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art7_1m6", Integer.toString(agem6));
            parameterMap.put("art7_1f6", Integer.toString(agef6));
            parameterMap.put("art7_1t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art7_1m7", Integer.toString(agem7));
            parameterMap.put("art7_1f7", Integer.toString(agef7));
            parameterMap.put("art7_1t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art7_1m8", Integer.toString(agem8));
            parameterMap.put("art7_1f8", Integer.toString(agef8));
            parameterMap.put("art7_1t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art7_1m9", Integer.toString(agem9));
            parameterMap.put("art7_1f9", Integer.toString(agef9));
            parameterMap.put("art7_1t9", Integer.toString(agem9 + agef9));

            parameterMap.put("art7_1m10", Integer.toString(agem10));
            parameterMap.put("art7_1f10", Integer.toString(agef10));
            parameterMap.put("art7_1t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art7_1m11", Integer.toString(agem11));
            parameterMap.put("art7_1f11", Integer.toString(agef11));
            parameterMap.put("art7_1t11", Integer.toString(agem11 + agef11));

            parameterMap.put("art7_1m12", Integer.toString(agem12));
            parameterMap.put("art7_1f12", Integer.toString(agef12));
            parameterMap.put("art7_1t12", Integer.toString(agem12 + agef12));

            System.out.println("Computing DMOC2.....");
            //DMOC1 b
            //Previously Undocumented Patient Transfer (Confirmed) this month
            initVariables();

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM devolve WHERE YEAR(date_devolved) = " + reportingYear + " AND MONTH(date_devolved) = " + reportingMonth + " AND type_dmoc = '" + Constants.DMOCType.CPARP + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("art7_2m1", Integer.toString(agem1));
            parameterMap.put("art7_2f1", Integer.toString(agef1));
            parameterMap.put("art7_2t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art7_2m2", Integer.toString(agem2));
            parameterMap.put("art7_2f2", Integer.toString(agef2));
            parameterMap.put("art7_2t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art7_2m3", Integer.toString(agem3));
            parameterMap.put("art7_2f3", Integer.toString(agef3));
            parameterMap.put("art7_2t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art7_2m4", Integer.toString(agem4));
            parameterMap.put("art7_2f4", Integer.toString(agef4));
            parameterMap.put("art7_2t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art7_2m5", Integer.toString(agem5));
            parameterMap.put("art7_2f5", Integer.toString(agef5));
            parameterMap.put("art7_2t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art7_2m6", Integer.toString(agem6));
            parameterMap.put("art7_2f6", Integer.toString(agef6));
            parameterMap.put("art7_2t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art7_2m7", Integer.toString(agem7));
            parameterMap.put("art7_2f7", Integer.toString(agef7));
            parameterMap.put("art7_2t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art7_2m8", Integer.toString(agem8));
            parameterMap.put("art7_2f8", Integer.toString(agef8));
            parameterMap.put("art7_2t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art7_2m9", Integer.toString(agem9));
            parameterMap.put("art7_2f9", Integer.toString(agef9));
            parameterMap.put("art7_2t9", Integer.toString(agem9 + agef9));

            parameterMap.put("art7_2m10", Integer.toString(agem10));
            parameterMap.put("art7_2f10", Integer.toString(agef10));
            parameterMap.put("art7_2t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art7_2m11", Integer.toString(agem11));
            parameterMap.put("art7_2f11", Integer.toString(agef11));
            parameterMap.put("art7_2t11", Integer.toString(agem11 + agef11));

            parameterMap.put("art7_2m12", Integer.toString(agem12));
            parameterMap.put("art7_2f12", Integer.toString(agef12));
            parameterMap.put("art7_2t12", Integer.toString(agem12 + agef12));

            System.out.println("Computing DMOC2.....");
            //DMOC1 c
            //Previously Undocumented Patient Transfer (Confirmed) this month
            initVariables();

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM devolve WHERE YEAR(date_devolved) = " + reportingYear + " AND MONTH(date_devolved) = " + reportingMonth + " AND type_dmoc = '" + Constants.DMOCType.CARC + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("art7_3m1", Integer.toString(agem1));
            parameterMap.put("art7_3f1", Integer.toString(agef1));
            parameterMap.put("art7_3t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art7_3m2", Integer.toString(agem2));
            parameterMap.put("art7_3f2", Integer.toString(agef2));
            parameterMap.put("art7_3t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art7_3m3", Integer.toString(agem3));
            parameterMap.put("art7_3f3", Integer.toString(agef3));
            parameterMap.put("art7_3t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art7_3m4", Integer.toString(agem4));
            parameterMap.put("art7_3f4", Integer.toString(agef4));
            parameterMap.put("art7_3t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art7_3m5", Integer.toString(agem5));
            parameterMap.put("art7_3f5", Integer.toString(agef5));
            parameterMap.put("art7_3t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art7_3m6", Integer.toString(agem6));
            parameterMap.put("art7_3f6", Integer.toString(agef6));
            parameterMap.put("art7_3t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art7_3m7", Integer.toString(agem7));
            parameterMap.put("art7_3f7", Integer.toString(agef7));
            parameterMap.put("art7_3t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art7_3m8", Integer.toString(agem8));
            parameterMap.put("art7_3f8", Integer.toString(agef8));
            parameterMap.put("art7_3t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art7_3m9", Integer.toString(agem9));
            parameterMap.put("art7_3f9", Integer.toString(agef9));
            parameterMap.put("art7_3t9", Integer.toString(agem9 + agef9));

            parameterMap.put("art7_3m10", Integer.toString(agem10));
            parameterMap.put("art7_3f10", Integer.toString(agef10));
            parameterMap.put("art7_3t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art7_3m11", Integer.toString(agem11));
            parameterMap.put("art7_3f11", Integer.toString(agef11));
            parameterMap.put("art7_3t11", Integer.toString(agem11 + agef11));

            parameterMap.put("art7_3m12", Integer.toString(agem12));
            parameterMap.put("art7_3f12", Integer.toString(agef12));
            parameterMap.put("art7_3t12", Integer.toString(agem12 + agef12));


            System.out.println("Computing DMOC2.....");
            //DMOC1 c
            //Previously Undocumented Patient Transfer (Confirmed) this month
            initVariables();

            executeUpdate("DROP INDEX IF EXISTS idx_active");
            executeUpdate("DROP TABLE IF EXISTS active");
            executeUpdate("CREATE TEMPORARY TABLE active AS SELECT DISTINCT patient_id, gender, date_birth AS age FROM patient WHERE facility_id = " + facilityId + " AND current_status IN (" + Constants.ClientStatus.ON_TREATMENT + ") AND DATEDIFF(DAY, date_last_refill + last_refill_duration, CURDATE()) <= " + Constants.LTFU.PEPFAR + " AND date_started IS NOT NULL ORDER BY current_status");
            executeUpdate("CREATE INDEX idx_active ON active(patient_id)");

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM active WHERE patient_id IN (SELECT DISTINCT patient_id FROM devolve WHERE date_devolved <= '" + reportingDateEnd + "')";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("art8m1", Integer.toString(agem1));
            parameterMap.put("art8f1", Integer.toString(agef1));
            parameterMap.put("art8t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art8m2", Integer.toString(agem2));
            parameterMap.put("art8f2", Integer.toString(agef2));
            parameterMap.put("art8t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art8m3", Integer.toString(agem3));
            parameterMap.put("art8f3", Integer.toString(agef3));
            parameterMap.put("art8t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art8m4", Integer.toString(agem4));
            parameterMap.put("art8f4", Integer.toString(agef4));
            parameterMap.put("art8t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art8m5", Integer.toString(agem5));
            parameterMap.put("art8f5", Integer.toString(agef5));
            parameterMap.put("art8t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art8m6", Integer.toString(agem6));
            parameterMap.put("art8f6", Integer.toString(agef6));
            parameterMap.put("art8t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art8m7", Integer.toString(agem7));
            parameterMap.put("art8f7", Integer.toString(agef7));
            parameterMap.put("art8t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art8m8", Integer.toString(agem8));
            parameterMap.put("art8f8", Integer.toString(agef8));
            parameterMap.put("art8t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art8m9", Integer.toString(agem9));
            parameterMap.put("art8f9", Integer.toString(agef9));
            parameterMap.put("art8t9", Integer.toString(agem9 + agef9));

            parameterMap.put("art8m10", Integer.toString(agem10));
            parameterMap.put("art8f10", Integer.toString(agef10));
            parameterMap.put("art8t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art8m11", Integer.toString(agem11));
            parameterMap.put("art8f11", Integer.toString(agef11));
            parameterMap.put("art8t11", Integer.toString(agem11 + agef11));

            parameterMap.put("art8m12", Integer.toString(agem12));
            parameterMap.put("art8f12", Integer.toString(agef12));
            parameterMap.put("art8t12", Integer.toString(agem12 + agef12));


            //this is the child of 8_1 to 8_12
            System.out.println("Computing DMOC2.....");
            //DMOC1 c
            //Previously Undocumented Patient Transfer (Confirmed) this month
            initVariables();

            parameterMap.put("art8_1m1", Integer.toString(agem1));
            parameterMap.put("art8_1f1", Integer.toString(agef1));
            parameterMap.put("art8_1t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art8_1m2", Integer.toString(agem2));
            parameterMap.put("art8_1f2", Integer.toString(agef2));
            parameterMap.put("art8_1t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art8_1m3", Integer.toString(agem3));
            parameterMap.put("art8_1f3", Integer.toString(agef3));
            parameterMap.put("art8_1t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art8_1m4", Integer.toString(agem4));
            parameterMap.put("art8_1f4", Integer.toString(agef4));
            parameterMap.put("art8_1t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art8_1m5", Integer.toString(agem5));
            parameterMap.put("art8_1f5", Integer.toString(agef5));
            parameterMap.put("art8_1t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art8_1m6", Integer.toString(agem6));
            parameterMap.put("art8_1f6", Integer.toString(agef6));
            parameterMap.put("art8_1t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art8_1m7", Integer.toString(agem7));
            parameterMap.put("art8_1f7", Integer.toString(agef7));
            parameterMap.put("art8_1t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art8_1m8", Integer.toString(agem8));
            parameterMap.put("art8_1f8", Integer.toString(agef8));
            parameterMap.put("art8_1t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art8_1m9", Integer.toString(agem9));
            parameterMap.put("art8_1f9", Integer.toString(agef9));
            parameterMap.put("art8_1t9", Integer.toString(agem9 + agef9));

            parameterMap.put("art8_1m10", Integer.toString(agem10));
            parameterMap.put("art8_1f10", Integer.toString(agef10));
            parameterMap.put("art8_1t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art8_1m11", Integer.toString(agem11));
            parameterMap.put("art8_1f11", Integer.toString(agef11));
            parameterMap.put("art8_1t11", Integer.toString(agem11 + agef11));

            parameterMap.put("art8_1m12", Integer.toString(agem12));
            parameterMap.put("art8_1f12", Integer.toString(agef12));
            parameterMap.put("art8_1t12", Integer.toString(agem12 + agef12));


            //this 8_2 to 8_3
            System.out.println("Computing DMOC2.....");
            //DMOC1 c
            //Previously Undocumented Patient Transfer (Confirmed) this month
            initVariables();

            parameterMap.put("art8_2m1", Integer.toString(agem1));
            parameterMap.put("art8_2f1", Integer.toString(agef1));
            parameterMap.put("art8_2t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art8_2m2", Integer.toString(agem2));
            parameterMap.put("art8_2f2", Integer.toString(agef2));
            parameterMap.put("art8_2t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art8_2m3", Integer.toString(agem3));
            parameterMap.put("art8_2f3", Integer.toString(agef3));
            parameterMap.put("art8_2t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art8_2m4", Integer.toString(agem4));
            parameterMap.put("art8_2f4", Integer.toString(agef4));
            parameterMap.put("art8_2t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art8_2m5", Integer.toString(agem5));
            parameterMap.put("art8_2f5", Integer.toString(agef5));
            parameterMap.put("art8_2t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art8_2m6", Integer.toString(agem6));
            parameterMap.put("art8_2f6", Integer.toString(agef6));
            parameterMap.put("art8_2t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art8_2m7", Integer.toString(agem7));
            parameterMap.put("art8_2f7", Integer.toString(agef7));
            parameterMap.put("art8_2t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art8_2m8", Integer.toString(agem8));
            parameterMap.put("art8_2f8", Integer.toString(agef8));
            parameterMap.put("art8_2t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art8_2m9", Integer.toString(agem9));
            parameterMap.put("art8_2f9", Integer.toString(agef9));
            parameterMap.put("art8_2t9", Integer.toString(agem9 + agef9));

            parameterMap.put("art8_2m10", Integer.toString(agem10));
            parameterMap.put("art8_2f10", Integer.toString(agef10));
            parameterMap.put("art8_2t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art8_2m11", Integer.toString(agem11));
            parameterMap.put("art8_2f11", Integer.toString(agef11));
            parameterMap.put("art8_2t11", Integer.toString(agem11 + agef11));

            parameterMap.put("art8_2m12", Integer.toString(agem12));
            parameterMap.put("art8_2f12", Integer.toString(agef12));
            parameterMap.put("art8_2t12", Integer.toString(agem12 + agef12));


            System.out.println("Computing DMOC2.....");
            //DMOC1 c
            //Previously Undocumented Patient Transfer (Confirmed) this month
            initVariables();

            parameterMap.put("art8_3m1", Integer.toString(agem1));
            parameterMap.put("art8_3f1", Integer.toString(agef1));
            parameterMap.put("art8_3t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art8_3m2", Integer.toString(agem2));
            parameterMap.put("art8_3f2", Integer.toString(agef2));
            parameterMap.put("art8_3t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art8_3m3", Integer.toString(agem3));
            parameterMap.put("art8_3f3", Integer.toString(agef3));
            parameterMap.put("art8_3t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art8_3m4", Integer.toString(agem4));
            parameterMap.put("art8_3f4", Integer.toString(agef4));
            parameterMap.put("art8_3t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art8_3m5", Integer.toString(agem5));
            parameterMap.put("art8_3f5", Integer.toString(agef5));
            parameterMap.put("art8_3t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art8_3m6", Integer.toString(agem6));
            parameterMap.put("art8_3f6", Integer.toString(agef6));
            parameterMap.put("art8_3t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art8_3m7", Integer.toString(agem7));
            parameterMap.put("art8_3f7", Integer.toString(agef7));
            parameterMap.put("art8_3t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art8_3m8", Integer.toString(agem8));
            parameterMap.put("art8_3f8", Integer.toString(agef8));
            parameterMap.put("art8_3t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art8_3m9", Integer.toString(agem9));
            parameterMap.put("art8_3f9", Integer.toString(agef9));
            parameterMap.put("art8_3t9", Integer.toString(agem9 + agef9));

            parameterMap.put("art8_3m10", Integer.toString(agem10));
            parameterMap.put("art8_3f10", Integer.toString(agef10));
            parameterMap.put("art8_3t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art8_3m11", Integer.toString(agem11));
            parameterMap.put("art8_3f11", Integer.toString(agef11));
            parameterMap.put("art8_3t11", Integer.toString(agem11 + agef11));

            parameterMap.put("art8_3m12", Integer.toString(agem12));
            parameterMap.put("art8_3f12", Integer.toString(agef12));
            parameterMap.put("art8_3t12", Integer.toString(agem12 + agef12));


            System.out.println("Computing DMOC2.....");
            //DMOC1 c
            //Previously Undocumented Patient Transfer (Confirmed) this month
            initVariables();

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM patient WHERE patient_id IN (SELECT DISTINCT patient_id FROM pharmacy WHERE YEAR(date_visit) = " + reportingYear + " AND MONTH() = " + reportingYear + " AND regimen_id >= 116 AND regimen_id <= 119)";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("art9m1", Integer.toString(agem1));
            parameterMap.put("art9f1", Integer.toString(agef1));
            parameterMap.put("art9t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art9m2", Integer.toString(agem2));
            parameterMap.put("art9f2", Integer.toString(agef2));
            parameterMap.put("art9t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art9m3", Integer.toString(agem3));
            parameterMap.put("art9f3", Integer.toString(agef3));
            parameterMap.put("art9t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art9m4", Integer.toString(agem4));
            parameterMap.put("art9f4", Integer.toString(agef4));
            parameterMap.put("art9t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art9m5", Integer.toString(agem5));
            parameterMap.put("art9f5", Integer.toString(agef5));
            parameterMap.put("art9t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art9m6", Integer.toString(agem6));
            parameterMap.put("art9f6", Integer.toString(agef6));
            parameterMap.put("art9t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art9m7", Integer.toString(agem7));
            parameterMap.put("art9f7", Integer.toString(agef7));
            parameterMap.put("art9t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art9m8", Integer.toString(agem8));
            parameterMap.put("art9f8", Integer.toString(agef8));
            parameterMap.put("art9t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art9m9", Integer.toString(agem9));
            parameterMap.put("art9f9", Integer.toString(agef9));
            parameterMap.put("art9t9", Integer.toString(agem9 + agef9));

            parameterMap.put("art9m10", Integer.toString(agem10));
            parameterMap.put("art9f10", Integer.toString(agef10));
            parameterMap.put("art9t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art9m11", Integer.toString(agem11));
            parameterMap.put("art9f11", Integer.toString(agef11));
            parameterMap.put("art9t11", Integer.toString(agem11 + agef11));

            parameterMap.put("art9m12", Integer.toString(agem12));
            parameterMap.put("art9f12", Integer.toString(agef12));
            parameterMap.put("art9t12", Integer.toString(agem12 + agef12));


            System.out.println("Computing DMOC2.....");
            //DMOC1 c
            //Previously Undocumented Patient Transfer (Confirmed) this month
            initVariables();

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM patient WHERE facility_id = " + facilityId + " AND current_status IN (" + Constants.ClientStatus.ON_TREATMENT + ") AND DATEDIFF(DAY, date_last_refill + last_refill_duration, CURDATE()) <= " + Constants.LTFU.PEPFAR + " AND date_started IS NOT NULL";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                rs = getLastRefillVisit(patientId);
                while (rs != null && rs.next()) {
                    long regimenId = rs.getLong("regimen_id");
                    Date dateLastRefill = rs.getDate("date_visit");
                    int duration = rs.getInt("duration");
                    int monthRefill = duration / 30;
                    if (monthRefill <= 0) {
                        monthRefill = 1;
                    }

                    if (dateLastRefill != null) {
                        //If the last refill date plus refill duration plus 28 days is before the last day of the reporting month this patient is LTFU     if(DateUtil.addYearMonthDay(lastRefill, duration+90, "day(s)").before(reportingDateEnd
                        if (!DateUtil.addYearMonthDay(dateLastRefill, duration + Constants.LTFU.PEPFAR, "DAY").after(DateUtil.getLastDateOfMonth(reportingYear, reportingMonth))) {

                            if (regimenId >= 116 && regimenId <= 119) {
                                disaggregate(gender, age);
                            }
                        }
                    }
                }
            }

            //Populate the report parameter map with values computed for TxMl 1
            parameterMap.put("art10m1", Integer.toString(agem1));
            parameterMap.put("art10f1", Integer.toString(agef1));
            parameterMap.put("art10t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art10m2", Integer.toString(agem2));
            parameterMap.put("art10f2", Integer.toString(agef2));
            parameterMap.put("art10t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art10m3", Integer.toString(agem3));
            parameterMap.put("art10f3", Integer.toString(agef3));
            parameterMap.put("art10t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art10m4", Integer.toString(agem4));
            parameterMap.put("art10f4", Integer.toString(agef4));
            parameterMap.put("art10t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art10m5", Integer.toString(agem5));
            parameterMap.put("art10f5", Integer.toString(agef5));
            parameterMap.put("art10t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art10m6", Integer.toString(agem6));
            parameterMap.put("art10f6", Integer.toString(agef6));
            parameterMap.put("art10t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art10m7", Integer.toString(agem7));
            parameterMap.put("art10f7", Integer.toString(agef7));
            parameterMap.put("art10t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art10m8", Integer.toString(agem8));
            parameterMap.put("art10f8", Integer.toString(agef8));
            parameterMap.put("art10t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art10m9", Integer.toString(agem9));
            parameterMap.put("art10f9", Integer.toString(agef9));
            parameterMap.put("art10t9", Integer.toString(agem9 + agef9));

            parameterMap.put("art1010", Integer.toString(agem10));
            parameterMap.put("art10f10", Integer.toString(agef10));
            parameterMap.put("art10t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art10m11", Integer.toString(agem11));
            parameterMap.put("art10f11", Integer.toString(agef11));
            parameterMap.put("art10t11", Integer.toString(agem11 + agef11));

            parameterMap.put("art10m12", Integer.toString(agem12));
            parameterMap.put("art10f12", Integer.toString(agef12));
            parameterMap.put("art10t12", Integer.toString(agem12 + agef12));


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

    private ResultSet getLastRefillVisit(long patientId) {
        ResultSet rs = null;
        try {
            query = "SELECT DISTINCT regimentype_id, regimen_id, date_visit, duration FROM pharm WHERE patient_id = " + patientId + " ORDER BY date_visit DESC LIMIT 1";
            preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return rs;
    }


}
