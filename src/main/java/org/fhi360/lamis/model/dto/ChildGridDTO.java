package org.fhi360.lamis.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChildGridDTO extends GridDTO {
    private List<ChildDTO> childList;
}
