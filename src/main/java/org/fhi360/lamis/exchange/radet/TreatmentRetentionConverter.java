/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.radet;

/**
 * @author user1
 */

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.FileUtil;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.RequestParam;


public class TreatmentRetentionConverter {


    private ArrayList<Map<String, String>> periodLabelList = new ArrayList<Map<String, String>>();
    private int[][] value;
    private String[][] indicator;
    private int numberOfCohorts;

    private PreparedStatement preparedStatement;
    private HttpServletRequest request;
    private HttpSession session;
    private JDBCUtil jdbcUtil;
    private String query;

    private String fileName = "";
    private final ApplicationProperties applicationProperties = ContextProvider.getBean(ApplicationProperties.class);

    public TreatmentRetentionConverter() {

        try {
            this.jdbcUtil = new JDBCUtil();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    public synchronized String convertExcel(String cohortMonthBegin1,
                                             String cohortYearBegin1,
                                            String cohortMonthEnd1,
                                             String cohortYearEnd1) {
        int cohortMonthBegin = DateUtil.getMonth(cohortMonthBegin1);
        int cohortYearBegin = Integer.parseInt(cohortYearBegin1);
        int cohortMonthEnd = DateUtil.getMonth(cohortMonthEnd1);
        int cohortYearEnd = Integer.parseInt(cohortYearEnd1);
        long facilityId = (Long) session.getAttribute("id");
        String facilityName = (String) session.getAttribute("facilityName");

        //Initialize indicator to process
        int len = 0;//new TreatmentRetentionIndicators().length();
        System.out.println("Indicator length..." + len);
        indicator = new String[len][2];
        indicator = null;//new TreatmentRetentionIndicators().initialize();
        try {
            Date startDate = DateUtil.getFirstDateOfMonth(cohortYearBegin, cohortMonthBegin);
            Date endDate = DateUtil.getLastDateOfMonth(cohortYearEnd, cohortMonthEnd);
            numberOfCohorts = DateUtil.monthsBetweenIgnoreDays(startDate, endDate);
            System.out.println("Number of cohorts..." + numberOfCohorts);

            //Initialize the indicator values array
            value = new int[indicator.length][numberOfCohorts * 12];
            for (int i = 0; i <= numberOfCohorts; i++) {
                //ART start month
                Map<String, Object> period = DateUtil.getPeriod(startDate, i);
                int month = (Integer) period.get("month");
                int year = (Integer) period.get("year");
                String periodLabelBegin = (String) period.get("periodLabel");

                //Month expected to have completed 12 months
                //For example Jan 2010 will have an expected end month of Jan 2011
                Date expected = DateUtil.addMonth(DateUtil.getLastDateOfMonth(year, month), 12);
                period = DateUtil.getPeriod(expected, 0);
                String periodLabelEnd = (String) period.get("periodLabel");

                Map<String, String> map = new HashMap<String, String>();
                map.put("periodLabelBegin", periodLabelBegin);
                map.put("periodLabelEnd", periodLabelEnd);
                periodLabelList.add(map);

                analyzeCohort(month, year, i);
            }
            convert(cohortMonthBegin,
                    cohortYearBegin,
                    cohortMonthEnd,
                     cohortYearEnd);  //Convert values to worksheet
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return "transfer/" + fileName;
    }

    private void analyzeCohort(int month, int year, int cohort) {
        String cohortDateBegin = DateUtil.parseDateToString(DateUtil.getFirstDateOfMonth(year, month), "yyyy-MM-dd");
        Long facilityId = (Long) session.getAttribute("id");
        ResultSet resultSet;
        try {
            //Computing Net Current cohort
            query = "SELECT DISTINCT patient_id, gender, TIMESTAMPDIFF(YEAR, date_birth, '" + cohortDateBegin + "') AS age, date_started, DATE_ADD(date_started, INTERVAL 12 MONTH) AS date_outcome, status_registration FROM patient "
                    + " WHERE facility_id = " + facilityId + " AND YEAR(date_started) = " + year + " AND MONTH(date_started) = " + month;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String statusRegistration = resultSet.getString("status_registration");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                //For pregnant female, the script will check for any clinic visit between the date of ART start and date of ART start + 12 months with pregnancy status of 1
                boolean pregnant = false;
                boolean breastfeeding = false;
                if (gender.equalsIgnoreCase("Female")) {
                    long patientId = resultSet.getLong("patient_id");
                    String dateStarted = DateUtil.parseDateToString(resultSet.getDate("date_started"), "yyyy-MM-dd");
                    String dateOutcome = DateUtil.parseDateToString(resultSet.getDate("date_outcome"), "yyyy-MM-dd");

                    query = "SELECT * FROM clinic WHERE patient_id = " + patientId + " AND date_visit >=  '" + dateStarted + "' AND date_visit <= '" + dateOutcome + "' AND (pregnant = 1 OR breastfeeding = 1) ORDER BY date_visit DESC";
                    preparedStatement = jdbcUtil.getStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        if (rs.getInt("pregnant") == 1) {
                            pregnant = true;
                            break;
                        }
                        if (rs.getInt("breastfeeding") == 1) {
                            breastfeeding = true;
                            break;
                        }
                    }
                }

                if (statusRegistration.trim().equalsIgnoreCase("HIV+ non ART") || statusRegistration.trim().equalsIgnoreCase("Pre-ART Transfer In"))
                    disaggregate(gender, age, pregnant, breastfeeding, 0, cohort);
                if (statusRegistration.trim().equalsIgnoreCase("ART Transfer In"))
                    disaggregate(gender, age, pregnant, breastfeeding, 22, cohort);
            }

            //Computing Total Loss
            //Select all patients who started ART within the month/year of cohort and check if there current is status either LTFU, Stopped or Dead 
            //and the date their status changed to either Transfer Out, LTFU, stopped or dead is within 12 months from the date they started ART

            query = "SELECT DISTINCT statushistory.patient_id, statushistory.current_status, statushistory.date_current_status, patient.gender, TIMESTAMPDIFF(YEAR, patient.date_birth, '" + cohortDateBegin + "') AS age, patient.date_started, DATE_ADD(patient.date_started, INTERVAL 12 MONTH) AS date_outcome FROM statushistory JOIN patient ON statushistory.patient_id = patient.patient_id "
                    + " WHERE statushistory.facility_id = " + facilityId + " AND YEAR(patient.date_started) = " + year + " AND MONTH(patient.date_started) = " + month + " AND statushistory.current_status IN ('ART Transfer Out', 'Lost to Follow Up', 'Stopped Treatment', 'Known Death') AND statushistory.date_current_status <= DATE_ADD(patient.date_started, INTERVAL 12 MONTH)";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String currentStatus = resultSet.getString("current_status");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                //For pregnant female, the script will check for any clinic visit between the date of ART start and date of ART start + 12 months with pregnancy status of 1
                boolean pregnant = false;
                boolean breastfeeding = false;
                if (gender.equalsIgnoreCase("Female")) {
                    long patientId = resultSet.getLong("patient_id");
                    String dateStarted = DateUtil.parseDateToString(resultSet.getDate("date_started"), "yyyy-MM-dd");
                    String dateOutcome = DateUtil.parseDateToString(resultSet.getDate("date_outcome"), "yyyy-MM-dd");

                    query = "SELECT * FROM clinic WHERE patient_id = " + patientId + " AND date_visit >=  '" + dateStarted + "' AND date_visit <= '" + dateOutcome + "' AND (pregnant = 1 OR breastfeeding = 1) ORDER BY date_visit DESC";
                    preparedStatement = jdbcUtil.getStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        if (rs.getInt("pregnant") == 1) {
                            pregnant = true;
                            break;
                        }
                        if (rs.getInt("breastfeeding") == 1) {
                            breastfeeding = true;
                            break;
                        }
                    }
                }

                if (currentStatus.trim().equalsIgnoreCase("ART Transfer Out"))
                    disaggregate(gender, age, pregnant, breastfeeding, 44, cohort);
                if (currentStatus.trim().equalsIgnoreCase("Stopped Treatment"))
                    disaggregate(gender, age, pregnant, breastfeeding, 66, cohort);
                if (currentStatus.trim().equalsIgnoreCase("Known Death"))
                    disaggregate(gender, age, pregnant, breastfeeding, 88, cohort);
                if (currentStatus.trim().equalsIgnoreCase("Lost to Follow Up"))
                    disaggregate(gender, age, pregnant, breastfeeding, 110, cohort);
            }
            resultSet = null;
        } catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private void disaggregate(String gender, int age, boolean pregnant, boolean breastfeeding, int indicatorNum, int cohort) {
        if (gender.trim().equalsIgnoreCase("Male")) {
            if (age < 1) {
                value[indicatorNum][cohort]++;  //males < 1yr
            } else {
                if (age >= 1 && age < 9) {
                    value[indicatorNum + 1][cohort]++;  //males 1-9yrs
                } else {
                    if (age >= 10 && age < 14) {
                        value[indicatorNum + 2][cohort]++;  //males 10-14yrs
                    } else {
                        if (age >= 15 && age < 19) {
                            value[indicatorNum + 3][cohort]++;  //males 15-19yrs
                        } else {
                            if (age >= 20 && age < 24) {
                                value[indicatorNum + 4][cohort]++;  //males 15-19yrs
                            } else {
                                if (age >= 25 && age < 29) {
                                    value[indicatorNum + 5][cohort]++;  //males 25-49yrs
                                } else {
                                    if (age >= 30 && age < 34) {
                                        value[indicatorNum + 6][cohort]++;  //males 30-34yrs
                                    } else {
                                        if (age >= 35 && age < 39) {
                                            value[indicatorNum + 7][cohort]++;  //males 35-39yrs
                                        } else {
                                            if (age >= 40 && age < 49) {
                                                value[indicatorNum + 8][cohort]++;  //males 40-49yrs
                                            } else {
                                                value[indicatorNum + 9][cohort]++;  //males 50+yrs
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
                value[indicatorNum + 10][cohort]++;  //females < 1yr
            } else {
                if (age < 9) {
                    value[indicatorNum + 11][cohort]++;  //females 1-9yrs
                } else {
                    if (age >= 10 && age < 14) {
                        value[indicatorNum + 12][cohort]++;  //females 10-14yrs
                    } else {
                        if (age >= 15 && age < 19) {
                            value[indicatorNum + 13][cohort]++;  //females 15-19yrs
                        } else {
                            if (age >= 20 && age < 24) {
                                value[indicatorNum + 14][cohort]++;  //females 15-19yrs
                            } else {
                                if (age >= 25 && age < 29) {
                                    value[indicatorNum + 15][cohort]++;  //females 25-49yrs
                                } else {
                                    if (age >= 30 && age < 34) {
                                        value[indicatorNum + 16][cohort]++;  //females 30-34yrs
                                    } else {
                                        if (age >= 35 && age < 39) {
                                            value[indicatorNum + 17][cohort]++;  //females 35-39yrs
                                        } else {
                                            if (age >= 40 && age < 49) {
                                                value[indicatorNum + 18][cohort]++;  //females 40-49yrs
                                            } else {
                                                value[indicatorNum + 19][cohort]++;  //females 50+yrs
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //After disgragating females by age, further disaggregate by pregnancy or breastfeeding
            if (pregnant) {
                value[indicatorNum + 20][cohort]++;  //pregnant females                    
            } else {
                if (breastfeeding) {
                    value[indicatorNum + 21][cohort]++;  //breastfeeding females                                            
                }
            }
        }
    }

    private void convert(@RequestParam int cohortMonthBegin,
                         @RequestParam int cohortYearBegin,
                         @RequestParam int cohortMonthEnd,
                         @RequestParam int cohortYearEnd) {
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);  // turn off auto-flushing and accumulate all rows in memory

        Sheet sheet = workbook.createSheet();
        String facilityName = (String) session.getAttribute("facilityName");
        //Create a new font
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(new HSSFColor.WHITE().getIndex());

        //Create a style and set the font into it
        CellStyle style = workbook.createCellStyle();
        //style.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        style.setFillBackgroundColor(new HSSFColor.BLUE().getIndex());
        style.setFillPattern(HSSFCellStyle.FINE_DOTS);
        style.setFont(font);

        //Populating Excel sheet headings
        int rownum = 0;
        Row row = sheet.createRow(rownum++);
        Cell cell = row.createCell(0);
        cell.setCellValue("Cohort Analysis Report Template");
        row = sheet.createRow(rownum++);
        cell = row.createCell(0);
        String title = DateUtil.getMonth(cohortMonthBegin) + " " + cohortYearBegin + " - " + DateUtil.getMonth(cohortMonthEnd) + " " + cohortYearEnd;
        cell.setCellValue("Cohort Group: Clients started ART between " + title);
        row = sheet.createRow(rownum++);
        cell = row.createCell(0);
        cell.setCellValue("Name of Facility: " + facilityName);

        //Populating ART start month e.g Jan'10, Feb'10
        int cellnum = 0;
        row = sheet.createRow(rownum++);
        cell = row.createCell(cellnum++);
        cell.setCellValue("ART start month");
        cell = row.createCell(cellnum++);
        cell.setCellValue("");
        for (int i = 0; i < periodLabelList.size(); i++) {
            cell = row.createCell(cellnum++);
            cell.setCellValue(periodLabelList.get(i).get("periodLabelBegin"));
        }

        //Populating Month expected to have completed 12 month e.g Jan'11, Feb'11
        cellnum = 0;
        row = sheet.createRow(rownum++);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Month expected to have completed 12 months");
        cell = row.createCell(cellnum++);
        cell.setCellValue("");
        for (int i = 0; i < periodLabelList.size(); i++) {
            cell = row.createCell(cellnum++);
            cell.setCellValue(periodLabelList.get(i).get("periodLabelEnd"));
        }

        cellnum = 0;
        row = sheet.createRow(rownum++);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Indicator");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Gender/Age");
        cell.setCellStyle(style);
        for (int i = 0; i < periodLabelList.size(); i++) {
            cell = row.createCell(cellnum++);
            cell.setCellValue("");
            cell.setCellStyle(style);
        }

        //Populating indicator 
        title = "";
        for (int i = 0; i < indicator.length; i++) {
            System.out.println("Index..." + indicator[i][0]);
            //If the indicator title changes and is not empty create a new row
            if (!title.equals((String) indicator[i][0])) {
                if (!title.trim().isEmpty()) sheet.createRow(rownum++);
                title = (String) indicator[i][0];
            }
            cellnum = 0;
            row = sheet.createRow(rownum++);
            cell = row.createCell(cellnum++);
            cell.setCellValue(indicator[i][0]);
            cell = row.createCell(cellnum++);
            cell.setCellValue(indicator[i][1]);
            for (int j = 0; j <= numberOfCohorts; j++) {
                cell = row.createCell(cellnum++);
                cell.setCellValue(value[i][j]);
            }
        }

        try {
            String contextPath = applicationProperties.getContextPath();
            String directory = contextPath + "transfer/";

            FileUtil fileUtil = new FileUtil();
            fileUtil.makeDir(directory);
            fileUtil.makeDir(request.getContextPath() + "/transfer/");

            fileName = "RETENTION_" + facilityName + "_" + cohortYearBegin + " to " + cohortYearEnd + ".xlsx";
            FileOutputStream outputStream = new FileOutputStream(new File(directory + fileName));
            workbook.write(outputStream);
            outputStream.close();
            workbook.dispose();  // dispose of temporary files backing this workbook on disk

            //for servlets in the default(root) context, copy file to the transfer folder in root 
            if (!contextPath.equalsIgnoreCase(request.getContextPath()))
                fileUtil.copyFile(fileName, contextPath + "transfer/", request.getContextPath() + "/transfer/");
        } catch (Exception exception) {
            exception.printStackTrace();
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

}
