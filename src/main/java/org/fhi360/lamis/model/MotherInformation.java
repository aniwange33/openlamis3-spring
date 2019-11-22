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
@Table(name = "MOTHERINFORMATION")
@Data
public class MotherInformation extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MOTHERINFORMATION_ID")
    private Long motherinformationId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @Size(max = 25)
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

    @Size(max = 255)
    @Column(name = "ADDRESS")
    private String address;

    @Size(max = 60)
    @Column(name = "PHONE")
    private String phone;

    @Size(max = 120)
    @Column(name = "ART_STATUS")
    private String artStatus;

    @Size(max = 120)
    @Column(name = "ENTRY_POINT")
    private String entryPoint;

    @Size(max = 120)
    @Column(name = "ANC_NUMBER")
    private String ancNumber;

    @Size(max = 40)
    @Column(name = "IN_FACILITY")
    private String inFacility;

    @Column(name = "DATE_STARTED")
    private LocalDate dateStarted;

    @Column(name = "DATE_CONFIRMED_HIV")
    private LocalDate dateConfirmedHiv;

    @Size(max = 35)
    @Column(name = "TIME_HIV_DIAGNOSIS")
    private String timeHivDiagnosis;

    @Column(name = "DATE_ENROLLED_PMTCT")
    private LocalDate dateEnrolledPMTCT;

    @Size(max = 100)
    @Column(name = "REGIMEN")
    private String regimen;

    @OneToMany(mappedBy = "mother", cascade = CascadeType.ALL)
    List<Child> children = new ArrayList<>();
}
