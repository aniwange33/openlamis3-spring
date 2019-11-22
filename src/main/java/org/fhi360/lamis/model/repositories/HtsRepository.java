package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Hts;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HtsRepository extends JpaRepository<Hts, Long> {
    Hts findByFacilityAndClientCode(Facility facility, String code);
    List<Hts> findByFacility(Facility facility, Pageable pageable);
    List<Hts> findByFacility(Facility facility);

    Hts findByFacilityAndSurnameAndOtherNamesAndGenderAndDateBirth(Facility facility, String surname, String otherNames, String gender, LocalDate dateBirth);
}
