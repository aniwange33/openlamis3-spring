/**
 * @author aalozie
 */
package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.Adr;
import org.fhi360.lamis.model.repositories.AdrRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import org.fhi360.lamis.utility.JDBCUtil;

@RestController
@RequestMapping("/api/adr")
@Api(tags = "Adr Grid Chart", description = " ")
public class AdrGridAction {
    private final AdrRepository adrRepository;

    public AdrGridAction(AdrRepository adrRepository) {
        this.adrRepository = adrRepository;
    }

    @GetMapping("/grid")
    public ResponseEntity adrGrid(HttpSession session) {
        List<Adr> list = adrRepository.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("currpage", 1);
        response.put("adrList", list);
        session.setAttribute("adrList", list);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/update")
    public ResponseEntity updateAdrList(@RequestParam("id") Long id, @RequestParam("severity") String severity, HttpSession session) {
        List<Adr> adrList = (List<Adr>) session.getAttribute("adrList");
        adrList = adrList.stream()
                .map(adr -> {
                    if (adr.getAdrId().equals(id)) {
                        adr.setSeverity(severity);
                    }
                    return adr;
                }).collect(Collectors.toList());
        session.setAttribute("adrList", adrList);
        return ResponseEntity.ok().build();
    }
}
