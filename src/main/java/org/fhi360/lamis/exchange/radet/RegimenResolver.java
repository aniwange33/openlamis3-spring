/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.radet;

import au.com.bytecode.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.utility.JDBCUtil;

public class RegimenResolver {
    private String query; 
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private final ApplicationProperties applicationProperties = ContextProvider.getBean(ApplicationProperties.class);
    public RegimenResolver() {
        try {
            jdbcUtil = new JDBCUtil(); 
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }                
    }
    
    public void initialize() {
        try {            
            executeUpdate("DROP VIEW IF EXISTS resolver");        
            executeUpdate(" CREATE VIEW resolver (regimensys VARCHAR(100), regimen VARCHAR(100))"); 

            String fileName = applicationProperties.getContextPath()+"regimen.csv";
            if(new File(fileName).exists()) {
                excelFile(fileName);
            }
            else {
                query = "INSERT INTO resolver (regimensys, regimen) SELECT description, description FROM regimen WHERE regimentype_id IN(1, 2, 3, 4, 14)";  
                executeUpdate(query);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }          
        finally {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        } 
        
    }
    
    private void excelFile(String fileName) {
        String[] row = null;
        try {            
            CSVReader csvReader = new CSVReader(new FileReader(fileName));
            while((row = csvReader.readNext()) != null) {
                String regimensys =  row[0];
                String regimen =  row[1];
                query = "INSERT INTO resolver (regimensys, regimen) VALUES('" + regimensys + "', '" + regimen + "')";  
                executeUpdate(query);
            }
            csvReader.close();        
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void textFile(String fileName) {
        String content = "";
        String[] row = null;
        try {            
            File radet = new File(fileName);
            InputStream inputStream = new FileInputStream(radet);
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            
            while ((content = bufferedReader.readLine()) != null) {
                row = content.split(",");
                String regimensys =  row[0];
                String regimen =  row[1];
                query = "INSERT INTO resolver (regimensys, regimen) VALUES('" + regimensys + "', '" + regimen + "')";  
                executeUpdate(query);
            }
            bufferedReader.close();
            reader.close();
            inputStream.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    
    public String getRegimen1(String regimensys) {       
        String regimen = "";
        query = "SELECT regimen FROM resolver WHERE regimensys = '" + regimensys + "'";
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) regimen = resultSet.getString("regimen");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }          
        finally {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        } 
        return regimen;
    }

    public String getRegimen(String regimensys) {       
        String regimen = "";
        query = "SELECT regimen FROM regimenresolver WHERE regimensys = '" + regimensys + "'";
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) regimen = rs.getString("regimen");
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return regimen;
    }
    
    private void executeUpdate(String query) {
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }        
    }
            
}
