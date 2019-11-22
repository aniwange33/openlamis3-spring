package org.fhi360.lamis.controller;

import org.fhi360.lamis.service.ImportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data")
public class DataImportController {
    private final ImportService importService;

    public DataImportController(ImportService importService) {
        this.importService = importService;
    }

    @GetMapping("/import/{fileName}")
    public void importData(@PathVariable String fileName) {
        importService.processXml(fileName);
    }
}
