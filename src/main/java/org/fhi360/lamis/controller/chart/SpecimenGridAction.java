/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.chart;

import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.service.parser.json.LabFileParser;

import org.fhi360.lamis.utility.JDBCUtil;
import org.fhi360.lamis.utility.Scrambler;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

;import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class SpecimenGridAction {

    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private final LabFileParser labFileParser;

    public SpecimenGridAction(LabFileParser labFileParser) {
        this.labFileParser = labFileParser;
    }

    private ArrayList<Map<String, String>> specimenList = new ArrayList<Map<String, String>>();

    @GetMapping("specimenGrid")
    public Map<String, String> specimenGrid(@RequestParam("page") int page,
                                            @RequestParam("rows") int rows,
                                            HttpSession session,
                                            @RequestParam("labno") String labno
    ) {
        Scrambler scrambler = new Scrambler();
        Map<String, Object> pagerParams = new PaginationUtil().paginateGrid(page, rows, "specimen");
        int start = (Integer) pagerParams.get("start");
        int numberOfRows = rows;
        long facilityId = (Long) session.getAttribute("id");
        try {
            if (labno != null) {
                query = "SELECT specimen.specimen_id, specimen.specimen_type, specimen.labno, specimen.barcode, specimen.facility_id, specimen.state_id, specimen.lga_id, specimen.treatment_unit_id, specimen.date_received, specimen.date_collected, specimen.date_assay, specimen.date_reported, specimen.date_dispatched, specimen.quality_cntrl, "
                        + " specimen.hospital_num, specimen.result, specimen.reason_no_test, specimen.surname, specimen.other_names, specimen.gender, specimen.date_birth, specimen.age, specimen.age_unit, specimen.address, specimen.phone, specimen.time_stamp, facility.name FROM specimen "
                        + " JOIN facility ON specimen.treatment_unit_id = facility.facility_id WHERE specimen.facility_id = " + facilityId + " AND specimen.labno LIKE '" + labno + "%' ORDER BY specimen.labno ASC LIMIT " + start + " , " + numberOfRows;
            } else {
                query = "SELECT specimen.specimen_id, specimen.specimen_type, specimen.labno, specimen.barcode, specimen.facility_id, specimen.state_id, specimen.lga_id, specimen.treatment_unit_id, specimen.date_received, specimen.date_collected, specimen.date_assay, specimen.date_reported, specimen.date_dispatched, specimen.quality_cntrl, "
                        + " specimen.hospital_num, specimen.result, specimen.reason_no_test, specimen.surname, specimen.other_names, specimen.gender, specimen.date_birth, specimen.age, specimen.age_unit, specimen.address, specimen.phone, specimen.time_stamp, facility.name FROM specimen "
                        + " JOIN facility ON specimen.treatment_unit_id = facility.facility_id WHERE specimen.facility_id = " + facilityId + " ORDER BY labno ASC LIMIT " + start + " , " + numberOfRows;
            }
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()) {
                String specimenId = Long.toString(resultSet.getLong("specimen_id"));
                String specimenType = resultSet.getString("specimen_type");

                String barcode = (resultSet.getString("barcode") == null) ? "" : resultSet.getString("barcode");
                String facilityName = (resultSet.getString("name") == null) ? "" : resultSet.getString("name");
                String stateId = Long.toString(resultSet.getLong("state_id"));
                String lgaId = Long.toString(resultSet.getLong("lga_id"));
                String treatmentUnitId = Long.toString(resultSet.getLong("treatment_unit_id"));
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String dateReceived = (resultSet.getDate("date_received") == null) ? "" : dateFormat.format(resultSet.getDate("date_received"));
                String dateCollected = (resultSet.getDate("date_collected") == null) ? "" : dateFormat.format(resultSet.getDate("date_collected"));
                String dateAssay = (resultSet.getDate("date_assay") == null) ? "" : dateFormat.format(resultSet.getDate("date_assay"));
                String dateReported = (resultSet.getDate("date_reported") == null) ? "" : dateFormat.format(resultSet.getDate("date_reported"));
                String dateDispatched = (resultSet.getDate("date_dispatched") == null) ? "" : dateFormat.format(resultSet.getDate("date_dispatched"));
                String qualityCntrl = (resultSet.getInt("quality_cntrl") == 0) ? "" : Integer.toString(resultSet.getInt("quality_cntrl"));
                String hospitalNum = (resultSet.getDate("hospital_num") == null) ? "" : resultSet.getString("hospital_num");
                String result = (resultSet.getDate("result") == null) ? "" : resultSet.getString("result");
                String reasonNoTest = (resultSet.getDate("reason_no_test") == null) ? "" : resultSet.getString("reason_no_test");
                String surname = resultSet.getString("surname") == null ? "" : resultSet.getString("surname");
                surname = scrambler.unscrambleCharacters(surname);
                surname = StringUtils.upperCase(surname);
                String otherNames = resultSet.getString("other_names") == null ? "" : resultSet.getString("other_names");
                otherNames = scrambler.unscrambleCharacters(otherNames);
                otherNames = StringUtils.capitalize(otherNames);
                String gender = resultSet.getString("gender") == null ? "" : resultSet.getString("gender");
                String dateBirth = (resultSet.getDate("date_birth") == null) ? "" : dateFormat.format(resultSet.getDate("date_birth"));
                String age = (resultSet.getInt("age") == 0) ? "" : Integer.toString(resultSet.getInt("age"));
                String ageUnit = resultSet.getString("age_unit") == null ? "" : resultSet.getString("age_unit");
                String address = resultSet.getString("address") == null ? "" : resultSet.getString("address");
                address = scrambler.unscrambleCharacters(address);
                address = StringUtils.capitalize(address);
                String phone = resultSet.getString("phone") == null ? "" : resultSet.getString("phone");
                phone = scrambler.unscrambleNumbers(phone);
                String timeStamp = (resultSet.getDate("time_stamp") == null) ? "" : dateFormat.format(resultSet.getDate("time_stamp"));

                Map<String, String> map = new HashMap<String, String>();
                map.put("specimenId", specimenId);
                map.put("specimenType", specimenType);
                map.put("labno", labno);
                map.put("barcode", barcode);
                map.put("facilityId", String.valueOf(facilityId));
                map.put("facilityName", facilityName);
                map.put("stateId", stateId);
                map.put("lgaId", lgaId);
                map.put("treatmentUnitId", treatmentUnitId);
                map.put("dateReceived", dateReceived);
                map.put("dateCollected", dateCollected);
                map.put("dateAssay", dateAssay);
                map.put("dateReported", dateReported);
                map.put("dateDispatched", dateDispatched);
                map.put("qualityCntrl", qualityCntrl);
                map.put("result", result);
                map.put("reasonNoTest", reasonNoTest);
                map.put("hospitalNum", hospitalNum);
                map.put("surname", surname);
                map.put("otherNames", otherNames);
                map.put("name", surname + ' ' + otherNames);
                map.put("gender", gender);
                map.put("dateBirth", dateBirth);
                map.put("age", age);
                map.put("ageUnit", ageUnit);
                map.put("address", address);
                map.put("phone", phone);
                map.put("timeStamp", timeStamp);
                specimenList.add(map);
            }
            session.setAttribute("specimenList", specimenList);

            resultSet = null;
            specimenList = null;
        } catch (Exception sqlException) {
            resultSet = null;

        }
        return null;
    }


    @GetMapping("/resultProcessorGrid")
    public String resultProcessorGrid(HttpServletRequest request, HttpSession session) {
        //setTotalpages(1);
        //setCurrpage(1);
        //setTotalrecords(20);
        Scrambler scrambler = new Scrambler();
        int fileUploaded = 0;
        if (request.getSession().getAttribute("fileUploaded") != null) {
            fileUploaded = (Integer) request.getSession().getAttribute("fileUploaded");
        }
        if (fileUploaded == 1) {
            try {
                labFileParser.parseFile((String) request.getSession().getAttribute("fileName"));
                if (request.getParameterMap().containsKey("discard")) executeUpdate("DELETE FROM labresult");

                query = "SELECT specimen.specimen_id, specimen.labno, specimen.barcode, specimen.date_received, specimen.hospital_num, specimen.surname, specimen.other_names, labresult.result, facility.name FROM specimen "
                        + " JOIN labresult ON specimen.labno = labresult.labno JOIN facility ON specimen.treatment_unit_id = facility.facility_id WHERE specimen.labno = labresult.labno";
                jdbcUtil = new JDBCUtil();
                preparedStatement = jdbcUtil.getStatement(query);
                resultSet = preparedStatement.executeQuery();

                resultSet.beforeFirst();
                while (resultSet.next()) {
                    String specimenId = Long.toString(resultSet.getLong("specimen_id"));
                    String labno = resultSet.getString("labno");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    String dateReceived = (resultSet.getDate("date_received") == null) ? "" : dateFormat.format(resultSet.getDate("date_received"));
                    String hospitalNum = (resultSet.getString("hospital_num") == null) ? "" : resultSet.getString("hospital_num");
                    String facilityName = (resultSet.getString("name") == null) ? "" : resultSet.getString("name");
                    String result = (resultSet.getString("result") == null) ? "" : resultSet.getString("result");
                    String surname = resultSet.getString("surname") == null ? "" : resultSet.getString("surname");
                    surname = scrambler.unscrambleCharacters(surname);
                    surname = StringUtils.upperCase(surname);
                    String otherNames = resultSet.getString("other_names") == null ? "" : resultSet.getString("other_names");
                    otherNames = scrambler.unscrambleCharacters(otherNames);
                    otherNames = StringUtils.capitalize(otherNames);

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("specimenId", specimenId);
                    map.put("labno", labno);
                    map.put("dateReceived", dateReceived);
                    map.put("result", result);
                    map.put("hospitalNum", hospitalNum);
                    map.put("facilityName", facilityName);
                    map.put("surname", surname);
                    map.put("otherNames", otherNames);
                    map.put("name", surname + ' ' + otherNames);
                    map.put("sel", "1");
                    specimenList.add(map);
                }
                session.setAttribute("specimenList", specimenList);
                resultSet = null;
            } catch (Exception exception) {
                resultSet = null;
                jdbcUtil.disconnectFromDatabase();  //disconnect from database
            }
        }
        return "SUCCESS";
    }

    @GetMapping("/resultDispatcherGrid")
    public String resultDispatcherGrid(@RequestParam("treatmentUnitId")
                                               long treatmentUnitId,
                                       @RequestParam("dispatched")
                                               String dispatched,
                                       HttpSession session,
                                       @RequestParam("rows") int
                                               rows, @RequestParam("page") int page
    ) {
        //setTotalpages(1);
        //setCurrpage(1);
        //setTotalrecords(20);
        Scrambler scrambler = new Scrambler();
        executeUpdate("DROP VIEW IF EXISTS dispatcher");
        if (dispatched != null) {
            query = " CREATE VIEW dispatcher AS SELECT specimen.specimen_id, specimen.labno, specimen.barcode, specimen.date_received, specimen.date_reported, specimen.date_assay, specimen.hospital_num, specimen.surname, specimen.other_names, specimen.gender, specimen.result, facility.name, facility.phone2 FROM specimen "
                    + " JOIN facility ON specimen.treatment_unit_id = facility.facility_id WHERE specimen.result != '' AND specimen.result IS NOT NULL AND specimen.date_dispatched IS NOT NULL";
            if (treatmentUnitId != 0) {
                query = " CREATE VIEW dispatcher AS SELECT specimen.specimen_id, specimen.labno, specimen.barcode, specimen.date_received, specimen.date_reported, specimen.date_assay, specimen.hospital_num, specimen.surname, specimen.other_names, specimen.gender, specimen.result, facility.name, facility.phone2 FROM specimen "
                        + " JOIN facility ON specimen.treatment_unit_id = facility.facility_id WHERE specimen.treatment_unit_id = " + treatmentUnitId + " AND specimen.result != '' AND specimen.result IS NOT NULL AND specimen.date_dispatched IS NOT NULL";
            }
        } else {
            query = " CREATE VIEW dispatcher AS SELECT specimen.specimen_id, specimen.labno, specimen.barcode, specimen.date_received, specimen.date_reported, specimen.date_assay, specimen.hospital_num, specimen.surname, specimen.other_names, specimen.gender, specimen.result, facility.name, facility.phone2 FROM specimen "
                    + " JOIN facility ON specimen.treatment_unit_id = facility.facility_id WHERE specimen.result != '' AND specimen.result IS NOT NULL AND specimen.date_dispatched IS NULL";
            if (treatmentUnitId != 0) {
                System.out.println("Select result not dispatched for specific site");
                query = " CREATE VIEW dispatcher AS SELECT specimen.specimen_id, specimen.labno, specimen.barcode, specimen.date_received, specimen.date_reported, specimen.date_assay, specimen.hospital_num, specimen.surname, specimen.other_names, , specimen.gender, specimen.result, facility.name, facility.phone2 FROM specimen "
                        + " JOIN facility ON specimen.treatment_unit_id = facility.facility_id WHERE specimen.treatment_unit_id = " + treatmentUnitId + " AND specimen.result != '' AND specimen.result IS NOT NULL AND specimen.date_dispatched IS NULL";
            }
        }
        executeUpdate(query);
        try {
            query = "SELECT * FROM dispatcher";
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()) {
                String specimenId = Long.toString(resultSet.getLong("specimen_id"));
                String labno = resultSet.getString("labno");
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String dateReceived = (resultSet.getDate("date_received") == null) ? "" : dateFormat.format(resultSet.getDate("date_received"));
                String hospitalNum = (resultSet.getString("hospital_num") == null) ? "" : resultSet.getString("hospital_num");
                String facilityName = (resultSet.getString("name") == null) ? "" : resultSet.getString("name");
                String result = (resultSet.getString("result") == null) ? "" : resultSet.getString("result");
                String surname = resultSet.getString("surname") == null ? "" : resultSet.getString("surname");
                surname = scrambler.unscrambleCharacters(surname);
                surname = StringUtils.upperCase(surname);
                String otherNames = resultSet.getString("other_names") == null ? "" : resultSet.getString("other_names");
                otherNames = scrambler.unscrambleCharacters(otherNames);
                otherNames = StringUtils.capitalize(otherNames);

                Map<String, String> map = new HashMap<String, String>();
                map.put("specimenId", specimenId);
                map.put("labno", labno);
                map.put("dateReceived", dateReceived);
                map.put("result", result);
                map.put("hospitalNum", hospitalNum);
                map.put("facilityName", facilityName);
                map.put("surname", surname);
                map.put("otherNames", otherNames);
                map.put("name", surname + ' ' + otherNames);
                map.put("sel", "1");
                specimenList.add(map);
            }
            session.setAttribute("specimenList", specimenList);
            resultSet = null;
            specimenList = null;
        } catch (Exception exception) {
            resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return "SUCCESS";
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

}
