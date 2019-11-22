package org.fhi360.lamis.model.dto;

import lombok.Data;

@Data
public class MaternalFollowupDTO {
    private String patientId;
    private String facilityId;
    private String hospitalNum;
    private String uniqueId;
    private String surname;
    private String otherNames;
    private String currentStatus;
    private String inFacility;
    private String address;
    private String name;
    private String dateConfirmedHiv;
    private String dateEnrolledPmtct;
    private String timeHivDiagnosis;
    private String dateStarted;
    private String maternalfollowupId;
    private String ancId;
    private String dateVisit;
    private String bodyWeight;
    private String bp;
    private String fundalHeight;
    private String fetalPresentation;
    private String syphilisTested;
    private String syphilisTestResult;
    private String viralLoadCollected;
    private String dateSampleCollected;
    private String cd4Ordered;
    private String cd4;
    private String counselNutrition;
    private String tbStatus;
    private String visitStatus;
    private String counselFeeding;
    private String counselFamilyPlanning;
    private String familyPlanningMethod;
    private String referred;
    private String dateNextVisit;
    private String typeOfVisit;
    private String gestationalAge;
    private String partnerHivStatus;
    private String arvRegimenPast;
    private String arvRegimenCurrent;
    private String dateArvRegimenCurrent;
    private String screenPostPartum;
}
