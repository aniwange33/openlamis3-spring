package org.fhi360.lamis.service.parser.xml;


/**
 *
 * @author user1
 */
public class XmlParserDelegator {

    public XmlParserDelegator() {
    }

    public void delegate(String table, String fileName, Long facilityId) {
        try {
            // CREATE VIEW to hold local ids and server generated ids
            switch (table) {
                case "patient":
                    PatientXmlParser patientXmlParser = new PatientXmlParser(facilityId);
                    patientXmlParser.parseXml(fileName);
                    break;
                case "clinic":
                    ClinicXmlParser clinicXmlParser = new ClinicXmlParser(facilityId);
                    clinicXmlParser.parseXml(fileName);
                    break;

                case "pharmacy":
                    PharmacyXmlParser pharmacyXmlParser = new PharmacyXmlParser(facilityId);
                    pharmacyXmlParser.parseXml(fileName);
                    break;
                case "laboratory":
                    LaboratoryXmlParser laboratoryXmlParser = new LaboratoryXmlParser(facilityId);
                    laboratoryXmlParser.parseXml(fileName);
                    break;
                case "adrhistory":
                    AdrXmlParser adrXmlParser = new AdrXmlParser(facilityId);
                    adrXmlParser.parseXml(fileName);
                    break;
                case "adherehistory":
                    AdhereXmlParser adhereXmlParser = new AdhereXmlParser(facilityId);
                    adhereXmlParser.parseXml(fileName);
                    break;
                case "statushistory":
                    StatusXmlParser statusXmlParser = new StatusXmlParser(facilityId);
                    statusXmlParser.parseXml(fileName);
                    break;
                case "anc":
                    AncXmlParser ancXmlParser = new AncXmlParser(facilityId);
                    ancXmlParser.parseXml(fileName);
                    break;
                default:
                    break;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }
}