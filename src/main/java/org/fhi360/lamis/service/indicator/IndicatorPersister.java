
package org.fhi360.lamis.service.indicator;

import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.model.*;
import org.fhi360.lamis.model.repositories.IndicatorValueRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndicatorPersister {
    private final IndicatorValueRepository indicatorValueRepository;

    public IndicatorPersister(IndicatorValueRepository indicatorValueRepository) {
        this.indicatorValueRepository = indicatorValueRepository;
    }

    public void persist(long dataElementId, long categoryId, long stateId, long lgaId, long facilityId, int value, String reportDate) {
        try {
            IndicatorValue indicator = new IndicatorValue();
            indicator.setDataElementId(dataElementId);
            indicator.setCategoryId(categoryId);
            State state = new State();
            state.setId(stateId);
            indicator.setState(state);
            Lga lga = new Lga();
            lga.setId(lgaId);
            indicator.setLga(lga);
            Facility facility = new Facility();
            facility.setId(facilityId);
            indicator.setFacility(facility);
            indicator.setValue(value);
            //indicator.setReportDate();
            long indicatorId = getIndicatorId(dataElementId, categoryId, facilityId, reportDate, "");
            if (indicatorId == 0) {
                indicatorValueRepository.save(indicator);
            } else {
                indicator.setId(indicatorId);
                indicatorValueRepository.save(indicator);
            }
        } catch (Exception e) {
            System.out.println("Exception:::  " + e.getMessage());
        }

    }

    public void persistDhis(long dataElementId, long categoryId, long stateId, long lgaId, long facilityId, String period, int value, String reportingPeriod) {
        try {
            DhisValue indicator = new DhisValue();
            indicator.setDataElementId(dataElementId);
            indicator.setCategoryId(categoryId);
            indicator.setStateId(stateId);
            State state = new State();
            state.setId(stateId);
            Lga lga = new Lga();
            lga.setId(lgaId);
            indicator.setLga(lga);
            Facility facility = new Facility();
            facility.setId(facilityId);
            indicator.setFacility(facility);
            if (reportingPeriod.equalsIgnoreCase("daily")) {
                //    indicator.setDataElementIdDhis(DhisCodeSetResolver.getCode("DATA ELEMENT DR", dataElementId));
            } else {
                //  indicator.setDataElementIdDhis(DhisCodeSetResolver.getCode("DATA ELEMENT WR", dataElementId));
            }

            //    indicator.setCategoryIdDhis(DhisCodeSetResolver.getCode("CATEGORY COMBO", categoryId));
            //    indicator.setFacilityIdDhis(DhisCodeSetResolver.getCode("FACILITY", id));

            indicator.setPeriod(period);
            indicator.setValue(value);

            long indicatorId = getIndicatorId(dataElementId, categoryId, facilityId, period, "dhis");
            if (indicatorId == 0) {
                // DhisvalueDAO.save(indicator);
            } else {
                //  indicator.setDhisvalueId(indicatorId);
                // DhisvalueDAO.update(indicator);
            }

            System.out.println("Indicator persister update completed.");

        } catch (Exception ex) {
            System.out.println("Indicator Persister Exception: " + ex);
        }
    }

    //Get indicatorvalueId
    private long getIndicatorId(long dataElementId, long categoryId, long facilityId, String reportDate, String table) {
        String query = "SELECT indicatorvalue_id AS regimentypeId FROM indicatorvalue WHERE data_element_id = " + dataElementId +
                "  AND category_id = " + categoryId + " AND facility_id = " + facilityId + " AND report_date = '" + reportDate + "'";
        if (table.equalsIgnoreCase("dhis"))
            query = "SELECT dhisvalue_id AS regimentypeId FROM dhisvalue WHERE data_element_id = " + dataElementId + "  AND category_id = " + categoryId + " AND facility_id = " + facilityId + " AND period = '" + reportDate + "'";
        long id = 0;
        List<Long> ids = ContextProvider.getBean(JdbcTemplate.class).queryForList(query, Long.class);
        if (!ids.isEmpty()) {
            return ids.get(0);
        }
        return id;
    }
}
