package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
}
