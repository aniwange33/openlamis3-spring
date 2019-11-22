/**
 * @author aalozie
 */
package org.fhi360.lamis.service.parser.xml;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.model.Anc;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.AncRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AncXmlParser extends DefaultHandler {

    private Long facilityId;
    private Boolean skipRecord;
    private Anc anc;

    public AncXmlParser(Long facilityId) {
        this.facilityId = facilityId;
    }

    public void parseXml(String xmlFileName) {
        skipRecord = false;
        try {
            //obtain and configure a SAX based parser
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

            //obtain object for SAX hadler class
            SAXParser saxParser = saxParserFactory.newSAXParser();

            //default handler for SAX handler class
            // all three methods are written in handler's body
            DefaultHandler defaultHandler = new DefaultHandler() {
                String ancTag = "close";
                String ancIdTag = "close";
                String patientIdTag = "close";
                String facilityIdTag = "close";
                String hospitalNumTag = "close";
                String ancNumTag = "close";
                String uniqueIdTag = "close";
                String dateVisitTag = "close";
                String dateEnrolledPmtctTag = "close";
                String sourceReferralTag = "close";
                String lmpTag = "close";
                String eddTag = "close";
                String gestationalAgeTag = "close";
                String gravidaTag = "close";
                String parityTag = "close";
                String timeHivDiagnosisTag = "close";
                String arvRegimenPastTag = "close";
                String arvRegimenCurrentTag = "close";
                String dateArvRegimenCurrentTag = "close";
                String dateConfirmedHivTag = "close";
                String clinicStageTag = "close";
                String funcStatusTag = "close";
                String syphilisTestedTag = "close";
                String syphilisTestResultTag = "close";
                String syphilisTreatedTag = "close";
                String bodyWeightTag = "close";
                String cd4OrderedTag = "close";
                String cd4Tag = "close";
                String timeStampTag = "close";
                String userIdTag = "close";
                String idUUIDTag = "close";
                String hospitalNum;
                String dateVisit;
                String ancId;
                String uuid;

                //this method is called every time the parser gets an open tag '<'
                //identifies which tag is being open at the time by assigning an open flag
                @Override
                public void startElement(String uri, String localName, String element, Attributes attributes) throws SAXException {
                    if (element.equalsIgnoreCase("anc")) {
                        ancTag = "open";
                        anc = new Anc();
                        skipRecord = false;
                    }
                    if (element.equalsIgnoreCase("facility_id")) {
                        facilityIdTag = "open";
                    }
                    if (element.equalsIgnoreCase("anc_id")) {
                        ancIdTag = "open";
                    }
                    if (element.equalsIgnoreCase("patient_id")) {
                        patientIdTag = "open";
                    }
                    if (element.equalsIgnoreCase("facility_id")) {
                        facilityIdTag = "open";
                    }
                    if (element.equalsIgnoreCase("hospital_num")) {
                        hospitalNumTag = "open";
                    }
                    if (element.equalsIgnoreCase("anc_num")) {
                        ancNumTag = "open";
                    }
                    if (element.equalsIgnoreCase("unique_id")) {
                        uniqueIdTag = "open";
                    }
                    if (element.equalsIgnoreCase("date_visit")) {
                        dateVisitTag = "open";
                    }
                    if (element.equalsIgnoreCase("date_enrolled_pmtct")) {
                        dateEnrolledPmtctTag = "open";
                    }
                    if (element.equalsIgnoreCase("source_referral")) {
                        sourceReferralTag = "open";
                    }
                    if (element.equalsIgnoreCase("lmp")) {
                        lmpTag = "open";
                    }
                    if (element.equalsIgnoreCase("edd")) {
                        eddTag = "open";
                    }
                    if (element.equalsIgnoreCase("gestational_age")) {
                        gestationalAgeTag = "open";
                    }
                    if (element.equalsIgnoreCase("gravida")) {
                        gravidaTag = "open";
                    }
                    if (element.equalsIgnoreCase("parity")) {
                        parityTag = "open";
                    }
                    if (element.equalsIgnoreCase("time_hiv_diagnosis")) {
                        timeHivDiagnosisTag = "open";
                    }
                    if (element.equalsIgnoreCase("arv_regimen_past")) {
                        arvRegimenPastTag = "open";
                    }
                    if (element.equalsIgnoreCase("arv_regimen_current")) {
                        arvRegimenCurrentTag = "open";
                    }
                    if (element.equalsIgnoreCase("date_arv_regimen_current")) {
                        dateArvRegimenCurrentTag = "open";
                    }
                    if (element.equalsIgnoreCase("clinic_stage")) {
                        clinicStageTag = "open";
                    }
                    if (element.equalsIgnoreCase("date_confirmed_hiv")) {
                        dateConfirmedHivTag = "open";
                    }
                    if (element.equalsIgnoreCase("func_status")) {
                        funcStatusTag = "open";
                    }
                    if (element.equalsIgnoreCase("syphilis_tested")) {
                        syphilisTestedTag = "open";
                    }
                    if (element.equalsIgnoreCase("syphilis_test_result")) {
                        syphilisTestResultTag = "open";
                    }
                    if (element.equalsIgnoreCase("syphilis_treated")) {
                        syphilisTreatedTag = "open";
                    }
                    if (element.equalsIgnoreCase("body_weight")) {
                        bodyWeightTag = "open";
                    }
                    if (element.equalsIgnoreCase("cd4_ordered")) {
                        cd4OrderedTag = "open";
                    }
                    if (element.equalsIgnoreCase("cd4")) {
                        cd4Tag = "open";
                    }
                    if (element.equalsIgnoreCase("time_stamp")) {
                        timeStampTag = "open";
                    }
                    if (element.equalsIgnoreCase("user_id")) {
                        userIdTag = "open";
                    }
                    if (element.equalsIgnoreCase("id_uuid")) {
                        idUUIDTag = "open";
                    }
                }

                //store data store in between '<' and '>' tags                     
                @Override
                public void characters(char[] chars, int start, int length) throws SAXException {
                    if (hospitalNumTag.equals("open")) {
                        hospitalNum = new String(chars, start, length);
                    }
                    if (facilityIdTag.equals("open")) {
                        facilityId = Long.parseLong(new String(chars, start, length));
                        anc.setFacilityId(facilityId);
                    }
                    if (ancNumTag.equals("open")) {
                        String ancNum = new String(chars, start, length);
                        if (ancNum.isEmpty()) {
                            anc.setAncNum(hospitalNum);
                        } else {
                            anc.setAncNum(ancNum);
                        }
                    }
                    if (uniqueIdTag.equals("open")) {
                        anc.setUniqueId(new String(chars, start, length));
                    }
                    if (dateVisitTag.equals("open")) {
                        dateVisit = new String(chars, start, length);
                        if (!dateVisit.trim().isEmpty()) {
                            anc.setDateVisit(LocalDate.parse(dateVisit, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        } else {
                            skipRecord = true;
                        }
                    }
                    if (dateEnrolledPmtctTag.equals("open")) {
                        String dateEnrolledPmtct = new String(chars, start, length);
                        if (!dateEnrolledPmtct.trim().isEmpty()) {
                            anc.setDateEnrolledPMTCT(LocalDate.parse(dateEnrolledPmtct, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    }
                    if (sourceReferralTag.equals("open")) {
                        anc.setSourceReferral(new String(chars, start, length));
                    }
                    if (lmpTag.equals("open")) {
                        String lmp = new String(chars, start, length);
                        if (!lmp.trim().isEmpty()) {
                            anc.setLmp(LocalDate.parse(lmp, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        } else {
                            skipRecord = true;
                        }
                    }
                    if (eddTag.equals("open")) {
                        String edd = new String(chars, start, length);
                        if (!edd.trim().isEmpty()) {
                            anc.setEdd(LocalDate.parse(edd, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    }
                    if (gestationalAgeTag.equals("open")) {
                        String gestationalAge = new String(chars, start, length);
                        if (!gestationalAge.trim().isEmpty()) {
                            if (StringUtils.isNumeric(gestationalAge))
                                anc.setGestationalAge(Integer.parseInt(gestationalAge));
                            else skipRecord = true;
                        }
                    }
                    if (gravidaTag.equals("open")) {
                        String gravida = new String(chars, start, length);
                        if (!gravida.trim().isEmpty()) {
                            if (StringUtils.isNumeric(gravida))
                                anc.setGravida(Integer.parseInt(gravida));
                            else skipRecord = true;
                        }
                    }
                    if (parityTag.equals("open")) {
                        String parity = new String(chars, start, length);
                        if (!parity.trim().isEmpty()) {
                            if (StringUtils.isNumeric(parity))
                                anc.setParity(Integer.parseInt(parity));
                            else skipRecord = true;
                        }
                    }
                    if (timeHivDiagnosisTag.equals("open")) {
                        anc.setTimeHivDiagnosis(new String(chars, start, length));
                    }
                    if (arvRegimenPastTag.equals("open")) {
                        anc.setArvRegimenPast(new String(chars, start, length));
                    }
                    if (arvRegimenCurrentTag.equals("open")) {
                        anc.setArvRegimenCurrent(new String(chars, start, length));
                    }
                    if (dateArvRegimenCurrentTag.equals("open")) {
                        String dateArvRegimenCurrent = new String(chars, start, length);
                        if (!dateArvRegimenCurrent.trim().isEmpty()) {
                            anc.setDateArvRegimenCurrent(LocalDate.parse(dateArvRegimenCurrent, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    }
                    if (dateConfirmedHivTag.equals("open")) {
                        String dateConfirmedHiv = new String(chars, start, length);
                        if (!dateConfirmedHiv.trim().isEmpty()) {
                            anc.setDateConfirmedHiv(LocalDate.parse(dateConfirmedHiv, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    }
                    if (syphilisTestedTag.equals("open")) {
                        anc.setSyphilisTested(new String(chars, start, length));
                    }
                    if (syphilisTestResultTag.equals("open")) {
                        anc.setSyphilisTestResult(new String(chars, start, length));
                    }
                    if (syphilisTreatedTag.equals("open")) {
                        anc.setSyphilisTreated(new String(chars, start, length));
                    }
                    if (bodyWeightTag.equals("open")) {
                        String bodyWeight = new String(chars, start, length);
                        if (!bodyWeight.trim().isEmpty()) {
                                anc.setBodyWeight(Double.parseDouble(bodyWeight));
                        }
                    }
                    if (clinicStageTag.equals("open")) {
                        anc.setClinicStage(new String(chars, start, length));
                    }
                    if (funcStatusTag.equals("open")) {
                        anc.setFuncStatus(new String(chars, start, length));
                    }
                    if (cd4OrderedTag.equals("open")) {
                        anc.setCd4Ordered(new String(chars, start, length));
                    }
                    if (cd4Tag.equals("open")) {
                        String cd4 = new String(chars, start, length);
                        if (!cd4.trim().isEmpty()) {
                                anc.setCd4(Double.parseDouble(cd4));
                        }
                    }
                    if (idUUIDTag.equals("open")) {
                        uuid = new String(chars, start, length);
                        anc.setUuid(uuid);
                    }
                }

                @Override
                public void endElement(String uri, String localName, String element) throws SAXException {
                    if (element.equalsIgnoreCase("anc_id")) {
                        ancIdTag = "close";
                    }
                    if (element.equalsIgnoreCase("patient_id")) {
                        patientIdTag = "close";
                    }
                    if (element.equalsIgnoreCase("facility_id")) {
                        facilityIdTag = "close";
                    }
                    if (element.equalsIgnoreCase("hospital_num")) {
                        hospitalNumTag = "close";
                    }
                    if (element.equalsIgnoreCase("anc_num")) {
                        ancNumTag = "close";
                    }
                    if (element.equalsIgnoreCase("unique_id")) {
                        uniqueIdTag = "close";
                    }
                    if (element.equalsIgnoreCase("date_visit")) {
                        dateVisitTag = "close";
                    }
                    if (element.equalsIgnoreCase("date_enrolled_pmtct")) {
                        dateEnrolledPmtctTag = "close";
                    }
                    if (element.equalsIgnoreCase("source_referral")) {
                        sourceReferralTag = "close";
                    }
                    if (element.equalsIgnoreCase("lmp")) {
                        lmpTag = "close";
                    }
                    if (element.equalsIgnoreCase("edd")) {
                        eddTag = "close";
                    }
                    if (element.equalsIgnoreCase("gestational_age")) {
                        gestationalAgeTag = "close";
                    }
                    if (element.equalsIgnoreCase("gravida")) {
                        gravidaTag = "close";
                    }
                    if (element.equalsIgnoreCase("parity")) {
                        parityTag = "close";
                    }
                    if (element.equalsIgnoreCase("time_hiv_diagnosis")) {
                        timeHivDiagnosisTag = "close";
                    }
                    if (element.equalsIgnoreCase("arv_regimen_past")) {
                        arvRegimenPastTag = "close";
                    }
                    if (element.equalsIgnoreCase("arv_regimen_current")) {
                        arvRegimenCurrentTag = "close";
                    }
                    if (element.equalsIgnoreCase("date_arv_regimen_current")) {
                        dateArvRegimenCurrentTag = "close";
                    }
                    if (element.equalsIgnoreCase("date_confirmed_hiv")) {
                        dateConfirmedHivTag = "close";
                    }
                    if (element.equalsIgnoreCase("clinic_stage")) {
                        clinicStageTag = "close";
                    }
                    if (element.equalsIgnoreCase("func_status")) {
                        funcStatusTag = "close";
                    }
                    if (element.equalsIgnoreCase("syphilis_tested")) {
                        syphilisTestedTag = "close";
                    }
                    if (element.equalsIgnoreCase("syphilis_test_result")) {
                        syphilisTestResultTag = "close";
                    }
                    if (element.equalsIgnoreCase("syphilis_treated")) {
                        syphilisTreatedTag = "close";
                    }
                    if (element.equalsIgnoreCase("body_weight")) {
                        bodyWeightTag = "close";
                    }
                    if (element.equalsIgnoreCase("cd4_ordered")) {
                        cd4OrderedTag = "close";
                    }
                    if (element.equalsIgnoreCase("cd4")) {
                        cd4Tag = "close";
                    }
                    if (element.equalsIgnoreCase("time_stamp")) {
                        timeStampTag = "close";
                    }
                    if (element.equalsIgnoreCase("user_id")) {
                        userIdTag = "close";
                    }
                    if (element.equals("id_uuid")) {
                        idUUIDTag = "close";
                    }
                    //if this is the closing tag of a patient element save the record
                    if (element.equalsIgnoreCase("anc")) {
                        ancTag = "close";
                        if (skipRecord) {
                            System.out.println("....record has a null value: " + hospitalNum);
                        } else {
                            Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, facilityId);
                            if (patientId != null) {
                                Patient patient = new Patient();
                                patient.setPatientId(patientId);
                                anc.setPatient(patient);
                                Long id = ServerIDProvider
                                        .getPatientDependantId("anc", hospitalNum,
                                                anc.getDateVisit(), facilityId);
                                anc.setAncId(id);
                                try {
                                    ContextProvider.getBean(AncRepository.class).save(anc);
                                }catch (Exception ignored) {

                                }
                            }
                        }
                    }
                }
            };

            //parse the XML specified in the given path and uses supplied handler to parse the document
            //this calls startElement(), endElement(), and character() methods accordingly
            saxParser.parse(xmlFileName, defaultHandler);
            //new CleanupService().cleanNullFields("anc", facilityId);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
