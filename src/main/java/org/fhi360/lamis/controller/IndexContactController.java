/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.fhi360.lamis.controller.mapstruct.IndexContactMapper;
import org.fhi360.lamis.model.IndexContact;
import org.fhi360.lamis.model.dto.IndexContactDto;
import org.fhi360.lamis.service.impl.IndexContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Idris
 */
@RestController
@RequestMapping(value = "/api/index-contact")
@Api(tags = "Index Contact", description = " ")
public class IndexContactController {
    private final IndexContactService indexcontactservice;
    public IndexContactController(IndexContactService indexcontactservice) {
        this.indexcontactservice = indexcontactservice;
    }

    @PostMapping
    @PutMapping
    public ResponseEntity saveIndexContact(@RequestBody IndexContactDto indexContactDto) {
        indexcontactservice.saveIndexContact(indexContactDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") Long indexContactId) {
        indexcontactservice.delete(indexContactId);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findIndexContact(@PathVariable long id) {
        return ResponseEntity.ok(Collections.singletonList(indexcontactservice.findIndexcontact(id)));
    }

    @GetMapping(value = "/grid")
    public ResponseEntity indexContactGrid(@RequestParam Long htsId, @RequestParam("rows") int rows, @RequestParam("page") int page) {
        List<IndexContactDto> indexContact = indexcontactservice.indexcontactGrid(htsId, page, rows);
        Map<String, Object> response = new HashMap<>();
        response.put("indexcontactList", indexContact);
        return ResponseEntity.ok(response);
    }
}
