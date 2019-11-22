/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.converter;

import lombok.extern.slf4j.Slf4j;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author user10
 */
@Component
@Slf4j
public class DqaReportConverter implements ServletContextAware {

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private long userId;
    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public synchronized ByteArrayOutputStream convertExcel(String facilityIds, String state, Long userId) {
        ByteArrayOutputStream outputStream =new ByteArrayOutputStream();

        DateFormat dateFormatExcel = new SimpleDateFormat("dd-MMM-yyyy");
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);  // turn off auto-flushing and accumulate all rows in memory
        Sheet sheet = workbook.createSheet();

        try {
            jdbcUtil = new JDBCUtil();

            query = "SELECT MAX(visit) AS count FROM (SELECT patient_id, COUNT(DISTINCT date_visit) AS visit " +
                    "FROM pharmacy WHERE facility_id IN (" + facilityIds + ") GROUP BY facility_id, patient_id) " +
                    "AS t1";
            int max_col = getCount(query);

            int rownum = 0;
            int cellnum = 0;
            Row row = sheet.createRow(rownum++);
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue("Facility Id");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Patient Id");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Hospital Num");

            for (int i = 1; i <= max_col; i++) {
                cell = row.createCell(cellnum++);
                cell.setCellValue("Date Visit" + i);
                cell = row.createCell(cellnum++);
                cell.setCellValue("Regimen Line" + i);
                cell = row.createCell(cellnum++);
                cell.setCellValue("Regimen" + i);
                cell = row.createCell(cellnum++);
                cell.setCellValue("Refill" + i);
                cell = row.createCell(cellnum++);
                cell.setCellValue("Next Appoint" + i);
            }

            query = "SELECT DISTINCT pharmacy.facility_id, pharmacy.patient_id, pharmacy.date_visit, " +
                    "pharmacy.regimentype_id, pharmacy.regimen_id, pharmacy.duration, pharmacy.next_appointment, " +
                    "patient.hospital_num FROM pharmacy JOIN patient ON pharmacy.patient_id = patient.patient_id " +
                    "WHERE pharmacy.facility_id IN (" + facilityIds + ") ORDER BY pharmacy.facility_id, " +
                    "pharmacy.patient_id, pharmacy.date_visit";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            long facilityId = 0;
            long patientId = 0;
            while (resultSet.next()) {
                if (resultSet.getLong("facility_id") != facilityId ||
                        resultSet.getLong("patient_id") != patientId) {
                    cellnum = 0;
                    row = sheet.createRow(rownum++);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(resultSet.getLong("facility_id"));
                    facilityId = resultSet.getLong("facility_id");
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(resultSet.getLong("patient_id"));
                    patientId = resultSet.getLong("patient_id");
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(resultSet.getString("hospital_num"));
                }
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_visit") == null) ? "" : dateFormatExcel.format(resultSet.getDate("date_visit")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getLong("regimentype_id"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getLong("regimen_id"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getInt("duration"));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("next_appointment") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("next_appointment")));

                if (rownum % 100 == 0) {
                    ((SXSSFSheet) sheet).flushRows(100); // retain 100 last rows and flush all others

                    // ((SXSSFSheet)sheet).flushRows() is a shortcut for ((SXSSFSheet)sheet).flushRows(0),
                    // this method flushes all rows
                }
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

}
