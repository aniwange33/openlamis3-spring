package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Hts;
import org.fhi360.lamis.model.IndexContact;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndexContactRepository extends JpaRepository<IndexContact, Long> {
    IndexContact findByClientCode(String code);

    IndexContact findByClientCodeAndFacility(String code, Facility facility);

    List<IndexContact> findByHts(Hts hts, Pageable pageable);
}
