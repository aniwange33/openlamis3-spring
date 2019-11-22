/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service;

import org.fhi360.lamis.service.parser.json.*;
import org.fhi360.lamis.service.parser.mobile.*;
import org.springframework.stereotype.Component;

/**
 * @author user1
 */
@Component
public class XmlParserDelegator {
    private final MonitorJsonParser monitorJsonParser;
    private final UserJsonParser userJsonParser;
    private final PatientCaseManagerJsonParser caseManagerJsonParser;
    private final PatientJsonParser patientJsonParser;
    private final ClinicJsonParser clinicJsonParser;
    private final PharmacyJsonParser pharmacyJsonParser;
    private final LaboratoryJsonParser laboratoryJsonParser;
    private final AdrHistoryJsonParser adrHistoryJsonParser;
    private final OiHistoryJsonParser oiHistoryJsonParser;
    private final AdhereHistoryJsonParser adhereHistoryJsonParser;
    private final StatusHistoryJsonParser statusHistoryJsonParser;
    private final RegimenHistoryJsonParser regimenHistoryJsonParser;
    private final ChronicCareJsonParser chronicCareJsonParser;
    private final TbScreenHistoryJsonParser tbScreenHistoryJsonParser;
    private final AncJsonParser ancJsonParser;
    private final DeliveryJsonParser deliveryJsonParser;
    private final ChildJsonParser childJsonParser;
    private final MaternalFollowUpJsonParser maternalFollowUpJsonParser;
    private final ChildFollowUpJsonParser childFollowUpJsonParser;
    private final PartnerInformationJsonParser partnerInformationJsonParser;
    private final SpecimenJsonParser specimenJsonParser;
    private final EidJsonParser eidJsonParser;
    private final DevolveJsonParser devolveJsonParser;
    private final PatientCaseManagerJsonParser patientCaseManagerJsonParser;
    private final EacJsonParser eacJsonParser;
    private final BiometricJsonParser biometricJsonParser;
    private final EncounterJsonParser encounterJsonParser;
    private final HtsJsonParser htsJsonParser;
    private final IndexcontactJsonParser indexcontactJsonParser;
    private final AssessmentJsonParser assessmentJsonParser;
    private final DrugtherapyJsonParser drugtherapyJsonParser;
    private final MhtcJsonParser mhtcJsonParser;
    private final AppointmentJsonParser appointmentJsonParser;

    public XmlParserDelegator(MonitorJsonParser monitorJsonParser, UserJsonParser userJsonParser, PatientCaseManagerJsonParser caseManagerJsonParser, PatientJsonParser patientJsonParser, ClinicJsonParser clinicJsonParser, PharmacyJsonParser pharmacyJsonParser, LaboratoryJsonParser laboratoryJsonParser, AdrHistoryJsonParser adrHistoryJsonParser, OiHistoryJsonParser oiHistoryJsonParser, AdhereHistoryJsonParser adhereHistoryJsonParser, StatusHistoryJsonParser statusHistoryJsonParser, RegimenHistoryJsonParser regimenHistoryJsonParser, ChronicCareJsonParser chronicCareJsonParser, TbScreenHistoryJsonParser tbScreenHistoryJsonParser, AncJsonParser ancJsonParser, DeliveryJsonParser deliveryJsonParser, ChildJsonParser childJsonParser, MaternalFollowUpJsonParser maternalFollowUpJsonParser, ChildFollowUpJsonParser childFollowUpJsonParser, PartnerInformationJsonParser partnerInformationJsonParser, SpecimenJsonParser specimenJsonParser, EidJsonParser eidJsonParser, DevolveJsonParser devolveJsonParser, PatientCaseManagerJsonParser patientCaseManagerJsonParser, EacJsonParser eacJsonParser, BiometricJsonParser biometricJsonParser, EncounterJsonParser encounterJsonParser, HtsJsonParser htsJsonParser, IndexcontactJsonParser indexcontactJsonParser, AssessmentJsonParser assessmentJsonParser, DrugtherapyJsonParser drugtherapyJsonParser, MhtcJsonParser mhtcJsonParser, AppointmentJsonParser appointmentJsonParser) {
        this.monitorJsonParser = monitorJsonParser;
        this.userJsonParser = userJsonParser;
        this.caseManagerJsonParser = caseManagerJsonParser;
        this.patientJsonParser = patientJsonParser;
        this.clinicJsonParser = clinicJsonParser;
        this.pharmacyJsonParser = pharmacyJsonParser;
        this.laboratoryJsonParser = laboratoryJsonParser;
        this.adrHistoryJsonParser = adrHistoryJsonParser;
        this.oiHistoryJsonParser = oiHistoryJsonParser;
        this.adhereHistoryJsonParser = adhereHistoryJsonParser;
        this.statusHistoryJsonParser = statusHistoryJsonParser;
        this.regimenHistoryJsonParser = regimenHistoryJsonParser;
        this.chronicCareJsonParser = chronicCareJsonParser;
        this.tbScreenHistoryJsonParser = tbScreenHistoryJsonParser;
        this.ancJsonParser = ancJsonParser;
        this.deliveryJsonParser = deliveryJsonParser;
        this.childJsonParser = childJsonParser;
        this.maternalFollowUpJsonParser = maternalFollowUpJsonParser;
        this.childFollowUpJsonParser = childFollowUpJsonParser;
        this.partnerInformationJsonParser = partnerInformationJsonParser;
        this.specimenJsonParser = specimenJsonParser;
        this.eidJsonParser = eidJsonParser;
        this.devolveJsonParser = devolveJsonParser;
        this.patientCaseManagerJsonParser = patientCaseManagerJsonParser;
        this.eacJsonParser = eacJsonParser;
        this.biometricJsonParser = biometricJsonParser;
        this.encounterJsonParser = encounterJsonParser;
        this.htsJsonParser = htsJsonParser;
        this.indexcontactJsonParser = indexcontactJsonParser;
        this.assessmentJsonParser = assessmentJsonParser;
        this.drugtherapyJsonParser = drugtherapyJsonParser;
        this.mhtcJsonParser = mhtcJsonParser;
        this.appointmentJsonParser = appointmentJsonParser;
    }


