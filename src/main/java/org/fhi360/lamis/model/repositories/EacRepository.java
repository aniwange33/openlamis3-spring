package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Eac;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface EacRepository extends JpaRepository<Eac, Long> {
    Eac findByPatientAndDateEac1(Patient patient, LocalDate date);

    Eac findFirstByPatientOrderByDateEac1Desc(Patient patient);

    List<Eac> findByPatientOrderByDateEac1Desc(Patient patient);

    List<Eac> findByPatient(Patient patient);
}
