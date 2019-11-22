/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.fhi360.lamis.controller.mapstruct.AncMapper;
import org.fhi360.lamis.model.Anc;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.PartnerInformation;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.AncDTO;
import org.fhi360.lamis.model.repositories.*;
import org.fhi360.lamis.security.SecurityUtils;
import org.fhi360.lamis.service.MonitorService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/anc")
@Api(tags = "ANC", description = " ")
public class AncController {
    private final FacilityRepository facilityRepository;
    private final PatientRepository patientRepository;
    private final AncRepository ancRepository;
    private final AncMapper ancMapper;
    private final PartnerInformationRepository partnerInformationRepository;
    private final MotherInformationRepository motherInformationRepository;

    public AncController(FacilityRepository facilityRepository, PatientRepository patientRepository,
                         AncRepository ancRepository, AncMapper ancMapper,
                         PartnerInformationRepository partnerInformationRepository,
                         MotherInformationRepository motherInformationRepository) {
        this.facilityRepository = facilityRepository;
        this.patientRepository = patientRepository;
        this.ancRepository = ancRepository;
        this.ancMapper = ancMapper;
        this.partnerInformationRepository = partnerInformationRepository;
        this.motherInformationRepository = motherInformationRepository;
    }

    @PostMapping
    @PutMapping
    public ResponseEntity saveAnc(@RequestBody AncDTO dto) throws URISyntaxException {
        Anc anc = ancMapper.dtoToAnc(dto);
        ancRepository.save(anc);
        PartnerInformation partnerInformation = ancMapper.dtoToPartnerInformation(dto);
        partnerInformation.setFacility(anc.getFacility());
        if (partnerInformation.getPartnerNotification() != null) {
            partnerInformationRepository.save(partnerInformation);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAnc(@PathVariable("id") Long ancId) {
        Anc anc = ancRepository.getOne(ancId);
        String dateVisit = anc.getDateVisit().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        MonitorService.logEntity(anc.getPatient().getHospitalNum() + "#" + dateVisit, "anc", 3);
        ancRepository.delete(anc);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/first-visit/patient/{id}")
    public ResponseEntity retrieveFirstAncList(@PathVariable("id") Long patientId) {
        Patient patient = patientRepository.getOne(patientId);
        Anc anc = ancRepository.findFirstByPatientOrderByDateVisit(patient);
        AncDTO ancDTO = ancMapper.ancToDto(anc);
        return ResponseEntity.ok(ancDTO);
    }

    @GetMapping("/last-visit/patient/{id}")
    public ResponseEntity retrieveLastAncList(@PathVariable("id") Long patientId) {
        Patient patient = patientRepository.getOne(patientId);
        Anc anc = ancRepository.findFirstByPatientOrderByDateVisitDesc(patient);
        AncDTO ancDTO = ancMapper.ancToDto(anc);
        return ResponseEntity.ok(ancDTO);
    }

    @GetMapping("/partner-information")
    public ResponseEntity retrievePartnerInformationList() {
        Long facilityId = SecurityUtils.getCurrentFacility().getId();
        Facility facility = facilityRepository.getOne(facilityId);
        List<PartnerInformation> partnerInformations = partnerInformationRepository.findByFacility(facility);
        Map<String, Object> response = new HashMap<>();
        response.put("partnerinformationList", partnerInformations);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find")
    public ResponseEntity findAnc(@RequestParam("id") Long patientId, @RequestParam("dateVisit") LocalDate date) {
        Patient patient = patientRepository.getOne(patientId);
        List<Anc> ancs = ancRepository.findByPatientAndDateVisit(patient, date);
        Map<String, Object> response = new HashMap<>();
        response.put("ancList", ancs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mother-list")
    public ResponseEntity retrieveMotherList(HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        Facility facility = facilityRepository.getOne(facilityId);
        return ResponseEntity.ok(partnerInformationRepository.findByFacility(facility));
    }

    @GetMapping("/retrieveMotherListForChild")
    public ResponseEntity retrieveMotherListForChild(@RequestParam("motherId") Long motherId) {
        return ResponseEntity.ok(Collections.singletonList(motherInformationRepository.getOne(motherId)));
    }

    @GetMapping("/grid")
    public ResponseEntity<?> ancGrid(@RequestParam(value = "rows", defaultValue = "100") Integer rows, @RequestParam("page") Integer page) {
        Long facilityId = SecurityUtils.getCurrentFacility().getId();
        if (page > 0)
            page--;
        Facility facility = facilityRepository.getOne(facilityId);
        List<Anc> ancs = ancRepository.findByFacility(facility, PageRequest.of(page, rows));
        Map<String, Object> response = new HashMap<>();
        Long count = ancRepository.countByFacility(facility);
        response.put("ancList", ancs);
        response.put("currpage", page);
        response.put("totalrecords", count);
        response.put("totalpages", count / rows);
        return ResponseEntity.ok(response);
    }
}
