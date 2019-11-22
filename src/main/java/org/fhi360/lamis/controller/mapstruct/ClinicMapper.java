package org.fhi360.lamis.controller.mapstruct;

import org.fhi360.lamis.model.Clinic;
import org.fhi360.lamis.model.dto.ClinicDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClinicMapper {
    @Mappings({
            @Mapping(source = "facilityId", target = "facility.id"),
            @Mapping(source = "patientId", target = "patient.patientId"),
            @Mapping(source = "lmp", target = "lmp", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateVisit", target = "dateVisit", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "nextAppointment", target = "nextAppointment", dateFormat = "MM/dd/yyyy"),
            @Mapping(target = "commence", expression = "java(dto.getCommence() == \"1\" ? true : false)"),
            @Mapping(target = "breastfeeding",
                    expression = "java(dto.getBreastfeeding() == \"1\" ? true : false)"),
            @Mapping(target = "pregnant", expression = "java(dto.getPregnant() == \"1\" ? true : false)"),
            @Mapping(source = "regimentype", target = "regimenType"),
    })
    Clinic dtoToClinic(ClinicDTO dto);

    @Mappings({
            @Mapping(source = "facility.id", target = "facilityId"),
            @Mapping(source = "patient.patientId", target = "patientId"),
            @Mapping(source = "regimenType", target = "regimentype"),
            @Mapping(source = "lmp", target = "lmp", dateFormat = "MM/dd/yyyy"),

            @Mapping(source = "dateVisit", target = "dateVisit", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "nextAppointment", target = "nextAppointment", dateFormat = "MM/dd/yyyy"),
            @Mapping(target = "commence", expression = "java(clinic.getCommence() == null || !clinic.getCommence() ? \"0\" : \"1\")"),
            @Mapping(target = "breastfeeding",
                    expression = "java(clinic.getBreastfeeding() == null || !clinic.getBreastfeeding() ? \"0\" : \"1\")"),
            @Mapping(target = "pregnant", expression = "java(clinic.getPregnant() == null || !clinic.getPregnant() ? \"1\" : \"0\")"),
    })
    ClinicDTO clinicToDto(Clinic clinic);

    List<ClinicDTO> clinicToDto(List<Clinic> clinic);
}
