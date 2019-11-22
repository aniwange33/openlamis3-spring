/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service;

import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 *
 * @author MEdor
 */
@Component
public class SyncUtilService {

    private static PreparedStatement preparedStatement;
    private static JDBCUtil jdbcUtil;
    private static ResultSet resultSet;

    public static void syncFolder() {
        executeUpdate("DROP TABLE IF EXISTS sync");
        executeUpdate("CREATE TABLE sync (facility_id int, facility_name varchar(100), last_modified date)");
        String contextPath = "";//ServletActionContext.getServletContext().getInitParameter("contextPath");
        final String name = contextPath + "exchange/sync/";
        File directory = new File(name);

        //get all the files from a directory
        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {
                if (file.isDirectory()) {
                    String facilityId = file.getName();
                    File[] tableList = file.listFiles();
                    boolean lockFile = Arrays.stream(tableList)
                            .anyMatch(f -> f.getName().equals("lock.ser"));
                    if (lockFile) {
                        try {
                            jdbcUtil = new JDBCUtil();
                            String query = "SELECT name FROM facility WHERE facility_id = " + facilityId;
                            resultSet = executeQuery(query);
                            while (resultSet.next()) {
                                String facilityName = resultSet.getString("name");

                                PreparedStatement statement = jdbcUtil.getStatement("INSERT INTO sync (facility_id, facility_name, last_modified) VALUES("
                                        + facilityId + ", '" + facilityName + "', ?)");
                                statement.setDate(1, new Date(file.lastModified()));
                                statement.execute();
                                System.out.println("Inserted....");
                            }
                        } catch (SQLException exception) {
                            jdbcUtil.disconnectFromDatabase();  //disconnect from database
                        }
                        executeUpdate("call CSVWRITE('C:/lamis2/sync.csv', 'select * from sync')");
                    }
                }
            }
        }
    }

    private static void executeUpdate(String query) {
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException | IllegalStateException exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private static ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
        } catch (SQLException | IllegalStateException exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return rs;
    }

}
