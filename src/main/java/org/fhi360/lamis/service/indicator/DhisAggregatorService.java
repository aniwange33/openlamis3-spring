/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.indicator;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.utility.DateUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class DhisAggregatorService {
    private final JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);

    public DhisAggregatorService() {
    }

    public void process(long facilityId, Date reportingDate) {

        String reportDate = DateUtil.parseDateToString(reportingDate, "yyyy-MM-dd");
        //Loop through dhiscodeset
        String query = "SELECT lamis_id, code_set_nm FROM dhiscodeset " +
                "WHERE SUBSTRING(code_set_nm,1,12) = 'DATA ELEMENT' ORDER BY lamis_id";
        jdbcTemplate.query(query, rs -> {
            long lamisId = rs.getLong("lamis_id");
            String codeSetNm = rs.getString("code_set_nm");

            if (codeSetNm.trim().equalsIgnoreCase("DATA ELEMENT DR")) {
                computeIndicatorValue(facilityId, lamisId, reportDate, "DR");
            } else if (codeSetNm.trim().equalsIgnoreCase("DATA ELEMENT WR"))
                computeIndicatorValue(facilityId, lamisId, reportDate, "WR");
        });
    }


    private void computeIndicatorValue(long facilityId, long dataElementId, String reportDate, String reportingPeriod) {
        String cummulativeData = "407,408,409,411";
        IndicatorPersister indicatorPersister = ContextProvider.getBean(IndicatorPersister.class);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date inputDate = null;
        try {
            inputDate = dateFormat.parse(reportDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final long[] stateId = {0};
        final long[] lgaId = {0};
        final long[] categoryId = {0};
        final int[] value = {0};


        String query = "SELECT state_id, lga_id FROM facility  WHERE facility_id = " + facilityId;
        jdbcTemplate.query(query, rs -> {
            stateId[0] = rs.getLong("state_id");
            lgaId[0] = rs.getLong("lga_id");
        });

        int reportingDay = DateUtil.getDay(inputDate);
        int reportingMonth = DateUtil.getMonth(inputDate) + 1;
        int reportingYear = DateUtil.getYear(inputDate);
        int reportingWeek = DateUtil.getWeekYear(inputDate);

        String weekFirstDate = dateFormat.format(DateUtil.getWeekStartDate(inputDate));
        String weekLastDate = dateFormat.format(DateUtil.getWeekEndDate(inputDate));

        String strDay = String.valueOf(reportingDay);
        String strMonth = String.valueOf(reportingMonth);
        String strWeek = String.valueOf(reportingWeek);

        if (reportingDay < 10) {
            strDay = "0" + reportingDay;
        }
        if (reportingMonth < 10) {
            strMonth = "0" + reportingMonth;
        }
        if (reportingWeek < 10) {
            strWeek = "0" + reportingWeek;
        }

        strDay = reportingYear + strMonth + strDay;
        strWeek = reportingYear + "W" + strWeek;

        if (reportingPeriod.equalsIgnoreCase("DR")) {
            //populate dhisvalue table for daily
            java.sql.Date reportingDate = java.sql.Date.valueOf(reportDate);

            query = "SELECT SUM(value) AS total, category_id FROM indicatorvalue WHERE facility_id = " + facilityId +
                    " AND data_element_id =" + dataElementId + " AND report_date = '" + reportingDate + "' " +
                    "GROUP BY category_id";
            String finalStrDay = strDay;
            jdbcTemplate.query(query, rs -> {
                categoryId[0] = rs.getLong("category_id");
                value[0] = rs.getInt("total");
                indicatorPersister.persistDhis(dataElementId, categoryId[0], stateId[0], lgaId[0],
                        facilityId, finalStrDay, value[0], reportingPeriod);
            });
        } else {
            //populate dhisvalue table for weekly
            System.out.println("Week of the year: " + strWeek);
            java.sql.Date reportingDate = java.sql.Date.valueOf(reportDate);
            java.sql.Date reportingDateBegin = java.sql.Date.valueOf(weekFirstDate);
            java.sql.Date reportingDateEnd = java.sql.Date.valueOf(weekLastDate);

            if (cummulativeData.contains(dataElementId + "")) {
                query = "SELECT SUM(value) AS total, category_id FROM indicatorvalue WHERE facility_id = " + facilityId +
                        " AND data_element_id =" + dataElementId + " AND report_date = '" + reportingDate + "' GROUP BY category_id";
            } else {
                query = "SELECT SUM(value) AS total, category_id FROM indicatorvalue WHERE facility_id = " + facilityId +
                        " AND data_element_id =" + dataElementId + " AND report_date BETWEEN '" + reportingDateBegin + "' " +
                        "AND '" + reportingDateEnd + "' GROUP BY category_id";
            }

            String finalStrWeek = strWeek;
            jdbcTemplate.query(query, rs -> {
                categoryId[0] = rs.getLong("category_id");
                value[0] = rs.getInt("total");
                indicatorPersister.persistDhis(dataElementId, categoryId[0], stateId[0], lgaId[0],
                        facilityId, finalStrWeek, value[0], reportingPeriod);
            });
        }

//          reportingDateBegin = java.sql.Date.valueOf(monthFirstDate);
//            reportingDateEnd = java.sql.Date.valueOf(monthLastDate);
//            query = "SELECT SUM(value) AS total, category_id FROM indicatorvalue WHERE data_element_id =" + dataElementId + " AND report_date BETWEEN '" + reportingDateBegin + "' AND '" + reportingDateEnd + "' GROUP BY category_id";
//
//            System.out.println("Mothly Report Computation: " + query);
//            preparedStatement = jdbcUtil.getStatement(query);
//            resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                categoryId = resultSet.getLong("category_id");
//                value = resultSet.getInt("total");
//                System.out.println("Monthly Report");
//                indicatorPersister.persistDhis(dataElementId, categoryId, stateId, lgaId, id, strMonth, value);
//            }

    }
}
