package org.fhi360.lamis.controller.mapstruct;

import javax.annotation.Generated;
import org.fhi360.lamis.model.CaseManager;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.dto.CaseManagerDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-21T15:49:44+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class CaseManagerMapperImpl implements CaseManagerMapper {

    @Override
    public CaseManager dtoToCaseManager(CaseManagerDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CaseManager caseManager = new CaseManager();

        caseManager.setFullName( dto.getFullname() );
        caseManager.setPhoneNumber( dto.getPhone() );
        caseManager.setFacilityId( dto.getFacilityId() );
        caseManager.setCasemanagerId( dto.getCasemanagerId() );
        caseManager.setSex( dto.getSex() );
        if ( dto.getAge() != null ) {
            caseManager.setAge( String.valueOf( dto.getAge() ) );
        }
        caseManager.setReligion( dto.getReligion() );
        caseManager.setAddress( dto.getAddress() );

        return caseManager;
    }

    @Override
    public CaseManagerDTO caseManagerToDto(CaseManager caseManager) {
        if ( caseManager == null ) {
            return null;
        }

        CaseManagerDTO caseManagerDTO = new CaseManagerDTO();

        caseManagerDTO.setFacilityId( caseManagerFacilityId( caseManager ) );
        caseManagerDTO.setFullname( caseManager.getFullName() );
        caseManagerDTO.setPhone( caseManager.getPhoneNumber() );
        caseManagerDTO.setCasemanagerId( caseManager.getCasemanagerId() );
        caseManagerDTO.setAddress( caseManager.getAddress() );
        caseManagerDTO.setReligion( caseManager.getReligion() );
        if ( caseManager.getAge() != null ) {
            caseManagerDTO.setAge( Integer.parseInt( caseManager.getAge() ) );
        }
        caseManagerDTO.setSex( caseManager.getSex() );

        return caseManagerDTO;
    }

    private Long caseManagerFacilityId(CaseManager caseManager) {
        if ( caseManager == null ) {
            return null;
        }
        Facility facility = caseManager.getFacility();
        if ( facility == null ) {
            return null;
        }
        Long id = facility.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
