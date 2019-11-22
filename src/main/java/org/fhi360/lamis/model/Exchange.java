/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "EXCHANGE")
@Data
public class Exchange implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "EXCHANGE_ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @Column(name = "CLINIC")
    private LocalDateTime clinic;

    @Column(name = "PHARMACY")
    private LocalDateTime pharmacy;

    @Column(name = "LABORATORY")
    private LocalDateTime laboratory;

    @Column(name = "ADRHISTORY")
    private LocalDateTime adrHistory;

    @Column(name = "OIHISTORY")
    private LocalDateTime oiHistory;

    @Column(name = "ADHEREHISTORY")
    private LocalDateTime adhereHistory;

    @Column(name = "STATUSHISTORY")
    private LocalDateTime statusHistory;

    @Column(name = "REGIMENHISTORY")
    private LocalDateTime regimenHistory;

    @Column(name = "SPECIMEN")
    private LocalDateTime specimen;

    @Column(name = "Eid")
    private LocalDateTime eid;

    @Column(name = "LABNO")
    private LocalDateTime labNo;

    @Column(name = "CHRONICCARE")
    private LocalDateTime chronicCare;

    @Column(name = "TBSCREENHISTORY")
    private LocalDateTime tbscreenHistory;

    @Column(name = "DMSCREENHISTORY")
    private LocalDateTime dmscreenHistory;
    
    @Column(name = "Anc")
    private LocalDateTime anc;

    @Column(name = "DELIVERY")
    private LocalDateTime delivery;

    @Column(name = "MATERNALFOLLOWUP")
    private LocalDateTime maternalfollowup;

    @Column(name = "CHILD")
    private LocalDateTime child;

    @Column(name = "CHILDFOLLOWUP")
    private LocalDateTime childfollowup;

    @Column(name = "PARTNERINFORMATION")
    private LocalDateTime partnerinformation;

    @Column(name = "NIGQUAL")
    private LocalDateTime nigqual;

    @Column(name = "DEVOLVE")
    private LocalDateTime devolve;

    @Column(name = "ENCOUNTER")
    private LocalDateTime encounter;
    
    @Column(name = "DRUGTHERAPY")
    private LocalDateTime drugtherapy;
    
    @Column(name = "APPOINTMENT")
    private LocalDateTime appointment;
    
    @Column(name = "Mhtc")
    private LocalDateTime mhtc;
    
    @Column(name = "COMMUNITYPHARM")
    private LocalDateTime communitypharm;
    
    @Column(name = "CASEMANAGER")
    private LocalDateTime casemanager;
    
    @Column(name = "PATIENTCASEMANAGER")
    private LocalDateTime patientcasemanager;
    
    @Column(name = "Eac")
    private LocalDateTime eac;
    
    @Column(name = "USER")
    private LocalDateTime user;
    
    @Column(name = "MONITOR")
    private LocalDateTime monitor;
    
    @Column(name = "EXPORT")
    private LocalDateTime export;
    
    @Column(name = "MOTHERINFORMATION")
    private LocalDateTime motherinformation;

    @NotNull
    @JoinColumn(name = "FACILITY_ID")
    @ManyToOne
    private Facility facility;
}
