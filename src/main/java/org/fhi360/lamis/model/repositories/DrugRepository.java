package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugRepository extends JpaRepository<Drug, Long> {
}
