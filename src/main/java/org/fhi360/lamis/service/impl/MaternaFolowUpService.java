/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.impl;

import java.time.LocalDate;
import java.util.Date;

import org.fhi360.lamis.model.MaternalFollowup;

import org.springframework.http.ResponseEntity;

/**
 *
 * @author user10
 */
public interface MaternaFolowUpService {

    ResponseEntity<?> save(MaternalFollowup maternalfollowup);

    ResponseEntity<?> update(MaternalFollowup maternalfollowup);

    ResponseEntity<?> delete(MaternalFollowup maternalfollowup);

    ResponseEntity<?> findMaternalfollowup(long patientId, LocalDate dateVisit);
    ResponseEntity<?> findAll();
}
