/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.TBScreenHistory;
import org.fhi360.lamis.model.repositories.TBScreenHistoryRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Idris
 */
@Component
public class TbScreenHistoryJsonParser {
    private final TBScreenHistoryRepository historyRepository;

    public TbScreenHistoryJsonParser(TBScreenHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public void parserJson(String table, String content) {
        try {
            getObject(content).forEach(screenHistory -> {
                String hospitalNum = screenHistory.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, screenHistory.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                screenHistory.setPatient(patient);
                Long id = ServerIDProvider.getScreenId("tbscreenhistory", screenHistory.getDateVisit(),
                        screenHistory.getDescription(), hospitalNum, screenHistory.getFacilityId());
                screenHistory.setHistoryId(id);
                historyRepository.save(screenHistory);
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static List<TBScreenHistory> getObject(String content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return objectMapper.readValue(content, new TypeReference<List<TBScreenHistory>>() {
        });
    }
}
