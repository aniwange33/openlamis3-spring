package org.fhi360.lamis.exchange.ndr;

import au.com.bytecode.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.exchange.ndr.schema.*;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.service.UploadFolderService;
import org.fhi360.lamis.utility.FileUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class NdrConverter {
    private final static ExecutorService executorService = Executors.newFixedThreadPool(20);
    private String contextPath = ContextProvider.getBean(ApplicationProperties.class).getContextPath();
    private final PatientDemographicsMapper patientDemographicsMapper;
    private final AddressTypeMapper addressTypeMapper;
    private final CommonQuestionsTypeMapper commonQuestionsTypeMapper;
    private final ConditionSpecificQuestionsTypeMapper conditionSpecificQuestionsTypeMapper;
    private final EncountersTypeMapper encountersTypeMapper;
    private final MessageHeaderTypeMapper messageHeaderTypeMapper;
    private final LaboratoryReportTypeMapper laboratoryoratoryReportTypeMapper;
    private final RegimenTypeMapper regimenTypeMapper;
    private final JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);
    private final TransactionTemplate transactionTemplate = ContextProvider.getBean(TransactionTemplate.class);
    private final FacilityRepository facilityRepository = ContextProvider.getBean(FacilityRepository.class);
    private final ApplicationProperties applicationProperties = ContextProvider.getBean(ApplicationProperties.class);

    private AtomicLong messageId = new AtomicLong(0);
    //private String statusCode;

    public NdrConverter() {
        this.messageHeaderTypeMapper = new MessageHeaderTypeMapper();
        this.patientDemographicsMapper = new PatientDemographicsMapper();
        this.addressTypeMapper = new AddressTypeMapper();
        this.commonQuestionsTypeMapper = new CommonQuestionsTypeMapper();
        this.conditionSpecificQuestionsTypeMapper = new ConditionSpecificQuestionsTypeMapper();
        this.encountersTypeMapper = new EncountersTypeMapper();
        this.regimenTypeMapper = new RegimenTypeMapper();
        this.laboratoryoratoryReportTypeMapper = new LaboratoryReportTypeMapper();

    }

    public void buildMessage(String query, boolean batch, boolean hasRequest) {
        this.messageId.set(MessageStatus.getLastMessageId());
        FileUtil fileUtil = new FileUtil();
        UploadFolderService uploadFolderService = new UploadFolderService();
        List<Long> facilityIds = jdbcTemplate.queryForList(query, Long.class);
        facilityIds.forEach(facilityId -> {
            LOG.info("Facility: {}", facilityId);
            String folder = contextPath + "transfer/temp/" + facilityId + "/";
            fileUtil.makeDir(folder);
            System.out.println("NDR messaging....." + facilityId);
            if (uploadFolderService.getUploadFolderStatus(folder).equalsIgnoreCase("unlocked")) {
                fileUtil.deleteFileWithExtension(folder, ".xml");
                fileUtil.deleteFileWithExtension(folder, ".zip");
                uploadFolderService.lockUploadFolder(folder);
                if (!hasRequest) {
                    NdrThread processorThread = new NdrThread(facilityId, hasRequest);
                    executorService.execute(processorThread);
                } else {
                    buildMessage(facilityId, hasRequest);
                }
                uploadFolderService.unlockUploadFolder(folder);
            }
        });
    }

    public String buildMessage(long facilityId, boolean hasRequest) {

        String query = "SELECT DISTINCT patient_id, hospital_num FROM patient WHERE facility_id = " + facilityId;
        int[] rowcount = {0};
        jdbcTemplate.query(query, (resultSet) -> {
                    while (resultSet.next()) {
                        rowcount[0]++;
                        long patientId = resultSet.getLong("patient_id");
                        String hospitalNum = resultSet.getString("hospital_num");

                        String identifier = Long.toString(facilityId).concat("_").concat(StringUtils.trim(hospitalNum));
                        //Get the last message status
                        Map messageStatus = MessageStatus.getMessageStatus(patientId, identifier);
                        String statusCode = (String) messageStatus.get("statusCode");

                        //Get the last time this patient message was generated
                        Timestamp lastMessage = (Timestamp) messageStatus.get("lastMessage");
                        if (lastMessage == null) {
                            buildMessage(facilityId, patientId, statusCode, identifier);
                        } else {
                            String query1 = "SELECT DISTINCT patient_id FROM patient WHERE patient_id = " + patientId +
                                    " AND time_stamp > '" + lastMessage + "' UNION SELECT DISTINCT patient_id FROM clinic " +
                                    "WHERE  patient_id = " + patientId + " AND time_stamp > '" + lastMessage + "' " +
                                    "UNION SELECT DISTINCT patient_id FROM pharmacy WHERE patient_id = " + patientId + " " +
                                    "AND time_stamp > '" + lastMessage + "' UNION SELECT DISTINCT patient_id FROM " +
                                    "laboratory WHERE patient_id = " + patientId + " AND time_stamp > '" + lastMessage + "'";
                            List<Long> ids = jdbcTemplate.queryForList(query1, Long.class);
                            if (ids.size() > 0) {
                                buildMessage(facilityId, ids.get(0), statusCode, identifier);
                            }
                        }
                        System.out.println("Patient ID..." + patientId + "....Rec...." + rowcount[0]);
                    }
                }
        );

        return zipFiles(facilityId, hasRequest);
    }

