/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.nigqual;

/**
 * @author user1
 */

import javax.servlet.http.HttpSession;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.fhi360.lamis.model.dto.DataDTO;
import org.fhi360.lamis.utility.JDBCUtil;

import javax.servlet.http.HttpServletRequest;

import org.fhi360.lamis.utility.DateUtil;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

@Component
public class PmtctDataConverter {
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
    private String WomanCounselledForHCT;
    private String WomanAcceptedHCT;
    private String HCT_Done;
    private String Date_HCT_Done;
    private String Time_HCT_Done;
    private String HIV_Status;
    private String Date_HIV_Diagnosis;
    private String InfantFeedingCounsellingReceived;
    private String WomanAgreedToPartnerNotification;
    private String CD4_Ordered;
    private String CD4_Order_Date;
    private String CD4_Date_Done;
    private String CD4_Date_Result_Received;
    private String CD4_Count;
    private String WHO_Clinical_Stage;
    private String PatientFoundEligible_for_ARTByClinician;
    private String ARV_Received;
    private String Date_ARV_Regimen_Started;
    private String PrePregnancyHAART;
    private String Referred_for_ART;
    private String AntePartumARV;
    private String AntePartumARV_OtherValue;
    private String ZDV_OnlyOptionA_Antepartum_ARV;
    private String HAART_for_Prophylaxis_Antepartum_ARV;
    private String HAART_for_Treatment_Antepartum_ARV;
    private String None_Antepartum_ARV;
    private String Unknown_Antepartum_ARV;
    private String Other_Antepartum_ARV;

    private String DBS_Sample_Collected;
    private String PCR_EID_Done1st;
    private String PCR_EID_Result1st;
    private String PCR_EID_Date_Sample_Collected1st;
    private String PCR_EID_Date_CareGiver_Received_Results1st;
    private String PCR_EID_Done2nd;
    private String PCR_EID_Result2nd;
    private String PCR_EID_Date_Sample_Collected2nd;
    private String PCR_EID_Date_CareGiver_Received_Results2nd;
    private String RapidTest_L12Months_Done;
    private String RapidTest_L12Months_Result;
    private String RapidTest_L12Months_Date_Sample_Collected;
    private String RapidTest_L12Months_Date_CareGiver_Received_Results;
    private String RapidTest_18Months_Done;
    private String RapidTest_18Months_Result;
    private String RapidTest_18Months_Date_Sample_Collected;
    private String RapidTest_18Months_Date_CareGiver_Received_Results;
    private String ReferredToARVClinic;
    private String ARV_Prophylaxis_Given;
    private String Infant_Received_NVP_Within72hours;
    private String Infant_Received_CPT;
    private String AgeOfCotrimInitiation;
    private String UnitOfAgeMeasure;
    private String Date_DBS_Collected;
    //private String Did_the_Infant-Receive_Following;
    private String Daily_NVP_Until1WeekAfterCeasationOfBreast;
    private String Daily_NVP_for_6_Weeks;
    private String sdNVP_and_daily_ZDV_for_6_Weeks;
    private String Other_infant_received;
    private String NotRecorded_infant_received;
    private String None_infant_received;

    private String Date_of_Delivery;
    private String Maternal_Intrapartum_ARVRegimen_Received;
    private String Mode_of_Delivery;
    private String Gestation_at_DeliveryInWeeks;
    private String Epistomy;
    private String Infant_Feeding_Choice;
    private String Maternal_Outcome;
    private String Child_Status;
    private String Was_Maternal_Intrapartum_ARVRegiment_Received;
    private String MaternalIntrapartumARV_Other;
    private String OptA_Maternal_Intrapartum_ARVRegimen;
    private String OptB_Maternal_Intrapartum_ARVRegimen;
    private String HAART_for_Treatment_Maternal_Intrapartum_ARVRegimen;
    private String None_Maternal_Intrapartum_ARVRegimen;
    private String Unknown_Maternal_Intrapartum_ARVRegimen;
    private String Other_Maternal_Intrapartum_ARVRegimen;

    private String Mother_Accessed_FamilyPlanning;
    private String Family_Planning_Method;
    private String InfantFeedingMethodUsed;
    private String Mother_Received_CPT;
    private String Maternal_Referral;
    private String Infant_Referral;

    private String Partner_of_HIVPositiveWoman_TestedAndReceivedResult;
    private String Partner_HIV_Status;

    private String BookingStatus;
    private String InfectionStatus;

    private String MotherHIVStatus;
    private String WhenWasMotherDiagnosedWithHIV;
    private String WasMotherOnART;
    private String PregnancyDurationWhenDiagnosedInWeeks;
    private String ExclusiveBreastFeeding6Months;
    private String BreastMilkSupplementedBefore6Months;
    private String MixedWithBFBefore6Months;
    private String MixedWithbFAfter6Months;
    private String RegularDietForAge;
    private String NutritionalSupplements;
    private String Other_treatment_received_by_infant;
    private String NoOfWeeksInPregnancyWhenMotherInfected;
    private String PMTCT_PerinatalID;
    private String Did_infant_receive_following;
    private String Daily_NVP_1_week_until_breastfeeding;
    private String Daily_NVP_for_6_weeks;
    private String sdNVP_daily_ZDV_for_6_weeks;
    private String NotRecord_infant_received;

