package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.AdhereHistory;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdhereHistoryRepository extends JpaRepository<AdhereHistory, Long> {
    List<AdhereHistory> findByPatient(Patient patient);
}
