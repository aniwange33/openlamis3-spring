/**
 * @author AALOZIE
 */

package org.fhi360.lamis.report;

import org.fhi360.lamis.controller.report.ReportParameterDTO;
import org.fhi360.lamis.report.indicator.PerformanceIndicators;
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
public class PerformanceIndicatorProcessor {
    private int reportingMonth;
    private int reportingYear;
    private String reportingDateBegin;
    private String reportingDateEnd;
    //private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private int[][] value = new int[23][2];
    private String[] indicator = new String[23];
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private long facilityId;

    public PerformanceIndicatorProcessor() {
    }

    //Calculation of performance indicator report
    public synchronized List<Map<String, Object>> process(ReportParameterDTO dto, Long facilityId) {
        List<Map<String, Object>> reportList = new ArrayList<>();
        reportingMonth = DateUtil.getMonth(dto.getReportingMonth());
        reportingYear = Integer.parseInt(dto.getReportingYear());
        reportingDateBegin = DateUtil.parseDateToString(DateUtil.getFirstDateOfMonth(reportingYear, reportingMonth), "yyyy-MM-dd");
        reportingDateEnd = DateUtil.parseDateToString(DateUtil.getLastDateOfMonth(reportingYear, reportingMonth), "yyyy-MM-dd");
        indicator = new PerformanceIndicators().initialize();

        for (int i = 0; i < 23; i++) {
            process(i);
        }

        for (int i = 0; i < 23; i++) {
            persist(i + 1, value[i][1], value[i][0]);
            DecimalFormat decimalFormat = new DecimalFormat("###.##");
            double denominator = (double) value[i][0];
            double numerator = (double) value[i][1];
            String percentage = numerator > 0.0 ? decimalFormat.format((numerator / denominator) * 100) : "0.0";

            Map<String, Object> map = new HashMap<>();
            map.put("sno", Integer.toString(i + 1));
            map.put("indicator", indicator[i]);
            map.put("percentage", percentage);
            map.put("proportion", value[i][1] + "/" + value[i][0]);
            reportList.add(map);
        }
        return reportList;
    }

    //Calculation of performance indicator chart
    public double process(int month, int year, int i, Long facilityId) {
        reportingDateBegin = DateUtil.parseDateToString(DateUtil.getFirstDateOfMonth(year, month), "yyyy-MM-dd");
        reportingDateEnd = DateUtil.parseDateToString(DateUtil.getLastDateOfMonth(year, month), "yyyy-MM-dd");
        indicator = new PerformanceIndicators().initialize();

        process(i);
        int denominator = value[i][0];
        int numerator = value[i][1];
        return numerator > 0 ? 100.0 * numerator / denominator : 0.0;
    }

