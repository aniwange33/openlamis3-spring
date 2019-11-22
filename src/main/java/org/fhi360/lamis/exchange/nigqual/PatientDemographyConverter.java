
package org.fhi360.lamis.exchange.nigqual;
import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import org.fhi360.lamis.model.dto.PatientDemographyDTO;
import org.fhi360.lamis.utility.JDBCUtil;
import javax.servlet.http.HttpServletRequest;
import org.fhi360.lamis.utility.Scrambler;
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.utility.DateUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

@Component
public class PatientDemographyConverter {    
    private HttpServletRequest request;
    private HttpSession session;
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;

    private Scrambler scrambler;
    
    public PatientDemographyConverter() {
        this.scrambler = new Scrambler();

    } 

    public synchronized void convertXml(PatientDemographyDTO patientDemographyDTO,  long facilityId ) {
        String portalId = patientDemographyDTO.getPortalId();
        String reviewPeriodId = patientDemographyDTO.getReviewPeriodId();
        
        String ClinicalVisit6MonthsPriorToReview = "Yes";
        String RNL_SerialNo = "";
        String HospitalAdmissionDuringReview = "No";
        String WardVillageTown_OfResidence = "";
        String Tribe = "";
        String FacilityID = portalId;
        String RecordCompletionPosition = "16";
        String UploaderId = "NULL";
        String UploadDt = "NULL";
        String webUploadFlag = "No";
        String ReviewPeriodID = reviewPeriodId;
        String HasThePatientHadaClinicalVisit3MonthsPriorToTheReviewPeriod = "Yes";
        
        String hospitalNum = "";
        String Lastname = "";
        String Firstname = "";
        String Gender = "";                
        String MaritalStatus =  "";
        String DateOfBirth = "";
        String Age = "";
        String Education = "";
        String Occupation = "";
        String State_OfResidence = "";
        String LGA_OfResidence = "";
        String DateEnrolled = "";
        String dateTestedPositive = "";
        
        String UnitOfAgeMeasure = "";
        String DeliveryLocation = "";
        String PrimaryCareGiver = "";
        String DateOfLastVisit = "";
        String AdmissionDuringReviewPeriod = "";
        String CareGiverOccupation = "";

        String Patient_ANC_No = "";
        String Parity = "";
        String LMP = "";
        String EDD = "";
        String Date_Of_1st_Booking = "";
        String GestationAge_At_1st_BookingInWeeks = "";

        int SerialNo = 0;
        try {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("data-set");

            jdbcUtil = new JDBCUtil();
            query = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, patient.gender, patient.date_birth, patient.age, patient.age_unit, patient.marital_status, patient.occupation, patient.education, patient.phone, patient.address, patient.state, patient.lga, patient.date_registration, patient.status_registration, patient.current_status, patient.date_current_status, patient.date_started, nigqual.thermatic_area FROM patient "
                    + " JOIN nigqual ON patient.facility_id = nigqual.facility_id AND patient.patient_id = nigqual.patient_id WHERE patient.facility_id = " + facilityId + " AND nigqual.portal_id = " + Long.parseLong(portalId) + " AND nigqual.review_period_id = " + Long.parseLong(reviewPeriodId) + " AND nigqual.thermatic_area = '" + patientDemographyDTO.getThermaticArea() + "'" ;

            preparedStatement = jdbcUtil.getStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery(); 
            while (resultSet.next()) {
                SerialNo++;
                RNL_SerialNo = Long.toString(SerialNo); 
                hospitalNum = resultSet.getString("hospital_num");
                Lastname = resultSet.getString("surname") == null ? "" : resultSet.getString("surname");
                Lastname = (patientDemographyDTO.getViewIdentifier())? scrambler.unscrambleCharacters(Lastname) : Lastname;
                Lastname = StringUtils.upperCase(Lastname);                
                Firstname = resultSet.getString("other_names") == null ? "" : resultSet.getString("other_names");
                Firstname = ((patientDemographyDTO.getViewIdentifier())? scrambler.unscrambleCharacters(Firstname) : Firstname);
                Firstname = StringUtils.capitalize(Firstname);
                Gender = resultSet.getString("gender") == null ? "" : resultSet.getString("gender");                
                MaritalStatus =  resultSet.getString("marital_status") == null ? "" : resultSet.getString("marital_status");
                DateOfBirth = (resultSet.getDate("date_birth") == null)? "01/01/1900" : DateUtil.parseDateToString(resultSet.getDate("date_birth"), "dd/MM/yyyy");
                Age = (resultSet.getInt("age") == 0)? "0" : Integer.toString(resultSet.getInt("age"));
                UnitOfAgeMeasure = resultSet.getString("age_unit") == null ? "YEARS" : resultSet.getString("age_unit");
                Education = resultSet.getString("education") == null ? "" :resultSet.getString("education");
                Occupation = resultSet.getString("occupation") == null ? "" : resultSet.getString("occupation");
                State_OfResidence = resultSet.getString("state") == null ? "" : resultSet.getString("state");
                LGA_OfResidence = resultSet.getString("lga") == null ? "" : resultSet.getString("lga");
                DateEnrolled = (resultSet.getDate("date_registration") == null)? "01/01/1900" : DateUtil.parseDateToString(resultSet.getDate("date_registration"), "dd/MM/yyyy");                
                dateTestedPositive = (resultSet.getDate("date_registration") == null)? "01/01/1900" : DateUtil.parseDateToString(resultSet.getDate("date_registration"), "dd/MM/yyyy");

                String currentStatus = resultSet.getString("current_status") == null ? "" : resultSet.getString("current_status");         
                if(currentStatus.contains("Transfer In") && resultSet.getDate("date_started") != null) {
                    Date date = DateUtil.addDay(resultSet.getDate("date_started"), -14);
                    dateTestedPositive = DateUtil.parseDateToString(date, "MM/dd/yyyy");
                    System.out.println("Tested Positive"+"...Status"+currentStatus+"...ART"+resultSet.getDate("date_started")+"....."+date);
                }
                
                if(patientDemographyDTO.getThermaticArea().equalsIgnoreCase("AD")) {
                    Element row = root.addElement("patientdemographics_record");
                    Element column = row.addElement("Lastname");
                    column.setText(Lastname);
                    column = row.addElement("Firstname");
                    column.setText(Firstname);
                    column = row.addElement("ClinicalVisit6MonthsPriorToReview");
                    column.setText(ClinicalVisit6MonthsPriorToReview);
                    column = row.addElement("MaritalStatus");
                    column.setText(MaritalStatus);
                    column = row.addElement("HospitalNo");
                    column.setText(hospitalNum);
                    column = row.addElement("RNL_SerialNo");
                    column.setText(RNL_SerialNo);
                    column = row.addElement("Gender");
                    column.setText(Gender);
                    column = row.addElement("DateOfBirth");
                    column.setText(DateOfBirth);
                    column = row.addElement("Age");
                    column.setText(Age);
                    column = row.addElement("HospitalAdmissionDuringReview");
                    column.setText(HospitalAdmissionDuringReview);
                    column = row.addElement("Occupation");
                    column.setText(Occupation);
                    column = row.addElement("Education");
                    column.setText(Education);
                    column = row.addElement("WardVillageTown_OfResidence");
                    column.setText(WardVillageTown_OfResidence);
                    column = row.addElement("LGA_OfResidence");
                    column.setText(LGA_OfResidence);
                    column = row.addElement("State_OfResidence");
                    column.setText(State_OfResidence);
                    column = row.addElement("State_OfOrigin");
                    column.setText(State_OfResidence);
                    column = row.addElement("Tribe");
                    column.setText(Tribe);
                    column = row.addElement("PatientID");
                    column.setText(hospitalNum);
                    column = row.addElement("DateEnrolled");
                    column.setText(DateEnrolled);
                    column = row.addElement("HasThePatientHadaClinicalVisit3MonthsPriorToTheReviewPeriod");
                    column.setText(HasThePatientHadaClinicalVisit3MonthsPriorToTheReviewPeriod);
                    column = row.addElement("dateTestedPositive");
                    column.setText(dateTestedPositive);
                }
                if(patientDemographyDTO.getThermaticArea().equalsIgnoreCase("PD")) {
                    Element row = root.addElement("pediatric_patientdemographics_record");
                    Element column = row.addElement("PatientID");
                    column.setText(hospitalNum);
                    column = row.addElement("Lastname");
                    column.setText(Lastname);
                    column = row.addElement("Firstname");
                    column.setText(Firstname);
                    column = row.addElement("HospitalNo");
                    column.setText(hospitalNum);
                    column = row.addElement("Gender");
                    column.setText(Gender);
                    column = row.addElement("DateOfBirth");
                    column.setText(DateOfBirth);
                    column = row.addElement("Age");
                    column.setText(Age);
                    column = row.addElement("UnitOfAgeMeasure");
                    column.setText(UnitOfAgeMeasure);
                    column = row.addElement("DateEnrolledInCare");
                    column.setText(DateEnrolled);
                    column = row.addElement("ClinicalVisit6MonthsPriorToReview");
                    column.setText(ClinicalVisit6MonthsPriorToReview);
                    column = row.addElement("DeliveryLocation");
                    column.setText(DeliveryLocation);
                    column = row.addElement("PrimaryCareGiver");
                    column.setText(PrimaryCareGiver);
                    column = row.addElement("StateOfResidence");
                    column.setText(State_OfResidence);
                    column = row.addElement("LGAOfResidence");
                    column.setText(LGA_OfResidence);
                    column = row.addElement("StateOfOrigin");
                    column.setText(State_OfResidence);
                    column = row.addElement("Tribe");
                    column.setText(Tribe);
                    column = row.addElement("DateOfLastVisit");
                    column.setText(DateOfLastVisit);
                    column = row.addElement("AdmissionDuringReviewPeriod");
                    column.setText(AdmissionDuringReviewPeriod);
                    column = row.addElement("RNL_SerialNo");
                    column.setText(RNL_SerialNo);
                    column = row.addElement("CareGiverOccupation");
                    column.setText(CareGiverOccupation);

                }

                //PMTCT demography
                if(patientDemographyDTO.getThermaticArea().equalsIgnoreCase("PM")) {
                    Element row = root.addElement("pmtct_patientdemographics_record");
                    Element column = row.addElement("PatientID");
                    column.setText(hospitalNum);
                    column = row.addElement("Lastname");
                    column.setText(Lastname);
                    column = row.addElement("Firstname");
                    column.setText(Firstname);
                    column = row.addElement("HospitalNo");
                    column.setText(hospitalNum);
                    column = row.addElement("RNL_SerialNo");
                    column.setText(RNL_SerialNo);
                    column = row.addElement("Patient_ANC_No");
                    column.setText(Patient_ANC_No);
                    column = row.addElement("Age");
                    column.setText(Age);
                    column = row.addElement("Parity");
                    column.setText(Parity);
                    column = row.addElement("LMP");
                    column.setText(LMP);
                    column = row.addElement("EDD");
                    column.setText(EDD);
                    column = row.addElement("Date_Of_1st_Booking");
                    column.setText(Date_Of_1st_Booking);
                    column = row.addElement("GestationAge_At_1st_BookingInWeeks");
                    column.setText(GestationAge_At_1st_BookingInWeeks);
                    column = row.addElement("RecordCompletionPosition");
                    column.setText(RecordCompletionPosition);

                }
            }
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }                  

    }
    
}
