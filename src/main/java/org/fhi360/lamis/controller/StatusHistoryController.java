/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.fhi360.lamis.controller.mapstruct.StatusHistoryMapper;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.StatusHistory;
import org.fhi360.lamis.model.dto.StatusHistoryDTO;
import org.fhi360.lamis.model.dto.StatusListDTO;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.model.repositories.StatusHistoryRepository;
import org.fhi360.lamis.service.MonitorService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mattae
 */

@RestController
@RequestMapping(value = "/api/status-history")
@Api(tags = "Status History", description = " ")
public class StatusHistoryController {
    private final PatientRepository patientRepository;
    private final StatusHistoryRepository statusHistoryRepository;
    private final FacilityRepository facilityRepository;
    private final StatusHistoryMapper historyMapper;

    public StatusHistoryController(PatientRepository patientRepository, StatusHistoryRepository statusHistoryRepository,
                                   FacilityRepository facilityRepository, StatusHistoryMapper historyMapper) {
        this.patientRepository = patientRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.facilityRepository = facilityRepository;
        this.historyMapper = historyMapper;
    }

    @PostMapping
    @PutMapping
    public ResponseEntity<StatusHistory> save(@RequestBody StatusHistoryDTO dto) {
        StatusHistory statusHistory = historyMapper.dtoToStatusHistory(dto);
        StatusHistory result = statusHistoryRepository.save(statusHistory);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        StatusHistory statusHistory = statusHistoryRepository.getOne(id);
        Patient patient = statusHistory.getPatient();
        StatusHistory currentStatus = statusHistoryRepository.getCurrentStatusForPatient(patient);
        if (statusHistory.equals(currentStatus)) {
            StatusHistory history = statusHistoryRepository
                    .getCurrentStatusForPatientAt(patient, statusHistory.getDateCurrentStatus());
            if (history != null) {
                patient.setCurrentStatus(history.getCurrentStatus());
                patient.setDateCurrentStatus(history.getDateCurrentStatus());
            }
            {
                patient.setCurrentStatus(null);
                patient.setDateCurrentStatus(null);
            }
            patientRepository.save(patient);
        }
        statusHistoryRepository.delete(statusHistory);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        MonitorService.logEntity(patient.getHospitalNum() + "#" + statusHistory.getCurrentStatus() + "#" +
                format.format(statusHistory.getDateCurrentStatus()), "statushistory", 3);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity findStatusHistory(@PathVariable Long id) {
        StatusHistory statushistory = statusHistoryRepository.getOne(id);
        Map<String, Object> response = new HashMap<>();
        response.put("statusList", Collections.singletonList(historyMapper.statusHistoryToDto(statushistory)));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find")
    public ResponseEntity findStatus(@RequestParam("patientId") Long id, @RequestParam("dateCurrentStatus") LocalDate date) {
        Patient patient = patientRepository.getOne(id);
        List<StatusHistory> histories = statusHistoryRepository.findByPatientAndDateCurrentStatus(patient, date);
        Map<String, Object> response = new HashMap<>();
        response.put("statusList", historyMapper.statusHistoryToDto(histories));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/grid")
    public ResponseEntity<?> statusGrid(@RequestParam("rows") Integer rows,
                                        @RequestParam("page") Integer page,
                                        @RequestParam Long facilityId) {
        Facility facility = facilityRepository.getOne(facilityId);
        List<StatusHistory> histories = statusHistoryRepository.findByFacility(facility, PageRequest.of(page, rows));
        List<StatusHistoryDTO> dtos = historyMapper.statusHistoryToDto(histories);
        StatusListDTO dto = new StatusListDTO();
        Long count = statusHistoryRepository.countByFacility(facility);
        dto.setStatusList(dtos);
        dto.setCurrpage(page);
        dto.setTotalrecords(count);
        dto.setTotalpages(count / rows);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity getByPatient(@PathVariable Long id) {
        Patient patient = patientRepository.getOne(id);
        List<StatusHistory> histories = statusHistoryRepository.findByPatient(patient);
        List<StatusHistoryDTO> dtos = historyMapper.statusHistoryToDto(histories);
        Map<String, Object> response = new HashMap<>();
        response.put("statusList", dtos);
        return ResponseEntity.ok(response);
    }

}
