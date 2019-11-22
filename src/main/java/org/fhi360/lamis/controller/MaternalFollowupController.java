/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.fhi360.lamis.controller.mapstruct.MaternalFollowupMapper;
import org.fhi360.lamis.model.MaternalFollowup;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.MaternalFollowupDTO;
import org.fhi360.lamis.model.repositories.MaternalFollowupRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.service.impl.MaternaFolowUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Idris
 */
@RestController
@RequestMapping(value = "/api/maternal-followup")
@Api(tags = "Maternal Followup ", description = " ")
public class MaternalFollowupController {
    private final MaternalFollowupRepository maternalFollowupRepository;
    private final MaternaFolowUpService maternaFolowUpService;
    private final PatientRepository patientRepository;
    private final MaternalFollowupMapper maternalFollowupMapper;

    public MaternalFollowupController(MaternalFollowupRepository maternalFollowupRepository,
                                      MaternaFolowUpService maternaFolowUpService,
                                      PatientRepository patientRepository, MaternalFollowupMapper maternalFollowupMapper) {
        this.maternalFollowupRepository = maternalFollowupRepository;
        this.maternaFolowUpService = maternaFolowUpService;
        this.patientRepository = patientRepository;
        this.maternalFollowupMapper = maternalFollowupMapper;
    }

    @PostMapping
    @PutMapping
    public ResponseEntity<?> save(@RequestBody MaternalFollowup maternalfollowup) {
        return maternaFolowUpService.save(maternalfollowup);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        maternalFollowupRepository.deleteById(id);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity getByPatient(@PathVariable Long id) {
        Patient patient = patientRepository.getOne(id);
        List<MaternalFollowup> followups = maternalFollowupRepository.findByPatient(patient);
        List<MaternalFollowupDTO> dtos = maternalFollowupMapper.modelToDto(followups);
        Map<String, Object> response = new HashMap<>();
        response.put("maternalfollowupList", dtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/find")
    public ResponseEntity<?> findMaternalFollowup(@RequestParam(value = "id") long patientId, @RequestParam(value = "dateVisit") LocalDate dateVisit) {
        return maternaFolowUpService.findMaternalfollowup(patientId, dateVisit);
    }


    @RequestMapping(value = "/findMaternalFollowUp", method = RequestMethod.GET)
    public ResponseEntity maternalFollowupGrid(@RequestParam(value = "id") long patientId, @RequestParam(value = "dateVisit") LocalDate dateVisit) {
        return maternaFolowUpService.findMaternalfollowup(patientId, dateVisit);
    }


}
