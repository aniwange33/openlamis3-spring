/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.report.indicator;

/**
 *
 * @author user10
 */
public class CommunitypharmSummaryIndicators {
   public String[] initialize() {
        String [] indicator = new String[22];
        indicator[0] = "Number of ART clients devolved to the Community Pharmacy to receive ART refills (CUMMULATIVE)";                
        indicator[1] = "Number of ART clients devolved to the Community Pharmacy to receive ART refills this reporting month";
        indicator[2] = "Number of devolved ART clients who were scheduled to access ART refills in community pharmacy this reporting month";
        indicator[3] = "Number of  devolved ART clients who accessed ART refills in community pharmacy this reporting month";
        indicator[4] = "Number of devolved ART clients who defaulted to come for a refill within 7 days of the appointment date at the Community Pharmacy this reporting month";
        indicator[5] = "Number of devolved ART clients who defaulted to come for a refill, were tracked and returned to pick up ART refill at the Community Pharmacy this reporting";
        indicator[6] = "Number of devolved ART clients who received refills for Cotrimoxazole prophylaxis (CTX) this reporting month";
        indicator[7] = "Number of devolved ART clients who received refill for Isoniazid prophylaxis (INH) this reporting month";
        indicator[8] = "Number of devolved ART clients who were provided Chronic Care Screening services using care and support checklist this reporting month";
        indicator[9] = "Number of devolved ART clients who had medication adherence issue(s) this reporting month";
        indicator[10] = "Number of devolved ART clients with suspected Adverse Drug Reactions (ADRs) this reporting month";
        indicator[11] = "Number of individual case safety reports form [ADR reports] filled for devolved clients this reporting month";
        indicator[12] = "Number of devolved ART clients who are eligible to return back to Hospital for semiannual clinical and laboratory reassements this reporting month";
        indicator[13] = "Number of devolved ART clients who are eligible for viral load re-assessment this reporting month";
        indicator[14] = "Number of devolved ART clients who had clinical or/& laboratory re-assessments done at the hospital with new ART prescriptions for Community ART refills this reporting month";
        indicator[15] = "Number of devolved ART clients who accessed viral load re-assessment with viral load test result this reporting month";
        indicator[16] = "Number of devolved ART clients who had viral load re-assessment and are virologically suppressed (VL <1000 c/ml) this reporting month";
        indicator[17] = "Number of devolved ART clients who discontinued Community ART refill services and returned back to the Hospital to continue ART refill services this reporting";
        indicator[18] = "Number of Clients Tested for HIV, Counselled and Received results this reporting month";
        indicator[19] = "Number of clients who tested positive for HIV this reporting month";
        indicator[20] = "Number of HIV positive clients who were referred for ART and related services this reporting month";
        indicator[21] = "Number of Community-based external onsite monitoring visit(s) conducted this reporting period using standard check list";
        return indicator;
   }
    
}
