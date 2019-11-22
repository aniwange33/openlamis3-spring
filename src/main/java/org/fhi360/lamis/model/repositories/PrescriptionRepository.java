package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatient(Patient patient);
}
