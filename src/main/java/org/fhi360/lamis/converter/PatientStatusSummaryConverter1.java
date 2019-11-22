/**
 *
 * @author aalozie
 */

package org.fhi360.lamis.converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
@Component
public class PatientStatusSummaryConverter1  implements ServletContextAware {

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private ServletContext servletContext;
    public PatientStatusSummaryConverter1() {

    }
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
    public synchronized ByteArrayOutputStream convertExcel(Long userId, String facilityIds, String state1) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);  // turn off auto-flushing and accumulate all rows in memory
        Sheet sheet = workbook.createSheet();
        
        try{
            jdbcUtil = new JDBCUtil();
            
            int rownum = 0;
            int cellnum = 0;
            Row row = sheet.createRow(rownum++);
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue("Facility");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Newly Enrolled (in this facility)");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Pre-ART Transfer In");
            cell = row.createCell(cellnum++);
            cell.setCellValue("ART Transfer In");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Newly Started (in this facility)");
            cell = row.createCell(cellnum++);
            cell.setCellValue("ART Restart");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Current On Treatment");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Active");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Pre-ART Transfer Out");
            cell = row.createCell(cellnum++);
            cell.setCellValue("ART Transfer Out");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Lost to Follow Up");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Stopped Treament");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Known Death");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Virally Suppressed");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Due for Viral Load");

