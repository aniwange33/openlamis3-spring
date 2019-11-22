package org.fhi360.lamis.controller.mapstruct;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.LabTest;
import org.fhi360.lamis.model.Laboratory;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.LaboratoryDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-21T15:49:45+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class LaboratoryMapperImpl implements LaboratoryMapper {

    @Override
    public Laboratory dtoToLaboratory(LaboratoryDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Laboratory laboratory = new Laboratory();

        laboratory.setPatient( laboratoryDTOToPatient( dto ) );
        laboratory.setLabTest( laboratoryDTOToLabTest( dto ) );
        laboratory.setFacility( laboratoryDTOToFacility( dto ) );
        laboratory.setLabNo( dto.getLabno() );
        laboratory.setResultAB( dto.getResultab() );
        if ( dto.getDateCollected() != null ) {
            laboratory.setDateCollected( LocalDate.parse( dto.getDateCollected(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        if ( dto.getDateReported() != null ) {
            laboratory.setDateReported( LocalDate.parse( dto.getDateReported(), DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ) );
        }
        laboratory.setResultPC( dto.getResultpc() );
        laboratory.setHospitalNum( dto.getHospitalNum() );
        laboratory.setFacilityId( dto.getFacilityId() );
        laboratory.setLaboratoryId( dto.getLaboratoryId() );

        return laboratory;
    }

    @Override
    public LaboratoryDTO laboratoryToDto(Laboratory laboratory) {
        if ( laboratory == null ) {
            return null;
        }

        LaboratoryDTO laboratoryDTO = new LaboratoryDTO();

        laboratoryDTO.setLabtestId( laboratoryLabTestLabtestId( laboratory ) );
        laboratoryDTO.setLabno( laboratory.getLabNo() );
        laboratoryDTO.setResultab( laboratory.getResultAB() );
        laboratoryDTO.setFacilityId( laboratoryFacilityId( laboratory ) );
        laboratoryDTO.setPatientId( laboratoryPatientPatientId( laboratory ) );
        if ( laboratory.getDateCollected() != null ) {
            laboratoryDTO.setDateCollected( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( laboratory.getDateCollected() ) );
        }
        laboratoryDTO.setHospitalNum( laboratoryPatientHospitalNum( laboratory ) );
        if ( laboratory.getDateReported() != null ) {
            laboratoryDTO.setDateReported( DateTimeFormatter.ofPattern( "MM/dd/yyyy" ).format( laboratory.getDateReported() ) );
        }
        laboratoryDTO.setResultpc( laboratory.getResultPC() );
        laboratoryDTO.setLaboratoryId( laboratory.getLaboratoryId() );

        return laboratoryDTO;
    }

    @Override
    public List<LaboratoryDTO> laboratoryToDto(List<Laboratory> laboratories) {
        if ( laboratories == null ) {
            return null;
        }

        List<LaboratoryDTO> list = new ArrayList<LaboratoryDTO>( laboratories.size() );
        for ( Laboratory laboratory : laboratories ) {
            list.add( laboratoryToDto( laboratory ) );
        }

        return list;
    }

    protected Patient laboratoryDTOToPatient(LaboratoryDTO laboratoryDTO) {
        if ( laboratoryDTO == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setHospitalNum( laboratoryDTO.getHospitalNum() );
        patient.setPatientId( laboratoryDTO.getPatientId() );

        return patient;
    }

    protected LabTest laboratoryDTOToLabTest(LaboratoryDTO laboratoryDTO) {
        if ( laboratoryDTO == null ) {
            return null;
        }

        LabTest labTest = new LabTest();

        labTest.setLabtestId( laboratoryDTO.getLabtestId() );

        return labTest;
    }

    protected Facility laboratoryDTOToFacility(LaboratoryDTO laboratoryDTO) {
        if ( laboratoryDTO == null ) {
            return null;
        }

        Facility facility = new Facility();

        facility.setId( laboratoryDTO.getFacilityId() );

        return facility;
    }

    private Long laboratoryLabTestLabtestId(Laboratory laboratory) {
        if ( laboratory == null ) {
            return null;
        }
        LabTest labTest = laboratory.getLabTest();
        if ( labTest == null ) {
            return null;
        }
        Long labtestId = labTest.getLabtestId();
        if ( labtestId == null ) {
            return null;
        }
        return labtestId;
    }

    private Long laboratoryFacilityId(Laboratory laboratory) {
        if ( laboratory == null ) {
            return null;
        }
        Facility facility = laboratory.getFacility();
        if ( facility == null ) {
            return null;
        }
        Long id = facility.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long laboratoryPatientPatientId(Laboratory laboratory) {
        if ( laboratory == null ) {
            return null;
        }
        Patient patient = laboratory.getPatient();
        if ( patient == null ) {
            return null;
        }
        Long patientId = patient.getPatientId();
        if ( patientId == null ) {
            return null;
        }
        return patientId;
    }

    private String laboratoryPatientHospitalNum(Laboratory laboratory) {
        if ( laboratory == null ) {
            return null;
        }
        Patient patient = laboratory.getPatient();
        if ( patient == null ) {
            return null;
        }
        String hospitalNum = patient.getHospitalNum();
        if ( hospitalNum == null ) {
            return null;
        }
        return hospitalNum;
    }
}
