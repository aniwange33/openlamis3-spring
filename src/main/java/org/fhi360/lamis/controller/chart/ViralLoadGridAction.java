
package org.fhi360.lamis.controller.chart;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import io.swagger.annotations.Api;
import org.fhi360.lamis.utility.Scrambler;
import org.fhi360.lamis.utility.builder.ViralLoadListBuilder;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chart/viralLoadGridAction")
@Api(tags = "ViralLoadGridAction Chart", description = " ")
public class ViralLoadGridAction {
    private final JdbcTemplate jdbcTemplate;
    private ArrayList<Map<String, String>> viralLoadList = new ArrayList<Map<String, String>>();
    long facilityId;

    public ViralLoadGridAction(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    String query = "";
     @GetMapping("/viralLoadGridRetrieve")
    public void viralLoadGridRetrieve(HttpSession session, @Param("page") int page, @Param("row") int row) {
        facilityId = (Long) session.getAttribute("id");
        Map<String, Object> pagerParams = new PaginationUtil().paginateGrid(page, row, "patient");
        int start = (Integer) pagerParams.get("start");
        int numberOfRows = row;
            //The global table...
            query = "SELECT patient_id, facility_id, hospital_num, surname, other_names, phone, date_started FROM patient WHERE current_status IN('ART Start', 'ART Restart', 'ART Transfer In') AND date_started IS NOT NULL ORDER BY date_started DESC LIMIT " + start + ", " + numberOfRows;
            SqlRowSet resultSet = jdbcTemplate.queryForRowSet(query);
            while (resultSet.next()) {
                try {
                    Integer sn;
                    if (session.getAttribute("currpage") != null) {
                        sn = ((Integer) session.getAttribute("currpage") * 100) + 1;
                    } else {
                        sn = 1;
                    }
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    // loop through resultSet for each row and put into Map
                    resultSet.beforeFirst();
                    while (resultSet.next()) { //patient_id, facility_id, hospital_num, surname, other_names, phone, date_started
                        String dateStarted = (resultSet.getDate("date_started") == null) ? "" : dateFormat.format(resultSet.getDate("date_started"));
                        String facilityId = (resultSet.getString("facility_id") == null) ? "" : resultSet.getString("facility_id");
                        String surname = (resultSet.getString("surname") == null) ? "" : resultSet.getString("surname");
                        String otherNames = (resultSet.getString("other_names") == null) ? "" : resultSet.getString("other_names");
                        String hospitalNum = (resultSet.getString("hospital_num") == null) ? "" : resultSet.getString("hospital_num");
                        String patientId = (resultSet.getString("patient_id") == null) ? "" : resultSet.getString("patient_id");
                        String phone = (resultSet.getString("phone") == null) ? "" : resultSet.getString("phone");

                        Map<String, String> map = new HashMap<String, String>();
                        Scrambler scrambler = new Scrambler();
                        String name = scrambler.unscrambleCharacters(surname).toUpperCase() + " " + scrambler.unscrambleCharacters(otherNames).toUpperCase();
                        map.put("sn", String.valueOf(sn));
                        map.put("patientId", patientId);
                        map.put("facilityId", facilityId);
                        map.put("hospitalNum", hospitalNum);
                        map.put("name", name);
                        map.put("phone", scrambler.unscrambleNumbers(phone));
                        map.put("dateStarted", dateStarted);
                        map.put("eac", "");
                        map.put("repeatVl", "");
                        String query;
                        DateFormat selectDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        //For the VL Baseline...
                        if (dateStarted != null && !dateStarted.equals("")) {
                            dateStarted = selectDateFormat.format(resultSet.getDate("date_started"));
                            //System.out.println("Date Started = "+dateStarted);
                            query = "SELECT resultab, date_reported FROM laboratory WHERE labtest_id = 16 AND date_reported >= '" + dateStarted + "' AND patient_id = '" + patientId + "' ORDER BY date_reported ASC LIMIT 1";

                            SqlRowSet rs = jdbcTemplate.queryForRowSet(query);
                            while (rs.next()) {
                                //Fetch the Baseline conditions...
                                if (rs.next()) {
                                    String baselineVl = (rs.getString("resultab") == null) ? "" : rs.getString("resultab");
                                    //System.out.println("VL Count Baseline: "+baselineVl);
                                    map.put("baselineVl", baselineVl);
                                    map.put("dateBaselineVl", selectDateFormat.format(rs.getDate("date_reported")));
                                } else {
                                    map.put("baselineVl", "N/A");
                                    map.put("dateBaselineVl", "N/A");
                                }
                            }
                            //For the Surpressed and UnSurpressed...
                            query = "SELECT resultab, date_reported FROM laboratory WHERE labtest_id = 16 AND date_reported >= '" + dateStarted + "' AND patient_id = '" + patientId + "' ORDER BY date_reported DESC LIMIT 1";

                            SqlRowSet rs1 = jdbcTemplate.queryForRowSet(query);
                            //Fetch the Eligibility conditions...
                            if (rs1.next()) {
                                String vlCount = rs1.getString("resultab");
                                if (vlCount != null && !vlCount.equals("")) {
                                    map.put("currentVl", rs1.getString("resultab"));
                                    map.put("dateCurrentVl", selectDateFormat.format(rs.getDate("date_reported")));
                                    if (vlCount.contains("<")) vlCount = vlCount.substring(1);
                                    if (Integer.parseInt(vlCount) > 1000) map.put("status", "Unsuppressed");
                                    else if (Integer.parseInt(vlCount) < 1000) map.put("status", "Suppressed");
                                } else {

                                    //Eligible Not Done..
                                    query = "SELECT patient_id FROM patient WHERE TIMESTAMPDIFF(MONTH, date_started, CURDATE()) > 6 AND patient_id = '" + patientId + "'";
                                    SqlRowSet rs2 = jdbcTemplate.queryForRowSet(query);

                                    if (rs2.next()) {
                                        map.put("currentVl", "N/A");
                                        map.put("dateCurrentVl", "N/A");
                                        map.put("status", "Eligible not Done");
                                    }
                                }

                            } else {
                                //Eligible Not Done..
                                query = "SELECT patient_id FROM patient WHERE TIMESTAMPDIFF(MONTH, date_started, CURDATE()) > 6 AND patient_id = '" + patientId + "'";
                                SqlRowSet rs3 = jdbcTemplate.queryForRowSet(query);

                                if (rs3.next()) {
                                    map.put("currentVl", "N/A");
                                    map.put("dateCurrentVl", "N/A");
                                    map.put("status", "Eligible not Done");
                                } else {
                                    map.put("currentVl", "N/A");
                                    map.put("dateCurrentVl", "N/A");
                                    map.put("status", "Not Eligible");
                                }
                            }
                        } else {
                            map.put("currentVl", "N/A");
                            map.put("dateBaselineVl", "N/A");
                            map.put("dateCurrentVl", "N/A");
                            map.put("status", " N/A");
                            map.put("baselineVl", "N/A");
                        }

                        viralLoadList.add(map);

                        sn++;
                        //session.setAttribute("sn", sn);
                    }
                    session.setAttribute("viralLoadList", viralLoadList);
                    resultSet = null;
                    viralLoadList = null;
                } catch (Exception ex) {
                    resultSet = null;
                    ex.printStackTrace();
                }


                viralLoadList = new ViralLoadListBuilder().retrieveViralLoadList();

                resultSet = null;
            }


            page = (Integer) pagerParams.get("page");
            page = (Integer) pagerParams.get("page");
//        totalpages = (Integer) pagerParams.get("totalpages");
//        totalrecords = (Integer) pagerParams.get("totalrecords");

            //System.out.println("Current_page is: "+currpage+" and Page is: "+page);
           // session.setAttribute("currpage", currpage);

           // return SUCCESS;
        }

    }
