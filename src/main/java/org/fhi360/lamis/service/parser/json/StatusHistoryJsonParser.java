/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.StatusHistory;
import org.fhi360.lamis.model.repositories.StatusHistoryRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author user10
 */
@Component
public class StatusHistoryJsonParser {
    private final StatusHistoryRepository historyRepository;

    public StatusHistoryJsonParser(StatusHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public void parserJson(String table, String content) {
        try {
            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                StatusHistory statusHistory = getObject(record.toString());
                String hospitalNum = statusHistory.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, statusHistory.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                statusHistory.setPatient(patient);
                Long id = ServerIDProvider.getStatusHistoryId(hospitalNum, statusHistory.getDateCurrentStatus(),
                        statusHistory.getCurrentStatus(), statusHistory.getFacilityId());
                statusHistory.setHistoryId(id);
                historyRepository.save(statusHistory);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static StatusHistory getObject(String content) {
        StatusHistory statushistory = new StatusHistory();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            statushistory = objectMapper.readValue(content, StatusHistory.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return statushistory;
    }
}
