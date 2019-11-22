/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Lga;
import org.fhi360.lamis.model.State;
import org.fhi360.lamis.model.dto.FacilityDTO;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chart/facility")
@Api(tags = "Facility Grid Chart", description = " ")
public class FacilityGridAction {
    private final FacilityRepository facilityRepository;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;


    public FacilityGridAction(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;

    }

    @GetMapping("/grid")
    public ResponseEntity<List<Facility>> facilityGrid(@RequestParam(required = false) Long stateId,
                                                       @RequestParam(required = false) Long lgaId) {
        List<Facility> facilities;
        if (lgaId != null) {
            Lga lga = new Lga();
            lga.setId(lgaId);
            facilities = facilityRepository.findByLga(lga);
        } else if (stateId != null) {
            State state = new State();
            state.setId(stateId);
            facilities = facilityRepository.findByState(state);
        } else {
            facilities = facilityRepository.findAll();
        }
        return ResponseEntity.ok().body(facilities);

    }

    //This method controls the NDR page.
    @GetMapping("facilitySelGrid")
    public ResponseEntity<?> facilitySelGrid(@RequestParam("stateId") String stateId) {
        ArrayList facilityList = new ArrayList();
        try {
            jdbcUtil = new JDBCUtil();
            String query = "";
            executeUpdate("DROP VIEW IF EXISTS actives");
            query = " CREATE VIEW actives AS SELECT facility_id, COUNT(patient_id) AS count FROM patient WHERE " +
                    "current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND TIMESTAMPDIFF(DAY," +
                    "DATE_ADD(date_last_refill, INTERVAL last_refill_duration DAY), CURDATE()) <= " +
                    Constants.LTFU.GON + " AND date_started IS NOT NULL GROUP BY facility_id";
            executeUpdate(query);

            executeUpdate("DROP VIEW IF EXISTS counts");
            query = " CREATE VIEW counts AS SELECT facility_id, COUNT(patient_id) AS count FROM patient GROUP BY facility_id";
            executeUpdate(query);

            query = "SELECT facility_id, name, datim_id FROM facility WHERE facility_id IN (SELECT DISTINCT " +
                    "facility_id FROM patient) AND state_id = ? ORDER BY name ASC";
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.setLong(1, Long.parseLong(stateId));
            ResultSet resultSet = preparedStatement.executeQuery();
            int i = 1;
            while (resultSet.next()) {
                String facilityId = Long.toString(resultSet.getLong("facility_id"));
                String name = resultSet.getString("name");
                Long facId = resultSet.getLong("facility_id");
                String count = getFacilityPatientCount(facId);
                String active = getActiveClientsForFacility(facId);
                String datimId = resultSet.getString("datim_id");

                Map<String, String> map = new HashMap<>();
                map.put("facilityId", facilityId);
                map.put("name", name);
                map.put("count", count);
                map.put("active", active);
                map.put("datimId", datimId);
                map.put("sn", "" + i);
                map.put("sel", "0");
                facilityList.add(map);
                i++;
            }
        } catch (Exception exception) {
            // resultSet = null;
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return ResponseEntity.ok().body(facilityList);
    }


    private String getFacilityPatientCount(long facId) {
        String count = "0";
        JDBCUtil jdbcUtil1 = null;
        ResultSet resultSet1;
        try {
            jdbcUtil1 = new JDBCUtil();
            String query1 = "SELECT count FROM counts WHERE facility_id = ?";
            PreparedStatement preparedStatement1 = jdbcUtil1.getStatement(query1);
            preparedStatement1.setLong(1, facId);
            resultSet1 = preparedStatement1.executeQuery();
            while (resultSet1.next()) {
                count = Integer.toString(resultSet1.getInt("count"));
            }
        } catch (Exception ex) {
            resultSet1 = null;
            jdbcUtil1.disconnectFromDatabase();  //disconnect from database
        }
        return count;
    }

    public String getActiveClientsForFacility(long facId) {

        String count = "0";
        JDBCUtil jdbcUtil1 = null;
        ResultSet resultSet2;
        try {
            jdbcUtil1 = new JDBCUtil();
            String query2 = "SELECT count FROM actives WHERE facility_id = ? ";
            PreparedStatement preparedStatement2 = jdbcUtil1.getStatement(query2);
            preparedStatement2.setLong(1, facId);
            resultSet2 = preparedStatement2.executeQuery();
            while (resultSet2.next()) {
                count = Integer.toString(resultSet2.getInt("count"));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            resultSet2 = null;
            jdbcUtil1.disconnectFromDatabase();  //disconnect from database
        }
        return count;
    }

    private void executeUpdate(String query) {
        try {
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
    }
}
