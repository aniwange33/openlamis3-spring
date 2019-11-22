package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.PharmacyEventConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping(value = "/export/pharmacyEvent")
@Api(tags = "PharmacyEvent", description = " ")
public class PharmacyEventController {
    private  final PharmacyEventConverter pharmacyEventConverter;

    public PharmacyEventController(PharmacyEventConverter pharmacyEventConverter) {
        this.pharmacyEventConverter = pharmacyEventConverter;
    }

    //Long id,String facilityIds, String state1
    @GetMapping(value = "/convertExcel")
    public void convertExcel(@RequestParam("id") String facilityId,
                             @RequestParam("state") String state,
                             HttpSession session, HttpServletResponse response) throws IOException {
        Long userId = (Long) session.getAttribute("id");
        response.addHeader("Content-disposition", "attachment;filename=pharmacy_event.xlsx");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = pharmacyEventConverter.convertExcel(userId, facilityId, state);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }


}
