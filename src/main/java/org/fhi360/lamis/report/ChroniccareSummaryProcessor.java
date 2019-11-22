/**
 * @author AALOZIE
 */
package org.fhi360.lamis.report;

import org.fhi360.lamis.controller.report.ReportParameterDTO;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Component
public class ChroniccareSummaryProcessor {
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;

    public ChroniccareSummaryProcessor() {

    }

    public synchronized Map<String, Object> getReportParameters(ReportParameterDTO dto, Long facilityId) {
        int reportingMonth;
        int reportingYear;
        String reportingDateBegin;
        String reportingDateEnd;
        int reportingMonthBegin;
        int reportingMonthEnd;
        int reportingYearBegin;
        int reportingYearEnd;
        String reportingDateBeginFy;
        String reportingDateEndFy;
        reportingMonth = DateUtil.getMonth(dto.getReportingMonth());
        reportingYear = Integer.parseInt(dto.getReportingYear());
        reportingDateBegin = DateUtil.parseDateToString(DateUtil.getFirstDateOfMonth(reportingYear, reportingMonth), "yyyy-MM-dd");
        reportingDateEnd = DateUtil.parseDateToString(DateUtil.getLastDateOfMonth(reportingYear, reportingMonth), "yyyy-MM-dd");
        reportingMonthBegin = DateUtil.getMonth(dto.getReportingMonthBegin());
        reportingYearBegin = Integer.parseInt(dto.getReportingYearBegin());
        reportingDateBeginFy = DateUtil.parseDateToString(DateUtil.getFirstDateOfMonth(reportingYearBegin, reportingMonthBegin), "yyyy-MM-dd");

        reportingMonthEnd = DateUtil.getMonth(dto.getReportingMonthEnd());
        reportingYearEnd = Integer.parseInt(dto.getReportingYearEnd());
        reportingDateEndFy = DateUtil.parseDateToString(DateUtil.getLastDateOfMonth(reportingYearEnd, reportingMonthEnd), "yyyy-MM-dd");

        Map<String, Object> parameterMap = new HashMap<>();

        try {
            jdbcUtil = new JDBCUtil();
            //create a temporary table of positives at the end of the review period
            executeUpdate("DROP TABLE IF EXISTS positives");
            String query = "CREATE TEMPORARY TABLE positives AS SELECT DISTINCT patient_id, MAX(date_current_status) FROM statushistory "
                    + "WHERE facility_id = " + facilityId + " AND date_current_status <= '" + reportingDateEnd + "' "
                    + "AND current_status IN ('HIV+ non ART', 'ART Transfer In', 'Pre-ART Transfer In', 'ART Start - external', 'ART Start') "
                    + "GROUP BY patient_id";
            executeUpdate(query);

            /* Page 1 */

            //create a temporary table of min dates of visit from chroniccare 
            executeUpdate("DROP TABLE IF EXISTS mindatevisits");
            query = "CREATE TEMPORARY TABLE mindatevisits AS SELECT DISTINCT patient_id, MIN(date_visit) AS date_visit FROM chroniccare GROUP BY patient_id";
            executeUpdate(query);

            //PLHIV newly in HIV care & receiving clinical care for the first time  
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "JOIN mindatevisits ON chroniccare.patient_id = mindatevisits.patient_id "
                    + "WHERE (patient.date_registration >= '" + reportingDateBeginFy + "' AND patient.date_registration <= '" + reportingDateEndFy + "') AND ("
                    + "mindatevisits.date_visit >= '" + reportingDateBegin + "' AND mindatevisits.date_visit <= '" + reportingDateEnd + "') "
                    + "GROUP BY patient.patient_id";
            parameterMap.put("i1", Integer.toString(executeQuery(query)));

            int i2m1 = 0, i2f1 = 0, i2t1 = 0;
            int i2m2 = 0, i2f2 = 0, i2t2 = 0;
            int i2m3 = 0, i2f3 = 0, i2t3 = 0;
            int i2m4 = 0, i2f4 = 0, i2t4 = 0;
            int i2m5 = 0, i2f5 = 0, i2t5 = 0;
            int i2m6 = 0, i2f6 = 0, i2t6 = 0;
            int i2m7 = 0, i2f7 = 0, i2t7 = 0;
            int i2m8 = 0, i2f8 = 0, i2t8 = 0;
            int i3 = 0;

            query = "SELECT DISTINCT patient.patient_id, patient.gender, chroniccare.tb_values, DATEDIFF(YEAR, patient.date_birth, '" + reportingDateBegin + "') AS age "
                    + "FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE (chroniccare.tb_values IS NOT NULL AND chroniccare.tb_values != '') "
                    + "AND (patient.date_registration >= '" + reportingDateBeginFy + "' AND patient.date_registration <= '" + reportingDateEndFy + "') "
                    + "AND (chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "')";
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                String tbValues = resultSet.getString("tb_values");

                //PLHIV screened for TB
                if (age < 1) { // < 1
                    if (isMale(gender)) {
                        ++i2m1;
                    } else {
                        ++i2f1;
                    }
                }
                if (age >= 1 && age <= 4) { // 1 - 4
                    if (isMale(gender)) {
                        ++i2m2;
                    } else {
                        ++i2f2;
                    }
                }
                if (age >= 5 && age <= 9) { // 5 - 9
                    if (isMale(gender)) {
                        ++i2m3;
                    } else {
                        ++i2f3;
                    }
                }
                if (age >= 10 && age <= 14) { // 10 - 14
                    if (isMale(gender)) {
                        ++i2m4;
                    } else {
                        ++i2f4;
                    }
                }
                if (age >= 15 && age <= 19) { // 15 - 19
                    if (isMale(gender)) {
                        ++i2m5;
                    } else {
                        ++i2f5;
                    }
                }
                if (age >= 20 && age <= 24) { // 20 - 24
                    if (isMale(gender)) {
                        ++i2m6;
                    } else {
                        ++i2f6;
                    }
                }
                if (age >= 25 && age <= 49) { // 25 - 49
                    if (isMale(gender)) {
                        ++i2m7;
                    } else {
                        ++i2f7;
                    }
                }
                if (age >= 50) { // 50+
                    if (isMale(gender)) {
                        ++i2m8;
                    } else {
                        ++i2f8;
                    }
                }

                //PLHIV presumptive TB cases (TB Suspects)
                if (tbValues.contains("Yes")) {
                    ++i3;
                }
            }

            parameterMap.put("i2m1", Integer.toString(i2m1));
            parameterMap.put("i2m2", Integer.toString(i2m2));
            parameterMap.put("i2m3", Integer.toString(i2m3));
            parameterMap.put("i2m4", Integer.toString(i2m4));
            parameterMap.put("i2m5", Integer.toString(i2m5));
            parameterMap.put("i2m6", Integer.toString(i2m6));
            parameterMap.put("i2m7", Integer.toString(i2m7));
            parameterMap.put("i2m8", Integer.toString(i2m8));

            parameterMap.put("i2f1", Integer.toString(i2f1));
            parameterMap.put("i2f2", Integer.toString(i2f2));
            parameterMap.put("i2f3", Integer.toString(i2f3));
            parameterMap.put("i2f4", Integer.toString(i2f4));
            parameterMap.put("i2f5", Integer.toString(i2f5));
            parameterMap.put("i2f6", Integer.toString(i2f6));
            parameterMap.put("i2f7", Integer.toString(i2f7));
            parameterMap.put("i2f8", Integer.toString(i2f8));

            parameterMap.put("i2t1", Integer.toString(i2m1 + i2f1));
            parameterMap.put("i2t2", Integer.toString(i2m2 + i2f2));
            parameterMap.put("i2t3", Integer.toString(i2m3 + i2f3));
            parameterMap.put("i2t4", Integer.toString(i2m4 + i2f4));
            parameterMap.put("i2t5", Integer.toString(i2m5 + i2f5));
            parameterMap.put("i2t6", Integer.toString(i2m6 + i2f6));
            parameterMap.put("i2t7", Integer.toString(i2m7 + i2f7));
            parameterMap.put("i2t8", Integer.toString(i2m8 + i2f8));

            parameterMap.put("i3", Integer.toString(i3));

            //PLHIV TB positive; not on TB drugs
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE (patient.date_registration >= '" + reportingDateBeginFy + "' AND patient.date_registration <= '" + reportingDateEndFy + "') AND ("
                    + "chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND (chroniccare.cotrim_eligibility3 = 1 AND chroniccare.tb_treatment = 'No')";
            parameterMap.put("i4", Integer.toString(executeQuery(query)));

            //PLHIV started on TB treatment
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE (patient.date_registration >= '" + reportingDateBeginFy + "' AND patient.date_registration <= '" + reportingDateEndFy + "') AND ("
                    + "chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND chroniccare.tb_treatment = 'Yes'";
            parameterMap.put("i5", Integer.toString(executeQuery(query)));

            //New PLHIV started on Isonaizid Preventive Therapy (IPT)
            int i6m1 = 0, i6f1 = 0, i6t1 = 0;
            int i6m2 = 0, i6f2 = 0, i6t2 = 0;
            int i6m3 = 0, i6f3 = 0, i6t3 = 0;
            int i6m4 = 0, i6f4 = 0, i6t4 = 0;
            int i6m5 = 0, i6f5 = 0, i6t5 = 0;
            int i6m6 = 0, i6f6 = 0, i6t6 = 0;
            int i6m7 = 0, i6f7 = 0, i6t7 = 0;
            int i6m8 = 0, i6f8 = 0, i6t8 = 0;

            query = "SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" + reportingDateBegin + "') AS age "
                    + "FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE chroniccare.ipt = 'Yes' "
                    + "AND (patient.date_registration >= '" + reportingDateBeginFy + "' AND patient.date_registration <= '" + reportingDateEndFy + "') "
                    + "AND (chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "')";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                if (age < 1) { // < 1
                    if (isMale(gender)) {
                        ++i6m1;
                    } else {
                        ++i6f1;
                    }
                }
                if (age >= 1 && age <= 4) { // 1 - 4
                    if (isMale(gender)) {
                        ++i6m2;
                    } else {
                        ++i6f2;
                    }
                }
                if (age >= 5 && age <= 9) { // 5 - 9
                    if (isMale(gender)) {
                        ++i6m3;
                    } else {
                        ++i6f3;
                    }
                }
                if (age >= 10 && age <= 14) { // 10 - 14
                    if (isMale(gender)) {
                        ++i6m4;
                    } else {
                        ++i6f4;
                    }
                }
                if (age >= 15 && age <= 19) { // 15 - 19
                    if (isMale(gender)) {
                        ++i6m5;
                    } else {
                        ++i6f5;
                    }
                }
                if (age >= 20 && age <= 24) { // 20 - 24
                    if (isMale(gender)) {
                        ++i6m6;
                    } else {
                        ++i6f6;
                    }
                }
                if (age >= 25 && age <= 49) { // 25 - 49
                    if (isMale(gender)) {
                        ++i6m7;
                    } else {
                        ++i6f7;
                    }
                }
                if (age >= 50) { // 50+
                    if (isMale(gender)) {
                        ++i6m8;
                    } else {
                        ++i6f8;
                    }
                }
            }

            parameterMap.put("i6m1", Integer.toString(i6m1));
            parameterMap.put("i6m2", Integer.toString(i6m2));
            parameterMap.put("i6m3", Integer.toString(i6m3));
            parameterMap.put("i6m4", Integer.toString(i6m4));
            parameterMap.put("i6m5", Integer.toString(i6m5));
            parameterMap.put("i6m6", Integer.toString(i6m6));
            parameterMap.put("i6m7", Integer.toString(i6m7));
            parameterMap.put("i6m8", Integer.toString(i6m8));

            parameterMap.put("i6f1", Integer.toString(i6f1));
            parameterMap.put("i6f2", Integer.toString(i6f2));
            parameterMap.put("i6f3", Integer.toString(i6f3));
            parameterMap.put("i6f4", Integer.toString(i6f4));
            parameterMap.put("i6f5", Integer.toString(i6f5));
            parameterMap.put("i6f6", Integer.toString(i6f6));
            parameterMap.put("i6f7", Integer.toString(i6f7));
            parameterMap.put("i6f8", Integer.toString(i6f8));

            parameterMap.put("i6t1", Integer.toString(i6m1 + i6f1));
            parameterMap.put("i6t2", Integer.toString(i6m2 + i6f2));
            parameterMap.put("i6t3", Integer.toString(i6m3 + i6f3));
            parameterMap.put("i6t4", Integer.toString(i6m4 + i6f4));
            parameterMap.put("i6t5", Integer.toString(i6m5 + i6f5));
            parameterMap.put("i6t6", Integer.toString(i6m6 + i6f6));
            parameterMap.put("i6t7", Integer.toString(i6m7 + i6f7));
            parameterMap.put("i6t8", Integer.toString(i6m8 + i6f8));

            //New PLHIV in Clinical Care (CARE_NEW) 
            int i7m1 = 0, i7f1 = 0, i7t1 = 0;
            int i7m2 = 0, i7f2 = 0, i7t2 = 0;
            int i7m3 = 0, i7f3 = 0, i7t3 = 0;
            int i7m4 = 0, i7f4 = 0, i7t4 = 0;
            int i7m5 = 0, i7f5 = 0, i7t5 = 0;
            int i7m6 = 0, i7f6 = 0, i7t6 = 0;
            int i7m7 = 0, i7f7 = 0, i7t7 = 0;
            int i7m8 = 0, i7f8 = 0, i7t8 = 0;

            query = "SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" + reportingDateBegin + "') AS age "
                    + "FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE (patient.date_registration >= '" + reportingDateBeginFy + "' AND patient.date_registration <= '" + reportingDateEndFy + "') "
                    + "AND (chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "')";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                if (age < 1) { // < 1
                    if (isMale(gender)) {
                        ++i7m1;
                    } else {
                        ++i7f1;
                    }
                }
                if (age >= 1 && age <= 4) { // 1 - 4
                    if (isMale(gender)) {
                        ++i7m2;
                    } else {
                        ++i7f2;
                    }
                }
                if (age >= 5 && age <= 9) { // 5 - 9
                    if (isMale(gender)) {
                        ++i7m3;
                    } else {
                        ++i7f3;
                    }
                }
                if (age >= 10 && age <= 14) { // 10 - 14
                    if (isMale(gender)) {
                        ++i7m4;
                    } else {
                        ++i7f4;
                    }
                }
                if (age >= 15 && age <= 19) { // 15 - 19
                    if (isMale(gender)) {
                        ++i7m5;
                    } else {
                        ++i7f5;
                    }
                }
                if (age >= 20 && age <= 24) { // 20 - 24
                    if (isMale(gender)) {
                        ++i7m6;
                    } else {
                        ++i7f6;
                    }
                }
                if (age >= 25 && age <= 49) { // 25 - 49
                    if (isMale(gender)) {
                        ++i7m7;
                    } else {
                        ++i7f7;
                    }
                }
                if (age >= 50) { // 50+
                    if (isMale(gender)) {
                        ++i7m8;
                    } else {
                        ++i7f8;
                    }
                }
            }

            parameterMap.put("i7m1", Integer.toString(i7m1));
            parameterMap.put("i7m2", Integer.toString(i7m2));
            parameterMap.put("i7m3", Integer.toString(i7m3));
            parameterMap.put("i7m4", Integer.toString(i7m4));
            parameterMap.put("i7m5", Integer.toString(i7m5));
            parameterMap.put("i7m6", Integer.toString(i7m6));
            parameterMap.put("i7m7", Integer.toString(i7m7));
            parameterMap.put("i7m8", Integer.toString(i7m8));

            parameterMap.put("i7f1", Integer.toString(i7f1));
            parameterMap.put("i7f2", Integer.toString(i7f2));
            parameterMap.put("i7f3", Integer.toString(i7f3));
            parameterMap.put("i7f4", Integer.toString(i7f4));
            parameterMap.put("i7f5", Integer.toString(i7f5));
            parameterMap.put("i7f6", Integer.toString(i7f6));
            parameterMap.put("i7f7", Integer.toString(i7f7));
            parameterMap.put("i7f8", Integer.toString(i7f8));

            parameterMap.put("i7t1", Integer.toString(i7m1 + i7f1));
            parameterMap.put("i7t2", Integer.toString(i7m2 + i7f2));
            parameterMap.put("i7t3", Integer.toString(i7m3 + i7f3));
            parameterMap.put("i7t4", Integer.toString(i7m4 + i7f4));
            parameterMap.put("i7t5", Integer.toString(i7m5 + i7f5));
            parameterMap.put("i7t6", Integer.toString(i7m6 + i7f6));
            parameterMap.put("i7t7", Integer.toString(i7m7 + i7f7));
            parameterMap.put("i7t8", Integer.toString(i7m8 + i7f8));

            int i8 = 0;
            int i9m1 = 0, i9f1 = 0, i9t1 = 0;
            int i9m2 = 0, i9f2 = 0, i9t2 = 0;
            int i9m3 = 0, i9f3 = 0, i9t3 = 0;
            int i9m4 = 0, i9f4 = 0, i9t4 = 0;
            int i9m5 = 0, i9f5 = 0, i9t5 = 0;

            int i10m1 = 0, i10f1 = 0, i10t1 = 0;
            int i10m2 = 0, i10f2 = 0, i10t2 = 0;
            int i10m3 = 0, i10f3 = 0, i10t3 = 0;
            int i10m4 = 0, i10f4 = 0, i10t4 = 0;
            int i10m5 = 0, i10f5 = 0, i10t5 = 0;

            query = "SELECT DISTINCT patient.patient_id, patient.gender, chroniccare.bmi, chroniccare.muac_pediatrics, chroniccare.supplementary_food, DATEDIFF(YEAR, patient.date_birth, '" + reportingDateBegin + "') AS age "
                    + "FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE (patient.date_registration >= '" + reportingDateBeginFy + "' AND patient.date_registration <= '" + reportingDateEndFy + "') AND ("
                    + "chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND ((chroniccare.bmi IS NOT NULL AND chroniccare.bmi != '') OR (chroniccare.muac_pediatrics IS NOT NULL AND chroniccare.muac_pediatrics != ''))";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                String bmi = resultSet.getString("bmi");
                String muacPediatrics = resultSet.getString("muac_pediatrics");
                String supplementaryFood = resultSet.getString("supplementary_food");

                //PLHIV in care and treatment who received nutritional assessment
                ++i8;

                if (age < 1) { // < 1
                    if (muacPediatrics.equals("<11.5cm (Severe Acute Malnutrition)") || muacPediatrics.equals("11.5-12.5cm (Moderate Acute Malnutrition)")) {
                        if (isMale(gender)) {
                            ++i9m1;
                        } else {
                            ++i9f1;  //Clinically undernourished PLHIV 
                        }
                        if (supplementaryFood.equals("Yes")) {
                            if (isMale(gender)) {
                                ++i10m1;
                            } else {
                                ++i10f1; //Clinically undernourished PLHIV who received therapeutic or supplementary food 
                            }
                        }
                    }
                }

                if (age >= 1 && age <= 4) { // 1 - 4
                    if (muacPediatrics.equals("<11.5cm (Severe Acute Malnutrition)") || muacPediatrics.equals("11.5-12.5cm (Moderate Acute Malnutrition)")) {
                        if (isMale(gender)) {
                            ++i9m2;
                        } else {
                            ++i9f2;  //Clinically undernourished PLHIV 
                        }
                        if (supplementaryFood.equals("Yes")) {
                            if (isMale(gender)) {
                                ++i10m2;
                            } else {
                                ++i10f2; //Clinically undernourished PLHIV who received therapeutic or supplementary food 
                            }
                        }
                    }
                }

                if (age >= 5 && age <= 14) { // 5 - 14
                    if (bmi.equals("<18.5 (Underweight)")) {
                        if (isMale(gender)) {
                            ++i9m3;
                        } else {
                            ++i9f3;  //Clinically undernourished PLHIV 
                        }
                        if (supplementaryFood.equals("Yes")) {
                            if (isMale(gender)) {
                                ++i10m3;
                            } else {
                                ++i10f3; //Clinically undernourished PLHIV who received therapeutic or supplementary food 
                            }
                        }
                    }
                }

                if (age >= 15 && age <= 17) { // 15 - 17
                    if (bmi.equals("<18.5 (Underweight)")) {
                        if (isMale(gender)) {
                            ++i9m4;
                        } else {
                            ++i9f4;  //Clinically undernourished PLHIV 
                        }
                        if (supplementaryFood.equals("Yes")) {
                            if (isMale(gender)) {
                                ++i10m4;
                            } else {
                                ++i10f4; //Clinically undernourished PLHIV who received therapeutic or supplementary food 
                            }
                        }
                    }
                }

                if (age >= 18) { // 18+
                    if (bmi.equals("<18.5 (Underweight)")) {
                        if (isMale(gender)) {
                            ++i9m5;
                        } else {
                            ++i9f5;  //Clinically undernourished PLHIV 
                        }
                        if (supplementaryFood.equals("Yes")) {
                            if (isMale(gender)) {
                                ++i10m5;
                            } else {
                                ++i10f5; //Clinically undernourished PLHIV who received therapeutic or supplementary food 
                            }
                        }
                    }
                }
            }

            parameterMap.put("i8", Integer.toString(i8));

            parameterMap.put("i9m1", Integer.toString(i9m1));
            parameterMap.put("i9m2", Integer.toString(i9m2));
            parameterMap.put("i9m3", Integer.toString(i9m3));
            parameterMap.put("i9m4", Integer.toString(i9m4));
            parameterMap.put("i9m5", Integer.toString(i9m5));

            parameterMap.put("i9f1", Integer.toString(i9f1));
            parameterMap.put("i9f2", Integer.toString(i9f2));
            parameterMap.put("i9f3", Integer.toString(i9f3));
            parameterMap.put("i9f4", Integer.toString(i9f4));
            parameterMap.put("i9f5", Integer.toString(i9f5));

            parameterMap.put("i9t1", Integer.toString(i9m1 + i9f1));
            parameterMap.put("i9t2", Integer.toString(i9m2 + i9f2));
            parameterMap.put("i9t3", Integer.toString(i9m3 + i9f3));
            parameterMap.put("i9t4", Integer.toString(i9m4 + i9f4));
            parameterMap.put("i9t5", Integer.toString(i9m5 + i9f5));

            parameterMap.put("i10m1", Integer.toString(i10m1));
            parameterMap.put("i10m2", Integer.toString(i10m2));
            parameterMap.put("i10m3", Integer.toString(i10m3));
            parameterMap.put("i10m4", Integer.toString(i10m4));
            parameterMap.put("i10m5", Integer.toString(i10m5));

            parameterMap.put("i10f1", Integer.toString(i10f1));
            parameterMap.put("i10f2", Integer.toString(i10f2));
            parameterMap.put("i10f3", Integer.toString(i10f3));
            parameterMap.put("i10f4", Integer.toString(i10f4));
            parameterMap.put("i10f5", Integer.toString(i10f5));

            parameterMap.put("i10t1", Integer.toString(i10m1 + i10f1));
            parameterMap.put("i10t2", Integer.toString(i10m2 + i10f2));
            parameterMap.put("i10t3", Integer.toString(i10m3 + i10f3));
            parameterMap.put("i10t4", Integer.toString(i10m4 + i10f4));
            parameterMap.put("i10t5", Integer.toString(i10m5 + i10f5));

            //PLHIV counselled on gender norms & GBV
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE (patient.date_registration >= '" + reportingDateBeginFy + "' AND patient.date_registration <= '" + reportingDateEndFy + "') AND ("
                    + "chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND ((chroniccare.gbv1 = 'Yes' AND chroniccare.gbv1_referred = 'Yes') OR  (chroniccare.gbv2 = 'Yes' AND chroniccare.gbv2_referred = 'Yes'))";
            parameterMap.put("i11", Integer.toString(executeQuery(query)));

            //PLHIV who were screened for GBV 
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE (patient.date_registration >= '" + reportingDateBeginFy + "' AND patient.date_registration <= '" + reportingDateEndFy + "') AND ("
                    + "chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND ((chroniccare.gbv1 IS NOT NULL AND chroniccare.gbv1 != '') AND (chroniccare.gbv2 IS NOT NULL AND chroniccare.gbv2 != ''))";
            parameterMap.put("i12", Integer.toString(executeQuery(query)));

            //GBV cases referred for post-GBV Care
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE (patient.date_registration >= '" + reportingDateBeginFy + "' AND patient.date_registration <= '" + reportingDateEndFy + "') AND ("
                    + "chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND ((chroniccare.gbv1 = 'Yes' AND chroniccare.gbv1_referred = 'Yes') OR  (chroniccare.gbv2 = 'Yes' AND chroniccare.gbv2_referred = 'Yes'))";
            parameterMap.put("i13", Integer.toString(executeQuery(query)));

            //PLHIV screened for chronic illnesses
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE (patient.date_registration >= '" + reportingDateBeginFy + "' AND patient.date_registration <= '" + reportingDateEndFy + "') AND ("
                    + "chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND ((chroniccare.bp_above IS NOT NULL AND chroniccare.bp_above != '') AND (chroniccare.dm_values IS NOT NULL AND chroniccare.dm_values != ''))";
            parameterMap.put("i14", Integer.toString(executeQuery(query)));

            //PLHIV suspected to have HTN
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE (patient.date_registration >= '" + reportingDateBeginFy + "' AND patient.date_registration <= '" + reportingDateEndFy + "') AND ("
                    + "chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND (chroniccare.hypertensive = 'No' AND chroniccare.bp_above = 'Yes')";
            parameterMap.put("i15", Integer.toString(executeQuery(query)));

            //PLHIV suspected to have DM
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE (patient.date_registration >= '" + reportingDateBeginFy + "' AND patient.date_registration <= '" + reportingDateEndFy + "') AND ("
                    + "chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND (chroniccare.diabetic = 'No' AND (chroniccare.dm_values LIKE '%Yes' OR chroniccare.dm_values LIKE 'Yes%' OR chroniccare.dm_values LIKE '%Yes%'))";
            parameterMap.put("i16", Integer.toString(executeQuery(query)));

            //PLHIV co-morbid with HTN (newly identified) 
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE (patient.date_registration >= '" + reportingDateBeginFy + "' AND patient.date_registration <= '" + reportingDateEndFy + "') AND ("
                    + "chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND (chroniccare.hypertensive = 'Yes' AND chroniccare.first_hypertensive = 'Yes')";
            parameterMap.put("i17", Integer.toString(executeQuery(query)));

            //PLHIV co-morbid with DM (newly identified) 
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE (patient.date_registration >= '" + reportingDateBeginFy + "' AND patient.date_registration <= '" + reportingDateEndFy + "') AND ("
                    + "chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND (chroniccare.diabetic = 'Yes' AND chroniccare.first_diabetic = 'Yes')";
            parameterMap.put("i18", Integer.toString(executeQuery(query)));

            //PLHIV who received PHDP service 
            int i19m1 = 0, i19f1 = 0, i19t1 = 0;
            int i19m2 = 0, i19f2 = 0, i19t2 = 0;

            query = "SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" + reportingDateBegin + "') AS age "
                    + "FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE (patient.date_registration >= '" + reportingDateBeginFy + "' AND patient.date_registration <= '" + reportingDateEndFy + "') AND ("
                    + "chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND (chroniccare.phdp1_services_provided = 'Yes' OR chroniccare.phdp4_services_provided = 'Yes' "
                    + "OR chroniccare.phdp7_services_provided = 'Yes' OR chroniccare.phdp8_services_provided = 'Yes' "
                    + "OR (chroniccare.phdp9_services_provided IS NOT NULL AND chroniccare.phdp9_services_provided != ''))";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                if (age < 15) { // < 15
                    if (isMale(gender)) {
                        ++i19m1;
                    } else {
                        ++i19f1;
                    }
                }
                if (age >= 15) { // 15+
                    if (isMale(gender)) {
                        ++i19m2;
                    } else {
                        ++i19f2;
                    }
                }
            }

            parameterMap.put("i19m1", Integer.toString(i19m1));
            parameterMap.put("i19m2", Integer.toString(i19m2));

            parameterMap.put("i19f1", Integer.toString(i19f1));
            parameterMap.put("i19f2", Integer.toString(i19f2));

            parameterMap.put("i19t1", Integer.toString(i19m1 + i19f1));
            parameterMap.put("i19t2", Integer.toString(i19m2 + i19f2));

            /* Page 2 */

            //create a temporary table of care visits in the selected FY
            executeUpdate("DROP TABLE IF EXISTS carevisits");
            query = "CREATE TEMPORARY TABLE carevisits AS SELECT patient_id, date_visit FROM chroniccare WHERE facility_id = " + facilityId
                    + "AND date_visit >= '" + reportingDateBeginFy + "' AND date_visit <= '" + reportingDateEndFy + "'";
            executeUpdate(query);

            //create a temporary table of min dates of visit from carevisits 
            executeUpdate("DROP TABLE IF EXISTS mindatevisitsold");
            query = "CREATE TEMPORARY TABLE mindatevisitsold AS SELECT DISTINCT patient_id, MIN(date_visit) AS date_visit FROM carevisits GROUP BY patient_id";
            executeUpdate(query);

            //Registered PLHIV on New/first visit this FY (OLD_first time this FY)
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "mindatevisitsold.date_visit >= '" + reportingDateBegin + "' AND mindatevisitsold.date_visit <= '" + reportingDateEnd + "')";
            parameterMap.put("i20", Integer.toString(executeQuery(query)));

            //Registered PLHIV on Follow up/subsequent visit this FY (OLD_on Follow up this FY)
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN carevisits ON chroniccare.patient_id = carevisits.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "carevisits.date_visit >= '" + reportingDateBegin + "' AND carevisits.date_visit <= '" + reportingDateEnd + "') "
                    + "AND mindatevisitsold.date_visit < '" + reportingDateBegin + "' "
                    + "AND patient.patient_id IN (SELECT DISTINCT patient_id FROM positives)";
            parameterMap.put("i21", Integer.toString(executeQuery(query)));

            //Registered PLHIV on New/first visit this FY Eligible for CD4 Testing 
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "mindatevisitsold.date_visit >= '" + reportingDateBegin + "' AND mindatevisitsold.date_visit <= '" + reportingDateEnd + "') "
                    + "AND eligible_cd4 = 'Yes'";
            parameterMap.put("i22", Integer.toString(executeQuery(query)));

            //Registered PLHIV on New/first visit this FY Eligible for Viral Load Testing 
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "mindatevisitsold.date_visit >= '" + reportingDateBegin + "' AND mindatevisitsold.date_visit <= '" + reportingDateEnd + "') "
                    + "AND eligible_viral_load = 'Yes'";
            parameterMap.put("i23", Integer.toString(executeQuery(query)));

            //PLHIV Screened for TB
            int i24m1 = 0, i24f1 = 0, i24t1 = 0;
            int i24m2 = 0, i24f2 = 0, i24t2 = 0;
            int i24m3 = 0, i24f3 = 0, i24t3 = 0;
            int i24m4 = 0, i24f4 = 0, i24t4 = 0;
            int i24m5 = 0, i24f5 = 0, i24t5 = 0;
            int i24m6 = 0, i24f6 = 0, i24t6 = 0;
            int i24m7 = 0, i24f7 = 0, i24t7 = 0;
            int i24m8 = 0, i24f8 = 0, i24t8 = 0;

            query = "SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" + reportingDateBegin + "') AS age "
                    + "FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE (chroniccare.tb_values IS NOT NULL AND chroniccare.tb_values != '') "
                    + "AND patient.date_registration < '" + reportingDateBeginFy + "' "
                    + "AND (chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "')";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                if (age < 1) { // < 1
                    if (isMale(gender)) {
                        ++i24m1;
                    } else {
                        ++i24f1;
                    }
                }
                if (age >= 1 && age <= 4) { // 1 - 4
                    if (isMale(gender)) {
                        ++i24m2;
                    } else {
                        ++i24f2;
                    }
                }
                if (age >= 5 && age <= 9) { // 5 - 9
                    if (isMale(gender)) {
                        ++i24m3;
                    } else {
                        ++i24f3;
                    }
                }
                if (age >= 10 && age <= 14) { // 10 - 14
                    if (isMale(gender)) {
                        ++i24m4;
                    } else {
                        ++i24f4;
                    }
                }
                if (age >= 15 && age <= 19) { // 15 - 19
                    if (isMale(gender)) {
                        ++i24m5;
                    } else {
                        ++i24f5;
                    }
                }
                if (age >= 20 && age <= 24) { // 20 - 24
                    if (isMale(gender)) {
                        ++i24m6;
                    } else {
                        ++i24f6;
                    }
                }
                if (age >= 25 && age <= 49) { // 25 - 49
                    if (isMale(gender)) {
                        ++i24m7;
                    } else {
                        ++i24f7;
                    }
                }
                if (age >= 50) { // 50+
                    if (isMale(gender)) {
                        ++i24m8;
                    } else {
                        ++i24f8;
                    }
                }
            }

            parameterMap.put("i24m1", Integer.toString(i24m1));
            parameterMap.put("i24m2", Integer.toString(i24m2));
            parameterMap.put("i24m3", Integer.toString(i24m3));
            parameterMap.put("i24m4", Integer.toString(i24m4));
            parameterMap.put("i24m5", Integer.toString(i24m5));
            parameterMap.put("i24m6", Integer.toString(i24m6));
            parameterMap.put("i24m7", Integer.toString(i24m7));
            parameterMap.put("i24m8", Integer.toString(i24m8));

            parameterMap.put("i24f1", Integer.toString(i24f1));
            parameterMap.put("i24f2", Integer.toString(i24f2));
            parameterMap.put("i24f3", Integer.toString(i24f3));
            parameterMap.put("i24f4", Integer.toString(i24f4));
            parameterMap.put("i24f5", Integer.toString(i24f5));
            parameterMap.put("i24f6", Integer.toString(i24f6));
            parameterMap.put("i24f7", Integer.toString(i24f7));
            parameterMap.put("i24f8", Integer.toString(i24f8));

            parameterMap.put("i24t1", Integer.toString(i24m1 + i24f1));
            parameterMap.put("i24t2", Integer.toString(i24m2 + i24f2));
            parameterMap.put("i24t3", Integer.toString(i24m3 + i24f3));
            parameterMap.put("i24t4", Integer.toString(i24m4 + i24f4));
            parameterMap.put("i24t5", Integer.toString(i24m5 + i24f5));
            parameterMap.put("i24t6", Integer.toString(i24m6 + i24f6));
            parameterMap.put("i24t7", Integer.toString(i24m7 + i24f7));
            parameterMap.put("i24t8", Integer.toString(i24m8 + i24f8));

            //PLHIV presumptive TB cases (TB suspects)  (OLD_first time this FY)
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "mindatevisitsold.date_visit >= '" + reportingDateBegin + "' AND mindatevisitsold.date_visit <= '" + reportingDateEnd + "') "
                    + "AND (chroniccare.tb_values LIKE 'Yes%' OR chroniccare.tb_values LIKE '%Yes' OR chroniccare.tb_values LIKE '%Yes%')";
            parameterMap.put("i25", Integer.toString(executeQuery(query)));

            //PLHIV TB Positive; not on TB drugs (OLD_first time this FY)		
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "mindatevisitsold.date_visit >= '" + reportingDateBegin + "' AND mindatevisitsold.date_visit <= '" + reportingDateEnd + "') "
                    + "AND (chroniccare.cotrim_eligibility3 = 1 AND chroniccare.tb_treatment = 'No')";
            parameterMap.put("i26", Integer.toString(executeQuery(query)));

            //PLHIV started on TB treatment (OLD_first time this FY)
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "mindatevisitsold.date_visit >= '" + reportingDateBegin + "' AND mindatevisitsold.date_visit <= '" + reportingDateEnd + "') "
                    + "AND chroniccare.tb_treatment = 'Yes'";
            parameterMap.put("i27", Integer.toString(executeQuery(query)));

            //PLHIV presumptive TB cases (TB Suspects) (OLD_on Follow up this FY)
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN carevisits ON chroniccare.patient_id = carevisits.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "carevisits.date_visit >= '" + reportingDateBegin + "' AND carevisits.date_visit <= '" + reportingDateEnd + "') "
                    + "AND mindatevisitsold.date_visit < '" + reportingDateBegin + "' "
                    + "AND (chroniccare.tb_values LIKE 'Yes%' OR chroniccare.tb_values LIKE '%Yes' OR chroniccare.tb_values LIKE '%Yes%') "
                    + "AND patient.patient_id IN (SELECT DISTINCT patient_id FROM positives)";
            parameterMap.put("i28", Integer.toString(executeQuery(query)));

            //PLHIV TB Positive; not on TB drugs (OLD_on Follow up this FY)
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN carevisits ON chroniccare.patient_id = carevisits.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "carevisits.date_visit >= '" + reportingDateBegin + "' AND carevisits.date_visit <= '" + reportingDateEnd + "') "
                    + "AND mindatevisitsold.date_visit < '" + reportingDateBegin + "' "
                    + "AND (chroniccare.cotrim_eligibility3 = 1 AND chroniccare.tb_treatment = 'No') "
                    + "AND patient.patient_id IN (SELECT DISTINCT patient_id FROM positives)";
            parameterMap.put("i29", Integer.toString(executeQuery(query)));

            //PLHIV started on TB treatment  (OLD_on Follow up this FY)
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN carevisits ON chroniccare.patient_id = carevisits.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "carevisits.date_visit >= '" + reportingDateBegin + "' AND carevisits.date_visit <= '" + reportingDateEnd + "') "
                    + "AND mindatevisitsold.date_visit < '" + reportingDateBegin + "' "
                    + "AND chroniccare.tb_treatment = 'Yes' "
                    + "AND patient.patient_id IN (SELECT DISTINCT patient_id FROM positives)";
            parameterMap.put("i30", Integer.toString(executeQuery(query)));

            //Old PLHIV (Non-ART) in clinical care
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' "
                    + "AND (chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND chroniccare.current_status = 'HIV+ non ART'";
            parameterMap.put("i31", Integer.toString(executeQuery(query)));

            //Old PLHIV (ART) in clinical care  
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' "
                    + "AND (chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND chroniccare.current_status = 'ART Start'";
            parameterMap.put("i32", Integer.toString(executeQuery(query)));

            //Old PLHIV in Clinical Care (CARE_CURRENT) 
            int i33m1 = 0, i33f1 = 0, i33t1 = 0;
            int i33m2 = 0, i33f2 = 0, i33t2 = 0;
            int i33m3 = 0, i33f3 = 0, i33t3 = 0;
            int i33m4 = 0, i33f4 = 0, i33t4 = 0;
            int i33m5 = 0, i33f5 = 0, i33t5 = 0;
            int i33m6 = 0, i33f6 = 0, i33t6 = 0;
            int i33m7 = 0, i33f7 = 0, i33t7 = 0;
            int i33m8 = 0, i33f8 = 0, i33t8 = 0;

            query = "SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" + reportingDateBegin + "') AS age "
                    + "FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' "
                    + "AND (chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "')";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                if (age < 1) { // < 1
                    if (isMale(gender)) {
                        ++i33m1;
                    } else {
                        ++i33f1;
                    }
                }
                if (age >= 1 && age <= 4) { // 1 - 4
                    if (isMale(gender)) {
                        ++i33m2;
                    } else {
                        ++i33f2;
                    }
                }
                if (age >= 5 && age <= 9) { // 5 - 9
                    if (isMale(gender)) {
                        ++i33m3;
                    } else {
                        ++i33f3;
                    }
                }
                if (age >= 10 && age <= 14) { // 10 - 14
                    if (isMale(gender)) {
                        ++i33m4;
                    } else {
                        ++i33f4;
                    }
                }
                if (age >= 15 && age <= 19) { // 15 - 19
                    if (isMale(gender)) {
                        ++i33m5;
                    } else {
                        ++i33f5;
                    }
                }
                if (age >= 20 && age <= 24) { // 20 - 24
                    if (isMale(gender)) {
                        ++i33m6;
                    } else {
                        ++i33f6;
                    }
                }
                if (age >= 25 && age <= 49) { // 25 - 49
                    if (isMale(gender)) {
                        ++i33m7;
                    } else {
                        ++i33f7;
                    }
                }
                if (age >= 50) { // 50+
                    if (isMale(gender)) {
                        ++i33m8;
                    } else {
                        ++i33f8;
                    }
                }
            }

            parameterMap.put("i33m1", Integer.toString(i33m1));
            parameterMap.put("i33m2", Integer.toString(i33m2));
            parameterMap.put("i33m3", Integer.toString(i33m3));
            parameterMap.put("i33m4", Integer.toString(i33m4));
            parameterMap.put("i33m5", Integer.toString(i33m5));
            parameterMap.put("i33m6", Integer.toString(i33m6));
            parameterMap.put("i33m7", Integer.toString(i33m7));
            parameterMap.put("i33m8", Integer.toString(i33m8));

            parameterMap.put("i33f1", Integer.toString(i33f1));
            parameterMap.put("i33f2", Integer.toString(i33f2));
            parameterMap.put("i33f3", Integer.toString(i33f3));
            parameterMap.put("i33f4", Integer.toString(i33f4));
            parameterMap.put("i33f5", Integer.toString(i33f5));
            parameterMap.put("i33f6", Integer.toString(i33f6));
            parameterMap.put("i33f7", Integer.toString(i33f7));
            parameterMap.put("i33f8", Integer.toString(i33f8));

            parameterMap.put("i33t1", Integer.toString(i33m1 + i33f1));
            parameterMap.put("i33t2", Integer.toString(i33m2 + i33f2));
            parameterMap.put("i33t3", Integer.toString(i33m3 + i33f3));
            parameterMap.put("i33t4", Integer.toString(i33m4 + i33f4));
            parameterMap.put("i33t5", Integer.toString(i33m5 + i33f5));
            parameterMap.put("i33t6", Integer.toString(i33m6 + i33f6));
            parameterMap.put("i33t7", Integer.toString(i33m7 + i33f7));
            parameterMap.put("i33t8", Integer.toString(i33m8 + i33f8));

            //PLHIV in care and treatment who received nutritional assessment
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' "
                    + "AND (chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND ((chroniccare.bmi IS NOT NULL AND chroniccare.bmi != '') OR (chroniccare.muac_pediatrics IS NOT NULL AND chroniccare.muac_pediatrics != ''))";
            parameterMap.put("i34", Integer.toString(executeQuery(query)));

            int i35m1 = 0, i35f1 = 0, i35t1 = 0;
            int i35m2 = 0, i35f2 = 0, i35t2 = 0;
            int i35m3 = 0, i35f3 = 0, i35t3 = 0;
            int i35m4 = 0, i35f4 = 0, i35t4 = 0;
            int i35m5 = 0, i35f5 = 0, i35t5 = 0;

            int i36m1 = 0, i36f1 = 0, i36t1 = 0;
            int i36m2 = 0, i36f2 = 0, i36t2 = 0;
            int i36m3 = 0, i36f3 = 0, i36t3 = 0;
            int i36m4 = 0, i36f4 = 0, i36t4 = 0;
            int i36m5 = 0, i36f5 = 0, i36t5 = 0;

            query = "SELECT DISTINCT patient.patient_id, patient.gender, chroniccare.bmi, chroniccare.muac_pediatrics, chroniccare.supplementary_food, DATEDIFF(YEAR, patient.date_birth, '" + reportingDateBegin + "') AS age "
                    + "FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "mindatevisitsold.date_visit >= '" + reportingDateBegin + "' AND mindatevisitsold.date_visit <= '" + reportingDateEnd + "') "
                    + "AND ((chroniccare.bmi IS NOT NULL AND chroniccare.bmi != '') OR (chroniccare.muac_pediatrics IS NOT NULL AND chroniccare.muac_pediatrics != ''))";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                String bmi = resultSet.getString("bmi");
                String muacPediatrics = resultSet.getString("muac_pediatrics");
                String supplementaryFood = resultSet.getString("supplementary_food");

                if (age < 1) { // < 1
                    if (muacPediatrics.equals("<11.5cm (Severe Acute Malnutrition)") || muacPediatrics.equals("11.5-12.5cm (Moderate Acute Malnutrition)")) {
                        if (isMale(gender)) {
                            ++i35m1;
                        } else {
                            ++i35f1;  //Clinically undernourished PLHIV (OLD_first time this FY) 
                        }
                        if (supplementaryFood.equals("Yes")) {
                            if (isMale(gender)) {
                                ++i36m1;
                            } else {
                                ++i36f1; //Clinically undernourished PLHIV who received therapeutic or supplementary food (OLD_first time this FY) 
                            }
                        }
                    }
                }

                if (age >= 1 && age <= 4) { // 1 - 4
                    if (muacPediatrics.equals("<11.5cm (Severe Acute Malnutrition)") || muacPediatrics.equals("11.5-12.5cm (Moderate Acute Malnutrition)")) {
                        if (isMale(gender)) {
                            ++i35m2;
                        } else {
                            ++i35f2;  //Clinically undernourished PLHIV (OLD_first time this FY) 
                        }
                        if (supplementaryFood.equals("Yes")) {
                            if (isMale(gender)) {
                                ++i36m2;
                            } else {
                                ++i36f2; //Clinically undernourished PLHIV who received therapeutic or supplementary food (OLD_first time this FY) 
                            }
                        }
                    }
                }

                if (age >= 5 && age <= 14) { // 5 - 14
                    if (bmi.equals("<18.5 (Underweight)")) {
                        if (isMale(gender)) {
                            ++i35m3;
                        } else {
                            ++i35f3;  //Clinically undernourished PLHIV (OLD_first time this FY) 
                        }
                        if (supplementaryFood.equals("Yes")) {
                            if (isMale(gender)) {
                                ++i36m3;
                            } else {
                                ++i36f3; //Clinically undernourished PLHIV who received therapeutic or supplementary food (OLD_first time this FY) 
                            }
                        }
                    }
                }

                if (age >= 15 && age <= 17) { // 15 - 17
                    if (bmi.equals("<18.5 (Underweight)")) {
                        if (isMale(gender)) {
                            ++i35m4;
                        } else {
                            ++i35f4;  //Clinically undernourished PLHIV (OLD_first time this FY) 
                        }
                        if (supplementaryFood.equals("Yes")) {
                            if (isMale(gender)) {
                                ++i36m4;
                            } else {
                                ++i36f4; //Clinically undernourished PLHIV who received therapeutic or supplementary food (OLD_first time this FY) 
                            }
                        }
                    }
                }

                if (age >= 18) { // 18+
                    if (bmi.equals("<18.5 (Underweight)")) {
                        if (isMale(gender)) {
                            ++i35m5;
                        } else {
                            ++i35f5;  //Clinically undernourished PLHIV (OLD_first time this FY)
                        }
                        if (supplementaryFood.equals("Yes")) {
                            if (isMale(gender)) {
                                ++i36m5;
                            } else {
                                ++i36f5; //Clinically undernourished PLHIV who received therapeutic or supplementary food (OLD_first time this FY) 
                            }
                        }
                    }
                }
            }

            parameterMap.put("i35m1", Integer.toString(i35m1));
            parameterMap.put("i35m2", Integer.toString(i35m2));
            parameterMap.put("i35m3", Integer.toString(i35m3));
            parameterMap.put("i35m4", Integer.toString(i35m4));
            parameterMap.put("i35m5", Integer.toString(i35m5));

            parameterMap.put("i35f1", Integer.toString(i35f1));
            parameterMap.put("i35f2", Integer.toString(i35f2));
            parameterMap.put("i35f3", Integer.toString(i35f3));
            parameterMap.put("i35f4", Integer.toString(i35f4));
            parameterMap.put("i35f5", Integer.toString(i35f5));

            parameterMap.put("i35t1", Integer.toString(i35m1 + i35f1));
            parameterMap.put("i35t2", Integer.toString(i35m2 + i35f2));
            parameterMap.put("i35t3", Integer.toString(i35m3 + i35f3));
            parameterMap.put("i35t4", Integer.toString(i35m4 + i35f4));
            parameterMap.put("i35t5", Integer.toString(i35m5 + i35f5));

            parameterMap.put("i36m1", Integer.toString(i36m1));
            parameterMap.put("i36m2", Integer.toString(i36m2));
            parameterMap.put("i36m3", Integer.toString(i36m3));
            parameterMap.put("i36m4", Integer.toString(i36m4));
            parameterMap.put("i36m5", Integer.toString(i36m5));

            parameterMap.put("i36f1", Integer.toString(i36f1));
            parameterMap.put("i36f2", Integer.toString(i36f2));
            parameterMap.put("i36f3", Integer.toString(i36f3));
            parameterMap.put("i36f4", Integer.toString(i36f4));
            parameterMap.put("i36f5", Integer.toString(i36f5));

            parameterMap.put("i36t1", Integer.toString(i36m1 + i36f1));
            parameterMap.put("i36t2", Integer.toString(i36m2 + i36f2));
            parameterMap.put("i36t3", Integer.toString(i36m3 + i36f3));
            parameterMap.put("i36t4", Integer.toString(i36m4 + i36f4));
            parameterMap.put("i36t5", Integer.toString(i36m5 + i36f5));

            int i37m1 = 0, i37f1 = 0, i37t1 = 0;
            int i37m2 = 0, i37f2 = 0, i37t2 = 0;
            int i37m3 = 0, i37f3 = 0, i37t3 = 0;
            int i37m4 = 0, i37f4 = 0, i37t4 = 0;
            int i37m5 = 0, i37f5 = 0, i37t5 = 0;

            int i38m1 = 0, i38f1 = 0, i38t1 = 0;
            int i38m2 = 0, i38f2 = 0, i38t2 = 0;
            int i38m3 = 0, i38f3 = 0, i38t3 = 0;
            int i38m4 = 0, i38f4 = 0, i38t4 = 0;
            int i38m5 = 0, i38f5 = 0, i38t5 = 0;

            query = "SELECT DISTINCT patient.patient_id, patient.gender, chroniccare.bmi, chroniccare.muac_pediatrics, chroniccare.supplementary_food, DATEDIFF(YEAR, patient.date_birth, '" + reportingDateBegin + "') AS age "
                    + "FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN carevisits ON chroniccare.patient_id = carevisits.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "carevisits.date_visit >= '" + reportingDateBegin + "' AND carevisits.date_visit <= '" + reportingDateEnd + "') "
                    + "AND mindatevisitsold.date_visit < '" + reportingDateBegin + "' "
                    + "AND ((chroniccare.bmi IS NOT NULL AND chroniccare.bmi != '') OR (chroniccare.muac_pediatrics IS NOT NULL AND chroniccare.muac_pediatrics != '')) "
                    + "AND patient.patient_id IN (SELECT DISTINCT patient_id FROM positives)";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                String bmi = resultSet.getString("bmi");
                String muacPediatrics = resultSet.getString("muac_pediatrics");
                String supplementaryFood = resultSet.getString("supplementary_food");

                if (age < 1) { // < 1
                    if (muacPediatrics.equals("<11.5cm (Severe Acute Malnutrition)") || muacPediatrics.equals("11.5-12.5cm (Moderate Acute Malnutrition)")) {
                        if (isMale(gender)) {
                            ++i37m1;
                        } else {
                            ++i37f1;  //Clinically undernourished PLHIV (OLD_on Follow up this FY) 
                        }
                        if (supplementaryFood.equals("Yes")) {
                            if (isMale(gender)) {
                                ++i38m1;
                            } else {
                                ++i38f1; //Clinically undernourished PLHIV who received therapeutic or supplementary food (OLD_on Follow up this FY)
                            }
                        }
                    }
                }

                if (age >= 1 && age <= 4) { // 1 - 4
                    if (muacPediatrics.equals("<11.5cm (Severe Acute Malnutrition)") || muacPediatrics.equals("11.5-12.5cm (Moderate Acute Malnutrition)")) {
                        if (isMale(gender)) {
                            ++i37m2;
                        } else {
                            ++i37f2;  //Clinically undernourished PLHIV (OLD_on Follow up this FY) 
                        }
                        if (supplementaryFood.equals("Yes")) {
                            if (isMale(gender)) {
                                ++i38m2;
                            } else {
                                ++i38f2; //Clinically undernourished PLHIV who received therapeutic or supplementary food (OLD_on Follow up this FY)
                            }
                        }
                    }
                }

                if (age >= 5 && age <= 14) { // 5 - 14
                    if (bmi.equals("<18.5 (Underweight)")) {
                        if (isMale(gender)) {
                            ++i37m3;
                        } else {
                            ++i37f3;  //Clinically undernourished PLHIV (OLD_on Follow up this FY) 
                        }
                        if (supplementaryFood.equals("Yes")) {
                            if (isMale(gender)) {
                                ++i38m3;
                            } else {
                                ++i38f3; //Clinically undernourished PLHIV who received therapeutic or supplementary food (OLD_on Follow up this FY)
                            }
                        }
                    }
                }

                if (age >= 15 && age <= 17) { // 15 - 17
                    if (bmi.equals("<18.5 (Underweight)")) {
                        if (isMale(gender)) {
                            ++i37m4;
                        } else {
                            ++i37f4;  //Clinically undernourished PLHIV (OLD_on Follow up this FY) 
                        }
                        if (supplementaryFood.equals("Yes")) {
                            if (isMale(gender)) {
                                ++i38m4;
                            } else {
                                ++i38f4; //Clinically undernourished PLHIV who received therapeutic or supplementary food (OLD_on Follow up this FY)
                            }
                        }
                    }
                }

                if (age >= 18) { // 18+
                    if (bmi.equals("<18.5 (Underweight)")) {
                        if (isMale(gender)) {
                            ++i37m5;
                        } else {
                            ++i37f5;  //Clinically undernourished PLHIV (OLD_on Follow up this FY)
                        }
                        if (supplementaryFood.equals("Yes")) {
                            if (isMale(gender)) {
                                ++i38m5;
                            } else {
                                ++i38f5; //Clinically undernourished PLHIV who received therapeutic or supplementary food (OLD_on Follow up this FY)
                            }
                        }
                    }
                }
            }

            parameterMap.put("i37m1", Integer.toString(i37m1));
            parameterMap.put("i37m2", Integer.toString(i37m2));
            parameterMap.put("i37m3", Integer.toString(i37m3));
            parameterMap.put("i37m4", Integer.toString(i37m4));
            parameterMap.put("i37m5", Integer.toString(i37m5));

            parameterMap.put("i37f1", Integer.toString(i37f1));
            parameterMap.put("i37f2", Integer.toString(i37f2));
            parameterMap.put("i37f3", Integer.toString(i37f3));
            parameterMap.put("i37f4", Integer.toString(i37f4));
            parameterMap.put("i37f5", Integer.toString(i37f5));

            parameterMap.put("i37t1", Integer.toString(i37m1 + i37f1));
            parameterMap.put("i37t2", Integer.toString(i37m2 + i37f2));
            parameterMap.put("i37t3", Integer.toString(i37m3 + i37f3));
            parameterMap.put("i37t4", Integer.toString(i37m4 + i37f4));
            parameterMap.put("i37t5", Integer.toString(i37m5 + i37f5));

            parameterMap.put("i38m1", Integer.toString(i38m1));
            parameterMap.put("i38m2", Integer.toString(i38m2));
            parameterMap.put("i38m3", Integer.toString(i38m3));
            parameterMap.put("i38m4", Integer.toString(i38m4));
            parameterMap.put("i38m5", Integer.toString(i38m5));

            parameterMap.put("i38f1", Integer.toString(i38f1));
            parameterMap.put("i38f2", Integer.toString(i38f2));
            parameterMap.put("i38f3", Integer.toString(i38f3));
            parameterMap.put("i38f4", Integer.toString(i38f4));
            parameterMap.put("i38f5", Integer.toString(i38f5));

            parameterMap.put("i38t1", Integer.toString(i38m1 + i38f1));
            parameterMap.put("i38t2", Integer.toString(i38m2 + i38f2));
            parameterMap.put("i38t3", Integer.toString(i38m3 + i38f3));
            parameterMap.put("i38t4", Integer.toString(i38m4 + i38f4));
            parameterMap.put("i38t5", Integer.toString(i38m5 + i38f5));

            //PLHIV counselled on gender norms & GBV
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND ((chroniccare.gbv1 = 'Yes' AND chroniccare.gbv1_referred = 'Yes') OR  (chroniccare.gbv2 = 'Yes' AND chroniccare.gbv2_referred = 'Yes'))";
            parameterMap.put("i39", Integer.toString(executeQuery(query)));

            //PLHIV who are screened for GBV cases 
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND ((chroniccare.gbv1 IS NOT NULL AND chroniccare.gbv1 != '') AND (chroniccare.gbv2 IS NOT NULL AND chroniccare.gbv2 != ''))";
            parameterMap.put("i40", Integer.toString(executeQuery(query)));

            //GBV cases referred for post-GBV care (OLD_first time this FY)
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "mindatevisitsold.date_visit >= '" + reportingDateBegin + "' AND mindatevisitsold.date_visit <= '" + reportingDateEnd + "') "
                    + "AND ((chroniccare.gbv1 = 'Yes' AND chroniccare.gbv1_referred = 'Yes') OR  (chroniccare.gbv2 = 'Yes' AND chroniccare.gbv2_referred = 'Yes'))";
            parameterMap.put("i41", Integer.toString(executeQuery(query)));

            //GBV cases referred for post-GBV care (OLD_on Follow up this FY)
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN carevisits ON chroniccare.patient_id = carevisits.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "carevisits.date_visit >= '" + reportingDateBegin + "' AND carevisits.date_visit <= '" + reportingDateEnd + "') "
                    + "AND mindatevisitsold.date_visit < '" + reportingDateBegin + "' "
                    + "AND ((chroniccare.gbv1 = 'Yes' AND chroniccare.gbv1_referred = 'Yes') OR  (chroniccare.gbv2 = 'Yes' AND chroniccare.gbv2_referred = 'Yes')) "
                    + "AND patient.patient_id IN (SELECT DISTINCT patient_id FROM positives)";
            parameterMap.put("i42", Integer.toString(executeQuery(query)));

            //PLHIV screened for chronic illnesses
            int i43m = 0, i43f = 0, i43t = 0;

            query = "SELECT DISTINCT patient.patient_id, patient.gender FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND ((chroniccare.bp_above IS NOT NULL AND chroniccare.bp_above != '') AND (chroniccare.dm_values IS NOT NULL AND chroniccare.dm_values != ''))";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                if (isMale(gender)) {
                    ++i43m;
                } else {
                    ++i43f;
                }
            }

            parameterMap.put("i43m", Integer.toString(i43m));
            parameterMap.put("i43f", Integer.toString(i43f));
            parameterMap.put("i43t", Integer.toString(i43m + i43f));

            //PLHIV suspected to have HTN (OLD_first time this FY)
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "mindatevisitsold.date_visit >= '" + reportingDateBegin + "' AND mindatevisitsold.date_visit <= '" + reportingDateEnd + "') "
                    + "AND (chroniccare.hypertensive = 'No' AND chroniccare.bp_above = 'Yes')";
            parameterMap.put("i44", Integer.toString(executeQuery(query)));

            //PLHIV suspected to have DM (OLD_first time this FY)
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "mindatevisitsold.date_visit >= '" + reportingDateBegin + "' AND mindatevisitsold.date_visit <= '" + reportingDateEnd + "') "
                    + "AND (chroniccare.diabetic = 'No' "
                    + "AND (chroniccare.dm_values LIKE '%Yes' OR chroniccare.dm_values LIKE 'Yes%' OR chroniccare.dm_values LIKE '%Yes%'))";
            parameterMap.put("i45", Integer.toString(executeQuery(query)));

            //PLHIV co-morbid with HTN (newly identified) (OLD_first time this FY)
            int i46m = 0, i46f = 0, i46t = 0;

            query = "SELECT DISTINCT patient.patient_id, patient.gender FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "mindatevisitsold.date_visit >= '" + reportingDateBegin + "' AND mindatevisitsold.date_visit <= '" + reportingDateEnd + "') "
                    + "AND (chroniccare.hypertensive = 'Yes' AND chroniccare.first_hypertensive = 'Yes')";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                if (isMale(gender)) {
                    ++i46m;
                } else {
                    ++i46f;
                }
            }

            parameterMap.put("i46m", Integer.toString(i46m));
            parameterMap.put("i46f", Integer.toString(i46f));
            parameterMap.put("i46t", Integer.toString(i46m + i46f));

            //PLHIV co-morbid with DM (newly identified)  (OLD_first time this FY)
            int i47m = 0, i47f = 0, i47t = 0;

            query = "SELECT DISTINCT patient.patient_id, patient.gender FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "mindatevisitsold.date_visit >= '" + reportingDateBegin + "' AND mindatevisitsold.date_visit <= '" + reportingDateEnd + "') "
                    + "AND (chroniccare.diabetic = 'Yes' AND chroniccare.first_diabetic = 'Yes')";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                if (isMale(gender)) {
                    ++i47m;
                } else {
                    ++i47f;
                }
            }

            parameterMap.put("i47m", Integer.toString(i47m));
            parameterMap.put("i47f", Integer.toString(i47f));
            parameterMap.put("i47t", Integer.toString(i47m + i47f));

            //PLHIV suspected to have HTN (OLD_on Follow up this FY)
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN carevisits ON chroniccare.patient_id = carevisits.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "carevisits.date_visit >= '" + reportingDateBegin + "' AND carevisits.date_visit <= '" + reportingDateEnd + "') "
                    + "AND mindatevisitsold.date_visit < '" + reportingDateBegin + "' "
                    + "AND (chroniccare.hypertensive = 'No' AND chroniccare.bp_above = 'Yes') "
                    + "AND patient.patient_id IN (SELECT DISTINCT patient_id FROM positives)";
            parameterMap.put("i48", Integer.toString(executeQuery(query)));

            //PLHIV suspected to have DM (OLD_on Follow up this FY)
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN carevisits ON chroniccare.patient_id = carevisits.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "carevisits.date_visit >= '" + reportingDateBegin + "' AND carevisits.date_visit <= '" + reportingDateEnd + "') "
                    + "AND mindatevisitsold.date_visit < '" + reportingDateBegin + "' "
                    + "AND (chroniccare.diabetic = 'No' "
                    + "AND (chroniccare.dm_values LIKE '%Yes' OR chroniccare.dm_values LIKE 'Yes%' OR chroniccare.dm_values LIKE '%Yes%')) "
                    + "AND patient.patient_id IN (SELECT DISTINCT patient_id FROM positives)";
            parameterMap.put("i49", Integer.toString(executeQuery(query)));

            //PLHIV co-morbid with HTN (newly identified) (OLD_on Follow up this FY)
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN carevisits ON chroniccare.patient_id = carevisits.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "carevisits.date_visit >= '" + reportingDateBegin + "' AND carevisits.date_visit <= '" + reportingDateEnd + "') "
                    + "AND mindatevisitsold.date_visit < '" + reportingDateBegin + "' "
                    + "AND (chroniccare.hypertensive = 'Yes' AND chroniccare.first_hypertensive = 'Yes') "
                    + "AND patient.patient_id IN (SELECT DISTINCT patient_id FROM positives)";
            parameterMap.put("i50", Integer.toString(executeQuery(query)));

            //PLHIV co-morbid with DM (newly identified)  (OLD_on Follow up this FY)
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN carevisits ON chroniccare.patient_id = carevisits.patient_id "
                    + "JOIN mindatevisitsold ON chroniccare.patient_id = mindatevisitsold.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "carevisits.date_visit >= '" + reportingDateBegin + "' AND carevisits.date_visit <= '" + reportingDateEnd + "') "
                    + "AND mindatevisitsold.date_visit < '" + reportingDateBegin + "' "
                    + "AND (chroniccare.diabetic = 'Yes' AND chroniccare.first_diabetic = 'Yes') "
                    + "AND patient.patient_id IN (SELECT DISTINCT patient_id FROM positives)";
            parameterMap.put("i51", Integer.toString(executeQuery(query)));

            //PLHIV who received PHDP service 
            int i52m1 = 0, i52f1 = 0, i52t1 = 0;
            int i52m2 = 0, i52f2 = 0, i52t2 = 0;

            query = "SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" + reportingDateBegin + "') AS age "
                    + "FROM patient JOIN chroniccare ON patient.patient_id = chroniccare.patient_id "
                    + "JOIN positives ON chroniccare.patient_id = positives.patient_id "
                    + "WHERE patient.date_registration < '" + reportingDateBeginFy + "' AND ("
                    + "chroniccare.date_visit >= '" + reportingDateBegin + "' AND chroniccare.date_visit <= '" + reportingDateEnd + "') "
                    + "AND (chroniccare.phdp1_services_provided = 'Yes' OR chroniccare.phdp4_services_provided = 'Yes' "
                    + "OR chroniccare.phdp7_services_provided = 'Yes' OR chroniccare.phdp8_services_provided = 'Yes' "
                    + "OR (chroniccare.phdp9_services_provided IS NOT NULL AND chroniccare.phdp9_services_provided != ''))";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                if (age < 15) { // < 15
                    if (isMale(gender)) {
                        ++i52m1;
                    } else {
                        ++i52f1;
                    }
                }
                if (age >= 15) { // 15+
                    if (isMale(gender)) {
                        ++i52m2;
                    } else {
                        ++i52f2;
                    }
                }
            }

            parameterMap.put("i52m1", Integer.toString(i52m1));
            parameterMap.put("i52m2", Integer.toString(i52m2));

            parameterMap.put("i52f1", Integer.toString(i52f1));
            parameterMap.put("i52f2", Integer.toString(i52f2));

            parameterMap.put("i52t1", Integer.toString(i52m1 + i52f1));
            parameterMap.put("i52t2", Integer.toString(i52m2 + i52f2));


            System.out.println("...Successful end");


            parameterMap.put("reportingMonth", dto.getReportingMonth());
            parameterMap.put("reportingYear", dto.getReportingYear());

            // fetch the required records from the database   
            query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility JOIN lga ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE facility_id = " + facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                parameterMap.put("facilityName", resultSet.getString("name"));
                parameterMap.put("lga", resultSet.getString("lga"));
                parameterMap.put("state", resultSet.getString("state"));
            }
        } catch (Exception exception) {

            System.out.println("...ExcepFetch - " + exception.getMessage());

            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }

        System.out.println("...Before returning parameter map");

        return parameterMap;
    }

    private boolean isMale(String gender) {
        return gender.trim().equals("Male") ? true : false;
    }

    private int executeQuery(String query) {
        int count = 0;
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (Exception exception) {

            System.out.println("...ExcepCount - " + exception.getMessage());

            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return count;
    }

    private void executeUpdate(String query) {
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {

            System.out.println("...ExcepCreate - " + exception.getMessage());

            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }
}
