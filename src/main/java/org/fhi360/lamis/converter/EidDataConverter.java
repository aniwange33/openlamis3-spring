/**
 * @author aalozie
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Component
public class EidDataConverter implements ServletContextAware {
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

    public synchronized ByteArrayOutputStream convertExcel(String facilityIds, String state, Long userId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DateFormat dateFormatExcel = new SimpleDateFormat("dd-MMM-yyyy");
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);  // turn off auto-flushing and accumulate all rows in memory
        Sheet sheet = workbook.createSheet();

        try {
            jdbcUtil = new JDBCUtil();
            int rownum = 0;
            int cellnum = 0;
            Row row = sheet.createRow(rownum++);
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue("Treatment Unit");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Specimen Type");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Lab No");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of Received");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date Sample Collected");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of Assay");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date Result Reported");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date Result Dispatched");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Test Result");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Reason Sample Untestable");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Hospital No.");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Gender");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of Birth");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Age");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Age Unit");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Reason for PCR");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Rapid Test Done");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date Rapid Test");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Rapid Test Result");
            cell = row.createCell(cellnum++);
            cell.setCellValue("ART Received Mother");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Prophylax Received Mother");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Prophylax Received Child");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Ever Breast");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Breastfeding Now");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Cessation Age");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Cotrim");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Next Appointment");

            query = "SELECT specimen.*, eid.* FROM specimen JOIN eid ON specimen.labno = eid.labno " +
                    "ORDER BY specimen.treatment_unit_id";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cellnum = 0;
                row = sheet.createRow(rownum++);
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getLong("treatment_unit_id"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("specimen_type"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("labno"));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_received") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_received")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_collected") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_collected")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_assay") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_assay")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_reported") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_reported")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_dispatched") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_dispatched")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("result"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("reason_no_test"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("hospital_num"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("gender"));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_birth") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_birth")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(Integer.toString(resultSet.getInt("age")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("age_unit"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("reason_pcr"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("rapid_test_done"));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_rapid_test") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_rapid_test")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("rapid_test_result"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("mother_art_received"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("mother_prophylax_received"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("child_prophylax_received"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("breastfed_ever"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("breastfed_now"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(Integer.toString(resultSet.getInt("feeding_cessation_age")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("cotrim"));
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

}
