/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Specimen;
import org.fhi360.lamis.model.repositories.SpecimenRepository;
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
public class SpecimenJsonParser {
    private final SpecimenRepository specimenRepository;

    public SpecimenJsonParser(SpecimenRepository specimenRepository) {
        this.specimenRepository = specimenRepository;
    }

    public void parserJson(String table, String content) {
        try {
            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                Specimen specimen = getObject(record.toString());
                Long specimenId = ServerIDProvider.getSpecimenId(specimen.getLabNo(), specimen.getFacilityId());
                specimen.setSpecimenId(specimenId);
                specimenRepository.save(specimen);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static Specimen getObject(String content) {
        Specimen specimen = new Specimen();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            specimen = objectMapper.readValue(content, Specimen.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return specimen;
    }
}
