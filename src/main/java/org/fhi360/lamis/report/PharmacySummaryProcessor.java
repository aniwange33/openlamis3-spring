/**
 * @author aalozie
 */

package org.fhi360.lamis.report;

import org.fhi360.lamis.controller.report.ReportParameterDTO;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PharmacySummaryProcessor {
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;

    private long facilityId;

    public PharmacySummaryProcessor() {
    }

    public synchronized List<Map<String, Object>> process(ReportParameterDTO dto, Long facilityId) {
        List<Map<String, Object>> reportList = new ArrayList<>();
        int reportingMonth = DateUtil.getMonth(dto.getReportingMonth());
        int reportingYear = Integer.parseInt(dto.getReportingYear());
        ResultSet resultSet;

        try {
            jdbcUtil = new JDBCUtil();

            executeUpdate("DROP TABLE IF EXISTS refill");
            query = "CREATE TEMPORARY TABLE refill AS SELECT pharmacy.pharmacy_id, pharmacy.patient_id, pharmacy.morning+pharmacy.afternoon+pharmacy.evening AS quantity, pharmacy.regimentype_id, pharmacy.regimendrug_id, regimendrug.regimen_id, regimendrug.drug_id, drug.name, drug.strength "
                    + " FROM pharmacy JOIN regimendrug ON pharmacy.regimendrug_id = regimendrug.regimendrug_id JOIN drug ON regimendrug.drug_id = drug.drug_id WHERE pharmacy.facility_id = " + facilityId + " AND YEAR(pharmacy.date_visit) = " + reportingYear + " AND MONTH(pharmacy.date_visit) = " + reportingMonth;
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();

            query = "SELECT refill.name AS description, SUM(refill.quantity) AS quantity, COUNT(DISTINCT refill.patient_id) AS num FROM refill GROUP BY refill.name";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            // loop through resultSet for each row and put into Map
            int i = 0;
            while (resultSet.next()) {
                String description = resultSet.getString("description");
                double quantity = resultSet.getDouble("quantity");
                int num = resultSet.getInt("num");

                // create map of values 
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("sno", Integer.toString(i++));
                map.put("description", description);
                map.put("quantity", Double.toString(quantity));
                map.put("num", Integer.toString(num));
                reportList.add(map);
            }
            resultSet = null;
        } catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return reportList;
    }

    public Map<String, Object> getReportParameters(String reportingMonth, String reportingYear, Long facilityId) {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("reportingMonth", reportingMonth);
        parameterMap.put("reportingYear", reportingYear);
        ResultSet resultSet;

        try {
            // fetch the required records from the database
            jdbcUtil = new JDBCUtil();
            query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility JOIN lga ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE facility_id = " + facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                parameterMap.put("facilityName", resultSet.getString("name"));
                parameterMap.put("lga", resultSet.getString("lga"));
                parameterMap.put("state", resultSet.getString("state"));
            }
            resultSet = null;
        } catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return parameterMap;
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
