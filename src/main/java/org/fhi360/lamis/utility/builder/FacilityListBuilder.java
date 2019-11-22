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

public class FacilityListBuilder {
    private HttpServletRequest request;
    private HttpSession session;
    private ArrayList<Map<String, String>> facilityList = new ArrayList<Map<String, String>>();

    public FacilityListBuilder() {
    }
        
    public void buildFacilityList(ResultSet resultSet) throws SQLException{
        try {
            // loop through resultSet for each row and put into Map
            resultSet.beforeFirst();
            while (resultSet.next()) {
                String facilityId = Long.toString(resultSet.getLong("facility_id")); 
                String name = resultSet.getObject("name") == null ? "" : resultSet.getString("name");
                String address1 = resultSet.getObject("address1") == null ? "" : resultSet.getString("address1");
                String address2 = resultSet.getString("address2") == null ? "" : resultSet.getString("address2");
                String address = address1 + " " + address2;
                String phone1 = resultSet.getObject("phone1") == null ? "" : resultSet.getString("phone1");
                String phone2 = resultSet.getObject("phone2") == null ? "" : resultSet.getString("phone2");
                String email = resultSet.getObject("email") == null ? "" : resultSet.getString("email");
                String dayDqa = resultSet.getObject("day_dqa") == null ? "" : Integer.toString(resultSet.getInt("day_dqa"));
                String facilityType = resultSet.getObject("facility_type") == null ? "" : resultSet.getString("facility_type");
                String padHospitalNum = resultSet.getObject("pad_hospital_num") == null ? "" : Integer.toString(resultSet.getInt("pad_hospital_num"));
                String stateId = Long.toString(resultSet.getLong("state_id")); 
                String lgaId = Long.toString(resultSet.getLong("lga_id")); 

                Map<String, String> map = new HashMap<String, String>();                
                map.put("facilityId", facilityId);
                map.put("name", name);
                map.put("address1", address1);
                map.put("address2", address2);
                map.put("address", address);
                map.put("phone1", phone1);                
                map.put("phone2", phone2);
                map.put("email", email);
                map.put("dayDqa", dayDqa);
                map.put("facilityType", facilityType);
                map.put("padHospitalNum", padHospitalNum);
                map.put("stateId", stateId);
                map.put("lgaId", lgaId);
                facilityList.add(map);
            }            
            session.setAttribute("facilityList", facilityList);   
            resultSet = null;
            facilityList = null;
        }
        catch (SQLException sqlException) {
            resultSet = null;
            throw sqlException;  
        }
    }

    public ArrayList<Map<String, String>> retrieveFacilityList() {
        // retrieve the facility record store in session attribute
        if(session.getAttribute("facilityList") != null) {
            facilityList = (ArrayList) session.getAttribute("facilityList");                        
        }
        return facilityList;
    }            
    
}
