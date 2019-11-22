/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.ndr;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.exchange.ndr.schema.CodedSimpleType;
import org.fhi360.lamis.exchange.ndr.schema.EncountersType;
import org.fhi360.lamis.exchange.ndr.schema.HIVEncounterType;
import org.fhi360.lamis.model.repositories.RegimenTypeRepository;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.StringUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.xml.datatype.DatatypeConfigurationException;

/**
 * @author user1
 */
public class EncountersTypeMapper {
    private final RegimenTypeRepository regimenTypeRepository = ContextProvider.getBean(RegimenTypeRepository.class);

    public EncountersTypeMapper() {
    }

    public EncountersType encounterType(long patientId) {
        EncountersType encounter = new EncountersType();
        String databaseName = "mysql"; //new PropertyAccessor().getDatabaseName();
        JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);
        //If the statusCode is INITIAL all record is retrieved, if the status Code is UPDATED then the last message timestamp is used to retrieve the appropriate records
        String query = "SELECT clinic_id, date_visit, clinic_stage, func_status, bp, tb_status, body_weight, height, " +
                "next_appointment FROM clinic WHERE patient_id = " + patientId;
        jdbcTemplate.query(query, resultSet -> {
            String clinicStage = StringUtils.trimToEmpty(resultSet.getString("clinic_stage"));
            String funcStatus = StringUtils.trimToEmpty(resultSet.getString("func_status"));
            String bp = StringUtils.trimToEmpty(resultSet.getString("bp"));
            String tbStatus = StringUtils.trimToEmpty(resultSet.getString("tb_status"));

            //Encounters
            HIVEncounterType hivEncounter = new HIVEncounterType();
            try {
                hivEncounter.setVisitDate(DateUtil.getXmlDate(resultSet.getDate("date_visit")));
            } catch (DatatypeConfigurationException e) {
                e.printStackTrace();
            }
            hivEncounter.setVisitID(Long.toString(resultSet.getLong("clinic_id")));

            if (!clinicStage.isEmpty()) {
                clinicStage = CodeSetResolver.getCode("WHO_STAGE", clinicStage);
                if (!clinicStage.isEmpty()) hivEncounter.setWHOClinicalStage(clinicStage);
            }
            if (!funcStatus.isEmpty()) {
                funcStatus = CodeSetResolver.getCode("FUNCTIONAL_STATUS", funcStatus);
                if (!funcStatus.isEmpty()) hivEncounter.setFunctionalStatus(funcStatus);
            }
            if (!tbStatus.isEmpty()) {
                tbStatus = CodeSetResolver.getCode("TB_STATUS", tbStatus);
                if (!tbStatus.isEmpty()) hivEncounter.setTBStatus(tbStatus);
            }
            if (!bp.isEmpty()) hivEncounter.setBloodPressure(bp);

            int weight = (int) resultSet.getDouble("body_weight");
            int height = (int) resultSet.getDouble("height");
            if (weight > 0) hivEncounter.setWeight(weight);
            if (height > 0) hivEncounter.setChildHeight(height);
            if (resultSet.getDate("next_appointment") != null) {
                try {
                    hivEncounter.setNextAppointmentDate(DateUtil.getXmlDate(resultSet.getDate("next_appointment")));
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }
            }

            //Check for refill visit close to the date of this clinic visit
            //and populate the refill variables in the encounter object
            //ARV drug

            String query1 = "SELECT DISTINCT regimentype_id, regimen_id, duration FROM pharmacy WHERE patient_id = "
                    + patientId + " AND date_visit >= DATE_ADD(" + resultSet.getDate("date_visit") + ", " +
                    "INTERVAL -7 DAY) AND date_visit <= DATE_ADD(" + resultSet.getDate("date_visit") + "," +
                    " INTERVAL 7 DAY) AND regimentype_id IN (1, 2, 3, 4, 14)";
            if (databaseName.equalsIgnoreCase("h2"))
                query1 = "SELECT DISTINCT regimentype_id, regimen_id, duration FROM pharmacy WHERE patient_id = " +
                        patientId + " AND date_visit >= DATE_ADD('" + resultSet.getDate("date_visit") + "', " +
                        "INTERVAL -7 DAY) AND date_visit <= DATE_ADD('" + resultSet.getDate("date_visit") + "', " +
                        "INTERVAL 7 DAY) AND regimentype_id IN (1, 2, 3, 4, 14)";

            jdbcTemplate.query(query1, rs -> {
                CodedSimpleType cst = CodeSetResolver.getRegimenById(rs.getLong("regimen_id"));
                if (cst.getCode() != null) hivEncounter.setARVDrugRegimen(cst);
            });

            //cotrim
            query1 = "SELECT DISTINCT regimentype_id, regimen_id, duration FROM pharmacy WHERE patient_id = " +
                    patientId + " AND date_visit >= DATE_ADD(" + resultSet.getDate("date_visit") + "," +
                    " INTERVAL -7 DAY) AND date_visit <= DATE_ADD(" + resultSet.getDate("date_visit") + "," +
                    " INTERVAL 7 DAY) AND regimentype_id = 8";
            if (databaseName.equalsIgnoreCase("h2"))
                query1 = "SELECT DISTINCT regimentype_id, regimen_id, duration FROM pharmacy WHERE patient_id = " +
                        patientId + " AND date_visit >= DATE_ADD('" + resultSet.getDate("date_visit") + "'," +
                        " INTERVAL -7 DAY) AND date_visit <= DATE_ADD('" + resultSet.getDate("date_visit") + "'," +
                        " INTERVAL 7 DAY) AND regimentype_id = 8";

            jdbcTemplate.query(query1, rs -> {
                String description = regimenTypeRepository.getOne(rs.getLong("regimentype_id")).getDescription();
                CodedSimpleType cst = CodeSetResolver.getCodedSimpleType("REGIMEN_TYPE", description);
                if (cst.getCode() != null) hivEncounter.setCotrimoxazoleDose(cst);
            });

            //Check for lab investigation close to the date of this clinic visit
            //and populate the CD4 variables in the encounter object
            query1 = "SELECT DISTINCT date_reported, resultab, resultpc FROM laboratory WHERE patient_id = " +
                    patientId + " AND date_reported >= DATE_ADD(" + resultSet.getDate("date_visit") +
                    ", INTERVAL -7 DAY) AND date_reported <= DATE_ADD(" + resultSet.getDate("date_visit") +
                    ", INTERVAL -7 DAY) AND labtest_id = 1";
            jdbcTemplate.query(query1, rs -> {
                String resultab = rs.getString("resultab") == null ? "" : rs.getString("resultab");
                String resultpc = rs.getString("resultpc") == null ? "" : rs.getString("resultpc");

                int cd4 = resultab.isEmpty() ? 0 : StringUtil.isInteger(resultab) ? (int) Double.parseDouble(StringUtil.stripCommas(resultab)) : 0;
                int cd4p = resultpc.isEmpty() ? 0 : StringUtil.isInteger(resultpc) ? (int) Double.parseDouble(StringUtil.stripCommas(resultpc)) : 0;
                if (cd4 > 0) {
                    hivEncounter.setCD4(cd4);
                    try {
                        hivEncounter.setCD4TestDate(DateUtil.getXmlDate(rs.getDate("date_reported")));
                    } catch (DatatypeConfigurationException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (cd4p > 0) {
                        hivEncounter.setCD4(cd4p);
                        try {
                            hivEncounter.setCD4TestDate(DateUtil.getXmlDate(rs.getDate("date_reported")));
                        } catch (DatatypeConfigurationException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            encounter.getHIVEncounter().add(hivEncounter);
        });
        return encounter;
    }
}
