package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Indicator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndicatorRepository extends JpaRepository<Indicator, Long> {
}
