package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Eid;
import org.fhi360.lamis.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EidRepository extends JpaRepository<Eid, Long> {
    List<Eid> findByFacilityAndLabno(Facility facility, String labno);
}
