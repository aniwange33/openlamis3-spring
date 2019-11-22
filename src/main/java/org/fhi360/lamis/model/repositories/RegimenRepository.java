package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Regimen;
import org.fhi360.lamis.model.RegimenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegimenRepository extends JpaRepository<Regimen, Long> {

    List<Regimen> findByRegimenType(RegimenType regimenType);

    List<Regimen> findByRegimenTypeIn(List<RegimenType> regimenTypes);

    @Query("SELECT r FROM Regimen r WHERE r.regimenType = ?1 ORDER BY r.description")
    Regimen findByRegimenType1(RegimenType regimenType);

    @Query("SELECT distinct r FROM Regimen r join r.regimenType t WHERE t.description = ?1 ORDER BY r.description")
    List<Regimen> retrieveRegimenByName(RegimenType regimenType);

    @Query("SELECT r FROM Regimen r WHERE r.regimenType = ?1 ORDER BY r.description")
    List<Regimen> findByRegimenTypes(RegimenType regimenType);

}
