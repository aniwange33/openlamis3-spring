/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.fhi360.lamis.model.dto.RegimenIntrospector;
import org.fhi360.lamis.utility.JDBCUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

/**
 * @author user10
 */
@Component
public class PedeatricsDataConverter implements ServletContextAware {

    private JDBCUtil jdbcUtil;
    private static final Logger LOG = LoggerFactory.getLogger(PedeatricsDataConverter.class);

    public PedeatricsDataConverter() {

    }

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    private void ppr() {
        try {
            jdbcUtil = new JDBCUtil();
            executeUpdate("DROP TABLE IF EXISTS ppr", jdbcUtil);
            executeUpdate("CREATE TABLE ppr AS SELECT facility_id, regimen, TIMESTAMPDIFF(YEAR, date_birth, CURDATE()) AS age, COUNT(*) AS total FROM patient WHERE current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND regimen IS NOT NULL GROUP BY facility_id, regimen, age", jdbcUtil);

            ResultSet resultSet = executeQuery("SELECT regimen FROM ppr", jdbcUtil);
            while (resultSet.next()) {
                String regimen = RegimenIntrospector.resolveRegimen(resultSet.getString("regimen"));

                String query = "UPDATE ppr SET regimen = '" + regimen + "' WHERE regimen = '" + resultSet.getString("regimen") + "'";
                executeUpdate(query, jdbcUtil);
            }
            jdbcUtil.disconnectFromDatabase();
        } catch (Exception exception) {
            exception.printStackTrace();
            jdbcUtil.disconnectFromDatabase();
        }

    }

    public synchronized ByteArrayOutputStream convertExcel(Long userId, String facilityIds, String state) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);
        workbook.setCompressTempFiles(true);
        Sheet sheet = workbook.createSheet();
        ppr();

        try (Statement statement = new JDBCUtil().getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery("select facility.name, ppr.regimen, ppr.age, " +
                    "ppr.total from ppr join facility on ppr.facility_id = facility.facility_id " +
                    "order by facility.name");

            ResultSetMetaData rsMetaData = rs.getMetaData();
            int numberOfColumns = rsMetaData.getColumnCount();
            int rownum = 0;
            int cellnum = 0;
            Row row = sheet.createRow(rownum++);
            for (int i = 1; i < numberOfColumns + 1; i++) {
                Cell cell = row.createCell(cellnum++);
                cell.setCellValue(rsMetaData.getColumnName(i));
            }
            while (rs.next()) {
                row = sheet.createRow(rownum++);
                cellnum = 0;
                for (int i = 1; i < numberOfColumns + 1; i++) {
                    Cell cell = row.createCell(cellnum++);
                    Object value = rs.getObject(rsMetaData.getColumnName(i));
                    cell.setCellValue(value != null ? value.toString() : null);
                }
                if (rownum % 100 == 0) {
                    ((SXSSFSheet) sheet).flushRows(100);
                }
            }
            workbook.write(outputStream);
            outputStream.close();
            workbook.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return outputStream;
    }

    public void executeUpdate(String query, JDBCUtil jdbcUtil) {
        try {
            Statement st = jdbcUtil.getStatement();
            st.executeUpdate(query);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private ResultSet executeQuery(String query, JDBCUtil jdbcUtil) {
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return rs;
    }

}
