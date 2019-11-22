/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "USERPRIVILEGE")
@Data
public class UserPrivilege implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "USERPRIVILEGE_ID")
    private Long id;

    @JoinColumn(name = "PRIVILEGE_ID", referencedColumnName = "PRIVILEGE_ID")
    @ManyToOne(optional = false)
    private Privilege privilege;

    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private User user;
}
