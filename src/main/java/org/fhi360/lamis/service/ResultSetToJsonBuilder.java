/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author user1
 */
package org.fhi360.lamis.service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Map;
import org.fhi360.lamis.model.Hts;
import org.fhi360.lamis.utility.StringUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class ResultSetToJsonBuilder {

    public ResultSetToJsonBuilder() {
    }

    public byte[] build(ResultSet resultSet, String table) {
        JSONArray jsonArray = new JSONArray();
        byte[] bytes = null;
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colCount = metaData.getColumnCount();
            while (resultSet.next()) {
                JSONObject record = new JSONObject();
                for (int i = 1; i <= colCount; i++) {
                    String columnTypeName = metaData.getColumnTypeName(i);
                    String columnName = metaData.getColumnName(i);
                    System.out.println("COLUMN "+columnTypeName +" NAME "+columnName);
                  
                    Object value = resultSet.getObject(i) == null ? "" : resultSet.getObject(i);
                    
                    columnName = StringUtil.toCamelCase(columnName);
                    record.put(columnName, value.toString());
                }
                jsonArray.put(record);
            }
            JSONObject obj = new JSONObject();
            obj.put(table, jsonArray);
            String json = obj.toString();

            bytes = new byte[json.length()];
            bytes = json.getBytes();

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return bytes;
    }

    public byte[] build(ResultSet resultSet, String table, Map<Long, String> patientEntityMap) {
        JSONArray jsonArray = new JSONArray();
        byte[] bytes = null;
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colCount = metaData.getColumnCount();
            while (resultSet.next()) {
                if (patientEntityMap.get(resultSet.getLong("patient_id")) != null) {
                    long id = 0;
                    JSONObject record = new JSONObject();
                    for (int i = 1; i <= colCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        columnName = StringUtil.toCamelCase(columnName);
                        Object value = resultSet.getObject(i) == null ? "" : resultSet.getObject(i);
                        if (columnName.equals("patient_id")) {
                            id = (Long) value;
                        }
                        record.put(columnName, value.toString());
                    }
                    record.put("hospital_num", patientEntityMap.get(id));
                    jsonArray.put(record);
                }
            }
            JSONObject obj = new JSONObject();
            obj.put(table, jsonArray);
            String json = obj.toString();
            bytes = new byte[json.length()];
            bytes = json.getBytes();

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return bytes;
    }

    public byte[] buildChild(ResultSet resultSet, String table, Map<Long, String> patientEntityMap) {
        JSONArray jsonArray = new JSONArray();
        byte[] bytes = null;
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colCount = metaData.getColumnCount();
            while (resultSet.next()) {
                if (patientEntityMap.get(resultSet.getLong("patient_id")) != null) {
                    long id = 0;
                    JSONObject record = new JSONObject();
                    for (int i = 1; i <= colCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        columnName = StringUtil.toCamelCase(columnName);
                        Object value = resultSet.getObject(i) == null ? "" : resultSet.getObject(i);
                        if (columnName.equals("patient_id_mother")) {
                            id = (Long) value;
                        }
                        record.put(columnName, value.toString());
                    }
                    record.put("hospital_num_mother", patientEntityMap.get(id));
                    jsonArray.put(record);
                }
            }
            JSONObject obj = new JSONObject();
            obj.put(table, jsonArray);
            String json = obj.toString();
            bytes = new byte[json.length()];
            bytes = json.getBytes();

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return bytes;
    }

    public byte[] buildDeliveryOrMaternalFollowup(ResultSet resultSet, String table, Map<Long, String> patientEntityMap, Map<Long, String> ancEntityMap) {
        JSONArray jsonArray = new JSONArray();
        byte[] bytes = null;
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colCount = metaData.getColumnCount();
            while (resultSet.next()) {
                if (patientEntityMap.get(resultSet.getLong("patient_id")) != null) {
                    long id = 0;
                    long ancId = 0;
                    JSONObject record = new JSONObject();
                    for (int i = 1; i <= colCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        columnName = StringUtil.toCamelCase(columnName);
                        Object value = resultSet.getObject(i) == null ? "" : resultSet.getObject(i);
                        if (columnName.equals("patient_id")) {
                            id = (Long) value;
                        }
                        if (columnName.equals("anc_id")) {
                            ancId = (Long) value;
                        }
                        record.put(columnName, value.toString());
                    }
                    record.put("hospital_num", patientEntityMap.get(id));
                    record.put("anc_num", ancEntityMap.get(ancId));
                    jsonArray.put(record);
                }
            }
            JSONObject obj = new JSONObject();
            obj.put(table, jsonArray);
            String json = obj.toString();
            bytes = new byte[json.length()];
            bytes = json.getBytes();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return bytes;
    }

    public byte[] buildChildFollowup(ResultSet resultSet, String table, Map<Long, String> childEntityMap) {
        JSONArray jsonArray = new JSONArray();
        byte[] bytes = null;
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colCount = metaData.getColumnCount();
            while (resultSet.next()) {
                long id = 0;
                JSONObject record = new JSONObject();
                for (int i = 1; i <= colCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    columnName = StringUtil.toCamelCase(columnName);
                    Object value = resultSet.getObject(i) == null ? "" : resultSet.getObject(i);
                    if (columnName.equals("child_id")) {
                        id = (Long) value;
                    }
                    record.put(columnName, value.toString());
                }
                record.put("reference_num", childEntityMap.get(id) == null ? "" : childEntityMap.get(id));
                jsonArray.put(record);
            }
            JSONObject obj = new JSONObject();
            obj.put(table, jsonArray);
            String json = obj.toString();
            bytes = new byte[json.length()];
            bytes = json.getBytes();

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return bytes;
    }

}
