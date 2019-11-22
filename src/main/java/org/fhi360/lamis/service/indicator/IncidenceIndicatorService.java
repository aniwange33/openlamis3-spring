/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.indicator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;


/**
 *
 * @author user10
 */
@Component
public class IncidenceIndicatorService {   
    private Long stateId;
    private Long lgaId;
    private final IndicatorPersister indicatorPersister;
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private int agem1;
    private int agef1;

    private int sumTested, sumPositive, sumInitiated;
    private int dataElementId = 0;

    public IncidenceIndicatorService(IndicatorPersister indicatorPersister) {
        this.indicatorPersister = indicatorPersister;
    }


    public void process() {
            System.out.println("Trigger.............incidence processing");
            try {
                jdbcUtil = new JDBCUtil();
                preparedStatement = jdbcUtil.getStatement("SELECT DISTINCT facility_id FROM hts WHERE CAST(time_stamp AS DATE) = CURDATE()");
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) { 
                    System.out.println("Analyzing....."+rs.getLong("facility_id"));
                    process(rs.getLong("facility_id"));
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
      }
        
        
     private void process(long facilityId) {
        try {            
            String reportDate = DateUtil.parseDateToString(new Date(), "yyyy-MM-dd");   
            System.out.println("IncidenceIndicatorService: running report for : "+reportDate);

            jdbcUtil = new JDBCUtil();
            getStateId(facilityId); // stateId and lgaId

            //Compute values for HTS total client tested
            System.out.println("Computing HTS Total Client Tested.....");
            initVariables();

            String query = "SELECT DISTINCT hts_id, gender, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age FROM hts WHERE facility_id = " + facilityId + " AND date_visit = '" + reportDate + "' ";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                if (gender.trim().equalsIgnoreCase("Male")) {
                    agem1++;
                } else {
                    agef1++;
                }
            }

            //Populate indicatorvalue with values computed for HTS Total client tested
            dataElementId = 101;
            indicatorPersister.persist(dataElementId, 13, stateId, lgaId, facilityId, agem1, reportDate);         // male <1   
            indicatorPersister.persist(dataElementId, 1,  stateId, lgaId, facilityId, agef1, reportDate);        //female <1

            // Compute values for HTS total client tested positive
            System.out.println("Computing HTS Total Client Tested Positive.....");
            initVariables();

            query = "SELECT DISTINCT hts_id, gender, hiv_test_result, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age FROM hts WHERE facility_id = " + facilityId + " AND date_visit = '" + reportDate + "' ";

            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                String hivResult = resultSet.getString("hiv_test_result");

                if (hivResult.equalsIgnoreCase("Positive")) {
                    if (gender.trim().equalsIgnoreCase("Male")) {
                        agem1++;
                    } else {
                        agef1++;
                    }
                }
                // disaggregate(gender, age);
            }

            //Populate indicatorvalue table with values computed for HTS total client tested positive
            dataElementId = 102;
            indicatorPersister.persist(dataElementId, 13,  stateId, lgaId, facilityId, agem1, reportDate);         // male <1   
            indicatorPersister.persist(dataElementId, 1,  stateId, lgaId, facilityId, agef1, reportDate);        //female <1

            //Compute values for HTS total client enrolled
            System.out.println("Computing HTS Total Client Enrolled.....");
            initVariables();

            query = "SELECT DISTINCT hts_id, gender, date_started, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age FROM hts WHERE facility_id = " + facilityId + " AND date_visit = '" + reportDate + "' ";

            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                String artReferred = resultSet.getString("date_started");

                if (artReferred != null && !artReferred.isEmpty()) {
                    if (gender.trim().equalsIgnoreCase("Male")) {
                        agem1++;
                    } else {
                        agef1++;
                    }
                }
            }

            //Populate the report parameter map with values computed for HTS total client enrolled
            dataElementId = 103;
            indicatorPersister.persist(dataElementId, 13, stateId, lgaId, facilityId, agem1, reportDate);         // male <1   
            indicatorPersister.persist(dataElementId, 1, stateId, lgaId, facilityId, agef1, reportDate);        //female <1
            
