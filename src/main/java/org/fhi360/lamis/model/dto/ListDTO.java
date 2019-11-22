package org.fhi360.lamis.model.dto;

import lombok.Data;

@Data
public class ListDTO {
    private Integer currpage = 1;
    private Long totalrecords = 1L;
    private Long totalpages = 1L;
}
