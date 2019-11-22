package org.fhi360.lamis.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@Entity
public class Sync {
    @Id
    private Long facilityId;

    private String facilityName;

    @Temporal(TemporalType.DATE)
    private Date lastModified;
}
