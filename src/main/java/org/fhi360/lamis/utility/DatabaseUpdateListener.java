/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility;

import java.sql.SQLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author user1
 */
public class DatabaseUpdateListener implements ServletContextListener{

    public void contextInitialized(ServletContextEvent sce) {
        DatabaseUpdater.update();
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
    
}
