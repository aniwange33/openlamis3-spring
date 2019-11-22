package org.fhi360.lamis.controller.mapstruct;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.fhi360.lamis.model.Anc;
import org.fhi360.lamis.model.Child;
import org.fhi360.lamis.model.Delivery;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.ChildDTO;
import org.fhi360.lamis.model.dto.DeliveryDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-21T15:49:45+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class DeliveryMapperImpl implements DeliveryMapper {

    @Override
    public Delivery dtoToDelivery(DeliveryDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Delivery delivery = new Delivery();

        delivery.setAnc( deliveryDTOToAnc( dto ) );
        delivery.setFacility( deliveryDTOToFacility( dto ) );
        delivery.setPatient( deliveryDTOToPatient( dto ) );
        if ( dto.getDateDelivery() != null ) {
            delivery.setDateDelivery( LocalDate.parse( dto.getDateDelivery(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateArvRegimenCurrent() != null ) {
            delivery.setDateArvRegimenCurrent( LocalDate.parse( dto.getDateArvRegimenCurrent(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateConfirmedHiv() != null ) {
            delivery.setDateConfirmedHiv( LocalDate.parse( dto.getDateConfirmedHiv(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        delivery.setFacilityId( dto.getFacilityId() );
        delivery.setDeliveryId( dto.getDeliveryId() );
        if ( dto.getBookingStatus() != null ) {
            delivery.setBookingStatus( Integer.parseInt( dto.getBookingStatus() ) );
        }
        delivery.setRomDeliveryInterval( dto.getRomDeliveryInterval() );
        delivery.setModeDelivery( dto.getModeDelivery() );
        delivery.setEpisiotomy( dto.getEpisiotomy() );
        delivery.setVaginalTear( dto.getVaginalTear() );
        delivery.setMaternalOutcome( dto.getMaternalOutcome() );
        delivery.setGestationalAge( dto.getGestationalAge() );
        delivery.setTimeHivDiagnosis( dto.getTimeHivDiagnosis() );
        delivery.setSourceReferral( dto.getSourceReferral() );
        delivery.setHepatitisBStatus( dto.getHepatitisBStatus() );
        delivery.setHepatitisCStatus( dto.getHepatitisCStatus() );
        if ( dto.getScreenPostPartum() != null ) {
            delivery.setScreenPostPartum( Boolean.parseBoolean( dto.getScreenPostPartum() ) );
        }
        delivery.setArvRegimenPast( dto.getArvRegimenPast() );
        delivery.setArvRegimenCurrent( dto.getArvRegimenCurrent() );
        delivery.setClinicStage( dto.getClinicStage() );
        delivery.setCd4Ordered( dto.getCd4Ordered() );
        delivery.setCd4( dto.getCd4() );
        delivery.setChildren( childDTOListToChildList( dto.getChildren() ) );

        return delivery;
    }

    @Override
    public DeliveryDTO deliveryToDto(Delivery delivery) {
        if ( delivery == null ) {
            return null;
        }

        DeliveryDTO deliveryDTO = new DeliveryDTO();

        if ( delivery.getDateDelivery() != null ) {
            deliveryDTO.setDateDelivery( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( delivery.getDateDelivery() ) );
        }
        deliveryDTO.setFacilityId( deliveryFacilityId( delivery ) );
        if ( delivery.getDateArvRegimenCurrent() != null ) {
            deliveryDTO.setDateArvRegimenCurrent( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( delivery.getDateArvRegimenCurrent() ) );
        }
        deliveryDTO.setPatientId( deliveryPatientPatientId( delivery ) );
        if ( delivery.getDateConfirmedHiv() != null ) {
            deliveryDTO.setDateConfirmedHiv( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( delivery.getDateConfirmedHiv() ) );
        }
        deliveryDTO.setDeliveryId( delivery.getDeliveryId() );
        if ( delivery.getBookingStatus() != null ) {
            deliveryDTO.setBookingStatus( String.valueOf( delivery.getBookingStatus() ) );
        }
        deliveryDTO.setRomDeliveryInterval( delivery.getRomDeliveryInterval() );
        deliveryDTO.setModeDelivery( delivery.getModeDelivery() );
        deliveryDTO.setEpisiotomy( delivery.getEpisiotomy() );
        deliveryDTO.setVaginalTear( delivery.getVaginalTear() );
        deliveryDTO.setMaternalOutcome( delivery.getMaternalOutcome() );
        deliveryDTO.setTimeHivDiagnosis( delivery.getTimeHivDiagnosis() );
        if ( delivery.getScreenPostPartum() != null ) {
            deliveryDTO.setScreenPostPartum( String.valueOf( delivery.getScreenPostPartum() ) );
        }
        deliveryDTO.setSourceReferral( delivery.getSourceReferral() );
        deliveryDTO.setHepatitisBStatus( delivery.getHepatitisBStatus() );
        deliveryDTO.setHepatitisCStatus( delivery.getHepatitisCStatus() );
        if ( delivery.getGestationalAge() != null ) {
            deliveryDTO.setGestationalAge( delivery.getGestationalAge() );
        }
        deliveryDTO.setArvRegimenPast( delivery.getArvRegimenPast() );
        deliveryDTO.setArvRegimenCurrent( delivery.getArvRegimenCurrent() );
        deliveryDTO.setClinicStage( delivery.getClinicStage() );
        deliveryDTO.setCd4Ordered( delivery.getCd4Ordered() );
        deliveryDTO.setCd4( delivery.getCd4() );
        deliveryDTO.setChildren( childListToChildDTOList( delivery.getChildren() ) );

        return deliveryDTO;
    }

    @Override
    public List<DeliveryDTO> deliveryToDto(List<Delivery> delivery) {
        if ( delivery == null ) {
            return null;
        }

        List<DeliveryDTO> list = new ArrayList<DeliveryDTO>( delivery.size() );
        for ( Delivery delivery1 : delivery ) {
            list.add( deliveryToDto( delivery1 ) );
        }

        return list;
    }

    protected Anc deliveryDTOToAnc(DeliveryDTO deliveryDTO) {
        if ( deliveryDTO == null ) {
            return null;
        }

        Anc anc = new Anc();

        anc.setAncId( deliveryDTO.getAncId() );

        return anc;
    }

    protected Facility deliveryDTOToFacility(DeliveryDTO deliveryDTO) {
        if ( deliveryDTO == null ) {
            return null;
        }

        Facility facility = new Facility();

        facility.setId( deliveryDTO.getFacilityId() );

        return facility;
    }

    protected Patient deliveryDTOToPatient(DeliveryDTO deliveryDTO) {
        if ( deliveryDTO == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setPatientId( deliveryDTO.getPatientId() );

        return patient;
    }

    protected Child childDTOToChild(ChildDTO childDTO) {
        if ( childDTO == null ) {
            return null;
        }

        Child child = new Child();

        if ( childDTO.getFacilityId() != null ) {
            child.setFacilityId( Long.parseLong( childDTO.getFacilityId() ) );
        }
        if ( childDTO.getChildId() != null ) {
            child.setChildId( Long.parseLong( childDTO.getChildId() ) );
        }
        child.setReferenceNum( childDTO.getReferenceNum() );
        try {
            if ( childDTO.getDateBirth() != null ) {
                child.setDateBirth( new SimpleDateFormat().parse( childDTO.getDateBirth() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        child.setHospitalNumber( childDTO.getHospitalNumber() );
        child.setSurname( childDTO.getSurname() );
        child.setOtherNames( childDTO.getOtherNames() );
        child.setGender( childDTO.getGender() );
        if ( childDTO.getBodyWeight() != null ) {
            child.setBodyWeight( Double.parseDouble( childDTO.getBodyWeight() ) );
        }
        if ( childDTO.getApgarScore() != null ) {
            child.setApgarScore( Integer.parseInt( childDTO.getApgarScore() ) );
        }
        child.setStatus( childDTO.getStatus() );
        child.setArv( childDTO.getArv() );
        child.setHepb( childDTO.getHepb() );
        child.setRegistrationStatus( childDTO.getRegistrationStatus() );
        child.setHbv( childDTO.getHbv() );

        return child;
    }

    protected List<Child> childDTOListToChildList(List<ChildDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Child> list1 = new ArrayList<Child>( list.size() );
        for ( ChildDTO childDTO : list ) {
            list1.add( childDTOToChild( childDTO ) );
        }

        return list1;
    }

    private Long deliveryFacilityId(Delivery delivery) {
        if ( delivery == null ) {
            return null;
        }
        Facility facility = delivery.getFacility();
        if ( facility == null ) {
            return null;
        }
        Long id = facility.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long deliveryPatientPatientId(Delivery delivery) {
        if ( delivery == null ) {
            return null;
        }
        Patient patient = delivery.getPatient();
        if ( patient == null ) {
            return null;
        }
        Long patientId = patient.getPatientId();
        if ( patientId == null ) {
            return null;
        }
        return patientId;
    }

    protected ChildDTO childToChildDTO(Child child) {
        if ( child == null ) {
            return null;
        }

        ChildDTO childDTO = new ChildDTO();

        if ( child.getChildId() != null ) {
            childDTO.setChildId( String.valueOf( child.getChildId() ) );
        }
        if ( child.getFacilityId() != null ) {
            childDTO.setFacilityId( String.valueOf( child.getFacilityId() ) );
        }
        childDTO.setReferenceNum( child.getReferenceNum() );
        childDTO.setRegistrationStatus( child.getRegistrationStatus() );
        childDTO.setHospitalNumber( child.getHospitalNumber() );
        childDTO.setSurname( child.getSurname() );
        childDTO.setOtherNames( child.getOtherNames() );
        if ( child.getDateBirth() != null ) {
            childDTO.setDateBirth( new SimpleDateFormat().format( child.getDateBirth() ) );
        }
        childDTO.setGender( child.getGender() );
        childDTO.setArv( child.getArv() );
        childDTO.setHepb( child.getHepb() );
        childDTO.setHbv( child.getHbv() );
        if ( child.getBodyWeight() != null ) {
            childDTO.setBodyWeight( String.valueOf( child.getBodyWeight() ) );
        }
        if ( child.getApgarScore() != null ) {
            childDTO.setApgarScore( String.valueOf( child.getApgarScore() ) );
        }
        childDTO.setStatus( child.getStatus() );

        return childDTO;
    }

    protected List<ChildDTO> childListToChildDTOList(List<Child> list) {
        if ( list == null ) {
            return null;
        }

        List<ChildDTO> list1 = new ArrayList<ChildDTO>( list.size() );
        for ( Child child : list ) {
            list1.add( childToChildDTO( child ) );
        }

        return list1;
    }
}
