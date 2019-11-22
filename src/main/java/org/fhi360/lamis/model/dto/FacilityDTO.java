/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model.dto;

import lombok.Data;

/**
 *
 * @author user1
 */
@Data
public class FacilityDTO {
     private Long facilityId;
     private Long lgaId;
     private String name;
     private String address1;
     private String address2;
     private String phone1;
     private String phone2;
     private String email;
     private String facilityType;
     private Integer padHospitalNum;

}
