package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository  extends JpaRepository<Message, Long> {
}
