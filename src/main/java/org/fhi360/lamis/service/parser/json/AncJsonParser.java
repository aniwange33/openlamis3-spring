/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Anc;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.AncRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

/**
 *
 * @author Idris
 */
@Component
public class AncJsonParser {
private final AncRepository ancRepository;

    public AncJsonParser(AncRepository ancRepository) {
        this.ancRepository = ancRepository;
    }

    public void parserJson(String table, String content) {
        try {

            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                Anc anc = getObject(record.toString());
                anc.setTimeStamp(LocalDateTime.now());
                String hospitalNum = anc.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum,
                        anc.getFacilityId());Patient patient = new Patient();
                patient.setPatientId(patientId);
                anc.setPatient(patient);
                anc.setPatient(patient);
                ancRepository.save(anc);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static Anc getObject(String content) {
        Anc anc = new Anc();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            anc = objectMapper.readValue(content, Anc.class);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return anc;
    }

}
