package org.fhi360.lamis.model.dto;

import lombok.Data;

@Data
public class ClinicDTO {
    private String clinicId;
    private String patientId;
    private String facilityId;
    private String deliveryId;
    private String ancId;
    private String hospitalNum;
    private String currentStatus;
    private String dateCurrentStatus;
    private String dateLastClinic;
    private String dateVisit;
    private String clinicStage;
    private String gestationalAge;
    private String funcStatus;
    private String tbStatus;
    private String viralLoad;
    private String cd4;
    private String cd4p;
    private String regimentype;
    private String regimen;
    private String bodyWeight;
    private String height;
    private String waist;
    private String bp;
    private String pregnant;
    private String lmp;
    private String breastfeeding;
    private String oiScreened;
    private String oiIds = "";
    private String adrScreened = "";
    private String adrIds = "";
    private String adherenceLevel;
    private String adhereIds = "";
    private String nextAppointment;
    private String notes;
    private String commence;
}
