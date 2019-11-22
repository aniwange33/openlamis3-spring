/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.resource;

import java.io.File;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.Timestamp;					  
import java.util.Map;
import java.util.UUID;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.io.FileUtils;
import org.fhi360.lamis.utility.StringUtil;

import org.fhi360.lamis.service.UploadFolderService;
import org.fhi360.lamis.service.XmlParserDelegator;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.DatabaseHandler;												
import org.fhi360.lamis.utility.FileUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.PropertyAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user1
 */
public class WebserviceRequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(WebserviceRequestHandler.class);

    //This method is called from upload service in the webservice
    //It writes the content of the input stream to file, inflate the file and then process the xml into table records
    public static void uploadStream(InputStream stream, String table, long facilityId) {
        try {
            String contextPath = null;//ServletActionContext.getServletContext().getInitParameter("contextPath");
            String directory = contextPath + "exchange/sync/" + Long.toString(facilityId) + "/";
            String fileName = directory + table + ".xml";  // Write the content to facility folder
            
            File fileUploaded = new File(fileName);									   
            FileUtils.copyInputStreamToFile(stream, fileUploaded);
            //Update the upload for this facility and append the filename to the list of uploaded files, and also increase file count
            Date uploadDate = new Date(new java.util.Date().getTime());
            String tablesUploaded = DatabaseHandler.getFilesUploadedToday(facilityId, uploadDate);
            Timestamp timeStamp = new java.sql.Timestamp(new java.util.Date().getTime());
            Integer uploadCount;
            if(!tablesUploaded.isEmpty()){
                //Check if this is a new table being added and modify accordingly...
                if(!tablesUploaded.contains(table)){ //Update time
                    tablesUploaded += ","+table;
                    uploadCount = tablesUploaded.split(",").length;
                }           
                else{
                    uploadCount = tablesUploaded.split(",").length;
                }
            }else{
                tablesUploaded = table;
                uploadCount = 1;
            }
            DatabaseHandler.executeUpdate("UPDATE synchistory "
                + "SET tables_uploaded = '" + tablesUploaded +"' "
                        + "num_files_uploaded = "+uploadCount+", upload_completed = 0, upload_time_stamp = '" + timeStamp + "' WHERE facility_id = " + facilityId + " AND upload_date = '" + uploadDate + "'");
            
            stream = null;
        } catch (Exception exception) {
            stream = null;
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }

    //This method is called from sync service in the webservice. The method performs two tasks.
    //First, it writes the content of the input stream to file, inflate the file and then process the xml into table records
    //Second, it reads all the records in the corresponding table, convert records to xml file, deflate it and send as array of bytes
    public static synchronized byte[] syncStream(InputStream stream, String table, long facilityId) {
        try {
            String contextPath = null;//ServletActionContext.getServletContext().getInitParameter("contextPath");
            String fileName = contextPath + "exchange/sync/" + table + ".xml";
            FileUtils.copyInputStreamToFile(stream, new File(fileName));
           //  return new ResultSetSerializer().serializeAll(table, "xml", id);  // return the records from the table
        } catch (Exception exception) {
            stream = null;
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
        return  null;
    }

    //This method is invoked by the upload service when an xml string is recieved instead of an input stream
    public static void uploadXml(String content, String table, long facilityId) {
        initTableUUID(table);
        try {
            String contextPath =null;// ServletActionContext.getServletContext().getInitParameter("contextPath");
            String directory = contextPath + "exchange/sync/" + Long.toString(facilityId) + "/";
            String fileName = directory + table + ".xml";  // Write the content to facility folder
            FileUtils.writeStringToFile(new File(fileName), content);
            content = null;
        } catch (Exception exception) {
            content = null;
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }

    //This method is invokes by the sync service when an xml string is recieved instead of an input stream
    //The file deflater is not called here, because string are not delated
    public static synchronized byte[] syncXml(String content, String table, long facilityId) {
        try {
            String contextPath =null;// ServletActionContext.getServletContext().getInitParameter("contextPath");
            String fileName = contextPath + "exchange/sync/" + table + ".xml";
            FileUtils.writeStringToFile(new File(fileName), content);
            content = null;
            //new XmlParserDelegator().delegate(table, fileName,0.0);  // process the xml file that has been written to file
           // return new ResultSetSerializer().serializeAll(table, "xml", id);  // return the records from the table
        } catch (Exception exception) {
            content = null;
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
        return  null;
    }

    //This method is invoked by the upload service when a json string is recieved instead of an input stream
    public static void uploadJson(String content, String table, long facilityId) {
        XMLSerializer serializer = new XMLSerializer();
        serializer.setRootName(StringUtil.pluralize(table));
        serializer.setTypeHintsEnabled(false);
        serializer.setElementName(table);

        try {
            String contextPath = null;//ServletActionContext.getServletContext().getInitParameter("contextPath");
            String fileName = contextPath + "exchange/sync/" + Long.toString(facilityId) + "/" + table + ".xml"; // Write the content to facility folder
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

    //This method is invoked by the sync service when a json string is recieved instead of an input stream
    public static synchronized byte[] syncJson(String content, String table, long facilityId) {
        XMLSerializer serializer = new XMLSerializer();
        serializer.setRootName(StringUtil.pluralize(table));
        serializer.setTypeHintsEnabled(false);
        serializer.setElementName(table);

        try {
            String contextPath = null;//ServletActionContext.getServletContext().getInitParameter("contextPath");
            String fileName = contextPath + "exchange/sync/" + table + ".xml";
            FileUtils.writeStringToFile(new File(fileName), serializer.write(JSONSerializer.toJSON(content)));
            content = null;
            serializer = null;
            //new XmlParserDelegator().delegate(table, fileName,"");  // process the xml file that has been written to file
           // return new ResultSetSerializer().serializeAll(table, "xml", id);  // return the records from the table
        } catch (Exception exception) {
            content = null;
            serializer = null;
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
        return null;
    }

    
    public static synchronized byte[] getXmlMobile(String table, long facilityId) {
        try {
           // return new ResultSetSerializer().serializeMobile(table, "xml", id);  // return the records from the table
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
        return  null;
    }
     public static synchronized byte[] getJsonMobile(String table, long facilityId) {
        try {
          //  return new ResultSetSerializer().serializeMobile(table, "json", id);  // return the records from the table
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
        return null;
    }
    
    public static synchronized byte[] getXml(String table) {
        try {
        //     return new ResultSetSerializer().serialize(table, "xml");
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
        return null;
    }

    public static synchronized byte[] getJson(String table) {
        try {
          //  return new ResultSetSerializer().serialize(table, "json");
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
        return null;
    }
    
     public static synchronized byte[] getDhisXml(String table, String period) {
        try {
          //  return new ResultSetSerializer().getDhisXml(table, period);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
        return null;
    }
   
    public static String getUploadFolderStatus(long facilityId) {
        return new UploadFolderService().getUploadFolderStatus(facilityId);
    }

    public static void lockUploadFolder(long facilityId) {
        new UploadFolderService().lockUploadFolder(facilityId);

        //Update the Sync Report...
        Date uploadDate = new Date(new java.util.Date().getTime());
        Timestamp timeStamp = new java.sql.Timestamp(new java.util.Date().getTime());
        DatabaseHandler.executeUpdate("UPDATE synchistory "
                + "SET upload_completed = 1, upload_time_stamp = '" + timeStamp + "' WHERE facility_id = " + facilityId + " AND upload_date = '" + uploadDate + "'");																																					 
    }

    private static void initTableUUID(String table) {
        try {
            JDBCUtil jdbcu = new JDBCUtil();
            LOG.info("Selecting from {} table", table);
            PreparedStatement statement = jdbcu.getStatement("select count(*) from information_schema.columns "
                    + "where table_name = ? and column_name = 'id_uuid'");
            statement.setString(1, table);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int count = rs.getInt(1);
                if (count == 0) {
                    statement = jdbcu.getStatement("alter table " + table + " add id_uuid varchar(36)");
                    statement.executeUpdate();
                } else {
                    LOG.info("Updating table {}", table);
                    String idColumn = table + "_id";
                    if (table.contains("history")) {
                        idColumn = "history_id";
                    }
                    statement = jdbcu.getStatement("select " + idColumn + " as id from " + table
                            + " where id_uuid is null limit 2000");
                    rs = statement.executeQuery();

                    while (rs.next()) {
                        Long id = rs.getLong("id");
                        LOG.info("Updating {} id {}", table, id);
                        statement = jdbcu.getStatement(
                                String.format("update %s set id_uuid = '%s' where %s = %s",
                                        table, UUID.randomUUID().toString(), idColumn, id));
                        statement.executeUpdate();
                    }
                    jdbcu.getConnection().commit();
                }
            }
            jdbcu.getConnection().close();
        } catch (Exception ex) {
            LOG.error("Error: {}", ex.getMessage());
        }
    }
}
