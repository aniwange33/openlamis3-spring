/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.ndr;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.exchange.ndr.schema.CodedSimpleType;
import org.fhi360.lamis.exchange.ndr.schema.ConditionSpecificQuestionsType;
import org.fhi360.lamis.exchange.ndr.schema.HIVQuestionsType;
import org.fhi360.lamis.utility.DateUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.xml.datatype.DatatypeConfigurationException;

/**
 * @author user1
 */
public class ConditionSpecificQuestionsTypeMapper {

    public ConditionSpecificQuestionsTypeMapper() {
    }

    public ConditionSpecificQuestionsType conditionSpecificQuestionsType(long patientId) {
        ConditionSpecificQuestionsType disease = new ConditionSpecificQuestionsType();
        HIVQuestionsType hiv = new HIVQuestionsType();
        String databaseName = "h2"; //new PropertyAccessor().getDatabaseName();
        int[] age = {0};
        try {
            //These are HIV question relating to registration
            String query = "SELECT TIMESTAMPDIFF(YEAR, date_birth, CURDATE()) AS age, date_registration," +
                    " status_registration, current_status, date_current_status, date_started FROM patient " +
                    "WHERE patient_id = " + patientId;

            ContextProvider.getBean(JdbcTemplate.class).query(query, rs -> {
                String statusRegistration = rs.getString("status_registration") == null ? "" : rs.getString("status_registration");
                String currentStatus = rs.getString("current_status") == null ? "" : rs.getString("current_status");
                String ARTStatus = rs.getDate("date_started") == null ? "Pre-ART" : "ART";
                age[0] = rs.getInt("age");

                //hiv.setCareEntryPoint("2");  Entry point is not captured in LAMIS
                if (statusRegistration.equalsIgnoreCase("HIV+ non ART")) {
                    try {
                        hiv.setFirstConfirmedHIVTestDate(DateUtil.getXmlDate(rs.getDate("date_registration")));
                    } catch (DatatypeConfigurationException e) {
                        e.printStackTrace();
                    }
                }
                if (statusRegistration.equalsIgnoreCase("Transfer In")) {
                    try {
                        hiv.setTransferredInDate(DateUtil.getXmlDate(rs.getDate("date_registration")));
                    } catch (DatatypeConfigurationException e) {
                        e.printStackTrace();
                    }
                }

                if (currentStatus.contains("Transfer Out")) {
                    String status = CodeSetResolver.getCode("ART_STATUS", ARTStatus);
                    if (!status.isEmpty()) hiv.setTransferredOutStatus(status);
                    try {
                        hiv.setTransferredOutDate(DateUtil.getXmlDate(rs.getDate("date_current_status")));
                    } catch (DatatypeConfigurationException e) {
                        e.printStackTrace();
                    }
                    hiv.setPatientTransferredOut(true);
                } else {
                    hiv.setPatientTransferredOut(false);
                }
                if (currentStatus.equalsIgnoreCase("Known Death")) {
                    String status = CodeSetResolver.getCode("ART_STATUS", ARTStatus);
                    if (!status.isEmpty()) hiv.setStatusAtDeath(status);
                    try {
                        hiv.setDeathDate(DateUtil.getXmlDate(rs.getDate("date_current_status")));
                    } catch (DatatypeConfigurationException e) {
                        e.printStackTrace();
                    }
                    hiv.setPatientHasDied(true);
                } else {
                    hiv.setPatientTransferredOut(false);
                }
                if (statusRegistration.equalsIgnoreCase("HIV+ non ART")) {
                    try {
                        hiv.setEnrolledInHIVCareDate(DateUtil.getXmlDate(rs.getDate("date_registration")));
                    } catch (DatatypeConfigurationException e) {
                        e.printStackTrace();
                    }
                }
            });
            //These are HIV question ART commencement
            query = "SELECT * FROM clinic WHERE patient_id = " + patientId + " AND commence = 1";
            ContextProvider.getBean(JdbcTemplate.class).query(query, rs -> {
                String eligible = "";
                int cd4 = (int) rs.getDouble("cd4");
                int cd4p = (int) rs.getDouble("cd4p");
                String clinicStage = StringUtils.trimToEmpty(rs.getString("clinic_stage"));
                String funcStatus = StringUtils.trimToEmpty(rs.getString("func_status"));
                String regimen = StringUtils.trimToEmpty(rs.getString("regimen"));

                if (age[0] >= 15) {
                    if (cd4 < 350) {
                        eligible = CodeSetResolver.getCode("WHY_ELIGIBLE", "CD4");
                    } else {
                        if (clinicStage.equalsIgnoreCase("Stage III") ||
                                clinicStage.equalsIgnoreCase("Stage IV")) {
                            eligible = CodeSetResolver.getCode("WHY_ELIGIBLE", "Staging");
                        } else {
                            //Information not available
                        }
                    }
                } else {
                    if (cd4 < 750 || cd4p < 25) {
                        eligible = cd4 < 25 ? CodeSetResolver.getCode("WHY_ELIGIBLE", "CD4p")
                                : CodeSetResolver.getCode("WHY_ELIGIBLE", "CD4");
                    } else {
                        if (clinicStage.equalsIgnoreCase("Stage III") ||
                                clinicStage.equalsIgnoreCase("Stage IV")) {
                            eligible = CodeSetResolver.getCode("WHY_ELIGIBLE", "Staging");
                        } else {
                            //Information not available
                        }
                    }
                }
                try {
                    hiv.setARTStartDate(DateUtil.getXmlDate(rs.getDate("date_visit")));
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }
                try {
                    hiv.setMedicallyEligibleDate(DateUtil.getXmlDate(rs.getDate("date_visit")));
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }
                if (!eligible.isEmpty()) hiv.setReasonMedicallyEligible(eligible);
                if (!clinicStage.isEmpty()) {
                    clinicStage = CodeSetResolver.getCode("WHO_STAGE", clinicStage);
                    if (!clinicStage.isEmpty()) hiv.setWHOClinicalStageARTStart(clinicStage);
                }
                if (!funcStatus.isEmpty()) {
                    funcStatus = CodeSetResolver.getCode("FUNCTIONAL_STATUS", funcStatus);
                    if (!funcStatus.isEmpty()) hiv.setFunctionalStatusStartART(funcStatus);
                }
                if (!regimen.isEmpty()) {
                    CodedSimpleType cst = CodeSetResolver.getRegimen(regimen);
                    if (cst.getCode() != null) {
                        hiv.setFirstARTRegimen(cst);
                    }
                }

                int weight = (int) rs.getDouble("body_weight");
                int height = (int) rs.getDouble("height");
                if (weight > 0) hiv.setWeightAtARTStart(weight);
                if (height > 0) hiv.setChildHeightAtARTStart(height);
                if (cd4 > 0) {
                    hiv.setCD4AtStartOfART(Integer.toString(cd4)); //Long.toString(Math.round(rs.getDouble("cd4")))
                } else {
                    if (cd4p > 0) hiv.setCD4AtStartOfART(Integer.toString(cd4p));
                }
                disease.setHIVQuestions(hiv);
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return disease;
    }

}


//
//double d = 9.5;
//int i = (int)d;
////i = 9
//
//Double D = 9.5;
//int i = Integer.valueOf(D.intValue());
////i = 9
//
//double d = 9.5;
//Long L = Math.round(d);
//int i = Integer.valueOf(L.intValue());
////i = 10
