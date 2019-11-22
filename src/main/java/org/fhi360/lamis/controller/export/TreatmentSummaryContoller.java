package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.TreatmentSummaryConverter;
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
@RequestMapping(value = "/export/TtreatmentSummary")
@Api(tags = "TreatmentSummary", description = " ")
public class TreatmentSummaryContoller {
    private final TreatmentSummaryConverter treatmentSummaryConverter;

    public TreatmentSummaryContoller(TreatmentSummaryConverter treatmentSummaryConverter) {
        this.treatmentSummaryConverter = treatmentSummaryConverter;
    }

    @GetMapping(value = "/convertExcel")
    public void convertExcel(@RequestParam("id") String facilityIds,
                             @RequestParam("state1") String state1,
                             HttpSession session, HttpServletResponse response) throws IOException {
        Long userId = (Long) session.getAttribute("id");
        response.addHeader("Content-disposition", "attachment;filename=treatment_summary.xlsx");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = treatmentSummaryConverter.convertExcel(facilityIds, state1);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }


}
