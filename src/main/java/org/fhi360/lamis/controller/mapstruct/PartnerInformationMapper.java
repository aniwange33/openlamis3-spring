package org.fhi360.lamis.controller.mapstruct;

import org.fhi360.lamis.model.PartnerInformation;
import org.fhi360.lamis.model.dto.PartnerInformationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PartnerInformationMapper {
    @Mappings({
            @Mapping(source = "patientId", target = "patient.patientId"),
            @Mapping(source = "facilityId", target = "facility.id"),
            @Mapping(source = "dateVisit", target = "dateVisit", dateFormat = "MM/dd/yyyy")
    })
    PartnerInformation dtoToModel(PartnerInformationDTO dto);

    @Mappings({
            @Mapping(target = "patientId", source = "patient.patientId"),
            @Mapping(target = "facilityId", source = "facility.id"),
            @Mapping(source = "dateVisit", target = "dateVisit", dateFormat = "MM/dd/yyyy")
    })
    PartnerInformationDTO modelToDto(PartnerInformation model);
}
