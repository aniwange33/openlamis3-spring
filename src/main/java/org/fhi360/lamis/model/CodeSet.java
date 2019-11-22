/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 *
 * @author User10
 */
@Embeddable
@Data
public class CodeSet implements Serializable {
    @Column(name="CODE_SET_NM")
    private String name;
    
    private String code;
}
