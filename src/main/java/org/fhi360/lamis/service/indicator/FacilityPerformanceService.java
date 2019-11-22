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
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author user10
 */
@Component
public class FacilityPerformanceService {
    private final static JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);
    private final DhisIndicatorService dhisIndicatorService;
    private static ExecutorService executorService = Executors.newFixedThreadPool(30);
    private final String START = "2019-05-01";

    public FacilityPerformanceService(DhisIndicatorService dhisIndicatorService) {
        this.dhisIndicatorService = dhisIndicatorService;
    }

    public void process() {
        //Select all id that has uploaded data in the last 7 days and run performance analysis
        String query = "SELECT DISTINCT facility_id FROM patient WHERE time_stamp BETWEEN DATE_ADD(CURDATE(), " +
                "INTERVAL -14 DAY) AND CURDATE() UNION SELECT DISTINCT facility_id FROM clinic WHERE " +
                "time_stamp BETWEEN DATE_ADD(CURDATE(), INTERVAL -14 DAY) AND CURDATE() UNION SELECT " +
                "DISTINCT facility_id FROM pharmacy WHERE time_stamp BETWEEN DATE_ADD(CURDATE(), INTERVAL -14 DAY) " +
                "AND CURDATE() UNION SELECT DISTINCT facility_id FROM laboratory WHERE time_stamp BETWEEN " +
                "DATE_ADD(CURDATE(), INTERVAL -14 DAY) AND CURDATE() UNION SELECT DISTINCT facility_id FROM " +
                "statushistory WHERE time_stamp BETWEEN DATE_ADD(CURDATE(), INTERVAL -14 DAY) AND CURDATE()";


        List<Long> facilityIds = new ArrayList<>();
        jdbcTemplate.query(query, resultSet -> {
            facilityIds.add(resultSet.getLong("facility_id"));
        });

        for (Long facilityId : facilityIds) {

            //For every facility generate a db suffix for temporary tables
            String dbSuffix = org.apache.commons.lang.RandomStringUtils.randomAlphabetic(6);
            dbSuffix = dbSuffix + "_" + facilityId;

            ProcessorThread processorThread = new ProcessorThread(facilityId, dbSuffix);
            executorService.execute(processorThread);
        }
    }


    public void process(long facilityId, Date reportingDate, String dbSuffix) {
        try {
            System.out.println("Performance Report Started @ " + new Timestamp(new Date().getTime()) + " for facility --- " + facilityId);

            //new ArtIndicatorService().process(id, reportingDate);
            //new HtsIndicatorService().process(id, reportingDate);
            dhisIndicatorService.process(facilityId, reportingDate, dbSuffix);
            new DhisAggregatorService().process(facilityId, reportingDate);

            System.out.println("Performance Report Completed @ " + new Timestamp(new Date().getTime()) + " for facility --- " + facilityId);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    class ProcessorThread implements Runnable {
        private Long facilityId;
        private String dbSuffix;

        ProcessorThread(Long facilityId, String dbSuffix) {
            this.facilityId = facilityId;
            this.dbSuffix = dbSuffix;
        }

        @Override
        public void run() {
            String query = "SELECT COALESCE(MAX(report_date), ?) AS report_date FROM indicatorvalue where facility_id = ?";
            Date start = jdbcTemplate.queryForObject(query, Date.class, START, facilityId);

            Date today = new Date();
            Date dateStart = convertToDate(LocalDate.of(2019,6, 28));
            Date end = convertToDate(LocalDate.of(2019,4, 1));
            if (start.after(dateStart)) {
                end = convertToDate(LocalDate.now().minusDays(7));
            }

            /*Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 8);
            Date today = calendar.getTime();*/
            boolean processing = facilityProcessing(facilityId);
            if (!processing) {
                processFacility(facilityId, true);
                while (end.before(today)) {
                    process(facilityId, today, dbSuffix);
                    //start = DateUtil.addDay(start, -1);
                    today = DateUtil.addDay(today, -1);
                }
                processFacility(facilityId, false);
            }
        }
    }

    private boolean facilityProcessing(Long facilityId) {
        return jdbcTemplate.queryForObject("select processing from facility where facility_id = ?",
                Boolean.class, facilityId);
    }

    private void processFacility(Long facilityId, boolean processing) {
        ContextProvider.getBean(TransactionTemplate.class).execute(status -> {
            jdbcTemplate.update("update facility set processing = ? where facility_id = ?",
                    processing, facilityId);
            return null;
        });
    }

    static {
        ContextProvider.getBean(TransactionTemplate.class).execute(status -> {
            jdbcTemplate.execute("update facility set processing = 0 where processing  = 1");
            return null;
        });
    }

    public Date convertToDate(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public LocalDate convertToLocalDate(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
