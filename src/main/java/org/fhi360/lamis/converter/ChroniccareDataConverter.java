/**
 * @author aalozie
 */

package org.fhi360.lamis.converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.config.ApplicationProperties;
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
public class ChroniccareDataConverter implements ServletContextAware {
    private final ApplicationProperties applicationProperties = ContextProvider.getBean(ApplicationProperties.class);
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private long userId;
    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }


    public synchronized ByteArrayOutputStream convertExcel(String facilityIds, String state1, long userId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DateFormat dateFormatExcel = new SimpleDateFormat("dd-MMM-yyyy");
        String contextPath = servletContext.getInitParameter("contextPath");
        String state = state1.toLowerCase();
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);
        // turn off auto-flushing and accumulate all rows in memory
        Sheet sheet = workbook.createSheet();

        try {
            jdbcUtil = new JDBCUtil();
            int rownum = 0;
            int cellnum = 0;
            Row row = sheet.createRow(rownum++);
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue("Facility Id");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Patient Id");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Hospital Num");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of Visit");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Client Type");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Current Status");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Clinical Stage");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Pregnancy Status");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Last CD4");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of Last CD4");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Last Viral Load");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of Last Viral Load");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Eligible for CD4");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Eligible for Last Viral Load");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Currently on TB treatment");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date Started TB");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Referred for TB Diagnosis");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Currently on IPT");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Received INH past 2 year");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Eligible for IPT");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Body Weight");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Height");
            cell = row.createCell(cellnum++);
            cell.setCellValue("BMI (Adult)");
            cell = row.createCell(cellnum++);
            cell.setCellValue("MUAC (under 5yrs)");
            cell = row.createCell(cellnum++);
            cell.setCellValue("MUAC Pregnant");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Hypertensive");
            cell = row.createCell(cellnum++);
            cell.setCellValue("First Hypertensive");
            cell = row.createCell(cellnum++);
            cell.setCellValue("BP above 140/90mmHg");
            cell = row.createCell(cellnum++);
            cell.setCellValue("BP Referred");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Diabetic");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Diabetic Referred");
            cell = row.createCell(cellnum++);
            cell.setCellValue("First Diabetic");
            cell = row.createCell(cellnum++);
            cell.setCellValue("DM Referred");

            query = "SELECT * FROM chroniccare WHERE facility_id IN (" + facilityIds + ") ORDER BY facility_id, patient_id, date_visit";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cellnum = 0;
                row = sheet.createRow(rownum++);
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getLong("facility_id"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getLong("patient_id"));
                cell = row.createCell(cellnum++);
                //cell.setCellValue(resultSet.getString("hospital_num"));                
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_visit") == null) ? "" : dateFormatExcel.format(resultSet.getDate("date_visit")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("client_type"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("current_status"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("clinic_stage"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("pregnancy_status"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(Double.toString(resultSet.getDouble("last_cd4")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_last_cd4") == null) ? "" : dateFormatExcel.format(resultSet.getDate("date_last_cd4")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(Double.toString(resultSet.getDouble("last_viral_load")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_last_viral_load") == null) ? "" : dateFormatExcel.format(resultSet.getDate("date_last_viral_load")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("eligible_cd4"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("eligible_viral_load"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("tb_treatment"));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_started_tb_treatment") == null) ? "" : dateFormatExcel.format(resultSet.getDate("date_started_tb_treatment")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("tb_referred"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("ipt"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("inh"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("eligible_ipt"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(Double.toString(resultSet.getDouble("body_weight")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(Double.toString(resultSet.getDouble("height")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("bmi"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(Double.toString(resultSet.getDouble("muac")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("muac_pediatrics"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("muac_pregnant"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("hypertensive"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("first_hypertensive"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("bp_above"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("bp_referred"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("diabetic"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("first_diabetic"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("dm_referred"));

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
