package org.fhi360.lamis.model.dto;

import lombok.Data;
import org.fhi360.lamis.model.Anc;

@Data
public class AncDTO {
    private Long ancId;
    private String ancNum;
    private String dateVisit;
    private String uniqueId;
    private String dateEnrolledPMTCT;
    private String sourceReferral;
    private String lmp;
    private String edd;
    private Integer gestationalAge;
    private Integer gravida;
    private Integer parity;
    private String timeHivDiagnosis;
    private String arvRegimenPast;
    private String arvRegimenCurrent;
    private String dateArvRegimenCurrent;
    private String clinicStage;
    private String dateConfirmedHiv;
    private String funcStatus;
    private Double bodyWeight;
    private String cd4Ordered;
    private Double cd4;
    private Integer numberAncVisit;
    private String dateCd4;
    private String viralLoadTestDone;
    private String dateViralLoad;
    private String syphilisTested;
    private String syphilisTestResult;
    private String syphilisTreated;
    private String hepatitisBTested;
    private String hepatitisBTestResult;
    private String hepatitisCTested;
    private String hepatitisCTestResult;
    private String dateNextAppointment;
    private String viralLoadResult;
    private Double height;
    private Long patientId;
    private String partnerNotification;
    private String partnerHivStatus;
    private String partnerReferred;
    private String partnerinformationId;
}
