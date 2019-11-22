/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author user1
 */
@Component
public class ViralLoadSummaryConverter implements ServletContextAware {

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet, resultSet2;
    private long userId;

    public ViralLoadSummaryConverter() {

    }

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public synchronized ByteArrayOutputStream convertExcel(Long userId, String facilityIds, String state1, String reportingDateBegin1, String reportingDateEnd1) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String state = state1.toLowerCase();


        String reportingDateBegin = DateUtil.formatDateString(reportingDateBegin1, "MM/dd/yyyy", "yyyy-MM-dd");
        String reportingDateEnd = DateUtil.formatDateString(reportingDateEnd1, "MM/dd/yyyy", "yyyy-MM-dd");

        System.out.println("date con: " + reportingDateBegin);
        System.out.println("date con: " + reportingDateEnd);

        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);  // turn off auto-flushing and accumulate all rows in memory
        Sheet sheet = workbook.createSheet();

        try {
            jdbcUtil = new JDBCUtil();

            int rownum = 0;
            int cellnum = 0;
            Row row = sheet.createRow(rownum++);
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue("Facility");
            cell = row.createCell(cellnum++);
            cell.setCellValue("No of Viral Load Test");
            cell = row.createCell(cellnum++);
            cell.setCellValue("No of Patients Test");

            query = "SELECT COUNT(*) AS total, laboratory.facility_id, facility.name FROM laboratory JOIN facility ON laboratory.facility_id = facility.facility_id WHERE laboratory.facility_id IN (" + facilityIds + ")"
                    + " AND laboratory.date_reported >= '" + reportingDateBegin + "' AND laboratory.date_reported <= '" + reportingDateEnd + "' AND laboratory.labtest_id = 16 GROUP BY facility.facility_id ORDER BY facility.name";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long facilityId = resultSet.getLong("facility_id");

                cellnum = 0;
                row = sheet.createRow(rownum++);
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("name"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getInt("total"));

                // Count the number of patient tested during the reporting period
                query = "SELECT COUNT(DISTINCT patient_id) AS subtotal FROM laboratory WHERE facility_id = " + facilityId
                        + " AND laboratory.date_reported >= '" + reportingDateBegin + "' AND laboratory.date_reported <= '" + reportingDateEnd + "' AND laboratory.labtest_id = 16";
                preparedStatement = jdbcUtil.getStatement(query);
                resultSet2 = preparedStatement.executeQuery();
                if (resultSet2.next()) {
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(resultSet2.getInt("subtotal"));
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
}
