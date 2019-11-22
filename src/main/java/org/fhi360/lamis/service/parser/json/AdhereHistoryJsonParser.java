/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.AdhereHistory;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.AdhereHistoryRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Component
public class AdhereHistoryJsonParser {
    private final AdhereHistoryRepository adhereHistoryRepository;

    public AdhereHistoryJsonParser(AdhereHistoryRepository adhereHistoryRepository) {
        this.adhereHistoryRepository = adhereHistoryRepository;
    }

    public void parserJson(String table, String content) {
        try {
            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                AdhereHistory adherehistory = getObject(record.toString());
                adherehistory.setTimeStamp(LocalDateTime.now());
                String hospitalNum = adherehistory.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum,
                        adherehistory.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                adherehistory.setPatient(patient);
                Long adrHistoryId = ServerIDProvider.getPatientDependantId("adherehistory", hospitalNum,
                        adherehistory.getDateVisit(), adherehistory.getFacilityId());
                adherehistory.setHistoryId(adrHistoryId);
                adhereHistoryRepository.save(adherehistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static AdhereHistory getObject(String content) {
        AdhereHistory adhere = new AdhereHistory();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            adhere = objectMapper.readValue(content, AdhereHistory.class);
        } catch (IOException ignored) {
        }
        return adhere;
    }

}
