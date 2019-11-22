package org.fhi360.lamis.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChildListDTO extends ListDTO {
    private List<ChildDTO> childList;
}
