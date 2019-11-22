package org.fhi360.lamis.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class LaboratoryDTO {
    private Long laboratoryId;
    private Long patientId;
    private Long facilityId;
    private String hospitalNum;
    private Double dateLastCd4;
    private String dateCollected;
    private String dateReported;
    private String labno;
    private Long labtestId;
    private String description;
    private String resultab;
    private String resultpc;
    List<LabTestDTO> labtests;
}
