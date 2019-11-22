/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.ndr;

/**
 * @author user1
 */

import org.fhi360.lamis.config.ContextProvider;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageStatus {
    private static JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);

    public MessageStatus() {
    }

    public static Map getMessageStatus(long patientId, String identifier) {
        Map<String, Object> map = new HashMap<>();
        map.put("statusCode", "INITIAL");
        map.put("lastMessage", null);

        String query = "SELECT time_stamp FROM ndrmessagelog WHERE patient_id = " + patientId +
                " AND identifier = ?";
        List<Timestamp> dates = jdbcTemplate.queryForList(query, Timestamp.class, identifier);
        if (!dates.isEmpty()) {
            if (dates.get(0) != null) {
                map.put("statusCode", "UPDATED");
                map.put("lastMessage", dates.get(0));
            }
        }
        return map;
    }

    public static long getLastMessageId() {
        long messageId = 0L;
        String query = "SELECT MAX(message_id) AS message_id FROM ndrmessagelog";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class);
        if (!ids.isEmpty()) {
            if (ids.get(0) != null) {
                return ids.get(0);
            }
        }
        return messageId;
    }
}
