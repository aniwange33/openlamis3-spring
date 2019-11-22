package org.fhi360.lamis.model.dto;

import lombok.Data;

@Data
public class LabDto {
    private long labtestId;
    private String description;
    private String resultab;
    private String measureab;
    private String resultpc;
    private String measurepc;
    private String comment;
}
