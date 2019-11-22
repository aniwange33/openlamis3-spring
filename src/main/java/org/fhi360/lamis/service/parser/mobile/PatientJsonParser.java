/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.mobile;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.List;

import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.springframework.stereotype.Component;

/**
 * @author user10
 */
@Component
public class PatientJsonParser {

    private final PatientRepository patientRepository;

    public PatientJsonParser(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public void parserJson(String table, String content) {
        try {
            getObject(content).forEach(patient1 -> {
                String hospitalNum = patient1.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, patient1.getFacilityId());
                patient1.setPatientId(patientId);
                patientRepository.save(patient1);
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    private static List<Patient> getObject(String content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return objectMapper.readValue(content, new TypeReference<List<Patient>>() {
        });
    }


}
