/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.Pharmacy;
import org.fhi360.lamis.model.RegimenDrug;
import org.fhi360.lamis.model.repositories.PharmacyRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * @author Idris
 */
@Component
public class PharmacyJsonParser {
    private final PharmacyRepository pharmacyRepository;

    public PharmacyJsonParser(PharmacyRepository pharmacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
    }

    public void parserJson(String table, String content) {
        try {
            JSONObject jsonObj = new JSONObject(content);
            JSONArray jsonArray = jsonObj.optJSONArray(table);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.optJSONObject(i);
                Pharmacy pharmacy = getObject(record.toString());
                String hospitalNum = pharmacy.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, pharmacy.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                pharmacy.setPatient(patient);
                RegimenDrug regimenDrug = new RegimenDrug();
                regimenDrug.setRegimendrugId(pharmacy.getRegimendrugId());
                pharmacy.setRegimenDrug(regimenDrug);
                Long id = ServerIDProvider.getPharmacyId(hospitalNum, pharmacy.getDateVisit(),
                        pharmacy.getRegimenDrug().getRegimendrugId(), pharmacy.getFacilityId());
                pharmacy.setPharmacyId(id);
                pharmacyRepository.save(pharmacy);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static Pharmacy getObject(String content) {
        Pharmacy pharmacy = new Pharmacy();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            pharmacy = objectMapper.readValue(content, Pharmacy.class);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return pharmacy;
    }
}
