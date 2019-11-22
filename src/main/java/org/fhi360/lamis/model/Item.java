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
import java.time.LocalDateTime;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "ITEM")
@Data
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ITEM_ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DESCRIPTION")
    private String description;

    @Size(max = 45)
    @Column(name = "UNIT_MEASURE")
    private String unitMeasure;

    @Column(name = "MAX_LEVEL")
    private Integer maxLevel;

    @Column(name = "MIN_LEVEL")
    private Integer minLevel;

    @Column(name = "DATE_LAST_RECEIVED")
    private LocalDate dateLastReceived;

    @Column(name = "DATE_LAST_ISSUED")
    private LocalDate dateLastIssued;

    @Column(name = "DATE_LAST_AUDITED")
    private LocalDate dateLastAudited;

    @Size(max = 1)
    @Column(name = "BALANCE_TYPE")
    private String balanceType;

    @Column(name = "UNIT_COST")
    private Double unitCost;

    @Column(name = "BALANCE")
    private Integer balance;

    @Column(name = "TIME_STAMP")
    @JsonIgnore
    private LocalDateTime timeStamp;
}
