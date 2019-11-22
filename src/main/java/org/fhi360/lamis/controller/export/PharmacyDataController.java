package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.PharmacyDataConverter;
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
@RequestMapping(value = "/export/pharmacyData")
@Api(tags = "PharmacyData", description = " ")
public class PharmacyDataController {
    private final PharmacyDataConverter pharmacyDataConverter;

    public PharmacyDataController(PharmacyDataConverter pharmacyDataConverter) {
        this.pharmacyDataConverter = pharmacyDataConverter;
    }

    @GetMapping(value = "/convertExcel")
    public void convertExcel(
            @RequestParam("state") String state,
            @RequestParam("facilityIds") String facilityIds,
            @RequestParam("option") String option,
            @RequestParam("aspect") Integer aspect,
            HttpSession session, HttpServletResponse response) throws IOException {
        Long userId = (Long) session.getAttribute("id");
        response.addHeader("Content-disposition", "attachment;filename=pharmacy_summary.xlsx");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = pharmacyDataConverter
                .convertExcel(userId, facilityIds, state, option, aspect, true);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }
}
