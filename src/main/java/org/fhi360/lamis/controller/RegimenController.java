package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.Pharmacy;
import org.fhi360.lamis.model.Regimen;
import org.fhi360.lamis.model.RegimenType;
import org.fhi360.lamis.model.dto.RegimenIdAndDescriptionDto;
import org.fhi360.lamis.model.dto.RegimenTypeDescription;
import org.fhi360.lamis.model.repositories.PharmacyRepository;
import org.fhi360.lamis.model.repositories.RegimenRepository;
import org.fhi360.lamis.model.repositories.RegimenTypeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping(value = "/api/regimen")
@Api(tags = "Regimen", description = " ")
@Slf4j
public class RegimenController {

    private final RegimenTypeRepository regimenTypeRepository;
    ///api/regimen/grid
    private final RegimenRepository regimenRepository;

    public RegimenController(RegimenTypeRepository regimenTypeRepository, RegimenRepository regimenRepository) {
        this.regimenTypeRepository = regimenTypeRepository;
        this.regimenRepository = regimenRepository;
    }

    @RequestMapping(value = "/by-id/{regimenTypeId}", method = RequestMethod.GET)
    public ResponseEntity retrieveRegimenByIdMap(@PathVariable(value = "regimenTypeId") Long regimenTypeId) {
        RegimenIdAndDescriptionDto regimenIdAndDescriptionDto = new RegimenIdAndDescriptionDto();
        List<RegimenIdAndDescriptionDto> regimenIdAndDescriptionDtos = new ArrayList<>();
        RegimenType regimenType = new RegimenType();
        regimenType.setId(regimenTypeId);
        List<Regimen> regimenList = regimenRepository.retrieveRegimenByName(regimenType);
        regimenList.forEach((regimen) -> {
            regimenIdAndDescriptionDto.setRegimenId(regimen.getId());
            regimenIdAndDescriptionDto.setDescriptions(regimen.getDescription());
            regimenIdAndDescriptionDtos.add(regimenIdAndDescriptionDto);
        });
        return ResponseEntity.ok().body(regimenIdAndDescriptionDtos);
    }

    @RequestMapping(value = "/retrieve-regimen-type-by-name/{regimenTypeId}", method = RequestMethod.GET)
    public ResponseEntity<List<RegimenTypeDescription>> retrieveRegimenByName(@PathVariable(value = "regimenTypeId") Long regimenTypeId, HttpSession session) {
        RegimenTypeDescription regimenTypeDescription = new RegimenTypeDescription();
        List<RegimenTypeDescription> regimenTypeDescriptions = new ArrayList<>();
        RegimenType regimenType = new RegimenType();
        regimenType.setId(regimenTypeId);
        List<Regimen> regimenList = regimenRepository.retrieveRegimenByName(regimenType);
        regimenList.forEach((regimen) -> {
            regimenTypeDescription.setDescriptions(regimen.getDescription());
            regimenTypeDescriptions.add(regimenTypeDescription);
        });
        return ResponseEntity.ok().body(regimenTypeDescriptions);
    }

    @GetMapping(value = "/all-regimen")
    public ResponseEntity retrieveAllRegimen() {
        List<Regimen> regimenList = regimenRepository.findAll(Sort.by(Sort.Order.by("description")));
        return ResponseEntity.ok().body(regimenList);
    }

    @GetMapping(value = "/last-regimen/patient/{id}")
    public ResponseEntity getLastRegimen(@PathVariable Long id) {
        Patient patient = new Patient();
        patient.setPatientId(id);
        LOG.info("lastRegimenList {}", id );
        Pharmacy pharmacy = ContextProvider.getBean(PharmacyRepository.class).getLastRefillByPatient(patient);
        Regimen regimen = ContextProvider.getBean(RegimenRepository.class).getOne(pharmacy.getRegimen().getId());
        Map<String, Object> response = new HashMap<>();
        response.put("lastRegimenList", regimen);
        return ResponseEntity.ok(Collections.singletonList(response));
    }

    @GetMapping("/types")
    public ResponseEntity regimenTypes() {
        List<RegimenType> types = regimenTypeRepository.findAll();
        return ResponseEntity.ok(types);
    }

    @GetMapping("/grid")
    public ResponseEntity regimenGrid(@RequestParam(required = false) Long regimentypeId,
                                      @RequestParam(required = false, defaultValue = "") String regimenType) {
        RegimenType type = null;
        if (regimentypeId != null) {
            type = regimenTypeRepository.getOne(regimentypeId);
        }
        List<RegimenType> regimenTypes = new ArrayList<>();
        if (type != null) {
            if ((type.getDescription().toLowerCase().contains("first"))) {
                regimenType = "first";
            } else if ((type.getDescription().toLowerCase().contains("second"))) {
                regimenType = "second";
            } else if ((type.getDescription().toLowerCase().contains("commence"))) {
                regimenType = "commence";
            }
        } else {
            if (regimenType.equals("first")) {
                regimenTypes = regimenTypeRepository.firstRegimentType();
            }
            if (regimenType.equals("second")) {
                regimenTypes = regimenTypeRepository.secondRegimentType();
            }
            if (regimenType.equals("commence")) {
                regimenTypes = regimenTypeRepository.commenceRegimentType();
            }
        }
        List<Regimen> regimen;
        if (!regimenTypes.isEmpty()) {
            regimen = regimenRepository.findByRegimenTypeIn(regimenTypes);
        } else {
            regimen = regimenRepository.findAll();
        }
        Collections.sort(regimen);
        Map<String, Object> response = new HashMap<>();
        response.put("currpage", 1);
        response.put("regimenList", regimen);
        return ResponseEntity.ok(response);
    }

}
