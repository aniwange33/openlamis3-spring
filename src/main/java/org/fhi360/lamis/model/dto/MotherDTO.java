package org.fhi360.lamis.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class MotherDTO {
    private Long patientId;
    private Long facilityId;
    private String willing;
    private String dateConfirmedHiv;
    private String motherUniqueId;
    private String motherSurname;
    private String motherOtherNames;
    private String address;
    private String phone;
    private String inFacility;
    private String dateStarted;
    private List<ChildDTO> children;
}
