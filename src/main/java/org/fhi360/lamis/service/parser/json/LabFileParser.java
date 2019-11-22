/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import au.com.bytecode.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Iterator;


/**
 * @author Idris
 */
@Component
@Slf4j
public class LabFileParser {
    private String query;
    private static JDBCUtil jdbcUtil;
    private static PreparedStatement preparedStatement;
   private final ApplicationProperties properties;

    public LabFileParser(ApplicationProperties properties) {
        this.properties = properties;
    }

    public void parseFile(String attachmentFileName) {
        executeUpdate("DROP TABLE IF EXISTS labresult");
        executeUpdate("CREATE TEMPORARY TABLE labresult (labno varchar(15), result varchar(25), date_assay date)");
        String fileName = properties.getContextPath() + "transfer/" + attachmentFileName;
        if (attachmentFileName.toLowerCase().endsWith("csv")) {
            csvFile(fileName);
        } else {
            if (attachmentFileName.toLowerCase().endsWith("xls") || attachmentFileName.toLowerCase().endsWith("xlsx")) {
                excelFile(fileName);
            } else {
                //textFile(fileName);
            }
        }
    }

    private void excelFile(String fileName) {
        int rowcount = 0;
        String labno = "";
        String result = "";
        Date dateAssay = null;
        try {
            //Create the input stream from xlsx/xls file
            File file = new File(fileName);
            FileInputStream inputStream = new FileInputStream(file);

            //Create Workbook instance for xlsx/xls file input stream
            Workbook workbook = null;
            if (fileName.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else if (fileName.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(inputStream);
            }

            Sheet sheet = workbook.getSheetAt(0);
            Row row = null;
            Cell cell = null;
            Iterator<Row> iterator = sheet.iterator();
            while (iterator.hasNext()) {
                rowcount++;
                row = iterator.next();
                if (rowcount > 1) {
                    labno = "";
                    result = "";
                    dateAssay = null;
                    cell = row.getCell(4);
                    if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                labno = cell.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                labno = Double.toString(cell.getNumericCellValue());
                                break;
                        }
                    }
                    cell = row.getCell(9);
                    if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) result = cell.getStringCellValue();
                    } else {
                        cell = row.getCell(8);
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) result = cell.getStringCellValue();
                        if (result.equalsIgnoreCase("Target Not Detected")) result = "Negative";
                    }
                    cell = row.getCell(38);
                    if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                dateAssay = cell.getDateCellValue();
                            }
                        }
                    }

                    if (dateAssay == null) {
                        query = "INSERT INTO labresult(labno, result, date_assay) VALUES('" + labno + "', '" + result + "', null)";
                    } else {
                        query = "INSERT INTO labresult(labno, result, date_assay) VALUES('" + labno + "', '" + result + "', '" + DateUtil.formatSqlDate(dateAssay, "yyyy-MM-dd") + "')";
                    }
                    executeUpdate(query);
                }
            }
            inputStream.close();
            file = null;
            iterator = null;
            workbook = null;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void csvFile(String fileName) {
        int rowcount = 0;
        String labno = "";
        String result = "";
        String dateAssay = "";
        String[] row = null;
        try {
            CSVReader csvReader = new CSVReader(new FileReader(fileName));
            while ((row = csvReader.readNext()) != null) {
                rowcount++;
                if (rowcount > 1) {
                    labno = row[4];
                    result = row[9];
                    dateAssay = row[38];
                    if (result.trim().isEmpty()) {
                        result = row[8];
                        if (result.equalsIgnoreCase("Target Not Detected")) result = "Negative";
                    }
                    if (dateAssay.trim().isEmpty()) {
                        query = "INSERT INTO labresult(labno, result, date_assay) VALUES('" + labno + "', '" + result + "', null)";
                    } else {
                        query = "INSERT INTO labresult(labno, result, date_assay) VALUES('" + labno + "', '" + result + "', '" + DateUtil.formatDateStringToSqlDate(dateAssay, "MM/dd/yyyy hh:mm", "yyyy-MM-dd") + "')";     //eg String dateString = "9/23/2014 15:20" to "2014-9-23";
                    }
                    executeUpdate(query);
                }
            }
            csvReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void textFile(String fileName) {
        int rowcount = 0;
        String labno = "";
        String result = "";
        String dateAssay = "";
        String content = "";
        String[] row = null;
        try {
            File lab = new File(fileName);
            InputStream inputStream = new FileInputStream(lab);
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            while ((content = bufferedReader.readLine()) != null) {
                rowcount++;
                if (rowcount > 1) {
                    row = content.split(",");
                    labno = row[4];
                    result = row[9];
                    dateAssay = row[38];
                    if (result.trim().isEmpty()) {
                        result = row[8];
                        if (result.equalsIgnoreCase("Target Not Detected")) result = "Negative";
                    }
                    System.out.println(labno + " is " + result + " at " + dateAssay);
                    query = "INSERT INTO labresult(labno, result) VALUES('" + labno + "', '" + result + "')";
                    executeUpdate(query);
                }
            }
            bufferedReader.close();
            reader.close();
            inputStream.close();
            lab = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void executeUpdate(String query) {
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }


}
