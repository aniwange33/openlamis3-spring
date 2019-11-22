package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.CaseManager;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("select p from Patient p where p.facility = ?1 and trim(leading '0' from p.hospitalNum) = ?2")
    Patient findByFacilityAndHospitalNum(Facility facility, String hospitalNum);

    Patient findByUuid(String uuid);

    List<Patient> findByCaseManager(CaseManager caseManager);

    Long countByCaseManager(CaseManager caseManager);

    @Query("SELECT count(p) > 0 FROM Patient p WHERE p = ?1 AND p.viralLoadDueDate <= current_date")
    Boolean dueViralLoad(Patient patient);
}
