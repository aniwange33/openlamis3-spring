package org.fhi360.lamis.service;

import lombok.extern.slf4j.Slf4j;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.DateUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
@Transactional
@Slf4j
public class CaseManagerService {
    private final JdbcTemplate jdbcTemplate;

    public CaseManagerService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void initClientSearch() {
        //Create a temporary Table to hold values.
        String client_drop = "DROP TABLE IF EXISTS CLIENTS";
        jdbcTemplate.execute(client_drop);

        Long facilityId = 1190L;

        //(
        String client = "CREATE  TABLE clients (facility_id bigint, patient_id bigint, " +
                "hospital_num varchar(25), surname varchar(45), other_names varchar(75), gender varchar(7), " +
                "state varchar(75), lga varchar(150), date_birth date, age int(11), age_unit varchar(30), " +
                "address varchar(100), date_started date, current_viral_load double, current_cd4 double, " +
                "current_cd4p double, current_status varchar(75), status int(11), casemanager_id bigint, " +
                "pregnant int(11), breastfeeding int(11))";
        //String client = "CREATE TEMPORARY TABLE IF NOT EXISTS clients (facility_id INT, patient_id INT, hospital_num TEXT, surname TEXT, other_names TEXT, gender TEXT, date_birth DATE, address TEXT, date_started DATE, current_viral_load INT, current_cd4 INT, current_status TEXT, status INT)";
        jdbcTemplate.execute(client);
        //ACTIVE CLIENTS
        String active_clients = "INSERT INTO clients (facility_id, patient_id, hospital_num, surname, other_names, " +
                "gender, state, lga, date_birth, age, age_unit, address, date_started, current_viral_load, " +
                "current_cd4, current_cd4p, current_status, status, casemanager_id, pregnant, breastfeeding)" +
                "SELECT facility_id, patient_id, hospital_num, surname, other_names, gender, state, lga, " +
                "date_birth, age, age_unit, address, date_started, last_viral_load, last_cd4, last_cd4p, " +
                "current_status, 0, casemanager_id, 0, 0 FROM patient WHERE current_status NOT IN ('Known Death', " +
                "'ART Transfer Out', 'Pre-ART Transfer Out') AND facility_id =" + facilityId;

        jdbcTemplate.execute(active_clients);
        //Update pregnant and breastfeeding...
        pregnantWomen();
        breastfeedingWomen();

        String query_pregnant = "UPDATE clients SET pregnant = 1 where patient_id IN (SELECT patient_id from pregnants)";
        jdbcTemplate.execute(query_pregnant);

        String query_breastfeeding = "UPDATE clients SET breastfeeding = 1 where patient_id IN (SELECT patient_id from breastfeeding)";
        jdbcTemplate.execute(query_breastfeeding);

        //GROUP VARIOUS CLIENTS BY STATUS

        String query_client_status = "SELECT * FROM clients";

        LOG.info("Before select");
        //Iterate through the result set...
        jdbcTemplate.query(query_client_status, resultSet -> {
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String internal_query;
                String currentStatus = resultSet.getString("current_status");
                String patient_id = String.valueOf(resultSet.getLong("patient_id"));
                String currentViralLoad = resultSet.getObject("current_viral_load") == null ? "" :
                        resultSet.getDouble("current_viral_load") == 0.0 ? "" :
                                Double.toString(resultSet.getDouble("current_viral_load"));
                String currentCd4 = resultSet.getObject("current_cd4") == null ? "0" :
                        resultSet.getDouble("current_cd4") == 0.0 ? "0" :
                                Double.toString(resultSet.getDouble("current_cd4"));
                String currentCd4p = resultSet.getObject("current_cd4p") == null ? "0" :
                        resultSet.getDouble("current_cd4p") == 0.0 ? "0" :
                                Double.toString(resultSet.getDouble("current_cd4p"));

                if (!currentStatus.equals("HIV+ non ART") && !currentStatus.equals("Pre-ART Transfer In")) {

                    String dateStarted = resultSet.getObject("date_started") == null ? "" :
                            DateUtil.parseDateToString(resultSet.getDate("date_started"), "yyyy-MM-dd");
                    if (!dateStarted.equals("")) {
                        LocalDate startDate = LocalDate.parse(dateStarted);
                        LocalDate today = LocalDate.now();
                        Period intervalPeriod = Period.between(startDate, today);
                        if (intervalPeriod.getYears() >= 1) { //START WORKING ON STABLE OR UNSTABLE
                            if (currentStatus.equals("Stopped Treatment") && currentStatus.equals("Lost to Follow Up")) {
                                internal_query = "UPDATE clients SET status = " +
                                        Constants.CaseManager.UNSTABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                            } else {
                                //Check for the stablilty or unstablility...
                                if (preceedingOis(patientId) == 0) { //-- Preceeding Ois
                                    if (clinicVisits(patientId) >= 5) { //-- Clinic Visits
                                        if (!currentViralLoad.equals("")) { //-- Viral Load
                                            if (Double.parseDouble(currentViralLoad) < 1000) {
                                                internal_query = "UPDATE clients SET status = " +
                                                        Constants.CaseManager.STABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                            } else {
                                                internal_query = "UPDATE clients SET status = " +
                                                        Constants.CaseManager.UNSTABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                            }
                                        } else {
                                            if (!currentCd4.equals("0")) {
                                                if (Double.parseDouble(currentCd4) > 250) {
                                                    internal_query = "UPDATE clients SET status = " +
                                                            Constants.CaseManager.STABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                                } else {
                                                    internal_query = "UPDATE clients SET status = " +
                                                            Constants.CaseManager.UNSTABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                                }
                                            } else {
                                                if (!currentCd4p.equals("0")) {
                                                    if (Double.parseDouble(currentCd4p) > 250) {
                                                        internal_query = "UPDATE clients SET status = " +
                                                                Constants.CaseManager.STABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                                    } else {
                                                        internal_query = "UPDATE clients SET status = " +
                                                                Constants.CaseManager.UNSTABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                                    }
                                                } else {
                                                    internal_query = "UPDATE clients SET status = " + Constants.CaseManager.UNSTABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                                }
                                            }
                                        }
                                    } else {//UNSTABLE
                                        internal_query = "UPDATE clients SET status = " + Constants.CaseManager.UNSTABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                    }
                                } else {//UNSTABLE
                                    internal_query = "UPDATE clients SET status = " + Constants.CaseManager.UNSTABLE_ONE_YEAR + " WHERE patient_id = " + patientId;
                                }
                            }
                        } else { //UNSTABLE NOT ONE YEAR = 3
                            if (currentStatus.equals("Stopped Treatment") && currentStatus.equals("Lost to Follow Up")) {
                                internal_query = "UPDATE clients SET status = " + Constants.CaseManager.UNSTABLE_NOT_ONE_YEAR + " WHERE patient_id = " + patientId;
                            } else {
                                internal_query = "UPDATE clients SET status = " + Constants.CaseManager.UNSTABLE_NOT_ONE_YEAR + " WHERE patient_id = " + patientId;
                            }
                        }
                    } else { //UNSTABLE NOT ONE YEAR = 3
                        internal_query = "UPDATE clients SET status = " + Constants.CaseManager.UNSTABLE_NOT_ONE_YEAR + " WHERE patient_id = " + patientId;
                    }
                } else {//Pre-ART = 4
                    internal_query = "UPDATE clients SET status = " + Constants.CaseManager.PRE_ART + " WHERE patient_id = " + patientId;
                }

                jdbcTemplate.execute(internal_query);
            }
            return null;
        });
    }

    private void pregnantWomen() {
        String client_drop = "DROP TABLE IF EXISTS PREGNANTS";
        jdbcTemplate.execute(client_drop);

        Long facilityId = 1190L;

        String pregnants = "CREATE  TABLE pregnants (patient_id bigint, facility_id int, date_visit date)";
        jdbcTemplate.execute(pregnants);

        String pregnant_clients = "INSERT INTO pregnants (patient_id, facility_id, date_visit)"
                + " SELECT DISTINCT patient_id, facility_id, date_visit FROM clinic WHERE pregnant = 1 " +
                "AND facility_id =" + facilityId + " ORDER BY date_visit DESC";
        jdbcTemplate.execute(pregnant_clients);
    }

    private void breastfeedingWomen() {
        String client_drop = "DROP TABLE IF EXISTS breastfeeding";
        jdbcTemplate.execute(client_drop);

        Long facilityId = 1190L;

        String client = "CREATE  TABLE breastfeeding (patient_id bigint, facility_id bigint, date_visit date)";
        jdbcTemplate.execute(client);

        String active_clients = "INSERT INTO breastfeeding ( patient_id, facility_id, date_visit)"
                + "SELECT DISTINCT patient_id, facility_id, date_visit FROM clinic WHERE breastfeeding = 1 " +
                "AND facility_id =" + facilityId + " ORDER BY date_visit DESC";
        jdbcTemplate.execute(active_clients);
    }

    private int clinicVisits(long patientId) {
        String query = "SELECT count(*) AS count FROM clinic WHERE patient_id = " + patientId +
                " AND date_visit >= DATE_ADD(CURDATE(), INTERVAL -12 MONTH) AND date_visit <= CURDATE()";
        return jdbcTemplate.queryForObject(query, Long.class).intValue();
    }

    private int preceedingOis(long patientId) {
        String query = "SELECT count(*) AS count FROM clinic WHERE patient_id = " + patientId +
                " AND date_visit >= DATE_ADD(CURDATE(), INTERVAL -6 MONTH ) AND date_visit <= CURDATE() AND oi_ids " +
                "IS NOT NULL AND oi_ids != ''";
        return jdbcTemplate.queryForObject(query, Long.class).intValue();
    }
}
