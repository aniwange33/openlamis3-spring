/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.report.indicator;

/**
 *
 * @author user1
 */
public class ServiceIndicators {
   public String[] initialize(){
        String[] indicator = new String[12];
        indicator[0] = "Number of eligible adults and children provided with a minimum of one care service";                
        indicator[1] = "Number of HIV positive adults and children receiving a minimum of one clinical service";
        indicator[2] = "Number of HIV positive patients receiving cotrimoxazole prophylaxis";
        indicator[3] = "Number of HIV positive patients who were screened for TB in HIV care and treatment setting";
        indicator[4] = "Number of HIV positive patients (pre-ART or ART) who started TB treatment";
        indicator[5] = "Number of adults and children with advanced HIV infection newly enrolled on ART";
        indicator[6] = "Number of adults and children with advanced HIV infection receiving ART (current)";
        indicator[7] = "Number of patients on ART who were lost to follow up during the reporting period";
        indicator[8] = "Number of patients who were on ART that died during the reporting period";
        indicator[9] = "Number of HIV positive patients (pre-ART or ART) who defaulted for over 7 days of scheduled clinic appointment";
        indicator[10] = "Number of current ART patients with documentation of ADR status during the reporting period";
        indicator[11] = "Number of patients reporting an ADR (with severity grade 3 or 4) during the reporting period";   
        return indicator;
    }
    
}
