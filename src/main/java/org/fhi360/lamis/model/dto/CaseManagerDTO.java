package org.fhi360.lamis.model.dto;

import lombok.Data;

@Data
public class CaseManagerDTO {
    private Long casemanagerId;
    private Long facilityId;
    private String fullname;
    private String address;
    private String phone;
    private String religion;
    private Integer age;
    private String sex;
}
