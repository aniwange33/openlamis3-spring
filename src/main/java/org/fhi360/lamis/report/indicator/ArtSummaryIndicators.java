/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.report.indicator;

/**
 *
 * @author user1
 */
public class ArtSummaryIndicators {
   public String[] initialize() {
        String [] indicator = new String[18];
        indicator[0] = "Number of patients newly enrolled for Pre-ART care during the reporting month (exclude Pre-ART Transfer In)";                
        indicator[1] = "Number of patients cummulatively enrolled into HIV care (exclude Pre-ART Transfer In)";
        indicator[2] = "Number of Pre-ART patients transfered in from another ART service point during the reporting month";
        indicator[3] = "Number of Pre-ART patients transfered out from another ART service point during the reporting month";
        indicator[4] = "Number of Pre-ART patients lost to follow up during the reporting month";
        indicator[5] = "Number of Pre-ART patients known to have died during the reporting month";
        indicator[6] = "Number of patients newly started on ART during the reporting month";
        indicator[7] = "Number of patients currently on 1st line ARV during the reporting month";
        indicator[8] = "Number of patients currently on 2nd line ARV during the reporting month";
        indicator[9] = "Number of patients currently on Salvage ARV during the reporting month";
        indicator[10] = "Number of ART patients transfered into the ART programme from other facilities during the reporting month";
        indicator[11] = "Number of ART patients who stopped ART during the reporting month";
        indicator[12] = "Number of ART patients lost to follow up during the reporting the month";
        indicator[13] = "Number of ART patients known to have died during the reporting the month";
        indicator[14] = "Number of ART patients transfered out to another ART service point during the reporting month";
        indicator[15] = "Number of ART patients who restarted ART during the reporting the month";
        indicator[16] = "Number of HIV infected pregnant women newly enrolled for Pre-ART care during the reporting month";
        indicator[17] = "Number of HIV infected pregnant women newly initiated on ART for their own health during the reporting month";
        return indicator;
   }
    
}
