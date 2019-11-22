package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.Lga;
import org.fhi360.lamis.model.State;
import org.fhi360.lamis.model.repositories.LgaRepository;
import org.fhi360.lamis.model.repositories.StateRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/state")
@Api(tags = "State & Lga", description = " ")
public class StateLgaController {
    private final StateRepository stateRepository;
    private final LgaRepository lgaRepository;

    public StateLgaController(StateRepository stateRepository, LgaRepository lgaRepository) {
        this.stateRepository = stateRepository;
        this.lgaRepository = lgaRepository;
    }

    @GetMapping
    public ResponseEntity retrieveState() {
        List<State> stateList = stateRepository.findAll(Sort.by(Sort.Order.by("name")));
        return ResponseEntity.ok(stateList);
    }

    @GetMapping("/lga/{id}")
    public ResponseEntity retrieveLgaById(@PathVariable Long id) {
        State state = stateRepository.getOne(id);
        List<Lga> lgaList = lgaRepository.findByState(state);
        return ResponseEntity.ok(lgaList);
    }
}
