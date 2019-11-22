/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "BIOMETRIC_TRANSFER")
@Data
public class BiometricTransfer implements Serializable {

    @Id
    @Column(name = "BIOMETRIC_DUPLICATE_ID")
    private String id;

    @JoinColumn(name = "PATIENT_ID", referencedColumnName = "UUID")
    @ManyToOne(optional = false)
    private Patient patient;

    @Column(name = "HOSPITAL_NUM")
    private String hospitalNumber;

    @JoinColumn(name = "FACILITY_ID", referencedColumnName = "FACILITY_ID")
    @ManyToOne(optional = false)
    private Facility facility;

    @JoinColumn(name = "TRANSFER_FACILITY_ID", referencedColumnName = "FACILITY_ID")
    @ManyToOne(optional = false)
    private Facility transferFacility;

    @Column(name = "TRANSFER_DATE")
    private LocalDate date;
}