//            cell = row.createCell(cellnum++);
//            cell.setCellValue("Started this month");
//
//            cell = row.createCell(cellnum++);
//            cell.setCellValue("Current this month");
            
            query = "SELECT DISTINCT patient.facility_id, facility.name FROM patient JOIN facility ON patient.facility_id = facility.facility_id WHERE patient.facility_id IN (" + facilityIds + ") ORDER BY facility.name"; 
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();       
            
            while(resultSet.next()) {
                long facilityId = resultSet.getLong("facility_id");
                
                cellnum = 0;
                row = sheet.createRow(rownum++);
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("name"));
                
                
                int newlyEnrolled = 0;
                int preArtTransferIn = 0;
                int artTransferIn = 0;
                int newlyStarted = 0;
                int restart = 0;
                int currentOnTreatment = 0;
                int preArtTransferOut = 0;
                int artTransferOut = 0;
                int LTFU = 0;
                int stopped = 0;
                int dead = 0;
                int vlSuppressed = 0;
                int vlDue = 0;
                int active = 0;
                               
                int newlyStartedPeriod = 0;
                int currentOnTreatmentPeriod = 0;

                
                SimpleDateFormat dateformat3 = new SimpleDateFormat("dd/MM/yyyy");
                Date reportingDateBegin = dateformat3.parse("30/09/2018");
                //Date reportingDateBegin = new Date();


                
                query = "SELECT status_registration, current_status, date_started, last_viral_load, " +
                        "viral_load_due_date, date_last_refill, last_refill_duration FROM patient " +
                        "WHERE facility_id = " + facilityId + " AND status_registration IS NOT NULL " +
                        "AND current_status IS NOT NULL";
                preparedStatement = jdbcUtil.getStatement(query);
                ResultSet rs = preparedStatement.executeQuery();       
                while(rs.next()) {
                    String statusRegistration = rs.getString("status_registration");
                    String currentStatus = rs.getString("current_status");
                    Date dateStarted = rs.getDate("date_started");                
                    double lastViralLoad = rs.getDouble("last_viral_load");
                    Date viralLoadDueDate = rs.getDate("viral_load_due_date");
                    Date dateLastRefill = rs.getDate("date_last_refill"); 
                    int duration = rs.getInt("last_refill_duration");
                    
                    int monthRefill = duration/30;
                    if(monthRefill <= 0) monthRefill = 1;

                    if(currentStatus.equalsIgnoreCase("ART Start") ||
                            currentStatus.equalsIgnoreCase("ART Restart") ||
                            currentStatus.equalsIgnoreCase("ART Transfer In")) {
                        if(dateLastRefill != null) {
                            if(DateUtil.addYearMonthDay(dateLastRefill, monthRefill+3, "MONTH")
                                    .after(reportingDateBegin)) {
                                active++;
                            }                        
                        }
                        else {
                            if(dateStarted != null) {
                                if(DateUtil.addYearMonthDay(dateStarted, 3, "MONTH")
                                        .before(reportingDateBegin)) {
                                    active++;
                                }                                                        
                            }
                        }

                        //query = "SELECT DISTINCT patient_id FROM patient WHERE patient_id = " + id + " AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND DATEDIFF(DAY, date_last_refill + last_refill_duration, CURDATE()) <= 90 AND date_started IS NOT NULL ORDER BY current_status";
                    }
                    

                    int year = 0;
                    int month = 0;
                    if(dateStarted != null) {
                        year = DateUtil.getYear(dateStarted);
                        month = DateUtil.getMonth(dateStarted);                       
                    }
                    
                    System.out.println("Year/Month: " + year + "/"  + month);

                    if(statusRegistration.equalsIgnoreCase("HIV+ non ART")) {
                        newlyEnrolled++;
                    }
                    if(statusRegistration.equalsIgnoreCase("Pre-ART Transfer In")) {
                        preArtTransferIn++;
                    }
                    if(statusRegistration.equalsIgnoreCase("ART Transfer In")) {
                        artTransferIn++;
                    }
                    if(!statusRegistration.equalsIgnoreCase("ART Transfer In") && dateStarted != null) {
                        newlyStarted++;
                        if(year == 2017 && month == 9) newlyStartedPeriod++;
                    }

                    if(currentStatus.equalsIgnoreCase("ART Start") && dateStarted != null) {
                        currentOnTreatment++;
                        if(year <= 2017) currentOnTreatmentPeriod++;

                        //check for virally suppresed and due for VL
                        if(lastViralLoad < 1000) {
                            vlSuppressed++;
                        }

                        if(viralLoadDueDate != null && viralLoadDueDate.after(new Date())) {
                            vlDue++;
                        }
                    }

                    if(currentStatus.equalsIgnoreCase("ART Transfer In") && dateStarted != null) {
                        currentOnTreatment++;
                        if(year <= 2017) currentOnTreatmentPeriod++;

                        //check for virally suppresed and due for VL
                        if(lastViralLoad < 1000) {
                            vlSuppressed++;
                        }

                        if(viralLoadDueDate != null && viralLoadDueDate.after(new Date())) {
                            vlDue++;
                        }
                    }

                    if(currentStatus.equalsIgnoreCase("ART Restart") && dateStarted != null) {
                        restart++;
                        currentOnTreatment++;
                        if(year <= 2017) currentOnTreatmentPeriod++;

                        //check for virally suppresed and due for VL
                        if(lastViralLoad < 1000) {
                            vlSuppressed++;
                        }

                        if(viralLoadDueDate != null && viralLoadDueDate.after(new Date())) {
                            vlDue++;
                        }
                    }

                    if(currentStatus.equalsIgnoreCase("Pre-ART Transfer Out")) {
                        preArtTransferOut++;
                    }

                    if(currentStatus.equalsIgnoreCase("ART Transfer Out")) {
                        artTransferOut++;
                    }

                    if(currentStatus.equalsIgnoreCase("Lost to Follow Up")) {
                        LTFU++;
                    }

                    if(currentStatus.equalsIgnoreCase("Stopped Treatment")) {
                        stopped++;
                    }

                    if(currentStatus.equalsIgnoreCase("Known Death")) {
                        dead++;
                    }
                }                

                cell = row.createCell(cellnum++);
                cell.setCellValue(newlyEnrolled);

                cell = row.createCell(cellnum++);
                cell.setCellValue(preArtTransferIn);

                cell = row.createCell(cellnum++);
                cell.setCellValue(artTransferIn);

                cell = row.createCell(cellnum++);
                cell.setCellValue(newlyStarted);

                cell = row.createCell(cellnum++);
                cell.setCellValue(restart);

                cell = row.createCell(cellnum++);
                cell.setCellValue(currentOnTreatment);

                cell = row.createCell(cellnum++);
                cell.setCellValue(active);
                
                cell = row.createCell(cellnum++);
                cell.setCellValue(preArtTransferOut);

                cell = row.createCell(cellnum++);
                cell.setCellValue(artTransferOut);

                cell = row.createCell(cellnum++);
                cell.setCellValue(LTFU);

                cell = row.createCell(cellnum++);
                cell.setCellValue(stopped);

                cell = row.createCell(cellnum++);
                cell.setCellValue(dead);

                cell = row.createCell(cellnum++);
                cell.setCellValue(vlSuppressed);

                cell = row.createCell(cellnum++);
                cell.setCellValue(vlDue);


//                cell = row.createCell(cellnum++);
//                cell.setCellValue(newlyStartedPeriod);
//
//                cell = row.createCell(cellnum++);
//                cell.setCellValue(currentOnTreatmentPeriod);
            }
            workbook.write(outputStream);
            outputStream.close();
            workbook.dispose();  // dispose of temporary files backing this workbook on disk
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return outputStream;
    }
        
}
