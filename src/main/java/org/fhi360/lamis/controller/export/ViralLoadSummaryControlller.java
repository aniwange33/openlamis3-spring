package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.ViralLoadSummaryConverter;
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
@RequestMapping(value = "/export/viral-load-summary")
@Api(tags = "TXmlReport", description = " ")
public class ViralLoadSummaryControlller {
    private final ViralLoadSummaryConverter viralLoadSummaryConverter;

    public ViralLoadSummaryControlller(ViralLoadSummaryConverter viralLoadSummaryConverter) {
        this.viralLoadSummaryConverter = viralLoadSummaryConverter;
    }

    @GetMapping(value = "/convertExcel")
    public void convertExcel(@RequestParam("id") String facilityIds,
                             @RequestParam("state") String state1,
                             @RequestParam("reportingDateBegin1") String reportingDateBegin1,
                             @RequestParam("reportingDateEnd1") String reportingDateEnd1,
                             HttpSession session, HttpServletResponse response) throws IOException {
        Long userId = (Long) session.getAttribute("id");
        response.addHeader("Content-disposition", "attachment;filename=viral_load_summary.xlsx");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = viralLoadSummaryConverter
                .convertExcel(userId, facilityIds, state1, reportingDateBegin1, reportingDateEnd1);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }
}
