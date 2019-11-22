/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.chart;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;

import io.swagger.annotations.Api;
import org.fhi360.lamis.controller.mapstruct.LabTestMapper;
import org.fhi360.lamis.controller.mapstruct.LaboratoryMapper;
import org.fhi360.lamis.model.dto.LabDto;
import org.fhi360.lamis.model.dto.LabTestDTO;
import org.fhi360.lamis.model.dto.LaboratoryDTO;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.LabTest;
import org.fhi360.lamis.model.Laboratory;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.LabTestRepository;
import org.fhi360.lamis.model.repositories.LaboratoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chart/lab-result")
@Api(tags = "LabResult Grid Chart", description = " ")
public class LabResultGridAction {
    private final LaboratoryRepository laboratoryRepository;
    private final LaboratoryMapper laboratoryMapper;
    private final LabTestMapper labTestMapper;

    private final LabTestRepository labTestRepository;

    public LabResultGridAction(LaboratoryRepository laboratoryRepository, LaboratoryMapper laboratoryMapper, LabTestMapper labTestMapper, LabTestRepository labTestRepository) {
        this.laboratoryRepository = laboratoryRepository;
        this.laboratoryMapper = laboratoryMapper;
        this.labTestMapper = labTestMapper;
        this.labTestRepository = labTestRepository;
    }

    @GetMapping("/grid")
    public ResponseEntity labresultGrid(@Param("labtestId") long labtestId, HttpSession session) {
        ArrayList<LaboratoryDTO> laboratoryArrayList = new ArrayList<>();
        long[] labtestId1s = new long[10];
        boolean[] test = new boolean[0];
        if (session.getAttribute("labresultList") != null) {
            laboratoryArrayList = (ArrayList) session.getAttribute("labresultList");
        }
        ArrayList<LaboratoryDTO> finalLaboratoryArrayList = laboratoryArrayList;
        laboratoryArrayList.forEach(laboratoryDTO -> {
            labtestId1s[0] = laboratoryDTO.getLabtestId();
            if (labtestId1s[0] == labtestId) {
                finalLaboratoryArrayList.clear();
                test[0] = false;
            }
            if (test[0]) {
                test(labtestId1s[0]);
            }
            session.setAttribute("labresultList", finalLaboratoryArrayList);
        });
        return ResponseEntity.ok().build();
    }


    @GetMapping("/laboratoryGrid")
    public ResponseEntity laboratoryGrid(@RequestParam("id") long patientId, @RequestParam("rows") int rows, @RequestParam("page") int page, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        Facility facility = new Facility();
        facility.setId(facilityId);
        List<Laboratory> laboratories = laboratoryRepository.findByPatient(patient, PageRequest.of(page, rows));
        List<LabTest> labTests = laboratories.stream().map(Laboratory::getLabTest).collect(Collectors.toList());
        List<LaboratoryDTO> laboratoryArrayList = laboratoryMapper.laboratoryToDto(laboratories);
        List<LabTestDTO> labDtoArrayList = labTestMapper.latTestToDto(labTests);
        session.setAttribute("laboratoryList", laboratoryArrayList);
        session.setAttribute("labresultList", labDtoArrayList);
        return ResponseEntity.ok().build();
    }


    private void test(long id) {
        LabTest labTest = labTestRepository.getOne(id);
        LabDto labTestDTO = labTestMapper.labToDto(labTest);

    }

    @PostMapping("/update")
    public ResponseEntity updateLabresultList(@RequestBody LabTestDTO labTestDTO1, HttpSession session) {
        List<LabTestDTO> labDtoArrayList = null;
        // retrieve the defaulter list stored as an attribute in session object
        if (session.getAttribute("labresultList") != null) {
            labDtoArrayList = (ArrayList) session.getAttribute("labresultList");
        }

        final LabTestDTO[] labresultList = new LabTestDTO[]{null};
        labDtoArrayList.forEach(labTestDTO -> {
            String id = labTestDTO.getLabtestId() + "";
            if (id.equals(labTestDTO1.getLabtestId())) {
                labresultList[0].setResultab(labTestDTO1.getResultab());

                labresultList[0].setResultpc(labTestDTO1.getResultpc());

                labresultList[0].setComment(labTestDTO1.getComment());
                labresultList[0].setIndication(labTestDTO1.getIndication());

            }
        });
        session.setAttribute("labresultList", labresultList);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/labresultGridRetrieve")
    public ResponseEntity labresultGridRetrieve(HttpSession session, @RequestParam("row") int rows, @RequestParam("page") int page) {

        List<LabTest> labresultList;
        // retrieve the labresult record store in session attribute
        if (session.getAttribute("labresultList") != null) {
            labresultList = (ArrayList) session.getAttribute("labresultList");
        }
        return ResponseEntity.ok().build();
    }


}
