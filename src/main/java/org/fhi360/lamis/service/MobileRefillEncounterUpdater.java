/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

import org.fhi360.lamis.model.*;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.model.repositories.PharmacyRepository;
import org.fhi360.lamis.model.repositories.RegimenRepository;
import org.fhi360.lamis.utility.JDBCUtil;
import org.springframework.stereotype.Component;

/**
 *
 * @author user10
 */
@Component
public class MobileRefillEncounterUpdater {
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String query;

    private Long facilityId;
    private Long patientId;
    private Patient patient;
    private Long pharmacyId;
    private Long userId;
    private Pharmacy pharmacy;
    private  final FacilityRepository facilityRepository;
    private final RegimenRepository regimenRepository;
    private  final PharmacyRepository pharmacyRepository;

    public MobileRefillEncounterUpdater(FacilityRepository facilityRepository, RegimenRepository regimenRepository, PharmacyRepository pharmacyRepository) {
        this.facilityRepository = facilityRepository;
        this.regimenRepository = regimenRepository;
        this.pharmacyRepository = pharmacyRepository;
    }

    public void logDrug(long facilityId, long patientId, String dateVisit, String nextAppointment, String regimentype, String regimen, int duration) {
        try {
            RegimenType regimenType = new RegimenType();
            regimenType.setId(Long.parseLong(regimentype));
            long regimentypeId = regimenRepository.findByRegimenType1(regimenType).getRegimenType().getId();
            jdbcUtil = new JDBCUtil();  
            query = "SELECT regimen.regimen_id, regimen.regimentype_id, regimendrug.regimendrug_id, drug.morning, drug.afternoon, drug.evening FROM regimen "
                    + " JOIN regimendrug ON regimen.regimen_id = regimendrug.regimen_id JOIN drug ON regimendrug.drug_id = drug.drug_id WHERE regimen.description = '" + regimen + "' AND regimen.regimentype_id = " + regimentypeId;
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                long regimenId = resultSet.getLong("regimen_id");
                long regimendrugId = resultSet.getLong("regimendrug_id");
                double morning = resultSet.getDouble("morning");
                double afternoon = resultSet.getDouble("afternoon");
                double evening = resultSet.getDouble("evening");
                
                pharmacy = new Pharmacy();
                patient.setPatientId(patientId);
                pharmacy.setPatient(patient);
                pharmacy.setFacilityId(facilityId);
                RegimenType regimenType1 = new RegimenType();
                regimenType1.setId(Long.parseLong(regimentype));
                pharmacy.setRegimenType(regimenType1);
                Regimen regimen1 = new Regimen();
                regimen1.setId(regimenId);
                pharmacy.setRegimen(regimen1);
                pharmacy.setRegimendrugId(regimendrugId);
                pharmacy.setMorning(morning);
                pharmacy.setAfternoon(afternoon);
                pharmacy.setEvening(evening);
                pharmacy.setDuration(duration);
                User user = new User();
                user.setId(1L);
                pharmacy.setUser(user);
                pharmacy.setTimeStamp(LocalDateTime.now());
                
                long stateId = 0;
                query = "SELECT pharmacy_id FROM pharmacy WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_visit = '" + dateVisit + "' AND regimendrug_id = " + regimendrugId;
                preparedStatement = jdbcUtil.getStatement(query);
                ResultSet rs = preparedStatement.executeQuery();
                if(rs.next()) {
                    stateId = rs.getLong("pharmacy_id");
                    pharmacy.setPharmacyId(stateId);
                    pharmacyRepository.save(pharmacy);
                }
                else {
                    pharmacyRepository.save(pharmacy);
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public void logDrug(long facilityId, long patientId, String dateVisit, String nextAppointment, String regimen, int duration) {
        try {
            jdbcUtil = new JDBCUtil();  
            query = "SELECT regimen.regimen_id, regimen.regimentype_id, regimendrug.regimendrug_id, drug.morning, drug.afternoon, drug.evening FROM regimen "
                    + " JOIN regimendrug ON regimen.regimen_id = regimendrug.regimen_id JOIN drug ON regimendrug.drug_id = drug.drug_id WHERE regimen.description = '" + regimen + "'";
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                long regimentypeId = resultSet.getLong("regimentype_id");
                long regimenId = resultSet.getLong("regimen_id");
                long regimendrugId = resultSet.getLong("regimendrug_id");
                double morning = resultSet.getDouble("morning");
                double afternoon = resultSet.getDouble("afternoon");
                double evening = resultSet.getDouble("evening");

                pharmacy = new Pharmacy();
                patient.setPatientId(patientId);
                pharmacy.setPatient(patient);
                pharmacy.setFacilityId(facilityId);
                RegimenType regimenType1 = new RegimenType();
                regimenType1.setId(regimentypeId);
                pharmacy.setRegimenType(regimenType1);
                Regimen regimen1 = new Regimen();
                regimen1.setId(regimenId);
                pharmacy.setRegimen(regimen1);
                pharmacy.setRegimendrugId(regimendrugId);
                pharmacy.setMorning(morning);
                pharmacy.setAfternoon(afternoon);
                pharmacy.setEvening(evening);
                pharmacy.setDuration(duration);
                User user = new User();
                user.setId(1L);
                pharmacy.setUser(user);
                pharmacy.setTimeStamp(LocalDateTime.now());
                
                long stateId = 0;
                query = "SELECT pharmacy_id FROM pharmacy WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_visit = '" + dateVisit + "' AND regimendrug_id = " + regimendrugId;
                preparedStatement = jdbcUtil.getStatement(query);
                ResultSet rs = preparedStatement.executeQuery();
                if(rs.next()) {
                    stateId = rs.getLong("pharmacy_id");
                    pharmacy.setPharmacyId(stateId);
                    pharmacyRepository.save(pharmacy);
                }
                else {
                    pharmacyRepository.save(pharmacy);
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void updateRefillAttribute(long facilityId, long patientId, String dateVisit, String nextRefill) {
        query = "UPDATE patient SET date_last_refill = '" + dateVisit + "', date_next_refill = '" + nextRefill + "', refill_setting = 'COMMUNITY', time_stamp = NOW() WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_last_refill < '" + dateVisit + "'";          
    }
}
