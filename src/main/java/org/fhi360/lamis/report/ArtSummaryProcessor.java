/**
 * @author AALOZIE
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

@Component
public class ArtSummaryProcessor {
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //private static final Log log = LogFactory.getLog(ArtSummaryProcessor.class);

    private int agem1, agem2, agem3, agem4, agem5, agem6, agem7, agem8;
    private int agef1, agef2, agef3, agef4, agef5, agef6, agef7, agef8;
    private int agem9_1, agem10_1, agem11_1, agem9_2, agem10_2, agem11_2;
    private int agef9_1, agef10_1, agef11_1, agef9_2, agef10_2, agef11_2;
    private int preg, feeding, tbm, tbf;

    public ArtSummaryProcessor() {
    }

    public synchronized Map<String, Object> process(String month, String year, Long facilityId) {

        Map<String, Object> parameterMap = new HashMap<>();
        int reportingMonth = DateUtil.getMonth(month);
        int reportingYear = Integer.parseInt(year);
        String reportingDateBegin = dateFormat.format(DateUtil.getFirstDateOfMonth(reportingYear, reportingMonth));
        String reportingDateEnd = dateFormat.format(DateUtil.getLastDateOfMonth(reportingYear, reportingMonth));

        ResultSet resultSet;
        try {
            jdbcUtil = new JDBCUtil();

            ResultSet rs;
            System.out.println("Computing ART1.....");
            //ART 1
            //Total Number of HIV-positive newly enrolled in clinical care during the month (excludes transfer-in)
            initVariables();
            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateEnd + "') AS age, pregnant, breastfeeding, tb_status FROM patient WHERE facility_id = " + facilityId + " AND YEAR(date_registration) = " + reportingYear + " AND MONTH(date_registration) = " + reportingMonth + " AND status_registration = 'HIV+ non ART'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                int pregnant = resultSet.getInt("pregnant");
                int breastfeeding = resultSet.getInt("breastfeeding");
                String tbStatus = resultSet.getString("tb_status") == null ? "" : resultSet.getString("tb_status");

                disaggregate(gender, age);
                if (gender.trim().equalsIgnoreCase("Male")) {
                    //check for TB status during enrolmemnt
                    if (tbStatus.equalsIgnoreCase("Currently on TB treatment") || tbStatus.equalsIgnoreCase("TB positive not on TB drugs")) {
                        tbm++;
                    }
                } else {
                    //check if client is pregnant or breast feeding during enrolment
                    if (pregnant == 1) {
                        preg++;
                    } else {
                        if (breastfeeding == 1) {
                            feeding++;
                        }
                    }
                    //check for TB status during enrolmemnt
                    if (tbStatus.equalsIgnoreCase("Currently on TB treatment") || tbStatus.equalsIgnoreCase("TB positive not on TB drugs")) {
                        tbf++;
                    }
                }
            }
            //Populate the report parameter map with values computed for ART 1
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

            parameterMap.put("art1f9", Integer.toString(preg));
            parameterMap.put("art1t9", Integer.toString(preg));

            parameterMap.put("art1f10", Integer.toString(feeding));
            parameterMap.put("art1t10", Integer.toString(feeding));

            parameterMap.put("art1m11", Integer.toString(tbm));
            parameterMap.put("art1f11", Integer.toString(tbf));
            parameterMap.put("art1t11", Integer.toString(tbm + tbf));

            System.out.println("Computing ART2.....");
            //ART 2
            //Total number of people living with HIV who are currently in HIV care who received at least one of the following
            //by the end of the month: clinical assessment(WHO staging) OR CD4 count OR viral load OR current on treatment           
            initVariables();

            executeUpdate("DROP INDEX IF EXISTS idx_visit");
            executeUpdate("DROP TABLE IF EXISTS visit");
            executeUpdate("CREATE TEMPORARY TABLE visit AS SELECT * FROM clinic WHERE facility_id = " + facilityId + " AND date_visit >= DATEADD('MONTH', -6, '" + reportingDateBegin + "') AND date_visit <= '" + reportingDateEnd + "' AND clinic_stage IS NOT NULL OR clinic_stage != ''");
            executeUpdate("CREATE INDEX idx_visit ON visit(patient_id)");

            executeUpdate("DROP INDEX IF EXISTS idx_preg");
            executeUpdate("DROP TABLE IF EXISTS preg");
            executeUpdate("CREATE TEMPORARY TABLE preg AS SELECT * FROM clinic WHERE facility_id = " + facilityId + " AND date_visit >= DATEADD('MONTH', -9, '" + reportingDateBegin + "') AND date_visit <= '" + reportingDateEnd + "' ORDER BY date_visit DESC LIMIT 1");
            executeUpdate("CREATE INDEX idx_preg ON preg(patient_id)");

            executeUpdate("DROP INDEX IF EXISTS idx_pharm");
            executeUpdate("DROP TABLE IF EXISTS pharm");
            executeUpdate("CREATE TEMPORARY TABLE pharm AS SELECT * FROM pharmacy WHERE facility_id = " + facilityId + " AND date_visit <= '" + reportingDateEnd + "' AND regimentype_id IN (1, 2, 3, 4, 14)");
            executeUpdate("CREATE INDEX idx_pharm ON pharm(patient_id)");

            executeUpdate("DROP INDEX IF EXISTS idx_lab");
            executeUpdate("DROP TABLE IF EXISTS lab");
            executeUpdate("CREATE TEMPORARY TABLE lab AS SELECT * FROM laboratory WHERE facility_id = " + facilityId + " AND date_reported >= DATEADD('MONTH', -6, '" + reportingDateBegin + "') AND date_reported <= '" + reportingDateEnd + "' AND labtest_id IN (1, 16)");
            executeUpdate("CREATE INDEX idx_lab ON lab(patient_id)");

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateEnd + "') AS age," +
                    " date_started FROM patient WHERE facility_id = " + facilityId + " AND date_registration <= '" +
                    reportingDateEnd + "' AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') OR " +
                    "(current_status IN ('ART Transfer Out', 'Lost to Follow Up', 'Stopped Treatment', 'Known Death') " +
                    "AND date_current_status > '" + reportingDateEnd + "')";
            //query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateEnd + "') AS age FROM patient WHERE facility_id = " + id + " AND date_registration <= '" + reportingDateEnd + "' AND  current_status IN (" + Constants.ClientStatus.ON_TREATMENT + ") OR (current_status IN (" + Constants.ClientStatus.LOSSES + ") AND date_current_status > '" + reportingDateEnd + "')";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                Date dateStarted = resultSet.getDate("date_started");

                int pregnant = 0;
                int breastfeeding = 0;

                boolean count = false;

                query = "SELECT DISTINCT patient_id FROM visit WHERE patient_id = " + patientId;
                if (found(query)) {
                    count = true;
                } else {
                    query = "SELECT DISTINCT patient_id FROM lab WHERE patient_id = " + patientId;
                    if (found(query)) {
                        count = true;
                    } else {
                        if (dateStarted != null) {
                            rs = getLastRefillVisit(patientId);
                            while (rs != null && rs.next()) {
                                Date dateLastRefill = rs.getDate("date_visit");
                                int duration = rs.getInt("duration");
                                int monthRefill = duration / 30;
                                if (monthRefill <= 0) {
                                    monthRefill = 1;
                                }

                                if (dateLastRefill != null) {
                                    //If the last refill date plus refill duration plus 90 days is before the last day of the reporting month this patient is LTFU     if(DateUtil.addYearMonthDay(lastRefill, duration+90, "day(s)").before(reportingDateEnd)) 
                                    if (DateUtil.addYearMonthDay(dateLastRefill, duration + Constants.LTFU.GON,
                                            "DAY").after(DateUtil.getLastDateOfMonth(reportingYear, reportingMonth))) {
                                        count = true;
                                    }
                                }
                            }
                        }
                    }
                }

                if (count) {
                    disaggregate(gender, age);
                    if (gender.trim().equalsIgnoreCase("Female")) {
                        //check if client is pregnant or breast feeding during enrolment
                        query = "SELECT pregnant, breastfeeding FROM preg WHERE patient_id = " + patientId + " ORDER BY date_visit DESC LIMIT 1";
                        rs = executeQuery(query);
                        if (rs.next()) {
                            pregnant = rs.getInt("pregnant");
                            breastfeeding = rs.getInt("breastfeeding");
                        }
                        if (pregnant == 1) {
                            preg++;
                        } else {
                            if (breastfeeding == 1) {
                                feeding++;
                            }
                        }
                    }
                }
            }
            //Populate the report parameter map with values computed for ART 2
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

            parameterMap.put("art2f9", Integer.toString(preg));
            parameterMap.put("art2t9", Integer.toString(preg));

            System.out.println("Computing ART3.....");
            //ART 3
            //Total number of people living with HIV newly started on ART during the month (excludes ART transfer-in)
            initVariables();

            executeUpdate("DROP INDEX IF EXISTS idx_preg");
            executeUpdate("DROP TABLE IF EXISTS preg");
            executeUpdate("CREATE TEMPORARY TABLE preg AS SELECT * FROM clinic WHERE facility_id = " +
                    facilityId + " AND commence = 1");
            executeUpdate("CREATE INDEX idx_preg ON preg(patient_id)");

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateEnd + "') AS age " +
                    "FROM patient WHERE facility_id = " + facilityId + " AND YEAR(date_started) = " + reportingYear +
                    " AND MONTH(date_started) = " + reportingMonth + " AND status_registration != 'ART Transfer In'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                int pregnant = 0;
                int breastfeeding = 0;

                disaggregate(gender, age);

                if (gender.trim().equalsIgnoreCase("Female")) {
                    //check if client is pregnant or breast feeding during visit
                    query = "SELECT pregnant, breastfeeding FROM preg WHERE patient_id = " + patientId + " ORDER BY " +
                            "date_visit DESC LIMIT 1";
                    rs = executeQuery(query);
                    if (rs.next()) {
                        pregnant = rs.getInt("pregnant");
                        breastfeeding = rs.getInt("breastfeeding");
                    }
                    if (pregnant == 1) {
                        preg++;
                    } else {
                        if (breastfeeding == 1) {
                            feeding++;
                        }
                    }
                }
            }
            //Populate the report parameter map with values computed for ART 3
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

            parameterMap.put("art3f9", Integer.toString(preg));
            parameterMap.put("art3t9", Integer.toString(preg));

            parameterMap.put("art3f10", Integer.toString(feeding));
            parameterMap.put("art3t10", Integer.toString(feeding));

            System.out.println("Computing ART4.....");
            //ART 4
            //Total number of people living with HIV who are currently receiving ART during the month (All regimen)
            initVariables();

            executeUpdate("DROP INDEX IF EXISTS idx_preg");
            executeUpdate("DROP TABLE IF EXISTS preg");
            executeUpdate("CREATE TEMPORARY TABLE preg AS SELECT * FROM clinic WHERE facility_id = " +
                    facilityId + " AND date_visit >=  DATEADD('MONTH', -9, '" + reportingDateBegin + "') AND " +
                    "date_visit <= '" + reportingDateEnd + "'");
            executeUpdate("CREATE INDEX idx_preg ON preg(patient_id)");

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateEnd + "') AS age " +
                    "FROM patient WHERE facility_id = " + facilityId + " AND current_status IN ('ART Start', " +
                    "'ART Restart', 'ART Transfer In') " +
                    "OR (current_status IN ('ART Transfer Out', 'Lost to Follow Up', 'Stopped Treatment', " +
                    "'Known Death') AND date_current_status > '" + reportingDateEnd + "') AND date_started IS NOT NULL " +
                    "AND date_started <= '" + reportingDateEnd + "'";
            //query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateEnd + "') AS age FROM patient WHERE facility_id = " + id + " AND current_status IN (" + Constants.ClientStatus.ON_TREATMENT + ") OR (current_status IN (" + Constants.ClientStatus.LOSSES + ") AND date_current_status > '" + reportingDateEnd + "') AND date_started IS NOT NULL AND date_started <= '" + reportingDateEnd + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                int pregnant = 0;
                int breastfeeding = 0;
                rs = getLastRefillVisit(patientId);
                while (rs != null && rs.next()) {
                    Date dateLastRefill = rs.getDate("date_visit");
                    int duration = rs.getInt("duration");
                    int monthRefill = duration / 30;
                    if (monthRefill <= 0) {
                        monthRefill = 1;
                    }

                    if (dateLastRefill != null) {
                        //If the last refill date plus refill duration plus 90 days is before the last day of the reporting month this patient is LTFU     if(DateUtil.addYearMonthDay(lastRefill, duration+90, "day(s)").before(reportingDateEnd)) 
                        if (DateUtil.addYearMonthDay(dateLastRefill, duration +
                                Constants.LTFU.GON, "DAY").after(DateUtil.getLastDateOfMonth(reportingYear,
                                reportingMonth))) {
                            disaggregate(gender, age);

                            long regimentypeId = rs.getLong("regimentype_id");
                            if (gender.trim().equalsIgnoreCase("Male")) {
                                if (age < 15) {
                                    if (regimentypeId == 1 || regimentypeId == 3) {
                                        agem9_1++;
                                    } else {
                                        if (regimentypeId == 2 || regimentypeId == 4) {
                                            agem10_1++;
                                        } else {
                                            agem11_1++;
                                        }
                                    }
                                } else {
                                    if (regimentypeId == 1 || regimentypeId == 3) {
                                        agem9_2++;
                                    } else {
                                        if (regimentypeId == 2 || regimentypeId == 4) {
                                            agem10_2++;
                                        } else {
                                            agem11_2++;
                                        }
                                    }
                                }
                            } else {
                                if (age < 15) {
                                    if (regimentypeId == 1 || regimentypeId == 3) {
                                        agef9_1++;
                                    } else {
                                        if (regimentypeId == 2 || regimentypeId == 4) {
                                            agef10_1++;
                                        } else {
                                            agef11_1++;
                                        }
                                    }
                                } else {
                                    if (regimentypeId == 1 || regimentypeId == 3) {
                                        agef9_2++;
                                    } else {
                                        if (regimentypeId == 2 || regimentypeId == 4) {
                                            agef10_2++;
                                        } else {
                                            agef11_2++;
                                        }
                                    }
                                }
                            }

                            if (gender.trim().equalsIgnoreCase("Female")) {
                                //check if client is pregnant or breast feeding during enrolment
                                query = "SELECT pregnant, breastfeeding FROM preg WHERE patient_id = " + patientId +
                                        " ORDER BY date_visit DESC LIMIT 1";
                                ResultSet rs1 = executeQuery(query);
                                if (rs1.next()) {
                                    pregnant = rs1.getInt("pregnant");
                                    breastfeeding = rs1.getInt("breastfeeding");
                                }

                                if (pregnant == 1) {
                                    preg++;
                                } else {
                                    if (breastfeeding == 1) {
                                        feeding++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //Populate the report parameter map with values computed for ART 4
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

            parameterMap.put("art4m9_1", Integer.toString(agem9_1));
            parameterMap.put("art4f9_1", Integer.toString(agef9_1));
            parameterMap.put("art4m9_2", Integer.toString(agem9_2));
            parameterMap.put("art4f9_2", Integer.toString(agef9_2));
            parameterMap.put("art4t9", Integer.toString(agem9_1 + agef9_1 + agem9_2 + agef9_2));

            parameterMap.put("art4m10_1", Integer.toString(agem10_1));
            parameterMap.put("art4f10_1", Integer.toString(agef10_1));
            parameterMap.put("art4m10_2", Integer.toString(agem10_2));
            parameterMap.put("art4f10_2", Integer.toString(agef10_2));
            parameterMap.put("art4t10", Integer.toString(agem10_1 + agef10_1 + agem10_2 + agef10_2));

            parameterMap.put("art4m11_1", Integer.toString(agem11_1));
            parameterMap.put("art4f11_1", Integer.toString(agef11_1));
            parameterMap.put("art4m11_2", Integer.toString(agem11_2));
            parameterMap.put("art4f11_2", Integer.toString(agef11_2));
            parameterMap.put("art4t11", Integer.toString(agem11_1 + agef11_1 + agem11_2 + agef11_2));

            parameterMap.put("art4f12", Integer.toString(preg));
            parameterMap.put("art4f13", Integer.toString(feeding));

            parameterMap.put("art4t12", Integer.toString(preg));
            parameterMap.put("art4t13", Integer.toString(feeding));

            System.out.println("Computing ART5.....");
            //ART 5
            //Number of people living with HIV and on ART with a viral load test result during the month
            initVariables();

            executeUpdate("DROP INDEX IF EXISTS idx_lab");
            executeUpdate("DROP TABLE IF EXISTS lab");
            executeUpdate("CREATE TEMPORARY TABLE lab AS SELECT * FROM laboratory WHERE facility_id = " +
                    facilityId + " AND YEAR(date_reported) = " + reportingYear + " AND MONTH(date_reported) = " +
                    reportingMonth + " AND labtest_id = 16");
            executeUpdate("CREATE INDEX idx_lab ON lab(patient_id)");

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateEnd + "') AS age " +
                    "FROM patient WHERE facility_id = " + facilityId + " AND date_registration <= '" +
                    reportingDateEnd + "' AND date_started IS NOT NULL AND DATEDIFF(MONTH, date_started, '" +
                    reportingDateEnd + "') >= 6 AND date_started <= '" + reportingDateEnd + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                //Check for viral load this reporting month
                query = "SELECT patient_id FROM lab WHERE patient_id = " + patientId + " ORDER BY date_reported DESC " +
                        "LIMIT 1";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    disaggregate(gender, age);
                }
            }
            //Populate the report parameter map with values computed for ART 5
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

            System.out.println("Computing ART6.....");
            //ART 6
            //Number of people living with HIV and on ART who are virologically suppressed (viral load < 1000 c/ml) during the month
            initVariables();

            executeUpdate("DROP INDEX IF EXISTS idx_lab");
            executeUpdate("DROP TABLE IF EXISTS lab");
            executeUpdate("CREATE TEMPORARY TABLE lab AS SELECT * FROM laboratory WHERE facility_id = " +
                    facilityId + " AND YEAR(date_reported) = " + reportingYear + " AND MONTH(date_reported) = " +
                    reportingMonth + " AND labtest_id = 16");
            executeUpdate("CREATE INDEX idx_lab ON lab(patient_id)");

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateEnd + "') AS age" +
                    " FROM patient WHERE facility_id = " + facilityId + " AND date_registration <= '" +
                    reportingDateEnd + "' AND date_started IS NOT NULL AND DATEDIFF(MONTH, date_started, '" +
                    reportingDateEnd + "') >= 6 AND date_started <= '" + reportingDateEnd + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                //Check if the last viral load before the reporting month is less than 1000
                query = "SELECT * FROM lab WHERE patient_id = " + patientId + " ORDER BY date_reported DESC LIMIT 1";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String resultab = rs.getString("resultab");
                    if (isInteger(resultab)) {
                        if (Double.valueOf(resultab) < 1000) {
                            disaggregate(gender, age);
                        }
                    }
                }
            }
            //Populate the report parameter map with values computed for ART 6
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

            System.out.println("Computing ART7.....");
            //ART 7
            //Total number of people living with HIV known to have died during the month
            initVariables();
            query = "SELECT DISTINCT gender, DATEDIFF(YEAR, date_birth, '" + reportingDateEnd + "') AS age FROM patient WHERE facility_id = " + facilityId + " AND current_status = 'Known Death' AND YEAR(date_current_status) = " + reportingYear + " AND MONTH(date_current_status) = " + reportingMonth;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                if (gender.trim().equalsIgnoreCase("Male")) {
                    agem1++;
                } else {
                    agef1++;
                }
            }
            parameterMap.put("art7m1", Integer.toString(agem1));
            parameterMap.put("art7f1", Integer.toString(agef1));
            parameterMap.put("art7t1", Integer.toString(agem1 + agef1));

            System.out.println("Computing ART8.....");
            //ART 8
            //Number of People living with HIV who are lost to follow up during the month
            initVariables();
            query = "SELECT DISTINCT gender, DATEDIFF(YEAR, date_birth, '" + reportingDateEnd + "') AS age FROM " +
                    "patient WHERE patient_id IN (SELECT DISTINCT patient_id FROM statushistory WHERE facility_id = " +
                    facilityId + " AND current_status = 'Lost to Follow Up' AND YEAR(date_current_status) = " +
                    reportingYear + " AND MONTH(date_current_status) = " + reportingMonth + ")";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                if (gender.trim().equalsIgnoreCase("Male")) {
                    agem1++;
                } else {
                    agef1++;
                }
            }
            parameterMap.put("art8m1", Integer.toString(agem1));
            parameterMap.put("art8f1", Integer.toString(agef1));
            parameterMap.put("art8t1", Integer.toString(agem1 + agef1));

            System.out.println("Adding headers.....");
            //Include reproting period & facility details into report header 
            parameterMap.put("reportingMonth", month);
            parameterMap.put("reportingYear", year);

            query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, " +
                    "facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility JOIN lga " +
                    "ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE " +
                    "facility_id = " + facilityId;
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
                                    if (age >= 25 && age <= 49) {
                                        agem7++;
                                    } else {
                                        if (age >= 50) {
                                            agem8++;
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
                                    if (age >= 25 && age <= 49) {
                                        agef7++;
                                    } else {
                                        if (age >= 50) {
                                            agef8++;
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

    private ResultSet getLastRefillVisit(long patientId) {
        ResultSet rs = null;
        try {
            query = "SELECT DISTINCT regimentype_id, regimen_id, date_visit, duration FROM pharm WHERE " +
                    "patient_id = " + patientId + " ORDER BY date_visit DESC LIMIT 1";
            preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return rs;
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
        agef1 = 0;
        agef2 = 0;
        agef3 = 0;
        agef4 = 0;
        agef5 = 0;
        agef6 = 0;
        agef7 = 0;
        agef8 = 0;
        agem9_1 = 0;
        agem10_1 = 0;
        agem11_1 = 0;
        agem9_2 = 0;
        agem10_2 = 0;
        agem11_2 = 0;
        agef9_1 = 0;
        agef10_1 = 0;
        agef11_1 = 0;
        agef9_2 = 0;
        agef10_2 = 0;
        agef11_2 = 0;
        preg = 0;
        feeding = 0;
        tbm = 0;
        tbf = 0;
    }

}

//create a temporary table of date of the latest status change on or before the last day of reporting month 
//executeUpdate("DROP TABLE IF EXISTS currentstatus");        
//query = "CREATE TEMPORARY TABLE currentstatus AS SELECT DISTINCT patient_id, MAX(date_current_status) AS date_status FROM statushistory WHERE facility_id = " + id + " AND date_current_status <= '" + reportingDateEnd + "' GROUP BY patient_id";
//preparedStatement = jdbcUtil.getStatement(query);
//preparedStatement.executeUpdate();
//query = "SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" + reportingDateBegin + "') AS age, patient.date_registration, patient.status_registration, patient.date_started, statushistory.current_status, currentstatus.date_status "
//+ " FROM patient JOIN statushistory ON patient.patient_id = statushistory.patient_id JOIN currentstatus ON patient.patient_id = currentstatus.patient_id WHERE patient.facility_id = " + id + " AND statushistory.facility_id = " + id + " AND statushistory.patient_id = currentstatus.patient_id AND statushistory.date_current_status = currentstatus.date_status";
// create a temporary table of date of the latest regimen change on or before the last day of reporting month 
//executeUpdate("DROP TABLE IF EXISTS currentregimen");
//query = "CREATE TEMPORARY TABLE currentregimen AS SELECT DISTINCT patient_id, MAX(date_visit) AS date_visit FROM regimenhistory WHERE facility_id = " + id + " AND date_visit <= '" + reportingDateEnd + "' GROUP BY patient_id";
//preparedStatement = jdbcUtil.getStatement(query);
//preparedStatement.executeUpdate();
//query = "SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" + reportingDateBegin + "') AS age, patient.date_registration, patient.date_started, regimenhistory.regimentype, currentregimen.date_visit "
//+ " FROM patient JOIN regimenhistory ON patient.patient_id = regimenhistory.patient_id JOIN currentregimen ON patient.patient_id = currentregimen.patient_id WHERE patient.facility_id = " + id + " AND regimenhistory.facility_id = " + id + " AND regimenhistory.patient_id = currentregimen.patient_id AND regimenhistory.date_visit = currentregimen.date_visit";
//preparedStatement = jdbcUtil.getStatement(query);
//query = "SELECT patient_id FROM clinic WHERE facility_id = " + id + " AND date_visit BETWEEN DATEADD('MONTH', -3, '" + reportingDateEnd + "') AND " + reportingDateEnd + " AND pregnant = 1";


//"Pre-ART Transfer Out", "ART Transfer Out", "Stopped Treatment", "Lost to Follow Up", "Known Death", 


