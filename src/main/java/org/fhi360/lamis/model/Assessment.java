/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "ASSESSMENT")
public class Assessment extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ASSESSMENT_ID")
    private Long assessmentId;

    private LocalDate dateVisit;

    private String clientCode;

    private String question1;

    private String question2;

    private Integer question3;

    private Integer question4;

    private Integer question5;

    private Integer question6;

    private Integer question7;

    private Integer question8;

    private Integer question9;

    private Integer question10;

    private Integer question11;

    private Integer question12;
  
    private Integer sti1;

    private Integer sti2;

    private Integer sti3;

    private Integer sti4;

    private Integer sti5;

    private Integer sti6;

    private Integer sti7;

    private Integer sti8;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "DEVICECONFIG_ID")
    @ManyToOne
    private DeviceConfig deviceConfig;
}
