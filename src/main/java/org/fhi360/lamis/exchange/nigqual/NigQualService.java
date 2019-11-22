
/**
 * @author user1
 */
package org.fhi360.lamis.exchange.nigqual;


import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.model.Laboratory;
import org.fhi360.lamis.model.dto.DataDTO;
import org.fhi360.lamis.model.dto.NigqualServiceDTO;
import org.fhi360.lamis.service.DeleteService;
import org.fhi360.lamis.service.MonitorService;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.RandomNumberGenerator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
@Component
public class NigQualService {
    private final JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);
    private final TransactionTemplate transactionTemplate = ContextProvider.getBean(TransactionTemplate.class);
    private int reportingMonthBegin;
    private int reportingYearBegin;
    private int reportingMonthEnd;
    private int reportingYearEnd;
    private String reportingDateBegin;
    private String reportingDateEnd;
    private long facilityId;
    private  final DeleteService deleteService = ContextProvider.getBean(DeleteService.class);

    private HttpServletRequest request;
    private HttpSession session;
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;

    private int reviewPeriodId;
    private int portalId;
    private int rnl;


    public NigQualService() {

    }

    public void generateCohort(NigqualServiceDTO nigqualServiceDTO, Long facilityId) {
        reportingMonthBegin = DateUtil.getMonth(nigqualServiceDTO.getReportingMonthBegin());
        reportingYearBegin = Integer.parseInt(nigqualServiceDTO.getReportingYearBegin());
        reportingMonthEnd = DateUtil.getMonth(nigqualServiceDTO.getReportingMonthEnd());
        reportingYearEnd = Integer.parseInt(nigqualServiceDTO.getReportingYearEnd());
        reportingDateBegin = DateUtil.parseDateToString(DateUtil.getFirstDateOfMonth(reportingYearBegin, reportingMonthBegin), "yyyy-MM-dd");
        reportingDateEnd = DateUtil.parseDateToString(DateUtil.getLastDateOfMonth(reportingYearEnd, reportingMonthEnd), "yyyy-MM-dd");
        reviewPeriodId = Integer.parseInt(nigqualServiceDTO.getReviewPeriodId());
        portalId = Integer.parseInt(nigqualServiceDTO.getPortalId());
        rnl = Integer.parseInt(nigqualServiceDTO.getRnl());

        //delete all record for this review period for this facility
        deleteService.deleteNigqual(facilityId, reviewPeriodId);
        //then log identity of the deleted nigqual record in the monitor for the server to effect changes with synced to the server
        String entityId = request.getParameter("reviewPeriodId");
        MonitorService.logEntity(entityId, "niqual", 3);

        generate("AD");
        generate("PD");
        //generate("PB");  //Booked pregnant women who received ante natal care 6 months prior to 
        //generate("PU");  //Unbooked pregnant women who delivered at the facility 6 months prior to 
        //generate("DL");  //All deliveries during the 6 months of review period
        //generate("DT");  //All deliveries 12 - 18 months prior to 
    }

    private void generate(String thermaticArea) {
        int sampleSize = 0;
        final int[] reccount = {0};
        Map<Integer, Long> entityMap = new HashMap<Integer, Long>();
        if (thermaticArea.equals("AD"))
            query = "SELECT DISTINCT patient_id, current_status FROM patient WHERE " +
                    "TIMESTAMPDIFF(YEAR, date_birth, CURDATE()) >= 15 AND facility_id = " +
                    facilityId + " AND date_registration < '" + reportingDateBegin + "' " +
                    "AND current_status IN ('HIV+ non ART', 'ART Start', 'ART Restart', 'ART Transfer In', " +
                    "'Pre-ART Transfer In') ORDER BY patient_id";
        if (thermaticArea.equals("PD"))
            query = "SELECT DISTINCT patient_id, current_status FROM patient WHERE " +
                    "TIMESTAMPDIFF(YEAR, date_birth, CURDATE()) < 15 AND facility_id = " + facilityId + " " +
                    "AND date_registration < '" + reportingDateBegin + "' AND current_status IN " +
                    "('HIV+ non ART', 'ART Start', 'ART Restart', 'ART Transfer In', 'Pre-ART Transfer In')" +
                    " ORDER BY patient_id";
        if (thermaticArea.equals("PM"))
            query = "SELECT DISTINCT patient.patient_id, patient.current_status FROM patient JOIN anc ON " +
                    "patient.patient_id = anc.patient_id AND patient.facility_id = anc.facility_id JOIN " +
                    "delivery ON patient.patient_id = delivery.patient_id AND patient.facility_id = " +
                    "delivery.facility_id WHERE patient.gender = 'Female' AND patient.facility_id = " +
                    facilityId + " AND patient.date_registration <= '" + reportingDateBegin + "' AND " +
                    "patient.current_status IN ('HIV+ non ART', 'ART Start', 'ART Restart', 'ART Transfer In', " +
                    "'Pre-ART Transfer In', 'Known Death') ORDER BY patient.patient_id";
        jdbcTemplate.query(query, rs -> {
            while (rs.next()) {
                if (!exclude(rs.getLong("patient_id"),
                        rs.getString("current_status"), thermaticArea)) {
                    reccount[0]++;
                    entityMap.put(reccount[0], rs.getLong("patient_id"));
                    System.out.println("Processing......." + rs.getLong("patient_id"));
                }
            }
            return null;
        });

        //If Generate cohort using Random Number List is unchecked, use all clients in the selected cohort
        //else apply the RNL
        if (rnl == 0) {
            sampleSize = reccount[0];
        } else {
            if (reccount[0] < 20) {
                sampleSize = reccount[0];
            } else {
                if (reccount[0] >= 5000) {
                    sampleSize = 150;
                } else {
                    sampleSize = getSampleSize(reccount[0]);
                }
            }
        }

//            RECCOUNT is the number of patients who were registered before the beginning of the review period 
//            with a clinic or refill visit 6/3 month before the beginning of the review period
//            
//            SAMPLE SIZE is the number of patients to be evaluated using RNL
//            
//            Generate random numbers beginning from 1 to reccount
//            Then get the hospital numbers from the entity map
        int numbers[] = new RandomNumberGenerator().randomUnique(sampleSize, 1, reccount[0]);
        for (int i = 0; i <= numbers.length; i++) {
            long patientId = entityMap.get(numbers[i]);
            query = "INSERT INTO nigqual (facility_id, portal_id, reporting_date_begin, reporting_date_end, " +
                    "review_period_id, thermatic_area, patient_id, population, sample_size) " +
                    "VALUES(" + facilityId + ", " + portalId + ", '" + reportingDateBegin + "', '" +
                    reportingDateEnd + "', '" + reviewPeriodId + "', '" + thermaticArea + "', " +
                    patientId + ", " + reccount[0] + "," + sampleSize + ")";
            transactionTemplate.execute(status -> {
                jdbcTemplate.update(query);
                return null;
            });
        }
    }

    private int getSampleSize(int population) {

        //query = "SELECT size FROM population WHERE lower => " + population + " AND upper <= " + population; //This query is not the same as following query
        String query = "SELECT size FROM samplesize WHERE " + population + " >= lower AND " +
                population + " <= upper";
        Integer size = jdbcTemplate.queryForObject(query, Integer.class);
        return size != null ? size : 0;
    }

    private boolean exclude(long patientId, String currentStatus, String thermaticArea) {
        final boolean[] noClinic = {false};
        final boolean[] noRefill = {false};
        final boolean[] exclude = {true};

        try {
            ResultSet rs = null;
            if (thermaticArea.equalsIgnoreCase("AD")) {

                //Get last refill visit
                String query = null;//new PharmacyJDBC().getLastRefillVisit(id);
                jdbcTemplate.query(query, rs1 -> {
                    noRefill[0] = rs.getDate("date_visit") == null ? true :
                            DateUtil.formatDate(rs.getDate("date_visit"), "MM/dd/yyyy")
                                    .before(DateUtil.addMonth(DateUtil.formatDateStringToDate(
                                            reportingDateBegin,
                                            "yyyy-MM-dd", "MM/dd/yyyy"), -3)) ? true : false;
                });
//
//                //Get last clinic visit
//                jdbcTemplate.query(new ClinicJDBC().getLastClinicVisit(id), rs1 -> {
//                    if (currentStatus.equalsIgnoreCase("HIV+ non ART")) {
//                        noClinic[0] = rs1.getDate("date_visit") == null ? true :
//                                DateUtil.formatDate(rs1.getDate("date_visit"), "MM/dd/yyyy")
//                                        .before(DateUtil.addMonth(DateUtil.formatDateStringToDate(
//                                                reportingDateBegin, "yyyy-MM-dd", "MM/dd/yyyy"), -6)) ?
//                                        true : false;
//                    } else {
//                        noClinic[0] = rs1.getDate("date_visit") == null ? true :
//                                DateUtil.formatDate(rs1.getDate("date_visit"), "MM/dd/yyyy")
//                                        .before(DateUtil.addMonth(DateUtil.formatDateStringToDate(
//                                                reportingDateBegin, "yyyy-MM-dd", "MM/dd/yyyy"), -3)) ?
//                                        true : false;
//                    }
//                });
                //Exclude patient without clinic or refill visit 
                if (noRefill[0] || noClinic[0]) {
                    exclude[0] = true;
                } else {
                    exclude[0] = false;
                }

            }

            if (thermaticArea.equalsIgnoreCase("PD")) {
                //Get last clinic visit
//                jdbcTemplate.query(new ClinicJDBC().getLastClinicVisit(id), rs1 -> {
//                    exclude[0] = rs1.getDate("date_visit") == null ? true :
//                            DateUtil.formatDate(rs1.getDate("date_visit"), "MM/dd/yyyy")
//                                    .before(DateUtil.addMonth(DateUtil.formatDateStringToDate(
//                                            reportingDateBegin, "yyyy-MM-dd", "MM/dd/yyyy"), -6))
//                                    ? true : false;
//                });
            }
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return exclude[0];
    }
}
