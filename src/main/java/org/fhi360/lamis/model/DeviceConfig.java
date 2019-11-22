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

/**
 *
 * @author User10
 */
@Entity
@Table(name = "DEVICECONFIG")
@Data
public class DeviceConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DEVICECONFIG_ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "FACILITY_ID")
    @ManyToOne
    private Facility facility;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "DEVICE_ID")
    private String deviceId;
}
