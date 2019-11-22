/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.resource;

import java.io.File;
import java.io.InputStream;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.io.FileUtils;
import org.fhi360.lamis.utility.StringUtil;
import org.fhi360.lamis.service.XmlParserDelegator;
import org.fhi360.lamis.utility.FileUtil;

/**
 *
 * @author user1
 */
public class WebserviceResponseHandler {

   /* public static synchronized void writeXmlToFile(String content, String table) {
        try {
            String contextPath = ServletActionContext.getServletContext().getInitParameter("contextPath");
            String fileName = contextPath + "exchange/sync/" + table + ".xml";
            FileUtils.writeStringToFile(new File(fileName), content);
            content = null;
        } catch (Exception exception) {
            content = null;
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }

    public static synchronized void writeJsonToFile(String content, String table) {
        XMLSerializer serializer = new XMLSerializer();
        serializer.setRootName(StringUtil.pluralize(table));
        serializer.setTypeHintsEnabled(false);
        serializer.setElementName(table);

        try {
            String contextPath = ServletActionContext.getServletContext().getInitParameter("contextPath");
            String fileName = contextPath + "exchange/sync/" + table + ".xml";
            FileUtils.writeStringToFile(new File(fileName), serializer.write(JSONSerializer.toJSON(content)));
            content = null;
            serializer = null;
        } catch (Exception exception) {
            content = null;
            serializer = null;
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }

    public static synchronized void writeStreamToFile(InputStream stream, String table, String formart) {
        try {
            String contextPath = ServletActionContext.getServletContext().getInitParameter("contextPath");
            
            String fileName = contextPath + "exchange/sync/" + table + "."+formart;
            FileUtils.copyInputStreamToFile(stream, new File(fileName));

            //The content of the stream is deflated and therefore has to be inflated
            FileUtil fileUtil = new FileUtil();
            fileUtil.inflateFile(fileName);  //decompress the file 
            stream = null;
        } catch (Exception exception) {
            stream = null;
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }
    
    

    public static synchronized void process(String[] tables, long id, String format) {
        String contextPath = ServletActionContext.getServletContext().getInitParameter("contextPath");
        try {

            for (String table : tables) {

                String fileName = contextPath + "exchange/sync/" + table + "." + format;
                if (new File(fileName).exists()) {
                    if (format.equalsIgnoreCase("xml")) {
                        new XmlParserDelegator().delegate(table, fileName, "");
                    } 
                    else {
                        String content = new FileUtil().readStringFromFile(fileName);
                        new JsonParserDeligator().delegate(table, content);
                    }

                    new File(fileName).delete();
                    //new CleanupService().updateTimestampField(table, id);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }*/
}
