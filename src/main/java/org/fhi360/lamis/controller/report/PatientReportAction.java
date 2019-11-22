/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.report;

import org.fhi360.lamis.report.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/controller/report")
public class PatientReportAction {
    private final PatientReports patientReports;
    private final ArtSummaryProcessor artSummaryProcessor;
    private final UnbundledArtSummaryProcessor unbundledArtSummaryProcessor;
    private final TxMlSummaryProcessor txMlSummaryProcessor;
    private final ArtAddendumSummaryProcessor artAddendumSummaryProcessor;
    private final ServiceSummaryProcessor serviceSummaryProcessor;
    private final QualityIndicatorProcessor qualityIndicatorProcessor;
    private final CohortAnalysisProcessor cohortAnalysisProcessor;
    private final PerformanceIndicatorProcessor performanceIndicatorProcessor;


    public PatientReportAction(PatientReports patientReports, ArtSummaryProcessor artSummaryProcessor,
                               UnbundledArtSummaryProcessor unbundledArtSummaryProcessor,
                               TxMlSummaryProcessor txMlSummaryProcessor, ArtAddendumSummaryProcessor artAddendumSummaryProcessor,
                               ServiceSummaryProcessor serviceSummaryProcessor, QualityIndicatorProcessor qualityIndicatorProcessor,
                               CohortAnalysisProcessor cohortAnalysisProcessor, PerformanceIndicatorProcessor performanceIndicatorProcessor) {
        this.patientReports = patientReports;
        this.artSummaryProcessor = artSummaryProcessor;
        this.unbundledArtSummaryProcessor = unbundledArtSummaryProcessor;
        this.txMlSummaryProcessor = txMlSummaryProcessor;
        this.artAddendumSummaryProcessor = artAddendumSummaryProcessor;
        this.serviceSummaryProcessor = serviceSummaryProcessor;
        this.qualityIndicatorProcessor = qualityIndicatorProcessor;
        this.cohortAnalysisProcessor = cohortAnalysisProcessor;
        this.performanceIndicatorProcessor = performanceIndicatorProcessor;
    }

    @PostMapping("/list-of-patients")
    public ResponseEntity listOfPatients(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.listOfPatients(dto, facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/list-of-patients-notification")
    public ResponseEntity listOfPatientsNotification(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.listOfPatientsNotification(dto, facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/casemanager-clients-list")
    public ResponseEntity caseManagerClientsList(@RequestBody ReportParameterDTO dto, @RequestParam Long casemagerId, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.caseManagerClientsList(casemagerId, facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/current-on-care")
    public ResponseEntity currentOnCare(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.currentOnCare(facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/current-on-treatment")
    public ResponseEntity currentOnTreatment(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.currentOnTreatment(facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/appointment")
    public ResponseEntity appointment(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.appointment(dto.getReportType(), dto.getReportingDateBegin(),
                dto.getReportingDateEnd(), facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/client-appointment")
    public ResponseEntity clientAppointment(@RequestBody ReportParameterDTO dto, @RequestParam Long caseManagerId,
                                            HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.clientAppointment(caseManagerId, dto.getReportingDateBegin(),
                dto.getReportingDateEnd(), facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/visit")
    public ResponseEntity visit(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.visit(dto.getReportType(), dto.getReportingDateBegin(),
                dto.getReportingDateEnd(), facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/defaulters")
    public ResponseEntity defaulters(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.defaulters(dto.getReportType(), dto.getReportingDateBegin(),
                dto.getReportingDateEnd(), facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/client-defaulter-refill")
    public ResponseEntity clientDefaulterRefill(@RequestBody ReportParameterDTO dto, @RequestParam Long casemanagerId,
                                                HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.clientDefaulterRefill(casemanagerId, dto.getReportingDateBegin(),
                dto.getReportingDateEnd(), dto.getReportType(), facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/lost-unconfirmed-pepfar")
    public ResponseEntity lostUnconfirmedPepfar(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.lostUnconfirmedPEPFAR(facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/lost-unconfirmed-gon")
    public ResponseEntity lostUnconfirmedGon(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.lostUnconfirmedGON(facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/defaulter-refill")
    public ResponseEntity defaulterRefill(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.defaulterRefill(facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/list-of-txml")
    public ResponseEntity listOfTxml(@RequestBody ReportParameterDTO dto, @RequestParam String outcome, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = patientReports.trackingOutcome(outcome, facilityId);
        Map<String, Object> parameterMap = patientReports.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/art-summary")
    public ResponseEntity artSummary(@RequestParam String reportingMonth, @RequestParam String reportingYear,
                                     HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> reportList = new ArrayList<>();
        map.put("stub", "");
        reportList.add(map);
        Map<String, Object> parameterMap = artSummaryProcessor.process(reportingMonth, reportingYear, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("parameterMap", parameterMap);
        result.put("reportList", reportList);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/unbundled-art-summary")
    public ResponseEntity unbundledArtSummary(@RequestParam String reportingMonth, @RequestParam String reportingYear,
                                              HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> reportList = new ArrayList<>();
        map.put("stub", "");
        reportList.add(map);
        Map<String, Object> parameterMap = unbundledArtSummaryProcessor.process(reportingMonth, reportingYear, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("parameterMap", parameterMap);
        result.put("reportList", reportList);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/txml-summary")
    public ResponseEntity txmlSummary(@RequestParam String reportingMonth, @RequestParam String reportingYear,
                                      HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> reportList = new ArrayList<>();
        map.put("stub", "");
        reportList.add(map);
        Map<String, Object> parameterMap = txMlSummaryProcessor.process(reportingMonth, reportingYear, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("parameterMap", parameterMap);
        result.put("reportList", reportList);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/art-addendum-summary")
    public ResponseEntity artAddendumSummary(@RequestParam String reportingMonth, @RequestParam String reportingYear,
                                             HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> reportList = new ArrayList<>();
        map.put("stub", "");
        reportList.add(map);
        Map<String, Object> parameterMap = artAddendumSummaryProcessor.process(reportingMonth, reportingYear, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("parameterMap", parameterMap);
        result.put("reportList", reportList);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/service-summary")
    public ResponseEntity serviceSummary(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = serviceSummaryProcessor.process(dto, facilityId);
        Map<String, Object> parameterMap = serviceSummaryProcessor.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/quality-indicator")
    public ResponseEntity qualityIndicator(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = qualityIndicatorProcessor.process(dto, facilityId);
        Map<String, Object> parameterMap = qualityIndicatorProcessor.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cohort-analysis")
    public ResponseEntity cohortAnalysis(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = cohortAnalysisProcessor.process(dto, facilityId, session);
        Map<String, Object> parameterMap = cohortAnalysisProcessor.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/performance-indicators")
    public ResponseEntity performanceIndicators(@RequestBody ReportParameterDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        List<Map<String, Object>> reportList = performanceIndicatorProcessor.process(dto, facilityId);
        Map<String, Object> parameterMap = performanceIndicatorProcessor.getReportParameters(dto, facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("reportList", reportList);
        result.put("parameterMap", parameterMap);
        return ResponseEntity.ok(result);
    }
}
