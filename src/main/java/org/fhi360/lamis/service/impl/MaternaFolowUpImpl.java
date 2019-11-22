/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.impl;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import org.fhi360.lamis.model.*;
import org.fhi360.lamis.model.repositories.MaternalFollowupRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author Idris
 */
@Service
@Transactional
public class MaternaFolowUpImpl implements MaternaFolowUpService {

    @Autowired
    private MaternalFollowupRepository maternalFollowupRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public ResponseEntity<?> save(MaternalFollowup maternalfollowup) {
        try {
            MaternalFollowup maternalFollowup = new MaternalFollowup();
            Patient patient = new Patient();
            patient.setPatientId(maternalfollowup.getPatient().getPatientId());
            maternalFollowup.setPatient(patient);
            Facility facility =new Facility();
            facility.setId(maternalfollowup.getFacility().getId());
            maternalFollowup.setFacility(facility);
            User user = new User();
            user.setId(maternalfollowup.getUser().getId());
            maternalFollowup.setUser(user);
            return new ResponseEntity<>(maternalFollowupRepository.save(maternalFollowup), ServerResponseStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerResponseStatus.FAILED);
        }

    }

    @Override
    public ResponseEntity<?> update(MaternalFollowup maternalfollowup) {
        try {
            MaternalFollowup maternalFollowup = new MaternalFollowup();
            Patient patient = new Patient();
            patient.setPatientId(maternalfollowup.getPatient().getPatientId());
            maternalFollowup.setPatient(patient);
            Facility facility =new Facility();
            facility.setId(maternalfollowup.getFacility().getId());
            maternalFollowup.setFacility(facility);
            User user = new User();
            user.setId(maternalfollowup.getUser().getId());
            maternalFollowup.setMaternalfollowupId(maternalfollowup.getMaternalfollowupId());
            return new ResponseEntity<>(maternalFollowupRepository.save(maternalFollowup), ServerResponseStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerResponseStatus.FAILED);
        }

    }

    @Override
    public ResponseEntity<?> delete(MaternalFollowup maternalfollowup) {

        try {
            MaternalFollowup maternalfollowup1 = maternalFollowupRepository.getOne(maternalfollowup.getMaternalfollowupId());
            if (maternalfollowup1 != null) {
                maternalFollowupRepository.delete(maternalfollowup1);
                return new ResponseEntity<>(ServerResponseStatus.CREATED);
            } else {
                return new ResponseEntity<>(ServerResponseStatus.FAILED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(ServerResponseStatus.FAILED);
        }

    }
    
    
    @Override
    public ResponseEntity<?> findAll() {

        try {
            List<MaternalFollowup> maternalfollowup1 = maternalFollowupRepository.findAll();
            if (maternalfollowup1.isEmpty()) {
                return new ResponseEntity<>(maternalfollowup1,ServerResponseStatus.OK);
            } else {
                return new ResponseEntity<>(ServerResponseStatus.FAILED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(ServerResponseStatus.FAILED);
        }

    }

    @Override
    public ResponseEntity<?> findMaternalfollowup(long patientId, LocalDate dateVisit) {
        HashMap hashMap = new HashMap<>();
        try {

        Patient patient = new Patient();
        patient.setPatientId(patientId);
            List<MaternalFollowup> maternalfollowup = maternalFollowupRepository.findByPatientAndDateVisit(patient, dateVisit);
            if (!maternalfollowup.isEmpty()) {
                patient = findPatient(patientId);
               hashMap.put("maternalFollowup", maternalfollowup);
               hashMap.put("patient", patient);

                return new ResponseEntity<>(maternalfollowup, ServerResponseStatus.OK);
            } else {
                return new ResponseEntity<>(ServerResponseStatus.FAILED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(ServerResponseStatus.FAILED);
        }

    }
    private Patient findPatient(long patientId) {
        Patient patient = patientRepository.getOne(patientId);
        if(patient.getCurrentStatus().equalsIgnoreCase("Pre-ART Transfer In") && (patient.getCurrentStatus().equalsIgnoreCase("HIV+ non ART") || patient.getCurrentStatus().equalsIgnoreCase("HIV exposed status unknown"))) {
        }
        else {
            if(patient.getCurrentStatus().equalsIgnoreCase("ART Transfer In") && (patient.getCurrentStatus().equalsIgnoreCase("HIV+ non ART") || patient.getCurrentStatus().equalsIgnoreCase("HIV exposed status unknown") || patient.getCurrentStatus().equalsIgnoreCase("ART Start"))) {
            }
            else {
                patient.setCurrentStatus(patient.getCurrentStatus());
                patient.setDateCurrentStatus(patient.getDateCurrentStatus());
            }


        }
        return  patient;

}
}
