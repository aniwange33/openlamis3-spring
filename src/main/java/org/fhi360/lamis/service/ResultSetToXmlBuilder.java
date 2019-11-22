/**
 * @author user1
 */
package org.fhi360.lamis.service;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.fhi360.lamis.utility.StringUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Base64;
import java.util.Map;
@Component
public class ResultSetToXmlBuilder {

    public ResultSetToXmlBuilder() {
    }

    public void build(ResultSet resultSet, String table, String directory) {
        String fileName = directory + table + ".xml";
        try {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement(StringUtil.pluralize(table));
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Element row = root.addElement(table);
                for (int i = 1; i <= colCount; i++) {
                    String columnName = metaData.getColumnName(i).toLowerCase();
                    Object value = resultSet.getObject(i) == null ? "" : resultSet.getObject(i);
                    Element column = row.addElement(columnName);
                    if (columnName.equals("template")) {
                        if (value != null) {
                            try {
                                String stringVal = Base64.getEncoder().encodeToString((byte[]) value);
                                column.setText(stringVal);
                            } catch (ClassCastException ignore) {
                            }
                        }
                    } else {
                        column.setText(value.toString());
                    }
                }
            }
            resultSet = null;
            writeXmlToFile(document, fileName);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public void build(ResultSet resultSet, String table, String directory, Map<Long, String> patientEntityMap) {
        String fileName = directory + table + ".xml";
        try {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement(StringUtil.pluralize(table));
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colCount = metaData.getColumnCount();
            while (resultSet.next()) {
                if (patientEntityMap.get(resultSet.getLong("patient_id")) != null) {
                    long id = 0;
                    Element row = root.addElement(table);
                    for (int i = 1; i <= colCount; i++) {
                        String columnName = metaData.getColumnName(i).toLowerCase();
                        Object value = resultSet.getObject(i) == null ? "" : resultSet.getObject(i);

                        if (columnName.equals("patient_id")) {
                            try {
                                id = (Long) value;
                            } catch (ClassCastException ignored) {
                            }
                        }
                        Element column = row.addElement(columnName);
                        if (columnName.equals("template")) {
                            if (value != null) {
                                try {
                                    String stringVal = Base64.getEncoder().encodeToString((byte[]) value);
                                    column.setText(stringVal);
                                } catch (ClassCastException ignore) {
                                }
                            }
                        } else {
                            column.setText(value.toString());
                        }
                    }
                    System.out.println("patient.: " + id + "... table.." + table);
                    Element column = row.addElement("hospital_num");
                    column.setText(patientEntityMap.get(id));     // column.setText(entityIdentifier.getHospitalNum(id));
                }
            }
            resultSet = null;
            writeXmlToFile(document, fileName);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public void buildChild(ResultSet resultSet, String table, String directory, Map<Long, String> patientEntityMap) {
        String fileName = directory + table + ".xml";
        try {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement(StringUtil.pluralize(table));
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colCount = metaData.getColumnCount();
            while (resultSet.next()) {
                if (patientEntityMap.get(resultSet.getLong("patient_id")) != null) {
                    long id = 0;
                    Element row = root.addElement(table);
                    for (int i = 1; i <= colCount; i++) {
                        String columnName = metaData.getColumnName(i).toLowerCase();
                        Object value = resultSet.getObject(i) == null ? "" : resultSet.getObject(i);

                        if (columnName.equals("patient_id")) {
                            id = (Long) value;
                        }
                        Element column = row.addElement(columnName);
                        column.setText(value.toString());
                    }
                    Element column = row.addElement("hospital_num_mother");
                    column.setText(patientEntityMap.get(id));     // column.setText(entityIdentifier.getHospitalNum(id));
                }
            }
            resultSet = null;
            writeXmlToFile(document, fileName);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public void buildDeliveryOrMaternalFollowup(ResultSet resultSet, String table, String directory, Map<Long, String> patientEntityMap, Map<Long, String> ancEntityMap) {
        String fileName = directory + table + ".xml";
        try {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement(StringUtil.pluralize(table));
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colCount = metaData.getColumnCount();
            while (resultSet.next()) {
                if (patientEntityMap.get(resultSet.getLong("patient_id")) != null) {
                    long id = 0;
                    long ancId = 0;
                    Element row = root.addElement(table);
                    for (int i = 1; i <= colCount; i++) {
                        String columnName = metaData.getColumnName(i).toLowerCase();
                        Object value = resultSet.getObject(i) == null ? "" : resultSet.getObject(i);

                        if (columnName.equals("patient_id")) {
                            id = (Long) value;
                        }
                        if (columnName.equals("anc_id")) {
                            ancId = (Long) value;
                        }
                        Element column = row.addElement(columnName);
                        column.setText(value.toString());
                    }
                    Element column = row.addElement("hospital_num");
                    column.setText(patientEntityMap.get(id));     // column.setText(entityIdentifier.getHospitalNum(id));
                    column = row.addElement("anc_num");
                    column.setText((ancEntityMap.get(ancId) == null) ? "" : ancEntityMap.get(ancId));
                }
            }
            resultSet = null;
            writeXmlToFile(document, fileName);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public void buildChildFollowup(ResultSet resultSet, String table, String directory, Map<Long, String> childEntityMap) {
        String fileName = directory + table + ".xml";
        try {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement(StringUtil.pluralize(table));
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colCount = metaData.getColumnCount();
            while (resultSet.next()) {
                long id = 0;
                Element row = root.addElement(table);
                for (int i = 1; i <= colCount; i++) {
                    String columnName = metaData.getColumnName(i).toLowerCase();
                    Object value = resultSet.getObject(i) == null ? "" : resultSet.getObject(i);

                    if (columnName.equals("child_id")) {
                        id = (Long) value;
                    }
                    Element column = row.addElement(columnName);
                    column.setText(value.toString());
                }
                Element column = row.addElement("reference_num");
                column.setText(childEntityMap.get(id) == null ? "" : childEntityMap.get(id));     // column.setText(entityIdentifier.getHospitalNum(id));
            }
            resultSet = null;
            writeXmlToFile(document, fileName);
        } catch (Exception exception) {
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
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

    }
}
