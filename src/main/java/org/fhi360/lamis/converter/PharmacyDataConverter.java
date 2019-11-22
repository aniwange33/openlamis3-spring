/**
 * @author aalozie
 */

package org.fhi360.lamis.converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.ConversionUtil;
import org.fhi360.lamis.utility.FileUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import static org.fhi360.lamis.utility.ConversionUtil.getCount;

@Component
public class PharmacyDataConverter implements ServletContextAware {
    private final JdbcTemplate jdbcTemplate;
    private final ApplicationProperties properties;

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public PharmacyDataConverter(JdbcTemplate jdbcTemplate, ApplicationProperties properties) {
        this.jdbcTemplate = jdbcTemplate;
        this.properties = properties;
    }

    public synchronized ByteArrayOutputStream convertExcel(Long userId, String facilityIds, String state,
                                                           String option, Integer aspect, boolean hasRequest) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String[] stateName = {"N/A"};

        DateFormat dateFormatExcel = new SimpleDateFormat("dd-MMM-yyyy");

        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);  // turn off auto-flushing and accumulate all rows in memory
        Sheet sheet = workbook.createSheet();

        try {
            String query = "SELECT MAX(visit) AS count FROM (SELECT patient_id, COUNT(DISTINCT date_visit) " +
                    "AS visit FROM pharmacy WHERE facility_id IN (" + facilityIds + ") GROUP BY facility_id," +
                    " patient_id) AS t1";
            int max_col = getCount(query);

            final int[] rownum = {0};
            final int[] cellnum = {0};
            final Row[] row = {sheet.createRow(rownum[0]++)};
            final Cell[] cell = {row[0].createCell(cellnum[0]++)};
            cell[0].setCellValue("State");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("LGA");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Facility Name");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Facility Id");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Patient Id");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Hospital Num");

            for (int i = 1; i <= max_col; i++) {
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("Date Visit" + i);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("Regimen Line" + i);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("Regimen" + i);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("Refill" + i);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("Next Appoint" + i);
            }

            query = "SELECT DISTINCT pharmacy.facility_id, pharmacy.patient_id, pharmacy.date_visit, " +
                    "pharmacy.regimentype_id, pharmacy.regimen_id, pharmacy.duration, pharmacy.next_appointment," +
                    " patient.hospital_num FROM pharmacy JOIN patient ON pharmacy.patient_id = " +
                    "patient.patient_id WHERE pharmacy.facility_id IN (" + facilityIds + ") " +
                    "ORDER BY pharmacy.facility_id, pharmacy.patient_id, pharmacy.date_visit";


            final long[] facilityId = {0};
            final long[] patientId = {0};
            jdbcTemplate.query(query, resultSet -> {
                if (resultSet.getLong("facility_id") != facilityId[0] ||
                        resultSet.getLong("patient_id") != patientId[0]) {
                    cellnum[0] = 0;
                    row[0] = sheet.createRow(rownum[0]++);
                    facilityId[0] = resultSet.getLong("facility_id");
                    Map facility = ConversionUtil.getFacility(facilityId[0]);
                    cell[0] = row[0].createCell(cellnum[0]++);
                    stateName[0] = facility.get("state").toString();
                    cell[0].setCellValue((String) facility.get("state"));
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue((String) facility.get("lga"));
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue((String) facility.get("facilityName"));
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue(resultSet.getLong("facility_id"));
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue(resultSet.getLong("patient_id"));
                    patientId[0] = resultSet.getLong("patient_id");
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue(resultSet.getString("hospital_num"));
                }
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("date_visit") == null) ? "" : dateFormatExcel.format(resultSet.getDate("date_visit")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getLong("regimentype_id"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getLong("regimen_id"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getInt("duration"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("next_appointment") == null) ? "" : dateFormatExcel.format(resultSet.getDate("next_appointment")));

                if (rownum[0] % 100 == 0) {
                    try {
                        ((SXSSFSheet) sheet).flushRows(100); // retain 100 last rows and flush all others
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // ((SXSSFSheet)sheet).flushRows() is a shortcut for ((SXSSFSheet)sheet).flushRows(0),
                    // this method flushes all rows
                }
            });

            if (option.equals("cron")) {
                if (!stateName[0].equals("N/A")) {
                    String directory = properties.getContextPath() + "transfer/conversions/" + stateName[0] + "/";
                    FileUtil fileUtil = new FileUtil();
                    fileUtil.makeDir(directory);

                    String fileName = Constants.Conversion.aspects[aspect].toLowerCase() + ".xlsx";
                    FileOutputStream fos = new FileOutputStream(new File(directory + fileName));
                    workbook.write(fos);
                    outputStream.close();
                    workbook.dispose();  // dispose of temporary files backing this workbook on disk

                }
            } else {
                workbook.write(outputStream);
                outputStream.close();
                workbook.dispose();  // dispose of temporary files backing this workbook on disk
            }
        } catch (Exception exception) {
            //resultSet = null;
            exception.printStackTrace();
        }
        return outputStream;
    }

    private void executeUpdate(String query, JDBCUtil jdbcUtil) {
        try {
            PreparedStatement preparedStatement1 = jdbcUtil.getStatement(query);
            preparedStatement1.executeUpdate();
        } catch (Exception exception) {
            //jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }


    private String getContextPath() {
        String contextPath = servletContext.getInitParameter("contextPath");
        //String contextPath = ServletActionContext.getServletContext().getRealPath(File.separator).replace("\\", "/");
        return contextPath;
    }

}
