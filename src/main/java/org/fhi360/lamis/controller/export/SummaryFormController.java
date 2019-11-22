package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.SummaryFormConverter;
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
@RequestMapping(value = "/export/summaryForm")
@Api(tags = "SummaryForm", description = " ")
@Slf4j
public class SummaryFormController {
    private final SummaryFormConverter summaryFormConverter;

    public SummaryFormController(SummaryFormConverter summaryFormConverter) {
        this.summaryFormConverter = summaryFormConverter;
    }

    @GetMapping(value = "/convertExcel")
    public void convertExcel(@RequestParam("id") String facilityIds,
                             @RequestParam("yearId") String yearId,
                             @RequestParam("state") String state,
                             HttpSession session,
                             HttpServletResponse response) throws IOException {
        Long userId =(Long)session.getAttribute("id");
        response.addHeader("Content-disposition", "attachment;filename=summary_form.xls");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = summaryFormConverter.convertExcel(facilityIds,   yearId,   state,  userId);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }
}
