/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.nigqual;

/**
 * @author user1
 */

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.model.Clinic;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.Pharmacy;
import org.fhi360.lamis.model.dto.DataDTO;
import org.fhi360.lamis.model.repositories.ClinicRepository;
import org.fhi360.lamis.model.repositories.PharmacyRepository;
import org.fhi360.lamis.utility.DateUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

@Component
public class PediatricsDataConverter {
    private final JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);
    private HttpServletRequest request;
    private HttpSession session;
    private String query;

    private final ClinicRepository clinicRepository;
    private final PharmacyRepository pharmacyRepository;

    private String FacilityID;
    private String UploaderId;
    private String UploadDt;
    private String webUploadFlag;
    private String ReviewPeriodID;

    private long patientId;
    private String hospitalNum;
    private String ARTAdherenceAssessmentPerformedDuringLast3Months;
    private String LastDateOfAssessment;
    private String PatientOnARTAnytimeDuringReviewPeriod;
    private String C1stRegminen;
    private String C1stRegimenStartDate;
    private String C1stRegimenChangeDate;
    private String C2ndRegimen;
    private String C2ndRegimenStartDate;
    private String C2ndRegimenChangeDate;
    private String C3rdRegimen;
    private String C3rdRegimenStartDate;
    private String C3rdRegimenChangeDate;
    private String OtherRegimenSpecify;
    private String CD4_Count;
    private String CD4_Count_Date;
    private String Weight;
    private String Weight_Date;
    private String WHO_Clinical_Stage;
    private String WHO_Clinical_State_Date;
    private String CD4_Not_Recorded;
    private String Weight_Not_Recorded;
    private String WHO_Clinical_Stage_Not_Recorded;
    private String PatientEverStartedOnART;
    private String ART_Start_Date;
    private String Visit1;
    private String Visit2;
    private String Visit3;
    private String Visit4;
    private String PatientCurrentlyOnCotrimoxazoleProphylaxis;
    private String DateOfFirstPrescription;
    private String AgeOfFirstPrescription;
    private String UnitOfAgeMeasure;

    private String Status;
    private String DateOfStatusChange;
    private String Reason;
    private String Transferred_Out;
    private String Death;
    private String Discontinued_Care;
    private String Transferred_Out_Date;
    private String Death_Date;
    private String Discontinued_Care_Date;
    private String Discontinued_Care_Reason;
    private String Discontinued_Care_Reason_Other;

    private String PDG001_VALUE;
    private String PDG001_DATE;
    private String PDG002_VALUE;
    private String PDG002_DATE;
    private String PDG003_VALUE;
    private String PDG003_DATE;
    private String PDG004_VALUE;
    private String PDG004_DATE;
    private String PDG005_VALUE;
    private String PDG005_DATE;
    private String PDG006_VALUE;
    private String PDG006_DATE;
    private String PDG007_VALUE;
    private String PDG007_DATE;
    private String PDG008_VALUE;
    private String PDG008_DATE;
    private String PDG009_VALUE;
    private String PDG009_DATE;
    private String PDG010_VALUE;
    private String PDG010_DATE;
    private String PDG011_VALUE;
    private String PDG011_DATE;
    private String PDG012_VALUE;
    private String PDG012_DATE;
    private String PDG013_VALUE;
    private String PDG013_DATE;
    private String PDG014_VALUE;
    private String PDG014_DATE;
    private String PDG015_VALUE;
    private String PDG015_DATE;
    private String PDG016_VALUE;
    private String PDG016_DATE;
    private String PDG017_VALUE;
    private String PDG017_DATE;
    private String PDG018_VALUE;
    private String PDG018_DATE;
    private String PDG019_VALUE;
    private String PDG019_DATE;
    private String PDG020_VALUE;
    private String PDG020_DATE;
    private String PDG021_VALUE;
    private String PDG021_DATE;
    private String PDG022_VALUE;
    private String PDG022_DATE;
    private String PDG023_VALUE;
    private String PDG023_DATE;
    private String PDG024_VALUE;
    private String PDG024_DATE;
    private String HasThePatientReceivedViralLoadTesting;
    private String ViralLoadTestResult;
    private String ViralLoadTestDate;

    private String PatientAssessedForAdverseEffectsDuringReviewPeriod;

    private String MissedAppointment;
    private String AttemptedContact;
    private String DateOfAttemptedContact;
    private String OutcomeOfTracking;
    private String ReasonFor_LTFU;
    private String CauseOfDeath;
    private String Missed_appointment_1;
    private String Missed_appointment_1_Date;
    private String Missed_appointment_1_AttemptedContact;
    private String Missed_appointment_1_AttemptedContactDate;
    private String Missed_appointment_1_OutcomeOfTracking;
    private String Missed_appointment_1_ReasonForLFTU;
    private String Missed_appointment_1_CauseOfDeath;
    private String Missed_appointment_2;
    private String Missed_appointment_2_Date;
    private String Missed_appointment_2_AttemptedContact;
    private String Missed_appointment_2_AttemptedContactDate;
    private String Missed_appointment_2_OutcomeOfTracking;
    private String Missed_appointment_2_ReasonForLFTU;
    private String Missed_appointment_2_CauseOfDeath;
    private String Missed_appointment_3;
    private String Missed_appointment_3_Date;
    private String Missed_appointment_3_AttemptedContact;
    private String Missed_appointment_3_AttemptedContactDate;
    private String Missed_appointment_3_OutcomeOfTracking;
    private String Missed_appointment_3_ReasonForLFTU;
    private String Missed_appointment_3_CauseOfDeath;
    private String PatientOnTBTreatmentDuringReviewPeriod;
    private String PatientClinicallyScreenForTBDuringReviewPeriod;
    private String TBClinicalScreeningCriteria;
    private String BasedOnScreeningWasPatientedSuspectedToHaveTB;
    private String PatientHdChestXRay;
    private String PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture;
    private String PatientDiagnosedOfTBInReviewPeriod;
    private String TBDiagnosis_Date;
    private String PatientStartTBTreatment;
    private String TB_TreatmentStartDate;
    private String TBScreeningCriteria_CurrentCough;
    private String TBScreeningCriteria_ContactWithTBCase;
    private String TBScreeningCriteria_PoorWeighGain;

    private boolean pediatric_art_adherence_record;
    private boolean pediatric_artregimensincestartingtreatment_record;
    private boolean pediatric_baselineparameters_record;
    private boolean pediatric_clinicalevaluationinreviewperiod_record;
    private boolean pediatric_cotrimoxazole_record;
    private boolean pediatric_patientstatus_record;
    private boolean pediatric_patientmonitoringduringreviewperiod_record;
    private boolean pediatric_missedappointmentandpatienttracking_record;
    private boolean pediatric_tuberculosis_record;

    public PediatricsDataConverter(ClinicRepository clinicRepository, PharmacyRepository pharmacyRepository) {
        this.clinicRepository = clinicRepository;

        this.pharmacyRepository = pharmacyRepository;
    }


    public synchronized void convertXml(DataDTO dataDTO, Long facilityId) {
        String portalId = dataDTO.getPortalId();
        String reviewPeriodId = dataDTO.getReviewPeriodId();
        String reportingDateBegin = dataDTO.getReportingDateBegin();
        String reportingDateEnd = dataDTO.getReportingDateEnd();

        FacilityID = portalId;
        UploaderId = "NULL";
        UploadDt = "NULL";
        webUploadFlag = "No";
        ReviewPeriodID = reviewPeriodId;
        Document document1 = DocumentHelper.createDocument();
        Element root1 = document1.addElement("data-set");

        Document document2 = DocumentHelper.createDocument();
        Element root2 = document2.addElement("data-set");

        Document document3 = DocumentHelper.createDocument();
        Element root3 = document3.addElement("data-set");

        Document document4 = DocumentHelper.createDocument();
        Element root4 = document4.addElement("data-set");

        Document document5 = DocumentHelper.createDocument();
        Element root5 = document5.addElement("data-set");

        Document document6 = DocumentHelper.createDocument();
        Element root6 = document6.addElement("data-set");

        Document document7 = DocumentHelper.createDocument();
        Element root7 = document7.addElement("data-set");

        Document document8 = DocumentHelper.createDocument();
        Element root8 = document8.addElement("data-set");

        Document document9 = DocumentHelper.createDocument();
        Element root9 = document9.addElement("data-set");

        query = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.date_birth, patient.date_started FROM patient JOIN nigqual ON patient.facility_id = nigqual.facility_id AND patient.patient_id = nigqual.patient_id "
                + " WHERE patient.facility_id = " + facilityId + " AND nigqual.portal_id = " + Long.parseLong(portalId) + " AND nigqual.review_period_id = " + Long.parseLong(reviewPeriodId) + " AND nigqual.thermatic_area = 'PD'";

        jdbcTemplate.query(query, resultSet -> {
            while (resultSet.next()) {
                initVariables();
                //Table ART
                patientId = resultSet.getLong("patient_id");
                hospitalNum = resultSet.getString("hospital_num"); //Long.toString(resultSet.getLong("patient_id"));
                PatientEverStartedOnART = (resultSet.getDate("date_started") == null) ? "NO" : "YES";
                ART_Start_Date = (resultSet.getDate("date_started") == null) ? "" : DateUtil.parseDateToString(resultSet.getDate("date_started"), "dd/MM/yyyy");

                if (PatientEverStartedOnART.equals("YES")) {
                    PatientOnARTAnytimeDuringReviewPeriod = DateUtil.formatDate(resultSet.getDate("date_started"), "MM/dd/yyyy").before(DateUtil.parseStringToDate(reportingDateBegin, "MM/dd/yyyy")) ? "NO" : "YES";
                } else {
                    PatientOnARTAnytimeDuringReviewPeriod = "NO";
                }

                //Table ART_Adherence
                ARTAdherenceAssessmentPerformedDuringLast3Months = "NO";
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                Clinic clinicQuery = clinicRepository.getLastClinicVisit(patient);

                Date dateVisist = DateUtil.convertToDateViaSqlDate(clinicQuery.getDateVisit());
                LastDateOfAssessment = (dateVisist == null) ? "" :
                        DateUtil.parseDateToString(dateVisist, "dd/MM/yyyy");
                //If the last clinic visit is after the end of the reporting minus 3 months, the adherence was performed during the last 3 months
                ARTAdherenceAssessmentPerformedDuringLast3Months =
                        DateUtil.formatDate(dateVisist, "MM/dd/yyyy")
                                .before(DateUtil.addMonth(DateUtil.parseStringToDate(
                                        reportingDateEnd, "MM/dd/yyyy"), -3)) ? "NO" : "YES";

                //Table ARTRegimenDuringReviewPeriod
                //Get ART commencement data
                Patient patient1 = new Patient();
                patient1.setPatientId(patientId);
                Clinic clinic = clinicRepository.getArtCommencement(patient1);
                if (clinic != null) {
                    C1stRegminen = clinic.getRegimenType() == null ? "" :
                            clinic.getRegimenType().contains("ART First Line") ? "1" : "2";
                    Date date = DateUtil.convertToDateViaSqlDate(clinic.getDateVisit());
                    C1stRegimenStartDate = clinic.getDateVisit() == null ? "" :
                            DateUtil.parseDateToString(date, "dd/MM/yyyy");
                    pediatric_artregimensincestartingtreatment_record = true;
                }


                //Get last refill visit
                Patient patient2 = new Patient();
                patient2.setPatientId(patientId);
                Pharmacy pharmacy = pharmacyRepository.getLastRefillByPatient(patient2);
                if (pharmacy.getRegimenType().getId() == 3) C2ndRegimen = "1";
                if (pharmacy.getRegimenType().getId() == 4) C2ndRegimen = "2";
                Date date = DateUtil.convertToDateViaSqlDate(pharmacy.getDateVisit());
                C2ndRegimenStartDate = pharmacy.getDateVisit() == null ? "" :
                        DateUtil.parseDateToString(date, "dd/MM/yyyy");
                Patient patient4 = new Patient();
                patient4.setPatientId(patientId);
                Clinic rs1 = clinicRepository.getArtCommencement(patient4);
                CD4_Count = rs1.getCd4() == null ? "0" :
                        rs1.getCd4() == 0.0 ? "0" : Long.toString(Math.round(rs1.getCd4()));
                Date date1 = DateUtil.convertToDateViaSqlDate(rs1.getDateVisit());
                CD4_Count_Date = rs1.getDateVisit() == null ? "01/01/1900’" :
                        DateUtil.parseDateToString(date1, "dd/MM/yyyy");
                if (CD4_Count.equals("0")) {
                    query = "SELECT * FROM laboratory WHERE facility_id = " + facilityId + " AND patient_id = " +
                            patientId + " AND labtest_id = 1 AND date_reported <= '" +
                            rs1.getDateVisit() + "' ORDER BY date_reported DESC LIMIT 1";
                    CD4_Count = "0";
                    CD4_Count_Date = "01/01/1900";
                    jdbcTemplate.query(query, rs2 -> {
                        CD4_Count = rs2.getObject("resultab") == null ? "0" : rs2.getString("resultab");
                        if (CD4_Count.equals("0")) CD4_Count = rs2.getString("resultpc");
                        CD4_Count_Date = rs2.getDate("date_reported") == null ? "" :
                                DateUtil.parseDateToString(rs2.getDate("date_reported"), "dd/MM/yyyy");
                    });
                } else {
                    CD4_Count = rs1.getCd4() == null ? "0" :
                            rs1.getCd4() == 0.0 ? "0" :
                                    Long.toString(Math.round(rs1.getCd4()));
                    Date date2 = DateUtil.convertToDateViaSqlDate(rs1.getDateVisit());
                    CD4_Count_Date = date2 == null ? "" :
                            DateUtil.parseDateToString(date2, "dd/MM/yyyy");
                }

                Weight = Long.toString(Math.round(rs1.getBodyWeight()));
                Date date2 = DateUtil.convertToDateViaSqlDate(rs1.getDateVisit());
                Weight_Date = date2 == null ? "01/01/1900’" :
                        DateUtil.parseDateToString(date2, "dd/MM/yyyy");
                String clinicStage = rs1.getClinicStage() == null ? "" :
                        rs1.getClinicStage();
                WHO_Clinical_Stage = clinicStage.equalsIgnoreCase("STAGE I") ? "1" :
                        clinicStage.equalsIgnoreCase("STAGE II") ? "2" :
                                clinicStage.equalsIgnoreCase("STAGE III") ? "3" :
                                        clinicStage.equalsIgnoreCase("STAGE IV") ? "4" : "0";
                WHO_Clinical_State_Date = rs1.getDateVisit() == null ?
                        "01/01/1900’" : DateUtil.parseDateToString(
                        date2, "dd/MM/yyyy");
                if (rs1.getCd4() < 0 || rs1.getCd4() == 0) {
                    CD4_Not_Recorded = "YES";
                } else {
                    CD4_Not_Recorded = "NO";
                }

                if (rs1.getBodyWeight() < 0 || rs1.getCd4() == 0) {
                    Weight_Not_Recorded = "YES";
                } else {
                    Weight_Not_Recorded = "NO";
                }
                if (WHO_Clinical_Stage.trim().isEmpty()) {
                    WHO_Clinical_Stage_Not_Recorded = "YES";
                } else {
                    WHO_Clinical_Stage_Not_Recorded = "NO";
                }
                pediatric_baselineparameters_record = true;


                //Table ClinicalEvaluationVisitsInReviewPeriod
                query = "SELECT * FROM clinic WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_visit >= '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "' AND date_visit <= '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' ORDER BY date_visit ASC";  //i.e A, B, C, ....
                final int[] rec = {1};
                jdbcTemplate.query(query, rs7 -> {
                    String clinicStage1 = rs7.getString("clinic_stage") == null ? "" :
                            rs7.getString("clinic_stage");
                    clinicStage1 = clinicStage1.equalsIgnoreCase("STAGE I") ? "1" :
                            clinicStage1.equalsIgnoreCase("STAGE II") ? "2" :
                                    clinicStage1.equalsIgnoreCase("STAGE III") ? "3" :
                                            clinicStage1.equalsIgnoreCase("STAGE IV") ? "4" : "0";
                    if (rec[0] == 1) {
                        Visit1 = DateUtil.parseDateToString(
                                rs7.getDate("date_visit"), "dd/MM/yyyy");
                        PDG013_VALUE = clinicStage;
                        PDG013_DATE = Visit1;
                        PDG009_VALUE = Long.toString(Math.round(rs7.getDouble("body_weight")));
                        PDG009_DATE = Visit1;
                    }
                    if (rec[0] == 2) {
                        Visit2 = DateUtil.parseDateToString(
                                rs7.getDate("date_visit"), "dd/MM/yyyy");
                        PDG014_VALUE = clinicStage1;
                        PDG014_DATE = Visit1;
                        PDG010_VALUE = Long.toString(Math.round(rs7.getDouble("body_weight")));
                        PDG010_DATE = Visit1;
                    }
                    if (rec[0] == 3) {
                        Visit3 = DateUtil.parseDateToString(rs7.getDate("date_visit"), "dd/MM/yyyy");
                        PDG015_VALUE = clinicStage1;
                        PDG015_DATE = Visit1;
                        PDG011_VALUE = Long.toString(Math.round(rs7.getDouble("body_weight")));
                        PDG011_DATE = Visit1;
                    }
                    if (rec[0] == 4) {
                        Visit4 = DateUtil.parseDateToString(rs7.getDate("date_visit"), "dd/MM/yyyy");
                        PDG016_VALUE = clinicStage1;
                        PDG016_DATE = Visit1;
                        PDG012_VALUE = Long.toString(Math.round(rs7.getDouble("body_weight")));
                        PDG012_DATE = Visit1;
                    }
                    rec[0]++;
                    pediatric_clinicalevaluationinreviewperiod_record = true;
                });

                //ViralLoadTesting
                query = "SELECT * FROM laboratory WHERE facility_id = " + facilityId + " AND patient_id = " +
                        patientId + " AND date_reported >= '" + DateUtil.formatDateString(
                        reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "' " +
                        "AND date_reported <= '" + DateUtil.formatDateString(
                        reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' " +
                        "AND resultab REGEXP('(^[0-9]+$)') AND labtest_id = 16 ORDER BY date_reported DESC";
                HasThePatientReceivedViralLoadTesting = "NO";
                jdbcTemplate.query(query, rs6 -> {
                    String resultab = rs6.getString("resultab") == null ? "" : rs6.getString("resultab");
                    HasThePatientReceivedViralLoadTesting = "YES";
                    ViralLoadTestResult = resultab.isEmpty() ? "0" : resultab;
                    ViralLoadTestDate = rs6.getDate("date_reported") == null ? "01/01/1900" :
                            DateUtil.parseDateToString(rs6.getDate("date_reported"), "dd/MM/yyyy");
                });
                System.out.println("VL....");

                //Table COTRIMOXAZOLE
                //query = "SELECT date_visit FROM pharmacy WHERE facility_id = id AND patient_id = id AND regimentype_id = 8 AND date_visit >= '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "' AND date_visit <= '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "' ORDER BY date_visit DESC";  //i.e Z, Y, X, ....
                query = "SELECT date_visit FROM pharmacy WHERE facility_id = " + facilityId + " AND patient_id = " +
                        patientId + " AND regimentype_id = 8 ORDER BY date_visit ASC";  //i.e Z, Y, X, ....
                PatientCurrentlyOnCotrimoxazoleProphylaxis = "NO";
                jdbcTemplate.query(query, rs2 -> {
                    PatientCurrentlyOnCotrimoxazoleProphylaxis = "YES";
                    DateOfFirstPrescription = DateUtil.parseDateToString(
                            rs2.getDate("date_visit"), "dd/MM/yyyy");
                    Map map = DateUtil.getAge(resultSet.getDate("date_birth"),
                            rs2.getDate("date_visit"));
                    AgeOfFirstPrescription = ((Integer) map.get("age")).toString();
                    String ageUnit = (String) map.get("ageUnit");
                    UnitOfAgeMeasure = ageUnit.equalsIgnoreCase("Year(s)") ? "YEARS" : ageUnit.equalsIgnoreCase("Month(s)") ? "MONTHS" : "WEEKS";
                });


                // Patient Monitoring durig review period
                query = "SELECT * FROM laboratory WHERE facility_id = " + facilityId + " AND patient_id = " +
                        patientId + " AND date_reported >= '" + DateUtil.formatDateString(
                        reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "' " +
                        "AND date_reported <= '" + DateUtil.formatDateString(
                        reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' " +
                        "AND labtest_id IN (1, 9, 12, 15) ORDER BY date_reported ";
                rec[0] = 1;
                jdbcTemplate.query(query, rs5 -> {
                    long labtestId = rs5.getLong("labtest_id");
                    String resultab = rs5.getString("resultab") == null ? "" :
                            rs5.getString("resultab");
                    String resultpc = rs5.getString("resultpc") == null ? "" :
                            rs5.getString("resultpc");
                    if (rec[0] == 1) {
                        if (labtestId == 1) { //CD4
                            PDG001_VALUE = resultab.isEmpty() ? "0" : resultab;
                            PDG001_DATE = rs5.getDate("date_reported") == null
                                    ? "01/01/1900" : DateUtil.parseDateToString(
                                    rs5.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 1) { //CD4%
                            PDG005_VALUE = resultpc.isEmpty() ? "0" : resultpc;
                            PDG005_DATE = rs5.getDate("date_reported") == null ? "01/01/1900" :
                                    DateUtil.parseDateToString(rs5.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 9) {  //PCV
                            PDG017_VALUE = resultab.isEmpty() ? "0" : resultab;
                            PDG017_DATE = rs5.getDate("date_reported") == null ? "01/01/1900" :
                                    DateUtil.parseDateToString(rs5.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 15) {  //ALT
                            PDG021_VALUE = resultab.isEmpty() ? "0" : resultab;
                            PDG021_DATE = rs5.getDate("date_reported") == null ? "01/01/1900" :
                                    DateUtil.parseDateToString(rs5.getDate("date_reported"), "dd/MM/yyyy");
                        }
                    }

                    if (rec[0] == 2) {
                        if (labtestId == 1) { //CD4
                            PDG002_VALUE = resultab.isEmpty() ? "0" : resultab;
                            PDG002_DATE = rs5.getDate("date_reported") == null ? "01/01/1900" :
                                    DateUtil.parseDateToString(rs5.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 1) { //CD4%
                            PDG006_VALUE = resultpc.isEmpty() ? "0" : resultpc;
                            PDG006_DATE = rs5.getDate("date_reported") == null ? "01/01/1900" :
                                    DateUtil.parseDateToString(rs5.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 9) {  //PCV
                            PDG018_VALUE = resultab.isEmpty() ? "0" : resultab;
                            PDG018_DATE = rs5.getDate("date_reported") == null ? "01/01/1900" :
                                    DateUtil.parseDateToString(rs5.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 15) {  //ALT
                            PDG022_VALUE = resultab.isEmpty() ? "0" : resultab;
                            PDG022_DATE = rs5.getDate("date_reported") == null ? "01/01/1900" :
                                    DateUtil.parseDateToString(rs5.getDate("date_reported"), "dd/MM/yyyy");
                        }
                    }

                    if (rec[0] == 3) {
                        if (labtestId == 1) { //CD4
                            PDG003_VALUE = resultab.isEmpty() ? "0" : resultab;
                            PDG003_DATE = rs5.getDate("date_reported") == null ? "01/01/1900" :
                                    DateUtil.parseDateToString(rs5.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 1) { //CD4%
                            PDG007_VALUE = resultpc.isEmpty() ? "0" : resultpc;
                            PDG007_DATE = rs5.getDate("date_reported") == null ? "01/01/1900" :
                                    DateUtil.parseDateToString(rs5.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 9) {  //PCV
                            PDG019_VALUE = resultab.isEmpty() ? "0" : resultab;
                            PDG019_DATE = rs5.getDate("date_reported") == null ? "01/01/1900" :
                                    DateUtil.parseDateToString(rs5.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 15) {  //ALT
                            PDG023_VALUE = resultab.isEmpty() ? "0" : resultab;
                            PDG023_DATE = rs5.getDate("date_reported") == null ? "01/01/1900" :
                                    DateUtil.parseDateToString(rs5.getDate("date_reported"), "dd/MM/yyyy");
                        }
                    }
                    if (rec[0] == 4) {
                        if (labtestId == 1) { //CD4
                            PDG004_VALUE = resultab.isEmpty() ? "0" : resultab;
                            PDG004_DATE = rs5.getDate("date_reported") == null ? "01/01/1900" :
                                    DateUtil.parseDateToString(rs5.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 1) { //CD4%
                            PDG008_VALUE = resultpc.isEmpty() ? "0" : resultpc;
                            PDG008_DATE = rs5.getDate("date_reported") == null ? "01/01/1900" :
                                    DateUtil.parseDateToString(rs5.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 9) {  //PCV
                            PDG020_VALUE = resultab.isEmpty() ? "0" : resultab;
                            PDG020_DATE = rs5.getDate("date_reported") == null ?
                                    "01/01/1900" : DateUtil.parseDateToString(
                                    rs5.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 15) {  //ALT
                            PDG024_VALUE = resultab.isEmpty() ? "0" : resultab;
                            PDG024_DATE = rs5.getDate("date_reported") == null ? "01/01/1900" :
                                    DateUtil.parseDateToString(rs5.getDate("date_reported"), "dd/MM/yyyy");
                        }
                    }
                    rec[0]++;
                    pediatric_patientmonitoringduringreviewperiod_record = true;
                });

                //Status Change
                query = "SELECT * FROM statushistory WHERE facility_id = " + facilityId + " AND patient_id = " +
                        patientId + " AND date_current_status >= '" + DateUtil.formatDateString(
                        reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "' " +
                        "AND date_current_status <= '" + DateUtil.formatDateString(
                        reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' " +
                        "AND current_status IN ('ART Transfer Out', 'Pre-ART Transfer Out', 'Stopped Treatment', " +
                        "'Known Death') ORDER BY date_current_status DESC";
                jdbcTemplate.query(query, rs4 -> {
                    if (rs4.getString("current_status").trim().contains("Transfer Out")) {
                        Status = "TRANSFERRED OUT";
                        Transferred_Out = "YES";
                        Transferred_Out_Date = (rs4.getDate("date_current_status") == null) ? "01/01/1900" :
                                DateUtil.parseDateToString(rs4.getDate("date_current_status"), "dd/MM/yyyy");
                    }
                    if (rs4.getString("current_status").trim().contains("Stopped Treatment")) {
                        Status = "DISCONTINUED CARE";
                        Discontinued_Care = "YES";
                        Discontinued_Care_Date = (rs4.getDate("date_current_status") == null) ? "01/01/1900" :
                                DateUtil.parseDateToString(rs4.getDate("date_current_status"), "dd/MM/yyyy");
                        Discontinued_Care_Reason = "NOT INDICATED";
                        Discontinued_Care_Reason_Other = "";
                    }
                    if (rs4.getString("current_status").trim().contains("Known Death")) {
                        Status = "DEATH";
                        Death = "YES";
                        Death_Date = (rs4.getDate("date_current_status") == null) ? "01/01/1900" :
                                DateUtil.parseDateToString(rs4.getDate("date_current_status"), "dd/MM/yyyy");
                    }
                    pediatric_patientstatus_record = true;
                });

                //Pediatric_MissedAppointmentAndPatientTracking

                //Pediatric_Tuberculosis
                final boolean[] startedTB = {false};
                query = "SELECT date_visit, tb_status FROM clinic WHERE facility_id = " + facilityId + " " +
                        "AND patient_id = " + patientId + " AND tb_status IS NOT NULL";
                jdbcTemplate.query(query, rs3 -> {
                    String dateVisit = rs3.getDate("date_visit") == null ? "" :
                            DateUtil.parseDateToString(rs3.getDate("date_visit"), "dd/MM/yyyy");
                    String tbStatus = rs3.getString("tb_status") == null ? "" :
                            rs3.getString("tb_status");

                    if (!startedTB[0] && tbStatus.trim().equalsIgnoreCase("Currently on TB treatment")) {
                        PatientStartTBTreatment = "YES";
                        TB_TreatmentStartDate = rs3.getDate("date_visit") == null ? "01/01/1900" :
                                DateUtil.parseDateToString(rs3.getDate("date_visit"), "dd/MM/yyyy");
                        TBDiagnosis_Date = TB_TreatmentStartDate;
                        startedTB[0] = true;
                    }

                    if (!dateVisit.isEmpty() && DateUtil.isBetween(
                            DateUtil.parseStringToDate(dateVisit, "MM/dd/yyyy"),
                            DateUtil.parseStringToDate(reportingDateBegin, "MM/dd/yyyy"),
                            DateUtil.parseStringToDate(reportingDateEnd, "MM/dd/yyyy"))) {
                        TBScreeningCriteria_CurrentCough = "YES";
                        TBScreeningCriteria_PoorWeighGain = "YES";
                        PatientClinicallyScreenForTBDuringReviewPeriod = "YES";
                        TBClinicalScreeningCriteria = "YES";
                        PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture = "YES";
                        PatientDiagnosedOfTBInReviewPeriod = "YES";

                        //if the patient has not started TB but not during the review period
                        if (!startedTB[0] && tbStatus.trim().equalsIgnoreCase("Currently on TB treatment")) {
                            PatientStartTBTreatment = "YES";
                            TB_TreatmentStartDate = rs3.getDate("date_visit") == null ? "01/01/1900" :
                                    DateUtil.parseDateToString(rs3.getDate("date_visit"), "dd/MM/yyyy");
                            TBDiagnosis_Date = TB_TreatmentStartDate;
                            startedTB[0] = true;
                        }

                        if (!startedTB[0] && tbStatus.trim()
                                .equalsIgnoreCase("TB suspected and referred for evaluation")) {
                            BasedOnScreeningWasPatientedSuspectedToHaveTB = "YES";
                        }
                    }
                });

                //Adding records to the xml files
                if (pediatric_art_adherence_record) {
                    Element row1 = root1.addElement("pediatric_art_adherence_record");
                    Element column1 = row1.addElement("PatientID");
                    column1.setText(hospitalNum);
                    column1 = row1.addElement("ARTAdherenceAssessmentPerformedDuringLast3Months");
                    column1.setText(ARTAdherenceAssessmentPerformedDuringLast3Months);
                    column1 = row1.addElement("LastDateOfAssessment");
                    column1.setText(LastDateOfAssessment);
                    //column1 = row1.addElement("FacilityID");
                    //column1.setText(FacilityID);
                    //column1 = row1.addElement("UploaderId");
                    //column1.setText(UploaderId);
                    //column1 = row1.addElement("UploadDt");
                    //column1.setText(UploadDt);
                    //column1 = row1.addElement("webUploadFlag");
                    //column1.setText(webUploadFlag);
                    //column1 = row1.addElement("ReviewPeriodID");
                    //column1.setText(ReviewPeriodID);
                }

                if (pediatric_artregimensincestartingtreatment_record) {
                    Element row2 = root2.addElement("pediatric_artregimensincestartingtreatment_record");
                    Element column2 = row2.addElement("PatientID");
                    column2.setText(hospitalNum);
                    column2 = row2.addElement("PatientOnARTAnytimeDuringReviewPeriod");
                    column2.setText(PatientOnARTAnytimeDuringReviewPeriod);
                    column2 = row2.addElement("C1stRegminen");
                    column2.setText(C1stRegminen);
                    column2 = row2.addElement("C1stRegimenStartDate");
                    column2.setText(C1stRegimenStartDate);
                    column2 = row2.addElement("C1stRegimenChangeDate");
                    column2.setText(C1stRegimenChangeDate);
                    column2 = row2.addElement("C2ndRegimen");
                    column2.setText(C2ndRegimen);
                    column2 = row2.addElement("C2ndRegimenStartDate");
                    column2.setText(C2ndRegimenStartDate);
                    column2 = row2.addElement("C2ndRegimenChangeDate");
                    column2.setText(C2ndRegimenChangeDate);
                    column2 = row2.addElement("C3rdRegimen");
                    column2.setText(C3rdRegimen);
                    column2 = row2.addElement("C3rdRegimenStartDate");
                    column2.setText(C3rdRegimenStartDate);
                    column2 = row2.addElement("C3rdRegimenChangeDate");
                    column2.setText(C3rdRegimenChangeDate);
                    column2 = row2.addElement("OtherRegimenSpecify");
                    column2.setText(OtherRegimenSpecify);
                    //column2 = row2.addElement("FacilityID");
                    //column2.setText(FacilityID);
                    //column2 = row2.addElement("UploaderId");
                    //column2.setText(UploaderId);
                    //column2 = row2.addElement("UploadDt");
                    //column2.setText(UploadDt);
                    //column2 = row2.addElement("webUploadFlag");
                    //column2.setText(webUploadFlag);
                    //column2 = row2.addElement("ReviewPeriodID");
                    //column2.setText(ReviewPeriodID);
                }

                if (pediatric_baselineparameters_record) {
                    Element row3 = root3.addElement("pediatric_baselineparameters_record");
                    Element column3 = row3.addElement("PatientID");
                    column3.setText(hospitalNum);
                    column3 = row3.addElement("CD4_Count");
                    column3.setText(CD4_Count);
                    column3 = row3.addElement("CD4_Count_Date");
                    column3.setText(CD4_Count_Date);
                    column3 = row3.addElement("Weight");
                    column3.setText(Weight);
                    column3 = row3.addElement("Weight_Date");
                    column3.setText(Weight_Date);
                    column3 = row3.addElement("WHO_Clinical_Stage");
                    column3.setText(WHO_Clinical_Stage);
                    column3 = row3.addElement("WHO_Clinical_State_Date");
                    column3.setText(WHO_Clinical_State_Date);
                    column3 = row3.addElement("CD4_Not_Recorded");
                    column3.setText(CD4_Not_Recorded);
                    column3 = row3.addElement("Weight_Not_Recorded");
                    column3.setText(Weight_Not_Recorded);
                    column3 = row3.addElement("WHO_Clinical_Stage_Not_Recorded");
                    column3.setText(WHO_Clinical_Stage_Not_Recorded);
                    //column3 = row3.addElement("FacilityID");
                    //column3.setText(FacilityID);
                    column3 = row3.addElement("PatientEverStartedOnART");
                    column3.setText(PatientEverStartedOnART);
                    column3 = row3.addElement("ART_Start_Date");
                    column3.setText(ART_Start_Date);
                    //column3 = row3.addElement("UploaderId");
                    //column3.setText(UploaderId);
                    //column3 = row3.addElement("UploadDt");
                    //column3.setText(UploadDt);
                    //column3 = row3.addElement("webUploadFlag");
                    //column3.setText(webUploadFlag);
                    //column3 = row3.addElement("ReviewPeriodID");
                    //column3.setText(ReviewPeriodID);
                }

                if (pediatric_clinicalevaluationinreviewperiod_record) {
                    Element row4 = root4.addElement("pediatric_clinicalevaluationinreviewperiod_record");
                    Element column4 = row4.addElement("PatientID");
                    column4.setText(hospitalNum);
                    column4 = row4.addElement("Visit1");
                    column4.setText(Visit1);
                    column4 = row4.addElement("Visit2");
                    column4.setText(Visit2);
                    column4 = row4.addElement("Visit3");
                    column4.setText(Visit3);
                    column4 = row4.addElement("Visit4");
                    column4.setText(Visit4);
                    //column4 = row4.addElement("FacilityID");
                    //column4.setText(FacilityID);
                    //column4 = row4.addElement("UploaderId");
                    //column4.setText(UploaderId);
                    //column4 = row4.addElement("UploadDt");
                    //column4.setText(UploadDt);
                    //column4 = row4.addElement("webUploadFlag");
                    //column4.setText(webUploadFlag);
                    //column4 = row4.addElement("ReviewPeriodID");
                    //column4.setText(ReviewPeriodID);
                }

                if (pediatric_cotrimoxazole_record) {
                    Element row5 = root5.addElement("pediatric_cotrimoxazole_record");
                    Element column5 = row5.addElement("PatientID");
                    column5.setText(hospitalNum);
                    column5 = row5.addElement("PatientCurrentlyOnCotrimoxazoleProphylaxis");
                    column5.setText(PatientCurrentlyOnCotrimoxazoleProphylaxis);
                    column5 = row5.addElement("DateOfFirstPrescription");
                    column5.setText(DateOfFirstPrescription);
                    column5 = row5.addElement("AgeOfFirstPrescription");
                    column5.setText(AgeOfFirstPrescription);
                    column5 = row5.addElement("UnitOfAgeMeasure");
                    column5.setText(UnitOfAgeMeasure);
                    //column5 = row5.addElement("FacilityID");
                    //column5.setText(FacilityID);
                    //column5 = row5.addElement("UploaderId");
                    //column5.setText(UploaderId);
                    //column5 = row5.addElement("UploadDt");
                    //column5.setText(UploadDt);
                    //column5 = row5.addElement("webUploadFlag");
                    //column5.setText(webUploadFlag);
                    //column5 = row5.addElement("ReviewPeriodID");
                    //column5.setText(ReviewPeriodID);
                }

                if (pediatric_patientstatus_record) {
                    Element row6 = root6.addElement("pediatric_patientstatus_record");
                    Element column6 = row6.addElement("PatientID");
                    column6.setText(hospitalNum);
                    column6 = row6.addElement("Status");
                    column6.setText(Status);
                    column6 = row6.addElement("DateOfStatusChange");
                    column6.setText(DateOfStatusChange);
                    column6 = row6.addElement("Reason");
                    column6.setText(Reason);
                    column6 = row6.addElement("ID");
                    column6.setText("");        //To be clarified
                    //column6 = row6.addElement("FacilityID");
                    //column6.setText(FacilityID);
                    //column6 = row6.addElement("UploaderId");
                    //column6.setText(UploaderId);
                    //column6 = row6.addElement("UploadDt");
                    //column6.setText(UploadDt);
                    //column6 = row6.addElement("webUploadFlag");
                    //column6.setText(webUploadFlag);
                    column6 = row6.addElement("Transferred_Out");
                    column6.setText(Transferred_Out);
                    column6 = row6.addElement("Death");
                    column6.setText(Death);
                    column6 = row6.addElement("Discontinued_Care");
                    column6.setText(Discontinued_Care);
                    column6 = row6.addElement("Transferred_Out_Date");
                    column6.setText(Transferred_Out_Date);
                    column6 = row6.addElement("Death_Date");
                    column6.setText(Death_Date);
                    column6 = row6.addElement("Discontinued_Care_Date");
                    column6.setText(Discontinued_Care_Date);
                    column6 = row6.addElement("Discontinued_Care_Reason");
                    column6.setText(Discontinued_Care_Reason);
                    column6 = row6.addElement("Discontinued_Care_Reason_Other");
                    column6.setText(Discontinued_Care_Reason_Other);
                    //column6 = row6.addElement("ReviewPeriodID");
                    //column6.setText(ReviewPeriodID);
                }

                if (pediatric_patientmonitoringduringreviewperiod_record) {
                    Element row7 = root7.addElement("pediatric_patientmonitoringduringreviewperiod_record");
                    Element column7 = row7.addElement("PatientID");
                    column7.setText(hospitalNum);
                    column7 = row7.addElement("PDG001_VALUE");
                    column7.setText(PDG001_VALUE);
                    column7 = row7.addElement("PDG001_DATE");
                    column7.setText(PDG001_DATE);
                    column7 = row7.addElement("PDG002_VALUE");
                    column7.setText(PDG002_VALUE);
                    column7 = row7.addElement("PDG002_DATE");
                    column7.setText(PDG002_DATE);
                    column7 = row7.addElement("PDG003_VALUE");
                    column7.setText(PDG003_VALUE);
                    column7 = row7.addElement("PDG003_DATE");
                    column7.setText(PDG003_DATE);
                    column7 = row7.addElement("PDG004_VALUE");
                    column7.setText(PDG004_VALUE);
                    column7 = row7.addElement("PDG004_DATE");
                    column7.setText(PDG004_DATE);
                    column7 = row7.addElement("PDG005_VALUE");
                    column7.setText(PDG005_VALUE);
                    column7 = row7.addElement("PDG005_DATE");
                    column7.setText(PDG005_DATE);
                    column7 = row7.addElement("PDG006_VALUE");
                    column7.setText(PDG006_VALUE);
                    column7 = row7.addElement("PDG006_DATE");
                    column7.setText(PDG006_DATE);
                    column7 = row7.addElement("PDG007_VALUE");
                    column7.setText(PDG007_VALUE);
                    column7 = row7.addElement("PDG007_DATE");
                    column7.setText(PDG007_DATE);
                    column7 = row7.addElement("PDG008_VALUE");
                    column7.setText(PDG008_VALUE);
                    column7 = row7.addElement("PDG008_DATE");
                    column7.setText(PDG008_DATE);
                    column7 = row7.addElement("PDG009_VALUE");
                    column7.setText(PDG009_VALUE);
                    column7 = row7.addElement("PDG009_DATE");
                    column7.setText(PDG009_DATE);
                    column7 = row7.addElement("PDG010_VALUE");
                    column7.setText(PDG010_VALUE);
                    column7 = row7.addElement("PDG010_DATE");
                    column7.setText(PDG010_DATE);
                    column7 = row7.addElement("PDG011_VALUE");
                    column7.setText(PDG011_VALUE);
                    column7 = row7.addElement("PDG011_DATE");
                    column7.setText(PDG011_DATE);
                    column7 = row7.addElement("PDG012_VALUE");
                    column7.setText(PDG012_VALUE);
                    column7 = row7.addElement("PDG012_DATE");
                    column7.setText(PDG012_DATE);
                    column7 = row7.addElement("PDG013_VALUE");
                    column7.setText(PDG013_VALUE);
                    column7 = row7.addElement("PDG013_DATE");
                    column7.setText(PDG013_DATE);
                    column7 = row7.addElement("PDG014_VALUE");
                    column7.setText(PDG014_VALUE);
                    column7 = row7.addElement("PDG014_DATE");
                    column7.setText(PDG014_DATE);
                    column7 = row7.addElement("PDG015_VALUE");
                    column7.setText(PDG015_VALUE);
                    column7 = row7.addElement("PDG015_DATE");
                    column7.setText(PDG015_DATE);
                    column7 = row7.addElement("PDG016_VALUE");
                    column7.setText(PDG016_VALUE);
                    column7 = row7.addElement("PDG016_DATE");
                    column7.setText(PDG016_DATE);
                    column7 = row7.addElement("PDG017_VALUE");
                    column7.setText(PDG017_VALUE);
                    column7 = row7.addElement("PDG017_DATE");
                    column7.setText(PDG017_DATE);
                    column7 = row7.addElement("PDG018_VALUE");
                    column7.setText(PDG018_VALUE);
                    column7 = row7.addElement("PDG018_DATE");
                    column7.setText(PDG018_DATE);
                    column7 = row7.addElement("PDG019_VALUE");
                    column7.setText(PDG019_VALUE);
                    column7 = row7.addElement("PDG019_DATE");
                    column7.setText(PDG019_DATE);
                    column7 = row7.addElement("PDG020_VALUE");
                    column7.setText(PDG020_VALUE);
                    column7 = row7.addElement("PDG020_DATE");
                    column7.setText(PDG020_DATE);
                    column7 = row7.addElement("PDG021_VALUE");
                    column7.setText(PDG021_VALUE);
                    column7 = row7.addElement("PDG021_DATE");
                    column7.setText(PDG021_DATE);
                    column7 = row7.addElement("PDG022_VALUE");
                    column7.setText(PDG022_VALUE);
                    column7 = row7.addElement("PDG022_DATE");
                    column7.setText(PDG022_DATE);
                    column7 = row7.addElement("PDG023_VALUE");
                    column7.setText(PDG023_VALUE);
                    column7 = row7.addElement("PDG023_DATE");
                    column7.setText(PDG023_DATE);
                    column7 = row7.addElement("PDG024_VALUE");
                    column7.setText(PDG024_VALUE);
                    column7 = row7.addElement("PDG024_DATE");
                    column7.setText(PDG024_DATE);

                    column7 = row7.addElement("HasThePatientReceivedViralLoadTesting");
                    column7.setText(HasThePatientReceivedViralLoadTesting);
                    column7 = row7.addElement("ViralLoadTestResult");
                    column7.setText(ViralLoadTestResult);
                    column7 = row7.addElement("ViralLoadTestDate");
                    column7.setText(ViralLoadTestDate);

                    //column7 = row7.addElement("FacilityID");
                    //column7.setText(FacilityID);
                    //column7 = row7.addElement("UploaderId");
                    //column7.setText(UploaderId);
                    //column7 = row7.addElement("UploadDt");
                    //column7.setText(UploadDt);
                    //column7 = row7.addElement("webUploadFlag");
                    //column7.setText(webUploadFlag);
                    //column7 = row7.addElement("ReviewPeriodID");
                    //column7.setText(ReviewPeriodID);
                }

                if (pediatric_missedappointmentandpatienttracking_record) {
                    Element row8 = root8.addElement("pediatric_missedappointmentandpatienttracking_record");
                    Element column8 = row8.addElement("PatientID");
                    column8.setText(hospitalNum);
                    column8 = row8.addElement("MissedAppointment");
                    column8.setText(MissedAppointment);
                    column8 = row8.addElement("AttemptedContact");
                    column8.setText(AttemptedContact);
                    column8 = row8.addElement("DateOfAttemptedContact");
                    column8.setText(DateOfAttemptedContact);
                    column8 = row8.addElement("OutcomeOfTracking");
                    column8.setText(OutcomeOfTracking);
                    column8 = row8.addElement("ReasonFor_LTFU");
                    column8.setText(ReasonFor_LTFU);
                    column8 = row8.addElement("CauseOfDeath");
                    column8.setText(CauseOfDeath);
                    column8 = row8.addElement("ID");
                    column8.setText("");
                    //column8 = row8.addElement("FacilityID");
                    //column8.setText(FacilityID);
                    //column8 = row8.addElement("UploaderId");
                    //column8.setText(UploaderId);
                    //column8 = row8.addElement("UploadDt");
                    //column8.setText(UploadDt);
                    //column8 = row8.addElement("webUploadFlag");
                    //column8.setText(webUploadFlag);
                    column8 = row8.addElement("Missed_appointment_1");
                    column8.setText(Missed_appointment_1);
                    column8 = row8.addElement("Missed_appointment_1_Date");
                    column8.setText(Missed_appointment_1_Date);
                    column8 = row8.addElement("Missed_appointment_1_AttemptedContact");
                    column8.setText(Missed_appointment_1_AttemptedContact);
                    column8 = row8.addElement("Missed_appointment_1_AttemptedContactDate");
                    column8.setText(Missed_appointment_1_AttemptedContactDate);
                    column8 = row8.addElement("Missed_appointment_1_OutcomeOfTracking");
                    column8.setText(Missed_appointment_1_OutcomeOfTracking);
                    column8 = row8.addElement("Missed_appointment_1_ReasonForLFTU");
                    column8.setText(Missed_appointment_1_ReasonForLFTU);
                    column8 = row8.addElement("Missed_appointment_1_CauseOfDeath");
                    column8.setText(Missed_appointment_1_CauseOfDeath);
                    column8 = row8.addElement("Missed_appointment_2");
                    column8.setText(Missed_appointment_2);
                    column8 = row8.addElement("Missed_appointment_2_Date");
                    column8.setText(Missed_appointment_2_Date);
                    column8 = row8.addElement("Missed_appointment_2_AttemptedContact");
                    column8.setText(Missed_appointment_2_AttemptedContact);
                    column8 = row8.addElement("Missed_appointment_2_AttemptedContactDate");
                    column8.setText(Missed_appointment_2_AttemptedContactDate);
                    column8 = row8.addElement("Missed_appointment_2_OutcomeOfTracking");
                    column8.setText(Missed_appointment_2_OutcomeOfTracking);
                    column8 = row8.addElement("Missed_appointment_2_ReasonForLFTU");
                    column8.setText(Missed_appointment_2_ReasonForLFTU);
                    column8 = row8.addElement("Missed_appointment_2_CauseOfDeath");
                    column8.setText(Missed_appointment_2_CauseOfDeath);
                    column8 = row8.addElement("Missed_appointment_3");
                    column8.setText(Missed_appointment_3);
                    column8 = row8.addElement("Missed_appointment_3_Date");
                    column8.setText(Missed_appointment_3_Date);
                    column8 = row8.addElement("Missed_appointment_3_AttemptedContact");
                    column8.setText(Missed_appointment_3_AttemptedContact);
                    column8 = row8.addElement("Missed_appointment_3_AttemptedContactDate");
                    column8.setText(Missed_appointment_3_AttemptedContactDate);
                    column8 = row8.addElement("Missed_appointment_3_OutcomeOfTracking");
                    column8.setText(Missed_appointment_3_OutcomeOfTracking);
                    column8 = row8.addElement("Missed_appointment_3_ReasonForLFTU");
                    column8.setText(Missed_appointment_3_ReasonForLFTU);
                    column8 = row8.addElement("Missed_appointment_3_CauseOfDeath");
                    column8.setText(Missed_appointment_3_CauseOfDeath);
                    //column8 = row8.addElement("ReviewPeriodID");
                    //column8.setText(ReviewPeriodID);
                }

                if (pediatric_tuberculosis_record) {
                    Element row9 = root9.addElement("pediatric_tuberculosis_record");
                    Element column9 = row9.addElement("PatientID");
                    column9.setText(hospitalNum);
                    column9 = row9.addElement("PatientOnTBTreatmentDuringReviewPeriod");
                    column9.setText(PatientOnTBTreatmentDuringReviewPeriod);
                    column9 = row9.addElement("PatientClinicallyScreenForTBDuringReviewPeriod");
                    column9.setText(PatientClinicallyScreenForTBDuringReviewPeriod);
                    column9 = row9.addElement("TBClinicalScreeningCriteria");
                    column9.setText(TBClinicalScreeningCriteria);
                    column9 = row9.addElement("BasedOnScreeningWasPatientedSuspectedToHaveTB");
                    column9.setText(BasedOnScreeningWasPatientedSuspectedToHaveTB);
                    column9 = row9.addElement("PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture");
                    column9.setText(PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture);
                    column9 = row9.addElement("PatientHdChestXRay");
                    column9.setText(PatientHdChestXRay);
                    column9 = row9.addElement("PatientDiagnosedOfTBInReviewPeriod");
                    column9.setText(PatientDiagnosedOfTBInReviewPeriod);
                    column9 = row9.addElement("PatientStartTBTreatment");
                    column9.setText(PatientStartTBTreatment);
                    column9 = row9.addElement("TB_TreatmentStartDate");
                    column9.setText(TB_TreatmentStartDate);
                    column9 = row9.addElement("TBDiagnosis_Date");
                    column9.setText(TBDiagnosis_Date);
                    //column9 = row9.addElement("FacilityID");
                    //column9.setText(FacilityID);
                    //column9 = row9.addElement("UploaderId");
                    //column9.setText(UploaderId);
                    //column9 = row9.addElement("UploadDt");
                    //column9.setText(UploadDt);
                    //column9 = row9.addElement("webUploadFlag");
                    //column9.setText(webUploadFlag);
                    column9 = row9.addElement("TBScreeningCriteria_CurrentCough");
                    column9.setText(TBScreeningCriteria_CurrentCough);
                    column9 = row9.addElement("TBScreeningCriteria_ContactWithTBCase");
                    column9.setText(TBScreeningCriteria_ContactWithTBCase);
                    column9 = row9.addElement("TBScreeningCriteria_PoorWeighGain");
                    column9.setText(TBScreeningCriteria_PoorWeighGain);
                    //column9 = row9.addElement("ReviewPeriodID");
                    //column9.setText(ReviewPeriodID);
                }
            }
            return null;
        });

        DocumentWriter.writeXmlToFile(document1, "pediatric_art_adherence");
        DocumentWriter.writeXmlToFile(document2, "pediatric_artregimensincestartingtreatment");
        DocumentWriter.writeXmlToFile(document3, "pediatric_baselineparameters");
        DocumentWriter.writeXmlToFile(document4, "pediatric_clinicalevaluationinreviewperiod");
        DocumentWriter.writeXmlToFile(document5, "pediatric_cotrimoxazole");
        DocumentWriter.writeXmlToFile(document6, "pediatric_patientstatus");
        DocumentWriter.writeXmlToFile(document7, "pediatric_patientmonitoringduringreviewperiod");
        DocumentWriter.writeXmlToFile(document8, "pediatric_missedappointmentandpatienttracking");
        DocumentWriter.writeXmlToFile(document9, "pediatric_tuberculosis");
    }

    private void initVariables() {
        patientId = 0L;
        hospitalNum = "";
        ARTAdherenceAssessmentPerformedDuringLast3Months = "";
        LastDateOfAssessment = "01/01/1900";
        PatientOnARTAnytimeDuringReviewPeriod = "";
        C1stRegminen = "0";
        C1stRegimenStartDate = "01/01/1900";
        C1stRegimenChangeDate = "01/01/1900";
        C2ndRegimen = "0";
        C2ndRegimenStartDate = "01/01/1900";
        C2ndRegimenChangeDate = "01/01/1900";
        C3rdRegimen = "0";
        C3rdRegimenStartDate = "01/01/1900";
        C3rdRegimenChangeDate = "01/01/1900";
        OtherRegimenSpecify = "0";
        CD4_Count = "0";
        CD4_Count_Date = "01/01/1900";
        Weight = "0";
        Weight_Date = "01/01/1900";
        WHO_Clinical_Stage = "0";
        WHO_Clinical_State_Date = "01/01/1900";
        CD4_Not_Recorded = "";
        Weight_Not_Recorded = "";
        WHO_Clinical_Stage_Not_Recorded = "";
        PatientEverStartedOnART = "";
        Visit1 = "01/01/1900";
        Visit2 = "01/01/1900";
        Visit3 = "01/01/1900";
        Visit4 = "01/01/1900";
        PatientCurrentlyOnCotrimoxazoleProphylaxis = "";
        DateOfFirstPrescription = "01/01/1900";
        AgeOfFirstPrescription = "0";
        UnitOfAgeMeasure = "YEARS";
        ART_Start_Date = "01/01/1900";

        Status = "";
        DateOfStatusChange = "01/01/1900";
        Reason = "";
        Transferred_Out = "";
        Death = "";
        Discontinued_Care = "";
        Transferred_Out_Date = "01/01/1900";
        Death_Date = "01/01/1900";
        Discontinued_Care_Date = "01/01/1900";
        Discontinued_Care_Reason = "";
        Discontinued_Care_Reason_Other = "";

        PDG001_VALUE = "0";
        PDG001_DATE = "01/01/1900";
        PDG002_VALUE = "0";
        PDG002_DATE = "01/01/1900";
        PDG003_VALUE = "0";
        PDG003_DATE = "01/01/1900";
        PDG004_VALUE = "0";
        PDG004_DATE = "01/01/1900";
        PDG005_VALUE = "0";
        PDG005_DATE = "01/01/1900";
        PDG006_VALUE = "0";
        PDG006_DATE = "01/01/1900";
        PDG007_VALUE = "0";
        PDG007_DATE = "01/01/1900";
        PDG008_VALUE = "0";
        PDG008_DATE = "01/01/1900";
        PDG009_VALUE = "0";
        PDG009_DATE = "01/01/1900";
        PDG010_VALUE = "0";
        PDG010_DATE = "01/01/1900";
        PDG011_VALUE = "0";
        PDG011_DATE = "01/01/1900";
        PDG012_VALUE = "0";
        PDG012_DATE = "01/01/1900";
        PDG013_VALUE = "0";
        PDG013_DATE = "01/01/1900";
        PDG014_VALUE = "0";
        PDG014_DATE = "01/01/1900";
        PDG015_VALUE = "0";
        PDG015_DATE = "01/01/1900";
        PDG016_VALUE = "0";
        PDG016_DATE = "01/01/1900";
        PDG017_VALUE = "0";
        PDG017_DATE = "01/01/1900";
        PDG018_VALUE = "0";
        PDG018_DATE = "01/01/1900";
        PDG019_VALUE = "0";
        PDG019_DATE = "01/01/1900";
        PDG020_VALUE = "0";
        PDG020_DATE = "01/01/1900";
        PDG021_VALUE = "0";
        PDG021_DATE = "01/01/1900";
        PDG022_VALUE = "0";
        PDG022_DATE = "01/01/1900";
        PDG023_VALUE = "0";
        PDG023_DATE = "01/01/1900";
        PDG024_VALUE = "0";
        PDG024_DATE = "01/01/1900";
        HasThePatientReceivedViralLoadTesting = "";
        ViralLoadTestResult = "0";
        ViralLoadTestDate = "0";

        MissedAppointment = "";
        AttemptedContact = "";
        DateOfAttemptedContact = "01/01/1900";
        OutcomeOfTracking = "";
        ReasonFor_LTFU = "";
        CauseOfDeath = "";
        Missed_appointment_1 = "";
        Missed_appointment_1_Date = "01/01/1900";
        Missed_appointment_1_AttemptedContact = "";
        Missed_appointment_1_AttemptedContactDate = "01/01/1900";
        Missed_appointment_1_OutcomeOfTracking = "";
        Missed_appointment_1_ReasonForLFTU = "";
        Missed_appointment_1_CauseOfDeath = "01/01/1900";
        Missed_appointment_2 = "";
        Missed_appointment_2_Date = "01/01/1900";
        Missed_appointment_2_AttemptedContact = "";
        Missed_appointment_2_AttemptedContactDate = "01/01/1900";
        Missed_appointment_2_OutcomeOfTracking = "";
        Missed_appointment_2_ReasonForLFTU = "";
        Missed_appointment_2_CauseOfDeath = "01/01/1900";
        Missed_appointment_3 = "";
        Missed_appointment_3_Date = "01/01/1900";
        Missed_appointment_3_AttemptedContact = "";
        Missed_appointment_3_AttemptedContactDate = "01/01/1900";
        Missed_appointment_3_OutcomeOfTracking = "";
        Missed_appointment_3_ReasonForLFTU = "";
        Missed_appointment_3_CauseOfDeath = "";

        PatientOnTBTreatmentDuringReviewPeriod = "";
        PatientClinicallyScreenForTBDuringReviewPeriod = "";
        TBClinicalScreeningCriteria = "";
        BasedOnScreeningWasPatientedSuspectedToHaveTB = "";
        PatientHdChestXRay = "";
        PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture = "";
        PatientDiagnosedOfTBInReviewPeriod = "";
        TBDiagnosis_Date = "01/01/1900";
        PatientStartTBTreatment = "";
        TB_TreatmentStartDate = "01/01/1900";
        TBScreeningCriteria_CurrentCough = "";
        TBScreeningCriteria_ContactWithTBCase = "";
        TBScreeningCriteria_PoorWeighGain = "";

        pediatric_art_adherence_record = true;
        pediatric_artregimensincestartingtreatment_record = true;
        pediatric_baselineparameters_record = false;
        pediatric_clinicalevaluationinreviewperiod_record = true;
        pediatric_cotrimoxazole_record = true;
        pediatric_patientstatus_record = false;
        pediatric_patientmonitoringduringreviewperiod_record = false;
        pediatric_missedappointmentandpatienttracking_record = false;
        pediatric_tuberculosis_record = true;
    }
}
