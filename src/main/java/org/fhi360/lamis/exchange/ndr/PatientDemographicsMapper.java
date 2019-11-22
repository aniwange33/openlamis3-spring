/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.ndr;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.exchange.ndr.schema.NoteType;
import org.fhi360.lamis.exchange.ndr.schema.PatientDemographicsType;

import org.fhi360.lamis.utility.DateUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.xml.datatype.DatatypeConfigurationException;

/**
 * @author user1
 */
public class PatientDemographicsMapper {

    public PatientDemographicsMapper() {
    }

    public PatientDemographicsType patientDemograhics(long patientId) {
        final PatientDemographicsType patient = new PatientDemographicsType();

        String query = "SELECT patient_id, hospital_num, facility_id, date_birth, gender, marital_status, education, " +
                "occupation, state, current_status, date_current_status FROM patient WHERE patient_id = " + patientId;
        ContextProvider.getBean(JdbcTemplate.class).query(query, rs -> {
            String gender = StringUtils.trimToEmpty(rs.getString("gender"));

            String maritalStatus = StringUtils.trimToEmpty(rs.getString("marital_status"));
            String education = StringUtils.trimToEmpty(rs.getString("education"));
            String occupation = StringUtils.trimToEmpty(rs.getString("occupation"));
            String state = StringUtils.trimToEmpty(rs.getString("state"));
            String facilityId = rs.getObject("facility_id") == null ? "" : Long.toString(rs.getLong("facility_id"));
            String hospitalNumber = StringUtils.trimToEmpty(rs.getString("hospital_num"));
            String currentStatus = StringUtils.trimToEmpty(rs.getString("current_status"));

            patient.setPatientIdentifier(facilityId.concat("_").concat(hospitalNumber)); //This is the EMR Identifier
//                patient.setPatientIdentifier(Long.toString(rs.getLong("id_uuid"))); //This is the EMR Identifier
            if (rs.getString("date_birth") != null && !rs.getString("date_birth").isEmpty()) {
                try {
                    patient.setPatientDateOfBirth(DateUtil.getXmlDate(rs.getDate("date_birth")));
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }
            }
            if (!gender.isEmpty()) {
                gender = CodeSetResolver.getCode("SEX", gender);
                if (!gender.isEmpty()) patient.setPatientSexCode(gender);
            }
            if (currentStatus.equalsIgnoreCase("Known Death")) {
                patient.setPatientDeceasedIndicator(false);
                try {
                    patient.setPatientDeceasedDate(DateUtil.getXmlDate(rs.getDate("date_current_status")));
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }
            }
            if (!education.isEmpty()) {
                education = CodeSetResolver.getCode("EDUCATIONAL_LEVEL", education);
                if (!education.isEmpty()) patient.setPatientEducationLevelCode(education);
            }
            if (!occupation.isEmpty()) {
                occupation = CodeSetResolver.getCode("OCCUPATION_STATUS", occupation);
                if (!occupation.isEmpty()) patient.setPatientOccupationCode(occupation);
            }
            if (!maritalStatus.isEmpty()) {
                maritalStatus = CodeSetResolver.getCode("MARITAL_STATUS", maritalStatus);
                if (!maritalStatus.isEmpty()) patient.setPatientMaritalStatusCode(maritalStatus);
            }
            if (!state.isEmpty()) {
                state = CodeSetResolver.getCode("STATES", state);
                if (!state.isEmpty()) patient.setStateOfNigeriaOriginCode(state);
            }
            patient.setTreatmentFacility(TreatmentFacility.getFacility(rs.getLong("facility_id")));
            //Add a Patient Note
            NoteType patientNote = new NoteType();
            patientNote.setNote("");
            patient.setPatientNotes(patientNote);
        });
        return patient;
    }

}
