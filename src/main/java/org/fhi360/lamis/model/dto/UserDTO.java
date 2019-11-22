package org.fhi360.lamis.model.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private Long facilityId;
    private String username;
    private String password;
    private String fullName;
    private String surname;
    private String otherNames;
    private String stateIds;
    private String userGroup;
    private  String viewIdentifier = "0";

}
