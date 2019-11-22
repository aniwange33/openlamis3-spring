/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.PatientCaseManager;
import org.fhi360.lamis.model.repositories.PatientCaseManagerRepository;
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
public class PatientCaseManagerJsonParser {
    private final PatientCaseManagerRepository caseManagerRepository;

    public PatientCaseManagerJsonParser(PatientCaseManagerRepository caseManagerRepository) {
        this.caseManagerRepository = caseManagerRepository;
    }

    public void parserJson(String table, String content) {
        try {
            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                PatientCaseManager patientCaseManager = getObject(record.toString());
                String hospitalNum = patientCaseManager.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, patientCaseManager.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                patientCaseManager.setPatient(patient);
                Long id = ServerIDProvider.getPatientDependantId("patientcasemanager",
                        hospitalNum, patientCaseManager.getDateAssigned(), patientCaseManager.getFacilityId());
                patientCaseManager.setPatientcasemanagerId(id);
                caseManagerRepository.save(patientCaseManager);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static PatientCaseManager getObject(String content) {
        PatientCaseManager patientcasemanager = new PatientCaseManager();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            patientcasemanager = objectMapper.readValue(content, PatientCaseManager.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return patientcasemanager;
    }
}
