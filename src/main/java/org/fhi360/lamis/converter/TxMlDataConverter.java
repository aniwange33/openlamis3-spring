/**
 * @author aalozie
 */

package org.fhi360.lamis.converter;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.fhi360.lamis.model.RegimenResolver;
import org.fhi360.lamis.utility.Constants;
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

@Component
public class TxMlDataConverter implements ServletContextAware {
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private Scrambler scrambler;
    private RegimenResolver regimenResolver;
    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public TxMlDataConverter() {

        this.scrambler = new Scrambler();
        this.regimenResolver = new RegimenResolver();

    }

    public synchronized ByteArrayOutputStream convertExcel(Boolean viewIdentifier, String facilityId, Long userId, String outcome1, String state) {
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
            cell.setCellValue("Unique ID");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Surname");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Other Names");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date Birth");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Age");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Age Unit");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Gender");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Marital Status");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Education");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Occupation");
            cell = row.createCell(cellnum++);
            cell.setCellValue("State of Residence");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Lga of Residence");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Address");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Phone");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Care Entry Point");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of Confirmed HIV Test");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date Registration");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Status at Registration");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Current Status");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date Current Status");
            cell = row.createCell(cellnum++);

            //ARV information
            cell.setCellValue("ART Start Date");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Current Regimen Line");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Current Regimen");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of Last Refill");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Last Refill Duration (days)");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of Next Refill");

            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of Last Clinic");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Last Clinic Stage");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of Next Clinic");

            //Contact tracking information
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of Tracking");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Tracking Outcome");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Cause of Death");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date Agreed to Return");

            //VL information
            cell = row.createCell(cellnum++);
            cell.setCellValue("Last Viral Load");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date of Last Viral Load");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Viral Load Due Date");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Viral Load Type");

            String query = "SELECT patient.*, DATEDIFF(YEAR, date_birth, CURDATE()) AS age FROM patient WHERE facility_id = " + facilityId + " AND outcome IN ('" + Constants.TxMlStatus.TX_ML_DIED + "', '" + Constants.TxMlStatus.TX_ML_TRANSFER + "', '" + Constants.TxMlStatus.TX_ML_TRACED + "', '" + Constants.TxMlStatus.TX_ML_NOT_TRACED + "')";
            System.out.println("string query ..........." + query);
            String outcome = outcome1.trim();

            if (outcome.equalsIgnoreCase("All"))
                query = "SELECT patient.*, DATEDIFF(YEAR, date_birth, CURDATE()) AS age FROM patient WHERE facility_id = " + facilityId + " AND outcome IN ('" + Constants.TxMlStatus.TX_ML_DIED + "', '" + Constants.TxMlStatus.TX_ML_TRANSFER + "', '" + Constants.TxMlStatus.TX_ML_TRACED + "', '" + Constants.TxMlStatus.TX_ML_NOT_TRACED + "') ORDER BY current_status";
            if (outcome.equalsIgnoreCase(Constants.TxMlStatus.TX_ML_DIED))
                query = "SELECT patient.*, DATEDIFF(YEAR, date_birth, CURDATE()) AS age FROM patient WHERE facility_id = " + facilityId + " AND outcome = '" + Constants.TxMlStatus.TX_ML_DIED + "'";
            if (outcome.equalsIgnoreCase(Constants.TxMlStatus.TX_ML_TRANSFER))
                query = "SELECT patient.*, DATEDIFF(YEAR, date_birth, CURDATE()) AS age FROM patient WHERE facility_id = " + facilityId + " AND outcome = '" + Constants.TxMlStatus.TX_ML_TRANSFER + "'";
            if (outcome.equalsIgnoreCase(Constants.TxMlStatus.TX_ML_TRACED))
                query = "SELECT patient.*, DATEDIFF(YEAR, date_birth, CURDATE()) AS age FROM patient WHERE facility_id = " + facilityId + " AND outcome = '" + Constants.TxMlStatus.TX_ML_TRACED + "'";
            if (outcome.equalsIgnoreCase(Constants.TxMlStatus.TX_ML_NOT_TRACED))
                query = "SELECT patient.*, DATEDIFF(YEAR, date_birth, CURDATE()) AS age FROM patient WHERE facility_id = " + facilityId + " AND outcome = '" + Constants.TxMlStatus.TX_ML_NOT_TRACED + "'";

            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            long patientId = 0;
            while (resultSet.next()) {
                String regimentype1 = "";
                String regimentype2 = "";
                String regimen1 = "";
                String regimen2 = "";
                executeUpdate("DROP TABLE IF EXISTS commence");
                executeUpdate("CREATE TEMPORARY TABLE commence AS SELECT * FROM clinic WHERE facility_id = " + facilityId + " AND commence = 1");
                executeUpdate("CREATE INDEX idx_visit ON commence(patient_id)");
                if (resultSet.getLong("facility_id") != Long.parseLong(facilityId) || resultSet.getLong("patient_id") != patientId) {
                    cellnum = 0;
                    row = sheet.createRow(rownum++);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(resultSet.getLong("facility_id"));
                    facilityId = String.valueOf(resultSet.getLong("facility_id"));
                    Map facility = getFacility(Long.parseLong(facilityId));
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
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(resultSet.getString("unique_id"));
                }
                cell = row.createCell(cellnum++);
                String surname = resultSet.getObject("surname") == null ? "" : resultSet.getString("surname");
                surname = (viewIdentifier) ? scrambler.unscrambleCharacters(surname) : surname;
                surname = StringUtils.upperCase(surname);
                cell.setCellValue(surname);
                cell = row.createCell(cellnum++);
                String otherNames = resultSet.getObject("other_names") == null ? "" : resultSet.getString("other_names");
                otherNames = (viewIdentifier) ? scrambler.unscrambleCharacters(otherNames) : otherNames;
                otherNames = StringUtils.capitalize(otherNames);
                cell.setCellValue(otherNames);
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_birth") == null) ? "" : dateFormatExcel.format(resultSet.getDate("date_birth")));
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
                String address = resultSet.getString("address") == null ? "" : resultSet.getString("address");
                address = (viewIdentifier) ? scrambler.unscrambleCharacters(address) : address;
                address = StringUtils.capitalize(address);
                cell.setCellValue(address);
                cell = row.createCell(cellnum++);
                String phone = resultSet.getString("phone") == null ? "" : resultSet.getString("phone");
                phone = (viewIdentifier) ? scrambler.unscrambleNumbers(phone) : phone;
                cell.setCellValue(phone);
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("entry_point"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("date_confirmed_hiv"));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_registration") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_registration")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("status_registration"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("current_status"));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_current_status") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_current_status")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_started") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_started")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("regimenType") == null ? "" :
                        resultSet.getString("regimenType"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("regimen") == null ? "" :
                        regimenResolver.getRegimen());
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_last_refill") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_last_refill")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(Integer.toString(resultSet.getInt("last_refill_duration")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_next_refill") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_next_refill")));

                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_last_clinic") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_last_clinic")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("last_clinic_stage"));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_next_clinic") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_next_clinic")));
                cell = row.createCell(cellnum++);


                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_tracked") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_tracked")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("outcome"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("cause_death"));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("agreed_date") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("agreed_date")));
                cell = row.createCell(cellnum++);


                cell.setCellValue(Double.toString(resultSet.getDouble("last_viral_load")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_last_viral_load") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_last_viral_load")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("viral_load_due_date") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("viral_load_due_date")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("viral_load_type"));

                if (rownum % 100 == 0) {
                    ((SXSSFSheet) sheet).flushRows(100); // retain 100 last rows and flush all others

                    // ((SXSSFSheet)sheet).flushRows() is a shortcut for ((SXSSFSheet)sheet).flushRows(0),
                    // this method flushes all rows
                }
            }

            workbook.write(outputStream);
            outputStream.close();
            workbook.dispose();  // dispose of temporary files backing this workbook on diskse(servletContext.getContextPath())) fileUtil.copyFile(fileName, contextPath+"transfer/", servletContext.getContextPath()+"/transfer/");
            //resultSet = null;                        
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return outputStream;
    }

    private String getNrti(String regimen) {
        String nrti = regimen == null ? "" : "Other";
        if (regimen.contains("d4T")) {
            nrti = "D4T (Stavudine)";
        } else {
            if (regimen.contains("AZT")) {
                nrti = "AZT (Zidovudine)";
            } else {
                if (regimen.contains("TDF")) {
                    nrti = "TDF (Tenofovir)";
                } else {
                    if (regimen.contains("DDI")) {
                        nrti = "DDI (Didanosine)";
                    }
                }
            }
        }
        return nrti;
    }

    private String getNnrti(String regimen) {
        String nnrti = regimen == null ? "" : "Other";
        if (regimen.contains("EFV")) {
            nnrti = " EFV (Efavirenz)";
        } else {
            if (regimen.contains("NVP")) {
                nnrti = " NVP (Nevirapine)";
            }
        }
        return nnrti;
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

    private void executeUpdate(String query) {
        try {
            jdbcUtil = new JDBCUtil();
            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
