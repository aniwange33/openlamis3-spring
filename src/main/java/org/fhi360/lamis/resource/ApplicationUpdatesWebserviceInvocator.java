/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.


package org.fhi360.lamis.resource;

*
 *
 * @author user1



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.io.FileUtils;

public class ApplicationUpdatesWebserviceInvocator {
    private static String serverUrl;
    private static String contextPath;
    private static String message;
    
    public static synchronized String invokeUpdatesService() {
        //Compare verison manifest date on the client against the manifest date on the server
        //if the server date is after the client manifest date, then ask the server to send 
        //the files in the manifest
        
        try {
            File file = new File(contextPath+"version/manifest.txt");
            if (file.exists()) {
                InputStream input = new FileInputStream(file);
                InputStreamReader reader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains("Manifest-Date")) break;
                }
                String split[] = line.split("#");
                String manifestDate = split[1];
                input.close();
                reader.close();

                String resourceUrl = "";//ServletActionContext.getServletContext().getInitParameter("serverUrl") + "resources/webservice/manifest/"+manifestDate.trim();
                URLConnection connection = getconnectUrl(resourceUrl, "text/plain"); 

                String token = "";
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    token += line;
                }
                System.out.println("Manifest..."+token);
                bufferedReader.close();
                if(!token.trim().isEmpty()) getUpdates(token);
                message = "Download Completed";
            }
        } 
        catch (Exception exception) {
            message = "Error While Uploading Data";
            exception.printStackTrace();
        }
        return message;
    }
    
    private static void getUpdates(String manifestToken) {
        //Get the update files from the server using the manifest information sent during checkUpdates() method
        String[] filenames = manifestToken.split(",");
        try {
            for (String filename : filenames) {
                ServletActionContext.getRequest().getSession().setAttribute("processingStatus", filename);
                String resourceUrl = serverUrl + "resources/webservice/updates/"+filename.trim();                
                URLConnection connection = getconnectUrl(resourceUrl, "text/plain");
                                          
                //The content of input stream (files) returned by the server should be written to disk
                filename = filename.equalsIgnoreCase("manifest.txt")? contextPath+"version/"+filename : contextPath+"version/updates/"+filename;
                InputStream stream = connection.getInputStream();                
                FileUtils.copyInputStreamToFile(stream, new File(filename));
                stream.close();
                System.out.println(filename+ " update downloaded...");
            }
            //After receiving updates files from server, lock the version folder until the current updates 
            //is applied by the LAMIS tomcat launcher when restarted  
            lockVersionFolder();
            ServletActionContext.getRequest().getSession().setAttribute("processingStatus", "completed");
        } 
        catch (Exception exception) {
            ServletActionContext.getRequest().getSession().setAttribute("processingStatus", "terminated");
            throw new RuntimeException(exception); 
        }       
    }

    private static void lockVersionFolder() {
        try {
            File file = new File(contextPath+"version/lock.ser");
            FileOutputStream stream = new FileOutputStream(file);
            stream.close();
        }
        catch (Exception exception) {
            throw new RuntimeException(exception); 
        }                       
    }
    
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
*/
