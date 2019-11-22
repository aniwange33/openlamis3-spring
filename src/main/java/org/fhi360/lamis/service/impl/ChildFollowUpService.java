package org.fhi360.lamis.service.impl;

import org.fhi360.lamis.controller.mapstruct.ChildFollowupMapper;
import org.fhi360.lamis.controller.mapstruct.ChildMapper;
import org.fhi360.lamis.model.Child;
import org.fhi360.lamis.model.ChildFollowup;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.dto.ChildDTO;
import org.fhi360.lamis.model.dto.ChildFollowupDTO;
import org.fhi360.lamis.model.dto.ChildFollowupGridDTO;
import org.fhi360.lamis.model.repositories.ChildFollowupRepository;
import org.fhi360.lamis.model.repositories.ChildRepository;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.service.MonitorService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class ChildFollowUpService {
    private final ChildRepository childRepository;
    private final ChildFollowupRepository childFollowupRepository;
    private final FacilityRepository facilityRepository;
    private final ChildFollowupMapper childFollowupMapper;
    private final ChildMapper childMapper;
    private final HttpSession gridSession;

    public ChildFollowUpService(ChildRepository childRepository, ChildFollowupRepository childFollowupRepository, FacilityRepository facilityRepository, ChildFollowupMapper childFollowupMapper, ChildMapper childMapper, HttpSession gridSession) {
        this.childRepository = childRepository;
        this.childFollowupRepository = childFollowupRepository;
        this.facilityRepository = facilityRepository;
        this.childFollowupMapper = childFollowupMapper;
        this.childMapper = childMapper;
        this.gridSession = gridSession;
    }

    public ChildFollowup saveChildFollowup(ChildFollowupDTO dto, long facilityId){
        ChildFollowup childFollowup = childFollowupMapper.dtoToChildFollowup(dto);
        Facility facility = facilityRepository.getOne(facilityId);
        Child child = childRepository.getOne(dto.getChildId());
        childFollowup.setChild(child);
        childFollowup.setFacility(facility);
        ChildFollowup result = childFollowupRepository.save(childFollowup);
        return  result;
    }
    public ChildFollowup updateChildFollowup(ChildFollowupDTO dto){
        ChildFollowup childFollowup = childFollowupMapper.dtoToChildFollowup(dto);
        return childFollowupRepository.save(childFollowup);
    }

    public  void  delete(long childFollowupId,  String hospitalNum, String dateVisit){
        childFollowupRepository.deleteById(childFollowupId);
        MonitorService.logEntity(hospitalNum + "#" + dateVisit, "childfollowup", 3);
    }
    public List<ChildFollowupDTO> findChildFollowup(long childId, LocalDate dateVisit){
        Child child = childRepository.getOne(childId);
        List<ChildFollowup> followups = childFollowupRepository.findByChildAndDateVisit(child, dateVisit);
        return childFollowupMapper.childFollowupToDto(followups);
    }
    public List<ChildDTO> findChilld(Long childId){
        Child child = childRepository.getOne(childId);
        return childMapper.childToDTO(Collections.singletonList(child));
    }

    public ChildFollowupGridDTO childFollowupGrid(Long facilityId, Integer page, Long childId){
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
        ChildFollowupGridDTO dto = new ChildFollowupGridDTO();
        dto.setChildfollowupList(dtos);
        dto.setTotalrecords(count);
        dto.setCurrpage(page);
        return  dto;
    }

}
