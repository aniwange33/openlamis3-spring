package org.fhi360.lamis.service.impl;

import org.fhi360.lamis.controller.mapstruct.AncMapper;
import org.fhi360.lamis.model.*;
import org.fhi360.lamis.model.dto.AncDTO;
import org.fhi360.lamis.model.repositories.*;
import org.fhi360.lamis.service.MonitorService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class AncService {
    private final FacilityRepository facilityRepository;
    private final PatientRepository patientRepository;
    private final AncRepository ancRepository;
    private final AncMapper ancMapper;
    private final PartnerInformationRepository partnerInformationRepository;
    private final MotherInformationRepository motherInformationRepository;

    public AncService(FacilityRepository facilityRepository, PatientRepository patientRepository, AncRepository ancRepository,
                      AncMapper ancMapper, PartnerInformationRepository partnerInformationRepository, MotherInformationRepository motherInformationRepository) {
        this.facilityRepository = facilityRepository;
        this.patientRepository = patientRepository;
        this.ancRepository = ancRepository;
        this.ancMapper = ancMapper;
        this.partnerInformationRepository = partnerInformationRepository;
        this.motherInformationRepository = motherInformationRepository;
    }

    public AncDTO saveAncOrUpdate(AncDTO ancDTO, long facilityId) {
        Facility facility = facilityRepository.getOne(facilityId);
        Anc anc = ancMapper.dtoToAnc(ancDTO);
        anc.setFacility(facility);
        Anc result = ancRepository.save(anc);
        PartnerInformation partnerInformation = ancMapper.dtoToPartnerInformation(ancDTO);
        partnerInformation.setFacility(facility);
        partnerInformationRepository.save(partnerInformation);
        return ancMapper.ancToDto(result);
    }

    public void delete(long ancId, LocalDate dateVisit) {
        Anc anc = ancRepository.getOne(ancId);
        MonitorService.logEntity(anc.getPatient().getHospitalNum() + "#" + dateVisit, "anc", 3);
        ancRepository.delete(anc);
    }


    public Collection<AncDTO> retrieveFirstAncList(long patientId) {
        Patient patient = patientRepository.getOne(patientId);
        Anc anc = ancRepository.findFirstByPatientOrderByDateVisit(patient);
        return Collections.singletonList(ancMapper.ancToDto(anc));
    }


    public List<PartnerInformation> retrievePartnerInformationList(Long facilityId) {
        Facility facility = facilityRepository.getOne(facilityId);
        return partnerInformationRepository.findByFacility(facility);
    }


    public Collection<AncDTO> retrieveLastAncList(long patientId) {
        Patient patient = patientRepository.getOne(patientId);
        Anc anc = ancRepository.findFirstByPatientOrderByDateVisitDesc(patient);
        return Collections.singletonList(ancMapper.ancToDto(anc));
    }


    public List<AncDTO> findAnc(long patientId, LocalDate date) {
        Patient patient = patientRepository.getOne(patientId);
        List<Anc> anc = ancRepository.findByPatientAndDateVisit(patient, date);
        return ancMapper.ancToDto(anc);
    }

    public List<PartnerInformation> retrieveMotherList(long facilityId) {
        Facility facility = facilityRepository.getOne(facilityId);
        return partnerInformationRepository.findByFacility(facility);
    }

    public MotherInformation retrieveMotherListForChild(long id) {
        return motherInformationRepository.getOne(id);
    }
    public  Map<String, Object> ancGrid(long facilityId, int rows, int page){
        Facility facility = facilityRepository.getOne(facilityId);
        List<Anc> ancs = ancRepository.findByFacility(facility, PageRequest.of(page, rows));
        Map<String, Object> response = new HashMap<>();
        Long count = ancRepository.countByFacility(facility);
        response.put("ancList", ancs);
        response.put("currpage", page);
        response.put("totalrecords", count);
        response.put("totalpages", count / rows);
        return  response;
    }

}
