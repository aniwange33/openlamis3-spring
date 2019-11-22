/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.ConversionUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author BRAINERGY SOLUTIONS
 */
@Component
public class ExcelConverterService {
    private final ConversionUtil conversionUtil;
    private   FacilityRepository facilityRepository;
    public ExcelConverterService()
    {
        this.conversionUtil = new ConversionUtil();
    }
    
    public void startConversion() {        
        System.out.println("Starting...!");
        List<Long> statesCount = facilityRepository.getStatesForFacilitiesInPatient();
        
        System.out.println("State count size is: " + statesCount.size());
        if(statesCount.size() > 0) {
            for(int i = 0; i< statesCount.size(); i++){
                System.out.println("Starting conversion for state ID: " + statesCount.get(i));
                long stateToConvet = statesCount.get(i);
                System.out.println("Records Count is: " + Constants.Conversion.aspects.length);
                for(int j = 0; j < Constants.Conversion.aspects.length; j++){
                    int jj = j+1;
                    System.out.println("Starting conversion record: " + Constants.Conversion.aspects[j]);
                    conversionUtil.convert(stateToConvet, jj);
                }
            }
        }
    }
}
