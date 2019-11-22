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
@Table(name = "PRESCRIPTION")
@Data
public class Prescription extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PRESCRIPTION_ID")
    private Long prescriptionId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @Column(name = "DATE_VISIT")
    private LocalDate dateVisit;

    @Size(max = 100)
    @Column(name = "PRESCRIPTION_TYPE")
    private String prescriptionType;

    @JoinColumn(name = "LABTEST_ID")
    @ManyToOne
    private LabTest labTest;

    @JoinColumn(name = "REGIMEN_ID")
    @ManyToOne
    private Regimen regimen;

    @JoinColumn(name = "REGIMENTYPE_ID")
    @ManyToOne
    private RegimenType regimenType;

    @Column(name = "DURATION")
    private Integer duration;
    @Column(name = "STATUS")
    private Integer status;

}
