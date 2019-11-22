/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.report;

import org.fhi360.lamis.report.PcrLabSummaryProcessor;
import org.fhi360.lamis.report.SpecimenReports;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/controller/specimen-report")
public class SpecimenReportAction {
    private final PcrLabSummaryProcessor eidSummaryProcessor;
    private final SpecimenReports specimenReports;

    public SpecimenReportAction(PcrLabSummaryProcessor eidSummaryProcessor, SpecimenReports specimenReports) {
        this.eidSummaryProcessor = eidSummaryProcessor;
        this.specimenReports = specimenReports;
    }

    @GetMapping("/eid-register")
    public ResponseEntity eidRegister(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = specimenReports.eidRegister(dto.getReportingMonth(), dto.getReportingYear(),
                facilityId);
        Map<String, Object> parameterMap = eidSummaryProcessor.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/eid-summary")
    public ResponseEntity eidSummary(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = eidSummaryProcessor.process(dto, facilityId);
        Map<String, Object> parameterMap = eidSummaryProcessor.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

}
