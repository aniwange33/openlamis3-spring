/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.Pharmacy;
import org.fhi360.lamis.model.repositories.PharmacyRepository;
import org.fhi360.lamis.utility.DateUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/chart/pharmacyGridAction")
@Api(tags = "PharmacyGridAction Chart", description = " ")
public class PharmacyGridAction {
    private final PharmacyRepository pharmacyRepository;

    public PharmacyGridAction(PharmacyRepository pharmacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
    }

    @GetMapping("/pharmacyGrid")
    public ResponseEntity<?> pharmacyGrid(@RequestParam("id") long patientId, @RequestParam("id") long facilityId, @RequestParam("rows") int rows, @RequestParam("page") int page) {
        ArrayList pharmacyList = new ArrayList();
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        Facility facility = new Facility();
        facility.setId(facilityId);
        List<Pharmacy> pharmacies = pharmacyRepository.findByPatientAndFacilityOrderByDateVisit(patient, facility, PageRequest.of(page, rows));
        pharmacies.forEach(pharmacy -> {
            Map<String, String> map = new HashMap<>();
            map.put("pharmacyId", String.valueOf(pharmacy.getPharmacyId()));
            map.put("patientId", String.valueOf(pharmacy.getPatient().getPatientId()));
            map.put("facilityId", String.valueOf(pharmacy.getFacility().getId()));
            Date dateVisit = DateUtil.convertToDateViaSqlDate(pharmacy.getNextAppointment());
            map.put("dateVisit", DateUtil.parseDateToString(dateVisit, "yyyy/MM/ddd"));
            // map.put("description",  pharmacy.);
            map.put("duration", String.valueOf(pharmacy.getDuration()));
            /// map.put("dmocType", pharmacy.get);
            Date dateNextAppointment = DateUtil.convertToDateViaSqlDate(pharmacy.getNextAppointment());
            map.put("nextAppointment", DateUtil.parseDateToString(dateNextAppointment, "yyyy/MM/ddd"));
            pharmacyList.add(map);

        });
        return ResponseEntity.ok().body(pharmacyList);
    }

}
