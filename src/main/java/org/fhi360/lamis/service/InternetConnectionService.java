/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author user1
 */
@Component
public class InternetConnectionService implements ServletContextAware {
    //Implementing singleton using the classic design pattern
    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    private static InternetConnectionService INSTANCE = null;

    protected InternetConnectionService() {
    }

    ;

    public static InternetConnectionService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InternetConnectionService();
        }
        return INSTANCE;
    }

    public String getConnectionStatus() {
        try {
            String serverUrl =servletContext.getInitParameter("serverUrl");
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == 200) {
                connection.disconnect();
                return "1";
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "0";
    }


}
//String host = url.getHost();
