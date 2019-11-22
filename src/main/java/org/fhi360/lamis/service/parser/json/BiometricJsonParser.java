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
import java.io.IOException;

import java.text.SimpleDateFormat;
import javax.xml.bind.JAXBException;

import org.fhi360.lamis.model.Biometric;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 *
 * @author Idris
 */
@Component
public class BiometricJsonParser {

    public void parserJson(String table, String content) {
        try {

            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                Biometric biometric = getObject(record.toString());
                
//                String biometricId = new BiometricJDBC().getBiomtricId(biometric.getId(), Long.parseLong(biometric.getId()), biometric.getHospitalNum());
//                if (biometricId == null) {
//                    BiometricDAO.save(biometric);
//                } else {
//                    biometric.setRegimentypeId(biometricId);
//                    BiometricDAO.save(biometric);
//                }
            }
        } catch (IOException | NumberFormatException | JAXBException | JSONException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static Biometric getObject(String content) throws JAXBException, JsonParseException, JsonMappingException, IOException {
        Biometric biometric = new Biometric();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            biometric = objectMapper.readValue(content, Biometric.class);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return biometric;
    }

}
