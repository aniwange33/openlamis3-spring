package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Encounter;
import org.fhi360.lamis.model.Exchange;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
    Exchange findByFacility(Facility facility);

}