    private void process(int i) {
        if (i >= 0 && i <= 3) {
            //denominator - all clinic visits during the reporting month
            query = "SELECT COUNT(*) AS count FROM clinic WHERE facility_id = " + facilityId + " AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "'";
            value[0][0] = getCount(query);
        }
        if (i == 0) {
            //Indicator 1 = clinic visits during the reporting period that had a documentation of TB status
            //numerator - all clinic visit during the reporting month with TB status not equal to null 
            query = "SELECT COUNT(*) AS count FROM clinic WHERE facility_id = " + facilityId + " AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "' AND tb_status != '' AND tb_status IS NOT NULL";
            value[0][1] = getCount(query);
        }

        if (i == 1) {
            //Indicator 2 = clinic visits during the reporting period that had a documentation of functional status  
            //denominator - all clinic visits during the reporting month
            value[1][0] = value[0][0];
            //numerator - all clinic visit during the reporting month with functional status not equal to null 
            query = "SELECT COUNT(*) AS count FROM clinic WHERE facility_id = " + facilityId + " AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "' AND func_status != '' AND func_status IS NOT NULL";
            value[1][1] = getCount(query);
        }

        if (i == 2) {
            //Indicator 3 = clinic visits during the reporting period that had a documentation of weight 
            //denominator - all clinic visits during the reporting month
            value[2][0] = value[0][0];
            //numerator - all clinic visit during the reporting month with body weight not equal to zero 
            query = "SELECT COUNT(*) AS count FROM clinic WHERE facility_id = " + facilityId + " AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "' AND body_weight > 0";
            value[2][1] = getCount(query);
        }

        if (i >= 3) {
            //create a temporary table of patient status on or before the last day of reporting month 
            executeUpdate("DROP TABLE IF EXISTS currentstatus");
            query = "CREATE TEMPORARY TABLE currentstatus AS SELECT DISTINCT patient_id, MAX(date_current_status) AS date_status FROM statushistory WHERE facility_id = " + facilityId + " AND date_current_status <= '" + reportingDateEnd + "' GROUP BY patient_id";
            executeUpdate(query);

            executeUpdate("DROP TABLE IF EXISTS ps");
            query = "CREATE TEMPORARY TABLE ps AS SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" + reportingDateBegin + "') AS age, patient.date_registration, patient.status_registration, patient.date_started, statushistory.current_status, currentstatus.date_status "
                    + " FROM patient JOIN statushistory ON patient.patient_id = statushistory.patient_id JOIN currentstatus ON patient.patient_id = currentstatus.patient_id WHERE patient.facility_id = " + facilityId + " AND statushistory.facility_id = " + facilityId + " AND statushistory.patient_id = currentstatus.patient_id AND statushistory.date_current_status = currentstatus.date_status";
            executeUpdate(query);
        }

        if (i == 3) {
            //Indicator 4 = clinic visits during the of the reporting month that had documentation of OI status in ART patients            //denominator - all clinic visits of current ART patients during the reporting month
            query = "SELECT COUNT(*) AS count FROM clinic WHERE facility_id = " + facilityId + " AND patient_id IN (SELECT DISTINCT patient_id FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')) AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "'";
            value[3][0] = getCount(query);
            //numerator - all clinic visit of current ART patients during the reporting month with oi screening NOT NULL 
            query = "SELECT COUNT(*) AS count FROM clinic WHERE facility_id = " + facilityId + " AND patient_id IN (SELECT DISTINCT patient_id FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')) AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "' AND oi_screened != '' AND oi_screened IS NOT NULL";
            value[3][1] = getCount(query);
        }

        if (i == 4) {
            //Indicator 5 = clinic visits during the of the reporting month that had documentation of OI status in non-ART patients
            //denominator - all clinic visits of non-ART patients during the reporting month
            query = "SELECT COUNT(*) AS count FROM clinic WHERE facility_id = " + facilityId + " AND patient_id IN (SELECT DISTINCT patient_id FROM ps WHERE status_registration = 'HIV+ non ART') AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "'";
            value[4][0] = getCount(query);
            //numerator - all clinic visit of non-ART patients during the reporting month with oi screening not equal to null 
            query = "SELECT COUNT(*) AS count FROM clinic WHERE facility_id = " + facilityId + " AND patient_id IN (SELECT DISTINCT patient_id FROM ps WHERE status_registration = 'HIV+ non ART') AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "' AND oi_screened != '' AND oi_screened IS NOT NULL";
            value[4][1] = getCount(query);
        }

        if (i == 5) {
            //Indicator 6 = current ART patients with documentation of ADR status in clinic visit within the reporting period   
            //denominator - all clinic visits of current ART patients during the reporting month
            query = "SELECT COUNT(DISTINCT date_visit) AS count FROM clinic WHERE facility_id = " + facilityId + " AND patient_id IN (SELECT DISTINCT patient_id FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')) AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "'";
            value[5][0] = getCount(query);
            //numerator - all clinic visit during the reporting month with adr screening not equal to null 
            query = "SELECT COUNT(DISTINCT date_visit) AS count FROM clinic WHERE facility_id = " + facilityId + " AND patient_id IN (SELECT DISTINCT patient_id FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')) AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "' AND adr_screened != '' AND adr_screened IS NOT NULL";
            value[5][1] = getCount(query);
        }

        if (i == 6) {
            //Indicator 7 - current ART patients with documentation of ADR status in refill visit within the reporting period   
            //denominator - all pharmacy visits during the reporting month
            query = "SELECT COUNT(DISTINCT date_visit) AS count FROM pharmacy WHERE facility_id = " + facilityId + " AND patient_id IN (SELECT DISTINCT patient_id FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')) AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "'";
            value[6][0] = getCount(query);
            //numerator - all pharmacy visit during the reporting month with adr screening not equal to null             
            query = "SELECT COUNT(DISTINCT date_visit) AS count FROM pharmacy WHERE facility_id = " + facilityId + " AND patient_id IN (SELECT DISTINCT patient_id FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')) AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "' AND adr_screened != '' AND adr_screened IS NOT NULL";
            value[6][1] = getCount(query);
        }

        if (i >= 7 && i <= 10) {
            reportingDateBegin = DateUtil.parseDateToString(DateUtil.addMonth(DateUtil.getFirstDateOfMonth(reportingYear, reportingMonth), -6), "yyyy-MM-dd");
            //denominator - all currently on ART as at the last day of the reporting month
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')";
            value[7][0] = getCount(query);
        }

        if (i == 7) {
            //Indicator 8 - current ART patients who had at least one documented clinic visit in the last 6 months   
            //numerator - current ART patients who had at least one documented clinic visit in the last 6 months            
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM clinic WHERE facility_id = " + facilityId + " AND patient_id IN (SELECT DISTINCT patient_id FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')) AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "'";
            value[7][1] = getCount(query);
        }

        if (i == 8) {
            //Indicator 9 - current ART patients with at least at one time CD4 count test done in the last 6 months   
            //denominator - all currently on ART as the last day of the reporting month
            value[8][0] = value[7][0];
            //numerator - current ART patients with at least at one time CD4 count test done in the last 6 months   
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM laboratory WHERE facility_id = " + facilityId + " AND patient_id IN (SELECT DISTINCT patient_id FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')) AND date_reported >= '" + reportingDateBegin + "' AND date_reported <= '" + reportingDateEnd + "' AND labtest_id = 1";
            value[8][1] = getCount(query);
        }

        if (i == 9) {
            //Indicator 10 - current ART patients with at least at one time a minimum set of standard haematology tests (HCT or HB) and (WBC) done in the last 6 months   
            //denominator - all currently on ART as the last day of the reporting month
            value[9][0] = value[7][0];
            //numerator - current ART patients with at least at one time a minimum set of standard haematology tests (HCT or HB) and (WBC) done in the last 6 months
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM laboratory WHERE facility_id = " + facilityId + " AND patient_id IN (SELECT patient_id FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')) AND date_reported >= '" + reportingDateBegin + "' AND date_reported <= '" + reportingDateEnd + "' AND (labtest_id = 8 OR labtest_id = 9) AND labtest_id = 2";
            value[9][1] = getCount(query);
        }

        if (i == 10) {
            //Indicator 11 - current ART patients with at least at one time a minimum set of standard chemistry tests (creatine and ALT/SGPT) done in the last 6 months  
            //denominator - all currently on ART as the last day of the reporting month
            value[10][0] = value[7][0];
            //numerator - current ART patients with at least at one time a minimum set of standard chemistry tests (creatine and ALT/SGPT) done in the last 6 months   
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM laboratory WHERE facility_id = " + facilityId + " AND patient_id IN (SELECT patient_id FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')) AND date_reported >= '" + reportingDateBegin + "' AND date_reported <= '" + reportingDateEnd + "' AND labtest_id = 12 AND labtest_id = 15";
            value[10][1] = getCount(query);
        }

        if (i >= 11 && i <= 12) {
            //create a temporary table of last clinic visit on or before the last day of reporting month 
            executeUpdate("DROP TABLE IF EXISTS visit");
            query = "CREATE TEMPORARY TABLE visit AS SELECT DISTINCT patient_id, MAX(date_visit) AS date_visit FROM clinic WHERE facility_id = " + facilityId + " AND date_visit <= '" + reportingDateEnd + "' GROUP BY patient_id";
            executeUpdate(query);
        }

        if (i == 11) {
            //Indicator 12 - current ART patients who had clinical staging done at the last clinic visit prior to reporting period 
            //denominator - the last clinic visit of ART patients before the first day of reporting period            
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM visit WHERE patient_id IN (SELECT DISTINCT patient_id FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In'))";
            value[11][0] = getCount(query);
            //numerator - current ART patients who had clinical staging done at the last clinic visit prior to reporting period 
            query = "SELECT COUNT(DISTINCT clinic.patient_id) AS count FROM clinic JOIN visit ON clinic.patient_id = visit.patient_id WHERE clinic.facility_id = " + facilityId + " AND clinic.patient_id IN (SELECT DISTINCT patient_id FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')) AND clinic.date_visit = visit.date_visit AND clinic.clinic_stage != '' AND clinic.clinic_stage IS NOT NULL";
            value[11][1] = getCount(query);
        }

        if (i == 12) {
            reportingDateBegin = DateUtil.parseDateToString(DateUtil.getFirstDateOfMonth(reportingYear, reportingMonth), "yyyy-MM-dd");
            //Indicator 13 - current ART patients reporting ADR severity > 2 within the reporting period
            //denominator - all currently on ART as the last day of the reporting month
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM adrhistory WHERE facility_id = " + facilityId + " AND patient_id IN (SELECT DISTINCT patient_id FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')) AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "'";
            value[12][0] = getCount(query);
            //numerator - current ART patients reporting ADR severity > 2 within the reporting period   
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM adrhistory WHERE facility_id = " + facilityId + " AND patient_id IN (SELECT DISTINCT patient_id FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')) AND date_visit >= '" + reportingDateBegin + "' AND date_visit <= '" + reportingDateEnd + "' AND severity > 2";
            value[12][1] = getCount(query);
        }

        if (i == 13) {
            //create a temporary table of appointment of patients who started ART 3 months earlier than the first day of reporting month
            reportingDateBegin = DateUtil.parseDateToString(DateUtil.addMonth(DateUtil.getFirstDateOfMonth(reportingYear, reportingMonth), -3), "yyyy-MM-dd");
            executeUpdate("DROP TABLE IF EXISTS appointment");
            query = "CREATE TEMPORARY TABLE appointment AS SELECT DISTINCT patient_id, next_appointment FROM pharmacy WHERE facility_id = " + facilityId + " AND next_appointment >= '" + reportingDateBegin + "' AND next_appointment <= '" + reportingDateEnd + "'";
            executeUpdate(query);

            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM appointment WHERE patient_id IN (SELECT DISTINCT patient_id FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In'))";
            value[13][0] = getCount(query);
            //numerator - current ART patients who came for refill within the last 3 months            
            query = "SELECT COUNT(DISTINCT pharmacy.patient_id) AS count FROM pharmacy JOIN appointment ON pharmacy.patient_id = appointment.patient_id WHERE pharmacy.facility_id = " + facilityId + " AND pharmacy.patient_id IN (SELECT DISTINCT patient_id FROM ps WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')) AND pharmacy.date_visit >= appointment.next_appointment-7 AND pharmacy.date_visit <= appointment.next_appointment+7";
            value[13][1] = getCount(query);
        }

        if (i >= 14) {
            reportingDateBegin = DateUtil.parseDateToString(DateUtil.getFirstDateOfMonth(reportingYear, reportingMonth), "yyyy-MM-dd");
        }

        if (i == 14) {
            //Indicator 15 - current ART patients who have not had a refill 3 months after the last refill who have had their status correctly updated
            //denominator - current ART patients whos last refill was more than 3 months ago 
            Map map = new StatusUpdateIndicatorProcessor().calculate(facilityId, reportingDateEnd);
            value[14][0] = map.isEmpty() ? 0 : (Integer) map.get("denominator");
            //numerator - current ART patients whos last refill was more than 3 months ago, but had their status changed to either Lost to Follow Up, Stop Treatment, Restart, Dead, Tranfer Out 
            value[14][1] = map.isEmpty() ? 0 : (Integer) map.get("numerator");
        }

        if (i == 15) {
            //Indicator 16 - patients newly initiated on ART <= 5 years old with documented eligibility criteria
            //denominator - patients newly initiated on ART <= 5 years old within the reporting period            
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM patient WHERE facility_id = " + facilityId + " AND DATEDIFF(YEAR, date_birth, date_started) <= 5 AND status_registration = 'HIV+ non ART' AND date_started >= '" + reportingDateBegin + "' AND date_started <= '" + reportingDateEnd + "'";
            value[15][0] = getCount(query);
            //numerator - patients newly initiated on ART <= 5 years old with documented eligibility criteria           
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id = " + facilityId + " AND DATEDIFF(YEAR, patient.date_birth, patient.date_started) <= 5 AND patient.status_registration = 'HIV+ non ART' AND patient.date_started >= '" + reportingDateBegin + "' AND patient.date_started <= '" + reportingDateEnd + "' AND clinic.facility_id = " + facilityId + " AND (clinic.clinic_stage = 'Stage III' OR clinic.clinic_stage = 'Stage IV' OR clinic.cd4p < 25) AND clinic.commence = 1";
            value[15][1] = getCount(query);
        }

        if (i == 16) {
            //Indicator 17 - patients newly initiated on ART > 5 years old with documented eligibility criteria
            //denominator - patients newly initiated on ART > 5 years old within the reporting period            
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM patient WHERE facility_id = " + facilityId + "  AND DATEDIFF(YEAR, date_birth, date_started) > 5 AND status_registration = 'HIV+ non ART' AND date_started >= '" + reportingDateBegin + "' AND date_started <= '" + reportingDateEnd + "'";
            value[16][0] = getCount(query);
            //numerator - patients newly initiated on ART > 5 years old with documented eligibility criteria           
            query = "SELECT  COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id = " + facilityId + " AND DATEDIFF(YEAR, patient.date_birth, patient.date_started) > 5 AND patient.status_registration = 'HIV+ non ART' AND patient.date_started >= '" + reportingDateBegin + "' AND patient.date_started <= '" + reportingDateEnd + "' AND clinic.facility_id = " + facilityId + " AND (clinic.clinic_stage = 'Stage III' OR clinic.clinic_stage = 'Stage IV' OR clinic.cd4 < 350) AND clinic.commence = 1";
            value[16][1] = getCount(query);
        }

        if (i >= 17 && i <= 20) {
            //denominator - patients newly initiated on ART within the reporting period            
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM patient WHERE facility_id = " + facilityId + " AND date_started >= '" + reportingDateBegin + "' AND date_started <= '" + reportingDateEnd + "'";
            value[17][0] = getCount(query);
        }

        if (i == 17) {
            //Indicator 18 - patients newly initiated on ART who had at least one clinical staging done prior to ART commencement
            //numerator - patients newly initiated on ART who had at least one clinical staging done prior to ART commencement           
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id = " + facilityId + " AND patient.date_started >= '" + reportingDateBegin + "' AND patient.date_started <= '" + reportingDateEnd + "' AND clinic.facility_id = " + facilityId + " AND clinic.date_visit < patient.date_started AND clinic.clinic_stage != '' AND clinic.clinic_stage IS NOT NULL AND clinic.commence = 0";
            value[17][1] = getCount(query);
        }

        if (i == 18) {
            //Indicator 19 - patients newly initiated on ART with at least one CD4 count test done before ART commencement
            //denominator - patients newly initiated on ART within the reporting period            
            value[18][0] = value[17][0];
            //numerator - patients newly initiated on ART with at least one CD4 count test done before ART commencement
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN laboratory ON patient.patient_id = laboratory.patient_id WHERE patient.facility_id = " + facilityId + " AND patient.date_started >= '" + reportingDateBegin + "' AND patient.date_started <= '" + reportingDateEnd + "' AND laboratory.facility_id = " + facilityId + " AND laboratory.date_reported < patient.date_started AND laboratory.labtest_id = 1";
            value[18][1] = getCount(query);
        }

        if (i == 19) {
            //Indicator 20 - patients newly initiated on ART with at least at one time a minimum set of standard chemistry tests done before ART commencement
            //denominator - patients newly initiated on ART within the reporting period            
            value[19][0] = value[17][0];
            //numerator - patients newly initiated on ART with at least at one time a minimum set of standard chemistry tests done before ART commencement
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN laboratory ON patient.patient_id = laboratory.patient_id WHERE patient.facility_id = " + facilityId + " AND patient.date_started >= '" + reportingDateBegin + "' AND patient.date_started <= '" + reportingDateEnd + "' AND laboratory.facility_id = " + facilityId + " AND laboratory.date_reported < patient.date_started AND (laboratory.labtest_id = 8 OR laboratory.labtest_id = 9) AND laboratory.labtest_id = 2";
            value[19][1] = getCount(query);
        }

        if (i == 20) {
            //Indicator 21 - patients newly initiated on ART with at least at one time a minimum set of standard haematology tests done before ART commencement
            //denominator - patients newly initiated on ART within the reporting period            
            value[20][0] = value[17][0];
            //numerator - patients newly initiated on ART with at least at one time a minimum set of standard haematology tests done before ART commencement
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN laboratory ON patient.patient_id = laboratory.patient_id WHERE patient.facility_id = " + facilityId + " AND patient.date_started >= '" + reportingDateBegin + "' AND patient.date_started <= '" + reportingDateEnd + "' AND laboratory.facility_id = " + facilityId + " AND laboratory.date_reported < patient.date_started AND laboratory.labtest_id = 12 AND laboratory.labtest_id = 15";
            value[20][1] = getCount(query);
        }

        if (i >= 21 && i <= 22) {
            reportingDateBegin = DateUtil.parseDateToString(DateUtil.addMonth(DateUtil.getFirstDateOfMonth(reportingYear, reportingMonth), -6), "yyyy-MM-dd");
            //create a temporary table of first cotrim dispensed before the last day of reporting month 
            executeUpdate("DROP TABLE IF EXISTS cotrim");
            query = "CREATE TEMPORARY TABLE cotrim AS SELECT DISTINCT patient_id, MIN(date_visit) AS date_visit FROM pharmacy WHERE facility_id = " + facilityId + " AND date_visit <= '" + reportingDateEnd + "'  AND regimentype_id = 8 GROUP BY patient_id";
            executeUpdate(query);
            executeUpdate("DROP TABLE IF EXISTS ps");
            query = "CREATE TEMPORARY TABLE ps AS SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age, date_registration, status_registration FROM patient WHERE facility_id = " + facilityId + " AND date_registration >= '" + reportingDateBegin + "' AND date_registration <= '" + reportingDateEnd + "' AND status_registration = 'HIV+ non ART'";
            executeUpdate(query);
        }

        if (i == 21) {
            //Indicator 22 - HIV positive patients <= 5 years initiating cotrimoxazole prophylaxis in the last 6 months
            //denominator -             
            query = "SELECT COUNT(DISTINCT patient_id) FROM ps WHERE age <= 5";
            value[21][0] = getCount(query);
            //numerator - 
            query = "SELECT COUNT(DISTINCT ps.patient_id) AS count FROM ps JOIN cotrim ON ps.patient_id = cotrim.patient_id WHERE ps.age <= 5";
            value[21][1] = getCount(query);
        }
        if (i == 22) {
            //Indicator 23 - HIV positive patients > 5 years initiating cotrimoxazole prophylaxis in the last 6 months
            //denominator -             
            query = "SELECT COUNT(DISTINCT patient_id) FROM ps WHERE age > 5";
            value[22][0] = getCount(query);
            //numerator - 
            query = "SELECT COUNT(DISTINCT ps.patient_id) AS count FROM ps JOIN cotrim ON ps.patient_id = cotrim.patient_id WHERE ps.age > 5";
            value[22][1] = getCount(query);
        }
    }

    //save performance indicator values 
    public void persist(int indicatorId, int numerator, int denominator) {
        try {
            long performanceId = 0;
            query = "SELECT performance_id FROM performance WHERE indicator_id = ? AND facility_id = ? AND year = ? AND month = ?";
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.setInt(1, indicatorId);
            preparedStatement.setLong(2, facilityId);
            preparedStatement.setInt(3, reportingYear);
            preparedStatement.setInt(4, reportingMonth);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                performanceId = resultSet.getLong("performance_id");
            }
            if (performanceId == 0) {
                query = "INSERT INTO performance (indicator_id, facility_id, numerator, denominator, year, month) "
                        + " VALUES (?, ?, ?, ?, ?, ?)";
                preparedStatement = jdbcUtil.getStatement(query);
                preparedStatement.setInt(1, indicatorId);
                preparedStatement.setLong(2, facilityId);
                preparedStatement.setInt(3, numerator);
                preparedStatement.setInt(4, denominator);
                preparedStatement.setInt(5, reportingYear);
                preparedStatement.setInt(6, reportingMonth);
            } else {
                query = "UPDATE performance SET numerator = ?, denominator = ? WHERE performance_id = ?";
                preparedStatement = jdbcUtil.getStatement(query);
                preparedStatement.setInt(1, numerator);
                preparedStatement.setInt(2, denominator);
                preparedStatement.setLong(3, performanceId);
            }
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
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
            query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, " +
                    "facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility JOIN lga " +
                    "ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE " +
                    "facility_id = " + facilityId;
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

    private void executeUpdate(String query) {
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private int getCount(String query) {
        int count = 0;
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            resultSet = null;
        } catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return count;
    }
}
