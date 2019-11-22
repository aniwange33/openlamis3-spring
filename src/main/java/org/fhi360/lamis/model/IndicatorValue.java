/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "INDICATORVALUE")
@Data
public class IndicatorValue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "INDICATORVALUE_ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DATA_ELEMENT_ID")
    private Long dataElementId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "STATE_ID")
    @ManyToOne
    private State state;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "LGA_ID")
    @ManyToOne
    private Lga lga;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "FACILITY_ID")
    @ManyToOne
    private Facility facility;

    @Column(name = "VALUE")
    private Integer value;

    @Column(name = "REPORT_DATE")
    private LocalDate reportDate;

    @Column(name = "LOCAL_ID")
    private Long localId;
}
