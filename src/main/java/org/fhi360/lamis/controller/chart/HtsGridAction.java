/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;

import org.fhi360.lamis.controller.mapstruct.HtsMapper;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Hts;

import org.fhi360.lamis.model.dto.HtsDto;
import org.fhi360.lamis.model.repositories.HtsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author user10
 */
@RestController
@RequestMapping("/chart/hts")
@Api(tags = "Hts Grid Chart", description = " ")
public class HtsGridAction {

    private final HtsRepository htsRepository;
    private final HtsMapper htsMapper;

    public HtsGridAction(HtsRepository htsRepository, HtsMapper htsMapper) {
        this.htsRepository = htsRepository;
        this.htsMapper = htsMapper;
    }

    @GetMapping("htsGrid")
    public ResponseEntity<List<HtsDto>> htsGrid(@RequestParam("id") long facilityId, @RequestParam("rows") int rows, @RequestParam("page") int page) {
        Facility facility = new Facility();
        facility.setId(facilityId);
        List<Hts> hts = htsRepository.findByFacility(facility, PageRequest.of(page, rows));
        return ResponseEntity.ok().body(htsMapper.listHtsToDto(hts));
    }


}
