package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.fhi360.lamis.controller.mapstruct.ChildFollowupMapper;
import org.fhi360.lamis.controller.mapstruct.ChildMapper;
import org.fhi360.lamis.model.Child;
import org.fhi360.lamis.model.ChildFollowup;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.dto.ChildDTO;
import org.fhi360.lamis.model.dto.ChildFollowupDTO;
import org.fhi360.lamis.model.dto.ChildFollowupListDTO;
import org.fhi360.lamis.model.repositories.ChildFollowupRepository;
import org.fhi360.lamis.model.repositories.ChildRepository;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.service.MonitorService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/child-followup")
@Api(tags = "Child Followup", description = " ")
public class ChildFollowupController {
    private final ChildRepository childRepository;
    private final ChildFollowupRepository childFollowupRepository;
    private final FacilityRepository facilityRepository;
    private final ChildFollowupMapper childFollowupMapper;
    private final ChildMapper childMapper;

    public ChildFollowupController(ChildRepository childRepository, ChildFollowupRepository childFollowupRepository,
                                   FacilityRepository facilityRepository, ChildFollowupMapper childFollowupMapper,
                                   ChildMapper childMapper) {
        this.childRepository = childRepository;
        this.childFollowupRepository = childFollowupRepository;
        this.facilityRepository = facilityRepository;
        this.childFollowupMapper = childFollowupMapper;
        this.childMapper = childMapper;
    }

    @PostMapping
    public ResponseEntity<ChildFollowup> saveChildFollowup(@RequestBody ChildFollowupDTO dto, HttpSession session) throws URISyntaxException {
        Long facilityId = (Long) session.getAttribute("id");
        ChildFollowup childFollowup = childFollowupMapper.dtoToChildFollowup(dto);
        Facility facility = facilityRepository.getOne(facilityId);
        Child child = childRepository.getOne(dto.getChildId());
        childFollowup.setChild(child);
        childFollowup.setFacility(facility);
        ChildFollowup result = childFollowupRepository.save(childFollowup);
        return ResponseEntity.created(new URI("/child-followup/" + result.getChildfollowupId()))
                .body(result);
    }

    @PutMapping
    public ResponseEntity<ChildFollowup> updateChildFollowup(@RequestBody ChildFollowupDTO dto) {
        ChildFollowup childFollowup = childFollowupMapper.dtoToChildFollowup(dto);
        ChildFollowup result = childFollowupRepository.save(childFollowup);
        return ResponseEntity.ok()
                .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChildFollowup(@PathVariable("id") Long childFollowupId) {
        ChildFollowup childFollowup = childFollowupRepository.getOne(childFollowupId);
        String hospitalNum = childFollowup.getReferenceNum();
        String dateVisit = childFollowup.getDateVisit().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        childFollowupRepository.deleteById(childFollowupId);
        MonitorService.logEntity(hospitalNum + "#" + dateVisit, "childfollowup", 3);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/child/{id}")
    public ResponseEntity<List<ChildFollowupDTO>> findChildFollowup(@PathVariable("id") Long childId) {
        Child child = childRepository.getOne(childId);
        List<ChildFollowup> followups = childFollowupRepository.findByChild(child);
        List<ChildFollowupDTO> dtos = childFollowupMapper.childFollowupToDto(followups);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ChildDTO>> findChild(@PathVariable("id") Long childId) {
        Child child = childRepository.getOne(childId);
        List<ChildDTO> dtos = childMapper.childToDTO(Collections.singletonList(child));
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/childFollowupGrid")
    public ResponseEntity<ChildFollowupListDTO> childFollowupGrid(@Param("rows") Integer rows, @Param("page") Integer page,
                                                                  @Param("child") Long childId, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        long count;
        List<ChildFollowup> childFollowups;
        if (childId > 0) {
            Child child = childRepository.getOne(childId);
            childFollowups = childFollowupRepository.findByChild(child);
            count = childFollowupRepository.countByChild(child);
        } else {
            Facility facility = facilityRepository.getOne(facilityId);
            childFollowups = childFollowupRepository.findByFacility(facility);
            count = childFollowupRepository.countByFacility(facility);
        }
        List<ChildFollowupDTO> dtos = childFollowupMapper.childFollowupToDto(childFollowups);
        ChildFollowupListDTO dto = new ChildFollowupListDTO();
        dto.setChildfollowupList(dtos);
        dto.setTotalrecords(count);
        dto.setCurrpage(page);
        return ResponseEntity.ok(dto);
    }
}
