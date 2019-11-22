package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.AdrHistory;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdrHistoryRepository extends JpaRepository<AdrHistory, Long> {

    List<AdrHistory> findByPatient(Patient patient);
}
