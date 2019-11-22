/**
 * @author aalozie
 */
package org.fhi360.lamis.service.parser.xml;

import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.model.AdrHistory;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.AdrHistoryRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdrXmlParser extends DefaultHandler {
    private Long facilityId;
    private Boolean skipRecord;
    private AdrHistory adrhistory;

    public AdrXmlParser(Long facilityId) {
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
                String adrhistoryTag = "close";
                String historyIdTag = "close";
                String patientIdTag = "close";
                String facilityIdTag = "close";
                String hospitalNumTag = "close";
                String dateVisitTag = "close";
                String adrTag = "close";
                String severityTag = "close";
                String screenerTag = "close";
                String timeStampTag = "close";
                String idUUIDTag = "close";
                String uuid;
                String historyId;
                String hospitalNum;

                //this method is called every time the parser gets an open tag '<'
                //identifies which tag is being open at the time by assigning an open flag
                @Override
                public void startElement(String uri, String localName, String element, Attributes attributes) throws SAXException {
                    if (element.equalsIgnoreCase("adrhistory")) {
                        adrhistoryTag = "open";
                        adrhistory = new AdrHistory();
                        skipRecord = false;
                    }
                    if (element.equalsIgnoreCase("facility_id")) {
                        facilityIdTag = "open";
                    }
                    if (element.equalsIgnoreCase("history_id")) {
                        historyIdTag = "open";
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
                    if (element.equalsIgnoreCase("adr")) {
                        adrTag = "open";
                    }
                    if (element.equalsIgnoreCase("severity")) {
                        severityTag = "open";
                    }
                    if (element.equalsIgnoreCase("screener")) {
                        screenerTag = "open";
                    }
                    if (element.equalsIgnoreCase("time_stamp")) {
                        timeStampTag = "open";
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
                        adrhistory.setFacilityId(facilityId);
                    }
                    if (dateVisitTag.equals("open")) {
                        String dateVisit = new String(chars, start, length);
                        if (!dateVisit.trim().isEmpty()) {
                            adrhistory.setDateVisit(LocalDate.parse(dateVisit, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        } else {
                            skipRecord = true;
                        }
                    }
                    if (adrTag.equals("open")) {
                        String adr = new String(chars, start, length);
                        adrhistory.setAdr(adr);
                    }
                    if (severityTag.equals("open")) {
                        String severity = new String(chars, start, length);
                        if (!severity.trim().isEmpty()) adrhistory.setSeverity(Integer.parseInt(severity));
                    }
                    if (screenerTag.equals("open")) {
                        String screener = new String(chars, start, length);
                        if (!screener.trim().isEmpty()) adrhistory.setScreener(Integer.parseInt(screener));
                    }
                    if (idUUIDTag.equals("open")) {
                        uuid = new String(chars, start, length);
                        adrhistory.setUuid(uuid);
                    }
                }

                @Override
                public void endElement(String uri, String localName, String element) throws SAXException {
                    if (element.equalsIgnoreCase("history_id")) {
                        historyIdTag = "close";
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
                    if (element.equalsIgnoreCase("adr")) {
                        adrTag = "close";
                    }
                    if (element.equalsIgnoreCase("severity")) {
                        severityTag = "close";
                    }
                    if (element.equalsIgnoreCase("screener")) {
                        screenerTag = "close";
                    }
                    if (element.equalsIgnoreCase("time_stamp")) {
                        timeStampTag = "close";
                    }
                    if (element.equalsIgnoreCase("id_uuid")) {
                        idUUIDTag = "close";
                    }

                    //if this is the closing tag of a patient element save the record
                    if (element.equalsIgnoreCase("adrhistory")) {
                        adrhistoryTag = "close";
                        if (skipRecord) {
                            System.out.println("....record has a null value: " + hospitalNum);
                        } else {
                            Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, facilityId);
                            if (patientId != null) {
                                Patient patient = new Patient();
                                patient.setPatientId(patientId);
                                adrhistory.setPatient(patient);
                                Long id = ServerIDProvider
                                        .getPatientDependantId("adrhistory", hospitalNum,
                                                adrhistory.getDateVisit(), facilityId);
                                adrhistory.setHistoryId(id);
                                ContextProvider.getBean(AdrHistoryRepository.class).save(adrhistory);
                            }
                        }
                    }
                }
            };

            //parse the XML specified in the given path and uses supplied handler to parse the document
            //this calls startElement(), endElement(), and character() methods accordingly
            saxParser.parse(xmlFileName, defaultHandler);
            //new CleanupService().cleanNullFields("adrhistory", facilityId);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}

