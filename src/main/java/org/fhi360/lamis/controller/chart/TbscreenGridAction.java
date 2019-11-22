/**
 * @author aalozie
 */
package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.TBScreen;
import org.fhi360.lamis.model.repositories.TBScreenRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chart/tBScreenRepository")
@Api(tags = "TBScreenRepository Chart", description = " ")
public class TbscreenGridAction {
    private final TBScreenRepository tbScreenRepository;
    private ArrayList<Map<String, String>> tbscreenList = new ArrayList<Map<String, String>>();

    public TbscreenGridAction(TBScreenRepository tbScreenRepository) {
        this.tbScreenRepository = tbScreenRepository;
    }

    @GetMapping("tbscreenGrid")
    public void tbscreenGrid(@Param("tbscreen_id") String tbscreen_id, @Param("description") String description, HttpSession session) {
        try {
            List<TBScreen> tbScreens = tbScreenRepository.findAll();
            tbScreens.forEach(tbScreen -> {
                String value = "";
                Map<String, String> map = new HashMap<String, String>();
                map.put("tbscreenId", tbscreen_id);
                map.put("description", description);
                map.put("tbValue", value);
                tbscreenList.add(map);
            });
            // loop through resultSet for each row and put into Map
            session.setAttribute("tbscreenList", tbscreenList);
        } catch (Exception exception) {

        }

    }

    @PutMapping("/updateTbscreenList")
    public void updateTbscreenList(@Param("stateId") String tbscreenId, @Param("tbValue") String value, HttpSession session) {

        // retrieve the list stored as an attribute in session object
        if (session.getAttribute("tbscreenList") != null) {
            tbscreenList = (ArrayList) session.getAttribute("tbscreenList");
        }

        // find the target element and update with values of request parameters
        for (int i = 0; i < tbscreenList.size(); i++) {
            String id = (String) tbscreenList.get(i).get("tbscreenId"); // retrieve tbscreen stateId from list
            if (id.equals(tbscreenId)) {
                tbscreenList.get(i).remove("tbValue");
                tbscreenList.get(i).put("tbValue", value);
            }
        }
        session.setAttribute("tbscreenList", tbscreenList);

    }


}
