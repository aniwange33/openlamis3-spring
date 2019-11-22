/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.report;

import io.swagger.annotations.Api;
import org.fhi360.lamis.report.ChroniccareSummaryProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chronicCareReport")
@Api(tags = "chronicCareReport", description = " ")
public class ChroniccareReportAction {
    @PostMapping("/chronicCareSummary")
    public ResponseEntity<?> chronicCareSummary(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> reportList = new ArrayList<>();
        map.put("stub", "");
        Long facilityId = (Long) session.getAttribute("id");
        Map<String, Object> parameterMap = new ChroniccareSummaryProcessor().getReportParameters(dto, facilityId);
        map.put("stub", "");
        map.put("reportList", reportList);
        map.put("parameterMap", parameterMap);
        reportList.add(map);
        return ResponseEntity.ok(reportList);
    }
}
