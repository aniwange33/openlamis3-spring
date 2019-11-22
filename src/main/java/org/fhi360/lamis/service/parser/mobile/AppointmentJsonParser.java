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

import org.fhi360.lamis.model.Appointment;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.AppointmentRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.springframework.stereotype.Component;

/**
 * @author idris
 */
@Component
public class AppointmentJsonParser {
    private final AppointmentRepository appointmentRepository;

    public AppointmentJsonParser(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }


    public void parserJson(String table, String content) {
        try {
            getObject(content).forEach(appointment -> {
                String hospitalNum = appointment.getHospitalNum();
                Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, appointment.getFacilityId());
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                appointment.setPatient(patient);
                Facility facility = new Facility();
                facility.setId(appointment.getFacilityId());
                appointment.setFacility(facility);
                Appointment appointmentId = appointmentRepository.findByFacilityAndPatientAndDateTracked(facility, patient, appointment.getDateTracked());
                if (appointmentId == null) {
                    appointmentRepository.save(appointment);
                } else {
                    appointment.setAppointmentId(appointmentId.getAppointmentId());
                    appointmentRepository.save(appointment);
                }


            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    private static List<Appointment> getObject(String content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return objectMapper.readValue(content, new TypeReference<List<Appointment>>() {
        });
    }
}

