package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.DeduplicatorDataConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping(value = "/export/deduplicatorData")
@Api(tags = "DeduplicatorData", description = " ")
public class DeduplicatorDataController {
    private  final DeduplicatorDataConverter deduplicatorDataConverter;

    public DeduplicatorDataController(DeduplicatorDataConverter deduplicatorDataConverter) {
        this.deduplicatorDataConverter = deduplicatorDataConverter;
    }

    @RequestMapping(value = "/convertExcel", method = RequestMethod.GET)
    public void convertExcel(HttpSession session, HttpServletResponse response) throws IOException {
        Long userId = (Long) session.getAttribute("id");
        response.addHeader("Content-disposition", "attachment;filename=deduplicator.xlsx");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = deduplicatorDataConverter.convertExcel(userId);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }
}
