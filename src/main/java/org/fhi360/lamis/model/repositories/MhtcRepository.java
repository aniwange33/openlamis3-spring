package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.CommunityPharmacy;
import org.fhi360.lamis.model.Mhtc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MhtcRepository extends JpaRepository<Mhtc, Long> {
    Mhtc findByAndCommunityPharmacyAndMonthAndYear(CommunityPharmacy communityPharmacy,int month, int year);
}
