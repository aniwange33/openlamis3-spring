/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.impl;
import org.fhi360.lamis.model.Message;
import org.fhi360.lamis.model.ServerResponseStatus;
import org.fhi360.lamis.model.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author user10
 */
public class MessageImpl implements MessageService{
    @Autowired
    private MessageRepository messageRepository;
    
    @Override
    public ResponseEntity<?> saveMessage(Message message) {
        try {
        return new ResponseEntity<>(messageRepository.save(message), ServerResponseStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerResponseStatus.FAILED);
        }

    }
    
    @Override
    public ResponseEntity<?> updateMessage(Message message) {
        try {
        return new ResponseEntity<>(messageRepository.save(message), ServerResponseStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(ServerResponseStatus.FAILED);
        }

    }
   
    @Override
    public ResponseEntity<?> delete(Message message) {

        try {
            Message message1 = messageRepository.getOne(message.getMessageId());
            if (message1 != null) {
                messageRepository.delete(message1);
                return new ResponseEntity<>(ServerResponseStatus.CREATED);
            }else{
                return new ResponseEntity<>(ServerResponseStatus.FAILED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(ServerResponseStatus.FAILED);
        }
       
    }
    
      @Override
    public ResponseEntity<?> findMessage(long messageId) {

        try {
            Message message1 = messageRepository.getOne(messageId);
            if (message1 != null) {
               
                return new ResponseEntity<>(message1,ServerResponseStatus.OK);
            }else{
                return new ResponseEntity<>(ServerResponseStatus.FAILED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(ServerResponseStatus.FAILED);
        }
       
    }
}
