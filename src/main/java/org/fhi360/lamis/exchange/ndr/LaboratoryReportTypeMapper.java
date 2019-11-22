/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.ndr;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.exchange.ndr.schema.*;
import org.fhi360.lamis.model.repositories.LabTestRepository;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.StringUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.xml.datatype.DatatypeConfigurationException;

/**
 * @author user1
 */
public class LaboratoryReportTypeMapper {

    private final LabTestRepository labTestRepository = ContextProvider.getBean(LabTestRepository.class);
    public LaboratoryReportTypeMapper() {
    }

    public ConditionType laboratoryReportType(long patientId, ConditionType condition) {
        try {
            final LaboratoryReportType[] laboratory = {null};


            final String[] query = {"SELECT DISTINCT laboratory_id, date_collected, date_reported, labtest_id, " +
                    "resultab, resultpc FROM laboratory WHERE (resultab != '' OR resultpc != '') AND patient_id = " +
                    patientId + " ORDER BY date_reported"};
//            jdbcUtil = new JDBCUtil();
            ContextProvider.getBean(JdbcTemplate.class).query(query[0], rs -> {
                String dateReported = "";
                //If dateReported is not equal to date_reported, add this laboratory report to conditionType
                //ie if dateReported is before date_reported return < 0 OR if dateReported is after date_reported return > 0
                //If dateReported is equal to date_reported return 0
                if (!dateReported.equals(DateUtil.parseDateToString(rs.getDate("date_reported"), "yyyy-MM-dd"))) {
                    if (laboratory[0] != null) {
                        condition.getLaboratoryReport().add(laboratory[0]);
                        System.out.println("Lab test report added....");
                    }
                    //Any time the date changes reset the dateVisit variable
                    dateReported = DateUtil.parseDateToString(rs.getDate("date_reported"), "yyyy-MM-dd");
                    //Instantiate a new laboratory report for each date
                    laboratory[0] = new LaboratoryReportType();
                    laboratory[0].setVisitID(Long.toString(rs.getLong("laboratory_id")));
                    try {
                        laboratory[0].setVisitDate(DateUtil.getXmlDate(rs.getDate("date_collected")));
                    } catch (DatatypeConfigurationException e) {
                        e.printStackTrace();
                    }
                    laboratory[0].setLaboratoryTestIdentifier("0000001");
                }

                long labtestId = rs.getLong("labtest_id");
                String description = labTestRepository.getOne(labtestId).getDescription();
                CodedSimpleType cst = CodeSetResolver.getCodedSimpleType("LAB_RESULTED_TEST", description);
                if (cst.getCode() != null && laboratory[0] != null) {
                    String resultab = StringUtils.trimToEmpty(rs.getString("resultab"));
                    String resultpc = StringUtils.trimToEmpty(rs.getString("resultpc"));

                    if (!resultab.isEmpty() || !resultpc.isEmpty()) {
                        //Set the NDR code & description for this lab test
                        LaboratoryOrderAndResult result = new LaboratoryOrderAndResult();
                        result.setLaboratoryResultedTest(cst);
                        try {
                            result.setOrderedTestDate(DateUtil.getXmlDate(rs.getDate("date_collected")));
                        } catch (DatatypeConfigurationException e) {
                            e.printStackTrace();
                        }
                        try {
                            result.setResultedTestDate(DateUtil.getXmlDate(rs.getDate("date_reported")));
                        } catch (DatatypeConfigurationException e) {
                            e.printStackTrace();
                        }

                        //Set the lab test result values either numeric or text
                        AnswerType answer = new AnswerType();
                        NumericType numeric = new NumericType();
                        String value = !resultab.isEmpty() ? resultab : resultpc;
                        if (StringUtils.isNumeric(value)) {
                            Double d = Double.valueOf(StringUtil.stripCommas(value));
                            numeric.setValue1(d.intValue());
                            answer.setAnswerNumeric(numeric);
                        } else {
                            if (labtestId == 16) {
                                numeric.setValue1(0);  //if lab test is a viralload set the value to 0
                                answer.setAnswerNumeric(numeric);
                            } else {
                                answer.setAnswerText(value);
                            }
                        }
                        result.setLaboratoryResult(answer);
                        laboratory[0].getLaboratoryOrderAndResult().add(result);
                    }
                }
            });
            //Add the last lab test result in the resultSet to condition
            if (laboratory[0] != null) {
                condition.getLaboratoryReport().add(laboratory[0]);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return condition;
    }

}
