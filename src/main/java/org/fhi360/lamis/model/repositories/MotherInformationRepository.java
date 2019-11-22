package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.MotherInformation;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MotherInformationRepository extends JpaRepository<MotherInformation, Long> {
    MotherInformation findByPatient(Patient patient);

    List<MotherInformation> findByFacility(Facility facility);
}
