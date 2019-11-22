/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author User10
 */
@Table(name="NDRCODESET")
@Entity
@Data
public class NDRCodeSet implements Serializable {
    @EmbeddedId
    private CodeSet codeSet;
    
    @Column(name="CODE_DESCRIPTION")
    private String description;

    private String altDescription;

    private String sysDescription;
}
