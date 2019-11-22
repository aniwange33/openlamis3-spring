/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model.dto;

import org.fhi360.lamis.model.Specimen;

/**
 *
 * @author user1
 */
public class SpecimenObjHandler {
    /*public static void store(Specimen specimen) {
        ServletActionContext.getRequest().getSession().setAttribute("specimenObjSession", specimen); 
    }
    
    public static Specimen restore(Specimen specimen) {
        if(ServletActionContext.getRequest().getSession().getAttribute("specimenObjSession") != null) {
            Specimen specimenObjSession = (Specimen) ServletActionContext.getRequest().getSession().getAttribute("specimenObjSession");                        
            specimen.setDateAssay(specimenObjSession.getDateAssay());
            specimen.setDateReported(specimenObjSession.getDateReported());
            specimen.setDateDispatched(specimenObjSession.getDateDispatched());
            specimen.setResult(specimenObjSession.getResult());
        }
        return specimen;
    }    */
}
