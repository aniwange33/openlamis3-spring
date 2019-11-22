package org.fhi360.lamis.controller.mapstruct;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.model.CaseManager;
import org.fhi360.lamis.model.CommunityPharmacy;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Lga;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.State;
import org.fhi360.lamis.model.dto.PatientDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-21T15:49:44+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class PatientMapperImpl implements PatientMapper {

    @Override
    public Patient dtoToPatient(PatientDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setFacility( patientDTOToFacility( dto ) );
        patient.setCaseManager( patientDTOToCaseManager( dto ) );
        if ( dto.getDateLastCd4() != null ) {
            patient.setDateLastCd4( LocalDate.parse( dto.getDateLastCd4(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateLastClinic() != null ) {
            patient.setDateLastClinic( LocalDate.parse( dto.getDateLastClinic(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateCurrentStatus() != null ) {
            patient.setDateCurrentStatus( LocalDate.parse( dto.getDateCurrentStatus(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateLastViralLoad() != null ) {
            patient.setDateLastViralLoad( LocalDate.parse( dto.getDateLastViralLoad(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateConfirmedHiv() != null ) {
            patient.setDateConfirmedHiv( LocalDate.parse( dto.getDateConfirmedHiv(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateNextClinic() != null ) {
            patient.setDateNextClinic( LocalDate.parse( dto.getDateNextClinic(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateEnrolledPmtct() != null ) {
            patient.setDateEnrolledPMTCT( LocalDate.parse( dto.getDateEnrolledPmtct(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateStarted() != null ) {
            patient.setDateStarted( LocalDate.parse( dto.getDateStarted(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateLastRefill() != null ) {
            patient.setDateLastRefill( LocalDate.parse( dto.getDateLastRefill(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateNextRefill() != null ) {
            patient.setDateNextRefill( LocalDate.parse( dto.getDateNextRefill(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateRegistration() != null ) {
            patient.setDateRegistration( LocalDate.parse( dto.getDateRegistration(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateBirth() != null ) {
            patient.setDateBirth( LocalDate.parse( dto.getDateBirth(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getTimeStamp() != null ) {
            patient.setTimeStamp( LocalDateTime.parse( dto.getTimeStamp() ) );
        }
        if ( dto.getFacilityId() != null ) {
            patient.setFacilityId( Long.parseLong( dto.getFacilityId() ) );
        }
        if ( dto.getPatientId() != null ) {
            patient.setPatientId( Long.parseLong( dto.getPatientId() ) );
        }
        patient.setUniqueId( dto.getUniqueId() );
        patient.setSurname( dto.getSurname() );
        patient.setOtherNames( dto.getOtherNames() );
        patient.setGender( dto.getGender() );
        if ( dto.getAge() != null ) {
            patient.setAge( Integer.parseInt( dto.getAge() ) );
        }
        patient.setAgeUnit( dto.getAgeUnit() );
        patient.setMaritalStatus( dto.getMaritalStatus() );
        patient.setEducation( dto.getEducation() );
        patient.setOccupation( dto.getOccupation() );
        patient.setAddress( dto.getAddress() );
        patient.setPhone( dto.getPhone() );
        patient.setNextKin( dto.getNextKin() );
        patient.setAddressKin( dto.getAddressKin() );
        patient.setPhoneKin( dto.getPhoneKin() );
        patient.setRelationKin( dto.getRelationKin() );
        patient.setEntryPoint( dto.getEntryPoint() );
        patient.setSourceReferral( dto.getSourceReferral() );
        patient.setTimeHivDiagnosis( dto.getTimeHivDiagnosis() );
        patient.setTbStatus( dto.getTbStatus() );
        if ( dto.getPregnant() != null ) {
            patient.setPregnant( Boolean.parseBoolean( dto.getPregnant() ) );
        }
        if ( dto.getBreastfeeding() != null ) {
            patient.setBreastfeeding( Boolean.parseBoolean( dto.getBreastfeeding() ) );
        }
        patient.setStatusRegistration( dto.getStatusRegistration() );
        patient.setEnrollmentSetting( dto.getEnrollmentSetting() );
        patient.setCurrentStatus( dto.getCurrentStatus() );
        patient.setRegimentype( dto.getRegimentype() );
        patient.setRegimen( dto.getRegimen() );
        patient.setLastClinicStage( dto.getLastClinicStage() );
        patient.setLastViralLoad( dto.getLastViralLoad() );
        patient.setLastCd4( dto.getLastCd4() );
        patient.setViralLoadType( dto.getViralLoadType() );
        if ( dto.getLastRefillDuration() != null ) {
            patient.setLastRefillDuration( Integer.parseInt( dto.getLastRefillDuration() ) );
        }
        patient.setLastRefillSetting( dto.getLastRefillSetting() );
        if ( dto.getSendMessage() != null ) {
            patient.setSendMessage( Boolean.parseBoolean( dto.getSendMessage() ) );
        }

        patient.setHospitalNum( StringUtils.leftPad(dto.getHospitalNum(), 7, "0") );

        return patient;
    }

    @Override
    public PatientDTO patientToDto(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        PatientDTO patientDTO = new PatientDTO();

        if ( patient.getDateEnrolledPMTCT() != null ) {
            patientDTO.setDateEnrolledPmtct( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( patient.getDateEnrolledPMTCT() ) );
        }
        Long id = patientFacilityId( patient );
        if ( id != null ) {
            patientDTO.setFacilityId( String.valueOf( id ) );
        }
        if ( patient.getDateLastCd4() != null ) {
            patientDTO.setDateLastCd4( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( patient.getDateLastCd4() ) );
        }
        if ( patient.getDateLastClinic() != null ) {
            patientDTO.setDateLastClinic( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( patient.getDateLastClinic() ) );
        }
        patientDTO.setLga( patientFacilityLgaName( patient ) );
        if ( patient.getDateCurrentStatus() != null ) {
            patientDTO.setDateCurrentStatus( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( patient.getDateCurrentStatus() ) );
        }
        if ( patient.getDateLastViralLoad() != null ) {
            patientDTO.setDateLastViralLoad( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( patient.getDateLastViralLoad() ) );
        }
        Long casemanagerId = patientCaseManagerCasemanagerId( patient );
        if ( casemanagerId != null ) {
            patientDTO.setCasemanagerId( String.valueOf( casemanagerId ) );
        }
        if ( patient.getDateConfirmedHiv() != null ) {
            patientDTO.setDateConfirmedHiv( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( patient.getDateConfirmedHiv() ) );
        }
        if ( patient.getDateNextClinic() != null ) {
            patientDTO.setDateNextClinic( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( patient.getDateNextClinic() ) );
        }
        if ( patient.getDateStarted() != null ) {
            patientDTO.setDateStarted( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( patient.getDateStarted() ) );
        }
        if ( patient.getDateLastRefill() != null ) {
            patientDTO.setDateLastRefill( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( patient.getDateLastRefill() ) );
        }
        if ( patient.getDateNextRefill() != null ) {
            patientDTO.setDateNextRefill( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( patient.getDateNextRefill() ) );
        }
        if ( patient.getDateRegistration() != null ) {
            patientDTO.setDateRegistration( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( patient.getDateRegistration() ) );
        }
        patientDTO.setState( patientFacilityStateName( patient ) );
        if ( patient.getDateBirth() != null ) {
            patientDTO.setDateBirth( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( patient.getDateBirth() ) );
        }
        Long communitypharmId = patientCommunityPharmacyCommunitypharmId( patient );
        if ( communitypharmId != null ) {
            patientDTO.setCommunitypharmId( String.valueOf( communitypharmId ) );
        }
        if ( patient.getPatientId() != null ) {
            patientDTO.setPatientId( String.valueOf( patient.getPatientId() ) );
        }
        patientDTO.setHospitalNum( patient.getHospitalNum() );
        patientDTO.setUniqueId( patient.getUniqueId() );
        patientDTO.setSurname( patient.getSurname() );
        patientDTO.setOtherNames( patient.getOtherNames() );
        patientDTO.setGender( patient.getGender() );
        if ( patient.getAge() != null ) {
            patientDTO.setAge( String.valueOf( patient.getAge() ) );
        }
        patientDTO.setAgeUnit( patient.getAgeUnit() );
        patientDTO.setMaritalStatus( patient.getMaritalStatus() );
        patientDTO.setAddress( patient.getAddress() );
        patientDTO.setPhone( patient.getPhone() );
        patientDTO.setEducation( patient.getEducation() );
        patientDTO.setOccupation( patient.getOccupation() );
        patientDTO.setNextKin( patient.getNextKin() );
        patientDTO.setAddressKin( patient.getAddressKin() );
        patientDTO.setPhoneKin( patient.getPhoneKin() );
        patientDTO.setRelationKin( patient.getRelationKin() );
        patientDTO.setEntryPoint( patient.getEntryPoint() );
        if ( patient.getPregnant() != null ) {
            patientDTO.setPregnant( String.valueOf( patient.getPregnant() ) );
        }
        if ( patient.getBreastfeeding() != null ) {
            patientDTO.setBreastfeeding( String.valueOf( patient.getBreastfeeding() ) );
        }
        patientDTO.setStatusRegistration( patient.getStatusRegistration() );
        patientDTO.setEnrollmentSetting( patient.getEnrollmentSetting() );
        patientDTO.setCurrentStatus( patient.getCurrentStatus() );
        patientDTO.setSourceReferral( patient.getSourceReferral() );
        patientDTO.setTimeHivDiagnosis( patient.getTimeHivDiagnosis() );
        if ( patient.getLastViralLoad() != null ) {
            patientDTO.setLastViralLoad( patient.getLastViralLoad() );
        }
        if ( patient.getLastCd4() != null ) {
            patientDTO.setLastCd4( patient.getLastCd4() );
        }
        patientDTO.setLastClinicStage( patient.getLastClinicStage() );
        patientDTO.setRegimentype( patient.getRegimentype() );
        patientDTO.setRegimen( patient.getRegimen() );
        patientDTO.setLastRefillSetting( patient.getLastRefillSetting() );
        if ( patient.getLastRefillDuration() != null ) {
            patientDTO.setLastRefillDuration( String.valueOf( patient.getLastRefillDuration() ) );
        }
        if ( patient.getSendMessage() != null ) {
            patientDTO.setSendMessage( String.valueOf( patient.getSendMessage() ) );
        }
        if ( patient.getTimeStamp() != null ) {
            patientDTO.setTimeStamp( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( patient.getTimeStamp() ) );
        }
        patientDTO.setTbStatus( patient.getTbStatus() );
        patientDTO.setViralLoadType( patient.getViralLoadType() );

        patientDTO.setName( patient.getSurname() + " " + patient.getOtherNames() );
        patientDTO.setDevolve( patient.getCommunityPharmacy() != null ? 1 : 0 );

        return patientDTO;
    }

    @Override
    public List<PatientDTO> patientToDto(List<Patient> patients) {
        if ( patients == null ) {
            return null;
        }

        List<PatientDTO> list = new ArrayList<PatientDTO>( patients.size() );
        for ( Patient patient : patients ) {
            list.add( patientToDto( patient ) );
        }

        return list;
    }

    protected Facility patientDTOToFacility(PatientDTO patientDTO) {
        if ( patientDTO == null ) {
            return null;
        }

        Facility facility = new Facility();

        if ( patientDTO.getFacilityId() != null ) {
            facility.setId( Long.parseLong( patientDTO.getFacilityId() ) );
        }

        return facility;
    }

    protected CaseManager patientDTOToCaseManager(PatientDTO patientDTO) {
        if ( patientDTO == null ) {
            return null;
        }

        CaseManager caseManager = new CaseManager();

        if ( patientDTO.getCasemanagerId() != null ) {
            caseManager.setCasemanagerId( Long.parseLong( patientDTO.getCasemanagerId() ) );
        }

        return caseManager;
    }

    private Long patientFacilityId(Patient patient) {
        if ( patient == null ) {
            return null;
        }
        Facility facility = patient.getFacility();
        if ( facility == null ) {
            return null;
        }
        Long id = facility.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String patientFacilityLgaName(Patient patient) {
        if ( patient == null ) {
            return null;
        }
        Facility facility = patient.getFacility();
        if ( facility == null ) {
            return null;
        }
        Lga lga = facility.getLga();
        if ( lga == null ) {
            return null;
        }
        String name = lga.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long patientCaseManagerCasemanagerId(Patient patient) {
        if ( patient == null ) {
            return null;
        }
        CaseManager caseManager = patient.getCaseManager();
        if ( caseManager == null ) {
            return null;
        }
        Long casemanagerId = caseManager.getCasemanagerId();
        if ( casemanagerId == null ) {
            return null;
        }
        return casemanagerId;
    }

    private String patientFacilityStateName(Patient patient) {
        if ( patient == null ) {
            return null;
        }
        Facility facility = patient.getFacility();
        if ( facility == null ) {
            return null;
        }
        State state = facility.getState();
        if ( state == null ) {
            return null;
        }
        String name = state.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long patientCommunityPharmacyCommunitypharmId(Patient patient) {
        if ( patient == null ) {
            return null;
        }
        CommunityPharmacy communityPharmacy = patient.getCommunityPharmacy();
        if ( communityPharmacy == null ) {
            return null;
        }
        Long communitypharmId = communityPharmacy.getCommunitypharmId();
        if ( communitypharmId == null ) {
            return null;
        }
        return communitypharmId;
    }
}
