package org.fhi360.lamis.controller.mapstruct;


import org.fhi360.lamis.model.LabTest;
import org.fhi360.lamis.model.dto.LabDto;
import org.fhi360.lamis.model.dto.LabTestDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LabTestMapper {
    LabDto labToDto(LabTest labTest);

    List<LabTestDTO> latTestToDto(List<LabTest>  labTest);

    LabTest dtoLatTest(LabTestDTO labDto);

    List<LabTest> labToDto(List<LabTestDTO>  labDto);


}
