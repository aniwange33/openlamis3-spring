/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model.dto;

import org.fhi360.lamis.model.Patient;


/**
 *
 * @author user1
 */
public class PatientObjHandler {

    public static void store(Patient patient) {
    //    ServletActionContext.getRequest().getSession().setAttribute("patientObjSession", patient);
    }
    
    /*public static Patient restore(Patient patient) {
        if(ServletActionContext.getRequest().getSession().getAttribute("patientObjSession") != null) {
            Patient patientObjSession = (Patient) ServletActionContext.getRequest().getSession().getAttribute("patientObjSession");                        
            
            //On the patient registration page, the status at registration is set to the current status when creation or modifying a record
            //For record modifiction if a patient has had a change in status since after registration, then if the change in status is losses revert current status to a loss. 
            //However if the status at registration is Pre-ART Transfer In and current status is either 'HIV+ non ART' or 'HIV exposed status unknown' keep the current status as status at registration
            //If the status ate registration is ART Transfer In and current status is either 'HIV+ non ART' or 'HIV exposed status unknown' or 'ART Start' keep the current status as status at registration
            if(patient.getCurrentStatus().equalsIgnoreCase("Pre-ART Transfer In") && (patientObjSession.getCurrentStatus().equalsIgnoreCase("HIV+ non ART") || patientObjSession.getCurrentStatus().equalsIgnoreCase("HIV exposed status unknown"))) {
            }
            else {
                if(patient.getCurrentStatus().equalsIgnoreCase("ART Transfer In") && (patientObjSession.getCurrentStatus().equalsIgnoreCase("HIV+ non ART") || patientObjSession.getCurrentStatus().equalsIgnoreCase("HIV exposed status unknown") || patientObjSession.getCurrentStatus().equalsIgnoreCase("ART Start"))) {
                }
                else {
                    patient.setCurrentStatus(patientObjSession.getCurrentStatus());
                    patient.setDateCurrentStatus(patientObjSession.getDateCurrentStatus());                
                }                
            }
            patient.setEnrollmentSetting(patientObjSession.getEnrollmentSetting());
            patient.setDateStarted(patientObjSession.getDateStarted());
            patient.setRegimenType(patientObjSession.getRegimenType());
            patient.setRegimen(patientObjSession.getRegimen());
            patient.setLastClinicStage(patientObjSession.getLastClinicStage());
            patient.setLastViralLoad(patientObjSession.getLastViralLoad());
            patient.setLastCd4(patientObjSession.getLastCd4());
            patient.setLastCd4p(patientObjSession.getLastCd4p());
            patient.setDateLastCd4(patientObjSession.getDateLastCd4());
            patient.setDateLastViralLoad(patientObjSession.getDateLastViralLoad());
            patient.setViralLoadDueDate(patientObjSession.getViralLoadDueDate());
            patient.setViralLoadType(patientObjSession.getViralLoadType());
            patient.setDateLastRefill(patientObjSession.getDateLastRefill());
            patient.setLastRefillDuration(patientObjSession.getLastRefillDuration());
            patient.setLastRefillSetting(patientObjSession.getLastRefillSetting());
            patient.setDateNextRefill(patientObjSession.getDateNextRefill());
            patient.setDateLastClinic(patientObjSession.getDateLastClinic());
            patient.setDateNextClinic(patientObjSession.getDateNextClinic());
            patient.setDateTracked(patientObjSession.getDateTracked());
            patient.setOutcome(patientObjSession.getOutcome());
            patient.setAgreedDate(patientObjSession.getAgreedDate());
            patient.setSendMessage(patientObjSession.getSendMessage());
        }
        return patient;
    }
    */
}
