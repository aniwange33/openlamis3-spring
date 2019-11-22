package org.fhi360.lamis.model.dto;

import lombok.Data;

@Data
public class PatientDemographyDTO {
    String portalId;
    String reviewPeriodId;
    Boolean viewIdentifier = Boolean.TRUE;
    String thermaticArea;
}
