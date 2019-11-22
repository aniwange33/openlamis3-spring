package org.fhi360.lamis.controller.mapstruct;

import org.fhi360.lamis.model.dto.IndexContactDto;
import org.fhi360.lamis.model.IndexContact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IndexContactMapper {
    @Mappings({
            @Mapping(source = "hts.htsId", target = "htsId"),
            @Mapping(source = "dateBirth", target = "dateBirth", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateHivTest", target = "dateHivTest", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "facility.id", target = "facilityId")
    })
    IndexContactDto indexContactToDto(IndexContact indexContact);


    @Mappings({
            @Mapping(source = "htsId", target = "hts.htsId"),
            @Mapping(source = "dateBirth", target = "dateBirth", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateHivTest", target = "dateHivTest", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "facilityId", target = "facility.id")
    })
    IndexContact indexContactDtoToindexContact(IndexContactDto indexContactDto);

    List<IndexContactDto> indexContactToDto(List<IndexContact> indexContact);

    List<IndexContact> indexContactDtoToindexContact(List<IndexContactDto> indexContactDto);

}