    private String StagePeriod;
    private String GestationAtInitiationInWeeks;
    private String Regimen;
    private String PatientID;

    private String reportingDateBegin;
    private String reportingDateEnd;

    public PmtctDataConverter() {

    }

    public synchronized void convertXml(DataDTO dataDTO, Long facilityId) {
        String portalId = dataDTO.getPortalId();
        String reviewPeriodId = dataDTO.getReviewPeriodId();
        reportingDateBegin = dataDTO.getReportingDateBegin();
        reportingDateEnd = dataDTO.getReportingDateEnd();

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

            jdbcUtil = new JDBCUtil();
            ResultSet rs = null;
            query = "SELECT DISTINCT patient.* FROM patient JOIN nigqual ON patient.facility_id = nigqual.facility_id AND patient.patient_id = nigqual.patient_id "
                    + " WHERE patient.facility_id = " + facilityId + " AND nigqual.portal_id = " + Long.parseLong(portalId) + " AND nigqual.review_period_id = " + Long.parseLong(reviewPeriodId) + " AND nigqual.thermatic_area = 'PM'";

            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                initVariables();

                //PMTCT_ANC_TestingAndCounselling
                patientId = resultSet.getLong("patient_id");
                hospitalNum = resultSet.getString("hospital_num");

                counselFeeding(patientId);
                partnerNotification(patientId);
                timeHivDiagnosis(patientId);
                dateConfirmedHiv(patientId);
                arvRegimenCurrent(patientId);
                dateArvRegimenCurrent(patientId);
                cd4Ordered(patientId);
                cd4(patientId);

                query = "SELECT * FROM anc WHERE patient_id = " + patientId + " AND date_visit >= '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "' AND date_visit <= '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' ORDER BY date_visit DESC";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    WomanCounselledForHCT = "YES";
                    WomanAcceptedHCT = "YES";
                    HCT_Done = "YES";
                    Date_HCT_Done = rs.getDate("date_visit") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_visit"), "dd/MM/yyyy");
                    HIV_Status = "POSITIVE";

                    WHO_Clinical_Stage = rs.getString("clinic_stage");
                    PatientFoundEligible_for_ARTByClinician = "";
                    PrePregnancyHAART = "";
                    Referred_for_ART = "NOT APPLICABLE";
                    AntePartumARV = "";
                    ZDV_OnlyOptionA_Antepartum_ARV = "";
                    HAART_for_Prophylaxis_Antepartum_ARV = "";
                    HAART_for_Treatment_Antepartum_ARV = "";
                    None_Antepartum_ARV = "";
                    Unknown_Antepartum_ARV = "";
                    Other_Antepartum_ARV = "";
                }

