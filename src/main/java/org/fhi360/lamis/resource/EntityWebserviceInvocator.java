/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.resource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Alozie
 */
public class EntityWebserviceInvocator {
    private static String message;

    private static int BUFFER_SIZE = 32769;
            
    //This method is called by the Facility Action class to save a new facility or modify an existing before saving on the server 
   /* public static synchronized Long saveOrModifyEntity(String jsonString, boolean update) {
        ServletActionContext.getRequest().getSession().setAttribute("processingStatus", "processing");;
        Long generatedID = 0L;    
        try {
            String resourceUrl = ServletActionContext.getServletContext().getInitParameter("serverUrl") + "resources/webservice/entity";
            if(update) resourceUrl = resourceUrl + "/update";
            URLConnection connection = getconnectUrl(resourceUrl, "application/json");
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()), BUFFER_SIZE);
            out.write(jsonString);
            out.close();
            jsonString = null;

            String line = "";
            String content = "";
            BufferedReader in = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            while ((line = in.readLine()) != null) {
                content += line;
            }
            in.close();
            if(!content.isEmpty()) generatedID = Long.parseLong(content);
            content = null;
        } 
        catch (Exception exception) {
           // ServletActionContext.getRequest().getSession().setAttribute("processingStatus", "terminated");;
            exception.printStackTrace();
        }
        //ServletActionContext.getRequest().getSession().setAttribute("processingStatus", "completed");
        return generatedID;
    }
    */
    private static URLConnection getconnectUrl(String resourceUrl, String contentType) {
        URLConnection connection = null;
        try {
            URL url = new URL(resourceUrl);
            connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setConnectTimeout(120000);
            connection.setReadTimeout(0);
        } 
        catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
        return connection;
    }
}
