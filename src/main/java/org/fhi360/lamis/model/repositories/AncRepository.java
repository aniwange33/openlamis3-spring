package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Anc;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface AncRepository extends JpaRepository<Anc, Long> {
    Anc findFirstByPatientOrderByDateVisitDesc(Patient patient);

    Anc findFirstByPatientOrderByDateVisit(Patient patient);

    List<Anc> findByPatientAndDateVisit(Patient patient, LocalDate date);

    List<Anc> findByPatient(Patient patient);

    List<Anc> findByFacility(Facility facility, Pageable pageable);

    Long countByFacility(Facility facility);

    List<Anc> findByPatientOrderByDateVisitDesc(Patient patient);
}
