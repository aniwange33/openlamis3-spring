/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "DRUG")
@Data
public class Drug implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DRUG_ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ABBREV")
    private String abbrev;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NAME")
    private String name;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "STRENGTH")
    private String strength;

    @Basic(optional = false)
    @NotNull
    @Column(name = "PACK_SIZE")
    private int packSize;

    @Size(max = 45)
    @Column(name = "DOSEFORM")
    private String doseForm;

    @Column(name = "MORNING")
    private Integer morning;

    @Column(name = "AFTERNOON")
    private Integer afternoon;

    @Column(name = "EVENING")
    private Integer evening;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "ITEM_ID")
    @ManyToOne
    private Item item;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "drug")
    private List<RegimenDrug> regimenDrugList;
    
}
