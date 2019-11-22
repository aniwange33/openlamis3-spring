/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "PERFORMANCE")
@Data
public class Performance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PERFORMANCE_ID")
    private Long id;

    @Column(name = "YEAR")
    private Integer year;

    @Column(name = "MONTH")
    private Integer month;

    @Column(name = "DENOMINATOR")
    private Integer denominator;

    @Column(name = "NUMERATOR")
    private Integer numerator;

    @Column(name = "INDICATOR_ID")
    private Integer indicatorId;

    @JoinColumn(name = "FACILITY_ID")
    @ManyToOne
    private Facility facility;
}
