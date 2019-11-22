package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Child;
import org.fhi360.lamis.model.Delivery;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> findByPatient(Patient patient);

    @Query("select c from Child c where TRIM(LEADING '0' FROM c.referenceNum) = ?1")
    Child findByHospitalNumber(String hospitalNum);

    Long countByPatient(Patient patient);

    Long countByFacility(Facility facility);

    List<Child> findByFacility(Facility facility, Pageable pageable);

    List<Child> findByDelivery(Delivery delivery, Pageable pageable);
}
