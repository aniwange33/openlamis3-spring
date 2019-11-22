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
@Table(name = "COMMUNITYPHARM")
@Data
public class CommunityPharmacy implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COMMUNITYPHARM_ID")
    private Long communitypharmId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "STATE_ID")
    @ManyToOne
    @JsonIgnore
    private State state;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "LGA_ID")
    @ManyToOne
    private Lga lga;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "PHARMACY")
    private String pharmacy;

    @Size(max = 300)
    @Column(name = "ADDRESS")
    private String address;

    @Size(max = 25)
    @Column(name = "PHONE")
    private String phone;

    @Size(max = 25)
    @Column(name = "PHONE1")
    private String phone1;

    @Size(max = 100)
    @Column(name = "EMAIL")
    private String email;

    @Size(max = 13)
    @Column(name = "PIN")
    private String pin;
}
