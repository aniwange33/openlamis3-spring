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

/**
 *
 * @author User10
 */
@Entity
@Table(name = "INDEXCONTACT")
@Data
public class IndexContact extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "INDEXCONTACT_ID")
    private Long indexcontactId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "CLIENT_CODE")
    private String clientCode;

    @Size(max = 100)
    @Column(name = "CONTACT_TYPE")
    private String contactType;

    @Size(max = 100)
    @Column(name = "INDEXCONTACT_CODE")
    private String indexcontactCode;

    @Size(max = 100)
    @Column(name = "SURNAME")
    private String surname;

    @Size(max = 100)
    @Column(name = "OTHER_NAMES")
    private String otherNames;

    @Column(name = "DATE_BIRTH")
    private LocalDate dateBirth;

    @Column(name = "AGE")
    private Integer age;

    @Size(max = 15)
    @Column(name = "AGE_UNIT")
    private String ageUnit;

    @Size(max = 20)
    @Column(name = "GENDER")
    private String gender;

    @Size(max = 50)
    @Column(name = "PHONE")
    private String phone;

    @Size(max = 200)
    @Column(name = "ADDRESS")
    private String address;

    @Size(max = 100)
    @Column(name = "RELATION")
    private String relation;

    @Size(max = 5)
    @Column(name = "GBV")
    private String gbv;

    @Column(name = "DURATION_PARTNER")
    private Integer durationPartner;

    @Size(max = 5)
    @Column(name = "PHONE_TRACKING")
    private String phoneTracking;

    @Size(max = 5)
    @Column(name = "HOME_TRACKING")
    private String homeTracking;

    @Size(max = 100)
    @Column(name = "OUTCOME")
    private String outcome;

    @Column(name = "DATE_HIV_TEST")
    private LocalDate dateHivTest;

    @Size(max = 100)
    @Column(name = "HIV_STATUS")
    private String hivStatus;

    @Size(max = 5)
    @Column(name = "LINK_CARE")
    private String linkCare;

    @Size(max = 8)
    @Column(name = "PARTNER_NOTIFICATION")
    private String partnerNotification;

    @Size(max = 100)
    @Column(name = "MODE_NOTIFICATION")
    private String modeNotification;

    @Size(max = 300)
    @Column(name = "SERVICE_PROVIDED")
    private String serviceProvided;

    @Size(max = 25)
    @Column(name = "HOSPITAL_NUM")
    private String hospitalNum;

    @Size(max = 100)
    @Column(name = "RELATIONSHIP")
    private String relationship;

    @JoinColumn(name = "HTS_ID")
    @ManyToOne(optional = false)
    private Hts hts;
}
