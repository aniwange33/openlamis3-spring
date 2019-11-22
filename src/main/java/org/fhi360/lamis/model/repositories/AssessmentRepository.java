package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Assessment;
import org.fhi360.lamis.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    Assessment findByClientCodeAndFacility(String clientCode, Facility facility);
}
