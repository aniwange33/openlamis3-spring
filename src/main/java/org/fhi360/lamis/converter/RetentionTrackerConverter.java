/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.converter;

/**
 *
 * @author user10
 */

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

@Component
public class RetentionTrackerConverter implements ServletContextAware {

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private List<Map<String, Object>> reportParams = new ArrayList<>();
  //  private IndicatorMonitorReport iMReport = new IndicatorMonitorReport();
    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    public RetentionTrackerConverter() {
    } 

    public synchronized ByteArrayOutputStream convertExcel(String state, String reportingDateBegin) {
        ByteArrayOutputStream outputStream =new ByteArrayOutputStream();
        
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);  // turn off auto-flushing and accumulate all rows in memory
        Sheet sheet = workbook.createSheet();
        
        try{
            int rownum = 0;
            int cellnum = 0;
            //Create a new Row...
            Row row = sheet.createRow(rownum++);
//            CellStyle wrapText = workbook.createCellStyle();
//            wrapText.setWrapText(true);

            //Creates a new cell in current row...
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue("State");
            //Creates a new cell in current row...
            cell = row.createCell(cellnum++);
            cell.setCellValue("Facility");
            //Creates a new cell in current row...
            cell = row.createCell(cellnum++);
            cell.setCellValue("Facility Backstop");
            //Creates a new cell in current row...
            cell = row.createCell(cellnum++);
            cell.setCellValue("Case Manager");
            
            cell = row.createCell(cellnum++);
            cell.setCellValue(new XSSFRichTextString("Refill"));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 7));
            
            cell = row.createCell(cellnum++);
            cell.setCellValue("");
            
            cell = row.createCell(cellnum++);
            cell.setCellValue("");
            
            cell = row.createCell(cellnum++);
            cell.setCellValue("");
            
            //1 Month Retention
            cell = row.createCell(cellnum++);
            cell.setCellValue(new XSSFRichTextString("One Month Retention"));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 10));
            
            cell = row.createCell(cellnum++);
            cell.setCellValue("");
            
            cell = row.createCell(cellnum++);
            cell.setCellValue("");
            
            //10 Months Retention
            cell = row.createCell(cellnum++);
            cell.setCellValue(new XSSFRichTextString("10 Months Retention"));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 13));
            
            cell = row.createCell(cellnum++);
            cell.setCellValue("");
            
            cell = row.createCell(cellnum++);
            cell.setCellValue("");
            
            //Creates a new cell in current row...
            cellnum = 0;
            row = sheet.createRow(rownum++);
            cell = row.createCell(cellnum++);
            cell.setCellValue("");

            cell = row.createCell(cellnum++);
            cell.setCellValue("");
            
            cell = row.createCell(cellnum++);
            cell.setCellValue("");
            
            cell = row.createCell(cellnum++);
            cell.setCellValue("");
            
            cell = row.createCell(cellnum++);
            cell.setCellValue("No of clients due for refill this week");
            //Creates a new cell in current row...
            cell = row.createCell(cellnum++);
            cell.setCellValue("No of clients on refill appointment who came for refill this week");
            //Creates a new cell in current row...
            cell = row.createCell(cellnum++);
            cell.setCellValue("No of clients who missed appointment this week.");
            //Creates a new cell in current row...
            cell = row.createCell(cellnum++);
            cell.setCellValue("No of clients who agreed to return to care");
            
            cell = row.createCell(cellnum++);
            cell.setCellValue("Number started within 1 month");
            
            cell = row.createCell(cellnum++);
            cell.setCellValue("Number defaulted within period");
            
            cell = row.createCell(cellnum++);
            cell.setCellValue("Percentage (%)");
            
            cell = row.createCell(cellnum++);
            cell.setCellValue("Number started within 10 months");
            
            cell = row.createCell(cellnum++);
            cell.setCellValue("Number lost to follow up uncomfirmed");
            
            cell = row.createCell(cellnum++);
            cell.setCellValue("Percentage (%)");

            //Call report builder method...
            reportByActiveStates(Long.parseLong(state), reportingDateBegin);
            
            for(Map<String, Object> rParams : reportParams) {
                String stateName = rParams.get("stateName").toString();
                String facilityName = rParams.get("facilityName").toString();
                String backstop = rParams.get("backStop").toString();
                String caseManagerName = rParams.get("caseManagerName").toString();
                int due = Integer.parseInt(rParams.get("due").toString());
                int refill = Integer.parseInt(rParams.get("refill").toString());
                int missed = Integer.parseInt(rParams.get("missed").toString());
                int agreed = Integer.parseInt(rParams.get("agreed").toString());
                int totalOneMonth = Integer.parseInt(rParams.get("totalOneMonth").toString());
                int missedOneMonth = Integer.parseInt(rParams.get("notVisitedOneMonth").toString());
                int percentage = Integer.parseInt(rParams.get("oneMonthPercent").toString());
                int totalTenMonths = Integer.parseInt(rParams.get("totalTenMonths").toString());
                int ltfuTenMonths = Integer.parseInt(rParams.get("ltfuTenMonths").toString());
                int percentageTenMonths = Integer.parseInt(rParams.get("tenMonthsPercent").toString());
                
                //Get for the retetntions...

                cellnum = 0;
                row = sheet.createRow(rownum++);
                
                cell = row.createCell(cellnum++);
                cell.setCellValue(stateName);

                cell = row.createCell(cellnum++);
                cell.setCellValue(facilityName);

                cell = row.createCell(cellnum++);
                cell.setCellValue(backstop);
                
                cell = row.createCell(cellnum++);
                cell.setCellValue(caseManagerName);
                
                cell = row.createCell(cellnum++);
                cell.setCellValue(due);
                
                cell = row.createCell(cellnum++);
                cell.setCellValue(refill);
                
                cell = row.createCell(cellnum++);
                cell.setCellValue(missed);
                
                cell = row.createCell(cellnum++);
                cell.setCellValue(agreed);
                
                cell = row.createCell(cellnum++);
                cell.setCellValue(totalOneMonth);
                
                cell = row.createCell(cellnum++);
                cell.setCellValue(missedOneMonth);
                
                cell = row.createCell(cellnum++);
                cell.setCellValue(percentage);
                
                cell = row.createCell(cellnum++);
                cell.setCellValue(totalTenMonths);
                
                cell = row.createCell(cellnum++);
                cell.setCellValue(ltfuTenMonths);
                
                cell = row.createCell(cellnum++);
                cell.setCellValue(percentageTenMonths);
                
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

    private void reportByActiveStates(Long state_id, String reportingDateBegin){
        try {
            jdbcUtil = new JDBCUtil();
            if(state_id == null)
                query = "SELECT state_id, name FROM state WHERE state_id IN (2,3,4,5,6,8,9,12,18,20,25,33,36) ORDER BY name ASC";                                        
            else
                query = "SELECT state_id, name FROM state WHERE state_id = "+state_id+" ORDER BY name ASC";                                        
            
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String stateName = resultSet.getString("name");
                Long stateId = resultSet.getLong("state_id");
                
                //Perform the export for all facilities in this state...
                System.out.println("Started for: "+stateName);
                getFacilitiesToReport(stateName, stateId, reportingDateBegin);
            }
            resultSet = null;            
        }
        catch (Exception exception) {
            exception.printStackTrace();
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private void getFacilitiesToReport(String stateName, long stateId, String reportingDateBegin){
        try {
            JDBCUtil jdbcUtil1 = new JDBCUtil();
            String query1 = "SELECT facility_id, name FROM facility WHERE facility_id IN (SELECT DISTINCT facility_id FROM patient) AND state_id = ? ORDER BY name ASC";                                        

            PreparedStatement preparedStatement1 = jdbcUtil1.getStatement(query1);
            preparedStatement1.setLong(1, stateId);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            while(resultSet1.next()) {
                String facilityName = resultSet1.getString("name");
                long facilityId = resultSet1.getLong("facility_id");
                
                System.out.println("Running for: "+facilityName);                
                getRetentionForCaseManager(facilityId, stateName, facilityName, "N/A", reportingDateBegin);               
            }
            resultSet1 = null;            
        }
        catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }
    
//    private void getCaseManager
    
    public void getRetentionForCaseManager(long facilityId, String stateName, String facilityName, String backStop, String reportingDateBegin){
         Map<String, Object> reportData = null;
         JDBCUtil jdbcUtil1 = null;
    //    query = "SELECT casemager_id, fullname FROM casemanager WHERE facility_id = " + id + " active = 1";
       
        String query1 = "SELECT casemanager_id, fullname FROM casemanager WHERE facility_id = " + facilityId;
        try {
            jdbcUtil1 = new JDBCUtil();
            PreparedStatement preparedStatement1 = jdbcUtil1.getStatement(query1);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            resultSet1.beforeFirst();
           
            while (resultSet1.next()) {
                String fullname = resultSet1.getString("fullname");
                System.out.println("Starting for case manager: "+fullname);
                cm(resultSet1.getLong("casemanager_id"), facilityId, stateName, facilityName, backStop, fullname, reportingDateBegin);                
            }
        }
        catch (Exception exception) {
            jdbcUtil1.disconnectFromDatabase();  //disconnect from database
        }          
    }

 
     private void cm(long casemanagerId, long facilityId, String stateName, String facilityName, String backStop, String fullName, String reportingDateBegin) {

        Map<String, Object> result = new HashMap<>();
       //Refill appointment in the last 7 days
       Date reportDate = DateUtil.parseStringToSqlDate(reportingDateBegin, "MM/dd/yyyy");
       executeUpdate("DROP TABLE IF EXISTS temp");
       String query1 = "CREATE LOCAL TEMPORARY TABLE temp AS SELECT patient_id FROM patient WHERE date_next_refill BETWEEN DATEADD('DAY', -7, '" + reportDate + "') AND '" + reportDate + "' AND facility_id = "+ facilityId +" AND casemanager_id = "+casemanagerId; 
       executeUpdate(query1);
       
       result.put("stateName", stateName);
       result.put("facilityName", facilityName);
       result.put("backStop",backStop);
       result.put("caseManagerName", fullName);

       query1 = "SELECT COUNT(*) AS count FROM temp";        
       int due = getCount(query1);
       result.put("due",due);
       
       //Patient with refill appointment who came for refill
       query1 = "SELECT COUNT(*) AS count FROM pharmacy WHERE patient_id IN (SELECT patient_id FROM temp) AND date_visit BETWEEN DATEADD('DAY', -7, '" + reportDate + "') AND '" + reportDate +"'"; 
       int refill = getCount(query1); 
       result.put("refill", refill);
       
       int missed = due - refill; 
       result.put("missed",missed);

       //Clients whose agreed date of return is greater the date of last fill
       query1 = "SELECT COUNT(*) AS count FROM patient WHERE patient_id IN (SELECT patient_id FROM temp) AND agreed_date IS NOT NULL AND agreed_date > date_last_refill"; 
       int agreed = getCount(query1);
       result.put("agreed",agreed);
       
       //1 month cohort
       executeUpdate("DROP TABLE IF EXISTS temp2");
       String query2 = "CREATE LOCAL TEMPORARY TABLE temp2 AS SELECT patient_id FROM patient WHERE MONTH(date_started) = MONTH(DATEADD('MONTH', -1, '" + reportDate + "')) AND facility_id = "+ facilityId +" AND casemanager_id = "+casemanagerId; 
//       String query2 = "CREATE LOCAL TEMPORARY TABLE temp2 AS SELECT patient_id FROM patient WHERE MONTH(date_started) = DATEADD('MONTH', -1, '" + reportDate + "') AND '" + reportDate + "' AND facility_id = "+ id +" AND casemanager_id = "+casemanagerId;
       executeUpdate(query2);
       
       query2 = "SELECT COUNT(*) AS count FROM temp2";
       int totalOneMonth = getCount(query2); 
       result.put("totalOneMonth", totalOneMonth);
       
       query2 = "SELECT COUNT(DISTINCT patient_id) AS count FROM clinic WHERE patient_id IN (SELECT patient_id FROM temp2) AND date_visit BETWEEN DATEADD('DAY', -14, '" + reportDate + "') AND '" + reportDate + "'"; 
       int notVisitedOneMonth = getCount(query2); 
       result.put("notVisitedOneMonth", notVisitedOneMonth);
       
       int percentage = 0;
       if(notVisitedOneMonth > 0 && totalOneMonth > 0)
            percentage = (notVisitedOneMonth * 100 + (totalOneMonth >> 1)) / totalOneMonth;
       result.put("oneMonthPercent", percentage);
       
       //10 months cohort
       executeUpdate("DROP TABLE IF EXISTS temp3");
       String query3 = "CREATE LOCAL TEMPORARY TABLE temp3 AS SELECT patient_id, current_status, date_last_refill, last_refill_duration,date_started FROM patient WHERE MONTH(date_started) = MONTH(DATEADD('MONTH', -10, '" + reportDate + "')) AND facility_id = "+ facilityId +" AND casemanager_id = "+casemanagerId; 
       executeUpdate(query3);
       
       query3 = "SELECT COUNT(*) AS count FROM temp3";
       int totalTenMonths = getCount(query3); 
       result.put("totalTenMonths", totalTenMonths);
       
       query3 = "SELECT COUNT(*) AS count FROM temp3 WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND DATEDIFF(DAY, date_last_refill + last_refill_duration, '" + reportDate + "') > "+Constants.LTFU.PEPFAR + " AND date_started IS NOT NULL"; 
       int ltfuTenMonths = getCount(query3); 
       result.put("ltfuTenMonths", ltfuTenMonths);
       
       int percentages = 0;
       if(ltfuTenMonths > 0 && totalTenMonths > 0)
            percentages = (ltfuTenMonths * 100 + (totalTenMonths >> 1)) / totalTenMonths;
       result.put("tenMonthsPercent", percentages);
       
       reportParams.add(result);

     }
     
     private void executeUpdate(String query) {
        try {
            jdbcUtil = new JDBCUtil();
            
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }        
    }        
    
    private int getCount(String query) {
       int count  = 0;
       ResultSet resultSet;
       try {
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                count = resultSet.getInt("count");
            }
            resultSet = null;                        
        }
        catch (Exception exception) {
            resultSet = null;                      
            exception.printStackTrace();
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return count;
    }      
            
}
