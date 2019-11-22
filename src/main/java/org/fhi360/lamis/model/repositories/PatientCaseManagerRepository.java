package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.PatientCaseManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientCaseManagerRepository extends JpaRepository<PatientCaseManager, Long> {
}
