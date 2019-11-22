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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "DELIVERY")
@Data
public class Delivery extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DELIVERY_ID")
    private Long deliveryId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "FACILITY_ID")
    @ManyToOne
    private Facility facility;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_DELIVERY")
    private LocalDate dateDelivery;

    @JoinColumn(name = "ANC_ID")
    @ManyToOne
    private Anc anc;

    @Column(name = "BOOKING_STATUS")
    private Integer bookingStatus;

    @Size(max = 25)
    @Column(name = "ROM_DELIVERY_INTERVAL")
    private String romDeliveryInterval;

    @Size(max = 45)
    @Column(name = "MODE_DELIVERY")
    private String modeDelivery;

    @Size(max = 25)
    @Column(name = "EPISIOTOMY")
    private String episiotomy;

    @Size(max = 7)
    @Column(name = "VAGINAL_TEAR")
    private String vaginalTear;

    @Size(max = 25)
    @Column(name = "MATERNAL_OUTCOME")
    private String maternalOutcome;

    @Column(name = "GESTATIONAL_AGE")
    private Integer gestationalAge;

    @Size(max = 100)
    @Column(name = "TIME_HIV_DIAGNOSIS")
    private String timeHivDiagnosis;

    @Size(max = 100)
    @Column(name = "SOURCE_REFERRAL")
    private String sourceReferral;

    @Size(max = 70)
    @Column(name = "HEPATITISB_STATUS")
    private String hepatitisBStatus;

    @Size(max = 70)
    @Column(name = "HEPATITISC_STATUS")
    private String hepatitisCStatus;

    @Column(name = "SCREEN_POST_PARTUM")
    private Boolean screenPostPartum;

    @Size(max = 100)
    @Column(name = "ARV_REGIMEN_PAST")
    private String arvRegimenPast;

    @Size(max = 100)
    @Column(name = "ARV_REGIMEN_CURRENT")
    private String arvRegimenCurrent;

    @Column(name = "DATE_ARV_REGIMEN_CURRENT")
    private LocalDate dateArvRegimenCurrent;

    @Column(name = "DATE_CONFIRMED_HIV")
    private LocalDate dateConfirmedHiv;

    @Size(max = 15)
    @Column(name = "CLINIC_STAGE")
    private String clinicStage;

    @Size(max = 7)
    @Column(name = "CD4_ORDERED")
    private String cd4Ordered;

    @Column(name = "CD4")
    private Double cd4;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    List<Child> children = new ArrayList<>();
}
