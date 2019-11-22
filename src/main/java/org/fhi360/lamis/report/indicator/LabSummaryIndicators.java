/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.report.indicator;

/**
 *
 * @author user1
 */
public class LabSummaryIndicators {
   public String[] initialize() {
        String [] indicator = new String[19];
        indicator[0] = "CD4 count: age 5 years or older (< 200)";                
        indicator[1] = "CD4 count: age 5 years or older (200-300)";
        indicator[2] = "CD4 count: age 5 years or older (> 300)";
        indicator[3] = "CD4 percent: age < 5 year (< 15%)";
        indicator[4] = "CD4 percent: age < 5 year (15% +)";
        indicator[5] = "FBC";
        indicator[6] = "GOT";
        indicator[7] = "GPT";
        indicator[8] = "Creatine";
        indicator[9] = "K+";
        indicator[10] = "Glucose";
        indicator[11] = "VDRL/RPR in HIV+ Pregnant women (negative)";
        indicator[12] = "VDRL/RPR in HIV+ Pregnant women (positive)";
        indicator[13] = "Pregnancy";
        indicator[14] = "HBsAg";
        indicator[15] = "HCV";
        indicator[16] = "Malaria Parasite";
        indicator[17] = "Stool microscopy";
        indicator[18] = "Sputum Smear";
        return indicator;
    }
    
}
