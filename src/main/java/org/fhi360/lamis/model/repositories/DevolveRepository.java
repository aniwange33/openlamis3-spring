package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Devolve;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface DevolveRepository extends JpaRepository<Devolve, Long> {
    Devolve findFirstByPatientOrderByDateDevolved(Patient patient);

    Devolve findFirstByPatientOrderByDateDevolvedDesc(Patient patient);

    Devolve findByPatientAndDateDevolved(Patient patient, LocalDate date);

    List<Devolve> findByPatient(Patient patient);
}
