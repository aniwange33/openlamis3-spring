package org.fhi360.lamis.controller.mapstruct;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Hts;
import org.fhi360.lamis.model.IndexContact;
import org.fhi360.lamis.model.dto.IndexContactDto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-21T15:49:45+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class IndexContactMapperImpl implements IndexContactMapper {

    @Override
    public IndexContactDto indexContactToDto(IndexContact indexContact) {
        if ( indexContact == null ) {
            return null;
        }

        IndexContactDto indexContactDto = new IndexContactDto();

        indexContactDto.setHtsId( indexContactHtsHtsId( indexContact ) );
        indexContactDto.setFacilityId( indexContactFacilityId( indexContact ) );
        indexContactDto.setDateBirth( indexContact.getDateBirth() );
        if ( indexContact.getDateHivTest() != null ) {
            indexContactDto.setDateHivTest( Date.from( indexContact.getDateHivTest().atStartOfDay( ZoneOffset.UTC ).toInstant() ) );
        }
        indexContactDto.setIndexcontactId( indexContact.getIndexcontactId() );
        indexContactDto.setClientCode( indexContact.getClientCode() );
        indexContactDto.setContactType( indexContact.getContactType() );
        indexContactDto.setSurname( indexContact.getSurname() );
        indexContactDto.setOtherNames( indexContact.getOtherNames() );
        indexContactDto.setAge( indexContact.getAge() );
        indexContactDto.setAgeUnit( indexContact.getAgeUnit() );
        indexContactDto.setGender( indexContact.getGender() );
        indexContactDto.setPhone( indexContact.getPhone() );
        indexContactDto.setAddress( indexContact.getAddress() );
        indexContactDto.setRelation( indexContact.getRelation() );
        indexContactDto.setGbv( indexContact.getGbv() );
        indexContactDto.setDurationPartner( indexContact.getDurationPartner() );
        indexContactDto.setPhoneTracking( indexContact.getPhoneTracking() );
        indexContactDto.setHomeTracking( indexContact.getHomeTracking() );
        indexContactDto.setOutcome( indexContact.getOutcome() );
        indexContactDto.setHivStatus( indexContact.getHivStatus() );
        indexContactDto.setLinkCare( indexContact.getLinkCare() );
        indexContactDto.setPartnerNotification( indexContact.getPartnerNotification() );
        indexContactDto.setModeNotification( indexContact.getModeNotification() );
        indexContactDto.setServiceProvided( indexContact.getServiceProvided() );
        indexContactDto.setHospitalNum( indexContact.getHospitalNum() );
        indexContactDto.setRelationship( indexContact.getRelationship() );

        return indexContactDto;
    }

    @Override
    public IndexContact indexContactDtoToindexContact(IndexContactDto indexContactDto) {
        if ( indexContactDto == null ) {
            return null;
        }

        IndexContact indexContact = new IndexContact();

        indexContact.setHts( indexContactDtoToHts( indexContactDto ) );
        indexContact.setFacility( indexContactDtoToFacility( indexContactDto ) );
        indexContact.setDateBirth( indexContactDto.getDateBirth() );
        if ( indexContactDto.getDateHivTest() != null ) {
            indexContact.setDateHivTest( LocalDateTime.ofInstant( indexContactDto.getDateHivTest().toInstant(), ZoneOffset.UTC ).toLocalDate() );
        }
        indexContact.setFacilityId( indexContactDto.getFacilityId() );
        indexContact.setIndexcontactId( indexContactDto.getIndexcontactId() );
        indexContact.setClientCode( indexContactDto.getClientCode() );
        indexContact.setContactType( indexContactDto.getContactType() );
        indexContact.setSurname( indexContactDto.getSurname() );
        indexContact.setOtherNames( indexContactDto.getOtherNames() );
        indexContact.setAge( indexContactDto.getAge() );
        indexContact.setAgeUnit( indexContactDto.getAgeUnit() );
        indexContact.setGender( indexContactDto.getGender() );
        indexContact.setPhone( indexContactDto.getPhone() );
        indexContact.setAddress( indexContactDto.getAddress() );
        indexContact.setRelation( indexContactDto.getRelation() );
        indexContact.setGbv( indexContactDto.getGbv() );
        indexContact.setDurationPartner( indexContactDto.getDurationPartner() );
        indexContact.setPhoneTracking( indexContactDto.getPhoneTracking() );
        indexContact.setHomeTracking( indexContactDto.getHomeTracking() );
        indexContact.setOutcome( indexContactDto.getOutcome() );
        indexContact.setHivStatus( indexContactDto.getHivStatus() );
        indexContact.setLinkCare( indexContactDto.getLinkCare() );
        indexContact.setPartnerNotification( indexContactDto.getPartnerNotification() );
        indexContact.setModeNotification( indexContactDto.getModeNotification() );
        indexContact.setServiceProvided( indexContactDto.getServiceProvided() );
        indexContact.setHospitalNum( indexContactDto.getHospitalNum() );
        indexContact.setRelationship( indexContactDto.getRelationship() );

        return indexContact;
    }

    @Override
    public List<IndexContactDto> indexContactToDto(List<IndexContact> indexContact) {
        if ( indexContact == null ) {
            return null;
        }

        List<IndexContactDto> list = new ArrayList<IndexContactDto>( indexContact.size() );
        for ( IndexContact indexContact1 : indexContact ) {
            list.add( indexContactToDto( indexContact1 ) );
        }

        return list;
    }

    @Override
    public List<IndexContact> indexContactDtoToindexContact(List<IndexContactDto> indexContactDto) {
        if ( indexContactDto == null ) {
            return null;
        }

        List<IndexContact> list = new ArrayList<IndexContact>( indexContactDto.size() );
        for ( IndexContactDto indexContactDto1 : indexContactDto ) {
            list.add( indexContactDtoToindexContact( indexContactDto1 ) );
        }

        return list;
    }

    private Long indexContactHtsHtsId(IndexContact indexContact) {
        if ( indexContact == null ) {
            return null;
        }
        Hts hts = indexContact.getHts();
        if ( hts == null ) {
            return null;
        }
        Long htsId = hts.getHtsId();
        if ( htsId == null ) {
            return null;
        }
        return htsId;
    }

    private Long indexContactFacilityId(IndexContact indexContact) {
        if ( indexContact == null ) {
            return null;
        }
        Facility facility = indexContact.getFacility();
        if ( facility == null ) {
            return null;
        }
        Long id = facility.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Hts indexContactDtoToHts(IndexContactDto indexContactDto) {
        if ( indexContactDto == null ) {
            return null;
        }

        Hts hts = new Hts();

        hts.setHtsId( indexContactDto.getHtsId() );

        return hts;
    }

    protected Facility indexContactDtoToFacility(IndexContactDto indexContactDto) {
        if ( indexContactDto == null ) {
            return null;
        }

        Facility facility = new Facility();

        facility.setId( indexContactDto.getFacilityId() );

        return facility;
    }
}
