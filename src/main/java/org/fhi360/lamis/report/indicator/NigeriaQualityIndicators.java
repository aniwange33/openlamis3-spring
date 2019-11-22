/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.report.indicator;

/**
 *
 * @author user1
 */
public class NigeriaQualityIndicators {
   public String[] initialize() {
        String[] indicator = new String[8];
        indicator[0] = "Percentage of HIV positive pregnant women who received a complete course of ARV prophylaxis for PMTCT";
        indicator[1] = "Percentage of infants born to HIV positive women who received a complete course of ARVs for PMTCT";
        indicator[2] = "Percentage of HIV exposed children aged 6-24 weeks who had DBS sample collected for DNA PCR test at 6-8 weeks of age in the review period";
        indicator[3] = "Percentage of HIV exposed children aged 6-8 weeks in the first 3 months of review period who received their DNA PCR results by 12 weeks of age";
        indicator[4] = "Percentage of exposed infants who had PMTCT intervention and confirmed HIV Negative at 18 months";
        indicator[5] = "Percentage of HIV exposed children 6 weeks 18 months with at least one clinical visit in the last 3 months";
        indicator[6] = "Percentage of newly diagnosed HIV positive pregnant women who initiated ARV within 1 month of HIV diagnosis";
        indicator[7] = "Percentage of HIV positive pregnant women on ARV with an EDD within the review period who delivered in the health facility";
        return indicator;
    }

    
}
