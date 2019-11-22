/**
 *
 * @author user1
 */

package org.fhi360.lamis.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyAccessor {
    private Map<String, Object> map;
    private Properties properties; 
    private File file;
    
    public Map getJdbcProperties() {
        map = new HashMap<String, Object>();                
        properties = new Properties();
        try {
            file = new File("jdbc_setting.properties");
            if (file.exists()) {
                properties.load(new FileInputStream("jdbc_setting.properties"));
                map.put("dbUrl", properties.getProperty("dbUrl").trim());
                map.put("dbUser", properties.getProperty("dbUser").trim());
                map.put("dbPassword", properties.getProperty("dbPassword").trim());
                map.put("dbDriver", properties.getProperty("dbDriver").trim());                
            }            
        }
        catch (Exception exception) {
            exception.printStackTrace();    
        }
        return map; 
    }    

    public Map<String, Object> getSystemProperties() {
        map = new HashMap<String, Object>();                
        properties = new Properties();
        try {
            file = new File("system_setting.properties");
            if (file.exists()) {
                properties.load(new FileInputStream("system_setting.properties"));
                map.put("model", properties.getProperty("model").trim());
                map.put("manufacturer", properties.getProperty("manufacturer").trim());
                map.put("comPort", properties.getProperty("comPort").trim());
                map.put("baudRate", properties.getProperty("baudRate") == null ? null : Integer.parseInt(properties.getProperty("baudRate")));
                map.put("countryCode", properties.getProperty("countryCode").trim());
                map.put("modemStatus", properties.getProperty("modemStatus").trim());
                map.put("contextPath", properties.getProperty("contextPath").trim());
                map.put("serverUrl", properties.getProperty("serverUrl").trim());
                map.put("clientUrl", properties.getProperty("clientUrl").trim());
                map.put("tomcatPort", properties.getProperty("tomcatPort") == null ? null : Integer.parseInt(properties.getProperty("tomcatPort")));
                map.put("appInstance", properties.getProperty("appInstance").trim());
                map.put("enableSync", properties.getProperty("enableSync").trim());
                map.put("databaseName", properties.getProperty("databaseName").trim());
                map.put("userName", properties.getProperty("userName").trim());
                map.put("password", properties.getProperty("password").trim());
                map.put("api", properties.getProperty("api").trim());
                map.put("API_KEY", properties.getProperty("API_KEY").trim());
                map.put("API_SECRET", properties.getProperty("API_SECRET").trim());
                map.put("apiName", properties.getProperty("apiName").trim());
            }            
        }
        catch (Exception exception) {
            exception.printStackTrace();    
        }
        return map; 
    }    

    public void setSystemProperties(Map<String, Object> map) {
        properties = new Properties();
        try {
            properties.setProperty("model", (String) map.get("model"));
            properties.setProperty("manufacturer", (String) map.get("Manufacturer"));
            properties.setProperty("comPort", (String) map.get("comPort"));
            properties.setProperty("baudRate", Integer.toString((Integer) map.get("baudRate")));
            properties.setProperty("countryCode", (String) map.get("countryCode"));
            properties.setProperty("modemStatus", (String) map.get("modemStatus"));
            properties.setProperty("contextPath", (String) map.get("contextPath"));
            properties.setProperty("serverUrl", (String) map.get("serverUrl"));
            properties.setProperty("clientUrl", (String) map.get("clientUrl"));
            properties.setProperty("tomcatPort", Integer.toString((Integer) map.get("tomcatPort")));
            properties.setProperty("appInstance", (String) map.get("appInstance"));
            properties.setProperty("enableSync", (String) map.get("enableSync"));
            properties.setProperty("databaseName", (String) map.get("databaseName"));
            properties.setProperty("userName", (String) map.get("userName"));
            properties.setProperty("password", (String) map.get("password"));
            properties.setProperty("api", (String) map.get("api"));
            properties.setProperty("API_KEY", (String) map.get("API_KEY"));
            properties.setProperty("API_SECRET", (String) map.get("API_SECRET"));
            properties.setProperty("apiName", (String) map.get("apiName"));
            properties.store(new FileOutputStream("system_setting.properties"), null);
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }        
    }
    
    public Map<String, Object> getActivityTrackerProperties() {
        map = new HashMap<String, Object>(); 
        properties = new Properties();
        try {
            file = new File("activity_tracker.properties");
            if (file.exists()) {
                properties.load(new FileInputStream("activity_tracker.properties"));
                map.put("dateLastDqa", properties.getProperty("dateLastDqa"));
                map.put("dateLastAsyncTask", properties.getProperty("dateLastAsyncTask"));
                map.put("dateLastSms", properties.getProperty("dateLastSms"));
                map.put("appointmentMessages", properties.getProperty("appointmentMessages").trim());
                map.put("dailyMessages", properties.getProperty("dailyMessages").trim());
                map.put("specificMessages", properties.getProperty("specificMessages").trim());
            }            
        }
        catch (Exception exception) {
            exception.printStackTrace();    
        }
        return map; 
    }    
    
    public void setActivityTrackerProperties(Map<String, Object> map) {
        properties = new Properties();
        try {
            properties.setProperty("dateLastDqa", (String) map.get("dateLastDqa"));
            properties.setProperty("dateLastAsyncTask", (String) map.get("dateLastAsyncTask"));
            properties.setProperty("dateLastSms", (String) map.get("dateLastSms"));
            properties.setProperty("appointmentMessages", (String) map.get("appointmentMessages"));
            properties.setProperty("dailyMessages", (String) map.get("dailyMessages"));
            properties.setProperty("specificMessages", (String) map.get("specificMessages"));
            properties.store(new FileOutputStream("activity_tracker.properties"), null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }        
    }

    public int getDateLastDqa() {
        int status = 0;
        properties = new Properties();
        try {
            file = new File("activity_tracker.properties");
            if (!file.exists()) {
                map.put("dateLastDqa", new SimpleDateFormat("MMM yyyy").format(new Date()));
                map.put("dateLastAsyncTask", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                map.put("dateLastSms", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                map.put("appointmentMessages", "0");
                map.put("dailyMessages", "0");
                map.put("specificMessages", "0");
                setActivityTrackerProperties(map);
            }
            else {
                properties.load(new FileInputStream("activity_tracker.properties"));
                if (properties.getProperty("dateLastDqa").equals(new SimpleDateFormat("MMM yyyy").format(new Date()))) {
                     status = 1;                    
                }
                else {
                     properties.setProperty("dateLastDqa", new SimpleDateFormat("MMM yyyy").format(new Date()));
                     properties.store(new FileOutputStream("activity_tracker.properties"), null);
                     status = 0;                    
                }
            }
           
        } catch (Exception ex) {
        }
        return status;
    }
    
    public void setDateLastDqa(Date date) {
        properties = new Properties();
        try {
            file = new File("activity_tracker.properties");
            if (file.exists()) { 
                properties.load(new FileInputStream("activity_tracker.properties"));
                properties.setProperty("dateLastDqa", new SimpleDateFormat("MMM yyyy").format(date));
                properties.store(new FileOutputStream("activity_tracker.properties"), null);
            }
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }                
    }

    public int getDateLastAsyncTask() {
        int status = 0;
        properties = new Properties();
        try {
            file = new File("activity_tracker.properties");
            if (!file.exists()) {
                map.put("dateLastDqa", new SimpleDateFormat("MMM yyyy").format(new Date()));
                map.put("dateLastAsyncTask", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                map.put("dateLastSms", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                map.put("appointmentMessages", "0");
                map.put("dailyMessages", "0");
                map.put("specificMessages", "0");
                setActivityTrackerProperties(map);
            }
            else {
                properties.load(new FileInputStream("activity_tracker.properties"));
                if (properties.getProperty("dateLastAsyncTask").equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {
                     status = 1;                    
                }
                else {
                     properties.setProperty("dateLastAsyncTask", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                     properties.store(new FileOutputStream("activity_tracker.properties"), null);
                     status = 0;                    
                }
            }
           
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return status;
    }
    
    public void setDateLastAsyncTask(Date date) {
        properties = new Properties();
        try {
            file = new File("activity_tracker.properties");
            if (file.exists()) { 
                properties.load(new FileInputStream("activity_tracker.properties"));
                properties.setProperty("dateLastAsyncTask", new SimpleDateFormat("yyyy-MM-dd").format(date));
                properties.store(new FileOutputStream("activity_tracker.properties"), null);
            }
        } 
        catch (Exception ex) {
             ex.printStackTrace();
        }                
    }

    public String getDatabaseName() {
        String databaseName = "h2";
        properties = new Properties();
        try {
            file = new File("system_setting.properties");
            if (file.exists()) {
                properties.load(new FileInputStream("system_setting.properties"));
                databaseName = (String) properties.getProperty("databaseName").trim();
            }          
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return databaseName;
    }
}




//    public int getDqalogProperties() {
//        int status = 0;
//        formatter = new SimpleDateFormat("MMM yyyy");
//        String key = formatter.format(new Date());
//        try {
//            properties = new Properties();
//            file = new File("dqalog.properties");
//            if (!file.exists()) {
//                properties.setProperty(key, "0");
//                properties.store(new FileOutputStream("dqalog.properties"), null);
//            }
//            else {
//                // the dqalog properties file already exists
//                properties.load(new FileInputStream("dqalog.properties"));
//                if (properties.getProperty(key) == null) {
//                     properties.setProperty(key, "0");
//                     properties.store(new FileOutputStream("dqalog.properties"), null);
//                }
//                status = Integer.parseInt(properties.getProperty(key));
//            }
//           
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return status;
//    }
//    
//    public void setDqalogProperties(Date date) {
//        String key = formatter.format(date);
//        try {
//            properties.load(new FileInputStream("dqalog.properties"));
//            properties.setProperty(key, "1");
//            properties.store(new FileOutputStream("dqalog.properties"), null);
//         } catch (Exception ex) {
//            ex.printStackTrace();
//        }    
//    }
