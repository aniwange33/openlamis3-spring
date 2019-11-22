package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.DrugTherapy;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DrugTherapyRepository extends JpaRepository<DrugTherapy, Long> {
    DrugTherapy findByPatientAndDateVisit(Patient patient, LocalDate date);

    DrugTherapy findByFacilityAndPatientAndDateVisit(Facility facility, Patient patient, LocalDate date);

    List<DrugTherapy> findByPatient(Patient patient);
}
