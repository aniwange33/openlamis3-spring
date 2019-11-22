package org.fhi360.lamis.controller.mapstruct;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.model.Anc;
import org.fhi360.lamis.model.PartnerInformation;
import org.fhi360.lamis.model.dto.AncDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", imports = {StringUtils.class})
public interface AncMapper {

    @Mappings({
            @Mapping(source = "dateVisit", target = "dateVisit", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateCd4", target = "dateCd4", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "edd", target = "edd", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateViralLoad", target = "dateViralLoad", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateNextAppointment", target = "dateNextAppointment", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateArvRegimenCurrent", target = "dateArvRegimenCurrent", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateConfirmedHiv", target = "dateConfirmedHiv", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateVisit", target = "dateEnrolledPMTCT", dateFormat = "MM/dd/yyyy")
    })
    Anc dtoToAnc(AncDTO dto);

    @Mappings({
            @Mapping(source = "dateVisit", target = "dateVisit", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateCd4", target = "dateCd4", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "edd", target = "edd", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateViralLoad", target = "dateViralLoad", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateNextAppointment", target = "dateNextAppointment", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateArvRegimenCurrent", target = "dateArvRegimenCurrent", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateConfirmedHiv", target = "dateConfirmedHiv", dateFormat = "MM/dd/yyyy")
    })
    AncDTO ancToDto(Anc anc);

    List<AncDTO> ancToDto(List<Anc> anc);

    @Mappings({
            @Mapping(source = "partnerReferred", target = "partnerReferred"),
            @Mapping(source = "partnerinformationId", target = "partnerinformationId"),
            @Mapping(source = "partnerHivStatus", target = "partnerHivStatus"),
            @Mapping(source = "patientId", target = "patient.patientId"),
            @Mapping(target = "partnerNotification",
                    expression = "java(StringUtils.upperCase(dto.getPartnerNotification()))"),
            @Mapping(source = "dateVisit", target = "dateVisit", dateFormat = "MM/dd/yyyy")
    })
    PartnerInformation dtoToPartnerInformation(AncDTO dto);
}
