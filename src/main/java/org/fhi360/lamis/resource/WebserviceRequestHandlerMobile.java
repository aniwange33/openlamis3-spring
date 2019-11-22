/**
 *
 * @author Alozie
 */
package org.fhi360.lamis.resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.fhi360.lamis.utility.JDBCUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import org.fhi360.lamis.model.DeviceConfig;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebserviceRequestHandlerMobile {

    private static String query;
    private static JDBCUtil jdbcUtil;
    private static PreparedStatement preparedStatement;

    public static synchronized void syncStream(InputStream stream, String table) {
        String content = getJsonString(stream);
      //  new JsonParserDeligator().delegate(table, content);
    }

    //Get all newly devolved patients from the server and return to LAMIS mobile app
    public static synchronized byte[] getPatient(long communitypharmId) {
        Timestamp timestamp = new java.sql.Timestamp(new java.util.Date().getTime());
        byte[] bytes = null;
        try {
            jdbcUtil = new JDBCUtil();
            query = "SELECT patient_id, facility_id, hospital_num, unique_id, surname, other_names, gender, date_birth, age_unit, age, address, phone, date_started, regimen, regimenType, last_clinic_stage, "
                    + " date_last_viral_load, last_viral_load, viral_load_due_date, viral_load_type, date_last_cd4, last_cd4, date_last_clinic, date_last_refill, date_next_clinic, date_next_refill, last_refill_setting "
                    + " FROM patient WHERE communitypharm_id = " + communitypharmId + " AND time_stamp > (SELECT time_stamp FROM communitypharm WHERE communitypharm_id = " + communitypharmId + ")";
            preparedStatement = jdbcUtil.getStatement(query);
            JSONArray patientArray = convertResultSetToJsonArray(preparedStatement.executeQuery());

            query = "SELECT * FROM devolve WHERE communitypharm_id = " + communitypharmId + " AND time_stamp > SELECT time_stamp FROM communitypharm WHERE communitypharm_id = " + communitypharmId;
            preparedStatement = jdbcUtil.getStatement(query);
            JSONArray devolveArray = convertResultSetToJsonArray(preparedStatement.executeQuery());

            executeUpdate("DROP TABLE IF EXISTS temp");
            query = "CREATE TEMPORARY TABLE temp AS SELECT DISTINCT facility.facility_id, facility.name, lga.name AS lga, state.name AS state FROM facility JOIN lga ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE facility.facility_id IN (SELECT DISTINCT facility_id FROM devolve WHERE communitypharm_id = " + communitypharmId + ") ORDER BY state.name, facility.name";
            executeUpdate(query);
            preparedStatement = jdbcUtil.getStatement("SELECT * from temp");
            JSONArray facilityArray = convertResultSetToJsonArray(preparedStatement.executeQuery());

            JSONObject obj = new JSONObject();
            obj.put("patients", patientArray);
            obj.put("devolves", devolveArray);
            obj.put("facilities", facilityArray);
            String json = obj.toString();
            System.out.println("....." + json);

            bytes = new byte[json.length()];
            bytes = json.getBytes();   //Convert string to an array of bytes
            updateLastSync(timestamp, communitypharmId);
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();
            throw new RuntimeException(exception);
        }
        return bytes;
    }

    public static synchronized byte[] getActivationData(String ip, String pin, String deviceId, String username, String password) {
        Timestamp timestamp = new java.sql.Timestamp(new java.util.Date().getTime());
        byte[] bytes = null;

        if (ip.equalsIgnoreCase("fhi360")) {
            long communitypharmId = getCommunitypharmId(pin);

            long deviceconfigId =0;// new DeviceconfigJDBC().getDeviceconfigId(deviceId);
           // Deviceconfig deviceconfig = new Deviceconfig();
//            deviceconfig.setId(communitypharmId);
//            deviceconfig.setDeviceId(deviceId);
//            deviceconfig.setUsername(username);
//            deviceconfig.setPassword(password);

            if (deviceconfigId == 0L) {
              //  deviceconfigId = DeviceconfigDAO.save(deviceconfig);
            } else {
             ///   deviceconfig.setDeviceconfigId(deviceconfigId);
               // DeviceconfigDAO.update(deviceconfig);
            }

            try {
                jdbcUtil = new JDBCUtil();
                if (communitypharmId != 0) {
                    query = "SELECT * FROM communitypharm WHERE communitypharm_id = " + communitypharmId;
                    preparedStatement = jdbcUtil.getStatement(query);
                    JSONArray accountArray = convertResultSetToJsonArray(preparedStatement.executeQuery());

                    query = "SELECT patient_id, facility_id, hospital_num, unique_id, surname, other_names, gender, date_birth, age_unit, age, address, phone, date_started, regimen, regimenType, last_clinic_stage, "
                            + " date_last_viral_load, last_viral_load, viral_load_due_date, viral_load_type, date_last_cd4, last_cd4, date_last_clinic, date_last_refill, date_next_clinic, date_next_refill, last_refill_setting "
                            + " FROM patient WHERE communitypharm_id = " + communitypharmId;
                    preparedStatement = jdbcUtil.getStatement(query);
                    JSONArray patientArray = convertResultSetToJsonArray(preparedStatement.executeQuery());

                    query = "SELECT * FROM devolve WHERE communitypharm_id = " + communitypharmId;
                    preparedStatement = jdbcUtil.getStatement(query);
                    JSONArray devolveArray = convertResultSetToJsonArray(preparedStatement.executeQuery());

                    executeUpdate("DROP TABLE IF EXISTS temp");
                    query = "CREATE TEMPORARY TABLE temp AS SELECT DISTINCT facility.facility_id, facility.name, lga.name AS lga, state.name AS state FROM facility JOIN lga ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE facility.facility_id IN (SELECT DISTINCT facility_id FROM devolve WHERE communitypharm_id = " + communitypharmId + ") ORDER BY state.name, facility.name";
                    executeUpdate(query);
                    preparedStatement = jdbcUtil.getStatement("SELECT * from temp");
                    JSONArray facilityArray = convertResultSetToJsonArray(preparedStatement.executeQuery());

                    executeUpdate("DROP TABLE IF EXISTS temp");
                    query = "CREATE TEMPORARY TABLE temp AS SELECT regimen.regimen_id, regimen.description AS regimen, regimenType.regimentype_id, regimenType.description AS regimenType FROM regimen JOIN regimenType ON regimen.regimentype_id = regimenType.regimentype_id";
                    executeUpdate(query);
                    preparedStatement = jdbcUtil.getStatement("SELECT * from temp");
                    JSONArray regimenArray = convertResultSetToJsonArray(preparedStatement.executeQuery());

                    JSONObject obj = new JSONObject();
                    obj.put("accounts", accountArray);
                    obj.put("patients", patientArray);
                    obj.put("devolves", devolveArray);
                    obj.put("facilities", facilityArray);
                    obj.put("regimens", regimenArray);
                    String json = obj.toString();
                    System.out.println("....." + json);

                    bytes = new byte[json.length()];
                    bytes = json.getBytes();
                    updateLastSync(timestamp, communitypharmId);
                }
            } catch (Exception exception) {
                jdbcUtil.disconnectFromDatabase();
                throw new RuntimeException(exception);
            }
        }

        return bytes;
    }

    public static synchronized byte[] getInitializationData(String ip, long facilityId, String deviceId, String username, String password) {
        Timestamp timestamp = new java.sql.Timestamp(new java.util.Date().getTime());
        byte[] bytes = null;
        if (ip.equalsIgnoreCase("fhi360")) {
            long deviceconfigId =0;// new DeviceconfigJDBC().getDeviceconfigId(deviceId);
            DeviceConfig deviceconfig = new DeviceConfig();
          //  deviceconfig.setId(id);
            deviceconfig.setDeviceId(deviceId);
          //  deviceconfig.setUsername(username);
           // deviceconfig.setPassword(password);

            if (deviceconfigId == 0L) {
               // deviceconfigId = DeviceconfigDAO.save(deviceconfig);
            } else {
             //   deviceconfig.setDeviceconfigId(deviceconfigId);
             //   DeviceconfigDAO.update(deviceconfig);
            }

            try {
                jdbcUtil = new JDBCUtil();

                query = "SELECT state_id, name FROM state";
                preparedStatement = jdbcUtil.getStatement(query);
                JSONArray stateArray = convertResultSetToJsonArray(preparedStatement.executeQuery());

                query = "SELECT lga_id, state_id, name FROM lga";
                preparedStatement = jdbcUtil.getStatement(query);
                JSONArray lgaArray = convertResultSetToJsonArray(preparedStatement.executeQuery());

                query = "SELECT facility.facility_id, facility.state_id, facility.lga_id, facility.name, deviceconfig.deviceconfig_id FROM  facility JOIN deviceconfig ON facility.facility_id = deviceconfig.facility_id WHERE deviceconfig.device_id = '" + deviceId + "'";
                preparedStatement = jdbcUtil.getStatement(query);
                JSONArray facilityArray = convertResultSetToJsonArray(preparedStatement.executeQuery());

                executeUpdate("DROP TABLE IF EXISTS temp");
                query = "CREATE TEMPORARY TABLE temp AS SELECT regimen.regimen_id, regimen.description AS regimen, regimenType.regimentype_id, regimenType.description AS regimenType FROM regimen JOIN regimenType ON regimen.regimentype_id = regimenType.regimentype_id";
                executeUpdate(query);
                preparedStatement = jdbcUtil.getStatement("SELECT * from temp");
                JSONArray regimenArray = convertResultSetToJsonArray(preparedStatement.executeQuery());

                JSONObject obj = new JSONObject();
                obj.put("states", stateArray);
                obj.put("lgas", lgaArray);
                obj.put("facilities", facilityArray);
                obj.put("regimens", regimenArray);
                String json = obj.toString();
                System.out.println("....." + json);

                bytes = new byte[json.length()];
                bytes = json.getBytes();
            } catch (Exception exception) {
                jdbcUtil.disconnectFromDatabase();
                throw new RuntimeException(exception);
            }
        }
        return bytes;
    }

    public static synchronized byte[] getCredentials(String deviceId, String pin) {
        long communitypharmId = getCommunitypharmId(pin);
        return getCredentials(deviceId, communitypharmId);
    }

    public static synchronized byte[] getCredentials(String deviceId, long facilityId) {
        byte[] bytes = null;
        try {
            jdbcUtil = new JDBCUtil();

            query = "SELECT username, password FROM deviceconfig WHERE device_id = '" + deviceId + "' AND facility_id = " + facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            String username = "";
            String password = "";
            if (rs.next()) {
                username = rs.getString("username");
                password = rs.getString("password");
            }

            JSONObject obj = new JSONObject();
            obj.put("accountUserName", username);
            obj.put("accountPassword", password);

            String json = obj.toString();
            bytes = new byte[json.length()];
            bytes = json.getBytes();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();
            throw new RuntimeException(exception);
        }

        return bytes;
    }

    private static long getCommunitypharmId(String pin) {
        long communitypharmId = 0;
        try {
            jdbcUtil = new JDBCUtil();
            //query = "SELECT communitypharm_id FROM communitypharm WHERE (email = '" + username + "' OR phone = '" + username + "') AND pin = '" + pin + "'";
            query = "SELECT communitypharm_id FROM communitypharm WHERE pin = '" + pin + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                communitypharmId = rs.getLong("communitypharm_id");
            }
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();
            throw new RuntimeException(exception);
        }
        return communitypharmId;
    }

    private static JSONArray convertResultSetToJsonArray(ResultSet resultSet) {
        JSONArray jsonArray = new JSONArray();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colCount = metaData.getColumnCount();
            while (resultSet.next()) {
                JSONObject record = new JSONObject();
                for (int i = 1; i <= colCount; i++) {
                    String columnName = metaData.getColumnName(i).toLowerCase();
                    record.put(columnName, resultSet.getObject(i) == null ? "" : resultSet.getObject(i));
                }
                jsonArray.put(record);
            }
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
            throw new RuntimeException(exception);
        }
        return jsonArray;
    }

    private static String getJsonString(InputStream stream) {
        String content = "";
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(stream));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            content = stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    private static JSONObject getJson(InputStream stream) {
        JSONObject jsonObject = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(stream));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private static void updateLastSync(Timestamp timestamp, long communitypharmId) {
        try {
            query = "UPDATE communitypharm SET time_stamp = '" + timestamp + "' WHERE communitypharm_id = " + communitypharmId;
            executeUpdate(query);
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private static void executeUpdate(String query) {
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }
}

//                query = "SELECT facility_id, patient_id, hospital_num, unique_id, surname, other_names, address, gender, date_birth, phone, date_started, regimen, regimenType, last_clinic_stage, "
//                    + " last_viral_load, last_cd4, date_last_cd4, date_last_viral_load, date_last_refill, date_next_refill, date_last_clinic, date_next_clinic "
//                + " FROM patient WHERE patient_id IN (SELECT patient_id FROM devolve WHERE communitypharm_id = " + communitypharmId + ")"; 

