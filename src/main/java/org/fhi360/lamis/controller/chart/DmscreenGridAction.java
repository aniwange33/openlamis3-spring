/**
 * @author aalozie
 */
package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.DMScreen;
import org.fhi360.lamis.model.repositories.DMScreenRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chart/dmscreen")
@Api(tags = "Dmscreen Grid Chart", description = " ")
public class DmscreenGridAction {
    private final DMScreenRepository dmScreenRepository;

    public DmscreenGridAction(DMScreenRepository dmScreenRepository) {
        this.dmScreenRepository = dmScreenRepository;
    }

    @GetMapping("/grid")
    public ResponseEntity dmscreenGrid(HttpSession session) {
        List<DMScreen> screens = dmScreenRepository.findAll();
        List<Object> list = new ArrayList<>();
        screens.forEach(screen -> {
            Map<String, Object> value = new HashMap<>();
            Map<String, String> dmscreen = new HashMap<>();
            dmscreen.put("dmscreenId", screen.getDscreenId().toString());
            dmscreen.put("description", screen.getDescription());
            value.put("dmValue", dmscreen);
            list.add(value);
        });
        session.setAttribute("dmscreenList", list);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/update")
    public ResponseEntity updateDmscreenList(@RequestParam("regimentypeId") Long dmscreenId, @RequestParam("dmValue") String value,
                                             HttpSession session) {
        List<Map<String, Object>> dmscreenList = new ArrayList();

        // retrieve the list stored as an attribute in session object
        if (session.getAttribute("dmscreenList") != null) {
            dmscreenList = (List) session.getAttribute("dmscreenList");
        }

        // find the target element and update with values of request parameters
        for (int i = 0; i < dmscreenList.size(); i++) {
            String id = (String) dmscreenList.get(i).get("dmscreenId"); // retrieve dmscreen regimentypeId from list
            if (id.equals(dmscreenId)) {
                dmscreenList.get(i).remove("dmValue");
                dmscreenList.get(i).put("dmValue", value);
            }
        }
        // set list as a session-scoped attribute
        session.setAttribute("dmscreenList", dmscreenList);
        return ResponseEntity.ok().build();
    }
}
