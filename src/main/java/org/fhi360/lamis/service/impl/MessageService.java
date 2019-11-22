/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.impl;

import org.fhi360.lamis.model.Message;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author user10
 */
public interface MessageService {

    ResponseEntity<?> saveMessage(Message message);

    ResponseEntity<?> findMessage(long messageId);

    ResponseEntity<?> delete(Message message);

    ResponseEntity<?> updateMessage(Message message);

}
