/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "CONVERSATION")
@Data
@EqualsAndHashCode(of = "id")
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CONVERSATION_ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "PHONE")
    private String phone;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_MESSAGE")
    private LocalDate dateMessage;

    @Size(max = 200)
    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "TIME_STAMP")
    private LocalDateTime timeStamp;

    @Column(name = "ORIGINATOR_ID")
    private Long originatorId;

    @Column(name = "UNREAD")
    private Boolean unread;
}
