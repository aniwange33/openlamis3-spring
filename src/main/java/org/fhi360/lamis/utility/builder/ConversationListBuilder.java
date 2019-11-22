/**
 *
 * @author aalozie
 */

package org.fhi360.lamis.utility.builder;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.fhi360.lamis.utility.DateUtil;

public class ConversationListBuilder {
    private HttpServletRequest request;
    private HttpSession session;
    private ArrayList<Map<String, String>> conversationList = new ArrayList<Map<String, String>>();
    
    public ConversationListBuilder() {
    }
        
    public void buildList(ResultSet resultSet) throws SQLException{
        try {
            // loop through resultSet for each row and put into Map
            resultSet.beforeFirst();
            while (resultSet.next()) {
                String conversationId = Long.toString(resultSet.getLong("conversation_id")); 
                String phone = resultSet.getObject("phone") == null ? "" :  resultSet.getString("phone");
                String message = resultSet.getObject("message") == null ? "" :  resultSet.getString("message");
                String dateMessage = resultSet.getObject("date_message") == null ? "" : DateUtil.parseDateToString(resultSet.getDate("date_message"), "MM/dd/yyyy");                
                String originatorId = resultSet.getObject("originator_id") == null ? "" :  Integer.toString(resultSet.getInt("originator_id"));
                Timestamp time = resultSet.getTimestamp("time_stamp");
                String timeMessage = dateMessage; //
            
                Map<String, String> map = new HashMap<String, String>();                
                map.put("conversationId", conversationId);
                map.put("phone", phone);
                map.put("message", message);
                map.put("dateMessage", dateMessage);
                map.put("originatorId", originatorId);
                map.put("timeMessage", timeMessage);
                conversationList.add(map);
            }            
            session.setAttribute("conversationList", conversationList);   
            resultSet = null;
            conversationList = null;
        }
        catch (SQLException sqlException) {
            resultSet = null;
            throw sqlException;  
        }
    }    
    
    public ArrayList<Map<String, String>> retrieveList() {
        // retrieve the conversation record store in session attribute
        if(session.getAttribute("conversationList") != null) {
            conversationList = (ArrayList) session.getAttribute("conversationList");                        
        }
        return conversationList;
    }            
    
}
