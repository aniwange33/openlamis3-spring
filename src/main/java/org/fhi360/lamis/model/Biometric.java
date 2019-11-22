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
import javax.persistence.*;

/**
 *
 * @author User10
 */
@Entity
@Data
public class Biometric implements Serializable {

    @Id
    @Column(name = "BIOMETRIC_ID")
    private String id;

    @JoinColumn(name = "PATIENT_ID", referencedColumnName = "UUID")
    @ManyToOne(optional = false)
    private Patient patient;

    @JoinColumn(name = "FACILITY_ID", referencedColumnName = "FACILITY_ID")
    @ManyToOne(optional = false)
    private Facility facility;

    @Column(name = "HOSPITAL_NUM")
    private String hospitalNumber;

    @Lob
    private byte[] template;

    @Column(name = "BIOMETRIC_TYPE")
    private String biometricType;

    @Column(name = "TEMPLATE_TYPE")
    private String templateType;

    @Column(name = "PATIENT_NAME")
    private String name;

    @Column(name = "PATIENT_ADDRESS")
    private String address;

    @Column(name = "PATIENT_PHONE")
    private String phone;

    @Column(name = "PATIENT_GENDER")
    private String gender;

    @Column(name = "ENROLLMENT_DATE")
    private LocalDate date;
}
