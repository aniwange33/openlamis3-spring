/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.RegimenHistory;
import org.fhi360.lamis.model.repositories.RegimenHistoryRepository;
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
public class RegimenHistoryJsonParser {
    private final RegimenHistoryRepository historyRepository;

    public RegimenHistoryJsonParser(RegimenHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public void parserJson(String table, String content) {
        try {
            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                RegimenHistory regimenhistory = getObject(record.toString());
                String hospitalNum = regimenhistory.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, regimenhistory.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                regimenhistory.setPatient(patient);
                Long id = ServerIDProvider.getRegimenId(hospitalNum, regimenhistory.getDateVisit(),
                        regimenhistory.getRegimenType(), regimenhistory.getRegimen(), regimenhistory.getFacilityId());
                regimenhistory.setHistoryId(id);
                historyRepository.save(regimenhistory);
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private static RegimenHistory getObject(String content) {
        RegimenHistory regimenhistory = new RegimenHistory();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            regimenhistory = objectMapper.readValue(content, RegimenHistory.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return regimenhistory;
    }
}
