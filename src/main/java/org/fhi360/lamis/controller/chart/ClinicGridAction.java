/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.controller.mapstruct.ClinicMapper;
import org.fhi360.lamis.model.Clinic;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.ClinicDTO;
import org.fhi360.lamis.model.repositories.ClinicRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chart/clinic")
@Api(tags = "Clinic Grid Chart", description = " ")
public class ClinicGridAction {
    private final ClinicRepository clinicRepository;
    private final ClinicMapper clinicMapper;

    public ClinicGridAction(ClinicRepository clinicRepository, ClinicMapper clinicMapper) {
        this.clinicRepository = clinicRepository;
        this.clinicMapper = clinicMapper;
    }

    @GetMapping("/grid")
    public ResponseEntity clinicGrid(@RequestParam("id") Long patientId, @RequestParam("row") int row,@RequestParam("page") int page, HttpSession session) {
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        List<Clinic> clinics = clinicRepository.findByPatient(patient, PageRequest.of(page, row));
        List<ClinicDTO> dtos = clinicMapper.clinicToDto(clinics);
        session.setAttribute("clinicList", dtos);
        Map<String, Object> response = new HashMap<>();
        response.put("clinicList", dtos);
        return ResponseEntity.ok(response);
    }
}
