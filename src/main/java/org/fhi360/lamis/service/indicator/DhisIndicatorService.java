/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.indicator;

import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.DateUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

import static org.fhi360.lamis.utility.StringUtil.isInteger;

/**
 * @author user10
 */
@Component
public class DhisIndicatorService {
    private final JdbcTemplate jdbcTemplate;
    private final IndicatorPersister indicatorPersister;
    private long stateId;
    private long lgaId;
    private int dataElementId = 0;
    private int agem1, agem2, agef1, agef2;

    //private static final Log log = LogFactory.getLog(DhisIndicatorService.class);
    public DhisIndicatorService(JdbcTemplate jdbcTemplate, IndicatorPersister indicatorPersister) {
        this.jdbcTemplate = jdbcTemplate;
        this.indicatorPersister = indicatorPersister;
    }

    public void process(long facilityId, Date reportingDate, String dbSuffix) {

        try {
            getStateId(facilityId);

            String reportDate = DateUtil.parseDateToString(reportingDate, "yyyy-MM-dd");

            //Create a view containing patient whose next clinic appointment is the reporting date
            executeUpdate("DROP VIEW IF EXISTS clinic_appointment_" + dbSuffix);
            executeUpdate("CREATE VIEW clinic_appointment_" + dbSuffix + " AS SELECT c.patient_id, c.next_appointment FROM clinic c WHERE c.facility_id = " + facilityId + " AND c.next_appointment = '" + reportDate + "'");


            //Create a view containing patient whose next clinic appointment is reporting date but clinic visit date that is the same as the reporting date
            executeUpdate("DROP VIEW IF EXISTS clinic_defaulter_" + dbSuffix);
            executeUpdate("CREATE VIEW clinic_defaulter_" + dbSuffix + " AS SELECT ca.patient_id, ca.next_appointment FROM clinic_appointment_" + dbSuffix + "  ca JOIN clinic c  ON ca.patient_id = c.patient_id WHERE ca.next_appointment != c.date_visit");

            //Create a view containing patient whose next refill appointment is the reporting date
            executeUpdate("DROP VIEW IF EXISTS refill_appointment_" + dbSuffix);
            executeUpdate("CREATE VIEW refill_appointment_" + dbSuffix + " AS SELECT m.patient_id, m.next_appointment FROM pharmacy m WHERE m.facility_id = " + facilityId + " AND m.next_appointment = '" + reportDate + "' AND m.regimentype_id IN (1, 2, 3, 4, 14)");

            //Create a view containing patient whose next refill appointment is reporting date but refill visit date that is the same as the reporting date
            executeUpdate("DROP VIEW IF EXISTS refill_defaulter_" + dbSuffix);
            executeUpdate("CREATE VIEW refill_defaulter_" + dbSuffix + " AS SELECT ra.patient_id, ra.next_appointment FROM refill_appointment_" + dbSuffix + " ra JOIN pharmacy m  ON ra.patient_id = m.patient_id WHERE ra.next_appointment != m.date_visit AND m.regimentype_id IN (1, 2, 3, 4, 14)");

            executeUpdate("DROP VIEW IF EXISTS inh_" + dbSuffix);
            executeUpdate("CREATE VIEW inh_" + dbSuffix + " AS SELECT m.patient_id, MIN(m.date_visit) AS date_visit FROM pharmacy m WHERE m.facility_id = " + facilityId + " AND m.date_visit <= '" + reportDate + "' AND m.regimen_id = 115 GROUP BY m.patient_id");


            /*
             * ........................................................................................................................
             *  HTS
             * ........................................................................................................................
             */

            System.out.println("DR HTS INDEX_Contacts elicited.....");

            //DR HTS INDEX_Contacts elicited
            initVariables();
            dataElementId = 400;

            String query = "SELECT DISTINCT i.indexcontact_id, i.gender, i.age FROM  indexcontact i "
                    + " JOIN hts h ON i.hts_id = h.hts_id "
                    + " WHERE  h.facility_id = " + facilityId + " AND h.date_visit = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR HTS INDEX_Contacts identified as known positive.....");

            //DR HTS INDEX_Contacts identified as known positive
            initVariables();
            dataElementId = 401;

            query = "SELECT DISTINCT i.indexcontact_id, i.gender, i.age FROM  indexcontact i "
                    + " JOIN hts h ON i.hts_id = h.hts_id "
                    + " WHERE  i.facility_id = " + facilityId + " AND i.hiv_status = '" + Constants.HivTestResult.PREVIOUS_KNOWN + "' AND h.date_visit = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR HTS INDEX_Contacts tested: New negative.....");

