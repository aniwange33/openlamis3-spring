/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.report;

import org.fhi360.lamis.report.NigeriaqualIndicatorProcessor;
import org.fhi360.lamis.report.PmtctAddendumSummaryProcessor;
import org.fhi360.lamis.report.PmtctSummaryProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/controller/report")
public class PmtctReportAction {
    private final PmtctSummaryProcessor summaryProcessor;
    private final PmtctAddendumSummaryProcessor pmtctAddendumSummaryProcessor;
    private final NigeriaqualIndicatorProcessor nigeriaqualIndicatorProcessor;

    public PmtctReportAction(PmtctSummaryProcessor summaryProcessor,
                             PmtctAddendumSummaryProcessor pmtctAddendumSummaryProcessor,
                             NigeriaqualIndicatorProcessor nigeriaqualIndicatorProcessor) {
        this.summaryProcessor = summaryProcessor;
        this.pmtctAddendumSummaryProcessor = pmtctAddendumSummaryProcessor;
        this.nigeriaqualIndicatorProcessor = nigeriaqualIndicatorProcessor;
    }

    @GetMapping("/pmtct-summary")
    public ResponseEntity pmtctSummary(@RequestParam Integer reportingMonth, @RequestParam Integer reportingYear,
                                       HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("stub", "");
        reportList.add(map);
        Map<String, Object> parameterMap = summaryProcessor.process(reportingMonth, reportingYear, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }


    @GetMapping("pmtct-addendum-summary")
    public ResponseEntity pmtctAddendumSummary(@RequestParam Integer reportingMonth, @RequestParam Integer reportingYear,
                                               HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> reportList = new ArrayList<>();
        map.put("stub", "");
        reportList.add(map);
        Map<String, Object> parameterMap = pmtctAddendumSummaryProcessor.process(reportingMonth, reportingYear, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }


    @GetMapping("nigeria-qual-indicators")
    public ResponseEntity nigeriaqualIndicators(@RequestParam String reportingMonthBegin,
                                                @RequestParam String reportingMonthEnd,
                                                @RequestParam Integer reportingYearBegin,
                                                @RequestParam Integer reportingYearEnd,
                                                HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = nigeriaqualIndicatorProcessor.process(reportingMonthBegin,
                reportingMonthEnd, reportingYearBegin, reportingYearEnd, facilityId);
        Map<String, Object> parameterMap = nigeriaqualIndicatorProcessor.getReportParameters(facilityId);
        parameterMap.put("reportingPeriodBegin", reportingMonthBegin + " " + reportingYearBegin);
        parameterMap.put("reportingPeriodEnd", reportingMonthEnd + " " + reportingYearEnd);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }
}
