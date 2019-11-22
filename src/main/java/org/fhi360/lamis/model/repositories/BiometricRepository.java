package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Biometric;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BiometricRepository extends JpaRepository<Biometric, String> {
    Optional<Biometric> findByPatient(Patient patient);
}
