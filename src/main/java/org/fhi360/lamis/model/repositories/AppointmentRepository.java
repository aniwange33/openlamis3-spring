package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Appointment;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Appointment findByPatientAndDateTracked(Patient patient, LocalDate date);

    Appointment findByFacilityAndPatientAndDateTracked(Facility facility,Patient patient, LocalDate date);

    List<Appointment> findByPatient(Patient patient);
}
