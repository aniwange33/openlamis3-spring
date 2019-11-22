/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.fhi360.lamis.controller.mapstruct.LabTestMapper;
import org.fhi360.lamis.controller.mapstruct.LaboratoryMapper;
import org.fhi360.lamis.model.LabTest;
import org.fhi360.lamis.model.Laboratory;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.LabTestDTO;
import org.fhi360.lamis.model.dto.LaboratoryDTO;
import org.fhi360.lamis.model.repositories.LaboratoryRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author user10
 */

@RestController
@RequestMapping(value = "/api/laboratory")
@Api(tags = "Laboratory", description = " ")
@Slf4j
public class LaboratoryController {

    private final LaboratoryMapper laboratoryMapper;
    private final LaboratoryRepository laboratoryRepository;
    private final LabTestMapper labTestMapper;
    private final PatientRepository patientRepository;

    public LaboratoryController(LaboratoryMapper laboratoryMapper, LaboratoryRepository laboratoryRepository, LabTestMapper labTestMapper, PatientRepository patientRepository) {
        this.laboratoryMapper = laboratoryMapper;
        this.laboratoryRepository = laboratoryRepository;
        this.labTestMapper = labTestMapper;
        this.patientRepository = patientRepository;
    }

    @GetMapping("/grid")
    public ResponseEntity laboratoryGrid(@RequestParam(required = false, value = "page", defaultValue = "0") Integer rows,
                                         @RequestParam(required = false) Long patientId,
                                         @RequestParam(required = false, value = "rows", defaultValue = "50") Integer page
    ) {
        if (page < 0) {
            page++;
        }
        if (rows < 1) {
            rows = 100;
        }
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        List<Laboratory> laboratories = laboratoryRepository.findByPatient(patient, PageRequest.of(page, rows));
        List<LabTest> labTests = laboratories.stream().map(Laboratory::getLabTest).collect(Collectors.toList());
        List<LaboratoryDTO> laboratoryArrayList = laboratoryMapper.laboratoryToDto(laboratories);
        List<LabTestDTO> labDtoArrayList = labTestMapper.latTestToDto(labTests);
        Map<String, Object> response = new HashMap<>();
        response.put("laboratoryList", laboratoryArrayList);
        response.put("labresultList", labDtoArrayList);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/find")
    public ResponseEntity find(@RequestParam Long patientId, @RequestParam LocalDate dateReported) {
        Patient patient = patientRepository.getOne(patientId);
        List<Laboratory> laboratories = laboratoryRepository.findByPatientAndDateReported(patient, dateReported);
        Map<String, Object> response = new HashMap<>();
        response.put("laboratoryList", laboratories);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PutMapping
    public ResponseEntity saveLaboratory(@RequestBody LaboratoryDTO dto) {
        Laboratory laboratory = laboratoryMapper.dtoToLaboratory(dto);
        laboratoryRepository.save(laboratory);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLaboratory(@PathVariable Long id) {
        laboratoryRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getLaboratory(@PathVariable Long id) {
        Laboratory laboratory = laboratoryRepository.getOne(id);
        LaboratoryDTO dto = laboratoryMapper.laboratoryToDto(laboratory);
        LOG.info("Laboratory DTO1: {}", dto);
        Map<String, Object> response = new HashMap<>();
        response.put("laboratoryList", Collections.singletonList(dto));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity getByPatient(@PathVariable Long id) {
        Patient patient = patientRepository.getOne(id);
        List<Laboratory> laboratories = laboratoryRepository.findByPatient(patient);
        List<LaboratoryDTO> dtos = laboratoryMapper.laboratoryToDto(laboratories);
        Map<String, Object> response = new HashMap<>();
        response.put("laboratoryList", dtos);
        return ResponseEntity.ok(response);
    }
}