            //DR HTS INDEX_Contacts tested: New negative
            initVariables();
            dataElementId = 402;

            query = "SELECT DISTINCT i.indexcontact_id,  i.gender, i.age FROM  indexcontact i "
                    + " JOIN hts h ON i.hts_id = h.hts_id "
                    + " WHERE  i.facility_id = " + facilityId + " AND i.hiv_status = '" + Constants.HivTestResult.NEGATIVE + "' AND h.date_visit = '" + reportDate + "'";
            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR HTS INDEX_Contacts tested: New positive.....");

            //DR HTS INDEX_Contacts tested: New positive
            initVariables();
            dataElementId = 403;

            query = "SELECT DISTINCT i.indexcontact_id, i.gender, i.age FROM  indexcontact i "
                    + " JOIN hts h ON i.hts_id = h.hts_id "
                    + " WHERE  i.facility_id = " + facilityId + " AND i.hiv_status = '" + Constants.HivTestResult.POSITIVE + "' AND h.date_visit = '" + reportDate + "'";
            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Index cases accepted Index Couselling & Testing (ICT).....");

            //DR HTS INDEX_Index cases accepted PNS
            initVariables();
            dataElementId = 404;

            query = "SELECT DISTINCT h.hts_id, h.gender, h.age FROM  hts h  "
                    + " WHERE  h.facility_id = " + facilityId + " AND h.partner_notification = '" + Constants.YesNoOption.YES + "' AND h.date_visit = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });
            //query = "SELECT DISTINCT h.gender, h.age FROM  hts h  WHERE  h.partner_notification = '" + Constants.YesNoOption.YES + "' AND h.date_visit = '" + reportDate + "'";
            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR HTS INDEX_Index cases offered PNS.....");

            //DR HTS INDEX_Index cases offered PNS
            initVariables();
            dataElementId = 405;

