/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility;

/**
 *
 * @author user1
 */

import java.io.*;
import java.util.Map;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ObjectSerializer {
    private static final Log log = LogFactory.getLog(ObjectSerializer.class);

    private ObjectSerializer() { }
 
    /**
    * SingletonHolder is loaded on the first execution of ObjectSerializer.getInstance() 
    * or the first access to SingletonHolder.INSTANCE, not before.
    */
    private static class SingletonHolder { 
        public static final ObjectSerializer INSTANCE = new ObjectSerializer();
    }
 
    public static ObjectSerializer getInstance() {
        return SingletonHolder.INSTANCE;
    }
	
    public synchronized void serialize(Object obj, String fileName) throws IOException, ClassNotFoundException {
        System.out.println("Saving file.....");
        File file = null;
        try {
            Map<String, Object> map = new PropertyAccessor().getSystemProperties();
            String contextPath = (String) map.get("contextPath");
            String directory = contextPath + "temp/"; 
            new FileUtil().makeDir(directory);
            
            file = new File(directory + fileName + ".ser");            
            FileOutputStream stream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(stream);
            out.writeObject(obj);
            out.close();
            stream.close();
        } 
        catch (IOException exception) {
            log.info("Exception serializing object");
            throw exception;
        }	
    }
    
    public synchronized Object deserialize(String fileName) throws IOException, ClassNotFoundException {
        Object obj = null;
        File file = null;
        try {
            Map<String, Object> map = new PropertyAccessor().getSystemProperties();
            String contextPath = (String) map.get("contextPath");
            String directory = contextPath + "temp/"; 
            
            file = new File(directory + fileName + ".ser");
            if (file.exists()) {
                FileInputStream stream = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(stream);
                obj =  in.readObject();
                in.close();
                stream.close();
            }
        } 
        catch (IOException exception) {
            log.info("Exception deserializing object");
        } 
        catch (ClassNotFoundException exception) {
            log.info("Object class not found");
        }
        return obj;		
    }
    
 }
