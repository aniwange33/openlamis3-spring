package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.LabDataConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping(value = "/export/labData")
@Api(tags = "LabData", description = " ")
public class LabDataController {

    private final LabDataConverter labDataConverter;

    public LabDataController(LabDataConverter labDataConverter) {
        this.labDataConverter = labDataConverter;
    }

    @GetMapping(value = "/convertExcel")
    public void convertExcel(@RequestParam("labtestId") long labtestId,
                             @RequestParam("state") String state,
                             @RequestParam("facilityIds") String facilityIds,
                             @RequestParam("option") String option,
                             @RequestParam("aspect") Integer aspect,
                             HttpSession session, HttpServletResponse response) throws IOException {
        Long user = (Long) session.getAttribute("id");
        response.addHeader("Content-disposition", "attachment;filename=patient_encounter_summary.xlsx");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = labDataConverter
                .convertExcel(labtestId, user, state, facilityIds, option, aspect, true);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }
}
