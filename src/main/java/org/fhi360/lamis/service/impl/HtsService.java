/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.impl;

import org.fhi360.lamis.controller.mapstruct.HtsMapper;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Hts;
import org.fhi360.lamis.model.dto.HtsDto;
import org.fhi360.lamis.model.repositories.HtsRepository;
import org.fhi360.lamis.service.MonitorService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Idris
 */
@Service
@Transactional
public class HtsService {

    private final HtsRepository htsRepository;
    private final HtsMapper htsMapper;


    public HtsService(HtsRepository htsRepository, HtsMapper htsMapper) {
        this.htsRepository = htsRepository;
        this.htsMapper = htsMapper;

    }

    public HtsDto save(Hts hts) {
        return htsMapper.htsToDto(htsRepository.save(hts));

    }

    public HtsDto update(Hts hts) {
        return htsMapper.htsToDto(htsRepository.save(hts));
    }

    public void delete(Long htsId) {
        Hts hts = htsRepository.getOne(htsId);
        String entityId = hts.getClientCode();
        htsRepository.deleteById(htsId);
        MonitorService.logEntity(entityId, "hts", 3);

    }

    public HtsDto findHts(long htcId) {
        Hts hts = htsRepository.getOne(htcId);
        return htsMapper.htsToDto(hts);

    }


    public HtsDto findByClientCode(Facility facility, String clientCode) {
        return htsMapper.htsToDto(htsRepository.findByFacilityAndClientCode(facility, clientCode));
    }

    public HtsDto findHtsByNames(Facility facility, String surname, String otherNames, String gender, LocalDate dateBirth) {
        return htsMapper.htsToDto(htsRepository.findByFacilityAndSurnameAndOtherNamesAndGenderAndDateBirth(facility, surname, otherNames, gender, dateBirth));
    }

    public List<HtsDto> htsGrid(Facility facility, Integer rows, Integer page) {
        List<Hts> hts = htsRepository.findByFacility(facility, PageRequest.of(page, rows));
        return htsMapper.listHtsToDto(hts);
    }
}

