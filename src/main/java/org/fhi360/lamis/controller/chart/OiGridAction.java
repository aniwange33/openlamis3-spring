/**
 *
 * @author aalozie
 */
package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.Oi;
import org.fhi360.lamis.model.repositories.OiRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/oi")
@Api(tags = "OiGridAction Chart", description = " ")
public class OiGridAction{
private  final OiRepository oiRepository;

    public OiGridAction(OiRepository oiRepository) {
        this.oiRepository = oiRepository;
    }

    @GetMapping("grid")
    public ResponseEntity<?> oiGrid(HttpSession session) {
       List<Oi> oiList = this.oiRepository.findAll();
           session.setAttribute("oiList", oiList);
        return ResponseEntity.ok().build();
    }

}
