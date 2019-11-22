
package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.MaternalFollowup;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.MaternalFollowupRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chart/maternalfollowupGridAction")
@Api(tags = "MaternalfollowupGridAction Chart", description = " ")
public class MaternalfollowupGridAction {

    private final MaternalFollowupRepository maternalFollowupRepository;

    public MaternalfollowupGridAction(MaternalFollowupRepository maternalFollowupRepository) {
        this.maternalFollowupRepository = maternalFollowupRepository;
    }


    @GetMapping("getAllLabTests")
    public ResponseEntity<List<MaternalFollowup>> maternalfollowupGrid(@RequestParam("id") long patientId,
                                                                       @RequestParam("id") long facilityId, @RequestParam("rows") int rows, @RequestParam("page") int
            page) {
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        Facility facility = new Facility();
        facility.setId(facilityId);
        return ResponseEntity.ok().body(maternalFollowupRepository.findByPatient(patient, PageRequest.of(page, rows)));
    }

}
