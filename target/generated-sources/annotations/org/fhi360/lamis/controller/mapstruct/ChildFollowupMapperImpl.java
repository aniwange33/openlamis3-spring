package org.fhi360.lamis.controller.mapstruct;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.fhi360.lamis.model.Child;
import org.fhi360.lamis.model.ChildFollowup;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.dto.ChildFollowupDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-21T15:49:45+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class ChildFollowupMapperImpl implements ChildFollowupMapper {

    @Override
    public ChildFollowup dtoToChildFollowup(ChildFollowupDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ChildFollowup childFollowup = new ChildFollowup();

        if ( dto.getDateSampleCollected() != null ) {
            childFollowup.setDateSampleCollected( LocalDate.parse( dto.getDateSampleCollected(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateCotrimInitiated() != null ) {
            childFollowup.setDateCotrimInitiated( LocalDate.parse( dto.getDateCotrimInitiated(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDatePcrResult() != null ) {
            childFollowup.setDatePcrResult( LocalDate.parse( dto.getDatePcrResult(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateRapidTest() != null ) {
            childFollowup.setDateRapidTest( LocalDate.parse( dto.getDateRapidTest(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateNvpInitiated() != null ) {
            childFollowup.setDateNvpInitiated( LocalDate.parse( dto.getDateNvpInitiated(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateVisit() != null ) {
            childFollowup.setDateVisit( LocalDate.parse( dto.getDateVisit(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateLinkedToArt() != null ) {
            childFollowup.setDateLinkedToArt( LocalDate.parse( dto.getDateLinkedToArt(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateSampleSent() != null ) {
            childFollowup.setDateSampleSent( LocalDate.parse( dto.getDateSampleSent(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateNextVisit() != null ) {
            childFollowup.setDateNextVisit( LocalDate.parse( dto.getDateNextVisit(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        childFollowup.setFacilityId( dto.getFacilityId() );
        if ( dto.getChildfollowupId() != null ) {
            childFollowup.setChildfollowupId( Long.parseLong( dto.getChildfollowupId() ) );
        }
        if ( dto.getAgeVisit() != null ) {
            childFollowup.setAgeVisit( Integer.parseInt( dto.getAgeVisit() ) );
        }
        if ( dto.getAgeNvpInitiated() != null ) {
            childFollowup.setAgeNvpInitiated( Integer.parseInt( dto.getAgeNvpInitiated() ) );
        }
        if ( dto.getAgeCotrimInitiated() != null ) {
            childFollowup.setAgeCotrimInitiated( Integer.parseInt( dto.getAgeCotrimInitiated() ) );
        }
        if ( dto.getBodyWeight() != null ) {
            childFollowup.setBodyWeight( Double.parseDouble( dto.getBodyWeight() ) );
        }
        if ( dto.getHeight() != null ) {
            childFollowup.setHeight( Double.parseDouble( dto.getHeight() ) );
        }
        childFollowup.setFeeding( dto.getFeeding() );
        childFollowup.setArv( dto.getArv() );
        childFollowup.setCotrim( dto.getCotrim() );
        childFollowup.setReasonPcr( dto.getReasonPcr() );
        childFollowup.setPcrResult( dto.getPcrResult() );
        childFollowup.setRapidTest( dto.getRapidTest() );
        childFollowup.setRapidTestResult( dto.getRapidTestResult() );
        childFollowup.setCaregiverGivenResult( dto.getCaregiverGivenResult() );
        childFollowup.setChildOutcome( dto.getChildOutcome() );
        childFollowup.setReferred( dto.getReferred() );
        childFollowup.setArvType( dto.getArvType() );
        childFollowup.setArvTiming( dto.getArvTiming() );

        return childFollowup;
    }

    @Override
    public ChildFollowupDTO childFollowupToDto(ChildFollowup childFollowup) {
        if ( childFollowup == null ) {
            return null;
        }

        ChildFollowupDTO childFollowupDTO = new ChildFollowupDTO();

        if ( childFollowup.getDateSampleCollected() != null ) {
            childFollowupDTO.setDateSampleCollected( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( childFollowup.getDateSampleCollected() ) );
        }
        childFollowupDTO.setFacilityId( childFollowupFacilityId( childFollowup ) );
        if ( childFollowup.getDateCotrimInitiated() != null ) {
            childFollowupDTO.setDateCotrimInitiated( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( childFollowup.getDateCotrimInitiated() ) );
        }
        if ( childFollowup.getDatePcrResult() != null ) {
            childFollowupDTO.setDatePcrResult( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( childFollowup.getDatePcrResult() ) );
        }
        if ( childFollowup.getDateRapidTest() != null ) {
            childFollowupDTO.setDateRapidTest( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( childFollowup.getDateRapidTest() ) );
        }
        if ( childFollowup.getDateNvpInitiated() != null ) {
            childFollowupDTO.setDateNvpInitiated( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( childFollowup.getDateNvpInitiated() ) );
        }
        if ( childFollowup.getDateVisit() != null ) {
            childFollowupDTO.setDateVisit( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( childFollowup.getDateVisit() ) );
        }
        if ( childFollowup.getDateLinkedToArt() != null ) {
            childFollowupDTO.setDateLinkedToArt( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( childFollowup.getDateLinkedToArt() ) );
        }
        if ( childFollowup.getDateSampleSent() != null ) {
            childFollowupDTO.setDateSampleSent( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( childFollowup.getDateSampleSent() ) );
        }
        childFollowupDTO.setChildId( childFollowupChildChildId( childFollowup ) );
        if ( childFollowup.getDateNextVisit() != null ) {
            childFollowupDTO.setDateNextVisit( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( childFollowup.getDateNextVisit() ) );
        }
        if ( childFollowup.getChildfollowupId() != null ) {
            childFollowupDTO.setChildfollowupId( String.valueOf( childFollowup.getChildfollowupId() ) );
        }
        if ( childFollowup.getAgeVisit() != null ) {
            childFollowupDTO.setAgeVisit( String.valueOf( childFollowup.getAgeVisit() ) );
        }
        if ( childFollowup.getAgeNvpInitiated() != null ) {
            childFollowupDTO.setAgeNvpInitiated( String.valueOf( childFollowup.getAgeNvpInitiated() ) );
        }
        if ( childFollowup.getAgeCotrimInitiated() != null ) {
            childFollowupDTO.setAgeCotrimInitiated( String.valueOf( childFollowup.getAgeCotrimInitiated() ) );
        }
        if ( childFollowup.getBodyWeight() != null ) {
            childFollowupDTO.setBodyWeight( String.valueOf( childFollowup.getBodyWeight() ) );
        }
        if ( childFollowup.getHeight() != null ) {
            childFollowupDTO.setHeight( String.valueOf( childFollowup.getHeight() ) );
        }
        childFollowupDTO.setFeeding( childFollowup.getFeeding() );
        childFollowupDTO.setArv( childFollowup.getArv() );
        childFollowupDTO.setArvType( childFollowup.getArvType() );
        childFollowupDTO.setArvTiming( childFollowup.getArvTiming() );
        childFollowupDTO.setCotrim( childFollowup.getCotrim() );
        childFollowupDTO.setReasonPcr( childFollowup.getReasonPcr() );
        childFollowupDTO.setPcrResult( childFollowup.getPcrResult() );
        childFollowupDTO.setRapidTest( childFollowup.getRapidTest() );
        childFollowupDTO.setRapidTestResult( childFollowup.getRapidTestResult() );
        childFollowupDTO.setCaregiverGivenResult( childFollowup.getCaregiverGivenResult() );
        childFollowupDTO.setChildOutcome( childFollowup.getChildOutcome() );
        childFollowupDTO.setReferred( childFollowup.getReferred() );

        return childFollowupDTO;
    }

    @Override
    public List<ChildFollowupDTO> childFollowupToDto(List<ChildFollowup> childFollowup) {
        if ( childFollowup == null ) {
            return null;
        }

        List<ChildFollowupDTO> list = new ArrayList<ChildFollowupDTO>( childFollowup.size() );
        for ( ChildFollowup childFollowup1 : childFollowup ) {
            list.add( childFollowupToDto( childFollowup1 ) );
        }

        return list;
    }

    private Long childFollowupFacilityId(ChildFollowup childFollowup) {
        if ( childFollowup == null ) {
            return null;
        }
        Facility facility = childFollowup.getFacility();
        if ( facility == null ) {
            return null;
        }
        Long id = facility.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long childFollowupChildChildId(ChildFollowup childFollowup) {
        if ( childFollowup == null ) {
            return null;
        }
        Child child = childFollowup.getChild();
        if ( child == null ) {
            return null;
        }
        Long childId = child.getChildId();
        if ( childId == null ) {
            return null;
        }
        return childId;
    }
}
