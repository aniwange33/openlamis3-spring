/**
 * @author AALOZIE
 */
package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.Anc;
import org.fhi360.lamis.model.repositories.AncRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chart/anc")
@Api(tags = "ANC Grid Chart", description = " ")
public class AncGridAction {
    private final AncRepository ancRepository;
    private final PatientRepository patientRepository;

    public AncGridAction(AncRepository ancRepository, PatientRepository patientRepository) {
        this.ancRepository = ancRepository;
        this.patientRepository = patientRepository;
    }

    @GetMapping("/grid")
    public ResponseEntity ancGrid(@RequestParam("id") Long patientId) {
        List<Anc> ancs = ancRepository.findByPatient(patientRepository.getOne(patientId));
        Map<String, Object> response = new HashMap<>();
        response.put("ancList", ancs);
        return ResponseEntity.ok(response);
    }
}
