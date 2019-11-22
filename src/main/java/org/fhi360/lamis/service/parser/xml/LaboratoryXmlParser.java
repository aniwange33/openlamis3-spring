/**
 * @author aalozie
 */
package org.fhi360.lamis.service.parser.xml;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.LabTest;
import org.fhi360.lamis.model.Laboratory;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.repositories.LaboratoryRepository;
import org.fhi360.lamis.service.ServerIDProvider;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LaboratoryXmlParser extends DefaultHandler {

    private Long facilityId;
    private Boolean skipRecord;
    private Laboratory laboratory;

    public LaboratoryXmlParser(Long facilityId) {
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
                String laboratoryTag = "close";
                String laboratoryIdTag = "close";
                String patientIdTag = "close";
                String facilityIdTag = "close";
                String hospitalNumTag = "close";
                String dateReportedTag = "close";
                String dateCollectedTag = "close";
                String labnoTag = "close";
                String resultabTag = "close";
                String resultpcTag = "close";
                String commentTag = "close";
                String labtestIdTag = "close";
                String timeStampTag = "close";
                String userIdTag = "close";
                String idUUIDTag = "close";
                String id;
                String uuid;
                String hospitalNum;
                String dateReported;
                String labtestId;

                //this method is called every time the parser gets an open tag '<'
                //identifies which tag is being open at the time by assigning an open flag
                @Override
                public void startElement(String uri, String localName, String element, Attributes attributes) throws SAXException {
                    if (element.equalsIgnoreCase("laboratory")) {
                        laboratoryTag = "open";
                        laboratory = new Laboratory();
                        skipRecord = false;
                    }
                    if (element.equalsIgnoreCase("facility_id")) {
                        facilityIdTag = "open";
                    }
                    if (element.equalsIgnoreCase("laboratory_id")) {
                        laboratoryIdTag = "open";
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
                    if (element.equalsIgnoreCase("date_reported")) {
                        dateReportedTag = "open";
                    }
                    if (element.equalsIgnoreCase("date_collected")) {
                        dateCollectedTag = "open";
                    }
                    if (element.equalsIgnoreCase("labno")) {
                        labnoTag = "open";
                    }
                    if (element.equalsIgnoreCase("resultab")) {
                        resultabTag = "open";
                    }
                    if (element.equalsIgnoreCase("resultpc")) {
                        resultpcTag = "open";
                    }
                    if (element.equalsIgnoreCase("comment")) {
                        commentTag = "open";
                    }
                    if (element.equalsIgnoreCase("labtest_id")) {
                        labtestIdTag = "open";
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
                    if (dateReportedTag.equals("open")) {
                        dateReported = new String(chars, start, length);
                        if (!dateReported.trim().isEmpty()) {
                            laboratory.setDateReported(LocalDate.parse(dateReported, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        } else {
                            skipRecord = true;
                        }
                    }
                    if (dateCollectedTag.equals("open")) {
                        String dateCollected = new String(chars, start, length);
                        if (!dateCollected.trim().isEmpty()) {
                            laboratory.setDateCollected(LocalDate.parse(dateCollected, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        }
                    }
                    if (labnoTag.equals("open")) {
                        laboratory.setLabNo(new String(chars, start, length));
                    }
                    if (resultabTag.equals("open")) {
                        laboratory.setResultAB(new String(chars, start, length));
                    }
                    if (resultpcTag.equals("open")) {
                        laboratory.setResultPC(new String(chars, start, length));
                    }
                    if (commentTag.equals("open")) {
                        laboratory.setComment(new String(chars, start, length));
                    }
                    if (labtestIdTag.equals("open")) {
                        labtestId = new String(chars, start, length);
                        if (!labtestId.trim().isEmpty()) {
                            if (StringUtils.isNumeric(labtestId)) {
                                LabTest labTest = new LabTest();
                                labTest.setLabtestId(Long.parseLong(labtestId));
                                laboratory.setLabTest(labTest);
                            }
                            else skipRecord = true;
                        }
                    }
                    if (idUUIDTag.equals("open")) {
                        uuid = new String(chars, start, length);
                        laboratory.setUuid(uuid);
                    }
                    if (facilityIdTag.equals("open")) {
                        facilityId = Long.parseLong(new String(chars, start, length));
                        Facility facility = new Facility();
                        facility.setId(facilityId);
                        laboratory.setFacility(facility);
                    }
                }

                @Override
                public void endElement(String uri, String localName, String element) throws SAXException {
                    if (element.equalsIgnoreCase("laboratory_id")) {
                        laboratoryIdTag = "close";
                    }
                    if (element.equalsIgnoreCase("facility_id")) {
                        facilityIdTag = "close";
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
                    if (element.equalsIgnoreCase("date_reported")) {
                        dateReportedTag = "close";
                    }
                    if (element.equalsIgnoreCase("date_collected")) {
                        dateCollectedTag = "close";
                    }
                    if (element.equalsIgnoreCase("labno")) {
                        labnoTag = "close";
                    }
                    if (element.equalsIgnoreCase("resultab")) {
                        resultabTag = "close";
                    }
                    if (element.equalsIgnoreCase("resultpc")) {
                        resultpcTag = "close";
                    }
                    if (element.equalsIgnoreCase("comment")) {
                        commentTag = "close";
                    }
                    if (element.equalsIgnoreCase("labtest_id")) {
                        labtestIdTag = "close";
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

                    //if this is the closing tag of a patient element save the record
                    if (element.equalsIgnoreCase("laboratory")) {
                        laboratoryTag = "close";
                        if (skipRecord) {
                            System.out.println("....record has a null value: " + hospitalNum);
                        } else {
                            Long patientId = ServerIDProvider.getPatientServerId(hospitalNum, facilityId);
                            if (patientId != null) {
                                Patient patient = new Patient();
                                patient.setPatientId(patientId);
                                laboratory.setPatient(patient);
                                Long id = ServerIDProvider
                                        .getLaboratoryId(hospitalNum, laboratory.getDateReported(),
                                                Long.parseLong(labtestId), facilityId);
                                laboratory.setLaboratoryId(id);
                                try {
                                    ContextProvider.getBean(LaboratoryRepository.class).save(laboratory);
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
            //new CleanupService().cleanNullFields("laboratory", facilityId);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
