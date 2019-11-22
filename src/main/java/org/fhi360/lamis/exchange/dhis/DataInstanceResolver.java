
package org.fhi360.lamis.exchange.dhis;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

@Component
public class DataInstanceResolver {
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private String query;
    public void resolveInstance() {
        ResultSet resolver = null;
        ResultSet indicator = executeQuery("SELECT * FROM indicator");
        try {
            while (indicator.next()) {
                String state = indicator.getString("state");
                String lga = indicator.getString("lga");
                String orgUnit = indicator.getString("org_unit");
                String dataElement = indicator.getString("data_element");
                String category = indicator.getString("category");
                int value = indicator.getInt("value");
                String dataElementUUID = "";
                String categoryUUID = "";

                query = "SELECT data_element_instance, category_instance FROM datainstance WHERE data_element = '" + dataElement + "' AND category = '" + category + "'";
                resolver = executeQuery(query);
                while (resolver.next()) {
                    dataElementUUID = getDataElementUUID(resolver.getString("data_element_instance"));
                    categoryUUID = getCategoryUUID(resolver.getString("category_instance"));
                }
                String orgUnitUUID = getOrgUnitUUID(orgUnit);

                //Save indicator into datavalue table
                query = "INSERT INTO datavalue (state, lga, org_unit, data_element, category_option_combo, value, stored_by, time_stamp, follow_up) VALUES('" + state + "', '" + lga + "', '" + orgUnitUUID + "', '" + dataElementUUID + "', '" + categoryUUID + "', " + value + ", NOW(), 'false')";
                executeUpdate(query);
            }
        } catch (Exception exception) {
            indicator = null;
            resolver = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private String getDataElementUUID(String dataElementInstance) {
        query = "SELECT data_element_uuid AS uuid FROM dataelement WHERE data_element = '" + dataElementInstance + "'";
        return getUUID(query);
    }

    private String getCategoryUUID(String categoryInstance) {
        query = "SELECT category_uuid AS uuid FROM category WHERE category = '" + categoryInstance + "'";
        return getUUID(query);
    }

    private String getOrgUnitUUID(String orgUnit) {
        query = "SELECT org_unit_uuid AS uuid FROM orgunit WHERE org_unit = '" + orgUnit + "'";
        return getUUID(query);
    }

    private String getUUID(String query) {
        String uuid = "";
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            uuid = resultSet.getString("uuid");
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return uuid;
    }

    private ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return resultSet;
    }

    private void executeUpdate(String query) {
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

}
