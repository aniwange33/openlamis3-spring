package org.fhi360.lamis.controller.mapstruct;

import org.fhi360.lamis.model.StatusHistory;

import org.fhi360.lamis.model.dto.StatusHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatusHistoryMapper {
    @Mappings({
            @Mapping(source = "patient.patientId", target = "patientId"),
            @Mapping(source = "facility.id", target = "facilityId"),
            @Mapping(target = "deletable", expression = "java(statusHistory.getCurrentStatus() == \"ART Start\" ? \"0\" : \"1\")"),
            @Mapping(source = "dateCurrentStatus", target = "dateCurrentStatus", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateTracked", target = "dateTracked", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "agreedDate", target = "agreedDate", dateFormat = "MM/dd/yyyy")
    })
    StatusHistoryDTO statusHistoryToDto(StatusHistory statusHistory);

    List<StatusHistoryDTO> statusHistoryToDto(List<StatusHistory> statusHistory);

    @Mappings({
            @Mapping(source = "patientId", target = "patient.patientId"),
            @Mapping(source = "facilityId", target = "facility.id"),
            @Mapping(source = "dateCurrentStatus", target = "dateCurrentStatus", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateTracked", target = "dateTracked", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "agreedDate", target = "agreedDate", dateFormat = "MM/dd/yyyy")
    })
    StatusHistory dtoToStatusHistory(StatusHistoryDTO dto);
}
