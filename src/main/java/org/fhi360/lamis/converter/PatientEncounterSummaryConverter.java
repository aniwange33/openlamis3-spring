/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.Scrambler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author iduruanyanwu
 */
@Component
public class PatientEncounterSummaryConverter implements ServletContextAware {

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;


    public PatientEncounterSummaryConverter() {

    }

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public synchronized ByteArrayOutputStream convertExcel(String facilityIds, String state, long userId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int global_row = 0;

        DateFormat dateFormatExcel = new SimpleDateFormat("dd-MMM-yyyy");
        String contextPath = servletContext.getInitParameter("contextPath");


        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);  // turn off auto-flushing and accumulate all rows in memory
        Sheet sheet = workbook.createSheet();

        try {
            jdbcUtil = new JDBCUtil();

            //We Define the first row, ie the headers...
            int rownum = 0;
            int cellnum = 0;
            Row row = sheet.createRow(rownum++);
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue("State".toUpperCase());
            cell = row.createCell(cellnum++);
            cell.setCellValue("Facility Name".toUpperCase());
            cell = row.createCell(cellnum++);
            cell.setCellValue("Hospital Number".toUpperCase());
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of registration".toUpperCase());
            cell = row.createCell(cellnum++);
            cell.setCellValue("ART Start Date".toUpperCase());
            cell = row.createCell(cellnum++);
            cell.setCellValue("Current status".toUpperCase());
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of current status".toUpperCase());
            cell = row.createCell(cellnum++);
            cell.setCellValue("age".toUpperCase());
            cell = row.createCell(cellnum++);
            cell.setCellValue("sex".toUpperCase());
            cell = row.createCell(cellnum++);
            cell.setCellValue("Number of Clinic Visits".toUpperCase());
            cell = row.createCell(cellnum++);
            cell.setCellValue("Number Pharmacy Visits".toUpperCase());
            cell = row.createCell(cellnum++);
            cell.setCellValue("Number of CD4 Tests".toUpperCase());
            cell = row.createCell(cellnum++);
            cell.setCellValue("Number of Viral Load Tests".toUpperCase());

            //Algorithm for SQL Query begins here...
            ArrayList<String> patient_names = new ArrayList<>();
            Scrambler scrambler = new Scrambler();

            //1. Select the distinct facility ids...
            if (facilityIds == "")
                query = "SELECT state.name as state, facility.name as facility, patient.patient_id, patient.surname, patient.other_names, patient.hospital_num, patient.date_registration, patient.date_started, patient.current_status, patient.date_current_status, patient.age, patient.age_unit, patient.gender FROM patient JOIN facility ON patient.facility_id = facility.facility_id JOIN state ON facility.state_id = state.state_id  WHERE facility.active = 1 ORDER BY patient_id ASC";
            else
                query = "SELECT state.name as state, facility.name as facility, patient.patient_id, patient.surname, patient.other_names, patient.hospital_num, patient.date_registration, patient.date_started, patient.current_status, patient.date_current_status, patient.age, patient.age_unit, patient.gender FROM patient JOIN facility ON patient.facility_id = facility.facility_id JOIN state ON facility.state_id = state.state_id  WHERE facility.active = 1 AND facility.facility_id IN(" + facilityIds + ") ORDER BY patient_id ASC";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                //System.out.println(resultSet.getString("facility"));
                //rownum = global_row;
                cellnum = 0;
                row = sheet.createRow(rownum++);
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("state") == "" ? "N/A" : resultSet.getString("state"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("facility") == "" ? "N/A" : resultSet.getString("facility"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("hospital_num") == "" ? "N/A" : resultSet.getString("hospital_num"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("date_registration") == "" ? "N/A" : resultSet.getString("date_registration"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("date_started") == "" ? "N/A" : resultSet.getString("date_started"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("current_status") == "" ? "N/A" : resultSet.getString("current_status"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("date_current_status") == "" ? "N/A" : resultSet.getString("date_current_status"));
                String age = resultSet.getString("age");
                String age_unit = resultSet.getString("age_unit");
                cell = row.createCell(cellnum++);
                cell.setCellValue(age + " " + age_unit);
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("gender") == "" ? "N/A" : resultSet.getString("gender"));

                System.out.println("Patient ID is: " + resultSet.getString("patient_id"));

                //Setting the patient IDS
                //patient_names.add(resultSet.getString("patient_id"));
                String patient_id = resultSet.getString("patient_id");
                //global_row = rownum;

                //Clinic Visits
                query = "SELECT count(date_visit) as report_count FROM clinic WHERE patient_id = '" + patient_id + "' GROUP BY patient_id ORDER BY patient_id ASC";
                preparedStatement = jdbcUtil.getStatement(query);
                ResultSet rs = preparedStatement.executeQuery();
                System.out.println("Patient ID in Clinic is: " + resultSet.getString("patient_id"));

                //cellnum = 9;
                cell = row.createCell(cellnum++);
                cell.setCellValue(0);
                if (rs.next()) {
                    cell.setCellValue(rs.getInt("report_count"));
                }

                //Pharmacy Visits
                query = "SELECT count(DISTINCT date_visit) as report_count from pharmacy WHERE patient_id = '" + patient_id + "' GROUP BY patient_id ORDER BY patient_id ASC";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();

                System.out.println("Patient ID in Pharmacy is: " + resultSet.getString("patient_id"));

                //cellnum = 10;
                cell = row.createCell(cellnum++);
                cell.setCellValue(0);
                if (rs.next()) {
                    cell.setCellValue(rs.getInt("report_count"));
                }

                //CD4
                query = "SELECT count(DISTINCT date_reported) as report_count from laboratory WHERE patient_id = '" + patient_id + "' AND labtest_id = 1 GROUP BY patient_id ORDER BY patient_id ASC";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();

                System.out.println("Patient ID in Lab CD4 is: " + resultSet.getString("patient_id"));

                //cellnum = 11;
                cell = row.createCell(cellnum++);
                cell.setCellValue(0);
                if (rs.next()) {
                    cell.setCellValue(rs.getInt("report_count"));
                }

                //Viral Load
                query = "SELECT count(DISTINCT date_reported) as report_count from laboratory WHERE patient_id = '" + patient_id + "' AND labtest_id = 16 GROUP BY patient_id ORDER BY patient_id ASC";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();

                System.out.println("Patient ID in Lab Viral Load is: " + resultSet.getString("patient_id"));

                //cellnum = 12;
                cell = row.createCell(cellnum++);
                cell.setCellValue(0);
                if (rs.next()) {
                    cell.setCellValue(rs.getInt("report_count"));
                }
            }

            //Flusher
            if (rownum % 100 == 0) {
                ((SXSSFSheet) sheet).flushRows(100); // retain 100 last rows and flush all others

                // ((SXSSFSheet)sheet).flushRows() is a shortcut for ((SXSSFSheet)sheet).flushRows(0),
                // this method flushes all rows
            }

            workbook.write(outputStream);
            outputStream.close();
            workbook.dispose();  // dispose of temporary files backing this workbook on disk
        } catch (Exception ignored) {
        }
        return outputStream;
    }

//    private void executeUpdate(String query) {
//        try {
//            preparedStatement = jdbcUtil.getStatement(query);
//            preparedStatement.executeUpdate();
//        }
//        catch (Exception exception) {
//            jdbcUtil.disconnectFromDatabase();  //disconnect from database
//        }        
//    }        
//    
//    private int getCount(String query) {
//       int count  = 0;
//       try {
//            preparedStatement = jdbcUtil.getStatement(query);
//            resultSet = preparedStatement.executeQuery();
//            if(resultSet.next()) {
//                count = resultSet.getInt("count");
//            }
//        }
//        catch (Exception exception) {
//            jdbcUtil.disconnectFromDatabase();  //disconnect from database
//        }
//        return count;
//    }      

    private Map<Integer, String> getMonthMap() {

        // Initialize the Month Map...
        Map<Integer, String> month_map = new HashMap<>();
        month_map.put(1, "Jan");
        month_map.put(2, "Feb");
        month_map.put(3, "Mar");
        month_map.put(4, "Apr");
        month_map.put(5, "May");
        month_map.put(6, "Jun");
        month_map.put(7, "Jul");
        month_map.put(8, "Aug");
        month_map.put(8, "Sep");
        month_map.put(10, "Oct");
        month_map.put(11, "Nov");
        month_map.put(12, "Dec");

        return month_map;
    }

    private Map<String, Integer> getReverseMonthMap() {

        // initialize the Month Map...
        Map<String, Integer> month_map = getMonthMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        return month_map;
    }

    private String workMonth(String year, String month) {
        String return_value = year;

        try {
            if (month == "Apr" || month == "Jun" || month == "Sep" || month == "Nov") {
                return_value += "-" + getReverseMonthMap().get(month) + "-30";
            } else if (month == "Feb") {
                if (Integer.valueOf(year) % 4 == 0) { //leap year
                    return_value += "-" + getReverseMonthMap().get(month) + "-29";
                } else {
                    return_value += "-" + getReverseMonthMap().get(month) + "-28";
                }
            } else if (month != "Apr" && month != "Jun" && month != "Sep" && month != "Nov" && month != "Feb") {
                return_value += "-" + getReverseMonthMap().get(month) + "-31";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return return_value;
    }
}
