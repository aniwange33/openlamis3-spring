/**
 * @author aalozie
 */

package org.fhi360.lamis.utility.builder;

import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.utility.DateUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParticipantListBuilder {
    private final JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);
    private HttpServletRequest request;
    private HttpSession session;
    private ArrayList<Map<String, String>> participantList = new ArrayList<Map<String, String>>();

    public ParticipantListBuilder() {
    }

    public void buildList(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String query = "SELECT participant.*, conversation.* FROM participant JOIN conversation ON " +
                    "participant.phone = conversation.phone WHERE conversation.originator_id = 0  " +
                    "AND conversation.phone = ? AND conversation.time_stamp = ?";
            jdbcTemplate.query(query, resultSet1 -> {
                String participantId = Long.toString(resultSet1.getLong("participant_id"));
                String phone = resultSet1.getObject("phone") == null ? "" : resultSet1.getString("phone");
                String age = resultSet1.getObject("age") == null ? "" : resultSet1.getInt("age") == 0 ? "" : Integer.toString(resultSet1.getInt("age"));
                String gender = resultSet1.getObject("gender") == null ? "" : resultSet1.getString("gender");
                String location = resultSet1.getObject("location") == null ? "" : resultSet1.getString("location");
                String message = resultSet1.getObject("message") == null ? "" : resultSet1.getString("message");
                String dateMessage = resultSet1.getObject("date_message") == null ? "" : DateUtil.parseDateToString(resultSet1.getDate("date_message"), "MM/dd/yyyy");
                String unread = Integer.toString(resultSet1.getInt("unread"));

                message = "#" + participantId + "  " + message;
                Map<String, String> map = new HashMap<String, String>();
                map.put("participantId", participantId);
                map.put("phone", phone);
                map.put("age", age);
                map.put("gender", gender);
                map.put("location", location);
                map.put("message", message);
                map.put("dateMessage", dateMessage);
                map.put("unread", unread);
                participantList.add(map);
            }, resultSet.getString("phone"), resultSet.getTimestamp("time_stamp"));
        }
        session.setAttribute("participantList", participantList);
    }

    public ArrayList<Map<String, String>> retrieveList() {
        // retrieve the participant record store in session attribute
        if (session.getAttribute("participantList") != null) {
            participantList = (ArrayList) session.getAttribute("participantList");
        }
        return participantList;
    }

}
