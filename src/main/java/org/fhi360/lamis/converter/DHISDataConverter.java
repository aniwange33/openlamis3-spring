/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.converter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.fhi360.lamis.config.ContextProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author user10
 */
@Component
@Slf4j
public class DHISDataConverter implements ServletContextAware {
    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }


    private final NamedParameterJdbcTemplate parameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);

    public DHISDataConverter() {
        parameterJdbcTemplate = new NamedParameterJdbcTemplate(ContextProvider.getBean(DataSource.class));
    }

    public synchronized ByteArrayOutputStream convertExcel(String period, String stateId, String facilityId, Long userId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Map<String, Object> params = new HashMap<>();
        params.put("period", period);
        if ((StringUtils.isBlank(stateId) || StringUtils.equals(stateId, "null")) &&
                (StringUtils.isBlank(facilityId) || StringUtils.equals(facilityId, "null"))) {
            List<Long> ids = jdbcTemplate.queryForList("select distinct facility_id from patient", Long.class);
            params.put("facilities", ids);
        } else if (StringUtils.isBlank(facilityId)) {
            List<Long> ids = jdbcTemplate.queryForList("select facility_id from facility where state_id = ?",
                    Long.class, stateId);
            params.put("facilities", ids);
        } else {
            params.put("facilities", Collections.singletonList(facilityId));
        }

        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);
        workbook.setCompressTempFiles(true);
        Sheet sheet = workbook.createSheet();
        final int[] rownum = {0};
        final int[] cellnum = {0};
        final Row[] row = {sheet.createRow(rownum[0]++)};
        final Cell[] cell = {row[0].createCell(cellnum[0]++)};
        cell[0].setCellValue("State");
        cell[0] = row[0].createCell(cellnum[0]++);
        cell[0].setCellValue("LGA");
        cell[0] = row[0].createCell(cellnum[0]++);
        cell[0].setCellValue("Facility");
        cell[0] = row[0].createCell(cellnum[0]++);
        cell[0].setCellValue("Period");
        cell[0] = row[0].createCell(cellnum[0]++);
        cell[0].setCellValue("Data Element");
        cell[0] = row[0].createCell(cellnum[0]++);
        cell[0].setCellValue("Category");
        cell[0] = row[0].createCell(cellnum[0]);
        cell[0].setCellValue("Value");

        String query = "SELECT d.period, d.value, i.description AS DATA_ELEMENT, c.description AS CATEGORY_COMB, " +
                "f.name AS FACILITY, l.name as lga, s.name as state FROM dhisvalue d INNER JOIN dhiscodeset i " +
                "ON d.data_element_id = i.lamis_id INNER JOIN dhiscodeset c " +
                "ON d.category_id = c.lamis_id inner JOIN facility f ON d.facility_id = f.facility_id " +
                "inner join lga l on l.lga_id = f.lga_id inner join state s on l.state_id = s.state_id " +
                "WHERE d.period = :period and d.facility_id in (:facilities) and i.code_set_nm = 'DATA ELEMENT WR' " +
                "and c.code_set_nm = 'CATEGORY COMBO' order by FACILITY";
        parameterJdbcTemplate.query(query, params, rs -> {
            while (rs.next()) {
                cellnum[0] = 0;
                row[0] = sheet.createRow(rownum[0]++);
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(rs.getString("state"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(rs.getString("lga"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(rs.getString("facility"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(rs.getString("period"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(rs.getString("DATA_ELEMENT"));
                cell[0] = row[0].createCell(cellnum[0]++);
                cell[0].setCellValue(rs.getString("CATEGORY_COMB"));
                cell[0] = row[0].createCell(cellnum[0]);
                cell[0].setCellValue(rs.getDouble("value"));
                if (rownum[0] % 100 == 0) {
                    try {
                        ((SXSSFSheet) sheet).flushRows(100);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                workbook.write(outputStream);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            workbook.dispose();
            return null;
        });
        return outputStream;
    }

     /*try {
        executeUpdate("DROP TABLE IF EXISTS report");
        executeUpdate("CREATE TEMPORARY TABLE report AS SELECT d.period, d.value, i.description AS DATA_ELEMENT, c.description AS CATEGORY_COMB, f.name AS FACILITY FROM dhisvalue d JOIN indicator i ON d.data_element_id = i.data_element_id JOIN categorycomb c ON d.category_id = c.category_id JOIN facility f ON d.facility_id = f.facility_id WHERE d.period IN ('2019W14', '2019W15', '2019W16', '2019W17', '2019W18')");
        executeUpdate("call CSVWRITE('c:/lamis2/report.csv', 'select * from report')");

    } catch (Exception ex) {

    }*/

    public String test() {
        return "";
    }
}
