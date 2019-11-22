/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.utility;

import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.converter.ClinicDataConverter;
import org.fhi360.lamis.converter.LabDataConverter;
import org.fhi360.lamis.converter.PatientDataConverter;
import org.fhi360.lamis.converter.PharmacyDataConverter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BRAINERGY SOLUTIONS
 */
public class ConversionUtil {
    private final static JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);
    private String status;
    private String fileName;

    public void convert(long stateId, int recordType) {
        /*int aspect = recordType - 1;
        StringBuilder facilityIds;
        List<Long> facIds = FacilityJDBC.getFacilitiesInState(stateId);
        if (facIds.size() > 0) {
            facilityIds = new StringBuilder(Long.toString(facIds.get(0)));
            for (int i = 1; i < facIds.size(); i++) {
                facilityIds.append(",").append(facIds.get(i));
            }
            switch (recordType) {
                case 1:
                    setFileName(new PatientDataConverter().convertExcel(facilityIds.toString(), "cron", aspect, false));
                    break;
                case 2:
                    setFileName(new ClinicDataConverter().convertExcel(facilityIds.toString(), "cron", aspect, false));
                    break;
                case 3:
                    setFileName(new PharmacyDataConverter().convertExcel(facilityIds.toString(), "cron", aspect, false));
                    break;
                case 4:
                    setFileName(new LabDataConverter().convertExcel(facilityIds.toString(), "cron", aspect, false));
                *//*case 1: setFileName(new PatientDataConverter().convertExcel(facilityIds, "cron", aspect, false));
                        break;
                case 2: setFileName(new ClinicDataConverter().convertExcel(facilityIds, "cron", aspect, false)); 
                        break;
                case 3: setFileName(new PharmacyDataConverter().convertExcel(facilityIds, "cron", aspect, false));  
                        break;
                case 4: setFileName(new LabDataConverter().convertExcel(facilityIds, "cron", aspect, false));
                        break;*//*
                    //            case 5: setFileName(new PatientStatusSummaryConverter().convertExcel(facilityIds, stateName, "cron", aspect));
                    //                    break;
                    //            case 6: setFileName(new SyncAuditConverter().convertExcel(facilityIds, stateName, "cron", aspect));
                    //                    break;
                    //            case 7: setFileName(new ChroniccareDataConverter().convertExcel(facilityIds, stateName, "cron", aspect));
                    //                    break;
                    //            case 8: setFileName(new EidDataConverter().convertExcel(facilityIds, stateName, "cron", aspect));
                    //                    break;
                    //            case 9: setFileName(new ViralLoadSummaryConverter().convertExcel(facilityIds, stateName, "cron", aspect));
                    //                    break;
                    //            case 10: setFileName(new PatientOutcomeConverter().convertExcel(facilityIds, stateName, "cron", aspect));
                    //                    break;
                    //            case 11: setFileName(new SummaryFormConverter().convertExcel(facilityIds, stateName, "cron", aspect));
                    //                    break;
                    //            case 12: setFileName(new PatientEncounterSummaryConverter().convertExcel(facilityIds, stateName, "cron", aspect));
                    //                    break;
                    //            case 13: setFileName(new TreatmentTrackerConverter().convertExcel(facilityIds, stateName, "cron", aspect));
                    //                    break;
                    //            case 14: setFileName(new RetentionTrackerConverter().convertExcel(facilityIds, stateName, "cron", aspect));
                    //                    break;
                    //            case 15: setFileName(new TreatmentSummaryConverter().convertExcel(facilityIds, stateName, "cron", aspect));
                    //                    break;
                default:
                    break;
            }
        }*/
       // facilityIds = new StringBuilder(Long.toString(facIds.get(0)));
    }

    public static Map getFacility(long facilityId) {
        Map<String, Object> facilityMap = new HashMap<>();

        String query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, " +
                "facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility " +
                "JOIN lga ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = " +
                "state.state_id WHERE facility_id = " + facilityId;

        jdbcTemplate.query(query, rs -> {
            facilityMap.put("facilityName", rs.getString("name"));
            facilityMap.put("lga", rs.getString("lga"));
            facilityMap.put("state", rs.getString("state"));
        });
        return facilityMap;
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

    public static int getCount(String query) {
        List<Long> counts = jdbcTemplate.queryForList(query, Long.class);
        return !counts.isEmpty() && counts.get(0) != null ? counts.get(0).intValue() : 0;
    }
}
