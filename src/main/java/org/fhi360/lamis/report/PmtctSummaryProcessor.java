/**
 *
 * @author AALOZIE
 */
package org.fhi360.lamis.report;

import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class PmtctSummaryProcessor {
    //private static final Log log = LogFactory.getLog(PmtctSummaryProcessor.class);

    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ArrayList<Map<String, String>> reportList;
    private HashMap parameterMap;

    public PmtctSummaryProcessor() {
    }

    public synchronized Map<String, Object> process(Integer reportingMonth, Integer reportingYear, Long facilityId) {
        reportList = new ArrayList<>();
        String reportingDateBegin = dateFormat.format(DateUtil.getFirstDateOfMonth(reportingYear, reportingMonth));
        String reportingDateEnd = dateFormat.format(DateUtil.getLastDateOfMonth(reportingYear, reportingMonth));
        parameterMap = new HashMap();
        int total = 0;
        int count = 0;

        try {
            jdbcUtil = new JDBCUtil();
            
            //Number of  new ANC Clients
            String query = "SELECT COUNT(DISTINCT anc.patient_id) AS count FROM anc WHERE anc.facility_id = " + facilityId + " "
                    + "AND anc.date_visit >= '" + reportingDateBegin + "' AND anc.date_visit <= '" + reportingDateEnd + "'";
            parameterMap.put("pmtct1a", Integer.toString(executeQuery(query)));
            
            //Number of new ANC Clients tested for syphilis
            query = "SELECT COUNT(DISTINCT anc.patient_id) AS count FROM anc WHERE anc.facility_id = " + facilityId + " AND anc.date_visit >= '" + reportingDateBegin + "' AND anc.date_visit <= '" + reportingDateEnd + "' "
                    + "AND anc.syphilis_tested = 'Yes'";
            parameterMap.put("pmtct2a", Integer.toString(executeQuery(query)));

            //Number of new ANC Clients who tested positive for Syphilis
           query = "SELECT COUNT(DISTINCT anc.patient_id) AS count FROM anc WHERE anc.facility_id = " + facilityId + " AND anc.date_visit >= '" + reportingDateBegin + "' AND anc.date_visit <= '" + reportingDateEnd + "' "
                    + "AND anc.syphilis_test_result = '" + Constants.TestResult.POSITIVE + "'";
            parameterMap.put("pmtct3a", Integer.toString(executeQuery(query)));

            //Number of the ANC Clients treated for Syphilis
           query = "SELECT COUNT(DISTINCT anc.patient_id) AS count FROM anc WHERE anc.facility_id = " + facilityId + " AND anc.date_visit >= '" + reportingDateBegin + "' AND anc.date_visit <= '" + reportingDateEnd + "' "
                    + "AND anc.syphilis_treated = 'Yes' ";
            parameterMap.put("pmtct4a", Integer.toString(executeQuery(query)));

            //No. of pregnant women with previously known HIV +ve infection at ANC
            query = "SELECT COUNT(DISTINCT anc.patient_id) AS count FROM anc WHERE anc.facility_id = " + facilityId + " "
                    + "AND anc.date_visit >= '" + reportingDateBegin + "' AND anc.date_visit <= '" + reportingDateEnd + "' "
                    + "AND time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_ANC + "'";
            count = executeQuery(query);
            total += count;
            parameterMap.put("pmtct5a", Integer.toString(count));

            //No. of pregnant women with previously known HIV +ve infection at Labour
            query = "SELECT COUNT(DISTINCT delivery.patient_id) AS count FROM delivery WHERE delivery.facility_id = " + facilityId + " "
                    + "AND delivery.date_delivery >= '" + reportingDateBegin + "' AND delivery.date_delivery <= '" + reportingDateEnd + "' "
                    + "AND delivery.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_LD +"'";
            count = executeQuery(query);
            total += count;
            parameterMap.put("pmtct5b", Integer.toString(count));

            //No. of pregnant women with previously known HIV +ve infection at Post partum
            query = "SELECT COUNT(DISTINCT maternalfollowup.patient_id) AS count FROM maternalfollowup WHERE maternalfollowup.facility_id = " + facilityId + " "
                    + "AND maternalfollowup.date_visit >= '" + reportingDateBegin + "' AND maternalfollowup.date_visit <= '" + reportingDateEnd + "' "
                    + "AND maternalfollowup.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_PP_LESS + "'";
            count = executeQuery(query);
            total += count;
             parameterMap.put("pmtct5c", Integer.toString(count));

            //No. of pregnant women with previously known HIV +ve infection total
            parameterMap.put("pmtct5t", Integer.toString(total));
            total = 0;

//            //No. of Pregnant women tested for HIV at ANC (Proxy)
//            query = "SELECT COUNT(DISTINCT anc.anc_id) AS count FROM anc WHERE anc.facility_id = " + id + " "
//                    + "AND anc.date_visit >= '" + reportingDateBegin + "' AND anc.date_visit <= '" + reportingDateEnd + "'";
//            count = executeQuery(query);
//            total += count;
//            parameterMap.put("I6a", Integer.toString(count));
//
//            //No. of Pregnant women tested for HIV at Labour (Proxy)
//            query = "SELECT COUNT(DISTINCT delivery.delivery_id) AS count FROM delivery WHERE delivery.facility_id = " + id + " "
//                    + "AND delivery.date_delivery >= '" + reportingDateBegin + "' AND delivery.date_delivery <= '" + reportingDateEnd + "' "
//                    + "AND delivery.screen_post_partum = 0";
//            count = executeQuery(query);
//            total += count;
//            parameterMap.put("I6b", Integer.toString(count));
//
//            //No. of Pregnant women tested for HIV at Post partum (Proxy)
//            query = "SELECT COUNT(DISTINCT delivery.delivery_id) AS count FROM delivery WHERE delivery.facility_id = " + id + " "
//                    + "AND delivery.date_delivery >= '" + reportingDateBegin + "' AND delivery.date_delivery <= '" + reportingDateEnd + "' "
//                    + "AND delivery.screen_post_partum = 1";
//            count = executeQuery(query);
//            total += count;
//            parameterMap.put("I6c", Integer.toString(count));
//
//            //No. of Pregnant women tested for HIV total (Proxy)
//            parameterMap.put("I6d", Integer.toString(total));
//            total = 0;

            //create a temporary table of positives at the end of the review period
            executeUpdate("DROP TABLE IF EXISTS positives");
            query = "CREATE TEMPORARY TABLE positives AS SELECT DISTINCT patient_id, MAX(date_current_status) FROM statushistory "
                    + "WHERE facility_id = " + facilityId + " AND date_current_status <= '" + reportingDateEnd + "' "
                    + "AND current_status IN ('HIV+ non ART', 'ART Transfer In', 'Pre-ART Transfer In', 'ART Start') "
                    + "GROUP BY patient_id";
            executeUpdate(query);

            //No. tested HIV positive at ANC
            query = "SELECT COUNT(DISTINCT anc.patient_id) AS count FROM anc WHERE anc.facility_id = " + facilityId + " "
                    + "AND anc.date_visit >= '" + reportingDateBegin + "' AND anc.date_visit <= '" + reportingDateEnd + "'"
                    + "AND anc.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_ANC + "'";
            count = executeQuery(query);
            total += count;
            parameterMap.put("pmtct7a", Integer.toString(count));

            //No. tested HIV positive at Labour
            query = "SELECT COUNT(DISTINCT delivery.patient_id) AS count FROM delivery WHERE delivery.facility_id = " + facilityId + " "
                    + "AND delivery.date_delivery >= '" + reportingDateBegin + "' AND delivery.date_delivery <= '" + reportingDateEnd + "' AND delivery.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_LD +"'";
            count = executeQuery(query);
            total += count;
            parameterMap.put("pmtct7b", Integer.toString(count));

            //No. tested HIV positive at Post partum
            query = "SELECT COUNT(DISTINCT maternalfollowup.patient_id) AS count FROM maternalfollowup WHERE maternalfollowup.facility_id = " + facilityId + " "
                    + "AND maternalfollowup.date_visit >= '" + reportingDateBegin + "' AND maternalfollowup.date_visit <= '" + reportingDateEnd + "' AND maternalfollowup.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_PP_LESS +"'";
            count = executeQuery(query);
            total += count;
            parameterMap.put("pmtct7c", Integer.toString(count));

            //No. tested HIV positive total
            parameterMap.put("pmtct7t", Integer.toString(total));

//            //Not Calculated: No. of  pregnant women HIV tested, counseled and received results at ANC (Proxy)
//            query = "SELECT COUNT(DISTINCT anc.anc_id) AS count FROM anc WHERE anc.facility_id = " + id + " "
//                    + "AND anc.date_visit >= '" + reportingDateBegin + "' AND anc.date_visit <= '" + reportingDateEnd + "'";
//            count = executeQuery(query);
//            total += count;
//            parameterMap.put("I8a", Integer.toString(count));

            //No of new ANC  clients tested for HBV  
            total = 0;
            query = "SELECT COUNT(DISTINCT anc.patient_id) AS count FROM anc WHERE anc.facility_id = " + facilityId + " "
                    + "AND anc.date_visit >= '" + reportingDateBegin + "' AND anc.date_visit <= '" + reportingDateEnd + "' AND anc.hepatitisb_tested = '" +Constants.YesNoOption.YES + "' AND anc.hepatitisb_test_result = '" +Constants.TestResult.POSITIVE + "'";
            count = executeQuery(query);
            total += count;
            parameterMap.put("pmtct10a", Integer.toString(count));
            
            query = "SELECT COUNT(DISTINCT anc.patient_id) AS count FROM anc WHERE anc.facility_id = " + facilityId + " "
                    + "AND anc.date_visit >= '" + reportingDateBegin + "' AND anc.date_visit <= '" + reportingDateEnd + "' AND anc.hepatitisb_tested = '" +Constants.YesNoOption.YES + "' AND anc.hepatitisb_test_result = '" +Constants.TestResult.NEGATIVE +"'";
            count = executeQuery(query);
            total += count;
            parameterMap.put("pmtct10b", Integer.toString(count));
            
            //No of new ANC  clients tested for HBV  total 
            parameterMap.put("pmtct10t", Integer.toString(total));
            total = 0;
            
            //No of new ANC  clients tested for HCV   
            query = "SELECT COUNT(DISTINCT anc.patient_id) AS count FROM anc WHERE anc.facility_id = " + facilityId + " "
                    + "AND anc.date_visit >= '" + reportingDateBegin + "' AND anc.date_visit <= '" + reportingDateEnd + "' AND anc.hepatitisc_tested = '" +Constants.YesNoOption.YES + "' AND anc.hepatitisc_test_result = '" +Constants.TestResult.POSITIVE + "'";
            count = executeQuery(query);
            total += count;
            parameterMap.put("pmtct11a", Integer.toString(count));
            
            query = "SELECT COUNT(DISTINCT anc.patient_id) AS count FROM anc WHERE anc.facility_id = " + facilityId + " "
                    + "AND anc.date_visit >= '" + reportingDateBegin + "' AND anc.date_visit <= '" + reportingDateEnd + "' AND anc.hepatitisc_tested = '" + Constants.YesNoOption.YES + "' AND anc.hepatitisc_test_result = '" +Constants.TestResult.NEGATIVE +"'";
            count = executeQuery(query);
            total += count;
            parameterMap.put("pmtct11b", Integer.toString(count));

            //No. of  pregnant women HIV tested, counseled and received results total (Proxy)
            parameterMap.put("pmtct11t", Integer.toString(total));
            total = 0;
            
            //No of New ANC clients coinfected with HIV and HBV 
            query = "SELECT COUNT(DISTINCT anc.patient_id) AS count FROM anc WHERE anc.facility_id = " + facilityId + " "
                    + "AND anc.date_visit >= '" + reportingDateBegin + "' AND anc.date_visit <= '" + reportingDateEnd + "' "
                    + "AND (anc.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_ANC + "'  OR  anc.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_ANC + "') "
                    + "AND anc.hepatitisb_test_result = '" + Constants.TestResult.POSITIVE +"'";
            count = executeQuery(query);
            parameterMap.put("pmtct12t", Integer.toString(count));
            
            //No of New ANC clients coinfected with HIV and HCV 
            query = "SELECT COUNT(DISTINCT anc.patient_id) AS count FROM anc WHERE anc.facility_id = " + facilityId + " "
                    + "AND anc.date_visit >= '" + reportingDateBegin + "' AND anc.date_visit <= '" + reportingDateEnd + "' "
                    + "AND (anc.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_ANC + "'  OR  anc.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_ANC + "') "
                    + "AND anc.hepatitisc_test_result = '" + Constants.TestResult.POSITIVE +"'";
            count = executeQuery(query);
            parameterMap.put("pmtct13t", Integer.toString(count));
            total = 0;
           
            // TODO: 14at = TBD
            query = "SELECT COUNT(DISTINCT c.patient_id) AS count FROM clinic c JOIN anc a ON c.patient_id = a.patient_id WHERE c.commence = 1 AND c.facility_id = " + facilityId + " "
                    + " AND c.date_visit >= '" + reportingDateBegin + "' AND c.date_visit <= '" + reportingDateEnd + "' "
                    + " AND a.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_ANC + "'";
            count = executeQuery(query);
            total+=count;
            
            query = "SELECT COUNT(DISTINCT c.patient_id) AS count FROM clinic c JOIN delivery d ON c.patient_id = d.patient_id WHERE c.commence = 1 AND d.facility_id = " + facilityId + " "
                    + " AND d.date_delivery >= '" + reportingDateBegin + "' AND d.date_delivery <= '" + reportingDateEnd + "' "
                    + " AND d.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_LD + "'";
            count = executeQuery(query);
            total+=count;
            
            query = "SELECT COUNT(DISTINCT c.patient_id) AS count FROM clinic c JOIN maternalfollowup m ON c.patient_id = m.patient_id WHERE c.commence = 1 AND m.facility_id = " + facilityId + " "
                    + " AND m.date_visit >= '" + reportingDateBegin + "' AND m.date_visit <= '" + reportingDateEnd + "' "
                    + " AND (m.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_PP_LESS + "' AND "
                    + " m.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_PP_GREATER + "')";
            count = executeQuery(query);
            total+=count;
            
            parameterMap.put("pmtct14at", Integer.toString(total));
            
            //OLD PMTCT 14BT
//            query = "SELECT COUNT(DISTINCT clinic.clinic_id) AS count FROM clinic WHERE clinic.commence = 1 AND clinic.facility_id = " + id + " "
//                    + "AND clinic.date_visit >= '" + reportingDateBegin + "' AND clinic.date_visit <= '" + reportingDateEnd + "' "
//                    + "AND clinic.maternal_status_art = '" + Constants.MaternalStatus.PREGNANT + "' AND clinic.gestational_age = '" + Constants.GestationalPeriod.36WKS +"'";
//            count = executeQuery(query);
//            parameterMap.put("pmtct14ct", Integer.toString(count));

            // No of  HV +ve women newly started on ART during ANC  <=36 weeks of pregnancy
            query = "SELECT COUNT(DISTINCT c.patient_id) AS count FROM clinic c JOIN anc a ON c.patient_id = a.patient_id WHERE c.commence = 1 AND c.facility_id = " + facilityId + " "
                    + " AND c.date_visit >= '" + reportingDateBegin + "' AND c.date_visit <= '" + reportingDateEnd + "' "
                    + " AND a.gestational_age <= '" + 36 + "' AND a.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_ANC + "'";
            count = executeQuery(query);
            parameterMap.put("pmtct14bt", Integer.toString(count));
            
             // No of  HV +ve women newly started on ART during ANC  >36 weeks of pregnancy
           query = "SELECT COUNT(DISTINCT c.patient_id) AS count FROM clinic c JOIN anc a ON c.patient_id = a.patient_id WHERE c.commence = 1 AND c.facility_id = " + facilityId + " "
                    + " AND c.date_visit >= '" + reportingDateBegin + "' AND c.date_visit <= '" + reportingDateEnd + "' "
                    + " AND a.gestational_age > '" + 36 + "' AND a.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_ANC + "'";
            count = executeQuery(query);
            parameterMap.put("pmtct14ct", Integer.toString(count));
            
            // No of  HV +ve women newly started on ART during L&D
            query = "SELECT COUNT(DISTINCT c.patient_id) AS count FROM clinic c JOIN delivery l ON c.patient_id = l.patient_id WHERE c.commence = 1 AND c.facility_id = " + facilityId + " "
                    + " AND c.date_visit >= '" + reportingDateBegin + "' AND c.date_visit <= '" + reportingDateEnd + "' "
                    + " AND l.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_LD + "'";
            count = executeQuery(query);
            parameterMap.put("pmtct14dt", Integer.toString(count));
            
            // No of  HV +ve women newly started on ART during PP
            query = "SELECT COUNT(DISTINCT c.patient_id) AS count FROM clinic c JOIN maternalfollowup m ON c.patient_id = m.patient_id WHERE c.commence = 1 AND c.facility_id = " + facilityId + " "
                    + " AND c.date_visit >= '" + reportingDateBegin + "' AND c.date_visit <= '" + reportingDateEnd + "' "
                    + " AND ( m.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_PP_LESS + "' OR "
                    + " m.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_PP_GREATER + "')";
            count = executeQuery(query);
            parameterMap.put("pmtct14et", Integer.toString(count));
            total = 0;
            
            // No. partners of HIV +ve women tested HIV-
            query = "SELECT COUNT(DISTINCT p.partnerinformation_id) AS count FROM partnerinformation p JOIN anc a ON p.patient_id  = a.patient_id WHERE a.facility_id = " + facilityId + " "
                    + "AND a.date_visit>= '" + reportingDateBegin + "' AND a.date_visit <= '" + reportingDateEnd + "' "
                    + "AND a.date_visit = p.date_visit AND "
                    + "(a.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_ANC + "' OR "
                    + " a.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_ANC + "') "
                    + "AND p.partner_hiv_status = '" + Constants.GeneralTestResult.NEGATIVE +"'";
            count = executeQuery(query);
            total+= count;
            
            query = "SELECT COUNT(DISTINCT p.partnerinformation_id) AS count FROM partnerinformation p JOIN delivery d ON p.patient_id  = d.patient_id WHERE d.facility_id = " + facilityId + " "
                    + "AND d.date_delivery>= '" + reportingDateBegin + "' AND d.date_delivery <= '" + reportingDateEnd + "' "
                    + "AND d.date_delivery = p.date_visit AND "
                    + "(d.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_LD + "' OR "
                    + " d.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_LD + "') "
                    + "AND p.partner_hiv_status = '" + Constants.GeneralTestResult.NEGATIVE +"'";
            count = executeQuery(query);
            total+= count;
            
            query = "SELECT COUNT(DISTINCT p.partnerinformation_id) AS count FROM partnerinformation p JOIN maternalfollowup m ON p.patient_id  = m.patient_id WHERE m.facility_id = " + facilityId + " "
                    + "AND m.date_visit>= '" + reportingDateBegin + "' AND m.date_visit <= '" + reportingDateEnd + "' "
                    + "AND m.date_visit = p.date_visit AND "
                    + " (m.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_PP_LESS + "' OR "
                    + " m.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_PP_GREATER + "' OR "
                    + " m.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_PP_LESS + "' OR "
                    + " m.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_PP_GREATER + "') "
                    + "AND p.partner_hiv_status = '" + Constants.GeneralTestResult.NEGATIVE +"'";
            count = executeQuery(query);
            total+= count;
            
            parameterMap.put("pmtct19t", Integer.toString(total));
            total = 0;
            
              // No. partners of HIV +ve women tested HIV+
            query = "SELECT COUNT(DISTINCT p.partnerinformation_id) AS count FROM partnerinformation p JOIN anc a ON p.patient_id  = a.patient_id WHERE a.facility_id = " + facilityId + " "
                    + "AND a.date_visit>= '" + reportingDateBegin + "' AND a.date_visit <= '" + reportingDateEnd + "' "
                    + "AND a.date_visit = p.date_visit AND "
                    + "(a.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_ANC + "' OR "
                    + " a.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_ANC + "') "
                    + "AND p.partner_hiv_status = '" + Constants.GeneralTestResult.POSITIVE +"'";
            count = executeQuery(query);
            total+= count;
            
            query = "SELECT COUNT(DISTINCT p.partnerinformation_id) AS count FROM partnerinformation p JOIN delivery d ON p.patient_id  = d.patient_id WHERE d.facility_id = " + facilityId + " "
                    + "AND d.date_delivery>= '" + reportingDateBegin + "' AND d.date_delivery <= '" + reportingDateEnd + "' "
                    + "AND d.date_delivery = p.date_visit AND "
                    + "(d.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_LD + "' OR "
                    + " d.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_LD + "') "
                    + "AND p.partner_hiv_status = '" + Constants.GeneralTestResult.POSITIVE +"'";
            count = executeQuery(query);
            total+= count;
            
            query = "SELECT COUNT(DISTINCT p.partnerinformation_id) AS count FROM partnerinformation p JOIN maternalfollowup m ON p.patient_id  = m.patient_id WHERE m.facility_id = " + facilityId + " "
                    + "AND m.date_visit>= '" + reportingDateBegin + "' AND m.date_visit <= '" + reportingDateEnd + "' "
                    + "AND m.date_visit = p.date_visit AND "
                    + " (m.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_PP_LESS + "' OR "
                    + " m.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.NEW_PP_GREATER + "' OR "
                    + " m.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_PP_LESS + "' OR "
                    + " m.time_hiv_diagnosis = '" + Constants.TimeDiagnosis.PREVIOUS_PP_GREATER + "') "
                    + "AND p.partner_hiv_status = '" + Constants.GeneralTestResult.POSITIVE +"'";
            count = executeQuery(query);
            total+= count;
            
            parameterMap.put("pmtct20t", Integer.toString(total));
            
            // No. of  booked HIV posiive pregnant women who delivered at the facility
            query = "SELECT COUNT(DISTINCT delivery.patient_id) AS count FROM delivery WHERE  delivery.facility_id = " + facilityId + " "
                    + "AND delivery.date_delivery>= '" + reportingDateBegin + "' AND delivery.date_delivery <= '" + reportingDateEnd + "' "
                    + "AND  delivery.booking_status = '" + Constants.BookingStatus.BOOKED +"'";
            count = executeQuery(query);
            parameterMap.put("pmtct22t", Integer.toString(count));
            
            // No. of  unbooked HIV posiive pregnant women who delivered at the facility
            query = "SELECT COUNT(DISTINCT delivery.patient_id) AS count FROM delivery WHERE  delivery.facility_id = " + facilityId + " "
                    + "AND delivery.date_delivery>= '" + reportingDateBegin + "' AND delivery.date_delivery <= '" + reportingDateEnd + "' "
                    + "AND  delivery.booking_status = '" + Constants.BookingStatus.UNBOOKED +"'";
            count = executeQuery(query);
            parameterMap.put("pmtct23t", Integer.toString(count));
            
            // No. of  live births by HIV+ women who delivered in the facility.
            query = "SELECT COUNT(DISTINCT c.child_id) AS count FROM child c JOIN delivery d ON c.delivery_id = d.delivery_id WHERE d.facility_id = " + facilityId + " "
                    + "AND d.date_delivery >= '" + reportingDateBegin + "' AND d.date_delivery <= '" + reportingDateEnd + "' "
                    + "AND  c.status = '" + Constants.ChildStatus.ALIVE +"'";
            count = executeQuery(query);
            parameterMap.put("pmtct24t", Integer.toString(count));
            
             //No of babies born to hepatitis B positive women
            query = "SELECT COUNT(DISTINCT c.child_id) AS count FROM child c JOIN delivery d ON c.delivery_id = d.delivery_id WHERE d.facility_id = " + facilityId + " "
                    + "AND d.date_delivery >= '" + reportingDateBegin + "' AND d.date_delivery <= '" + reportingDateEnd + "' "
                    + "AND d.hepatitisb_status = '" + Constants.GeneralTestResult.POSITIVE +"'";
            count = executeQuery(query);
            parameterMap.put("pmtct25t", Integer.toString(count));
            
            //No of babies born to hepatitis B positive women who received immuniglobulin within 24 hours
            query = "SELECT COUNT(DISTINCT c.child_id) AS count FROM child c JOIN delivery d ON c.delivery_id = d.delivery_id "
                    + "WHERE d.facility_id = " + facilityId + " "
                    + "AND d.date_delivery >= '" + reportingDateBegin + "' AND d.date_delivery <= '" + reportingDateEnd + "' "
                    + "AND  d.hepatitisb_status = '" + Constants.GeneralTestResult.POSITIVE + "' AND c.hepb = '" + Constants.YesNoOption.YES +"'";
            count = executeQuery(query);
            parameterMap.put("pmtct26t", Integer.toString(count));
            
            //No of babies who received HBV within 24hrs
            query = "SELECT COUNT(DISTINCT c.child_id) AS count FROM child c WHERE  c.facility_id = " + facilityId + " "
                    + "AND c.date_birth >= '" + reportingDateBegin + "' AND c.date_birth <= '" + reportingDateEnd + "' "
                    + "AND c.hbv = '" + Constants.YesNoOption.YES +"'";
            count = executeQuery(query);
            parameterMap.put("pmtct27t", Integer.toString(count));
            total = 0;
            
            //No of babies who received ARV within 72hrs In Facilitty
            query = "SELECT COUNT(DISTINCT childfollowup.child_id) AS count FROM childfollowup WHERE childfollowup.facility_id = " + facilityId + " "
                    + "AND childfollowup.date_visit >= '" + reportingDateBegin + "' AND childfollowup.date_visit <= '" + reportingDateEnd + "' "
                    + "AND childfollowup.arv_timing = '" + Constants.ArvTiming.IN_FACILITY_72HRS + "'";
            count = executeQuery(query);
            total+=count;
            parameterMap.put("pmtct28a", Integer.toString(count));
            
             //No of babies who received ARV within 72hrs Outside facility
            query = "SELECT COUNT(DISTINCT childfollowup.child_id) AS count FROM childfollowup WHERE childfollowup.facility_id = " + facilityId + " "
                    + "AND childfollowup.date_visit >= '" + reportingDateBegin + "' AND childfollowup.date_visit <= '" + reportingDateEnd + "' "
                    + "AND childfollowup.arv_timing = '" + Constants.ArvTiming.OUT_FACILITY_72HRS +"'";
            count = executeQuery(query);
            total+=count;
            parameterMap.put("pmtct28b", Integer.toString(count));
            
            parameterMap.put("pmtct28t", Integer.toString(total));
            total = 0;
            
            //No of babies who received ARV after 72hrs within facility
            query = "SELECT COUNT(DISTINCT childfollowup.child_id) AS count FROM childfollowup WHERE childfollowup.facility_id = " + facilityId + " "
                    + "AND childfollowup.date_visit >= '" + reportingDateBegin + "' AND childfollowup.date_visit <= '" + reportingDateEnd + "' "
                    + "AND childfollowup.arv_timing = '" + Constants.ArvTiming.IN_FACILITY_AFTER_72HRS +"'";
            count = executeQuery(query);
            total+=count;
            parameterMap.put("pmtct29a", Integer.toString(count));
            
            //No of babies who received ARV after 72hrs outside facility
            query = "SELECT COUNT(DISTINCT childfollowup.child_id) AS count FROM childfollowup WHERE childfollowup.facility_id = " + facilityId + " "
                    + "AND childfollowup.date_nvp_initiated >= '" + reportingDateBegin + "' AND childfollowup.date_nvp_initiated <= '" + reportingDateEnd + "' "
                    + "AND childfollowup.arv_timing = '" + Constants.ArvTiming.OUT_FACILITY_AFTER_72HRS +"'";
            count = executeQuery(query);
            total+=count;
            parameterMap.put("pmtct29b", Integer.toString(count));
            
            parameterMap.put("pmtct29t", Integer.toString(total));
            total = 0;

            //No of infants born to HIV infected women started on CTX prophylaxis within 2 months of birth
            query = "SELECT COUNT(DISTINCT childfollowup.child_id) AS count FROM childfollowup WHERE childfollowup.facility_id = " + facilityId + " "
                    + "AND childfollowup.date_cotrim_initiated >= '" + reportingDateBegin + "' AND childfollowup.date_cotrim_initiated <= '" + reportingDateEnd + "' "
                    + "AND childfollowup.cotrim = '" + Constants.YesNoOption.YES + "' AND childfollowup.age_cotrim_initiated <= '" + 8 +"'";
            count = executeQuery(query);
            parameterMap.put("pmtct30t", Integer.toString(count));
            
            //No of infants whose blood samples were taken for PCR within 2 months of birth
            query = "SELECT COUNT(DISTINCT cf.child_id) AS count FROM childfollowup cf JOIN child c ON cf.child_id = c.child_id WHERE cf.facility_id = " + facilityId + " "
                    + "AND cf.date_sample_collected >= '" + reportingDateBegin + "' AND cf.date_sample_collected <= '" + reportingDateEnd + "' "
                    + "AND MONTH(cf.date_sample_collected) - MONTH(c.date_birth) <= 2";
            count = executeQuery(query);
            parameterMap.put("pmtct31t", Integer.toString(count));
            
            //No of infants whose blood samples were taken for PCR within 2 to 12 months of birth
            query = "SELECT COUNT(DISTINCT cf.child_id) AS count FROM childfollowup cf JOIN child c ON cf.child_id = c.child_id WHERE cf.facility_id = " + facilityId + " "
                    + "AND cf.date_sample_collected >= '" + reportingDateBegin + "' AND cf.date_sample_collected <= '" + reportingDateEnd + "' "
                    + "AND (MONTH(cf.date_sample_collected) - MONTH(c.date_birth) > 2) AND "
                    + "(MONTH(cf.date_sample_collected) - MONTH(c.date_birth) <= 12)";
            count = executeQuery(query);
            parameterMap.put("pmtct32t", Integer.toString(count));
            total = 0;
            
            //No of HIV PCR Results received for babies whose samples were taken within 2 months of birth (Positive)
            query = "SELECT COUNT(DISTINCT cf.child_id) AS count FROM childfollowup cf JOIN child c ON cf.child_id = c.child_id WHERE cf.facility_id = " + facilityId + " "
                    + "AND cf.date_pcr_result >= '" + reportingDateBegin + "' AND cf.date_pcr_result <= '" + reportingDateEnd + "' "
                    + "AND MONTH(cf.date_sample_collected) - MONTH(c.date_birth) <= 2 AND pcr_result = '" + Constants.TestResult.POSITIVE + "'";
            count = executeQuery(query);
            total += count;
            parameterMap.put("pmtct33a", Integer.toString(count));
            
            //No of HIV PCR Results received for babies whose samples were taken within 2 months of birth (Negative)
            query = "SELECT COUNT(DISTINCT cf.child_id) AS count FROM childfollowup cf JOIN child c ON cf.child_id = c.child_id WHERE cf.facility_id = " + facilityId + " "
                    + "AND cf.date_pcr_result >= '" + reportingDateBegin + "' AND cf.date_pcr_result <= '" + reportingDateEnd + "' "
                    + "AND MONTH(cf.date_sample_collected) - MONTH(c.date_birth) <= 2 AND pcr_result = '" + Constants.TestResult.NEGATIVE +"'";
            count = executeQuery(query);
            total += count;
            parameterMap.put("pmtct33b", Integer.toString(count));
            
            parameterMap.put("pmtct33t", Integer.toString(total));
            total = 0;
            
            //No of HIV PCR Results received for babies whose samples were taken within 2 - 12 months of birth (Positive)
            query = "SELECT COUNT(DISTINCT cf.child_id) AS count FROM childfollowup cf JOIN child c ON cf.child_id = c.child_id WHERE cf.facility_id = " + facilityId + " "
                    + "AND cf.date_pcr_result >= '" + reportingDateBegin + "' AND cf.date_pcr_result <= '" + reportingDateEnd + "' "
                    + "AND ( MONTH(cf.date_sample_collected) - MONTH(c.date_birth) > 2 AND "
                    + "MONTH(cf.date_sample_collected) - MONTH(c.date_birth) <= 12 ) AND pcr_result = '" + Constants.TestResult.POSITIVE + "'";
            count = executeQuery(query);
            total += count;
            parameterMap.put("pmtct34a", Integer.toString(count));
            
            //No of HIV PCR Results received for babies whose samples were taken within 2 - 12 months of birth (Negative)
            query = "SELECT COUNT(DISTINCT cf.child_id) AS count FROM childfollowup cf JOIN child c ON cf.child_id = c.child_id WHERE cf.facility_id = " + facilityId + " "
                    + "AND cf.date_pcr_result >= '" + reportingDateBegin + "' AND cf.date_pcr_result <= '" + reportingDateEnd + "' "
                    + "AND (MONTH(cf.date_sample_collected) - MONTH(c.date_birth) > 2 AND "
                    + "MONTH(cf.date_sample_collected) - MONTH(c.date_birth) <= 12) AND pcr_result = '" + Constants.TestResult.NEGATIVE + "'";
            count = executeQuery(query);
            total += count;
            parameterMap.put("pmtct34b", Integer.toString(count));
            
            parameterMap.put("pmtct34t", Integer.toString(total));
            total = 0;
            
            //No of HIV exposed babies who tested for HIV within 18 months of birth by Rapid Test (Positive)
            query = "SELECT COUNT(DISTINCT cf.child_id) AS count FROM childfollowup cf WHERE cf.facility_id = " + facilityId + " "
                    + "AND cf.date_rapid_test >= '" + reportingDateBegin + "' AND cf.date_rapid_test <= '" + reportingDateEnd + "' "
                    + "AND cf.rapid_test = '" + Constants.YesNoOption.YES + "' AND cf.rapid_test_result = '" + Constants.RapidTestResult.POSITIVE + "'";
            count = executeQuery(query);
            total += count;
            parameterMap.put("pmtct35a", Integer.toString(count));
            
            //No of HIV exposed babies who tested for HIV within 18 months of birth by Rapid Test (Negative)
           query = "SELECT COUNT(DISTINCT cf.child_id) AS count FROM childfollowup cf WHERE cf.facility_id = " + facilityId + " "
                    + "AND cf.date_rapid_test >= '" + reportingDateBegin + "' AND cf.date_rapid_test <= '" + reportingDateEnd + "' "
                    + "AND cf.rapid_test = '" + Constants.YesNoOption.YES + "' AND cf.rapid_test_result = '" + Constants.RapidTestResult.NEGATIVE + "'";
            count = executeQuery(query);
            total += count;
            parameterMap.put("pmtct35b", Integer.toString(count));
            
            parameterMap.put("pmtct35t", Integer.toString(total));
            
            System.out.println("...after end");

            parameterMap.put("reportingMonth", reportingMonth);
            parameterMap.put("reportingYear", reportingYear);

            // fetch the required records from the database   
            query = "SELECT DISTINCT facility.name, facility.address1, facility.address2, facility.phone1, facility.phone2, facility.email, lga.name AS lga, state.name AS state FROM facility JOIN lga ON facility.lga_id = lga.lga_id JOIN state ON facility.state_id = state.state_id WHERE facility_id = " + facilityId;
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                parameterMap.put("facilityName", resultSet.getString("name"));
                parameterMap.put("lga", resultSet.getString("lga"));
                parameterMap.put("state", resultSet.getString("state"));
            }
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }

        System.out.println("...Before returning parameter map");

        return parameterMap;
    }

    private int executeQuery(String query) {
        int count = 0;
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (Exception exception) {


            System.out.println("...ExcepCount - " + exception);

            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return count;
    }

    private void executeUpdate(String query) {
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {


            System.out.println("...ExcepUpdate - " + exception);

            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }
}
