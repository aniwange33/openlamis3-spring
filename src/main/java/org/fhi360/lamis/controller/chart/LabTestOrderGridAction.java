/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.LabTest;
import org.fhi360.lamis.model.dto.LabTestListDto;
import org.fhi360.lamis.model.repositories.LabTestRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Idris
 */

@RestController
@RequestMapping("/api/labtest")
@Api(tags = "LabTestOrderGridAction Chart", description = " ")
public class LabTestOrderGridAction {
    private final LabTestRepository labTestRepository;

    public LabTestOrderGridAction(LabTestRepository labTestRepository) {
        this.labTestRepository = labTestRepository;
    }

    @GetMapping("/grid")
    public ResponseEntity getAllLabTests(@RequestParam(required = false, value = "selectedLabtest") String selectedLabtest) {
        List<LabTest> labTests = labTestRepository.findAll(Sort.by(Sort.Order.by("description")));
        LabTestListDto labTestListDto = new LabTestListDto();
        List<LabTestListDto> labTestListDtoList = new ArrayList<>();
        labTests.forEach(labTest -> {
            labTestListDto.setSelectedLabtest(selectedLabtest);
            labTestListDto.setLabtestId(labTest.getLabtestId());
            labTestListDto.setLabtestCategoryId(labTest.getLabTestCategory().getLabTestCategoryId());
            labTestListDto.setDescription(labTest.getDescription());
            labTestListDtoList.add(labTestListDto);
        });
        Map<String, Object> response = new HashMap<>();
        response.put("currpage", 1);
        response.put("labtestList", labTestListDtoList);
        return ResponseEntity.ok().body(response);

    }

}
