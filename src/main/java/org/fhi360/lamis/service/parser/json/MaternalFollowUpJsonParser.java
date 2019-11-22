/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Anc;
import org.fhi360.lamis.model.MaternalFollowup;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.MaternalFollowupRepository;
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
public class MaternalFollowUpJsonParser {
    private final MaternalFollowupRepository maternalFollowupRepository;

    public MaternalFollowUpJsonParser(MaternalFollowupRepository maternalFollowupRepository) {
        this.maternalFollowupRepository = maternalFollowupRepository;
    }

    public void parserJson(String table, String content) {
        try {
            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                MaternalFollowup maternalfollowup = getObject(record.toString());
                String hospitalNum = maternalfollowup.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, maternalfollowup.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                String ancNum = maternalfollowup.getAncNum();
                Long ancId = ServerIDProvider.getAncId(ancNum, maternalfollowup.getFacilityId());
                Anc anc = new Anc();
                anc.setAncId(ancId);
                maternalfollowup.setAnc(anc);
                maternalfollowup.setPatient(patient);
                Long id = ServerIDProvider.getPatientDependantId("maternalfollowup",
                        hospitalNum, maternalfollowup.getDateVisit(), maternalfollowup.getFacilityId());
                maternalfollowup.setMaternalfollowupId(id);
                maternalFollowupRepository.save(maternalfollowup);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static MaternalFollowup getObject(String content) {
        MaternalFollowup maternalfollowup = new MaternalFollowup();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            maternalfollowup = objectMapper.readValue(content, MaternalFollowup.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return maternalfollowup;
    }
}
