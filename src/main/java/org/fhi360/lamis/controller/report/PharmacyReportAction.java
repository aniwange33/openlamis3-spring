/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.report;

import org.fhi360.lamis.report.PatientReports;
import org.fhi360.lamis.report.PharmacySummaryProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/controller/report/pharmacy")
public class PharmacyReportAction {
    private final PatientReports patientReports;
    private final PharmacySummaryProcessor pharmacySummaryProcessor;

    public PharmacyReportAction(PatientReports patientReports, PharmacySummaryProcessor pharmacySummaryProcessor) {
        this.patientReports = patientReports;
        this.pharmacySummaryProcessor = pharmacySummaryProcessor;
    }

    @PostMapping("/patients-first-line")
    public ResponseEntity patientsFirstLine(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.patientsRegimen("first", facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/patients-second-line")
    public ResponseEntity patientsSecondLine(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.patientsRegimen("second", facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/patients-third")
    public ResponseEntity patientsThird(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.patientsRegimen("third", facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/regimen-summary")
    public ResponseEntity regimenSummary(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.regimenSummary(facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/devolved-summary")
    public ResponseEntity devolvedSummary(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.devolvedSummary(facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/dispensed-summary")
    public ResponseEntity dispensedSummary(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = pharmacySummaryProcessor.process(dto, facilityId);
        Map<String, Object> parameterMap = pharmacySummaryProcessor.getReportParameters(dto.getReportingMonth(),
                dto.getReportingYear(), facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }
}
