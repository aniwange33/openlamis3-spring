/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.report.indicator;

/**
 *
 * @author user1
 */
public class QualityIndicators {
   public String[] initialize(){
        String [] indicator = new String[12];
        indicator[0] = "Percentage of eligible patients placed on ART within the review period";                
        indicator[1] = "Percentage of Pre-ART patients whose first visit within the review period is less than or equal to 6 months after their last visit";
        indicator[2] = "Percentage of ART patients who had at least one documented adherence assessment during the last 3 months";
        indicator[3] = "Percentage of HIV patients with at least one clinic visit in the review period who were clinically screened for TB";
        indicator[4] = "Percentage of HIV patients with at least one clinic visit within the last 6 months who have a CD4 test result";
        indicator[5] = "Percentage of ART patients with at least one clinic visit within the last 6 months that have all the relevant laboratory tests conducted";
        indicator[6] = "Percentage of HIV patients with at least one CD4 count <350 cells or confirmed TB in the past 6 months that received CPT";

        indicator[7] = "Percentage of HIV infected children 0-23 months commenced on ART in the past 6 months";        
        indicator[8] = "Percentage of eligible HIV infected children 24 and 59 months commenced on ART in the past 6 months";
        indicator[9] = "Percentage of eligible HIV infected children 5 to < 15 years commenced on ART in the past 6 months";
        indicator[10] = "Percentage of HIV infected children < 15 years on ART with a visit in the last 3 months who have had at least one adherence assessment";
        indicator[11] = "Percentage of HIV infected children < 15 years who have had at least one clinical visit in the last 3 months";
        
        return indicator;
    }
    
}
