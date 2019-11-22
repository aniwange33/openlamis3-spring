/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.Pharmacy;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.model.repositories.PharmacyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Idris
 */

@RestController
@RequestMapping(value = "/api/pharmacy")
@Api(tags = "Pharmacy", description = " ")
public class PharmacyController {
    //
//    @Autowired
//    private PatientService patientService;
//
//    @Autowired
//    private PharmacyService pharmacyService;
//
//    @Autowired
//    private PharmacyJdbc pharmacyJdbc;
//
//    @Autowired
//    private RegimenJdbc regimenJdbc;
//
//    @InitBinder
//    public void initBinder(WebDataBinder webDataBinder) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        dateFormat.setLenient(false);
//        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
//    }
//
//    @RequestMapping(method = RequestMethod.GET)
//    public String getPharmacy(@RequestParam("id") String id, @RequestParam("pharmacyId") String pharmacyId, HttpSession session) {
//        session.setAttribute("id", id);
//        session.setAttribute("pharmacyId", pharmacyId);
//        return "pharmacy";
//    }
//
//    @RequestMapping(value = "/search", method = RequestMethod.GET)
//    public String getPharmacysearch() {
//        return "pharmacysearch";
//    }
//
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//     public String save(@RequestBody Pharmacy pharmacy,  Model model, HttpSession session) throws ServletException, IOException, ParseException {
//        User user = new User();
//        if(session.getAttribute("user") != null) user = (User) session.getAttribute("user");
//
//
//        pharmacy.setId(user.getId());
//        pharmacy.setId(user.getId());
//        pharmacy.setTimeUploaded(new Date());
//        pharmacy.setUploaded(0);
//        pharmacyService.save(pharmacy);
//        //Delete any drug refill done on this date
//        deleteByDateVisit(pharmacy);
//
//
//
//
//        return "pharmacysearch";
//    }
//
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    public String delete(@RequestBody Pharmacy pharmacy, Model model) throws ServletException, IOException, ParseException {
//        deleteByDateVisit(pharmacy);
//        return "pharmacysearch";
//    }
//
//    @RequestMapping(value = "/findByPharmacyId", method = RequestMethod.GET)
//    public @ResponseBody Map<String, ? extends Object> findByPharmacyId(@RequestParam("pharmacyId") int pharmacyId) {
//        Pharmacy pharmacy = pharmacyService.findByPharmacyId(pharmacyId);
//        Map<String, Object> map = new HashMap<>();
//        map.put("pharmacyList", pharmacy);
//        return map;
//    }
//
//    @RequestMapping(value = "/findByCachedId", method = RequestMethod.GET)
//    public @ResponseBody Map<String, ? extends Object> findByCachedId(HttpSession session) {
//        List<Object> patientList = new ArrayList<>();
//        List<Object> pharmacyList = new ArrayList<>();
//
//        String id = "0";
//        if(session.getAttribute("id") != null) id = (String) session.getAttribute("id");
//        if(!id.equals("0")) {
//            Patient patient = patientService.findByPatientId(Integer.parseInt(id));
//            patientList.add(patient);
//        }
//
//        String pharmacyId = "0";
//        if(session.getAttribute("pharmacyId") != null) pharmacyId = (String) session.getAttribute("pharmacyId");
//        if(!pharmacyId.equals("0")) {
//            Pharmacy pharmacy = pharmacyService.findByPharmacyId(Integer.parseInt(pharmacyId));
//            pharmacyList.add(pharmacy);
//        }
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("patientList", patientList);
//        map.put("pharmacyList", pharmacyList);
//        return map;
//    }
//
//    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
//    public @ResponseBody Map<String, ? extends Object> findAll(@RequestParam("id") long id) {
//        return pharmacyJdbc.findAll(id);
//    }
//
//    @RequestMapping(value = "/findRegimen", method = RequestMethod.GET)
//    public @ResponseBody Map<String, ? extends Object> findRegimen(@RequestParam("regimenlineIds") String regimenlineIds) {
//        return regimenJdbc.findAll(regimenlineIds);
//    }
//
//    @RequestMapping(value = "/findRegimenByRegimenlineName", method = RequestMethod.GET)
//    public @ResponseBody Map<String, ? extends Object> findRegimenByRegimenlineName(@RequestParam String name) {
//        return regimenJdbc.findRegimenByRegimenlineName(name);
//    }
//
//    private void deleteByDateVisit(Pharmacy pharmacy) {
//        List<Pharmacy> list = pharmacyService.findByDateVisit(pharmacy.getPharmacyId(), pharmacy.getDateVisit());
//        for (Pharmacy pharm : list) {
//           pharmacyService.delete(pharm);
//        }
//    }
    private final PharmacyRepository pharmacyRepository;
    private final PatientRepository patientRepository;

    public PharmacyController(PharmacyRepository pharmacyRepository, PatientRepository patientRepository) {
        this.pharmacyRepository = pharmacyRepository;
        this.patientRepository = patientRepository;
    }

    @PutMapping
    @PostMapping
    public ResponseEntity savePharmacy(@RequestBody Pharmacy pharmacy) {
        pharmacyRepository.save(pharmacy);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void deletePharmacy(@PathVariable Long id) {
        pharmacyRepository.deleteById(id);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity getByPatient(@PathVariable Long id) {
        Patient patient = patientRepository.getOne(id);
        List<Pharmacy> pharmacies = pharmacyRepository.findByPatient(patient);
        Map<String, Object> response = new HashMap<>();
        response.put("pharmacyList", pharmacies);
        return ResponseEntity.ok(response);
    }
}
