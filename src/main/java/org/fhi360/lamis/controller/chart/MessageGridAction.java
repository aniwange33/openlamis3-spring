/**
 *
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.Message;
import org.fhi360.lamis.model.repositories.MessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chart/MessageGridAction")
@Api(tags = "MessageGridAction Chart", description = " ")
public class MessageGridAction{
    private  final MessageRepository messageRepository;

    public MessageGridAction(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("messageGrid")
    public ResponseEntity<List<Message>> messageGrid() {
        return ResponseEntity.ok().body(messageRepository.findAll());
    }

}
