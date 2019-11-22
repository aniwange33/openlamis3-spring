/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @author iduruanyanwu
 */
package org.fhi360.lamis.converter;

import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

@Component
public class SummaryFormConverter implements ServletContextAware {

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private long userId;
    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }


    public SummaryFormConverter() {

    }

//    public void convertByOption(){
//        
//    }

    public synchronized ByteArrayOutputStream convertExcel(String facilityIds, String yearId, String state1, Long userId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int global_row = 0;

        DateFormat dateFormatExcel = new SimpleDateFormat("dd-MMM-yyyy");
        String contextPath = servletContext.getInitParameter("contextPath");

        String state = state1.toLowerCase();

        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);  // turn off auto-flushing and accumulate all rows in memory
        Sheet sheet = workbook.createSheet();

        try {
            jdbcUtil = new JDBCUtil();

            //We Define the first row, ie the headers...
            int rownum = 0;
            int cellnum = 0;
            Row row = sheet.createRow(rownum++);
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue("FACILITY");

            //Build the Excel file headers...
            ArrayList<Integer> month_array = new ArrayList<>();
            for (int i = 1; i < 13; i++) {
                month_array.add(i);

                cell = row.createCell(cellnum++);
                cell.setCellValue("PRE_ART_" + getMonthMap().get(i).toString().toUpperCase());
                cell = row.createCell(cellnum++);
                cell.setCellValue("TX_NEW_" + getMonthMap().get(i).toString().toUpperCase());
                cell = row.createCell(cellnum++);
                cell.setCellValue("TX_CURR_" + getMonthMap().get(i).toString().toUpperCase());
            }


            //Algorithm for SQL Query begins here...
            ArrayList<String> facility_names = new ArrayList<>();

            //1. Select the distinct facility ids...
            if (facilityIds == "")
                query = "SELECT DISTINCT facility.name as facility FROM facility JOIN patient ON patient.facility_id = facility.facility_id WHERE facility.active ORDER by facility.name ASC";
            else
                query = "SELECT DISTINCT facility.name as facility FROM facility JOIN patient ON patient.facility_id = facility.facility_id WHERE facility.active AND facility.facility_id IN(" + facilityIds + ") ORDER by facility.name ASC";

            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                cellnum = 0;
                row = sheet.createRow(rownum++);
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("facility"));
                facility_names.add(resultSet.getString("facility"));
            }


            //2. Select the various parameters to add...
            //Perform this in a loop for each month...
            int inter_num = 0;
            for (int ii = 0; ii < 12; ii++) {

                //PRE_ART
                int i = ii + 1;
                //System.out.println("The Count Now is: "+i);
                //System.out.println("The Count Now is: "+i+" and this is first");
                query = "SELECT DISTINCT facility.name as facility, count(patient.status_registration) as count FROM patient JOIN facility ON patient.facility_id = facility.facility_id WHERE YEAR(patient.date_registration) = '" + yearId + "' AND MONTH(patient.date_registration) = '" + month_array.get(ii) + "' AND patient.status_registration = 'HIV+ non ART' AND facility.active GROUP BY facility.name ORDER by facility.name ASC";
                preparedStatement = jdbcUtil.getStatement(query);
                resultSet = preparedStatement.executeQuery();

                Map<String, Integer> countsMap = new HashMap<>();
                while (resultSet.next()) {
                    countsMap.put(resultSet.getString("facility"), resultSet.getInt("count"));
                    //System.out.println(resultSet.getString("facility") +" - "+resultSet.getInt("count"));
                }

                cellnum = i + inter_num;
                for (int j = 0; j < facility_names.size(); j++) {
                    //row = sheet.createRow(rownum++);                  
                    cell = sheet.getRow(j + 1).createCell(cellnum);
                    //System.out.println(countsMap.get(facility_names.get(j)));
                    //System.out.println(sheet.getRow(j + 1).getCell(0).getStringCellValue());
                    if (countsMap.get(facility_names.get(j)) != null) {
                        cell.setCellValue(countsMap.get(facility_names.get(j)));
                    } else {
                        cell.setCellValue(0);
                    }
                }

                //TX_NEW
                //System.out.println("The Count Now is: "+i+" and this is second");
                inter_num++;
                query = "SELECT DISTINCT facility.name as facility, count(status_registration) as count FROM patient JOIN facility ON patient.facility_id = facility.facility_id WHERE YEAR(date_started) = '" + yearId + "' AND MONTH(date_started) = '" + month_array.get(ii) + "' AND status_registration='ART Transfer In' AND facility.active GROUP BY facility.name ORDER by facility.name ASC";
                preparedStatement = jdbcUtil.getStatement(query);
                resultSet = preparedStatement.executeQuery();

                Map<String, Integer> countsMap_2 = new HashMap<>();
                while (resultSet.next()) {
                    countsMap_2.put(resultSet.getString("facility"), resultSet.getInt("count"));
                }

                cellnum = i + inter_num;
                for (int j = 0; j < facility_names.size(); j++) {
                    //row = sheet.createRow(rownum++);
                    cell = sheet.getRow(j + 1).createCell(cellnum);
                    //System.out.println(sheet.getRow(j + 1).getCell(0).getStringCellValue());
                    //System.out.println(countsMap_2.get(facility_names.get(j)));
                    if (countsMap_2.get(facility_names.get(j)) != null) {
                        cell.setCellValue(countsMap_2.get(facility_names.get(j)));
                    } else {
                        cell.setCellValue(0);
                    }
                }

                //TX_CURR

                String period = workMonth(yearId, getMonthMap().get(month_array.get(ii)));
                inter_num++;
                query = "SELECT DISTINCT facility.name as facility, count(patient.current_status) as count FROM patient JOIN facility ON patient.facility_id = facility.facility_id WHERE ((current_status IN ('ART Start', 'ART Restart', 'ART Transfer In')) OR (current_status IN ('ART Transfer Out', 'Lost to Follow Up', 'Stopped Treatment', 'Known Death') AND date_current_status > '" + period + "')) AND date_started IS NOT NULL AND date_started <= '" + period + "' AND facility.active GROUP BY facility.name ORDER by facility.name ASC";
                preparedStatement = jdbcUtil.getStatement(query);
                resultSet = preparedStatement.executeQuery();

                Map<String, Integer> countsMap_3 = new HashMap<>();
                while (resultSet.next()) {
                    countsMap_3.put(resultSet.getString("facility"), resultSet.getInt("count"));
                }

                cellnum = i + inter_num;
                for (int j = 0; j < facility_names.size(); j++) {
                    //row = sheet.createRow(rownum++);
                    cell = sheet.getRow(j + 1).createCell(cellnum);
                    //System.out.println(countsMap_3.get(facility_names.get(j)));
                    //System.out.println(sheet.getRow(j + 1).getCell(0).getStringCellValue());
                    if (countsMap_3.get(facility_names.get(j)) != null) {
                        cell.setCellValue(countsMap_3.get(facility_names.get(j)));
                    } else {
                        cell.setCellValue(0);
                    }
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
            return outputStream;
        } catch (Exception exception) {
            resultSet = null;
            exception.printStackTrace();
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
        month_map.put(9, "Sep");
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
                if (getReverseMonthMap().get(month) < 10)
                    return_value += "-0" + getReverseMonthMap().get(month) + "-30";
                else if (getReverseMonthMap().get(month) >= 10)
                    return_value += "-" + getReverseMonthMap().get(month) + "-30";
            } else if (month == "Feb") {
                if (Integer.valueOf(year) % 4 == 0) { //leap year
                    if (getReverseMonthMap().get(month) < 10)
                        return_value += "-0" + getReverseMonthMap().get(month) + "-29";
                    else if (getReverseMonthMap().get(month) >= 10)
                        return_value += "-" + getReverseMonthMap().get(month) + "-29";
                } else {
                    if (getReverseMonthMap().get(month) < 10)
                        return_value += "-0" + getReverseMonthMap().get(month) + "-28";
                    else if (getReverseMonthMap().get(month) >= 10)
                        return_value += "-" + getReverseMonthMap().get(month) + "-28";
                }
            } else if (month != "Apr" && month != "Jun" && month != "Sep" && month != "Nov" && month != "Feb") {
                if (getReverseMonthMap().get(month) < 10)
                    return_value += "-0" + getReverseMonthMap().get(month) + "-31";
                else if (getReverseMonthMap().get(month) >= 10)
                    return_value += "-" + getReverseMonthMap().get(month) + "-31";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return return_value;
    }
}
