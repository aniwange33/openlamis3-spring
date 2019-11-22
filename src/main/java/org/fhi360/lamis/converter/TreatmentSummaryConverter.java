/**
 * @author aalozie
 */

package org.fhi360.lamis.converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TreatmentSummaryConverter implements ServletContextAware {

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private List<Map<String, Object>> reportParams = new ArrayList<>();
    //private IndicatorMonitorReport iMReport = new IndicatorMonitorReport();

    public TreatmentSummaryConverter() {

    }

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public synchronized ByteArrayOutputStream convertExcel(String state, String reportingDateBegin) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);  // turn off auto-flushing and accumulate all rows in memory
        Sheet sheet = workbook.createSheet();

        try {

            int rownum = 0;
            int cellnum = 0;
            //Create a new Row...
            Row row = sheet.createRow(rownum++);
            //Creates a new cell in current row...
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue("State");
            //Creates a new cell in current row...
            cell = row.createCell(cellnum++);
            cell.setCellValue("Facility");
            //Creates a new cell in current row...
            cell = row.createCell(cellnum++);
            cell.setCellValue(new XSSFRichTextString("TLD Transition"));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 4));

            cell = row.createCell(cellnum++);
            cell.setCellValue("");

            cell = row.createCell(cellnum++);
            cell.setCellValue("");
            //Creates a new cell in current row...
            cell = row.createCell(cellnum++);
            cell.setCellValue(new XSSFRichTextString("LPV/r"));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 7));

            cell = row.createCell(cellnum++);
            cell.setCellValue("");

            cell = row.createCell(cellnum++);
            cell.setCellValue("");
            //Creates a new cell in current row...
            cell = row.createCell(cellnum++);
            cell.setCellValue(new XSSFRichTextString("DMOC"));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 14));
            //Creates a new cell in current row...
            cell = row.createCell(cellnum++);
            cell.setCellValue("");
            //Creates a new cell in current row...
            cell = row.createCell(cellnum++);
            cell.setCellValue("");
            //Creates a new cell in current row...
            cell = row.createCell(cellnum++);
            cell.setCellValue("");

            cell = row.createCell(cellnum++);
            cell.setCellValue("");

            cell = row.createCell(cellnum++);
            cell.setCellValue("");

            cell = row.createCell(cellnum++);
            cell.setCellValue("");

            //Creates a new cell in current row...
            cell = row.createCell(cellnum++);
            cell.setCellValue(new XSSFRichTextString("TX_NEW"));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 15, 17));

            cell = row.createCell(cellnum++);
            cell.setCellValue("");

            cell = row.createCell(cellnum++);
            cell.setCellValue("");

            //Creates a new cell in current row...
            cell = row.createCell(cellnum++);
            cell.setCellValue(new XSSFRichTextString("TX_ML"));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 18, 21));

            cell = row.createCell(cellnum++);
            cell.setCellValue("");

            cell = row.createCell(cellnum++);
            cell.setCellValue("");

            cell = row.createCell(cellnum++);
            cell.setCellValue("");

            //Create the second Row...
            cellnum = 0;
            row = sheet.createRow(rownum++);
            cell = row.createCell(cellnum++);
            cell.setCellValue("");

            cell = row.createCell(cellnum++);
            cell.setCellValue("");

            //TLD empty
            cell = row.createCell(cellnum++);
            cell.setCellValue("Total");

            cell = row.createCell(cellnum++);
            cell.setCellValue("Expected");

            cell = row.createCell(cellnum++);
            cell.setCellValue("Percentage (%)");

            //LPV/r Empty
            cell = row.createCell(cellnum++);
            cell.setCellValue("Number on LPV");

            cell = row.createCell(cellnum++);
            cell.setCellValue("Current under 3 years");

            cell = row.createCell(cellnum++);
            cell.setCellValue("Percentage (%)");

            //DMOC Not Empty
            cell = row.createCell(cellnum++);
            cell.setCellValue("CPARP");

            cell = row.createCell(cellnum++);
            cell.setCellValue("CARC");

            cell = row.createCell(cellnum++);
            cell.setCellValue("MMS");

            cell = row.createCell(cellnum++);
            cell.setCellValue("MMD");

            cell = row.createCell(cellnum++);
            cell.setCellValue("TX_CURR");

            cell = row.createCell(cellnum++);
            cell.setCellValue("Expected");

            cell = row.createCell(cellnum++);
            cell.setCellValue("Percentage (%)");

            //TX_NEW empty
            cell = row.createCell(cellnum++);
            cell.setCellValue("New");

            cell = row.createCell(cellnum++);
            cell.setCellValue("Assigned");

            cell = row.createCell(cellnum++);
            cell.setCellValue("Percentage (%)");

            //TX_ML data
            cell = row.createCell(cellnum++);
            cell.setCellValue(Constants.TxMlStatus.TX_ML_DIED);

            cell = row.createCell(cellnum++);
            cell.setCellValue(Constants.TxMlStatus.TX_ML_TRANSFER);

            cell = row.createCell(cellnum++);
            cell.setCellValue(Constants.TxMlStatus.TX_ML_TRACED);

            cell = row.createCell(cellnum++);
            cell.setCellValue(Constants.TxMlStatus.TX_ML_NOT_TRACED);

            //Call method below...
            reportByActiveStates(Long.parseLong(state), reportingDateBegin);

            for (Map<String, Object> rParams : reportParams) {
                String stateName = rParams.get("stateName").toString();
                String facilityName = rParams.get("facilityName").toString();
                List<Integer> tld = (List<Integer>) rParams.get("tld");
                List<Integer> lpv = (List<Integer>) rParams.get("lpv/r");
                List<Integer> dmoc = (List<Integer>) rParams.get("dmoc");
                List<Integer> tx_new = (List<Integer>) rParams.get("tx_new");
                List<Integer> tx_ml1 = (List<Integer>) rParams.get("tx_ml1");
                List<Integer> tx_ml2 = (List<Integer>) rParams.get("tx_ml2");
                List<Integer> tx_ml3 = (List<Integer>) rParams.get("tx_ml3");
                List<Integer> tx_ml4 = (List<Integer>) rParams.get("tx_ml4");

                cellnum = 0;
                row = sheet.createRow(rownum++);
                cell = row.createCell(cellnum++);
                cell.setCellValue(stateName);

                cell = row.createCell(cellnum++);
                cell.setCellValue(facilityName);

                for (int tl : tld) {
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(tl);
                }

                for (int lp : lpv) {
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(lp);
                }

                for (int dm : dmoc) {
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(dm);
                }

                for (int tx : tx_new) {
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(tx);
                }

                for (int tx : tx_ml1) {
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(tx);
                }

                for (int tx : tx_ml2) {
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(tx);
                }

                for (int tx : tx_ml3) {
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(tx);
                }

                for (int tx : tx_ml4) {
                    cell = row.createCell(cellnum++);
                    cell.setCellValue(tx);
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

    private void reportByActiveStates(Long state_id, String reportingDateBegin) {
        try {
            jdbcUtil = new JDBCUtil();
            if (state_id == null)
                query = "SELECT state_id, name FROM state WHERE state_id IN (2,3,4,5,6,8,9,12,18,20,25,33,36) ORDER BY name ASC";
            else
                query = "SELECT state_id, name FROM state WHERE state_id = " + state_id + " ORDER BY name ASC";

            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String stateName = resultSet.getString("name");
                Long stateId = resultSet.getLong("state_id");

                //Perform the export for all facilities in this state...
                System.out.println("Started for: " + stateName);
                facilityByFacility(stateName, stateId, reportingDateBegin);
            }
            resultSet = null;
        } catch (Exception exception) {
            exception.printStackTrace();
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private void facilityByFacility(String stateName, long stateId, String reportingDateBegin) {
        Map<String, Object> result;
        ResultSet resultSet1 = null;
        try {
            JDBCUtil jdbcUtil1 = new JDBCUtil();
            String query1 = "SELECT facility_id, name FROM facility WHERE facility_id IN (SELECT DISTINCT facility_id FROM patient) AND state_id = ? ORDER BY name ASC";

            PreparedStatement preparedStatement1 = jdbcUtil1.getStatement(query1);
            preparedStatement1.setLong(1, stateId);
            resultSet1 = preparedStatement1.executeQuery();
            while (resultSet1.next()) {
                result = new HashMap<>();
                String facilityName = resultSet1.getString("name");
                Long facilityId = resultSet1.getLong("facility_id");

                //Perform the Calculation and plug into the Excel Sheet
                System.out.println("Started for Facility Name: " + facilityName + " and id: " + facilityId);
                //Put Basic Information...
                result.put("stateName", stateName);
                result.put("facilityName", facilityName);

//                //For TLD
//                result.put("tld", iMReport.getTld(id, stateId, reportingDateBegin));
//
//                //For Lpv/r
//                result.put("lpv/r", iMReport.getLpv(id, reportingDateBegin));
//
//                //For DMOC
//                result.put("dmoc", iMReport.getDmoc(id, reportingDateBegin));
//
//                //For TX_NEW
//                result.put("tx_new", iMReport.getTxNew(id, reportingDateBegin));
//
//                //For TX_NEW
//                result.put("tx_ml1", iMReport.getTxML1(id, reportingDateBegin));
//
//                //For TX_NEW
//                result.put("tx_ml2", iMReport.getTxML2(id, reportingDateBegin));
//
//                //For TX_NEW
//                result.put("tx_ml3", iMReport.getTxML3(id, reportingDateBegin));
//
//                //For TX_NEW
//                result.put("tx_ml4", iMReport.getTxML4(id, reportingDateBegin));

                reportParams.add(result);
            }
            resultSet1 = null;
        } catch (Exception exception) {
            exception.printStackTrace();
            resultSet1 = null;
//            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

}
