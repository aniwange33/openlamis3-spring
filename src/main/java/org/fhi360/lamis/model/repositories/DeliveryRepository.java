package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Delivery;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByPatientAndDateDelivery(Patient patient, LocalDate date);

    List<Delivery> findByPatient(Patient patient);
}
