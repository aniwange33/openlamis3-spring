package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.User;
import org.fhi360.lamis.model.dto.UserDTO;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.model.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/user")
@Api(tags = "User", description = " ")
public class UserController {
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;

    public UserController(UserRepository userRepository, FacilityRepository facilityRepository) {
        this.userRepository = userRepository;
        this.facilityRepository = facilityRepository;
    }

    // Save new user to database
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<String> saveUser(@RequestBody UserDTO user, HttpSession session) {
        User user1 = new User();
        Long facilityId = (Long) session.getAttribute("id");
        if (session.getAttribute("userGroup") != null) {
            if (session.getAttribute("userGroup").equals("Administrator")) {
                Facility facility = new Facility();
                facility.setId(facilityId);
                /*user1.setFacility(facility);
                if (user.getViewIdentifier().equals("1")) {
                    user1.setViewIdentifier(true);
                } else {
                    user1.setViewIdentifier(false);
                }
                if (user.getStateIds().trim().isEmpty()) {
                    user1.setStateIds("0");
                } else {
                    user1.setStateIds(user.getStateIds());
                }
                user1.setTimeStamp(LocalDateTime.now());
                user1.setPassword(user.getPassword());
                user1.setFullname(user.getSurname() + " " + user.getOtherNames());
                user1.setUsername(user.getUsername());
                user1.setUserGroup(user.getUserGroup());
                */userRepository.save(user1);
            }
        }
        return ResponseEntity.ok().body("administration/user_data");
    }

    // Update user in database
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<String> updateUser(@RequestBody UserDTO user, HttpSession session) {
        User user1 = new User();
        Long facilityId = (Long) session.getAttribute("id");
        if (session.getAttribute("userGroup") != null) {
            if (session.getAttribute("userGroup").equals("Administrator")) {
                Facility facility = new Facility();
                facility.setId(facilityId);
                /*user1.setFacility(facility);
                if (user.getViewIdentifier().equals("1")) {
                    user1.setViewIdentifier(true);
                } else {
                    user1.setViewIdentifier(false);
                }
                if (user.getStateIds().trim().isEmpty()) {
                    user1.setStateIds("0");
                }
                user1.setId(user.getUserId());
                user1.setTimeStamp(LocalDateTime.now());
                user1.setStateIds(user.getStateIds());
                user1.setPassword(user.getPassword());
                user1.setFullname(user.getSurname() + " " + user.getOtherNames());
                user1.setUsername(user.getUsername());
                user1.setUserGroup(user.getUserGroup());
                */userRepository.save(user1);
            }
        }
        return ResponseEntity.ok().body("administration/user_data");
    }


    // Delete user from database

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(HttpSession session, @RequestParam("id") Long userId) {
        if (session.getAttribute("userGroup") != null) {
            if (session.getAttribute("userGroup").equals("Administrator")) {
                User user = userRepository.getOne(userId);
                userRepository.delete(user);
            } else {
                return ResponseEntity.ok().body("administration/error_message");
            }
        }
        return ResponseEntity.ok().body("administration/user_data");
    }

    // Retrieve a user in database
    @RequestMapping(value = "/findUser", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> findUser(@RequestParam("id") Long userId) {
        UserDTO userDTO = new UserDTO();
        User user = userRepository.getOne(userId);
        //userDTO.setPassword(user.getPassword());
        /*userDTO.setStateIds(user.getStateIds());
        userDTO.setUserId(user.getId());
        userDTO.setUserGroup(user.getUserGroup());
        userDTO.setFullName(user.getFullname());
        userDTO.setUsername(user.getUsername());
        userDTO.setViewIdentifier(user.getViewIdentifier() ? "1" : "0");
        userDTO.setId(user.getFacility().getId());
        */return ResponseEntity.ok().body(userDTO);
    }


    /*@RequestMapping(value = "/login", method = RequestMethod.POST)
        public ResponseEntity login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        User user = userRepository.findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword());
        if (user != null) {
           session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
          //  session.setAttribute("userGroup", user.getUserGroup());
         //   session.setAttribute("id", user.getFacility() != null ? user.getFacility().getId() : 0);
           // session.setAttribute("fullname", user.getFullname() == null ? "Guest" :
                   // user.getFullname());
           // session.setAttribute("viewIdentifier", user.getViewIdentifier() ? "1" : "0");
           // session.setAttribute("stateIds", user.getStateIds());
          //  if (user.getFacility().getId() == 0L) {
             //   session.setAttribute("facilityName", "No Active");
          //  } else {
             //   String facilityName = facilityRepository.getOne(user.getFacility().getId()).getName();
               // session.setAttribute("facilityName", facilityName);
           // }
            return ResponseEntity.ok(loginDTO);
       } else {
           return ResponseEntity.ok().body("login");
       }

    }
*/
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body("welcome");
    }

    @RequestMapping(value = "/verifyGroup", method = RequestMethod.PUT)
    public ResponseEntity<String> verifyGroup(@RequestParam long facilityId, HttpSession session) {
        String userGroup;
        if (session.getAttribute("userGroup") != null) {
            userGroup = (String) session.getAttribute("userGroup");
            if (userGroup.equals("Administrator")) {
                Facility facility = facilityRepository.getOne(facilityId);
                session.setAttribute("facilityId", facilityId);
                session.setAttribute("facilityName", facility.getName());
                User user = userRepository.getOne((Long) session.getAttribute("id"));
                //user.setFacility(facility);
                userRepository.save(user);
            }
        }

        return ResponseEntity.ok().body("administration/maintenance_page");
    }
}
