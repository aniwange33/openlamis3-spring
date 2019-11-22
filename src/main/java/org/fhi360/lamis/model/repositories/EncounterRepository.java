package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Encounter;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface EncounterRepository extends JpaRepository<Encounter, Long> {
    Encounter findByPatientAndDateVisit(Patient patient, LocalDate date);
    Encounter findByFacilityAndPatientAndDateVisit(Facility facility,Patient patient, LocalDate date);
}
