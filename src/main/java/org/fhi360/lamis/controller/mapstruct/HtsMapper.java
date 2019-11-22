package org.fhi360.lamis.controller.mapstruct;

import org.fhi360.lamis.model.Hts;
import org.fhi360.lamis.model.dto.HtsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HtsMapper {
    @Mappings({
            @Mapping(source = "facility.id", target = "facilityId"),
//            @Mapping(source = "state.stateId", target = "state"),
//            @Mapping(source = "lga.lgaId", target = "lga"),
            @Mapping(source = "dateBirth", target = "dateBirth", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "assessment.assessmentId", target = "assessmentId"),
            @Mapping(source = "dateRegistration", target = "dateRegistration", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateStarted", target = "dateStarted", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateVisit", target = "dateVisit", dateFormat = "MM/dd/yyyy")
    })
    HtsDto htsToDto(Hts hts);

    List<Hts> listDtoToHts(List<HtsDto> dtos);

    @Mappings({
            @Mapping(source = "facilityId", target = "facility.id"),
//            @Mapping(source = "state", target = "state.stateId"),
//            @Mapping(source = "lga", target = "lga.lgaId"),
            @Mapping(source = "dateBirth", target = "dateBirth", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "assessmentId", target = "assessment.assessmentId"),
            @Mapping(source = "dateStarted", target = "dateStarted", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateRegistration", target = "dateRegistration", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateVisit", target = "dateVisit", dateFormat = "MM/dd/yyyy")
    })
    Hts dtoToHts(HtsDto htsDto);

    List<HtsDto> listHtsToDto(List<Hts> hts);

}
