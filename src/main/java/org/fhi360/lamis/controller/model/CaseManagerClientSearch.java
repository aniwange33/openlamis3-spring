package org.fhi360.lamis.controller.model;

import lombok.Data;

@Data
public class CaseManagerClientSearch {
    private int start = 0;
    private int rows = 100;
    private String gender;
    private String ageGroup;
    private String state;
    private String lga;
    private String pregnancyStatus;
    private String categoryId;
    private boolean showAssigned;
}
