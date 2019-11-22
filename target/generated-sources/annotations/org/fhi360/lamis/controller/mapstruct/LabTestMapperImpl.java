package org.fhi360.lamis.controller.mapstruct;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.fhi360.lamis.model.LabTest;
import org.fhi360.lamis.model.dto.LabDto;
import org.fhi360.lamis.model.dto.LabTestDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-21T15:49:45+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class LabTestMapperImpl implements LabTestMapper {

    @Override
    public LabDto labToDto(LabTest labTest) {
        if ( labTest == null ) {
            return null;
        }

        LabDto labDto = new LabDto();

        if ( labTest.getLabtestId() != null ) {
            labDto.setLabtestId( labTest.getLabtestId() );
        }
        labDto.setDescription( labTest.getDescription() );

        return labDto;
    }

    @Override
    public List<LabTestDTO> latTestToDto(List<LabTest> labTest) {
        if ( labTest == null ) {
            return null;
        }

        List<LabTestDTO> list = new ArrayList<LabTestDTO>( labTest.size() );
        for ( LabTest labTest1 : labTest ) {
            list.add( labTestToLabTestDTO( labTest1 ) );
        }

        return list;
    }

    @Override
    public LabTest dtoLatTest(LabTestDTO labDto) {
        if ( labDto == null ) {
            return null;
        }

        LabTest labTest = new LabTest();

        labTest.setLabtestId( labDto.getLabtestId() );
        labTest.setDescription( labDto.getDescription() );

        return labTest;
    }

    @Override
    public List<LabTest> labToDto(List<LabTestDTO> labDto) {
        if ( labDto == null ) {
            return null;
        }

        List<LabTest> list = new ArrayList<LabTest>( labDto.size() );
        for ( LabTestDTO labTestDTO : labDto ) {
            list.add( dtoLatTest( labTestDTO ) );
        }

        return list;
    }

    protected LabTestDTO labTestToLabTestDTO(LabTest labTest) {
        if ( labTest == null ) {
            return null;
        }

        LabTestDTO labTestDTO = new LabTestDTO();

        labTestDTO.setLabtestId( labTest.getLabtestId() );
        labTestDTO.setDescription( labTest.getDescription() );

        return labTestDTO;
    }
}
