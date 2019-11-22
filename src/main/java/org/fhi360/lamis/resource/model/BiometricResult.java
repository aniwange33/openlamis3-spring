/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.resource.model;

/**
 *
 * @author User10
 */
public class BiometricResult {
    private Long patientId;
    private Long facilityId;
    private String name;
    private String address;
    private String phone;
    private String gender;
    private String message;
    private Boolean inFacility;

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public BiometricResult() {
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }    

    public Boolean getInFacility() {
        return inFacility;
    }

    public void setInFacility(Boolean inFacility) {
        this.inFacility = inFacility;
    }

    @Override
    public String toString() {
        return "BiometricResult{" + "id=" + patientId + ", id=" + facilityId + ", name=" + name + ", address=" + address + ", phone=" + phone + ", gender=" + gender + ", message=" + message + '}';
    }
}
