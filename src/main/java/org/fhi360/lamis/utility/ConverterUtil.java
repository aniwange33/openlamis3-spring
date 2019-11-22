/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.exchange.ndr.NdrConverter;

/**
 * @author user10
 */
public class ConverterUtil {

    public static void performNdrConversion(String stateId, String facilityIds, boolean hasRequest) {
        NdrConverter converter = new NdrConverter();
        if (hasRequest) {
            if (facilityIds.trim().isEmpty() && stateId.trim().isEmpty()) {
                //Extract all facilities
                String query = "SELECT DISTINCT facility_id FROM patient WHERE hospital_num != '' " +
                        "AND hospital_num IS NOT NULL AND facility_id IN (SELECT facility_id FROM facility WHERE active = 1 " +
                        "AND datim_id IS NOT NULL AND datim_id != '') ORDER BY facility_id";
                converter.buildMessage(query, true, hasRequest);
            } else {
                //Extract for selected facilities
                if (!StringUtils.isEmpty(facilityIds)) {
                    String query = "SELECT DISTINCT facility_id FROM patient WHERE hospital_num != '' AND hospital_num " +
                            "IS NOT NULL AND facility_id IN (SELECT facility_id FROM facility WHERE " +
                            "facility_id IN (" + facilityIds + ") AND active = 1 AND datim_id IS NOT NULL AND datim_id != '') " +
                            "ORDER BY facility_id";
                    //                System.out.println(query);
                    converter.buildMessage(query, false, hasRequest);
                } else {
                    //Extract for selected State
                    if (!StringUtils.isEmpty(stateId)) {
                        String query = "SELECT DISTINCT facility_id FROM patient WHERE hospital_num != '' " +
                                "AND hospital_num IS NOT NULL AND facility_id IN (SELECT facility_id FROM " +
                                "facility WHERE state_id = " + stateId + " AND active = 1 AND datim_id IS NOT " +
                                "NULL AND datim_id != '') ORDER BY facility_id";
                        converter.buildMessage(query, false, hasRequest);
                    }
                }
            }
        } else {
            String query = "SELECT DISTINCT facility_id FROM patient WHERE hospital_num != '' " +
                    "AND hospital_num IS NOT NULL AND facility_id IN (SELECT facility_id FROM facility " +
                    "WHERE active = 1 AND datim_id IS NOT NULL AND datim_id != '') ORDER BY facility_id";
//                System.out.println(query);
            converter.buildMessage(query, false, hasRequest);
        }
    }

    //@PostConstruct
    public void start() {
        System.out.println("Starting NDR Coversion....");
        performNdrConversion("", "", false);
    }
}
