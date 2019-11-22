/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.User;
import org.fhi360.lamis.model.repositories.UserRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chart/userGridAction")
@Api(tags = "UserGridAction Chart", description = " ")
public class UserGridAction {
    private final UserRepository userRepository;

    private ArrayList<Map<String, String>> userList1 = new ArrayList<Map<String, String>>();

    public UserGridAction(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void userGrid(HttpSession session) {
        List<User> userList = userRepository.findAll();
        userList.forEach(user -> {
            Map<String, String> map = new HashMap<String, String>();
            map.put("userId", String.valueOf(user.getId()));
            /*map.put("username", user.getUsername());
            map.put("password", user.getPassword());
            map.put("id", String.valueOf(user.getFacility().getId()));
            map.put("name", user.getFullname());
            map.put("userGroup", user.getUserGroup());
            map.put("stateIds", String.valueOf(user.getStateIds()));
            map.put("viewIdentifier", String.valueOf(user.getViewIdentifier()));
            */DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
           // map.put("timeLogin", date.format(user.getTimeLogin()));
            //map.put("fullName", user.getFullname());
            userList1.add(map);
        });

        session.setAttribute("userList", userList1);


    }

}
