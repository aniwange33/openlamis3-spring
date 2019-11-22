/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Eac;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.EacRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author Idris
 */
@Component
public class EacJsonParser {
    private final EacRepository eacRepository;

    public EacJsonParser(EacRepository eacRepository) {
        this.eacRepository = eacRepository;
    }

    public void parserJson(String table, String content) {
        try {
            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                Eac eac = getObject(record.toString());
                String hospitalNum = eac.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum,
                        eac.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                eac.setPatient(patient);

                Long id = ServerIDProvider.getPatientDependantId("eac", hospitalNum, eac.getDateEac1(),
                        eac.getFacilityId());
                eac.setEacId(id);
                eacRepository.save(eac);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static Eac getObject(String content) {
        Eac eac = new Eac();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            eac = objectMapper.readValue(content, Eac.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return eac;
    }
}
