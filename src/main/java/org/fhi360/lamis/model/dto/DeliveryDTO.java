package org.fhi360.lamis.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DeliveryDTO {
    private Long deliveryId;
    private Long patientId;
    private Long facilityId;
    private Long ancId;
    private String bookingStatus;
    private String dateDelivery;
    private String romDeliveryInterval;
    private String modeDelivery;
    private String episiotomy;
    private String vaginalTear;
    private String maternalOutcome;
    private String timeHivDiagnosis;
    private String screenPostPartum;
    private String sourceReferral;
    private String hepatitisBStatus;
    private String hepatitisCStatus;
    private int gestationalAge;
    private String arvRegimenPast;
    private String arvRegimenCurrent;
    private String clinicStage;
    private String cd4Ordered;
    private Double cd4;
    private String dateArvRegimenCurrent;
    private String dateConfirmedHiv;
    private List<ChildDTO> children = new ArrayList<>();
    private PartnerInformationDTO partnerInformation;
}
