/**
 * @author aalozie
 */
package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.repositories.AdhereRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/adhere")
@Api(tags = "Adhere Grid Chart", description = " ")
public class AdhereGridAction {
    private final AdhereRepository adhereRepository;

    public AdhereGridAction(AdhereRepository adhereRepository) {
        this.adhereRepository = adhereRepository;
    }

    @GetMapping("/grid")
    public ResponseEntity adhereGrid() {
        Map<String, Object> response = new HashMap<>();
        response.put("currpage", 1);
        System.out.println("adhereList "+ adhereRepository.findAll());
        response.put("adhereList", adhereRepository.findAll());

        return ResponseEntity.ok(response);
    }
}
