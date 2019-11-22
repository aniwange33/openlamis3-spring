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
 * @author User10
 */
@Entity
@Table(name = "DRUGTHERAPY")
@Data
public class DrugTherapy extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DRUGTHERAPY_ID")
    private Long drugTherapyId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @Basic(optional = false)
    @NotNull
    @ManyToOne
    @JoinColumn(name = "COMMUNITYPHARM_ID")
    private CommunityPharmacy communityPharmacy;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_VISIT")
    private LocalDate dateVisit;

    @Size(max = 100)
    @Column(name = "OIS")
    private String ois;

    @Size(max = 5)
    @Column(name = "THERAPY_PROBLEM_SCREENED")
    private String therapyProblemScreened;

    @Size(max = 5)
    @Column(name = "ADHERENCE_ISSUES")
    private String adherenceIssues;

    @Size(max = 100)
    @Column(name = "MEDICATION_ERROR")
    private String medicationError;

    @Size(max = 100)
    @Column(name = "ADRS")
    private String adrs;

    @Size(max = 45)
    @Column(name = "SEVERITY")
    private String severity;

    @Size(max = 5)
    @Column(name = "ICSR_FORM")
    private String icsrForm;

    @Size(max = 5)
    @Column(name = "ADR_REFERRED")
    private String adrReferred;

}
