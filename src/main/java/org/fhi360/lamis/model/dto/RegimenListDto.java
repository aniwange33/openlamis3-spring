package org.fhi360.lamis.model.dto;

import lombok.Data;

@Data
public class RegimenListDto {
    private long regimenTypeId;
    private long regimenId;
    private String description;
    private String selectedRegimen;
}
