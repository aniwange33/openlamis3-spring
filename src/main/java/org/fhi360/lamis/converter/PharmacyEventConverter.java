
package org.fhi360.lamis.converter;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.Scrambler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author idris
 */
@Component
public class PharmacyEventConverter implements ServletContextAware {

    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private long userId;
    private Boolean viewIdentifier;
    private Scrambler scrambler;

    private long facilityId, patientId;


    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        if (servletContext.getAttribute("viewIdentifier") != null) {
            this.viewIdentifier = (Boolean) servletContext.getAttribute("viewIdentifier");
        }
    }

    public PharmacyEventConverter() {
        this.scrambler = new Scrambler();
    }

    public synchronized ByteArrayOutputStream convertExcel(Long userId, String facilityIds, String state) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DateFormat dateFormatExcel = new SimpleDateFormat("dd-MMM-yyyy");

        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);  // turn off auto-flushing and accumulate all rows in memory
        workbook.setCompressTempFiles(true); // temp files will be gzipped
        Sheet sheet = workbook.createSheet();

        try {
            jdbcUtil = new JDBCUtil();

            int rownum = 0;
            int cellnum = 0;
            Row row = sheet.createRow(rownum++);
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue("Facility Id");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Facility Name");
            cell = row.createCell(cellnum++);
            cell.setCellValue("State");
            cell = row.createCell(cellnum++);
            cell.setCellValue("LGA");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Patient Id");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Hospital Num");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Unique Id");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date Birth");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Age");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Age Unit");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Gender");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Weight");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Height");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Type of Encounter");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Encounter Date");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Did patient come with completed devolvement &/or encounter form from");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Did patient com with completed Pharmacy Order Forms for 4 months ARV refill at CP from the HF");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Did patient confirm that he/she was dispensed two months ARV refill at the health facility at the last visit");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Was chronic care screening services provided at the CP during the visit");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Was medication adherence assessment & counselling done at the CP during this visit");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Were prescribed ARVs dispensed at the CP during this visit");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Were prescribed concomitant drugs dispensed at the CP during the visit");

            cell = row.createCell(cellnum++);
            cell.setCellValue("Medicine dispensed #1");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Duration #1");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Qty Prescribed #1");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Qty Dispensed #1");

            cell = row.createCell(cellnum++);
            cell.setCellValue("Medicine dispensed #2");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Duration #2");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Qty Prescribed #2");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Qty Dispensed #2");

            cell = row.createCell(cellnum++);
            cell.setCellValue("Medicine dispensed #3");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Duration #3");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Qty Prescribed #3");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Qty Dispensed #3");

            cell = row.createCell(cellnum++);
            cell.setCellValue("Medicine dispensed #4");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Duration #4");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Qty Prescribed #4");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Qty Dispensed #4");

            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of next ARV refills");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of next appointment for lab & clinic evaluation at HF");

            cell = row.createCell(cellnum++);
            cell.setCellValue("Devolvement date");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Is patient stable on ART");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date ART started");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Has patient completed and signed the consent form for develvement");
            cell = row.createCell(cellnum++);
            cell.setCellValue("CD4 at start of ART");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Current CD4");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of current CD4");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Current Viral load");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of current Viral load");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Current clinical stage");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of current clinical stage");
            cell = row.createCell(cellnum++);
            cell.setCellValue("First ART Regimen");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Current ART Regimen");

            preparedStatement = jdbcUtil.getStatement(getQuery());
            resultSet = preparedStatement.executeQuery();

            facilityId = 0;
            patientId = 0;
            while (resultSet.next()) {
                if (resultSet.getLong("facility_id") != facilityId ||
                        resultSet.getLong("patient_id") != patientId) {
                    cellnum = 0;
                    row = sheet.createRow(rownum++);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(resultSet.getLong("facility_id"));
                    facilityId = resultSet.getLong("facility_id");
                    Map facility = getFacility(facilityId);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue((String) facility.get("facilityName"));
                    cell = row.createCell(cellnum++);
                    cell.setCellValue((String) facility.get("state"));
                    cell = row.createCell(cellnum++);
                    cell.setCellValue((String) facility.get("lga"));
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(resultSet.getLong("patient_id"));
                    patientId = resultSet.getLong("patient_id");
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(resultSet.getString("hospital_num"));
                }
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_birth") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_birth")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(Integer.toString(resultSet.getInt("age")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("age_unit"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("gender"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("marital_status"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("education"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("occupation"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("state"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("lga"));
                cell = row.createCell(cellnum++);
                String address = StringUtils.trimToEmpty(resultSet.getString("address"));
                address = (viewIdentifier) ? scrambler.unscrambleCharacters(address) : address;
                address = StringUtils.capitalize(address);
                cell.setCellValue(address);
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("status_registration"));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_registration") == null) ? "" : dateFormatExcel.format(resultSet.getDate("date_registration")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("current_status"));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_current_status") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_current_status")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_started") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_started")));

                //Adding baseline data
                preparedStatement = jdbcUtil.getStatement("SELECT * FROM clinic WHERE facility_id = " +
                        facilityId + " AND patient_id = " + patientId + " AND commence = 1");
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(Double.toString(rs.getDouble("cd4")));
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(Double.toString(rs.getDouble("cd4p")));
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(rs.getString("clinic_stage"));
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(rs.getString("func_status"));
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(Double.toString(rs.getDouble("body_weight")));
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(Double.toString(rs.getDouble("height")));
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(rs.getString("regimenType") == null ? "" :
                            rs.getString("regimenType"));
                    cell = row.createCell(cellnum++);
                    //cell.setCellValue(rs.getString("regimen") == null? "" : regimenResolver.getRegimen(rs.getString("regimen")));                    
                } else {
                    cell = row.createCell(cellnum++);
                    cell = row.createCell(cellnum++);
                    cell = row.createCell(cellnum++);
                    cell = row.createCell(cellnum++);
                    cell = row.createCell(cellnum++);
                    cell = row.createCell(cellnum++);
                    cell = row.createCell(cellnum++);
                    cell = row.createCell(cellnum++);
                    cell = row.createCell(cellnum++);
                    cell = row.createCell(cellnum++);
                }

                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("last_clinic_stage"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(Double.toString(resultSet.getDouble("last_cd4")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(Double.toString(resultSet.getDouble("last_cd4p")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_last_cd4") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_last_cd4")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(Double.toString(resultSet.getDouble("last_viral_load")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_last_viral_load") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_last_viral_load")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_last_refill") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_last_refill")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_next_refill") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_next_refill")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_last_clinic") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_last_clinic")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_next_clinic") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_next_clinic")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getInt("send_message"));

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

    private String getQuery() {
        String facilityIds = "";
        String query = "";
        return query;
    }

    private Map getFacility(long facilityId) {
        Map<String, Object> facilityMap = new HashMap<String, Object>();
        try {
            // fetch the required records from the database
            jdbcUtil = new JDBCUtil();
            String query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility JOIN lga ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE facility_id = " + facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                facilityMap.put("facilityName", rs.getString("name"));
                facilityMap.put("lga", rs.getString("lga"));
                facilityMap.put("state", rs.getString("state"));
            }
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return facilityMap;
    }

}
