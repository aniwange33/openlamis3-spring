/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "PATIENT")
@Data
public class Patient extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PATIENT_ID")
    private Long patientId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "HOSPITAL_NUM")
    private String hospitalNum;

    @Size(max = 36)
    @Column(name = "UNIQUE_ID")
    private String uniqueId;

    @Size(max = 45)
    @Column(name = "SURNAME")
    private String surname;

    @Size(max = 75)
    @Column(name = "OTHER_NAMES")
    private String otherNames;

    @Size(max = 7)
    @Column(name = "GENDER")
    private String gender;

    @Column(name = "DATE_BIRTH")
    private LocalDate dateBirth;

    @Column(name = "AGE")
    private Integer age;

    @Size(max = 15)
    @Column(name = "AGE_UNIT")
    private String ageUnit;

    @Size(max = 15)
    @Column(name = "MARITAL_STATUS")
    private String maritalStatus;

    @Size(max = 25)
    @Column(name = "EDUCATION")
    private String education;

    @Size(max = 25)
    @Column(name = "OCCUPATION")
    private String occupation;

    @Size(max = 100)
    @Column(name = "ADDRESS")
    private String address;

    @Size(max = 25)
    @Column(name = "PHONE")
    private String phone;

    @Size(max = 75)
    @Column(name = "NEXT_KIN")
    private String nextKin;

    @Size(max = 100)
    @Column(name = "ADDRESS_KIN")
    private String addressKin;

    @Size(max = 25)
    @Column(name = "PHONE_KIN")
    private String phoneKin;

    @Size(max = 25)
    @Column(name = "RELATION_KIN")
    private String relationKin;

    @Size(max = 15)
    @Column(name = "ENTRY_POINT")
    private String entryPoint;

    @Size(max = 15)
    @Column(name = "TARGET_GROUP")
    private String targetGroup;

    @Column(name = "DATE_CONFIRMED_HIV")
    private LocalDate dateConfirmedHiv;

    @Column(name = "DATE_ENROLLED_PMTCT")
    private LocalDate dateEnrolledPMTCT;

    @Size(max = 100)
    @Column(name = "SOURCE_REFERRAL")
    private String sourceReferral;

    @Size(max = 35)
    @Column(name = "TIME_HIV_DIAGNOSIS")
    private String timeHivDiagnosis;

    @Size(max = 75)
    @Column(name = "TB_STATUS")
    private String tbStatus;

    @Column(name = "PREGNANT")
    private Boolean pregnant;

    @Column(name = "BREASTFEEDING")
    private Boolean breastfeeding;

    @Column(name = "DATE_REGISTRATION")
    private LocalDate dateRegistration;

    @Size(max = 75)
    @Column(name = "STATUS_REGISTRATION")
    private String statusRegistration;

    @Size(max = 15)
    @Column(name = "ENROLLMENT_SETTING")
    private String enrollmentSetting;

    @JoinColumn(name = "CASEMANAGER_ID")
    @ManyToOne
    private CaseManager caseManager;

    @JoinColumn(name = "COMMUNITYPHARM_ID")
    @ManyToOne
    private CommunityPharmacy communityPharmacy;

    @Column(name = "DATE_STARTED")
    private LocalDate dateStarted;

    @Size(max = 75)
    @Column(name = "CURRENT_STATUS")
    private String currentStatus;

    @Column(name = "DATE_CURRENT_STATUS")
    private LocalDate dateCurrentStatus;

    @Size(max = 100)
    @Column(name = "REGIMENTYPE")
    private String regimentype;

    @Size(max = 100)
    @Column(name = "REGIMEN")
    private String regimen;

    @Size(max = 15)
    @Column(name = "LAST_CLINIC_STAGE")
    private String lastClinicStage;

    @Column(name = "LAST_VIRAL_LOAD")
    private Double lastViralLoad;

    @Column(name = "LAST_CD4")
    private Double lastCd4;

    @Column(name = "LAST_CD4P")
    private Double lastCd4p;

    @Column(name = "DATE_LAST_CD4")
    private LocalDate dateLastCd4;

    @Column(name = "DATE_LAST_VIRAL_LOAD")
    private LocalDate dateLastViralLoad;

    @Column(name = "VIRAL_LOAD_DUE_DATE")
    private LocalDate viralLoadDueDate;

    @Size(max = 15)
    @Column(name = "VIRAL_LOAD_TYPE")
    private String viralLoadType;

    @Column(name = "DATE_LAST_REFILL")
    private LocalDate dateLastRefill;

    @Column(name = "DATE_NEXT_REFILL")
    private LocalDate dateNextRefill;

    @Column(name = "LAST_REFILL_DURATION")
    private Integer lastRefillDuration;

    @Size(max = 15)
    @Column(name = "LAST_REFILL_SETTING")
    private String lastRefillSetting;

    @Column(name = "DATE_LAST_CLINIC")
    private LocalDate dateLastClinic;

    @Column(name = "DATE_NEXT_CLINIC")
    private LocalDate dateNextClinic;

    @Column(name = "DATE_TRACKED")
    private LocalDate dateTracked;

    @Size(max = 75)
    @Column(name = "OUTCOME")
    private String outcome;

    @Size(max = 100)
    @Column(name = "CAUSE_DEATH")
    private String causeDeath;

    @Column(name = "AGREED_DATE")
    private LocalDate agreedDate;

    @Column(name = "SEND_MESSAGE")
    private Boolean sendMessage;

    @Basic(optional = false)
    @JoinColumn(name = "PARTNERINFORMATION_ID")
    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
    private PartnerInformation partnerInformation;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "patient")
    @JsonIgnore
    private List<Devolve> devolves;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Prescription> prescriptions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "patient")
    @JsonIgnore
    private List<Validated> validateds;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Eac> eacs;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "patient")
    @JsonIgnore
    private List<Encounter> encounters;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "patient")
    @JsonIgnore
    private List<DrugTherapy> drugTherapies;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "patient")
    @JsonIgnore
    private List<Nigqual> nigquals;
}
