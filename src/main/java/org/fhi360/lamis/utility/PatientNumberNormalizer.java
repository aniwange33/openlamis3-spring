/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.model.Facility;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author user1
 */
public class PatientNumberNormalizer {
    private static String query;
    private static JDBCUtil jdbcUtil;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;

    public static void normalize(Facility facility) {
        int paddingStatus = 0; //FacilityDAO.find(facility.getId()).getPadHospitalNum();
        if (facility.getPadHospitalNum() != paddingStatus) {
            try {
                jdbcUtil = new JDBCUtil();
                query = "SELECT patient_id, hospital_num FROM patient WHERE facility_id = ?";
                preparedStatement = jdbcUtil.getStatement(query);
                preparedStatement.setLong(1, facility.getId());
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Long patientId = resultSet.getLong("patient_id");
                    String hospitalNum = resultSet.getString("hospital_num");
                    if (facility.getPadHospitalNum() == 1) {
                        hospitalNum = padNumber(hospitalNum);
                    } else {
                        hospitalNum = unpadNumber(hospitalNum);
                    }
                    query = "UPDATE patient SET hospital_num = ? WHERE facility_id = ? AND patient_id = ?";
                    preparedStatement = jdbcUtil.getStatement(query);
                    preparedStatement.setString(1, hospitalNum);
                    preparedStatement.setLong(2, facility.getId());
                    preparedStatement.setLong(3, patientId);
                    preparedStatement.executeUpdate();
                }
                resultSet = null;
            } catch (Exception exception) {
                resultSet = null;
                jdbcUtil.disconnectFromDatabase();  //disconnect from database
            }
        }
    }
    
    /*public static String normalize(String hospitalNum, long id) {
        if(FacilityDAO.find(id).getPadHospitalNum() == 1) {
            return padNumber(hospitalNum);
        }
        else {            
            return unpadNumber(hospitalNum); 
        }
    }*/

    public static String padNumber(String hospitalNum) {
        hospitalNum = StringUtils.trimToEmpty(hospitalNum);
        hospitalNum = cleanNumber(hospitalNum);
        int MAX_LENGTH = 7;
        return StringUtils.leftPad(hospitalNum, MAX_LENGTH, "0");
    }

    public static String unpadNumber(String hospitalNum) {
        hospitalNum = StringUtils.trimToEmpty(hospitalNum);
        hospitalNum = cleanNumber(hospitalNum);
        char[] numbers = hospitalNum.toCharArray();
        for (int i = 0; i < numbers.length; i++) {
            String ch = Character.toString(numbers[i]);
            if (ch.equals("0")) {
                numbers[i] = ' ';
            } else {
                hospitalNum = String.valueOf(numbers);
                break;
            }
        }
        return hospitalNum.toUpperCase().trim();
    }

    public static String cleanNumber(String hospitalNum) {
        //remove special some characters from hospital number 
        hospitalNum = hospitalNum.replace("'", "");
        hospitalNum = hospitalNum.replace("&", "");
        hospitalNum = hospitalNum.replace("%", "");
        hospitalNum = hospitalNum.replace("?", "");
        hospitalNum = hospitalNum.replace(",", "");
        hospitalNum = hospitalNum.replace(" ", "");
        return hospitalNum;
    }
}

//JSONObject json = (JSONObject) JSONSerializer.toJSON(content);
//normalize((Integer) json.get("padHospitalNum") == 1? true : false, (Long) json.get("id"));
