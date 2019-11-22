package org.fhi360.lamis.controller.mapstruct;

import org.fhi360.lamis.model.MotherInformation;
import org.fhi360.lamis.model.dto.MotherDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MotherInformationMapper {
    @Mappings({
            @Mapping(source = "dateConfirmedHiv", target = "dateConfirmedHiv"),
            @Mapping(source = "motherUniqueId", target = "hospitalNum"),
            @Mapping(source = "motherUniqueId", target = "uniqueId"),
            @Mapping(source = "motherSurname", target = "surname"),
            @Mapping(source = "facilityId", target = "facility.id"),
            @Mapping(source = "motherOtherNames", target = "otherNames")
    })
    MotherInformation dtoToMotherInformation(MotherDTO dto);
}
