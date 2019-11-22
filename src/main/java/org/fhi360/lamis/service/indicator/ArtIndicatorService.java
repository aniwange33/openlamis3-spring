
package org.fhi360.lamis.service.indicator;

import org.fhi360.lamis.service.TreatmentCurrentService;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import static org.fhi360.lamis.utility.StringUtil.isInteger;

@Component
public class ArtIndicatorService {
    private final IndicatorPersister indicatorPersister;
    private long stateId;
    private long lgaId;
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private int agem1, agem2, agem3, agem4, agem5, agem6, agem7, agem8, agem9, agem10, agem11, agem12;
    ;
    private int agef1, agef2, agef3, agef4, agef5, agef6, agef7, agef8, agef9, agef10, agef11, agef12;
    private int agem13_1, agem14_1, agem15_1, agem13_2, agem14_2, agem15_2;
    private int agef13_1, agef14_1, agef15_1, agef13_2, agef14_2, agef15_2;
    private int preg, feeding, tbm, tbf;

    private int dataElementId = 0;

    public ArtIndicatorService(IndicatorPersister indicatorPersister) {
        this.indicatorPersister = indicatorPersister;
    }

    public void process(long facilityId, Date reportingDate) {
        TreatmentCurrentService treatmentCurrentService = new TreatmentCurrentService();
        try {
            String reportDate = DateUtil.parseDateToString(reportingDate, "yyyy-MM-dd");
            System.out.println("ArtIndicatorService: running report for : " + reportDate);

            jdbcUtil = new JDBCUtil();
            getStateId(facilityId); // stateId and lgaId

            ResultSet rs;

            System.out.println("Computing ART1.....");

            //ART 1
            //Total Number of HIV-positive newly enrolled in clinical care during the month (excludes transfer-in)
            initVariables();
            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age, pregnant, breastfeeding, tb_status FROM patient WHERE facility_id = " + facilityId + " AND date_registration = '" + reportDate + "' AND status_registration = 'HIV+ non ART'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                int pregnant = resultSet.getInt("pregnant");
                int breastfeeding = resultSet.getInt("breastfeeding");
                String tbStatus = resultSet.getString("tb_status") == null ? "" : resultSet.getString("tb_status");

                disaggregate(gender, age);
                if (gender.trim().equalsIgnoreCase("Male")) {
                    //check for TB status during enrolmemnt
                    if (tbStatus.equalsIgnoreCase("Currently on TB treatment") || tbStatus.equalsIgnoreCase("TB positive not on TB drugs")) {
                        tbm++;
                    }
                } else {
                    //check if client is pregnant or breast feeding during enrolment
                    if (pregnant == 1) {
                        preg++;
                    } else {
                        if (breastfeeding == 1) {
                            feeding++;
                        }
                    }
                    //check for TB status during enrolmemnt
                    if (tbStatus.equalsIgnoreCase("Currently on TB treatment") || tbStatus.equalsIgnoreCase("TB positive not on TB drugs")) {
                        tbf++;
                    }
                }
            }
            //Populate the report parameter map with values computed for ART 1
            dataElementId = 1;
            indicatorPersister.persist(dataElementId, 13, stateId, lgaId, facilityId, agem1, reportDate);      // male <1

            indicatorPersister.persist(dataElementId, 1, stateId, lgaId, facilityId, agef1, reportDate);        //female <1

            indicatorPersister.persist(dataElementId, 14, stateId, lgaId, facilityId, agem2, reportDate);         // male 1-4
            indicatorPersister.persist(dataElementId, 2, stateId, lgaId, facilityId, agef2, reportDate);        //female 1-4

            indicatorPersister.persist(dataElementId, 15, stateId, lgaId, facilityId, agem3, reportDate);         // male  5-9
            indicatorPersister.persist(dataElementId, 3, stateId, lgaId, facilityId, agef3, reportDate);        //female 5-9

            indicatorPersister.persist(dataElementId, 16, stateId, lgaId, facilityId, agem4, reportDate);         // male 10-14
            indicatorPersister.persist(dataElementId, 4, stateId, lgaId, facilityId, agef4, reportDate);        //female 10-14

            indicatorPersister.persist(dataElementId, 17, stateId, lgaId, facilityId, agem5, reportDate);         // male 15-19
            indicatorPersister.persist(dataElementId, 5, stateId, lgaId, facilityId, agef5, reportDate);        //female 15-19

            indicatorPersister.persist(dataElementId, 18, stateId, lgaId, facilityId, agem6, reportDate);         // male 20-24
            indicatorPersister.persist(dataElementId, 6, stateId, lgaId, facilityId, agef6, reportDate);        //female 20-24

            indicatorPersister.persist(dataElementId, 19, stateId, lgaId, facilityId, agem7, reportDate);         // male 25-29
            indicatorPersister.persist(dataElementId, 7, stateId, lgaId, facilityId, agef7, reportDate);        //female 25-29

            indicatorPersister.persist(dataElementId, 20, stateId, lgaId, facilityId, agem8, reportDate);         // male 30-34
            indicatorPersister.persist(dataElementId, 8, stateId, lgaId, facilityId, agef8, reportDate);        //female 30-34

            indicatorPersister.persist(dataElementId, 21, stateId, lgaId, facilityId, agem9, reportDate);         // male 35-39
            indicatorPersister.persist(dataElementId, 9, stateId, lgaId, facilityId, agef9, reportDate);        //female 35-39

            indicatorPersister.persist(dataElementId, 22, stateId, lgaId, facilityId, agem10, reportDate);         // male 40-44
            indicatorPersister.persist(dataElementId, 10, stateId, lgaId, facilityId, agef10, reportDate);        //female 40-44

            indicatorPersister.persist(dataElementId, 23, stateId, lgaId, facilityId, agem11, reportDate);         // male 45-49
            indicatorPersister.persist(dataElementId, 11, stateId, lgaId, facilityId, agef11, reportDate);        //female 45-49

            indicatorPersister.persist(dataElementId, 24, stateId, lgaId, facilityId, agem12, reportDate);        // male 50+
            indicatorPersister.persist(dataElementId, 12, stateId, lgaId, facilityId, agef12, reportDate);        //female 50+

            dataElementId = 2;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, preg, reportDate);

