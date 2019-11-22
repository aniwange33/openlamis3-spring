/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.fhi360.lamis.controller.mapstruct.ClinicMapper;
import org.fhi360.lamis.model.Clinic;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.ClinicDTO;
import org.fhi360.lamis.model.dto.ServerResponse;
import org.fhi360.lamis.model.repositories.ClinicRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.service.impl.ClinicService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Idris
 */
@RestController
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RequestMapping(value = "/api/clinic")
@Api(tags = "Clinic", description = " ")
@Slf4j
public class ClinicController {
    private final ClinicService clinicService;
    private final PatientRepository patientRepository;
    private final ClinicRepository clinicRepository;
    private final ClinicMapper clinicMapper;

    public ClinicController(ClinicService clinicService, PatientRepository patientRepository, ClinicRepository clinicRepository, ClinicMapper clinicMapper) {
        this.clinicService = clinicService;
        this.patientRepository = patientRepository;
        this.clinicRepository = clinicRepository;
        this.clinicMapper = clinicMapper;
    }

    @PostMapping
    public ResponseEntity<?> saveClinic(@RequestBody ClinicDTO clinicDto) throws URISyntaxException {
        return ResponseEntity.created(new URI("/clinic/save/" + clinicService.save(clinicDto).getClinicId()))
                .body(clinicDto);
    }

    @PutMapping
    public ResponseEntity<?> updateClinic(@RequestBody ClinicDTO clinicDto)
    {
        return ResponseEntity.ok(clinicService.save(clinicDto));
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id)
    {
        clinicService.delete(id);
    }

    @GetMapping(value ="/{id}")
    public ResponseEntity findClinicById(@PathVariable Long id)
    {
        Clinic clinic = clinicRepository.getOne(id);
        List<ClinicDTO> dtos = clinicMapper.clinicToDto(Collections.singletonList(clinic));
        Map<String, Object> response = new HashMap<>();
        response.put("clinicList", dtos);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/findCommence", method = RequestMethod.GET)
    public ResponseEntity<ServerResponse> findCommence(@RequestParam(value = "id") long patientId) {
        return ResponseEntity.ok().body(clinicService.findCommence(patientId));
    }

    @RequestMapping(value = "/grid", method = RequestMethod.GET)
    public ResponseEntity<?> clinicGrid(@RequestParam("id") long patientId, @RequestParam("rows") int rows,
                                        @RequestParam("page") int page) {
        List<ClinicDTO> dtos = clinicService.clinicGrid(patientId, page, rows);
        Map<String, Object> response = new HashMap<>();
        response.put("clinicList", dtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity getByPatient(@PathVariable Long id) {
        Patient patient = patientRepository.getOne(id);
        List<ClinicDTO> dtos = clinicMapper.clinicToDto(clinicRepository.findByPatient(patient, PageRequest.of(0, 1000)));
        Map<String, Object> response = new HashMap<>();
        response.put("clinicList", dtos);
        return ResponseEntity.ok(response);
    }
}
