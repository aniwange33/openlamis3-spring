package org.fhi360.lamis.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.controller.mapstruct.ChildMapper;
import org.fhi360.lamis.controller.mapstruct.MotherInformationMapper;
import org.fhi360.lamis.model.Child;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.MotherInformation;
import org.fhi360.lamis.model.dto.ChildDTO;
import org.fhi360.lamis.model.dto.ChildGridDTO;
import org.fhi360.lamis.model.dto.MotherDTO;
import org.fhi360.lamis.model.repositories.ChildRepository;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.model.repositories.MotherInformationRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.service.MonitorService;
import org.fhi360.lamis.utility.PatientNumberNormalizer;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChildService {

    private final ChildRepository childRepository;
    private final FacilityRepository facilityRepository;
    private final ChildMapper childMapper;
    private final MotherInformationMapper motherInformationMapper;
    private final MotherInformationRepository motherInformationRepository;
    private final PatientRepository patientRepository;

    public ChildService(ChildRepository childRepository, FacilityRepository facilityRepository, ChildMapper childMapper, MotherInformationMapper motherInformationMapper, MotherInformationRepository motherInformationRepository, PatientRepository patientRepository) {
        this.childRepository = childRepository;
        this.facilityRepository = facilityRepository;
        this.childMapper = childMapper;
        this.motherInformationMapper = motherInformationMapper;
        this.motherInformationRepository = motherInformationRepository;
        this.patientRepository = patientRepository;
    }

    public List<ChildDTO> findChild(Long childId) {
        Child child = childRepository.getOne(childId);
        return Collections.singletonList(childMapper.childToDTO(child));
    }

    public List<ChildDTO> findByHospitalNumber(String hospitalNum) {
        Child child = childRepository.findByHospitalNumber(PatientNumberNormalizer.unpadNumber(hospitalNum));
        return Collections.singletonList(childMapper.childToDTO(child));
    }


    public ChildGridDTO childGrid(long facilityId, int page, int rows) {
        Facility facility = facilityRepository.getOne(facilityId);
        List<Child> children = childRepository.findByFacility(facility, PageRequest.of(page, rows));
        List<ChildDTO> dtos = children.stream()
                .map(childMapper::childToDTO).collect(Collectors.toList());
        ChildGridDTO dto = new ChildGridDTO();
        Long count = childRepository.countByFacility(facility);
        dto.setChildList(dtos);
        dto.setCurrpage(page);
        dto.setTotalrecords(count);
        dto.setTotalpages(count / rows);
        return dto;
    }

    public void delete(long childId) {
        childRepository.deleteById(childId);
        MonitorService.logEntity("", "child", 3);
    }


    public ChildDTO update(ChildDTO childDTO) {
        return childMapper.childToDTO(childRepository.save(childMapper.dtoToChild(childDTO)));
    }

    public MotherDTO save(MotherDTO motherDTO, long facilityId) {
        MotherInformation motherInformation;
        if (!StringUtils.equals(motherDTO.getWilling(), "Yes")) {
            motherInformation = motherInformationMapper.dtoToMotherInformation(motherDTO);
            motherInformation.setFacility(facilityRepository.getOne(facilityId));
        } else {
            motherInformation = motherInformationRepository
                    .findByPatient(patientRepository.getOne(motherDTO.getPatientId()));
        }
        AtomicInteger count = new AtomicInteger(1);
        List<Child> children = childMapper.dtosToChildren(motherDTO.getChildren()).stream()
                .map(child -> {
                    child.setReferenceNum(motherInformation.getHospitalNum() + "#" + count.getAndIncrement());
                    return child;
                }).collect(Collectors.toList());
        motherInformation.getChildren().addAll(children);
        motherInformationRepository.save(motherInformation);
        return motherDTO;
    }

}
