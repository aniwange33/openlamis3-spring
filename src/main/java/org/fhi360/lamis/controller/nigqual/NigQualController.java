package org.fhi360.lamis.controller.nigqual;

import io.swagger.annotations.Api;
import org.fhi360.lamis.exchange.nigqual.*;
import org.fhi360.lamis.model.dto.DataDTO;
import org.fhi360.lamis.model.dto.NigqualServiceDTO;
import org.fhi360.lamis.model.dto.PatientDemographyDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/nigQual")
@Api(tags = "NigQual", description = " ")
public class NigQualController {
    private final AdultDataConverter adultDataConverter;
    private final NigQualService nigQualService;
    private final PatientDemographyConverter patientDemographyConverter;
    private final PediatricsDataConverter pediatricsDataConverter;
    private  final PmtctDataConverter pmtctDataConverter;

    public NigQualController(AdultDataConverter adultDataConverter, NigQualService nigQualService, PatientDemographyConverter patientDemographyConverter, PediatricsDataConverter pediatricsDataConverter, PmtctDataConverter pmtctDataConverter) {
        this.adultDataConverter = adultDataConverter;
        this.nigQualService = nigQualService;
        this.patientDemographyConverter = patientDemographyConverter;
        this.pediatricsDataConverter = pediatricsDataConverter;
        this.pmtctDataConverter = pmtctDataConverter;
    }

    @PostMapping("/adult-ata-converter")
    public ResponseEntity<?> adultDataConverter(@RequestBody DataDTO dataDTO, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        adultDataConverter.convertXml(dataDTO, facilityId);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/generate-cohort")
    public ResponseEntity<?> generateCohort(@RequestBody NigqualServiceDTO nigqualServiceDTO, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        nigQualService.generateCohort(nigqualServiceDTO, facilityId);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/patient-demography")
    public ResponseEntity<?> patientDemographyConverter(@RequestBody PatientDemographyDTO patientDemographyDTO, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        patientDemographyConverter.convertXml(patientDemographyDTO, facilityId);
        return ResponseEntity.ok().body(null);

    }

    @PostMapping("/pediatric-data")
    public ResponseEntity<?> pediatricDataConverter(@RequestBody DataDTO dataDTO, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        pediatricsDataConverter.convertXml(dataDTO, facilityId);
        return ResponseEntity.ok().body(null);

    }
    @PostMapping("/pmtct-data")
    public ResponseEntity<?> pmtctDataConverter(@RequestBody DataDTO dataDTO, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        pmtctDataConverter.convertXml(dataDTO, facilityId);
        return ResponseEntity.ok().body(null);

    }
}
