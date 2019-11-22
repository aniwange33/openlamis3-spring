package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.IndicatorValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndicatorValueRepository extends JpaRepository<IndicatorValue, Long> {
}
