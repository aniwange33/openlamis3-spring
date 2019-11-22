package org.fhi360.lamis.controller.mapstruct;

import org.fhi360.lamis.model.Laboratory;
import org.fhi360.lamis.model.dto.LaboratoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LaboratoryMapper {
    @Mappings({
            @Mapping(source = "patientId", target = "patient.patientId"),
            @Mapping(source = "hospitalNum", target = "patient.hospitalNum"),
            @Mapping(source = "labtestId", target = "labTest.labtestId"),
            @Mapping(source = "labno", target = "labNo"),
            @Mapping(source = "resultab", target = "resultAB"),
            @Mapping(source = "resultpc", target = "resultPC"),
            @Mapping(source = "facilityId", target = "facility.id"),
            @Mapping(source = "dateReported", target = "dateReported", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateCollected", target = "dateCollected", dateFormat = "MM/dd/yyyy")
    })
    Laboratory dtoToLaboratory(LaboratoryDTO dto);

    @Mappings({
            @Mapping(source = "patient.patientId", target = "patientId"),
            @Mapping(source = "labNo", target = "labno"),
            @Mapping(source = "patient.hospitalNum", target = "hospitalNum"),
            @Mapping(source = "facility.id", target = "facilityId"),
            @Mapping(source = "resultAB", target = "resultab"),
            @Mapping(source = "resultPC", target = "resultpc"),
            @Mapping(source = "labTest.labtestId", target = "labtestId"),
            @Mapping(source = "dateReported", target = "dateReported", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateCollected", target = "dateCollected", dateFormat = "MM/dd/yyyy")
    })
    LaboratoryDTO laboratoryToDto(Laboratory laboratory);

    List<LaboratoryDTO> laboratoryToDto(List<Laboratory> laboratories);
}
