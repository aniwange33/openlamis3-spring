package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.TreatmentTrackerConverter;
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
@RequestMapping(value = "/export/treatmentTracker")
@Api(tags = "TreatmentTracker", description = " ")
public class TreatmentTrackerController {
    private final TreatmentTrackerConverter treatmentTrackerConverter;

    public TreatmentTrackerController(TreatmentTrackerConverter treatmentTrackerConverter) {
        this.treatmentTrackerConverter = treatmentTrackerConverter;
    }

    @GetMapping(value = "/convertExcel")
    public void convertExcel(@RequestParam("id") String facilityIds,
                             @RequestParam("state") String state,
                             @RequestParam("reportingDateBegin") String reportingDateBegin,
                             @RequestParam("reportingDateEnd") String reportingDateEnd,
                             HttpSession session, HttpServletResponse response) throws IOException {
        Long userId = (Long) session.getAttribute("id");
        response.addHeader("Content-disposition", "attachment;filename=treatment_tracker.xlsx");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = treatmentTrackerConverter
                .convertExcel(userId, facilityIds, state, reportingDateBegin, reportingDateEnd);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }
}
