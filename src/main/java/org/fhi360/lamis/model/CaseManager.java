/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "CASEMANAGER")
@Data
public class CaseManager extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CASEMANAGER_ID")
    private Long casemanagerId;

    @Size(max = 100)
    @Column(name = "FULLNAME")
    private String fullName;

    @Size(max = 50)
    @Column(name = "SEX")
    private String sex;

    @Size(max = 30)
    @Column(name = "AGE")
    private String age;

    @Size(max = 80)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Size(max = 50)
    @Column(name = "RELIGION")
    private String religion;

    @Size(max = 300)
    @Column(name = "ADDRESS")
    private String address;
}
