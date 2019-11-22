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
@Table(name = "PARTNERINFORMATION")
@Data
public class PartnerInformation extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PARTNERINFORMATION_ID")
    private Long partnerinformationId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @Column(name = "DATE_VISIT")
    private LocalDate dateVisit;

    @Size(max = 7)
    @Column(name = "PARTNER_NOTIFICATION")
    private String partnerNotification;

    @Size(max = 25)
    @Column(name = "PARTNER_HIV_STATUS")
    private String partnerHivStatus;

    @Size(max = 45)
    @Column(name = "PARTNER_REFERRED")
    private String partnerReferred;

}
