

package org.fhi360.lamis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import io.swagger.annotations.Api;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.State;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.model.repositories.StateRepository;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/facility")
@Api(tags = "Facility"  , description = " ")
public class FacilityController {
    private final FacilityRepository facilityRepository;
    private final StateRepository stateRepository;

    public FacilityController(FacilityRepository facilityRepository, StateRepository stateRepository) {
        this.facilityRepository = facilityRepository;
        this.stateRepository = stateRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getFacility() {
        return "facility";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@RequestBody Facility facility) {
        facilityRepository.save(facility);
        return "facility";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestBody Facility facility) {
        facilityRepository.delete(facility);
        return "facility";
    }

    @RequestMapping(value = "/findByFacilityId", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, ? extends Object> findByFacilityId(@RequestParam("id") long facilityId) {
        Facility facility = facilityRepository.getOne(facilityId);
        List<Object> facilityList = new ArrayList<>();
        facilityList.add(facility);
        Map<String, Object> map = new HashMap<>();
        map.put("facilityList", facilityList);
        return map;
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, ? extends Object> findAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("currpage", 1);
        map.put("facilityList", facilityRepository.findAll());
        return map;
    }


    @RequestMapping(value = "/findAllState", method = RequestMethod.GET)
    public ResponseEntity<List<State>> findAllState() {
        return ResponseEntity.ok().body(stateRepository.findAll());
    }

    @RequestMapping(value = "/findLgaByStateId", method = RequestMethod.GET)
    public ResponseEntity<State> findLgaByStateId(@RequestParam("stateId") String stateId) {
        return ResponseEntity.ok().body(stateRepository.getOne(Long.parseLong(stateId)));
    }

}
