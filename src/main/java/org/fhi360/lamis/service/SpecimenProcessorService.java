/*
package org.fhi360.lamis.service;

import org.fhi360.lamis.service.sms.ModemGatewayService;
import org.fhi360.lamis.utility.JDBCUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.Scrambler;

public class SpecimenProcessorService {
    private static JDBCUtil jdbcUtil;
    private static PreparedStatement preparedStatement;
    private static ModemGatewayService smsGatewayService;
    
    public static synchronized void saveResult() {
        ResultSet resultSet = null;
        String query = "SELECT labno, result, date_assay FROM labresult";  //labresult is a temp table created during labfile parsing in the class LabFileParser
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String labno = resultSet.getString("labno"); //LabNumberNormalizer.normalize(resultSet.getString("labno"));
                String result = resultSet.getString("result");
                java.sql.Date dateAssay = resultSet.getDate("date_assay");
                executeUpdate("UPDATE specimen SET result = '" + result + "', date_assay = '" + dateAssay + "', date_reported = CURDATE() WHERE labno = '" + labno.trim().toUpperCase() + "'");
            }
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database            
        }        
    }
            
    public static synchronized void dispatchResult() {
        smsGatewayService = new ModemGatewayService();
        Scrambler scrambler = new Scrambler();        
        ResultSet resultSet = null;
        String query = "SELECT * FROM dispatcher ORDER BY name";  //dispatcher temp table is created in the class SpecimenGridManager when results are retrieved for dispatch 
        try {           
            if(!smsGatewayService.isStarted()) smsGatewayService.startSmsService(); 
            
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String hospitalNum = resultSet.getString("hospital_num");
                String surname = scrambler.unscrambleCharacters(resultSet.getString("surname"));
                String otherNames = scrambler.unscrambleCharacters(resultSet.getString("other_names"));
                String name = surname + " " + otherNames;
                String gender = resultSet.getString("gender");
                String labno = resultSet.getString("labno");
                String result = resultSet.getString("result");
                String facilityName = resultSet.getString("name");
                String phone = resultSet.getString("phone2");
                String dateAssay = (resultSet.getDate("date_assay") == null)? "" : DateUtil.parseDateToString(resultSet.getDate("date_assay"), "MM/dd/yyyy");
                String message = "81Patient ID: " + hospitalNum + ", Name: " + name + ", Gender: " + gender +", Ref No: " + labno + ", HIV-1 DNAPCR Result: " + result + ", Hospital: " + facilityName + ", Assay Date: " + dateAssay;
                
                if(phone != null || !phone.equals("")) {
                    String messageStatus = smsGatewayService.sendSms(phone, message);
                    if (messageStatus.equals("SENT")) {
                        executeUpdate("UPDATE specimen SET date_dispatched = CURDATE() WHERE labno = '" + labno + "'");
                    } 
                    else if ("FAILED".equals(messageStatus) || "UNSENT".equals(messageStatus)) {    
                        System.out.println("Result dispatched failed......");  
                    }
                }
                if(ServletActionContext.getRequest().getParameterMap().containsKey("forwardSms")) forwardSms(labno, message);                
            }
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database            
        }               
    }
    
    public static void sendSms(String phone, String message) {
        smsGatewayService = new ModemGatewayService();
        try { 
            if(!smsGatewayService.isStarted()) smsGatewayService.startSmsService(); 
            if(phone != null || !phone.equals("")) {
                smsGatewayService.sendSms(phone, message);
            }                            
        }          
        catch (Exception exception) {
            exception.printStackTrace();            
        }                
    }
    
    private static void forwardSms(String labno, String message) {
        ResultSet resultSet1 = null;
        String query = "SELECT sender_phone FROM eid WHERE labno = '" + labno + "'";
        try { 
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet1 = preparedStatement.executeQuery();
            if(resultSet1.next()) {
                String phone = resultSet1.getString("sender_phone");
                if(phone != null || !phone.equals("")) {
                    smsGatewayService.sendSms(phone, message);
                }                            
            }              
        }          
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database            
        }        
        
    }
        
    private static void executeUpdate(String query) {
        try {
            jdbcUtil = new JDBCUtil();            
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }        
    }            
        
}

*/
