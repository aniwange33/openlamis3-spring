/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.dhis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author user1
 */
@Component
@Setter
@Getter
public class DhisConverterDispatchAction {
    private final ArtSummaryConverter artSummaryConverter;
    private final LabSummaryConverter labSummaryConverter;

    public DhisConverterDispatchAction(ArtSummaryConverter artSummaryConverter, LabSummaryConverter labSummaryConverter) {
        this.artSummaryConverter = artSummaryConverter;
        this.labSummaryConverter = labSummaryConverter;
    }

    @GetMapping
    public String dispatcher(@RequestParam Integer recordType, @RequestParam String reportingMonth,
                             @RequestParam String reportingYear, @RequestParam String facilityIds) {
        String fileName = "";
        switch (recordType) {
            case 1:
                fileName = artSummaryConverter.convertXml(reportingMonth, reportingYear, facilityIds);
                break;
            case 2:
                fileName = labSummaryConverter.convertXml(reportingMonth, reportingYear, facilityIds);
                break;
            default:
                break;
        }
        return fileName;
    }

}
