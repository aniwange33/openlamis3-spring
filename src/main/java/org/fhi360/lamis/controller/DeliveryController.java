package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.fhi360.lamis.controller.mapstruct.ChildMapper;
import org.fhi360.lamis.controller.mapstruct.DeliveryMapper;
import org.fhi360.lamis.controller.mapstruct.PartnerInformationMapper;
import org.fhi360.lamis.model.*;
import org.fhi360.lamis.model.dto.DeliveryDTO;
import org.fhi360.lamis.model.dto.PartnerInformationDTO;
import org.fhi360.lamis.model.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/delivery")
@Api(tags = "Delivery", description = " ")
@Slf4j
public class DeliveryController {
    private final DeliveryRepository deliveryRepository;
    private final PatientRepository patientRepository;
    private final DeliveryMapper deliveryMapper;
    private final AncRepository ancRepository;
    private final MotherInformationRepository motherInformationRepository;
    private final ChildMapper childMapper;
    private final PartnerInformationRepository partnerInformationRepository;
    private final PartnerInformationMapper partnerInformationMapper;

    public DeliveryController(DeliveryRepository deliveryRepository,
                              PatientRepository patientRepository, DeliveryMapper deliveryMapper,
                              AncRepository ancRepository, MotherInformationRepository motherInformationRepository,
                              ChildMapper childMapper, PartnerInformationRepository partnerInformationRepository,
                              PartnerInformationMapper partnerInformationMapper) {
        this.deliveryRepository = deliveryRepository;
        this.patientRepository = patientRepository;
        this.deliveryMapper = deliveryMapper;
        this.ancRepository = ancRepository;
        this.motherInformationRepository = motherInformationRepository;
        this.childMapper = childMapper;
        this.partnerInformationRepository = partnerInformationRepository;
        this.partnerInformationMapper = partnerInformationMapper;
    }

    @PostMapping
    @PutMapping
    public ResponseEntity saveDelivery(@RequestBody DeliveryDTO dto) {
        Patient patient = patientRepository.getOne(dto.getPatientId());
        Delivery delivery = deliveryMapper.dtoToDelivery(dto);
        delivery.setPatient(patient);
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
        Delivery result = deliveryRepository.save(delivery);
        PartnerInformation partnerInformation = partnerInformationMapper.dtoToModel(dto.getPartnerInformation());
        partnerInformationRepository.save(partnerInformation);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteDelivery(@PathVariable("id") Long deliveryId) {
        deliveryRepository.deleteById(deliveryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find")
    public ResponseEntity findDelivery(@RequestParam("id") Long patientId, @RequestParam("deliveryDate") LocalDate date) {
        Patient patient = patientRepository.getOne(patientId);
        List<Delivery> deliveries = deliveryRepository.findByPatientAndDateDelivery(patient, date);
        List<DeliveryDTO> dtos = deliveryMapper.deliveryToDto(deliveries);
        Map<String, Object> response = new HashMap<>();
        response.put("deliveryList", dtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity getByDelivery(@PathVariable Long id) {
        Delivery delivery = deliveryRepository.getOne(id);
        DeliveryDTO deliveryDTO = deliveryMapper.deliveryToDto(delivery);
        Map<String, Object> response = new HashMap<>();
        response.put("deliveryList", deliveryDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity getByPatient(@PathVariable Long id) {
        Patient patient = patientRepository.getOne(id);
        List<Delivery> deliveries = deliveryRepository.findByPatient(patient);
        List<DeliveryDTO> dtos = deliveryMapper.deliveryToDto(deliveries);
        Map<String, Object> response = new HashMap<>();
        response.put("deliveryList", dtos);
        return ResponseEntity.ok(response);
    }
}
