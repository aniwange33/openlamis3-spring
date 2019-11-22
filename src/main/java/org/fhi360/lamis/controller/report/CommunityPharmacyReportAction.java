/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller.report;

import io.swagger.annotations.Api;
import org.fhi360.lamis.report.CommunityPharmacySummaryProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/communitypharmReport")
@Api(tags = "CommunitypharmReport", description = " ")
public class CommunityPharmacyReportAction {

    private final CommunityPharmacySummaryProcessor communityPharmacySummaryProcessor;

    public CommunityPharmacyReportAction(CommunityPharmacySummaryProcessor communityPharmacySummaryProcessor) {
        this.communityPharmacySummaryProcessor = communityPharmacySummaryProcessor;
    }

    @PostMapping("/communitypharmSummary")
    public ResponseEntity communitypharmSummary(@RequestBody ReportParameterDTO dto, @RequestParam Long communitypharmId,
                                                HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = communityPharmacySummaryProcessor.process(dto.getReportingMonth(),
                dto.getReportingYear(), communitypharmId, facilityId);
        Map<String, Object> parameterMap = communityPharmacySummaryProcessor.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }
}
