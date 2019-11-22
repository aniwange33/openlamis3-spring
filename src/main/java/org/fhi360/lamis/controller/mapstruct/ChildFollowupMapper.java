package org.fhi360.lamis.controller.mapstruct;

import org.fhi360.lamis.model.ChildFollowup;
import org.fhi360.lamis.model.dto.ChildFollowupDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChildFollowupMapper {
    @Mappings({
            @Mapping(source = "dateVisit", target = "dateVisit", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateCotrimInitiated", target = "dateCotrimInitiated", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateLinkedToArt", target = "dateLinkedToArt", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateNextVisit", target = "dateNextVisit", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateNvpInitiated", target = "dateNvpInitiated", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "datePcrResult", target = "datePcrResult", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateRapidTest", target = "dateRapidTest", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateSampleSent", target = "dateSampleSent", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateSampleCollected", target = "dateSampleCollected", dateFormat = "MM/dd/yyyy")
    })
    ChildFollowup dtoToChildFollowup(ChildFollowupDTO dto);

    @Mappings({
            @Mapping(source = "child.childId", target = "childId"),
            @Mapping(source = "facility.id", target = "facilityId"),
            @Mapping(source = "dateVisit", target = "dateVisit", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateCotrimInitiated", target = "dateCotrimInitiated", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateLinkedToArt", target = "dateLinkedToArt", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateNextVisit", target = "dateNextVisit", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateNvpInitiated", target = "dateNvpInitiated", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "datePcrResult", target = "datePcrResult", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateRapidTest", target = "dateRapidTest", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateSampleSent", target = "dateSampleSent", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateSampleCollected", target = "dateSampleCollected", dateFormat = "MM/dd/yyyy")
    })
    ChildFollowupDTO childFollowupToDto(ChildFollowup childFollowup);

    List<ChildFollowupDTO> childFollowupToDto(List<ChildFollowup> childFollowup);
}
