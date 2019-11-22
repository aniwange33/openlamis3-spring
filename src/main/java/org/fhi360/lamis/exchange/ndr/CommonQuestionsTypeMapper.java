/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.ndr;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.exchange.ndr.schema.CommonQuestionsType;
import org.fhi360.lamis.utility.DateUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.Date;
import java.util.Map;

/**
 * @author user1
 */
public class CommonQuestionsTypeMapper {

    public CommonQuestionsTypeMapper() {
    }

    public CommonQuestionsType commonQuestionsType(long patientId) {
        CommonQuestionsType common = new CommonQuestionsType();
        String query = "SELECT facility_id, hospital_num, TIMESTAMPDIFF(YEAR, date_birth, CURDATE()) AS age, " +
                "gender, date_registration, status_registration, current_status FROM patient WHERE patient_id = " + patientId;
//            jdbcUtil = new JDBCUtil();
        ContextProvider.getBean(JdbcTemplate.class).query(query, rs -> {
            //Common Questions
            common.setHospitalNumber(rs.getString("hospital_num"));
            if (!StringUtils.trimToEmpty(rs.getString("status_registration")).contains("Transfer In")) {
                common.setDiagnosisFacility(TreatmentFacility.getFacility(rs.getLong("facility_id")));
                if (rs.getString("date_registration") != null) {
                    try {
                        common.setDiagnosisDate(DateUtil.getXmlDate(rs.getDate("date_registration")));
                    } catch (DatatypeConfigurationException e) {
                        e.printStackTrace();
                    }
                }
            }

            boolean died = false;
            if (rs.getString("current_status") != null) {
                died = rs.getString("current_status").equalsIgnoreCase("Known Death");
            }
            common.setPatientDieFromThisIllness(died);

            // Check pregnancy status is patient is a female
            if (StringUtils.trimToEmpty(rs.getString("gender")).equalsIgnoreCase("Female")) {
                Map map = PregnancyStatus.getPregnancyStatus(patientId);
                common.setPatientPregnancyStatusCode((String) map.get("status"));
                if (map.get("edd") != null) {
                    try {
                        common.setEstimatedDeliveryDate(DateUtil.getXmlDate((Date) map.get("edd")));
                    } catch (DatatypeConfigurationException e) {
                        e.printStackTrace();
                    }
                }
                if (rs.getInt("age") > 0) common.setPatientAge(rs.getInt("age"));
            }
        });
        return common;
    }

}
