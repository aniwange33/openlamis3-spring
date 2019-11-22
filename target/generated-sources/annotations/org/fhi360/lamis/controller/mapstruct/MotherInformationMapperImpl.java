package org.fhi360.lamis.controller.mapstruct;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.fhi360.lamis.model.Child;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.MotherInformation;
import org.fhi360.lamis.model.dto.ChildDTO;
import org.fhi360.lamis.model.dto.MotherDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-21T15:49:45+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class MotherInformationMapperImpl implements MotherInformationMapper {

    @Override
    public MotherInformation dtoToMotherInformation(MotherDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MotherInformation motherInformation = new MotherInformation();

        motherInformation.setFacility( motherDTOToFacility( dto ) );
        motherInformation.setOtherNames( dto.getMotherOtherNames() );
        motherInformation.setSurname( dto.getMotherSurname() );
        motherInformation.setHospitalNum( dto.getMotherUniqueId() );
        if ( dto.getDateConfirmedHiv() != null ) {
            motherInformation.setDateConfirmedHiv( LocalDate.parse( dto.getDateConfirmedHiv() ) );
        }
        motherInformation.setUniqueId( dto.getMotherUniqueId() );
        motherInformation.setFacilityId( dto.getFacilityId() );
        motherInformation.setAddress( dto.getAddress() );
        motherInformation.setPhone( dto.getPhone() );
        motherInformation.setInFacility( dto.getInFacility() );
        if ( dto.getDateStarted() != null ) {
            motherInformation.setDateStarted( LocalDate.parse( dto.getDateStarted() ) );
        }
        motherInformation.setChildren( childDTOListToChildList( dto.getChildren() ) );

        return motherInformation;
    }

    protected Facility motherDTOToFacility(MotherDTO motherDTO) {
        if ( motherDTO == null ) {
            return null;
        }

        Facility facility = new Facility();

        facility.setId( motherDTO.getFacilityId() );

        return facility;
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
}
