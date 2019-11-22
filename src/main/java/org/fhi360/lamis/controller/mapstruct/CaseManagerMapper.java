package org.fhi360.lamis.controller.mapstruct;

import org.fhi360.lamis.model.CaseManager;
import org.fhi360.lamis.model.dto.CaseManagerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CaseManagerMapper {

    @Mappings({
            @Mapping(source = "phone", target = "phoneNumber"),
            @Mapping(source = "fullname", target = "fullName")
    })
    CaseManager dtoToCaseManager(CaseManagerDTO dto);

    @Mappings({
            @Mapping(source = "phoneNumber", target = "phone"),
            @Mapping(source = "facility.id", target = "facilityId"),
            @Mapping(source = "fullName", target = "fullname")
    })
    CaseManagerDTO caseManagerToDto(CaseManager caseManager);
}
