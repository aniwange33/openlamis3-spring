package org.fhi360.lamis.controller.mapstruct;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.annotation.Generated;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.PartnerInformation;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.PartnerInformationDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-21T15:49:45+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class PartnerInformationMapperImpl implements PartnerInformationMapper {

    @Override
    public PartnerInformation dtoToModel(PartnerInformationDTO dto) {
        if ( dto == null ) {
            return null;
        }

        PartnerInformation partnerInformation = new PartnerInformation();

        partnerInformation.setFacility( partnerInformationDTOToFacility( dto ) );
        partnerInformation.setPatient( partnerInformationDTOToPatient( dto ) );
        if ( dto.getDateVisit() != null ) {
            partnerInformation.setDateVisit( LocalDate.parse( dto.getDateVisit(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        partnerInformation.setFacilityId( dto.getFacilityId() );
        partnerInformation.setPartnerinformationId( dto.getPartnerinformationId() );
        partnerInformation.setPartnerNotification( dto.getPartnerNotification() );
        partnerInformation.setPartnerHivStatus( dto.getPartnerHivStatus() );
        partnerInformation.setPartnerReferred( dto.getPartnerReferred() );

        return partnerInformation;
    }

    @Override
    public PartnerInformationDTO modelToDto(PartnerInformation model) {
        if ( model == null ) {
            return null;
        }

        PartnerInformationDTO partnerInformationDTO = new PartnerInformationDTO();

        if ( model.getDateVisit() != null ) {
            partnerInformationDTO.setDateVisit( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( model.getDateVisit() ) );
        }
        partnerInformationDTO.setFacilityId( modelFacilityId( model ) );
        partnerInformationDTO.setPatientId( modelPatientPatientId( model ) );
        partnerInformationDTO.setPartnerinformationId( model.getPartnerinformationId() );
        partnerInformationDTO.setPartnerNotification( model.getPartnerNotification() );
        partnerInformationDTO.setPartnerHivStatus( model.getPartnerHivStatus() );
        partnerInformationDTO.setPartnerReferred( model.getPartnerReferred() );

        return partnerInformationDTO;
    }

    protected Facility partnerInformationDTOToFacility(PartnerInformationDTO partnerInformationDTO) {
        if ( partnerInformationDTO == null ) {
            return null;
        }

        Facility facility = new Facility();

        facility.setId( partnerInformationDTO.getFacilityId() );

        return facility;
    }

    protected Patient partnerInformationDTOToPatient(PartnerInformationDTO partnerInformationDTO) {
        if ( partnerInformationDTO == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setPatientId( partnerInformationDTO.getPatientId() );

        return patient;
    }

    private Long modelFacilityId(PartnerInformation partnerInformation) {
        if ( partnerInformation == null ) {
            return null;
        }
        Facility facility = partnerInformation.getFacility();
        if ( facility == null ) {
            return null;
        }
        Long id = facility.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long modelPatientPatientId(PartnerInformation partnerInformation) {
        if ( partnerInformation == null ) {
            return null;
        }
        Patient patient = partnerInformation.getPatient();
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
