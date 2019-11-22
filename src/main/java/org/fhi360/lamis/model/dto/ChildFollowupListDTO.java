package org.fhi360.lamis.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChildFollowupListDTO extends ListDTO {
    private List<ChildFollowupDTO> childfollowupList;
}
