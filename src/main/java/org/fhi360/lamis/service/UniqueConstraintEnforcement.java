package org.fhi360.lamis.service;

import org.fhi360.lamis.config.ContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.*;
@Component
public class UniqueConstraintEnforcement {
    private final JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final TransactionTemplate transactionTemplate = ContextProvider.getBean(TransactionTemplate.class);
    private final Logger LOG = LoggerFactory.getLogger(UniqueConstraintEnforcement.class);

    private List<String> tables = Arrays.asList("adherehistory", "adrhistory", "anc", "chroniccare", "clinic",
            "delivery", "devolve", "dmscreenhistory", "eac", "encounter", "laboratory", "maternalfollowup",
            "motherinformation", "nigqual", "oihistory", "partnerinformation", "pharmacy", "regimenhistory",
            "statushistory", "tbscreenhistory");

    public UniqueConstraintEnforcement() {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ContextProvider.getBean(DataSource.class));
    }

    public void setupConstraints() {
        String queries = "alter table PATIENT add constraint if not exists ux_facility_hospital_num unique (HOSPITAL_NUM, FACILITY_ID);" +
                "alter table PHARMACY add constraint if not exists ux_facility_pharmacy unique (PATIENT_ID, REGIMENDRUG_ID, FACILITY_ID, DATE_VISIT);" +
                "alter table CLINIC add constraint if not exists ux_facility_clinic unique (PATIENT_ID, FACILITY_ID, DATE_VISIT);" +
                "alter table LABORATORY add constraint if not exists ux_facility_laboratory unique (PATIENT_ID, FACILITY_ID, DATE_REPORTED, LABTEST_ID);" +
                "alter table DELIVERY add constraint if not exists ux_facility_delivery unique (PATIENT_ID, FACILITY_ID, DATE_DELIVERY);" +
                "alter table EAC add constraint if not exists ux_facility_eac unique (PATIENT_ID, FACILITY_ID, DATE_EAC1);" +
                "alter table STATUSHISTORY add constraint if not exists ux_facility_status unique (PATIENT_ID, FACILITY_ID, DATE_CURRENT_STATUS, CURRENT_STATUS);" +
                "alter table REGIMENHISTORY add constraint if not exists ux_facility_regimen unique (PATIENT_ID, FACILITY_ID, DATE_VISIT, REGIMENTYPE, REGIMEN)";
        transactionTemplate.execute(status -> {
            Arrays.asList(queries.split(";"))
                    .forEach(query -> {
                        LOG.info("Executing constraint: {}", query);
                        jdbcTemplate.execute(query);
                    });
            return null;
        });
    }

    public List<Long> removeDuplicate(Duplicate duplicate) {
        List<Long> deleteIds = new ArrayList<>();
        LOG.info("Duplicate: {}", duplicate);
        List<Long> patientIds = jdbcTemplate.queryForList("select PATIENT_ID from PATIENT where FACILITY_ID = ? and HOSPITAL_NUM = ?",
                Long.class, duplicate.getFacilityId(), duplicate.getHospitalNum());
        Long primaryId = patientIds.get(0);
        transactionTemplate.execute(status1 -> {
            patientIds.stream()
                    .filter(id -> !id.equals(primaryId))
                    .forEach(id -> {
                        deleteIds.add(id);
                        /*
                          Migrate the following tables rows to the primary id: AdhereHistory, ADRHistory, ANC, ChronicCare,
                          Clinic, Delivery, Devolve, DMScreenHistory, EAC, Encounter, Laboratory, MaternalFollowup,
                          MotherInformation, Nigqual, OIHistory, PartnerInformation, Pharmacy, RegimenHistory, StatusHistory
                          and TBScreenHistory
                         */

                        tables.forEach(table -> {
                            LOG.info("{}", String.format("update %s set patient_id = %s where patient_id = %s and facility_id = %s",
                                    table, primaryId, id, duplicate.getFacilityId()));
                            String tableQuery = String.format("update %s set patient_id = %s where patient_id = %s and facility_id = %s",
                                    table, primaryId, id, duplicate.getFacilityId());
                            jdbcTemplate.execute(tableQuery);
                        });
                    });

            //Remove duplicates from Pharmacy
            String pharmacyQuery = "select FACILITY_ID, PATIENT_ID, REGIMENDRUG_ID, DATE_VISIT, count(*) as count from PHARMACY " +
                    " where FACILITY_ID = ? and PATIENT_ID = ? group by FACILITY_ID, PATIENT_ID, REGIMENDRUG_ID, DATE_VISIT having count > 1";
            List<List<Long>> ids = jdbcTemplate.query(pharmacyQuery, (rs, i) -> {
                Date date = rs.getDate("date_visit");
                Long regimenDrugId = rs.getLong("regimendrug_id");
                return jdbcTemplate.queryForList("select PHARMACY_ID from PHARMACY where FACILITY_ID = ? " +
                        "and PATIENT_ID = ? and DATE_VISIT = ? and regimendrug_id = ?", Long.class, duplicate.getFacilityId(), primaryId, date, regimenDrugId);
            }, duplicate.getFacilityId(), primaryId);
            ids.forEach(ids1 -> {
                if (ids1.size() > 0) {
                    ids1.remove(0);
                }
                ids1.forEach(id -> {
                            LOG.info("Deleting Pharmacy {}, facility {}", id, duplicate.getFacilityId());
                            jdbcTemplate.update("delete from PHARMACY where PHARMACY_ID = ? and PATIENT_ID = ? and FACILITY_ID = ?",
                                    id, primaryId, duplicate.getFacilityId());
                        }
                );
            });

            //Remove duplicates from Clinic
            String clinicQuery = "select FACILITY_ID, PATIENT_ID, DATE_VISIT, count(*) as count from CLINIC " +
                    " where FACILITY_ID = ? and PATIENT_ID = ? group by FACILITY_ID, PATIENT_ID, DATE_VISIT having count > 1";
            ids = jdbcTemplate.query(clinicQuery, (rs, i) -> {
                Date date = rs.getDate("date_visit");
                return jdbcTemplate.queryForList("select CLINIC_ID from CLINIC where FACILITY_ID = ? " +
                        "and PATIENT_ID = ? and DATE_VISIT = ?", Long.class, duplicate.getFacilityId(), primaryId, date);
            }, duplicate.getFacilityId(), primaryId);
            ids.forEach(ids1 -> {
                if (ids1.size() > 0) {
                    ids1.remove(0);
                }
                ids1.forEach(id -> {
                            LOG.info("Deleting Clinic {}, facility {}", id, duplicate.getFacilityId());
                            jdbcTemplate.update("delete from CLINIC where CLINIC_ID = ? and PATIENT_ID = ? and FACILITY_ID = ?",
                                    id, primaryId, duplicate.getFacilityId());
                        }
                );
            });

            //Remove duplicates from Laboratory
            String labQuery = "select FACILITY_ID, PATIENT_ID, LABTEST_ID, DATE_REPORTED, count(*) as count from LABORATORY " +
                    " where FACILITY_ID = ? and PATIENT_ID = ? group by FACILITY_ID, PATIENT_ID, DATE_REPORTED having count > 1";
            ids = jdbcTemplate.query(labQuery, (rs, i) -> {
                Date date = rs.getDate("date_reported");
                Long labTestId = rs.getLong("LABTEST_ID");
                return jdbcTemplate.queryForList("select LABORATORY_ID from LABORATORY where FACILITY_ID = ? " +
                        "and PATIENT_ID = ? and DATE_REPORTED = ? and LABTEST_ID = ?", Long.class, duplicate.getFacilityId(), primaryId, date, labTestId);
            }, duplicate.getFacilityId(), primaryId);
            ids.forEach(ids1 -> {
                if (ids1.size() > 0) {
                    ids1.remove(0);
                }
                ids1.forEach(id -> {
                            LOG.info("Deleting Laboratory {}, facility {}", id, duplicate.getFacilityId());
                            jdbcTemplate.update("delete from LABORATORY where LABORATORY_ID = ? and PATIENT_ID = ? and FACILITY_ID = ?",
                                    id, primaryId, duplicate.getFacilityId());
                        }
                );
            });

            //Remove duplicates from Delivery
            String deliveryQuery = "select FACILITY_ID, PATIENT_ID, DATE_DELIVERY, count(*) as count from DELIVERY " +
                    " where FACILITY_ID = ? and PATIENT_ID = ? group by FACILITY_ID, PATIENT_ID, DATE_DELIVERY having count > 1";
            ids = jdbcTemplate.query(deliveryQuery, (rs, i) -> {
                Date date = rs.getDate("date_delivery");
                return jdbcTemplate.queryForList("select DELIVERY_ID from DELIVERY where FACILITY_ID = ? " +
                        "and PATIENT_ID = ? and DATE_DELIVERY = ?", Long.class, duplicate.getFacilityId(), primaryId, date);
            }, duplicate.getFacilityId(), primaryId);
            ids.forEach(ids1 -> {
                if (ids1.size() > 0) {
                    ids1.remove(0);
                }
                ids1.forEach(id -> {
                            LOG.info("Deleting Delivery {}, facility {}", id, duplicate.getFacilityId());
                            jdbcTemplate.update("delete from DELIVERY where DELIVERY_ID = ? and PATIENT_ID = ? and FACILITY_ID = ?",
                                    id, primaryId, duplicate.getFacilityId());
                        }
                );
            });

            //Remove duplicates from EAC
            String eacQuery = "select FACILITY_ID, PATIENT_ID, DATE_EAC1, count(*) as count from EAC " +
                    " where FACILITY_ID = ? and PATIENT_ID = ? group by FACILITY_ID, PATIENT_ID, DATE_EAC1 having count > 1";
            ids = jdbcTemplate.query(eacQuery, (rs, i) -> {
                Date date = rs.getDate("date_eac1");
                return jdbcTemplate.queryForList("select EAC_ID from EAC where FACILITY_ID = ? " +
                        "and PATIENT_ID = ? and DATE_EAC1 = ?", Long.class, duplicate.getFacilityId(), primaryId, date);
            }, duplicate.getFacilityId(), primaryId);
            ids.forEach(ids1 -> {
                if (ids1.size() > 0) {
                    ids1.remove(0);
                }
                ids1.forEach(id -> {
                            LOG.info("Deleting EAC {}, facility {}", id, duplicate.getFacilityId());
                            jdbcTemplate.update("delete from EAC where EAC_ID = ? and PATIENT_ID = ? and FACILITY_ID = ?",
                                    id, primaryId, duplicate.getFacilityId());
                        }
                );
            });

            //Remove duplicates from Status History
            String statusQuery = "select FACILITY_ID, PATIENT_ID, DATE_CURRENT_STATUS, CURRENT_STATUS, count(*) as count from STATUSHISTORY " +
                    " where FACILITY_ID = ? and PATIENT_ID = ? group by FACILITY_ID, PATIENT_ID, DATE_CURRENT_STATUS, CURRENT_STATUS having count > 1";
            ids = jdbcTemplate.query(statusQuery, (rs, i) -> {
                Date date = rs.getDate("date_current_status");
                String status = rs.getString("current_status");
                return jdbcTemplate.queryForList("select HISTORY_ID from STATUSHISTORY where FACILITY_ID = ? " +
                        "and PATIENT_ID = ? and DATE_CURRENT_STATUS = ? and CURRENT_STATUS = ?", Long.class, duplicate.getFacilityId(), primaryId, date, status);
            }, duplicate.getFacilityId(), primaryId);
            ids.forEach(ids1 -> {
                if (ids1.size() > 0) {
                    ids1.remove(0);
                }
                ids1.forEach(id -> {
                            LOG.info("Deleting Status History {}, facility {}", id, duplicate.getFacilityId());
                            jdbcTemplate.update("delete from STATUSHISTORY where HISTORY_ID = ? and PATIENT_ID = ? and FACILITY_ID = ?",
                                    id, primaryId, duplicate.getFacilityId());
                        }
                );
            });

            //Remove duplicates from Regimen History
            String regimeQuery = "select FACILITY_ID, PATIENT_ID, DATE_VISIT, REGIMEN, REGIMENTYPE, count(*) as count from REGIMENHISTORY " +
                    " where FACILITY_ID = ? and PATIENT_ID = ? group by FACILITY_ID, PATIENT_ID, DATE_VISIT, REGIMEN, REGIMENTYPE having count > 1";
            ids = jdbcTemplate.query(regimeQuery, (rs, i) -> {
                Date date = rs.getDate("date_visit");
                String regimen = rs.getString("regimen");
                String regimenType = rs.getString("regimenType");
                return jdbcTemplate.queryForList("select HISTORY_ID from REGIMENHISTORY where FACILITY_ID = ? " +
                                "and PATIENT_ID = ? and DATE_VISIT = ? and REGIMEN = ? and REGIMENTYPE = ?", Long.class, duplicate.getFacilityId(),
                        primaryId, date, regimen, regimenType);
            }, duplicate.getFacilityId(), primaryId);
            ids.forEach(ids1 -> {
                if (ids1.size() > 0) {
                    ids1.remove(0);
                }
                ids1.forEach(id -> {
                            LOG.info("Deleting Regimen History {}, facility {}", id, duplicate.getFacilityId());
                            jdbcTemplate.update("delete from REGIMENHISTORY where HISTORY_ID = ? and PATIENT_ID = ? and FACILITY_ID = ?",
                                    id, primaryId, duplicate.getFacilityId());
                        }
                );
            });
            return null;
        });
        return deleteIds;
    }

    public void deleteOrphanedRecords() {
        //Remove orphaned records
        tables.forEach(table -> {
            List<Long> ids = jdbcTemplate.queryForList(String.format("select %s from %s t inner join patient p on " +
                    "t.patient_id = p.patient_id where t.facility_id != p.facility_id", getIdColumnName(table), table), Long.class);
            String orphanedRecords = String.format("delete t from %s t where %s in (:ids);", table, getIdColumnName(table));
            Map<String, Object> params = new HashMap<>();
            params.put("ids", ids);
            if (!ids.isEmpty()) {
                transactionTemplate.execute(status -> {
                    namedParameterJdbcTemplate.update(orphanedRecords, params);
                    return null;
                });
            }
        });
    }

    public void removeDuplicates() {
        String query = "select FACILITY_ID as facilityId, HOSPITAL_NUM as hospitalNum, count(*) count from PATIENT " +
                "group by FACILITY_ID, HOSPITAL_NUM having count > 1 order by 1";
        List<Duplicate> duplicates = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Duplicate.class));
        duplicates.forEach(duplicate -> {
            List<Long> deletes = removeDuplicate(duplicate);
            LOG.info("Deleting Patient {}, facility {}", deletes, duplicate.getFacilityId());
            Map<String, Object> params = new HashMap<>();
            params.put("ids", deletes);
            params.put("facility", duplicate.getFacilityId());
            transactionTemplate.execute(status -> {
                namedParameterJdbcTemplate.update("delete from patient where patient_id in (:ids) and facility_id = :facility", params);
                return null;
            });
        });
    }

    private String getIdColumnName(String table) {
        String entityId = table + "_id";
        if (table.contains("history")) {
            entityId = "history_id";
        }
        return entityId;
    }


    //@PostConstruct
    public void setup() {
        //deleteOrphanedRecords();
        //removeDuplicates();
        //setupConstraints();
    }
}
