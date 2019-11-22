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
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataProfileConverter implements ServletContextAware {

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public synchronized ByteArrayOutputStream convertExcel(Long facilityId, String year, String month, Long userId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);
        // turn off auto-flushing and accumulate all rows in memory
        Sheet sheet = workbook.createSheet();
        try {
            int rownum = 0;
            int cellnum = 0;
            Row row = sheet.createRow(rownum++);
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue("Date");
            cell = row.createCell(cellnum++);
            cell.setCellValue("User");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Enrolment Records");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Clinic Records");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Pharmacy Records");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Laboratory Records");


            //Get all the users in this facility...
            query = "SELECT DISTINCT user_id, username FROM user where facility_id = '" + facilityId + "'";
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //id = Integer.toString(resultSet.getInt("user_id"));
                String username = resultSet.getString("username");

                cellnum = 0;
                row = sheet.createRow(rownum++);
                cell = row.createCell(cellnum++);
                cell.setCellValue(getMonthMap().get(month) + " " + year);
                cell = row.createCell(cellnum++);
                cell.setCellValue(username);

                //The Enrolment records
                query = "SELECT count(*) as enrolment_count FROM monitor where user_id = '" + userId + "' AND table_name = 'patient' AND YEAR(time_stamp) = '" + year + "' AND MONTH(time_stamp) = '" + month + "'";

                jdbcUtil = new JDBCUtil();
                preparedStatement = jdbcUtil.getStatement(query);
                resultSet = preparedStatement.executeQuery();

                cell = row.createCell(cellnum++);
                cell.setCellValue(0);
                if (resultSet.next()) {
                    int enrolment_count = resultSet.getInt("enrolment_count");
                    cell.setCellValue(enrolment_count);
                }

                //The Clinic records
                query = "SELECT count(*) as clinic_count FROM monitor where user_id = '" + userId +
                        "' AND table_name = 'clinic' AND YEAR(time_stamp) = '" + year + "' AND " +
                        "MONTH(time_stamp) = '" + month + "'";

                jdbcUtil = new JDBCUtil();
                preparedStatement = jdbcUtil.getStatement(query);
                resultSet = preparedStatement.executeQuery();

                cell = row.createCell(cellnum++);
                cell.setCellValue(0);
                if (resultSet.next()) {
                    int clinic_count = resultSet.getInt("clinic_count");
                    cell.setCellValue(clinic_count);
                }

                //The Pharmacy records
                query = "SELECT count(*) as pharmacy_count FROM monitor where user_id = '" + userId +
                        "' AND table_name = 'pharmacy' AND YEAR(time_stamp) = '" + year +
                        "' AND MONTH(time_stamp) = '" + month + "'";

                jdbcUtil = new JDBCUtil();
                preparedStatement = jdbcUtil.getStatement(query);
                resultSet = preparedStatement.executeQuery();

                cell = row.createCell(cellnum++);
                cell.setCellValue(0);
                if (resultSet.next()) {
                    int pharmacy_count = resultSet.getInt("pharmacy_count");
                    cell.setCellValue(pharmacy_count);
                }

                //The Laboratory records
                query = "SELECT count(*) as laboratory_count FROM monitor where user_id = '" + userId +
                        "' AND table_name = 'laboratory' AND YEAR(time_stamp) = '" + year +
                        "' AND MONTH(time_stamp) = '" + month + "'";

                jdbcUtil = new JDBCUtil();
                preparedStatement = jdbcUtil.getStatement(query);
                resultSet = preparedStatement.executeQuery();

                cell = row.createCell(cellnum++);
                cell.setCellValue(0);
                if (resultSet.next()) {
                    int laboratory_count = resultSet.getInt("laboratory_count");
                    cell.setCellValue(laboratory_count);
                }
            }

            if (rownum % 100 == 0) {
                ((SXSSFSheet) sheet).flushRows(100); // retain 100 last rows and flush all others

                // ((SXSSFSheet)sheet).flushRows() is a shortcut for ((SXSSFSheet)sheet).flushRows(0),
                // this method flushes all rows
            }

            workbook.write(outputStream);
            outputStream.close();
            workbook.dispose();  // dispose of temporary files backing this workbook on disk
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return outputStream;
    }

    private void executeUpdate(String query) {
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private int getCount(String query) {
        int count = 0;
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return count;
    }

    private String getContextPath() {
        String contextPath = servletContext.getInitParameter("contextPath");
        //String contextPath = ServletActionContext.getServletContext().getRealPath(File.separator).replace("\\", "/");
        return contextPath;
    }

    private Map<String, String> getMonthMap() {

        // Initialize the Month Map...
        Map<String, String> month_map = new HashMap<>();
        month_map.put("01", "Jan");
        month_map.put("02", "Feb");
        month_map.put("03", "Mar");
        month_map.put("04", "Apr");
        month_map.put("05", "May");
        month_map.put("06", "Jun");
        month_map.put("07", "Jul");
        month_map.put("08", "Aug");
        month_map.put("09", "Sep");
        month_map.put("10", "Oct");
        month_map.put("11", "Nov");
        month_map.put("12", "Dec");

        return month_map;
    }

}
