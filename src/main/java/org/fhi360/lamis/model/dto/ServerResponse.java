package org.fhi360.lamis.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class ServerResponse {
    private List<ClinicDTO> clinic;
    private PatientDTO patient;
}
