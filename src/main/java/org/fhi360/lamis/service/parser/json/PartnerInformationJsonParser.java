/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.PartnerInformation;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.PartnerInformationRepository;
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
public class PartnerInformationJsonParser {
    private final PartnerInformationRepository informationRepository;

    public PartnerInformationJsonParser(PartnerInformationRepository informationRepository) {
        this.informationRepository = informationRepository;
    }

    public void parserJson(String table, String content) {
        try {
            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                PartnerInformation partnerInformation = getObject(record.toString());
                String hospitalNum = partnerInformation.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, partnerInformation.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                partnerInformation.setPatient(patient);
                Long id = ServerIDProvider.getPartnerInformationId(hospitalNum, partnerInformation.getFacilityId());
                partnerInformation.setPartnerinformationId(id);
                informationRepository.save(partnerInformation);
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private static PartnerInformation getObject(String content) {
        PartnerInformation partnerInformation = new PartnerInformation();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            partnerInformation = objectMapper.readValue(content, PartnerInformation.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return partnerInformation;
    }
}
