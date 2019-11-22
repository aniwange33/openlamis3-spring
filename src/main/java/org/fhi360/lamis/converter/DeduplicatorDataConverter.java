/**
 * @author aalozie
 */

package org.fhi360.lamis.converter;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.fhi360.lamis.service.Deduplicator;
import org.fhi360.lamis.utility.FileUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.Scrambler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Component
@Slf4j
public class DeduplicatorDataConverter implements ServletContextAware {

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Scrambler scrambler;
    private Boolean viewIdentifier;
    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;

        if (servletContext.getAttribute("viewIdentifier") != null) {
            this.viewIdentifier = (Boolean) servletContext.getAttribute("viewIdentifier");
        }
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public DeduplicatorDataConverter() {
        this.scrambler = new Scrambler();
    }

    public synchronized ByteArrayOutputStream convertExcel(Long userId) {
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
            cell.setCellValue("Patient ID");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Hospital Num");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Unique ID");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Surname");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Other Names");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Gender");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of Birth");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Address");
            cell = row.createCell(cellnum++);
            cell.setCellValue("ART Status");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Encounter");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Retain Patient");

            query = "SELECT patient_id, hospital_num, unique_id, surname, other_names, gender, date_birth, " +
                    "address, current_status FROM duplicate ORDER BY hospital_num";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            int retain = 1;
            while (resultSet.next()) {
                int count = 0;
                ResultSet rs = new Deduplicator().executeQuery("SELECT COUNT(DISTINCT date_visit) AS count " +
                        "FROM clinic WHERE patient_id = " + resultSet.getLong("patient_id"));
                if (rs.next()) count = count + rs.getInt("count");
                rs = new Deduplicator().executeQuery("SELECT COUNT(DISTINCT date_visit) AS count FROM " +
                        "pharmacy WHERE patient_id = " + resultSet.getLong("patient_id"));
                if (rs.next()) count = count + rs.getInt("count");
                rs = new Deduplicator().executeQuery("SELECT COUNT(DISTINCT date_reported) AS count FROM " +
                        "laboratory WHERE patient_id = " + resultSet.getLong("patient_id"));
                if (rs.next()) count = count + rs.getInt("count");

                cellnum = 0;
                String surname = scrambler.unscrambleCharacters(resultSet.getString("surname"));
                String other_names = scrambler.unscrambleCharacters(resultSet.getString("other_names"));
                String address = scrambler.unscrambleCharacters(resultSet.getString("address"));

                row = sheet.createRow(rownum++);
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getLong("patient_id"));
                cell = row.createCell(cellnum++);
                sheet.setColumnHidden(0, true);
                cell.setCellValue(resultSet.getString("hospital_num"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("unique_id"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(surname);
                cell = row.createCell(cellnum++);
                cell.setCellValue(other_names);
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("gender"));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_birth") == null) ? "" : dateFormatExcel.format(resultSet.getDate("date_birth")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(address);
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("current_status"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(count);
                cell = row.createCell(cellnum++);
                cell.setCellValue(retain);
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
