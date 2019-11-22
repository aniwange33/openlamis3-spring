/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service;

import org.fhi360.lamis.utility.JDBCUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.UUID;

/**
 * @author User10
 */
@Component
public class TableUUIDGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(TableUUIDGenerator.class);
    private final String[] tables = {"patient", "user", "casemanager",
            "communitypharm", "monitor", "clinic", "pharmacy", "laboratory",
            "adrhistory", "oihistory", "adherehistory", "statushistory",
            "regimenhistory", "chroniccare", "dmscreenhistory", "tbscreenhistory",
            "anc", "delivery", "child", "childfollowup", "maternalfollowup",
            "partnerinformation", "specimen", "eid", "labno", "nigqual", "devolve",
            "patientcasemanager", "eac", "motherinformation", "prescription", "mhtc"};

    public void init() {
        LOG.info("Executing....");
        try {
            JDBCUtil jdbcu = new JDBCUtil();

            for (String table : tables) {
                LOG.info("Selecting from {} table", table);
                PreparedStatement statement = jdbcu.getStatement(String.format("select * from %s limit 1", table));
                ResultSet rs = statement.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int cc = rsmd.getColumnCount();
                boolean hasUUID = false;
                for (int i = 1; i < cc + 1; i++) {
                    if (rsmd.getColumnName(i).equalsIgnoreCase("id_uuid")) {
                        hasUUID = true;
                    }
                }
                if (!hasUUID) {
                    statement = jdbcu.getStatement("alter table " + table + " add id_uuid varchar(36)");
                    statement.executeUpdate();
                } else {
                    LOG.info("Updating table {}", table);
                    String idColumn = table + "_id";
                    if (table.contains("history")) {
                        idColumn = "history_id";
                    }
                    statement = jdbcu.getStatement("select " + idColumn + " as id from " + table
                            + " where id_uuid is null limit 1000");
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
            LOG.error("Error: {}", ex);
        }
    }
}
