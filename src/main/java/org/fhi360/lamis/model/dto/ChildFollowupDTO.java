package org.fhi360.lamis.model.dto;

import lombok.Data;

@Data
public class ChildFollowupDTO {
    private Long facilityId;
    private Long childId;
    private String childfollowupId;
    private String dateVisit;
    private String ageVisit;
    private String dateNvpInitiated;
    private String ageNvpInitiated;
    private String dateCotrimInitiated;
    private String ageCotrimInitiated;
    private String bodyWeight;
    private String height;
    private String feeding;
    private String arv;
    private String arvType;
    private String dateRapidTest;
    private String arvTiming;
    private String cotrim;
    private String dateSampleCollected;
    private String reasonPcr;
    private String dateSampleSent;
    private String datePcrResult;
    private String pcrResult;
    private String rapidTest;
    private String rapidTestResult;
    private String caregiverGivenResult;
    private String childOutcome;
    private String referred;
    private String dateNextVisit;
    private String dateLinkedToArt;
}
