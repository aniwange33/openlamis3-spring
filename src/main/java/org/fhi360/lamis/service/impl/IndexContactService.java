/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.impl;

import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.controller.mapstruct.IndexContactMapper;
import org.fhi360.lamis.model.Hts;
import org.fhi360.lamis.model.IndexContact;
import org.fhi360.lamis.model.dto.IndexContactDto;
import org.fhi360.lamis.model.repositories.HtsRepository;
import org.fhi360.lamis.model.repositories.IndexContactRepository;
import org.fhi360.lamis.service.MonitorService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author user10
 */
@Service
@Transactional
public class IndexContactService {

    private final IndexContactRepository indexContactRepository = ContextProvider.getBean(IndexContactRepository.class);
    private final IndexContactMapper indexContactMapper = ContextProvider.getBean(IndexContactMapper.class);
    private final HtsRepository htsRepository = ContextProvider.getBean(HtsRepository.class);

    public void saveIndexContact(IndexContactDto indexContact) {
        Hts hts = htsRepository.getOne(indexContact.getHtsId());
        IndexContact indexContact1 = indexContactMapper.indexContactDtoToindexContact(indexContact);
        indexContact1.setHts(hts);
        indexContactRepository.save(indexContact1);

    }


    public void delete(@PathVariable long id) {
        IndexContact indexContact = indexContactRepository.getOne(id);
        MonitorService.logEntity(indexContact.getIndexcontactCode(), "indexContact", 3);
        indexContactRepository.deleteById(id);

    }

    public IndexContactDto findIndexcontact(long indexcContactId) {
        IndexContact indexContactId = indexContactRepository.getOne(indexcContactId);
        return indexContactMapper.indexContactToDto(indexContactId);

    }


    public List<IndexContactDto> indexcontactGrid(Long htsId, Integer rows, Integer page) {
        Hts hts = htsRepository.getOne(htsId);
        return indexContactMapper.indexContactToDto(indexContactRepository.findByHts(hts, PageRequest.of(page, rows)));
    }

}
