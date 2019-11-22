package org.fhi360.lamis.controller.mapstruct;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.fhi360.lamis.model.Anc;
import org.fhi360.lamis.model.Child;
import org.fhi360.lamis.model.Delivery;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.MotherInformation;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.ChildDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-21T15:49:44+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class ChildMapperImpl implements ChildMapper {

    @Override
    public ChildDTO childToDTO(Child child) {
        if ( child == null ) {
            return null;
        }

        ChildDTO childDTO = new ChildDTO();

        Long motherinformationId = childMotherMotherinformationId( child );
        if ( motherinformationId != null ) {
            childDTO.setMotherId( String.valueOf( motherinformationId ) );
        }
        Long deliveryId = childDeliveryDeliveryId( child );
        if ( deliveryId != null ) {
            childDTO.setDeliveryId( String.valueOf( deliveryId ) );
        }
        Long id = childFacilityId( child );
        if ( id != null ) {
            childDTO.setFacilityId( String.valueOf( id ) );
        }
        Long patientId = childPatientPatientId( child );
        if ( patientId != null ) {
            childDTO.setPatientId( String.valueOf( patientId ) );
        }
        if ( childMotherInFacility( child ) != null ) {
            childDTO.setInFacility( childMotherInFacility( child ) );
        }
        else {
            childDTO.setInFacility( "N/A" );
        }
        Long ancId = childAncAncId( child );
        if ( ancId != null ) {
            childDTO.setAncId( String.valueOf( ancId ) );
        }
        if ( child.getDateBirth() != null ) {
            childDTO.setDateBirth( new SimpleDateFormat( "MM/dd/yyyy" ).format( child.getDateBirth() ) );
        }
        if ( child.getChildId() != null ) {
            childDTO.setChildId( String.valueOf( child.getChildId() ) );
        }
        childDTO.setReferenceNum( child.getReferenceNum() );
        childDTO.setRegistrationStatus( child.getRegistrationStatus() );
        childDTO.setHospitalNumber( child.getHospitalNumber() );
        childDTO.setSurname( child.getSurname() );
        childDTO.setOtherNames( child.getOtherNames() );
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

        childDTO.setNameMother( child.getMother().getSurname() + " " + child.getMother().getOtherNames() );
        childDTO.setChildName( child.getSurname() + " " + child.getOtherNames() );

        return childDTO;
    }

    @Override
    public Child dtoToChild(ChildDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Child child = new Child();

        child.setAnc( childDTOToAnc( dto ) );
        child.setFacility( childDTOToFacility( dto ) );
        child.setPatient( childDTOToPatient( dto ) );
        try {
            if ( dto.getDateBirth() != null ) {
                child.setDateBirth( new SimpleDateFormat( "MM/dd/yyyy" ).parse( dto.getDateBirth() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        if ( dto.getFacilityId() != null ) {
            child.setFacilityId( Long.parseLong( dto.getFacilityId() ) );
        }
        if ( dto.getChildId() != null ) {
            child.setChildId( Long.parseLong( dto.getChildId() ) );
        }
        child.setReferenceNum( dto.getReferenceNum() );
        child.setHospitalNumber( dto.getHospitalNumber() );
        child.setSurname( dto.getSurname() );
        child.setOtherNames( dto.getOtherNames() );
        child.setGender( dto.getGender() );
        if ( dto.getBodyWeight() != null ) {
            child.setBodyWeight( Double.parseDouble( dto.getBodyWeight() ) );
        }
        if ( dto.getApgarScore() != null ) {
            child.setApgarScore( Integer.parseInt( dto.getApgarScore() ) );
        }
        child.setStatus( dto.getStatus() );
        child.setArv( dto.getArv() );
        child.setHepb( dto.getHepb() );
        child.setRegistrationStatus( dto.getRegistrationStatus() );
        child.setHbv( dto.getHbv() );

        return child;
    }

    @Override
    public List<ChildDTO> childToDTO(List<Child> children) {
        if ( children == null ) {
            return null;
        }

        List<ChildDTO> list = new ArrayList<ChildDTO>( children.size() );
        for ( Child child : children ) {
            list.add( childToDTO( child ) );
        }

        return list;
    }

    @Override
    public List<Child> dtosToChildren(List<ChildDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Child> list = new ArrayList<Child>( dtos.size() );
        for ( ChildDTO childDTO : dtos ) {
            list.add( dtoToChild( childDTO ) );
        }

        return list;
    }

    private Long childMotherMotherinformationId(Child child) {
        if ( child == null ) {
            return null;
        }
        MotherInformation mother = child.getMother();
        if ( mother == null ) {
            return null;
        }
        Long motherinformationId = mother.getMotherinformationId();
        if ( motherinformationId == null ) {
            return null;
        }
        return motherinformationId;
    }

    private Long childDeliveryDeliveryId(Child child) {
        if ( child == null ) {
            return null;
        }
        Delivery delivery = child.getDelivery();
        if ( delivery == null ) {
            return null;
        }
        Long deliveryId = delivery.getDeliveryId();
        if ( deliveryId == null ) {
            return null;
        }
        return deliveryId;
    }

    private Long childFacilityId(Child child) {
        if ( child == null ) {
            return null;
        }
        Facility facility = child.getFacility();
        if ( facility == null ) {
            return null;
        }
        Long id = facility.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long childPatientPatientId(Child child) {
        if ( child == null ) {
            return null;
        }
        Patient patient = child.getPatient();
        if ( patient == null ) {
            return null;
        }
        Long patientId = patient.getPatientId();
        if ( patientId == null ) {
            return null;
        }
        return patientId;
    }

    private String childMotherInFacility(Child child) {
        if ( child == null ) {
            return null;
        }
        MotherInformation mother = child.getMother();
        if ( mother == null ) {
            return null;
        }
        String inFacility = mother.getInFacility();
        if ( inFacility == null ) {
            return null;
        }
        return inFacility;
    }

    private Long childAncAncId(Child child) {
        if ( child == null ) {
            return null;
        }
        Anc anc = child.getAnc();
        if ( anc == null ) {
            return null;
        }
        Long ancId = anc.getAncId();
        if ( ancId == null ) {
            return null;
        }
        return ancId;
    }

    protected Anc childDTOToAnc(ChildDTO childDTO) {
        if ( childDTO == null ) {
            return null;
        }

        Anc anc = new Anc();

        if ( childDTO.getAncId() != null ) {
            anc.setAncId( Long.parseLong( childDTO.getAncId() ) );
        }

        return anc;
    }

    protected Facility childDTOToFacility(ChildDTO childDTO) {
        if ( childDTO == null ) {
            return null;
        }

        Facility facility = new Facility();

        if ( childDTO.getFacilityId() != null ) {
            facility.setId( Long.parseLong( childDTO.getFacilityId() ) );
        }

        return facility;
    }

    protected Patient childDTOToPatient(ChildDTO childDTO) {
        if ( childDTO == null ) {
            return null;
        }

        Patient patient = new Patient();

        if ( childDTO.getPatientId() != null ) {
            patient.setPatientId( Long.parseLong( childDTO.getPatientId() ) );
        }

        return patient;
    }
}
