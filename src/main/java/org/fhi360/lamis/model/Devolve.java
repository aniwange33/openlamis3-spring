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

/**
 *
 * @author User10
 */
@Entity
@Table(name = "DEVOLVE")
@Data
public class Devolve extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DEVOLVE_ID")
    private Long devolveId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @JoinColumn(name = "COMMUNITYPHARM_ID")
    @ManyToOne
    private CommunityPharmacy communityPharmacy;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_DEVOLVED")
    @JsonIgnore
    private LocalDate dateDevolved;

    @Size(max = 15)
    @Column(name = "TYPE_DMOC")
    private String typeDmoc;

    @Size(max = 5)
    @Column(name = "VIRAL_LOAD_ASSESSED")
    private String viralLoadAssessed;

    @Column(name = "LAST_VIRAL_LOAD")
    private Double lastViralLoad;

    @Column(name = "DATE_LAST_VIRAL_LOAD")
    private LocalDate dateLastViralLoad;

    @Size(max = 5)
    @Column(name = "CD4_ASSESSED")
    private String cd4Assessed;

    @Column(name = "LAST_CD4")
    private Double lastCd4;

    @Column(name = "DATE_LAST_CD4")
    private LocalDate dateLastCd4;

    @Size(max = 15)
    @Column(name = "LAST_CLINIC_STAGE")
    private String lastClinicStage;

    @Column(name = "DATE_LAST_CLINIC_STAGE")
    private LocalDate dateLastClinicStage;

    @Size(max = 5)
    @Column(name = "ARV_DISPENSED")
    private String arvDispensed;

    @Size(max = 100)
    @Column(name = "REGIMENTYPE")
    private String regimentype;

    @Size(max = 100)
    @Column(name = "REGIMEN")
    private String regimen;

    @Column(name = "DATE_NEXT_CLINIC")
    private LocalDate dateNextClinic;

    @Column(name = "DATE_NEXT_REFILL")
    private LocalDate dateNextRefill;

    @Column(name = "DATE_LAST_CLINIC")
    private LocalDate dateLastClinic;

    @Column(name = "DATE_LAST_REFILL")
    private LocalDate dateLastRefill;

    @Size(max = 500)
    @Column(name = "NOTES")
    private String notes;

    @Column(name = "DATE_DISCONTINUED")
    private LocalDate dateDiscontinued;

    @Size(max = 25)
    @Column(name = "REASON_DISCONTINUED")
    private String reasonDiscontinued;
}
