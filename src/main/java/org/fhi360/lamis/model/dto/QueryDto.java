package org.fhi360.lamis.model.dto;

import lombok.Data;

@Data
public class QueryDto {
    String gender ;
    String ageBegin;
    String ageEnd;
    String lga;
    String currentStatus;
    String dateCurrentStatusBegin;
    String dateCurrentStatusEnd;
    String regimentype;
    String dateRegistrationBegin;
    String dateRegistrationEnd;
    String artStartDateBegin;
     String artStartDateEnd;
    String clinicStage;
    String cd4Begin;
    String cd4End;
    String viralloadBegin;
    String viralloadEnd;
}
