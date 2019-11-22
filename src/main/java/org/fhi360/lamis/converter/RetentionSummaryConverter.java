/**
 * @author aalozie
 */

package org.fhi360.lamis.converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

@Component
public class RetentionSummaryConverter implements ServletContextAware {
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private long userId;

    public RetentionSummaryConverter() {

    }

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public synchronized ByteArrayOutputStream convertExcel(Long userId, String facilityIds, String state) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);
        Sheet sheet = workbook.createSheet();

        try {
            jdbcUtil = new JDBCUtil();

            int rownum = 0;
            int cellnum = 0;
            Row row = sheet.createRow(rownum++);
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue("Facility");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Started On ART");
            cell = row.createCell(cellnum++);
            cell.setCellValue("ART Transfer Out");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Lost to Follow Up");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Stopped Treament");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Known Death");
            cell = row.createCell(cellnum++);
            cell.setCellValue("Alive and on Treatment");

            query = "SELECT DISTINCT patient.facility_id, facility.name FROM patient JOIN facility ON patient.facility_id = facility.facility_id WHERE patient.facility_id IN (" + facilityIds + ") ORDER BY facility.name";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long facilityId = resultSet.getLong("facility_id");

                cellnum = 0;
                row = sheet.createRow(rownum++);
                cell = row.createCell(cellnum++);
                cell.setCellValue(resultSet.getString("name"));


                int artStart = 0;
                int artTransferOut = 0;
                int LTFU = 0;
                int stopped = 0;
                int dead = 0;
                int alive = 0;
                int vlSuppressed = 0;
                int vlDue = 0;

                String dateBegin = "2016-10-01";
                String dateEnd = "2017-09-30";

                query = "SELECT current_status, date_started, last_viral_load, viral_load_due_date FROM patient WHERE facility_id = " + facilityId + " AND date_started >= '" + dateBegin + "' AND date_started <= '" + dateEnd + "' AND current_status IS NOT NULL";
                preparedStatement = jdbcUtil.getStatement(query);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    String currentStatus = rs.getString("current_status");
                    double lastViralLoad = rs.getDouble("last_viral_load");
                    Date viralLoadDueDate = rs.getDate("viral_load_due_date");

                    artStart++;
                    if (currentStatus.equalsIgnoreCase("ART Start")) {
                        //check for virally suppresed and due for VL
                        if (lastViralLoad < 1000) {
                            vlSuppressed++;
                        }

                        if (viralLoadDueDate != null && viralLoadDueDate.after(new Date())) {
                            vlDue++;
                        }
                    }

                    if (currentStatus.equalsIgnoreCase("ART Transfer In")) {
                        //check for virally suppresed and due for VL
                        if (lastViralLoad < 1000) {
                            vlSuppressed++;
                        }

                        if (viralLoadDueDate != null && viralLoadDueDate.after(new Date())) {
                            vlDue++;
                        }
                    }

                    if (currentStatus.equalsIgnoreCase("ART Restart")) {
                        //check for virally suppresed and due for VL
                        if (lastViralLoad < 1000) {
                            vlSuppressed++;
                        }

                        if (viralLoadDueDate != null && viralLoadDueDate.after(new Date())) {
                            vlDue++;
                        }
                    }

                    if (currentStatus.equalsIgnoreCase("ART Transfer Out")) {
                        artTransferOut++;
                    }

                    if (currentStatus.equalsIgnoreCase("Lost to Follow Up")) {
                        LTFU++;
                    }

                    if (currentStatus.equalsIgnoreCase("Stopped Treatment")) {
                        stopped++;
                    }

                    if (currentStatus.equalsIgnoreCase("Known Death")) {
                        dead++;
                    }
                }
                //Alive and on treatment
                alive = artStart - artTransferOut - LTFU - stopped - dead;

                cell = row.createCell(cellnum++);
                cell.setCellValue(artStart);

                cell = row.createCell(cellnum++);
                cell.setCellValue(artTransferOut);

                cell = row.createCell(cellnum++);
                cell.setCellValue(LTFU);

                cell = row.createCell(cellnum++);
                cell.setCellValue(stopped);

                cell = row.createCell(cellnum++);
                cell.setCellValue(dead);

                cell = row.createCell(cellnum++);
                cell.setCellValue(alive);

//                cell = row.createCell(cellnum++);
//                cell.setCellValue(vlSuppressed);
//
//                cell = row.createCell(cellnum++);
//                cell.setCellValue(vlDue);
            }

            workbook.write(outputStream);
            outputStream.close();
            workbook.dispose();  // dispose of temporary files backing this workbook on disk
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return outputStream;
    }

}
