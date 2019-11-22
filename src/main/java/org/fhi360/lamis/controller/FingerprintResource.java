/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import com.neurotec.biometrics.*;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.devices.NDevice;
import com.neurotec.devices.NDeviceManager;
import com.neurotec.devices.NDeviceManager.DeviceCollection;
import com.neurotec.devices.NDeviceType;
import com.neurotec.devices.NFScanner;
import com.neurotec.io.NBuffer;
import com.neurotec.lang.NCore;
import com.neurotec.lang.NObject;
import com.neurotec.licensing.NLicense;
import com.neurotec.licensing.NLicenseManager;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.resource.LibraryManager;
import org.fhi360.lamis.resource.model.BiometricResult;
import org.fhi360.lamis.resource.model.Device;
import org.fhi360.lamis.utility.Scrambler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


@RestController
@RequestMapping("/api/fingerprint")
@Api(tags = "Fingerprint"  , description = " ")
public class FingerprintResource {

    private static final Logger LOG = LoggerFactory.getLogger(FingerprintResource.class.getName());
    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;
    private static final Set<String> LICENSES = new HashSet<>();
    private NDeviceManager deviceManager;
    private NBiometricClient client;

    public FingerprintResource(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    @GetMapping("/readers")
    public List<Device> getReaders() {
        List<Device> devices = new ArrayList<>();
        for (NDevice device : getDevices()) {
            Device d = new Device();
            d.setName(device.getDisplayName());
            d.setId(device.getId());
            devices.add(d);
        }
        return devices;
    }

    @GetMapping("save-duplicate/{patientId1}/{patientId2}")
    public boolean saveDuplicate(@PathVariable("patientId1") Long patientId1,
                                 @PathVariable("patientId2") Long patientId2, HttpServletRequest request) {
        Long facilityId = (Long) request.getSession().getAttribute("id");
        transactionTemplate.execute(status -> {
            jdbcTemplate.update("insert into biometric_duplicate "
                            + "(biometric_duplicate_id, patient_id_1, patient_id_2, facility_id, duplicate_date) values(?,?,?,?,?)",
                    UUID.randomUUID().toString(), getUUID(patientId1), getUUID(patientId2), facilityId, new Date());
            return null;
        });
        return true;
    }

    private String getUUID(Long patientId) {
        String uuid = jdbcTemplate.queryForObject("select uuid from patient where patient_id = ?",
                String.class, patientId);
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            String finalUuid = uuid;
            transactionTemplate.execute(status -> {
                jdbcTemplate.update("update patient set uuid = ? where patient_id = ?",
                        finalUuid, patientId);
                return null;
            });
        }
        return uuid;
    }

    @GetMapping("find-duplicate/{hospitalNum}")
    public BiometricResult addDuplicate(@PathVariable("hospitalNum") String hospitalNum,
                                        HttpServletRequest request) {
        Long facilityId = (Long) request.getSession().getAttribute("id");
        Long patientId = jdbcTemplate.queryForObject("select patient_id "
                        + " from patient where hospital_num = ? and facility_id = ?",
                Long.class, hospitalNum, facilityId);
        BiometricResult result = getResult(facilityId, patientId);
        if (result != null) {
            result.setMessage("Patient identified");
        }
        return result;
    }

