
package org.fhi360.lamis.service;
import org.fhi360.lamis.service.parser.mobile.*;
import org.springframework.stereotype.Component;

@Component
public class JsonBuilderDelegator {
    private final EncounterJsonParser encounterJsonParser;
    private final HtsJsonParser htsJsonParser;
    private final IndexcontactJsonParser indexcontactJsonParser;
    private final AssessmentJsonParser assessmentJsonParser;
    private final DrugtherapyJsonParser drugtherapyJsonParser;
    private final MhtcJsonParser mhtcJsonParser;
    private final AppointmentJsonParser appointmentJsonParser;
    private final MonitorJsonParser monitorJsonParser;
    private final PatientJsonParser patientJsonParser;
    private final ClinicJsonParser clinicJsonParser;

    public JsonBuilderDelegator(EncounterJsonParser encounterJsonParser, HtsJsonParser htsJsonParser, IndexcontactJsonParser indexcontactJsonParser, AssessmentJsonParser assessmentJsonParser, DrugtherapyJsonParser drugtherapyJsonParser, MhtcJsonParser mhtcJsonParser, AppointmentJsonParser appointmentJsonParser, MonitorJsonParser monitorJsonParser, PatientJsonParser patientJsonParser, ClinicJsonParser clinicJsonParser) {
        this.encounterJsonParser = encounterJsonParser;
        this.htsJsonParser = htsJsonParser;
        this.indexcontactJsonParser = indexcontactJsonParser;
        this.assessmentJsonParser = assessmentJsonParser;
        this.drugtherapyJsonParser = drugtherapyJsonParser;
        this.mhtcJsonParser = mhtcJsonParser;
        this.appointmentJsonParser = appointmentJsonParser;
        this.monitorJsonParser = monitorJsonParser;
        this.patientJsonParser = patientJsonParser;
        this.clinicJsonParser = clinicJsonParser;
    }

    public void delegate(String table, String content) {
        try {
            switch (table) {
                case "monitor":
                    monitorJsonParser.parserJson(content);
                    break;
                case "patient":

                    patientJsonParser.parserJson(table, content);
                    break;
                case "clinic":

                    clinicJsonParser.parserJson(table, content);
                    break;
                case "hts":

                    htsJsonParser.parserJson(table, content);
                    break;
                case "indexcontact":
                    indexcontactJsonParser.parserJson(table, content);
                    break;
                case "assessment":
                    assessmentJsonParser.parserJson(table, content);
                    break;
                case "encounter":
                    encounterJsonParser.parserJson(table, content);
                    break;
                case "drugtherapy":
                    drugtherapyJsonParser.parserJson(table, content);
                    break;
                case "mhtc":
                    mhtcJsonParser.parserJson(table, content);
                    break;
                case "appointment":

                    appointmentJsonParser.parserJson(table, content);
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

