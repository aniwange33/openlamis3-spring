/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.util.Map;

/**
 *
 * @author user1
 */
public class DatabaseUpdater {
    private static JDBCUtil jdbcUtil;
    private static PreparedStatement preparedStatement;

    private static String contextPath;
    
    public static void update() {
        File file  = null;
        try {
            file = new File("system_setting.properties");
            if (file.exists()) {
                Map<String, Object> map = new PropertyAccessor().getSystemProperties();
                contextPath = (String) map.get("contextPath");
            } 
            else {
                System.out.println("System properties file not found .....");              
            }
            
            file = new File(contextPath+"version/manifest.txt");
            if (file.exists()) {
                String line = "";
                InputStream input = new FileInputStream(file);
                InputStreamReader reader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(reader);
                while ((line = bufferedReader.readLine()) != null) {
                    String split[] = line.split("#");
                    String fileName = split[0].trim();
                    String directory = split[1].trim();
                    if(directory.trim().equalsIgnoreCase("schema/")) update(contextPath+directory+fileName);
                }
                input.close();
                reader.close();             
                bufferedReader.close();
            }
            else {
                System.out.println("System properties file not found .....");              
            }
        } 
        catch (Exception exception) {
            System.out.println("OI exception ........" + exception.getMessage());
            throw new RuntimeException(exception);
        } 
    }
    
    private static void update(String fileName) {
        InputStream in  = null;
        InputStreamReader reader  = null;
        BufferedReader br  = null;
        File file  = null;
        try {
            file = new File(fileName);
            if (file.exists()) {
                System.out.println("Database update file found .....");              
                in = new FileInputStream(file);
                reader = new InputStreamReader(in);
                br = new BufferedReader(reader);

                String line = "";
                while((line = br.readLine()) != null) {
                    if(!line.contains(";")) break;
                    String query = line.substring(0, line.lastIndexOf(";"));
                    try {
                        if(!query.trim().isEmpty()) executeUpdate(query);
                    }
                    catch (Exception exception) {
                        System.out.println("Error occured while excuting database updates ........" + exception.getMessage());
                        throw new RuntimeException(exception);    
                    }
                }
                if(in != null) in.close();
                if(reader != null) reader.close();
                if(br != null) br.close();
                if (file.exists()) file.delete();                
            }
        } 
        catch (Exception exception) {
            System.out.println("OI exception ........" + exception.getMessage());
            throw new RuntimeException(exception);
        } 
    }
    
    private static void executeUpdate (String query) throws Exception{
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
            throw exception;
        }        
    }    
}
