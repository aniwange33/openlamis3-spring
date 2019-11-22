/**
 * @author aalozie
 */
package org.fhi360.lamis.service.parser.xml;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.fhi360.lamis.utility.Scrambler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PatientXmlParser extends DefaultHandler {

    private Long facilityId;
    private boolean skipRecord;
    private Patient patient;
    private Scrambler scrambler = new Scrambler();
    Logger LOG = LoggerFactory.getLogger(PatientXmlParser.class);

    public PatientXmlParser(Long facilityId) {
        this.facilityId = facilityId;
    }

    public void parseXml(String xmlFileName) {
        try {
            //obtain and configure a SAX based parser
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

            //obtain object for SAX hadler class
            SAXParser saxParser = saxParserFactory.newSAXParser();

            //default handler for SAX handler class
            // all three methods are written in handler's body
            DefaultHandler defaultHandler = new DefaultHandler() {
                String patientTag = "close";
                String patientIdTag = "close";
                String facilityIdTag = "close";
                String hospitalNumTag = "close";
                String uniqueIdTag = "close";
                String surnameTag = "close";
                String otherNamesTag = "close";
                String genderTag = "close";
                String dateBirthTag = "close";
                String ageTag = "close";
                String ageUnitTag = "close";
                String maritalStatusTag = "close";
                String educationTag = "close";
                String occupationTag = "close";
                String addressTag = "close";
                String phoneTag = "close";
                String stateTag = "close";
                String lgaTag = "close";
                String nextKinTag = "close";
                String addressKinTag = "close";
                String phoneKinTag = "close";
                String relationKinTag = "close";
                String entryPointTag = "close";
                String targetGroupTag = "close";
                String dateConfirmedHivTag = "close";
                String tbStatusTag = "close";
                String pregnantTag = "close";
                String breastfeedingTag = "close";
                String lmpTag = "close";
                String dateRegistrationTag = "close";
                String statusRegistrationTag = "close";
                String enrollmentSettingTag = "close";
                String dateStartedTag = "close";
                String currentStatusTag = "close";
                String dateCurrentStatusTag = "close";
                String regimentypeTag = "close";
                String regimenTag = "close";
                String lastClinicStageTag = "close";
                String lastViralLoadTag = "close";
                String lastCd4Tag = "close";
                String lastCd4pTag = "close";
                String dateLastCd4Tag = "close";
                String dateLastViralLoadTag = "close";
                String viralLoadDueDateTag = "close";
                String viralLoadTypeTag = "close";
                String dateLastRefillTag = "close";
                String dateNextRefillTag = "close";
                String lastRefillDurationTag = "close";
                String lastRefillSettingTag = "close";
                String dateLastClinicTag = "close";
                String dateNextClinicTag = "close";
                String dateTrackedTag = "close";
                String outcomeTag = "close";
                String agreedDateTag = "close";
                String causeDeathTag = "close";
                String sendMessageTag = "close";
                String timeStampTag = "close";
                String caseManagerIdTag = "close";
                String communitypharmIdTag = "close";
                String userIdTag = "close";
                String idUUIDTag = "close";
                String id;
                String uuid;
                String hospitalNum;

                JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);

                //this method is called every time the parser gets an open tag '<'
                //identifies which element is being open at the time by assigning an open flag
                @Override
                public void startElement(String uri, String localName, String element, Attributes attributes) throws SAXException {
                    if (element.equalsIgnoreCase("patient")) {
                        patientTag = "open";
                        patient = new Patient();
                        skipRecord = false;
                    }
                    if (element.equalsIgnoreCase("facility_id")) {
                        facilityIdTag = "open";
                    } else if (element.equalsIgnoreCase("patient_id")) {
                        patientIdTag = "open";
                    } else if (element.equalsIgnoreCase("hospital_num")) {
                        hospitalNumTag = "open";
                    } else if (element.equalsIgnoreCase("unique_id")) {
                        uniqueIdTag = "open";
                    } else if (element.equalsIgnoreCase("surname")) {
                        surnameTag = "open";
                    } else if (element.equalsIgnoreCase("other_names")) {
                        otherNamesTag = "open";
                    } else if (element.equalsIgnoreCase("gender")) {
                        genderTag = "open";
                    } else if (element.equalsIgnoreCase("date_birth")) {
                        dateBirthTag = "open";
                    } else if (element.equalsIgnoreCase("age")) {
                        ageTag = "open";
                    } else if (element.equalsIgnoreCase("age_unit")) {
                        ageUnitTag = "open";
                    } else if (element.equalsIgnoreCase("marital_status")) {
                        maritalStatusTag = "open";
                    } else if (element.equalsIgnoreCase("education")) {
                        educationTag = "open";
                    } else if (element.equalsIgnoreCase("occupation")) {
                        occupationTag = "open";
                    } else if (element.equalsIgnoreCase("address")) {
                        addressTag = "open";
                    } else if (element.equalsIgnoreCase("phone")) {
                        phoneTag = "open";
                    } else if (element.equalsIgnoreCase("state")) {
                        stateTag = "open";
                    } else if (element.equalsIgnoreCase("lga")) {
                        lgaTag = "open";
                    } else if (element.equalsIgnoreCase("next_kin")) {
                        nextKinTag = "open";
                    } else if (element.equalsIgnoreCase("address_kin")) {
                        addressKinTag = "open";
                    } else if (element.equalsIgnoreCase("phone_kin")) {
                        phoneKinTag = "open";
                    } else if (element.equalsIgnoreCase("relation_kin")) {
                        relationKinTag = "open";
                    } else if (element.equalsIgnoreCase("entry_point")) {
                        entryPointTag = "open";
                    } else if (element.equalsIgnoreCase("target_group")) {
                        targetGroupTag = "open";
                    } else if (element.equalsIgnoreCase("date_confirmed_hiv")) {
                        dateConfirmedHivTag = "open";
                    } else if (element.equalsIgnoreCase("tb_status")) {
                        tbStatusTag = "open";
                    } else if (element.equalsIgnoreCase("pregnant")) {
                        pregnantTag = "open";
                    } else if (element.equalsIgnoreCase("breastfeeding")) {
                        breastfeedingTag = "open";
                    } else if (element.equalsIgnoreCase("date_registration")) {
                        dateRegistrationTag = "open";
                    } else if (element.equalsIgnoreCase("status_registration")) {
                        statusRegistrationTag = "open";
                    } else if (element.equalsIgnoreCase("enrollment_setting")) {
                        enrollmentSettingTag = "open";
                    } else if (element.equalsIgnoreCase("date_started")) {
                        dateStartedTag = "open";
                    } else if (element.equalsIgnoreCase("current_status")) {
                        currentStatusTag = "open";
                    } else if (element.equalsIgnoreCase("date_current_status")) {
                        dateCurrentStatusTag = "open";
                    } else if (element.equalsIgnoreCase("regimentype")) {
                        regimentypeTag = "open";
                    } else if (element.equalsIgnoreCase("regimen")) {
                        regimenTag = "open";
                    } else if (element.equalsIgnoreCase("date_last_refill")) {
                        dateLastRefillTag = "open";
                    } else if (element.equalsIgnoreCase("date_next_refill")) {
                        dateNextRefillTag = "open";
                    } else if (element.equalsIgnoreCase("last_refill_duration")) {
                        lastRefillDurationTag = "open";
                    } else if (element.equalsIgnoreCase("last_refill_setting")) {
                        lastRefillSettingTag = "open";
                    } else if (element.equalsIgnoreCase("date_last_clinic")) {
                        dateLastClinicTag = "open";
                    } else if (element.equalsIgnoreCase("date_next_clinic")) {
                        dateNextClinicTag = "open";
                    } else if (element.equalsIgnoreCase("last_clinic_stage")) {
                        lastClinicStageTag = "open";
                    } else if (element.equalsIgnoreCase("last_cd4")) {
                        lastCd4Tag = "open";
                    } else if (element.equalsIgnoreCase("last_cd4p")) {
                        lastCd4pTag = "open";
                    } else if (element.equalsIgnoreCase("date_last_cd4")) {
                        dateLastCd4Tag = "open";
                    } else if (element.equalsIgnoreCase("last_viral_load")) {
                        lastViralLoadTag = "open";
                    } else if (element.equalsIgnoreCase("date_last_viral_load")) {
                        dateLastViralLoadTag = "open";
                    } else if (element.equalsIgnoreCase("viral_load_due_date")) {
                        viralLoadDueDateTag = "open";
                    } else if (element.equalsIgnoreCase("viral_load_type")) {
                        viralLoadTypeTag = "open";
                    } else if (element.equalsIgnoreCase("date_tracked")) {
                        dateTrackedTag = "open";
                    } else if (element.equalsIgnoreCase("outcome")) {
                        outcomeTag = "open";
                    } else if (element.equalsIgnoreCase("agreed_date")) {
                        agreedDateTag = "open";
                    } else if (element.equalsIgnoreCase("cause_death")) {
                        causeDeathTag = "open";
                    } else if (element.equalsIgnoreCase("send_message")) {
                        sendMessageTag = "open";
                    } else if (element.equalsIgnoreCase("time_stamp")) {
                        timeStampTag = "open";
                    } else if (element.equalsIgnoreCase("casemanager_id")) {
                        caseManagerIdTag = "open";
                    } else if (element.equalsIgnoreCase("communitypharm_id")) {
                        communitypharmIdTag = "open";
                    } else if (element.equalsIgnoreCase("user_id")) {
                        userIdTag = "open";
                    } else if (element.equalsIgnoreCase("id_uuid")) {
                        idUUIDTag = "open";
                    }
                }

                //store data store in between '<' and '>' tags                     
                @Override
                public void characters(char[] chars, int start, int length) throws SAXException {
                    String statusRegistration = "";
                    String dateRegistration = "";
                    if (hospitalNumTag.equals("open")) {
                        hospitalNum = new String(chars, start, length);
                        if (StringUtils.isBlank(hospitalNum)) {
                            skipRecord = true;
                        }
                        patient.setHospitalNum(hospitalNum);
                    } else if (uniqueIdTag.equals("open")) {
                        patient.setUniqueId(new String(chars, start, length));
                    } else if (surnameTag.equals("open")) {
                        patient.setSurname(scrambler.scrambleCharacters(new String(chars, start, length)));
                    } else if (otherNamesTag.equals("open")) {
                        patient.setOtherNames(scrambler.scrambleCharacters(new String(chars, start, length)));
                    } else if (genderTag.equals("open")) {
                        patient.setGender(new String(chars, start, length));
                    } else if (dateBirthTag.equals("open")) {
                        String dateBirth = new String(chars, start, length);
                        if (!dateBirth.trim().isEmpty()) {
                            patient.setDateBirth(LocalDate.parse(dateBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        } else {
                            skipRecord = true;
                        }
                    } else if (ageTag.equals("open")) {
                        String age = new String(chars, start, length);
                        if (!age.trim().isEmpty()) {
                            if (StringUtils.isNumeric(age))
                                patient.setAge(Integer.parseInt(age));
                            else patient.setAge(null);
                        } else {
                            patient.setAge(null);
                        }
                    } else if (ageUnitTag.equals("open")) {
                        patient.setAgeUnit(new String(chars, start, length));
                    } else if (maritalStatusTag.equals("open")) {
                        patient.setMaritalStatus(new String(chars, start, length));
                    } else if (educationTag.equals("open")) {
                        patient.setEducation(new String(chars, start, length));
                    } else if (occupationTag.equals("open")) {
                        patient.setOccupation(new String(chars, start, length));
                    } else if (addressTag.equals("open")) {
                        patient.setAddress(scrambler.scrambleCharacters(new String(chars, start, length)));
                    } else if (phoneTag.equals("open")) {
                        patient.setPhone(scrambler.scrambleNumbers(new String(chars, start, length)));
                    } else if (stateTag.equals("open")) {
                    } else if (nextKinTag.equals("open")) {
                        patient.setNextKin(scrambler.scrambleCharacters(new String(chars, start, length)));
                    } else if (addressKinTag.equals("open")) {
                        patient.setAddressKin(scrambler.scrambleCharacters(new String(chars, start, length)));
                    } else if (phoneKinTag.equals("open")) {
                        patient.setPhoneKin(scrambler.scrambleNumbers(new String(chars, start, length)));
                    } else if (relationKinTag.equals("open")) {
                        patient.setRelationKin(new String(chars, start, length));
                    } else if (entryPointTag.equals("open")) {
                        patient.setEntryPoint(new String(chars, start, length));
                    } else if (dateConfirmedHivTag.equals("open")) {
                        String dateConfirmedHiv = new String(chars, start, length);
                        if (!dateConfirmedHiv.trim().isEmpty()) {
                            patient.setDateConfirmedHiv(LocalDate.parse(dateConfirmedHiv, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    } else if (tbStatusTag.equals("open")) {
                        patient.setTbStatus(new String(chars, start, length));
                    } else if (pregnantTag.equals("open")) {
                        String pregnant = new String(chars, start, length);
                        if (!pregnant.trim().isEmpty()) {
                            if (StringUtils.isNumeric(pregnant))
                                patient.setPregnant(true);
                            else patient.setPregnant(false);
                        }
                    } else if (breastfeedingTag.equals("open")) {
                        String breastfeeding = new String(chars, start, length);
                        if (!breastfeeding.trim().isEmpty()) {
                            if (breastfeeding.equals("1"))
                                patient.setBreastfeeding(true);
                            else patient.setBreastfeeding(false);
                        }
                    } else if (dateRegistrationTag.equals("open")) {
                        dateRegistration = new String(chars, start, length);
                        if (!dateRegistration.trim().isEmpty()) {
                            patient.setDateRegistration(LocalDate.parse(dateRegistration, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    } else if (statusRegistrationTag.equals("open")) {
                        statusRegistration = new String(chars, start, length);
                        patient.setStatusRegistration(statusRegistration);
                    } else if (enrollmentSettingTag.equals("open")) {
                        String enrollmentSetting = new String(chars, start, length);
                        patient.setEnrollmentSetting(enrollmentSetting);
                    } else if (dateStartedTag.equals("open")) {
                        String dateStarted = new String(chars, start, length);
                        if (!dateStarted.trim().isEmpty()) {
                            patient.setDateStarted(LocalDate.parse(dateStarted, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    } else if (currentStatusTag.equals("open")) {
                        String currentStatus = new String(chars, start, length);
                        if (!currentStatus.trim().isEmpty()) {
                            patient.setCurrentStatus(currentStatus);
                        } else {
                            patient.setCurrentStatus(statusRegistration);
                        }
                    } else if (dateCurrentStatusTag.equals("open")) {
                        String dateCurrentStatus = new String(chars, start, length);
                        if (!dateCurrentStatus.trim().isEmpty()) {
                            patient.setDateCurrentStatus(LocalDate.parse(dateCurrentStatus, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        } else {
                            patient.setDateCurrentStatus(LocalDate.parse(dateRegistration, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    } else if (regimentypeTag.equals("open")) {
                        patient.setRegimentype(new String(chars, start, length));
                    } else if (regimenTag.equals("open")) {
                        patient.setRegimen(new String(chars, start, length));
                    } else if (dateLastRefillTag.equals("open")) {
                        String dateLastRefill = new String(chars, start, length);
                        if (!dateLastRefill.trim().isEmpty()) {
                            patient.setDateLastRefill(LocalDate.parse(dateLastRefill, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    } else if (dateNextRefillTag.equals("open")) {
                        String dateNextRefill = new String(chars, start, length);
                        if (!dateNextRefill.trim().isEmpty()) {
                            patient.setDateNextRefill(LocalDate.parse(dateNextRefill, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    } else if (lastRefillDurationTag.equals("open")) {
                        String lastRefillDuration = new String(chars, start, length);
                        if (!lastRefillDuration.trim().isEmpty()) {
                            if (StringUtils.isNumeric(lastRefillDuration))
                                patient.setLastRefillDuration(Integer.parseInt(lastRefillDuration));
                        } else {
                            patient.setLastRefillDuration(0);
                        }
                    } else if (lastRefillSettingTag.equals("open")) {
                        patient.setLastRefillSetting(new String(chars, start, length));
                    } else if (dateLastClinicTag.equals("open")) {
                        String dateLastClinic = new String(chars, start, length);
                        if (!dateLastClinic.trim().isEmpty()) {
                            patient.setDateLastClinic(LocalDate.parse(dateLastClinic, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    } else if (dateNextClinicTag.equals("open")) {
                        String dateNextClinic = new String(chars, start, length);
                        if (!dateNextClinic.trim().isEmpty()) {
                            patient.setDateNextClinic(LocalDate.parse(dateNextClinic, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    } else if (lastCd4Tag.equals("open")) {
                        String lastCd4 = new String(chars, start, length);
                        if (!lastCd4.trim().isEmpty()) {
                            patient.setLastCd4(Double.parseDouble(lastCd4));
                        }
                    } else if (lastCd4pTag.equals("open")) {
                        String lastCd4p = new String(chars, start, length);
                        if (!lastCd4p.trim().isEmpty()) {
                            patient.setLastCd4p(Double.parseDouble(lastCd4p));
                        }
                    } else if (dateLastCd4Tag.equals("open")) {
                        String dateLastCd4 = new String(chars, start, length);
                        if (!dateLastCd4.trim().isEmpty()) {
                            patient.setDateLastCd4(LocalDate.parse(dateLastCd4, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    } else if (lastClinicStageTag.equals("open")) {
                        String lastClinicStage = new String(chars, start, length) == null ? "" : new String(chars, start, length);
                        patient.setLastClinicStage(lastClinicStage);
                    } else if (dateTrackedTag.equals("open")) {
                        String dateTracked = new String(chars, start, length);
                        if (!dateTracked.trim().isEmpty()) {
                            patient.setDateTracked(LocalDate.parse(dateTracked, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    } else if (outcomeTag.equals("open")) {
                        patient.setOutcome(new String(chars, start, length));
                    } else if (causeDeathTag.equals("open")) {
                        patient.setCauseDeath(new String(chars, start, length));
                    } else if (agreedDateTag.equals("open")) {
                        String agreedDate = new String(chars, start, length);
                        if (!agreedDate.trim().isEmpty()) {
                            patient.setAgreedDate(LocalDate.parse(agreedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    } else if (lastViralLoadTag.equals("open")) {
                        String lastViralLoad = new String(chars, start, length);
                        if (!lastViralLoad.trim().isEmpty()) {
                            patient.setLastViralLoad(Double.parseDouble(lastViralLoad));
                        }
                    } else if (dateLastViralLoadTag.equals("open")) {
                        String dateLastViralLoad = new String(chars, start, length);
                        if (!dateLastViralLoad.trim().isEmpty()) {
                            patient.setDateLastViralLoad(LocalDate.parse(dateLastViralLoad, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    } else if (viralLoadDueDateTag.equals("open")) {
                        String viralLoadDueDate = new String(chars, start, length);
                        patient.setViralLoadDueDate(LocalDate.parse(viralLoadDueDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    } else if (viralLoadTypeTag.equals("open")) {
                        patient.setViralLoadType(new String(chars, start, length));
                    } else if (sendMessageTag.equals("open")) {
                        String sendMessage = new String(chars, start, length);
                        if (!sendMessage.trim().isEmpty()) {
                            if (sendMessage.equals("1"))
                                patient.setSendMessage(true);
                            else patient.setSendMessage(false);
                        }
                    } else if (communitypharmIdTag.equals("open")) {
                        String id = new String(chars, start, length);
                    } else if (caseManagerIdTag.equals("open")) {
                        String id = new String(chars, start, length);
                    } else if (userIdTag.equals("open")) {
                        String id = new String(chars, start, length);
                        if (StringUtils.isNumeric(id)) {
                        }
                    } else if (idUUIDTag.equals("open")) {
                        uuid = new String(chars, start, length);
                        patient.setUuid(uuid);
                    }
                    if (facilityIdTag.equals("open")) {
                        facilityId = Long.parseLong(new String(chars, start, length));
                        Facility facility = new Facility();
                        facility.setId(facilityId);
                        patient.setFacility(facility);
                    }
                }

                @Override
                public void endElement(String uri, String localName, String element) throws SAXException {
                    if (element.equalsIgnoreCase("patient_id")) {
                        patientIdTag = "close";
                    } else if (element.equalsIgnoreCase("facility_id")) {
                        facilityIdTag = "close";
                    } else if (element.equalsIgnoreCase("hospital_num")) {
                        hospitalNumTag = "close";
                    } else if (element.equalsIgnoreCase("unique_id")) {
                        uniqueIdTag = "close";
                    } else if (element.equalsIgnoreCase("surname")) {
                        surnameTag = "close";
                    } else if (element.equalsIgnoreCase("other_names")) {
                        otherNamesTag = "close";
                    } else if (element.equalsIgnoreCase("gender")) {
                        genderTag = "close";
                    } else if (element.equalsIgnoreCase("date_birth")) {
                        dateBirthTag = "close";
                    } else if (element.equalsIgnoreCase("age")) {
                        ageTag = "close";
                    } else if (element.equalsIgnoreCase("age_unit")) {
                        ageUnitTag = "close";
                    } else if (element.equalsIgnoreCase("marital_status")) {
                        maritalStatusTag = "close";
                    } else if (element.equalsIgnoreCase("education")) {
                        educationTag = "close";
                    } else if (element.equalsIgnoreCase("occupation")) {
                        occupationTag = "close";
                    } else if (element.equalsIgnoreCase("address")) {
                        addressTag = "close";
                    } else if (element.equalsIgnoreCase("phone")) {
                        phoneTag = "close";
                    } else if (element.equalsIgnoreCase("state")) {
                        stateTag = "close";
                    } else if (element.equalsIgnoreCase("lga")) {
                        lgaTag = "close";
                    } else if (element.equalsIgnoreCase("next_kin")) {
                        nextKinTag = "close";
                    } else if (element.equalsIgnoreCase("address_kin")) {
                        addressKinTag = "close";
                    }
                    if (element.equalsIgnoreCase("facility_id")) {
                        facilityIdTag = "close";
                    } else if (element.equalsIgnoreCase("phone_kin")) {
                        phoneKinTag = "close";
                    } else if (element.equalsIgnoreCase("relation_kin")) {
                        relationKinTag = "close";
                    } else if (element.equalsIgnoreCase("target_group")) {
                        targetGroupTag = "close";
                    } else if (element.equalsIgnoreCase("entry_point")) {
                        entryPointTag = "close";
                    } else if (element.equalsIgnoreCase("date_confirmed_hiv")) {
                        dateConfirmedHivTag = "close";
                    } else if (element.equalsIgnoreCase("tb_status")) {
                        tbStatusTag = "close";
                    } else if (element.equalsIgnoreCase("pregnant")) {
                        pregnantTag = "close";
                    } else if (element.equalsIgnoreCase("breastfeeding")) {
                        breastfeedingTag = "close";
                    } else if (element.equalsIgnoreCase("date_registration")) {
                        dateRegistrationTag = "close";
                    } else if (element.equalsIgnoreCase("status_registration")) {
                        statusRegistrationTag = "close";
                    } else if (element.equalsIgnoreCase("enrollment_setting")) {
                        enrollmentSettingTag = "close";
                    } else if (element.equalsIgnoreCase("date_started")) {
                        dateStartedTag = "close";
                    } else if (element.equalsIgnoreCase("current_status")) {
                        currentStatusTag = "close";
                    } else if (element.equalsIgnoreCase("date_current_status")) {
                        dateCurrentStatusTag = "close";
                    } else if (element.equalsIgnoreCase("regimentype")) {
                        regimentypeTag = "close";
                    } else if (element.equalsIgnoreCase("regimen")) {
                        regimenTag = "close";
                    } else if (element.equalsIgnoreCase("date_last_refill")) {
                        dateLastRefillTag = "close";
                    } else if (element.equalsIgnoreCase("date_next_refill")) {
                        dateNextRefillTag = "close";
                    } else if (element.equalsIgnoreCase("last_refill_duration")) {
                        lastRefillDurationTag = "close";
                    } else if (element.equalsIgnoreCase("last_refill_setting")) {
                        lastRefillSettingTag = "close";
                    } else if (element.equalsIgnoreCase("date_last_clinic")) {
                        dateLastClinicTag = "close";
                    } else if (element.equalsIgnoreCase("date_next_clinic")) {
                        dateNextClinicTag = "close";
                    } else if (element.equalsIgnoreCase("last_clinic_stage")) {
                        lastClinicStageTag = "close";
                    } else if (element.equalsIgnoreCase("last_cd4")) {
                        lastCd4Tag = "close";
                    } else if (element.equalsIgnoreCase("last_cd4p")) {
                        lastCd4pTag = "close";
                    } else if (element.equalsIgnoreCase("date_last_cd4")) {
                        dateLastCd4Tag = "close";
                    } else if (element.equalsIgnoreCase("last_viral_load")) {
                        lastViralLoadTag = "close";
                    } else if (element.equalsIgnoreCase("date_last_viral_load")) {
                        dateLastViralLoadTag = "close";
                    } else if (element.equalsIgnoreCase("viral_load_due_date")) {
                        viralLoadDueDateTag = "close";
                    } else if (element.equalsIgnoreCase("viral_load_type")) {
                        viralLoadTypeTag = "close";
                    } else if (element.equalsIgnoreCase("date_tracked")) {
                        dateTrackedTag = "close";
                    } else if (element.equalsIgnoreCase("outcome")) {
                        outcomeTag = "close";
                    } else if (element.equalsIgnoreCase("agreed_date")) {
                        agreedDateTag = "close";
                    } else if (element.equalsIgnoreCase("cause_death")) {
                        causeDeathTag = "close";
                    } else if (element.equalsIgnoreCase("send_message")) {
                        sendMessageTag = "close";
                    } else if (element.equalsIgnoreCase("time_stamp")) {
                        timeStampTag = "close";
                    } else if (element.equalsIgnoreCase("casemanager_id")) {
                        caseManagerIdTag = "close";
                    } else if (element.equalsIgnoreCase("communitypharm_id")) {
                        communitypharmIdTag = "close";
                    } else if (element.equalsIgnoreCase("user_id")) {
                        userIdTag = "close";
                    } else if (element.equalsIgnoreCase("id_uuid")) {
                        idUUIDTag = "close";
                    }

                    //if this is the closing tag of a patient element save the record
                    else if (element.equalsIgnoreCase("patient")) {
                        if (!skipRecord) {
                            Long patientId = ServerIDProvider.getPatientServerId(
                                    patient.getHospitalNum(), facilityId
                            );
                            patient.setPatientId(patientId);
                            try {
                                ContextProvider.getBean(PatientRepository.class).save(patient);
                            } catch (Exception ignored) {

                            }
                        }
                        patientTag = "close";
                    }
                }
            };
            //parse the XML specified in the given path and uses supplied handler to parse the document
            //this calls startElement(), endElement(), and character() methods accordingly
            saxParser.parse(xmlFileName, defaultHandler);
            // new CleanupService().cleanNullFields("patient", facilityId);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
