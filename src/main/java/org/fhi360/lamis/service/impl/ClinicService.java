/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fhi360.lamis.controller.mapstruct.ClinicMapper;
import org.fhi360.lamis.controller.mapstruct.PatientMapper;
import org.fhi360.lamis.model.Clinic;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.ClinicDTO;
import org.fhi360.lamis.model.dto.PatientDTO;
import org.fhi360.lamis.model.dto.ServerResponse;
import org.fhi360.lamis.model.repositories.ClinicRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


/**
 * @author user10
 */
@Service
@Transactional
@Slf4j
public class ClinicService {
    private final PatientRepository patientRepository;
    private final ClinicRepository clinicRepository;
    private final ClinicMapper clinicMapper;
    private final PatientMapper patientMapper;

    public ClinicService(PatientRepository patientRepository,
                         ClinicRepository clinicRepository,
                         ClinicMapper clinicMapper,
                         PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.clinicRepository = clinicRepository;
        this.clinicMapper = clinicMapper;
        this.patientMapper = patientMapper;
    }

    public ClinicDTO save(ClinicDTO clinicDto) {
        Clinic clinic = clinicMapper.dtoToClinic(clinicDto);
        Patient patient = patientRepository.getOne(clinic.getPatient().getPatientId());
        clinic.setFacility(patient.getFacility());
        return clinicMapper.clinicToDto(clinicRepository.save(clinic));
    }

    public void delete(Long id) {
        clinicRepository.deleteById(id);
    }

    public ServerResponse findClinic(long patientId, LocalDate dateVisit) {
        Patient patient = patientRepository.getOne(patientId);
        ServerResponse serverResponse = new ServerResponse();
        List<Clinic> clinicList = clinicRepository.findByPatientAndDateVisit(patient, dateVisit);
        LOG.info("Clinic: {}", clinicList);
        if (!clinicList.isEmpty()) {
            List<ClinicDTO> clinicDto = clinicMapper.clinicToDto(clinicList);
            PatientDTO patientDTO = patientMapper.patientToDto(patient);
            serverResponse.setClinic(clinicDto);
            serverResponse.setPatient(patientDTO);
        }
        return serverResponse;

    }


    public ServerResponse findCommence(long patientId) {
        Patient patient = patientRepository.getOne(patientId);
        ServerResponse serverResponse = new ServerResponse();
        List<Clinic> clinicList;
        clinicList = clinicRepository.findByPatientAndCommenceIsTrue(patient);
        if (!clinicList.isEmpty()) {
            List<ClinicDTO> clinicDto = clinicMapper.clinicToDto(clinicList);
            PatientDTO patientDTO = patientMapper.patientToDto(patient);
            serverResponse.setClinic(clinicDto);
            serverResponse.setPatient(patientDTO);
        }
        return serverResponse;

    }

    public List<ClinicDTO> clinicGrid(long patientId, int page, int rows) {
        Patient patient = patientRepository.getOne(patientId);
        if (page > 0) {
            page--;
        }
        if (rows < 1) {
            rows = 100;
        }
        List<Clinic> clinicDto = clinicRepository.findByPatient(patient, PageRequest.of(page, rows));

        return clinicMapper.clinicToDto(clinicDto);
    }

}
