/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.mobile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.DrugTherapy;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.DrugTherapyRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.springframework.stereotype.Component;

/**
 *
 * @author user10
 */
@Component
public class DrugtherapyJsonParser {

    private final DrugTherapyRepository drugTherapyRepository;

    public DrugtherapyJsonParser(DrugTherapyRepository drugTherapyRepository) {
        this.drugTherapyRepository = drugTherapyRepository;
    }


    public void parserJson(String table, String content) {
        try {
            getObject(content).forEach(drugTherapy -> {
                String hospitalNum = drugTherapy.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, drugTherapy.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                drugTherapy.setPatient(patient);
                Facility facility = new Facility();
                facility.setId(drugTherapy.getFacilityId());
                drugTherapy.setFacility(facility);
                DrugTherapy drugtherapyId = drugTherapyRepository.findByFacilityAndPatientAndDateVisit(facility, patient, drugTherapy.getDateVisit());
                if (drugtherapyId == null) {
                    drugTherapyRepository.save(drugTherapy);
                } else {
                    drugTherapy.setDrugTherapyId(drugtherapyId.getDrugTherapyId());
                    drugTherapyRepository.save(drugTherapy);
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static List<DrugTherapy> getObject(String content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return objectMapper.readValue(content, new TypeReference<List<DrugTherapy>>() {
        });
    }
}
