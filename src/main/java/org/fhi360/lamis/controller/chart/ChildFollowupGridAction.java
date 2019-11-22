/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.controller.mapstruct.ChildFollowupMapper;
import org.fhi360.lamis.model.Child;
import org.fhi360.lamis.model.ChildFollowup;
import org.fhi360.lamis.model.dto.ChildFollowupDTO;
import org.fhi360.lamis.model.repositories.ChildFollowupRepository;
import org.fhi360.lamis.model.repositories.ChildRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chart/child-followup")
@Api(tags = "ChildFollowup Grid Chart", description = " ")
public class ChildFollowupGridAction {
    private final ChildRepository childRepository;
    private final ChildFollowupRepository childFollowupRepository;
    private final ChildFollowupMapper childFollowupMapper;

    public ChildFollowupGridAction(ChildRepository childRepository, ChildFollowupRepository childFollowupRepository,
                                   ChildFollowupMapper childFollowupMapper) {
        this.childRepository = childRepository;
        this.childFollowupRepository = childFollowupRepository;
        this.childFollowupMapper = childFollowupMapper;
    }

    @GetMapping("/grid")
    public ResponseEntity childFollowupGrid(@RequestParam("childId") Long childId) {
        Child child = childRepository.getOne(childId);
        List<ChildFollowup> followups = childFollowupRepository.findByChild(child);
        List<ChildFollowupDTO> dtos = childFollowupMapper.childFollowupToDto(followups);
        Map<String, Object> response = new HashMap<>();
        response.put("childfollowupList", dtos);
        return ResponseEntity.ok(response);
    }
}
