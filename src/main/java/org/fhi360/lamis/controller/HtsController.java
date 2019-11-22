/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.fhi360.lamis.controller.mapstruct.HtsMapper;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Hts;
import org.fhi360.lamis.model.dto.HtsDto;
import org.fhi360.lamis.model.repositories.HtsRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.security.SecurityUtils;
import org.fhi360.lamis.service.impl.HtsService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author idris
 */
@RestController
@RequestMapping(value = "/api/hts")
@Api(tags = "HTS", description = " ")
@Slf4j
public class HtsController {
    private final HtsService htsService;
    private final HtsRepository htsRepository;
    private final HtsMapper htsMapper;

    public HtsController(HtsService htsService, HtsRepository htsRepository, HtsMapper htsMapper) {
        this.htsService = htsService;
        this.htsRepository = htsRepository;
        this.htsMapper = htsMapper;
    }

    @PostMapping
    @PutMapping
    public ResponseEntity saveHts(@RequestBody HtsDto htsDto) {
        htsService.save(htsMapper.dtoToHts(htsDto));
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        htsService.delete(id);
    }


    @GetMapping(value = "/find-client-code")
    public ResponseEntity findHtsByClientCode(@RequestParam(value = "clientCode") String clientCode) {
        Facility facility = SecurityUtils.getCurrentFacility();
        HtsDto hts = htsService.findByClientCode(facility, clientCode);
        return ResponseEntity.ok(hts);
    }

    @GetMapping("/{id}")
    public ResponseEntity getHts(@PathVariable Long id) {
        Hts hts = htsRepository.getOne(id);
        HtsDto htsDtoList = htsMapper.htsToDto(hts);
        return ResponseEntity.ok(Collections.singleton(htsDtoList));
    }

    @GetMapping("/client-code/{clienCode}")
    public ResponseEntity getHtsByClientCode(@PathVariable String clientCode) {
        Facility facility = SecurityUtils.getCurrentFacility();
        Hts hts = htsRepository.findByFacilityAndClientCode(facility, clientCode);
        HtsDto htsDtoList = htsMapper.htsToDto(hts);
        return ResponseEntity.ok(Collections.singleton(htsDtoList));
    }

    @GetMapping("/facility")
    public ResponseEntity getHtsByFacility() {
        Facility facility = SecurityUtils.getCurrentFacility();
        List<Hts> hts = htsRepository.findByFacility(facility);
        List<HtsDto> htsDtoList = htsMapper.listHtsToDto(hts);
        return ResponseEntity.ok(htsDtoList);
    }

    @GetMapping("/grid")
    public ResponseEntity htsGrid(@RequestParam("rows") Integer rows, @RequestParam("page") Integer page) {
        if (page > 0) {
            page--;
        }
        //Facility facility = SecurityUtils.getCurrentFacility().get();
        Facility facility = new Facility();
        facility.setId(1190L);
        List<HtsDto> htsDtoList = htsService.htsGrid(facility, rows, page);
        Map<String, Object> response = new HashMap<>();
        response.put("htsList", htsDtoList);
        LOG.info("htsList {}", htsDtoList);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/find-by-names")
    public ResponseEntity findHtsByNames(@RequestParam(value = "surname") String surname,
                                            @RequestParam(value = "otherNames") String otherNames,
                                            @RequestParam(value = "gender") String gender,
                                            @RequestParam(value = "dateBirth") LocalDate dateBirth) {
        Facility facililty = SecurityUtils.getCurrentFacility();
        HtsDto htsDto = htsService.findHtsByNames(facililty, surname, otherNames, gender, dateBirth);
        return ResponseEntity.ok(Collections.singletonList(htsDto));
    }


    @GetMapping("/all")
    public ResponseEntity findAll() {
        List<Hts> hts = htsRepository.findAll(Sort.by(Sort.Order.by("htsId")));
        return ResponseEntity.ok(Collections.singletonList(hts));
    }

}
