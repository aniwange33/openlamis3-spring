package org.fhi360.lamis.controller.export;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.fhi360.lamis.converter.TxMlDataConverter;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(value = "/export/txMlDataConverter")
@Api(tags = "TxMlDataConverter", description = " ")
public class TxMIDataController {
    private final TxMlDataConverter txMlDataConverter;


    public TxMIDataController(TxMlDataConverter txMlDataConverter) {
        this.txMlDataConverter = txMlDataConverter;
    }


    @GetMapping(value = "/convertExcel")
    public void convertExcel(@RequestParam("id") String facilityId,
                             @RequestParam("outcome1") String outcome1,
                             @RequestParam("state") String state,
                             HttpSession session, HttpServletResponse response) throws IOException {
        Long userId = (Long) session.getAttribute("id");
        response.addHeader("Content-disposition", "attachment;filename=txml_data.xlsx");
        response.setContentType("application/vnd.ms-excel");
        // Copy the stream to the response's output stream.
        ByteArrayOutputStream outputStream = txMlDataConverter
                .convertExcel(true, facilityId, userId, outcome1, state);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }
}
