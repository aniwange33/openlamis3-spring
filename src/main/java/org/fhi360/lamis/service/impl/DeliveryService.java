package org.fhi360.lamis.service.impl;

import org.fhi360.lamis.controller.mapstruct.ChildMapper;
import org.fhi360.lamis.controller.mapstruct.DeliveryMapper;
import org.fhi360.lamis.model.*;
import org.fhi360.lamis.model.dto.DeliveryDTO;
import org.fhi360.lamis.model.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final FacilityRepository facilityRepository;
    private final PatientRepository patientRepository;
    private final DeliveryMapper deliveryMapper;
    private final AncRepository ancRepository;
    private final MotherInformationRepository motherInformationRepository;
    private final ChildMapper childMapper;

    public DeliveryService(DeliveryRepository deliveryRepository, FacilityRepository facilityRepository, PatientRepository patientRepository, DeliveryMapper deliveryMapper, AncRepository ancRepository, MotherInformationRepository motherInformationRepository, ChildMapper childMapper) {
        this.deliveryRepository = deliveryRepository;
        this.facilityRepository = facilityRepository;
        this.patientRepository = patientRepository;
        this.deliveryMapper = deliveryMapper;
        this.ancRepository = ancRepository;
        this.motherInformationRepository = motherInformationRepository;
        this.childMapper = childMapper;
    }

    public Delivery saveDelivery(DeliveryDTO dto, long facilityId){
        Facility facility = facilityRepository.getOne(facilityId);
        Patient patient = patientRepository.getOne(dto.getPatientId());
        Delivery delivery = deliveryMapper.dtoToDelivery(dto);
        delivery.setPatient(patient);
        delivery.setFacility(facility);
        if (dto.getAncId() != null) {
            Anc anc = ancRepository.getOne(dto.getAncId());
            delivery.setAnc(anc);
        }
        MotherInformation motherInformation = motherInformationRepository.findByPatient(patient);
        AtomicInteger count = new AtomicInteger(1);
        List<Child> children = childMapper.dtosToChildren(dto.getChildren())
                .stream()
                .map(child -> {
                    child.setMother(motherInformation);
                    child.setReferenceNum(motherInformation.getHospitalNum() + "#" + count.getAndIncrement());
                    return child;
                }).collect(Collectors.toList());
        delivery.getChildren().addAll(children);
        return deliveryRepository.save(delivery);
    }
    public  void  deleteDelivery(long deliveryId){
        deliveryRepository.deleteById(deliveryId);
    }

    public List<DeliveryDTO>  findDelivery(long patientId, LocalDate date){
        Patient patient = patientRepository.getOne(patientId);
        List<Delivery> deliveries = deliveryRepository.findByPatientAndDateDelivery(patient, date);
        return deliveryMapper.deliveryToDto(deliveries);
    }

}
