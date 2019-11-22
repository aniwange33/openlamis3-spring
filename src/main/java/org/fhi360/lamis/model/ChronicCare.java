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
@Table(name = "CHRONICCARE")
@Data
public class ChronicCare extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CHRONICCARE_ID")
    private Long chroniccareId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_VISIT")
    private LocalDate dateVisit;

    @Size(max = 75)
    @Column(name = "CLIENT_TYPE")
    private String clientType;

    @Size(max = 75)
    @Column(name = "CURRENT_STATUS")
    private String currentStatus;

    @Size(max = 15)
    @Column(name = "CLINIC_STAGE")
    private String clinicStage;

    @Size(max = 75)
    @Column(name = "PREGNANCY_STATUS")
    private String pregnancyStatus;

    @Column(name = "LAST_CD4")
    private Double lastCd4;

    @Column(name = "DATE_LAST_CD4")
    private LocalDate dateLastCd4;

    @Column(name = "LAST_VIRAL_LOAD")
    private Double lastViralLoad;

    @Column(name = "DATE_LAST_VIRAL_LOAD")
    private LocalDate dateLastViralLoad;

    @Size(max = 7)
    @Column(name = "ELIGIBLE_CD4")
    private String eligibleCd4;

    @Size(max = 7)
    @Column(name = "ELIGIBLE_VIRAL_LOAD")
    private String eligibleViralLoad;

    @Column(name = "COTRIM_ELIGIBILITY1")
    private Integer cotrimEligibility1;

    @Column(name = "COTRIM_ELIGIBILITY2")
    private Integer cotrimEligibility2;

    @Column(name = "COTRIM_ELIGIBILITY3")
    private Integer cotrimEligibility3;

    @Column(name = "COTRIM_ELIGIBILITY4")
    private Integer cotrimEligibility4;

    @Column(name = "COTRIM_ELIGIBILITY5")
    private Integer cotrimEligibility5;

    @Size(max = 7)
    @Column(name = "IPT")
    private String ipt;

    @Size(max = 7)
    @Column(name = "INH")
    private String inh;

    @Size(max = 7)
    @Column(name = "TB_TREATMENT")
    private String tbTreatment;

    @Column(name = "DATE_STARTED_TB_TREATMENT")
    private LocalDate dateStartedTbTreatment;

    @Size(max = 7)
    @Column(name = "TB_REFERRED")
    private String tbReferred;

    @Size(max = 7)
    @Column(name = "ELIGIBLE_IPT")
    private String eligibleIpt;

    @Size(max = 75)
    @Column(name = "TB_VALUES")
    private String tbValues;

    @Column(name = "BODY_WEIGHT")
    private Double bodyWeight;

    @Column(name = "HEIGHT")
    private Double height;

    @Size(max = 45)
    @Column(name = "BMI")
    private String bmi;

    @Column(name = "MUAC")
    private Double muac;

    @Size(max = 45)
    @Column(name = "MUAC_PEDIATRICS")
    private String muacPediatrics;

    @Size(max = 45)
    @Column(name = "MUAC_PREGNANT")
    private String muacPregnant;

    @Size(max = 7)
    @Column(name = "SUPPLEMENTARY_FOOD")
    private String supplementaryFood;

    @Size(max = 7)
    @Column(name = "NUTRITIONAL_STATUS_REFERRED")
    private String nutritionalStatusReferred;

    @Size(max = 7)
    @Column(name = "GBV1")
    private String gbv1;

    @Size(max = 7)
    @Column(name = "GBV1_REFERRED")
    private String gbv1Referred;

    @Size(max = 7)
    @Column(name = "GBV2")
    private String gbv2;

    @Size(max = 7)
    @Column(name = "GBV2_REFERRED")
    private String gbv2Referred;

    @Size(max = 7)
    @Column(name = "HYPERTENSIVE")
    private String hypertensive;

    @Size(max = 7)
    @Column(name = "FIRST_HYPERTENSIVE")
    private String firstHypertensive;

    @Size(max = 7)
    @Column(name = "BP_ABOVE")
    private String bpAbove;

    @Size(max = 7)
    @Column(name = "BP_REFERRED")
    private String bpReferred;

    @Size(max = 7)
    @Column(name = "DIABETIC")
    private String diabetic;

    @Size(max = 7)
    @Column(name = "FIRST_DIABETIC")
    private String firstDiabetic;

    @Size(max = 7)
    @Column(name = "DM_REFERRED")
    private String dmReferred;

    @Size(max = 50)
    @Column(name = "DM_VALUES")
    private String dmValues;

    @Size(max = 7)
    @Column(name = "PHDP1")
    private String phdp1;

    @Size(max = 7)
    @Column(name = "PHDP1_SERVICES_PROVIDED")
    private String phdp1ServicesProvided;

    @Size(max = 7)
    @Column(name = "PHDP2")
    private String phdp2;

    @Size(max = 7)
    @Column(name = "PHDP2_SERVICES_PROVIDED")
    private String phdp2ServicesProvided;

    @Size(max = 7)
    @Column(name = "PHDP3")
    private String phdp3;

    @Size(max = 7)
    @Column(name = "PHDP3_SERVICES_PROVIDED")
    private String phdp3ServicesProvided;

    @Size(max = 7)
    @Column(name = "PHDP4")
    private String phdp4;

    @Size(max = 7)
    @Column(name = "PHDP4_SERVICES_PROVIDED")
    private String phdp4ServicesProvided;

    @Size(max = 7)
    @Column(name = "PHDP5")
    private String phdp5;

    @Size(max = 7)
    @Column(name = "PHDP5_SERVICES_PROVIDED")
    private String phdp5ServicesProvided;

    @Column(name = "PHDP6")
    private Integer phdp6;

    @Size(max = 7)
    @Column(name = "PHDP6_SERVICES_PROVIDED")
    private String phdp6ServicesProvided;

    @Column(name = "PHDP7")
    private Integer phdp7;

    @Size(max = 7)
    @Column(name = "PHDP7_SERVICES_PROVIDED")
    private String phdp7ServicesProvided;

    @Size(max = 7)
    @Column(name = "PHDP8")
    private String phdp8;

    @Size(max = 7)
    @Column(name = "PHDP8_SERVICES_PROVIDED")
    private String phdp8ServicesProvided;

    @Size(max = 255)
    @Column(name = "PHDP9_SERVICES_PROVIDED")
    private String phdp9ServicesProvided;

    @Size(max = 7)
    @Column(name = "REPRODUCTIVE_INTENTIONS1")
    private String reproductiveIntentions1;

    @Size(max = 7)
    @Column(name = "REPRODUCTIVE_INTENTIONS1_REFERRED")
    private String reproductiveIntentions1Referred;

    @Size(max = 7)
    @Column(name = "REPRODUCTIVE_INTENTIONS2")
    private String reproductiveIntentions2;

    @Size(max = 7)
    @Column(name = "REPRODUCTIVE_INTENTIONS2_REFERRED")
    private String reproductiveIntentions2Referred;

    @Size(max = 7)
    @Column(name = "REPRODUCTIVE_INTENTIONS3")
    private String reproductiveIntentions3;

    @Size(max = 7)
    @Column(name = "REPRODUCTIVE_INTENTIONS3_REFERRED")
    private String reproductiveIntentions3Referred;

    @Size(max = 7)
    @Column(name = "MALARIA_PREVENTION1")
    private String malariaPrevention1;

    @Size(max = 7)
    @Column(name = "MALARIA_PREVENTION1_REFERRED")
    private String malariaPrevention1Referred;

    @Size(max = 7)
    @Column(name = "MALARIA_PREVENTION2")
    private String malariaPrevention2;

    @Size(max = 7)
    @Column(name = "MALARIA_PREVENTION2_REFERRED")
    private String malariaPrevention2Referred;

    @JoinColumn(name = "COMMUNITYPHARM_ID")
    @ManyToOne
    private CommunityPharmacy communityPharmacy;
}
