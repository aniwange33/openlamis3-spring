package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.LabTest;
import org.fhi360.lamis.model.Laboratory;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LaboratoryRepository extends JpaRepository<Laboratory, Long> {
    List<Laboratory> findByPatientAndDateReported(Patient patient, LocalDate date);

    List<Laboratory> findByPatient(Patient patient, Pageable pageable);

    List<Laboratory> findByFacility(Facility facility, Pageable pageable);

    Laboratory findFirstByPatientAndLabTestOrderByDateReportedDesc(Patient patient, LabTest labTest);

    default Laboratory lastCD4Count(Patient patient, LabTest labTest) {
        return findFirstByPatientAndLabTestOrderByDateReportedDesc(patient, labTest);
    }

    List<Laboratory> findByPatient(Patient patient);

    List<Laboratory> findByPatientOrderByDateReported(Patient patient);

    @Query(nativeQuery = true, value = "SELECT l FROM " +
            "laboratory l WHERE l.patient_id = ?1 AND l.date_reported > ?2 AND " +
            "resultab = (SELECT MAX(resultab REGEXP('(^[0-9]+$)')) FROM laboratory WHERE   patient_id = ?1 AND labtest_id = 1)")
    Laboratory getHighestCd4AbsoluteAfterArt(Patient patient, LocalDate dateStarted);

}
