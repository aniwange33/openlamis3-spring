/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Laboratory;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.LaboratoryRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Idris
 */
@Component
public class LaboratoryJsonParser {
private final LaboratoryRepository laboratoryRepository;

    public LaboratoryJsonParser(LaboratoryRepository laboratoryRepository) {
        this.laboratoryRepository = laboratoryRepository;
    }

    public void parserJson(String table, String content) {
        try {
            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                Laboratory laboratory = getObject(record.toString());
                String hospitalNum = laboratory.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, laboratory.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                laboratory.setPatient(patient);
                Long laboratoryId = ServerIDProvider.getPatientDependantId("laboratory", hospitalNum,
                        laboratory.getDateReported(), laboratory.getFacilityId());
                laboratory.setLaboratoryId(laboratoryId);
                laboratoryRepository.save(laboratory);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static Laboratory getObject(String content) {
        Laboratory laboratory = new Laboratory();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            laboratory = objectMapper.readValue(content, Laboratory.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return laboratory;
    }
}
