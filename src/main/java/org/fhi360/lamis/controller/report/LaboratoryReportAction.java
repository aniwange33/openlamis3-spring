/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.report;

import io.swagger.annotations.Api;
import org.fhi360.lamis.report.LabSummaryProcessor;
import org.fhi360.lamis.report.LaboratoryReports;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/laboratoryReport")
@Api(tags = "laboratoryReport", description = " ")
public class LaboratoryReportAction {
    private final LabSummaryProcessor labSummaryProcessor;
    private final LaboratoryReports laboratoryReports;

    public LaboratoryReportAction(LabSummaryProcessor labSummaryProcessor, LaboratoryReports laboratoryReports) {
        this.labSummaryProcessor = labSummaryProcessor;
        this.laboratoryReports = laboratoryReports;
    }

    @PostMapping("/labSummary")
    public ResponseEntity labSummary(ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = labSummaryProcessor.process(dto, facilityId);
        Map<String, Object> parameterMap = labSummaryProcessor.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);

    }

    @PostMapping("/labResultQuery")
    public ResponseEntity labResultQuery(@RequestBody ReportParameterDTO dto, @RequestParam String description,
                                         @RequestParam Integer labtestId, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = laboratoryReports.process(dto, description, labtestId, facilityId);
        Map<String, Object> parameterMap = laboratoryReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);

    }
}
