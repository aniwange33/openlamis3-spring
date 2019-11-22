/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.mobile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Hts;
import org.fhi360.lamis.model.repositories.HtsRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.springframework.stereotype.Component;

/**
 * @author user10
 */
@Component
public class HtsJsonParser {
    private final HtsRepository htsRepository;

    public HtsJsonParser(HtsRepository htsRepository) {
        this.htsRepository = htsRepository;
    }


    public void parserJson(String table, String content) {
        try {
            getObject(content).forEach(hts -> {
                String clientCode = hts.getClientCode();
                Long htsId = ServerIDProvider.getHtsId(clientCode, hts.getFacilityId());
                Facility facility = new Facility();
                facility.setId(hts.getFacilityId());
                hts.setFacility(facility);
                hts.setHtsId(htsId);
                htsRepository.save(hts);
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    private static List<Hts> getObject(String content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return objectMapper.readValue(content, new TypeReference<List<Hts>>() {
        });
    }
}

    

