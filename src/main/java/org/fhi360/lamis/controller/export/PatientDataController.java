package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.PatientDataConverter;
import org.fhi360.lamis.model.dto.QueryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping(value = "/export/patientData")
@Api(tags = "PatientData", description = " ")
public class PatientDataController {
    private final PatientDataConverter patientDataConverter;

    public PatientDataController(PatientDataConverter patientDataConverter) {
        this.patientDataConverter = patientDataConverter;
    }

    @GetMapping(value = "/convertExcel")
    public void convertExcel(@RequestBody QueryDto queryDto,
                                          @RequestParam("entity") String entity,
                                          @RequestParam("casemanagerId") Long casemanagerId,
                                          @RequestParam("state") String state,
                                          @RequestParam("facilityIds") String facilityIds,
                                          @RequestParam("option") String option,
                                          @RequestParam("aspect") Integer aspect,
                                          HttpSession session, HttpServletResponse response) throws IOException {
        Long userId = (Long) session.getAttribute("id");
        response.addHeader("Content-disposition", "attachment;filename=patient_encounter_summary.xlsx");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = patientDataConverter
                .convertExcel(queryDto, entity, casemanagerId, state, true, facilityIds,
                        option, aspect, true, userId);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }
}
