package org.fhi360.lamis.model.dto;

import lombok.Data;

@Data
public class LabReportDTO {
    private String description;
    private String labtestId;
    private String reportingMonth;
    private String reportingYear;
    private String reportingMonthBegin;
    private String reportingMonthEnd;
    private String reportingYearBegin;
    private String reportingYearEnd;
    private String reportingDateBegin;
    private String reportingDateEnd;
    private String reportingDate;

}
