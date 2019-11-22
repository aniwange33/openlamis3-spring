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
import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.model.Clinic;
import org.fhi360.lamis.model.Laboratory;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.Pharmacy;
import org.fhi360.lamis.model.dto.DataDTO;
import org.fhi360.lamis.model.repositories.ClinicRepository;
import org.fhi360.lamis.model.repositories.LaboratoryRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.model.repositories.PharmacyRepository;
import org.fhi360.lamis.utility.DateUtil;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class AdultDataConverter {
    private final JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);
    private HttpServletRequest request;
    private HttpSession session;
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String FacilityID;
    private String UploaderId;
    private String UploadDt;
    private String webUploadFlag;
    private String ReviewPeriodID;
    private long patientId;
    private String hospitalNum;
    private String PatientEverStartedOnART;
    private String ART_Start_Date;
    private String TreatmentPrepCompletedBeforeStartOfART;
    private String LastDateOfAssessment;
    private String ARTAdherenceAssessmentPerformedDuringLast3Months;
    private String HighestCD4SinceARTinitiation;
    private String DateOfHighestCD4Test;
    private String PatientOnARTFirstDayOfReviewPeriod;
    private String PatientOnARTAnytimeDuringReviewPeriod;
    private String stRegminen;
    private String stRegimenStartDate;
    private String stRegimenChangeDate;
    private String ndRegimen;
    private String ndRegimenStartDate;
    private String ndRegimenChangeDate;
    private String rdRegminen;
    private String rdRegimenStartDate;
    private String rdRegimenChangeDate;
    private String OtherRegimenSpecify;
    private String DateOfLastDrugPickup;
    private String DurationOfMedicationCoverageInMonths;
    private String CD4_Count;
    private String CD4_Count_Date;
    private String Weight;
    private String Weight_Date;
    private String WHO_Clinical_Stage;
    private String WHO_Clinical_State_Date;
    private String CareAndSupportAssementFormInPatientFolder;
    private String PatientReceiveCareAndSupportAssessmentInReviewPeriod;
    private String NutritionalAssessmentEverDoneForPatientSinceEnrolment;
    private String PatientReceiveNutritionalAssessementInReviewPeriod;
    private String PreventionGoaDocumentedInCareAndSupportForm;
    private String PatientEverReceivedBasicCarePackage;
    private String PatientReceiveBasicCarePackageInReviewPeriod;
    private String Visit1;
    private String Visit2;
    private String Visit3;
    private String Visit4;
    private String PatientReceiveCotrimoxazoleDuringReviewPeriod;
    private String PatientCurrentlyOnCotrimoxazoleProphylaxis;
    private String DateOfLastPrescription;

    private String Status;
    private String DateOfStatusChange;
    private String ReasonForStatusChange;
    private String Transferred_Out;
    private String Death;
    private String Discontinued_Care;
    private String Transferred_Out_Date;
    private String Death_Date;
    private String Discontinued_Care_Date;
    private String Discontinued_Care_Reason;
    private String Discontinued_Care_Reason_Other;

    private String ADH001_Value;
    private String ADH001_TestDate;
    private String ADH002_Value;
    private String ADH002_TestDate;
    private String ADH003_Value;
    private String ADH003_TestDate;
    private String ADH004_Value;
    private String ADH004_TestDate;
    private String ADH005_Value;
    private String ADHOO5_TestDate;
    private String ADH006_Value;
    private String ADH006_TestDate;
    private String ADH007_Value;
    private String ADH007_TestDate;
    private String ADH008_Value;
    private String ADH008_TestDate;
    private String ADH009_Value;
    private String ADH009_TestDate;
    private String ADH010_Value;
    private String ADH010_TestDate;
    private String ADH011_Value;
    private String ADH011_TestDate;
    private String ADH012_Value;
    private String ADH012_TestDate;
    private String ADH013_Value;
    private String ADH013_TestDate;
    private String ADH014_Value;
    private String ADH014_TestDate;
    private String ADH015_Value;
    private String ADH015_TestDate;
    private String ADH016_Value;
    private String ADH016_TestDate;
    private String ADH017_Value;
    private String ADH017_TestDate;
    private String ADH018_Value;
    private String ADH018_TestDate;
    private String ADH019_Value;
    private String ADH019_TestDate;
    private String ADH020_Value;
    private String ADH020_TestDate;
    private String ADH021_Value;
    private String ADH021_TestDate;
    private String ADH022_Value;
    private String ADH022_TestDate;
    private String ADH023_Value;
    private String ADH023_TestDate;
    private String ADH024_Value;
    private String ADH024_TestDate;
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

    private String PatientOnTBTreatmentAtStartOfReviewPeriod;
    private String PatientClinicallyScreenForTBDuringReviewPeriod;
    private String TBClinicalScreeningCriteria;
    private String BasedOnScreeningWasPatientedSuspectedToHaveTB;
    private String PatientHaveCRXPerformedDuringReviewPeriod;
    private String PatientReferredToDOTsClinic;
    private String PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture;
    private String PatientDiagnosedOfTBInReviewPeriod;
    private String TBDiagnosis_Date;
    private String PatientStartTBTreatment;
    private String TB_TreatmentStartDate;
    private String TBScreeningCriteria_CurrentCough;
    private String TBScreeningCriteria_ContactHistoryWithTBCase;
    private String TBScreeningCriteria_PoorWeightGain;

    private String HasPatientReceivedVLTesting;
    private String VLTestDate;
    private String Result_Copies_Per_ml;

    private String HepatitisBAssayEverDoneForPatient;
    private String ResultOfHepatitisBAssay;
    private String ClinicalEvaluationARTFormFilledAtLastVisit;

    private boolean art_record;
    private boolean art_adherence_record;
    private boolean artregimenduringreviewperiod_record;
    private boolean baselineparameters_record;
    private boolean careandsupportassessment_record;
    private boolean clinicalevaluationvisitsinreviewperiod_record;
    private boolean cotrimoxazole_record;
    private boolean patientstatusduringreviewperiod_record;
    private boolean patientmonitoringduringreviewperiod_record;
    private boolean pharmacovigilance_record;
    private boolean missedappointmentsandpatienttracking_record;
    private boolean tuberculosis_record;
    private boolean hepatitisb_record;
    private boolean viralloadtesting_record;
    private final ClinicRepository clinicRepository;
    private final PatientRepository patientRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final PharmacyRepository pharmacyRepository;
    private final ApplicationProperties applicationProperties;

    public AdultDataConverter(ClinicRepository clinicRepository, PatientRepository patientRepository, LaboratoryRepository laboratoryRepository, PharmacyRepository pharmacyRepository, ApplicationProperties applicationProperties) {
        this.clinicRepository = clinicRepository;
        this.patientRepository = patientRepository;
        this.laboratoryRepository = laboratoryRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.applicationProperties = applicationProperties;
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

        try {
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

            Document document10 = DocumentHelper.createDocument();
            Element root10 = document10.addElement("data-set");

            Document document11 = DocumentHelper.createDocument();
            Element root11 = document11.addElement("data-set");

            Document document12 = DocumentHelper.createDocument();
            Element root12 = document12.addElement("data-set");

            Document document13 = DocumentHelper.createDocument();
            Element root13 = document13.addElement("data-set");

            Document document14 = DocumentHelper.createDocument();
            Element root14 = document14.addElement("data-set");

            jdbcUtil = new JDBCUtil();
            ResultSet rs = null;
            query = "SELECT DISTINCT patient.* FROM patient JOIN nigqual ON patient.facility_id = nigqual.facility_id AND patient.patient_id = nigqual.patient_id "
                    + " WHERE patient.facility_id = " + facilityId + " AND nigqual.portal_id = " + Long.parseLong(portalId) + " AND nigqual.review_period_id = " + Long.parseLong(reviewPeriodId) + " AND nigqual.thermatic_area = 'AD'";

            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                initVariables();

                //Table ART
                patientId = resultSet.getLong("patient_id");
                System.out.println("Patient ID...." + patientId);
                hospitalNum = resultSet.getString("hospital_num");
                PatientEverStartedOnART = (resultSet.getDate("date_started") == null) ? "NO" : "YES";
                ART_Start_Date = (resultSet.getDate("date_started") == null) ? "" : DateUtil.parseDateToString(resultSet.getDate("date_started"), "dd/MM/yyyy");
                TreatmentPrepCompletedBeforeStartOfART = (resultSet.getDate("date_started") == null) ? "NOT YET" : "YES";

                if (PatientEverStartedOnART.equals("YES")) {
                    if (DateUtil.formatDate(resultSet.getDate("date_started"), "MM/dd/yyyy").before(DateUtil.parseStringToDate(reportingDateEnd, "MM/dd/yyyy"))) {
                        PatientOnARTAnytimeDuringReviewPeriod = "YES";
                        PatientOnARTFirstDayOfReviewPeriod = "YES";
                    } else {
                        PatientOnARTAnytimeDuringReviewPeriod = "NO";
                        PatientOnARTFirstDayOfReviewPeriod = "NO";
                    }
                } else {
                    PatientOnARTAnytimeDuringReviewPeriod = "NO";
                }

                System.out.println("ART....");
                //Table ART_Adherence
                ARTAdherenceAssessmentPerformedDuringLast3Months = "NO";
                Patient patient = patientRepository.getOne(patientId);
                Clinic rs1 = clinicRepository.getArtCommencement(patient);
                Date dateVisit = DateUtil.convertToDateViaSqlDate(rs1.getDateVisit());
                LastDateOfAssessment = (rs1.getDateVisit() == null) ? "01/01/1900" :
                        DateUtil.parseDateToString(dateVisit, "dd/MM/yyyy");
                //If the last clinic visit is after the end of the reporting minus 3 months, the adherence was performed during the last 3 months
                ARTAdherenceAssessmentPerformedDuringLast3Months =
                        DateUtil.formatDate(dateVisit, "MM/dd/yyyy")
                                .before(DateUtil.addMonth(DateUtil.parseStringToDate(
                                        reportingDateEnd, "MM/dd/yyyy"), -3)) ? "NO" : "YES";

                if (!ART_Start_Date.trim().isEmpty()) {
                    Laboratory laboratory = laboratoryRepository.getHighestCd4AbsoluteAfterArt(patient, rs1.getDateVisit());
                    HighestCD4SinceARTinitiation = laboratory.getResultAB() == null ? "" :
                            laboratory.getResultAB();
                    Date dateReport = DateUtil.convertToDateViaSqlDate(laboratory.getDateReported());
                    DateOfHighestCD4Test = laboratory.getDateReported() == null ? "01/01/1900" :
                            DateUtil.parseDateToString(dateReport, "dd/MM/yyyy");
                }
                Clinic clinic = clinicRepository.getArtCommencement(patient);

                stRegminen = clinic.getRegimenType() == null ? "" :
                        clinic.getRegimenType().contains("ART First Line") ? "1" : "2";
                Date clinicDateVisit = DateUtil.convertToDateViaSqlDate(clinic.getDateVisit());
                stRegimenStartDate = clinic.getDateVisit() == null ? "01/01/1900" :
                        DateUtil.parseDateToString(clinicDateVisit, "dd/MM/yyyy");
                artregimenduringreviewperiod_record = true;

                System.out.println("Commence....");

                Pharmacy pharmacy = pharmacyRepository.getLastRefillByPatient(patient);

                if (pharmacy.getRegimenType().getId() == 1) ndRegimen = "1";
                if (pharmacy.getRegimenType().getId() == 2) ndRegimen = "2";
                Date pharmacyDateVisit = DateUtil.convertToDateViaSqlDate(clinic.getDateVisit());
                ndRegimenStartDate = pharmacy.getDateVisit() == null ? "01/01/1900" :
                        DateUtil.parseDateToString(pharmacyDateVisit, "dd/MM/yyyy");
                DateOfLastDrugPickup = ndRegimenStartDate;
                DurationOfMedicationCoverageInMonths = Integer.toString(pharmacy.getDuration());
                if (!artregimenduringreviewperiod_record) {
                    stRegminen = ndRegimen;
                    stRegimenStartDate = ndRegimenStartDate;
                    artregimenduringreviewperiod_record = true;
                }

                //Table BaselineParameters
                Clinic clinic1 = clinicRepository.getArtCommencement(patient);
                CD4_Count = clinic1.getCd4() == null ? "0" :
                        clinic1.getCd4() == 0.0 ? "0" :
                                Long.toString(Math.round(clinic1.getCd4()));
                Date clinic1DateVisit = DateUtil.convertToDateViaSqlDate(clinic1.getDateVisit());
                CD4_Count_Date = clinic1.getDateVisit() == null ? "" :
                        DateUtil.parseDateToString(clinic1DateVisit, "dd/MM/yyyy");
                if (CD4_Count.equals("0") && !CD4_Count_Date.isEmpty()) {
                    String query1 = "SELECT * FROM laboratory WHERE facility_id = " + facilityId + " AND patient_id = " +
                            patientId + " AND labtest_id = 1 AND date_reported <= '" +
                            clinic1.getDateVisit() + "' ORDER BY date_reported DESC LIMIT 1";
                    jdbcTemplate.query(query1, rs2 -> {
                        CD4_Count = rs2.getObject("resultab") == null ? "0" :
                                rs2.getString("resultab");
                        if (CD4_Count.equals("0")) CD4_Count = rs2.getString("resultpc");
                        CD4_Count_Date = rs2.getDate("date_reported") == null ? "" :
                                DateUtil.parseDateToString(rs2.getDate("date_reported"), "dd/MM/yyyy");
                    });
                } else {
                    CD4_Count = clinic1.getCd4() == null ? "" :
                            clinic1.getCd4() == 0.0 ? "" :
                                    Long.toString(Math.round(clinic1.getCd4()));
                    CD4_Count_Date = clinic1.getDateVisit() == null ? "" :
                            DateUtil.parseDateToString(clinic1DateVisit, "dd/MM/yyyy");
                }
                Weight = Long.toString(Math.round(clinic1.getBodyWeight()));
                Weight_Date = clinic1.getDateVisit() == null ? "01/01/1900" :
                        DateUtil.parseDateToString(clinic1DateVisit, "dd/MM/yyyy");
                String clinicStage = clinic1.getClinicStage() == null ? "" :
                        clinic1.getClinicStage();
                WHO_Clinical_Stage = clinicStage.equalsIgnoreCase("STAGE I") ? "1" :
                        clinicStage.equalsIgnoreCase("STAGE II") ? "2" :
                                clinicStage.equalsIgnoreCase("STAGE III") ? "3" :
                                        clinicStage.equalsIgnoreCase("STAGE IV") ? "4" : "0";
                WHO_Clinical_State_Date = clinic1.getDateVisit() == null ? "01/01/1900" :
                        DateUtil.parseDateToString(clinic1DateVisit, "dd/MM/yyyy");
                baselineparameters_record = true;

                System.out.println("Baseline....");

                //Table CareAndSupportAssessment
                CareAndSupportAssementFormInPatientFolder = "NO";
                query = "SELECT * FROM chroniccare WHERE facility_id = " + facilityId + " AND patient_id = " + patientId;
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    String phdp9ServicesProvided = rs.getString("phdp9_services_provided") == null ? "" : rs.getString("phdp9_services_provided");
                    String supplementaryFood = rs.getString("supplementary_food") == null ? "" : rs.getString("supplementary_food");
                    if (!phdp9ServicesProvided.trim().isEmpty()) PatientEverReceivedBasicCarePackage = "YES";
                    if (!supplementaryFood.trim().isEmpty())
                        NutritionalAssessmentEverDoneForPatientSinceEnrolment = "YES";
                    if (DateUtil.isBetween(DateUtil.formatDate(rs.getDate("date_visit"), "MM/dd/yyyy"), DateUtil.parseStringToDate(reportingDateBegin, "MM/dd/yyyy"), DateUtil.parseStringToDate(reportingDateEnd, "MM/dd/yyyy"))) {
                        PatientReceiveCareAndSupportAssessmentInReviewPeriod = "YES";
                        if (!supplementaryFood.trim().isEmpty())
                            PatientReceiveNutritionalAssessementInReviewPeriod = "YES";
                        if (!phdp9ServicesProvided.trim().isEmpty())
                            PatientReceiveBasicCarePackageInReviewPeriod = "YES";
                    }
                }
                System.out.println("Care & Support....");

                //Table ClinicalEvaluationVisitsInReviewPeriod
                int rec = 1;
                query = "SELECT * FROM clinic WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_visit >= '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "' AND date_visit <= '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' ORDER BY date_visit ASC";  //i.e A, B, C, ....
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    clinicStage = rs.getString("clinic_stage") == null ? "" : rs.getString("clinic_stage");
                    clinicStage = clinicStage.equalsIgnoreCase("STAGE I") ? "1" : clinicStage.equalsIgnoreCase("STAGE II") ? "2" : clinicStage.equalsIgnoreCase("STAGE III") ? "3" : clinicStage.equalsIgnoreCase("STAGE IV") ? "4" : "0";
                    if (rec == 1) {
                        Visit1 = DateUtil.parseDateToString(rs.getDate("date_visit"), "dd/MM/yyyy");
                        ADH013_Value = clinicStage;
                        ADH013_TestDate = Visit1;
                        ADH009_Value = Long.toString(Math.round(rs.getDouble("body_weight")));
                        ADH009_TestDate = Visit1;
                    }
                    if (rec == 2) {
                        Visit2 = DateUtil.parseDateToString(rs.getDate("date_visit"), "dd/MM/yyyy");
                        ADH014_Value = clinicStage;
                        ADH014_TestDate = Visit2;
                        ADH010_Value = Long.toString(Math.round(rs.getDouble("body_weight")));
                        ADH010_TestDate = Visit2;
                    }
                    if (rec == 3) {
                        Visit3 = DateUtil.parseDateToString(rs.getDate("date_visit"), "dd/MM/yyyy");
                        ADH015_Value = clinicStage;
                        ADH015_TestDate = Visit3;
                        ADH011_Value = Long.toString(Math.round(rs.getDouble("body_weight")));
                        ADH011_TestDate = Visit3;
                    }
                    if (rec == 4) {
                        Visit4 = DateUtil.parseDateToString(rs.getDate("date_visit"), "dd/MM/yyyy");
                        ADH016_Value = clinicStage;
                        ADH016_TestDate = Visit4;
                        ADH012_Value = Long.toString(Math.round(rs.getDouble("body_weight")));
                        ADH012_TestDate = Visit4;
                    }
                    rec++;
                    clinicalevaluationvisitsinreviewperiod_record = true;
                }
                System.out.println("Evaluation....");

                //Table COTRIMOXAZOLE
                //query = "SELECT date_visit FROM pharmacy WHERE facility_id = id AND patient_id = id AND regimentype_id = 8 AND date_visit >= '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "' AND date_visit <= '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "' ORDER BY date_visit DESC";  //i.e Z, Y, X, ....
                rec = 1;
                PatientCurrentlyOnCotrimoxazoleProphylaxis = "NO";
                query = "SELECT date_visit FROM pharmacy WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND regimentype_id = 8 ORDER BY date_visit DESC";  //i.e Z, Y, X, ....
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    if (rec == 1)
                        DateOfLastPrescription = DateUtil.parseDateToString(rs.getDate("date_visit"), "dd/MM/yyyy");
                    PatientCurrentlyOnCotrimoxazoleProphylaxis = "YES";

                    if (DateUtil.isBetween(rs.getDate("date_visit"), DateUtil.parseStringToDate(reportingDateBegin, "MM/dd/yyyy"), DateUtil.parseStringToDate(reportingDateEnd, "MM/dd/yyyy"))) {
                        PatientReceiveCotrimoxazoleDuringReviewPeriod = "YES";
                        break;
                    }
                    rec++;
                }
                System.out.println("Cotrim....");

                //Patient Monitoring durig review period
                rec = 1;
                query = "SELECT * FROM laboratory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_reported >= '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "' AND date_reported <= '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' AND labtest_id IN (1, 9, 12, 15) ORDER BY date_reported ASC";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    long labtestId = rs.getLong("labtest_id");
                    String resultab = rs.getString("resultab") == null ? "" : rs.getString("resultab");
                    if (rec == 1) {
                        if (labtestId == 1) { //CD4
                            ADH001_Value = resultab.isEmpty() ? "0" : resultab;
                            ADH001_TestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 9) {  //PCV
                            ADH005_Value = resultab.isEmpty() ? "0" : resultab;
                            ADHOO5_TestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 12) {  //Creatinine
                            ADH017_Value = resultab.isEmpty() ? "0" : resultab;
                            ADH017_TestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 15) {  //ALT
                            ADH021_Value = resultab.isEmpty() ? "0" : resultab;
                            ADH021_TestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                        }
                    }

                    if (rec == 2) {
                        if (labtestId == 1) { //CD4
                            ADH002_Value = resultab.isEmpty() ? "0" : resultab;
                            ADH002_TestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 9) {  //PCV
                            ADH006_Value = resultab.isEmpty() ? "0" : resultab;
                            ADH006_TestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 12) {  //Creatinine
                            ADH018_Value = resultab.isEmpty() ? "0" : resultab;
                            ADH018_TestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 15) {  //ALT
                            ADH022_Value = resultab.isEmpty() ? "0" : resultab;
                            ADH022_TestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                        }
                    }

                    if (rec == 3) {
                        if (labtestId == 1) { //CD4
                            ADH003_Value = resultab.isEmpty() ? "0" : resultab;
                            ADH003_TestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 9) {  //PCV
                            ADH007_Value = resultab.isEmpty() ? "0" : resultab;
                            ADH007_TestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 12) {  //Creatinine
                            ADH019_Value = resultab.isEmpty() ? "0" : resultab;
                            ADH019_TestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 15) {  //ALT
                            ADH023_Value = resultab.isEmpty() ? "0" : resultab;
                            ADH023_TestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                        }
                    }
                    if (rec == 4) {
                        if (labtestId == 1) { //CD4
                            ADH004_Value = resultab.isEmpty() ? "0" : resultab;
                            ADH004_TestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 9) {  //PCV
                            ADH008_Value = resultab.isEmpty() ? "0" : resultab;
                            ADH008_TestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 12) {  //Creatinine
                            ADH020_Value = resultab.isEmpty() ? "0" : resultab;
                            ADH020_TestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                        }
                        if (labtestId == 15) {  //ALT
                            ADH024_Value = resultab.isEmpty() ? "0" : resultab;
                            ADH024_TestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                        }
                    }
                    rec++;
                    patientmonitoringduringreviewperiod_record = true;
                }
                System.out.println("Monitoring....");

                //Status Change
                query = "SELECT * FROM statushistory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_current_status >= '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "' AND date_current_status <= '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' AND current_status IN ('ART Transfer Out', 'Pre-ART Transfer Out', 'Stopped Treatment', 'Known Death') ORDER BY date_current_status DESC";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    if (rs.getString("current_status").trim().contains("Transfer Out")) {
                        Transferred_Out = "YES";
                        Transferred_Out_Date = (rs.getDate("date_current_status") == null) ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_current_status"), "dd/MM/yyyy");
                        Status = "TRANSFERRED OUT";
                        DateOfStatusChange = Transferred_Out_Date;
                        ReasonForStatusChange = "";
                    }
                    if (rs.getString("current_status").trim().contains("Stopped Treatment")) {
                        Discontinued_Care = "YES";
                        Discontinued_Care_Date = (rs.getDate("date_current_status") == null) ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_current_status"), "dd/MM/yyyy");
                        Discontinued_Care_Reason = "NOT INDICATED";
                        Discontinued_Care_Reason_Other = "";
                        Status = "DISCONTINUED CARE";
                        DateOfStatusChange = Discontinued_Care_Date;
                        ReasonForStatusChange = "";
                    }
                    if (rs.getString("current_status").trim().contains("Known Death")) {
                        Death = "YES";
                        Death_Date = (rs.getDate("date_current_status") == null) ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_current_status"), "dd/MM/yyyy");
                        Status = "DEATH";
                        DateOfStatusChange = Death_Date;
                        ReasonForStatusChange = "";
                    }
                    patientstatusduringreviewperiod_record = true;
                }
                System.out.println("Status....");

                //PHARMACOVIGILANCE
                query = "SELECT date_visit FROM clinic WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND adr_screened != '' AND adr_screened IS NOT NULL UNION SELECT date_visit FROM pharmacy WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND adr_screened != '' AND adr_screened IS NOT NULL";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    PatientAssessedForAdverseEffectsDuringReviewPeriod = "YES";
                } else {
                    PatientAssessedForAdverseEffectsDuringReviewPeriod = "NO";
                }
                System.out.println("Pharm....");

                //MissedAppointmentsAndPatientTracking
                //Tuberculosis
                PatientOnTBTreatmentAtStartOfReviewPeriod = "NO";
                PatientClinicallyScreenForTBDuringReviewPeriod = "NO";
                boolean startedTB = false;

                query = "SELECT date_visit, tb_status FROM clinic WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND tb_status IS NOT NULL";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    String dateVisit1 = rs.getDate("date_visit") == null ? "" : DateUtil.parseDateToString(rs.getDate("date_visit"), "dd/MM/yyyy");
                    String tbStatus = rs.getString("tb_status") == null ? "" : rs.getString("tb_status");

                    if (!startedTB && tbStatus.trim().equalsIgnoreCase("Currently on TB treatment")) {
                        PatientStartTBTreatment = "YES";
                        TB_TreatmentStartDate = rs.getDate("date_visit") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_visit"), "dd/MM/yyyy");
                        TBDiagnosis_Date = TB_TreatmentStartDate;
                        startedTB = true;
                    }

                    if (!dateVisit1.isEmpty() && DateUtil.isBetween(DateUtil.parseStringToDate(dateVisit1, "MM/dd/yyyy"), DateUtil.parseStringToDate(reportingDateBegin, "MM/dd/yyyy"), DateUtil.parseStringToDate(reportingDateEnd, "MM/dd/yyyy"))) {
                        PatientClinicallyScreenForTBDuringReviewPeriod = "YES";
                        TBScreeningCriteria_CurrentCough = "YES";
                        TBClinicalScreeningCriteria = "YES";
                        PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture = "YES";
                        PatientDiagnosedOfTBInReviewPeriod = "YES";

                        //if the patient has not started TB but not during the review period
                        if (!startedTB && tbStatus.trim().equalsIgnoreCase("Currently on TB treatment")) {
                            PatientStartTBTreatment = "YES";
                            TB_TreatmentStartDate = rs.getDate("date_visit") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_visit"), "dd/MM/yyyy");
                            TBDiagnosis_Date = TB_TreatmentStartDate;
                            PatientOnTBTreatmentAtStartOfReviewPeriod = "YES";
                            startedTB = true;
                        }

                        if (!startedTB && tbStatus.trim().equalsIgnoreCase("TB suspected and referred for evaluation")) {
                            BasedOnScreeningWasPatientedSuspectedToHaveTB = "YES";
                            PatientReferredToDOTsClinic = "YES";
                        }
                    }
                    if ((startedTB && !TB_TreatmentStartDate.isEmpty() && (DateUtil.parseStringToDate(TB_TreatmentStartDate, "dd/MM/yyyy")).before(DateUtil.parseStringToDate(reportingDateBegin, "MM/dd/yyyy")))) {
                        PatientOnTBTreatmentAtStartOfReviewPeriod = "YES";
                    } else {
                        PatientOnTBTreatmentAtStartOfReviewPeriod = "NO";
                    }
                }
                System.out.println("Tuberculosis....");

                //HepatitisB
                query = "SELECT * FROM laboratory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND labtest_id = 27 ORDER BY date_reported DESC";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String resultab = rs.getString("resultab") == null ? "" : rs.getString("resultab");
                    HepatitisBAssayEverDoneForPatient = "YES";
                    ClinicalEvaluationARTFormFilledAtLastVisit = "YES";
                    ResultOfHepatitisBAssay = resultab.equals("+") ? "Positive" : "Negative";
                } else {
                    HepatitisBAssayEverDoneForPatient = "NO";
                    ClinicalEvaluationARTFormFilledAtLastVisit = "YES";
                    ResultOfHepatitisBAssay = "";
                }
                System.out.println("Hepitiatis....");

                //ViralLoadTesting
                query = "SELECT * FROM laboratory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_reported >= '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "' AND date_reported <= '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' AND resultab REGEXP('(^[0-9]+$)') AND labtest_id = 16 ORDER BY date_reported DESC";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String resultab = rs.getString("resultab") == null ? "" : rs.getString("resultab");
                    HasPatientReceivedVLTesting = "YES";
                    Result_Copies_Per_ml = resultab.isEmpty() ? "0" : resultab;
                    VLTestDate = rs.getDate("date_reported") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_reported"), "dd/MM/yyyy");
                } else {
                    HasPatientReceivedVLTesting = "NO";
                }
                System.out.println("VL....");

                //Adding records to the xml files
                if (art_record) {
                    Element row1 = root1.addElement("art_record");
                    Element column1 = row1.addElement("PatientID");
                    column1.setText(hospitalNum);
                    column1 = row1.addElement("PatientEverStartedOnART");
                    column1.setText(PatientEverStartedOnART);
                    column1 = row1.addElement("ART_Start_Date");
                    column1.setText(ART_Start_Date);
                    column1 = row1.addElement("TreatmentPrepCompletedBeforeStartOfART");
                    column1.setText(TreatmentPrepCompletedBeforeStartOfART);
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

                if (art_adherence_record) {
                    Element row2 = root2.addElement("art_adherence_record");
                    Element column2 = row2.addElement("PatientID");
                    column2.setText(hospitalNum);
                    column2 = row2.addElement("ARTAdherenceAssessmentPerformedDuringLast3Months");
                    column2.setText(ARTAdherenceAssessmentPerformedDuringLast3Months);
                    column2 = row2.addElement("LastDateOfAssessment");
                    column2.setText(LastDateOfAssessment);
                    column2 = row2.addElement("HighestCD4SinceARTinitiation");
                    column2.setText(HighestCD4SinceARTinitiation);
                    column2 = row2.addElement("DateOfHighestCD4Test");
                    column2.setText(DateOfHighestCD4Test);
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

                if (artregimenduringreviewperiod_record) {
                    Element row3 = root3.addElement("artregimenduringreviewperiod_record");
                    Element column3 = row3.addElement("PatientID");
                    column3.setText(hospitalNum);
                    column3 = row3.addElement("PatientOnARTFirstDayOfReviewPeriod");
                    column3.setText(PatientOnARTFirstDayOfReviewPeriod);
                    column3 = row3.addElement("PatientOnARTAnytimeDuringReviewPeriod");
                    column3.setText(PatientOnARTAnytimeDuringReviewPeriod);
                    column3 = row3.addElement("_1stRegminen");
                    column3.setText(stRegminen);
                    column3 = row3.addElement("_1stRegimenStartDate");
                    column3.setText(stRegimenStartDate);
                    column3 = row3.addElement("_1stRegimenChangeDate");
                    column3.setText(stRegimenChangeDate);
                    column3 = row3.addElement("_2ndRegimen");
                    column3.setText(ndRegimen);
                    column3 = row3.addElement("_2ndRegimenStartDate");
                    column3.setText(ndRegimenStartDate);
                    column3 = row3.addElement("_2ndRegimenChangeDate");
                    column3.setText(ndRegimenChangeDate);
                    column3 = row3.addElement("_3rdRegminen");
                    column3.setText(rdRegminen);
                    column3 = row3.addElement("_3rdRegimenStartDate");
                    column3.setText(rdRegimenStartDate);
                    column3 = row3.addElement("_3rdRegimenChangeDate");
                    column3.setText(rdRegimenChangeDate);
                    column3 = row3.addElement("OtherRegimenSpecify");
                    column3.setText(OtherRegimenSpecify);
                    column3 = row3.addElement("DateOfLastDrugPickup");
                    column3.setText(DateOfLastDrugPickup);
                    column3 = row3.addElement("DurationOfMedicationCoverageInMonths");
                    column3.setText(DurationOfMedicationCoverageInMonths);
                }

                if (baselineparameters_record) {
                    Element row4 = root4.addElement("baselineparameters_record");
                    Element column4 = row4.addElement("PatientID");
                    column4.setText(hospitalNum);
                    column4 = row4.addElement("CD4_Count");
                    column4.setText(CD4_Count);
                    column4 = row4.addElement("CD4_Count_Date");
                    column4.setText(CD4_Count_Date);
                    column4 = row4.addElement("Weight");
                    column4.setText(Weight);
                    column4 = row4.addElement("Weight_Date");
                    column4.setText(Weight_Date);
                    column4 = row4.addElement("WHO_Clinical_Stage");
                    column4.setText(WHO_Clinical_Stage);
                    column4 = row4.addElement("WHO_Clinical_State_Date");
                    column4.setText(WHO_Clinical_State_Date);
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

                if (careandsupportassessment_record) {
                    Element row5 = root5.addElement("careandsupportassessment_record");
                    Element column5 = row5.addElement("PatientID");
                    column5.setText(hospitalNum);
                    column5 = row5.addElement("CareAndSupportAssementFormInPatientFolder");
                    column5.setText(CareAndSupportAssementFormInPatientFolder);
                    column5 = row5.addElement("PatientReceiveCareAndSupportAssessmentInReviewPeriod");
                    column5.setText(PatientReceiveCareAndSupportAssessmentInReviewPeriod);
                    column5 = row5.addElement("NutritionalAssessmentEverDoneForPatientSinceEnrolment");
                    column5.setText(NutritionalAssessmentEverDoneForPatientSinceEnrolment);
                    column5 = row5.addElement("PatientReceiveNutritionalAssessementInReviewPeriod");
                    column5.setText(PatientReceiveNutritionalAssessementInReviewPeriod);
                    column5 = row5.addElement("PreventionGoaDocumentedInCareAndSupportForm");
                    column5.setText(PreventionGoaDocumentedInCareAndSupportForm);
                    column5 = row5.addElement("PatientEverReceivedBasicCarePackage");
                    column5.setText(PatientEverReceivedBasicCarePackage);
                    column5 = row5.addElement("PatientReceiveBasicCarePackageInReviewPeriod");
                    column5.setText(PatientReceiveBasicCarePackageInReviewPeriod);

                }

                if (clinicalevaluationvisitsinreviewperiod_record) {
                    Element row6 = root6.addElement("clinicalevaluationvisitsinreviewperiod_record");
                    Element column6 = row6.addElement("PatientID");
                    column6.setText(hospitalNum);
                    column6 = row6.addElement("Visit1");
                    column6.setText(Visit1);
                    column6 = row6.addElement("Visit2");
                    column6.setText(Visit2);
                    column6 = row6.addElement("Visit3");
                    column6.setText(Visit3);
                    column6 = row6.addElement("Visit4");
                    column6.setText(Visit4);
                    //column6 = row6.addElement("FacilityID");
                    //column6.setText(FacilityID);
                    //column6 = row6.addElement("UploaderId");
                    //column6.setText(UploaderId);
                    //column6 = row6.addElement("UploadDt");
                    //column6.setText(UploadDt);
                    //column6 = row6.addElement("webUploadFlag");
                    //column6.setText(webUploadFlag);
                    //column6 = row6.addElement("ReviewPeriodID");
                    //column6.setText(ReviewPeriodID);
                }

                if (cotrimoxazole_record) {
                    Element row7 = root7.addElement("cotrimoxazole_record");
                    Element column7 = row7.addElement("PatientID");
                    column7.setText(hospitalNum);
                    column7 = row7.addElement("PatientReceiveCotrimoxazoleDuringReviewPeriod");
                    column7.setText(PatientReceiveCotrimoxazoleDuringReviewPeriod);
                    column7 = row7.addElement("PatientCurrentlyOnCotrimoxazoleProphylaxis");
                    column7.setText(PatientCurrentlyOnCotrimoxazoleProphylaxis);
                    column7 = row7.addElement("DateOfLastPrescription");
                    column7.setText(DateOfLastPrescription);

                }

                if (patientstatusduringreviewperiod_record) {
                    Element row8 = root8.addElement("patientstatusduringreviewperiod_record");
                    Element column8 = row8.addElement("PatientID");
                    column8.setText(hospitalNum);
                    column8 = row8.addElement("Status");
                    column8.setText(Status);
                    column8 = row8.addElement("DateOfStatusChange");
                    column8.setText(DateOfStatusChange);
                    column8 = row8.addElement("ReasonForStatusChange");
                    column8.setText(ReasonForStatusChange);
                    column8 = row8.addElement("Transferred_Out");
                    column8.setText(Transferred_Out);
                    column8 = row8.addElement("Death");
                    column8.setText(Death);
                    column8 = row8.addElement("Discontinued_Care");
                    column8.setText(Discontinued_Care);
                    column8 = row8.addElement("Transferred_Out_Date");
                    column8.setText(Transferred_Out_Date);
                    column8 = row8.addElement("Death_Date");
                    column8.setText(Death_Date);
                    column8 = row8.addElement("Discontinued_Care_Date");
                    column8.setText(Discontinued_Care_Date);
                    column8 = row8.addElement("Discontinued_Care_Reason");
                    column8.setText(Discontinued_Care_Reason);
                    column8 = row8.addElement("Discontinued_Care_Reason_Other");
                    column8.setText(Discontinued_Care_Reason_Other);
                }

                if (patientmonitoringduringreviewperiod_record) {
                    Element row9 = root9.addElement("patientmonitoringduringreviewperiod_record");
                    Element column9 = row9.addElement("PatientID");
                    column9.setText(hospitalNum);
                    column9 = row9.addElement("ADH001_Value");
                    column9.setText(ADH001_Value);
                    column9 = row9.addElement("ADH001_TestDate");
                    column9.setText(ADH001_TestDate);
                    column9 = row9.addElement("ADH002_Value");
                    column9.setText(ADH002_Value);
                    column9 = row9.addElement("ADH002_TestDate");
                    column9.setText(ADH002_TestDate);
                    column9 = row9.addElement("ADH003_Value");
                    column9.setText(ADH003_Value);
                    column9 = row9.addElement("ADH003_TestDate");
                    column9.setText(ADH003_TestDate);
                    column9 = row9.addElement("ADH004_Value");
                    column9.setText(ADH004_Value);
                    column9 = row9.addElement("ADH004_TestDate");
                    column9.setText(ADH004_TestDate);
                    column9 = row9.addElement("ADH005_Value");
                    column9.setText(ADH005_Value);
                    column9 = row9.addElement("ADHOO5_TestDate");
                    column9.setText(ADHOO5_TestDate);
                    column9 = row9.addElement("ADH006_Value");
                    column9.setText(ADH006_Value);
                    column9 = row9.addElement("ADH006_TestDate");
                    column9.setText(ADH006_TestDate);
                    column9 = row9.addElement("ADH007_Value");
                    column9.setText(ADH007_Value);
                    column9 = row9.addElement("ADH007_TestDate");
                    column9.setText(ADH007_TestDate);
                    column9 = row9.addElement("ADH008_Value");
                    column9.setText(ADH008_Value);
                    column9 = row9.addElement("ADH008_TestDate");
                    column9.setText(ADH008_TestDate);
                    column9 = row9.addElement("ADH009_Value");
                    column9.setText(ADH009_Value);
                    column9 = row9.addElement("ADH009_TestDate");
                    column9.setText(ADH009_TestDate);
                    column9 = row9.addElement("ADH010_Value");
                    column9.setText(ADH010_Value);
                    column9 = row9.addElement("ADH010_TestDate");
                    column9.setText(ADH010_TestDate);
                    column9 = row9.addElement("ADH011_Value");
                    column9.setText(ADH011_Value);
                    column9 = row9.addElement("ADH011_TestDate");
                    column9.setText(ADH011_TestDate);
                    column9 = row9.addElement("ADH012_Value");
                    column9.setText(ADH012_Value);
                    column9 = row9.addElement("ADH012_TestDate");
                    column9.setText(ADH012_TestDate);
                    column9 = row9.addElement("ADH013_Value");
                    column9.setText(ADH013_Value);
                    column9 = row9.addElement("ADH013_TestDate");
                    column9.setText(ADH013_TestDate);
                    column9 = row9.addElement("ADH014_Value");
                    column9.setText(ADH014_Value);
                    column9 = row9.addElement("ADH014_TestDate");
                    column9.setText(ADH014_TestDate);
                    column9 = row9.addElement("ADH015_Value");
                    column9.setText(ADH015_Value);
                    column9 = row9.addElement("ADH015_TestDate");
                    column9.setText(ADH015_TestDate);
                    column9 = row9.addElement("ADH016_Value");
                    column9.setText(ADH016_Value);
                    column9 = row9.addElement("ADH016_TestDate");
                    column9.setText(ADH016_TestDate);
                    column9 = row9.addElement("ADH017_Value");
                    column9.setText(ADH017_Value);
                    column9 = row9.addElement("ADH017_TestDate");
                    column9.setText(ADH017_TestDate);
                    column9 = row9.addElement("ADH018_Value");
                    column9.setText(ADH018_Value);
                    column9 = row9.addElement("ADH018_TestDate");
                    column9.setText(ADH018_TestDate);
                    column9 = row9.addElement("ADH019_Value");
                    column9.setText(ADH019_Value);
                    column9 = row9.addElement("ADH019_TestDate");
                    column9.setText(ADH019_TestDate);
                    column9 = row9.addElement("ADH020_Value");
                    column9.setText(ADH020_Value);
                    column9 = row9.addElement("ADH020_TestDate");
                    column9.setText(ADH020_TestDate);
                    column9 = row9.addElement("ADH021_Value");
                    column9.setText(ADH021_Value);
                    column9 = row9.addElement("ADH021_TestDate");
                    column9.setText(ADH021_TestDate);
                    column9 = row9.addElement("ADH022_Value");
                    column9.setText(ADH022_Value);
                    column9 = row9.addElement("ADH022_TestDate");
                    column9.setText(ADH022_TestDate);
                    column9 = row9.addElement("ADH023_Value");
                    column9.setText(ADH023_Value);
                    column9 = row9.addElement("ADH023_TestDate");
                    column9.setText(ADH023_TestDate);
                    column9 = row9.addElement("ADH024_Value");
                    column9.setText(ADH024_Value);
                    column9 = row9.addElement("ADH024_TestDate");
                    column9.setText(ADH024_TestDate);
                    //column9 = row9.addElement("FacilityID");
                    //column9.setText(FacilityID);
                    //column9 = row9.addElement("UploaderId");
                    //column9.setText(UploaderId);
                    //column9 = row9.addElement("UploadDt");
                    //column9.setText(UploadDt);
                    //column9 = row9.addElement("webUploadFlag");
                    //column9.setText(webUploadFlag);
                    //column9 = row9.addElement("ReviewPeriodID");
                    //column9.setText(ReviewPeriodID);
                }

                if (pharmacovigilance_record) {
                    Element row10 = root10.addElement("pharmacovigilance_record");
                    Element column10 = row10.addElement("PatientID");
                    column10.setText(hospitalNum);
                    column10 = row10.addElement("PatientAssessedForAdverseEffectsDuringReviewPeriod");
                    column10.setText(PatientAssessedForAdverseEffectsDuringReviewPeriod);

                }

                if (missedappointmentsandpatienttracking_record) {
                    Element row11 = root11.addElement("missedappointmentsandpatienttracking_record");
                    Element column11 = row11.addElement("PatientID");
                    column11.setText(hospitalNum);
                    column11 = row11.addElement("MissedAppointment");
                    column11.setText(MissedAppointment);
                    column11 = row11.addElement("AttemptedContact");
                    column11.setText(AttemptedContact);
                    column11 = row11.addElement("DateOfAttemptedContact");
                    column11.setText(DateOfAttemptedContact);
                    column11 = row11.addElement("OutcomeOfTracking");
                    column11.setText(OutcomeOfTracking);
                    column11 = row11.addElement("ReasonFor_LTFU");
                    column11.setText(ReasonFor_LTFU);
                    column11 = row11.addElement("CauseOfDeath");
                    column11.setText(CauseOfDeath);
                    column11 = row11.addElement("ID");
                    column11.setText("");

                    column11 = row11.addElement("Missed_appointment_1");
                    column11.setText(Missed_appointment_1);
                    column11 = row11.addElement("Missed_appointment_1_Date");
                    column11.setText(Missed_appointment_1_Date);
                    column11 = row11.addElement("Missed_appointment_1_AttemptedContact");
                    column11.setText(Missed_appointment_1_AttemptedContact);
                    column11 = row11.addElement("Missed_appointment_1_AttemptedContactDate");
                    column11.setText(Missed_appointment_1_AttemptedContactDate);
                    column11 = row11.addElement("Missed_appointment_1_OutcomeOfTracking");
                    column11.setText(Missed_appointment_1_OutcomeOfTracking);
                    column11 = row11.addElement("Missed_appointment_1_ReasonForLFTU");
                    column11.setText(Missed_appointment_1_ReasonForLFTU);
                    column11 = row11.addElement("Missed_appointment_1_CauseOfDeath");
                    column11.setText(Missed_appointment_1_CauseOfDeath);
                    column11 = row11.addElement("Missed_appointment_2");
                    column11.setText(Missed_appointment_2);
                    column11 = row11.addElement("Missed_appointment_2_Date");
                    column11.setText(Missed_appointment_2_Date);
                    column11 = row11.addElement("Missed_appointment_2_AttemptedContact");
                    column11.setText(Missed_appointment_2_AttemptedContact);
                    column11 = row11.addElement("Missed_appointment_2_AttemptedContactDate");
                    column11.setText(Missed_appointment_2_AttemptedContactDate);
                    column11 = row11.addElement("Missed_appointment_2_OutcomeOfTracking");
                    column11.setText(Missed_appointment_2_OutcomeOfTracking);
                    column11 = row11.addElement("Missed_appointment_2_ReasonForLFTU");
                    column11.setText(Missed_appointment_2_ReasonForLFTU);
                    column11 = row11.addElement("Missed_appointment_2_CauseOfDeath");
                    column11.setText(Missed_appointment_2_CauseOfDeath);
                    column11 = row11.addElement("Missed_appointment_3");
                    column11.setText(Missed_appointment_3);
                    column11 = row11.addElement("Missed_appointment_3_Date");
                    column11.setText(Missed_appointment_3_Date);
                    column11 = row11.addElement("Missed_appointment_3_AttemptedContact");
                    column11.setText(Missed_appointment_3_AttemptedContact);
                    column11 = row11.addElement("Missed_appointment_3_AttemptedContactDate");
                    column11.setText(Missed_appointment_3_AttemptedContactDate);
                    column11 = row11.addElement("Missed_appointment_3_OutcomeOfTracking");
                    column11.setText(Missed_appointment_3_OutcomeOfTracking);
                    column11 = row11.addElement("Missed_appointment_3_ReasonForLFTU");
                    column11.setText(Missed_appointment_3_ReasonForLFTU);
                    column11 = row11.addElement("Missed_appointment_3_CauseOfDeath");
                    column11.setText(Missed_appointment_3_CauseOfDeath);
                    //column11 = row11.addElement("ReviewPeriodID");
                    //column11.setText(ReviewPeriodID);
                }

                if (tuberculosis_record) {
                    Element row12 = root12.addElement("tuberculosis_record");
                    Element column12 = row12.addElement("PatientID");
                    column12.setText(hospitalNum);
                    column12 = row12.addElement("PatientOnTBTreatmentAtStartOfReviewPeriod");
                    column12.setText(PatientOnTBTreatmentAtStartOfReviewPeriod);
                    column12 = row12.addElement("PatientClinicallyScreenForTBDuringReviewPeriod");
                    column12.setText(PatientClinicallyScreenForTBDuringReviewPeriod);
                    column12 = row12.addElement("TBClinicalScreeningCriteria");
                    column12.setText(TBClinicalScreeningCriteria);
                    column12 = row12.addElement("BasedOnScreeningWasPatientedSuspectedToHaveTB");
                    column12.setText(BasedOnScreeningWasPatientedSuspectedToHaveTB);
                    column12 = row12.addElement("PatientHaveCRXPerformedDuringReviewPeriod");
                    column12.setText(PatientHaveCRXPerformedDuringReviewPeriod);
                    column12 = row12.addElement("PatientReferredToDOTsClinic");
                    column12.setText(PatientReferredToDOTsClinic);
                    column12 = row12.addElement("PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture");
                    column12.setText(PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture);
                    column12 = row12.addElement("PatientDiagnosedOfTBInReviewPeriod");
                    column12.setText(PatientDiagnosedOfTBInReviewPeriod);
                    column12 = row12.addElement("TBDiagnosis_Date");
                    column12.setText(TBDiagnosis_Date);
                    column12 = row12.addElement("PatientStartTBTreatment");
                    column12.setText(PatientStartTBTreatment);
                    column12 = row12.addElement("TB_TreatmentStartDate");
                    column12.setText(TB_TreatmentStartDate);

                    column12 = row12.addElement("TBScreeningCriteria_CurrentCough");
                    column12.setText(TBScreeningCriteria_CurrentCough);
                    column12 = row12.addElement("TBScreeningCriteria_ContactHistoryWithTBCase");
                    column12.setText(TBScreeningCriteria_ContactHistoryWithTBCase);
                    column12 = row12.addElement("TBScreeningCriteria_PoorWeightGain");
                    column12.setText(TBScreeningCriteria_PoorWeightGain);

                }

                if (hepatitisb_record) {
                    Element row13 = root13.addElement("hepatitisb_record");
                    Element column13 = row13.addElement("PatientID");
                    column13.setText(hospitalNum);
                    column13 = row13.addElement("HepatitisBAssayEverDoneForPatient");
                    column13.setText(HepatitisBAssayEverDoneForPatient);
                    column13 = row13.addElement("ResultOfHepatitisBAssay");
                    column13.setText(ResultOfHepatitisBAssay);
                    column13 = row13.addElement("ClinicalEvaluationARTFormFilledAtLastVisit");
                    column13.setText(ClinicalEvaluationARTFormFilledAtLastVisit);

                }

                if (viralloadtesting_record) {
                    Element row14 = root14.addElement("viralloadtesting_record");
                    Element column14 = row14.addElement("PatientID");
                    column14.setText(hospitalNum);
                    column14 = row14.addElement("HasPatientReceivedVLTesting");
                    column14.setText(HasPatientReceivedVLTesting);
                    column14 = row14.addElement("VLTestDate");
                    column14.setText(VLTestDate);
                    column14 = row14.addElement("Result_Copies_Per_ml");
                    column14.setText(Result_Copies_Per_ml);

                }
            }

            DocumentWriter.writeXmlToFile(document1, "art");
            DocumentWriter.writeXmlToFile(document2, "art_adherence");
            DocumentWriter.writeXmlToFile(document3, "artregimenduringreviewperiod");
            DocumentWriter.writeXmlToFile(document4, "baselineparameters");
            DocumentWriter.writeXmlToFile(document5, "careandsupportassessment");
            DocumentWriter.writeXmlToFile(document6, "clinicalevaluationvisitsinreviewperiod");
            DocumentWriter.writeXmlToFile(document7, "cotrimoxazole");
            DocumentWriter.writeXmlToFile(document8, "patientstatusduringreviewperiod");
            DocumentWriter.writeXmlToFile(document9, "patientmonitoringduringreviewperiod");
            DocumentWriter.writeXmlToFile(document10, "pharmacovigilance");
            DocumentWriter.writeXmlToFile(document11, "missedappointmentsandpatienttracking");
            DocumentWriter.writeXmlToFile(document12, "tuberculosis");
            DocumentWriter.writeXmlToFile(document13, "hepatitisb");
            DocumentWriter.writeXmlToFile(document14, "viralloadtesting");

            String folderPath = applicationProperties.getContextPath() + "transfer/nigqual";
            String folderToZip = applicationProperties.getContextPath() + "transfer/nigqual.zip";
            zipFolder(folderPath, folderToZip);
            resultSet = null;
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();
        }
    }


    public static void zipFolder(String folderPath, String folderToZip) {
        try {
            File inFolder = new File(folderPath);
            File outFolder = new File(folderToZip);
            ZipOutputStream out = new ZipOutputStream(new
                    BufferedOutputStream(new FileOutputStream(outFolder)));
            BufferedInputStream in;
            byte[] data = new byte[1000];
            String files[] = inFolder.list();
            for (int i = 0; i < files.length; i++) {
                in = new BufferedInputStream(new FileInputStream
                        (inFolder.getPath() + "/" + files[i]), 1000);
                out.putNextEntry(new ZipEntry(files[i]));
                int count;
                while ((count = in.read(data, 0, 1000)) != -1) {
                    out.write(data, 0, count);
                }
                out.closeEntry();
            }
            out.flush();
            out.close();
            inFolder.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initVariables() {
        patientId = 0L;
        hospitalNum = "";
        PatientEverStartedOnART = "";
        ART_Start_Date = "01/01/1900";
        TreatmentPrepCompletedBeforeStartOfART = "";
        LastDateOfAssessment = "01/01/1900";
        ARTAdherenceAssessmentPerformedDuringLast3Months = "";
        HighestCD4SinceARTinitiation = "0";
        DateOfHighestCD4Test = "01/01/1900";
        PatientOnARTFirstDayOfReviewPeriod = "";
        PatientOnARTAnytimeDuringReviewPeriod = "";
        stRegminen = "0";
        stRegimenStartDate = "01/01/1900";
        stRegimenChangeDate = "01/01/1900";
        ndRegimen = "0";
        ndRegimenStartDate = "01/01/1900";
        ndRegimenChangeDate = "01/01/1900";
        rdRegminen = "0";
        rdRegimenStartDate = "01/01/1900";
        rdRegimenChangeDate = "01/01/1900";
        OtherRegimenSpecify = "";
        DateOfLastDrugPickup = "01/01/1900";
        DurationOfMedicationCoverageInMonths = "0";
        CD4_Count = "0";
        CD4_Count_Date = "01/01/1900";
        Weight = "0";
        Weight_Date = "01/01/1900";
        WHO_Clinical_Stage = "0";
        WHO_Clinical_State_Date = "01/01/1900";
        CareAndSupportAssementFormInPatientFolder = "";
        PatientReceiveCareAndSupportAssessmentInReviewPeriod = "";
        NutritionalAssessmentEverDoneForPatientSinceEnrolment = "";
        PatientReceiveNutritionalAssessementInReviewPeriod = "";
        PreventionGoaDocumentedInCareAndSupportForm = "";
        PatientEverReceivedBasicCarePackage = "";
        PatientReceiveBasicCarePackageInReviewPeriod = "";
        Visit1 = "01/01/1900";
        Visit2 = "01/01/1900";
        Visit3 = "01/01/1900";
        Visit4 = "01/01/1900";
        PatientReceiveCotrimoxazoleDuringReviewPeriod = "";
        PatientCurrentlyOnCotrimoxazoleProphylaxis = "";
        DateOfLastPrescription = "01/01/1900";

        Status = "";
        DateOfStatusChange = "01/01/1900";
        ReasonForStatusChange = "";
        Transferred_Out = "";
        Death = "";
        Discontinued_Care = "";
        Transferred_Out_Date = "01/01/1900";
        Death_Date = "01/01/1900";
        Discontinued_Care_Date = "01/01/1900";
        Discontinued_Care_Reason = "";
        Discontinued_Care_Reason_Other = "";

        ADH001_Value = "0";
        ADH001_TestDate = "01/01/1900";
        ADH002_Value = "0";
        ADH002_TestDate = "01/01/1900";
        ADH003_Value = "0";
        ADH003_TestDate = "01/01/1900";
        ADH004_Value = "0";
        ADH004_TestDate = "01/01/1900";
        ADH005_Value = "0";
        ADHOO5_TestDate = "01/01/1900";
        ADH006_Value = "0";
        ADH006_TestDate = "01/01/1900";
        ADH007_Value = "0";
        ADH007_TestDate = "01/01/1900";
        ADH008_Value = "0";
        ADH008_TestDate = "01/01/1900";
        ADH009_Value = "0";
        ADH009_TestDate = "01/01/1900";
        ADH010_Value = "0";
        ADH010_TestDate = "01/01/1900";
        ADH011_Value = "0";
        ADH011_TestDate = "01/01/1900";
        ADH012_Value = "0";
        ADH012_TestDate = "01/01/1900";
        ADH013_Value = "0";
        ADH013_TestDate = "01/01/1900";
        ADH014_Value = "0";
        ADH014_TestDate = "01/01/1900";
        ADH015_Value = "0";
        ADH015_TestDate = "01/01/1900";
        ADH016_Value = "0";
        ADH016_TestDate = "01/01/1900";
        ADH017_Value = "0";
        ADH017_TestDate = "01/01/1900";
        ADH018_Value = "0";
        ADH018_TestDate = "01/01/1900";
        ADH019_Value = "0";
        ADH019_TestDate = "01/01/1900";
        ADH020_Value = "0";
        ADH020_TestDate = "01/01/1900";
        ADH021_Value = "0";
        ADH021_TestDate = "01/01/1900";
        ADH022_Value = "0";
        ADH022_TestDate = "01/01/1900";
        ADH023_Value = "0";
        ADH023_TestDate = "01/01/1900";
        ADH024_Value = "0";
        ADH024_TestDate = "01/01/1900";
        PatientAssessedForAdverseEffectsDuringReviewPeriod = "";

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

        PatientOnTBTreatmentAtStartOfReviewPeriod = "";
        PatientClinicallyScreenForTBDuringReviewPeriod = "";
        TBClinicalScreeningCriteria = "";
        BasedOnScreeningWasPatientedSuspectedToHaveTB = "";
        PatientHaveCRXPerformedDuringReviewPeriod = "";
        PatientReferredToDOTsClinic = "";
        PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture = "";
        PatientDiagnosedOfTBInReviewPeriod = "";
        TBDiagnosis_Date = "01/01/1900";
        PatientStartTBTreatment = "";
        TB_TreatmentStartDate = "01/01/1900";
        TBScreeningCriteria_CurrentCough = "";
        TBScreeningCriteria_ContactHistoryWithTBCase = "";
        TBScreeningCriteria_PoorWeightGain = "";

        HasPatientReceivedVLTesting = "";
        VLTestDate = "01/01/1900";
        Result_Copies_Per_ml = "";

        HepatitisBAssayEverDoneForPatient = "";
        ResultOfHepatitisBAssay = "";
        ClinicalEvaluationARTFormFilledAtLastVisit = "";

        art_record = true;
        art_adherence_record = true;
        artregimenduringreviewperiod_record = false;
        baselineparameters_record = false;
        careandsupportassessment_record = true;
        clinicalevaluationvisitsinreviewperiod_record = false;
        cotrimoxazole_record = true;
        patientstatusduringreviewperiod_record = false;
        patientmonitoringduringreviewperiod_record = false;
        pharmacovigilance_record = true;
        missedappointmentsandpatienttracking_record = false;
        tuberculosis_record = true;
        hepatitisb_record = true;
        viralloadtesting_record = true;
    }

}
