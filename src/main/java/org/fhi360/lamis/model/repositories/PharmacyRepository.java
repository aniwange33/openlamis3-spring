package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.Pharmacy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
    List<Pharmacy> findByPatientAndDateVisit(Patient patient, LocalDate date);

    List<Pharmacy> findByPatient(Patient patient);

    List<Pharmacy> findByPatientAndFacilityOrderByDateVisit(Patient patient, Facility facility , Pageable pageable);

    default Pharmacy getLastRefillByPatient(Patient patient) {
        return getRefillVisits(patient);
    }

    @Query(nativeQuery = true, value = "select * from PHARMACY where PATIENT_ID = ?1 and " +
            "regimentype_id in(1, 2, 3, 4, 14) order by DATE_VISIT desc limit 1")
    Pharmacy getRefillVisits(Patient patient);
}
