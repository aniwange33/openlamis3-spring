package org.fhi360.lamis.model.dto;

import lombok.Data;

@Data
public class LabTestDTO {
private Long labtestId;
private String description;
private String resultab;
private String measureab;
private String resultpc;
private String measurepc;
private String comment;
private String indication;


}
