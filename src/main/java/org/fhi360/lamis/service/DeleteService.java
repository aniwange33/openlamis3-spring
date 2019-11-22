/**
 * @author aalozie
 */
package org.fhi360.lamis.service;

import org.fhi360.lamis.utility.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class DeleteService {
    private static Logger LOG = LoggerFactory.getLogger(DeleteService.class);
    private final JdbcTemplate jdbcTemplate;

    public DeleteService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public synchronized void deletePatient(long facilityId, long patientId) {
        LOG.info("Delete patient {} at facility {}", patientId, facilityId);
        //String[] tables = {"ENCOUNTER", "DEVOLVE", "DRUGTHERAPY", "APPOINTMENT",  "MHTC", "VALIDATED","PATIENTCASEMANAGER","EAC","partnerinformation", "dmscreenhistory", "tbscreenhistory", "child", "maternalfollowup", "delivery", "anc", "chroniccare", "adrhistory", "oihistory", "adherehistory", "statushistory", "regimenhistory", "clinic", "pharmacy", "laboratory", "patient"};
        String[] tables = {"ENCOUNTER", "DEVOLVE", "DRUGTHERAPY", "APPOINTMENT", "VALIDATED", "PATIENTCASEMANAGER", "EAC", "partnerinformation", "dmscreenhistory", "tbscreenhistory", "child", "maternalfollowup", "delivery", "anc", "chroniccare", "adrhistory", "oihistory", "adherehistory", "statushistory", "regimenhistory", "clinic", "pharmacy", "laboratory", "patient"};
        for (String table : tables) {
            LOG.info("Deleting from table: {}", table);
            if (table.equalsIgnoreCase("child")) {
                executeUpdate("DELETE FROM childfollowup WHERE child_id IN (SELECT child_id FROM child WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + ")");
            }
            executeUpdate("DELETE FROM " + table + " WHERE facility_id = " + facilityId + " AND patient_id = " + patientId);
        }
    }

    public synchronized void deleteClinic(long facilityId, long patientId, Date date) {
        deleteAdr(facilityId, patientId, date, 1);
        deleteOi(facilityId, patientId, date);

        String query = "DELETE FROM clinic WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_visit = '" + DateUtil.parseDateToString(date, "yyyy-MM-dd") + "'";
        executeUpdate(query);
    }

    public synchronized void deletePharmacy(long facilityId, long patientId, Date date) {
        deleteAdr(facilityId, patientId, date, 2);
        deleteAdhere(facilityId, patientId, date);

        String query = "DELETE FROM pharmacy WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_visit = '" + DateUtil.parseDateToString(date, "yyyy-MM-dd") + "'";
        executeUpdate(query);
    }

    public synchronized void deleteLaboratory(long facilityId, long patientId, Date date) {
        String query = "DELETE FROM laboratory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_reported = '" + DateUtil.parseDateToString(date, "yyyy-MM-dd") + "'";
        executeUpdate(query);
    }

    public synchronized void deleteStatus(long facilityId, long patientId, String currentStatus, Date date) {
        String query = "DELETE FROM statushistory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND current_status = '" + currentStatus + "' AND date_current_status = '" + DateUtil.parseDateToString(date, "yyyy-MM-dd") + "'";
        executeUpdate(query);
    }

    public synchronized void deleteOi(long facilityId, long patientId, Date date) {
        String query = "DELETE FROM oihistory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_visit = '" + DateUtil.parseDateToString(date, "yyyy-MM-dd") + "'";
        executeUpdate(query);
    }

    public synchronized void deleteAdr(long facilityId, long patientId, Date date, int screener) {
        String query = "DELETE FROM adrhistory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_visit = '" + DateUtil.parseDateToString(date, "yyyy-MM-dd") + "' AND screener = " + screener;
        executeUpdate(query);
    }

    public synchronized void deleteAdhere(long facilityId, long patientId, Date date) {
        String query = "DELETE FROM adherehistory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_visit = '" + DateUtil.parseDateToString(date, "yyyy-MM-dd") + "'";
        executeUpdate(query);
    }

    public synchronized void deleteChroniccare(long facilityId, long patientId, Date date) {
        deleteTbscreen(facilityId, patientId, date);
        deleteDmscreen(facilityId, patientId, date);

        String query = "DELETE FROM chroniccare WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_visit = '" + DateUtil.parseDateToString(date, "yyyy-MM-dd") + "'";
        executeUpdate(query);
    }

    public synchronized void deleteTbscreen(long facilityId, long patientId, Date date) {
        String query = "DELETE FROM tbscreenhistory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_visit = '" + DateUtil.parseDateToString(date, "yyyy-MM-dd") + "'";
        executeUpdate(query);
    }

    public synchronized void deleteDmscreen(long facilityId, long patientId, Date date) {
        String query = "DELETE FROM dmscreenhistory WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_visit = '" + DateUtil.parseDateToString(date, "yyyy-MM-dd") + "'";
        executeUpdate(query);
    }

    public synchronized void deleteSpecimen(long facilityId, String labno) {
        String query = "DELETE FROM specimen WHERE facility_id = " + facilityId + " AND labno = '" + labno + "'";
        executeUpdate(query);
        query = "DELETE FROM eid WHERE facility_id = " + facilityId + " AND labno = '" + labno + "'";
        executeUpdate(query);
    }

    public synchronized void deleteAnc(long facilityId, long ancId) {
        String query = "DELETE FROM partnerinformation WHERE facility_id = " + facilityId + " AND anc_id = " + ancId;
        executeUpdate(query);
        query = "DELETE FROM childfollowup WHERE facility_id = " + facilityId + " AND child_id IN (SELECT child_id FROM child WHERE facility_id = " + facilityId + " AND anc_id = " + ancId + ")";
        executeUpdate(query);
        query = "DELETE FROM maternalfollowup WHERE facility_id = " + facilityId + " AND anc_id = " + ancId;
        executeUpdate(query);
        query = "DELETE FROM child WHERE facility_id = " + facilityId + " AND anc_id = " + ancId;
        executeUpdate(query);
        query = "DELETE FROM delivery WHERE facility_id = " + facilityId + " AND anc_id = " + ancId;
        executeUpdate(query);
        query = "DELETE FROM anc WHERE facility_id = " + facilityId + " AND anc_id = " + ancId;
        executeUpdate(query);
    }

    public synchronized void deleteDelivery(long facilityId, long deliveryId) {
        String query = "DELETE FROM childfollowup WHERE facility_id = " + facilityId + " AND child_id IN (SELECT child_id FROM child WHERE facility_id = " + facilityId + " AND delivery_id = " + deliveryId + ")";
        executeUpdate(query);
        query = "DELETE FROM child WHERE facility_id = " + facilityId + " AND delivery_id = " + deliveryId;
        executeUpdate(query);
        query = "DELETE FROM delivery WHERE facility_id = " + facilityId + " AND delivery_id = " + deliveryId;
        executeUpdate(query);
    }

    public synchronized void deleteMaternalfollowup(long facilityId, long maternalfollowupId) {
        String query = "DELETE FROM maternalfollowup WHERE facility_id = " + facilityId + " AND maternalfollowup_id = " + maternalfollowupId;
        executeUpdate(query);
    }

    public synchronized void deleteChildfollowup(long facilityId, long childfollowupId) {
        String query = "DELETE FROM childfollowup WHERE facility_id = " + facilityId + " AND childfollowup_id = " + childfollowupId;
        executeUpdate(query);
    }

    public synchronized void deleteChild(long facilityId, long childId) {
        String[] tables = {"childfollowup", "child"};
        for (String table : tables) {
            String query = "DELETE FROM " + table + " WHERE facility_id = " + facilityId + " AND child_id = " + childId;
            executeUpdate(query);
        }
    }

    public synchronized void deleteEac(long facilityId, long eacId) {
        String query = "DELETE FROM eac WHERE facility_id = " + facilityId + " AND eac_id = " + eacId;
        executeUpdate(query);
    }

    public synchronized void deleteDevolve(long facilityId, long devolveId) {
        String query = "DELETE FROM devolve WHERE facility_id = " + facilityId + " AND devolve_id = " + devolveId;
        executeUpdate(query);
    }

    public synchronized void deleteNigqual(long facilityId, int reviewPeriodId) {
        String query = "DELETE FROM nigqual WHERE facility_id = " + facilityId + " AND review_period_id = " + reviewPeriodId;
        executeUpdate(query);
    }

    public synchronized void deleteEncounter(long facilityId, long patientId, Date date) {
        String query = "DELETE FROM encounter WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_visit = '" + DateUtil.parseDateToString(date, "yyyy-MM-dd") + "'";
        executeUpdate(query);

        query = "DELETE FROM pharmacy WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_visit = '" + DateUtil.parseDateToString(date, "yyyy-MM-dd") + "'";
        executeUpdate(query);
    }

    public synchronized void deleteDrugtherapy(long facilityId, long patientId, Date date) {
        String query = "DELETE FROM drugtherapy WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_visit = '" + DateUtil.parseDateToString(date, "yyyy-MM-dd") + "'";
        executeUpdate(query);
    }

    public synchronized void deleteAppointment(long facilityId, long patientId, Date date) {
        String query = "DELETE FROM appointment WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " AND date_tracked = '" + DateUtil.parseDateToString(date, "yyyy-MM-dd") + "'";
        executeUpdate(query);
    }

    public synchronized void deleteHts(long facilityId, long htsId, long assessmentId) {
        executeUpdate("DELETE FROM indexcontact WHERE facility_id = " + facilityId + " AND hts_id = " + htsId);
        executeUpdate("DELETE FROM hts WHERE facility_id = " + facilityId + " AND hts_id = " + htsId);
        executeUpdate("DELETE FROM assessment WHERE facility_id = " + facilityId + " AND assessment_id = " + assessmentId);

    }

    public synchronized void deleteIndexcontact(long facilityId, long indexcontactId) {
        executeUpdate("DELETE FROM indexcontact WHERE facility_id = " + facilityId + " AND indexcontact_id = " + indexcontactId);

    }


    public synchronized void deleteMhtc(long communitypharmId, int month, int year) {
        String query = "DELETE FROM mhtc WHERE communitypharm_id = " + communitypharmId + " AND month = " + month + " AND year = " + year;
        executeUpdate(query);
    }

    @Transactional
    public void executeUpdate(String query) {
        jdbcTemplate.execute(query);
    }
}
