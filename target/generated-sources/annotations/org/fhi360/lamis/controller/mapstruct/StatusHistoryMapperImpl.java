package org.fhi360.lamis.controller.mapstruct;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.StatusHistory;
import org.fhi360.lamis.model.dto.StatusHistoryDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-21T15:49:45+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class StatusHistoryMapperImpl implements StatusHistoryMapper {

    @Override
    public StatusHistoryDTO statusHistoryToDto(StatusHistory statusHistory) {
        if ( statusHistory == null ) {
            return null;
        }

        StatusHistoryDTO statusHistoryDTO = new StatusHistoryDTO();

        if ( statusHistory.getDateTracked() != null ) {
            statusHistoryDTO.setDateTracked( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( statusHistory.getDateTracked() ) );
        }
        statusHistoryDTO.setFacilityId( statusHistoryFacilityId( statusHistory ) );
        if ( statusHistory.getAgreedDate() != null ) {
            statusHistoryDTO.setAgreedDate( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( statusHistory.getAgreedDate() ) );
        }
        statusHistoryDTO.setPatientId( statusHistoryPatientPatientId( statusHistory ) );
        if ( statusHistory.getDateCurrentStatus() != null ) {
            statusHistoryDTO.setDateCurrentStatus( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( statusHistory.getDateCurrentStatus() ) );
        }
        statusHistoryDTO.setHistoryId( statusHistory.getHistoryId() );
        statusHistoryDTO.setCurrentStatus( statusHistory.getCurrentStatus() );
        statusHistoryDTO.setOutcome( statusHistory.getOutcome() );
        statusHistoryDTO.setReasonInterrupt( statusHistory.getReasonInterrupt() );
        statusHistoryDTO.setCauseDeath( statusHistory.getCauseDeath() );

        statusHistoryDTO.setDeletable( statusHistory.getCurrentStatus() == "ART Start" ? "0" : "1" );

        return statusHistoryDTO;
    }

    @Override
    public List<StatusHistoryDTO> statusHistoryToDto(List<StatusHistory> statusHistory) {
        if ( statusHistory == null ) {
            return null;
        }

        List<StatusHistoryDTO> list = new ArrayList<StatusHistoryDTO>( statusHistory.size() );
        for ( StatusHistory statusHistory1 : statusHistory ) {
            list.add( statusHistoryToDto( statusHistory1 ) );
        }

        return list;
    }

    @Override
    public StatusHistory dtoToStatusHistory(StatusHistoryDTO dto) {
        if ( dto == null ) {
            return null;
        }

        StatusHistory statusHistory = new StatusHistory();

        statusHistory.setFacility( statusHistoryDTOToFacility( dto ) );
        statusHistory.setPatient( statusHistoryDTOToPatient( dto ) );
        if ( dto.getDateTracked() != null ) {
            statusHistory.setDateTracked( LocalDate.parse( dto.getDateTracked(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getAgreedDate() != null ) {
            statusHistory.setAgreedDate( LocalDate.parse( dto.getAgreedDate(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateCurrentStatus() != null ) {
            statusHistory.setDateCurrentStatus( LocalDate.parse( dto.getDateCurrentStatus(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        statusHistory.setFacilityId( dto.getFacilityId() );
        statusHistory.setHistoryId( dto.getHistoryId() );
        statusHistory.setCurrentStatus( dto.getCurrentStatus() );
        statusHistory.setOutcome( dto.getOutcome() );
        statusHistory.setReasonInterrupt( dto.getReasonInterrupt() );
        statusHistory.setCauseDeath( dto.getCauseDeath() );

        return statusHistory;
    }

    private Long statusHistoryFacilityId(StatusHistory statusHistory) {
        if ( statusHistory == null ) {
            return null;
        }
        Facility facility = statusHistory.getFacility();
        if ( facility == null ) {
            return null;
        }
        Long id = facility.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long statusHistoryPatientPatientId(StatusHistory statusHistory) {
        if ( statusHistory == null ) {
            return null;
        }
        Patient patient = statusHistory.getPatient();
        if ( patient == null ) {
            return null;
        }
        Long patientId = patient.getPatientId();
        if ( patientId == null ) {
            return null;
        }
        return patientId;
    }

    protected Facility statusHistoryDTOToFacility(StatusHistoryDTO statusHistoryDTO) {
        if ( statusHistoryDTO == null ) {
            return null;
        }

        Facility facility = new Facility();

        facility.setId( statusHistoryDTO.getFacilityId() );

        return facility;
    }

    protected Patient statusHistoryDTOToPatient(StatusHistoryDTO statusHistoryDTO) {
        if ( statusHistoryDTO == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setPatientId( statusHistoryDTO.getPatientId() );

        return patient;
    }
}
