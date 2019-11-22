

package org.fhi360.lamis.converter;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ClinicDataConverter implements ServletContextAware {
    private final JdbcTemplate jdbcTemplate;
    private final ApplicationProperties properties;

    public ClinicDataConverter(JdbcTemplate jdbcTemplate, ApplicationProperties properties) {
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

    public synchronized ByteArrayOutputStream convertExcel(String facilityIds,
                                                           String state, long userIds, String option,
                                                           Integer aspect) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String[] stateName = {"N/A"};
        String contextPath = properties.getContextPath();

        DateFormat dateFormatExcel = new SimpleDateFormat("dd-MMM-yyyy");

        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);  // turn off auto-flushing and accumulate all rows in memory
        Sheet sheet = workbook.createSheet();

        try {
            String query = "SELECT MAX(visit) AS count FROM (SELECT patient_id, COUNT(DISTINCT date_visit) AS " +
                    "visit FROM clinic WHERE facility_id IN (" + facilityIds + ") AND commence = 0 " +
                    "GROUP BY facility_id, patient_id) AS t1";
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
                cell[0].setCellValue("Clinic Stage" + i);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("Function Status" + i);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("TB Status" + i);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("Body Weight (kg)" + i);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("Height (cm)" + i);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("BP (mmHg)" + i);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("Pregnant" + i);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("LMP" + i);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("Breastfeeding" + i);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue("Next Appoint" + i);
            }

            query = "SELECT DISTINCT clinic.facility_id, clinic.patient_id, clinic.date_visit, clinic.clinic_stage, " +
                    "clinic.func_status, clinic.tb_status, clinic.body_weight, clinic.height, clinic.bp, " +
                    "clinic.pregnant, clinic.lmp, clinic.breastfeeding, clinic.next_appointment, " +
                    "patient.hospital_num FROM clinic JOIN patient ON clinic.patient_id = patient.patient_id WHERE " +
                    "clinic.facility_id IN (" + facilityIds + ") AND clinic.commence = 0 ORDER BY clinic.facility_id," +
                    " clinic.patient_id, clinic.date_visit";

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
                cell[0].setCellValue((resultSet.getDate("date_visit") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_visit")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("clinic_stage"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("func_status"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("tb_status"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(Double.toString(resultSet.getDouble("body_weight")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(Double.toString(resultSet.getDouble("height")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("bp"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(Integer.toString(resultSet.getInt("pregnant")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("lmp") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("lmp")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(Integer.toString(resultSet.getInt("breastfeeding")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("next_appointment") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("next_appointment")));

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
                    String directory = contextPath + "transfer/conversions/" + stateName[0] + "/";
                    FileUtil fileUtil = new FileUtil();
                    fileUtil.makeDir(directory);

                    String fileName = Constants.Conversion.aspects[aspect].toLowerCase() + ".xlsx";
                    FileOutputStream fos = new FileOutputStream(new File(directory + fileName));
                    workbook.write(fos);
                    outputStream.close();
                    workbook.dispose();  // dispose of temporary files backing this workbook on disk

                    LOG.debug("Completed for {}", stateName[0]);
                }
            } else {
                workbook.write(outputStream);
                outputStream.close();
                workbook.dispose();
            }
        } catch (Exception exception) {
            //resultSet = null;
            exception.printStackTrace();
        }
        return outputStream;
    }

}
