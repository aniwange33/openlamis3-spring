/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "SPECIMEN")
@Data
public class Specimen extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SPECIMEN_ID")
    private Long specimenId;

    @Basic(optional = false)
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
    @JoinColumn(name = "LGA_ID")
    @ManyToOne
    private Lga lga;

    @Basic(optional = false)
    @NotNull
    @Column(name = "TREATMENT_UNIT_ID")
    private long treatmentUnitId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "SPECIMEN_TYPE")
    private String specimenType;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "LABNO")
    private String labNo;

    @Size(max = 15)
    @Column(name = "BARCODE")
    private String barcode;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_RECEIVED")
    @Temporal(TemporalType.DATE)
    private Date dateReceived;

    @Column(name = "DATE_COLLECTED")
    @Temporal(TemporalType.DATE)
    private Date dateCollected;

    @Column(name = "DATE_ASSAY")
    @Temporal(TemporalType.DATE)
    private Date dateAssay;

    @Column(name = "DATE_REPORTED")
    @Temporal(TemporalType.DATE)
    private Date dateReported;

    @Column(name = "DATE_DISPATCHED")
    @Temporal(TemporalType.DATE)
    private Date dateDispatched;

    @Column(name = "QUALITY_CNTRL")
    private Integer qualityCntrl;

    @Size(max = 25)
    @Column(name = "RESULT")
    private String result;

    @Size(max = 100)
    @Column(name = "REASON_NO_TEST")
    private String reasonNoTest;

    @Size(max = 25)
    @Column(name = "HOSPITAL_NUM")
    private String hospitalNum;

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
    @Temporal(TemporalType.DATE)
    private Date dateBirth;

    @Column(name = "AGE")
    private Integer age;

    @Size(max = 15)
    @Column(name = "AGE_UNIT")
    private String ageUnit;

    @Size(max = 100)
    @Column(name = "ADDRESS")
    private String address;

    @Size(max = 25)
    @Column(name = "PHONE")
    private String phone;

    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    @ManyToOne
    private User user;

    @JsonIgnore
    @Column(name = "TIME_STAMP")
    private LocalDateTime timeStamp;

    @JsonIgnore
    @Column(name = "UPLOADED")
    private Boolean uploaded;

    @JsonIgnore
    @Column(name = "TIME_UPLOADED")
    private LocalDateTime timeUploaded;

    @JsonIgnore
    @Size(max = 36)
    @Column(name = "UUID")
    private String uuid;

    @JsonIgnore
    @Column(name = "LOCAL_ID")
    private Long localId;
}