                //PMTCT_DeliveryRegister
                query = "SELECT * FROM delivery WHERE patient_id = " + patientId + " AND date_visit >= '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "' AND date_visit <= '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' ORDER BY date_visit DESC";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    Date_of_Delivery = rs.getDate("date_delivery") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_delivery"), "dd/MM/yyyy");
                    Maternal_Intrapartum_ARVRegimen_Received = "";
                    Mode_of_Delivery = rs.getString("mode_of_delivery");
                    Mode_of_Delivery = Mode_of_Delivery.equals("Vaginal") ? "VAGINAL" : Mode_of_Delivery.equals("Elective CS") ? "ELECTIVE C SECTION" : Mode_of_Delivery.equals("Emergency CS") ? "EMERGENCY C SECTION" : Mode_of_Delivery.equals("Other") ? "OTHER" : "NOT DOCUMENTED";
                    Gestation_at_DeliveryInWeeks = "";
                    Epistomy = rs.getString("episiotomy").toUpperCase();
                    Infant_Feeding_Choice = "";
                    Maternal_Outcome = rs.getString("maternal_outcome").toUpperCase();
                    Child_Status = "";
                    Child_Status = Child_Status.equals("A - Alive") ? "ALIVE" : Child_Status.equals("SB - Stillbirth") ? "STILLBIRTH" : Child_Status.equals("NND - Neonatal death") ? "NEONATAL DEATH" : "";
                }

                //PMTCT_MaternalFollowUpRegister
                query = "SELECT * FROM delivery WHERE patient_id = " + patientId + " AND date_visit >= '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "' AND date_visit <= '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' ORDER BY date_visit DESC";
                preparedStatement = jdbcUtil.getStatement(query);
                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    Date_of_Delivery = rs.getDate("date_delivery") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_delivery"), "dd/MM/yyyy");
                    Maternal_Intrapartum_ARVRegimen_Received = "";
                    Mode_of_Delivery = rs.getString("mode_of_delivery");
                    Mode_of_Delivery = Mode_of_Delivery.equals("Vaginal") ? "VAGINAL" : Mode_of_Delivery.equals("Elective CS") ? "ELECTIVE C SECTION" : Mode_of_Delivery.equals("Emergency CS") ? "EMERGENCY C SECTION" : Mode_of_Delivery.equals("Other") ? "OTHER" : "NOT DOCUMENTED";
                    Gestation_at_DeliveryInWeeks = "";
                    Epistomy = rs.getString("episiotomy").toUpperCase();
                    Infant_Feeding_Choice = "";
                    Maternal_Outcome = rs.getString("maternal_outcome").toUpperCase();
                    Child_Status = "";
                    Child_Status = Child_Status.equals("A - Alive") ? "ALIVE" : Child_Status.equals("SB - Stillbirth") ? "STILLBIRTH" : Child_Status.equals("NND - Neonatal death") ? "NEONATAL DEATH" : "";
                }

            }

            Element row1 = root1.addElement("pmtct_anc_testingandcounselling_record");
            Element column1 = row1.addElement("PatientID");
            column1.setText(hospitalNum);
            column1 = row1.addElement("WomanCounselledForHCT");
            column1.setText(WomanCounselledForHCT);
            column1 = row1.addElement("HCT_Done");
            column1.setText(HCT_Done);
            column1 = row1.addElement("Date_HCT_Done");
            column1.setText(Date_HCT_Done);
            column1 = row1.addElement("Time_HCT_Done");
            column1.setText(Time_HCT_Done);
            column1 = row1.addElement("HIV_Status");
            column1.setText(HIV_Status);
            column1 = row1.addElement("Date_HIV_Diagnosis");
            column1.setText(Date_HIV_Diagnosis);
            column1 = row1.addElement("InfantFeedingCounsellingReceived");
            column1.setText(InfantFeedingCounsellingReceived);
            column1 = row1.addElement("WomanAgreedToPartnerNotification");
            column1.setText(WomanAgreedToPartnerNotification);
            column1 = row1.addElement("CD4_Ordered");
            column1.setText(CD4_Ordered);
            column1 = row1.addElement("CD4_Order_Date");
            column1.setText(CD4_Order_Date);
            column1 = row1.addElement("CD4_Date_Done");
            column1.setText(CD4_Date_Done);
            column1 = row1.addElement("CD4_Date_Result_Received");
            column1.setText(CD4_Date_Result_Received);
            column1 = row1.addElement("CD4_Count");
            column1.setText(CD4_Count);
            column1 = row1.addElement("WHO_Clinical_Stage");
            column1.setText(WHO_Clinical_Stage);
            column1 = row1.addElement("PatientFoundEligible_for_ARTByClinician");
            column1.setText(PatientFoundEligible_for_ARTByClinician);
            column1 = row1.addElement("ARV_Received");
            column1.setText(ARV_Received);
            column1 = row1.addElement("Date_ARV_Regimen_Started");
            column1.setText(Date_ARV_Regimen_Started);
            column1 = row1.addElement("PrePregnancyHAART");
            column1.setText(PrePregnancyHAART);
            column1 = row1.addElement("Referred_for_ART");
            column1.setText(Referred_for_ART);
            column1 = row1.addElement("FacilityID");
            column1.setText(FacilityID);
            column1 = row1.addElement("AntePartumARV");
            column1.setText(AntePartumARV);
            column1 = row1.addElement("UploaderId");
            column1.setText(UploaderId);
            column1 = row1.addElement("UploadDt");
            column1.setText(UploadDt);
            column1 = row1.addElement("AntePartumARV_OtherValue");
            column1.setText(AntePartumARV_OtherValue);
            column1 = row1.addElement("webUploadFlag");
            column1.setText(webUploadFlag);
            column1 = row1.addElement("ZDV_OnlyOptionA_Antepartum_ARV");
            column1.setText(ZDV_OnlyOptionA_Antepartum_ARV);
            column1 = row1.addElement("HAART_for_Prophylaxis_Antepartum_ARV");
            column1.setText(HAART_for_Prophylaxis_Antepartum_ARV);
            column1 = row1.addElement("HAART_for_Treatment_Antepartum_ARV");
            column1.setText(HAART_for_Treatment_Antepartum_ARV);
            column1 = row1.addElement("None_Antepartum_ARV");
            column1.setText(None_Antepartum_ARV);
            column1 = row1.addElement("Unknown_Antepartum_ARV");
            column1.setText(Unknown_Antepartum_ARV);
            column1 = row1.addElement("Other_Antepartum_ARV");
            column1.setText(Other_Antepartum_ARV);
            column1 = row1.addElement("ReviewPeriodID");
            column1.setText(ReviewPeriodID);

            Element row2 = root2.addElement("pmtct_deliveryregister_record");
            Element column2 = row2.addElement("PatientID");
            column2.setText(hospitalNum);
            column2 = row2.addElement("Date_of_Delivery");
            column2.setText(Date_of_Delivery);
            column2 = row2.addElement("Maternal_Intrapartum_ARVRegimen_Received");
            column2.setText(Maternal_Intrapartum_ARVRegimen_Received);
            column2 = row2.addElement("Mode_of_Delivery");
            column2.setText(Mode_of_Delivery);
            column2 = row2.addElement("Gestation_at_DeliveryInWeeks");
            column2.setText(Gestation_at_DeliveryInWeeks);
            column2 = row2.addElement("Epistomy");
            column2.setText(Epistomy);
            column2 = row2.addElement("Infant_Feeding_Choice");
            column2.setText(Infant_Feeding_Choice);
            column2 = row2.addElement("Maternal_Outcome");
            column2.setText(Maternal_Outcome);
            column2 = row2.addElement("Child_Status");
            column2.setText(Child_Status);
            column2 = row2.addElement("FacilityID");
            column2.setText(FacilityID);
            column2 = row2.addElement("UploaderId");
            column2.setText(UploaderId);
            column2 = row2.addElement("UploadDt");
            column2.setText(UploadDt);
            column2 = row2.addElement("Was_Maternal_Intrapartum_ARVRegiment_Received");
            column2.setText(Was_Maternal_Intrapartum_ARVRegiment_Received);
            column2 = row2.addElement("MaternalIntrapartumARV_Other");
            column2.setText(MaternalIntrapartumARV_Other);
            column2 = row2.addElement("webUploadFlag");
            column2.setText(webUploadFlag);
            column2 = row2.addElement("OptA_Maternal_Intrapartum_ARVRegimen");
            column2.setText(OptA_Maternal_Intrapartum_ARVRegimen);
            column2 = row2.addElement("OptB_Maternal_Intrapartum_ARVRegimen");
            column2.setText(OptB_Maternal_Intrapartum_ARVRegimen);
            column2 = row2.addElement("HAART_for_Treatment_Maternal_Intrapartum_ARVRegimen");
            column2.setText(HAART_for_Treatment_Maternal_Intrapartum_ARVRegimen);
            column2 = row2.addElement("None_Maternal_Intrapartum_ARVRegimen");
            column2.setText(None_Maternal_Intrapartum_ARVRegimen);
            column2 = row2.addElement("Unknown_Maternal_Intrapartum_ARVRegimen");
            column2.setText(Unknown_Maternal_Intrapartum_ARVRegimen);
            column2 = row2.addElement("Other_Maternal_Intrapartum_ARVRegimen");
            column2.setText(Other_Maternal_Intrapartum_ARVRegimen);
            column2 = row2.addElement("ReviewPeriodID");
            column2.setText(ReviewPeriodID);

            Element row3 = root3.addElement("pmtct_childfollowupregister_record");
            Element column3 = row3.addElement("PatientID");
            column3.setText(hospitalNum);
            column3 = row3.addElement("DBS_Sample_Collected");
            column3.setText(DBS_Sample_Collected);
            column3 = row3.addElement("PCR_EID_Done1st");
            column3.setText(PCR_EID_Done1st);
            column3 = row3.addElement("PCR_EID_Result1st");
            column3.setText(PCR_EID_Result1st);
            column3 = row3.addElement("PCR_EID_Date_Sample_Collected1st");
            column3.setText(PCR_EID_Date_Sample_Collected1st);
            column3 = row3.addElement("PCR_EID_Date_CareGiver_Received_Results1st");
            column3.setText(PCR_EID_Date_CareGiver_Received_Results1st);
            column3 = row3.addElement("PCR_EID_Done2nd");
            column3.setText(PCR_EID_Done2nd);
            column3 = row3.addElement("PCR_EID_Result2nd");
            column3.setText(PCR_EID_Result2nd);
            column3 = row3.addElement("PCR_EID_Date_Sample_Collected2nd");
            column3.setText(PCR_EID_Date_Sample_Collected2nd);
            column3 = row3.addElement("PCR_EID_Date_CareGiver_Received_Results2nd");
            column3.setText(PCR_EID_Date_CareGiver_Received_Results2nd);
            column3 = row3.addElement("RapidTest_L12Months_Done");
            column3.setText(RapidTest_L12Months_Done);
            column3 = row3.addElement("RapidTest_L12Months_Result");
            column3.setText(RapidTest_L12Months_Result);
            column3 = row3.addElement("RapidTest_L12Months_Date_Sample_Collected");
            column3.setText(RapidTest_L12Months_Date_Sample_Collected);
            column3 = row3.addElement("RapidTest_L12Months_Date_CareGiver_Received_Results");
            column3.setText(RapidTest_L12Months_Date_CareGiver_Received_Results);
            column3 = row3.addElement("RapidTest_18Months_Done");
            column3.setText(RapidTest_18Months_Done);
            column3 = row3.addElement("RapidTest_18Months_Result");
            column3.setText(RapidTest_18Months_Result);
            column3 = row3.addElement("RapidTest_18Months_Date_Sample_Collected");
            column3.setText(RapidTest_18Months_Date_Sample_Collected);
            column3 = row3.addElement("RapidTest_18Months_Date_CareGiver_Received_Results");
            column3.setText(RapidTest_18Months_Date_CareGiver_Received_Results);
            column3 = row3.addElement("ReferredToARVClinic");
            column3.setText(ReferredToARVClinic);
            column3 = row3.addElement("ARV_Prophylaxis_Given");
            column3.setText(ARV_Prophylaxis_Given);
            column3 = row3.addElement("Infant_Received_NVP_Within72hours");
            column3.setText(Infant_Received_NVP_Within72hours);
            column3 = row3.addElement("Infant_Received_CPT");
            column3.setText(Infant_Received_CPT);
            column3 = row3.addElement("AgeOfCotrimInitiation");
            column3.setText(AgeOfCotrimInitiation);
            column3 = row3.addElement("UnitOfAgeMeasure");
            column3.setText(UnitOfAgeMeasure);
            column3 = row3.addElement("Date_DBS_Collected");
            column3.setText(Date_DBS_Collected);
            column3 = row3.addElement("FacilityID");
            column3.setText(FacilityID);
            column3 = row3.addElement("Did_the_Infant-Receive_Following");
            //column3.setText(Did_the_Infant-Receive_Following);
            column3 = row3.addElement("UploaderId");
            column3.setText(UploaderId);
            column3 = row3.addElement("UploadDt");
            column3.setText(UploadDt);
            column3 = row3.addElement("webUploadFlag");
            column3.setText(webUploadFlag);
            column3 = row3.addElement("Daily_NVP_Until1WeekAfterCeasationOfBreast");
            column3.setText(Daily_NVP_Until1WeekAfterCeasationOfBreast);
            column3 = row3.addElement("Daily_NVP_for_6_Weeks");
            column3.setText(Daily_NVP_for_6_Weeks);
            column3 = row3.addElement("sdNVP_and_daily_ZDV_for_6_Weeks");
            column3.setText(sdNVP_and_daily_ZDV_for_6_Weeks);
            column3 = row3.addElement("Other_infant_received");
            column3.setText(Other_infant_received);
            column3 = row3.addElement("NotRecorded_infant_received");
            column3.setText(NotRecorded_infant_received);
            column3 = row3.addElement("None_infant_received");
            column3.setText(None_infant_received);
            column3 = row3.addElement("ReviewPeriodID");
            column3.setText(ReviewPeriodID);

            Element row4 = root4.addElement("pmtct_maternalfollowupregister_record");
            Element column4 = row4.addElement("PatientID");
            column4.setText(hospitalNum);
            column4 = row4.addElement("Mother_Accessed_FamilyPlanning");
            column4.setText(Mother_Accessed_FamilyPlanning);
            column4 = row4.addElement("Family_Planning_Method");
            column4.setText(Family_Planning_Method);
            column4 = row4.addElement("InfantFeedingMethodUsed");
            column4.setText(InfantFeedingMethodUsed);
            column4 = row4.addElement("Mother_Received_CPT");
            column4.setText(Mother_Received_CPT);
            column4 = row4.addElement("Maternal_Referral");
            column4.setText(Maternal_Referral);
            column4 = row4.addElement("Infant_Referral");
            column4.setText(Infant_Referral);
            column4 = row4.addElement("FacilityID");
            column4.setText(FacilityID);
            column4 = row4.addElement("UploaderId");
            column4.setText(UploaderId);
            column4 = row4.addElement("UploadDt");
            column4.setText(UploadDt);
            column4 = row4.addElement("webUploadFlag");
            column4.setText(webUploadFlag);
            column4 = row4.addElement("ReviewPeriodID");
            column4.setText(ReviewPeriodID);

            Element row5 = root5.addElement("pmtct_partnerregister_record");
            Element column5 = row5.addElement("PatientID");
            column5.setText(hospitalNum);
            column5 = row5.addElement("Partner_of_HIVPositiveWoman_TestedAndReceivedResult");
            column5.setText(Partner_of_HIVPositiveWoman_TestedAndReceivedResult);
            column5 = row5.addElement("Partner_HIV_Status");
            column5.setText(Partner_HIV_Status);
            column5 = row5.addElement("FacilityID");
            column5.setText(FacilityID);
            column5 = row5.addElement("UploaderId");
            column5.setText(UploaderId);
            column5 = row5.addElement("UploadDt");
            column5.setText(UploadDt);
            column5 = row5.addElement("webUploadFlag");
            column5.setText(webUploadFlag);
            column5 = row5.addElement("ReviewPeriodID");
            column5.setText(ReviewPeriodID);

            Element row6 = root6.addElement("pmtct_patienttype_record");
            Element column6 = row6.addElement("PatientID");
            column6.setText(hospitalNum);
            column6 = row6.addElement("BookingStatus");
            column6.setText(BookingStatus);
            column6 = row6.addElement("InfectionStatus");
            column6.setText(InfectionStatus);
            column6 = row6.addElement("FacilityID");
            column6.setText(FacilityID);
            column6 = row6.addElement("UploaderId");
            column6.setText(UploaderId);
            column6 = row6.addElement("UploadDt");
            column6.setText(UploadDt);
            column6 = row6.addElement("webUploadFlag");
            column6.setText(webUploadFlag);
            column6 = row6.addElement("ReviewPeriodID");
            column6.setText(ReviewPeriodID);

            Element row7 = root7.addElement("pediatric_pmtct_perinatal_record");
            Element column7 = row7.addElement("PatientID");
            column7.setText(hospitalNum);
            column7 = row7.addElement("MotherHIVStatus");
            column7.setText(MotherHIVStatus);
            column7 = row7.addElement("WhenWasMotherDiagnosedWithHIV");
            column7.setText(WhenWasMotherDiagnosedWithHIV);
            column7 = row7.addElement("WasMotherOnART");
            column7.setText(WasMotherOnART);
            column7 = row7.addElement("PregnancyDurationWhenDiagnosedInWeeks");
            column7.setText(PregnancyDurationWhenDiagnosedInWeeks);
            column7 = row7.addElement("ExclusiveBreastFeeding6Months");
            column7.setText(ExclusiveBreastFeeding6Months);
            column7 = row7.addElement("BreastMilkSupplementedBefore6Months");
            column7.setText(BreastMilkSupplementedBefore6Months);
            column7 = row7.addElement("MixedWithBFBefore6Months");
            column7.setText(MixedWithBFBefore6Months);
            column7 = row7.addElement("MixedWithbFAfter6Months");
            column7.setText(MixedWithbFAfter6Months);
            column7 = row7.addElement("RegularDietForAge");
            column7.setText(RegularDietForAge);
            column7 = row7.addElement("NutritionalSupplements");
            column7.setText(NutritionalSupplements);
            column7 = row7.addElement("Other_treatment_received_by_infant");
            column7.setText(Other_treatment_received_by_infant);
            column7 = row7.addElement("NoOfWeeksInPregnancyWhenMotherInfected");
            column7.setText(NoOfWeeksInPregnancyWhenMotherInfected);
            column7 = row7.addElement("PMTCT_PerinatalID");
            column7.setText(PMTCT_PerinatalID);
            column7 = row7.addElement("FacilityID");
            column7.setText(FacilityID);
            column7 = row7.addElement("UploaderId");
            column7.setText(UploaderId);
            column7 = row7.addElement("UploadDt");
            column7.setText(UploadDt);
            column7 = row7.addElement("Did_infant_receive_following");
            column7.setText(Did_infant_receive_following);
            column7 = row7.addElement("webUploadFlag");
            column7.setText(webUploadFlag);
            column7 = row7.addElement("Daily_NVP_1_week_until_breastfeeding");
            column7.setText(Daily_NVP_1_week_until_breastfeeding);
            column7 = row7.addElement("Daily_NVP_for_6_weeks");
            column7.setText(Daily_NVP_for_6_weeks);
            column7 = row7.addElement("sdNVP_daily_ZDV_for_6_weeks");
            column7.setText(sdNVP_daily_ZDV_for_6_weeks);
            column7 = row7.addElement("NotRecord_infant_received");
            column7.setText(NotRecord_infant_received);
            column7 = row7.addElement("ReviewPeriodID");
            column7.setText(ReviewPeriodID);

            Element row8 = root8.addElement("pediatric_pmtct_perinatal_intervention_record");
            Element column8 = row8.addElement("StagePeriod");
            column8.setText(StagePeriod);
            column8 = row8.addElement("GestationAtInitiationInWeeks");
            column8.setText(GestationAtInitiationInWeeks);
            column8 = row8.addElement("Regimen");
            column8.setText(Regimen);
            column8 = row8.addElement("PatientID");
            column8.setText(hospitalNum);
            column8 = row8.addElement("FacilityID");
            column8.setText(FacilityID);
            column8 = row8.addElement("UploaderId");
            column8.setText(UploaderId);
            column8 = row8.addElement("UploadDt");
            column8.setText(UploadDt);
            column8 = row8.addElement("ID");
            column8.setText(FacilityID);
            column8 = row8.addElement("webUploadFlag");
            column8.setText(webUploadFlag);
            column8 = row8.addElement("ReviewPeriodID");
            column8.setText(ReviewPeriodID);
