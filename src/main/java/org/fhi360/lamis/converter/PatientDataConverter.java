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
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.model.RegimenResolver;
import org.fhi360.lamis.model.dto.QueryDto;
import org.fhi360.lamis.utility.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

@Component
public class PatientDataConverter implements ServletContextAware {
    private final ApplicationProperties properties;
    private long userId;
    private Boolean viewIdentifier = false;
    private Scrambler scrambler;
    private RegimenResolver regimenResolver;
    private final JdbcTemplate jdbcTemplate;

    private String regimentype1, regimentype2, regimen1, regimen2;
    private long facilityId, patientId;

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public PatientDataConverter(ApplicationProperties properties, JdbcTemplate jdbcTemplate) {
        this.properties = properties;
        this.jdbcTemplate = jdbcTemplate;
        this.scrambler = new Scrambler();
        this.regimenResolver = new RegimenResolver();
    }


    public ByteArrayOutputStream convertExcel(QueryDto queryDto, String entity, Long casemanagerId, String state, Boolean viewIdentifier, String facilityIds, String option, Integer aspect, boolean hasRequest, long userId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String[] stateName = {"N/A"};
        DateFormat dateFormatExcel = new SimpleDateFormat("dd-MMM-yyyy");

        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);  // turn off auto-flushing and accumulate all rows in memory
        workbook.setCompressTempFiles(true); // temp files will be gzipped
        Sheet sheet = workbook.createSheet();
        try {

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
            cell[0].setCellValue("Unique ID");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Surname");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Other Names");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Date Birth");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Age");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Age Unit");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Gender");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Marital Status");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Education");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Occupation");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("State of Residence");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Lga of Residence");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Address");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Phone");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Care Entry Point");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Date of Confirmed HIV Test");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Date Registration");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Status at Registration");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Current Status");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Date Current Status");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("ART Start Date");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Baseline CD4");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Baseline CD4p");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Systolic BP");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Diastolic BP");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Baseline Clinic Stage");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Baseline Functional Status");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Baseline Weight (kg)");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Baseline Height (cm)");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("First Regimen Line");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("First Regimen");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("First NRTI");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("First NNRTI");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Current Regimen Line");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Current Regimen");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Current NRTI");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Current NNRTI");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Date Subsituted/Switched");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Date of Last Refill");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Last Refill Duration (days)");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Date of Next Refill");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Last Clinic Stage");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Date of Last Clinic");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Date of Next Clinic");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Last CD4");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Last CD4p");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Date of Last CD4");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Last Viral Load");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Date of Last Viral Load");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Viral Load Due Date");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Viral Load Type");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Date Tracked");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Outcome");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Cause of Death");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("Date Agreed to return");
            cell[0] = row[0].createCell(cellnum[0]++);
            cell[0].setCellValue("SMS consent");

            //Check if the url has a parameter for entity;
            String query;
            if (hasRequest) {
                if (entity != null) {
                    query = getQueryNotification(Integer.parseInt(entity), facilityIds);
                } else if (casemanagerId != null) {
                    query = getQueryCaseManagement(casemanagerId, Long.parseLong(facilityIds));
                } else {
                    query = getQuery(facilityIds, true, queryDto.getGender(),
                            queryDto.getAgeBegin(), queryDto.getAgeEnd(), state,
                            queryDto.getLga(), queryDto.getCurrentStatus(),
                            queryDto.getDateCurrentStatusBegin(),
                            queryDto.getDateCurrentStatusEnd(),
                            queryDto.getRegimentype(),
                            queryDto.getDateRegistrationBegin(),
                            queryDto.getDateRegistrationEnd(),
                            queryDto.getArtStartDateBegin(),
                            queryDto.getArtStartDateEnd(),
                            queryDto.getClinicStage(),
                            queryDto.getCd4Begin(),
                            queryDto.getCd4End(),
                            queryDto.getViralloadBegin(),
                            queryDto.getViralloadEnd());
                }
            } else {
                query = getQuery(facilityIds, false, queryDto.getGender(),
                        queryDto.getAgeBegin(), queryDto.getAgeEnd(), state,
                        queryDto.getLga(), queryDto.getCurrentStatus(),
                        queryDto.getDateCurrentStatusBegin(),
                        queryDto.getDateCurrentStatusEnd(),
                        queryDto.getRegimentype(),
                        queryDto.getDateRegistrationBegin(),
                        queryDto.getDateRegistrationEnd(),
                        queryDto.getArtStartDateBegin(),
                        queryDto.getArtStartDateEnd(),
                        queryDto.getClinicStage(),
                        queryDto.getCd4Begin(),
                        queryDto.getCd4End(),
                        queryDto.getViralloadBegin(),
                        queryDto.getViralloadEnd());
            }

            facilityId = 0;
            patientId = 0;
            Boolean finalViewIdentifier = viewIdentifier;
            jdbcTemplate.query(query, resultSet -> {
                regimentype1 = "";
                regimentype2 = "";
                regimen1 = "";
                regimen2 = "";
                if (resultSet.getLong("facility_id") != facilityId) {
                    executeUpdate("DROP VIEW IF EXISTS commence");
                    executeUpdate(" CREATE VIEW commence AS SELECT * FROM clinic WHERE facility_id = " +
                            resultSet.getLong("facility_id") + " AND commence = 1");
                    //executeUpdate("CREATE INDEX idx_visit ON commence(patient_id)");
                }
                if (resultSet.getLong("facility_id") != facilityId ||
                        resultSet.getLong("patient_id") != patientId) {
                    cellnum[0] = 0;
                    row[0] = sheet.createRow(rownum[0]++);
                    facilityId = resultSet.getLong("facility_id");
                    Map facility = ConversionUtil.getFacility(facilityId);
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
                    patientId = resultSet.getLong("patient_id");
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue(resultSet.getString("hospital_num"));
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue(resultSet.getString("unique_id"));

                    System.out.println("Hospital: " + facility.get("facilityName"));
                    System.out.println("Patient No: " + patientId);
                }
                cell[0] = row[0].createCell(cellnum[0]++);
                String surname = StringUtils.trimToEmpty(resultSet.getString("surname"));
                surname = (finalViewIdentifier) ? scrambler.unscrambleCharacters(surname) : surname;
                surname = StringUtils.upperCase(surname);
                cell[0].setCellValue(surname);
                cell[0] = row[0].createCell(cellnum[0]++);
                String otherNames = StringUtils.trimToEmpty(resultSet.getString("other_names"));
                otherNames = (finalViewIdentifier) ? scrambler.unscrambleCharacters(otherNames) : otherNames;
                otherNames = StringUtils.capitalize(otherNames);
                cell[0].setCellValue(otherNames);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("date_birth") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_birth")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(Integer.toString(resultSet.getInt("age")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("age_unit"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("gender"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("marital_status"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("education"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("occupation"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("state"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("lga"));
                cell[0] = row[0].createCell(cellnum[0]++);
                String address = resultSet.getString("address") == null ? "" : resultSet.getString("address");
                address = (finalViewIdentifier) ? scrambler.unscrambleCharacters(address) : address;
                address = StringUtils.capitalize(address);
                cell[0].setCellValue(address);
                cell[0] = row[0].createCell(cellnum[0]++);
                String phone = StringUtils.trimToEmpty(resultSet.getString("phone"));
                phone = (finalViewIdentifier) ? scrambler.unscrambleNumbers(phone) : phone;
                cell[0].setCellValue(phone);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("entry_point"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("date_confirmed_hiv"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("date_registration") == null) ? "" : dateFormatExcel.format(resultSet.getDate("date_registration")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("status_registration"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("current_status"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("date_current_status") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_current_status")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("date_started") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_started")));

                //Adding baseline data
                String query1 = "SELECT * FROM commence WHERE patient_id = " + patientId;
                boolean[] found = {false};
                jdbcTemplate.query(query1, rs -> {
                    found[0] = true;
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue(Double.toString(rs.getDouble("cd4")));
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue(Double.toString(rs.getDouble("cd4p")));
                    //Solve the BP
                    String[] bpData = (!"".equals(rs.getString("bp"))
                            && rs.getString("bp") != null) ?
                            rs.getString("bp").split("/") : new String[]{};
                    if (bpData.length == 2) {
                        cell[0] = row[0].createCell(cellnum[0]++);
                        cell[0].setCellValue(bpData[0]);
                        cell[0] = row[0].createCell(cellnum[0]++);
                        cell[0].setCellValue(bpData[1]);
                    } else {
                        cell[0] = row[0].createCell(cellnum[0]++);
                        cell[0].setCellValue("");
                        cell[0] = row[0].createCell(cellnum[0]++);
                        cell[0].setCellValue("");
                    }
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue(rs.getString("clinic_stage"));
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue(rs.getString("func_status"));
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue(Double.toString(rs.getDouble("body_weight")));
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue(Double.toString(rs.getDouble("height")));
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue(rs.getString("regimenType") == null ? "" :
                            rs.getString("regimenType"));
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue(rs.getString("regimen") == null ? "" :
                            regimenResolver.getRegimen());
                    cell[0] = row[0].createCell(cellnum[0]++);
                    String nrti = rs.getString("regimen") == null ? "" :
                            getNrti(rs.getString("regimen"));
                    cell[0].setCellValue(nrti);
                    cell[0] = row[0].createCell(cellnum[0]++);
                    String nnrti = rs.getString("regimen") == null ? "" :
                            getNnrti(rs.getString("regimen"));
                    cell[0].setCellValue(nnrti);
                    regimentype1 = rs.getString("regimenType") == null ? "" :
                            rs.getString("regimenType");
                    regimen1 = rs.getString("regimen") == null ? "" :
                            rs.getString("regimen");
                });
                if (!found[0]) {
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue("");
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue("");
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue("");
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue("");
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue("");
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue("");
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue("");
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue("");
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue("");
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue("");
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue("");
                    cell[0] = row[0].createCell(cellnum[0]++);
                    cell[0].setCellValue("");
                }

                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("regimenType") == null ? "" :
                        resultSet.getString("regimenType"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("regimen") == null ? "" :
                        regimenResolver.getRegimen());
                cell[0] = row[0].createCell(cellnum[0]++);
                String nrti = resultSet.getString("regimen") == null ? "" :
                        getNrti(resultSet.getString("regimen"));
                cell[0].setCellValue(nrti);
                cell[0] = row[0].createCell(cellnum[0]++);
                String nnrti = resultSet.getString("regimen") == null ? "" :
                        getNnrti(resultSet.getString("regimen"));
                cell[0].setCellValue(nnrti);

                //Determining when regimen was subsituted or switched
                cell[0] = row[0].createCell(cellnum[0]++);
                regimentype2 = resultSet.getString("regimenType") == null ? "" :
                        resultSet.getString("regimenType");
                regimen2 = resultSet.getString("regimen") == null ? "" :
                        resultSet.getString("regimen");
                if (!regimentype1.isEmpty() && !regimentype2.isEmpty() && !regimen1.isEmpty() &&
                        !regimen2.isEmpty()) {
                    //If regimen was substituted or swicted, get the date of change
//                    if (RegimenIntrospector.substitutedOrSwitched(regimen1, regimen2)) {
//                        RegimenHistory regimenhistory = RegimenIntrospector.getRegimenHistory(
//                                id, regimentype2, regimen2);
//                        System.out.println("Regimen history..." + regimenhistory.getDateVisit());
//                        cell[0].setCellValue(regimenhistory.getDateVisit() == null ? "" :
//                                dateFormatExcel.format(regimenhistory.getDateVisit()));
//                    }
                }
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("date_last_refill") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_last_refill")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(Integer.toString(resultSet.getInt("last_refill_duration")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("date_next_refill") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_next_refill")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("last_clinic_stage"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("date_last_clinic") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_last_clinic")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("date_next_clinic") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_next_clinic")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(Double.toString(resultSet.getDouble("last_cd4")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(Double.toString(resultSet.getDouble("last_cd4p")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("date_last_cd4") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_last_cd4")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(Double.toString(resultSet.getDouble("last_viral_load")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("date_last_viral_load") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_last_viral_load")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("viral_load_due_date") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("viral_load_due_date")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("viral_load_type"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("date_tracked") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("date_tracked")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("outcome"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getString("cause_death"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue((resultSet.getDate("agreed_date") == null) ? "" :
                        dateFormatExcel.format(resultSet.getDate("agreed_date")));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(resultSet.getInt("send_message"));

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

    private String getQuery(String facIds, boolean hasRequest, String gender, String ageBegin, String ageEnd, String state,
                            String lga, String currentStatus,
                            String dateCurrentStatusBegin,
                            String dateCurrentStatusEnd,
                            String regimentype,
                            String dateRegistrationBegin,
                            String dateRegistrationEnd,
                            String artStartDateBegin,
                            String artStartDateEnd,
                            String clinicStage,
                            String cd4Begin,
                            String cd4End,
                            String viralloadBegin,
                            String viralloadEnd

    ) {
        String facilityIds = null;
        String query;
        if (!hasRequest) {
            facilityIds = facIds;
            query = "SELECT DISTINCT patient.*, TIMESTAMPDIFF(YEAR, date_birth, CURDATE()) AS age FROM patient " +
                    "WHERE facility_id IN (" + facilityIds + ") ORDER BY facility_id, patient_id";
        } else {
            if (facilityIds != null) {
                facilityIds = facIds;
                System.out.println("Selected ids......" + facilityIds);
                query = "SELECT DISTINCT patient.*, TIMESTAMPDIFF(YEAR, date_birth, CURDATE()) AS age FROM patient " +
                        "WHERE facility_id IN (" + facilityIds + ") ORDER BY facility_id, patient_id";
            } else {
                query = "SELECT patient.*, TIMESTAMPDIFF(YEAR, date_birth, CURDATE()) AS age FROM patient WHERE " +
                        "facility_id IN (" + facilityIds + ")";

                if (!gender.trim().isEmpty() &&
                        !gender.trim().equals("--All--"))
                    query += " AND gender = '" + gender + "'";
                if (!ageBegin.trim().isEmpty() &&
                        !ageEnd.trim().isEmpty())
                    query += " AND age >= " + Integer.parseInt(ageBegin) + "" +
                            " AND age <= " + Integer.parseInt(ageEnd);
                if (!state.trim().isEmpty())
                    query += " AND state = '" + state + "'";
                if (!lga.trim().isEmpty())
                    query += " AND lga = '" + lga + "'";

                if (!currentStatus.trim().isEmpty() && !currentStatus.trim().equals("--All--")) {
                    String currentStatus1 = (currentStatus.trim().equals("HIV  non ART")) ? "HIV+ non ART" : currentStatus;
                    query += " AND current_status = '" + currentStatus1 + "'";
                }

                if (!dateCurrentStatusBegin.trim().isEmpty() &&
                        !dateCurrentStatusEnd.trim().isEmpty())
                    query += " AND date_current_status >= '" + DateUtil.parseStringToSqlDate(
                            dateCurrentStatusBegin, "MM/dd/yyyy") + "' " +
                            "AND date_current_status <= '" + DateUtil.parseStringToSqlDate(
                            dateCurrentStatusEnd, "MM/dd/yyyy") + "'";
                if (!regimentype.trim().isEmpty())
                    query += " AND regimenType = '" + regimentype + "'";
                if (!dateRegistrationBegin.trim().isEmpty() &&
                        !dateRegistrationEnd.trim().isEmpty())
                    query += " AND date_registration >= '" + DateUtil.parseStringToSqlDate(
                            dateRegistrationBegin, "MM/dd/yyyy") + "' " +
                            "AND date_registration <= '" + DateUtil.parseStringToSqlDate(
                            dateRegistrationEnd, "MM/dd/yyyy") + "'";
                if (!artStartDateBegin.trim().isEmpty() &&
                        !artStartDateEnd.trim().isEmpty())
                    query += " AND date_started >= '" + DateUtil.parseStringToSqlDate(
                            artStartDateBegin, "MM/dd/yyyy") + "' " +
                            "AND date_started <= '" + DateUtil.parseStringToSqlDate(
                            artStartDateEnd, "MM/dd/yyyy") + "'";
                if (clinicStage.trim().isEmpty())
                    query += " AND last_clinic_stage = '" + clinicStage + "'";
                if (!cd4Begin.trim().isEmpty() &&
                        !cd4End.trim().isEmpty())
                    query += " AND last_cd4 >= " + Double.parseDouble(
                            cd4Begin + " AND last_cd4 <= " +
                                    Double.parseDouble(cd4End));
                if (!viralloadBegin.trim().isEmpty() &&
                        !viralloadEnd.trim().isEmpty())
                    query += " AND last_viral_load >= " + Double.parseDouble(
                            viralloadBegin) + " AND last_viral_load <= " +
                            Double.parseDouble(viralloadEnd);
                query += " ORDER BY current_status";
            }
        }
        return query;
    }

    public String getQueryNotification(Integer entity, String facilityIds) {

        String query = "";

        switch (entity) {
            case 1:
                query = "SELECT patient.*, TIMESTAMPDIFF(YEAR, date_birth, CURDATE()) AS age FROM patient" +
                        " WHERE facility_id IN (" + facilityIds + ") AND current_status IN ('HIV+ non ART', " +
                        "'ART Start', 'ART Restart', 'ART Transfer In', 'Pre-ART Transfer In') AND date_started IS NULL ";
                break;

            case 2:
                query = "SELECT DISTINCT patient.* FROM patient WHERE facility_id IN (" + facilityIds + ") AND " +
                        "current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND " +
                        "TIMESTAMPDIFF(DAY, date_last_refill + last_refill_duration, CURDATE()) > 90 AND date_started " +
                        "IS NOT NULL";
                break;

            case 3:
                query = "SELECT patient.*, TIMESTAMPDIFF(YEAR, date_birth, CURDATE()) AS age FROM patient WHERE " +
                        "facility_id IN (" + facilityIds + ") AND current_status IN ('ART Start', 'ART Restart', " +
                        "'ART Transfer In') AND date_last_refill IS NULL";
                break;

            case 4:
                query = "SELECT patient.*, TIMESTAMPDIFF(YEAR, date_birth, CURDATE()) AS age FROM patient WHERE " +
                        "facility_id IN (" + facilityIds + ") AND current_status IN ('ART Start', 'ART Restart', " +
                        "'ART Transfer In') AND date_last_viral_load IS NULL AND " +
                        "TIMESTAMPDIFF(MONTH, date_started, CURDATE()) >= 6";
                break;

            case 5:
                query = "SELECT patient.*, TIMESTAMPDIFF(YEAR, date_birth, CURDATE()) AS age FROM patient WHERE " +
                        "facility_id IN (" + facilityIds + ") AND current_status IN ('ART Start', 'ART Restart', " +
                        "'ART Transfer In') AND last_viral_load >1000";
                break;
        }

        query += " ORDER BY current_status";
        return query;
    }

    public String getQueryCaseManagement(Long casemanagerId, long facilityId) {
        String query = "SELECT patient.*, TIMESTAMPDIFF(YEAR, date_birth, CURDATE()) AS age FROM patient WHERE " +
                "facility_id = " + facilityId + " AND casemanager_id = " + casemanagerId + " " +
                "ORDER BY current_status";
        return query;
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

    private void executeUpdate(String query) {
        ContextProvider.getBean(TransactionTemplate.class)
                .execute(status -> {
                    jdbcTemplate.execute(query);
                    return null;
                });
    }

}
