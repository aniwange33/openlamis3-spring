package org.fhi360.lamis.controller.mapstruct;

import org.fhi360.lamis.model.MaternalFollowup;
import org.fhi360.lamis.model.dto.MaternalFollowupDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MaternalFollowupMapper {
    @Mappings({
            @Mapping(source = "facility.id", target = "facilityId"),
            @Mapping(source = "patient.patientId", target = "patientId"),
            @Mapping(source = "anc.ancId", target = "ancId"),
            @Mapping(source = "patient.hospitalNum", target = "hospitalNum"),
            @Mapping(source = "dateConfirmedHiv", target = "dateConfirmedHiv", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "timeHivDiagnosis", target = "timeHivDiagnosis", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateSampleCollected", target = "dateSampleCollected", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateNextVisit", target = "dateNextVisit", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateArvRegimenCurrent", target = "dateArvRegimenCurrent", dateFormat = "MM/dd/yyyy")
    })
    MaternalFollowupDTO modelToDto(MaternalFollowup followup);

    List<MaternalFollowupDTO> modelToDto(List<MaternalFollowup> followups);
}
