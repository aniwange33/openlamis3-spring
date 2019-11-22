/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*

package org.fhi360.lamis.resource;

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
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.resource.model.BiometricResult;
import org.fhi360.lamis.resource.model.Device;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.Scrambler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

import static org.apache.commons.lang3.SystemUtils.FILE_SEPARATOR;

*/
/**
 *
 * @author User10
 *//*

@Path("/api/fingerprint")
@Singleton
public class FingerprintResource {

    private static final Logger LOG = LoggerFactory.getLogger(FingerprintResource.class.getName());
    private static final Set<String> LICENSES = new HashSet<>();
    private NDeviceManager deviceManager;
    private NBiometricClient client;

    @Path("/readers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
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

    @Path("save-duplicate/{patientId1}/{patientId2}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public boolean saveDuplicate(@PathParam("patientId1") Long patientId1,
            @PathParam("patientId2") Long patientId2,
            @Context HttpServletRequest request) {
        Long id = (Long) request.getSession().getAttribute("id");
        try {
            JDBCUtil jdbcu = new JDBCUtil();
            PreparedStatement statement = jdbcu.getStatement("insert into biometric_duplicate "
                    + "(biometric_duplicate_id, patient_id_1, patient_id_2, facility_id, duplicate_date) values(?,?,?,?)");
            statement.setString(1, UUID.randomUUID().toString());
            statement.setLong(4, id);
            statement.setString(2, getUUID(patientId1));
            statement.setString(3, getUUID(patientId2));
            statement.setDate(4, new java.sql.Date(new Date().getTime()));
        } catch (SQLException ex) {
            LOG.error("{}", ex);
        }
        return true;
    }

    private String getUUID(Long id) {
        PreparedStatement statement;
        try {
            statement = new JDBCUtil().getStatement(String.format("select id_uuid from patient where patient_id = %s", id));
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id_uuid");
                if (id == null) {
                    statement = new JDBCUtil().getStatement("update patient set id_uuid = ? where patient_id = ?");
                    id = UUID.randomUUID().toString();
                    statement.setString(1, id);
                    statement.setLong(2, id);
                    statement.executeUpdate();
                }
                return id;
            }
        } catch (SQLException ex) {
            LOG.info("UUID Error");
            ex.printStackTrace();
        }
        return null;
    }

    @Path("find-duplicate/{hospitalNum}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public BiometricResult addDuplicate(@PathParam("hospitalNum") String hospitalNum,
            @Context HttpServletRequest request) {
        Long id = (Long) request.getSession().getAttribute("id");
        try {
            JDBCUtil jdbcu = new JDBCUtil();
            PreparedStatement statement = jdbcu.getStatement("select patient_id "
                    + " from patient where hospital_num = ? and facility_id = ?");
            statement.setLong(2, id);
            statement.setString(1, hospitalNum);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong("patient_id");
                BiometricResult result = getResult(id, id);
                if (result != null) {
                    result.setMessage("Patient identified");
                }
                return result;
            }
        } catch (SQLException ex) {
            LOG.error("{}", ex);
        }
        return null;
    }

    @Path("identify/{reader}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public BiometricResult identify(@PathParam("reader") String reader,
            @Context HttpServletRequest request) {
        Long currentFacilityId = (Long) request.getSession().getAttribute("id");
        */
