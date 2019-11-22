package org.fhi360.lamis.controller.mapstruct;

import org.fhi360.lamis.model.Delivery;
import org.fhi360.lamis.model.dto.DeliveryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    @Mappings({
            @Mapping(source = "patientId", target = "patient.patientId"),
            @Mapping(source = "facilityId", target = "facility.id"),
            @Mapping(source = "ancId", target = "anc.ancId"),
            @Mapping(source = "dateDelivery", target = "dateDelivery", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateArvRegimenCurrent", target = "dateArvRegimenCurrent", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateConfirmedHiv", target = "dateConfirmedHiv", dateFormat = "MM/dd/yyyy")
    })
    Delivery dtoToDelivery(DeliveryDTO dto);

    @Mappings({
            @Mapping(source = "patient.patientId", target = "patientId"),
            @Mapping(source = "facility.id", target = "facilityId"),
            @Mapping(source = "dateDelivery", target = "dateDelivery", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateArvRegimenCurrent", target = "dateArvRegimenCurrent", dateFormat = "MM/dd/yyyy"),
            @Mapping(source = "dateConfirmedHiv", target = "dateConfirmedHiv", dateFormat = "MM/dd/yyyy")
    })
    DeliveryDTO deliveryToDto(Delivery delivery);

    List<DeliveryDTO> deliveryToDto(List<Delivery> delivery);
}
