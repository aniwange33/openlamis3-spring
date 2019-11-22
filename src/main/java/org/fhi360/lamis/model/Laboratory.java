/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "LABORATORY")
@Data
public class Laboratory extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "LABORATORY_ID")
    private Long laboratoryId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_REPORTED")
    @JsonIgnore
    private LocalDate dateReported;

    @Column(name = "DATE_COLLECTED")
    @JsonIgnore
    private LocalDate dateCollected;

    @Size(max = 15)
    @Column(name = "LABNO")
    private String labNo;

    @Size(max = 10)
    @Column(name = "RESULTAB")
    private String resultAB;

    @Size(max = 10)
    @Column(name = "RESULTPC")
    private String resultPC;

    @Size(max = 100)
    @Column(name = "COMMENT")
    private String comment;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "LABTEST_ID")
    @ManyToOne
    @JsonIgnore
    private LabTest labTest;
}
