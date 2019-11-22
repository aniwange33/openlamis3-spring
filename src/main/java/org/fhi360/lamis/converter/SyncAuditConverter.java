/**
 * @author aalozie
 */

package org.fhi360.lamis.converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
public class SyncAuditConverter implements ServletContextAware {

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public SyncAuditConverter() {

    }

    public synchronized ByteArrayOutputStream convertExcel(Long userId, String facilityIds, String state1) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String state = state1.toLowerCase();
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
            cell.setCellValue("No. of Registration");
            cell = row.createCell(cellnum++);
            cell.setCellValue("No. of Clinic");
            cell = row.createCell(cellnum++);
            cell.setCellValue("No. of Pharmacy");
            cell = row.createCell(cellnum++);
            cell.setCellValue("No. of Lab");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Total SMS Consent");

            query = "SELECT DISTINCT facility_id, name FROM facility WHERE facility_id IN (" + facilityIds + ") " +
                    "ORDER BY name";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long facilityId = resultSet.getLong("facility_id");

                cellnum = 0;
                row = sheet.createRow(rownum++);
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("name"));

                query = "SELECT COUNT(*) AS count FROM (SELECT DISTINCT facility_id, patient_id FROM patient WHERE MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE()) AND facility_id = " + facilityId + ")";
                int newRecPatient = getCount(query);

                query = "SELECT COUNT(*) AS count FROM (SELECT DISTINCT facility_id, patient_id, date_visit FROM clinic WHERE MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE()) AND facility_id = " + facilityId + ")";
                int newRecClinic = getCount(query);

                query = "SELECT COUNT(*) AS count FROM (SELECT DISTINCT facility_id, patient_id, date_visit FROM pharmacy WHERE MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE()) AND facility_id = " + facilityId + ")";
                int newRecPharm = getCount(query);

                query = "SELECT COUNT(*) AS count FROM (SELECT DISTINCT facility_id, patient_id, date_reported" +
                        " FROM laboratory WHERE MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = " +
                        "YEAR(CURDATE()) AND facility_id = " + facilityId + ")";
                int newRecLab = getCount(query);

                query = "SELECT COUNT(*) AS count FROM (SELECT DISTINCT facility_id, patient_id FROM patient " +
                        "WHERE send_message != 0 AND facility_id = " + facilityId + ")";
                int smsConsent = getCount(query);

                cell = row.createCell(cellnum++);
                cell.setCellValue(newRecPatient);

                cell = row.createCell(cellnum++);
                cell.setCellValue(newRecClinic);

                cell = row.createCell(cellnum++);
                cell.setCellValue(newRecPharm);

                cell = row.createCell(cellnum++);
                cell.setCellValue(newRecLab);

                cell = row.createCell(cellnum++);
                cell.setCellValue(smsConsent);
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
        ResultSet rs;
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return count;
    }

}
