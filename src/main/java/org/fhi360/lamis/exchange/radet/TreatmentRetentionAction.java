/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.radet;


import org.springframework.web.bind.annotation.RequestParam;

public class TreatmentRetentionAction {
    private String status;
    private String fileName;
         
    public String dispatcher(@RequestParam String cohortMonthBegin1,
                             @RequestParam String cohortYearBegin1,
                             @RequestParam String cohortMonthEnd1,
                             @RequestParam String cohortYearEnd1) {
        setFileName(new TreatmentRetentionConverter().convertExcel(  cohortMonthBegin1,
                 cohortYearBegin1,
                  cohortMonthEnd1,
                  cohortYearEnd1));
        return "SUCCESS";
    }
        

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
}
