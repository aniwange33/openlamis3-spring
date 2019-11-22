package org.fhi360.lamis.model.dto;

import lombok.Data;

@Data
public class ChildDTO {
    private String childId;
    private String patientId;
    private String facilityId;
    private String deliveryId;
    private String ancId;
    private String referenceNum;
    private String registrationStatus;
    private String hospitalNumber;
    private String surname;
    private String otherNames;
    private String childName;
    private String dateBirth;
    private String gender;
    private String arv;
    private String hepb;
    private String hbv;
    private String bodyWeight;
    private String apgarScore;
    private String status;
    private String nameMother;
    private String inFacility;
    private String motherId;
}
