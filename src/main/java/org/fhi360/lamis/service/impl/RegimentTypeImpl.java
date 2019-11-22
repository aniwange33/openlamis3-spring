package org.fhi360.lamis.service.impl;

import org.fhi360.lamis.model.Regimen;
import org.fhi360.lamis.model.RegimenType;
import org.fhi360.lamis.model.repositories.RegimenTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class RegimentTypeImpl {
      /*   @Autowired
      private  RegimenTypeRepository regimenTypeRepository;
         public ResponseEntity<?> retrieveRegimenTypeById(RegimenType regimenType){
             try{
                 List<RegimenType> regimenTypes= regimenTypeRepository.findAll(Sort.by(Sort.Order.by("description")));
                for(RegimenType regimenType1 : regimenTypes){
                   for(Regimen regimen: regimenType1.getRegimens()){
                       if(regimen.getRegimenType())
                   }
                }

             }catch(Exception e){

             }
         }
*/
}
