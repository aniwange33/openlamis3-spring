/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
@Table(name = "STATUSHISTORY")
@Data
@EqualsAndHashCode(of = "historyId", callSuper = false)
public class StatusHistory extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "HISTORY_ID")
    private Long historyId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @Size(max = 75)
    @Column(name = "CURRENT_STATUS")
    private String currentStatus;

    @Column(name = "DATE_CURRENT_STATUS")
    private LocalDate dateCurrentStatus;

    @Size(max = 100)
    @Column(name = "OUTCOME")
    private String outcome;

    @Column(name = "AGREED_DATE")
    private LocalDate agreedDate;

    @Size(max = 100)
    @Column(name = "REASON_INTERRUPT")
    private String reasonInterrupt;

    @Column(name = "DATE_TRACKED")
    private LocalDate dateTracked;

    @Size(max = 100)
    @Column(name = "CAUSE_DEATH")
    private String causeDeath;
}
