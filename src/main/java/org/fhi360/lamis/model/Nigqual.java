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
import java.util.Date;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "NIGQUAL")
@Data
public class Nigqual extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "NIGQUAL_ID")
    private Long nigqualId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @Basic(optional = false)
    @NotNull
    @Column(name = "PORTAL_ID")
    private Long portalId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "REVIEW_PERIOD_ID")
    private Integer reviewPeriodId;

    @Size(max = 2)
    @Column(name = "THERMATIC_AREA")
    private String thermaticArea;

    @Column(name = "REPORTING_DATE_BEGIN")
    private LocalDate reportingDateBegin;

    @Column(name = "REPORTING_DATE_END")
    private LocalDate reportingDateEnd;

    @Column(name = "POPULATION")
    private Integer population;

    @Column(name = "SAMPLE_SIZE")
    private Integer sampleSize;
}
