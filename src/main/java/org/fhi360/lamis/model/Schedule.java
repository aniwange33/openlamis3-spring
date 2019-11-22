/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "SCHEDULE")
@Data
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SCHEDULE_ID")
    private Long id;

    @Column(name = "MONDAY")
    private Integer monday;

    @Column(name = "TUESDAY")
    private Integer tuesday;

    @Column(name = "WEDNESDAY")
    private Integer wednesday;

    @Column(name = "THURSDAY")
    private Integer thursday;

    @Column(name = "FRIDAY")
    private Integer friday;

    @Column(name = "SATURDAY")
    private Integer saturday;

    @Column(name = "SUNDAY")
    private Integer sunday;

    @Size(max = 100)
    @Column(name = "SERVICE")
    private String service;

    @JoinColumn(name = "FACILITY_ID", referencedColumnName = "FACILITY_ID")
    @ManyToOne(optional = false)
    private Facility facility;

    @Column(name = "LOCAL_ID")
    private Long localId;
}
