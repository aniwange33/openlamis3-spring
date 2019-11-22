/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.dto;

import java.util.Date;

/**
 *
 * @author user10
 */
public class PatientLite {
    private long patientId;
    private long facilityId;
    private String hospitalNum;
    private String uniqueId;
    private String surname;
    private String otherNames;
    private String gender;
    private Date dateBirth;
    private Integer age;
    private String ageUnit;
    private String maritalStatus;
    private String education;
    private String occupation;
    private String address;
    private String phone;
    private String state;
    private String lga;
    private String nextKin;
    private String addressKin;
    private String phoneKin;
    private String relationKin;
    private String entryPoint;
    private String targetGroup;
    private Date dateConfirmedHiv;
    private String tbStatus;
    private Integer pregnant;
    private Integer breastfeeding;
    private String enrollmentSetting;
    private Date dateRegistration;
    private String statusRegistration;
    private Date dateStarted;
    private String currentStatus;
    private Date dateCurrentStatus;
    private String regimentype;
    private String regimen;
    private String lastClinicStage;
    private Double lastViralLoad;
    private Double lastCd4;
    private Double lastCd4p;
    private Date dateLastCd4;
    private Date dateLastViralLoad;
    private Date viralLoadDueDate;
    private String viralLoadType;
    private Date dateLastRefill;
    private Date dateNextRefill;
    private Integer lastRefillDuration;
    private String lastRefillSetting;
    private String sourceReferral;
    private String timeHivDiagnosis;
    private Date dateLastClinic;
    private Date dateNextClinic;
    private String causeDeath;
    private Date dateTracked;
    private String outcome;
    private Date agreedDate;
    private Date dateEnrolledPmtct;
    private Integer sendMessage;
    private Date timeStamp;
    private Long userId;
    private Integer uploaded;
    private Date timeUploaded;
    private Long casemanagerId;
    private Long communitypharmId;
    private String idUUID;

    
    public PatientLite() {        
    }

