/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
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
@Table(name = "MESSAGE")
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MESSAGE_ID")
    private Long messageId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "MESSAGE_TYPE")
    private Integer messageType;

    @Column(name = "DAYS_TO_APPOINTMENT")
    private Integer daysToAppointment;

    @Column(name = "DATE_TO_SEND")
    private LocalDate dateToSend;

    @Size(max = 100)
    @Column(name = "RECIPIENTS")
    private String recipients;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "MESSAGE1")
    private String message1;

    @Size(max = 300)
    @Column(name = "MESSAGE2")
    private String message2;

    @Size(max = 300)
    @Column(name = "MESSAGE3")
    private String message3;

    @Size(max = 300)
    @Column(name = "MESSAGE4")
    private String message4;
}