//    private void dropAllViews(String dbSuffix, JDBCUtil jdbcUtil){
//        try{
//            executeUpdate("DROP VIEW IF EXISTS patient_"+dbSuffix, jdbcUtil);
//            executeUpdate("DROP VIEW IF EXISTS clinic_"+dbSuffix, jdbcUtil);
//            executeUpdate("DROP VIEW IF EXISTS pharmacy_"+dbSuffix, jdbcUtil);
//            executeUpdate("DROP VIEW IF EXISTS laboratory_"+dbSuffix, jdbcUtil);
//            executeUpdate("DROP VIEW IF EXISTS ndrmessagelog_"+dbSuffix, jdbcUtil);
//        }catch(Exception ex){
//            ex.printStackTrace();;
//        }
//    }

    public String buildMessage(String csvFile, String identifier) {
        String[] row;
        int[] rowcount = {0};
        try {
            if (new File(csvFile).exists()) {
                CSVReader csvReader = new CSVReader(new FileReader(csvFile));
                while ((row = csvReader.readNext()) != null) {
                    rowcount[0]++;
                    if (rowcount[0] > 1) {
                        String fileName = row[0];
                        System.out.println("File name..." + fileName + "....Rec...." + rowcount[0]);

                        String query = "SELECT patient_id, facility_id FROM ndrmessagelog WHERE file_name = '" + fileName + "'";

                        jdbcTemplate.query(query, (resultSet -> {
                                    while (resultSet.next()) {
                                        long patientId = resultSet.getLong("patient_id");
                                        long facilityId = resultSet.getLong("facility_id");

                                        //Get the last message status
                                        Map messageStatus = MessageStatus.getMessageStatus(patientId, identifier);
                                        String statusCode = (String) messageStatus.get("statusCode");
                                        buildMessage(facilityId, patientId, statusCode, identifier);
                                        System.out.println("Patient ID..." + patientId + "....Rec...." + rowcount[0]);
                                    }
                                })
                        );
                    }
                }
                csvReader.close();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return ""; //zipFiles();
    }

    //This method builds the xml message for a patient. The last message variable determines what records in clinic encounters, drug refills and laboratoryoratory investigations should be included in the messages.
    private void buildMessage(long facilityId, long patientId, String statusCode, String identifier) {
        ///throws JAXBException, SAXException, DatatypeConfigurationException
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("org.fhi360.lamis.exchange.ndr.schema");

            //Represents the Container (highest level of the schema)
            Container container = new Container();

            //Set the Header Information
            MessageHeaderType header = messageHeaderTypeMapper.messageHeaderType(patientId);
            header.setMessageStatusCode(statusCode);
            header.setMessageUniqueID(Long.toString(messageId.get()));

            //Set the Header to the Container
            container.setMessageHeader(header);

            //Create the Individual Report
            IndividualReportType individual = new IndividualReportType();

            //Patient Demographics
            PatientDemographicsType patient = patientDemographicsMapper.patientDemograhics(patientId);
            individual.setPatientDemographics(patient);

            //Condition
            ConditionType condition = new ConditionType();
            condition.setConditionCode(CodeSetResolver.getCode("CONDITION_CODE", "HIV_CODE"));
            ProgramAreaType programArea = new ProgramAreaType();
            programArea.setProgramAreaCode(CodeSetResolver.getCode("PROGRAM_AREA", "HIV"));
            condition.setProgramArea(programArea);

            //Address
            AddressType address = addressTypeMapper.addressType(patientId);
            if (address != null) condition.setPatientAddress(address);

            //Common Questions
            CommonQuestionsType common = commonQuestionsTypeMapper.commonQuestionsType(patientId);
            if (common != null) condition.setCommonQuestions(common);

            //HIV Specific
            ConditionSpecificQuestionsType disease = conditionSpecificQuestionsTypeMapper.conditionSpecificQuestionsType(patientId);
            if (disease != null) condition.setConditionSpecificQuestions(disease);

            //Encounters
            EncountersType encounter = encountersTypeMapper.encounterType(patientId);
            if (encounter != null) condition.setEncounters(encounter);

            //Populate ConditionType with laboratoryoratory report
            condition = laboratoryoratoryReportTypeMapper.laboratoryReportType(patientId, condition);

            //Populate ConditionType with regimen
            //condition = regimenTypeMapper.regimenType(id, condition);

            /**
             //Immunizations
             ImmunizationType immunization = new ImmunizationTypeResolver().immunizationType(id);
             if(immunization != null) condition.getImmunization().add(immunization);

             //Contacts
             ContactType contact = new ContactTypeResolver().contactType(id);
             if(contact != null) condition.getContact().add(contact);
             **/

            //Set the Condition to Individual
            //And finally set the individual to the Container
            individual.getCondition().add(condition);
            container.setIndividualReport(individual);

            //Validate Message Against NDR Schema (Version 1.2)
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File("NDR 1.2.xsd"));

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            jaxbMarshaller.setSchema(schema);

            //Call Validator class to perform the validation
            jaxbMarshaller.setEventHandler(new Validator());

            Thread.sleep(1000);     //Delay for some milli seconds
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
            SimpleDateFormat dateForma2t = new SimpleDateFormat("HHmmss.ms");

            String fileName = header.getMessageSendingOrganization().getFacilityID() + "_" + dateFormat.format(date) + "_" + dateForma2t.format(date) + ".xml";
            File file = new File(contextPath + "transfer/temp/" + facilityId + "/" + fileName);

            jaxbMarshaller.marshal(container, file);

            //Log the particulars of message generated into the message log table
            boolean insert = true;
            String query = "INSERT INTO ndrmessagelog(message_id, patient_id, facility_id, file_name, time_stamp," +
                    " identifier) VALUES(?, ?, ?, ?, ?, ?) ";
            if (statusCode.equalsIgnoreCase("UPDATED")) {
                insert = false;
                query = "UPDATE ndrmessagelog SET facility_id = ?, file_name = ?, time_stamp = NOW() WHERE identifier = ?";
            }
            String finalQuery = query;
            boolean finalInsert = insert;
            transactionTemplate.execute(status -> {
                if (!finalInsert) {
                    jdbcTemplate.update(finalQuery, facilityId, fileName, identifier);
                } else {
                    try {
                        jdbcTemplate.update(finalQuery, messageId.incrementAndGet(), patientId, facilityId, fileName, new Date(), identifier);
                    } catch (Exception ignored) {
                    }
                }
                return null;
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private String zipFiles(long facilityId, boolean hasRequest) {
        String fileToReturn = "";
        String facilityName = facilityRepository.getOne(facilityId).getName();
        System.out.println("Facility name....." + facilityName);
        long timespamp = new Date().getTime();
        String fileName = facilityName.trim() + "_" + timespamp + ".zip";
        System.out.println("filename:" + fileName);

        try {
            if (!hasRequest) {
                //Zip extracted NDR messages
                FileUtil fileUtil = new FileUtil();
                //for servlets in the default(root) context, copy file to the transfer folder in root
                fileUtil.makeDir(contextPath + "transfer/ndr/");
                String sourceFolder = contextPath + "transfer/temp/" + facilityId + "/";
                String outputZipFile = contextPath + "transfer/ndr/" + fileName;

                fileUtil.deleteFileWithExtension(sourceFolder, ".ser");
                fileUtil.zipFolderContent(sourceFolder, outputZipFile);

            } else {
                //Zip extracted NDR messages
                String sourceFolder = contextPath + "transfer/temp/" + facilityId + "/";
                String outputZipFile = contextPath + "transfer/ndr/" + fileName;

                FileUtil fileUtil = new FileUtil();
                fileUtil.deleteFileWithExtension(sourceFolder, ".ser");
                fileUtil.zipFolderContent(sourceFolder, outputZipFile);

                //for servlets in the default(root) context, copy file to the transfer folder in root
                fileUtil.makeDir(applicationProperties.getContextPath() + "/transfer/ndr/");
                if (!contextPath.equalsIgnoreCase(applicationProperties.getContextPath()))
                    fileUtil.copyFile(fileName, contextPath + "transfer/ndr/", applicationProperties.getContextPath() + "/transfer/ndr/");

                fileToReturn = "transfer/ndr/" + fileName;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return fileToReturn;
    }

    private String zerorize(String messageId) {
        StringBuilder zeros = new StringBuilder();
        int MAX_LENGTH = 8;
        if (messageId.length() < MAX_LENGTH) {
            for (int i = 0; i < MAX_LENGTH - messageId.length(); i++) {
                zeros.append("0");
            }
        }
        return zeros + messageId;
    }

    public class NdrThread implements Runnable {
        private long facilityId;
        private boolean hasRequest;

        NdrThread(Long facilityId, boolean hasRequest) {
            this.facilityId = facilityId;
            this.hasRequest = hasRequest;
        }

        @Override
        public void run() {
            buildMessage(facilityId, hasRequest);
        }
    }

}
