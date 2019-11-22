/**
 * @author aalozie
 */
package org.fhi360.lamis.service.parser.xml;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.model.*;
import org.fhi360.lamis.model.repositories.PharmacyRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PharmacyXmlParser extends DefaultHandler {
    private Long facilityId;
    private Boolean skipRecord;
    private Pharmacy pharmacy;

    public PharmacyXmlParser(Long facilityId) {
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
                String pharmacyTag = "close";
                String pharmacyIdTag = "close";
                String patientIdTag = "close";
                String facilityIdTag = "close";
                String hospitalNumTag = "close";
                String dateVisitTag = "close";
                String durationTag = "close";
                String morningTag = "close";
                String afternoonTag = "close";
                String eveningTag = "close";
                String adrScreenedTag = "close";
                String adrIdsTag = "close";
                String prescripErrorTag = "close";
                String adherenceTag = "close";
                String nextAppointmentTag = "close";
                String regimentypeIdTag = "close";
                String regimenIdTag = "close";
                String regimendrugIdTag = "close";
                String timeStampTag = "close";
                String userIdTag = "close";
                String idUUIDTag = "close";
                String id;
                String uuid;
                String hospitalNum;

                //this method is called every time the parser gets an open tag '<'
                //identifies which tag is being open at the time by assigning an open flag
                @Override
                public void startElement(String uri, String localName, String element, Attributes attributes) throws SAXException {
                    if (element.equalsIgnoreCase("pharmacy")) {
                        pharmacyTag = "open";
                        pharmacy = new Pharmacy();
                        skipRecord = false;
                    }
                    if (element.equalsIgnoreCase("facility_id")) {
                        facilityIdTag = "open";
                    }
                    if (element.equalsIgnoreCase("pharmacy_id")) {
                        pharmacyIdTag = "open";
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
                    if (element.equalsIgnoreCase("date_visit")) {
                        dateVisitTag = "open";
                    }
                    if (element.equalsIgnoreCase("duration")) {
                        durationTag = "open";
                    }
                    if (element.equalsIgnoreCase("morning")) {
                        morningTag = "open";
                    }
                    if (element.equalsIgnoreCase("afternoon")) {
                        afternoonTag = "open";
                    }
                    if (element.equalsIgnoreCase("evening")) {
                        eveningTag = "open";
                    }
                    if (element.equalsIgnoreCase("adr_screened")) {
                        adrScreenedTag = "open";
                    }
                    if (element.equalsIgnoreCase("adr_ids")) {
                        adrIdsTag = "open";
                    }
                    if (element.equalsIgnoreCase("prescrip_error")) {
                        prescripErrorTag = "open";
                    }
                    if (element.equalsIgnoreCase("adherence")) {
                        adherenceTag = "open";
                    }
                    if (element.equalsIgnoreCase("regimentype_id")) {
                        regimentypeIdTag = "open";
                    }
                    if (element.equalsIgnoreCase("regimen_id")) {
                        regimenIdTag = "open";
                    }
                    if (element.equalsIgnoreCase("regimendrug_id")) {
                        regimendrugIdTag = "open";
                    }
                    if (element.equalsIgnoreCase("next_appointment")) {
                        nextAppointmentTag = "open";
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
                    if (dateVisitTag.equals("open")) {
                        String dateVisit = new String(chars, start, length);
                        if (!dateVisit.trim().isEmpty()) {
                            pharmacy.setDateVisit(LocalDate.parse(dateVisit, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        } else {
                            skipRecord = true;
                        }
                    }
                    if (durationTag.equals("open")) {
                        String duration = new String(chars, start, length);
                        if (!duration.trim().isEmpty()) {
                            if (StringUtils.isNumeric(duration))
                                pharmacy.setDuration(Integer.parseInt(duration));
                            else skipRecord = true;
                        }
                    }
                    if (morningTag.equals("open")) {
                        String morning = new String(chars, start, length);
                        if (!morning.trim().isEmpty()) {
                            pharmacy.setMorning(Double.parseDouble(morning));
                        }
                    }
                    if (afternoonTag.equals("open")) {
                        String afternoon = new String(chars, start, length);
                        if (!afternoon.trim().isEmpty()) {
                            pharmacy.setAfternoon(Double.parseDouble(afternoon));
                        }
                    }
                    if (eveningTag.equals("open")) {
                        String evening = new String(chars, start, length);
                        if (!evening.trim().isEmpty()) {
                            pharmacy.setEvening(Double.parseDouble(evening));
                        }
                    }
                    if (adrScreenedTag.equals("open")) {
                        pharmacy.setAdrScreened(new String(chars, start, length));
                    }
                    if (adrIdsTag.equals("open")) {
                        pharmacy.setAdrIds(new String(chars, start, length));
                    }
                    if (prescripErrorTag.equals("open")) {
                        String prescripError = new String(chars, start, length);
                        if (!prescripError.trim().isEmpty()) {
                            if (StringUtils.isNumeric(prescripError))
                                pharmacy.setPrescriptionError(Integer.parseInt(prescripError));
                            else pharmacy.setPrescriptionError(0);
                        }
                    }
                    if (adherenceTag.equals("open")) {
                        String adherence = new String(chars, start, length);
                        if (!adherence.trim().equals("")) {
                            if (StringUtils.isNumeric(adherence))
                                pharmacy.setAdherence(Integer.parseInt(adherence));
                            else pharmacy.setAdherence(0);
                        }
                    }
                    if (regimentypeIdTag.equals("open")) {
                        String regimenTypeId = new String(chars, start, length);
                        if (!regimenTypeId.trim().isEmpty()) {
                            if (StringUtils.isNumeric(regimenTypeId)) {
                                RegimenType regimenType = new RegimenType();
                                regimenType.setId(Long.parseLong(regimenTypeId));
                                pharmacy.setRegimenType(regimenType);
                            } else skipRecord = true;
                        }
                    }
                    if (regimenIdTag.equals("open")) {
                        String regimenId = new String(chars, start, length);
                        if (!regimenId.trim().isEmpty()) {
                            if (StringUtils.isNumeric(regimenId)) {
                                Regimen regimen = new Regimen();
                                regimen.setId(Long.parseLong(regimenId));
                                pharmacy.setRegimen(regimen);
                            } else skipRecord = true;
                        }
                    }
                    if (regimendrugIdTag.equals("open")) {
                        String regimenDrugId = new String(chars, start, length);
                        if (!regimenDrugId.trim().isEmpty()) {
                            pharmacy.setRegimendrugId(Long.parseLong(regimenDrugId));
                        } else {
                            skipRecord = true;
                        }
                    }
                    if (nextAppointmentTag.equals("open")) {
                        String nextAppointment = new String(chars, start, length);
                        if (!nextAppointment.trim().isEmpty()) {
                            pharmacy.setNextAppointment(LocalDate.parse(nextAppointment, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    }
                    if (idUUIDTag.equals("open")) {
                        uuid = new String(chars, start, length);
                        pharmacy.setUuid(uuid);
                    }
                    if (facilityIdTag.equals("open")) {
                        facilityId = Long.parseLong(new String(chars, start, length));
                        Facility facility = new Facility();
                        facility.setId(facilityId);
                        pharmacy.setFacility(facility);
                    }
                }

                @Override
                public void endElement(String uri, String localName, String element) throws SAXException {
                    if (element.equalsIgnoreCase("pharmacy_id")) {
                        pharmacyIdTag = "close";
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
                    if (element.equalsIgnoreCase("date_visit")) {
                        dateVisitTag = "close";
                    }
                    if (element.equalsIgnoreCase("duration")) {
                        durationTag = "close";
                    }
                    if (element.equalsIgnoreCase("morning")) {
                        morningTag = "close";
                    }
                    if (element.equalsIgnoreCase("afternoon")) {
                        afternoonTag = "close";
                    }
                    if (element.equalsIgnoreCase("evening")) {
                        eveningTag = "close";
                    }
                    if (element.equalsIgnoreCase("adr_screened")) {
                        adrScreenedTag = "close";
                    }
                    if (element.equalsIgnoreCase("adr_ids")) {
                        adrIdsTag = "close";
                    }
                    if (element.equalsIgnoreCase("prescrip_error")) {
                        prescripErrorTag = "close";
                    }
                    if (element.equalsIgnoreCase("adherence")) {
                        adherenceTag = "close";
                    }
                    if (element.equalsIgnoreCase("regimentype_id")) {
                        regimentypeIdTag = "close";
                    }
                    if (element.equalsIgnoreCase("regimen_id")) {
                        regimenIdTag = "close";
                    }
                    if (element.equalsIgnoreCase("regimendrug_id")) {
                        regimendrugIdTag = "close";
                    }
                    if (element.equalsIgnoreCase("next_appointment")) {
                        nextAppointmentTag = "close";
                    }
                    if (element.equalsIgnoreCase("time_stamp")) {
                        timeStampTag = "close";
                    }
                    if (element.equalsIgnoreCase("user_id")) {
                        userIdTag = "close";
                    }
                    if (element.equalsIgnoreCase("id_uuid")) {
                        idUUIDTag = "close";
                    }

                    //if this is the closing tag of a pharmacy element save the record
                    if (element.equalsIgnoreCase("pharmacy")) {
                        pharmacyTag = "close";
                        if (skipRecord) {
                            System.out.println("....record has a null value: " + hospitalNum);
                        } else {
                            final Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, facilityId);
                            if (patientId != null) {
                                Patient patient = new Patient();
                                patient.setPatientId(patientId);
                                pharmacy.setPatient(patient);
                                Long id = ServerIDProvider
                                        .getPharmacyId(hospitalNum, pharmacy.getDateVisit(),
                                                pharmacy.getRegimendrugId(), facilityId);
                                pharmacy.setPharmacyId(id);
                                try {
                                    ContextProvider.getBean(PharmacyRepository.class).save(pharmacy);
                                } catch (Exception ignored) {

                                }
                            }
                        }
                    }
                }
            };

            //parse the XML specified in the given path and uses supplied handler to parse the document
            //this calls startElement(), endElement(), and character() methods accordingly
            saxParser.parse(xmlFileName, defaultHandler);
            // new CleanupService().cleanNullFields("pharmacy", facilityId);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
