/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.impl;

import java.util.List;
import org.fhi360.lamis.model.LabTest;

import org.fhi360.lamis.model.ServerResponseStatus;

import org.fhi360.lamis.model.repositories.LabTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author user10
 */
@Service
@Transactional
public class LabtestImpl implements LabtestService {

    @Autowired
    private LabTestRepository labTestRepository;

    @Override
    public ResponseEntity<?> retrieveLabtestMap() {

        try {
            List<LabTest> labtest = labTestRepository.findAll(Sort.by(Sort.Order.by("description")));
            if (!labtest.isEmpty()) {
                return new ResponseEntity<>(labtest, ServerResponseStatus.OK);
            } else {
                return new ResponseEntity<>(labtest,ServerResponseStatus.FAILED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(ServerResponseStatus.FAILED);
        }

    }
}