    public void delegate(String table, String fileName) {
        try {
            // CREATE VIEW to hold local ids and server generated ids
            switch (table) {
                case "monitor":
                    monitorJsonParser.parserJson(fileName);
                    break;
                case "user":
                    userJsonParser.parserJson(table, fileName);
                    break;
                case "casemanager":
                    caseManagerJsonParser.parserJson(table, fileName);
                    break;
                case "patient":
                    patientJsonParser.parserJson(table, fileName);
                    break;
                case "clinic":
                    clinicJsonParser.parserJson(table, fileName);
                    break;
                case "pharmacy":
                    patientCaseManagerJsonParser.parserJson(table, fileName);
                    break;
                case "laboratory":
                    laboratoryJsonParser.parserJson(table, fileName);
                    break;
                case "adrhistory":
                    adrHistoryJsonParser.parserJson(table, fileName);
                    break;
                case "oihistory":
                    oiHistoryJsonParser.parserJson(table, fileName);
                    break;
                case "adherehistory":
                    adhereHistoryJsonParser.parserJson(table, fileName);
                    break;
                case "statushistory":
                    statusHistoryJsonParser.parserJson(table, fileName);
                    break;
                case "regimenhistory":
                    regimenHistoryJsonParser.parserJson(table, fileName);
                    break;
                case "chroniccare":
                    chronicCareJsonParser.parserJson(table, fileName);
                    break;
                case "tbscreenhistory":
                    tbScreenHistoryJsonParser.parserJson(table, fileName);
                    break;
//                case "dmscreenhistory":
//                    DmScreenHistoryXmlParser dmscreenhistoryXmlParser = new DmScreenHistoryXmlParser(id);
//                    dmscreenhistoryXmlParser.parserJson(fileName);
//                    break;
//                case "motherinformation":
//                    MotherInformationXmlParser motherinformationXmlParser = new MotherInformationXmlParser(id);
//                    motherinformationXmlParser.parserJson(fileName);
//                    break;
                case "anc":
                    ancJsonParser.parserJson(table, fileName);
                    break;
                case "delivery":
                    deliveryJsonParser.parserJson(table, fileName);
                    break;
                case "child":
                    childJsonParser.parserJson(table, fileName);
                    break;
                case "maternalfollowup":
                    maternalFollowUpJsonParser.parserJson(table, fileName);
                    break;
                case "childfollowup":
                    childFollowUpJsonParser.parserJson(table, fileName);
                    break;
                case "partnerinformation":
                    partnerInformationJsonParser.parserJson(table, fileName);
                    break;
                case "specimen":
                    specimenJsonParser.parserJson(table, fileName);
                    break;
                case "eid":
                    eidJsonParser.parserJson(table, fileName);
                    break;
//                case "labno":
//                    LabnoXmlParser labnoXmlParser = new LabnoXmlParser();
//                    labnoXmlParser.parserJson(fileName);
//                    break;
//                case "nigqual":
//                    NigqualXmlParser nigqualXmlParser = new NigqualXmlParser(id);
//                    nigqualXmlParser.parserJson(fileName);
//                    break;
                case "devolve":
                    devolveJsonParser.parserJson(table, fileName);
                    break;
                case "patientcasemanager":
                    patientCaseManagerJsonParser.parserJson(table, fileName);
                    break;
                case "eac":
                    eacJsonParser.parserJson(table, fileName);
                    break;
                case "biometric":
                    biometricJsonParser.parserJson(table, fileName);
                    break;
                case "hts":
                    htsJsonParser.parserJson(table, fileName);
                    break;
                case "indexcontact":
                    indexcontactJsonParser.parserJson(table, fileName);
                    break;
                case "assessment":
                    assessmentJsonParser.parserJson(table, fileName);
                    break;
                case "encounter":
                    encounterJsonParser.parserJson(table, fileName);
                    break;
                case "drugtherapy":
                    drugtherapyJsonParser.parserJson(table, fileName);
                    break;
                case "mhtc":
                    mhtcJsonParser.parserJson(table, fileName);
                    break;
                case "appointment":
                    appointmentJsonParser.parserJson(table, fileName);
                    break;
                default:
                    break;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }
}
