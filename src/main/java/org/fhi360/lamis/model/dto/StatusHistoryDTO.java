package org.fhi360.lamis.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StatusHistoryDTO {
    private Long patientId;
    private Long historyId;
    private Long facilityId;
    private String deletable;
    private String currentStatus;
    private String dateCurrentStatus;
    private String outcome;
    private String agreedDate;
    private String reasonInterrupt;
    private String dateTracked;
    private String causeDeath;
}