             //Compute values for HTS total client referred by settings
            System.out.println("Computing HTS Total Client Referred by Settings.....");
            initVariables();

            int ct = 0;
            int tb = 0;
            int sti = 0;
            int opd = 0;
            int ward = 0;
            int community = 0;
            int standalone = 0;
            int others = 0;

            query = "SELECT DISTINCT hts_id, gender, testing_setting, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age FROM hts WHERE facility_id = " + facilityId + " AND date_visit = '" + reportDate + "' ";

            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                String referredFrom = resultSet.getString("testing_setting");
                if (referredFrom.equalsIgnoreCase("CT")) {
                    ct++;
                }
                if (referredFrom.equalsIgnoreCase("TB")) {
                    tb++;
                }
                if (referredFrom.equalsIgnoreCase("STI")) {
                    sti++;
                }
                if (referredFrom.equalsIgnoreCase("OPD")) {
                    opd++;
                }
                if (referredFrom.equalsIgnoreCase("WARD")) {
                    ward++;
                }
                if (referredFrom.equalsIgnoreCase("Community")) {
                    community++;
                }

                if (referredFrom.equalsIgnoreCase("Standalone Hts")) {
                    standalone++;
                }
                if (referredFrom.equalsIgnoreCase("Others")) {
                    others++;
                }
            }

            //Populate the report parameter map with values computed for HTS total client testing settings
            //CT, tb, sti, opd, ward, community, standalone hts, others
            dataElementId = 104;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, ct, reportDate);         // ct   
            dataElementId = 105;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, tb, reportDate);        //tb
            dataElementId = 106;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, sti, reportDate);        //sti
            dataElementId = 107;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, opd, reportDate);        //opd
            dataElementId = 108;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, ward, reportDate);        //ward
            dataElementId = 109;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, community, reportDate);        //community
            dataElementId = 110;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, standalone, reportDate);        //standalone hts
            dataElementId = 111;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, others, reportDate);        //tb

            System.out.println("Computing HTS Total Client Referred From.....");
            int self = 0;
            tb = 0;
            sti = 0;
            opd = 0;
            int fp = 0;
            ward = 0;
            int blood = 0;
            others = 0;

            query = "SELECT DISTINCT hts_id, gender, referred_from, DATEDIFF(YEAR, date_birth, '" + reportDate + "') AS age FROM hts WHERE facility_id = " + facilityId + " AND date_visit = '" + reportDate + "' ";

            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String referredFrom = resultSet.getString("referred_from");
                if (referredFrom.equalsIgnoreCase("SELF")) {
                    self++;
                }
                if (referredFrom.equalsIgnoreCase("TB")) {
                    tb++;
                }
                if (referredFrom.equalsIgnoreCase("STI")) {
                    sti++;
                }
                if (referredFrom.equalsIgnoreCase("FP")) {
                    fp++;
                }
                if (referredFrom.equalsIgnoreCase("OPD")) {
                    opd++;
                }
                if (referredFrom.equalsIgnoreCase("WARD")) {
                    ward++;
                }
                if (referredFrom.equalsIgnoreCase("Blood bank")) {
                    blood++;
                }
                if (referredFrom.equalsIgnoreCase("Others")) {
                    others++;
                }
            }

            //Referred settings
            //self, tb, sti, fp, opd, ward, blood bank, others
            dataElementId = 112;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, self, reportDate);
            dataElementId = 113;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, tb, reportDate);
            dataElementId = 114;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, sti, reportDate);
            dataElementId = 115;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, fp, reportDate);
            dataElementId = 116;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, opd, reportDate);
            dataElementId = 117;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, ward, reportDate);
            dataElementId = 118;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, blood, reportDate);
            dataElementId = 119;
            indicatorPersister.persist(dataElementId, 0, stateId, lgaId, facilityId, others, reportDate);
            
            System.out.println("Completed");

        } catch (Exception exception) {
            resultSet = null;
            System.out.println("Exception: " + exception);
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
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
        agef1 = 0;
        sumTested = 0;
        sumPositive = 0;
        sumInitiated = 0;
    }

    private void getStateId(long facilityId){
       try{ 
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
