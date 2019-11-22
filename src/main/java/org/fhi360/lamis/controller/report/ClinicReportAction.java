package org.fhi360.lamis.controller.report;

import io.swagger.annotations.Api;
import org.fhi360.lamis.report.ClinicReports;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clinicReport")
@Api(tags = "ClinicReport", description = " ")
public class ClinicReportAction {
    private final ClinicReports clinicReports;

    public ClinicReportAction(ClinicReports clinicReports) {
        this.clinicReports = clinicReports;
    }

    @PostMapping("/eligible-for-art")
    public ResponseEntity eligibleForART(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = clinicReports.eligibleForART(facilityId);
        Map<String, Object> parameterMap = clinicReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);

    }

    @PostMapping("/cd4Due")
    public ResponseEntity cd4Due(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = clinicReports.cd4Due(facilityId);
        Map<String, Object> parameterMap = clinicReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/clientsCd4Due")
    public ResponseEntity clientsCd4Due(@RequestBody ReportParameterDTO dto, @RequestParam String casemanagerId,
                                        HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = clinicReports.clientsCd4Due(facilityId, casemanagerId);
        Map<String, Object> parameterMap = clinicReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cd4LessBaseline")
    public ResponseEntity cd4LessBaseline(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = clinicReports.cd4LessBaseline(facilityId);
        Map<String, Object> parameterMap = clinicReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/viralLoadDue")
    public ResponseEntity viralLoadDue(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = clinicReports.viralLoadDue(facilityId);
        Map<String, Object> parameterMap = clinicReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/unassignedClients")
    public ResponseEntity unassignedClients(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = clinicReports.unassignedClients(facilityId);
        Map<String, Object> parameterMap = clinicReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/clientsViralLoadDue")
    public ResponseEntity clientsViralLoadDue(@RequestBody ReportParameterDTO dto, HttpSession session, @RequestParam String caseMangerId) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = clinicReports.clientsViralLoadDue(facilityId, caseMangerId);
        Map<String, Object> parameterMap = clinicReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/baselineViralLoadDue")
    public ResponseEntity baselineViralLoadDue(@RequestBody ReportParameterDTO dto, HttpSession session) {
        System.out.println("Got Here!!!");
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = clinicReports.baselineViralLoadDue(facilityId);
        Map<String, Object> parameterMap = clinicReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/secondViralLoadDue")
    public ResponseEntity secondViralLoadDue(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = clinicReports.secondViralLoadDue(facilityId);
        Map<String, Object> parameterMap = clinicReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/routineViralLoadDue")
    public ResponseEntity routineViralLoadDue(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = clinicReports.routineViralLoadDue(facilityId);
        Map<String, Object> parameterMap = clinicReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/repeatViralLoadDue")
    public ResponseEntity repeatViralLoadDue(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = clinicReports.repeatViralLoadDue(facilityId);
        Map<String, Object> parameterMap = clinicReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/viralLoadSupressed")
    public ResponseEntity viralLoadSupressed(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = clinicReports.viralLoadSupressed(facilityId);
        Map<String, Object> parameterMap = clinicReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/viralLoadUnsupressed")
    public ResponseEntity viralLoadUnsupressed(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = clinicReports.viralLoadUnsupressed(facilityId);
        Map<String, Object> parameterMap = clinicReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }
}
