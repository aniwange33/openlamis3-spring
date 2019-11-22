package org.fhi360.lamis.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChildFollowupGridDTO extends GridDTO {
    private List<ChildFollowupDTO> childfollowupList;
}
