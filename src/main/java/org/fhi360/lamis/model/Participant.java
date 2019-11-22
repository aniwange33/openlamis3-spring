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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "PARTICIPANT")
@Data
public class Participant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PARTICIPANT_ID")
    private Long id;

    @Size(max = 12)
    @Column(name = "PHONE")
    private String phone;

    @Column(name = "AGE")
    private Integer age;

    @Size(max = 7)
    @Column(name = "GENDER")
    private String gender;

    @Size(max = 100)
    @Column(name = "LOCATION")
    private String location;

    @Column(name = "ACTIVE")
    private Boolean active;

    @Column(name = "LOCAL_ID")
    @JsonIgnore
    private Long localId;
}
