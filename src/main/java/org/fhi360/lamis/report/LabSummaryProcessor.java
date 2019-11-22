/**
 * @author AALOZIE
 */

package org.fhi360.lamis.report;

import org.fhi360.lamis.controller.report.ReportParameterDTO;
import org.fhi360.lamis.report.indicator.LabSummaryIndicators;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.StringUtil;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LabSummaryProcessor {
    private int reportingMonth;
    private int reportingYear;
    private int[][] value = new int[19][2];

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;

    public LabSummaryProcessor() {

    }

    public List<Map<String, Object>> process(ReportParameterDTO dto, Long facilityId) {
        List<Map<String, Object>> reportList = new ArrayList<>();
        reportingMonth = DateUtil.getMonth(dto.getReportingMonth());
        reportingYear = Integer.parseInt(dto.getReportingYear());
        String reportingDateBegin = DateUtil.parseDateToString(DateUtil.getFirstDateOfMonth(reportingYear, reportingMonth), "yyyy-MM-dd");
        String reportingDateEnd = DateUtil.parseDateToString(DateUtil.getLastDateOfMonth(reportingYear, reportingMonth), "yyyy-MM-dd");

        ResultSet resultSet;
        String[] indicator = new LabSummaryIndicators().initialize();

        try {
            jdbcUtil = new JDBCUtil();
            query = "SELECT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" + reportingDateBegin + "') AS age, patient.date_started, laboratory.resultab, laboratory.resultpc, labtest.description "
                    + " FROM patient JOIN laboratory ON patient.patient_id = laboratory.patient_id JOIN labtest ON laboratory.labtest_id = labtest.labtest_id WHERE patient.facility_id = " + facilityId + " AND laboratory.facility_id = " + facilityId + " AND MONTH(laboratory.date_reported) = " + reportingMonth + " AND YEAR(laboratory.date_reported) = " + reportingYear;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String description = resultSet.getString("description");
                String resultab = (resultSet.getString("resultab") == null) ? "" : resultSet.getString("resultab");
                String resultpc = (resultSet.getString("resultpc") == null) ? "" : resultSet.getString("resultpc");
                String dateStarted = (resultSet.getDate("date_started") == null) ? "" : DateUtil.parseDateToString(resultSet.getDate("date_started"), "MM/dd/yyyy");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                // count number of CD4 count test done by patients
                if (description.equals("CD4")) {
                    if (dateStarted.isEmpty()) {  //Non-ART patients
                        if (age >= 5 && !resultab.isEmpty() && StringUtil.isInteger(resultab)) {
                            if (Double.parseDouble(resultab) <= 200) {
                                value[0][0]++;
                            } else {
                                if (Double.parseDouble(resultab) >= 200 && Double.parseDouble(resultab) <= 300) {
                                    value[1][0]++;
                                } else {
                                    value[2][0]++;
                                }
                            }
                        } //age less than 5 years 
                        else {
                            if (!resultpc.isEmpty() && StringUtil.isInteger(resultpc)) {
                                if (Double.parseDouble(resultpc) < 15) {
                                    value[3][0]++;
                                } else {
                                    value[4][0]++;
                                }
                            }
                        }
                    } else { // ART patients
                        if (age >= 5 && !resultab.isEmpty() && StringUtil.isInteger(resultab)) {
                            if (Double.parseDouble(resultab) <= 200) {
                                value[0][1]++;
                            } else {
                                if (Double.parseDouble(resultab) >= 200 && Double.parseDouble(resultab) <= 300) {
                                    value[1][1]++;
                                } else {
                                    value[2][1]++;
                                }
                            }
                        } //age less than 5 years 
                        else {
                            if (!resultpc.isEmpty() && StringUtil.isInteger(resultpc)) {
                                if (Double.parseDouble(resultpc) < 15) {
                                    value[3][1]++;
                                } else {
                                    value[4][1]++;
                                }
                            }
                        }
                    }
                }

                // count number of FBC test done by patients
                if (description.equals("WBC")) {
                    if (dateStarted.isEmpty()) {
                        value[5][0]++;
                    } else {
                        value[5][1]++;
                    }
                }
                // count number of GOT test done by patients
                if (description.equals("AST/SGOT")) {
                    if (dateStarted.isEmpty()) {
                        value[6][0]++;
                    } else {
                        value[6][1]++;
                    }
                }
                // count number of GPT test done by patients
                if (description.equals("ALT/SGPT")) {
                    if (dateStarted.isEmpty()) {
                        value[7][0]++;
                    } else {
                        value[7][1]++;
                    }
                }
                // count number of Creatine test done by patients
                if (description.equals("Creatinine")) {
                    if (dateStarted.isEmpty()) {
                        value[8][0]++;
                    } else {
                        value[8][1]++;
                    }
                }
                // count number of K+ test done by patients
                if (description.equals("Potasium (K+)")) {
                    if (dateStarted.isEmpty()) {
                        value[9][0]++;
                    } else {
                        value[9][1]++;
                    }
                }
                // count number of Glucose test done by patients
                if (description.equals("GLUCOSE")) {
                    if (dateStarted.isEmpty()) {
                        value[10][0]++;
                    } else {
                        value[10][1]++;
                    }
                }

                // count number of VDRL test done by patients
                if (description.equals("VDRL") && gender.equalsIgnoreCase("Female")) {
                    if (dateStarted.isEmpty()) {
                        if (resultab.equals("-")) {  // negative VDRL result Non-ART
                            value[11][0]++;
                        } else {
                            value[12][0]++;
                        }
                    } else {
                        if (resultab.equals("-")) {  // negative VDRL result ART
                            value[11][1]++;
                        } else {
                            value[12][1]++;
                        }
                    }
                }

                // count number of Pregnancy test done by patients
                if (description.equals("Pregnancy") && gender.equalsIgnoreCase("Female")) {
                    if (dateStarted.isEmpty()) {
                        value[13][0]++;
                    } else {
                        value[13][1]++;
                    }
                }
                // count number of HBsAg test done by patients
                if (description.equals("HBsAg")) {
                    if (dateStarted.isEmpty()) {
                        value[14][0]++;
                    } else {
                        value[14][1]++;
                    }
                }
                // count number of HCV test done by patients
                if (description.equals("HCV")) {
                    if (dateStarted.isEmpty()) {
                        value[15][0]++;
                    } else {
                        value[15][1]++;
                    }
                }
                // count number of Malaria test done by patients
                if (description.equals("Malaria")) {
                    if (dateStarted.isEmpty()) {
                        value[16][0]++;
                    } else {
                        value[16][1]++;
                    }
                }
                // count number of Stool microscopy test done by patients
                if (description.equals("Stool microscopy")) {
                    if (dateStarted.isEmpty()) {
                        value[17][0]++;
                    } else {
                        value[17][1]++;
                    }
                }
                // count number of Sputum Smear test done by patients
                if (description.equals("Sputum Smear")) {
                    if (dateStarted.isEmpty()) {
                        value[18][0]++;
                    } else {
                        value[18][1]++;
                    }
                }
            } // end while loop

            for (int i = 0; i < 18; i++) {
                String nonart = Integer.toString(value[i][0]);
                String art = Integer.toString(value[i][1]);
                int total = value[i][0] + value[i][1];

                // create map of values 
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("indicator", indicator[i]);
                map.put("nonart", nonart);
                map.put("art", art);
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
        reportingMonth = DateUtil.getMonth(dto.getReportingMonth());
        reportingYear = Integer.parseInt(dto.getReportingYear());
        parameterMap.put("reportingMonth", dto.getReportingMonth());
        parameterMap.put("reportingYear", dto.getReportingYear());
        ResultSet resultSet;

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
            resultSet = null;
        } catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return parameterMap;
    }
}
