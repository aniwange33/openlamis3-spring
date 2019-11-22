/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.config.ContextProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/chart/dispenser")
@Api(tags = "Dispenser Grid Chart", description = " ")
public class DispenserGridAction {
    private final JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);

    @GetMapping("/grid")
    public ResponseEntity dispenserGrid(@RequestParam("regimentypeId") String regimentypeId, @RequestParam("id") String regimenId,
                                        HttpSession session) {
        String regimentypeId1;
        String regimenId1;
        String s = "1,2,3,4,14";
        boolean dispense = true;
        List<Map<String, String>> dispenserList = new ArrayList<>();
        if (session.getAttribute("dispenserList") != null) {
            dispenserList = (List) session.getAttribute("dispenserList");
        }

        //check if drug has already been dispensed and remove
        for (Iterator<Map<String, String>> iterator = dispenserList.iterator(); iterator.hasNext(); ) {
            Map map = iterator.next();
            regimentypeId1 = (String) map.get("regimentypeId");
            regimenId1 = (String) map.get("id");
            //If drug is the same or ARV remove from list
            if (regimenId.equals(regimenId1) || (s.contains(regimentypeId) && s.contains(regimentypeId1))) {
                iterator.remove();
                dispense = false;
            }
            //If ARV but different regimen dispense
            if (s.contains(regimentypeId) && !regimenId.equals(regimenId1)) {
                dispense = true;
            }
        }
        if (dispense) {
            dispenseDrug(regimenId, session);
        }
        session.setAttribute("dispenserList", dispenserList);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/retrieve")
    public ResponseEntity dispenserGridRetrieve(HttpSession session) {
        List dispenserList = new ArrayList();
        System.out.println("Got Here!");
        // retrieve the record store in session attribute
        if (session.getAttribute("dispenserList") != null) {
            dispenserList = (ArrayList) session.getAttribute("dispenserList");
//            System.out.println("Dispenser List in Dispenser Grid Retrieve is "+dispenserList);
        }
        return ResponseEntity.ok(dispenserList);
    }

    private void dispenseDrug(String id, HttpSession session) {
        List<Map<String, String>> dispenserList = new ArrayList<>();
        String query = "SELECT drug.name, drug.strength, drug.morning, drug.afternoon, drug.evening, " +
                "regimendrug.regimendrug_id, regimendrug.regimen_id, regimendrug.drug_id, regimen.regimentype_id "
                + " FROM drug JOIN regimendrug ON regimendrug.drug_id = drug.drug_id JOIN regimen ON " +
                "regimendrug.regimen_id = regimen.regimen_id WHERE regimendrug.regimen_id = ?";
        jdbcTemplate.query(query, resultSet -> {
            //loop through resultSet for each row and put into Map
            while (resultSet.next()) {
                String regimentypeId = Long.toString(resultSet.getLong("regimentype_id"));
                String regimendrugId = Long.toString(resultSet.getLong("regimendrug_id"));
                String regimenId = Long.toString(resultSet.getLong("regimen_id"));
                String drugId = Long.toString(resultSet.getLong("drug_id"));
                String description = resultSet.getString("name") + " " +
                        resultSet.getString("strength");
                String morning = Double.toString(resultSet.getDouble("morning"));
                String afternoon = Double.toString(resultSet.getDouble("afternoon"));
                String evening = Double.toString(resultSet.getDouble("evening"));
                String duration = "0";
                String quantity = "0.0";

                Map<String, String> map = new HashMap<String, String>();
                map.put("regimentypeId", regimentypeId);
                map.put("regimendrugId", regimendrugId);
                map.put("regimenId", regimenId);
                map.put("drugId", drugId);
                map.put("description", description);
                map.put("morning", morning);
                map.put("afternoon", afternoon);
                map.put("evening", evening);
                map.put("duration", duration);
                map.put("quantity", quantity);
                dispenserList.add(map);
            }
            return null;
        }, id);
        session.setAttribute("dispenserList", dispenserList);
    }

    @GetMapping("/update")
    public ResponseEntity updateDispenserList(@RequestParam("regimendrugId")
                                                      String regimendrugId, @RequestParam("morning") String morning,
                                              @RequestParam("afternoon") String afternoon, @RequestParam("evening") String evening,
                                              @RequestParam("duration") String duration, HttpSession session) {
        List<Map<String, String>> dispenserList = new ArrayList<>();
        double q = Double.parseDouble(morning) + Double.parseDouble(afternoon) + Double.parseDouble(evening);
        String quantity = Double.toString(Integer.parseInt(duration) * q);

        // retrieve the list stored as an attribute in session object
        if (session.getAttribute("dispenserList") != null) {
            dispenserList = (ArrayList) session.getAttribute("dispenserList");
        }

        // find the target drug and update with values of request parameters
        for (Map<String, String> stringStringMap : dispenserList) {
            String id = stringStringMap.get("regimendrugId"); // retrieve regimen regimentypeId from list
            if (id.equals(regimendrugId)) {
                stringStringMap.remove("morning");
                stringStringMap.put("morning", morning);
                stringStringMap.remove("afternoon");
                stringStringMap.put("afternoon", afternoon);
                stringStringMap.remove("evening");
                stringStringMap.put("evening", evening);
                stringStringMap.remove("duration");
                stringStringMap.put("duration", duration);
                stringStringMap.remove("quantity");
                stringStringMap.put("quantity", quantity);
            }
        }
        // set dispenserList as a session-scoped attribute
        session.setAttribute("dispenserList", dispenserList);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refillPeriod")
    public ResponseEntity refillPeriod(@RequestParam(required = false , name = "refill") String refill,
                                       @RequestParam(required = false, name = "regimen_durations") String regimenDurations,
                                       HttpSession session) {
        // retrieve the list stored as an attribute in session object
        List<Map<String, String>> dispenserList = new ArrayList<>();
        if (session.getAttribute("dispenserList") != null) {
            dispenserList = (ArrayList) session.getAttribute("dispenserList");
        }

        if (refill != null) {
            // find the target drug and update duration only regimenType ids 1,2,3,4,14
            String s = "1,2,3,4,14";
            for (Map<String, String> stringStringMap : dispenserList) {
                String id = stringStringMap.get("regimentypeId"); // retrieve regimenType regimentypeId from list
                if (s.contains(id)) {
                    Double morn = Double.parseDouble(stringStringMap.get("morning"));
                    Double after = Double.parseDouble(stringStringMap.get("afternoon"));
                    Double even = Double.parseDouble(stringStringMap.get("evening"));
                    String quantity = Double.toString((morn + after + even) * Integer.parseInt(refill));
                    stringStringMap.remove("duration");
                    stringStringMap.put("duration", refill);
                    stringStringMap.remove("quantity");
                    stringStringMap.put("quantity", quantity);
                }
            }
        } else if (session.getAttribute("fromPrescription") == "true" && regimenDurations != null) {
            String regimen_durations = regimenDurations;
            String[] regimen_durs = regimen_durations.split(",");
            List<String> regimenTypes = new ArrayList<>();
            Map<String, String> durations = new HashMap<>();
            for (int i = 0; i < regimen_durs.length; i += 3) {
                regimenTypes.add(regimen_durs[i]);
                durations.put(regimen_durs[i], regimen_durs[i + 2]);
            }
            for (Map<String, String> stringStringMap : dispenserList) {
                String id = stringStringMap.get("regimentypeId"); // retrieve regimenType regimentypeId from list
                if (regimenTypes.indexOf(id) != -1) {
                    Double morn = Double.parseDouble(stringStringMap.get("morning"));
                    Double after = Double.parseDouble(stringStringMap.get("afternoon"));
                    Double even = Double.parseDouble(stringStringMap.get("evening"));
                    String quantity = Double.toString((morn + after + even) * Integer.parseInt(durations.get(id)));
                    stringStringMap.remove("duration");
                    stringStringMap.put("duration", durations.get(id));
                    stringStringMap.remove("quantity");
                    stringStringMap.put("quantity", quantity);
                }
            }
        }
        // set dispenserList as a session-scoped attribute
        session.setAttribute("dispenserList", dispenserList);
        return ResponseEntity.ok().build();
    }
}
