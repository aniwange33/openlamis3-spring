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
import java.util.Date;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "CLINIC")
@Data
public class Clinic extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CLINIC_ID")
    private Long clinicId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_VISIT")
    private LocalDate dateVisit;

    @Size(max = 15)
    @Column(name = "CLINIC_STAGE")
    private String clinicStage;

    @Size(max = 15)
    @Column(name = "FUNC_STATUS")
    private String funcStatus;

    @Size(max = 75)
    @Column(name = "TB_STATUS")
    private String tbStatus;

    @Column(name = "VIRAL_LOAD")
    private Double viralLoad;

    @Column(name = "CD4")
    private Double cd4;

    @Column(name = "CD4P")
    private Double cd4p;

    @Size(max = 100)
    @Column(name = "REGIMENTYPE")
    private String regimenType;

    @Size(max = 100)
    @Column(name = "REGIMEN")
    private String regimen;

    @Column(name = "BODY_WEIGHT")
    private Double bodyWeight;

    @Column(name = "HEIGHT")
    private Double height;

    @Column(name = "WAIST")
    private Double waist;

    @Size(max = 7)
    @Column(name = "BP")
    private String bp;

    @Column(name = "PREGNANT")
    private Boolean pregnant;

    @Column(name = "LMP")
    private LocalDate lmp;

    @Column(name = "BREASTFEEDING")
    private Boolean breastfeeding;

    @Size(max = 5)
    @Column(name = "OI_SCREENED")
    private String oiScreened;

    @Size(max = 50)
    @Column(name = "STI_IDS")
    private String stiIds;

    @Size(max = 5)
    @Column(name = "STI_TREATED")
    private String stiTreated;

    @Size(max = 50)
    @Column(name = "OI_IDS")
    private String oiIds;

    @Size(max = 5)
    @Column(name = "ADR_SCREENED")
    private String adrScreened;

    @Size(max = 100)
    @Column(name = "ADR_IDS")
    private String adrIds;

    @Size(max = 15)
    @Column(name = "ADHERENCE_LEVEL")
    private String adherenceLevel;

    @Size(max = 50)
    @Column(name = "ADHERE_IDS")
    private String adhereIds;

    @Column(name = "COMMENCE")
    private Boolean commence;

    @Column(name = "NEXT_APPOINTMENT")
    private LocalDate nextAppointment;

    @Size(max = 500)
    @Column(name = "NOTES")
    private String notes;

    @Size(max = 90)
    @Column(name = "GESTATIONAL_AGE")
    private String gestationalAge;

    @Size(max = 90)
    @Column(name = "MATERNAL_STATUS_ART")
    private String maternalStatusArt;
}
