package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.ChronicCare;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ChronicCareRepository extends JpaRepository<ChronicCare, Long> {
    ChronicCare findByPatientAndDateVisit(Patient patient, LocalDate date);

    List<ChronicCare> findByPatient(Patient patient);
}
