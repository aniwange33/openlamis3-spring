package org.fhi360.lamis.controller.mapstruct;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.fhi360.lamis.model.Anc;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.MaternalFollowup;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.MaternalFollowupDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-21T15:49:45+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class MaternalFollowupMapperImpl implements MaternalFollowupMapper {

    @Override
    public MaternalFollowupDTO modelToDto(MaternalFollowup followup) {
        if ( followup == null ) {
            return null;
        }

        MaternalFollowupDTO maternalFollowupDTO = new MaternalFollowupDTO();

        if ( followup.getDateSampleCollected() != null ) {
            maternalFollowupDTO.setDateSampleCollected( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( followup.getDateSampleCollected() ) );
        }
        Long id = followupFacilityId( followup );
        if ( id != null ) {
            maternalFollowupDTO.setFacilityId( String.valueOf( id ) );
        }
        if ( followup.getDateArvRegimenCurrent() != null ) {
            maternalFollowupDTO.setDateArvRegimenCurrent( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( followup.getDateArvRegimenCurrent() ) );
        }
        Long patientId = followupPatientPatientId( followup );
        if ( patientId != null ) {
            maternalFollowupDTO.setPatientId( String.valueOf( patientId ) );
        }
        maternalFollowupDTO.setHospitalNum( followupPatientHospitalNum( followup ) );
        maternalFollowupDTO.setTimeHivDiagnosis( followup.getTimeHivDiagnosis() );
        Long ancId = followupAncAncId( followup );
        if ( ancId != null ) {
            maternalFollowupDTO.setAncId( String.valueOf( ancId ) );
        }
        if ( followup.getDateConfirmedHiv() != null ) {
            maternalFollowupDTO.setDateConfirmedHiv( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( followup.getDateConfirmedHiv() ) );
        }
        if ( followup.getDateNextVisit() != null ) {
            maternalFollowupDTO.setDateNextVisit( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( followup.getDateNextVisit() ) );
        }
        if ( followup.getMaternalfollowupId() != null ) {
            maternalFollowupDTO.setMaternalfollowupId( String.valueOf( followup.getMaternalfollowupId() ) );
        }
        if ( followup.getDateVisit() != null ) {
            maternalFollowupDTO.setDateVisit( DateTimeFormatter.ISO_LOCAL_DATE.format( followup.getDateVisit() ) );
        }
        if ( followup.getBodyWeight() != null ) {
            maternalFollowupDTO.setBodyWeight( String.valueOf( followup.getBodyWeight() ) );
        }
        maternalFollowupDTO.setBp( followup.getBp() );
        if ( followup.getFundalHeight() != null ) {
            maternalFollowupDTO.setFundalHeight( String.valueOf( followup.getFundalHeight() ) );
        }
        maternalFollowupDTO.setFetalPresentation( followup.getFetalPresentation() );
        maternalFollowupDTO.setSyphilisTested( followup.getSyphilisTested() );
        maternalFollowupDTO.setSyphilisTestResult( followup.getSyphilisTestResult() );
        maternalFollowupDTO.setViralLoadCollected( followup.getViralLoadCollected() );
        maternalFollowupDTO.setCd4Ordered( followup.getCd4Ordered() );
        if ( followup.getCd4() != null ) {
            maternalFollowupDTO.setCd4( String.valueOf( followup.getCd4() ) );
        }
        if ( followup.getCounselNutrition() != null ) {
            maternalFollowupDTO.setCounselNutrition( String.valueOf( followup.getCounselNutrition() ) );
        }
        maternalFollowupDTO.setTbStatus( followup.getTbStatus() );
        maternalFollowupDTO.setVisitStatus( followup.getVisitStatus() );
        if ( followup.getCounselFeeding() != null ) {
            maternalFollowupDTO.setCounselFeeding( String.valueOf( followup.getCounselFeeding() ) );
        }
        if ( followup.getCounselFamilyPlanning() != null ) {
            maternalFollowupDTO.setCounselFamilyPlanning( String.valueOf( followup.getCounselFamilyPlanning() ) );
        }
        maternalFollowupDTO.setFamilyPlanningMethod( followup.getFamilyPlanningMethod() );
        maternalFollowupDTO.setReferred( followup.getReferred() );
        maternalFollowupDTO.setTypeOfVisit( followup.getTypeOfVisit() );
        maternalFollowupDTO.setGestationalAge( followup.getGestationalAge() );
        maternalFollowupDTO.setArvRegimenPast( followup.getArvRegimenPast() );
        maternalFollowupDTO.setArvRegimenCurrent( followup.getArvRegimenCurrent() );
        if ( followup.getScreenPostPartum() != null ) {
            maternalFollowupDTO.setScreenPostPartum( String.valueOf( followup.getScreenPostPartum() ) );
        }

        return maternalFollowupDTO;
    }

    @Override
    public List<MaternalFollowupDTO> modelToDto(List<MaternalFollowup> followups) {
        if ( followups == null ) {
            return null;
        }

        List<MaternalFollowupDTO> list = new ArrayList<MaternalFollowupDTO>( followups.size() );
        for ( MaternalFollowup maternalFollowup : followups ) {
            list.add( modelToDto( maternalFollowup ) );
        }

        return list;
    }

    private Long followupFacilityId(MaternalFollowup maternalFollowup) {
        if ( maternalFollowup == null ) {
            return null;
        }
        Facility facility = maternalFollowup.getFacility();
        if ( facility == null ) {
            return null;
        }
        Long id = facility.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long followupPatientPatientId(MaternalFollowup maternalFollowup) {
        if ( maternalFollowup == null ) {
            return null;
        }
        Patient patient = maternalFollowup.getPatient();
        if ( patient == null ) {
            return null;
        }
        Long patientId = patient.getPatientId();
        if ( patientId == null ) {
            return null;
        }
        return patientId;
    }

    private String followupPatientHospitalNum(MaternalFollowup maternalFollowup) {
        if ( maternalFollowup == null ) {
            return null;
        }
        Patient patient = maternalFollowup.getPatient();
        if ( patient == null ) {
            return null;
        }
        String hospitalNum = patient.getHospitalNum();
        if ( hospitalNum == null ) {
            return null;
        }
        return hospitalNum;
    }

    private Long followupAncAncId(MaternalFollowup maternalFollowup) {
        if ( maternalFollowup == null ) {
            return null;
        }
        Anc anc = maternalFollowup.getAnc();
        if ( anc == null ) {
            return null;
        }
        Long ancId = anc.getAncId();
        if ( ancId == null ) {
            return null;
        }
        return ancId;
    }
}
