package org.fhi360.lamis.utility;

import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import org.fhi360.lamis.model.Pharmacy;


/**
 *
 * @author user10
 */
public class FileMakerConverter {
    private String query; 
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public void convert() {
        System.out.println("Processing");
        String fileName = "C:/LAMIS2/patient.csv";
        int facilityId = 1;
        String[] row = null;
        int rowcount = 0;
        try {            
            CSVReader csvReader = new CSVReader(new FileReader(fileName));
            System.out.println("File read..");
            while((row = csvReader.readNext()) != null) {
                rowcount++;
                if (rowcount > 1 ) {
                   long patientId = Long.parseLong(row[0].trim());
                   String hospitalNum = normalize(row[1].trim());
                   String d = row[2].trim();
                   String m = row[3].trim();
                   String y = row[4].trim();
                   String dateBirth = m+"-"+d+"-"+y;
                   String otherNames =  row[5].trim();
                   String surname =  row[6].trim();
                   String gender = row[7].trim();
                   String dateRegistration = row[8].trim();
                   Map map = DateUtil.getAge(DateUtil.parseStringToDate(dateBirth, "MM-dd-yyyy"), DateUtil.parseStringToDate(dateRegistration, "dd/MM/yyyy"));
                   int age = (int) map.get("age");
                   String ageUnit = (String) map.get("ageUnit");
                   dateRegistration = DateUtil.formatDateString(dateRegistration, "dd/MM/yyyy", "MM-dd-yyyy");
                   String statusRegistration = "HIV+ none ART";
                   String currentStatus = statusRegistration;
                   String dateCurrentStatus = dateRegistration;
                   System.out.println("Patient Id...."+patientId);
                   System.out.println("Hospital...."+hospitalNum);
                   System.out.println("Date birth...."+dateBirth);
                   System.out.println("Date...."+dateRegistration);
                   System.out.println("Age..."+age+ " "+ageUnit);
                   
                   String query = "INSERT INTO patient SET patient_id = " + patientId + ", facility_id = " + facilityId + ", hospital_num = '" + hospitalNum + "', date_birth = '" + dateBirth + "', surname = '" + surname + "', other_names = '" + otherNames + "', gender = '" + gender 
                           + "', date_registration = '" + dateRegistration + "', status_registration = '" + statusRegistration + "', current_status = '" + currentStatus + "', date_current_status = '" + dateCurrentStatus + "'";  
                }
            }
            csvReader.close();        
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void pharm() {
        System.out.println("Processing");
        String fileName = "C:/LAMIS2/pharmacy.csv";
        int facilityId = 1;
        String[] row = null;
        int rowcount = 0;
        try {            
            CSVReader csvReader = new CSVReader(new FileReader(fileName));
            System.out.println("File read..");
            while((row = csvReader.readNext()) != null) {
                rowcount++;
                if (rowcount > 1 ) {
                   long pharmacyId = Long.parseLong(row[0].trim());
                   String hospitalNum = normalize(row[1].trim());
                   long patientId = 0;
                   String d = row[2].trim();
                   String m = row[3].trim();
                   String y = row[4].trim();
                   String dateVisit = m+"-"+d+"-"+y;
                   int duration =  Integer.parseInt(row[5].trim());
                   String regimen =  row[6].trim();
                   System.out.println("Patient Id...."+patientId);
                   System.out.println("Hospital...."+hospitalNum);
                   
                   //String query = "INSERT INTO patient SET patient_id = " + id + ", facility_id = " + id + ", hospital_num = '" + hospitalNum + "', date_birth = '" + dateBirth + "', surname = '" + surname + "', other_names = '" + otherNames + "', gender = '" + gender
                   //        + "', date_registration = '" + dateRegistration + "', status_registration = '" + statusRegistration + "', current_status = '" + currentStatus + "', date_current_status = '" + dateCurrentStatus + "'";  
                }
            }
            csvReader.close();        
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    private void lab() {
        
    }

//    private void savePharmacy() {
//        Pharmacy pharmacy = new Pharmacy();
//        pharmacy.setPatient(patient);
//        pharmacy.setId(id);
//        pharmacy.setDateVisit(DateUtil.parseStringToDate(dateLastRefill, "yyyy-MM-dd"));
//        pharmacy.setDuration(duration.trim().equals("")? 0 : Integer.parseInt(duration));
//        pharmacy.setRegimentypeId((regimentypeId));
//        pharmacy.setId((id));
//        pharmacy.setId(id);
//        pharmacy.setTimeStamp(new java.sql.Timestamp(new java.util.Date().getTime()));
//
//        query = "SELECT drug.name, drug.strength, drug.morning, drug.afternoon, drug.evening, regimendrug.regimendrug_id, regimendrug.regimen_id, regimendrug.drug_id, regimen.regimentype_id "
//                + " FROM drug JOIN regimendrug ON regimendrug.drug_id = drug.drug_id JOIN regimen ON regimendrug.regimen_id = regimen.regimen_id WHERE regimen.regimen_id = " + id;
//        try {
//            preparedStatement = jdbcUtil.getStatement(query);
//            ResultSet rs = preparedStatement.executeQuery();
//            while (rs.next()) {
//                pharmacy.setMorning(rs.getDouble("morning"));
//                pharmacy.setAfternoon(rs.getDouble("afternoon"));
//                pharmacy.setEvening(rs.getDouble("evening"));
//                pharmacy.setRegimendrugId(rs.getLong("regimendrug_id"));
//                PharmacyDAO.save(pharmacy);
//            }            
//        }
//        catch (Exception exception) {
//            jdbcUtil.disconnectFromDatabase();  //disconnect from database
//        }                               
//    }
    
    private String normalize(String hospitalNum) {
        String zeros = "";
        int MAX_LENGTH = 7;
        if(hospitalNum.length() < MAX_LENGTH) {
            for(int i = 0; i < MAX_LENGTH-hospitalNum.length(); i++) {
                zeros = zeros + "0";  
            }
        }
        return zeros+hospitalNum;
    }


    private void executeUpdate(String query) {
        try {
            jdbcUtil = new JDBCUtil();            
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }        
    }            
    
    private ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try {
            jdbcUtil = new JDBCUtil();            
            preparedStatement = jdbcUtil.getStatement(query);
            rs = preparedStatement.executeQuery();
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        } 
        return rs;
    }            
    
}
