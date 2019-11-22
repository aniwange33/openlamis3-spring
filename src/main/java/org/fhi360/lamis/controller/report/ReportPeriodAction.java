/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller.report;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author user10
 */
@RestController
@RequestMapping("/controller/report-period")
public class ReportPeriodAction {
    private final JdbcTemplate jdbcTemplate;

    public ReportPeriodAction(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/retrieve")
    public ResponseEntity retrieve(@RequestParam String reportType) {
        System.out.println("........... reporting DHIS: " + reportType);
        int length = 8;
        switch (reportType) {
            case "WR":
                length = 7;
                break;
            case "MR":
                length = 6;
        }
        Map<String, String> periodMap = new HashMap<>();
        jdbcTemplate.queryForList("select distinct period from dhisvalue where length(period) = ?",
                String.class, length)
                .forEach(period -> periodMap.put(period, period));
        return ResponseEntity.ok(periodMap);
    }
}
