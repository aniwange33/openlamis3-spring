/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.fhi360.lamis.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "CHILDFOLLOWUP")
@Data
public class ChildFollowup extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CHILDFOLLOWUP_ID")
    private Long childfollowupId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_VISIT")
    private LocalDate dateVisit;

    @Column(name = "AGE_VISIT")
    private Integer ageVisit;

    @Column(name = "DATE_NVP_INITIATED")
    private LocalDate dateNvpInitiated;

    @Column(name = "AGE_NVP_INITIATED")
    private Integer ageNvpInitiated;

    @Column(name = "DATE_COTRIM_INITIATED")
    private LocalDate dateCotrimInitiated;

    @Column(name = "AGE_COTRIM_INITIATED")
    private Integer ageCotrimInitiated;

    @Column(name = "BODY_WEIGHT")
    private Double bodyWeight;

    @Column(name = "HEIGHT")
    private Double height;

    @Size(max = 75)
    @Column(name = "FEEDING")
    private String feeding;

    @Size(max = 7)
    @Column(name = "ARV")
    private String arv;

    @Size(max = 7)
    @Column(name = "COTRIM")
    private String cotrim;

    @Column(name = "DATE_SAMPLE_COLLECTED")
    private LocalDate dateSampleCollected;

    @Size(max = 75)
    @Column(name = "REASON_PCR")
    private String reasonPcr;

    @Column(name = "DATE_SAMPLE_SENT")
    private LocalDate dateSampleSent;

    @Column(name = "DATE_PCR_RESULT")
    private LocalDate datePcrResult;

    @Size(max = 45)
    @Column(name = "PCR_RESULT")
    private String pcrResult;

    @Size(max = 45)
    @Column(name = "RAPID_TEST")
    private String rapidTest;

    @Size(max = 45)
    @Column(name = "RAPID_TEST_RESULT")
    private String rapidTestResult;

    @Size(max = 7)
    @Column(name = "CAREGIVER_GIVEN_RESULT")
    private String caregiverGivenResult;

    @Size(max = 45)
    @Column(name = "CHILD_OUTCOME")
    private String childOutcome;

    @Size(max = 45)
    @Column(name = "REFERRED")
    private String referred;

    @Column(name = "DATE_NEXT_VISIT")
    private LocalDate dateNextVisit;

    @Size(max = 70)
    @Column(name = "INFANT_OUTCOME")
    private String infantOutcome;

    @Column(name = "DATE_LINKED_TO_ART")
    private LocalDate dateLinkedToArt;

    @Size(max = 45)
    @Column(name = "ARV_TYPE")
    private String arvType;

    @Column(name = "DATE_RAPID_TEST")
    private LocalDate dateRapidTest;

    @Size(max = 90)
    @Column(name = "ARV_TIMING")
    private String arvTiming;

    @JoinColumn(name = "CHILD_ID")
    @ManyToOne(optional = false)
    private Child child;

    @Transient
    private String referenceNum;
}