            query = "SELECT DISTINCT h.hts_id, h.gender, h.age FROM  hts h  "
                    + " WHERE  h.facility_id = " + facilityId + " AND h.notification_counseling = '" + Constants.YesNoOption.YES + "' AND h.date_visit = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR No. of people tested HIV+ & received results Gen Pop; ANC, L&D, PNC,TB; Index - No. of children tested HIV+, Partners tested HIV+.....");

            //DR No. of people tested HIV+ & received results (Gen Pop; ANC, L&D, PNC,TB; Index - No. of children tested HIV+, Partners tested HIV+)
            initVariables();
            dataElementId = 406;

            query = "SELECT DISTINCT  h.hts_id, h.gender, h.age FROM  hts h  "
                    + " WHERE h.facility_id = " + facilityId + " AND h.hiv_test_result = '" + Constants.HivTestResult.POSITIVE + "' AND  h.date_visit = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15


            /*
             * ........................................................................................................................
             *  Currently on ART
             * ........................................................................................................................
             */

            System.out.println("DR Number of Adult Patients currently on ART transitioned from AZT/3TC/NVP to TLD.....");

            //DR Number of Adult Patients currently on ART transitioned from AZT/3TC/NVP to TLD
            initVariables();
            dataElementId = 407;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " WHERE p.facility_id = " + facilityId + " AND p.current_status IN (" + Constants.ClientStatus.ON_TREATMENT + ")  AND p.date_current_status <= '" + reportDate + "' AND p.date_started IS NOT NULL";

            jdbcTemplate.query(query, resultSet -> {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                if (isPatientActiveTLD(patientId, Constants.LTFU.PEPFAR, reportingDate)) {
                    if (originalRegimen(patientId, "AZT-3TC-NVP")) disaggregate(gender, age);
                }
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15
            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        // female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        // female  >=15


            System.out.println("DR Number of Adult Patients currently on ART transitioned from TDF/3TC/EFV to TLD.....");

            //DR Number of Adult Patients currently on ART transitioned from TDF/3TC/EFV to TLD
            initVariables();
            dataElementId = 408;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient  p "
                    + " WHERE p.facility_id = " + facilityId + " AND p.current_status IN (" + Constants.ClientStatus.ON_TREATMENT + ")  AND p.date_current_status <= '" + reportDate + "' AND p.date_started IS NOT NULL";

            jdbcTemplate.query(query, resultSet -> {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                if (isPatientActiveTLD(patientId, Constants.LTFU.PEPFAR, reportingDate)) {
                    if (originalRegimen(patientId, "TDF-3TC-EFV")) disaggregate(gender, age);
                }
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        // female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        // female  >=15

            System.out.println("DR Number of Adult Patients currently on ART transitioned to TLD.....");

            //DR Number of Adult Patients currently on ART transitioned to TLD
            initVariables();
            dataElementId = 409;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " WHERE  p.facility_id = " + facilityId + " AND p.current_status IN (" + Constants.ClientStatus.ON_TREATMENT + ")  AND p.date_current_status <= '" + reportDate + "' AND p.date_started IS NOT NULL";

            jdbcTemplate.query(query, resultSet -> {

                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                if (isPatientActiveTLD(patientId, Constants.LTFU.PEPFAR, reportingDate)) {
                    disaggregate(gender, age);
                }
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        // female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        // female  >=15

            System.out.println("DR Number of Adult Patients newly initiated on ART with TLD.....");

            //DR Number of Adult Patients newly initiated on ART with TLD
            initVariables();
            dataElementId = 410;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age  FROM patient p "
                    + " WHERE  p.facility_id = " + facilityId + " AND p.date_started = '" + reportDate + "' AND  p.regimen LIKE '%DTG%'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        // female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        // female  >=15

            System.out.println("DR Number of adults and children currently receiving antiretroviral therapy (ART).....");

            //DR Number of adults and children currently receiving antiretroviral therapy (ART)
            initVariables();
            dataElementId = 411;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " WHERE p.facility_id = " + facilityId + " AND p.current_status IN (" + Constants.ClientStatus.ON_TREATMENT + ")  " +
                    "AND p.date_current_status <= '" + reportDate + "' AND p.date_started IS NOT NULL";

            jdbcTemplate.query(query, resultSet -> {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                if (isPatientActive(patientId, Constants.LTFU.PEPFAR, reportingDate)) {
                    disaggregate(gender, age);
                }
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of adults and children newly enrolled on antiretroviral therapy (ART) .....");

            //DR Number of adults and children newly enrolled on antiretroviral therapy (ART)
            initVariables();
            dataElementId = 412;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age " +
                    " FROM patient p  WHERE  p.facility_id = " + facilityId + " AND p.date_started = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15


            /*
             * ........................................................................................................................
             *  Refill appointment and defaulters
             * ........................................................................................................................
             */
            System.out.println("DR Number of ART clients expected to attend the clinic for ART refills.....");

            //DR Number of ART clients expected to attend the clinic for ART refills
            initVariables();
            dataElementId = 413;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN  refill_appointment_" + dbSuffix + " ra ON p.patient_id  = ra.patient_id where p.facility_id = " + facilityId;

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of ART clients who defaulted ARV refill appointments and were tracked.....");

            //DR Number of ART clients who defaulted ARV refill appointments and were tracked
            initVariables();
            dataElementId = 414;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN refill_defaulter_" + dbSuffix + " ra ON  p.patient_id = ra.patient_id JOIN statushistory s ON p.patient_id = s.patient_id  "
                    + " WHERE  p.facility_id = " + facilityId + " AND s.date_tracked BETWEEN ra.next_appointment AND '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of ART clients who defaulted ARV refill appointments and were tracked and returned on the next scheduled appointment.....");

            //DR Number of ART clients who defaulted ARV refill appointments and were tracked and returned on the next scheduled appointment
            initVariables();
            dataElementId = 415;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN refill_defaulter_" + dbSuffix + " ra ON  p.patient_id = ra.patient_id JOIN pharmacy m ON p.patient_id = m.patient_id  JOIN statushistory s ON p.patient_id = s.patient_id   "
                    + " WHERE  p.facility_id = " + facilityId + " AND m.date_visit = s.agreed_date  AND m.date_visit = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of ART clients who defaulted ARV refill appointments and were tracked_Known dead.....");

            //DR Number of ART clients who defaulted ARV refill appointments and were tracked_Known dead
            initVariables();
            dataElementId = 416;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN refill_defaulter_" + dbSuffix + " ra ON  p.patient_id = ra.patient_id JOIN statushistory s ON p.patient_id = s.patient_id  "
                    + " WHERE  p.facility_id = " + facilityId + " AND s.outcome = '" + Constants.TxMlStatus.TX_ML_DIED + "' AND s.date_tracked = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of ART clients who defaulted ARV refill appointments and were tracked_Not reached Client could not be contacted.....");

            //DR Number of ART clients who defaulted ARV refill appointments and were tracked_Not reached (Client could not be contacted)
            initVariables();
            dataElementId = 417;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN refill_defaulter_" + dbSuffix + " ra ON  p.patient_id = ra.patient_id JOIN statushistory s ON p.patient_id = s.patient_id  "
                    + " WHERE  p.facility_id = " + facilityId + " AND s.outcome = '" + Constants.TxMlStatus.TX_ML_NOT_TRACED + "' AND s.date_tracked = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of ART clients who defaulted ARV refill appointments and were tracked_Reached and agreed to return to care.....");

            //DR Number of ART clients who defaulted ARV refill appointments and were tracked_Reached and agreed to return to care
            initVariables();
            dataElementId = 418;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN refill_defaulter_" + dbSuffix + " ra ON  p.patient_id = ra.patient_id JOIN statushistory s ON p.patient_id = s.patient_id  "
                    + " WHERE  p.facility_id = " + facilityId + " AND s.outcome = '" + Constants.TxMlStatus.TX_ML_AGREED_RETURN + "' AND s.date_tracked = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of ART clients who defaulted ARV refill appointments and were tracked_Undocumented patient transfer.....");

            //DR Number of ART clients who defaulted ARV refill appointments and were tracked_Undocumented patient transfer
            initVariables();
            dataElementId = 419;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN refill_defaulter_" + dbSuffix + " ra ON  p.patient_id = ra.patient_id JOIN statushistory s ON p.patient_id = s.patient_id  "
                    + " WHERE  p.facility_id = " + facilityId + " AND s.outcome = '" + Constants.TxMlStatus.TX_ML_TRANSFER + "' AND s.date_tracked = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of ART clients who kept to their ART refill appointment.....");

            //DR Number of ART clients who kept to their ART refill appointment
            initVariables();
            dataElementId = 420;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN refill_appointment_" + dbSuffix + " ra ON  p.patient_id = ra.patient_id JOIN pharmacy m ON p.patient_id = m.patient_id  "
                    + " WHERE  p.facility_id = " + facilityId + " AND m.date_visit = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15


            /*
             * ........................................................................................................................
             *  Clinic appointment and defaulters
             * ........................................................................................................................
             */

            System.out.println("DR Number of ART patients with no clinical contact since their last expected contact clinical contact.....");

            //DR Number of ART patients with no clinical contact since their last expected contact clinical contact
            initVariables();
            dataElementId = 424;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN clinic_defaulter_" + dbSuffix + " ca ON  p.patient_id = ca.patient_id "
                    + " WHERE  p.facility_id = " + facilityId + " AND ca.next_appointment = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of ART patients with no clinical contact since their last expected contact clinical contact_Did not attempt to trace patient.....");

            //DR Number of ART patients with no clinical contact since their last expected contact clinical contact_Did not attempt to trace patient
            initVariables();
            dataElementId = 425;

//            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
//                    + " JOIN clinic_defaulter_" + dbSuffix + " ca ON  p.patient_id = ca.patient_id JOIN statushistory s ON p.patient_id = s.patient_id  "
//                    + " WHERE (s.outcome != '" + Constants.TxMlStatus.TX_ML_DIED + "'  OR s.outcome != '" + Constants.TxMlStatus.TX_ML_AGREED_RETURN + "' OR s.outcome != '" + Constants.TxMlStatus.TX_ML_TRANSFER + "'  OR s.outcome != '" + Constants.TxMlStatus.TX_ML_TRACED + "' OR s.outcome != '" + Constants.TxMlStatus.TX_ML_TRACED + "')";
//

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN clinic_defaulter_" + dbSuffix + " ca ON  p.patient_id = ca.patient_id  "
                    + " WHERE  p.facility_id = " + facilityId + " AND p.patient_id NOT IN (SELECT DISTINCT patient_id FROM statushistory s WHERE date_tracked = '" + reportDate + "')";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of ART patients with no clinical contact since their last expected contact clinical contact_Died (Confirmed).....");

            //DR Number of ART patients with no clinical contact since their last expected contact clinical contact_Died (Confirmed)
            initVariables();
            dataElementId = 426;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN clinic_defaulter_" + dbSuffix + " ca ON  p.patient_id = ca.patient_id JOIN statushistory s ON p.patient_id = s.patient_id  "
                    + " WHERE  p.facility_id = " + facilityId + " AND s.outcome = '" + Constants.TxMlStatus.TX_ML_DIED + "' AND s.date_tracked = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15


            System.out.println("DR Number of ART patients with no clinical contact since their last expected contact clinical contact_Previously undocumented patient transfer (confirmed).....");

            //DR Number of ART patients with no clinical contact since their last expected contact clinical contact_Previously undocumented patient transfer (confirmed)
            initVariables();
            dataElementId = 427;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN clinic_defaulter_" + dbSuffix + " ca ON  p.patient_id = ca.patient_id JOIN statushistory s ON p.patient_id = s.patient_id  "
                    + " WHERE  p.facility_id = " + facilityId + " AND s.outcome = '" + Constants.TxMlStatus.TX_ML_TRANSFER + "' AND s.date_tracked = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15


            System.out.println("DR Number of ART patients with no clinical contact since their last expected contact clinical contact_Traced patient (unable to locate).....");

            //DR Number of ART patients with no clinical contact since their last expected contact clinical contact_Traced patient (unable to locate)
            initVariables();
            dataElementId = 428;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN clinic_defaulter_" + dbSuffix + " ca ON  p.patient_id = ca.patient_id JOIN statushistory s ON p.patient_id = s.patient_id  "
                    + " WHERE  p.facility_id = " + facilityId + " AND s.outcome = '" + Constants.TxMlStatus.TX_ML_NOT_TRACED + "' AND s.date_tracked = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15


            /*
             * ........................................................................................................................
             *  TB
             * ........................................................................................................................
             */

            System.out.println("DR Number of ART patients who started a course of TB preventive therapy (INH)");

            //DR Number of ART patients who started a course of TB preventive therapy (INH)
            initVariables();
            dataElementId = 422;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN inh_" + dbSuffix + " n ON p.patient_id  = n.patient_id "
                    + " WHERE  p.facility_id = " + facilityId + " AND n.date_visit = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of ART patients who were screened for TB .....");

            //DR Number of ART patients who were screened for TB
            initVariables();
            dataElementId = 423;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN clinic c ON p.patient_id  = c.patient_id "
                    + " WHERE  p.facility_id = " + facilityId + " AND c.tb_status IS NOT NULL AND c.date_visit = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of patients initiated on CARC {Community ARV Refill Club} - DMOC.....");

            //DR Number of patients initiated on CARC {Community ARV Refill Club} - DMOC
            initVariables();
            dataElementId = 453;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN devolve d ON p.patient_id  = d.patient_id "
                    + " WHERE  p.facility_id = " + facilityId + " AND d.type_dmoc = '" + Constants.DMOCType.CARC + "' AND d.date_devolved = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of patients initiated on CPARP {Community Pharmacy ARV Refill Program} - DMOC.....");

            //DR Number of patients initiated on CPARP {Community Pharmacy ARV Refill Program} - DMOC
            initVariables();
            dataElementId = 454;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN devolve d ON p.patient_id  = d.patient_id "
                    + " WHERE  p.facility_id = " + facilityId + " AND d.type_dmoc = '" + Constants.DMOCType.CPARP + "' AND d.date_devolved = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of patients initiated on MMD (Multi Month Dispensing) (2 Months - DMOC.....");

            //DR Number of patients initiated on MMD (Multi Month Dispensing) 2 Months - DMOC
            initVariables();
            dataElementId = 455;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN devolve d ON p.patient_id  = d.patient_id "
                    + " WHERE  p.facility_id = " + facilityId + " AND d.type_dmoc = '" + Constants.DMOCType.MMD + "' AND d.arv_dispensed = '60' AND d.date_devolved = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of patients initiated on MMD (Multi Month Dispensing) (3 Months - DMOC.....");

            //DR Number of patients initiated on MMD (Multi Month Dispensing) 3 Months - DMOC
            initVariables();
            dataElementId = 456;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN devolve d ON p.patient_id  = d.patient_id "
                    + " WHERE  p.facility_id = " + facilityId + " AND d.type_dmoc = '" + Constants.DMOCType.MMD + "' AND d.arv_dispensed = '90' AND d.date_devolved = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of patients initiated on MMD (Multi Month Dispensing) (4 Months - DMOC.....");

            //DR Number of patients initiated on MMD (Multi Month Dispensing) 4 Months - DMOC
            initVariables();
            dataElementId = 457;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN devolve d ON p.patient_id  = d.patient_id "
                    + " WHERE  p.facility_id = " + facilityId + " AND d.type_dmoc = '" + Constants.DMOCType.MMD + "' AND d.arv_dispensed = '120' AND d.date_devolved = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of patients initiated on MMD (Multi Month Dispensing) (5 Months - DMOC.....");

            //DR Number of patients initiated on MMD (Multi Month Dispensing) 5 Months - DMOC
            initVariables();
            dataElementId = 458;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN devolve d ON p.patient_id  = d.patient_id "
                    + " WHERE  p.facility_id = " + facilityId + " AND d.type_dmoc = '" + Constants.DMOCType.MMD + "' AND d.arv_dispensed = '150' AND d.date_devolved = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of patients initiated on MMD (Multi Month Dispensing) (6 Months - DMOC.....");

            //DR Number of patients initiated on MMD (Multi Month Dispensing) 6 Months - DMOC
            initVariables();
            dataElementId = 459;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN devolve d ON p.patient_id  = d.patient_id "
                    + " WHERE  p.facility_id = " + facilityId + " AND d.type_dmoc = '" + Constants.DMOCType.MMD + "' AND d.arv_dispensed = '180' AND d.date_devolved = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of patients initiated on MMD (Multi Month Dispensing) - DMOC.....");

            //DR Number of patients initiated on MMD (Multi Month Dispensing) - DMOC
            initVariables();
            dataElementId = 460;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN devolve d ON p.patient_id  = d.patient_id "
                    + " WHERE  p.facility_id = " + facilityId + " AND d.type_dmoc = '" + Constants.DMOCType.MMD + "' AND d.date_devolved = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of patients initiated on MMS {Multi Month Scripting} - DMOC.....");

            //DR Number of patients initiated on MMS {Multi Month Scripting} - DMOC
            initVariables();
            dataElementId = 461;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN devolve d ON p.patient_id  = d.patient_id "
                    + " WHERE  p.facility_id = " + facilityId + " AND d.type_dmoc = '" + Constants.DMOCType.MMS + "' AND d.date_devolved = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of TB patients on ART .....");

            //DR Number of TB patients on ART
            initVariables();
            dataElementId = 469;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN clinic c ON p.patient_id  = c.patient_id "
                    + " WHERE  p.facility_id = " + facilityId + " AND c.tb_status = 'Currently on TB treatment' AND c.date_visit = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        // female  >=15

            System.out.println("DR Number of viral Load results <1000 c/ml.....");

            //DR Number of viral Load results <1000 c/ml
            initVariables();
            dataElementId = 470;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + " JOIN laboratory l ON p.patient_id  = l.patient_id "
                    + " WHERE  p.facility_id = " + facilityId + " AND l.labtest_id = 16 AND l.date_reported = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                //Check if the last viral load before the reporting month is less than 1000
                String query2 = "SELECT resultab FROM laboratory l "
                        + " WHERE  l.facility_id = " + facilityId + " AND l.patient_id = " + patientId + " ORDER BY l.date_reported DESC LIMIT 1";

                jdbcTemplate.query(query2, rs1 -> {
                    String resultab = rs1.getString("resultab");
                    if (isInteger(resultab)) {
                        if (Double.valueOf(resultab) < 1000) {
                            disaggregate(gender, age);
                        }
                    }
                });
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

            System.out.println("DR Number of viral Load results received.....");

            //DR Number of viral Load results received
            initVariables();
            dataElementId = 471;

            query = "SELECT DISTINCT p.patient_id, p.gender, TIMESTAMPDIFF(YEAR, p.date_birth, '" + reportDate + "') AS age FROM patient p "
                    + "JOIN laboratory l ON p.patient_id  = l.patient_id "
                    + " WHERE  p.facility_id = " + facilityId + " AND l.labtest_id = 16 AND l.date_reported = '" + reportDate + "'";

            jdbcTemplate.query(query, resultSet -> {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                disaggregate(gender, age);
            });

            indicatorPersister.persist(dataElementId, 40, stateId, lgaId, facilityId, agem1, reportDate);      // male <15
            indicatorPersister.persist(dataElementId, 41, stateId, lgaId, facilityId, agem2, reportDate);      // male  >=15

            indicatorPersister.persist(dataElementId, 42, stateId, lgaId, facilityId, agef1, reportDate);        //female <15
            indicatorPersister.persist(dataElementId, 43, stateId, lgaId, facilityId, agef2, reportDate);        //female  >=15

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void disaggregate(String gender, int age) {
        if (gender.trim().equalsIgnoreCase("Male")) {
            if (age < 15) {
                agem1++;
            } else {
                agem2++;
            }
        } else {
            if (age < 15) {
                agef1++;
            } else {
                agef2++;
            }
        }
    }

    private void initVariables() {
        agem1 = 0;
        agem2 = 0;

        agef1 = 0;
        agef2 = 0;
    }

    private void getStateId(long facilityId) {
        jdbcTemplate.query("SELECT state_id, lga_id FROM facility  WHERE facility_id = ?",
                rs -> {
                    stateId = rs.getLong("state_id");
                    lgaId = rs.getLong("lga_id");
                }, facilityId);
    }


    private boolean isPatientActive(long patientId, int daysSinceLastRefill, Date reportingDate) {
        final boolean[] active = {false};
        String query = "SELECT m.date_visit, m.duration, m.regimentype_id, m.regimen_id, l.description AS regimentype, " +
                "r.description AS regimen FROM pharmacy m JOIN regimentype l ON m.regimentype_id = l.regimentype_id " +
                "JOIN regimen r ON m.regimen_id = r.regimen_id WHERE m.regimentype_id IN (1, 2, 3, 4, 14) AND" +
                " m.patient_id = " + patientId + " ORDER BY m.date_visit DESC LIMIT 1";
        jdbcTemplate.query(query, rs -> {
            Date dateLastRefill = rs.getDate("date_visit");
            int duration = rs.getInt("duration");
            int monthRefill = duration / 30;
            if (monthRefill <= 0) {
                monthRefill = 1;
            }
            if (dateLastRefill != null) {
                //If the last refill date plus refill duration plus days since last refill  in days is before the last day of the reporting date this patient is Active
                //or in other words if your 28 days is not after the reporting date your are LTFU
                if (DateUtil.addYearMonthDay(dateLastRefill, duration + daysSinceLastRefill, "DAY").after(reportingDate)) {
                    active[0] = true;
                }
            }
        });
        return active[0];
    }


    private boolean originalRegimen(long patientId, String regimen) {
        String query = "SELECT c.regimen FROM clinic c  WHERE c.patient_id = " + patientId + " AND c.commence = 1";
        return jdbcTemplate.query(query, rs -> {
           /* if (rs.next()) {
                String regimenStart = rs.getString("regimen") == null ? "" :
                        RegimenIntrospector.resolveRegimen(rs.getString("regimen"));
                return regimenStart.equalsIgnoreCase(regimen);
            }*/
            return false;
        });
    }

    private boolean isPatientActiveTLD(long patientId, int daysSinceLastRefill, Date reportingDate) {
        final boolean[] active = {false};
        String query = "SELECT m.date_visit, m.duration, m.regimentype_id, m.regimen_id, l.description AS regimentype, " +
                "r.description AS regimen FROM pharmacy m JOIN regimentype l ON m.regimentype_id = l.regimentype_id " +
                "JOIN regimen r ON m.regimen_id = r.regimen_id WHERE m.regimentype_id IN (1, 2, 3, 4, 14) AND m.patient_id = " +
                patientId + " ORDER BY m.date_visit DESC LIMIT 1";
        jdbcTemplate.query(query, rs -> {
            Date dateLastRefill = rs.getDate("date_visit");
            int duration = rs.getInt("duration");
            String regimen = rs.getString("regimen");
            int monthRefill = duration / 30;
            if (monthRefill <= 0) {
                monthRefill = 1;
            }
            if (dateLastRefill != null) {
                //If the last refill date plus refill duration plus days since last refill  in days is before the last day of the reporting date this patient is Active
                //or in other words if your 28 days is not after the reporting date your are LTFU
                if (DateUtil.addYearMonthDay(dateLastRefill, duration + daysSinceLastRefill, "DAY").before(reportingDate)) {
                    active[0] = false;
                } else {
                    if (regimen.contains("DTG")) active[0] = true;
                }
            }
        });
        return active[0];
    }

    private void executeUpdate(String query) {
        ContextProvider.getBean(TransactionTemplate.class)
                .execute(status -> {
                    jdbcTemplate.execute(query);
                    return null;
                });
    }
}
