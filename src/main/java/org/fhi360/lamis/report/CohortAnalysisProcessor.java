/**
 * @author AALOZIE
 */

package org.fhi360.lamis.report;

import org.fhi360.lamis.controller.report.ReportParameterDTO;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

@Component
public class CohortAnalysisProcessor {
    private int[][] cohort = new int[5][6];

    private JDBCUtil jdbcUtil;
    private String query;
    private PreparedStatement preparedStatement;

    public CohortAnalysisProcessor() {
    }

    public synchronized List<Map<String, Object>> process(ReportParameterDTO dto, Long facilityId, HttpSession session) {
        List<Map<String, Object>> reportList = new ArrayList<>();
        int cohortMonthBegin = DateUtil.getMonth(dto.getReportingMonthBegin());
        int cohortYearBegin = Integer.parseInt(dto.getReportingYearBegin());
        int cohortMonthEnd = DateUtil.getMonth(dto.getReportingMonthEnd());
        int cohortYearEnd = Integer.parseInt(dto.getReportingYearEnd());
        processor(cohortMonthBegin, cohortYearBegin, cohortMonthEnd, cohortYearEnd, facilityId, session);
        return getReportList();
    }

    public synchronized int[][] process(String reportingMonthBegin, String reportingYearBegin, String reportingMonthEnd,
                                        String reportingYearEnd, Long facilityId, HttpSession session) {
        List<Map<String, Object>> patientList = new ArrayList<>();
        List<Map<String, Object>> reportList = new ArrayList<>();
        int cohortMonthBegin = DateUtil.getMonth(reportingMonthBegin);
        int cohortYearBegin = Integer.parseInt(reportingYearBegin);
        int cohortMonthEnd = DateUtil.getMonth(reportingMonthEnd);
        int cohortYearEnd = Integer.parseInt(reportingYearEnd);
        processor(cohortMonthBegin, cohortYearBegin, cohortMonthEnd, cohortYearEnd, facilityId, session);
        return cohort;
    }

    private List<Map<String, Object>> processor(Integer cohortMonthBegin, Integer cohortYearBegin, Integer cohortMonthEnd,
                                                Integer cohortYearEnd, Long facilityId, HttpSession session) {
        String cohortDateBegin = DateUtil.parseDateToString(DateUtil
                .getFirstDateOfMonth(cohortYearBegin, cohortMonthBegin), "yyyy-MM-dd");
        String cohortDateEnd = DateUtil.parseDateToString(DateUtil
                .getLastDateOfMonth(cohortYearEnd, cohortMonthEnd), "yyyy-MM-dd");
        List<Map<String, Object>> patientList = new ArrayList<>();
        try {
            jdbcUtil = new JDBCUtil();
            executeUpdate("DROP TABLE IF EXISTS dropped");
            executeUpdate("CREATE TEMPORARY TABLE dropped (patient_id bigint)");
            //patients on care and treatment 
            query = "SELECT patient_id, date_started FROM patient WHERE facility_id = " + facilityId + " AND date_started >= '" + cohortDateBegin + "' AND date_started <= '" + cohortDateEnd + "'"; // AND (current_status != 'ART Transfer Out' AND date_current_status <= '" + cohortDateBegin + "')";
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            int totalCohort = 0;
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("patientId", resultSet.getLong("patient_id"));
                map.put("dateStarted", resultSet.getDate("date_started"));
                patientList.add(map);
                totalCohort++;
            }
            session.setAttribute("totalCohort", Integer.toString(totalCohort));
            int alive = totalCohort;
            int month[] = {6, 12, 18, 24, 30, 36};
            for (int i = 0; i <= 5; i++) {
                for (Map map : patientList) {
                    long patientId = (Long) map.get("id");
                    Date dateStarted = (Date) map.get("dateStarted");
                    query = "SELECT patient_id FROM dropped WHERE patient_id = " + patientId;
                    preparedStatement = jdbcUtil.getStatement(query);
                    ResultSet resultSet1 = preparedStatement.executeQuery();
                    if (!resultSet1.next()) {
                        String endDate = DateUtil.parseDateToString(DateUtil.addMonth(dateStarted, month[i]), "yyyy-MM-dd");
                        query = "SELECT current_status FROM statushistory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_current_status = SELECT MAX(date_current_status) FROM statushistory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_current_status <= '" + endDate + "'";
                        preparedStatement = jdbcUtil.getStatement(query);
                        ResultSet resultSet2 = preparedStatement.executeQuery();
                        if (resultSet2.next()) {
                            String currentStatus = resultSet2.getString("current_status");
                            accummulator(patientId, currentStatus, i);
                        }
                    }
                }
                alive = alive - (cohort[0][i] + cohort[1][i] + cohort[2][i] + cohort[3][i]);  //alive and on ART at the end of cohort month
                cohort[4][i] = alive;
            }
            resultSet = null;
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return patientList;
    }

    private void accummulator(long patientId, String currentStatus, int i) {
        boolean drop = false;
        if (currentStatus.trim().equals("ART Transfer Out")) {
            cohort[0][i]++;
            drop = true;
        }
        if (currentStatus.trim().equals("Stopped Treatment")) {
            cohort[1][i]++;
            drop = true;
        }
        if (currentStatus.trim().equals("Lost to Follow Up")) {
            cohort[2][i]++;
            drop = true;
        }
        if (currentStatus.trim().equals("Known Death")) {
            cohort[3][i]++;
            drop = true;
        }
        query = "INSERT INTO dropped(patient_id) VALUES(" + patientId + ")";
        if (drop) executeUpdate(query);
    }

    private List<Map<String, Object>> getReportList() {
        String status[] = {"ART Transfer Out", "Stopped Treatment", "Lost to Follow Up", "Known Death",
                "Alive and on ART"};
        List<Map<String, Object>> reportList = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            // create map of values 
            Map<String, Object> map = new HashMap<>();
            map.put("status", status[i]);
            map.put("mon6", Integer.toString(cohort[i][0]));
            map.put("mon12", Integer.toString(cohort[i][1]));
            map.put("mon18", Integer.toString(cohort[i][2]));
            map.put("mon24", Integer.toString(cohort[i][3]));
            map.put("mon30", Integer.toString(cohort[i][4]));
            map.put("mon36", Integer.toString(cohort[i][5]));
            reportList.add(map);
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

    private void executeUpdate(String query) {
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }
}
