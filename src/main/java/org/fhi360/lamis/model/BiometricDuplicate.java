/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "BIOMETRIC_DUPLICATE")
@Data
public class BiometricDuplicate implements Serializable {

    @Id
    @Column(name = "BIOMETRIC_DUPLICATE_ID")
    private String id;

    @JoinColumn(name = "PATIENT_ID_1", referencedColumnName = "UUID")
    @ManyToOne(optional = false)
    private Patient patient1;

    @JoinColumn(name = "PATIENT_ID_2", referencedColumnName = "UUID")
    @ManyToOne(optional = false)
    private Patient patient2;

    @JoinColumn(name = "FACILITY_ID", referencedColumnName = "FACILITY_ID")
    @ManyToOne(optional = false)
    private Facility facility;

    @Column(name = "DUPLICATE_DATE")
    private LocalDate date;
}
