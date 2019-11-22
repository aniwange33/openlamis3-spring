/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.AdrHistory;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.AdrHistoryRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Component
public class AdrHistoryJsonParser {
    private final AdrHistoryRepository adrHistoryRepository;

    public AdrHistoryJsonParser(AdrHistoryRepository adrHistoryRepository) {
        this.adrHistoryRepository = adrHistoryRepository;
    }

    public void parserJson(String table, String content) {
        try {

            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                AdrHistory adrhistory = getObject(record.toString());
                adrhistory.setTimeStamp(LocalDateTime.now());
                String hospitalNum = adrhistory.getHospitalNum();
                Long facilityId = adrhistory.getFacilityId();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum,
                        adrhistory.getFacility().getId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                adrhistory.setPatient(patient);
                Long adrHistoryId = ServerIDProvider.getPatientDependantId("adrhistory", hospitalNum,
                        adrhistory.getDateVisit(), facilityId);
                adrhistory.setHistoryId(adrHistoryId);
                adrHistoryRepository.save(adrhistory);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static AdrHistory getObject(String content) {
        AdrHistory adrhistory = new AdrHistory();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            adrhistory = objectMapper.readValue(content, AdrHistory.class);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return adrhistory;
    }

}
