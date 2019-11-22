package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.ClinicDataConverter;
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
@RequestMapping(value = "/export/clinicData")
@Api(tags = "clinicData", description = " ")
public class ClinicDataController {
    private final ClinicDataConverter clinicDataConverter;

    public ClinicDataController(ClinicDataConverter clinicDataConverter) {
        this.clinicDataConverter = clinicDataConverter;
    }

    @GetMapping(value = "/convertExcel")
    public void convertExcel(@RequestParam("facilityIds") String facilityIds,
                                          @RequestParam("state") String state,
                                          @RequestParam("option") String option,
                                          @RequestParam("aspect") Integer aspect,
                                          HttpSession session, HttpServletResponse response) throws IOException {
        Long user = (Long) session.getAttribute("id");
        response.addHeader("Content-disposition", "attachment;filename=clinic_data.xlsx");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = clinicDataConverter.convertExcel(facilityIds, state, user, option, aspect);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }
}
