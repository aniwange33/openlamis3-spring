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
@Table(name = "APPOINTMENT")
@Data
public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "APPOINTMENT_ID")
    private Long appointmentId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @Basic(optional = false)
    @NotNull
    @ManyToOne
    @JoinColumn(name = "FACILITY_ID")
    private Facility facility;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "COMMUNITYPHARM_ID")
    @ManyToOne
    private CommunityPharmacy communityPharmacy;

    @Column(name = "DATE_LAST_VISIT")
    private LocalDate dateLastVisit;

    @Column(name = "DATE_NEXT_VISIT")
    private LocalDate dateNextVisit;

    @Column(name = "DATE_TRACKED")
    private LocalDate dateTracked;

    @Size(max = 15)
    @Column(name = "TYPE_TRACKING")
    private String typeTracking;

    @Size(max = 15)
    @Column(name = "TRACKING_OUTCOME")
    private String trackingOutcome;

    @Column(name = "DATE_AGREED")
    private LocalDate dateAgreed;

    @Transient
    private String hospitalNum;

    @Transient
    private Long facilityId;
}
