/**
 * @author AALOZIE
 */

package org.fhi360.lamis.exchange.dhis;

import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Component
public class ArtSummaryConverter {
    private int reportingMonth;
    private int reportingYear;
    private String reportingDateBegin;
    private String reportingDateEnd;
    private int[][] indicator;

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private long facilityId;
    private String facility;
    private String state;
    private String lga;
    private final ApplicationProperties applicationProperties;

    public ArtSummaryConverter(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String convertXml(String month, String year, String facilityIds) {
        reportingMonth = DateUtil.getMonth(month);
        reportingYear = Integer.parseInt(year);
        reportingDateBegin = dateFormat.format(DateUtil.getFirstDateOfMonth(reportingYear, reportingMonth));
        reportingDateEnd = dateFormat.format(DateUtil.getLastDateOfMonth(reportingYear, reportingMonth));

        //indicator = new ArtIndicatorInitializer().init();
        //executeUpdate("DROP VIEW IF EXISTS datavalue"); 
        //executeUpdate(" CREATE VIEW datavalue (state VARCHAR(45), lga VARCHAR(45), org_unit VARCHAR(100), data_element VARCHAR(200), category_option_combo VARCHAR(100), value INT, stored_by VARCHAR(75), time_stamp TIMESTAMP, follow_up VARCHAR(5))"); 

        String[] ids = facilityIds.split(",");
        for (String id : ids) {
            //id = Long.parseLong(id);
            //process();  //process indicator values
            //saveValue();  //save values into indicator table and resolve indicator instance
            //clearValue();  //clear indicator value counter
        }
        String contextPath = applicationProperties.getContextPath();
        String fileName = "";//DhisExportService.buildXml();
        return fileName;
    }

    private void process() {
        try {
            jdbcUtil = new JDBCUtil();

            //create a temporary table of date of the latest status change on or before the last day of reporting month 
            executeUpdate("DROP VIEW IF EXISTS currentstatus");
            query = " CREATE VIEW currentstatus AS SELECT DISTINCT patient_id, MAX(date_current_status) AS date_status " +
                    "FROM statushistory WHERE facility_id = " + facilityId + " AND date_current_status <= '" +
                    reportingDateEnd + "' GROUP BY patient_id";
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();

            query = "SELECT DISTINCT patient.patient_id, patient.gender, TIMESTAMPDIFF(YEAR, patient.date_birth, '" +
                    reportingDateBegin + "') AS age, patient.date_registration, patient.status_registration, " +
                    "patient.date_started, statushistory.current_status, currentstatus.date_status " +
                    "FROM patient JOIN statushistory ON patient.patient_id = statushistory.patient_id JOIN " +
                    "currentstatus ON patient.patient_id = currentstatus.patient_id WHERE patient.facility_id = " +
                    facilityId + " AND statushistory.facility_id = " + facilityId + " AND statushistory.patient_id = " +
                    "currentstatus.patient_id AND statushistory.date_current_status = currentstatus.date_status";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            // loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String dateRegistration = (resultSet.getDate("date_registration") == null) ? "" :
                        DateUtil.parseDateToString(resultSet.getDate("date_registration"),
                                "MM/dd/yyyy");
                String statusRegistration = resultSet.getString("status_registration");
                String currentStatus = resultSet.getString("current_status");
                String dateStatus = (resultSet.getDate("date_status") == null) ? "" :
                        DateUtil.parseDateToString(resultSet.getDate("date_status"), "MM/dd/yyyy");
                String dateStarted = (resultSet.getDate("date_started") == null) ? "" :
                        DateUtil.parseDateToString(resultSet.getDate("date_started"), "MM/dd/yyyy");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                if (statusRegistration.trim().equals("HIV+ non ART") && !dateRegistration.isEmpty()) {
                    if (Integer.parseInt(dateRegistration.substring(0, 2)) == reportingMonth &&
                            Integer.parseInt(dateRegistration.substring(6)) == reportingYear) {
                        disaggregate(gender, age, 0); //newly enrolled into care this reporting month excluding transfer in 
                    }
                    if (DateUtil.parseStringToDate(dateRegistration, "MM/dd/yyyy")
                            .before(DateUtil.getLastDateOfMonth(reportingYear, reportingMonth))) {
                        disaggregate(gender, age, 6); //cummulatievly enrolled into care this reporting month excluding transfer in                        
                    }
                }

                if (statusRegistration.trim().equals("Pre-ART Transfer In") && !dateRegistration.isEmpty()) {
                    if (Integer.parseInt(dateRegistration.substring(0, 2)) == reportingMonth &&
                            Integer.parseInt(dateRegistration.substring(6)) == reportingYear) {
                        disaggregate(gender, age, 11); //transfer in Pre-ART this reporting month 
                    }
                }

                if (currentStatus.trim().equals("Pre-ART Transfer Out") && !dateStatus.isEmpty()) {
                    if (Integer.parseInt(dateStatus.substring(0, 2)) == reportingMonth &&
                            Integer.parseInt(dateStatus.substring(6)) == reportingYear) {
                        disaggregate(gender, age, 17); //transfer out Pre-ART this reporting month 
                    }
                }

                if (currentStatus.trim().equals("Lost to Follow Up") && dateStarted.isEmpty() && !dateStatus.isEmpty()) {
                    if (Integer.parseInt(dateStatus.substring(0, 2)) == reportingMonth && Integer.parseInt(dateStatus.substring(6)) == reportingYear) {
                        disaggregate(gender, age, 23); //lost to follow up Pre-ART this reporting month 
                    }
                }

                if (currentStatus.trim().equals("Known Death") && dateStarted.isEmpty() && !dateStatus.isEmpty()) {
                    if (Integer.parseInt(dateStatus.substring(0, 2)) == reportingMonth && Integer.parseInt(dateStatus.substring(6)) == reportingYear) {
                        disaggregate(gender, age, 29); //known death Pre-ART this reporting month 
                    }
                }

                if (!dateStarted.isEmpty()) {
                    if (Integer.parseInt(dateStarted.substring(0, 2)) == reportingMonth && Integer.parseInt(dateStarted.substring(6)) == reportingYear) {
                        disaggregate(gender, age, 35); //started on ART this reporting month 
                    }
                }

                if (statusRegistration.trim().equals("ART Transfer In") && !dateRegistration.isEmpty()) {
                    if (Integer.parseInt(dateRegistration.substring(0, 2)) == reportingMonth && Integer.parseInt(dateRegistration.substring(6)) == reportingYear) {
                        disaggregate(gender, age, 39); //transfer in ART this reporting month 
                    }
                }

                if (currentStatus.trim().equals("Stopped Treatment") && !dateStarted.isEmpty() && !dateStatus.isEmpty()) {
                    if (Integer.parseInt(dateStatus.substring(0, 2)) == reportingMonth &&
                            Integer.parseInt(dateStatus.substring(6)) == reportingYear) {
                        disaggregate(gender, age, 45); //stopped ART this reporting month 
                    }
                }

                if (currentStatus.trim().equals("Lost to Follow Up") && !dateStarted.isEmpty() && !dateStatus.isEmpty()) {
                    if (Integer.parseInt(dateStatus.substring(0, 2)) == reportingMonth &&
                            Integer.parseInt(dateStatus.substring(6)) == reportingYear) {
                        disaggregate(gender, age, 51); //lost to follow up ART this reporting month 
                    }
                }

                if (currentStatus.trim().equals("Known Death") && !dateStarted.isEmpty() && !dateStatus.isEmpty()) {
                    if (Integer.parseInt(dateStatus.substring(0, 2)) == reportingMonth && Integer.parseInt(dateStatus.substring(6)) == reportingYear) {
                        disaggregate(gender, age, 57); //known death ART this reporting month 
                    }
                }

                if (currentStatus.trim().equals("ART Transfer Out") && !dateStatus.isEmpty()) {
                    if (Integer.parseInt(dateStatus.substring(0, 2)) == reportingMonth && Integer.parseInt(dateStatus.substring(6)) == reportingYear) {
                        disaggregate(gender, age, 63); //transfer out ART this reporting month 
                    }
                }

                if (currentStatus.trim().equals("ART Restart") && !dateStatus.isEmpty()) {
                    if (Integer.parseInt(dateStatus.substring(0, 2)) == reportingMonth && Integer.parseInt(dateStatus.substring(6)) == reportingYear) {
                        disaggregate(gender, age, 69); //restart ART this reporting month 
                    }
                }
            }

            query = "SELECT patient.patient_id, patient.gender, TIMESTAMPDIFF(YEAR, patient.date_birth, " +
                    "patient.date_registration) AS age, patient.status_registration, patient.date_registration, " +
                    "patient.date_started, clinic.date_visit, clinic.pregnant FROM patient JOIN clinic ON " +
                    "patient.patient_id = clinic.patient_id WHERE patient.facility_id = " + facilityId +
                    " AND clinic.facility_id = " + facilityId + " AND YEAR(clinic.date_visit) = " + reportingYear +
                    " AND MONTH(clinic.date_visit) = " + reportingMonth + " AND clinic.pregnant = 1";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String dateRegistration = (resultSet.getDate("date_registration") == null) ? "" :
                        DateUtil.parseDateToString(resultSet.getDate("date_registration"),
                                "MM/dd/yyyy");
                String statusRegistration = resultSet.getString("status_registration");
                String dateStarted = (resultSet.getDate("date_started") == null) ? "" :
                        DateUtil.parseDateToString(resultSet.getDate("date_started"), "MM/dd/yyyy");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                if (statusRegistration.trim().equals("HIV+ non ART") && !dateRegistration.isEmpty()) {
                    if (Integer.parseInt(dateRegistration.substring(0, 2)) == reportingMonth &&
                            Integer.parseInt(dateRegistration.substring(6)) == reportingYear) {
                        disaggregate(gender, age, 75); //HIV+ pregnant women newly enrolled 
                    }
                }

                if (!dateStarted.isEmpty()) {
                    if (Integer.parseInt(dateStarted.substring(0, 2)) == reportingMonth &&
                            Integer.parseInt(dateStarted.substring(6)) == reportingYear) {
                        disaggregate(gender, age, 79); //HIV+ pregnant women initiated on ART 
                    }
                }
            }

            // create a temporary table of date of the latest regimen change on or before the last day of reporting month 
            executeUpdate("DROP VIEW IF EXISTS currentregimen");
            query = " CREATE VIEW currentregimen AS SELECT DISTINCT patient_id, MAX(date_visit) AS date_visit FROM " +
                    "regimenhistory WHERE facility_id = " + facilityId + " AND date_visit <= '" +
                    reportingDateEnd + "' GROUP BY patient_id";
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();

            query = "SELECT DISTINCT patient.patient_id, patient.gender, TIMESTAMPDIFF(YEAR, patient.date_birth, '" +
                    reportingDateBegin + "') AS age, patient.date_registration, patient.date_started, " +
                    "regimenhistory.regimenType, currentregimen.date_visit FROM patient JOIN regimenhistory ON " +
                    "patient.patient_id = regimenhistory.patient_id JOIN currentregimen ON patient.patient_id = " +
                    "currentregimen.patient_id WHERE patient.facility_id = " + facilityId + " AND " +
                    "regimenhistory.facility_id = " + facilityId + " AND regimenhistory.patient_id = " +
                    "currentregimen.patient_id AND regimenhistory.date_visit = currentregimen.date_visit";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into array
            while (resultSet.next()) {
                String regimentype = resultSet.getString("regimenType");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                if (regimentype.contains("First Line")) {
                    disaggregate(gender, age, 85); //currently on 1st line
                }

                if (regimentype.contains("Second Line")) {
                    disaggregate(gender, age, 89); //currently on 2nd line
                }

                if (regimentype.contains("Salvage")) {
                    disaggregate(gender, age, 95); //currently on Salvage ARV
                }
            }

            query = "SELECT DISTINCT patient.patient_id, patient.gender, TIMESTAMPDIFF(YEAR, patient.date_birth, '" +
                    reportingDateBegin + "') AS age, patient.date_started, clinic.pregnant, clinic.tb_status, " +
                    "clinic.date_visit FROM patient JOIN clinic ON patient.patient_id = clinic.patient_id WHERE " +
                    "patient.facility_id = " + facilityId + " AND clinic.facility_id = " + facilityId +
                    " AND YEAR(clinic.date_visit) = " + reportingYear + " AND MONTH(clinic.date_visit) = " +
                    reportingMonth;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into array
            while (resultSet.next()) {
                String tbStatus = resultSet.getString("tb_status");
                String dateStarted = (resultSet.getDate("date_started") == null) ? "" : DateUtil.parseDateToString(resultSet.getDate("date_started"), "MM/dd/yyyy");
                int pregnant = resultSet.getInt("pregnant");
                if (!tbStatus.isEmpty()) {
                    if (!dateStarted.isEmpty()) {
                        indicator[96][0]++;  //screened for TB ART
                    } else {
                        indicator[97][0]++;  //screened for TB Pre-ART
                    }
                }
                if (tbStatus.trim().equals("Currently on TB treatment")) {
                    if (!dateStarted.isEmpty()) {
                        indicator[98][0]++;  //commenced on TB ART
                    } else {
                        indicator[99][0]++;  //commenced on TB Pre-ART
                    }
                }
                if (tbStatus.trim().equals("Currently on INH prophylaxis")) {
                    if (!dateStarted.isEmpty()) {
                        indicator[100][0]++;  //on INH prophylaxis ART
                    } else {
                        indicator[101][0]++;  //on INH prophylaxis Pre-ART
                    }
                }
                if (pregnant == 1 && !dateStarted.isEmpty()) {
                    if (Integer.parseInt(dateStarted.substring(0, 2)) == reportingMonth &&
                            Integer.parseInt(dateStarted.substring(6)) == reportingYear) {
                        indicator[102][0]++;  //pregnant women newly enrolled into ART
                    }
                }
            }

            query = "SELECT DISTINCT facility.name, lga.name AS lga, state.name AS state FROM facility JOIN lga ON " +
                    "facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE facility_id = " +
                    facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                facility = resultSet.getString("name");
                lga = resultSet.getString("lga");
                state = resultSet.getString("state");
            }
            resultSet = null;
        } catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private void disaggregate(String gender, int age, int i) {
        if (gender.trim().equals("Male")) {
            if (age < 1) {
                indicator[i + 0][0]++;  //males < 1yr
            } else {
                if (age >= 1 && age < 15) {
                    indicator[i + 1][0]++;  //males 1-14yrs
                } else {
                    indicator[i + 2][0]++;  //males => 15yrs
                }
            }
        } else {
            if (age < 1) {
                indicator[i + 3][0]++;  //fmales < 1yr
            } else {
                if (age >= 1 && age < 15) {
                    indicator[i + 4][0]++;  //fmales 1-14yrs
                } else {
                    indicator[i + 5][0]++;  //fmales => 15yrs
                }
            }
        }
    }

    private void saveValue() {
        executeUpdate("DROP VIEW IF EXISTS indicator");
        executeUpdate(" CREATE VIEW indicator (state VARCHAR(45), lga VARCHAR(45), org_unit VARCHAR(100), " +
                "data_element VARCHAR(200), category VARCHAR(100), value INT, script_id INT)");
        executeUpdate("INSERT INTO indicator (data_element, category, script_id) SELECT data_element, category," +
                " script_id FROM datasystem");
        for (int i = 0; i < indicator.length; i++) {
            int scriptId = indicator[i][0];
            int dataValue = indicator[i][1];
            query = "UPDATE indicator SET state = '" + state + "', lga = '" + lga + "', org_unit = '" + facility +
                    "', value = " + dataValue + " WHERE script_id = " + scriptId;
            executeUpdate(query);
        }
        new DataInstanceResolver().resolveInstance();
    }

    private void clearValue() {
        for (int i = 0; i < indicator.length; i++) {
            indicator[i][1] = 0;
        }
    }

    private void executeUpdate(String query) {
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

}
