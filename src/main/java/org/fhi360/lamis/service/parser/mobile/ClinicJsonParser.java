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

import org.fhi360.lamis.model.Clinic;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.ClinicRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.springframework.stereotype.Component;

/**
 * @author idris
 */
@Component
public class ClinicJsonParser {
    private final ClinicRepository clinicRepository;

    public ClinicJsonParser(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }


    public void parserJson(String table, String content) {
        try {
            getObject(content).forEach(clinic -> {
                String hospitalNum = clinic.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, clinic.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                clinic.setPatient(patient);
                Facility facility = new Facility();
                facility.setId(clinic.getFacilityId());
                clinic.setFacility(facility);

                Long id = ServerIDProvider.getPatientDependantId("clinic", hospitalNum,
                        clinic.getDateVisit(), clinic.getFacilityId());
                clinic.setClinicId(id);
                clinicRepository.save(clinic);
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static List<Clinic> getObject(String content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return objectMapper.readValue(content, new TypeReference<List<Clinic>>() {
        });
    }

}
