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
@Table(name = "PHARMACY")
@Data
public class Pharmacy extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PHARMACY_ID")
    private Long pharmacyId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_VISIT")
    private LocalDate dateVisit;

    @Column(name = "DURATION")
    private Integer duration;

    @Column(name = "MORNING")
    private Double morning;

    @Column(name = "AFTERNOON")
    private Double afternoon;

    @Column(name = "EVENING")
    private Double evening;

    @Size(max = 5)
    @Column(name = "ADR_SCREENED")
    private String adrScreened;

    @Size(max = 100)
    @Column(name = "ADR_IDS")
    private String adrIds;

    @Column(name = "PRESCRIP_ERROR")
    private Integer prescriptionError;

    @Column(name = "ADHERENCE")
    private Integer adherence;

    @Column(name = "NEXT_APPOINTMENT")
    private LocalDate nextAppointment;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "REGIMENTYPE_ID")
    @ManyToOne
    private RegimenType regimenType;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "REGIMEN_ID")
    @ManyToOne
    private Regimen regimen;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "REGIMENDRUG_ID")
    @ManyToOne
    private RegimenDrug regimenDrug;

    @Transient
    private Long regimendrugId;
}
