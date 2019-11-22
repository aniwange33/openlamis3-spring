package org.fhi360.lamis.model.dto;

import lombok.Data;

@Data
public class LabTestListDto {
    private Long labtestId;
    private Long labtestCategoryId;
    private String description;
    private String selectedLabtest;
}
