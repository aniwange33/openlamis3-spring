/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model.dto;

import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author user10
 */
public class PrescriptionDTO {
    
    private Long prescriptionId;
    private Long facilityId;
    private Long patientId;
    private String prescriptionType;
    private Long labtestId;
    private Integer status;
    private Long regimenTypeId;
    private Long regimenId;
    private Integer duration;
    private Date dateVisit;

    public PrescriptionDTO() {
        
    }

    public PrescriptionDTO(Long prescriptionId, Long facilityId, Long patientId, String prescriptionType, Long labtestId, Integer status, Long regimenTypeId, Long regimenId, Integer duration, Date dateVisit) {
        this.prescriptionId = prescriptionId;
        this.facilityId = facilityId;
        this.patientId = patientId;
        this.prescriptionType = prescriptionType;
        this.labtestId = labtestId;
        this.status = status;
        this.regimenTypeId = regimenTypeId;
        this.regimenId = regimenId;
        this.duration = duration;
        this.dateVisit = dateVisit;
    }

    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPrescriptionType() {
        return prescriptionType;
    }

    public void setPrescriptionType(String prescriptionType) {
        this.prescriptionType = prescriptionType;
    }

    public Long getLabtestId() {
        return labtestId;
    }

    public void setLabtestId(Long labtestId) {
        this.labtestId = labtestId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getRegimenTypeId() {
        return regimenTypeId;
    }

    public void setRegimenTypeId(Long regimenTypeId) {
        this.regimenTypeId = regimenTypeId;
    }

    public Long getRegimenId() {
        return regimenId;
    }

    public void setRegimenId(Long regimenId) {
        this.regimenId = regimenId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Date getDateVisit() {
        return dateVisit;
    }

    public void setDateVisit(Date dateVisit) {
        this.dateVisit = dateVisit;
    }

    @Override
    public String toString() {
        return "PrescriptionDTO{" + "prescriptionId=" + prescriptionId + ", id=" + facilityId + ", id=" + patientId + ", prescriptionType=" + prescriptionType + ", labtestId=" + labtestId + ", status=" + status + ", id=" + regimenTypeId + ", id=" + regimenId + ", duration=" + duration + ", dateVisit=" + dateVisit + '}';
    }

}
