package org.fhi360.lamis.controller.report;

import lombok.Data;

@Data
public class ReportParameterDTO {
    private String dateCurrentStatusBegin;
    private String dateCurrentStatusEnd;
    private String gender;
    private String ageBegin;
    private String ageEnd;
    private String state;
    private String lga;
    private String entryPoint;
    private String currentStatus;
    private String regimentype;
    private String dateRegistrationBegin;
    private String dateRegistrationEnd;
    private String artStartDateBegin;
    private String artStartDateEnd;
    private String clinicStage;
    private String cd4Begin;
    private String cd4End;
    private String viralloadBegin;
    private String viralloadEnd;
    private String reportingMonth;
    private String reportingYear;
    private String reportingMonthBegin;
    private String reportingMonthEnd;
    private String reportingYearBegin;
    private String reportingYearEnd;
    private String reportingDateBegin;
    private String reportingDateEnd;
    private String reportingDate;
    private String reportType;
    private Integer entity;
}
