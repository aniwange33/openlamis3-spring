package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.RegimenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegimenTypeRepository extends JpaRepository<RegimenType, Long> {
    @Query("SELECT r FROM RegimenType r WHERE r.id = 1 OR r.id = 3 ORDER BY r.description")
    List<RegimenType> firstRegimentType();

    @Query("SELECT r FROM RegimenType r WHERE r.id = 2 OR r.id = 4 ORDER BY  r.description")
    List<RegimenType> secondRegimentType();

    @Query("SELECT r FROM RegimenType r WHERE r.id <= 4 OR r.id= 14 ORDER BY r.description")
    List<RegimenType> commenceRegimentType();


}
