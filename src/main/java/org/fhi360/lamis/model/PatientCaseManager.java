/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;
import lombok.NonNull;

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
@Table(name = "PATIENTCASEMANAGER")
@Data
public class PatientCaseManager extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PATIENTCASEMANAGER_ID")
    private Long patientcasemanagerId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @JoinColumn(name = "CASEMANAGER_ID", nullable = false)
    @ManyToOne
    private CaseManager caseManager;

    @Column(name = "DATE_ASSIGNED")
    private LocalDate dateAssigned;

    @Size(max = 120)
    @Column(name = "ACTION")
    private String action;

    @PrePersist
    public void update() {
        dateAssigned = LocalDate.now();
    }
}
