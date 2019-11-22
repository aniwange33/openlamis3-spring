/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.controller.mapstruct.LabTestMapper;
import org.fhi360.lamis.controller.mapstruct.LaboratoryMapper;
import org.fhi360.lamis.model.LabTest;
import org.fhi360.lamis.model.repositories.LabTestRepository;
import org.fhi360.lamis.model.repositories.LaboratoryRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/chart/laboratory/version")
@Api(tags = "LabResult Grid Chart", description = " ")
public class LabresultGridAction_version1 {
    private final LaboratoryRepository laboratoryRepository;
    private final LaboratoryMapper laboratoryMapper;
    private final LabTestMapper labTestMapper;

    private final LabTestRepository labTestRepository;

    public LabresultGridAction_version1(LaboratoryRepository laboratoryRepository, LaboratoryMapper laboratoryMapper, LabTestMapper labTestMapper, LabTestRepository labTestRepository) {
        this.laboratoryRepository = laboratoryRepository;
        this.laboratoryMapper = laboratoryMapper;
        this.labTestMapper = labTestMapper;
        this.labTestRepository = labTestRepository;
    }

    @GetMapping("/labresultGrid")
    public ResponseEntity labresultGrid(@RequestParam("labtestIds") long labtestIds, HttpSession session) {

        String ids = String.valueOf(labtestIds);
        String[] idList = ids.split(",");
        ArrayList labresultList = new ArrayList();
        for (String id : idList) {
            LabTest labTest = labTestRepository.getOne(Long.parseLong(id));
            String labtestId = Long.toString(labTest.getLabtestId());
            String description = labTest.getDescription();
            String resultab = "";
            String measureab = labTest.getMeasureAB();
            String resultpc = "";
            String measurepc = labTest.getMeasurePC();
            String comment = "";

            Map<String, String> map = new HashMap<>();
            map.put("labtestId", labtestId);
            map.put("description", description);
            map.put("resultab", resultab);
            map.put("measureab", measureab);
            map.put("resultpc", resultpc);
            map.put("measurepc", measurepc);
            map.put("comment", comment);

            labresultList.add(map);

        }
        session.setAttribute("labresultList", labresultList);

        return ResponseEntity.ok().build();

    }


    @PutMapping("/updateLabResultList")
    public ResponseEntity updateLabresultList(@RequestParam("labtestIds") long labtestIds, HttpSession session) {

        String ids = String.valueOf(labtestIds);
        String[] idList = ids.split(",");
        ArrayList labresultList = new ArrayList();
        for (String id : idList) {
            LabTest labTest = labTestRepository.getOne(Long.parseLong(id));
            String labtestId = Long.toString(labTest.getLabtestId());
            String description = labTest.getDescription();
            String resultab = "";
            String measureab = labTest.getMeasureAB();
            String resultpc = "";
            String measurepc = labTest.getMeasurePC();
            String comment = "";

            Map<String, String> map = new HashMap<>();
            map.put("labtestId", labtestId);
            map.put("description", description);
            map.put("resultab", resultab);
            map.put("measureab", measureab);
            map.put("resultpc", resultpc);
            map.put("measurepc", measurepc);
            map.put("comment", comment);

            labresultList.add(map);

        }
        session.setAttribute("labresultList", labresultList);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/labresultGrid1")
    public ResponseEntity labresultGrid1(@RequestParam("rows") int rows, @RequestParam("page") int page, HttpSession session) {
        ArrayList labresultList = null;
        if (session.getAttribute("labresultList") != null) {
            labresultList = (ArrayList) session.getAttribute("labresultList");
        }
        return  ResponseEntity.ok().build();
    }



}
