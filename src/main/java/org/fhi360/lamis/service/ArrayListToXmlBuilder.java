/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service;

/**
 *
 * @author user1
 */

import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import org.dom4j.io.OutputFormat;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.utility.FileUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ArrayListToXmlBuilder {

private  final ApplicationProperties applicationProperties;

    public ArrayListToXmlBuilder(ApplicationProperties applicationProperties) {

        this.applicationProperties = applicationProperties;
    }

    public void build(ArrayList<Map<String, String>> list, String table) {
        String contextPath = applicationProperties.getContextPath();
        String fileName = contextPath + "transfer/nigqual/" + table + ".xml";
        try {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("data-set");
            for(Map<String, String> map: list) {
                Element row = root.addElement(table);
                for(Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    Element column = row.addElement(key);
                    column.setText(value);
                }
            }
            writeXmlToFile(document, fileName);            
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        //Copy files to secondary locations
        FileUtil fileUtil = new FileUtil();
        fileUtil.makeDir(contextPath+"transfer/nigqual/");
        fileUtil.makeDir(applicationProperties.getContextPath()+"/transfer/nigqual/");
        
        try {
            fileName = table + ".xml";
            //for servlets in the default(root) context, copy file to the transfer folder in root 
            if(!contextPath.equalsIgnoreCase(applicationProperties.getContextPath())) fileUtil.copyFile(fileName, contextPath + "transfer/nigqual/", applicationProperties.getContextPath() + "/transfer/nigqual/");
        } 
        catch (Exception exception) {
            //session.setAttribute("processingStatus", "terminated");
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    
    }    

    private void writeXmlToFile(Document document, String fileName) {
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(fileName));
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(outputStream, format);
            writer.write(document);
            writer.flush();

            writer.close();
            outputStream.close();
            document = null;
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }  
        
    }    
}
