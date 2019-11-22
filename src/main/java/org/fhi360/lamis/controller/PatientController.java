/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.controller.mapstruct.PatientMapper;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.User;
import org.fhi360.lamis.model.dto.PatientDTO;
import org.fhi360.lamis.model.repositories.BiometricRepository;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.security.SecurityUtils;
import org.fhi360.lamis.service.MonitorService;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.Scrambler;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author Mattae
 */
@Controller
@RequestMapping(value = "/api/patient")
@Api(tags = "Patient", description = " ")
@Slf4j
public class PatientController {
    private final PatientRepository patientRepository;
    private final FacilityRepository facilityRepository;
    private final PatientMapper patientMapper;
    private final BiometricRepository biometricRepository;
    private final JdbcTemplate jdbcTemplate;

    public PatientController(PatientRepository patientRepository, FacilityRepository facilityRepository,
                             PatientMapper patientMapper, BiometricRepository biometricRepository,
                             JdbcTemplate jdbcTemplate) {
        this.patientRepository = patientRepository;
        this.facilityRepository = facilityRepository;
        this.patientMapper = patientMapper;
        this.biometricRepository = biometricRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping
    public ResponseEntity savePatient(@RequestBody PatientDTO dto) {
        Long facilityId = SecurityUtils.getCurrentFacility().getId();
        Facility facility = facilityRepository.getOne(facilityId);
        Patient patient = patientMapper.dtoToPatient(dto);
        if (patient.getCaseManager() != null && patient.getCaseManager().getCasemanagerId() == null) {
            patient.setCaseManager(null);
        }
        patient.setFacility(facility);
      /*  if (patient.getDateBirth() == null) {
            LocalDate registrationDate = patient.getDateRegistration();
            Integer age = patient.getAge();
            String ageUnit = patient.getAgeUnit();
            ChronoUnit unit = ChronoUnit.YEARS;
            if (ageUnit.contains("month")) {
                unit = ChronoUnit.MONTHS;
            } else if (ageUnit.contains("week")) {
                unit = ChronoUnit.WEEKS;
            } else if (ageUnit.contains("day")) {
                unit = ChronoUnit.DAYS;
            }
            LocalDate dob = registrationDate.minus(age, unit);
            patient.setDateBirth(dob);
        }*/
        patient.setSendMessage(Boolean.FALSE);
        patient.setPregnant(Boolean.FALSE);
        patient.setBreastfeeding(Boolean.FALSE);
        if (patient.getEntryPoint().equals("PMTCT") && patient.getGender().equals("Female")) {
            switch (dto.getPregnant()) {
                case "2":
                    patient.setPregnant(Boolean.TRUE);
                    patient.setBreastfeeding(Boolean.FALSE);
                    break;
                case "3":
                    patient.setPregnant(Boolean.FALSE);
                    patient.setBreastfeeding(Boolean.TRUE);
                    break;
                case "1":
                    patient.setPregnant(Boolean.FALSE);
                    patient.setBreastfeeding(Boolean.FALSE);
                    break;
                default:

                    break;
            }
        }
        patientRepository.save(patient);
        return ResponseEntity.ok().build();
    }


    @PutMapping
    public ResponseEntity updatePatient(@RequestBody PatientDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        Facility facility = facilityRepository.getOne(facilityId);
        Patient patient = patientMapper.dtoToPatient(dto);
        patient.setFacility(facility);
        if (patient.getCaseManager() != null && patient.getCaseManager().getCasemanagerId() == null) {
            patient.setCaseManager(null);
        }
        if (patient.getDateBirth() == null) {
            LocalDate registrationDate = patient.getDateRegistration();
            Integer age = patient.getAge();
            String ageUnit = patient.getAgeUnit();
            ChronoUnit unit = ChronoUnit.YEARS;
            if (ageUnit.contains("month")) {
                unit = ChronoUnit.MONTHS;
            } else if (ageUnit.contains("week")) {
                unit = ChronoUnit.WEEKS;
            } else if (ageUnit.contains("day")) {
                unit = ChronoUnit.DAYS;
            }
            LocalDate dob = registrationDate.minus(age, unit);
            patient.setDateBirth(dob);
        }
        patientRepository.save(patient);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{patientId}")
    public String delete(@PathVariable Long patientId) {
        Patient patient = patientRepository.getOne(patientId);
        MonitorService.logEntity(patient.getHospitalNum(), "patient", 3);
        patientRepository.delete(patient);
        return "patients/patients-search";
    }

    @GetMapping("/{patientId}")
    public ResponseEntity findPatientId(@PathVariable Long patientId) {
        Map<String, Object> response = new HashMap<>();
        Patient patient = patientRepository.getOne(patientId);
        PatientDTO dto = patientMapper.patientToDto(patient);
        biometricRepository.findByPatient(patient).ifPresent(b -> dto.setBiometric(true));
        response.put("patientList", Collections.singletonList(dto));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-number/{num}/{facilityId}")
    public ResponseEntity findPatientByNumber(@PathVariable String num, @PathVariable Long facilityId) {
        Facility facility = facilityRepository.getOne(facilityId);
        Patient patient = patientRepository.findByFacilityAndHospitalNum(facility, num);
        PatientDTO dto = patientMapper.patientToDto(patient);
        biometricRepository.findByPatient(patient).ifPresent(b -> dto.setBiometric(true));
        return ResponseEntity.ok(Collections.singletonList(dto));
    }

    @PutMapping("/update-hospital-number")
    public ResponseEntity updateNewHospitalNumber(@RequestParam("hospitalNum") String hospitalNum, @RequestParam("newHospitalNum") String newHospitalNum,
                                                  @RequestParam Long facilityId) {
        Facility facility = facilityRepository.getOne(facilityId);
        hospitalNum = StringUtils.leftPad(hospitalNum, 7, "0");
        newHospitalNum = StringUtils.leftPad(newHospitalNum, 7, "0");
        Patient patient = patientRepository.findByFacilityAndHospitalNum(facility, hospitalNum);
        patient.setHospitalNum(newHospitalNum);
        patientRepository.save(patient);
        MonitorService.logEntity(hospitalNum + "#" + newHospitalNum, "patient", 4);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/grid")
    public ResponseEntity patientGrid(@RequestParam(value = "rows", defaultValue = "50") Integer rows,
                                      @RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String female,
                                      @RequestParam(required = false) String unsuppressed, HttpSession session) {
        Long facilityId = 1190L;//(Long) session.getAttribute("id");
        if (page > 0) {
            page--;
        }
        String query;
        if (name != null) {
            Scrambler scrambler = new Scrambler();
            name = scrambler.scrambleCharacters(name);
            name = name.toUpperCase();
            if (StringUtils.isEmpty(name)) {
                query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b inner " +
                        "join patient x on x.uuid = b.patient_id  where x.patient_id = p.patient_id and x.facility_id " +
                        "= p.facility_id) as biometric FROM patient p WHERE facility_id = " + facilityId + "" +
                        " ORDER BY surname ASC LIMIT " + page + " , " + rows;
                if (female != null) {
                    query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b " +
                            "inner join patient x on x.uuid = b.patient_id  where x.patient_id = p.patient_id and " +
                            "x.facility_id = p.facility_id) as biometric FROM patient p WHERE facility_id = " +
                            facilityId + " AND gender = 'Female' ORDER BY surname ASC LIMIT " + page + " , " + rows;
                }
            } else {
                query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b inner " +
                        "join patient x on x.uuid = b.patient_id  where x.patient_id = p.patient_id and x.facility_id " +
                        "= p.facility_id) as biometric FROM patient p WHERE facility_id = " + facilityId + " " +
                        "AND UPPER(surname) LIKE '" + name + "%' OR UPPER(other_names) LIKE '" + name +
                        "%' OR UPPER(CONCAT(surname, ' ', other_names)) LIKE '" + name + "%'  OR UPPER(CONCAT(other_names, " +
                        "' ', surname)) LIKE '" + name + "%'  ORDER BY surname ASC LIMIT " + page + " , " + rows;
                if (female != null) {
                    query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b inner " +
                            "join patient x on x.uuid = b.patient_id  where x.patient_id = p.patient_id and " +
                            "x.facility_id = p.facility_id) as biometric FROM patient p WHERE facility_id = " + facilityId +
                            " AND gender = 'Female' AND UPPER(surname) LIKE '" + name + "%' OR UPPER(other_names) LIKE " +
                            "'" + name + "%' OR UPPER(CONCAT(surname, ' ', other_names)) LIKE '" + name + "%'  " +
                            "OR UPPER(CONCAT(other_names, ' ', surname)) LIKE '" + name + "%' ORDER BY surname " +
                            "ASC LIMIT " + page + " , " + rows;
                }
                if (unsuppressed != null) {
                    query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b inner " +
                            "join patient x on x.uuid = b.patient_id  where x.patient_id = p.patient_id and " +
                            "x.facility_id = p.facility_id) as biometric FROM patient p WHERE facility_id = " +
                            facilityId + " AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND " +
                            "last_viral_load >=1000 AND UPPER(surname) LIKE '" + name + "%' OR UPPER(other_names) LIKE " +
                            "'" + name + "%' OR UPPER(CONCAT(surname, ' ', other_names)) LIKE '" + name + "%'  OR " +
                            "UPPER(CONCAT(other_names, ' ', surname)) LIKE '" + name + "%' ORDER BY surname " +
                            "ASC LIMIT " + page + " , " + rows;
                }
            }

        } else {
            query = "SELECT p.*,(select case when count(*) > 0 then true else false end from biometric b inner join " +
                    "patient x on x.uuid = b.patient_id  where x.patient_id = p.patient_id and x.facility_id = " +
                    "p.facility_id) as biometric FROM patient p WHERE facility_id = " + facilityId + " " +
                    "ORDER BY surname ASC LIMIT " + page + " , " + rows;
            if (female != null) {
                query = "SELECT p.*,(select case when count(*) > 0 then true else false end from biometric b inner join" +
                        " patient x on x.uuid = b.patient_id  where x.patient_id = p.patient_id and x.facility_id = " +
                        "p.facility_id) as biometric FROM patient p WHERE facility_id = " + facilityId + " AND gender =" +
                        " 'Female' ORDER BY surname ASC LIMIT " + page + " , " + rows;
            }
            if (unsuppressed != null) {
                query = "SELECT p.*,(select case when count(*) > 0 then true else false end from biometric b inner join " +
                        "patient x on x.uuid = b.patient_id  where x.patient_id = p.patient_id and x.facility_id = " +
                        "p.facility_id) as biometric FROM patient p WHERE facility_id = " + facilityId +
                        " AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND last_viral_load " +
                        ">=1000 ORDER BY surname ASC LIMIT " + page + " , " + rows;
            }
        }
      Map<String, Object> response = new HashMap<>();
        LOG.info("Query: {}", query);
        List<Patient> patients = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Patient.class));
        System.out.println("patients: " + patients.toString());
        List<PatientDTO> dtos = patientMapper.patientToDto(patients).stream()
                .map(dto -> {
                    Scrambler scrambler = new Scrambler();
                    dto.setAddress(scrambler.unscrambleCharacters(dto.getAddress()));
                    dto.setName(scrambler.unscrambleCharacters(dto.getSurname() +" "+ dto.getOtherNames()));
                    biometricRepository.findByPatient(patientMapper.dtoToPatient(dto)).ifPresent(b -> dto.setBiometric(true));
                   Boolean due = patientRepository.dueViralLoad(patientMapper.dtoToPatient(dto));
                    if (due) {
                        dto.setDueViralLoad(1);
                    }
                    return dto;
                }).collect(toList());
        response.put("patientList", dtos);
        response.put("currpage", page);
        return ResponseEntity.ok(response);
    }




}
