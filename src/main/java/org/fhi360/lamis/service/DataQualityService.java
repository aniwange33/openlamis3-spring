/*
 * To change this template, choose Tools | Templates
/**
 *
 * @author aalozie
 */
package org.fhi360.lamis.service;

import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.Scrambler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class DataQualityService {
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String query;

    private String filename;
    private long userId;
    private Boolean viewIdentifier;
    private Scrambler scrambler;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private String[][] data = new String[28][3];
    private String facilityIds;
    private ArrayList<Map<String, Object>> elementList;
    private ArrayList<Map<String, Object>> reportList;

    private final ApplicationProperties applicationProperties;

    public DataQualityService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        try {
            this.elementList = new ArrayList<>();
            this.reportList = new ArrayList<>();
           /* Long id = (Long) session.getAttribute("id");
            this.id = (Long) session.getAttribute("id");
           *//* if (facilityIds != null) {
                facilityIds = facilityIds;
            }
            else {
                facilityIds = id;
            }*/
            this.scrambler = new Scrambler();
            /*if (session.getAttribute("viewIdentifier") != null) {
                this.viewIdentifier = (Boolean) session.getAttribute("viewIdentifier");
            }*/
            initELement();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public ArrayList<Map<String, Object>> analysis(int analysisOption) {
        System.out.println("DQA running......");
        filename = "dqa" + Long.toString(userId);
        query = "DROP TABLE IF EXISTS " + filename;
        executeUpdate(query);
        query = "CREATE TEMPORARY TABLE " + filename + " (hospital_num VARCHAR(25), surname VARCHAR(45), other_names VARCHAR(75), date_trans DATE, element_id INT)";
        executeUpdate(query);
        System.out.println("Analysis...." + analysisOption);
        for (int i = 0; i < data.length; i++) {
            query = (analysisOption == 2) ? data[i][1] : data[i][2];
            System.out.println("Analysis...." + query);
            Map map = new HashMap();
            map.put("element", data[i][0]);
            map.put("total", getCount(query, i + 1));
            map.put("elementId", i + 1);
            elementList.add(map);
        }
        return elementList;
    }

    public ArrayList<Map<String, Object>> dqaReport(int elementId) {
        int i = elementId;
        filename = "dqa" + Long.toString(userId);
        try {
            jdbcUtil = new JDBCUtil();
            query = "SELECT * FROM " + filename + " WHERE element_id = " + i;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String surname = (viewIdentifier) ? scrambler.unscrambleCharacters(resultSet.getString("surname")) : resultSet.getString("surname");
                surname = StringUtils.upperCase(surname);
                String otherNames = (viewIdentifier) ? scrambler.unscrambleCharacters(resultSet.getString("other_names")) : resultSet.getString("other_names");
                otherNames = StringUtils.capitalize(otherNames);

                Map map = new HashMap();
                map.put("hospitalNum", resultSet.getString("hospital_num"));
                map.put("name", surname + " " + otherNames);
                map.put("dateTrans", (resultSet.getDate("date_trans") == null) ? "" : dateFormat.format(resultSet.getDate("date_trans")));
                reportList.add(map);
            }
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return reportList;
    }

    public HashMap getReportParameters(int elementId) {
        int i = elementId;
        if (i > 0) i = i - 1;
        HashMap parameterMap = new HashMap();
        parameterMap.put("reportTitle", data[i][0]);
        return parameterMap;
    }

    private void initELement() {
        data[0][0] = "ART commencement record with no original regimen";
        data[0][1] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND (clinic.regimenType IS NULL OR clinic.regimenType = '') AND clinic.commence = 1";
        data[0][2] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND (clinic.regimenType IS NULL OR clinic.regimenType = '') AND clinic.commence = 1 AND MONTH(clinic.time_stamp) = MONTH(CURDATE()) AND YEAR(clinic.time_stamp) = YEAR(CURDATE())";

        data[1][0] = "ART commencement record with no CD4 count";
        data[1][1] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND (clinic.cd4 IS NULL OR clinic.cd4 = '' OR clinic.cd4p IS NULL OR clinic.cd4p = '') AND clinic.commence = 1";
        data[1][2] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND (clinic.cd4 IS NULL OR clinic.cd4 = '' OR clinic.cd4p IS NULL OR clinic.cd4p = '') AND clinic.commence = 1 AND MONTH(clinic.time_stamp) = MONTH(CURDATE()) AND YEAR(clinic.time_stamp) = YEAR(CURDATE())";

        data[2][0] = "ART commencement record with no clinic stage";
        data[2][1] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND (clinic.clinic_stage IS NULL OR clinic.clinic_stage = '') AND clinic.commence = 1";
        data[2][2] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND (clinic.clinic_stage IS NULL OR clinic.clinic_stage = '') AND clinic.commence = 1 AND MONTH(clinic.time_stamp) = MONTH(CURDATE()) AND YEAR(clinic.time_stamp) = YEAR(CURDATE())";

        data[3][0] = "ART commencement record with no functional status";
        data[3][1] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND (clinic.func_status IS NULL OR clinic.func_status = '') AND clinic.commence = 1";
        data[3][2] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND (clinic.func_status IS NULL OR clinic.func_status = '') AND clinic.commence = 1 AND MONTH(clinic.time_stamp) = MONTH(CURDATE()) AND YEAR(clinic.time_stamp) = YEAR(CURDATE())";

        data[4][0] = "ART commencement record with no body weight";
        data[4][1] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND clinic.body_weight = 0.0 AND clinic.commence = 1";
        data[4][2] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND clinic.body_weight = 0.0 AND clinic.commence = 1 AND MONTH(clinic.time_stamp) = MONTH(CURDATE()) AND YEAR(clinic.time_stamp) = YEAR(CURDATE())";

        data[5][0] = "Clinic records with no clinic stage";
        data[5][1] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND (clinic.clinic_stage IS NULL OR clinic.clinic_stage = '') AND clinic.commence = 0";
        data[5][2] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND (clinic.clinic_stage IS NULL OR clinic.clinic_stage = '') AND clinic.commence = 0 AND MONTH(clinic.time_stamp) = MONTH(CURDATE()) AND YEAR(clinic.time_stamp) = YEAR(CURDATE())";

        data[6][0] = "Clinic records with no functional status";
        data[6][1] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND (clinic.func_status IS NULL OR clinic.func_status = '') AND clinic.commence = 0";
        data[6][2] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND (clinic.func_status IS NULL OR clinic.func_status = '') AND clinic.commence = 0 AND MONTH(clinic.time_stamp) = MONTH(CURDATE()) AND YEAR(clinic.time_stamp) = YEAR(CURDATE())";

        data[7][0] = "Clinic records with no body weight";
        data[7][1] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND clinic.body_weight = 0.0 AND clinic.commence = 0";
        data[7][2] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND clinic.body_weight = 0.0 AND clinic.commence = 0 AND MONTH(clinic.time_stamp) = MONTH(CURDATE()) AND YEAR(clinic.time_stamp) = YEAR(CURDATE())";

        data[8][0] = "Clinic records with pregnant status +ve but no lmp";
        data[8][1] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND clinic.pregnant = 1 AND clinic.lmp IS NULL AND clinic.commence = 0";
        data[8][2] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND clinic.pregnant = 1 AND clinic.lmp IS NULL AND clinic.commence = 0 AND MONTH(clinic.time_stamp) = MONTH(CURDATE()) AND YEAR(clinic.time_stamp) = YEAR(CURDATE())";

        data[9][0] = "Clinic records with no date of next appointment";
        data[9][1] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND clinic.next_appointment IS NULL AND clinic.commence = 0";
        data[9][2] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, clinic.date_visit AS date_trans FROM patient "
                + " JOIN clinic ON patient.patient_id = clinic.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND clinic.facility_id IN (" + facilityIds + ") AND clinic.next_appointment IS NULL AND clinic.commence = 0 AND MONTH(clinic.time_stamp) = MONTH(CURDATE()) AND YEAR(clinic.time_stamp) = YEAR(CURDATE())";

        data[10][0] = "Pharmacy records with no date of next refill";
        data[10][1] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, pharmacy.date_visit AS date_trans FROM patient "
                + " JOIN pharmacy ON patient.patient_id = pharmacy.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND pharmacy.facility_id IN (" + facilityIds + ") AND pharmacy.next_appointment IS NULL";
        data[10][2] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, pharmacy.date_visit AS date_trans FROM patient "
                + " JOIN pharmacy ON patient.patient_id = pharmacy.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND pharmacy.facility_id IN (" + facilityIds + ") AND pharmacy.next_appointment IS NULL AND MONTH(pharmacy.time_stamp) = MONTH(CURDATE()) AND YEAR(pharmacy.time_stamp) = YEAR(CURDATE())";

        data[11][0] = "Lab records with no date of sample collection";
        data[11][1] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, laboratory.date_reported AS date_trans FROM patient "
                + " JOIN laboratory ON patient.patient_id = laboratory.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND laboratory.facility_id IN (" + facilityIds + ") AND laboratory.date_collected IS NULL";
        data[11][2] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, laboratory.date_reported AS date_trans FROM patient "
                + " JOIN laboratory ON patient.patient_id = laboratory.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND laboratory.facility_id IN (" + facilityIds + ") AND laboratory.date_collected IS NULL AND MONTH(laboratory.time_stamp) = MONTH(CURDATE()) AND YEAR(laboratory.time_stamp) = YEAR(CURDATE())";

        data[12][0] = "Lab records with no value recorded";
        data[12][1] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, laboratory.date_reported AS date_trans FROM patient "
                + " JOIN laboratory ON patient.patient_id = laboratory.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND laboratory.facility_id IN (" + facilityIds + ") AND laboratory.resultab IS NULL AND laboratory.resultpc IS NULL";
        data[12][2] = "SELECT DISTINCT patient.patient_id, patient.hospital_num, patient.surname, patient.other_names, laboratory.date_reported AS date_trans FROM patient "
                + " JOIN laboratory ON patient.patient_id = laboratory.patient_id WHERE patient.facility_id IN (" + facilityIds + ") AND laboratory.facility_id IN (" + facilityIds + ") AND laboratory.resultab IS NULL AND laboratory.resultpc IS NULL AND MONTH(laboratory.time_stamp) = MONTH(CURDATE()) AND YEAR(laboratory.time_stamp) = YEAR(CURDATE())";

        data[13][0] = "Lost to Follow Up with records in clinic after date tracked";
        data[13][1] = "SELECT DISTINCT patient_id,hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Lost to Follow Up' AND date_last_clinic > date_current_status";
        data[13][2] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Lost to Follow Up' AND date_last_clinic > date_current_status AND MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE())";

        data[14][0] = "Lost to Follow Up with records in pharmacy after date tracked";
        data[14][1] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Lost to Follow Up' AND date_last_refill > date_current_status";
        data[14][2] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Lost to Follow Up' AND date_last_refill > date_current_status AND MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE())";

        data[15][0] = "Lost to Follow Up with records in lab after date of tracked";
        data[15][1] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Lost to Follow Up' AND date_last_cd4 > date_current_status";
        data[15][2] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Lost to Follow Up' AND date_last_cd4 > date_current_status AND MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE())";

        data[16][0] = "Stopped Treatment with records in clinic after date of treatment stop";
        data[16][1] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Stopped Treatment' AND date_last_clinic > date_current_status";
        data[16][2] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Stopped Treatment' AND date_last_clinic > date_current_status AND MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE())";

        data[17][0] = "Stopped Treatment with records in pharmacy after date of treatment stop";
        data[17][1] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Stopped Treatment' AND date_last_refill > date_current_status";
        data[17][2] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Stopped Treatment' AND date_last_refill > date_current_status AND MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE())";

        data[18][0] = "Stopped Treatment with records in lab after date of treatment stop";
        data[18][1] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Stopped Treatment' AND date_last_cd4 > date_current_status";
        data[18][2] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Stopped Treatment' AND date_last_cd4 > date_current_status AND MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE())";

        data[19][0] = "Known Deaths with records in clinic after date of death";
        data[19][1] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Known Death' AND date_last_clinic > date_current_status";
        data[19][2] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Known Death' AND date_last_clinic > date_current_status AND MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE())";

        data[20][0] = "Known Deaths with records in pharmacy after date of death";
        data[20][1] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Known Death' AND date_last_refill > date_current_status";
        data[20][2] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Known Death' AND date_last_refill > date_current_status AND MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE())";

        data[21][0] = "Known Deaths with records in lab after date of death";
        data[21][1] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Known Death' AND date_last_cd4 > date_current_status";
        data[21][2] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status = 'Known Death' AND date_last_cd4 > date_current_status AND MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE())";

        data[22][0] = "Transfer Outs with records in clinic after date of transfer";
        data[22][1] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status IN ('ART Transfer Out', 'Pre-ART Transfer Out') AND date_last_clinic > date_current_status";
        data[22][2] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status IN ('ART Transfer Out', 'Pre-ART Transfer Out') AND date_last_clinic > date_current_status AND MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE())";

        data[23][0] = "Transfer Out with record in pharmacy after date of transfer";
        data[23][1] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status IN ('ART Transfer Out', 'Non-ART Transfer Out') AND date_last_refill > date_current_status";
        data[23][2] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status IN ('ART Transfer Out', 'Non-ART Transfer Out') AND date_last_refill > date_current_status AND MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE())";

        data[24][0] = "Transfer Out with record in lab after date of transfer";
        data[24][1] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status IN ('ART Transfer Out', 'Non-ART Transfer Out') AND date_last_cd4 > date_current_status";
        data[24][2] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND current_status IN ('ART Transfer Out', 'Non-ART Transfer Out') AND date_last_cd4 > date_current_status AND MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE())";

        data[25][0] = "Patient records without telephone numbers";
        data[25][1] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND (phone IS NULL OR phone = '')";
        data[25][2] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND (phone IS NULL OR phone = '') AND MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE())";

        data[26][0] = "Patient records without address";
        data[26][1] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND (address IS NULL OR address = '')";
        data[26][2] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND (address IS NULL OR address = '') AND MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE())";

        data[27][0] = "Patient records without treatment supporters phone number";
        data[27][1] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND (phone_kin IS NULL OR phone_kin = '')";
        data[27][2] = "SELECT DISTINCT patient_id, hospital_num, surname, other_names, date_registration AS date_trans FROM patient WHERE facility_id IN (" + facilityIds + ") AND (phone_kin IS NULL OR phone_kin = '') AND MONTH(time_stamp) = MONTH(CURDATE()) AND YEAR(time_stamp) = YEAR(CURDATE())";
    }

    private void executeUpdate(String query) {
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }

    private int getCount(String query, int stateId) {
        int count = 0;
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String hospital_num = resultSet.getString("hospital_num");
                String surname = resultSet.getString("surname");
                String other_names = resultSet.getString("other_names");
                Date date_trans = resultSet.getDate("date_trans");
                query = "INSERT INTO " + filename + " (hospital_num, surname, other_names, date_trans, element_id) VALUES('" + hospital_num + "', '" + surname + "', '" + other_names + "', '" + date_trans + "', " + stateId + ")";
                executeUpdate(query);
                count++;
            }
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return count;
    }

}
