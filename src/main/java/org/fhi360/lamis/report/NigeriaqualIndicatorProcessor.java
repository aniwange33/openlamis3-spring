/**
 *
 * @author AALOZIE
 */
package org.fhi360.lamis.report;

import org.fhi360.lamis.report.indicator.NigeriaQualityIndicators;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NigeriaqualIndicatorProcessor {
    private int[][] value = new int[8][2];

    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;

    public NigeriaqualIndicatorProcessor() {
    }

    public synchronized List<Map<String, Object>> process(String monthBegin, String monthEnd,
                                                          Integer  reportingYearBegin, Integer reportingYearEnd,
                                                          Long facilityId) {
        List<Map<String, Object>> reportList = new ArrayList<>();
        int reportingMonthBegin = DateUtil.getMonth(monthBegin);
        int reportingMonthEnd = DateUtil.getMonth(monthEnd);

        String reportingDateBegin = DateUtil.parseDateToString(DateUtil.getFirstDateOfMonth(reportingYearBegin, reportingMonthBegin), "yyyy-MM-dd");
        String reportingDateEnd = DateUtil.parseDateToString(DateUtil.getLastDateOfMonth(reportingYearEnd, reportingMonthEnd), "yyyy-MM-dd");
        String[] indicator = new NigeriaQualityIndicators().initialize();

        try {
            jdbcUtil = new JDBCUtil();

            //Percentage of HIV positive pregnant women who received a complete course of ARV prophylaxis for PMTCT

            //create a temporary table of positives at the end of the review period
            executeUpdate("DROP TABLE IF EXISTS positives");
            String query = "CREATE TEMPORARY TABLE positives AS SELECT DISTINCT patient_id, MAX(date_current_status) FROM statushistory "
                    + "WHERE facility_id = " + facilityId + " AND date_current_status <= '" + reportingDateEnd + "' "
                    + "AND current_status IN ('HIV+ non ART', 'ART Transfer In', 'Pre-ART Transfer In', 'ART Start - external', 'ART Start') "
                    + "GROUP BY patient_id";
            executeUpdate(query);

            //Numerator - Number of HIV positive pregnant women who delivered within the review period who received a complete course of ARV prophylaxis for PMTCT
            query = "SELECT COUNT(DISTINCT delivery.patient_id) AS count FROM delivery JOIN positives ON delivery.patient_id = positives.patient_id "
                    + "WHERE (delivery.arv_regimen_current IN ('ART', 'Prophylaxis (Triple)', 'Prophylaxis (ZDV)', 'Prophylaxis (sdNVP)', 'Prophylaxis (sdNVP+ZDV+3TC)') "
                    + "OR delivery.patient_id IN (SELECT DISTINCT anc.patient_id FROM anc WHERE anc.arv_regimen_current IN ('ART', 'Prophylaxis (Triple)', 'Prophylaxis (ZDV)', 'Prophylaxis (sdNVP+(AZT)+(3TC))'))) "
                    + "AND delivery.date_delivery >= '" + reportingDateBegin + "' AND delivery.date_delivery <= '" + reportingDateEnd + "'";
            value[0][0] = executeQuery(query);

            //Denominator - Number of HIV positive pregnant women who delivered within the review period (including known positives)
            query = "SELECT COUNT(DISTINCT delivery.patient_id) AS count FROM delivery JOIN positives ON delivery.patient_id = positives.patient_id "
                    + "WHERE delivery.date_delivery >= '" + reportingDateBegin + "' AND delivery.date_delivery <= '" + reportingDateEnd + "'";
            value[0][1] = executeQuery(query);

            //Percentage of infants born to HIV positive women who received a complete course of ARVs for PMTCT

            //create a temporary table of positives within the first 3 months of the review period
            String reportingDateEnd1 = DateUtil.parseDateToString(DateUtil.addMonth(DateUtil.getFirstDateOfMonth(reportingYearBegin, reportingMonthBegin), 3), "yyyy-MM-dd");
            executeUpdate("DROP TABLE IF EXISTS positives");
            query = "CREATE TEMPORARY TABLE positives AS SELECT DISTINCT patient_id, MAX(date_current_status) FROM statushistory "
                    + "WHERE facility_id = " + facilityId + " AND date_current_status <= '" + reportingDateEnd1 + "' "
                    + "AND current_status IN ('HIV+ non ART', 'ART Transfer In', 'Pre-ART Transfer In', 'ART Start - external', 'ART Start') "
                    + "GROUP BY patient_id";
            executeUpdate(query);

            //Numerator - Number of infants born to HIV positive women within the first 3 months of the review period who received a complete course of ARV for PMTCT
            query = "SELECT COUNT(DISTINCT childfollowup.child_id) AS count FROM childfollowup JOIN child ON childfollowup.child_id = child.child_id "
                    + "JOIN positives ON child.patient_id_mother = positives.patient_id "
                    + "WHERE child.date_birth >= '" + reportingDateBegin + "' AND child.date_birth <= '" + reportingDateEnd1 + "' "
                    + "AND childfollowup.arv = 'Yes'";
            value[1][0] = executeQuery(query);

            //Denominator - Number of infants born to HIV positive women within the first 3 months of the review period
            query = "SELECT COUNT(DISTINCT childfollowup.child_id) AS count FROM childfollowup JOIN child ON childfollowup.child_id = child.child_id "
                    + "JOIN positives ON child.patient_id_mother = positives.patient_id "
                    + "WHERE child.date_birth >= '" + reportingDateBegin + "' AND child.date_birth <= '" + reportingDateEnd1 + "'";
            value[1][1] = executeQuery(query);

            //Percentage of HIV exposed children aged 6-24 weeks who had DBS sample collected for DNA PCR test at 6-8 weeks of age in the review period

            //reset the table of positives
            executeUpdate("DROP TABLE IF EXISTS positives");
            query = "CREATE TEMPORARY TABLE positives AS SELECT DISTINCT patient_id, MAX(date_current_status) FROM statushistory "
                    + "WHERE facility_id = " + facilityId + " AND date_current_status <= '" + reportingDateEnd + "' "
                    + "AND current_status IN ('HIV+ non ART', 'ART Transfer In', 'Pre-ART Transfer In', 'ART Start - external', 'ART Start') "
                    + "GROUP BY patient_id";
            executeUpdate(query);

            //Numerator - Number of HIV exposed children that had DBS sample collected at age 6-8 weeks during the review period
            query = "SELECT COUNT(DISTINCT childfollowup.child_id) AS count FROM childfollowup JOIN child ON childfollowup.child_id = child.child_id "
                    + "JOIN positives ON child.patient_id_mother = positives.patient_id "
                    + "WHERE childfollowup.date_visit >= '" + reportingDateBegin + "' AND childfollowup.date_visit <= '" + reportingDateEnd + "' "
                    + "AND ((TIMESTAMPDIFF(DAY, child.date_birth, '" + reportingDateEnd + "') / 7) >= 6 AND (TIMESTAMPDIFF(DAY, child.date_birth, '" + reportingDateEnd + "') / 7) <= 24) "
                    + "AND ((TIMESTAMPDIFF(DAY, child.date_birth, childfollowup.date_sample_collected) / 7) >= 6 AND (TIMESTAMPDIFF(DAY, child.date_birth, childfollowup.date_sample_collected) / 7) <= 8)";
            value[2][0] = executeQuery(query);

            //Denominator - Number of HIV exposed children 6 to 24 weeks of age within the 6 months review period
            query = "SELECT COUNT(DISTINCT childfollowup.child_id) AS count FROM childfollowup JOIN child ON childfollowup.child_id = child.child_id "
                    + "JOIN positives ON child.patient_id_mother = positives.patient_id "
                    + "WHERE childfollowup.date_visit >= '" + reportingDateBegin + "' AND childfollowup.date_visit <= '" + reportingDateEnd + "' "
                    + "AND ((TIMESTAMPDIFF(DAY, child.date_birth, '" + reportingDateEnd + "') / 7) >= 6 AND (TIMESTAMPDIFF(DAY, child.date_birth, '" + reportingDateEnd + "') / 7) <= 24)";
            value[2][1] = executeQuery(query);

            //Percentage of HIV exposed children aged 6-8 weeks in the first 3 months of review period who received their DNA PCR results by 12 weeks of age

            //create a temporary table of positives within the first 3 months of the review period
            executeUpdate("DROP TABLE IF EXISTS positives");
            query = "CREATE TEMPORARY TABLE positives AS SELECT DISTINCT patient_id, MAX(date_current_status) FROM statushistory "
                    + "WHERE facility_id = " + facilityId + " AND date_current_status <= '" + reportingDateEnd1 + "' "
                    + "AND current_status IN ('HIV+ non ART', 'ART Transfer In', 'Pre-ART Transfer In', 'ART Start - external', 'ART Start') "
                    + "GROUP BY patient_id";
            executeUpdate(query);

            //Numerator - Number of HIV exposed children aged 6-8 weeks in the first 3 months of review period who had their DNA PCR result by 12 weeks of age
            query = "SELECT COUNT(DISTINCT childfollowup.child_id) AS count FROM childfollowup JOIN child ON childfollowup.child_id = child.child_id "
                    + "JOIN positives ON child.patient_id_mother = positives.patient_id "
                    + "WHERE childfollowup.date_visit >= '" + reportingDateBegin + "' AND childfollowup.date_visit <= '" + reportingDateEnd + "' "
                    + "AND ((TIMESTAMPDIFF(DAY, child.date_birth, childfollowup.date_sample_collected) / 7) >= 6 AND (TIMESTAMPDIFF(DAY, child.date_birth, childfollowup.date_sample_collected) / 7) <= 8) "
                    + "AND (TIMESTAMPDIFF(DAY, child.date_birth, childfollowup.date_pcr_result) / 7) <= 12";
            value[3][0] = executeQuery(query);

            //Denominator - Number of HIV exposed children aged 6-8 weeks in the first 3 months of review period that had DBS sample collected at age 6-8 weeks
            query = "SELECT COUNT(DISTINCT childfollowup.child_id) AS count FROM childfollowup JOIN child ON childfollowup.child_id = child.child_id "
                    + "JOIN positives ON child.patient_id_mother = positives.patient_id "
                    + "WHERE childfollowup.date_visit >= '" + reportingDateBegin + "' AND childfollowup.date_visit <= '" + reportingDateEnd + "' "
                    + "AND ((TIMESTAMPDIFF(DAY, child.date_birth, childfollowup.date_sample_collected) / 7) >= 6 AND (TIMESTAMPDIFF(DAY, child.date_birth, childfollowup.date_sample_collected) / 7) <= 8)";
            value[3][1] = executeQuery(query);

            //Percentage of exposed infants who had PMTCT intervention and confirmed HIV Negative at 18 months

            //reset the table of positives
            executeUpdate("DROP TABLE IF EXISTS positives");
            query = "CREATE TEMPORARY TABLE positives AS SELECT DISTINCT patient_id, MAX(date_current_status) FROM statushistory "
                    + "WHERE facility_id = " + facilityId + " AND date_current_status <= '" + reportingDateEnd + "' "
                    + "AND current_status IN ('HIV+ non ART', 'ART Transfer In', 'Pre-ART Transfer In', 'ART Start - external', 'ART Start') "
                    + "GROUP BY patient_id";
            executeUpdate(query);

            //Numerator - Number of exposed infants born to HIV positive women who had PMTCT intervention and confirmed HIV negative at 18 months of age during the reporting period
            query = "SELECT COUNT(DISTINCT childfollowup.child_id) AS count FROM childfollowup JOIN child ON childfollowup.child_id = child.child_id "
                    + "JOIN positives ON child.patient_id_mother = positives.patient_id "
                    + "WHERE childfollowup.arv = 'Yes' AND (childfollowup.pcr_result = 'N - Negative' OR childfollowup.rapid_test_result = 'Negative') "
                    + "AND ((TIMESTAMPDIFF(DAY, child.date_birth, '" + reportingDateEnd + "') / 30) >= 18 AND (TIMESTAMPDIFF(DAY, child.date_birth, '" + reportingDateEnd + "') / 30) <= (18 + (TIMESTAMPDIFF(DAY, '" + reportingDateBegin + "', '" + reportingDateEnd + "') / 30)))";
            value[4][0] = executeQuery(query);

            //Denominator - Number of exposed infants born to HIV positive women who had PMTCT intervention and had a confirmatory HIV test at 18 months of age during the reporting period
            query = "SELECT COUNT(DISTINCT childfollowup.child_id) AS count FROM childfollowup JOIN child ON childfollowup.child_id = child.child_id "
                    + "JOIN positives ON child.patient_id_mother = positives.patient_id "
                    + "WHERE childfollowup.arv = 'Yes' AND (childfollowup.pcr_result IN ('P - Positive', 'N - Negative') OR childfollowup.rapid_test_result IN ('Positive', 'Negative')) "
                    + "AND ((TIMESTAMPDIFF(DAY, child.date_birth, '" + reportingDateEnd + "') / 30) >= 18 AND (TIMESTAMPDIFF(DAY, child.date_birth, '" + reportingDateEnd + "') / 30) <= (18 + (TIMESTAMPDIFF(DAY, '" + reportingDateBegin + "', '" + reportingDateEnd + "') / 30)))";
            value[4][1] = executeQuery(query);

            //Percentage of HIV exposed children 6 weeks � 18 months with at least one clinical visit in the last 3 months

            //reset the table of positives
            executeUpdate("DROP TABLE IF EXISTS positives");
            query = "CREATE TEMPORARY TABLE positives AS SELECT DISTINCT patient_id, MAX(date_current_status) FROM statushistory "
                    + "WHERE facility_id = " + facilityId + " AND date_current_status <= '" + reportingDateEnd + "' "
                    + "AND current_status IN ('HIV+ non ART', 'ART Transfer In', 'Pre-ART Transfer In', 'ART Start - external', 'ART Start') "
                    + "GROUP BY patient_id";
            executeUpdate(query);

            //Numerator - Number of HIV-exposed children who have had at least one documented clinical visit in the last 3 months
            String reportingDateBegin1 = DateUtil.parseDateToString(DateUtil.addMonth(DateUtil.getLastDateOfMonth(reportingYearEnd, reportingMonthEnd), -3), "yyyy-MM-dd");
            query = "SELECT COUNT(DISTINCT childfollowup.child_id) AS count FROM childfollowup JOIN child ON childfollowup.child_id = child.child_id "
                    + "JOIN positives ON child.patient_id_mother = positives.patient_id "
                    + "WHERE childfollowup.date_visit >= '" + reportingDateBegin1 + "' AND childfollowup.date_visit <= '" + reportingDateEnd + "' "
                    + "AND ((TIMESTAMPDIFF(DAY, child.date_birth, '" + reportingDateEnd + "') / 7) >= 6 AND (TIMESTAMPDIFF(DAY, child.date_birth, '" + reportingDateEnd + "') / 30) <= 18)";
            value[5][0] = executeQuery(query);

            //Denominator - Number of HIV exposed children 6 weeks � 18 months of age sampled in review period
            query = "SELECT COUNT(DISTINCT childfollowup.child_id) AS count FROM childfollowup JOIN child ON childfollowup.child_id = child.child_id "
                    + "JOIN positives ON child.patient_id_mother = positives.patient_id "
                    + "WHERE childfollowup.date_visit >= '" + reportingDateBegin + "' AND childfollowup.date_visit <= '" + reportingDateEnd + "' "
                    + "AND ((TIMESTAMPDIFF(DAY, child.date_birth, '" + reportingDateEnd + "') / 7) >= 6 AND (TIMESTAMPDIFF(DAY, child.date_birth, '" + reportingDateEnd + "') / 30) <= 18)";
            value[5][1] = executeQuery(query);

            //Percentage of newly diagnosed HIV positive pregnant women who initiated ARV within 1 month of HIV diagnosis
            //Numerator - Number of newly diagnosed positive pregnant women in the sample who initiated ARV within one month of diagnosis
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN anc ON patient.patient_id = anc.patient_id "
                    + "JOIN delivery ON patient.patient_id = delivery.patient_id "
                    + "JOIN positives ON patient.patient_id = positives.patient_id "
                    + "WHERE ((anc.date_confirmed_hiv >= '" + reportingDateBegin + "' AND anc.date_confirmed_hiv <= '" + reportingDateEnd + "') "
                    + "OR (delivery.date_confirmed_hiv >= '" + reportingDateBegin + "' AND delivery.date_confirmed_hiv <= '" + reportingDateEnd + "')) "
                    + "AND ((TIMESTAMPDIFF(DAY, anc.date_confirmed_hiv, anc.date_arv_regimen_current) / 30) <= 1 "
                    + "OR (TIMESTAMPDIFF(DAY, delivery.date_confirmed_hiv, delivery.date_arv_regimen_current) / 30) <= 1)";
            value[6][0] = executeQuery(query);

            //Denominator - Number of newly diagnosed positive pregnant women in the sample
            query = "SELECT COUNT(DISTINCT patient.patient_id) AS count FROM patient JOIN anc ON patient.patient_id = anc.patient_id "
                    + "JOIN delivery ON patient.patient_id = delivery.patient_id "
                    + "JOIN positives ON patient.patient_id = positives.patient_id "
                    + "WHERE ((anc.date_confirmed_hiv >= '" + reportingDateBegin + "' AND anc.date_confirmed_hiv <= '" + reportingDateEnd + "') "
                    + "OR (delivery.date_confirmed_hiv >= '" + reportingDateBegin + "' AND delivery.date_confirmed_hiv <= '" + reportingDateEnd + "'))";
            value[6][1] = executeQuery(query);

            //Percentage of HIV positive pregnant women on ARV with an EDD within the review period who delivered in the health facility
            //Numerator - Number of booked HIV positive pregnant women who delivered in the health facility within the review period
            query = "SELECT COUNT(DISTINCT anc.patient_id) AS count FROM anc JOIN delivery ON anc.patient_id = delivery.patient_id "
                    + "JOIN positives ON anc.patient_id = positives.patient_id "
                    + "WHERE anc.arv_regimen_current IN ('ART', 'Prophylaxis (Triple)') "
                    + "AND (anc.edd >= '" + reportingDateBegin + "' AND anc.edd <= '" + reportingDateEnd + "') "
                    + "AND (delivery.date_delivery >= '" + reportingDateBegin + "' AND delivery.date_delivery <= '" + reportingDateEnd + "')";
            value[7][0] = executeQuery(query);

            //Denominator - Total number of booked HIV positive pregnant women on ARV with an EDD within the review period 
            query = "SELECT COUNT(DISTINCT anc.patient_id) AS count FROM anc JOIN delivery ON anc.patient_id = delivery.patient_id "
                    + "JOIN positives ON anc.patient_id = positives.patient_id "
                    + "WHERE anc.arv_regimen_current IN ('ART', 'Prophylaxis (Triple)') "
                    + "AND (anc.edd >= '" + reportingDateBegin + "' AND anc.edd <= '" + reportingDateEnd + "')";
            value[7][1] = executeQuery(query);



            System.out.println("...After end");



        //Populating indicator values
        for(int i = 0; i < indicator.length; i++) {
            DecimalFormat decimalFormat = new DecimalFormat("###.##");
                double d1 = Double.valueOf(value[i][0]);
                double d2 = Double.valueOf(value[i][1]);
                String percentage = d1 > 0.0 ? decimalFormat.format((d1 / d2) * 100) : "0.0";

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("sno", Integer.toString(i + 1));
                map.put("indicator", indicator[i]);
                map.put("percentage", percentage);
                map.put("proportion", value[i][0] + "/" + value[i][1]);
                reportList.add(map);
            }
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return reportList;
    }

    public Map<String, Object> getReportParameters(Long facilityId) {
        Map<String, Object> parameterMap = new HashMap<>();
        ResultSet resultSet;

        try {
            jdbcUtil = new JDBCUtil();
            String query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, " +
                    "facility.phone1, facility.phone2, facility.email, lga.name AS lga, state.name AS state" +
                    " FROM facility JOIN lga ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id " +
                    "= state.state_id WHERE facility_id = " + facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                parameterMap.put("facilityName", resultSet.getString("name"));
                parameterMap.put("lga", resultSet.getString("lga"));
                parameterMap.put("state", resultSet.getString("state"));
            }
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return parameterMap;
    }

    private void executeUpdate(String query) {
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {

            System.out.println("...ExcepUpdate - " + exception);

            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private int executeQuery(String query) {
        int count = 0;
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } 
        catch (Exception exception) {
            System.out.println("...ExcepCount - " + exception);

            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return count;
    }
}
