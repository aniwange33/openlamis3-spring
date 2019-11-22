package org.fhi360.lamis.model.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Date;

@Data
public class IndexContactDto {
    private Long indexcontactId;
    private String clientCode;
    private String contactType;
    private String surname;
    private String otherNames;
    private LocalDate dateBirth;
    private Integer age;
    private String ageUnit;
    private String gender;
    private String phone;
    private String address;
    private String relation;
    private String gbv;
    private Integer durationPartner;
    private String phoneTracking;
    private String homeTracking;
    private String outcome;
    private Date dateHivTest;
    private String hivStatus;
    private String linkCare;
    private String partnerNotification;
    private String modeNotification;
    private String serviceProvided;
    private String hospitalNum;
    private String relationship;
    private Long htsId;
    private Long facilityId;
}
