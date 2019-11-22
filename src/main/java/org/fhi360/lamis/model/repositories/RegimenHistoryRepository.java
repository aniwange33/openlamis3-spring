package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.Regimen;
import org.fhi360.lamis.model.RegimenHistory;
import org.fhi360.lamis.model.RegimenType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegimenHistoryRepository extends JpaRepository<RegimenHistory, Long> {
    RegimenHistory findByPatientAndRegimenTypeAndRegimen(Patient patient, RegimenType regimenType, Regimen regimen);

}
