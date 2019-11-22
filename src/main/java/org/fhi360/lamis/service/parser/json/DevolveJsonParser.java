/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Devolve;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.DevolveRepository;
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
public class DevolveJsonParser {
    private final DevolveRepository devolveRepository;

    public DevolveJsonParser(DevolveRepository devolveRepository) {
        this.devolveRepository = devolveRepository;
    }

    public void parserJson(String table, String content) {

        JSONObject jsonObj = new JSONObject(content);
        JSONArray jsonArray = jsonObj.optJSONArray(table);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject record = jsonArray.optJSONObject(i);
            Devolve devolve = getObject(record.toString());
            String hospitalNum = devolve.getHospitalNum();
            Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, devolve.getFacilityId());
            Patient patient = new Patient();
            patient.setPatientId(patientId);
            devolve.setPatient(patient);

            long devolveId = ServerIDProvider.getPatientDependantId("devolve", hospitalNum,
                    devolve.getDateDevolved(), devolve.getFacilityId());
            devolve.setDevolveId(devolveId);
            try {
                devolveRepository.save(devolve);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Devolve getObject(String content) {
        Devolve devolve = new Devolve();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            devolve = objectMapper.readValue(content, Devolve.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return devolve;
    }
}
