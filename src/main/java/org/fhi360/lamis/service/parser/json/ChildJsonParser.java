/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Child;
import org.fhi360.lamis.model.MotherInformation;
import org.fhi360.lamis.model.repositories.ChildRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Idris
 */
@Component
public class ChildJsonParser {
private final ChildRepository childRepository;

    public ChildJsonParser(ChildRepository childRepository) {
        this.childRepository = childRepository;
    }

    public void parserJson(String table, String content) {
        try {

            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                Child child = getObject(record.toString());
                MotherInformation motherInformation = new MotherInformation();
                Long motherId = ServerIDProvider.getChildMotherInformationId(child.getHospitalNum(), child.getFacilityId());
                motherInformation.setMotherinformationId(motherId);
                child.setMother(motherInformation);
                Long childId = ServerIDProvider.getChildId(child.getReferenceNum(), child.getFacilityId());
                child.setChildId(childId);
                childRepository.save(child);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static Child getObject(String content) {
        Child child = new Child();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            child = objectMapper.readValue(content, Child.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return child;
    }
}
