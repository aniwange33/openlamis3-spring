package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.DHISDataConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping(value = "/export/dHISData")
@Api(tags = "DHISData", description = " ")
public class DHISDataController {
    private final DHISDataConverter dhisDataConverter;

    public DHISDataController(DHISDataConverter dhisDataConverter) {
        this.dhisDataConverter = dhisDataConverter;
    }

    @GetMapping(value = "/convertExcel")
    public void convertExcel(@RequestParam("period") String period, @RequestParam("stateId") String stateId,
                             @RequestParam("id") String facilityId, HttpSession session,
                             HttpServletResponse response) throws IOException {
        Long userId = (Long) session.getAttribute("id");
        response.addHeader("Content-disposition", "attachment;filename=dhis_daily_report.xlsx");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = dhisDataConverter.convertExcel(period, facilityId, stateId, userId);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }
}
