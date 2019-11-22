/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.resource;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author user10
 */
public class HttpServiceInvocator {
    private final String USER_AGENT = "Mozilla/5.0";
    
    public String sendGet(String url) {
        HttpURLConnection urlConnection = null;
        StringBuilder response = new StringBuilder();
        try {
            //String url = "http://xxx.xxx.xxx.xxx/?user=fhi360&password=fhi360&from=shortcode&to="+phone+"&message="+message;
            URL obj = new URL(url);
            urlConnection = (HttpURLConnection) obj.openConnection();

            //Set request header
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent", USER_AGENT);

            //Read the response
            int statusCode = urlConnection.getResponseCode();
            if(statusCode == 200) {
                String line = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                if(reader != null) reader.close();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return response.toString();
    }

    public String sendPost(String url, String parameters) {
        HttpURLConnection urlConnection = null;
        StringBuilder response = new StringBuilder();
        try {
            //String url = "http://xxx.xxx.xxx.xxx";
            //String parameters = "user=fhi360&password=fhi360&from=shortcode&to="+phone+"&message="+message;
            URL obj = new URL(url);
            urlConnection = (HttpURLConnection) obj.openConnection();

            //Set request header
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("User-Agent", USER_AGENT);
            urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            urlConnection.setDoOutput(true);
            DataOutputStream writer = new DataOutputStream(urlConnection.getOutputStream());
            writer.writeBytes(parameters);
            writer.flush();
            writer.close();

            //Read the response
            int statusCode = urlConnection.getResponseCode();
            if(statusCode == 200) {
                String line = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                if(reader != null) reader.close();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return response.toString();        
    }
}
