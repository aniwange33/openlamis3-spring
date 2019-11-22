package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Child;
import org.fhi360.lamis.model.ChildFollowup;
import org.fhi360.lamis.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ChildFollowupRepository extends JpaRepository<ChildFollowup, Long> {
    List<ChildFollowup> findByChildAndDateVisit(Child child, LocalDate date);

    List<ChildFollowup> findByChild(Child child);

    List<ChildFollowup> findByFacility(Facility facility);

    Long countByFacility(Facility facility);

    Long countByChild(Child child);
}
