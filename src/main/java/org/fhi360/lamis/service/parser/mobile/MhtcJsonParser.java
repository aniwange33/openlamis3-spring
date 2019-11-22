/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.parser.mobile;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fhi360.lamis.model.*;
import org.fhi360.lamis.model.repositories.MhtcRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author user10
 */
@Component
public class MhtcJsonParser {

private  final MhtcRepository mhtcRepository;

    public MhtcJsonParser(MhtcRepository mhtcRepository) {
        this.mhtcRepository = mhtcRepository;
    }


    public void parserJson(String table, String content) {
        try {
            getObject(content).forEach(mhtc -> {
                CommunityPharmacy communityPharmacy = new CommunityPharmacy();
                communityPharmacy.setCommunitypharmId(mhtc.getCommunityPharmacy().getCommunitypharmId());
                Mhtc mhtcId = mhtcRepository.findByAndCommunityPharmacyAndMonthAndYear(communityPharmacy, mhtc.getMonth(), mhtc.getYear());
                if (mhtcId == null) {
                    mhtcRepository.save(mhtc);
                } else {
                    mhtc.setMhtcId(mhtcId.getMhtcId());
                    mhtcRepository.save(mhtc);
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }



    private static List<Mhtc> getObject(String content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return objectMapper.readValue(content, new TypeReference<List<Mhtc>>() {
        });
    }


}
