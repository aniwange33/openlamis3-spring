package org.fhi360.lamis.service;
import org.fhi360.lamis.config.ContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.*;

//@Component
public class ServerIDProvider {
    private static JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);
    private static NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final static TransactionTemplate transactionTemplate = ContextProvider.getBean(TransactionTemplate.class);
    private final static Logger LOG = LoggerFactory.getLogger(ServerIDProvider.class);


    private static List<String> tables = Arrays.asList("adherehistory", "adrhistory", "anc", "chroniccare", "clinic",
            "delivery", "devolve", "dmscreenhistory", "eac", "encounter", "laboratory", "maternalfollowup",
            "motherinformation", "nigqual", "oihistory", "partnerinformation", "pharmacy", "regimenhistory",
            "statushistory", "tbscreenhistory");

    public static Long getPatientServerId(String hospitalNum, final Long facilityId) {
        String query = "select patient_id from patient where " +
                "facility_id = ? and hospital_num = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, hospitalNum);
        return getId(ids);
    }

    public static Long getScreenId(String table, LocalDate dateVisit, String description, String hospitalNum, Long facilityId) {
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        String query = String.format("select HISTORY_ID from %s where PATIENT_ID = ? and DATE_VISIT = ? " +
                "and DESCRIPTION = ?", table);
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId, dateVisit, description);
        return getId(ids);
    }

    public static Long getLaboratoryId(String hospitalNum, LocalDate dateReported, Long labtestid, Long facilityId) {
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        String query = "select laboratory_id from laboratory where PATIENT_ID = ? and DATE_REPORTED = ? and LABTEST_ID = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId, dateReported, labtestid);
        return getId(ids);
    }

    public static Long getPatientDependantId(String table, String hospitalNum, Object dateVisit, Long facilityId) {
        String field = "date_visit";
        switch (table) {
            case "patientcasemanager":
                field = "date_assigned";
                break;
            case "nigqual":
                field = "review_period_id";
                break;
            case "delivery":
                field = "date_delivery";
                break;
            case "devolve":
                field = "date_devolved";
                break;
            case "eac":
                field = "date_eac1";
                break;
        }
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        String query = String.format("select %s from %s d where patient_id = ? and d.%s = ? ",
                getIdColumnName(table), table, field);
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId, dateVisit);
        return getId(ids);
    }

    public static Long getOIHistoryId(String hospitalNum, LocalDate dateVisit, String oi, Long facilityId) {
        String query = "select history_id from oihistory d where PATIENT_ID = ? and d.date_visit = ? and OI = ?";
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId, dateVisit, oi);
        return getId(ids);
    }

    public static Long getHtsId(String clientCode, Long facilityId) {
        String query = "select hts_id from hts where facility_id = ? and client_code = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, clientCode);
        return getId(ids);
    }

    public static Long getIndexcontactId(String indexContactCode, Long facilityId) {
        String query = "select indexcontact_id from indexcontact where facility_id = ? and index_contact_code = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, indexContactCode);
        return getId(ids);
    }

    public static Long getPharmacyId(String hospitalNum, LocalDate dateVisit, Long regimenDrugId, Long facilityId) {
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        String query = "select PHARMACY_ID from PHARMACY where PATIENT_ID = ? and date_visit = ? and REGIMENDRUG_ID = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId, dateVisit, regimenDrugId);
        return getId(ids);
    }

    public static Long getStatusHistoryId(String hospitalNum, LocalDate dateCurrentStatus, String currentStatus, Long facilityId) {
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        String query = "select HISTORY_ID from STATUSHISTORY where PATIENT_ID = ? and DATE_CURRENT_STATUS = ? and CURRENT_STATUS = ? ";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId, dateCurrentStatus, currentStatus);
        return getId(ids);
    }

    public static Long getRegimenId(String hospitalNum, LocalDate dateVisit, String regimenType, String regimen, Long facilityId) {
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        String query = "select HISTORY_ID from REGIMENHISTORY d where PATIENT_ID = ? and d.date_visit = ? and d.REGIMENTYPE = ? and d.REGIMEN = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId, dateVisit, regimenType, regimen);
        return getId(ids);
    }

    public static Long getSpecimenId(String labNo, Long facilityId) {
        String query = "select SPECIMEN_ID from SPECIMEN where FACILITY_ID = ? and LABNO = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, labNo);
        return getId(ids);
    }

    public static Long getAncId(String ancNum, Long facilityId) {
        String query = "select ANC_ID from ANC where FACILITY_ID = ? and anc_num = ? ";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, ancNum);
        if (ids.size() > 0 && ids.get(0) != null) {
            return ids.get(0);
        }
        return null;
    }

    public static Long getEidId(String labNo, Long facilityId) {
        String query = "select EID_ID from EID where FACILITY_ID = ? and LABNO = ? ";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, labNo);
        return getId(ids);
    }

    public static Long getChildFollowupChildId(String referenceNum, Long facilityId) {
        String query = "select c.CHILD_ID from CHILDFOLLOWUP cf inner join CHILD c on cf.CHILD_ID = c.CHILD_ID" +
                " where c.FACILITY_ID = ? and REFERENCE_NUM";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, referenceNum);
        if (ids.size() > 0 && ids.get(0) != null) {
            return ids.get(0);
        }
        return null;
    }

    public static Long getChildFollowupId(String referenceNum, LocalDate dateVisit, Long facilityId) {
        String query = "select CHILD_ID from CHILD where REFERENCE_NUM = ? and FACILITY_ID = ?";
        Long childId = jdbcTemplate.queryForObject(query, Long.class, referenceNum, facilityId);
        query = "select CHILDFOLLOWUP_ID from CHILDFOLLOWUP cf where CHILD_ID = ? and DATE_VISIT = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, childId, dateVisit);
        return getId(ids);
    }

    public static Long getChildMotherInformationId(String hospitalNum, Long facilityId) {
        String query = "select MOTHERINFORMATION_ID from MOTHERINFORMATION where FACILITY_ID = ? and " +
                "HOSPITAL_NUM = ? ";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, hospitalNum);
        if (ids.size() > 0 && ids.get(0) != null) {
            return ids.get(0);
        }
        return null;
    }

    public static Long getChildId(String referenceNum, Long facilityId) {
        String query = "select CHILD_ID from CHILD where FACILITY_ID = ? and reference_num = ? ";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, referenceNum);
        return getId(ids);
    }

    public static Long getMotherInformationId(String hospitalNum, Long facilityId) {
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        String query = "select MOTHERINFORMATION_ID from MOTHERINFORMATION where PATIENT_ID = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId);
        return getId(ids);
    }

    public static Long getPartnerInformationId(String hospitalNum, Long facilityId) {
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        String query = "select PARTNERINFORMATION_ID from PARTNERINFORMATION p  where p.PATIENT_ID = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId);
        return getId(ids);
    }

    public static Long getCaseManagerId(Long localId, Long facilityId) {
        String query = "select CASEMANAGER_ID from casemanager where FACILITY_ID = ? and LOCAL_ID = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, localId);
        if (ids.size() > 0 && ids.get(0) != null) {
            return ids.get(0);
        }
        return null;
    }

    private static String getIdColumnName(String table) {
        String entityId = table + "_id";
        if (table.contains("history")) {
            entityId = "history_id";
        }
        return entityId;
    }

    private static Long getId(List<Long> ids) {
        if (ids.size() > 0) {
            return ids.get(0);
        }
        return null;
    }

    private static void setupConstraints() {
        String queries = "alter table PATIENT add constraint ux_facility_hospital_num unique (HOSPITAL_NUM, FACILITY_ID);" +
                "alter table PHARMACY add constraint ux_facility_pharmacy unique (PATIENT_ID, REGIMENDRUG_ID, FACILITY_ID, DATE_VISIT);" +
                "alter table CLINIC add constraint ux_facility_clinic unique (PATIENT_ID, FACILITY_ID, DATE_VISIT);" +
                "alter table LABORATORY add constraint ux_facility_laboratory unique (PATIENT_ID, FACILITY_ID, DATE_REPORTED);" +
                "alter table DELIVERY add constraint ux_facility_delivery unique (PATIENT_ID, FACILITY_ID, DATE_DELIVERY);" +
                "alter table EAC add constraint ux_facility_eac unique (PATIENT_ID, FACILITY_ID, DATE_EAC1);" +
                "alter table STATUSHISTORY add constraint ux_facility_status unique (PATIENT_ID, FACILITY_ID, DATE_CURRENT_STATUS, CURRENT_STATUS);" +
                "alter table REGIMENHISTORY add constraint ux_facility_regimen unique (PATIENT_ID, FACILITY_ID, DATE_VISIT, REGIMENTYPE, REGIMEN)";
        Arrays.asList(queries.split(";"))
                .forEach(query -> {
                    System.out.println(String.format("Executing constraint: %s", query));
                    transactionTemplate.execute(status -> {
                        try {
                            jdbcTemplate.execute(query);
                        } catch (Exception ignored) {
                        }
                        return null;
                    });
                });
    }

    private static List<Long> removeDuplicate(org.fhi360.lamis.service.Duplicate duplicate) {
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
                          Migrate the following tables rows to the primary regimentypeId: AdhereHistory, ADRHistory, ANC, ChronicCare,
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

    private static void removeRecordDuplicate() {
        //Remove duplicates from Pharmacy
        String pharmacyQuery = "select FACILITY_ID, PATIENT_ID, REGIMENDRUG_ID, DATE_VISIT, count(*) as count from PHARMACY " +
                "group by FACILITY_ID, PATIENT_ID, REGIMENDRUG_ID, DATE_VISIT having count > 1";
        List<List<Long>> ids = jdbcTemplate.query(pharmacyQuery, (rs, i) -> {
            Long facilityId = rs.getLong("facility_id");
            Long patientId = rs.getLong("patient_id");
            Date date = rs.getDate("date_visit");
            Long regimenDrugId = rs.getLong("regimendrug_id");
            return jdbcTemplate.queryForList("select PHARMACY_ID from PHARMACY where FACILITY_ID = ? " +
                    "and PATIENT_ID = ? and DATE_VISIT = ? and regimendrug_id = ?", Long.class, facilityId, patientId, date, regimenDrugId);
        });
        ids.forEach(ids1 -> {
            if (ids1.size() > 0) {
                ids1.remove(0);
            }
            ids1.forEach(id -> transactionTemplate.execute(status -> {
                        jdbcTemplate.update("delete from PHARMACY where PHARMACY_ID = ?", id);
                        return null;
                    })
            );
        });

        //Remove duplicates from Clinic
        String clinicQuery = "select FACILITY_ID, PATIENT_ID, DATE_VISIT, count(*) as count from CLINIC " +
                "group by FACILITY_ID, PATIENT_ID, DATE_VISIT having count > 1";
        ids = jdbcTemplate.query(clinicQuery, (rs, i) -> {
            Long facilityId = rs.getLong("facility_id");
            Long patientId = rs.getLong("patient_id");
            Date date = rs.getDate("date_visit");
            return jdbcTemplate.queryForList("select CLINIC_ID from CLINIC where FACILITY_ID = ? " +
                    "and PATIENT_ID = ? and DATE_VISIT = ?", Long.class, facilityId, patientId, date);
        });
        ids.forEach(ids1 -> {
            if (ids1.size() > 0) {
                ids1.remove(0);
            }
            ids1.forEach(id -> transactionTemplate.execute(status -> {
                        jdbcTemplate.update("delete from CLINIC where CLINIC_ID = ? ", id);
                        return null;
                    })
            );
        });

        //Remove duplicates from Laboratory
        String labQuery = "select FACILITY_ID, PATIENT_ID, LABTEST_ID, DATE_REPORTED, count(*) as count from LABORATORY " +
                "group by FACILITY_ID, PATIENT_ID, DATE_REPORTED having count > 1";
        ids = jdbcTemplate.query(labQuery, (rs, i) -> {
            Long facilityId = rs.getLong("facility_id");
            Long patientId = rs.getLong("patient_id");
            Date date = rs.getDate("date_reported");
            Long labTestId = rs.getLong("LABTEST_ID");
            return jdbcTemplate.queryForList("select LABORATORY_ID from LABORATORY where FACILITY_ID = ? " +
                    "and PATIENT_ID = ? and DATE_REPORTED = ? and LABTEST_ID = ?", Long.class, facilityId, patientId, date, labTestId);
        });
        ids.forEach(ids1 -> {
            if (ids1.size() > 0) {
                ids1.remove(0);
            }
            ids1.forEach(id -> transactionTemplate.execute(status -> {
                        jdbcTemplate.update("delete from LABORATORY where LABORATORY_ID = ?", id);
                        return null;
                    })
            );
        });

        //Remove duplicates from Delivery
        String deliveryQuery = "select FACILITY_ID, PATIENT_ID, DATE_DELIVERY, count(*) as count from DELIVERY " +
                "group by FACILITY_ID, PATIENT_ID, DATE_DELIVERY having count > 1";
        ids = jdbcTemplate.query(deliveryQuery, (rs, i) -> {
            Long facilityId = rs.getLong("facility_id");
            Long patientId = rs.getLong("patient_id");
            Date date = rs.getDate("date_delivery");
            return jdbcTemplate.queryForList("select DELIVERY_ID from DELIVERY where FACILITY_ID = ? " +
                    "and PATIENT_ID = ? and DATE_DELIVERY = ?", Long.class, facilityId, patientId, date);
        });
        ids.forEach(ids1 -> {
            if (ids1.size() > 0) {
                ids1.remove(0);
            }
            ids1.forEach(id -> transactionTemplate.execute(status -> {
                        jdbcTemplate.update("delete from DELIVERY where DELIVERY_ID = ?", id);
                        return null;
                    })
            );
        });

        //Remove duplicates from EAC
        String eacQuery = "select FACILITY_ID, PATIENT_ID, DATE_EAC1, count(*) as count from EAC " +
                " group by FACILITY_ID, PATIENT_ID, DATE_EAC1 having count > 1";
        ids = jdbcTemplate.query(eacQuery, (rs, i) -> {
            Long facilityId = rs.getLong("facility_id");
            Long patientId = rs.getLong("patient_id");
            Date date = rs.getDate("date_eac1");
            return jdbcTemplate.queryForList("select EAC_ID from EAC where FACILITY_ID = ? " +
                    "and PATIENT_ID = ? and DATE_EAC1 = ?", Long.class, facilityId, patientId, date);
        });
        ids.forEach(ids1 -> {
            if (ids1.size() > 0) {
                ids1.remove(0);
            }
            ids1.forEach(id -> transactionTemplate.execute(status -> {
                        jdbcTemplate.update("delete from EAC where EAC_ID = ?", id);
                        return null;
                    })
            );
        });

        //Remove duplicates from Status History
        String statusQuery = "select FACILITY_ID, PATIENT_ID, DATE_CURRENT_STATUS, CURRENT_STATUS, count(*) as count from STATUSHISTORY " +
                "group by FACILITY_ID, PATIENT_ID, DATE_CURRENT_STATUS, CURRENT_STATUS having count > 1";
        ids = jdbcTemplate.query(statusQuery, (rs, i) -> {
            Long facilityId = rs.getLong("facility_id");
            Long patientId = rs.getLong("patient_id");
            Date date = rs.getDate("date_current_status");
            String status = rs.getString("current_status");
            return jdbcTemplate.queryForList("select HISTORY_ID from STATUSHISTORY where FACILITY_ID = ? " +
                    "and PATIENT_ID = ? and DATE_CURRENT_STATUS = ? and CURRENT_STATUS = ?", Long.class, facilityId, patientId, date, status);
        });
        ids.forEach(ids1 -> {
            if (ids1.size() > 0) {
                ids1.remove(0);
            }
            ids1.forEach(id -> transactionTemplate.execute(status -> {
                        jdbcTemplate.update("delete from STATUSHISTORY where HISTORY_ID = ?", id);
                        return null;
                    })
            );
        });

        //Remove duplicates from Regimen History
        String regimeQuery = "select FACILITY_ID, PATIENT_ID, DATE_VISIT, REGIMEN, REGIMENTYPE, count(*) as count from REGIMENHISTORY " +
                "group by FACILITY_ID, PATIENT_ID, DATE_VISIT, REGIMEN, REGIMENTYPE having count > 1";
        ids = jdbcTemplate.query(regimeQuery, (rs, i) -> {
            Long facilityId = rs.getLong("facility_id");
            Long patientId = rs.getLong("patient_id");
            Date date = rs.getDate("date_visit");
            String regimen = rs.getString("regimen");
            String regimenType = rs.getString("regimenType");
            return jdbcTemplate.queryForList("select HISTORY_ID from REGIMENHISTORY where FACILITY_ID = ? " +
                            "and PATIENT_ID = ? and DATE_VISIT = ? and REGIMEN = ? and REGIMENTYPE = ?", Long.class, facilityId,
                    patientId, date, regimen, regimenType);
        });
        ids.forEach(ids1 -> {
            if (ids1.size() > 0) {
                ids1.remove(0);
            }
            ids1.forEach(id -> transactionTemplate.execute(status -> {
                        jdbcTemplate.update("delete from REGIMENHISTORY where HISTORY_ID = ?", id);
                        return null;
                    })
            );
        });
    }

    private static void removeDuplicates() {
        String query = "select FACILITY_ID as facilityId, HOSPITAL_NUM as hospitalNum, count(*) count from PATIENT " +
                "group by FACILITY_ID, HOSPITAL_NUM having count > 1 order by 1";
        List<org.fhi360.lamis.service.Duplicate> duplicates = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(org.fhi360.lamis.service.Duplicate.class));
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

    private static void deleteOrphanedRecords() {
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

    //@PostConstruct
    static {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ContextProvider.getBean(DataSource.class));
        deleteOrphanedRecords();
        removeDuplicates();
        removeRecordDuplicate();
        setupConstraints();
    }
}
