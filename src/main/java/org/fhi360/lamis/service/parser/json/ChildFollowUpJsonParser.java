/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Child;
import org.fhi360.lamis.model.ChildFollowup;
import org.fhi360.lamis.model.repositories.ChildFollowupRepository;
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
public class ChildFollowUpJsonParser {
private final ChildFollowupRepository childFollowupRepository;

    public ChildFollowUpJsonParser(ChildFollowupRepository childFollowupRepository) {
        this.childFollowupRepository = childFollowupRepository;
    }

    public void parserJson(String table, String content) {
        try {
            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                ChildFollowup childFollowUp = getObject(record.toString());
                Child child = new Child();
                Long childId = ServerIDProvider.getChildFollowupChildId(childFollowUp.getReferenceNum(),
                        childFollowUp.getFacilityId());
                child.setChildId(childId);
                childFollowUp.setChild(child);
                Long childFollowUpId = ServerIDProvider.getChildFollowupId(childFollowUp.getReferenceNum(),
                        childFollowUp.getDateVisit(), childFollowUp.getFacilityId());
                childFollowUp.setChildfollowupId(childFollowUpId);
                childFollowupRepository.save(childFollowUp);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static ChildFollowup getObject(String content) {
        ChildFollowup childFollowUp = new ChildFollowup();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            childFollowUp = objectMapper.readValue(content, ChildFollowup.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return childFollowUp;
    }
}
