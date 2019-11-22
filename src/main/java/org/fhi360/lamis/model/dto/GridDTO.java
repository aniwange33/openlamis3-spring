package org.fhi360.lamis.model.dto;

import lombok.Data;

@Data
public class GridDTO {
    private Integer currpage;
    private Long totalrecords;
    private Long totalpages;
}
