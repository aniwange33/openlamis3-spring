/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author User10
 */
@Entity
@Table(name="TBSCREEN")
@Data
public class TBScreen implements Serializable {
    @Id
    @Column(name="TBSCREEN_ID")
    private Long id;
    
    private String description;
}
