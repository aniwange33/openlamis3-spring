/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.ndr;

import org.fhi360.lamis.service.UploadFolderService;
import org.fhi360.lamis.utility.ConverterUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author user1
 */
@RestController
public class NdrAction  {
    private String status;
    private String fileName;

    UploadFolderService uploadFolderService = new UploadFolderService();

    @GetMapping("/dispatcher")
    public ResponseEntity dispatcher(@RequestParam String stateId, @RequestParam String facilityIds) {
        ConverterUtil.performNdrConversion(stateId, facilityIds, true);
        return ResponseEntity.ok().build();
    }
}
