/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "VALIDATED")
@Data
public class Validated extends TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "VALIDATED_ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "PATIENT_ID")
    @ManyToOne
    private Patient patient;

    @Size(max = 90)
    @Column(name = "TABLE_VALIDATED")
    private String tableValidated;

    @Size(max = 100)
    @Column(name = "RECORD_ID")
    private String recordId;

    @Size(max = 100)
    @Column(name = "CREATED_BY")
    private String createdBy;

    @Size(max = 100)
    @Column(name = "VALIDATED_BY")
    private String validatedBy;

    @Column(name = "DATE_VALIDATED")
    private LocalDateTime dateValidated;
}
