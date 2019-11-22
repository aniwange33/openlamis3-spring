package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.controller.mapstruct.ChildMapper;
import org.fhi360.lamis.controller.mapstruct.MotherInformationMapper;
import org.fhi360.lamis.model.Child;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.MotherInformation;
import org.fhi360.lamis.model.dto.ChildDTO;
import org.fhi360.lamis.model.dto.ChildListDTO;
import org.fhi360.lamis.model.dto.MotherDTO;
import org.fhi360.lamis.model.repositories.ChildRepository;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.model.repositories.MotherInformationRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.service.MonitorService;
import org.fhi360.lamis.utility.PatientNumberNormalizer;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/child")
@Api(tags = "Child"  , description = " ")
public class ChildController {
    private final ChildRepository childRepository;
    private final FacilityRepository facilityRepository;
    private final ChildMapper childMapper;
    private final MotherInformationMapper motherInformationMapper;
    private final MotherInformationRepository motherInformationRepository;
    private final PatientRepository patientRepository;

    public ChildController(ChildRepository childRepository, FacilityRepository facilityRepository,
                           ChildMapper childMapper, MotherInformationMapper motherInformationMapper,
                           MotherInformationRepository motherInformationRepository, PatientRepository patientRepository) {
        this.childRepository = childRepository;
        this.facilityRepository = facilityRepository;
        this.childMapper = childMapper;
        this.motherInformationMapper = motherInformationMapper;
        this.motherInformationRepository = motherInformationRepository;
        this.patientRepository = patientRepository;
    }

    @GetMapping("/{childId}")
    public ResponseEntity findChild(@PathVariable Long childId) {
        Child child = childRepository.getOne(childId);
        List<ChildDTO> dtos = Collections.singletonList(childMapper.childToDTO(child));
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/findChildByNumber")
    public ResponseEntity<List<ChildDTO>> findChildByNumber(@PathVariable String hospitalNum) {
        Child child = childRepository.findByHospitalNumber(PatientNumberNormalizer.unpadNumber(hospitalNum));
        List<ChildDTO> dtos = Collections.singletonList(childMapper.childToDTO(child));
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/grid")
    public ResponseEntity<ChildListDTO> childGrid(@RequestParam("rows") Integer rows, @RequestParam("page") Integer page,
                                                  @RequestParam Long facilityId) {
        Facility facility = facilityRepository.getOne(facilityId);
        List<Child> children = childRepository.findByFacility(facility, PageRequest.of(page, rows));
        List<ChildDTO> dtos = children.stream()
                .map(childMapper::childToDTO).collect(Collectors.toList());
        ChildListDTO dto = new ChildListDTO();
        Long count = childRepository.countByFacility(facility);
        dto.setChildList(dtos);
        dto.setCurrpage(page);
        dto.setTotalrecords(count);
        dto.setTotalpages(count / rows);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{childId}")
    public ResponseEntity<?> deleteChild(@PathVariable Long childId) {
        childRepository.deleteById(childId);
        MonitorService.logEntity("", "child", 3);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @PutMapping
    public ResponseEntity<?> saveChild(@RequestBody MotherDTO motherDTO) {
        MotherInformation motherInformation;
        if (!StringUtils.equals(motherDTO.getWilling(), "Yes")) {
            motherInformation = motherInformationMapper.dtoToMotherInformation(motherDTO);
        } else {
            motherInformation = motherInformationRepository.findByPatient(patientRepository.getOne(motherDTO.getPatientId()));
        }
        AtomicInteger count = new AtomicInteger(1);
        List<Child> children = childMapper.dtosToChildren(motherDTO.getChildren()).stream()
                .map(child -> {
                    child.setReferenceNum(motherInformation.getHospitalNum() + "#" + count.getAndIncrement());
                    return child;
                }).collect(Collectors.toList());
        motherInformation.getChildren().addAll(children);
        motherInformationRepository.save(motherInformation);
        return ResponseEntity.ok(motherDTO);
    }

    @PutMapping("/updateChild")
    public ResponseEntity<ChildDTO> updateChild(@RequestBody ChildDTO childDTO) {
        Child child = childMapper.dtoToChild(childDTO);
        childRepository.save(child);
        return ResponseEntity.ok(childDTO);
    }
}