            dataElementId = 3;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, feeding, reportDate);

            dataElementId = 5;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, tbm, reportDate);
            dataElementId = 4;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, tbf, reportDate);

            System.out.println("Computing ART2.....");
            //ART 2
            //Total number of people living with HIV who are currently in HIV care who received at least one of the following
            //by the end of the month: clinical assessment(WHO staging) OR CD4 count OR viral load OR current on treatment
            initVariables();

            executeUpdate("DROP INDEX IF EXISTS idx_visit");
            executeUpdate("DROP TABLE IF EXISTS visit");
            executeUpdate("CREATE TEMPORARY TABLE visit AS SELECT * FROM clinic WHERE facility_id = " + facilityId + " AND date_visit >= DATEADD('MONTH', -6, '" + reportDate + "') AND date_visit <= '" + reportDate + "' AND clinic_stage IS NOT NULL OR clinic_stage != ''");
            executeUpdate("CREATE INDEX idx_visit ON visit(patient_id)");

            executeUpdate("DROP INDEX IF EXISTS idx_preg");
            executeUpdate("DROP TABLE IF EXISTS preg");
            executeUpdate("CREATE TEMPORARY TABLE preg AS SELECT * FROM clinic WHERE facility_id = " + facilityId + " AND date_visit >= DATEADD('MONTH', -9, '" + reportDate + "') AND date_visit <= '" + reportDate + "' ORDER BY date_visit DESC LIMIT 1");
            executeUpdate("CREATE INDEX idx_preg ON preg(patient_id)");

            executeUpdate("DROP INDEX IF EXISTS idx_pharm");
            executeUpdate("DROP TABLE IF EXISTS pharm");
            executeUpdate("CREATE TEMPORARY TABLE pharm AS SELECT * FROM pharmacy WHERE facility_id = " + facilityId + " AND date_visit <= '" + reportDate + "' AND regimentype_id IN (1, 2, 3, 4, 14)");
            executeUpdate("CREATE INDEX idx_pharm ON pharm(patient_id)");

            executeUpdate("DROP INDEX IF EXISTS idx_lab");
            executeUpdate("DROP TABLE IF EXISTS lab");
            executeUpdate("CREATE TEMPORARY TABLE lab AS SELECT * FROM laboratory WHERE facility_id = " + facilityId + " AND date_reported >= DATEADD('MONTH', -6, '" + reportDate + "') AND date_reported <= '" + reportDate + "' AND labtest_id IN (1, 16)");
            executeUpdate("CREATE INDEX idx_lab ON lab(patient_id)");

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age, current_status FROM patient WHERE facility_id = " + facilityId + " AND current_status IN (" + Constants.ClientStatus.ON_TREATMENT + ")  AND date_current_status = '" + reportDate + "' AND date_started IS NOT NULL ORDER BY current_status";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                Date dateStarted = resultSet.getDate("date_started");

                int pregnant = 0;
                int breastfeeding = 0;

                boolean count = false;

                query = "SELECT DISTINCT patient_id FROM visit WHERE patient_id = " + patientId;
                if (found(query)) {
                    count = true;
                } else {
                    query = "SELECT DISTINCT patient_id FROM lab WHERE patient_id = " + patientId;
                    if (found(query)) {
                        count = true;
                    } else {
                        if (dateStarted != null) {
                            if (treatmentCurrentService.isPatientActive(patientId, Constants.LTFU.PEPFAR, reportingDate)) {
                                count = true;
                            }
                        }
                    }
                }

                if (count) {
                    disaggregate(gender, age);
                    if (gender.trim().equalsIgnoreCase("Female")) {
                        //check if client is pregnant or breast feeding during enrolment
                        query = "SELECT pregnant, breastfeeding FROM preg WHERE patient_id = " + patientId + " ORDER BY date_visit DESC LIMIT 1";
                        rs = executeQuery(query);
                        if (rs.next()) {
                            pregnant = rs.getInt("pregnant");
                            breastfeeding = rs.getInt("breastfeeding");
                        }
                        if (pregnant == 1) {
                            preg++;
                        } else {
                            if (breastfeeding == 1) {
                                feeding++;
                            }
                        }
                    }
                }
            }
            //Populate the report parameter map with values computed for ART 2

            dataElementId = 6;
            indicatorPersister.persist(dataElementId, 13, stateId, lgaId, facilityId, agem1, reportDate);         // male <1
            indicatorPersister.persist(dataElementId, 1, stateId, lgaId, facilityId, agef1, reportDate);        //female <1

            indicatorPersister.persist(dataElementId, 14, stateId, lgaId, facilityId, agem2, reportDate);         // male 1-4
            indicatorPersister.persist(dataElementId, 2, stateId, lgaId, facilityId, agef2, reportDate);        //female 1-4

            indicatorPersister.persist(dataElementId, 15, stateId, lgaId, facilityId, agem3, reportDate);         // male  5-9
            indicatorPersister.persist(dataElementId, 3, stateId, lgaId, facilityId, agef3, reportDate);        //female 5-9

            indicatorPersister.persist(dataElementId, 16, stateId, lgaId, facilityId, agem4, reportDate);         // male 10-14
            indicatorPersister.persist(dataElementId, 4, stateId, lgaId, facilityId, agef4, reportDate);        //female 10-14

            indicatorPersister.persist(dataElementId, 17, stateId, lgaId, facilityId, agem5, reportDate);         // male 15-19
            indicatorPersister.persist(dataElementId, 5, stateId, lgaId, facilityId, agef5, reportDate);        //female 15-19

            indicatorPersister.persist(dataElementId, 18, stateId, lgaId, facilityId, agem6, reportDate);         // male 20-24
            indicatorPersister.persist(dataElementId, 6, stateId, lgaId, facilityId, agef6, reportDate);        //female 20-24

            indicatorPersister.persist(dataElementId, 19, stateId, lgaId, facilityId, agem7, reportDate);         // male 25-29
            indicatorPersister.persist(dataElementId, 7, stateId, lgaId, facilityId, agef7, reportDate);        //female 25-29

            indicatorPersister.persist(dataElementId, 20, stateId, lgaId, facilityId, agem8, reportDate);         // male 30-34
            indicatorPersister.persist(dataElementId, 8, stateId, lgaId, facilityId, agef8, reportDate);        //female 30-34

            indicatorPersister.persist(dataElementId, 21, stateId, lgaId, facilityId, agem9, reportDate);         // male 35-39
            indicatorPersister.persist(dataElementId, 9, stateId, lgaId, facilityId, agef9, reportDate);        //female 35-39

            indicatorPersister.persist(dataElementId, 22, stateId, lgaId, facilityId, agem10, reportDate);         // male 40-44
            indicatorPersister.persist(dataElementId, 10, stateId, lgaId, facilityId, agef10, reportDate);        //female 40-44

            indicatorPersister.persist(dataElementId, 23, stateId, lgaId, facilityId, agem11, reportDate);         // male 45-49
            indicatorPersister.persist(dataElementId, 11, stateId, lgaId, facilityId, agef11, reportDate);        //female 45-49

            indicatorPersister.persist(dataElementId, 24, stateId, lgaId, facilityId, agem12, reportDate);         // male 50+
            indicatorPersister.persist(dataElementId, 12, stateId, lgaId, facilityId, agef12, reportDate);        //female 50+

            dataElementId = 7;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, preg, reportDate);


            System.out.println("Computing ART3.....");
            //ART 3
            //Total number of people living with HIV newly started on ART during the month (excludes ART transfer-in)
            initVariables();

            executeUpdate("DROP INDEX IF EXISTS idx_preg");
            executeUpdate("DROP TABLE IF EXISTS preg");
            executeUpdate("CREATE TEMPORARY TABLE preg AS SELECT * FROM clinic WHERE facility_id = " + facilityId + " AND commence = 1");
            executeUpdate("CREATE INDEX idx_preg ON preg(patient_id)");

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age FROM patient WHERE facility_id = " + facilityId + " AND date_started = '" + reportDate + "' AND status_registration != 'ART Transfer In'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                int pregnant = 0;
                int breastfeeding = 0;

                disaggregate(gender, age);

                if (gender.trim().equalsIgnoreCase("Female")) {
                    //check if client is pregnant or breast feeding during visit
                    query = "SELECT pregnant, breastfeeding FROM preg WHERE patient_id = " + patientId + " ORDER BY date_visit DESC LIMIT 1";
                    rs = executeQuery(query);
                    if (rs.next()) {
                        pregnant = rs.getInt("pregnant");
                        breastfeeding = rs.getInt("breastfeeding");
                    }
                    if (pregnant == 1) {
                        preg++;
                    } else {
                        if (breastfeeding == 1) {
                            feeding++;
                        }
                    }
                }
            }
            //Populate the report parameter map with values computed for ART 3
            dataElementId = 8;
            indicatorPersister.persist(dataElementId, 13, stateId, lgaId, facilityId, agem1, reportDate);         // male <1
            indicatorPersister.persist(dataElementId, 1, stateId, lgaId, facilityId, agef1, reportDate);        //female <1

            indicatorPersister.persist(dataElementId, 14, stateId, lgaId, facilityId, agem2, reportDate);         // male 1-4
            indicatorPersister.persist(dataElementId, 2, stateId, lgaId, facilityId, agef2, reportDate);        //female 1-4

            indicatorPersister.persist(dataElementId, 15, stateId, lgaId, facilityId, agem3, reportDate);         // male  5-9
            indicatorPersister.persist(dataElementId, 3, stateId, lgaId, facilityId, agef3, reportDate);        //female 5-9

            indicatorPersister.persist(dataElementId, 16, stateId, lgaId, facilityId, agem4, reportDate);         // male 10-14
            indicatorPersister.persist(dataElementId, 4, stateId, lgaId, facilityId, agef4, reportDate);        //female 10-14

            indicatorPersister.persist(dataElementId, 17, stateId, lgaId, facilityId, agem5, reportDate);         // male 15-19
            indicatorPersister.persist(dataElementId, 5, stateId, lgaId, facilityId, agef5, reportDate);        //female 15-19

            indicatorPersister.persist(dataElementId, 18, stateId, lgaId, facilityId, agem6, reportDate);         // male 20-24
            indicatorPersister.persist(dataElementId, 6, stateId, lgaId, facilityId, agef6, reportDate);        //female 20-24

            indicatorPersister.persist(dataElementId, 19, stateId, lgaId, facilityId, agem7, reportDate);         // male 25-29
            indicatorPersister.persist(dataElementId, 7, stateId, lgaId, facilityId, agef7, reportDate);        //female 25-29

            indicatorPersister.persist(dataElementId, 20, stateId, lgaId, facilityId, agem8, reportDate);         // male 30-34
            indicatorPersister.persist(dataElementId, 8, stateId, lgaId, facilityId, agef8, reportDate);        //female 30-34

            indicatorPersister.persist(dataElementId, 21, stateId, lgaId, facilityId, agem9, reportDate);         // male 35-39
            indicatorPersister.persist(dataElementId, 9, stateId, lgaId, facilityId, agef9, reportDate);        //female 35-39

            indicatorPersister.persist(dataElementId, 22, stateId, lgaId, facilityId, agem10, reportDate);         // male 40-44
            indicatorPersister.persist(dataElementId, 10, stateId, lgaId, facilityId, agef10, reportDate);        //female 40-44

            indicatorPersister.persist(dataElementId, 23, stateId, lgaId, facilityId, agem11, reportDate);         // male 45-49
            indicatorPersister.persist(dataElementId, 11, stateId, lgaId, facilityId, agef11, reportDate);        //female 45-49

            indicatorPersister.persist(dataElementId, 24, stateId, lgaId, facilityId, agem12, reportDate);         // male 50+
            indicatorPersister.persist(dataElementId, 12, stateId, lgaId, facilityId, agef12, reportDate);        //female 50+

            dataElementId = 9;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, preg, reportDate);

            dataElementId = 10;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, feeding, reportDate);


            System.out.println("Computing ART4.....");
            //ART 4
            //Total number of people living with HIV who are currently receiving ART during the month (All regimen)
            initVariables();

            executeUpdate("DROP INDEX IF EXISTS idx_preg");
            executeUpdate("DROP TABLE IF EXISTS preg");
            executeUpdate("CREATE TEMPORARY TABLE preg AS SELECT * FROM clinic WHERE facility_id = " + facilityId + " AND date_visit >=  DATEADD('MONTH', -9, '" + reportDate + "') AND date_visit <= '" + reportDate + "'");
            executeUpdate("CREATE INDEX idx_preg ON preg(patient_id)");

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age FROM patient WHERE facility_id = " + facilityId + " AND current_status IN (" + Constants.ClientStatus.ON_TREATMENT + ")  AND date_current_status <= '" + reportDate + "' AND date_started IS NOT NULL ORDER BY current_status";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                int pregnant = 0;
                int breastfeeding = 0;
                rs = getLastRefillVisit(patientId);
                while (rs != null && rs.next()) {
                    Date dateLastRefill = rs.getDate("date_visit");
                    int duration = rs.getInt("duration");
                    int monthRefill = duration / 30;
                    if (monthRefill <= 0) {
                        monthRefill = 1;
                    }

                    if (dateLastRefill != null) {
                        //If the last refill date plus refill duration plus 90 days is before the last day of the reporting month this patient is LTFU     if(DateUtil.addYearMonthDay(lastRefill, duration+90, "day(s)").before(reportingDateEnd))
                        if (!DateUtil.addYearMonthDay(dateLastRefill, duration + Constants.LTFU.PEPFAR, "DAY").after(reportingDate)) {
                            disaggregate(gender, age);

                            long regimentypeId = rs.getLong("regimentype_id");
                            if (gender.trim().equalsIgnoreCase("Male")) {
                                if (age < 15) {
                                    if (regimentypeId == 1 || regimentypeId == 3) {
                                        agem13_1++;
                                    } else {
                                        if (regimentypeId == 2 || regimentypeId == 4) {
                                            agem14_1++;
                                        } else {
                                            agem15_1++;
                                        }
                                    }
                                } else {
                                    if (regimentypeId == 1 || regimentypeId == 3) {
                                        agem13_2++;
                                    } else {
                                        if (regimentypeId == 2 || regimentypeId == 4) {
                                            agem14_2++;
                                        } else {
                                            agem15_2++;
                                        }
                                    }
                                }
                            } else {
                                if (age < 15) {
                                    if (regimentypeId == 1 || regimentypeId == 3) {
                                        agef13_1++;
                                    } else {
                                        if (regimentypeId == 2 || regimentypeId == 4) {
                                            agef14_1++;
                                        } else {
                                            agef15_1++;
                                        }
                                    }
                                } else {
                                    if (regimentypeId == 1 || regimentypeId == 3) {
                                        agef13_2++;
                                    } else {
                                        if (regimentypeId == 2 || regimentypeId == 4) {
                                            agef14_2++;
                                        } else {
                                            agef15_2++;
                                        }
                                    }
                                }
                            }

                            if (gender.trim().equalsIgnoreCase("Female")) {
                                //check if client is pregnant or breast feeding during enrolment
                                query = "SELECT pregnant, breastfeeding FROM preg WHERE patient_id = " + patientId + " ORDER BY date_visit DESC LIMIT 1";
                                ResultSet rs1 = executeQuery(query);
                                if (rs1.next()) {
                                    pregnant = rs1.getInt("pregnant");
                                    breastfeeding = rs1.getInt("breastfeeding");
                                }

                                if (pregnant == 1) {
                                    preg++;
                                } else {
                                    if (breastfeeding == 1) {
                                        feeding++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //Populate the report parameter map with values computed for ART 4
            dataElementId = 11;
            indicatorPersister.persist(dataElementId, 13, stateId, lgaId, facilityId, agem1, reportDate);         // male <1
            indicatorPersister.persist(dataElementId, 1, stateId, lgaId, facilityId, agef1, reportDate);        //female <1

            indicatorPersister.persist(dataElementId, 14, stateId, lgaId, facilityId, agem2, reportDate);         // male 1-4
            indicatorPersister.persist(dataElementId, 2, stateId, lgaId, facilityId, agef2, reportDate);        //female 1-4

            indicatorPersister.persist(dataElementId, 15, stateId, lgaId, facilityId, agem3, reportDate);         // male  5-9
            indicatorPersister.persist(dataElementId, 3, stateId, lgaId, facilityId, agef3, reportDate);        //female 5-9

            indicatorPersister.persist(dataElementId, 16, stateId, lgaId, facilityId, agem4, reportDate);         // male 10-14
            indicatorPersister.persist(dataElementId, 4, stateId, lgaId, facilityId, agef4, reportDate);        //female 10-14

            indicatorPersister.persist(dataElementId, 17, stateId, lgaId, facilityId, agem5, reportDate);         // male 15-19
            indicatorPersister.persist(dataElementId, 5, stateId, lgaId, facilityId, agef5, reportDate);        //female 15-19

            indicatorPersister.persist(dataElementId, 18, stateId, lgaId, facilityId, agem6, reportDate);         // male 20-24
            indicatorPersister.persist(dataElementId, 6, stateId, lgaId, facilityId, agef6, reportDate);        //female 20-24

            indicatorPersister.persist(dataElementId, 19, stateId, lgaId, facilityId, agem7, reportDate);         // male 25-29
            indicatorPersister.persist(dataElementId, 7, stateId, lgaId, facilityId, agef7, reportDate);        //female 25-29

            indicatorPersister.persist(dataElementId, 20, stateId, lgaId, facilityId, agem8, reportDate);         // male 30-34
            indicatorPersister.persist(dataElementId, 8, stateId, lgaId, facilityId, agef8, reportDate);        //female 30-34

            indicatorPersister.persist(dataElementId, 21, stateId, lgaId, facilityId, agem9, reportDate);         // male 35-39
            indicatorPersister.persist(dataElementId, 9, stateId, lgaId, facilityId, agef9, reportDate);        //female 35-39

            indicatorPersister.persist(dataElementId, 22, stateId, lgaId, facilityId, agem10, reportDate);         // male 40-44
            indicatorPersister.persist(dataElementId, 10, stateId, lgaId, facilityId, agef10, reportDate);        //female 40-44

            indicatorPersister.persist(dataElementId, 23, stateId, lgaId, facilityId, agem11, reportDate);         // male 45-49
            indicatorPersister.persist(dataElementId, 11, stateId, lgaId, facilityId, agef11, reportDate);        //female 45-49

            indicatorPersister.persist(dataElementId, 24, stateId, lgaId, facilityId, agem12, reportDate);         // male 50+
            indicatorPersister.persist(dataElementId, 12, stateId, lgaId, facilityId, agef12, reportDate);        //female 50+

            dataElementId = 12;
            indicatorPersister.persist(dataElementId, 27, stateId, lgaId, facilityId, agem13_1, reportDate);

            dataElementId = 12;
            indicatorPersister.persist(dataElementId, 28, stateId, lgaId, facilityId, agem13_2, reportDate);

            dataElementId = 12;
            indicatorPersister.persist(dataElementId, 25, stateId, lgaId, facilityId, agef13_1, reportDate);

            dataElementId = 12;
            indicatorPersister.persist(dataElementId, 26, stateId, lgaId, facilityId, agef13_2, reportDate);


            dataElementId = 13;
            indicatorPersister.persist(dataElementId, 27, stateId, lgaId, facilityId, agem14_1, reportDate);

            dataElementId = 13;
            indicatorPersister.persist(dataElementId, 28, stateId, lgaId, facilityId, agem14_2, reportDate);

            dataElementId = 13;
            indicatorPersister.persist(dataElementId, 25, stateId, lgaId, facilityId, agef14_1, reportDate);

            dataElementId = 13;
            indicatorPersister.persist(dataElementId, 26, stateId, lgaId, facilityId, agef14_2, reportDate);


            dataElementId = 14;
            indicatorPersister.persist(dataElementId, 27, stateId, lgaId, facilityId, agem15_1, reportDate);

            dataElementId = 14;
            indicatorPersister.persist(dataElementId, 28, stateId, lgaId, facilityId, agem15_2, reportDate);

            dataElementId = 14;
            indicatorPersister.persist(dataElementId, 25, stateId, lgaId, facilityId, agef15_1, reportDate);

            dataElementId = 14;
            indicatorPersister.persist(dataElementId, 26, stateId, lgaId, facilityId, agef15_2, reportDate);

            dataElementId = 15;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, preg, reportDate);

            dataElementId = 16;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, feeding, reportDate);


            System.out.println("Computing ART5.....");
            //ART 5
            //Number of people living with HIV and on ART with a viral load test result during the month
            initVariables();

            executeUpdate("DROP INDEX IF EXISTS idx_lab");
            executeUpdate("DROP TABLE IF EXISTS lab");
            executeUpdate("CREATE TEMPORARY TABLE lab AS SELECT * FROM laboratory WHERE facility_id = " + facilityId + " AND date_reported = '" + reportDate + "' AND labtest_id = 16");
            executeUpdate("CREATE INDEX idx_lab ON lab(patient_id)");

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age FROM patient WHERE facility_id = " + facilityId + " AND date_registration <= '" + reportDate + "' AND date_started IS NOT NULL AND DATEDIFF(MONTH, date_started, '" + reportDate + "') >= 6 AND date_started <= '" + reportDate + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                //Check for viral load this reporting month
                query = "SELECT patient_id FROM lab WHERE patient_id = " + patientId + " ORDER BY date_reported DESC LIMIT 1";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    disaggregate(gender, age);
                }
            }
            //Populate the report parameter map with values computed for ART 5
            dataElementId = 17;
            indicatorPersister.persist(dataElementId, 13, stateId, lgaId, facilityId, agem1, reportDate);         // male <1
            indicatorPersister.persist(dataElementId, 1, stateId, lgaId, facilityId, agef1, reportDate);        //female <1

            indicatorPersister.persist(dataElementId, 14, stateId, lgaId, facilityId, agem2, reportDate);         // male 1-4
            indicatorPersister.persist(dataElementId, 2, stateId, lgaId, facilityId, agef2, reportDate);        //female 1-4

            indicatorPersister.persist(dataElementId, 15, stateId, lgaId, facilityId, agem3, reportDate);         // male  5-9
            indicatorPersister.persist(dataElementId, 3, stateId, lgaId, facilityId, agef3, reportDate);        //female 5-9

            indicatorPersister.persist(dataElementId, 16, stateId, lgaId, facilityId, agem4, reportDate);         // male 10-14
            indicatorPersister.persist(dataElementId, 4, stateId, lgaId, facilityId, agef4, reportDate);        //female 10-14

            indicatorPersister.persist(dataElementId, 17, stateId, lgaId, facilityId, agem5, reportDate);         // male 15-19
            indicatorPersister.persist(dataElementId, 5, stateId, lgaId, facilityId, agef5, reportDate);        //female 15-19

            indicatorPersister.persist(dataElementId, 18, stateId, lgaId, facilityId, agem6, reportDate);         // male 20-24
            indicatorPersister.persist(dataElementId, 6, stateId, lgaId, facilityId, agef6, reportDate);        //female 20-24

            indicatorPersister.persist(dataElementId, 19, stateId, lgaId, facilityId, agem7, reportDate);         // male 25-29
            indicatorPersister.persist(dataElementId, 7, stateId, lgaId, facilityId, agef7, reportDate);        //female 25-29

            indicatorPersister.persist(dataElementId, 20, stateId, lgaId, facilityId, agem8, reportDate);         // male 30-34
            indicatorPersister.persist(dataElementId, 8, stateId, lgaId, facilityId, agef8, reportDate);        //female 30-34

            indicatorPersister.persist(dataElementId, 21, stateId, lgaId, facilityId, agem9, reportDate);         // male 35-39
            indicatorPersister.persist(dataElementId, 9, stateId, lgaId, facilityId, agef9, reportDate);        //female 35-39

            indicatorPersister.persist(dataElementId, 22, stateId, lgaId, facilityId, agem10, reportDate);         // male 40-44
            indicatorPersister.persist(dataElementId, 10, stateId, lgaId, facilityId, agef10, reportDate);        //female 40-44

            indicatorPersister.persist(dataElementId, 23, stateId, lgaId, facilityId, agem11, reportDate);         // male 45-49
            indicatorPersister.persist(dataElementId, 11, stateId, lgaId, facilityId, agef11, reportDate);        //female 45-49

            indicatorPersister.persist(dataElementId, 24, stateId, lgaId, facilityId, agem12, reportDate);         // male 50+
            indicatorPersister.persist(dataElementId, 12, stateId, lgaId, facilityId, agef12, reportDate);        //female 50+

            System.out.println("Computing ART6.....");
            //ART 6
            //Number of people living with HIV and on ART who are virologically suppressed (viral load < 1000 c/ml) during the month
            initVariables();

            executeUpdate("DROP INDEX IF EXISTS idx_lab");
            executeUpdate("DROP TABLE IF EXISTS lab");
            executeUpdate("CREATE TEMPORARY TABLE lab AS SELECT * FROM laboratory WHERE facility_id = " + facilityId + " AND date_reported = '" + reportDate + "' AND labtest_id = 16");
            executeUpdate("CREATE INDEX idx_lab ON lab(patient_id)");

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age FROM patient WHERE facility_id = " + facilityId + " AND date_registration <= '" + reportDate + "' AND date_started IS NOT NULL AND DATEDIFF(MONTH, date_started, '" + reportDate + "') >= 6 AND date_started <= '" + reportDate + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                //Check if the last viral load before the reporting month is less than 1000
                query = "SELECT * FROM lab WHERE patient_id = " + patientId + " ORDER BY date_reported DESC LIMIT 1";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String resultab = rs.getString("resultab");
                    if (isInteger(resultab)) {
                        if (Double.valueOf(resultab) < 1000) {
                            disaggregate(gender, age);
                        }
                    }
                }
            }
            //Populate the report parameter map with values computed for ART 6
            dataElementId = 18;
            indicatorPersister.persist(dataElementId, 13, stateId, lgaId, facilityId, agem1, reportDate);         // male <1
            indicatorPersister.persist(dataElementId, 1, stateId, lgaId, facilityId, agef1, reportDate);        //female <1

            indicatorPersister.persist(dataElementId, 14, stateId, lgaId, facilityId, agem2, reportDate);         // male 1-4
            indicatorPersister.persist(dataElementId, 2, stateId, lgaId, facilityId, agef2, reportDate);        //female 1-4

            indicatorPersister.persist(dataElementId, 15, stateId, lgaId, facilityId, agem3, reportDate);         // male  5-9
            indicatorPersister.persist(dataElementId, 3, stateId, lgaId, facilityId, agef3, reportDate);        //female 5-9

            indicatorPersister.persist(dataElementId, 16, stateId, lgaId, facilityId, agem4, reportDate);         // male 10-14
            indicatorPersister.persist(dataElementId, 4, stateId, lgaId, facilityId, agef4, reportDate);        //female 10-14

            indicatorPersister.persist(dataElementId, 17, stateId, lgaId, facilityId, agem5, reportDate);         // male 15-19
            indicatorPersister.persist(dataElementId, 5, stateId, lgaId, facilityId, agef5, reportDate);        //female 15-19

            indicatorPersister.persist(dataElementId, 18, stateId, lgaId, facilityId, agem6, reportDate);         // male 20-24
            indicatorPersister.persist(dataElementId, 6, stateId, lgaId, facilityId, agef6, reportDate);        //female 20-24

            indicatorPersister.persist(dataElementId, 19, stateId, lgaId, facilityId, agem7, reportDate);         // male 25-29
            indicatorPersister.persist(dataElementId, 7, stateId, lgaId, facilityId, agef7, reportDate);        //female 25-29

            indicatorPersister.persist(dataElementId, 20, stateId, lgaId, facilityId, agem8, reportDate);         // male 30-34
            indicatorPersister.persist(dataElementId, 8, stateId, lgaId, facilityId, agef8, reportDate);        //female 30-34

            indicatorPersister.persist(dataElementId, 21, stateId, lgaId, facilityId, agem9, reportDate);         // male 35-39
            indicatorPersister.persist(dataElementId, 9, stateId, lgaId, facilityId, agef9, reportDate);        //female 35-39

            indicatorPersister.persist(dataElementId, 22, stateId, lgaId, facilityId, agem10, reportDate);         // male 40-44
            indicatorPersister.persist(dataElementId, 10, stateId, lgaId, facilityId, agef10, reportDate);        //female 40-44

            indicatorPersister.persist(dataElementId, 23, stateId, lgaId, facilityId, agem11, reportDate);         // male 45-49
            indicatorPersister.persist(dataElementId, 11, stateId, lgaId, facilityId, agef11, reportDate);        //female 45-49

            indicatorPersister.persist(dataElementId, 24, stateId, lgaId, facilityId, agem12, reportDate);         // male 50+
            indicatorPersister.persist(dataElementId, 12, stateId, lgaId, facilityId, agef12, reportDate);        //female 50+


            initVariables();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");


                //Number VL test unsuppressed during the reporting period who attended EAC
                query = "SELECT COUNT(DISTINCT patient_id) FROM eac WHERE patient_id IN (SELECT DISTINCT patient_id FROM lab WHERE labtest_id = 16 AND result >= 1000 AND date_reported = '" + reportDate + "' AND (date_eac1 >= '" + reportDate + "' OR date_eac2 >= '" + reportDate + "' OR date_eac3 >= '" + reportDate + "'))";

                //Check if the last viral load before the reporting month is less than 1000
                query = "SELECT * FROM lab WHERE patient_id = " + patientId + " ORDER BY date_reported DESC LIMIT 1";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String resultab = rs.getString("resultab");
                    if (isInteger(resultab)) {
                        if (Double.valueOf(resultab) >= 1000) {
                            disaggregate(gender, age);
                        }
                    }
                }
            }
            //Populate the indicatior value table with virallly unsuppressed
            dataElementId = 42;
            indicatorPersister.persist(dataElementId, 13, stateId, lgaId, facilityId, agem1, reportDate);         // male <1
            indicatorPersister.persist(dataElementId, 1, stateId, lgaId, facilityId, agef1, reportDate);        //female <1

            indicatorPersister.persist(dataElementId, 14, stateId, lgaId, facilityId, agem2, reportDate);         // male 1-4
            indicatorPersister.persist(dataElementId, 2, stateId, lgaId, facilityId, agef2, reportDate);        //female 1-4

            indicatorPersister.persist(dataElementId, 15, stateId, lgaId, facilityId, agem3, reportDate);         // male  5-9
            indicatorPersister.persist(dataElementId, 3, stateId, lgaId, facilityId, agef3, reportDate);        //female 5-9

            indicatorPersister.persist(dataElementId, 16, stateId, lgaId, facilityId, agem4, reportDate);         // male 10-14
            indicatorPersister.persist(dataElementId, 4, stateId, lgaId, facilityId, agef4, reportDate);        //female 10-14

            indicatorPersister.persist(dataElementId, 17, stateId, lgaId, facilityId, agem5, reportDate);         // male 15-19
            indicatorPersister.persist(dataElementId, 5, stateId, lgaId, facilityId, agef5, reportDate);        //female 15-19

            indicatorPersister.persist(dataElementId, 18, stateId, lgaId, facilityId, agem6, reportDate);         // male 20-24
            indicatorPersister.persist(dataElementId, 6, stateId, lgaId, facilityId, agef6, reportDate);        //female 20-24

            indicatorPersister.persist(dataElementId, 19, stateId, lgaId, facilityId, agem7, reportDate);         // male 25-29
            indicatorPersister.persist(dataElementId, 7, stateId, lgaId, facilityId, agef7, reportDate);        //female 25-29

            indicatorPersister.persist(dataElementId, 20, stateId, lgaId, facilityId, agem8, reportDate);         // male 30-34
            indicatorPersister.persist(dataElementId, 8, stateId, lgaId, facilityId, agef8, reportDate);        //female 30-34

            indicatorPersister.persist(dataElementId, 21, stateId, lgaId, facilityId, agem9, reportDate);         // male 35-39
            indicatorPersister.persist(dataElementId, 9, stateId, lgaId, facilityId, agef9, reportDate);        //female 35-39

            indicatorPersister.persist(dataElementId, 22, stateId, lgaId, facilityId, agem10, reportDate);         // male 40-44
            indicatorPersister.persist(dataElementId, 10, stateId, lgaId, facilityId, agef10, reportDate);        //female 40-44

            indicatorPersister.persist(dataElementId, 23, stateId, lgaId, facilityId, agem11, reportDate);         // male 45-49
            indicatorPersister.persist(dataElementId, 11, stateId, lgaId, facilityId, agef11, reportDate);        //female 45-49

            indicatorPersister.persist(dataElementId, 24, stateId, lgaId, facilityId, agem12, reportDate);         // male 50+
            indicatorPersister.persist(dataElementId, 12, stateId, lgaId, facilityId, agef12, reportDate);        //female 50+

            initVariables();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                //Check if the last viral load before the reporting month is less than 1000
                query = "SELECT * FROM lab WHERE patient_id = " + patientId + " ORDER BY date_reported DESC LIMIT 1";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String resultab = rs.getString("resultab");

                    if (isInteger(resultab)) {
                        if (Double.valueOf(resultab) >= 1000) {
                            String dateReported = DateUtil.parseDateToString(rs.getDate("date_reported"), "yyyy-MM-dd");

                            Date date = DateUtil.addDay(rs.getDate("date_reported"), 30);
                            String dateEnd = DateUtil.parseDateToString(date, "yyyy-MM-dd");

                            //Number VL test unsuppressed during the reporting period who attended EAC
                            query = "SELECT patient_id FROM eac WHERE patient_id = " + patientId + " AND  ((date_eac1 >= " + dateReported + "  AND date_eac1 <=  " + dateEnd + ") OR (date_eac2 >= " + dateReported + "  AND date_eac2 <=  " + dateEnd + ")   OR (date_eac3 >= " + dateReported + "  AND date_eac3 <=  " + dateEnd + "))";
                            preparedStatement = jdbcUtil.getStatement(query);
                            ResultSet rs1 = preparedStatement.executeQuery();
                            if (rs1.next()) {
                                disaggregate(gender, age);
                            }

                        }
                    }
                }
            }
            //Populate the report parameter map with values computed for Eligible for VL
            dataElementId = 43;
            indicatorPersister.persist(dataElementId, 13, stateId, lgaId, facilityId, agem1, reportDate);         // male <1
            indicatorPersister.persist(dataElementId, 1, stateId, lgaId, facilityId, agef1, reportDate);        //female <1

            indicatorPersister.persist(dataElementId, 14, stateId, lgaId, facilityId, agem2, reportDate);         // male 1-4
            indicatorPersister.persist(dataElementId, 2, stateId, lgaId, facilityId, agef2, reportDate);        //female 1-4

            indicatorPersister.persist(dataElementId, 15, stateId, lgaId, facilityId, agem3, reportDate);         // male  5-9
            indicatorPersister.persist(dataElementId, 3, stateId, lgaId, facilityId, agef3, reportDate);        //female 5-9

            indicatorPersister.persist(dataElementId, 16, stateId, lgaId, facilityId, agem4, reportDate);         // male 10-14
            indicatorPersister.persist(dataElementId, 4, stateId, lgaId, facilityId, agef4, reportDate);        //female 10-14

            indicatorPersister.persist(dataElementId, 17, stateId, lgaId, facilityId, agem5, reportDate);         // male 15-19
            indicatorPersister.persist(dataElementId, 5, stateId, lgaId, facilityId, agef5, reportDate);        //female 15-19

            indicatorPersister.persist(dataElementId, 18, stateId, lgaId, facilityId, agem6, reportDate);         // male 20-24
            indicatorPersister.persist(dataElementId, 6, stateId, lgaId, facilityId, agef6, reportDate);        //female 20-24

            indicatorPersister.persist(dataElementId, 19, stateId, lgaId, facilityId, agem7, reportDate);         // male 25-29
            indicatorPersister.persist(dataElementId, 7, stateId, lgaId, facilityId, agef7, reportDate);        //female 25-29

            indicatorPersister.persist(dataElementId, 20, stateId, lgaId, facilityId, agem8, reportDate);         // male 30-34
            indicatorPersister.persist(dataElementId, 8, stateId, lgaId, facilityId, agef8, reportDate);        //female 30-34

            indicatorPersister.persist(dataElementId, 21, stateId, lgaId, facilityId, agem9, reportDate);         // male 35-39
            indicatorPersister.persist(dataElementId, 9, stateId, lgaId, facilityId, agef9, reportDate);        //female 35-39

            indicatorPersister.persist(dataElementId, 22, stateId, lgaId, facilityId, agem10, reportDate);         // male 40-44
            indicatorPersister.persist(dataElementId, 10, stateId, lgaId, facilityId, agef10, reportDate);        //female 40-44

            indicatorPersister.persist(dataElementId, 23, stateId, lgaId, facilityId, agem11, reportDate);         // male 45-49
            indicatorPersister.persist(dataElementId, 11, stateId, lgaId, facilityId, agef11, reportDate);        //female 45-49

            indicatorPersister.persist(dataElementId, 24, stateId, lgaId, facilityId, agem12, reportDate);         // male 50+
            indicatorPersister.persist(dataElementId, 12, stateId, lgaId, facilityId, agef12, reportDate);        //female 50+


            System.out.println("Computing ART7.....");
            //ART 7
            //Total number of people living with HIV known to have died during the month
            initVariables();
            query = "SELECT DISTINCT gender, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age FROM patient WHERE facility_id = " + facilityId + " AND current_status = 'Known Death' AND date_current_status = '" + reportDate + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                if (gender.trim().equalsIgnoreCase("Male")) {
                    agem1++;
                } else {
                    agef1++;
                }
            }

            System.out.println("Computing ART8.....");
            //ART 8
            //Number of People living with HIV who are lost to follow up during the month
            initVariables();
            query = "SELECT DISTINCT gender, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age FROM patient WHERE patient_id IN (SELECT DISTINCT patient_id FROM statushistory WHERE facility_id = " + facilityId + " AND current_status = 'Lost to Follow Up' AND date_current_status = '" + reportDate + "')";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                if (gender.trim().equalsIgnoreCase("Male")) {
                    agem1++;
                } else {
                    agef1++;
                }
            }

            dataElementId = 19;
            indicatorPersister.persist(dataElementId, 13, stateId, lgaId, facilityId, agem1, reportDate);         // male
            indicatorPersister.persist(dataElementId, 1, stateId, lgaId, facilityId, agef1, reportDate);        //female

            //Indicattor  41
            //Number of patients eligible for viral load test during the reporting month
            System.out.println(".....Compute eligible for viral load test");
            initVariables();

            query = "SELECT DISTINCT gender, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age FROM patient WHERE facility_id = " + facilityId + "  AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND viral_load_due_date = '" + reportDate + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }
            //Populate the report parameter map with values computed for Eligible for VL
            dataElementId = 41;
            indicatorPersister.persist(dataElementId, 13, stateId, lgaId, facilityId, agem1, reportDate);         // male <1
            indicatorPersister.persist(dataElementId, 1, stateId, lgaId, facilityId, agef1, reportDate);        //female <1

            indicatorPersister.persist(dataElementId, 14, stateId, lgaId, facilityId, agem2, reportDate);         // male 1-4
            indicatorPersister.persist(dataElementId, 2, stateId, lgaId, facilityId, agef2, reportDate);        //female 1-4

            indicatorPersister.persist(dataElementId, 15, stateId, lgaId, facilityId, agem3, reportDate);         // male  5-9
            indicatorPersister.persist(dataElementId, 3, stateId, lgaId, facilityId, agef3, reportDate);        //female 5-9

            indicatorPersister.persist(dataElementId, 16, stateId, lgaId, facilityId, agem4, reportDate);         // male 10-14
            indicatorPersister.persist(dataElementId, 4, stateId, lgaId, facilityId, agef4, reportDate);        //female 10-14

            indicatorPersister.persist(dataElementId, 17, stateId, lgaId, facilityId, agem5, reportDate);         // male 15-19
            indicatorPersister.persist(dataElementId, 5, stateId, lgaId, facilityId, agef5, reportDate);        //female 15-19

            indicatorPersister.persist(dataElementId, 18, stateId, lgaId, facilityId, agem6, reportDate);         // male 20-24
            indicatorPersister.persist(dataElementId, 6, stateId, lgaId, facilityId, agef6, reportDate);        //female 20-24

            indicatorPersister.persist(dataElementId, 19, stateId, lgaId, facilityId, agem7, reportDate);         // male 25-29
            indicatorPersister.persist(dataElementId, 7, stateId, lgaId, facilityId, agef7, reportDate);        //female 25-29

            indicatorPersister.persist(dataElementId, 20, stateId, lgaId, facilityId, agem8, reportDate);         // male 30-34
            indicatorPersister.persist(dataElementId, 8, stateId, lgaId, facilityId, agef8, reportDate);        //female 30-34

            indicatorPersister.persist(dataElementId, 21, stateId, lgaId, facilityId, agem9, reportDate);         // male 35-39
            indicatorPersister.persist(dataElementId, 9, stateId, lgaId, facilityId, agef9, reportDate);        //female 35-39

            indicatorPersister.persist(dataElementId, 22, stateId, lgaId, facilityId, agem10, reportDate);         // male 40-44
            indicatorPersister.persist(dataElementId, 10, stateId, lgaId, facilityId, agef10, reportDate);        //female 40-44

            indicatorPersister.persist(dataElementId, 23, stateId, lgaId, facilityId, agem11, reportDate);         // male 45-49
            indicatorPersister.persist(dataElementId, 11, stateId, lgaId, facilityId, agef11, reportDate);        //female 45-49

            indicatorPersister.persist(dataElementId, 24, stateId, lgaId, facilityId, agem12, reportDate);         // male 50+
            indicatorPersister.persist(dataElementId, 12, stateId, lgaId, facilityId, agef12, reportDate);        //female 50+

            System.out.println("......Computing TB documentation");

            //Clinic visit with documented TB status
            //denominator - all clinic visits during the reporting month
            query = "SELECT COUNT(*) AS count FROM visit WHERE date_visit =  '" + reportDate + "'";
            dataElementId = 44;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, getCount(query), reportDate);        //Total clinic visti

            //numerator - all clinic visit during the reporting month with TB status not equal to null
            query = "SELECT COUNT(*) AS count FROM visit WHERE date_visit =  '" + reportDate + "' AND tb_status != '' AND tb_status IS NOT NULL";
            dataElementId = 45;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, getCount(query), reportDate);        //Total clinic visti

            executeUpdate("DROP TABLE IF EXISTS appoint");
            query = "CREATE TEMPORARY TABLE appoint AS SELECT ph.patient_id, ph.date_visit, ph.next_appointment, pa.gender, DATEDIFF(YEAR, pa.date_birth, '" + reportDate + "') AS age FROM pharm ph, patient pa WHERE ph.patient_id = pa.patient_id AND ph.next_appointment = '" + reportDate + "')";
            executeUpdate(query);

            //denominator - all refill appointment during the repiorting month
            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM appoint";
            dataElementId = 46;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, getCount(query), reportDate);        //Total refill appointtment

            query = "SELECT COUNT(DISTINCT patient_id) AS count FROM appoint WHERE patient_id NOT IN (SELECT patient_id FROM pharm WHERE date_visit =  '" + reportDate + "')";

            dataElementId = 47;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, getCount(query), reportDate);        //Total missed appointment

            //Missed appointment
            executeUpdate("DROP TABLE IF EXISTS missed");
            query = "CREATE TEMPORARY TABLE missed AS SELECT patient_id, date_visit, next_appointment, gender, age FROM appoint WHERE patient_id NOT IN (SELECT patient_id FROM pharm WHERE date_visit =  '" + reportDate + "')";
            executeUpdate(query);

            System.out.println("......Computing number of patient who returned into care");
            query = "SELECT * FROM missed";
            preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                long patientId = rs.getLong("patient_id");
                String gender = rs.getString("gender");
                int age = rs.getInt("age");


                String nextAppointment = DateUtil.parseDateToString(rs.getDate("next_appointment"), "yyyy-MM-dd");

                Date date = DateUtil.addDay(rs.getDate("next_appointment"), 30);
                String dateEnd = DateUtil.parseDateToString(date, "yyyy-MM-dd");

                System.out.println(".... Next Appointment Database value:  " + rs.getDate("next_appointment"));
                System.out.println(".... Next Appointment " + nextAppointment);

                //Number  of patients who missed apointment and returned to care
                query = "SELECT patient_id FROM pharm WHERE patient_id = " + patientId + " AND  (date_visit >= " + nextAppointment + "  AND date_visit <=  " + dateEnd + ")";
                preparedStatement = jdbcUtil.getStatement(query);
                ResultSet rs1 = preparedStatement.executeQuery();

                System.out.println(".... Resultset query successful ");

                if (rs1.next()) {
                    disaggregate(gender, age);
                }
            }

            System.out.println("......Computing number of patient who returned into care2");

            //Populate number of patient who returned into care
            dataElementId = 48;
            indicatorPersister.persist(dataElementId, 13, stateId, lgaId, facilityId, agem1, reportDate);         // male <1
            indicatorPersister.persist(dataElementId, 1, stateId, lgaId, facilityId, agef1, reportDate);        //female <1

            indicatorPersister.persist(dataElementId, 14, stateId, lgaId, facilityId, agem2, reportDate);         // male 1-4
            indicatorPersister.persist(dataElementId, 2, stateId, lgaId, facilityId, agef2, reportDate);        //female 1-4

            indicatorPersister.persist(dataElementId, 15, stateId, lgaId, facilityId, agem3, reportDate);         // male  5-9
            indicatorPersister.persist(dataElementId, 3, stateId, lgaId, facilityId, agef3, reportDate);        //female 5-9

            indicatorPersister.persist(dataElementId, 16, stateId, lgaId, facilityId, agem4, reportDate);         // male 10-14
            indicatorPersister.persist(dataElementId, 4, stateId, lgaId, facilityId, agef4, reportDate);        //female 10-14

            indicatorPersister.persist(dataElementId, 17, stateId, lgaId, facilityId, agem5, reportDate);         // male 15-19
            indicatorPersister.persist(dataElementId, 5, stateId, lgaId, facilityId, agef5, reportDate);        //female 15-19

            indicatorPersister.persist(dataElementId, 18, stateId, lgaId, facilityId, agem6, reportDate);         // male 20-24
            indicatorPersister.persist(dataElementId, 6, stateId, lgaId, facilityId, agef6, reportDate);        //female 20-24

            indicatorPersister.persist(dataElementId, 19, stateId, lgaId, facilityId, agem7, reportDate);         // male 25-29
            indicatorPersister.persist(dataElementId, 7, stateId, lgaId, facilityId, agef7, reportDate);        //female 25-29

            indicatorPersister.persist(dataElementId, 20, stateId, lgaId, facilityId, agem8, reportDate);         // male 30-34
            indicatorPersister.persist(dataElementId, 8, stateId, lgaId, facilityId, agef8, reportDate);        //female 30-34

            indicatorPersister.persist(dataElementId, 21, stateId, lgaId, facilityId, agem9, reportDate);         // male 35-39
            indicatorPersister.persist(dataElementId, 9, stateId, lgaId, facilityId, agef9, reportDate);        //female 35-39

            indicatorPersister.persist(dataElementId, 22, stateId, lgaId, facilityId, agem10, reportDate);         // male 40-44
            indicatorPersister.persist(dataElementId, 10, stateId, lgaId, facilityId, agef10, reportDate);        //female 40-44

            indicatorPersister.persist(dataElementId, 23, stateId, lgaId, facilityId, agem11, reportDate);         // male 45-49
            indicatorPersister.persist(dataElementId, 11, stateId, lgaId, facilityId, agef11, reportDate);        //female 45-49

            indicatorPersister.persist(dataElementId, 24, stateId, lgaId, facilityId, agem12, reportDate);         // male 50+
            indicatorPersister.persist(dataElementId, 12, stateId, lgaId, facilityId, agef12, reportDate);        //female 50+

            //Number of people living with HIV newly initiated on/transitioned to TLD during the month
            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age FROM patient WHERE facility_id = " + facilityId + " AND current_status IN (" + Constants.ClientStatus.ON_TREATMENT + ") AND DATEDIFF(DAY, date_last_refill + last_refill_duration, CURDATE()) <= " + Constants.LTFU.PEPFAR + " AND date_started IS NOT NULL ORDER BY current_status";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                rs = getLastRefillVisit(patientId);
                while (rs != null && rs.next()) {
                    long regimenId = rs.getLong("regimen_id");
                    Date dateLastRefill = rs.getDate("date_visit");
                    int duration = rs.getInt("duration");
                    int monthRefill = duration / 30;
                    if (monthRefill <= 0) {
                        monthRefill = 1;
                    }

                    if (dateLastRefill != null) {
                        //If the last refill date plus refill duration plus 28 days is before the last day of the reporting month this patient is LTFU     if(DateUtil.addYearMonthDay(lastRefill, duration+90, "day(s)").before(reportingDateEnd
                        if (!DateUtil.addYearMonthDay(dateLastRefill, duration + Constants.LTFU.PEPFAR, "DAY").after(reportingDate)) {

                            if (regimenId >= 116 && regimenId <= 119) {
                                disaggregate(gender, age);
                            }
                        }
                    }
                }
            }
            dataElementId = 49;
            indicatorPersister.persist(dataElementId, 13, stateId, lgaId, facilityId, agem1, reportDate);         // male <1
            indicatorPersister.persist(dataElementId, 1, stateId, lgaId, facilityId, agef1, reportDate);        //female <1

            indicatorPersister.persist(dataElementId, 14, stateId, lgaId, facilityId, agem2, reportDate);         // male 1-4
            indicatorPersister.persist(dataElementId, 2, stateId, lgaId, facilityId, agef2, reportDate);        //female 1-4

            indicatorPersister.persist(dataElementId, 15, stateId, lgaId, facilityId, agem3, reportDate);         // male  5-9
            indicatorPersister.persist(dataElementId, 3, stateId, lgaId, facilityId, agef3, reportDate);        //female 5-9

            indicatorPersister.persist(dataElementId, 16, stateId, lgaId, facilityId, agem4, reportDate);         // male 10-14
            indicatorPersister.persist(dataElementId, 4, stateId, lgaId, facilityId, agef4, reportDate);        //female 10-14

            indicatorPersister.persist(dataElementId, 17, stateId, lgaId, facilityId, agem5, reportDate);         // male 15-19
            indicatorPersister.persist(dataElementId, 5, stateId, lgaId, facilityId, agef5, reportDate);        //female 15-19

            indicatorPersister.persist(dataElementId, 18, stateId, lgaId, facilityId, agem6, reportDate);         // male 20-24
            indicatorPersister.persist(dataElementId, 6, stateId, lgaId, facilityId, agef6, reportDate);        //female 20-24

            indicatorPersister.persist(dataElementId, 19, stateId, lgaId, facilityId, agem7, reportDate);         // male 25-29
            indicatorPersister.persist(dataElementId, 7, stateId, lgaId, facilityId, agef7, reportDate);        //female 25-29

            indicatorPersister.persist(dataElementId, 20, stateId, lgaId, facilityId, agem8, reportDate);         // male 30-34
            indicatorPersister.persist(dataElementId, 8, stateId, lgaId, facilityId, agef8, reportDate);        //female 30-34

            indicatorPersister.persist(dataElementId, 21, stateId, lgaId, facilityId, agem9, reportDate);         // male 35-39
            indicatorPersister.persist(dataElementId, 9, stateId, lgaId, facilityId, agef9, reportDate);        //female 35-39

            indicatorPersister.persist(dataElementId, 22, stateId, lgaId, facilityId, agem10, reportDate);         // male 40-44
            indicatorPersister.persist(dataElementId, 10, stateId, lgaId, facilityId, agef10, reportDate);        //female 40-44

            indicatorPersister.persist(dataElementId, 23, stateId, lgaId, facilityId, agem11, reportDate);         // male 45-49
            indicatorPersister.persist(dataElementId, 11, stateId, lgaId, facilityId, agef11, reportDate);        //female 45-49

            indicatorPersister.persist(dataElementId, 24, stateId, lgaId, facilityId, agem12, reportDate);         // male 50+
            indicatorPersister.persist(dataElementId, 12, stateId, lgaId, facilityId, agef12, reportDate);        //female 50+


            int mms = 0;
            int mmd = 0;
            int carc = 0;
            int cparp = 0;

            query = "SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" + reportDate + "') AS age, devolve.type_dmoc FROM patient JOIN devolve ON patient.patient_id = devolve.patient_id WHERE devolve.date_devolved = '" + reportDate + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                String typeDmoc = resultSet.getString("type_dmoc");

                if (typeDmoc.equalsIgnoreCase("MMS")) mms++;
                if (typeDmoc.equalsIgnoreCase("MMD")) mmd++;
                if (typeDmoc.equalsIgnoreCase("CARC")) carc++;
                if (typeDmoc.equalsIgnoreCase("CPARP")) cparp++;

                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for Eligible for VL

            dataElementId = 50;

            indicatorPersister.persist(dataElementId, 13, stateId, lgaId, facilityId, agem1, reportDate);         // male <1
            indicatorPersister.persist(dataElementId, 1, stateId, lgaId, facilityId, agef1, reportDate);        //female <1

            indicatorPersister.persist(dataElementId, 14, stateId, lgaId, facilityId, agem2, reportDate);         // male 1-4
            indicatorPersister.persist(dataElementId, 2, stateId, lgaId, facilityId, agef2, reportDate);        //female 1-4

            indicatorPersister.persist(dataElementId, 15, stateId, lgaId, facilityId, agem3, reportDate);         // male  5-9
            indicatorPersister.persist(dataElementId, 3, stateId, lgaId, facilityId, agef3, reportDate);        //female 5-9

            indicatorPersister.persist(dataElementId, 16, stateId, lgaId, facilityId, agem4, reportDate);         // male 10-14
            indicatorPersister.persist(dataElementId, 4, stateId, lgaId, facilityId, agef4, reportDate);        //female 10-14

            indicatorPersister.persist(dataElementId, 17, stateId, lgaId, facilityId, agem5, reportDate);         // male 15-19
            indicatorPersister.persist(dataElementId, 5, stateId, lgaId, facilityId, agef5, reportDate);        //female 15-19

            indicatorPersister.persist(dataElementId, 18, stateId, lgaId, facilityId, agem6, reportDate);         // male 20-24
            indicatorPersister.persist(dataElementId, 6, stateId, lgaId, facilityId, agef6, reportDate);        //female 20-24

            indicatorPersister.persist(dataElementId, 19, stateId, lgaId, facilityId, agem7, reportDate);         // male 25-29
            indicatorPersister.persist(dataElementId, 7, stateId, lgaId, facilityId, agef7, reportDate);        //female 25-29

            indicatorPersister.persist(dataElementId, 20, stateId, lgaId, facilityId, agem8, reportDate);         // male 30-34
            indicatorPersister.persist(dataElementId, 8, stateId, lgaId, facilityId, agef8, reportDate);        //female 30-34

            indicatorPersister.persist(dataElementId, 21, stateId, lgaId, facilityId, agem9, reportDate);         // male 35-39
            indicatorPersister.persist(dataElementId, 9, stateId, lgaId, facilityId, agef9, reportDate);        //female 35-39

            indicatorPersister.persist(dataElementId, 22, stateId, lgaId, facilityId, agem10, reportDate);         // male 40-44
            indicatorPersister.persist(dataElementId, 10, stateId, lgaId, facilityId, agef10, reportDate);        //female 40-44

            indicatorPersister.persist(dataElementId, 23, stateId, lgaId, facilityId, agem11, reportDate);         // male 45-49
            indicatorPersister.persist(dataElementId, 11, stateId, lgaId, facilityId, agef11, reportDate);        //female 45-49

            indicatorPersister.persist(dataElementId, 24, stateId, lgaId, facilityId, agem12, reportDate);         // male 50+
            indicatorPersister.persist(dataElementId, 12, stateId, lgaId, facilityId, agef12, reportDate);        //female 50+


            dataElementId = 51;

            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, mms, reportDate);         // mms

            dataElementId = 52;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, mmd, reportDate);        //mmd

            dataElementId = 53;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, carc, reportDate);         // carc

            dataElementId = 54;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, cparp, reportDate);        //cparp


            //Number of people living with HIV newly initiated on/transitioned to TLD during the month
            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age FROM patient WHERE facility_id = " + facilityId + " AND  date_started = '" + reportDate + "' AND  regimen LIKE '%DTG%'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long patientId = resultSet.getLong("patient_id");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            dataElementId = 49;
            indicatorPersister.persist(dataElementId, 13, stateId, lgaId, facilityId, agem1, reportDate);         // male <1
            indicatorPersister.persist(dataElementId, 1, stateId, lgaId, facilityId, agef1, reportDate);        //female <1

            indicatorPersister.persist(dataElementId, 14, stateId, lgaId, facilityId, agem2, reportDate);         // male 1-4
            indicatorPersister.persist(dataElementId, 2, stateId, lgaId, facilityId, agef2, reportDate);        //female 1-4

            indicatorPersister.persist(dataElementId, 15, stateId, lgaId, facilityId, agem3, reportDate);         // male  5-9
            indicatorPersister.persist(dataElementId, 3, stateId, lgaId, facilityId, agef3, reportDate);        //female 5-9

            indicatorPersister.persist(dataElementId, 16, stateId, lgaId, facilityId, agem4, reportDate);         // male 10-14
            indicatorPersister.persist(dataElementId, 4, stateId, lgaId, facilityId, agef4, reportDate);        //female 10-14

            indicatorPersister.persist(dataElementId, 17, stateId, lgaId, facilityId, agem5, reportDate);         // male 15-19
            indicatorPersister.persist(dataElementId, 5, stateId, lgaId, facilityId, agef5, reportDate);        //female 15-19

            indicatorPersister.persist(dataElementId, 18, stateId, lgaId, facilityId, agem6, reportDate);         // male 20-24
            indicatorPersister.persist(dataElementId, 6, stateId, lgaId, facilityId, agef6, reportDate);        //female 20-24

            indicatorPersister.persist(dataElementId, 19, stateId, lgaId, facilityId, agem7, reportDate);         // male 25-29
            indicatorPersister.persist(dataElementId, 7, stateId, lgaId, facilityId, agef7, reportDate);        //female 25-29

            indicatorPersister.persist(dataElementId, 20, stateId, lgaId, facilityId, agem8, reportDate);         // male 30-34
            indicatorPersister.persist(dataElementId, 8, stateId, lgaId, facilityId, agef8, reportDate);        //female 30-34

            indicatorPersister.persist(dataElementId, 21, stateId, lgaId, facilityId, agem9, reportDate);         // male 35-39
            indicatorPersister.persist(dataElementId, 9, stateId, lgaId, facilityId, agef9, reportDate);        //female 35-39

            indicatorPersister.persist(dataElementId, 22, stateId, lgaId, facilityId, agem10, reportDate);         // male 40-44
            indicatorPersister.persist(dataElementId, 10, stateId, lgaId, facilityId, agef10, reportDate);        //female 40-44

            indicatorPersister.persist(dataElementId, 23, stateId, lgaId, facilityId, agem11, reportDate);         // male 45-49
            indicatorPersister.persist(dataElementId, 11, stateId, lgaId, facilityId, agef11, reportDate);        //female 45-49

            indicatorPersister.persist(dataElementId, 24, stateId, lgaId, facilityId, agem12, reportDate);         // male 50+
            indicatorPersister.persist(dataElementId, 12, stateId, lgaId, facilityId, agef12, reportDate);        //female 50+

            System.out.println("Completed");


        } catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private void disaggregate(String gender, int age) {
        if (gender.trim().equalsIgnoreCase("Male")) {
            if (age < 1) {
                agem1++;
            } else {
                if (age >= 1 && age <= 4) {
                    agem2++;
                } else {
                    if (age >= 5 && age <= 9) {
                        agem3++;
                    } else {
                        if (age >= 10 && age <= 14) {
                            agem4++;
                        } else {
                            if (age >= 15 && age <= 19) {
                                agem5++;
                            } else {
                                if (age >= 20 && age <= 24) {
                                    agem6++;
                                } else {
                                    if (age >= 25 && age <= 29) {
                                        agem7++;
                                    } else {
                                        if (age >= 30 && age <= 34) {
                                            agem8++;
                                        } else {
                                            if (age >= 35 && age <= 39) {
                                                agem9++;
                                            } else {
                                                if (age >= 40 && age <= 44) {
                                                    agem10++;
                                                } else {
                                                    if (age >= 45 && age <= 49) {
                                                        agem11++;
                                                    } else {
                                                        if (age >= 50) {
                                                            agem12++;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (age < 1) {
                agef1++;
            } else {
                if (age >= 1 && age <= 4) {
                    agef2++;
                } else {
                    if (age >= 5 && age <= 9) {
                        agef3++;
                    } else {
                        if (age >= 10 && age <= 14) {
                            agef4++;
                        } else {
                            if (age >= 15 && age <= 19) {
                                agef5++;
                            } else {
                                if (age >= 20 && age <= 24) {
                                    agef6++;
                                } else {
                                    if (age >= 25 && age <= 29) {
                                        agef7++;
                                    } else {
                                        if (age >= 30 && age <= 34) {
                                            agef8++;
                                        } else {
                                            if (age >= 35 && age <= 39) {
                                                agef9++;
                                            } else {
                                                if (age >= 40 && age <= 44) {
                                                    agef10++;
                                                } else {
                                                    if (age >= 45 && age <= 49) {
                                                        agef11++;
                                                    } else {
                                                        if (age >= 50) {
                                                            agef12++;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return rs;
    }

    private void executeUpdate(String query) {
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private boolean found(String query) {
        boolean found = false;
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                found = true;
            }
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return found;
    }

    private ResultSet getLastRefillVisit(long patientId) {
        ResultSet rs = null;
        try {
            query = "SELECT DISTINCT regimentype_id, regimen_id, date_visit, duration FROM pharm WHERE patient_id = " + patientId + " ORDER BY date_visit DESC LIMIT 1";
            preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return rs;
    }

    private void initVariables() {
        agem1 = 0;
        agem2 = 0;
        agem3 = 0;
        agem4 = 0;
        agem5 = 0;
        agem6 = 0;
        agem7 = 0;
        agem8 = 0;
        agem9 = 0;
        agem10 = 0;
        agem11 = 0;
        agem12 = 0;

        agef1 = 0;
        agef2 = 0;
        agef3 = 0;
        agef4 = 0;
        agef5 = 0;
        agef6 = 0;
        agef7 = 0;
        agef8 = 0;
        agef9 = 0;
        agef10 = 0;
        agef11 = 0;
        agef12 = 0;

        agem13_1 = 0;
        agem14_1 = 0;
        agem15_1 = 0;
        agem13_2 = 0;
        agem14_2 = 0;
        agem15_2 = 0;
        agef13_1 = 0;
        agef14_1 = 0;
        agef15_1 = 0;
        agef13_2 = 0;
        agef14_2 = 0;
        agef15_2 = 0;
        preg = 0;
        feeding = 0;
        tbm = 0;
        tbf = 0;
    }

    private void getStateId(long facilityId) {
        try {
            query = "SELECT state_id, lga_id FROM facility  WHERE facility_id = " + facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                stateId = rs.getLong("state_id");
                lgaId = rs.getLong("lga_id");
            }
        } catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private int getCount(String query) {
        int count = 0;
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return count;
    }

}

//create a temporary table of date of the latest status change on or before the last day of reporting month
//executeUpdate("DROP TABLE IF EXISTS currentstatus");
//query = "CREATE TEMPORARY TABLE currentstatus AS SELECT DISTINCT patient_id, MAX(date_current_status) AS date_status FROM statushistory WHERE facility_id = " + id + " AND date_current_status <= '" + reportingDateEnd + "' GROUP BY patient_id";
//preparedStatement = jdbcUtil.getStatement(query);
//preparedStatement.executeUpdate();
//query = "SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" + reportDate + "') AS age, patient.date_registration, patient.status_registration, patient.date_started, statushistory.current_status, currentstatus.date_status "
//+ " FROM patient JOIN statushistory ON patient.patient_id = statushistory.patient_id JOIN currentstatus ON patient.patient_id = currentstatus.patient_id WHERE patient.facility_id = " + id + " AND statushistory.facility_id = " + id + " AND statushistory.patient_id = currentstatus.patient_id AND statushistory.date_current_status = currentstatus.date_status";
// create a temporary table of date of the latest regimen change on or before the last day of reporting month
//executeUpdate("DROP TABLE IF EXISTS currentregimen");
//query = "CREATE TEMPORARY TABLE currentregimen AS SELECT DISTINCT patient_id, MAX(date_visit) AS date_visit FROM regimenhistory WHERE facility_id = " + id + " AND date_visit <= '" + reportingDateEnd + "' GROUP BY patient_id";
//preparedStatement = jdbcUtil.getStatement(query);
//preparedStatement.executeUpdate();
//query = "SELECT DISTINCT patient.patient_id, patient.gender, DATEDIFF(YEAR, patient.date_birth, '" + reportDate + "') AS age, patient.date_registration, patient.date_started, regimenhistory.regimenType, currentregimen.date_visit "
//+ " FROM patient JOIN regimenhistory ON patient.patient_id = regimenhistory.patient_id JOIN currentregimen ON patient.patient_id = currentregimen.patient_id WHERE patient.facility_id = " + id + " AND regimenhistory.facility_id = " + id + " AND regimenhistory.patient_id = currentregimen.patient_id AND regimenhistory.date_visit = currentregimen.date_visit";
//preparedStatement = jdbcUtil.getStatement(query);
//query = "SELECT patient_id FROM clinic WHERE facility_id = " + id + " AND date_visit BETWEEN DATEADD('MONTH', -3, '" + reportingDateEnd + "') AND " + reportingDateEnd + " AND pregnant = 1";


//"Pre-ART Transfer Out", "ART Transfer Out", "Stopped Treatment", "Lost to Follow Up", "Known Death", 
    

