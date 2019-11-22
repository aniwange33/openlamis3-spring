package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Lga;
import org.fhi360.lamis.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByState(State state);

    List<Facility> findByLga(Lga lga);

    @Query(nativeQuery = true, value = "SELECT DISTINCT state_id FROM facility " +
            "WHERE facility_id IN(SELECT DISTINCT facility_id FROM patient)")
    List<Long> getStatesForFacilitiesInPatient();

}