//            DocumentWriter.writeXmlToFile(document1, "pmtct_anc_testingandcounselling");
//            DocumentWriter.writeXmlToFile(document2, "pmtct_deliveryregister");
//            DocumentWriter.writeXmlToFile(document3, "pmtct_childfollowupregister");
//            DocumentWriter.writeXmlToFile(document4, "pmtct_maternalfollowupregister");
//            DocumentWriter.writeXmlToFile(document5, "pmtct_partnerregister");
//            DocumentWriter.writeXmlToFile(document6, "pmtct_patienttype");
//            DocumentWriter.writeXmlToFile(document7, "pediatric_pmtct_perinatal");
//            DocumentWriter.writeXmlToFile(document8, "pediatric_pmtct_perinatal_intervention");
            resultSet = null;
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private void initVariables() {
        patientId = 0L;
        hospitalNum = "";
        WomanCounselledForHCT = "";
        WomanAcceptedHCT = "";
        HCT_Done = "";
        Date_HCT_Done = "";
        Time_HCT_Done = "";
        HIV_Status = "";
        Date_HIV_Diagnosis = "";
        InfantFeedingCounsellingReceived = "";
        WomanAgreedToPartnerNotification = "";
        CD4_Ordered = "";
        CD4_Order_Date = "";
        CD4_Date_Done = "";
        CD4_Date_Result_Received = "";
        CD4_Count = "";
        WHO_Clinical_Stage = "";
        PatientFoundEligible_for_ARTByClinician = "";
        ARV_Received = "";
        Date_ARV_Regimen_Started = "";
        PrePregnancyHAART = "";
        Referred_for_ART = "";
        AntePartumARV = "";
        AntePartumARV_OtherValue = "";
        ZDV_OnlyOptionA_Antepartum_ARV = "";
        HAART_for_Prophylaxis_Antepartum_ARV = "";
        HAART_for_Treatment_Antepartum_ARV = "";
        None_Antepartum_ARV = "";
        Unknown_Antepartum_ARV = "";
        Other_Antepartum_ARV = "";

        DBS_Sample_Collected = "";
        PCR_EID_Done1st = "";
        PCR_EID_Result1st = "";
        PCR_EID_Date_Sample_Collected1st = "";
        PCR_EID_Date_CareGiver_Received_Results1st = "";
        PCR_EID_Done2nd = "";
        PCR_EID_Result2nd = "";
        PCR_EID_Date_Sample_Collected2nd = "";
        PCR_EID_Date_CareGiver_Received_Results2nd = "";
        RapidTest_L12Months_Done = "";
        RapidTest_L12Months_Result = "";
        RapidTest_L12Months_Date_Sample_Collected = "";
        RapidTest_L12Months_Date_CareGiver_Received_Results = "";
        RapidTest_18Months_Done = "";
        RapidTest_18Months_Result = "";
        RapidTest_18Months_Date_Sample_Collected = "";
        RapidTest_18Months_Date_CareGiver_Received_Results = "";
        ReferredToARVClinic = "";
        ARV_Prophylaxis_Given = "";
        Infant_Received_NVP_Within72hours = "";
        Infant_Received_CPT = "";
        AgeOfCotrimInitiation = "";
        UnitOfAgeMeasure = "";
        Date_DBS_Collected = "";
        //Did_the_Infant-Receive_Following = "";
        Daily_NVP_Until1WeekAfterCeasationOfBreast = "";
        Daily_NVP_for_6_Weeks = "";
        sdNVP_and_daily_ZDV_for_6_Weeks = "";
        Other_infant_received = "";
        NotRecorded_infant_received = "";
        None_infant_received = "";

        Date_of_Delivery = "";
        Maternal_Intrapartum_ARVRegimen_Received = "";
        Mode_of_Delivery = "";
        Gestation_at_DeliveryInWeeks = "";
        Epistomy = "";
        Infant_Feeding_Choice = "";
        Maternal_Outcome = "";
        Child_Status = "";
        Was_Maternal_Intrapartum_ARVRegiment_Received = "";
        MaternalIntrapartumARV_Other = "";
        OptA_Maternal_Intrapartum_ARVRegimen = "";
        OptB_Maternal_Intrapartum_ARVRegimen = "";
        HAART_for_Treatment_Maternal_Intrapartum_ARVRegimen = "";
        None_Maternal_Intrapartum_ARVRegimen = "";
        Unknown_Maternal_Intrapartum_ARVRegimen = "";
        Other_Maternal_Intrapartum_ARVRegimen = "";

        Mother_Accessed_FamilyPlanning = "";
        Family_Planning_Method = "";
        InfantFeedingMethodUsed = "";
        Mother_Received_CPT = "";
        Maternal_Referral = "";
        Infant_Referral = "";

        Partner_of_HIVPositiveWoman_TestedAndReceivedResult = "";
        Partner_HIV_Status = "";

        BookingStatus = "";
        InfectionStatus = "";
    }

    private void timeHivDiagnosis(long patientId) {
        try {
            //query = "SELCT time_hiv_diagnosis FROM anc WHERE patient_id = " + id + " AND time_hiv_diagnosis != '' AND time_hiv_diagnosis IS NOT NULL AND date_visit BETWEEN DATEADD('MONTH', -6, '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "') AND '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' UNION SELCT time_hiv_diagnosis FROM delivery WHERE patient_id = " + id + " AND time_hiv_diagnosis != '' AND time_hiv_diagnosis IS NOT NULL AND date_visit BETWEEN DATEADD('MONTH', -6, '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "') AND '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' UNION SELCT time_hiv_diagnosis FROM maternalfollowup WHERE patient_id = " + id + " AND time_hiv_diagnosis != '' AND time_hiv_diagnosis IS NOT NULL AND date_visit BETWEEN DATEADD('MONTH', -6, '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "') AND '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "'";
            query = "SELCT time_hiv_diagnosis FROM anc WHERE patient_id = " + patientId + " AND time_hiv_diagnosis != '' AND time_hiv_diagnosis IS NOT NULL UNION SELCT time_hiv_diagnosis FROM delivery WHERE patient_id = " + patientId + " AND time_hiv_diagnosis != '' AND time_hiv_diagnosis IS NOT NULL UNION SELCT time_hiv_diagnosis FROM maternalfollowup WHERE patient_id = " + patientId + " AND time_hiv_diagnosis != '' AND time_hiv_diagnosis IS NOT NULL";
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Time_HCT_Done = rs.getString("time_hiv_diagnosis");
                Time_HCT_Done = Time_HCT_Done.equals("Previous - Non pregnant") ? "BEFFORE PREGNANCY" : Time_HCT_Done.equals("Previous pregnancy") ? "BEFFORE PREGNANCY" : Time_HCT_Done.equals("ANC") ? "ANC" : Time_HCT_Done.equals("Labour") ? " LABOUR DELIVERY" : "AFTER DELIVERY";
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void dateConfirmedHiv(long patientId) {
        try {
            query = "SELCT date_confirmed_hiv FROM anc WHERE patient_id = " + patientId + " AND date_confirmed_hiv != '' AND date_confirmed_hiv IS NOT NULL UNION SELCT date_confirmed_hiv FROM delivery WHERE patient_id = " + patientId + " AND date_confirmed_hiv != '' AND date_confirmed_hiv IS NOT NULL UNION SELCT date_confirmed_hiv FROM maternalfollowup WHERE patient_id = " + patientId + " AND date_confirmed_hiv != '' AND date_confirmed_hiv IS NOT NULL";
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Date_HIV_Diagnosis = rs.getDate("date_confirmed_hiv") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_confirmed_hiv"), "dd/MM/yyyy");
            } else {
                Date_HIV_Diagnosis = "01/01/1900";
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private String arvRegimenPast(long patientId) {
        try {
            query = "SELCT time_hiv_diagnosis FROM anc WHERE patient_id = " + patientId + " AND time_hiv_diagnosis != '' AND time_hiv_diagnosis IS NOT NULL UNION SELCT time_hiv_diagnosis FROM delivery WHERE patient_id = " + patientId + " AND time_hiv_diagnosis != '' AND time_hiv_diagnosis IS NOT NULL UNION SELCT time_hiv_diagnosis FROM maternalfollowup WHERE patient_id = " + patientId + " AND time_hiv_diagnosis != '' AND time_hiv_diagnosis IS NOT NULL";
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String content = rs.getString("time_hiv_diagnosis");
                return content.equals("Previous - Non pregnant") ? "BEFFORE PREGNANCY" : content.equals("Previous pregnancy") ? "BEFFORE PREGNANCY" : content.equals("ANC") ? "ANC" : content.equals("Labour") ? " LABOUR DELIVERY" : "AFTER DELIVERY";
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "";
    }

    private String arvRegimenCurrent(long patientId) {
        try {
            query = "SELCT arv_regimen_current FROM anc WHERE patient_id = " + patientId + " AND arv_regimen_current != '' AND arv_regimen_current IS NOT NULL UNION SELCT arv_regimen_current FROM delivery WHERE patient_id = " + patientId + " AND arv_regimen_current != '' AND arv_regimen_current IS NOT NULL UNION SELCT arv_regimen_current FROM maternalfollowup WHERE patient_id = " + patientId + " AND arv_regimen_current != '' AND arv_regimen_current IS NOT NULL";
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                ARV_Received = rs.getString("arv_regimen_current").trim().isEmpty() ? "NO" : "YES";
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "";
    }

    private String dateArvRegimenCurrent(long patientId) {
        try {
            query = "SELCT date_arv_regimen_current FROM anc WHERE patient_id = " + patientId + " AND date_arv_regimen_current != '' AND date_arv_regimen_current IS NOT NULL UNION SELCT date_arv_regimen_current FROM delivery WHERE patient_id = " + patientId + " AND date_arv_regimen_current != '' AND date_arv_regimen_current IS NOT NULL UNION SELCT date_arv_regimen_current FROM maternalfollowup WHERE patient_id = " + patientId + " AND date_arv_regimen_current != '' AND date_arv_regimen_current IS NOT NULL";
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Date_ARV_Regimen_Started = rs.getDate("date_arv_regimen_current") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_arv_regimen_current"), "dd/MM/yyyy");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "01/01/1900";
    }

    private void cd4Ordered(long patientId) {
        try {
            query = "SELECT cd4_ordered, date_visit FROM anc WHERE patient_id = " + patientId + " AND cd4_ordered != '' AND cd4_ordered IS NOT NULL AND date_visit BETWEEN DATEADD('MONTH', -6, '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "') AND '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' UNION SELECT cd4_ordered, date_delivery AS date_visit FROM delivery WHERE patient_id = " + patientId + " AND cd4_ordered != '' AND cd4_ordered IS NOT NULL AND date_delivery BETWEEN DATEADD('MONTH', -6, '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "') AND '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' UNION SELECT cd4_ordered, date_visit FROM maternalfollowup WHERE patient_id = " + patientId + " AND cd4_ordered != '' AND cd4_ordered IS NOT NULL AND date_visit BETWEEN DATEADD('MONTH', -6, '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "') AND '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                CD4_Ordered = rs.getString("cd4_ordered").trim().isEmpty() ? "NO" : "YES";
                CD4_Order_Date = rs.getDate("date_visit") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_visit"), "dd/MM/yyyy");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void cd4(long patientId) {
        try {
            query = "SELECT cd4, date_visit FROM anc WHERE patient_id = " + patientId + " AND cd4 IS NOT NULL AND date_visit BETWEEN DATEADD('MONTH', -6, '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "') AND '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' UNION SELECT cd4, date_delivery AS date_visit FROM delivery WHERE patient_id = " + patientId + " AND cd4 IS NOT NULL AND date_delivery BETWEEN DATEADD('MONTH', -6, '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "') AND '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "' UNION SELECT cd4, date_visit FROM maternalfollowup WHERE patient_id = " + patientId + " AND cd4_ordered IS NOT NULL AND date_visit BETWEEN DATEADD('MONTH', -6, '" + DateUtil.formatDateString(reportingDateBegin, "MM/dd/yyyy", "yyyy-MM-dd") + "') AND '" + DateUtil.formatDateString(reportingDateEnd, "MM/dd/yyyy", "yyyy-MM-dd") + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                CD4_Date_Result_Received = rs.getDate("date_visit") == null ? "01/01/1900" : DateUtil.parseDateToString(rs.getDate("date_visit"), "dd/MM/yyyy");
                CD4_Date_Done = CD4_Date_Result_Received;
                CD4_Count = Long.toString(Math.round(rs.getDouble("cd4")));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void counselFeeding(long patientId) {
        try {
            query = "SELCT * FROM maternalfollowup WHERE patient_id = " + patientId + " AND counsel_feeding != '' AND counsel_feeding IS NOT NULL";
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                InfantFeedingCounsellingReceived = "YES";
            } else {
                InfantFeedingCounsellingReceived = "NO";
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void partnerNotification(long patientId) {
        try {
            query = "SELCT * FROM partnerinformation WHERE patient_id = " + patientId + " AND partner_notification = 'YES'";
            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                WomanAgreedToPartnerNotification = "YES";
            } else {
                WomanAgreedToPartnerNotification = "NO";
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

