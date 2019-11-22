/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.mobile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.*;
import org.fhi360.lamis.model.repositories.ChronicCareRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.springframework.stereotype.Component;

/**
 *
 * @author idris
 */
@Component
public class ChronicCareJsonParser {

private final ChronicCareRepository chronicCareRepository;

    public ChronicCareJsonParser(ChronicCareRepository chronicCareRepository) {
        this.chronicCareRepository = chronicCareRepository;
    }


    public void parserJson(String table, String content) {
        try {
            getObject(content).forEach(chronicCare -> {
                String hospitalNum = chronicCare.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, chronicCare.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                chronicCare.setPatient(patient);
                Facility facility = new Facility();
                facility.setId(chronicCare.getFacilityId());
                chronicCare.setFacility(facility);

                Long chronicCareId = ServerIDProvider.getPatientDependantId("chroniccare", chronicCare.getHospitalNum(),
                        chronicCare.getDateVisit(), chronicCare.getFacilityId());
                chronicCare.setChroniccareId(chronicCareId);
                chronicCareRepository.save(chronicCare);


            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static List<ChronicCare> getObject(String content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return objectMapper.readValue(content, new TypeReference<List<ChronicCare>>() {
        });
    }
}
