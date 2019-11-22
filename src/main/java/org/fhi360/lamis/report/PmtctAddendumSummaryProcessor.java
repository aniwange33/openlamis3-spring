/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.report;

import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author user10
 */
@Component
public class PmtctAddendumSummaryProcessor {

    private int reportingMonth;
    private int reportingYear;
    private String reportingDateBegin;
    private String reportingDateEnd;

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private HashMap parameterMap;
    private long facilityId;
    //private static final Log log = LogFactory.getLog(ArtSummaryProcessor.class);
    
    private int agem1, agem2, agem3, agem4, agem5, agem6, agem7, agem8, agem9, agem10, agem11, agem12;;
    private int agef1, agef2, agef3, agef4, agef5, agef6, agef7, agef8, agef9, agef10, agef11, agef12;


    public PmtctAddendumSummaryProcessor() {
    }
    
    public Map<String, Object> process(Integer reportingMonth, Integer reportingYear, Long facilityId) {

        parameterMap = new HashMap();
        reportingDateBegin = dateFormat.format(DateUtil.getFirstDateOfMonth(reportingYear, reportingMonth));
        reportingDateEnd = dateFormat.format(DateUtil.getLastDateOfMonth(reportingYear, reportingMonth));

        try {
            jdbcUtil = new JDBCUtil();

            ResultSet rs;
            System.out.println("Computing EAC1.....");
            //EAC1
            //Number of virally unsuppressed PLHIV who received 1st EAC during the month
            initVariables();

            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM eac WHERE YEAR(date_eac1) = " + reportingYear + " AND MONTH(date_eac1) = " + reportingMonth;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for EAC1
            parameterMap.put("art1m1", Integer.toString(agem1));
            parameterMap.put("art1f1", Integer.toString(agef1));
            parameterMap.put("art1t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art1m2", Integer.toString(agem2));
            parameterMap.put("art1f2", Integer.toString(agef2));
            parameterMap.put("art1t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art1m3", Integer.toString(agem3));
            parameterMap.put("art1f3", Integer.toString(agef3));
            parameterMap.put("art1t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art1m4", Integer.toString(agem4));
            parameterMap.put("art1f4", Integer.toString(agef4));
            parameterMap.put("art1t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art1m5", Integer.toString(agem5));
            parameterMap.put("art1f5", Integer.toString(agef5));
            parameterMap.put("art1t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art1m6", Integer.toString(agem6));
            parameterMap.put("art1f6", Integer.toString(agef6));
            parameterMap.put("art1t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art1m7", Integer.toString(agem7));
            parameterMap.put("art1f7", Integer.toString(agef7));
            parameterMap.put("art1t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art1m8", Integer.toString(agem8));
            parameterMap.put("art1f8", Integer.toString(agef8));
            parameterMap.put("art1t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art1m9", Integer.toString(agem9));
            parameterMap.put("art1f9", Integer.toString(agef9));
            parameterMap.put("art1t9", Integer.toString(agem9 + agef9));
            
            parameterMap.put("art1m10", Integer.toString(agem10));
            parameterMap.put("art1f10", Integer.toString(agef10));
            parameterMap.put("art1t10", Integer.toString(agem10 + agef10));
            
            parameterMap.put("art1m11", Integer.toString(agem11));
            parameterMap.put("art1f11", Integer.toString(agef11));
            parameterMap.put("art1t11", Integer.toString(agem11 + agef11));
            
            parameterMap.put("art1m12", Integer.toString(agem12));
            parameterMap.put("art1f12", Integer.toString(agef12));
            parameterMap.put("art1t12", Integer.toString(agem12 + agef12));

            System.out.println("Computing EAC2.....");
            //EAC2
            //Total number of people living with HIV known to have died during the month (by TB)
            initVariables();
            query = "SELECT DISTINCT patient_id, gender, DATEDIFF(YEAR, date_birth, '" + reportingDateBegin + "') AS age FROM eac WHERE YEAR(date_eac2) = " + reportingYear + " AND MONTH(date_eac2) = " + reportingMonth;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                disaggregate(gender, age);
            }

            //Populate the report parameter map with values computed for EAC1
            parameterMap.put("art2m1", Integer.toString(agem1));
            parameterMap.put("art2f1", Integer.toString(agef1));
            parameterMap.put("art2t1", Integer.toString(agem1 + agef1));

            parameterMap.put("art2m2", Integer.toString(agem2));
            parameterMap.put("art2f2", Integer.toString(agef2));
            parameterMap.put("art2t2", Integer.toString(agem2 + agef2));

            parameterMap.put("art2m3", Integer.toString(agem3));
            parameterMap.put("art2f3", Integer.toString(agef3));
            parameterMap.put("art2t3", Integer.toString(agem3 + agef3));

            parameterMap.put("art2m4", Integer.toString(agem4));
            parameterMap.put("art2f4", Integer.toString(agef4));
            parameterMap.put("art2t4", Integer.toString(agem4 + agef4));

            parameterMap.put("art2m5", Integer.toString(agem5));
            parameterMap.put("art2f5", Integer.toString(agef5));
            parameterMap.put("art2t5", Integer.toString(agem5 + agef5));

            parameterMap.put("art2m6", Integer.toString(agem6));
            parameterMap.put("art2f6", Integer.toString(agef6));
            parameterMap.put("art2t6", Integer.toString(agem6 + agef6));

            parameterMap.put("art2m7", Integer.toString(agem7));
            parameterMap.put("art2f7", Integer.toString(agef7));
            parameterMap.put("art2t7", Integer.toString(agem7 + agef7));

            parameterMap.put("art2m8", Integer.toString(agem8));
            parameterMap.put("art2f8", Integer.toString(agef8));
            parameterMap.put("art2t8", Integer.toString(agem8 + agef8));

            parameterMap.put("art2m9", Integer.toString(agem9));
            parameterMap.put("art2f9", Integer.toString(agef9));
            parameterMap.put("art2t9", Integer.toString(agem9 + agef9));
            
            parameterMap.put("art2m10", Integer.toString(agem10));
            parameterMap.put("art2f10", Integer.toString(agef10));
            parameterMap.put("art2t10", Integer.toString(agem10 + agef10));

            parameterMap.put("art2m11", Integer.toString(agem11));
            parameterMap.put("art2f11", Integer.toString(agef11));
            parameterMap.put("art2t11", Integer.toString(agem11 + agef11));
            
            parameterMap.put("art2m12", Integer.toString(agem12));
            parameterMap.put("art2f12", Integer.toString(agef12));
            parameterMap.put("art2t12", Integer.toString(agem12 + agef12));
    
            System.out.println("Adding headers.....");
            //Include reproting period & facility details into report header 
            parameterMap.put("reportingMonth", reportingMonth);
            parameterMap.put("reportingYear", reportingYear);

            query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility JOIN lga ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE facility_id = " + facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                parameterMap.put("facilityName", rs.getString("name"));
                parameterMap.put("facilityType", "");
                parameterMap.put("lga", rs.getString("lga"));
                parameterMap.put("state", rs.getString("state"));
                parameterMap.put("level", "");
            }
        } catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return parameterMap;
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
    }

    private ResultSet getLastRefillVisit(long patientId) {
        ResultSet rs = null;
        try {
            query = "SELECT DISTINCT regimentype_id, regimen_id, date_visit, duration FROM pharm WHERE patient_id = " + patientId + " ORDER BY date_visit DESC LIMIT 1";  
            preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return rs;        
    }

    
}
