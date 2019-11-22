/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author User10
 */
@Entity
@Table(name = "APPLICATIONINFO")
@Data
public class ApplicationInfo implements Serializable {

    @Id
    @Column(name = "APPLICATIONINFO_ID")
    private Long id;

    private String databaseVersion;
    
    private String applicationVersion;
    
    @Column(name = "DATE_DATABASE_UPDATE")
    private LocalDateTime databaseUpdated;
    
    @Column(name = "DATE_APPLICATION_UPDATE")
    private LocalDate applicationUpdated;
}
