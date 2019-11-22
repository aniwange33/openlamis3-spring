package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.controller.mapstruct.CaseManagerMapper;
import org.fhi360.lamis.controller.mapstruct.PatientMapper;
import org.fhi360.lamis.controller.model.CaseManagerClientSearch;
import org.fhi360.lamis.model.CaseManager;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.PatientCaseManager;
import org.fhi360.lamis.model.dto.CaseManagerDTO;
import org.fhi360.lamis.model.dto.CaseManagerDTO2;
import org.fhi360.lamis.model.dto.PatientDTO;
import org.fhi360.lamis.model.repositories.CaseManagerRepository;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.model.repositories.PatientCaseManagerRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.service.CaseManagerService;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.Scrambler;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/case-manager")
@Api(tags = "Case Manager", description = " ")
@Slf4j
public class CaseManagerController {
    private final CaseManagerRepository caseManagerRepository;
    private final FacilityRepository facilityRepository;
    private final PatientRepository patientRepository;
    private final CaseManagerMapper caseManagerMapper;
    private final JdbcTemplate jdbcTemplate;
    private final CaseManagerService caseManagerService;
    private final PatientCaseManagerRepository patientCaseManagerRepository;
    private final PatientMapper patientMapper;

    public CaseManagerController(CaseManagerRepository caseManagerRepository, FacilityRepository facilityRepository,
                                 PatientRepository patientRepository, CaseManagerMapper caseManagerMapper,
                                 JdbcTemplate jdbcTemplate, CaseManagerService caseManagerService,
                                 PatientCaseManagerRepository patientCaseManagerRepository, PatientMapper patientMapper) {
        this.caseManagerRepository = caseManagerRepository;
        this.facilityRepository = facilityRepository;
        this.patientRepository = patientRepository;
        this.caseManagerMapper = caseManagerMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.caseManagerService = caseManagerService;
        this.patientCaseManagerRepository = patientCaseManagerRepository;
        this.patientMapper = patientMapper;
    }

