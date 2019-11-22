/**
 * @author AALOZIE
 */

package org.fhi360.lamis.converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class PatientOutcomeConverter implements ServletContextAware {
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;

    public PatientOutcomeConverter() {

    }

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public synchronized ByteArrayOutputStream convertExcel(String facilityIds, String state, Long userId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArrayList<Map<String, Object>> patientList = new ArrayList<>();
        try {
            jdbcUtil = new JDBCUtil();
            executeUpdate("DROP TABLE IF EXISTS cohort");
            executeUpdate("CREATE TEMPORARY TABLE cohort (facility_id bigint, patient_id bigint," +
                    " hospital_num varchar(25), gender varchar(7), age int, age_unit varchar(15), " +
                    "date_registration date, date_started date, mon12 varchar(75), mon24 varchar(75), " +
                    "mon36 varchar(75), mon48 varchar(75), dropped int)");
            executeUpdate("INSERT INTO cohort SELECT facility_id, patient_id, hospital_num, gender, age, " +
                    "age_unit, date_registration, date_started, '', '', '', '', 0 FROM patient WHERE " +
                    "facility_id IN (" + facilityIds + ")");

            String query = "SELECT patient_id, hospital_num, gender, age, age_unit, date_registration, current_status, " +
                    "date_current_status, date_started FROM patient WHERE facility_id IN (" + facilityIds + ")";
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("patientId", resultSet.getLong("patient_id"));
                map.put("dateRegistration", resultSet.getDate("date_registration"));
                patientList.add(map);
            }

            int month[] = {12, 24, 36, 48};
            for (int i = 0; i <= 3; i++) {
                for (Map map : patientList) {
                    long patientId = (Long) map.get("id");
                    Date dateRegistration = (Date) map.get("dateRegistration");
                    query = "SELECT patient_id FROM cohort WHERE patient_id = " + patientId + " AND dropped = 0";
                    preparedStatement = jdbcUtil.getStatement(query);
                    ResultSet resultSet1 = preparedStatement.executeQuery();
                    if (resultSet1.next()) {
                        String endDate = DateUtil.parseDateToString(DateUtil.addMonth(dateRegistration, month[i]), "yyyy-MM-dd");
                        query = "SELECT current_status FROM statushistory WHERE patient_id = " + patientId +
                                " AND date_current_status = SELECT MAX(date_current_status) FROM statushistory " +
                                "WHERE patient_id = " + patientId + " AND date_current_status <= '" + endDate + "'";
                        preparedStatement = jdbcUtil.getStatement(query);
                        ResultSet resultSet2 = preparedStatement.executeQuery();
                        if (resultSet2.next()) {
                            String currentStatus = resultSet2.getString("current_status");
                            checkOutcome(patientId, currentStatus, i);
                        }
                    }
                }
            }
            convert(outputStream, facilityIds, state, userId); // convert cohort table to MS excel
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return outputStream;
    }

    private void checkOutcome(long patientId, String currentStatus, int i) {
        String mon12 = "";
        String mon24 = "";
        String mon36 = "";
        String mon48 = "";
        String status = "";
        int drop = 0;

        if (currentStatus.trim().equals("ART Transfer Out")) {
            status = "Transfer Out";
            drop = 1;
        }
        if (currentStatus.trim().equals("Stopped Treatment")) {
            status = "Stopped Treatment";
            drop = 1;
        }
        if (currentStatus.trim().equals("Lost to Follow Up")) {
            status = "Lost";
            drop = 1;
        }
        if (currentStatus.trim().equals("Known Death")) {
            status = "Dead";
            drop = 1;
        }
        if (drop == 1) {
            if (i == 0) {
                mon12 = status;
            } else {
                if (i == 1) {
                    mon12 = "Alive";
                    mon24 = status;
                } else {
                    if (i == 2) {
                        mon12 = "Alive";
                        mon24 = "Alive";
                        mon36 = status;
                    } else {
                        mon12 = "Alive";
                        mon24 = "Alive";
                        mon36 = "Alive";
                        mon48 = status;
                    }
                }
            }
        } else {
            if (i == 0) {
                mon12 = "Alive";
            } else {
                if (i == 1) {
                    mon12 = "Alive";
                    mon24 = "Alive";
                } else {
                    if (i == 2) {
                        mon12 = "Alive";
                        mon24 = "Alive";
                        mon36 = "Alive";
                    } else {
                        mon12 = "Alive";
                        mon24 = "Alive";
                        mon36 = "Alive";
                        mon48 = "Alive";
                    }
                }
            }
        }
        System.out.println("Saving data ......");
        executeUpdate("UPDATE cohort SET mon12 = '" + mon12 + "', mon24 = '" + mon24 + "', mon36 = '" + mon36 + "', mon48 = '" + mon48 + "', dropped = " + drop + " WHERE patient_id = " + patientId);
    }

    private void convert(ByteArrayOutputStream outputStream, String facilityIds, String state, Long userId) {
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
            cell.setCellValue("Age");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Age Unit");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Gender");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Date Registration");
            cell = row.createCell(cellnum++);
            cell.setCellValue("ART Start Date");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Mon12");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Mon24");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Mon36");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Mon48");

            preparedStatement = jdbcUtil.getStatement("SELECT * FROM cohort ORDER BY facility_id, patient_id");
            ResultSet resultSet = preparedStatement.executeQuery();

            long facilityId = 0;
            long patientId = 0;
            while (resultSet.next()) {
                if (resultSet.getLong("facility_id") != facilityId || resultSet.getLong("patient_id") != patientId) {
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
                cell.setCellValue(Integer.toString(resultSet.getInt("age")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("age_unit"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("gender"));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_registration") == null) ? "" : dateFormatExcel.format(resultSet.getDate("date_registration")));
                cell = row.createCell(cellnum++);
                cell.setCellValue((resultSet.getDate("date_started") == null) ? "" : dateFormatExcel.format(resultSet.getDate("date_started")));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("mon12"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("mon24"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("mon36"));
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("mon48"));

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
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }
}
