package org.fhi360.lamis.controller.mapstruct;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.fhi360.lamis.model.Clinic;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.ClinicDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-21T15:49:45+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class ClinicMapperImpl implements ClinicMapper {

    @Override
    public Clinic dtoToClinic(ClinicDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Clinic clinic = new Clinic();

        clinic.setFacility( clinicDTOToFacility( dto ) );
        clinic.setPatient( clinicDTOToPatient( dto ) );
        clinic.setRegimenType( dto.getRegimentype() );
        if ( dto.getDateVisit() != null ) {
            clinic.setDateVisit( LocalDate.parse( dto.getDateVisit(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getNextAppointment() != null ) {
            clinic.setNextAppointment( LocalDate.parse( dto.getNextAppointment(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getLmp() != null ) {
            clinic.setLmp( LocalDate.parse( dto.getLmp(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        clinic.setHospitalNum( dto.getHospitalNum() );
        if ( dto.getFacilityId() != null ) {
            clinic.setFacilityId( Long.parseLong( dto.getFacilityId() ) );
        }
        if ( dto.getClinicId() != null ) {
            clinic.setClinicId( Long.parseLong( dto.getClinicId() ) );
        }
        clinic.setClinicStage( dto.getClinicStage() );
        clinic.setFuncStatus( dto.getFuncStatus() );
        clinic.setTbStatus( dto.getTbStatus() );
        if ( dto.getViralLoad() != null ) {
            clinic.setViralLoad( Double.parseDouble( dto.getViralLoad() ) );
        }
        if ( dto.getCd4() != null ) {
            clinic.setCd4( Double.parseDouble( dto.getCd4() ) );
        }
        if ( dto.getCd4p() != null ) {
            clinic.setCd4p( Double.parseDouble( dto.getCd4p() ) );
        }
        clinic.setRegimen( dto.getRegimen() );
        if ( dto.getBodyWeight() != null ) {
            clinic.setBodyWeight( Double.parseDouble( dto.getBodyWeight() ) );
        }
        if ( dto.getHeight() != null ) {
            clinic.setHeight( Double.parseDouble( dto.getHeight() ) );
        }
        if ( dto.getWaist() != null ) {
            clinic.setWaist( Double.parseDouble( dto.getWaist() ) );
        }
        clinic.setBp( dto.getBp() );
        clinic.setOiScreened( dto.getOiScreened() );
        clinic.setOiIds( dto.getOiIds() );
        clinic.setAdrScreened( dto.getAdrScreened() );
        clinic.setAdrIds( dto.getAdrIds() );
        clinic.setAdherenceLevel( dto.getAdherenceLevel() );
        clinic.setAdhereIds( dto.getAdhereIds() );
        clinic.setNotes( dto.getNotes() );
        clinic.setGestationalAge( dto.getGestationalAge() );

        clinic.setCommence( dto.getCommence() == "1" ? true : false );
        clinic.setBreastfeeding( dto.getBreastfeeding() == "1" ? true : false );
        clinic.setPregnant( dto.getPregnant() == "1" ? true : false );

        return clinic;
    }

    @Override
    public ClinicDTO clinicToDto(Clinic clinic) {
        if ( clinic == null ) {
            return null;
        }

        ClinicDTO clinicDTO = new ClinicDTO();

        clinicDTO.setRegimentype( clinic.getRegimenType() );
        Long id = clinicFacilityId( clinic );
        if ( id != null ) {
            clinicDTO.setFacilityId( String.valueOf( id ) );
        }
        Long patientId = clinicPatientPatientId( clinic );
        if ( patientId != null ) {
            clinicDTO.setPatientId( String.valueOf( patientId ) );
        }
        if ( clinic.getDateVisit() != null ) {
            clinicDTO.setDateVisit( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( clinic.getDateVisit() ) );
        }
        if ( clinic.getNextAppointment() != null ) {
            clinicDTO.setNextAppointment( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( clinic.getNextAppointment() ) );
        }
        if ( clinic.getLmp() != null ) {
            clinicDTO.setLmp( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( clinic.getLmp() ) );
        }
        if ( clinic.getClinicId() != null ) {
            clinicDTO.setClinicId( String.valueOf( clinic.getClinicId() ) );
        }
        clinicDTO.setHospitalNum( clinic.getHospitalNum() );
        clinicDTO.setClinicStage( clinic.getClinicStage() );
        clinicDTO.setGestationalAge( clinic.getGestationalAge() );
        clinicDTO.setFuncStatus( clinic.getFuncStatus() );
        clinicDTO.setTbStatus( clinic.getTbStatus() );
        if ( clinic.getViralLoad() != null ) {
            clinicDTO.setViralLoad( String.valueOf( clinic.getViralLoad() ) );
        }
        if ( clinic.getCd4() != null ) {
            clinicDTO.setCd4( String.valueOf( clinic.getCd4() ) );
        }
        if ( clinic.getCd4p() != null ) {
            clinicDTO.setCd4p( String.valueOf( clinic.getCd4p() ) );
        }
        clinicDTO.setRegimen( clinic.getRegimen() );
        if ( clinic.getBodyWeight() != null ) {
            clinicDTO.setBodyWeight( String.valueOf( clinic.getBodyWeight() ) );
        }
        if ( clinic.getHeight() != null ) {
            clinicDTO.setHeight( String.valueOf( clinic.getHeight() ) );
        }
        if ( clinic.getWaist() != null ) {
            clinicDTO.setWaist( String.valueOf( clinic.getWaist() ) );
        }
        clinicDTO.setBp( clinic.getBp() );
        clinicDTO.setOiScreened( clinic.getOiScreened() );
        clinicDTO.setOiIds( clinic.getOiIds() );
        clinicDTO.setAdrScreened( clinic.getAdrScreened() );
        clinicDTO.setAdrIds( clinic.getAdrIds() );
        clinicDTO.setAdherenceLevel( clinic.getAdherenceLevel() );
        clinicDTO.setAdhereIds( clinic.getAdhereIds() );
        clinicDTO.setNotes( clinic.getNotes() );

        clinicDTO.setCommence( clinic.getCommence() == null || !clinic.getCommence() ? "0" : "1" );
        clinicDTO.setBreastfeeding( clinic.getBreastfeeding() == null || !clinic.getBreastfeeding() ? "0" : "1" );
        clinicDTO.setPregnant( clinic.getPregnant() == null || !clinic.getPregnant() ? "1" : "0" );

        return clinicDTO;
    }

    @Override
    public List<ClinicDTO> clinicToDto(List<Clinic> clinic) {
        if ( clinic == null ) {
            return null;
        }

        List<ClinicDTO> list = new ArrayList<ClinicDTO>( clinic.size() );
        for ( Clinic clinic1 : clinic ) {
            list.add( clinicToDto( clinic1 ) );
        }

        return list;
    }

    protected Facility clinicDTOToFacility(ClinicDTO clinicDTO) {
        if ( clinicDTO == null ) {
            return null;
        }

        Facility facility = new Facility();

        if ( clinicDTO.getFacilityId() != null ) {
            facility.setId( Long.parseLong( clinicDTO.getFacilityId() ) );
        }

        return facility;
    }

    protected Patient clinicDTOToPatient(ClinicDTO clinicDTO) {
        if ( clinicDTO == null ) {
            return null;
        }

        Patient patient = new Patient();

        if ( clinicDTO.getPatientId() != null ) {
            patient.setPatientId( Long.parseLong( clinicDTO.getPatientId() ) );
        }

        return patient;
    }

    private Long clinicFacilityId(Clinic clinic) {
        if ( clinic == null ) {
            return null;
        }
        Facility facility = clinic.getFacility();
        if ( facility == null ) {
            return null;
        }
        Long id = facility.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long clinicPatientPatientId(Clinic clinic) {
        if ( clinic == null ) {
            return null;
        }
        Patient patient = clinic.getPatient();
        if ( patient == null ) {
            return null;
        }
        Long patientId = patient.getPatientId();
        if ( patientId == null ) {
            return null;
        }
        return patientId;
    }
}
