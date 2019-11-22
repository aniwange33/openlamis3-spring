/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Validated;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author user10
 */
@Component
public class ValidatedJsonParser {
     
    public void parserJson(String table, String content) {
        try {

            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                Validated validated = getObject(record.toString());
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
             /*
                long validatedId = new ValidatedJDBC().getValidatedId(validated.getId(),
                        validated.getId(), dateFormat.format(validated.getDateValidated()));
                if (validatedId == 0L) {
                    ValidatedDAO.save(validated);
                } else {
                    validated.setValidatedId(validatedId);
                    ValidatedDAO.update(validated);
                }*/

            }
        } catch (IOException | JAXBException | JSONException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static Validated getObject(String content) throws JAXBException, JsonParseException, JsonMappingException, IOException {
        Validated validated = new Validated();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            validated = objectMapper.readValue(content, Validated.class);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return validated;
    }
}
