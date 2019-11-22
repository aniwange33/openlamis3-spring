package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.EidDataConverter;
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
@RequestMapping(value = "/export/eidData")
@Api(tags = "EidData", description = " ")
public class EidDataController {

    private final EidDataConverter eidDataConverter;

    public EidDataController(EidDataConverter eidDataConverter) {
        this.eidDataConverter = eidDataConverter;
    }

    @GetMapping(value = "/convertExcel")
    public void convertExcel(@RequestParam("stateId") String stateId,
                                          @RequestParam("id") String facilityId,
                                          HttpSession session, HttpServletResponse response) throws IOException {
        Long userId = (Long) session.getAttribute("id");
        response.addHeader("Content-disposition", "attachment;filename=eid.xlsx");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = eidDataConverter.convertExcel(facilityId, stateId, userId);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }
}