    public ResponseEntity<CaseManager> saveCaseManager(@RequestBody CaseManagerDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("id");
        Facility facility = facilityRepository.getOne(facilityId);
        CaseManager caseManager = caseManagerMapper.dtoToCaseManager(dto);
        caseManager.setFacility(facility);
        CaseManager result = caseManagerRepository.save(caseManager);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity deleteCaseManager(@Param("casemanagerId") Long caseManagerId) {
        caseManagerRepository.deleteById(caseManagerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/assignment")
    public ResponseEntity assign(@RequestParam("casemanagerId") Long caseManagerId,
                                 @RequestParam("patientIds") String patientIds,
                                 @RequestParam("type") String type) {
        Map<String, String> assignment = new HashMap<>();
        CaseManager caseManager = caseManagerRepository.getOne(caseManagerId);
        List<Long> ids = Arrays.stream(patientIds.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
        switch (type) {
            case "assign":
                patientRepository.findAllById(ids)
                        .forEach(patient -> {
                            patient.setCaseManager(caseManager);
                            patientRepository.save(patient);
                            PatientCaseManager patientCaseManager = new PatientCaseManager();
                            patientCaseManager.setFacility(patient.getFacility());
                            patientCaseManager.setPatient(patient);
                            patientCaseManager.setCaseManager(caseManager);
                            patientCaseManager.setAction("Assignment");
                            patientCaseManagerRepository.save(patientCaseManager);
                        });
                break;
            case "deassign":
                patientRepository.findAllById(ids)
                        .forEach(patient -> {
                            patient.setCaseManager(null);
                            patientRepository.save(patient);
                            PatientCaseManager patientCaseManager = new PatientCaseManager();
                            patientCaseManager.setFacility(patient.getFacility());
                            patientCaseManager.setPatient(patient);
                            patientCaseManager.setCaseManager(caseManager);
                            patientCaseManager.setAction("De-Assignment");
                            patientCaseManagerRepository.save(patientCaseManager);
                        });
                break;
            case "reassign":
                patientRepository.findAllById(ids)
                        .forEach(patient -> {
                            CaseManager previous = patient.getCaseManager();
                            LOG.info("Previous: {}", previous);
                            patient.setCaseManager(caseManager);
                            patientRepository.save(patient);

                            PatientCaseManager patientCaseManager = new PatientCaseManager();
                            patientCaseManager.setPatient(patient);
                            patientCaseManager.setFacility(patient.getFacility());
                            patientCaseManager.setCaseManager(previous);
                            patientCaseManager.setAction("De-Assignment");
                            patientCaseManagerRepository.save(patientCaseManager);

                            patientCaseManager = new PatientCaseManager();
                            patientCaseManager.setPatient(patient);
                            patientCaseManager.setFacility(patient.getFacility());
                            patientCaseManager.setCaseManager(caseManager);
                            patientCaseManager.setAction("Assignment");
                            patientCaseManagerRepository.save(patientCaseManager);
                        });
                break;
        }
        assignment.put("response", "success");
        return ResponseEntity.ok(assignment);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCaseManagerDetails(@PathVariable("id") Long caseManagerId) {
        Map<String, Object> details = new HashMap<>();
        CaseManager caseManager = caseManagerRepository.getOne(caseManagerId);
        details.put("casemanagerId", caseManager.getCasemanagerId().toString());
        details.put("fullname", caseManager.getFullName());
        details.put("religion", caseManager.getReligion());
        details.put("sex", caseManager.getSex());
        details.put("clientCount", patientRepository.countByCaseManager(caseManager));

        return ResponseEntity.ok(details);
    }

    @GetMapping("/retrieve")
    public ResponseEntity retrieveCaseManagers(@RequestParam(required = false, value = "except") Long id) {
        Long facilityId = 1190L;
        Facility facility = facilityRepository.getOne(facilityId);
        List<CaseManager> caseManagers = caseManagerRepository.findByFacilityOrderByFullName(facility)
                .stream()
                .filter(caseManager -> {
                    if (id != null) {
                        return !caseManager.getCasemanagerId().equals(id);
                    }
                    return true;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(caseManagers);
    }

    @GetMapping("/client-status/{id}")
    public ResponseEntity getClientStatus(@PathVariable Long id) {
        Long facilityId = 1190L;
        Map<String, Object> status = new HashMap<>();
        String query = "SELECT DISTINCT status as treatment_status, COUNT(status) as treatment_status_count " +
                "FROM clients WHERE facility_id = " + facilityId + " AND casemanager_id = " + id +
                " GROUP BY status ORDER BY status ASC";

        jdbcTemplate.query(query, resultSet -> {
            while (resultSet.next()) {
                int treatmentStatus = resultSet.getInt("treatment_status");
                int treatmentStatusCount = resultSet.getInt("treatment_status_count");
                if (treatmentStatus == 1) {
                    //System.out.println("One");
                    status.put("stable", Integer.toString(treatmentStatusCount));
                } else if (treatmentStatus == 2) {
                    status.put("unstable_less", Integer.toString(treatmentStatusCount));
                } else if (treatmentStatus == 3) {
                    status.put("unstable_more", Integer.toString(treatmentStatusCount));
                } else if (treatmentStatus == 4) {
                    status.put("preart", Integer.toString(treatmentStatusCount));
                }
            }
            return null;
        });

        return ResponseEntity.ok(status);
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity clients(@PathVariable Long id) {
        CaseManager caseManager = caseManagerRepository.getOne(id);
        Scrambler scrambler = new Scrambler();
        List<Patient> patients = patientRepository.findByCaseManager(caseManager);
        patients.stream().map(response -> {
            response.setOtherNames(scrambler.unscrambleCharacters(response.getOtherNames()));
            response.setSurname(scrambler.unscrambleCharacters(response.getSurname()));
            response.setAddress(scrambler.unscrambleCharacters(response.getAddress()));
            return response;
        }).collect(Collectors.toList());
        Map<String, Object> responses = new HashMap<>();
        responses.put("caseManagerClientsList",  patientMapper.patientToDto(patients));
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/category-counts")
    public ResponseEntity categoryCount(@RequestBody CaseManagerClientSearch search) {
        String query = clientQuery(search);
        query = query.replace("clients.*", "status, COUNT(*) AS count");
        query += " GROUP BY status ORDER BY status ASC";
        Map<String, Object> clientsCategoryCountMap = new HashMap<>();
        jdbcTemplate.query(query, resultSet -> {
            while (resultSet.next()) {
                int defaultCount = 0;
                int count = resultSet.getInt("count");
                int option = resultSet.getInt("status");
                //System.out.println("Data is: "+count+" and option is: "+option);
                if (option == 1) {
                    clientsCategoryCountMap.put("stable", Integer.toString(count));
                } else {
                    if (!clientsCategoryCountMap.containsKey("stable"))
                        clientsCategoryCountMap.put("stable", Integer.toString(defaultCount));
                }
                if (option == 2) {
                    clientsCategoryCountMap.put("unstable_less", Integer.toString(count));
                } else {
                    if (!clientsCategoryCountMap.containsKey("unstable_less"))
                        clientsCategoryCountMap.put("unstable_less", Integer.toString(defaultCount));
                }
                if (option == 3) {
                    clientsCategoryCountMap.put("unstable_more", Integer.toString(count));
                } else {
                    if (!clientsCategoryCountMap.containsKey("unstable_more"))
                        clientsCategoryCountMap.put("unstable_more", Integer.toString(defaultCount));
                }
                if (option == 4) {
                    clientsCategoryCountMap.put("preart", Integer.toString(count));
                } else {
                    if (!clientsCategoryCountMap.containsKey("preart"))
                        clientsCategoryCountMap.put("preart", Integer.toString(defaultCount));
                }
            }
            return null;
        });
        return ResponseEntity.ok(clientsCategoryCountMap);
    }

    @PostMapping("/client-search")
    public ResponseEntity clientSearchGrid(@RequestBody CaseManagerClientSearch search) {
        int start = search.getStart();
        int numberOfRows = search.getRows();

        //Call for Grid..
        String query = clientQuery(search);
        query += " ORDER BY hospital_num, address ASC LIMIT " + start + " , " + numberOfRows;

        List<CaseManagerDTO2> caseManagerDTO2s = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(CaseManagerDTO2.class)).stream()
                .map(response -> {
                    String fullName = isAssignedToCaseManager(response.getPatientId());
                    response.setFullName(fullName);
                    return response;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(caseManagerDTO2s);
    }

    @GetMapping("/init-client-search")
    public void initClientSearch() {
        caseManagerService.initClientSearch();
    }


    public String isAssignedToCaseManager(long patientId) {
        String[] assignedTo = {"Not Assigned"};
        try {
            String query = "SELECT casemanager_id FROM patient WHERE patient_id =" + patientId;
            jdbcTemplate.query(query, resultSet -> {
                if (resultSet.getObject("casemanager_id") != null) {
                    Integer caseManagerId = resultSet.getInt("casemanager_id");
                    assignedTo[0] = getCaseManagerAssigned(Long.valueOf(caseManagerId));
                } else {
                    assignedTo[0] = getCaseManagerAssigned(0L);
                }
            });

        } catch (Exception ex) {
            assignedTo[0] = "Not Assigned";
            ex.printStackTrace();
        }

        return assignedTo[0];
    }


    public String getCaseManagerAssigned(long casemanagerId) {
        String[] casemanagerName = {""};
        try {
            if (casemanagerId != 0L) {
                String query = "SELECT fullname FROM casemanager WHERE casemanager_id =" + casemanagerId;

                jdbcTemplate.query(query, resultSet -> {
                    casemanagerName[0] = resultSet.getString("fullname") != null ? resultSet.getString("fullname") : "Not Assigned";
                });

            } else {
                casemanagerName[0] = "Not Assigned";
            }
        } catch (Exception ex) {
            casemanagerName[0] = "Not Assigned";
            ex.printStackTrace();
        }

        return casemanagerName[0];
    }

    private String clientQuery(CaseManagerClientSearch search) {
        Long facilityId = 1190L;
        String query = "SELECT clients.* FROM clients WHERE facility_id = " + facilityId;

        if (search.getGender() != null && !search.getGender().equals("--All--"))
            query += " AND gender = '" + search.getGender() + "'";
        //TODO: Arrange Age Group:
        if (search.getAgeGroup() != null && !search.getAgeGroup().equals("0")) {
            String ageGroup = search.getAgeGroup();
            String[] ageRange = ageGroup.split("-");
            query += " AND DATEDIFF('YEAR', date_birth, CURDATE()) >= " + Integer.parseInt(ageRange[0]) +
                    " AND DATEDIFF('YEAR', date_birth, CURDATE()) <= " + Integer.parseInt(ageRange[1]);
        }
        if (StringUtils.isNotBlank(search.getState()))
            query += " AND state = '" + search.getState() + "'";
        if (StringUtils.isNotBlank(search.getLga()))
            query += " AND lga = '" + search.getLga() + "'";
        //Pregnancy Status
        if (search.getPregnancyStatus() != null && !search.getPregnancyStatus().equals("--All--")) {
            if (search.getPregnancyStatus().equals("1"))
                query += " AND pregnant = 1";
            else if (search.getPregnancyStatus().equals("2"))
                query += " AND breastfeeding = 1";
        }

        if (StringUtils.isNotBlank(search.getCategoryId())) {
            String categoryId = search.getCategoryId();
            switch (categoryId) {
                case "0":
                    query += " AND (status = " + Constants.CaseManager.STABLE_ONE_YEAR + " OR status = " +
                            Constants.CaseManager.UNSTABLE_NOT_ONE_YEAR + " OR status = " +
                            Constants.CaseManager.UNSTABLE_ONE_YEAR + " OR status = " +
                            Constants.CaseManager.PRE_ART + ")";
                    break;
                case "1":
                    query += " AND status = " + Constants.CaseManager.STABLE_ONE_YEAR;
                    break;
                case "2":
                    query += " AND status = " + Constants.CaseManager.UNSTABLE_NOT_ONE_YEAR;
                    break;
                case "3":
                    query += " AND status = " + Constants.CaseManager.UNSTABLE_ONE_YEAR;
                    break;
                case "4":
                    query += " AND status = " + Constants.CaseManager.PRE_ART;
                    break;
            }
        }

        if (search.isShowAssigned())
            query += " AND (casemanager_id IS NOT NULL AND casemanager_id > 0)";
        else
            query += " AND (casemanager_id IS NULL OR casemanager_id = 0) ";


        return query;
    }
}
