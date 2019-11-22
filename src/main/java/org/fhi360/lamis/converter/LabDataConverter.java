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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import static org.fhi360.lamis.utility.ConversionUtil.getCount;

@Component
public class LabDataConverter implements ServletContextAware {
    private final JdbcTemplate jdbcTemplate;
    private final ApplicationProperties properties;

    public LabDataConverter(JdbcTemplate jdbcTemplate, ApplicationProperties properties) {
        this.jdbcTemplate = jdbcTemplate;
        this.properties = properties;
    }

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public synchronized ByteArrayOutputStream convertExcel(long labtestId, long userId, String state, String facilityIds,
                                                           String option, Integer aspect, boolean hasRequest) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String[] stateName = {"N/A"};
        DateFormat dateFormatExcel = new SimpleDateFormat("dd-MMM-yyyy");
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);  // turn off auto-flushing and accumulate all rows in memory
        Sheet sheet = workbook.createSheet();

        try {

            String query = "SELECT MAX(visit) AS count FROM (SELECT patient_id, COUNT(DISTINCT date_reported) AS visit " +
                    "FROM laboratory WHERE facility_id IN (" + facilityIds + ") AND labtest_id = " +
                    labtestId + " GROUP BY facility_id, patient_id) AS t1";
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
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Test Id");

            for (int i = 1; i <= max_col; i++) {
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("Sample Collect" + i);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("Date Report" + i);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("Result" + i);
            }

            query = "SELECT DISTINCT laboratory.facility_id, laboratory.patient_id, laboratory.date_reported, " +
                    "laboratory.resultab, laboratory.resultpc, laboratory.labtest_id, laboratory.date_collected," +
                    " patient.hospital_num FROM laboratory JOIN patient ON laboratory.patient_id = " +
                    "patient.patient_id WHERE laboratory.facility_id IN (" + facilityIds + ") AND " +
                    "laboratory.labtest_id = " + labtestId + " ORDER BY laboratory.facility_id, " +
                    "laboratory.patient_id, laboratory.date_reported";

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
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue(resultSet.getString("labtest_id"));
                }
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("date_collected") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_collected")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("date_reported") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_reported")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getString("resultab") == null) ?
                        resultSet.getString("resultpc") :
                        resultSet.getString("resultab"));

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
            exception.printStackTrace();
        }
        return outputStream;
    }

    private String getContextPath() {
        String contextPath = servletContext.getInitParameter("contextPath");
        //String contextPath = ServletActionContext.getServletContext().getRealPath(File.separator).replace("\\", "/");
        return contextPath;
    }

}
