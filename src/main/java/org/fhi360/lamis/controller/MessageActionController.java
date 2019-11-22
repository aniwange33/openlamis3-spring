/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.Message;
import org.fhi360.lamis.service.impl.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author user10
 */
@RestController
@RequestMapping(value = "/message")
@Api(tags = "Message" , description = " ")
public class MessageActionController {
    private MessageService messageService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Message message){
        return messageService.saveMessage(message);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMessage(@RequestBody Message message){

        return messageService.updateMessage(message);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?>  delete(@RequestBody Message message){
        return messageService.delete(message);
    }

    @RequestMapping(value = "/findMessage/{messageId}", method = RequestMethod.GET)
    public ResponseEntity<?> findMessage(@RequestParam("messageId") long messageId){
        return messageService.findMessage(messageId);

    }
}
