/**
 * @author AALOZIE
 */

package org.fhi360.lamis.report;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.controller.report.ReportParameterDTO;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

@Component
public class PatientReports {
    private String reportTitle;
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private final JdbcTemplate jdbcTemplate;

    public PatientReports(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> listOfPatients(ReportParameterDTO dto, Long facilityId) {
        reportTitle = "List of all Patients";
        query = "SELECT patient.*, DATEDIFF(YEAR, date_birth, CURDATE()) AS age FROM patient WHERE " +
                "facility_id = " + facilityId;

        if (!StringUtils.isBlank(dto.getGender()) && !StringUtils.equals(dto.getGender(), "--All--"))
            query += " AND gender = '" + dto.getGender() + "'";
        if (!StringUtils.isBlank(dto.getAgeBegin()) && !StringUtils.isBlank(dto.getAgeEnd()))
            query += " AND age >= " + dto.getAgeBegin() + " AND age <= " + dto.getAgeEnd();
        if (StringUtils.isNotBlank(dto.getState()))
            query += " AND state = '" + dto.getState() + "'";
        if (StringUtils.isNotBlank(dto.getLga())) {
            query += " AND lga = '" + dto.getLga() + "'";
        }
        if (StringUtils.isNotBlank(dto.getEntryPoint()))
            query += " AND entry_point = '" + dto.getEntryPoint() + "'";

        if (StringUtils.isBlank(dto.getCurrentStatus()) && !StringUtils.equals(dto.getCurrentStatus(), "--All--")) {
            String currentStatus = (dto.getCurrentStatus().equals("HIV  non ART")) ? "HIV+ non ART" : dto.getCurrentStatus();
            if (currentStatus.equals("Currently Active")) {
                query += " AND current_status IN (" + Constants.ClientStatus.ON_TREATMENT + ") AND DATEDIFF(DAY, " +
                        "date_last_refill + last_refill_duration, CURDATE()) <= " + Constants.LTFU.PEPFAR +
                        " AND date_started IS NOT NULL";
            } else {
                query += " AND current_status = '" + currentStatus + "'";
            }
        }

        if (StringUtils.isNotBlank(dto.getDateCurrentStatusBegin()) && StringUtils.isNotBlank(dto.getDateCurrentStatusEnd()))
            query += " AND date_current_status >= '" + DateUtil.parseStringToSqlDate(dto.getDateCurrentStatusBegin(),
                    "MM/dd/yyyy") + "' AND date_current_status <= '" +
                    DateUtil.parseStringToSqlDate(dto.getDateCurrentStatusEnd(), "MM/dd/yyyy") + "'";
        if (StringUtils.isNotBlank(dto.getRegimentype()))
            query += " AND regimentype = '" + dto.getRegimentype() + "'";
        if (StringUtils.isNotBlank(dto.getDateRegistrationBegin()) && StringUtils.isNotBlank(dto.getDateRegistrationEnd()))
            query += " AND date_registration >= '" + DateUtil.parseStringToSqlDate(dto.getDateRegistrationBegin(),
                    "MM/dd/yyyy") + "' AND date_registration <= '" +
                    DateUtil.parseStringToSqlDate(dto.getDateRegistrationEnd(), "MM/dd/yyyy") + "'";
        if (StringUtils.isNotBlank(dto.getArtStartDateBegin()) && StringUtils.isNotBlank(dto.getArtStartDateEnd()))
            query += " AND date_started >= '" + DateUtil.parseStringToSqlDate(dto.getArtStartDateBegin(),
                    "MM/dd/yyyy") + "' AND date_started <= '" +
                    DateUtil.parseStringToSqlDate(dto.getArtStartDateEnd(), "MM/dd/yyyy") + "'";
        if (StringUtils.isNotBlank(dto.getClinicStage()))
            query += " AND last_clinic_stage = '" + dto.getClinicStage() + "'";
        if (StringUtils.isNotBlank(dto.getCd4Begin()) && StringUtils.isNotBlank(dto.getCd4End()))
            query += " AND last_cd4 >= " + Double.parseDouble(dto.getCd4Begin()) + " AND last_cd4 <= " +
                    Double.parseDouble(dto.getCd4End());
        if (StringUtils.isNotBlank(dto.getViralloadBegin()) && StringUtils.isNotBlank(dto.getViralloadEnd()))
            query += " AND last_viral_load >= " + Double.parseDouble(dto.getViralloadBegin()) +
                    " AND last_viral_load <= " + Double.parseDouble(dto.getViralloadEnd());
        query += " ORDER BY current_status";
        System.out.println(query);

        return generateReportList(query);
    }

    public List<Map<String, Object>> listOfPatientsNotification(ReportParameterDTO dto, Long facilityId) {
        switch (dto.getEntity()) {
            case 1:
                reportTitle = "List of clients enrolled but not on treatment";
                query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth," +
                        " DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, state, lga, " +
                        "date_registration, status_registration, current_status, date_current_status, " +
                        "date_started, date_last_cd4, last_cd4, last_cd4p, date_last_viral_load, " +
                        "last_viral_load, date_last_refill, last_refill_duration FROM patient WHERE " +
                        "facility_id = " + facilityId + " AND current_status IN ('HIV+ non ART', 'ART Start', " +
                        "'ART Restart', 'ART Transfer In', 'Pre-ART Transfer In') AND date_started IS NULL";
                break;

            case 2:
                reportTitle = "List of clients who are lost to follow-up unconfirmed";
                query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth," +
                        " DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, state, lga, " +
                        "date_registration, status_registration, current_status, date_current_status, " +
                        "date_started, date_last_cd4, last_cd4, last_cd4p, date_last_viral_load, " +
                        "last_viral_load, date_last_refill, last_refill_duration FROM patient WHERE " +
                        "facility_id = " + facilityId + " AND current_status IN ('ART Start', 'ART Restart', " +
                        "'ART Transfer In') AND DATEDIFF(DAY, date_last_refill + last_refill_duration," +
                        " CURDATE()) > " + Constants.LTFU.PEPFAR + " AND date_started IS NOT NULL";
                break;

//            case 3:
//                reportTitle = "List of clients on treatment but no first ARV dispensed";
//             query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, state, lga, date_registration, status_registration, current_status, date_current_status, date_started FROM patient WHERE facility_id = " + id + " AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND date_last_refill IS NULL ";
//            break;

            case 3:
                reportTitle = "List of clients on treatment who are due for viral load test";
                query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, " +
                        "DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, state, lga, date_registration," +
                        " status_registration, current_status, date_current_status, date_started, date_last_cd4, " +
                        "last_cd4, last_cd4p, date_last_viral_load, last_viral_load, date_last_refill, " +
                        "last_refill_duration FROM patient WHERE facility_id = " + facilityId +
                        " AND viral_load_due_date <= CURDATE()";
                break;

            case 4:
                reportTitle = "List of clients on treatment with viral load un-suppressed.";
                query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, " +
                        "DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, state, lga, date_registration," +
                        " status_registration, current_status, date_current_status, date_started, date_last_cd4, " +
                        "last_cd4, last_cd4p, date_last_viral_load, last_viral_load, date_last_refill, " +
                        "last_refill_duration FROM patient WHERE facility_id = " + facilityId +
                        " AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND " +
                        "last_viral_load >=1000";
                break;
        }
        query += " ORDER BY patient.current_status";

        System.out.println("The query is: " + query);

        return generateReportList(query);
    }

    public List<Map<String, Object>> caseManagerClientsList(Long casemanagerId, Long facilityId) {
        JDBCUtil internalJdbcUtil = null;
        String casemanagerName = "";

        String caseManagerQuery = "SELECT fullname from casemanager WHERE casemanager_id = " + casemanagerId;
        try {
            internalJdbcUtil = new JDBCUtil();
            PreparedStatement preparedStatement = internalJdbcUtil.getStatement(caseManagerQuery);
            ResultSet internalResultSet = preparedStatement.executeQuery();

            while (internalResultSet.next()) {
                casemanagerName = internalResultSet.getString("fullname");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            internalJdbcUtil.disconnectFromDatabase();
        }

        reportTitle = "List of clients assigned to " + casemanagerName;
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, " +
                "DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, state, lga, date_registration, " +
                "status_registration, current_status, date_current_status, date_started, date_last_cd4, " +
                "last_cd4, last_cd4p, date_last_viral_load, last_viral_load, date_last_refill, " +
                "last_refill_duration FROM patient WHERE facility_id = " + facilityId +
                " AND casemanager_id = " + casemanagerId;
        query += " ORDER BY current_status";

        //System.out.println("The query is: "+query);

        return generateReportList(query);
    }


    public List<Map<String, Object>> currentOnCare(Long facilityId) {
        reportTitle = "Patients Currently on Care (ART & Pre-ART)";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, " +
                "DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, state, lga, date_registration, " +
                "status_registration, current_status, date_current_status, date_started, date_last_cd4, " +
                "last_cd4, last_cd4p, date_last_viral_load, last_viral_load, date_last_refill, " +
                "last_refill_duration FROM patient WHERE facility_id = " + facilityId +
                " AND current_status IN (" + Constants.ClientStatus.ON_CARE + ") ORDER BY current_status";
        return generateReportList(query);
    }

    public List<Map<String, Object>> currentOnTreatment(Long facilityId) {
        reportTitle = "Patients Currently on Treatment (ART)";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, " +
                "DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, state, lga, date_registration," +
                " status_registration, current_status, date_current_status, date_started, date_last_cd4, " +
                "last_cd4, last_cd4p, date_last_viral_load, last_viral_load, date_last_refill, " +
                "last_refill_duration FROM patient WHERE facility_id = " + facilityId +
                " AND current_status IN (" + Constants.ClientStatus.ON_TREATMENT + ") AND DATEDIFF(DAY, " +
                "date_last_refill + last_refill_duration, CURDATE()) <= " + Constants.LTFU.GON +
                " AND date_started IS NOT NULL";
        return generateReportList(query);
    }

    public List<Map<String, Object>> lostUnconfirmedPEPFAR(Long facilityId) {
        reportTitle = "Patients Lost to Follow Up Unconfirmed - PEPFAR";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, " +
                "DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, state, lga, date_registration, " +
                "status_registration, current_status, date_current_status, date_started, date_last_cd4, " +
                "last_cd4, last_cd4p, date_last_viral_load, last_viral_load, date_last_refill, " +
                "last_refill_duration FROM patient WHERE facility_id = " + facilityId +
                " AND current_status IN (" + Constants.ClientStatus.ON_TREATMENT + ") AND DATEDIFF(DAY, " +
                "date_last_refill + last_refill_duration, CURDATE()) > " + Constants.LTFU.PEPFAR +
                " AND date_started IS NOT NULL ORDER BY current_status";
        return generateReportList(query);
    }

    public List<Map<String, Object>> lostUnconfirmedGON(Long facilityId) {
        reportTitle = "Patients Lost to Follow Up Unconfirmed - GON";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, " +
                "DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, state, lga, date_registration, " +
                "status_registration, current_status, date_current_status, date_started, date_last_cd4, " +
                "last_cd4, last_cd4p, date_last_viral_load, last_viral_load, date_last_refill, " +
                "last_refill_duration FROM patient WHERE facility_id = " + facilityId +
                " AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND " +
                "DATEDIFF(DAY, date_last_refill + last_refill_duration, CURDATE()) > " +
                Constants.LTFU.GON + " AND date_started IS NOT NULL ORDER BY current_status";
        return generateReportList(query);
    }

    public List<Map<String, Object>> appointment(String reportType, String reportingDateBegin, String reportingDateEnd,
                                                 Long facilityId) {
        reportingDateBegin = DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd");
        reportingDateEnd = DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd");

        if (reportType.equals("1")) {
            reportTitle = "Patients for Refill Appointment From " + reportingDateBegin + " To " + reportingDateEnd;
            query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR," +
                    " date_birth, CURDATE()) AS age, phone, address, date_next_refill AS date_visit, current_status " +
                    "FROM patient WHERE facility_id = " + facilityId + " AND current_status NOT IN ('HIV+ non ART', " +
                    "'Pre-ART Transfer Out', 'ART Transfer Out', 'Known Death') AND date_next_refill >= '" +
                    reportingDateBegin + "' AND date_next_refill <= '" + reportingDateEnd + "'";
        }
        if (reportType.equals("2")) {
            reportTitle = "Patients for Clinic Appointment From " + reportingDateBegin + " To " + reportingDateEnd;
            query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR," +
                    " date_birth, CURDATE()) AS age, phone, address, date_next_clinic AS date_visit, current_status " +
                    "FROM patient WHERE facility_id = " + facilityId + " AND current_status NOT IN " +
                    "('Pre-ART Transfer Out', 'ART Transfer Out', 'Known Death') AND date_next_clinic >= '" +
                    reportingDateBegin + "' AND date_next_clinic <= '" + reportingDateEnd + "'";
        }
        if (reportType.equals("3")) {
            reportTitle = "Patients for Tracking 'Return' Appointment From " + reportingDateBegin + " To " + reportingDateEnd;
            query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, " +
                    "date_birth, CURDATE()) AS age, phone, address, agreed_date AS date_visit, current_status FROM " +
                    "patient WHERE facility_id = " + facilityId + " AND current_status NOT IN ('Pre-ART Transfer Out', " +
                    "'ART Transfer Out', 'Lost to Follow Up', 'Stopped Treatment', 'Known Death') AND agreed_date >= '" +
                    reportingDateBegin + "' AND agreed_date <= '" + reportingDateEnd + "'";
        }

        return generateReportList(query);
    }

    public List<Map<String, Object>> clientAppointment(Long casemanagerId, String reportingDateBegin, String reportingDateEnd,
                                                            Long facilityId)  {
        reportingDateBegin = DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd");
        reportingDateEnd = DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd");
        JDBCUtil internalJdbcUtil = null;
        String casemanagerName = "";

        String caseManagerQuery = "SELECT fullname from casemanager WHERE casemanager_id = " + casemanagerId;
        try {
            internalJdbcUtil = new JDBCUtil();
            PreparedStatement preparedStatement = internalJdbcUtil.getStatement(caseManagerQuery);
            ResultSet internalResultSet = preparedStatement.executeQuery();

            while (internalResultSet.next()) {
                casemanagerName = internalResultSet.getString("fullname");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            internalJdbcUtil.disconnectFromDatabase();
        }

        reportTitle = casemanagerName + "'s Clients Due For Refill Appointment From " + reportingDateBegin + " To " + reportingDateEnd;
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, " +
                "date_birth, CURDATE()) AS age, phone, address, date_next_refill AS date_visit, current_status FROM " +
                "patient WHERE facility_id = " + facilityId + " AND casemanager_id = " + casemanagerId +
                "AND date_next_refill IS NOT NULL AND date_next_refill >= '" + reportingDateBegin +
                "' AND date_next_refill <= '" + reportingDateEnd + "'";

        return generateReportList(query);
    }

    public List<Map<String, Object>> visit(String reportType, String reportingDateBegin, String reportingDateEnd,
                                                Long facilityId)  {
        reportingDateBegin = DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd");
        reportingDateEnd = DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd");

        if (reportType.equals("4")) {
            reportTitle = "Patients Refill Visit From " + reportingDateBegin + " To " + reportingDateEnd;
            query = "SELECT DISTINCT pharmacy.patient_id, pharmacy.facility_id, pharmacy.date_visit, pharmacy.regimentype_id, " +
                    "pharmacy.regimen_id, pharmacy.duration, pharmacy.next_appointment, patient.hospital_num, patient.surname, " +
                    "patient.other_names, patient.gender, DATEDIFF(YEAR, patient.date_birth, CURDATE()) AS age, " +
                    "patient.phone, patient.address, patient.current_status, regimentype.description AS regimentype, " +
                    "regimen.description AS regimen "
                    + " FROM pharmacy JOIN patient ON pharmacy.patient_id = patient.patient_id JOIN regimentype ON " +
                    "pharmacy.regimentype_id = regimentype.regimentype_id JOIN regimen ON pharmacy.regimen_id = " +
                    "regimen.regimen_id WHERE pharmacy.regimentype_id IN (1, 2, 3, 4, 14) AND pharmacy.facility_id = " +
                    facilityId + " AND pharmacy.date_visit >= '" + reportingDateBegin + "' AND pharmacy.date_visit <= '" +
                    reportingDateEnd + "'";
        }

        if (reportType.equals("5")) {
            reportTitle = "Patients Clinic Visit From " + reportingDateBegin + " To " + reportingDateEnd;
            query = "SELECT DISTINCT clinic.patient_id, clinic.facility_id, clinic.date_visit, clinic.clinic_stage," +
                    " clinic.tb_status, clinic.next_appointment, patient.hospital_num, patient.surname, " +
                    "patient.other_names, patient.gender, DATEDIFF(YEAR, patient.date_birth, CURDATE()) AS age, " +
                    "patient.phone, patient.address, patient.current_status FROM clinic JOIN patient ON " +
                    "clinic.patient_id = patient.patient_id WHERE clinic.facility_id = " + facilityId +
                    " AND clinic.date_visit >= '" + reportingDateBegin + "' AND clinic.date_visit <= '" +
                    reportingDateEnd + "'";
        }

        return generateReportList(query);
    }

    public List<Map<String, Object>> defaulters(String reportType, String reportingDateBegin, String reportingDateEnd,
                                                     Long facilityId)  {
        reportingDateBegin = DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd");
        reportingDateEnd = DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd");

        if (reportType.equals("6")) {
            reportTitle = "List of Missed Refill Appointment (defaulters)";

            //Retrieve all refill appointments for the period 
            executeUpdate("DROP TABLE IF EXISTS schedule");
            query = "CREATE TEMPORARY TABLE schedule AS SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, current_status, date_current_status, date_last_refill, date_last_clinic, date_next_clinic, date_next_refill FROM patient WHERE facility_id = " + facilityId + " AND date_next_refill >= '" + reportingDateBegin + "' AND date_next_refill <= '" + reportingDateEnd + "'";
            executeUpdate(query);

            //query = "CREATE TEMPORARY TABLE visit AS SELECT DISTINCT patient_id FROM clinic WHERE facility_id = " + id + " AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "' UNION  SELECT DISTINCT patient_id FROM pharmacy WHERE facility_id = " + id + " AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "'";
            //Retrieve all refill visits for the period
            executeUpdate("DROP TABLE IF EXISTS visit");
            query = "CREATE TEMPORARY TABLE visit AS SELECT DISTINCT patient_id FROM pharmacy WHERE facility_id = " + facilityId + " AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "'";
            executeUpdate(query);
        }

        if (reportType.equals("7")) {
            reportTitle = "List of Missed Clinic Appointment (Defaulters)";

            //Retrieve all refill appointments for the period 
            executeUpdate("DROP TABLE IF EXISTS schedule");
            query = "CREATE TEMPORARY TABLE schedule AS SELECT DISTINCT patient_id, hospital_num, surname, other_names," +
                    " gender, date_birth, DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, current_status," +
                    " date_current_status, date_last_refill, date_last_clinic, date_next_clinic, date_next_refill FROM " +
                    "patient WHERE facility_id = " + facilityId + " AND date_next_clinic >= '" + reportingDateBegin +
                    "' AND date_next_clinic <= '" + reportingDateEnd + "'";
            executeUpdate(query);

            //Retrieve all refill visits for the period
            executeUpdate("DROP TABLE IF EXISTS visit");
            query = "CREATE TEMPORARY TABLE visit AS SELECT DISTINCT patient_id FROM clinic WHERE facility_id = " +
                    facilityId + " AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "'";
            executeUpdate(query);
        }

        if (reportType.equals("8")) {
            reportTitle = "List of Missed Tracking 'Return' Appointment (based on agreed date of return)";

            //Retrieve all tracking 'return' appointments for the period 
            executeUpdate("DROP TABLE IF EXISTS schedule");
            query = "CREATE TEMPORARY TABLE schedule AS SELECT DISTINCT patient_id, hospital_num, surname, other_names, " +
                    "gender, date_birth, DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, current_status, " +
                    "date_current_status, date_last_refill, date_last_clinic, date_next_clinic, date_next_refill FROM " +
                    "patient WHERE facility_id = " + facilityId + " AND agreed_date >= '" + reportingDateBegin +
                    "' AND agreed_date <= '" + reportingDateEnd + "'";
            executeUpdate(query);

            //Retrieve all refill or clinic visits for the period
            executeUpdate("DROP TABLE IF EXISTS visit");
            query = "CREATE TEMPORARY TABLE visit AS SELECT DISTINCT patient_id FROM clinic WHERE facility_id = " +
                    facilityId + " AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" +
                    reportingDateEnd + "' UNION SELECT DISTINCT patient_id FROM pharmacy WHERE facility_id = " +
                    facilityId + " AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" +
                    reportingDateEnd + "'";
            executeUpdate(query);
        }

        //Retrieve patients in schedule but not in visit 
        query = "SELECT * FROM schedule WHERE patient_id NOT IN (SELECT DISTINCT patient_id FROM visit)";
        return generateReportList(query);
    }

    public List<Map<String, Object>> defaulterRefill(Long facilityId) {
        reportTitle = "List of Defaulters for ARV Refill";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, " +
                "date_birth, CURDATE()) AS age, phone, address, current_status, date_current_status, date_last_refill, " +
                "date_last_clinic, date_next_clinic AS date_visit FROM patient WHERE facility_id = " + facilityId +
                " AND ((current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND DATEDIFF(DAY, " +
                "date_next_refill, CURDATE()) >= 1) OR (current_status IN ('Lost to Follow Up', 'Stopped Treatment') " +
                "AND DATEDIFF(DAY, agreed_date, CURDATE()) >= 1)) AND date_started IS NOT NULL ORDER BY current_status";
        return generateReportList(query);
    }

    public List<Map<String, Object>> trackingOutcome(String outcome, Long facilityId) {
        reportTitle = "List of TX-ML Patients";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, " +
                "date_birth, CURDATE()) AS age, phone, address, current_status, date_current_status, date_last_refill," +
                " date_last_clinic, date_next_clinic AS date_visit, outcome, date_tracked, agreed_date, cause_death " +
                "FROM patient WHERE facility_id = " + facilityId + " AND outcome = '" + outcome +
                "' ORDER BY current_status";
        if (outcome.equalsIgnoreCase("All"))
            query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR," +
                    " date_birth, CURDATE()) AS age, phone, address, current_status, date_current_status, " +
                    "date_last_refill, date_last_clinic, date_next_clinic AS date_visit, outcome, date_tracked, " +
                    "agreed_date, cause_death FROM patient WHERE facility_id = " + facilityId + " AND outcome IN ('" +
                    Constants.TxMlStatus.TX_ML_DIED + "', '" + Constants.TxMlStatus.TX_ML_TRANSFER + "', '" +
                    Constants.TxMlStatus.TX_ML_TRACED + "', '" + Constants.TxMlStatus.TX_ML_NOT_TRACED + "') " +
                    "ORDER BY current_status";
        if (outcome.equalsIgnoreCase(Constants.TxMlStatus.TX_ML_NOT_TRACED))
            query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR," +
                    " date_birth, CURDATE()) AS age, phone, address, current_status, date_current_status," +
                    " date_last_refill, date_last_clinic, date_next_clinic AS date_visit, outcome, date_tracked, " +
                    "agreed_date, cause_death FROM patient WHERE facility_id = " + facilityId + "" +
                    " AND ((current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND DATEDIFF(DAY, " +
                    "date_next_refill, CURDATE()) >= 1) OR (current_status IN ('Lost to Follow Up', " +
                    "'Stopped Treatment') AND DATEDIFF(DAY, agreed_date, CURDATE()) >= 1)) AND date_started " +
                    "IS NOT NULL AND (outcome IS NULL OR outcome = '') ORDER BY current_status";
        return generateReportList(query);
    }

    public List<Map<String, Object>> clientDefaulterRefill(Long casemanagerId, String reportingDateBegin, String reportingDateEnd,
                                                           String reportType, Long facilityId)  {
        reportingDateBegin = DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd");
        reportingDateEnd = DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd");

        JDBCUtil internalJdbcUtil = null;
        String casemanagerName = "";

        String caseManagerQuery = "SELECT fullname from casemanager WHERE casemanager_id = " + casemanagerId;
        try {
            internalJdbcUtil = new JDBCUtil();
            PreparedStatement preparedStatement = internalJdbcUtil.getStatement(caseManagerQuery);
            ResultSet internalResultSet = preparedStatement.executeQuery();

            while (internalResultSet.next()) {
                casemanagerName = internalResultSet.getString("fullname");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            internalJdbcUtil.disconnectFromDatabase();
        }

        if (reportType.equals("6")) {
            reportTitle = "List of Missed Refill Appointment (defaulters) for Case Manager :" + casemanagerName;

            //Retrieve all refill appointments for the period 
            executeUpdate("DROP TABLE IF EXISTS schedule");
            query = "CREATE TEMPORARY TABLE schedule AS SELECT DISTINCT patient_id, hospital_num, surname, casemanager_id, " +
                    "other_names, gender, date_birth, DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, " +
                    "current_status, date_current_status, date_last_refill, date_last_clinic, date_next_clinic, " +
                    "date_next_refill FROM patient WHERE facility_id = " + facilityId + " AND date_next_refill >= '" +
                    reportingDateBegin + "' AND date_next_refill <= '" + reportingDateEnd + "' AND casemanager_id = " +
                    casemanagerId + "";
            executeUpdate(query);

            //query = "CREATE TEMPORARY TABLE visit AS SELECT DISTINCT patient_id FROM clinic WHERE facility_id = " + id + " AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "' UNION  SELECT DISTINCT patient_id FROM pharmacy WHERE facility_id = " + id + " AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "'";
            //Retrieve all refill visits for the period
            executeUpdate("DROP TABLE IF EXISTS visit");
            query = "CREATE TEMPORARY TABLE visit AS SELECT DISTINCT patient_id FROM pharmacy WHERE facility_id = " +
                    facilityId + " AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" +
                    reportingDateEnd + "'";
            executeUpdate(query);
        }

        if (reportType.equals("7")) {
            reportTitle = "List of Missed Clinic Appointment (Defaulters) for Case Manager :" + casemanagerName;

            //Retrieve all refill appointments for the period 
            executeUpdate("DROP TABLE IF EXISTS schedule");
            query = "CREATE TEMPORARY TABLE schedule AS SELECT DISTINCT patient_id, hospital_num, surname," +
                    " other_names, gender, casemanager_id, date_birth, DATEDIFF(YEAR, date_birth, CURDATE()) AS age, " +
                    "phone, address, current_status, date_current_status, date_last_refill, date_last_clinic, " +
                    "date_next_clinic, date_next_refill FROM patient WHERE facility_id = " + facilityId +
                    " AND date_next_clinic >= '" + reportingDateBegin + "' AND date_next_clinic <= '" +
                    reportingDateEnd + "'  AND casemanager_id = " + casemanagerId + "";
            executeUpdate(query);

            //Retrieve all refill visits for the period
            executeUpdate("DROP TABLE IF EXISTS visit");
            query = "CREATE TEMPORARY TABLE visit AS SELECT DISTINCT patient_id FROM clinic WHERE facility_id = " +
                    facilityId + " AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" +
                    reportingDateEnd + "'";
            executeUpdate(query);
        }

        if (reportType.equals("8")) {
            reportTitle = "List of Missed Tracking 'Return' Appointment (based on agreed date of return ) for Case Manager :" + casemanagerName;

            //Retrieve all tracking 'return' appointments for the period 
            executeUpdate("DROP TABLE IF EXISTS schedule");
            query = "CREATE TEMPORARY TABLE schedule AS SELECT DISTINCT patient_id, hospital_num, surname," +
                    "other_names, gender, date_birth, casemanager_id DATEDIFF(YEAR, date_birth, CURDATE()) AS age," +
                    " phone, address, current_status, date_current_status, date_last_refill, date_last_clinic, " +
                    "date_next_clinic, date_next_refill FROM patient WHERE facility_id = " + facilityId +
                    " AND agreed_date >= '" + reportingDateBegin + "' AND agreed_date <= '" + reportingDateEnd +
                    "' AND casemanager_id = " + casemanagerId + "";
            executeUpdate(query);

            //Retrieve all refill or clinic visits for the period
            executeUpdate("DROP TABLE IF EXISTS visit");
            query = "CREATE TEMPORARY TABLE visit AS SELECT DISTINCT patient_id FROM clinic WHERE facility_id = " +
                    facilityId + " AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" +
                    reportingDateEnd + "' UNION SELECT DISTINCT patient_id FROM pharmacy WHERE facility_id = " +
                    facilityId + " AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" +
                    reportingDateEnd + "'";
            executeUpdate(query);
        }

        //Retrieve patients in schedule but not in visit 
        query = "SELECT * FROM schedule WHERE patient_id NOT IN (SELECT DISTINCT patient_id FROM visit)";
        return generateReportList(query);
    }

    //One day defualters...
//    public ArrayList<Map<String, Object>> clientDefaulterRefill(){
//        
//        
//        reportTitle = "List of Defaulters for ARV Refill for Case Manager :"  +casemanagerName;
//        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, current_status, date_current_status, date_last_refill, date_last_clinic, date_next_clinic AS date_visit FROM patient WHERE facility_id = " + id + " AND ((current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND DATEDIFF(DAY, date_next_refill, CURDATE()) >= 1) OR (current_status IN ('Lost to Follow Up', 'Stopped Treatment') AND DATEDIFF(DAY, agreed_date, CURDATE()) >= 1)) AND date_started IS NOT NULL AND casemanager_id = "+casemanagerId+" ORDER BY current_status";
//        generateReportList(query);
//        return reportList;
//    }

    public List<Map<String, Object>> lostFollowUp(Long facilityId) {
        reportTitle = "List of Patients Lost to Followup";
        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, " +
                "DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, current_status, " +
                "date_current_status, date_last_refill, date_last_clinic, date_next_clinic AS date_visit " +
                "FROM patient WHERE facility_id = " + facilityId + " AND current_status = 'Lost to Follow Up'  ORDER BY current_status";
        return generateReportList(query);
    }

    public List<Map<String, Object>> coInfected(Long facilityId) {
        reportTitle = "List of TB-HIV co-infected Patients";
        //started on TB treatment -> select all patients whose tb status at last visit is Currently on TB treatment
        executeUpdate("DROP TABLE IF EXISTS tb");
        query = "CREATE TEMPORARY TABLE tb AS SELECT DISTINCT patient_id, MAX(date_visit) FROM clinic WHERE " +
                "facility_id = " + facilityId + " AND tb_status = 'Currently on TB treatment' GROUP BY " +
                "patient_id";
        executeUpdate(query);

        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, state, lga, date_registration, status_registration, current_status, date_current_status, date_started FROM patient WHERE patient_id IN (SELECT patient_id FROM tb) ORDER BY current_status";
        return generateReportList(query);
    }

    public List<Map<String, Object>> patientsRegimen(String regimenType, Long facilityId) {
        reportTitle = "Patients on First line Regimen";
        executeUpdate("DROP TABLE IF EXISTS original");
        query = "CREATE TEMPORARY TABLE original AS SELECT DISTINCT patient_id, regimen FROM clinic WHERE " +
                "facility_id = " + facilityId + " AND commence = 1";
        executeUpdate(query);
        if (regimenType.equals("first")) {
            reportTitle = "Patients on First line Regimen";
            query = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, " +
                    "patient.other_names, patient.gender, patient.date_birth, DATEDIFF(YEAR, " +
                    "patient.date_birth, CURDATE()) AS age, patient.phone, patient.address, " +
                    "patient.date_started, patient.regimentype, patient.regimen, original.regimen AS " +
                    "original_regimen FROM patient LEFT OUTER JOIN original ON patient.patient_id = original.patient_id " +
                    "WHERE patient.facility_id = " + facilityId + " AND patient.current_status IN " +
                    "('ART Start', 'ART Restart', 'ART Transfer In') AND patient.regimentype IN " +
                    "(SELECT description FROM regimentype WHERE regimentype_id = 1 OR regimentype_id = 3)";
        }
        if (regimenType.equals("second")) {
            reportTitle = "Patients on Second Therapy";
            query = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, " +
                    "patient.other_names, patient.gender, patient.date_birth, DATEDIFF(YEAR, " +
                    "patient.date_birth, CURDATE()) AS age, patient.phone, patient.address, " +
                    "patient.date_started, patient.regimentype, patient.regimen, original.regimen AS " +
                    "original_regimen "
                    + " FROM patient LEFT OUTER JOIN original ON patient.patient_id = original.patient_id " +
                    "WHERE patient.facility_id = " + facilityId + " AND patient.current_status IN " +
                    "('ART Start', 'ART Restart', 'ART Transfer In') AND patient.regimentype IN " +
                    "(SELECT description FROM regimentype WHERE regimentype_id = 2 OR regimentype_id = 4)";
        }
        if (regimenType.equals("third")) {
            reportTitle = "Patients on Third Line Regimen";
            query = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, " +
                    "patient.other_names, patient.gender, patient.date_birth, DATEDIFF(YEAR, " +
                    "patient.date_birth, CURDATE()) AS age, patient.phone, patient.address, " +
                    "patient.date_started, patient.regimentype, patient.regimen, original.regimen AS " +
                    "original_regimen FROM patient LEFT OUTER JOIN original ON patient.patient_id = " +
                    "original.patient_id WHERE patient.facility_id = " + facilityId +
                    " AND patient.current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND " +
                    "patient.regimentype IN (SELECT description FROM regimentype WHERE regimentype_id = 14)";
        }
        return generateReportList(query);
    }

    public synchronized List<Map<String, Object>> regimenSummary(Long facilityId) {
        reportTitle = "Patient Per Regimen Report";
        executeUpdate("DROP TABLE IF EXISTS ppr");
        query = "CREATE TEMPORARY TABLE ppr AS SELECT regimentype, regimen, COUNT(DISTINCT patient_id) AS " +
                "number_of_patients FROM patient WHERE facility_id = " + facilityId +
                " AND regimentype IN (SELECT description FROM regimentype WHERE regimentype_id " +
                "IN (1, 2, 3, 4, 14)) AND regimen != '' AND regimen IS NOT NULL AND current_status " +
                "IN ('ART Start', 'ART Restart', 'ART Transfer In') GROUP BY regimentype, regimen " +
                "HAVING regimen IS NOT NULL";
        executeUpdate(query);
        query = "SELECT * FROM ppr ORDER BY regimentype, regimen";
        return generateReportList(query);
    }

    public synchronized List<Map<String, Object>> devolvedSummary(Long facilityId) {
        reportTitle = "Patient Devolvement Report";
        //executeUpdate("DROP TABLE IF EXISTS dev");  
        query = "SELECT pt.patient_id, pt.facility_id, cp.pharmacy, dv.date_devolved, pt.hospital_num, " +
                "pt.surname, pt.other_names, pt.gender, pt.date_birth, DATEDIFF(YEAR, pt.date_birth, " +
                "CURDATE()) AS age, pt.phone, pt.address, pt.state, pt.lga, pt.date_registration, " +
                "pt.status_registration, pt.current_status, pt.date_current_status, pt.date_started, " +
                "pt.date_last_cd4, pt.last_cd4, pt.last_cd4p, pt.date_last_viral_load, pt.last_viral_load, " +
                "pt.date_last_refill, pt.last_refill_duration FROM patient AS pt JOIN devolve AS dv " +
                "ON pt.patient_id = dv.patient_id JOIN communitypharm AS cp ON dv.communitypharm_id = " +
                "cp.communitypharm_id WHERE pt.facility_id = " + facilityId + " ORDER BY cp.pharmacy";
        System.out.println("Query is: " + query);
        //executeUpdate(query);
        //query = "SELECT * FROM dev ORDER BY regimentype, regimen";
        return generateReportList(query);
    }


    public List<Map<String, Object>> generateReportList(String query) {
        return jdbcTemplate.queryForList(query);
    }

    public Map<String, Object> getReportParameters(ReportParameterDTO dto, long facilityId) {
        Map<String, Object> parameterMap = new HashMap<>();

        Calendar today = new GregorianCalendar();
        if (dto.getReportingMonth() != null) {
            parameterMap.put("reportingMonth", dto.getReportingMonth());
        } else {
            parameterMap.put("reportingMonth", DateUtil.getMonth(today.get(Calendar.MONTH) + 1));
        }
        if (dto.getReportingYear() != null) {
            parameterMap.put("reportingYear", dto.getReportingYear());
        } else {
            parameterMap.put("reportingYear", Integer.toString(today.get(Calendar.YEAR)));
        }
        if (dto.getReportingMonthBegin() != null) {
            parameterMap.put("reportingMonthBegin", dto.getReportingMonthBegin());
        }
        if (dto.getReportingMonthEnd() != null) {
            parameterMap.put("reportingMonthEnd", dto.getReportingMonthEnd());
        }
        if (dto.getReportingYearBegin() != null) {
            parameterMap.put("reportingYearBegin", dto.getReportingYearBegin());
        }
        if (dto.getReportingYearEnd() != null) {
            parameterMap.put("reportingYearEnd", dto.getReportingYearEnd());
        }
        if (dto.getReportingDateBegin() != null) {
            parameterMap.put("reportingDateBegin", dto.getReportingDateBegin());
        }
        if (dto.getReportingDateEnd() != null) {
            parameterMap.put("reportingDateEnd", dto.getReportingDateEnd());
        }
        if (dto.getReportingDate() != null) {
            parameterMap.put("reportingDate", dto.getReportingDate());
        }
        parameterMap.put("reportTitle", reportTitle);

        try {
            // fetch the required records from the database
            jdbcUtil = new JDBCUtil();
            query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility JOIN lga ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE facility_id = " + facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                parameterMap.put("facilityName", resultSet.getString("name"));
                parameterMap.put("lga", resultSet.getString("lga"));
                parameterMap.put("state", resultSet.getString("state"));
            }
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return parameterMap;
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


//        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, current_status, date_current_status, date_last_refill, date_last_clinic, date_next_clinic AS date_visit FROM patient WHERE facility_id = " + id + " AND current_status NOT IN ('ART Transfer Out', 'Pre-ART Transfer Out', 'Lost to Follow Up', 'Stopped Treatment', 'Known Death') AND ((date_next_clinic IS NOT NULL AND DATEDIFF(DAY, date_next_clinic, CURDATE()) > 1) OR (date_next_refill IS NOT NULL AND DATEDIFF(DAY, date_next_refill, CURDATE()) > 1)) ORDER BY current_status";
//        query = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, gender, date_birth, DATEDIFF(YEAR, date_birth, CURDATE()) AS age, phone, address, current_status, date_current_status, date_last_refill, date_last_clinic, date_next_clinic AS date_visit FROM patient WHERE facility_id = " + id + " AND current_status IN ('HIV+ non ART', 'ART Start', 'ART Restart', 'ART Transfer In') AND ((date_next_clinic IS NOT NULL AND DATEDIFF(DAY, date_next_clinic, CURDATE()) >= 90) OR (date_next_refill IS NOT NULL AND DATEDIFF(DAY, date_next_refill, CURDATE()) >= 90)) ORDER BY current_status";
