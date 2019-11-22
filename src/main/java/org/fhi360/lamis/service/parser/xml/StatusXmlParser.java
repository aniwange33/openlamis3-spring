/**
 * @author aalozie
 */
package org.fhi360.lamis.service.parser.xml;

import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.StatusHistory;
import org.fhi360.lamis.model.repositories.StatusHistoryRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.fhi360.lamis.utility.DateUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StatusXmlParser extends DefaultHandler {

    private Long facilityId;
    private Boolean skipRecord;
    private StatusHistory statushistory;

    public StatusXmlParser(Long facilityId) {
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
                String statushistoryTag = "close";
                String historyIdTag = "close";
                String patientIdTag = "close";
                String facilityIdTag = "close";
                String hospitalNumTag = "close";
                String currentStatusTag = "close";
                String dateCurrentStatusTag = "close";
                String dateTrackedTag = "close";
                String agreedDateTag = "close";
                String outcomeTag = "close";
                String reasonInterruptTag = "close";
                String causeDeathTag = "close";
                String timeStampTag = "close";
                String idUUIDTag = "close";
                String hospitalNum;
                String currentStatus;
                String uuid;

                //this method is called every time the parser gets an open tag '<'
                //identifies which tag is being open at the time by assigning an open flag
                @Override
                public void startElement(String uri, String localName, String element, Attributes attributes) throws SAXException {
                    if (element.equalsIgnoreCase("statushistory")) {
                        statushistoryTag = "open";
                        statushistory = new StatusHistory();
                        skipRecord = false;
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
                    if (element.equalsIgnoreCase("current_status")) {
                        currentStatusTag = "open";
                    }
                    if (element.equalsIgnoreCase("date_current_status")) {
                        dateCurrentStatusTag = "open";
                    }
                    if (element.equalsIgnoreCase("reason_interrupt")) {
                        reasonInterruptTag = "open";
                    }
                    if (element.equalsIgnoreCase("cause_death")) {
                        causeDeathTag = "open";
                    }
                    if (element.equalsIgnoreCase("agreed_date")) {
                        agreedDateTag = "open";
                    }
                    if (element.equalsIgnoreCase("outcome")) {
                        outcomeTag = "open";
                    }
                    if (element.equalsIgnoreCase("date_tracked")) {
                        dateTrackedTag = "open";
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
                        Facility facility = new Facility();
                        facility.setId(facilityId);
                        statushistory.setFacility(facility);
                    }
                    if (currentStatusTag.equals("open")) {
                        currentStatus = new String(chars, start, length);
                        statushistory.setCurrentStatus(new String(chars, start, length));
                    }
                    if (dateCurrentStatusTag.equals("open")) {
                        String dateCurrentStatus = new String(chars, start, length);
                        if (!dateCurrentStatus.trim().isEmpty()) {
                            statushistory.setDateCurrentStatus(LocalDate.parse(dateCurrentStatus, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        } else {
                            skipRecord = true;
                        }
                    }
                    if (reasonInterruptTag.equals("open")) {
                        statushistory.setReasonInterrupt(new String(chars, start, length));
                    }
                    if (causeDeathTag.equals("open")) {
                        statushistory.setCauseDeath(new String(chars, start, length));
                    }
                    if (dateTrackedTag.equals("open")) {
                        String dateTracked = new String(chars, start, length);
                        if (!dateTracked.trim().isEmpty()) {
                            statushistory.setDateTracked(LocalDate.parse(dateTracked, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    }
                    if (agreedDateTag.equals("open")) {
                        String agreedDate = new String(chars, start, length);
                        if (!agreedDate.trim().isEmpty()) {
                            statushistory.setAgreedDate(LocalDate.parse(agreedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    }
                    if (outcomeTag.equals("open")) {
                        statushistory.setOutcome(new String(chars, start, length));
                    }
                    if (idUUIDTag.equals("open")) {
                        uuid = new String(chars, start, length);
                        statushistory.setUuid(uuid);
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
                    if (element.equalsIgnoreCase("current_status")) {
                        currentStatusTag = "close";
                    }
                    if (element.equalsIgnoreCase("date_current_status")) {
                        dateCurrentStatusTag = "close";
                    }
                    if (element.equalsIgnoreCase("reason_interrupt")) {
                        reasonInterruptTag = "close";
                    }
                    if (element.equalsIgnoreCase("cause_death")) {
                        causeDeathTag = "close";
                    }
                    if (element.equalsIgnoreCase("date_tracked")) {
                        dateTrackedTag = "close";
                    }
                    if (element.equalsIgnoreCase("agreed_date")) {
                        agreedDateTag = "close";
                    }
                    if (element.equalsIgnoreCase("outcome")) {
                        outcomeTag = "close";
                    }
                    if (element.equalsIgnoreCase("time_stamp")) {
                        timeStampTag = "close";
                    }
                    if (element.equalsIgnoreCase("id_uuid")) {
                        idUUIDTag = "close";
                    }

                    //if this is the closing tag of a statushistory element save the record
                    if (element.equalsIgnoreCase("statushistory")) {
                        statushistoryTag = "close";
                        if (skipRecord) {
                            System.out.println("....record has a null value: ");
                        } else {
                            Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, facilityId);
                            if (patientId != null) {
                                Patient patient = new Patient();
                                patient.setPatientId(patientId);
                                statushistory.setPatient(patient);
                                Long id = ServerIDProvider
                                        .getStatusHistoryId(hospitalNum, statushistory.getDateCurrentStatus(),
                                                currentStatus, facilityId);
                                statushistory.setHistoryId(id);
                                try {
                                    ContextProvider.getBean(StatusHistoryRepository.class).save(statushistory);
                                }catch (Exception ignored){

                                }
                            }
                        }
                    }
                }
            };

            //parse the XML specified in the given path and uses supplied handler to parse the document
            //this calls startElement(), endElement(), and character() methods accordingly
            saxParser.parse(xmlFileName, defaultHandler);
            //new CleanupService().cleanNullFields("statushistory", facilityId);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
