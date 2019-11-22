package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.MaternalFollowup;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface MaternalFollowupRepository extends JpaRepository<MaternalFollowup, Long> {
    List<MaternalFollowup> findByPatientAndDateVisit(Patient patient, LocalDate date);

    List<MaternalFollowup> findByPatient(Patient patient);

    List<MaternalFollowup> findByPatient(Patient patient, Pageable pageable);
}
