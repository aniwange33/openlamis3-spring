package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.CaseManager;
import org.fhi360.lamis.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseManagerRepository extends JpaRepository<CaseManager, Long> {
    List<CaseManager> findByFacilityOrderByFullName(Facility facility);
}
