package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.PedeatricsDataConverter;
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
@RequestMapping(value = "/export/pediatricsData")
@Api(tags = "PediatricsData", description = " ")
public class PediatricsDataController {
    private final PedeatricsDataConverter pedeatricsDataConverter;

    public PediatricsDataController(PedeatricsDataConverter pedeatricsDataConverter) {
        this.pedeatricsDataConverter = pedeatricsDataConverter;
    }

    //Long id,String facilityIds, String state1
    @GetMapping(value = "/convertExcel")
    public void convertExcel(@RequestParam("id") String facilityId,
                             @RequestParam("state") String state,
                             HttpSession session, HttpServletResponse response) throws IOException {
        Long userId = (Long) session.getAttribute("id");
        response.addHeader("Content-disposition", "attachment;filename=pediatrics_summary.xlsx");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = pedeatricsDataConverter.convertExcel(userId, facilityId, state);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }

}
