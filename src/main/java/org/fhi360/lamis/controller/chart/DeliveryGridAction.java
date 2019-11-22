/**
 *
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.controller.mapstruct.DeliveryMapper;
import org.fhi360.lamis.model.Delivery;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.DeliveryDTO;
import org.fhi360.lamis.model.repositories.DeliveryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chart/delivery")
@Api(tags = "Delivery Grid Chart", description = " ")
public class DeliveryGridAction  {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;

    public DeliveryGridAction(DeliveryRepository deliveryRepository, DeliveryMapper deliveryMapper) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryMapper = deliveryMapper;
    }

    @GetMapping("/grid")
    public ResponseEntity deliveryGrid(@RequestParam("id") Long patientId, HttpSession session) {
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        List<Delivery> deliveries = deliveryRepository.findByPatient(patient);
        List<DeliveryDTO> dtos = deliveryMapper.deliveryToDto(deliveries);
        Map<String, Object> response = new HashMap<>();
        response.put("deliveryList", dtos);
        session.setAttribute("deliveryList", dtos);
        return ResponseEntity.ok(response);
    }
}
