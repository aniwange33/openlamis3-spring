/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.OiHistory;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.OiHistoryRepository;
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
public class OiHistoryJsonParser {
    private final OiHistoryRepository historyRepository;

    public OiHistoryJsonParser(OiHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public void parserJson(String table, String content) {
        try {
            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                OiHistory oihistory = getObject(record.toString());
                String hospitalNum = oihistory.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, oihistory.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                oihistory.setPatient(patient);
                Long id = ServerIDProvider.getOIHistoryId(hospitalNum, oihistory.getDateVisit(),
                        oihistory.getOi(), oihistory.getFacilityId());
                oihistory.setHistoryId(id);
                historyRepository.save(oihistory);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static OiHistory getObject(String content) {
        OiHistory oihistory = new OiHistory();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            oihistory = objectMapper.readValue(content, OiHistory.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return oihistory;
    }
}
