package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Lga;
import org.fhi360.lamis.model.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LgaRepository extends JpaRepository<Lga, Long> {
    List<Lga> findByState(State state);
}
