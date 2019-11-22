/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Delivery;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.DeliveryRepository;
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
public class DeliveryJsonParser {
    private final DeliveryRepository deliveryRepository;

    public DeliveryJsonParser(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public void parserJson(String table, String content) {
        try {

            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                Delivery delivery = getObject(record.toString());
                String hospitalNum = delivery.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum,
                        delivery.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                delivery.setPatient(patient);

                Long deliveryId = ServerIDProvider.getPatientDependantId("delivery", hospitalNum, delivery.getDateDelivery(),
                        delivery.getFacilityId());
                delivery.setDeliveryId(deliveryId);
                deliveryRepository.save(delivery);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static Delivery getObject(String content) {
        Delivery delivery = new Delivery();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            delivery = objectMapper.readValue(content, Delivery.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return delivery;
    }
}
