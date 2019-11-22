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

/**
 *
 * @author User10
 */
@Entity
@Table(name = "Mhtc")
@Data
public class Mhtc extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MHTC_ID")
    private Long mhtcId;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "COMMUNITYPHARM_ID")
    @ManyToOne
    private CommunityPharmacy communityPharmacy;

    @Basic(optional = false)
    @NotNull
    @Column(name = "MONTH")
    private int month;

    @Basic(optional = false)
    @NotNull
    @Column(name = "YEAR")
    private int year;

    @Column(name = "NUM_TESTED")
    private Integer numTested;

    @Column(name = "NUM_POSITIVE")
    private Integer numPositive;

    @Column(name = "NUM_REFERRED")
    private Integer numReferred;

    @Column(name = "NUM_ONSITE_VISIT")
    private Integer numOnsiteVisit;
}
