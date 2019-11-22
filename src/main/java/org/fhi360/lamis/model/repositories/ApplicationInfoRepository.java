package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.ApplicationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationInfoRepository extends JpaRepository<ApplicationInfo, Long> {
}
