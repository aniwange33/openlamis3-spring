package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
