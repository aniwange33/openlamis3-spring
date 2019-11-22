package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.PartnerInformation;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerInformationRepository extends JpaRepository<PartnerInformation, Long> {
    List<PartnerInformation> findByPatient(Patient patient);

    List<PartnerInformation> findByFacility(Facility facility);
}
