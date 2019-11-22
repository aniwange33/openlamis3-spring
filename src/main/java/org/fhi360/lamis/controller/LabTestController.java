/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.LabTest;
import org.fhi360.lamis.model.dto.LabTestListDto;
import org.fhi360.lamis.model.dto.RetrieveLabtestDto;
import org.fhi360.lamis.model.repositories.LabTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user10
 */
@RestController
@RequestMapping(value = "/labtest")
@Api(tags = "Labtest" , description = " ")
public class LabTestController {

    @Autowired
    private LabTestRepository labTestRepository;

    @RequestMapping(value = "/selectedLabTest",method = RequestMethod.POST)
    public ResponseEntity<List<LabTestListDto>>  retrieveLabtestMap(@RequestBody RetrieveLabtestDto retrieveLabtestDto) {
        LabTestListDto labTestListDto = new LabTestListDto();
       List<LabTestListDto> labTestListDtos = new ArrayList<>();
        List<LabTest> labTest = labTestRepository.findAll(Sort.by(Sort.Order.by("description")));
        labTest.forEach((labTest1) -> {
            labTestListDto.setLabtestId(labTest1.getLabtestId());
            labTestListDto.setDescription(labTest1.getDescription());
            labTestListDto.setLabtestCategoryId(labTest1.getLabTestCategory().getLabTestCategoryId());
            labTestListDto.setSelectedLabtest(retrieveLabtestDto.getSelectedLabtest());
            labTestListDtos.add(labTestListDto);
});
        return ResponseEntity.ok().body(labTestListDtos);
    }
    
}
