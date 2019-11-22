package org.fhi360.lamis.model.dto;

import lombok.Data;

@Data
public class PartnerInformationDTO {
    private Long partnerinformationId;
    private Long patientId;
    private Long facilityId;
    private String dateVisit;
    private String partnerNotification;
    private String partnerHivStatus;
    private String partnerReferred;
}
