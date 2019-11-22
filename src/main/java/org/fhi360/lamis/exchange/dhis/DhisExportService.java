/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.dhis;

/**
 * @author user1
 */

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.utility.FileUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.StringUtil;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;

public class DhisExportService {
    private static JDBCUtil jdbcUtil;
    private static PreparedStatement preparedStatement;
    private static Timestamp timestamp;
    private final ApplicationProperties applicationProperties = ContextProvider.getBean(ApplicationProperties.class);

    public synchronized String buildXml() {
        timestamp = new Timestamp(new java.util.Date().getTime());

        HttpSession session = null;
        String contextPath = applicationProperties.getContextPath();
        String stcontextPath = applicationProperties.getContextPath();
        String table = "datavalue";
        try {
            ResultSet resultSet = executeQuery("SELECT state, lga, org_unit, SUM(value) FROM datavalue GROUP BY category_option_combo");
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement(StringUtil.pluralize(table));
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Element row = root.addElement(table);
                for (int i = 1; i <= colCount; i++) {
                    String columnName = metaData.getColumnName(i).toLowerCase();
                    Object value = resultSet.getObject(i) == null ? "" : resultSet.getObject(i);

                    Element column = row.addElement(columnName);
                    column.setText(value.toString());
                }
            }
            String fileName = contextPath + "exchange/" + table + ".xml";
            FileOutputStream outputStream = new FileOutputStream(new File(fileName));
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(outputStream, format);
            writer.write(document);
            writer.flush();

            writer.close();
            outputStream.close();
            resultSet = null;
            document = null;
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
            throw new RuntimeException(exception);
        }
        String[] myFiles = {contextPath + "exchange/datavalue.xml"};

        String directory = contextPath + "archive/";
        FileUtil fileUtil = new FileUtil();
        fileUtil.makeDir(directory);
        fileUtil.makeDir(stcontextPath + "archive/");
        fileUtil.makeDir(applicationProperties.getContextPath() + "/archive/");

        String fileName = "lamis.zip";
        if (session.getAttribute("state") != null) {
            fileName = (String) session.getAttribute("state") + ".zip";
        }
        String zipFile = directory + fileName;
        try {
            fileUtil.zip(myFiles, zipFile);
            //for servlets in the stand alone (webapps) context, copy file to the transfer folder in webapps 
            fileUtil.copyFile(fileName, contextPath + "archive/", stcontextPath + "archive/");
            //for servlets in the default(root) context, copy file to the transfer folder in root 
            if (!contextPath.equalsIgnoreCase(applicationProperties.getContextPath()))
                fileUtil.copyFile(fileName, contextPath + "archive/", applicationProperties.getContextPath() + "/archive/");
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
        return "archive/" + fileName;
    }

    private static ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return resultSet;
    }
}
