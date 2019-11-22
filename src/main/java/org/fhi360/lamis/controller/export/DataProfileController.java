package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.DataProfileConverter;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping(value = "/export/dataProfile")
@Api(tags = "DataProfile", description = " ")
public class DataProfileController {
    private final DataProfileConverter dataProfileConverter;

    public DataProfileController(DataProfileConverter dataProfileConverter) {
        this.dataProfileConverter = dataProfileConverter;
    }

    @GetMapping(value = "/convertExcel")
    public void convertExcel(@RequestParam("year") String year,
                             @RequestParam("month") String month, HttpSession session,
                             HttpServletResponse response) throws IOException {
        Long userId = (Long) session.getAttribute("id");
        Long facilityId = (Long) session.getAttribute("id");
        response.addHeader("Content-disposition", String.format("attachment;filename=profiling_%s_%s.xlsx", year, month));
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = dataProfileConverter.convertExcel(facilityId, year, month, userId);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }
}
