/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.Conversation;
import org.fhi360.lamis.model.repositories.ConversationRepository;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chart/participantGridAction")
@Api(tags = "participantGridAction Chart", description = " ")
public class ParticipantGridAction {
    private final ConversationRepository conversationRepository;

    public ParticipantGridAction(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @GetMapping("/participantGrid")
    public ResponseEntity<Conversation> participantGrid(@RequestParam("phone") String phone) {
        return ResponseEntity.ok().body(conversationRepository.findRecentMessage(phone));
    }


}
