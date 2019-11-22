/**
 * @author aalozie
 */

package org.fhi360.lamis.service;

import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.FileUtil;

import java.sql.Timestamp;

import org.fhi360.lamis.utility.Constants;
import org.springframework.stereotype.Component;

@Component
public class ExportService {
    private static JDBCUtil jdbcUtil;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static Timestamp timestamp;
    private final ApplicationProperties applicationProperties;
    private final XmlParserDelegator xmlParserDelegator;

    public ExportService(ApplicationProperties applicationProperties, XmlParserDelegator xmlParserDelegator) {
        this.applicationProperties = applicationProperties;
        this.xmlParserDelegator = xmlParserDelegator;
    }

    public synchronized String buildXml(HttpSession session) {
        timestamp = new Timestamp(new java.util.Date().getTime());

        String contextPath = applicationProperties.getContextPath();
        String directory = contextPath + "exchange/";
        long facilityId = (Long) session.getAttribute("id");

        StringBuilder xmlFiles = new StringBuilder();

        String[] tables = Constants.Tables.TRANSACTION_TABLES.split(",");
        try {
            for (String table : tables) {
                session.setAttribute("processingStatus", table);
                xmlFiles.append(directory).append(table).append(".xml,");
                resultSet = getResultSet(table, session, true);
                xmlParserDelegator.delegate(table, directory);
            }
        } catch (Exception exception) {
            session.setAttribute("processingStatus", "terminated");
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
            throw new RuntimeException(exception);
        }

        FileUtil fileUtil = new FileUtil();
        fileUtil.makeDir(contextPath + "transfer/");
        fileUtil.makeDir(applicationProperties.getContextPath() + "/transfer/");

        String fileName = "lamis.zip";
        if (session.getAttribute("facilityName") != null) {
            fileName = session.getAttribute("facilityName") + ".zip";
        }
        String zipFile = contextPath + "transfer/" + fileName;


        try {
            String[] files = xmlFiles.toString().split(",");
            fileUtil.zip(files, zipFile);
            //for servlets in the default(root) context, copy file to the transfer folder in root 
            if (!contextPath.equalsIgnoreCase(applicationProperties.getContextPath()))
                fileUtil.copyFile(fileName, contextPath + "transfer/", applicationProperties.getContextPath() + "/transfer/");
            updateExportTime(session);
        } catch (Exception exception) {
            session.setAttribute("processingStatus", "terminated");
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
        session.setAttribute("processingStatus", "completed");
        return "transfer/" + fileName;
    }

    private static ResultSet getResultSet(String table, HttpSession session, boolean recordsAll) {
        long facilityId = (Long) session.getAttribute("id");

        try {
            String query = (recordsAll) ? "SELECT * FROM " + table + " WHERE facility_id = " + facilityId : "SELECT * FROM " + table + " WHERE facility_id = " + facilityId + " AND time_stamp > SELECT export FROM exchange WHERE facility_id = " + facilityId;
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
            throw new RuntimeException(exception);
        }
        return resultSet;
    }

    private static void updateExportTime(HttpSession session) {
        try {
            String query = "UPDATE exchange SET export = ? WHERE facility_id = ?";
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.setTimestamp(1, timestamp);
            preparedStatement.setLong(2, (Long) session.getAttribute("id"));
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
            throw new RuntimeException(exception);
        }
    }
}
//query = query.replace("[table]", table);
