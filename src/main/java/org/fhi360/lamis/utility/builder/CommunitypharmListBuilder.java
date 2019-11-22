/**
 *
 * @author aalozie
 */

package org.fhi360.lamis.utility.builder;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CommunitypharmListBuilder {
    private HttpServletRequest request;
    private HttpSession session;
    private ArrayList<Map<String, String>> pharmList = new ArrayList<Map<String, String>>();
    
    public CommunitypharmListBuilder() {
    }
        
    public void buildCommunitypharmList(ResultSet resultSet) throws SQLException{
        try {
            resultSet.beforeFirst();
            while (resultSet.next()) {
                String communitypharmId = Long.toString(resultSet.getLong("communitypharm_id"));
                String stateId = Long.toString(resultSet.getLong("state_id"));
                String lgaId = Long.toString(resultSet.getLong("lga_id"));
                String pharmacy = resultSet.getObject("pharmacy") == null ? "" : resultSet.getString("pharmacy");
                String address = resultSet.getObject("address") == null ? "" : resultSet.getString("address");
                String phone = resultSet.getObject("phone") == null ? "" : resultSet.getString("phone");
                String email = resultSet.getObject("email") == null ? "" : resultSet.getString("email");
                String pin = resultSet.getObject("pin") == null ? "" : resultSet.getString("pin");
                
                Map<String, String> map = new HashMap<String, String>();                
                map.put("communitypharmId", communitypharmId);
                map.put("stateId", stateId);
                map.put("lgaId", lgaId);
                map.put("pharmacy", pharmacy);
                map.put("address", address);
                map.put("phone", phone);                
                map.put("email", email);
                map.put("pin", pin);
                pharmList.add(map);
            }            
            session.setAttribute("pharmList", pharmList);   
            resultSet = null;
            pharmList = null;
        }
        catch (SQLException sqlException) {
            resultSet = null;
            throw sqlException;  
        }
    }

    public ArrayList<Map<String, String>> retrieveCommunitypharmList() {
        if(session.getAttribute("pharmList") != null) {
            pharmList = (ArrayList) session.getAttribute("pharmList");                        
        }
        return pharmList;
    }            
    
}
