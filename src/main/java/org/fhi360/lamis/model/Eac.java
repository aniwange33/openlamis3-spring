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
import java.time.LocalDateTime;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "Eac")
@Data
public class Eac extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "EAC_ID")
    private Long eacId;
    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @Column(name = "DATE_EAC1")
    private LocalDate dateEac1;

    @Column(name = "DATE_EAC2")
    private LocalDate dateEac2;

    @Column(name = "DATE_EAC3")
    private LocalDate dateEac3;

    @Column(name = "DATE_SAMPLE_COLLECTED")
    private LocalDateTime dateSampleCollected;

    @Size(max = 500)
    @Column(name = "NOTES")
    private String notes;

    @Column(name = "LAST_VIRAL_LOAD")
    private Double lastViralLoad;

    @Column(name = "DATE_LAST_VIRAL_LOAD")
    private LocalDate dateLastViralLoad;
}
