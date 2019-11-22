/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.resource;

import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.JDBCUtil;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

/**
 *
 * @author Alozie
 */
public class UploadWebserviceInvocator {

    private static JDBCUtil jdbcUtil;
    private static PreparedStatement preparedStatement;
    private static String message;

    private static int BUFFER_SIZE = 32769;
    private static boolean uploadAll = false;

    /*public static synchronized String invokeUploadService() {
        //Before uploading data to server, check if the facility upload folder is unlocked, i.e no previous xml files is still pending        
        //This is achieved by invoking the webservice GET method with facility id parameter
        //this then calls the getUploadFolderStatus in the WebserviceRequestHandler class
        long id = (Long) ServletActionContext.getRequest().getSession().getAttribute("id");
//        if (ServletActionContext.getRequest().getParameter("recordsAll") != null && !ServletActionContext.getRequest().getParameter("recordsAll").equals("false")) {
//            uploadAll = true;
//        }


        try {
            String line = "";
            String content = "";

            //Establish connection to the server and invoke appropriate method on the webservice
            //Get an input stream from connection and read content from the input stream
            String resourceUrl = ServletActionContext.getServletContext().getInitParameter("serverUrl") + "resources/webservice/uploadfolder/" + id + "/1";

            URLConnection connection = getconnectUrl(resourceUrl, "text/plain");
            BufferedReader input = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            while ((line = input.readLine()) != null) {
                content += line;
            }
            input.close();
            //Start upload if upload folder on the server is unlocked
            //and Lock upload folder after upload of all xml files
            if (content.equalsIgnoreCase("unlocked")) {
                Timestamp timestamp = new java.sql.Timestamp(new java.util.Date().getTime());
                upload();
                lockUploadFolder(id);
                updateLastUploadTime(timestamp, id);
                message = "Upload Completed";
            } else {
                message = "Your last upload is still in the server upload folder, please try again later";
            }
        } catch (Exception exception) {
            message = "Error While Uploading Data";
            exception.printStackTrace();
        }
        return message;
    }

    private static void upload() {
        //Upload each transaction table to the server by first converting data to xml and invoking the webservice POST method with table name and facility Id parameters
        //The xml file is the playload, table name and facility id are parameters
        long id = (Long) ServletActionContext.getRequest().getSession().getAttribute("id");
        String[] tables = Constants.Tables.TRANSACTION_TABLES.split(",");
        try {
            for (String table : tables) {
                ServletActionContext.getRequest().getSession().setAttribute("processingStatus", table);

                //Establish connection to the server and invoke appropriate method on the webservice
                //Get an output stream from connection and write content to the output stream
                String resourceUrl = ServletActionContext.getServletContext().getInitParameter("serverUrl") + "resources/webservice/upload/" + table.trim() + "/" + id;
                URLConnection connection = getconnectUrl(resourceUrl, "application/xml");
                BufferedOutputStream output = new BufferedOutputStream(connection.getOutputStream());

                //Select records from table, convert resultset to xml and return as array of bytes
                //then read and write until last byte is encountered
                byte[] buffer = (uploadAll) ? new ResultSetSerializer().serializeAll(table, "xml", id) : new ResultSetSerializer().serialize(table, "xml", id);
                for (int i = 0; i < buffer.length; i++) {
                    byte oneByte = buffer[i];
                    output.write(oneByte);
                }
                output.close();

//                //Select records from table, convert resultset to xml and return as string
//                URLConnection connection = getconnectUrl(resourceUrl, "application/xml");               
//                String content = new ResultSetSerializer().serialize(table, "xml", id);
//                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()), BUFFER_SIZE);                
//                output.write(content); 
//                output.close();
//                content = null;  
                String line = "";
                BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = input.readLine()) != null) {
                }
                input.close();
            }
        } catch (Exception exception) {
            ServletActionContext.getRequest().getSession().setAttribute("processingStatus", "terminated");;
            message = "Error While Uploading Data";
            throw new RuntimeException(exception);
        }
        ServletActionContext.getRequest().getSession().setAttribute("processingStatus", "completed");
    }

    private static void lockUploadFolder(long id) {
        try {
            String resourceUrl = ServletActionContext.getServletContext().getInitParameter("serverUrl") + "resources/webservice/uploadfolder/" + id + "/2";        //ServletActionContext.getServletContext().getInitParameter("serverUrl")
            URLConnection connection = getconnectUrl(resourceUrl, "text/plain");

            String line = "";
            BufferedReader input = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            while ((line = input.readLine()) != null) {
            }
            input.close();
        } catch (Exception exception) {
            message = "Error While Uploading Data";
            exception.printStackTrace();
        }
    }

    private static void updateLastUploadTime(Timestamp timestamp, long id) {
        String[] tables = Constants.Tables.TRANSACTION_TABLES.split(",");
        for (String table : tables) {
            if (uploadAll) {
                executeUpdate("UPDATE " + table + " SET uploaded = 1, time_uploaded = '" + timestamp + "' WHERE facility_id = " + id);
            } else {
                executeUpdate("UPDATE " + table + " SET uploaded = 1, time_uploaded = '" + timestamp + "' WHERE facility_id = " + id + " AND uploaded != 1");
            }
            executeUpdate("UPDATE exchange SET " + table + " = '" + timestamp + "' WHERE facility_id = " + id);
        }
    }

    private static void executeUpdate(String query) {
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private static URLConnection getconnectUrl(String resourceUrl, String contentType) {
        URLConnection connection = null;
        try {
            URL url = new URL(resourceUrl);
            connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setConnectTimeout(120000);  //set socket connection timeout to 60 sec  change to connection.setConnectTimeout(new Integer(1000 * 120)
            connection.setReadTimeout(0);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
        return connection;
    }*/
}
//This code to write json to an output stream
//String string = "[{id:76}]";
//JSONArray jsonArray = new JSONArray(string);  //JSONObject jsonObject = new JSONObject(string);
//OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
//out.write(jsonString);  //out.write(jsonArray.toString());
//out.close();
