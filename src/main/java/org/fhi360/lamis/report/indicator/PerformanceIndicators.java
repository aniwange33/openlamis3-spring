/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.report.indicator;

/**
 *
 * @author user1
 */
public class PerformanceIndicators {
   public String[] initialize() {
        String [] indicator = new String[23];
        indicator[0] = "Proportion of clinic visits during the reporting period that had a documentation of TB status";                
        indicator[1] = "Proportion of clinic visits during the reporting period that had a documentation of functional status";
        indicator[2] = "Proportion of clinic visits during the reporting period that had a documentation of weight";
        indicator[3] = "Proportion of clinic visits during the of the reporting month that had documentation of OI status in ART patients";
        indicator[4] = "Proportion of clinic visits during the of the reporting month that had documentation of OI status in non-ART patients";
        indicator[5] = "Proportion of clinic visits of current ART patients within the reporting period that had a documentation of ADR status";
        indicator[6] = "Proportion of pharmacy visits of current ART patients within the reporting period that had a documentation of ADR status";

        indicator[7] = "Proportion of current ART patients who had at least one documented clinical visit in the last 6 months";        
        indicator[8] = "Proportion of current ART patients with at least one CD4 count test done in the last 6 months";
        indicator[9] = "Proportion of current ART patients at least at one time a minimum set of standard haematology tests done in the last 6 months";
        indicator[10] = "Proportion of current ART patients with at least at one time a minimum set of standard chemistry tests done in the last 6 months";
        indicator[11] = "Proportion of current ART patients who had a clinical staging done at the last clinical visit prior to the reporting date";
        indicator[12] = "Proportion of current ART patients reporting an ADR with severity grade 3 or 4 in the reporting month";
        indicator[13] = "Proportion of current ART patients who picked up their drugs within 7 days of the appointment day within the last 3 months";
        indicator[14] = "Proportion of current ART patients who have not had a refill 3 months after the last refill who have had their status correctly updated";

        indicator[15] = "Proportion of patients newly initiated on ART <= 5 years old with documented eligibility criteria";
        indicator[16] = "Proportion of patients newly initiated on ART > 5 years old with documented eligibility criteria";
        indicator[17] = "Proportion of patients newly initiated on ART who had at least one clinical staging done prior to ART commencement";
        indicator[18] = "Proportion of patients newly initiated on ART with at least one CD4 count test done before ART commencement";
        indicator[19] = "Proportion of patients newly initiated on ART with at least at one time a minimum set of standard chemistry tests done before ART commencement";
        indicator[20] = "Proportion of patients newly initiated on ART with at least at one time a minimum set of standard haematology tests done before ART commencement";
        
        indicator[21] = "Proportion of HIV positive patients <= 5 years initiating cotrimoxazole prophylaxis in the last 6 months";
        indicator[22] = "Proportion of HIV positive patients > 5 years initiating cotrimoxazole prophylaxis in the last 6 months";
        
        return indicator;
    }
    
}
