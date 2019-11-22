/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.ndr;

import org.apache.commons.lang3.StringUtils;

import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.exchange.ndr.schema.CodedSimpleType;
import org.fhi360.lamis.exchange.ndr.schema.ConditionType;
import org.fhi360.lamis.model.RegimenType;

import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.Regimen;
import org.fhi360.lamis.model.RegimenHistory;
import org.fhi360.lamis.model.dto.RegimenIntrospector;
import org.fhi360.lamis.model.repositories.RegimenHistoryRepository;
import org.fhi360.lamis.model.repositories.RegimenTypeRepository;
import org.fhi360.lamis.utility.DateUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.Date;

/**
 * @author user1
 */
public class RegimenTypeMapper {
    private long patientId;
    private JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);
    private final RegimenTypeRepository regimenTypeRepository = ContextProvider.getBean(RegimenTypeRepository.class);
    private final RegimenHistoryRepository regimenHistoryRepository = ContextProvider.getBean(RegimenHistoryRepository.class);

    //public RegimenTypeMapper() {
//    }
//
//    public ConditionType regimenType(long id, ConditionType condition) {
//        this.id = id;
//
//        final RegimenType[] regimenType = {null};
//        final String[] dateVisit = {""};
//        final long[] previousRegimenId = {0};
//        final Date[] dateRegimenStarted = {null};
//        final String[] reasonSwitchedSubs = {""};
//        //for the last record in the resultset
//        final Date[] date = {null};
//        final long[] id = {0};
//        //The query is sorted by date of visit and then by regimen id, beacuse we need to know when a regimen changes chronologically
//        String query = "SELECT DISTINCT pharmacy_id, date_visit, regimen_id, regimentype_id, " +
//                "duration FROM pharmacy WHERE regimentype_id IN (1, 2, 3, 4, 14) AND duration > 0.0 " +
//                "AND patient_id = " + id + " ORDER BY date_visit, regimen_id";
////            jdbcUtil = new JDBCUtil();
//        jdbcTemplate.query(query, rs -> {
//            date[0] = rs.getDate("date_visit");
//            id[0] = rs.getLong("regimen_id");
//            //If this is a new dispensing encounter, reset previous regimen line
//            if (!dateVisit[0].equals(DateUtil.parseDateToString(rs.getDate("date_visit"), "yyyy-MM-dd"))) {
//                //Add regimen to condition if this a new dispensing encounter and regimenType object is not null (ie the first time in the loop)
//                if (regimenType[0] != null) {
//                    //Check for change of regime here and set date the previous regimen ended
//                    if (previousRegimenId[0] != 0) {
//                        if (RegimenIntrospector.substitutedOrSwitched(previousRegimenId[0], rs.getLong("regimen_id"))) {
//                            try {
//                                regimenType[0].setDateRegimenEnded(DateUtil.getXmlDate(rs.getDate("date_visit")));
//                            } catch (DatatypeConfigurationException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    //Add other drugs dispensed on the same date before adding regimen to condition
//                    regimenType[0] = addOtherDrugs(dateVisit[0], regimenType[0]);
//                    condition.getRegimen().add(regimenType[0]);
//                    System.out.println("Regimen type added....");
//                }
//
//                //Any time the date changes reset the dateVisit variable
//                dateVisit[0] = DateUtil.parseDateToString(rs.getDate("date_visit"), "yyyy-MM-dd");
//                //Instantiate a new regimen report for each date
//                regimenType[0] = new RegimenType();
//                regimenType[0].setVisitID(Long.toString(rs.getLong("pharmacy_id")));
////                try {
////                  //  regimenType[0].setVisitDate(DateUtil.getXmlDate(rs.getDate("date_visit")));
////                } catch (DatatypeConfigurationException e) {
////                    e.printStackTrace();
////                }
//            }
//
//            CodedSimpleType cst = CodeSetResolver.getRegimenById(rs.getLong("regimen_id"));
//            if (cst.getCode() != null && regimenType[0] != null) {
//             //   regimenType[0].setPrescribedRegimen(cst);
//                try {
//                 //   regimenType[0].setPrescribedRegimenDispensedDate(DateUtil.getXmlDate(rs.getDate("date_visit")));
//                } catch (DatatypeConfigurationException e) {
//                    e.printStackTrace();
//                }
//
//                String description = regimenTypeRepository.getOne(rs.getLong("regimentype_id")).getDescription();
//
//                if (description.contains("ART")) description = "ART";
//                else if (description.contains("TB")) description = "TB";
//                else if (description.contains("PEP")) description = "PEP";
//                else if (description.contains("ARV")) description = "PMTCT";
//                else if (description.contains("CTX")) description = "CTX";
//                regimenType[0].setPrescribedRegimenTypeCode(CodeSetResolver.getCode("REGIMEN_TYPE", description));
//
//                String regimenLineCode = "";
//                if (rs.getLong("regimentype_id") == 1 || rs.getLong("regimentype_id") == 3) {
//                    regimenLineCode = "First Line";
//                    if (previousRegimenId[0] != 0 && previousRegimenId[0] != rs.getLong("regimen_id")) {
//                        if (RegimenIntrospector.substitutedOrSwitched(previousRegimenId[0], rs.getLong("regimen_id")))
//                            regimenLineCode = "First Line Substitution";
//                    }
//                }
//                if (rs.getLong("regimentype_id") == 2 || rs.getLong("regimentype_id") == 4) {
//                    regimenLineCode = "Second Line";
//                    if (previousRegimenId[0] != 0 && previousRegimenId[0] != rs.getLong("regimen_id")) {
//                        if (RegimenIntrospector.substitutedOrSwitched(previousRegimenId[0], rs.getLong("regimen_id")))
//                            regimenLineCode = "Second Line Substitution";
//                    }
//                }
//                if (rs.getLong("regimentype_id") == 14) regimenLineCode = "Third Line";
//                regimenType[0].setPrescribedRegimenLineCode(CodeSetResolver.getCode("REGIMEN_LINE", regimenLineCode));
//
//                int duration = (int) rs.getDouble("duration");
//                regimenType[0].setPrescribedRegimenDuration(Integer.toString(duration));
//                //regimenType.setReasonForPoorAdherence("");
//                //regimenType.setPoorAdherenceIndicator(1);
//
//                //If regimen changes set the start date of the regimen
//                if (previousRegimenId[0] != rs.getLong("regimen_id")) {
//                    Patient patient = new Patient();
//                    patient.setId(id);
//                    org.fhi360.lamis.model.RegimenType regimenType2 = new RegimenType();
//                    regimenType2.setId(rs.getLong("regimentype_id"));
//                    Regimen regimen = new Regimen();
//                    regimen.setId(  rs.getLong("regimen_id"));
//                    RegimenHistory regimenhistory = regimenHistoryRepository.findByPatientRegimentypeAndRegimen(patient,regimenType2,regimen
//                   );
//                    dateRegimenStarted[0] = DateUtil.convertToDateViaSqlDate(regimenhistory.getDateVisit());
//                    reasonSwitchedSubs[0] = StringUtils.trimToEmpty(regimenhistory.getReasonSwitchedSubs());
//                    previousRegimenId[0] = rs.getLong("regimen_id");
//                }
//
//                if (dateRegimenStarted[0] == null)
//                    dateRegimenStarted[0] = getDateRegimenStarted(id, rs.getLong("regimen_id"));
//                try {
//                    regimenType[0].setDateRegimenStarted(DateUtil.getXmlDate(dateRegimenStarted[0]));
//                } catch (DatatypeConfigurationException e) {
//                    e.printStackTrace();
//                }
//                if (!reasonSwitchedSubs[0].isEmpty()) {
//                    regimenType[0].setReasonForRegimenSwitchSubs(reasonSwitchedSubs[0]);
//                    //regimenType.setSubstitutionIndicator();
//                    //regimenType.setSwitchIndicator();
//                }
//                regimenType[0].setPrescribedRegimenCurrentIndicator(true);
//            }
//            if (regimenType[0] != null) {
//                //Check for change of regime here and set date the previous regimen ended
//                if (previousRegimenId[0] != 0) {
//                    if (RegimenIntrospector.substitutedOrSwitched(previousRegimenId[0], id[0])) {
//                        try {
//                            regimenType[0].setDateRegimenEnded(DateUtil.getXmlDate(date[0]));
//                        } catch (DatatypeConfigurationException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                //Add other drugs dispensed on the same date before adding regimen to condition
//                regimenType[0] = addOtherDrugs(dateVisit[0], regimenType[0]);
//                condition.getRegimen().add(regimenType[0]);
//                System.out.println("Regimen type added....");
//            }
//        });
//        return condition;
//    }
//
//    private RegimenType addOtherDrugs(String dateVisit, RegimenType regimenType) {
//        final long[] previousRegimenId = {0};
//        final Date[] dateRegimenStarted = {null};
//        String query = "SELECT DISTINCT pharmacy_id, date_visit, regimen_id, regimentype_id, " +
//                "duration FROM pharmacy WHERE regimentype_id IN (5, 6, 7, 8, 9, 10, 11, 12) AND duration > 0.0 " +
//                "AND patient_id = " + id + " AND date_visit = '" + dateVisit + "' ORDER BY date_visit, regimentype_id";
//        jdbcTemplate.query(query, rs -> {
//            String description = RegimenJDBC.getRegimen(rs.getLong("regimen_id"));
//            CodedSimpleType cst = CodeSetResolver.getCodedSimpleType("OI_REGIMEN,TB_REGIMEN", description);
//
//            if (cst.getCode() != null && regimenType != null) {
//                System.out.println("Coded simple....." + cst.getCodeDescTxt());
//                regimenType.setPrescribedRegimen(cst);
//                try {
//                    regimenType.setPrescribedRegimenDispensedDate(DateUtil.getXmlDate(rs.getDate("date_visit")));
//                } catch (DatatypeConfigurationException e) {
//                    e.printStackTrace();
//                }
//                description = RegimenJDBC.getRegimenType(rs.getLong("regimentype_id"));
//                regimenType.setPrescribedRegimenTypeCode(CodeSetResolver.getCode("REGIMEN_TYPE", description));
//
//                int duration = (int) rs.getDouble("duration");
//                regimenType.setPrescribedRegimenDuration(Integer.toString(duration));
//
//                //If regimen changes set the start date of the regimen
//                if (previousRegimenId[0] != rs.getLong("regimen_id")) {
//                    dateRegimenStarted[0] = getDateRegimenStarted(id, rs.getLong("regimen_id"));
//                    previousRegimenId[0] = rs.getLong("regimen_id");
//                }
//                if (dateRegimenStarted[0] != null) {
//                    try {
//                        regimenType.setDateRegimenStarted(DateUtil.getXmlDate(dateRegimenStarted[0]));
//                    } catch (DatatypeConfigurationException e) {
//                        e.printStackTrace();
//                    }
//                }
//                regimenType.setPrescribedRegimenCurrentIndicator(true);
//            }
//        });
//        return regimenType;
//    }
//
//    public Date getDateRegimenEnded(long id, long id) {
//        final Date[] dateRegimenEnded = {null};
//        String query = "SELECT date_visit, duration FROM pharmacy WHERE patient_id = " + id + " " +
//                "AND regimen_id = " + id + " ORDER BY date_visit DESC LIMIT 1";
//        jdbcTemplate.query(query, rs -> {
//            dateRegimenEnded[0] = DateUtil.addDay(rs.getDate("date_visit"), (int) rs.getDouble("duration")); //Math.round(rs.getDouble("duration"));
//        });
//        return dateRegimenEnded[0];
//    }
//
//    public Date getDateRegimenStarted(long id, long id) {
//        final Date[] dateRegimenEnded = {null};
//        String query = "SELECT date_visit, duration FROM pharmacy WHERE patient_id = " + id + " AND regimen_id = "
//                + id + " ORDER BY date_visit ASC LIMIT 1";
////            jdbcUtil = new JDBCUtil();
//        jdbcTemplate.query(query, rs -> {
//            dateRegimenEnded[0] = rs.getDate("date_visit");
//        });
//        return dateRegimenEnded[0];
//    }

}
