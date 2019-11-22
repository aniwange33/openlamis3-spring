package org.fhi360.lamis.controller.mapstruct;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.model.Anc;
import org.fhi360.lamis.model.PartnerInformation;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.AncDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-21T15:49:45+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class AncMapperImpl implements AncMapper {

    @Override
    public Anc dtoToAnc(AncDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Anc anc = new Anc();

        if ( dto.getDateVisit() != null ) {
            anc.setDateEnrolledPMTCT( LocalDate.parse( dto.getDateVisit(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateCd4() != null ) {
            anc.setDateCd4( LocalDate.parse( dto.getDateCd4(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateNextAppointment() != null ) {
            anc.setDateNextAppointment( LocalDate.parse( dto.getDateNextAppointment(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getEdd() != null ) {
            anc.setEdd( LocalDate.parse( dto.getEdd(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateArvRegimenCurrent() != null ) {
            anc.setDateArvRegimenCurrent( LocalDate.parse( dto.getDateArvRegimenCurrent(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateViralLoad() != null ) {
            anc.setDateViralLoad( LocalDate.parse( dto.getDateViralLoad(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateVisit() != null ) {
            anc.setDateVisit( LocalDate.parse( dto.getDateVisit(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateConfirmedHiv() != null ) {
            anc.setDateConfirmedHiv( LocalDate.parse( dto.getDateConfirmedHiv(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        anc.setAncId( dto.getAncId() );
        anc.setAncNum( dto.getAncNum() );
        anc.setUniqueId( dto.getUniqueId() );
        anc.setSourceReferral( dto.getSourceReferral() );
        if ( dto.getLmp() != null ) {
            anc.setLmp( LocalDate.parse( dto.getLmp() ) );
        }
        anc.setGestationalAge( dto.getGestationalAge() );
        anc.setGravida( dto.getGravida() );
        anc.setParity( dto.getParity() );
        anc.setTimeHivDiagnosis( dto.getTimeHivDiagnosis() );
        anc.setArvRegimenPast( dto.getArvRegimenPast() );
        anc.setArvRegimenCurrent( dto.getArvRegimenCurrent() );
        anc.setClinicStage( dto.getClinicStage() );
        anc.setFuncStatus( dto.getFuncStatus() );
        anc.setBodyWeight( dto.getBodyWeight() );
        anc.setCd4Ordered( dto.getCd4Ordered() );
        anc.setCd4( dto.getCd4() );
        anc.setNumberAncVisit( dto.getNumberAncVisit() );
        anc.setViralLoadTestDone( dto.getViralLoadTestDone() );
        anc.setSyphilisTested( dto.getSyphilisTested() );
        anc.setSyphilisTestResult( dto.getSyphilisTestResult() );
        anc.setSyphilisTreated( dto.getSyphilisTreated() );
        anc.setHepatitisBTested( dto.getHepatitisBTested() );
        anc.setHepatitisBTestResult( dto.getHepatitisBTestResult() );
        anc.setHepatitisCTested( dto.getHepatitisCTested() );
        anc.setHepatitisCTestResult( dto.getHepatitisCTestResult() );
        anc.setViralLoadResult( dto.getViralLoadResult() );
        anc.setHeight( dto.getHeight() );

        return anc;
    }

    @Override
    public AncDTO ancToDto(Anc anc) {
        if ( anc == null ) {
            return null;
        }

        AncDTO ancDTO = new AncDTO();

        if ( anc.getDateCd4() != null ) {
            ancDTO.setDateCd4( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( anc.getDateCd4() ) );
        }
        if ( anc.getDateNextAppointment() != null ) {
            ancDTO.setDateNextAppointment( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( anc.getDateNextAppointment() ) );
        }
        if ( anc.getEdd() != null ) {
            ancDTO.setEdd( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( anc.getEdd() ) );
        }
        if ( anc.getDateArvRegimenCurrent() != null ) {
            ancDTO.setDateArvRegimenCurrent( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( anc.getDateArvRegimenCurrent() ) );
        }
        if ( anc.getDateViralLoad() != null ) {
            ancDTO.setDateViralLoad( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( anc.getDateViralLoad() ) );
        }
        if ( anc.getDateVisit() != null ) {
            ancDTO.setDateVisit( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( anc.getDateVisit() ) );
        }
        if ( anc.getDateConfirmedHiv() != null ) {
            ancDTO.setDateConfirmedHiv( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( anc.getDateConfirmedHiv() ) );
        }
        ancDTO.setAncId( anc.getAncId() );
        ancDTO.setAncNum( anc.getAncNum() );
        ancDTO.setUniqueId( anc.getUniqueId() );
        if ( anc.getDateEnrolledPMTCT() != null ) {
            ancDTO.setDateEnrolledPMTCT( DateTimeFormatter.ISO_LOCAL_DATE.format( anc.getDateEnrolledPMTCT() ) );
        }
        ancDTO.setSourceReferral( anc.getSourceReferral() );
        if ( anc.getLmp() != null ) {
            ancDTO.setLmp( DateTimeFormatter.ISO_LOCAL_DATE.format( anc.getLmp() ) );
        }
        ancDTO.setGestationalAge( anc.getGestationalAge() );
        ancDTO.setGravida( anc.getGravida() );
        ancDTO.setParity( anc.getParity() );
        ancDTO.setTimeHivDiagnosis( anc.getTimeHivDiagnosis() );
        ancDTO.setArvRegimenPast( anc.getArvRegimenPast() );
        ancDTO.setArvRegimenCurrent( anc.getArvRegimenCurrent() );
        ancDTO.setClinicStage( anc.getClinicStage() );
        ancDTO.setFuncStatus( anc.getFuncStatus() );
        ancDTO.setBodyWeight( anc.getBodyWeight() );
        ancDTO.setCd4Ordered( anc.getCd4Ordered() );
        ancDTO.setCd4( anc.getCd4() );
        ancDTO.setNumberAncVisit( anc.getNumberAncVisit() );
        ancDTO.setViralLoadTestDone( anc.getViralLoadTestDone() );
        ancDTO.setSyphilisTested( anc.getSyphilisTested() );
        ancDTO.setSyphilisTestResult( anc.getSyphilisTestResult() );
        ancDTO.setSyphilisTreated( anc.getSyphilisTreated() );
        ancDTO.setHepatitisBTested( anc.getHepatitisBTested() );
        ancDTO.setHepatitisBTestResult( anc.getHepatitisBTestResult() );
        ancDTO.setHepatitisCTested( anc.getHepatitisCTested() );
        ancDTO.setHepatitisCTestResult( anc.getHepatitisCTestResult() );
        ancDTO.setViralLoadResult( anc.getViralLoadResult() );
        ancDTO.setHeight( anc.getHeight() );

        return ancDTO;
    }

    @Override
    public List<AncDTO> ancToDto(List<Anc> anc) {
        if ( anc == null ) {
            return null;
        }

        List<AncDTO> list = new ArrayList<AncDTO>( anc.size() );
        for ( Anc anc1 : anc ) {
            list.add( ancToDto( anc1 ) );
        }

        return list;
    }

    @Override
    public PartnerInformation dtoToPartnerInformation(AncDTO dto) {
        if ( dto == null ) {
            return null;
        }

        PartnerInformation partnerInformation = new PartnerInformation();

        partnerInformation.setPatient( ancDTOToPatient( dto ) );
        if ( dto.getPartnerinformationId() != null ) {
            partnerInformation.setPartnerinformationId( Long.parseLong( dto.getPartnerinformationId() ) );
        }
        partnerInformation.setPartnerHivStatus( dto.getPartnerHivStatus() );
        partnerInformation.setPartnerReferred( dto.getPartnerReferred() );
        if ( dto.getDateVisit() != null ) {
            partnerInformation.setDateVisit( LocalDate.parse( dto.getDateVisit(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }

        partnerInformation.setPartnerNotification( StringUtils.upperCase(dto.getPartnerNotification()) );

        return partnerInformation;
    }

    protected Patient ancDTOToPatient(AncDTO ancDTO) {
        if ( ancDTO == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setPatientId( ancDTO.getPatientId() );

        return patient;
    }
}
