package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.PatientStatusSummaryConverter1;
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
@RequestMapping(value = "/export/patientStatusSummary")
@Api(tags = "PatientStatusSummary", description = " ")
public class PatientStatusSummaryController {
    private  final PatientStatusSummaryConverter1 patientStatusSummaryConverter1;

    public PatientStatusSummaryController(PatientStatusSummaryConverter1 patientStatusSummaryConverter1) {
        this.patientStatusSummaryConverter1 = patientStatusSummaryConverter1;
    }


//Long id,String facilityIds, String state1
    @GetMapping(value = "/convertExcel")
    public void convertExcel(@RequestParam("id") String facilityId,
                                          @RequestParam("state") String state,
                                          HttpSession session, HttpServletResponse response) throws IOException {
        Long userId = (Long) session.getAttribute("id");
        response.addHeader("Content-disposition", "attachment;filename=patient_encounter_summary.xlsx");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = patientStatusSummaryConverter1
                .convertExcel(userId, facilityId, state);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }
}
