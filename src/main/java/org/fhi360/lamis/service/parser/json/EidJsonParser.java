/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Eid;
import org.fhi360.lamis.model.repositories.EidRepository;
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
public class EidJsonParser {
    private final EidRepository eidRepository;

    public EidJsonParser(EidRepository eidRepository) {
        this.eidRepository = eidRepository;
    }

    public void parserJson(String table, String content) {
        try {
            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                Eid eid = getObject(record.toString());
                Long id = ServerIDProvider.getEidId(eid.getLabno(), eid.getFacilityId());
                eid.setEidId(id);
                eidRepository.save(eid);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static Eid getObject(String content) {
        Eid eid = new Eid();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            eid = objectMapper.readValue(content, Eid.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return eid;
    }
}
