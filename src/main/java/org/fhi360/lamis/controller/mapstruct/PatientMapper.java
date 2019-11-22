package org.fhi360.lamis.controller.mapstruct;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.PatientDTO;
import org.fhi360.lamis.utility.Scrambler;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", imports = {StringUtils.class})
public interface PatientMapper {
    @Mappings({
            @Mapping(target = "hospitalNum", expression = "java(StringUtils.leftPad(dto.getHospitalNum(), 7, \"0\"))"),
            @Mapping(source = "dateBirth", target = "dateBirth", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateConfirmedHiv", target = "dateConfirmedHiv", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "casemanagerId", target = "caseManager.casemanagerId"),
            @Mapping(source = "dateLastCd4", target = "dateLastCd4", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateLastViralLoad", target = "dateLastViralLoad", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateLastRefill", target = "dateLastRefill", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateNextRefill", target = "dateNextRefill", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateNextClinic", target = "dateNextClinic", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateLastClinic", target = "dateLastClinic", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "facilityId", target = "facility.id"),
            @Mapping(source = "dateEnrolledPmtct", target = "dateEnrolledPMTCT", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateRegistration", target = "dateRegistration", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateStarted", target = "dateStarted", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateCurrentStatus", target = "dateCurrentStatus", dateFormat = "MM/dd/yyyy"),
    })
    Patient dtoToPatient(PatientDTO dto);

    @Mappings({
            @Mapping(source = "communityPharmacy.communitypharmId", target = "communitypharmId"),
            @Mapping(source = "facility.id", target = "facilityId"),
            @Mapping(source = "dateLastViralLoad", target = "dateLastViralLoad", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateLastRefill", target = "dateLastRefill", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateBirth", target = "dateBirth", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateConfirmedHiv", target = "dateConfirmedHiv", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateStarted", target = "dateStarted", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateLastCd4", target = "dateLastCd4", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateNextClinic", target = "dateNextClinic", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateLastClinic", target = "dateLastClinic", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateNextRefill", target = "dateNextRefill", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "caseManager.casemanagerId", target = "casemanagerId"),
            @Mapping(source = "facility.state.name", target = "state"),
            @Mapping(source = "facility.lga.name", target = "lga"),
            @Mapping(target = "name", expression = "java(patient.getSurname() + \" \" + patient.getOtherNames())"),
            @Mapping(source = "dateEnrolledPMTCT", target = "dateEnrolledPmtct", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateRegistration", target = "dateRegistration", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateCurrentStatus", target = "dateCurrentStatus", dateFormat = "MM/dd/yyyy"),
            @Mapping(target = "devolve", expression = "java(patient.getCommunityPharmacy() != null ? 1 : 0)")
    })
    PatientDTO patientToDto(Patient patient);

    List<PatientDTO> patientToDto(List<Patient> patients);

}