    public PatientLite(long patientId, long facilityId, String hospitalNum, String uniqueId, String surname, String otherNames, String gender, Date dateBirth, Integer age, String ageUnit, String maritalStatus, String education, String occupation, String address, String phone, String state, String lga, String nextKin, String addressKin, String phoneKin, String relationKin, String entryPoint, String targetGroup, Date dateConfirmedHiv, String tbStatus, Integer pregnant, Integer breastfeeding, String enrollmentSetting, Date dateRegistration, String statusRegistration, Date dateStarted, String currentStatus, Date dateCurrentStatus, String regimentype, String regimen, String lastClinicStage, Double lastViralLoad, Double lastCd4, Double lastCd4p, Date dateLastCd4, Date dateLastViralLoad, Date viralLoadDueDate, String viralLoadType, Date dateLastRefill, Date dateNextRefill, Integer lastRefillDuration, String lastRefillSetting, String sourceReferral, String timeHivDiagnosis, Date dateLastClinic, Date dateNextClinic, String causeDeath, Date dateTracked, String outcome, Date agreedDate, Date dateEnrolledPmtct, Integer sendMessage, Date timeStamp, Long userId, Integer uploaded, Date timeUploaded, Long casemanagerId, Long communitypharmId, String idUUID) {
        this.patientId = patientId;
        this.facilityId = facilityId;
        this.hospitalNum = hospitalNum;
        this.uniqueId = uniqueId;
        this.surname = surname;
        this.otherNames = otherNames;
        this.gender = gender;
        this.dateBirth = dateBirth;
        this.age = age;
        this.ageUnit = ageUnit;
        this.maritalStatus = maritalStatus;
        this.education = education;
        this.occupation = occupation;
        this.address = address;
        this.phone = phone;
        this.state = state;
        this.lga = lga;
        this.nextKin = nextKin;
        this.addressKin = addressKin;
        this.phoneKin = phoneKin;
        this.relationKin = relationKin;
        this.entryPoint = entryPoint;
        this.targetGroup = targetGroup;
        this.dateConfirmedHiv = dateConfirmedHiv;
        this.tbStatus = tbStatus;
        this.pregnant = pregnant;
        this.breastfeeding = breastfeeding;
        this.enrollmentSetting = enrollmentSetting;
        this.dateRegistration = dateRegistration;
        this.statusRegistration = statusRegistration;
        this.dateStarted = dateStarted;
        this.currentStatus = currentStatus;
        this.dateCurrentStatus = dateCurrentStatus;
        this.regimentype = regimentype;
        this.regimen = regimen;
        this.lastClinicStage = lastClinicStage;
        this.lastViralLoad = lastViralLoad;
        this.lastCd4 = lastCd4;
        this.lastCd4p = lastCd4p;
        this.dateLastCd4 = dateLastCd4;
        this.dateLastViralLoad = dateLastViralLoad;
        this.viralLoadDueDate = viralLoadDueDate;
        this.viralLoadType = viralLoadType;
        this.dateLastRefill = dateLastRefill;
        this.dateNextRefill = dateNextRefill;
        this.lastRefillDuration = lastRefillDuration;
        this.lastRefillSetting = lastRefillSetting;
        this.sourceReferral = sourceReferral;
        this.timeHivDiagnosis = timeHivDiagnosis;
        this.dateLastClinic = dateLastClinic;
        this.dateNextClinic = dateNextClinic;
        this.causeDeath = causeDeath;
        this.dateTracked = dateTracked;
        this.outcome = outcome;
        this.agreedDate = agreedDate;
        this.dateEnrolledPmtct = dateEnrolledPmtct;
        this.sendMessage = sendMessage;
        this.timeStamp = timeStamp;
        this.userId = userId;
        this.uploaded = uploaded;
        this.timeUploaded = timeUploaded;
        this.casemanagerId = casemanagerId;
        this.communitypharmId = communitypharmId;
        this.idUUID = idUUID;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(long facilityId) {
        this.facilityId = facilityId;
    }

    public String getHospitalNum() {
        return hospitalNum;
    }

    public void setHospitalNum(String hospitalNum) {
        this.hospitalNum = hospitalNum;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAgeUnit() {
        return ageUnit;
    }

    public void setAgeUnit(String ageUnit) {
        this.ageUnit = ageUnit;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getNextKin() {
        return nextKin;
    }

    public void setNextKin(String nextKin) {
        this.nextKin = nextKin;
    }

    public String getAddressKin() {
        return addressKin;
    }

    public void setAddressKin(String addressKin) {
        this.addressKin = addressKin;
    }

    public String getPhoneKin() {
        return phoneKin;
    }

    public void setPhoneKin(String phoneKin) {
        this.phoneKin = phoneKin;
    }

    public String getRelationKin() {
        return relationKin;
    }

    public void setRelationKin(String relationKin) {
        this.relationKin = relationKin;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
    }

    public String getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(String targetGroup) {
        this.targetGroup = targetGroup;
    }

    public Date getDateConfirmedHiv() {
        return dateConfirmedHiv;
    }

    public void setDateConfirmedHiv(Date dateConfirmedHiv) {
        this.dateConfirmedHiv = dateConfirmedHiv;
    }

    public String getTbStatus() {
        return tbStatus;
    }

    public void setTbStatus(String tbStatus) {
        this.tbStatus = tbStatus;
    }

    public Integer getPregnant() {
        return pregnant;
    }

    public void setPregnant(Integer pregnant) {
        this.pregnant = pregnant;
    }

    public Integer getBreastfeeding() {
        return breastfeeding;
    }

    public void setBreastfeeding(Integer breastfeeding) {
        this.breastfeeding = breastfeeding;
    }

    public String getEnrollmentSetting() {
        return enrollmentSetting;
    }

    public void setEnrollmentSetting(String enrollmentSetting) {
        this.enrollmentSetting = enrollmentSetting;
    }

    public Date getDateRegistration() {
        return dateRegistration;
    }

    public void setDateRegistration(Date dateRegistration) {
        this.dateRegistration = dateRegistration;
    }

    public String getStatusRegistration() {
        return statusRegistration;
    }

    public void setStatusRegistration(String statusRegistration) {
        this.statusRegistration = statusRegistration;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Date getDateCurrentStatus() {
        return dateCurrentStatus;
    }

    public void setDateCurrentStatus(Date dateCurrentStatus) {
        this.dateCurrentStatus = dateCurrentStatus;
    }

    public String getRegimentype() {
        return regimentype;
    }

    public void setRegimentype(String regimentype) {
        this.regimentype = regimentype;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public String getLastClinicStage() {
        return lastClinicStage;
    }

    public void setLastClinicStage(String lastClinicStage) {
        this.lastClinicStage = lastClinicStage;
    }

    public Double getLastViralLoad() {
        return lastViralLoad;
    }

    public void setLastViralLoad(Double lastViralLoad) {
        this.lastViralLoad = lastViralLoad;
    }

    public Double getLastCd4() {
        return lastCd4;
    }

    public void setLastCd4(Double lastCd4) {
        this.lastCd4 = lastCd4;
    }

    public Double getLastCd4p() {
        return lastCd4p;
    }

    public void setLastCd4p(Double lastCd4p) {
        this.lastCd4p = lastCd4p;
    }

    public Date getDateLastCd4() {
        return dateLastCd4;
    }

    public void setDateLastCd4(Date dateLastCd4) {
        this.dateLastCd4 = dateLastCd4;
    }

    public Date getDateLastViralLoad() {
        return dateLastViralLoad;
    }

    public void setDateLastViralLoad(Date dateLastViralLoad) {
        this.dateLastViralLoad = dateLastViralLoad;
    }

    public Date getViralLoadDueDate() {
        return viralLoadDueDate;
    }

    public void setViralLoadDueDate(Date viralLoadDueDate) {
        this.viralLoadDueDate = viralLoadDueDate;
    }

    public String getViralLoadType() {
        return viralLoadType;
    }

    public void setViralLoadType(String viralLoadType) {
        this.viralLoadType = viralLoadType;
    }

    public Date getDateLastRefill() {
        return dateLastRefill;
    }

    public void setDateLastRefill(Date dateLastRefill) {
        this.dateLastRefill = dateLastRefill;
    }

    public Date getDateNextRefill() {
        return dateNextRefill;
    }

    public void setDateNextRefill(Date dateNextRefill) {
        this.dateNextRefill = dateNextRefill;
    }

    public Integer getLastRefillDuration() {
        return lastRefillDuration;
    }

    public void setLastRefillDuration(Integer lastRefillDuration) {
        this.lastRefillDuration = lastRefillDuration;
    }

    public String getLastRefillSetting() {
        return lastRefillSetting;
    }

    public void setLastRefillSetting(String lastRefillSetting) {
        this.lastRefillSetting = lastRefillSetting;
    }

    public String getSourceReferral() {
        return sourceReferral;
    }

    public void setSourceReferral(String sourceReferral) {
        this.sourceReferral = sourceReferral;
    }

    public String getTimeHivDiagnosis() {
        return timeHivDiagnosis;
    }

    public void setTimeHivDiagnosis(String timeHivDiagnosis) {
        this.timeHivDiagnosis = timeHivDiagnosis;
    }

    public Date getDateLastClinic() {
        return dateLastClinic;
    }

    public void setDateLastClinic(Date dateLastClinic) {
        this.dateLastClinic = dateLastClinic;
    }

    public Date getDateNextClinic() {
        return dateNextClinic;
    }

    public void setDateNextClinic(Date dateNextClinic) {
        this.dateNextClinic = dateNextClinic;
    }

    public String getCauseDeath() {
        return causeDeath;
    }

    public void setCauseDeath(String causeDeath) {
        this.causeDeath = causeDeath;
    }

    public Date getDateTracked() {
        return dateTracked;
    }

    public void setDateTracked(Date dateTracked) {
        this.dateTracked = dateTracked;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public Date getAgreedDate() {
        return agreedDate;
    }

    public void setAgreedDate(Date agreedDate) {
        this.agreedDate = agreedDate;
    }

    public Date getDateEnrolledPmtct() {
        return dateEnrolledPmtct;
    }

    public void setDateEnrolledPmtct(Date dateEnrolledPmtct) {
        this.dateEnrolledPmtct = dateEnrolledPmtct;
    }

    public Integer getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(Integer sendMessage) {
        this.sendMessage = sendMessage;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getUploaded() {
        return uploaded;
    }

    public void setUploaded(Integer uploaded) {
        this.uploaded = uploaded;
    }

    public Date getTimeUploaded() {
        return timeUploaded;
    }

    public void setTimeUploaded(Date timeUploaded) {
        this.timeUploaded = timeUploaded;
    }

    public Long getCasemanagerId() {
        return casemanagerId;
    }

    public void setCasemanagerId(Long casemanagerId) {
        this.casemanagerId = casemanagerId;
    }

    public Long getCommunitypharmId() {
        return communitypharmId;
    }

    public void setCommunitypharmId(Long communitypharmId) {
        this.communitypharmId = communitypharmId;
    }

    public String getIdUUID() {
        return idUUID;
    }

    public void setIdUUID(String idUUID) {
        this.idUUID = idUUID;
    }

    
}