    @GetMapping("identify/{reader}")
    public BiometricResult identify(@PathVariable("reader") String reader, HttpServletRequest request) {
        Long currentFacilityId = (Long) request.getSession().getAttribute("id");

        try {
            if (!NLicense.isComponentActivated("Biometrics.FingerExtraction")) {
                obtainLicenses();
            }
        } catch (IOException ex) {
            LOG.error("Error: {}", ex.getMessage());
        }

        createClient();
        try {
            reader = URLDecoder.decode(reader, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {

        }
        NSubject subject = new NSubject();
        NFinger finger = new NFinger();
        finger.setPosition(NFPosition.UNKNOWN);
        subject.getFingers().add(finger);
        for (NDevice device : getDevices()) {
            if (device.getId().equals(reader)) {
                client.setFingerScanner((NFScanner) device);
            }
        }
        client.setFingerScanner((NFScanner) getDevices().get(0));
        if (client.getFingerScanner() == null) {
            return null;
        }

        NBiometricStatus status = client.capture(subject);
        if (status.equals(NBiometricStatus.OK)) {
            status = client.createTemplate(subject);
            if (status.equals(NBiometricStatus.OK)) {
                List<NSubject> galleries = jdbcTemplate.query("select biometric_id, template from biometric",
                        (rs, i) -> {
                            byte[] template = rs.getBytes("template");
                            NSubject gallery = new NSubject();
                            gallery.setTemplateBuffer(new NBuffer(template));
                            gallery.setId(rs.getString("biometric_id"));
                            return gallery;
                        });
                NBiometricTask task = client.createTask(
                        EnumSet.of(NBiometricOperation.ENROLL), null);
                galleries.forEach(gallery -> task.getSubjects().add(gallery));
                client.performTask(task);
                if (task.getStatus().equals(NBiometricStatus.OK)) {
                    status = client.identify(subject);
                    if (status.equals(NBiometricStatus.OK)) {
                        String id = subject.getMatchingResults().get(0).getId();
                        final BiometricResult[] result = new BiometricResult[1];
                        jdbcTemplate.query("select p.patient_id, p.facility_id, p.hospital_num from biometric b "
                                        + " inner join patient p on p.uuid = b.patient_id where biometric_id = ?",
                                rs -> {
                                    Long facilityId = rs.getLong("facility_id");
                                    String hospitalNum = rs.getString("hospital_num");
                                    Long patientId = rs.getLong("patient_id");
                                    result[0] = getResult(facilityId, patientId);
                                    if (!currentFacilityId.equals(facilityId)) {
                                        result[0].setInFacility(false);
                                        result[0].setMessage("Patient identified");
                                        transactionTemplate.execute(status1 -> {
                                            jdbcTemplate.update("insert into biometric_transfer "
                                                            + "(biometric_transfer_id, hospital_num, transfer_facility_id, facility_id, patient_id, transfer_date)"
                                                            + " values(?,?,?,?,?,?)",
                                                    UUID.randomUUID().toString(), hospitalNum, facilityId,
                                                    currentFacilityId, getUUID(patientId), new Date());
                                            return null;
                                        });
                                    }
                                }, id);
                        return result[0];
                    }
                }
            }
        }
        return null;
    }

    @GetMapping("enrolled-fingers/{patient}")
    public List<String> enrolledFingers(
            @PathVariable("patient") Long patientId, HttpServletRequest request) {
        Long facilityId = (Long) request.getSession().getAttribute("id");
        return jdbcTemplate.query("select template_type from biometric where facility_id = ? and patient_id = ?",
                (rs, rowNum) -> {
                    String finger = rs.getString("template_type");
                    switch (finger) {
                        case "LEFT_THUMB":
                            finger = "Left Thumb";
                            break;
                        case "RIGHT_THUMB":
                            finger = "Right Thumb";
                            break;
                        case "LEFT_INDEX_FINGER":
                            finger = "Left Index Finger";
                            break;
                        case "RIGHT_INDEX_FINGER":
                            finger = "Right Index Finger";
                            break;
                    }
                    return finger;
                }, facilityId, getUUID(patientId));
    }

    @GetMapping("verify/{reader}/{patientId}")
    public BiometricResult verify(@PathVariable("patientId") Long patientId,
                                  @PathVariable("reader") String reader,
                                  HttpServletRequest request) {
        createClient();
        try {
            reader = URLDecoder.decode(reader, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {

        }
        Long facilityId = (Long) request.getSession().getAttribute("id");
        NSubject subject = new NSubject();
        subject.setId(getUUID(patientId));
        NFPosition position = NFPosition.UNKNOWN;
        NFinger finger = new NFinger();
        finger.setPosition(position);
        subject.getFingers().add(finger);

        for (NDevice device : getDevices()) {
            if (device.getId().equals(reader)) {
                client.setFingerScanner((NFScanner) device);
            }
        }
        if (client.getFingerScanner() == null) {
            return null;
        }

        NBiometricStatus status = client.capture(subject);
        if (status.equals(NBiometricStatus.OK)) {
            status = client.createTemplate(subject);
            if (status.equals(NBiometricStatus.OK)) {
                List<NSubject> subjects = jdbcTemplate.query(
                        "select biometric_id, template,"
                                + "template_type from biometric where patient_id = ? and facility_id = ?",
                        (rs, i) -> {
                            byte[] template = rs.getBytes("template");
                            String id = rs.getString("biometric_id");
                            //FMRecord record = new FMRecord(new NBuffer(template), BDIFStandard.UNSPECIFIED);
                            //referenceSubject.setTemplate(record);
                            //referenceSubject.setTemplate(value);
                            NSubject referenceSubject = new NSubject();
                            referenceSubject.setId(id);
                            referenceSubject.setTemplateBuffer(new NBuffer(template));
                            return referenceSubject;
                        },
                        getUUID(patientId), facilityId
                );
                for (NSubject referenceSubject : subjects) {
                    status = client.verify(subject, referenceSubject);
                    if (status.equals(NBiometricStatus.OK)) {
                        BiometricResult result = getResult(facilityId, patientId);
                        if (result != null) {
                            result.setMessage("Patient Verified");
                        }
                        dispose(client);
                        return result;
                    }
                }
            }
        }
        dispose(client);
        return null;
    }

    @GetMapping("enrol/{reader}/{patientId}/{finger}")
    public BiometricResult enrol(@PathVariable("patientId") Long patientId,
                                 @PathVariable("reader") String reader,
                                 @PathVariable("finger") String type,
                                 HttpServletRequest request) {

        createClient();
        try {
            reader = URLDecoder.decode(reader, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {

        }

        Long facilityId = (Long) request.getSession().getAttribute("id");
        NSubject subject = new NSubject();
        NFPosition position = getFingerPosition(type);
        NFinger finger = new NFinger();
        if (position != null) {
            finger.setPosition(position);
        }
        subject.getFingers().add(finger);
        for (NDevice device : getDevices()) {
            if (device.getId().equals(reader)) {
                client.setFingerScanner((NFScanner) device);
            }
        }
        if (client.getFingerScanner() == null) {
            return null;
        }

        NBiometricStatus status = client.capture(subject);
        if (status.equals(NBiometricStatus.OK)) {
            status = client.createTemplate(subject);
            if (status.equals(NBiometricStatus.OK)) {
                byte[] template = subject.getTemplateBuffer().toByteArray();
                boolean update = false;
                String id = jdbcTemplate.queryForObject("select biometric_id from biometric where facility_id = ? "
                                + "and patient_id = ? and template_type = ?",
                        String.class, facilityId, getUUID(patientId), position.name());
                if (id != null) {
                    update = true;
                }
                if (!update) {
                    jdbcTemplate.query("select uuid, surname, other_names, address, phone, gender,"
                                    + " hospital_num from patient where patient_id = ? and facility_id = ?",
                            rs -> {
                                String surname = rs.getString("surname");
                                String patientUUID = rs.getString("uuid");
                                String otherNames = rs.getString("other_names");
                                String address = rs.getString("address");
                                String phone = rs.getString("phone");
                                String gender = rs.getString("gender");
                                String hospitalNum = rs.getString("hospital_num");
                                Date enrollmentDate = jdbcTemplate.queryForObject("select enrollment_date from biometric where patient_id = ?",
                                        Date.class, getUUID(patientId));
                                transactionTemplate.execute(status1 -> {
                                    jdbcTemplate.update("insert into biometric (biometric_id, facility_id, patient_id, template,"
                                                    + "template_type, biometric_type, patient_name, patient_address, patient_phone, patient_gender, hospital_num, enrollment_date) "
                                                    + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                                            UUID.randomUUID().toString(), facilityId, patientUUID, template, position.name(),
                                            "FINGERPRINT", StringUtils.trimToEmpty(otherNames) + " " + StringUtils.trimToEmpty(surname),
                                            address, phone, gender, hospitalNum, enrollmentDate);
                                    return null;
                                });
                            }, patientId, facilityId);
                    BiometricResult result = getResult(facilityId, patientId);
                    if (result != null) {
                        result.setMessage("Patient Enrolled");
                    }
                    return result;
                } else {
                    jdbcTemplate.update("update biometric set template = ? where biometric_id = ?",
                            template, id);
                    BiometricResult result = getResult(facilityId, patientId);
                    if (result != null) {
                        result.setMessage("Patient Enrolled");
                    }
                    return result;
                }
            }

        }
        return null;
    }

    private BiometricResult getResult(Long facilityId, Long patientId) {
        Scrambler scrambler = new Scrambler();
        return jdbcTemplate.query("select surname, other_names, address, phone, gender"
                        + " from patient where patient_id = ? and facility_id = ?",
                (rs) -> {
                    String surname = rs.getString("surname");
                    String otherNames = rs.getString("other_names");
                    String address = rs.getString("address");
                    String phone = rs.getString("phone");
                    String gender = rs.getString("gender");
                    BiometricResult result = new BiometricResult();
                    result.setFacilityId(facilityId);
                    result.setPatientId(patientId);
                    result.setPhone(scrambler.unscrambleCharacters(phone));
                    result.setAddress(scrambler.unscrambleCharacters(address));
                    result.setGender(gender);
                    result.setName(scrambler.unscrambleCharacters(StringUtils.trimToEmpty(otherNames))
                            + " " + scrambler.unscrambleCharacters(StringUtils.trimToEmpty(surname)));
                    return result;
                }, patientId, facilityId);
    }

    private void obtainLicense(String component) {
        try {
            NLicense.obtainComponents("/local", 52358, component);
        } catch (IOException ex) {
            LOG.error("{}", ex.getMessage());
        }
    }

    private void releaseLicense(String component) {
        try {
            NLicense.releaseComponents(component);
        } catch (IOException ex) {
            LOG.error("{}", ex.getMessage());
        }
    }

    //@PostConstruct
    public void initialize() {
        StringBuilder path = new StringBuilder();
        path.append("C:\\LAMIS2\\neurotec\\license").append(File.separator);
        LibraryManager.initLibraryPath();
        try {
            URLConnection connection = new URL("http://google.com").openConnection();
            try {
                LOG.info("Connecting...");
                connection.connect();
            } catch (IOException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        } catch (IOException ex) {

        }
        LOG.info("Before initializing...");
        NLicenseManager.initialize();
        try {
            NLicense.add(String.join("\n", Files.readAllLines(
                    Paths.get(path.toString() + "matcher.txt"), Charset.defaultCharset())));
            /*
            NLicense.add("# \n"
                    + "\n"
                    + "Fingerprint Matcher internet license.\n"
                    + "\n"
                    + ":0000000589C1F6A40B76ECC1188C9B2DA463868246C8564DC254ECCAE5F401196B4A40\n"
                    + "9721EC95AF53263A739BC1A2AC5C5B555DCB05CF953AA5EDF968A4E578CF6C4F061BC90\n"
                    + "1AD5FC2D07F96118C61CAABDA651614103C68D4DEB097D22888B4BAC0B39761AE23D40B\n"
                    + "09D1699C756FEDAFA60A1F89898B22748931E81100461CDD9EA8BA88B64FFB2003B4597\n"
                    + "B85A0CD0CFA5B42DE802FFBD8B7FE6648B106EC670E99B492DEE5362C88C7AA828C3565\n"
                    + "95FD08FE9011DFF04098F8E614147847496F7C60\n");
            
             */
            /*

            NLicense.add(String.join("\n", Files.readAllLines(
                    Paths.get(path.toString() + "client.txt"), Charset.defaultCharset())));
            */
/*
            NLicense.add("# \n"
                    + "\n"
                    + "Fingerprint Client internet license.\n"
                    + "\n"
                    + ":0000000566F2C5EE2B6BA1A88E0B4AE0180A8E2329BB6511D416B225DD38C144514615\n"
                    + "947AF1033A31C3DAECC63D6E7F9B3D677F7BB525C9DCF98F8DE945135A268A66E9F245C\n"
                    + "B7DEF34230A9D8E55EC31F077617F91258B6AFD6B5DF5772A82E0035C76F082B23DA244\n"
                    + "AB8E7BE9DD711F8F31A4DA8E7B2D58F88B7AE9DD28C13E3F8204899134188E42A0F7FCD\n"
                    + "3F1D18636A68732AC45E68A2456531C3AD901BCCC43343DD37840C12F652A7581A5BF7D\n"
                    + "238EDCD33CE929E8656A89293663BC8862101858\n");
             */

        } catch (IOException ex) {
            LOG.error("{}", ex.getMessage());
        }
        obtainLicenses();

        initDeviceManager();

        createClient();
    }

    private void createClient() {
        client = new NBiometricClient();
        client.setMatchingThreshold(48);
        client.setFingersMatchingSpeed(NMatchingSpeed.LOW);
        client.setFingersTemplateSize(NTemplateSize.LARGE);
        client.initialize();
    }

    @PreDestroy
    public void cleanup() {
        dispose(deviceManager, client);
        for (String license : LICENSES) {
            releaseLicense(license);
        }
        NCore.shutdown();
    }

    private void obtainLicenses() {
        LOG.info("Obtaining licenses");
        for (String license : LICENSES) {
            obtainLicense(license);
        }
    }

    private void initDeviceManager() {
        deviceManager = new NDeviceManager();
        deviceManager.setDeviceTypes(EnumSet.of(NDeviceType.FINGER_SCANNER));
        deviceManager.setAutoPlug(true);
        deviceManager.initialize();
    }

    private DeviceCollection getDevices() {
        return deviceManager.getDevices();
    }

    private void dispose(NObject... objects) {
        for (NObject object : objects) {
            object.dispose();
        }
    }

    private NFPosition getFingerPosition(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        return NFPosition.valueOf(type);
    }

    static {
        LICENSES.add("Biometrics.FingerExtraction");
        LICENSES.add("Biometrics.Standards.FingerTemplates");
        LICENSES.add("Devices.FingerScanners");
        LICENSES.add("Biometrics.FingerMatching");
    }
}
