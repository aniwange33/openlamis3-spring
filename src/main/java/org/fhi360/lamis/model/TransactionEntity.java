package org.fhi360.lamis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Data
public abstract class TransactionEntity {
    @Column(name = "TIME_STAMP")
    @JsonIgnore
    private LocalDateTime timeStamp;

    @Column(name = "UPLOADED")
    @JsonIgnore
    private Boolean uploaded;

    @Column(name = "TIME_UPLOADED")
    @JsonIgnore
    private LocalDateTime timeUploaded;

    @Size(max = 36)
    @JsonIgnore
    @Column(name = "UUID")
    private String uuid;

    /*
    @JsonIgnore
    @Column(name = "LOCAL_ID")
    private Long localId;
    */

    @Basic(optional = false)
    @NotNull
    @ManyToOne
    @JoinColumn(name = "FACILITY_ID")
    @JsonIgnore
    private Facility facility;
    /*
    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "LGA_ID")
    @ManyToOne
    private Lga lga;

    @Basic(optional = false)
    @NotNull
    @JoinColumn(name = "STATE_ID")
    @ManyToOne
    private State state;
    */

    @JoinColumn(name = "USER_ID")
    @ManyToOne
    @JsonIgnore
    private User user;

    @Transient
    private String hospitalNum;

    @Transient
    private Long facilityId;

    @PrePersist
    public void preSave() {
        uuid = UUID.randomUUID().toString();
        timeStamp = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        timeStamp = LocalDateTime.now();
    }
}
