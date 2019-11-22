package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