/*
        try {
            if(!NLicense.isComponentActivated("Biometrics.FingerExtraction")) {
                obtainLicenses();
            }
        } catch (IOException ex) {
            LOG.error("Error: {}", ex.getMessage());
        }
         *//*

        createClient();
        try {
            reader = URLDecoder.decode(reader, "UTF-8");
        } catch (UnsupportedEncodingException ex) {

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
                JDBCUtil jDBCUtil = null;
                try {
                    jDBCUtil = new JDBCUtil();
                    PreparedStatement statement = jDBCUtil
                            .getStatement("select biometric_id, template from biometric");
                    ResultSet rs = statement.executeQuery();
                    NBiometricTask task = client.createTask(
                            EnumSet.of(NBiometricOperation.ENROLL), null);
                    while (rs.next()) {
                        byte[] template = rs.getBytes("template");
                        NSubject gallery = new NSubject();
                        gallery.setTemplateBuffer(new NBuffer(template));
                        gallery.setId(rs.getString("biometric_id"));
                        task.getSubjects().add(gallery);
                    }
                    client.performTask(task);
                    if (task.getStatus().equals(NBiometricStatus.OK)) {
                        status = client.identify(subject);
                        if (status.equals(NBiometricStatus.OK)) {
                            String id = subject.getMatchingResults().get(0).getId();
                            statement = jDBCUtil.getStatement("select p.patient_id, p.facility_id, p.hospital_num from biometric b "
                                    + " inner join patient p on p.id_uuid = b.patient_id where biometric_id = ?");
                            statement.setString(1, id);
                            rs = statement.executeQuery();
                            while (rs.next()) {
                                Long id = rs.getLong("facility_id");
                                String hospitalNum = rs.getString("hospital_num");
                                Long id = rs.getLong("patient_id");
                                BiometricResult result = getResult(id, id);
                                if (!currentFacilityId.equals(id)) {
                                    result.setInFacility(false);
                                    statement = jDBCUtil.getStatement("insert into biometric_transfer "
                                            + "(biometric_transfer_id, hospital_num, transfer_facility_id, facility_id, patient_id, transfer_date)"
                                            + " values(?,?,?,?,?,?)");
                                    statement.setString(1, UUID.randomUUID().toString());
                                    statement.setString(2, hospitalNum);
                                    statement.setLong(3, id);
                                    statement.setLong(4, currentFacilityId);
                                    statement.setString(5, getUUID(id));
                                    statement.setDate(6, new java.sql.Date(new Date().getTime()));
                                    statement.execute();
                                }
                                if (result != null) {
                                    result.setMessage("Patient identified");
                                }
                                dispose(task);
                                LOG.info("Result: {}", result);
                                return result;
                            }
                        }
                    }
                } catch (SQLException ex) {
                    LOG.error("{}", ex.getMessage());
                } finally {
                    dispose(client);
                    if (jDBCUtil != null) {
                        jDBCUtil.disconnectFromDatabase();
                    }
                }
            }
        }
        return null;
    }

    @Path("enrolled-fingers/{patient}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> enrolledFingers(
            @PathParam("patient") Long id, @Context HttpServletRequest request) {
        Long id = (Long) request.getSession().getAttribute("id");
        List<String> fingers = new ArrayList<>();
        try {
            JDBCUtil jDBCUtil = new JDBCUtil();
            PreparedStatement statement = jDBCUtil
                    .getStatement(
                            String.format("select template_type from biometric where facility_id = %s and patient_id = '%s'",
                                    id, getUUID(id)));
            LOG.info("Query: {}", String.format("select template_type from biometric where facility_id = %s and patient_id = '%s'",
                    id, getUUID(id)));
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String finger = rs.getString("template_type");
                LOG.info("Finger: {}", finger);
                switch (finger) {
                    case "LEFT_THUMB":
                        fingers.add("Left Thumb");
                        break;
                    case "RIGHT_THUMB":
                        fingers.add("Right Thumb");
                        break;
                    case "LEFT_INDEX_FINGER":
                        fingers.add("Left Index Finger");
                        break;
                    case "RIGHT_INDEX_FINGER":
                        fingers.add("Right Index Finger");
                        break;
                }
            }
        } catch (SQLException ex) {
            LOG.error("{}", ex.getMessage());
        }

        return fingers;
    }

    @Path("verify/{reader}/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public BiometricResult verify(@PathParam("id") Long id,
            @PathParam("reader") String reader,
            @Context HttpServletRequest request) {
        createClient();
        try {
            reader = URLDecoder.decode(reader, "UTF-8");
        } catch (UnsupportedEncodingException ex) {

        }
        Long id = (Long) request.getSession().getAttribute("id");
        NSubject subject = new NSubject();
        subject.setId(getUUID(id));
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
                try {
                    JDBCUtil jDBCUtil = new JDBCUtil();
                    try {
                        PreparedStatement statement = jDBCUtil.getStatement("select biometric_id, template,"
                                + "template_type from biometric where patient_id = ? and facility_id = ?");
                        statement.setString(1, getUUID(id));
                        statement.setLong(2, id);
                        ResultSet rs = statement.executeQuery();

                        while (rs.next()) {
                            byte[] template = rs.getBytes("template");
                            String id = rs.getString("biometric_id");
                            //FMRecord record = new FMRecord(new NBuffer(template), BDIFStandard.UNSPECIFIED);
                            //referenceSubject.setTemplate(record);
                            //referenceSubject.setTemplate(value);
                            NSubject referenceSubject = new NSubject();
                            referenceSubject.setId(id);
                            referenceSubject.setTemplateBuffer(new NBuffer(template));
                            status = client.verify(subject, referenceSubject);
                            if (status.equals(NBiometricStatus.OK)) {//
                                BiometricResult result = getResult(id, id);
                                if (result != null) {
                                    result.setMessage("Patient Verified");
                                }
                                return result;
                            }
                        }

                    } catch (SQLException ex) {
                        LOG.error("{}", ex.getMessage());
                    }
                } finally {
                    dispose(client);
                }
            }
        }

        return null;
    }

    @Path("enrol/{reader}/{id}/{finger}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public BiometricResult enrol(@PathParam("id") Long id,
            @PathParam("reader") String reader,
            @PathParam("finger") String type,
            @Context HttpServletRequest request) {

        createClient();
        try {
            reader = URLDecoder.decode(reader, "UTF-8");
        } catch (UnsupportedEncodingException ex) {

        }

        Long id = (Long) request.getSession().getAttribute("id");
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
                JDBCUtil jDBCUtil = null;
                try {
                    jDBCUtil = new JDBCUtil();
                    PreparedStatement statement = jDBCUtil.getStatement("select biometric_id from biometric where facility_id = ? "
                            + "and patient_id = ? and template_type = ?");
                    statement.setLong(1, id);
                    statement.setString(2, getUUID(id));
                    statement.setString(3, position.name());
                    ResultSet rs = statement.executeQuery();
                    boolean update = false;
                    String id = null;
                    while (rs.next()) {
                        update = true;
                        id = rs.getString("biometric_id");
                    }
                    if (!update) {
                        statement = jDBCUtil.getStatement("select id_uuid, surname, other_names, address, phone, gender,"
                                + " hospital_num from patient where patient_id = ? and facility_id = ?");
                        statement.setLong(1, id);
                        statement.setLong(2, id);
                        rs = statement.executeQuery();
                        while (rs.next()) {
                            String surname = rs.getString("surname");
                            String patientUUID = rs.getString("id_uuid");
                            String otherNames = rs.getString("other_names");
                            String address = rs.getString("address");
                            String phone = rs.getString("phone");
                            String gender = rs.getString("gender");
                            String hospitalNum = rs.getString("hospital_num");
                            statement = jDBCUtil.getStatement("select enrollment_date from biometric where patient_id = ?");
                            statement.setString(1, getUUID(id));
                            rs = statement.executeQuery();
                            Date enrollmentDate = new Date();
                            while (rs.next()) {
                                enrollmentDate = rs.getDate("enrollment_date");
                            }
                            statement = jDBCUtil.getStatement("insert into biometric (biometric_id, facility_id, patient_id, template,"
                                    + "template_type, biometric_type, patient_name, patient_address, patient_phone, patient_gender, hospital_num, enrollment_date) "
                                    + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                            statement.setString(1, UUID.randomUUID().toString());
                            statement.setLong(2, id);
                            statement.setString(3, patientUUID);
                            statement.setBytes(4, template);
                            statement.setString(5, position.name());
                            statement.setString(6, "FINGERPRINT");
                            statement.setString(7, StringUtils.trimToEmpty(otherNames)
                                    + " " + StringUtils.trimToEmpty(surname));
                            statement.setString(8, address);
                            statement.setString(9, phone);
                            statement.setString(10, gender);
                            statement.setString(11, hospitalNum);
                            statement.setDate(12, new java.sql.Date(enrollmentDate.getTime()));
                            statement.execute();

                            BiometricResult result = getResult(id, id);
                            if (result != null) {
                                result.setMessage("Patient Enrolled");
                            }
                            return result;
                        }
                    } else {
                        statement = jDBCUtil.getStatement("update biometric set template = ? where biometric_id = ?");
                        statement.setBytes(1, template);
                        statement.setString(2, id);
                        statement.execute();
                        BiometricResult result = getResult(id, id);
                        if (result != null) {
                            result.setMessage("Patient Enrolled");
                        }
                        return result;
                    }
                } catch (SQLException ex) {
                    LOG.error("{}", ex.getMessage());
                } finally {
                    dispose(client);
                    if (jDBCUtil != null) {
                        jDBCUtil.disconnectFromDatabase();
                    }
                }
            }
        }
        return null;
    }

    private BiometricResult getResult(Long id, Long id) {
        try {
            Scrambler scrambler = new Scrambler();
            PreparedStatement statement = new JDBCUtil()
                    .getStatement("select surname, other_names, address, phone, gender"
                            + " from patient where patient_id = ? and facility_id = ?");
            statement.setLong(1, id);
            statement.setLong(2, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String surname = rs.getString("surname");
                String otherNames = rs.getString("other_names");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                String gender = rs.getString("gender");
                BiometricResult result = new BiometricResult();
                result.setId(id);
                result.setId(id);
                result.setPhone(scrambler.unscrambleCharacters(phone));
                result.setAddress(scrambler.unscrambleCharacters(address));
                result.setGender(gender);
                result.setName(scrambler.unscrambleCharacters(StringUtils.trimToEmpty(otherNames))
                        + " " + scrambler.unscrambleCharacters(StringUtils.trimToEmpty(surname)));
                return result;
            }

        } catch (SQLException ex) {
            LOG.error("{}", ex.getMessage());
        }
        return null;
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

    @PostConstruct
    public void initialize() {
        StringBuilder path = new StringBuilder();
        path.append("C:\\LAMIS2\\neurotec\\license").append(FILE_SEPARATOR);
        LibraryManager.initLibraryPath();
        try {
            URLConnection connection = new URL("http://google.com").openConnection();
            try {
                LOG.info("Connecting...");
                connection.connect();
            } catch (IOException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        } catch (MalformedURLException ex) {
            java.util.logging.Logger.getLogger(FingerprintResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FingerprintResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        NLicenseManager.initialize();
        try {
            NLicense.add(String.join("\n", Files.readAllLines(
                    Paths.get(path.toString() + "matcher.txt"), Charset.defaultCharset())));
            */
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
            
             *//*

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
             *//*

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
*/
