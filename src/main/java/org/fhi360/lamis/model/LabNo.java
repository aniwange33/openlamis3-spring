/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "LABNO")
@Data
public class LabNo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "LABNO_ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "FACILITY_ID")
    @ManyToOne
    private Facility facility;

    @Column(name = "YEAR")
    private Integer year;

    @Column(name = "LASTNO")
    private Integer lastNo;

    @Column(name = "TIME_STAMP")
    @JsonIgnore
    private LocalDateTime timeStamp;

    @Column(name = "UPLOADED")
    @JsonIgnore
    private Boolean uploaded;

    @Column(name = "TIME_UPLOADED")
    @JsonIgnore
    private LocalDateTime timeUploaded;

    @Size(max = 36)
    @JsonIgnore
    @Column(name = "UUID")
    private String uuid;

    @Column(name = "LOCAL_ID")
    @JsonIgnore
    private Long localId;
    }
