/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

/**
 *
 * @author User10
 */
@Entity
@Table(name = "REGIMENDRUG")
@Data

public class RegimenDrug implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "REGIMENDRUG_ID")
    private Long regimendrugId;

    @JoinColumn(name = "DRUG_ID")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Drug drug;

    @JoinColumn(name = "REGIMEN_ID")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Regimen regimen;

}
