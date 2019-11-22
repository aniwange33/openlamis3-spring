package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Clinic;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    Clinic findFirstByPatientOrderByDateVisitDesc(Patient patient);

    List<Clinic> findByPatientAndDateVisit(Patient patient, LocalDate date);

    List<Clinic> findByPatient(Patient patient, Pageable pageable);

    List<Clinic> findByPatientAndCommenceIsTrue(Patient patient);

    @Query(nativeQuery = true, value = "SELECT c FROM clinic c WHERE  c.patient_id = ?1 and c.commence = 1")
    Clinic getArtCommencement(Patient patient);

    @Query(nativeQuery = true, value = "SELECT c FROM clinic c WHERE patient_id = ?1 ORDER BY date_visit DESC LIMIT 1")
    Clinic getLastClinicVisit(Patient patient);
}
