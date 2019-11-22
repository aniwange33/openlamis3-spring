/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Nigqual;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.NigqualRepository;
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
public class NigqualJsonParser {
    private final NigqualRepository nigqualRepository;

    public NigqualJsonParser(NigqualRepository nigqualRepository) {
        this.nigqualRepository = nigqualRepository;
    }

    public void parserJson(String table, String content) {
        try {
            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                Nigqual nigqual = getObject(record.toString());
                String hospitalNum = nigqual.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum,nigqual.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                nigqual.setPatient(patient);
                Long id = ServerIDProvider.getPatientDependantId("nigqual", hospitalNum,
                        nigqual.getReviewPeriodId(), nigqual.getFacilityId());
                nigqual.setNigqualId(id);
                nigqualRepository.save(nigqual);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static Nigqual getObject(String content) {
        Nigqual nigqual = new Nigqual();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            nigqual = objectMapper.readValue(content, Nigqual.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return nigqual;
    }
}
