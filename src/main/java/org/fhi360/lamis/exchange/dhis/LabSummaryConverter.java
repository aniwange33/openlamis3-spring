/**
 * @author AALOZIE
 */

package org.fhi360.lamis.exchange.dhis;

import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Component
public class LabSummaryConverter {
    private int[][] value = new int[18][6];

    private JDBCUtil jdbcUtil;
    private ResultSet resultSet;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private String facility;
    private String state;
    private String lga;

    public String convertXml(String month, String year, String facilityIds) {
        //private static final Log log = LogFactory.getLog(ArtSummaryProcessor.class);
        int reportingMonth = DateUtil.getMonth(month);
        int reportingYear = Integer.parseInt(year);
        String reportingDateBegin = dateFormat.format(DateUtil.getFirstDateOfMonth(reportingYear, reportingMonth));
        String reportingDateEnd = dateFormat.format(DateUtil.getLastDateOfMonth(reportingYear, reportingMonth));
        initScriptId();

        String[] ids = facilityIds.split(",");
        for (String id : ids) {
            long facilityId = Long.parseLong(id);
            process();
        }

        return "";

    }

    private void process() {

    }

    private void save() {
        for (int i = 0; i < 18; i++) {
            int scriptId = value[i][0];
            int dataValue = value[i][1];
            String query = "UPDATE indicator SET state = '" + state + "', lga = '" + lga + "', facility = '" + facility + "', value = '" + dataValue + "' WHERE script_id = '" + scriptId;
            executeUpdate(query);
        }
    }

    private void initScriptId() {
        value[0][0] = 1;
        value[1][0] = 2;
        value[2][0] = 3;
        value[3][0] = 4;
        value[4][0] = 5;
        value[5][0] = 6;
    }

    private void clearValue() {
        value[0][1] = 0;
        value[1][1] = 0;
        value[2][1] = 0;
        value[3][1] = 0;
        value[4][1] = 0;
        value[5][1] = 0;
    }

    private void executeUpdate(String query) {
        try {
            jdbcUtil = new JDBCUtil();
            PreparedStatement preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

}
