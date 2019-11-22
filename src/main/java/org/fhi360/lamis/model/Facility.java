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

/**
 *
 * @author User10
 */
@Entity
@Table(name = "FACILITY")
@Data
public class Facility implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "FACILITY_ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "STATE_ID")
    @ManyToOne
    @JsonIgnore
    private State state;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NAME")
    private String name;

    @Size(max = 45)
    @Column(name = "FACILITY_TYPE")
    private String facilityType;

    @Size(max = 45)
    @Column(name = "ADDRESS1")
    private String address1;

    @Size(max = 45)
    @Column(name = "ADDRESS2")
    private String address2;

    @Size(max = 25)
    @Column(name = "PHONE1")
    private String phone1;

    @Size(max = 25)
    @Column(name = "PHONE2")
    private String phone2;

    @Size(max = 45)
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PAD_HOSPITAL_NUM")
    private Integer padHospitalNum;

    @Column(name = "DAY_DQA")
    private Integer dayDqa;

    @Column(name = "ACTIVE")
    private Integer active;

    @Size(max = 45)
    @Column(name = "NATIONAL_ID")
    private String nationalId;

    @Size(max = 45)
    @Column(name = "DATIM_ID")
    private String datimId;

    @Size(max = 15)
    @Column(name = "PIN")
    private String pin;

    @JoinColumn(name = "LGA_ID", referencedColumnName = "LGA_ID")
    @ManyToOne(optional = false)
    private Lga lga;
}
