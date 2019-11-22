/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.mobile;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.List;

import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Hts;
import org.fhi360.lamis.model.IndexContact;
import org.fhi360.lamis.model.repositories.IndexContactRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.springframework.stereotype.Component;

/**
 * @author user10
 */
@Component
public class IndexcontactJsonParser {
    private final IndexContactRepository indexContactRepository;

    public IndexcontactJsonParser(IndexContactRepository indexContactRepository) {
        this.indexContactRepository = indexContactRepository;
    }

    public void parserJson(String table, String content) {
        try {
            getObject(content).forEach(indexcontact -> {
                String clientCode = indexcontact.getClientCode();
                Long htsId = ServerIDProvider.getHtsId(clientCode, indexcontact.getFacilityId());
                Hts hts = new Hts();
                hts.setHtsId(htsId);
                indexcontact.setHts(hts);
                Facility facility = new Facility();
                facility.setId(indexcontact.getFacilityId());
                indexcontact.setFacility(facility);
                Long indexContactId = ServerIDProvider.getIndexcontactId(indexcontact.getIndexcontactCode(), indexcontact.getFacilityId());
                indexcontact.setIndexcontactId(indexContactId);
                indexContactRepository.save(indexcontact);
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static List<IndexContact> getObject(String content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return objectMapper.readValue(content, new TypeReference<List<IndexContact>>() {
        });
    }

}
