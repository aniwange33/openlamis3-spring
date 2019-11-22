/**
 * @author user1
 */


package org.fhi360.lamis.service;

import org.fhi360.lamis.config.ContextProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.http.HttpSession;
import java.util.Date;
@Component
public class MonitorService {

    public static void logEntity(String entityId, String tableName, int operationId) {
        HttpSession session = null;
        JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);
        TransactionTemplate transactionTemplate = ContextProvider.getBean(TransactionTemplate.class);
        String query = "INSERT INTO monitor (facility_id, entity_id, table_name, operation_id, user_id, time_stamp) VALUES(?, ?, ?, ?, ?, ?)";
        transactionTemplate.execute(status -> {
            jdbcTemplate.update(query, session.getAttribute("id"), entityId,
                    tableName, operationId, session.getAttribute("id"), new Date());
            return null;
        });
    }

}
