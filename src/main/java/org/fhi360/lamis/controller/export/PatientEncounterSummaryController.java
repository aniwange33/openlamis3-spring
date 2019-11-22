package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.PatientEncounterSummaryConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping(value = "/export/patientEncounterSummary")
@Api(tags = " PatientEncounterSummary", description = " ")
public class PatientEncounterSummaryController {
    private final PatientEncounterSummaryConverter patientEncounterSummaryConverter;

    //String facilityIds, String state, long id
    public PatientEncounterSummaryController(PatientEncounterSummaryConverter patientEncounterSummaryConverter) {
        this.patientEncounterSummaryConverter = patientEncounterSummaryConverter;
    }

    @GetMapping(value = "/convertExcel")
    public void convertExcel(@RequestParam("stateId") String state,
                             @RequestParam("id") String facilityId,
                             HttpSession session, HttpServletResponse response) throws IOException {
        Long userId = (Long) session.getAttribute("id");

        response.addHeader("Content-disposition", "attachment;filename=patient_encounter_summary.xlsx");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = patientEncounterSummaryConverter.convertExcel(facilityId, state, userId);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }
}
