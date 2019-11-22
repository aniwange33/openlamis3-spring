/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.Eac;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.EacRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author user10
 */
@RestController
@RequestMapping("/api/eac")
@Api(tags = "Eac Grid Chart", description = " ")
public class EacGridAction {
    private final EacRepository eacRepository;
    private final PatientRepository patientRepository;

    public EacGridAction(EacRepository eacRepository, PatientRepository patientRepository) {
        this.eacRepository = eacRepository;
        this.patientRepository = patientRepository;
    }

    @GetMapping("/grid")
    public ResponseEntity eacGrid(@RequestParam("id") Long patientId) {
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        List<Eac> list = eacRepository.findByPatientOrderByDateEac1Desc(patient);
        Map<String, Object> response = new HashMap<>();
        response.put("eacList", list);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find")
    public ResponseEntity find(@RequestParam Long patientId, @RequestParam LocalDate dateVisit) {
        Patient patient = patientRepository.getOne(patientId);
        List<Eac> list = eacRepository.findByPatientOrderByDateEac1Desc(patient);
        Map<String, Object> response = new HashMap<>();
        response.put("eacList", list);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity getByPatient(@PathVariable Long id) {
        Patient patient = patientRepository.getOne(id);
        List<Eac> list = eacRepository.findByPatientOrderByDateEac1Desc(patient);
        Map<String, Object> response = new HashMap<>();
        response.put("eacList", list);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PutMapping
    public ResponseEntity saveEac(@RequestBody Eac eac) {
        eacRepository.save(eac);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity eac(@PathVariable Long id) {
        Eac eac = eacRepository.getOne(id);
        return ResponseEntity.ok(eac);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        eacRepository.deleteById(id);
    }
}
