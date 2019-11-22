/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.mobile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Encounter;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.EncounterRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author user1
 */
@Component
public class EncounterJsonParser {
    private final EncounterRepository encounterRepository;

    public EncounterJsonParser(EncounterRepository encounterRepository) {
        this.encounterRepository = encounterRepository;
    }


    public void parserJson(String table, String content) {
        try {
            getObject(content).forEach(encounter -> {
                String hospitalNum = encounter.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, encounter.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                encounter.setPatient(patient);
                Facility facility = new Facility();
                facility.setId(encounter.getFacilityId());
                encounter.setFacility(facility);
                Long id = ServerIDProvider.getPatientDependantId("encounter", hospitalNum, encounter.getDateVisit(),
                        encounter.getFacilityId());
                encounter.setEncounterId(id);
                encounterRepository.save(encounter);
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static List<Encounter> getObject(String content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return objectMapper.readValue(content, new TypeReference<List<Encounter>>() {
        });
    }

}
