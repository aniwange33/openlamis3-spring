/**
 * @author AALOZIE
 */

package org.fhi360.lamis.report;

import org.fhi360.lamis.controller.report.ReportParameterDTO;
import org.fhi360.lamis.report.indicator.ServiceSummaryIndicators;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

@Component
public class ServiceSummaryProcessor {
    private String reportingDateEnd;
    private int[][] value = new int[12][6];
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;

    private long facilityId;

    public ServiceSummaryProcessor() {
    }

    public synchronized List<Map<String, Object>> process(ReportParameterDTO dto, Long facilityId) {
        List<Map<String, Object>> reportList = new ArrayList<>();
        int reportingMonthBegin = DateUtil.getMonth(dto.getReportingMonthBegin());
        int reportingYearBegin = Integer.parseInt(dto.getReportingYearBegin());
        int reportingMonthEnd = DateUtil.getMonth(dto.getReportingMonthEnd());
        int reportingYearEnd = Integer.parseInt(dto.getReportingYearEnd());
        String reportingDateBegin = DateUtil.parseDateToString(DateUtil.getFirstDateOfMonth(reportingYearBegin, reportingMonthBegin), "yyyy-MM-dd");
        reportingDateEnd = DateUtil.parseDateToString(DateUtil.getLastDateOfMonth(reportingYearEnd, reportingMonthEnd), "yyyy-MM-dd");
        ResultSet resultSet;
        String[] indicator = new ServiceSummaryIndicators().initialize();

        try {
            jdbcUtil = new JDBCUtil();

            Date dateBegin = DateUtil.parseStringToDate(reportingDateBegin, "yyyy-MM-dd");
            Date dateEnd = DateUtil.parseStringToDate(reportingDateEnd, "yyyy-MM-dd");

            //patients on care and treatment 
            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS " +
                    "age FROM patient WHERE facility_id = " + facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                long patientId = resultSet.getLong("patient_id");
                query = "SELECT DISTINCT patient_id FROM clinic WHERE facility_id = " + facilityId + " AND " +
                        "patient_id = " + patientId + " AND date_visit >= '" + reportingDateBegin + "' AND " +
                        "date_visit <= '" + reportingDateEnd + "'";
                if (found(query)) {
                    disaggregate(gender, age, 0);
                    disaggregate(gender, age, 1);
                } else {
                    query = "SELECT DISTINCT patient_id FROM pharmacy WHERE facility_id = " + facilityId +
                            " AND patient_id = " + patientId + " AND date_visit >= '" + reportingDateBegin +
                            "' AND date_visit <= '" + reportingDateEnd + "'";
                    if (found(query)) {
                        disaggregate(gender, age, 0);
                        disaggregate(gender, age, 1);
                    } else {
                        query = "SELECT DISTINCT patient_id FROM laboratory WHERE facility_id = " + facilityId +
                                " AND patient_id = " + patientId + " AND date_reported >= '" + reportingDateBegin +
                                "' AND date_reported <= '" + reportingDateEnd + "'";
                        if (found(query)) {
                            disaggregate(gender, age, 0);
                            disaggregate(gender, age, 1);
                        }
                    }
                }
            }

            //receiving contrimoxazole - select the last refill visit before last day of reporting period for all patients on care and treatment
            executeUpdate("DROP TABLE IF EXISTS refill");
            query = "CREATE TEMPORARY TABLE refill AS SELECT DISTINCT patient_id, MAX(date_visit) AS date_visit FROM " +
                    "pharmacy WHERE facility_id = " + facilityId + " AND date_visit <= '" + reportingDateBegin +
                    "' GROUP BY patient_id";
            executeUpdate(query);

            query = "SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" +
                    reportingDateBegin + "') AS age FROM patient JOIN pharmacy ON patient.patient_id = " +
                    "pharmacy.patient_id JOIN refill ON " +
                    "patient.patient_id = refill.patient_id WHERE patient.facility_id = " + facilityId +
                    " AND pharmacy.facility_id = " + facilityId + " AND pharmacy.patient_id = refill.patient_id " +
                    "AND pharmacy.date_visit = refill.date_visit AND pharmacy.regimentype_id = 8";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age, 2);
            }

            //screened for TB in HIV care and treatment 
            query = "SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" +
                    reportingDateBegin + "') AS age FROM patient JOIN clinic ON patient.patient_id = clinic.patient_id " +
                    "WHERE patient.facility_id = " + facilityId + " AND clinic.facility_id = " + facilityId +
                    " AND clinic.date_visit >= '" + reportingDateBegin + "' AND clinic.date_visit <= '" +
                    reportingDateEnd + "' AND clinic.tb_status != '' AND clinic.tb_status IS NOT NULL";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age, 3);
            }

            //started on TB treatment -> select all patients whose tb status at any visit before the begining of the reporting period is Currently on TB treatment
            executeUpdate("DROP TABLE IF EXISTS tb");
            query = "CREATE TEMPORARY TABLE tb AS SELECT DISTINCT patient_id FROM clinic WHERE facility_id = " +
                    facilityId + " AND clinic.date_visit < '" + reportingDateBegin + "' AND clinic.tb_status = " +
                    "'Currently on TB treatment'";
            executeUpdate(query);

            //select all patients whose tb status at any visit within the reporting period is Currently on TB treatment who are not in the tb table created above
            query = "SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" +
                    reportingDateBegin + "') AS age FROM patient JOIN clinic ON patient.patient_id = clinic.patient_id " +
                    "WHERE patient.patient_id NOT IN (SELECT patient_id FROM tb) AND patient.facility_id = " +
                    facilityId + " AND clinic.facility_id = " + facilityId + " AND clinic.date_visit >= '" +
                    reportingDateBegin + "' AND clinic.date_visit <= '" + reportingDateEnd + "' AND clinic.tb_status " +
                    "= 'Currently on TB treatment'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age, 4);
            }

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') " +
                    "AS age, date_registration, status_registration, date_started FROM patient WHERE facility_id = " +
                    facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                Date dateRegistration = resultSet.getDate("date_registration");
                String statusRegistration = resultSet.getString("status_registration");
                Date dateStarted = resultSet.getDate("date_started");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                Map map = getCurrentStatus(patientId, statusRegistration);
                String currentStatus = map.isEmpty() ? "" : (String) map.get("currentStatus");
                Date dateStatus = map.isEmpty() ? null : (Date) map.get("dateStatus");

                if (dateStarted != null) {
                    if (DateUtil.isBetween(dateStarted, dateBegin, dateEnd)) {
                        disaggregate(gender, age, 5); //started on ART this reporting period
                    }
                }

                if (dateStarted != null) {
                    if (DateUtil.isBetween(dateStarted, dateBegin, dateEnd)) {
                        disaggregate(gender, age, 5); //started on ART this reporting period
                    }
                }

                if ((currentStatus.trim().equals("ART Start") || currentStatus.trim().equals("ART Restart") ||
                        currentStatus.trim().equals("ART Transfer In")) && dateStarted != null && dateStatus != null) {
                    if (DateUtil.isBetween(dateStatus, dateBegin, dateEnd)) {
                        disaggregate(gender, age, 6); //currently on ART this reporting period 
                    }
                }

                if (currentStatus.trim().equals("Lost to Follow Up") && dateStarted != null && dateStatus != null) {
                    if (DateUtil.isBetween(dateStatus, dateBegin, dateEnd)) {
                        disaggregate(gender, age, 7); //lost to follow up ART this reporting period 
                    }
                }

                if (currentStatus.trim().equals("Known Death") && dateStarted != null && dateStatus != null) {
                    if (DateUtil.isBetween(dateStatus, dateBegin, dateEnd)) {
                        disaggregate(gender, age, 8); //known death ART this reporting period
                    }
                }
            }

            //defaulters for over 7 days of scheduled clinic appointment
            //create a temporary table of clinic appointment within the reporting period
            executeUpdate("DROP TABLE IF EXISTS appointment");
            query = "CREATE TEMPORARY TABLE appointment AS SELECT DISTINCT patient.patient_id, patient.gender, " +
                    "DATEDIFF(YEAR, patient.date_birth, '" + reportingDateBegin + "') AS age, clinic.next_appointment "
                    + " FROM patient JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id = " +
                    facilityId + " AND clinic.facility_id = " + facilityId + " AND clinic.next_appointment >= '" +
                    reportingDateBegin + "' AND clinic.next_appointment <= '" + reportingDateEnd + "'";
            executeUpdate(query);
            executeUpdate("DROP TABLE IF EXISTS visit");  //patients who came within 7 days of appointment      
            query = "CREATE TEMPORARY TABLE visit AS SELECT DISTINCT clinic.patient_id FROM clinic JOIN appointment ON " +
                    "clinic.patient_id = appointment.patient_id WHERE clinic.facility_id = " + facilityId +
                    " AND clinic.date_visit >= appointment.next_appointment-7 AND clinic.date_visit <= " +
                    "appointment.next_appointment+7";
            executeUpdate(query);

            query = "SELECT * FROM appointment WHERE patient_id NOT IN (SELECT patient_id FROM visit)"; //patients who defaulted
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age, 9);
            }

            //documented ADR
            query = "SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" +
                    reportingDateBegin + "') AS age, patient.date_registration, patient.status_registration " +
                    " FROM patient JOIN adrhistory ON patient.patient_id = adrhistory.patient_id WHERE " +
                    "patient.facility_id = " + facilityId + " AND adrhistory.facility_id = " + facilityId +
                    " AND adrhistory.date_visit >= '" + reportingDateBegin + "' AND adrhistory.date_visit <= '" +
                    reportingDateEnd + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Date dateRegistration = resultSet.getDate("date_registration");
                String statusRegistration = resultSet.getString("status_registration");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                if (!statusRegistration.trim().equals("HIV+ non ART") && dateRegistration != null) {
                    disaggregate(gender, age, 10);
                }
            }

            query = "SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" +
                    reportingDateBegin + "') AS age, patient.date_registration, patient.status_registration "
                    + " FROM patient JOIN adrhistory ON patient.patient_id = adrhistory.patient_id WHERE " +
                    "patient.facility_id = " + facilityId + " AND adrhistory.facility_id = " + facilityId +
                    " AND adrhistory.date_visit >= '" + reportingDateBegin + "' AND adrhistory.date_visit <= '" +
                    reportingDateEnd + "' AND adrhistory.severity > 2";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Date dateRegistration = resultSet.getDate("date_registration");
                String statusRegistration = resultSet.getString("status_registration");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                if (!statusRegistration.trim().equals("HIV+ non ART") && dateRegistration != null) {
                    disaggregate(gender, age, 11);
                }
            }

            //Populating indicator values
            for (int i = 0; i < indicator.length; i++) {
                String male1 = Integer.toString(value[i][0]);
                String male2 = Integer.toString(value[i][1]);
                String male3 = Integer.toString(value[i][2]);
                String fmale1 = Integer.toString(value[i][3]);
                String fmale2 = Integer.toString(value[i][4]);
                String fmale3 = Integer.toString(value[i][5]);

                int total = value[i][0] + value[i][1] + value[i][2] + value[i][3] + value[i][4] + value[i][5];

                // create map of values 
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("sno", Integer.toString(i + 1));
                map.put("indicator", indicator[i]);
                map.put("male1", male1);
                map.put("male2", male2);
                map.put("male3", male3);
                map.put("fmale1", fmale1);
                map.put("fmale2", fmale2);
                map.put("fmale3", fmale3);
                map.put("total", Integer.toString(total));
                reportList.add(map);
            }
            resultSet = null;
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return reportList;
    }

    public Map<String, Object> getReportParameters(ReportParameterDTO dto, Long facilityId) {
        Map<String, Object> parameterMap = new HashMap<>();
        String reportingPeriodBegin = dto.getReportingMonthBegin() + " " + dto.getReportingYearBegin();
        String reportingPeriodEnd = dto.getReportingMonthEnd() + " " + dto.getReportingYearEnd();
        parameterMap.put("reportingPeriodBegin", reportingPeriodBegin);
        parameterMap.put("reportingPeriodEnd", reportingPeriodEnd);
        ResultSet resultSet;

        try {
            jdbcUtil = new JDBCUtil();

            query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility JOIN lga ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE facility_id = " + facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                parameterMap.put("facilityName", resultSet.getString("name"));
                parameterMap.put("lga", resultSet.getString("lga"));
                parameterMap.put("state", resultSet.getString("state"));
            }
            resultSet = null;
        } catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return parameterMap;
    }

    private void disaggregate(String gender, int age, int i) {
        if (gender.trim().equals("Male")) {
            if (age < 1) {
                value[i][0]++;  //males < 1yr
            } else {
                if (age >= 1 && age < 14) {
                    value[i][1]++;  //males 1-14yrs
                } else {
                    value[i][2]++;  //males => 15yrs
                }
            }
        } else {
            if (age < 1) {
                value[i][3]++;  //fmales < 1yr
            } else {
                if (age >= 1 && age < 14) {
                    value[i][4]++;  //fmales 1-14yrs
                } else {
                    value[i][5]++;  //fmales => 15yrs
                }
            }
        }
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
        ResultSet resultSet;
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                found = true;
            }
            resultSet = null;
        } catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return found;
    }

    private Map getCurrentStatus(long patientId, String statusRegistration) {
        Map<String, Object> map = new HashMap<String, Object>();
        ResultSet rs;
        String currentStatus = "";
        Date dateStatus = null;
        try {
            jdbcUtil = new JDBCUtil();
            query = "SELECT current_status, date_current_status FROM statushistory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_current_status = (SELECT MAX(date_current_status) FROM statushistory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_current_status <= '" + reportingDateEnd + "')";
            preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                currentStatus = rs.getString("current_status");
                dateStatus = rs.getDate("date_current_status");
                if (!currentStatus.trim().equals(statusRegistration)) break;
            }
            map.put("currentStatus", currentStatus);
            map.put("dateStatus", dateStatus);
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return map;
    }

}


//query = "CREATE TEMPORARY TABLE refill AS SELECT patient_id, MAX(date_visit) AS date_refill FROM (SELECT DISTINCT patient_id, date_visit FROM pharmacy WHERE facility_id = " + id + " AND date_visit <= '" + reportingDateEnd + "') AS t1 GROUP BY patient_id";
