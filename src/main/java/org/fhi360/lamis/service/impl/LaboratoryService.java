/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.impl;

import org.fhi360.lamis.model.Laboratory;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.LaboratoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * @author user10
 */

@Service
@Transactional
public class LaboratoryService {
    @Autowired
    private LaboratoryRepository laboratoryRepository;

    public Laboratory save(Laboratory laboratory) {
        return laboratoryRepository.save(laboratory);
    }

    public Laboratory update(Laboratory laboratory) {
        return laboratoryRepository.save(laboratory);
    }

    public void delete(Laboratory laboratory) {
        laboratoryRepository.delete(laboratory);
    }

    public Laboratory findByLaboratoryId(long laboratoryId) {
        return laboratoryRepository.getOne(laboratoryId);
    }


    public List<Laboratory> findByDateVisit(long patientId, LocalDate date) {
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        return laboratoryRepository.findByPatientAndDateReported(patient, date);
    }


    public List<Laboratory> findAll() {
        return laboratoryRepository.findAll();
    }

    public List<Laboratory> findByPatient(long patientId) {
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        return laboratoryRepository.findByPatient(patient);
    }

}
