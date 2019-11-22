/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.chart;

import java.util.*;

import io.swagger.annotations.Api;
import org.fhi360.lamis.controller.mapstruct.StatusHistoryMapper;
import org.fhi360.lamis.model.dto.StatusHistoryDTO;
import org.fhi360.lamis.model.dto.StatusListDTO;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.StatusHistory;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.model.repositories.StatusHistoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/chart/statusHistoryGridAction")
@Api(tags = "StatusHistoryGridAction Chart", description = " ")
public class StatusHistoryGridAction {
    private final PatientRepository patientRepository;
    private final StatusHistoryRepository statusHistoryRepository;
    private final FacilityRepository facilityRepository;
    private final StatusHistoryMapper historyMapper;

    public StatusHistoryGridAction(PatientRepository patientRepository, StatusHistoryRepository statusHistoryRepository,
                                   FacilityRepository facilityRepository, StatusHistoryMapper historyMapper) {
        this.patientRepository = patientRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.facilityRepository = facilityRepository;
        this.historyMapper = historyMapper;
    }

    @GetMapping("/statusGrid")
    public ResponseEntity<?> statusGrid(@RequestParam("rows") Integer rows, @RequestParam("page") Integer page, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        Facility facility = facilityRepository.getOne(facilityId);
        List<StatusHistory> histories = statusHistoryRepository.findByFacility(facility, PageRequest.of(page, rows));
        List<StatusHistoryDTO> dtos = historyMapper.statusHistoryToDto(histories);
        StatusListDTO dto = new StatusListDTO();
        Long count = statusHistoryRepository.countByFacility(facility);
        dto.setStatusList(dtos);
        dto.setCurrpage(page);
        dto.setTotalrecords(count);
        dto.setTotalpages(count / rows);
        return ResponseEntity.ok(dto);
    }

}
