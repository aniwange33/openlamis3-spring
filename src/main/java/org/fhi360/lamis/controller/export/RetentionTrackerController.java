package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.RetentionTrackerConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping(value = "/export/retentionTracker")
@Api(tags = "RetentionTracker", description = " ")
public class RetentionTrackerController {
    private final RetentionTrackerConverter retentionTrackerConverter;

    public RetentionTrackerController(RetentionTrackerConverter retentionTrackerConverter) {
        this.retentionTrackerConverter = retentionTrackerConverter;
    }

    //Long id,String facilityIds, String state1
    @RequestMapping(value = "/convertExcel", method = RequestMethod.GET)
    public void convertExcel(@RequestParam("state") String state,
                                          @RequestParam("reportingDateBegin") String reportingDateBegin,
                                          HttpSession session, HttpServletResponse response) throws IOException {
        response.addHeader("Content-disposition", "attachment;filename=retention_tracker.xlsx");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = retentionTrackerConverter.convertExcel(state, reportingDateBegin);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }
}
