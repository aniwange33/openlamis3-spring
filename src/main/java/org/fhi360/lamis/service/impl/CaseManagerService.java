package org.fhi360.lamis.service.impl;

import org.fhi360.lamis.controller.mapstruct.CaseManagerMapper;
import org.fhi360.lamis.model.CaseManager;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.dto.CaseManagerDTO;
import org.fhi360.lamis.model.repositories.CaseManagerRepository;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
public class CaseManagerService {
    private final CaseManagerRepository caseManagerRepository;
    private final FacilityRepository facilityRepository;
    private final PatientRepository patientRepository;
    private final CaseManagerMapper caseManagerMapper;

    public CaseManagerService(CaseManagerRepository caseManagerRepository, FacilityRepository facilityRepository, PatientRepository patientRepository, CaseManagerMapper caseManagerMapper) {
        this.caseManagerRepository = caseManagerRepository;
        this.facilityRepository = facilityRepository;
        this.patientRepository = patientRepository;
        this.caseManagerMapper = caseManagerMapper;
    }

    public CaseManager save(CaseManagerDTO caseManagerDTO, long facilityId){
        Facility facility = facilityRepository.getOne(facilityId);
        CaseManager caseManager = caseManagerMapper.dtoToCaseManager(caseManagerDTO);
        caseManager.setFacility(facility);
        return caseManagerRepository.save(caseManager);
    }

    public  void  delete(long caseManagerId){
        caseManagerRepository.deleteById(caseManagerId);
    }

    public  Map<String, String>  assign(Long caseManagerId,
                         String patientIds){
        Map<String, String> assignment = new HashMap<>();
        CaseManager caseManager = caseManagerRepository.getOne(caseManagerId);
        List<Long> ids = Arrays.stream(patientIds.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
        patientRepository.findAllById(ids)
                .forEach(patient -> {
                    patient.setCaseManager(caseManager);
                    patientRepository.save(patient);
                });

        assignment.put("response", "success");
        caseManagerRepository.deleteById(caseManagerId);
        return assignment;
    }


    public  Map<String, Object>  retrieveCaseManagers(long facilityId){
        Facility facility = facilityRepository.getOne(facilityId);
        Map<String, Object> details = new HashMap<>();
        caseManagerRepository.findByFacilityOrderByFullName(facility)
                .forEach(caseManager -> details.put(caseManager.getCasemanagerId().toString(), caseManager.getFullName()));
        return  details;
    }

    public  Map<String, Object>  getCaseManagerDetails(Long caseManagerId){
        Map<String, Object> details = new HashMap<>();
        CaseManager caseManager = caseManagerRepository.getOne(caseManagerId);
        details.put("casemanagerId", caseManager.getCasemanagerId().toString());
        details.put("fullname", caseManager.getFullName());
        details.put("religion", caseManager.getReligion());
        details.put("sex", caseManager.getSex());
        details.put("clientCount", patientRepository.countByCaseManager(caseManager));
        return  details;
    }

}
