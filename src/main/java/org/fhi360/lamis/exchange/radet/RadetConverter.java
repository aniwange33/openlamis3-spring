/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*

package org.fhi360.lamis.exchange.radet;


import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.*;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

*/
/**
 * @author user1
 *//*

public class RadetConverter {
    private final JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);
    private Date reportingDateBegin;
    private Date reportingDateEnd;
    private String facId;
    String facilityName;
    private String contextPath;
    private String query;
    private HttpServletRequest request;
    private HttpSession session;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public RadetConverter() {

        try {
            this.jdbcUtil = new JDBCUtil();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();
        }
    }

    public String convertExcel() {

        String stateId = request.getParameter("stateId");
        String facilityIds = request.getParameter("facilityIds");
        System.out.println("Conversion begin state Id..........." + stateId);
        System.out.println("Conversion begin facility Ids..........." + facilityIds);
        String fileName = "n/a";

        if (facilityIds.trim().isEmpty() && stateId.trim().isEmpty()) {
            System.out.println("Got here");
                    facId = session.getAttribute("id").toString();
            fileName = doRadetGeneration(new String[]{facId}, "single");

            return "transfer/radet_single/" + fileName;
        } else {
            if (!facilityIds.trim().isEmpty()) {
                String[] facilities = facilityIds.split(",");
                doRadetGeneration(facilities, "non-single");
                String stateName = FacilityJDBC.getStateName(Long.parseLong(stateId));
                fileName = zipFiles(stateName);
            } else {
                //Extract for selected State
                if (!stateId.trim().isEmpty()) {
                    List<Long> facilities = FacilityJDBC.getFacilitiesInState(Long.parseLong(stateId));
                    String[] facIds = new String[facilities.size()];
                    for (int i = 0; i < facilities.size(); i++) {
                        facIds[i] = String.valueOf(facilities.get(i));
                    }
                    doRadetGeneration(facIds, "non-single");
                    String stateName = FacilityJDBC.getStateName(Long.parseLong(stateId));
                    fileName = zipFiles(stateName);
                }
            }
        }

        return fileName;
    }

    private String buildFacilities(String[] facilities) {
        String facIds = "";
        try {
            for (int i = 0; i < facilities.length - 1; i++) {
                facIds += facilities[i] + ",";
            }
            facIds += facilities[facilities.length - 1];
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return facIds;
    }

    public String doRadetGeneration(String[] facilities, String option) {

        String fileName = "";
        String dbSuffix = org.apache.commons.lang.RandomStringUtils.randomAlphabetic(6);

        Integer cohortMonthBegin = DateUtil.getMonth(request.getParameter("cohortMonthBegin"));
        int cohortYearBegin = Integer.parseInt(request.getParameter("cohortYearBegin"));
        Integer cohortMonthEnd = DateUtil.getMonth(request.getParameter("cohortMonthEnd"));
        int cohortYearEnd = Integer.parseInt(request.getParameter("cohortYearEnd"));
        String cohortDateBegin = DateUtil.parseDateToString(DateUtil.getFirstDateOfMonth(cohortYearBegin, cohortMonthBegin), "yyyy-MM-dd");
        String cohortDateEnd = DateUtil.parseDateToString(DateUtil.getLastDateOfMonth(cohortYearEnd, cohortMonthEnd), "yyyy-MM-dd");

        int reportingMonth = DateUtil.getMonth(request.getParameter("reportingMonth"));
        int reportingYear = Integer.parseInt(request.getParameter("reportingYear"));
        reportingDateEnd = DateUtil.getLastDateOfMonth(reportingYear, reportingMonth);
        reportingDateBegin = DateUtil.getFirstDateOfMonth(reportingYear, reportingMonth);

        executeUpdate(" CREATE VIEW viralload AS SELECT patient_id, resultab, date_reported, comment FROM laboratory WHERE facility_id = " + id + " AND date_reported <= '" + dateFormat.format(reportingDateEnd) + "' AND resultab REGEXP('(^[0-9]+$)') AND labtest_id = 16 ORDER BY patient_id");
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);
        //turn off auto - flushing and accumulate all rows in memory
        Sheet sheet = workbook.createSheet();

        //Create a new font
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(new HSSFColor.WHITE().getIndex());

        //Create a style and set the font into it
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        style.setFillBackgroundColor(new HSSFColor.BLUE().getIndex());
        style.setFillPattern(HSSFCellStyle.FINE_DOTS);
        style.setFont(font);

        int rownum = 0;
        int cellnum = 0;
        Row row = sheet.createRow(rownum++);
        Cell cell = row.createCell(cellnum++);
        cell.setCellValue("S/No.");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("State");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("LGA");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Facility");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Patient Id");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Hospital Num");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Sex");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Age at Start of ART (Years)");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Age at Start of ART (Months) Enter for under 5s");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Date of Birth");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("ART Start Date (yyyy-mm-dd");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Last Pickup Date (yyyy-mm-dd)");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Days of ARV Refill");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Regimen Line at ART Start");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Regimen at ART Start");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Current Regimen Line");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Current ART Regimen");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Pregnancy Status");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Current Viral Load (c/ml)");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Date of Current Viral Load (yyyy-mm-dd)");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Viral Load Indication");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Current ART Status");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("ART Enrollment Setting");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Client Receiving DMOC Service?");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Date Commenced DMOC (yyyy-mm-dd)");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("DMOC of Type");
        cell.setCellStyle(style);

        cell = row.createCell(cellnum++);
        cell.setCellValue("Enhanced Adherence Counselling (EAC) Commenced?");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Date of Commencement of EAC (yyyy-mm-dd)");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Number of EAC Sessions Completed");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Repeat Viral Load - Post EAC VL Sample Collected?");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Date of Repeat Viral Load - Post EAC VL Sample Collected");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("TPT in the Last 2 years");
        cell.setCellStyle(style);
        cell = row.createCell(cellnum++);
        cell.setCellValue("If Yes to TPT, date of TPT Start");
        cell.setCellStyle(style);

        style = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
        style.setAlignment(CellStyle.ALIGN_RIGHT);

        try {
            String facs = buildFacilities(facilities);
            System.out.println("Building Pool..." + new Date());

            System.out.println("Creating Viral Load Pool..." + new Date());
            executeUpdate("DROP VIEW IF EXISTS viralloadpool_" + dbSuffix);
            executeUpdate(" CREATE VIEW viralloadpool_" + dbSuffix + " AS SELECT patient_id, resultab, date_reported, comment, facility_id, labtest_id FROM laboratory WHERE facility_id IN (" + facs + ") AND date_reported <= '" + DateUtil.parseDateToString(reportingDateEnd, "yyyy-MM-dd") + "' AND labtest_id = 16 ORDER BY patient_id");
            System.out.println("Done Viral Load Pool..." + new Date());

            System.out.println("Creating Visit Pool..." + new Date());
            executeUpdate("DROP VIEW IF EXISTS visitpool_" + dbSuffix);
            executeUpdate(" CREATE VIEW visitpool_" + dbSuffix + " AS SELECT * FROM clinic WHERE facility_id IN (" + facs + ") AND date_visit >= DATE_ADD('" + DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd") + "', INTERVAL -9 MONTH) AND date_visit <= '" + DateUtil.parseDateToString(reportingDateEnd, "yyyy-MM-dd") + "'");
            System.out.println("Done Visit Pool..." + new Date());

            System.out.println("Creating Commencement Pool..." + new Date());
            executeUpdate("DROP VIEW IF EXISTS commencepool_" + dbSuffix);
            executeUpdate(" CREATE VIEW commencepool_" + dbSuffix + " AS SELECT * FROM clinic WHERE facility_id IN (" + facs + ") AND commence = 1");
            System.out.println("Done Commencement Pool..." + new Date());

            System.out.println("Creating pharmacy Pool..." + new Date());
            executeUpdate("DROP VIEW IF EXISTS pharmpool_" + dbSuffix);
            executeUpdate(" CREATE VIEW pharmpool_" + dbSuffix + " AS SELECT * FROM pharmacy WHERE facility_id IN (" + facs + ") AND date_visit <= '" + DateUtil.parseDateToString(reportingDateEnd, "yyyy-MM-dd") + "' AND regimentype_id IN (1, 2, 3, 4, 14)");
            System.out.println("Done Pharmacy Pool..." + new Date());

            System.out.println("Creating INH Pool..." + new Date());
            executeUpdate("DROP VIEW IF EXISTS inhpool_" + dbSuffix);
            executeUpdate(" CREATE VIEW inhpool_" + dbSuffix + " AS SELECT * FROM pharmacy WHERE facility_id IN (" + facs + ") AND regimen_id = 115");
            System.out.println("Done INH Pool..." + new Date());

            System.out.println("Creating Patient Pool..." + new Date());
            executeUpdate("DROP VIEW IF EXISTS patientpool_" + dbSuffix);
            executeUpdate(" CREATE VIEW patientpool_" + dbSuffix + " AS SELECT DISTINCT patient_id, hospital_num, unique_id, enrollment_setting, gender, date_birth, date_started, facility_id FROM patient WHERE facility_id IN (" + facs + ") AND date_started >= '" + cohortDateBegin + "' AND date_started <= '" + cohortDateEnd + "'");
            System.out.println("Done Patient Pool..." + new Date());

            System.out.println("Creating Status Pool..." + new Date());
            executeUpdate("DROP VIEW IF EXISTS statuspool_" + dbSuffix);
            executeUpdate(" CREATE VIEW statuspool_" + dbSuffix + " AS SELECT current_status, patient_id, facility_id, date_current_status FROM statushistory WHERE facility_id IN (" + facs + ") AND date_current_status <= '" + DateUtil.parseDateToString(reportingDateEnd, "yyyy-MM-dd") + "' ORDER BY date_current_status");
            System.out.println("Done Status Pool..." + new Date());
            for (String facility : facilities) {
                Long id = Long.parseLong(facility);
                facilityName = FacilityJDBC.getFacilityName(id);
                String stateName = FacilityJDBC.getStateNameForFacility(id);
                System.out.println("State Name: " + stateName);
                String lga = FacilityJDBC.getLgaNameForFacility(id);
                System.out.println("LGA Name: " + lga);

                executeUpdate("DROP VIEW IF EXISTS viralload_" + dbSuffix);
                executeUpdate(" CREATE VIEW viralload_" + dbSuffix + " AS SELECT patient_id, resultab, date_reported, comment FROM viralloadpool_" + dbSuffix + " WHERE facility_id = " + id + " AND date_reported <= '" + DateUtil.parseDateToString(reportingDateEnd, "yyyy-MM-dd") + "' AND labtest_id = 16 ORDER BY patient_id");

                executeUpdate("DROP INDEX IF EXISTS idx_visit_" + dbSuffix);
                executeUpdate("DROP VIEW IF EXISTS visit_" + dbSuffix);
                executeUpdate(" CREATE VIEW visit_" + dbSuffix + " AS SELECT * FROM visitpool_" + dbSuffix + " WHERE facility_id = " + id + " AND date_visit >= DATE_ADD('" + DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd") + "', INTERVAL -9 MONTH) AND date_visit <= '" + DateUtil.parseDateToString(reportingDateEnd, "yyyy-MM-dd") + "'");
                executeUpdate("CREATE INDEX idx_visit_" + dbSuffix + " ON visit_" + dbSuffix + "(patient_id)");

                executeUpdate("DROP INDEX IF EXISTS idx_commence_" + dbSuffix);
                executeUpdate("DROP VIEW IF EXISTS commence_" + dbSuffix);
                executeUpdate(" CREATE VIEW commence_" + dbSuffix + " AS SELECT * FROM commencepool_" + dbSuffix + " WHERE facility_id = " + id + " AND commence = 1");
                executeUpdate("CREATE INDEX idx_commence_" + dbSuffix + " ON commence_" + dbSuffix + "(patient_id)");

                executeUpdate("DROP INDEX IF EXISTS idx_pharm_" + dbSuffix + "");
                executeUpdate("DROP VIEW IF EXISTS pharm_" + dbSuffix);
                executeUpdate(" CREATE VIEW pharm_" + dbSuffix + " AS SELECT * FROM pharmpool_" + dbSuffix + " WHERE facility_id = " + id + " AND date_visit <= '" + DateUtil.parseDateToString(reportingDateEnd, "yyyy-MM-dd") + "' AND regimentype_id IN (1, 2, 3, 4, 14)");
                executeUpdate("CREATE INDEX idx_pharm_" + dbSuffix + " ON pharm_" + dbSuffix + "(patient_id)");

                executeUpdate("DROP INDEX IF EXISTS idx_inh_" + dbSuffix + "");
                executeUpdate("DROP VIEW IF EXISTS inh_" + dbSuffix);
                executeUpdate(" CREATE VIEW inh_" + dbSuffix + " AS SELECT * FROM inhpool_" + dbSuffix + " WHERE facility_id = " + id + " AND regimen_id = 115");
                executeUpdate("CREATE INDEX idx_inh_" + dbSuffix + " ON inh_" + dbSuffix + "(patient_id)");

                query = "SELECT DISTINCT patient_id, hospital_num, unique_id, enrollment_setting, gender, date_birth, date_started FROM patientpool_" + dbSuffix + " WHERE facility_id = " + id + " AND date_started >= '" + cohortDateBegin + "' AND date_started <= '" + cohortDateEnd + "'";
                resultSet = executeQuery(query);
                int sno = 1;
                while (resultSet.next()) {
                    long id = resultSet.getLong("patient_id");
                    String uniqueId = resultSet.getString("unique_id") == null ? "" : resultSet.getString("unique_id");
                    String hospitalNum = resultSet.getString("hospital_num");
                    String gender = resultSet.getString("gender");
                    String enrollmentSetting = resultSet.getString("enrollment_setting") == null ? "" : resultSet.getString("enrollment_setting");
                    cellnum = 0;
                    row = sheet.createRow(rownum++);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(sno++);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(stateName);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(lga);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(facilityName);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(uniqueId);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(hospitalNum);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(gender);

                    System.out.println("Analysing patent Id" + id);
                    Date dateBirth = resultSet.getDate("date_birth");
                    Date dateStarted = resultSet.getDate("date_started");
                    int age = DateUtil.yearsBetweenIgnoreDays(dateBirth, dateStarted);
                    cell = row.createCell(cellnum++);
                    if (resultSet.getDate("date_birth") != null) {
                        cell.setCellValue(resultSet.getDate("date_birth"));
                        cell.setCellStyle(style);
                    }
                    cell = row.createCell(cellnum++);
                    if (resultSet.getDate("date_started") != null) {
                        cell.setCellValue(resultSet.getDate("date_started"));
                        cell.setCellStyle(style);
                    }

                    dateBirth = resultSet.getDate("date_birth");
                    age = DateUtil.yearsBetweenIgnoreDays(dateBirth, dateStarted);
                    cell = row.createCell(cellnum++);
                    if (age >= 5) cell.setCellValue(age);
                    cell.setCellValue(Integer.toString(age));
                    cell = row.createCell(cellnum++);
                    if (age < 5) {
                        age = DateUtil.monthsBetweenIgnoreDays(dateBirth, dateStarted);
                        cell.setCellValue(age);
                    }

                    query = "SELECT pharm_" + dbSuffix + ".date_visit, pharm_" + dbSuffix + ".duration, regimenType.description AS regimenType, regimen.description AS regimen FROM pharm_" + dbSuffix + " JOIN regimenType ON pharm_" + dbSuffix + ".regimentype_id = regimenType.regimentype_id JOIN regimen ON pharm_" + dbSuffix + ".regimen_id = regimen.regimen_id WHERE pharm_" + dbSuffix + ".patient_id = " + id + " ORDER BY pharm_" + dbSuffix + ".date_visit DESC LIMIT 1";
                    ResultSet rs = executeQuery(query);

                    Date dateLastRefill = null;
                    int duration = 0;
                    int monthRefill = 0;
                    String regimenType = "";
                    String regimen = "";

                    if (rs.next()) {
                        dateLastRefill = rs.getDate("date_visit");
                        duration = rs.getInt("duration");
                        monthRefill = duration / 30;
                        if (monthRefill <= 0) {
                            monthRefill = 1;
                        }
                        regimenType = rs.getString("regimenType") == null ? "" : rs.getString("regimenType");
                        if (regimenType.contains("ART First Line Adult")) {
                            regimenType = "Adult.1st.Line";
                        } else {
                            if (regimenType.contains("ART Second Line Adult")) {
                                regimenType = "Adult.2nd.Line";
                            } else {
                                if (regimenType.contains("ART First Line Children")) {
                                    regimenType = "Peds.1st.Line";
                                } else {
                                    if (regimenType.contains("ART Second Line Children")) {
                                        regimenType = "Peds.2nd.Line";
                                    } else {
                                        if (regimenType.contains("Third Line")) {
                                            if (age < 5) {
                                                regimenType = "Peds.3rd.Line";
                                            } else {
                                                regimenType = "Adult.3rd.Line";
                                            }
                                        } else {
                                            regimenType = "";
                                        }
                                    }
                                }
                            }
                        }
                        if (!regimenType.trim().isEmpty()) {
                            regimen = rs.getString("regimen") == null ? "" : resolveRegimen(rs.getString("regimen"));
                        }
                    }

                    cell = row.createCell(cellnum++);
                    if (dateLastRefill != null) {
                        cell.setCellValue(dateLastRefill);
                        cell.setCellStyle(style);
                    }

                    cell = row.createCell(cellnum++);
                    if (dateLastRefill != null) {
                        cell.setCellValue(duration);
                    }

                    query = "SELECT regimenType, regimen FROM commence_" + dbSuffix + " WHERE patient_id = " + id;
                    rs = executeQuery(query);
                    String regimentypeStart = "";
                    String regimenStart = "";
                    if (rs.next()) {
                        regimentypeStart = rs.getString("regimenType") == null ? "" : rs.getString("regimenType");
                        if (regimentypeStart.contains("ART First Line Adult")) {
                            regimentypeStart = "Adult.1st.Line";
                        } else {
                            if (regimentypeStart.contains("ART Second Line Adult")) {
                                regimentypeStart = "Adult.2nd.Line";
                            } else {
                                if (regimentypeStart.contains("ART First Line Children")) {
                                    regimentypeStart = "Peds.1st.Line";
                                } else {
                                    if (regimentypeStart.contains("ART Second Line Children")) {
                                        regimentypeStart = "Peds.2nd.Line";
                                    } else {
                                        if (regimentypeStart.contains("Third Line")) {
                                            if (age < 5) {
                                                regimentypeStart = "Peds.3rd.Line";
                                            } else {
                                                regimentypeStart = "Adult.3rd.Line";
                                            }
                                        } else {
                                            regimentypeStart = "";
                                        }
                                    }
                                }
                            }
                        }
                        if (!regimentypeStart.trim().isEmpty()) {
                            regimenStart = rs.getString("regimen") == null ? "" : resolveRegimen(rs.getString("regimen"));
                        }
                    }

                    cell = row.createCell(cellnum++);
                    cell.setCellValue(regimentypeStart);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(regimenStart);

                    cell.setCellValue(regimenType);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(regimen);

                    boolean pregnant = false;
                    boolean breastfeeding = false;
                    if (gender.trim().equals("Female")) {
                        query = "SELECT pregnant, breastfeeding FROM visit_" + dbSuffix + " WHERE patient_id = " + id + " ORDER BY date_visit DESC LIMIT 1";
                        rs = executeQuery(query);
                        if (rs.next()) {
                            if (rs.getInt("pregnant") == 1) {
                                pregnant = true;
                            }
                            if (rs.getInt("breastfeeding") == 1) {
                                breastfeeding = true;
                            }
                        }
                    }

                    cell = row.createCell(cellnum++);
                    if (gender.trim().equals("Female")) {
                        if (pregnant) {
                            cell.setCellValue("Pregnant");
                        } else {
                            if (breastfeeding) {
                                cell.setCellValue("Breastfeeding");
                            } else {
                                cell.setCellValue("Not pregnant");
                            }
                        }
                    }

                    String viralLoad = "";
                    Date dateOfViralLoad = null;
                    boolean unsuppressed = false;
                    String comment = "";
                    System.out.println("Comparing: dateStarted: " + dateStarted + " date reporting end " + reportingDateEnd);
                    if (DateUtil.monthsBetweenIgnoreDays(dateStarted, reportingDateEnd) >= 6) {
                        query = "SELECT resultab, date_reported, comment FROM viralload_" + dbSuffix + " WHERE patient_id = " + id + " ORDER BY date_reported DESC LIMIT 1";
                        rs = executeQuery(query);
                        if (rs.next()) {
                            viralLoad = rs.getString("resultab") == null ? "" : rs.getString("resultab");
                            dateOfViralLoad = rs.getDate("date_reported");
                            comment = rs.getString("comment") == null ? "" : rs.getString("comment");
                        }
                    }
                    cell = row.createCell(cellnum++);
                    if (!viralLoad.trim().isEmpty()) {
                        if (!StringUtil.isInteger(viralLoad)) {
                            viralLoad = "0.0";
                        }
                        Double value = Double.valueOf(StringUtil.stripCommas(viralLoad));

                        if (value > 1000) {
                            unsuppressed = true;
                        }
                        cell.setCellValue(value.intValue());
                    }
                    cell = row.createCell(cellnum++);
                    if (dateOfViralLoad != null) {
                        cell.setCellValue(dateOfViralLoad);
                        cell.setCellStyle(style);
                    }
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(comment);

                    query = "SELECT current_status FROM statuspool_" + dbSuffix + " WHERE facility_id = " + id + " AND patient_id = " + id + " AND date_current_status <= '" + DateUtil.parseDateToString(reportingDateEnd, "yyyy-MM-dd") + "' ORDER BY date_current_status DESC LIMIT 1";
                    rs = executeQuery(query);
                    String currentStatus = "";
                    if (rs.next()) {
                        currentStatus = rs.getString("current_status") == null ? "" : rs.getString("current_status");
                    }
                    System.out.println("Date Started: " + dateStarted + " reporting Date begin: " + reportingDateBegin);
                    if (currentStatus.trim().equalsIgnoreCase("ART Transfer Out")) {
                        currentStatus = "Transferred Out";
                    } else {
                        if (currentStatus.trim().equalsIgnoreCase("Stopped Treatment")) {
                            currentStatus = "Stopped";
                        } else {
                            if (currentStatus.trim().equalsIgnoreCase("Known Death")) {
                                currentStatus = "Dead";
                            } else {
                                if (dateLastRefill != null) {
                                    if (!DateUtil.addYearMonthDay(dateLastRefill, duration + Constants.LTFU.PEPFAR, "DAY").after(reportingDateBegin)) {
                                        currentStatus = "LTFU";
                                    } else {
                                        if (currentStatus.trim().equalsIgnoreCase("Lost to Follow Up")) {
                                            currentStatus = "LTFU";
                                        } else {
                                            currentStatus = currentStatus.trim().equalsIgnoreCase("ART Transfer In") ? "Active-Transfer In" : "Active";
                                        }
                                    }
                                } else {
                                    if (currentStatus.trim().equalsIgnoreCase("ART Transfer In")) {
                                        currentStatus = "Active-Transfer In";
                                    } else {
                                        if (DateUtil.addYearMonthDay(dateStarted, Constants.LTFU.PEPFAR, "DAY").before(reportingDateBegin)) {
                                            currentStatus = "Active";
                                        } else {
                                            currentStatus = "LTFU";
                                        }
                                    }
                                }
                            }
                        }
                    }
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(currentStatus);

                    cell = row.createCell(cellnum++);
                    cell.setCellValue(enrollmentSetting);

                    final String[] devolved = {""};
                    final String[] dateDevolved = {""};
                    final String[] typeDmoc = {""};
                    if (unsuppressed) {
                        devolved[0] = "N/A";
                    } else {
                        System.out.println("Patient ID is: " + id);
                        if (DateUtil.addYearMonthDay(dateStarted, 12, "MONTH")
                                .before(reportingDateBegin)) {
                            devolved[0] = "N/A";
                        } else {
                            devolved[0] = "No";
                        }
                        jdbcTemplate.query(new DevolveJDBC().getFirstDevolvement(id), rs1 -> {
                            devolved[0] = "Yes";
                            dateDevolved[0] = DateUtil.parseDateToString(rs1.getDate("date_devolved"), "yyyy-MM-dd");
                            typeDmoc[0] = rs1.getString("type_dmoc");
                        });
                    }

                    cell = row.createCell(cellnum++);
                    cell.setCellValue(devolved[0]);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(dateDevolved[0]);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(typeDmoc[0]);

                    System.out.println("Viral Load");
                    String adherence = "";
                    String dateAdherence = "";
                    int sessions = 0;
                    String repeatVL = "";
                    String dateSampleCollected = "";
                    if (unsuppressed) {
                        query = "SELECT * FROM eac WHERE patient_id = " + id + " ORDER BY date_eac1 DESC LIMIT 1";
                        rs = executeQuery(query);
                        if (rs.next()) {
                            adherence = "Yes";
                            repeatVL = "No";
                            dateAdherence = DateUtil.parseDateToString(rs.getDate("date_eac1"), "yyyy-MM-dd");
                            if (rs.getDate("date_eac1") != null) {
                                sessions = 1;
                            }
                            if (rs.getDate("date_eac2") != null) {
                                sessions = 2;
                            }
                            if (rs.getDate("date_eac3") != null) {
                                sessions = 3;
                            }
                            dateSampleCollected = rs.getDate("date_sample_collected") == null ? "" : DateUtil.parseDateToString(rs.getDate("date_sample_collected"), "yyyy-MM-dd");
                            if (!dateSampleCollected.isEmpty()) repeatVL = "Yes";
                        } else {
                            adherence = "No";
                        }
                    }
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(adherence);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(dateAdherence);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(sessions);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(repeatVL);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(dateSampleCollected);

                    System.out.println("TPT");
                    String tpt;
                    String dateTpt = "";
                    query = "SELECT date_visit FROM inh_" + dbSuffix + " WHERE patient_id = " + id + " AND date_visit >= DATE_ADD('" + DateUtil.parseDateToString(reportingDateBegin, "yyyy-MM-dd") + "', INTERVAL -30 MONTH) AND date_visit <= '" + DateUtil.parseDateToString(reportingDateEnd, "yyyy-MM-dd") + "' ORDER BY date_visit ASC LIMIT 1";
                    rs = executeQuery(query);
                    if (rs.next()) {
                        tpt = "Yes";
                        dateTpt = rs.getDate("date_visit") == null ? "" : DateUtil.parseDateToString(rs.getDate("date_visit"), "yyyy-MM-dd");
                    } else {
                        tpt = "No";
                    }
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(tpt);
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(dateTpt);

                    System.out.println("Finished");
                }
            }

            System.out.println("Context Path is: " + contextPath);
            String directory = contextPath + "transfer/radet_single/";

            FileUtil fileUtil = new FileUtil();
            fileUtil.makeDir(directory);
            fileUtil.deleteFileWithExtension(directory, "xlsx");
            System.out.println("Request Context Path is: " + request.getContextPath());
            fileUtil.makeDir(request.getContextPath() + "/transfer/radet_single/");
            fileUtil.deleteFileWithExtension(request.getContextPath() + "/transfer/radet_single/", "xlsx");

            if (option.equals("single")) {
                fileName = "RADET_" + facilityName + "_" + (cohortMonthBegin.toString().length() == 1 ? "0" + cohortMonthBegin : cohortMonthBegin) + "-" + Integer.toString(cohortYearBegin) + " to " + (cohortMonthEnd.toString().length() == 1 ? "0" + cohortMonthEnd : cohortMonthEnd) + "-" + Integer.toString(cohortYearEnd) + ".xlsx";
            }
            if (option.equals("non-single")) {
                fileName = "RADET_" + (cohortMonthBegin.toString().length() == 1 ? "0" + cohortMonthBegin : cohortMonthBegin) + "-" + Integer.toString(cohortYearBegin) + " to " + (cohortMonthEnd.toString().length() == 1 ? "0" + cohortMonthEnd : cohortMonthEnd) + "-" + Integer.toString(cohortYearEnd) + ".xlsx";
            }

            try (FileOutputStream outputStream = new FileOutputStream(new File(directory + fileName))) {
                workbook.write(outputStream);
            }
            workbook.dispose();
                if (!contextPath.equalsIgnoreCase(request.getContextPath())) {
                    fileUtil.copyFile(fileName, contextPath + "transfer/radet_single/", request.getContextPath() + "/transfer/radet_single/");
                }
                resultSet = null;

                dropAllTemporaryTables(dbSuffix);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fileName;
    }

    private void dropAllTemporaryTables(String dbSuffix) {
        try {
            executeUpdate("DROP VIEW IF EXISTS viralloadpool_" + dbSuffix);
            executeUpdate("DROP VIEW IF EXISTS visitpool_" + dbSuffix);
            executeUpdate("DROP VIEW IF EXISTS commencepool_" + dbSuffix);
            executeUpdate("DROP VIEW IF EXISTS pharmpool_" + dbSuffix);
            executeUpdate("DROP VIEW IF EXISTS inhpool_" + dbSuffix);
            executeUpdate("DROP VIEW IF EXISTS patientpool_" + dbSuffix);
            executeUpdate("DROP VIEW IF EXISTS statuspool_" + dbSuffix);

            executeUpdate("DROP VIEW IF EXISTS viralload_" + dbSuffix);
            executeUpdate("DROP VIEW IF EXISTS visit_" + dbSuffix);
            executeUpdate("DROP VIEW IF EXISTS commence_" + dbSuffix);
            executeUpdate("DROP VIEW IF EXISTS pharm_" + dbSuffix);
            executeUpdate("DROP VIEW IF EXISTS inh_" + dbSuffix);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String zipFiles(String stateName) {
        String fileName = "";
        try {
            fileName = stateName.trim() + ".zip";
            String sourceFolder = contextPath + "transfer/radet_single/";
            String destFolder = contextPath + "transfer/radet/";
            String outputZipFile = destFolder + fileName;

            FileUtil fileUtil = new FileUtil();
            fileUtil.makeDir(destFolder);
            fileUtil.deleteFileWithExtension(destFolder, "zip");
            fileUtil.zipFolder(sourceFolder, outputZipFile);
            fileUtil.zipFolderContent(sourceFolder, outputZipFile);
            fileUtil.deleteFileWithExtension(sourceFolder, "xlsx");

                String finalDestFolder = ServletActionContext.getRequest().getContextPath() + "/transfer/radet/";
                fileUtil.makeDir(finalDestFolder);
                fileUtil.deleteFileWithExtension(finalDestFolder, "zip");
                if (!contextPath.equalsIgnoreCase(ServletActionContext.getRequest().getContextPath())) {
                    fileUtil.copyFile(fileName, destFolder, finalDestFolder);
                }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "transfer/radet/" + fileName;
    }

    private String resolveRegimen(String regimensys) {
        String regimen = "";
        query = "SELECT regimen FROM regimenresolver WHERE regimensys = '" + regimensys + "'";
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                regimen = rs.getString("regimen");
            }
        } catch (Exception exception) {
            }
        return regimen;
    }

    private ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();
            }
        return rs;
    }

    private void executeUpdate(String query) {
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            jdbcUtil.disconnectFromDatabase();
        }
    }
}
*/
