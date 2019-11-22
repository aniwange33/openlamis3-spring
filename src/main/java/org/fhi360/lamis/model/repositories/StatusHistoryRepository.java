package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.StatusHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {

    List<StatusHistory> findByPatient(Patient patient);

    List<StatusHistory> findByPatientAndDateCurrentStatus(Patient patient, LocalDate date);

    List<StatusHistory> findByFacility(Facility facility, Pageable pageable);

    Long countByFacility(Facility facility);

    default StatusHistory getCurrentStatusForPatient(Patient patient) {
        return findByPatient(patient).stream()
                .min((s1, s2) ->
                        s2.getDateCurrentStatus().compareTo(s1.getDateCurrentStatus())).orElse(null);
    }

    default StatusHistory getCurrentStatusForPatientAt(Patient patient, LocalDate date) {
        return findByPatient(patient).stream()
                .filter(s -> s.getDateCurrentStatus().isBefore(date))
                .min((s1, s2) ->
                        s2.getDateCurrentStatus().compareTo(s1.getDateCurrentStatus())).orElse(null);
    }
}
