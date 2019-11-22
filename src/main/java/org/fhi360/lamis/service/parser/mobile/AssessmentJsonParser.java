/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.mobile;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.text.SimpleDateFormat;
import java.util.List;

import org.fhi360.lamis.model.Assessment;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.AssessmentRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.springframework.stereotype.Component;

/**
 *
 * @author idris
 */
@Component
public class AssessmentJsonParser {
private  final AssessmentRepository assessmentRepository;

    public AssessmentJsonParser(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    public void parserJson(String table, String content) {
        try {
            getObject(content).forEach(assessment -> {
                String hospitalNum = assessment.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, assessment.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                Facility facility = new Facility();
                facility.setId(assessment.getFacilityId());
                assessment.setFacility(facility);
                Assessment assessmentId = assessmentRepository.findByClientCodeAndFacility(assessment.getClientCode(),facility);
                if(assessmentId == null) {
                    assessmentRepository.save(assessment);
                }
                else {
                    assessment.setAssessmentId(assessmentId.getAssessmentId());
                    assessmentRepository.save(assessment);
                }

            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }





    private static List<Assessment> getObject(String content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return objectMapper.readValue(content, new TypeReference<List<Assessment>>() {
        });
    }


}
