/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "DHISVALUE")
@Data
public class DhisValue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DHISVALUE_ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DATA_ELEMENT_ID")
    private long dataElementId;

    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "STATE_ID")
    private long stateId;

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

    @Size(max = 15)
    @Column(name = "PERIOD")
    private String period;

    @Column(name = "VALUE")
    private Integer value;

    @Column(name = "LOCAL_ID")
    private Long localId;
}
