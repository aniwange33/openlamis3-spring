/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author User10
 */
@Entity
@Table(name = "Anc")
@Data
public class Anc extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ANC_ID")
    private Long ancId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "ANC_NUM")
    private String ancNum;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_VISIT")
    private LocalDate dateVisit;

    @Size(max = 45)
    @Column(name = "UNIQUE_ID")
    private String uniqueId;

    @Column(name = "DATE_ENROLLED_PMTCT")
    private LocalDate dateEnrolledPMTCT;

    @Size(max = 45)
    @Column(name = "SOURCE_REFERRAL")
    private String sourceReferral;

    @Column(name = "LMP")
    private LocalDate lmp;

    @Column(name = "EDD")
    private LocalDate edd;

    @Column(name = "GESTATIONAL_AGE")
    private Integer gestationalAge;

    @Column(name = "GRAVIDA")
    private Integer gravida;

    @Column(name = "PARITY")
    private Integer parity;

    @Size(max = 45)
    @Column(name = "TIME_HIV_DIAGNOSIS")
    private String timeHivDiagnosis;

    @Size(max = 100)
    @Column(name = "ARV_REGIMEN_PAST")
    private String arvRegimenPast;

    @Size(max = 100)
    @Column(name = "ARV_REGIMEN_CURRENT")
    private String arvRegimenCurrent;

    @Column(name = "DATE_ARV_REGIMEN_CURRENT")
    private LocalDate dateArvRegimenCurrent;

    @Size(max = 15)
    @Column(name = "CLINIC_STAGE")
    private String clinicStage;

    @Column(name = "DATE_CONFIRMED_HIV")
    private LocalDate dateConfirmedHiv;

    @Size(max = 45)
    @Column(name = "FUNC_STATUS")
    private String funcStatus;

    @Min(value = 2)
    @Max(value = 300)
    @Column(name = "BODY_WEIGHT")
    private Double bodyWeight;

    @Size(max = 7)
    @Column(name = "CD4_ORDERED")
    private String cd4Ordered;

    @Column(name = "CD4")
    private Double cd4;

    @Column(name = "NUMBER_ANC_VISIT")
    private Integer numberAncVisit;

    @Column(name = "DATE_CD4")
    private LocalDate dateCd4;

    @Size(max = 10)
    @Column(name = "VIRAL_LOAD_TEST_DONE")
    private String viralLoadTestDone;

    @Column(name = "DATE_VIRAL_LOAD")
    private LocalDate dateViralLoad;

    @Size(max = 50)
    @Column(name = "SYPHILIS_TESTED")
    private String syphilisTested;

    @Size(max = 50)
    @Column(name = "SYPHILIS_TEST_RESULT")
    private String syphilisTestResult;

    @Size(max = 50)
    @Column(name = "SYPHILIS_TREATED")
    private String syphilisTreated;

    @Size(max = 50)
    @Column(name = "HEPATITISB_TESTED")
    private String hepatitisBTested;

    @Size(max = 50)
    @Column(name = "HEPATITISB_TEST_RESULT")
    private String hepatitisBTestResult;

    @Size(max = 50)
    @Column(name = "HEPATITISC_TESTED")
    private String hepatitisCTested;

    @Size(max = 50)
    @Column(name = "HEPATITISC_TEST_RESULT")
    private String hepatitisCTestResult;

    @Column(name = "DATE_NEXT_APPOINTMENT")
    private LocalDate dateNextAppointment;

    @Size(max = 10)
    @Column(name = "VIRAL_LOAD_RESULT")
    private String viralLoadResult;

    @Column(name = "HEIGHT")
    private Double height;
}
