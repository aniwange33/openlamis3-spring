package org.fhi360.lamis.views;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Api(tags = "Login", description = " ")
public class PageController {

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logOut() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "patient/home_page";
    }

    /*
     * Patient page controller section
     * */

    @GetMapping("/patient")
    public String patient() {
        return "patient/patient_search";
    }

    @GetMapping("/patient/new")
    public String patientNew() {
        return "patient/patient_data";
    }

    @GetMapping("/patient/edit/{id}")
    public ModelAndView patientEdit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("patientId", id);
        //model.addAttribute("facilityId", SecurityUtils.getCurrentFacility().get().getId());
        return new ModelAndView("patient/patient_data", model);
    }

    @GetMapping("/patient/patient-query")
    public String patientQuery() {
        return "patient/patient_query";
    }
    /*
     * Clinic page controller section
     * */

    @GetMapping("/clinic")
    public String clinic() {
        return "clinic/clinic_search";
    }

    @GetMapping("/clinic/center/{id}")
    public ModelAndView clinicCenter(@PathVariable Long id, ModelMap model) {
        model.addAttribute("patientId", id);
        model.addAttribute("facilityId", 2);
        //model.addAttribute("facilityId", SecurityUtils.getCurrentFacility().get().getId());
        return new ModelAndView("clinic/clinic_center", model);
    }

    @GetMapping("/clinic/new/{id}")
    public ModelAndView clinicNew(@PathVariable Long id, ModelMap model) {
        model.addAttribute("patientId", id);
        return new ModelAndView("clinic/clinic_data", model);
    }

    @GetMapping("/clinic/{id}")
    public ModelAndView clinicEdit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("clinicId", id);
        //model.addAttribute("facilityId", SecurityUtils.getCurrentFacility().get().getId());
        return new ModelAndView("clinic/clinic_data", model);
    }

    @GetMapping("/clinic/commence/new/{id}")
    public ModelAndView commenceNew(@PathVariable Long id, ModelMap model) {
        model.addAttribute("patientId", id);
        return new ModelAndView("clinic/commence_data", model);
    }

    @GetMapping("/clinic/commence/edit/{id}")
    public ModelAndView commenceEdit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("clinicId", id);
        return new ModelAndView("clinic/commence_data", model);
    }

    @GetMapping("/clinic/chronic-care/new/{id}")
    public ModelAndView chronicCareNew(@PathVariable Long id, ModelMap model) {
        model.addAttribute("patientId", id);
        return new ModelAndView("clinic/chroniccare_data", model);
    }

    @GetMapping("/clinic/chronic-care/edit/{id}")
    public ModelAndView chronicCareEdit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("chroniccareId", id);
        return new ModelAndView("clinic/chroniccare_data", model);
    }

    @GetMapping("/clinic/eac/new/{id}")
    public ModelAndView eacNew(@PathVariable Long id, ModelMap model) {
        model.addAttribute("patientId", id);
        return new ModelAndView("clinic/eac_data", model);
    }

    @GetMapping("/clinic/eac/edit/{id}")
    public ModelAndView eacEdit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("eacId", id);
        return new ModelAndView("clinic/eac_data", model);
    }

    @GetMapping("/clinic/status-history/new/{id}")
    public ModelAndView statusHistoryNew(@PathVariable Long id, ModelMap model) {
        model.addAttribute("patientId", id);
        return new ModelAndView("patient/status_data", model);
    }

    @GetMapping("/clinic/status-history/edit/{id}")
    public ModelAndView statusHistoryEdit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("historyId", id);
        return new ModelAndView("patient/status_data", model);
    }

    /*
     * Pharmacy page controller section
     * */
    @GetMapping("/pharmacy")
    public String pharmacy() {
        return "pharmacy/pharmacy_search";
    }

    @GetMapping("/pharmacy/new/{id}")
    public ModelAndView pharmacyNew(@PathVariable Long id, ModelMap model) {
        model.addAttribute("patientId", id);
        return new ModelAndView("pharmacy/pharmacy_data", model);
    }

    @GetMapping("/pharmacy/edit/{id}")
    public ModelAndView pharmacyEdit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("pharmacyId", id);
        return new ModelAndView("pharmacy/pharmacy_data", model);
    }

    @GetMapping("/pharmacy/devolve")
    public String devolve() {
        return "pharmacy/devolve_search";
    }

    @GetMapping("/pharmacy/devolve/new/{id}")
    public ModelAndView pharmacyDevolveNew(@PathVariable Long id, ModelMap model) {
        model.addAttribute("patientId", id);
        return new ModelAndView("pharmacy/devolve_data", model);
    }

    @GetMapping("/pharmacy/devolve/edit/{id}")
    public ModelAndView pharmacyDevolveEdit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("devolveId", id);
        return new ModelAndView("pharmacy/devolve_data", model);
    }

    @GetMapping("/pharmacy/prescription")
    public String prescriptionPharmacy() {
        return "pharmacy/pharmacy_prescription_search";
    }

    /*
     * Laboratory page controller section
     * */
    @GetMapping("/laboratory")
    public String laboratory() {
        return "laboratory/laboratory_search";
    }

    @GetMapping("/laboratory/new/{id}")
    public ModelAndView laboratoryNew(@PathVariable Long id, ModelMap model) {
        model.addAttribute("patientId", id);
        return new ModelAndView("laboratory/laboratory_data", model);
    }

    @GetMapping("/laboratory/edit/{id}")
    public ModelAndView laboratoryEdit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("laboratoryId", id);
        return new ModelAndView("laboratory/laboratory_data", model);
    }

    @GetMapping("/laboratory/prescription")
    public String prescriptionLaboratory() {
        return "laboratory/laboratory_prescription_search";
    }


    /*
     * PMTCT page controller section
     * */
    @GetMapping("/pmct")
    public String pmct() {
        return "pmtct/anc_search";
    }

    @GetMapping("/pmtct/anc/new/{id}")
    public ModelAndView ancNew(@PathVariable Long id, ModelMap model) {
        model.addAttribute("patientId", id);
        return new ModelAndView("pmtct/anc_data", model);
    }

    @GetMapping("/pmtct/anc/edit/{id}")
    public ModelAndView ancEdit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("ancId", id);
        return new ModelAndView("pmtct/anc_data", model);
    }

    @GetMapping("/pmtct/delivery")
    public ModelAndView delivery(ModelMap model) {
        return new ModelAndView("pmtct/delivery_search", model);
    }

    @GetMapping("/pmtct/delivery/new/{id}")
    public ModelAndView deliveryNew(@PathVariable Long id, ModelMap model) {
        model.addAttribute("patientId", id);
        return new ModelAndView("pmtct/delivery_data", model);
    }

    @GetMapping("/pmtct/delivery/edit/{id}")
    public ModelAndView deliveryEdit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("deliveryId", id);
        return new ModelAndView("pmtct/delivery_data", model);
    }

    @GetMapping("/pmtct/anc/patient/{id}/mother/{motherId}/new")
    public ModelAndView ancNewMother(@PathVariable Long id, @PathVariable Long motherId, ModelMap map) {
        map.addAttribute("patientId", id);
        map.addAttribute("motherId", motherId);
        return new ModelAndView("pmtct/anc_data", map);
    }


    @GetMapping("/pmtct/maternal-followup")
    public ModelAndView maternalFollowup() {
        return new ModelAndView("pmtct/maternalfollowup_search");
    }

    @GetMapping("/pmtct/maternal-followup/edit/{id}")
    public ModelAndView maternalFollowupEdit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("maternalfollowupId", id);
        return new ModelAndView("pmtct/maternalfollowup_data", model);
    }

    @GetMapping("/pmtct/maternal-followup/new/{id}")
    public ModelAndView maternalFollowupNew(@PathVariable Long id, ModelMap model) {
        model.addAttribute("patientId", id);
        return new ModelAndView("pmtct/maternalfollowup_data", model);
    }

    @GetMapping("/pmtct/child-followup")
    public ModelAndView childFollowup() {
        return new ModelAndView("pmtct/childfollowup_search");
    }

    @GetMapping("/pmtct/child-followup/new/{id}")
    public ModelAndView childFollowupNew(@PathVariable Long id, ModelMap model) {
        model.addAttribute("childId", id);
        return new ModelAndView("pmtct/childfollowup_data", model);
    }

    @GetMapping("/pmtct/child-followup/edit/{id}")
    public ModelAndView childFollowupEdit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("childfollowupId", id);
        return new ModelAndView("pmtct/childfollowup_data", model);
    }

    @GetMapping("/case-management")
    public String caseManagement() {
        return "patient/status_search";
    }

    @GetMapping("/case-management/appointment/new/{id}")
    public ModelAndView appointmentNew(@PathVariable Long id, ModelMap model) {
        model.addAttribute("patientId", id);
        return new ModelAndView("patient/appointment_data", model);
    }

    @GetMapping("/case-management/assign-client")
    public String assign() {
        return "clinic/assign_client";
    }

    @GetMapping("/case-management/reassign-client")
    public String reassignClient() {
        return "clinic/reassign_client";
    }

    @GetMapping("/case-management/appointment")
    public String appointment1() {
        return "patient/appointment_search";
    }

    /*
     * HTS page controller section
     * */
    @GetMapping("/hts")
    public String hts() {
        return "hts/hts_search";
    }

    @GetMapping("/hts/assessment/new")
    public String assessmentNew() {
        return "hts/assessment_data";
    }

    @GetMapping("/hts/edit/{id}")
    public ModelAndView htsEdit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("htsId", id);
        return new ModelAndView("hts/hts_data", model);
    }

    @GetMapping("/hts/enrol/{id}")
    public ModelAndView htsEnrol(@PathVariable Long id, ModelMap model) {
        model.addAttribute("htsId", id);
        return new ModelAndView("patient/patient_data", model);
    }

    @GetMapping("/hts/index-contact")
    public String indexContact() {
        return "hts/indexcontact_search";
    }

    @GetMapping("/hts/index-contact/new/{id}")
    public ModelAndView indexContactNew(@PathVariable Long id, ModelMap model) {
        model.addAttribute("htsId", id);
        return new ModelAndView("hts/indexcontact_data", model);
    }

    @GetMapping("/hts/index-contact/edit/{id}")
    public ModelAndView indexContactEdit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("indexcontactId", id);
        return new ModelAndView("hts/indexcontact_data", model);
    }


    /*
     * Converter page controller section
     * */

    @GetMapping("/converter")
    public String converter() {
        return "administration/converter_excel";
    }

    @GetMapping("/converter/radet")
    public String ConverterRadet() {
        return "administration/converter_radet";
    }

    @GetMapping("/converter/retention")
    public String ConverterRetention() {
        return "administration/converter_retention";
    }

    @GetMapping("/converter/nigqual")
    public String converterNigqual() {
        return "administration/converter_nigqual";
    }

    @GetMapping("/converter/ndr")
    public String converterNdr() {
        return "administration/converter_ndr";
    }


    /*
     * Maintenance page controller section
     * */
    @GetMapping("/maintenance")
    public String export() {
        return "administration/export";
    }

    @GetMapping("/maintenance/import")
    public String imports() {
        return "administration/import";
    }

    @GetMapping("/maintenance/upload")
    public String upload() {
        return "administration/upload";
    }


    @GetMapping("/maintenance/sync")
    public String sync() {
        return "administration/sync";
    }


    @GetMapping("/maintenance/update")
    public String update() {
        return "administration/updates";
    }


    @GetMapping("/maintenance/dqa")
    public String dqa() {
        return "administration/dqa";
    }

    @GetMapping("/maintenance/cleanup")
    public String cleanup() {
        return "administration/cleanup";
    }

    @GetMapping("/maintenance/radet")
    public String radet() {
        return "administration/radet";
    }

    @GetMapping("/maintenance/facility-switch")
    public String facilitySwitch() {
        return "administration/facility_switch";
    }
    /*
    Visualizer Controller Section
     */


    @GetMapping("/visualizer")
    public String visualizer() {
        return "visualizer/treatment_dashboard";
    }

    @GetMapping("/visualizer/events/pharmacy")
    public String eventsPharmacy() {
        return "events/event_pharmacy";
    }

    @GetMapping("/visualizer/events/analyzer")
    public String eventsAnalyzer() {
        return "events/event_analyzer";
    }

    @GetMapping("/visualizer/events/validate")
    public String eventsValidate() {
        return "events/validate";
    }


    @GetMapping("/visualizer/events/retention-tracker")
    public String eventsRetentionTracker() {
        return "events/retention_tracker";
    }

    @GetMapping("/visualizer/events/treatment-tracker")
    public String eventsTreatmentTracker() {
        return "events/treatment_tracker";
    }

    @GetMapping("/visualizer/events/daily-tracker")
    public String eventsDailyTracker() {
        return "events/daily_tracker";
    }

    @GetMapping("/visualizer/events/upload-tracker")
    public String eventsUploadTracker() {
        return "events/upload_tracker";
    }

    /*
     * Setup page controller section
     * */

    @GetMapping("/setup")
    public String setup() {
        return "administration/user_data";
    }


    @GetMapping("/setup/facility")
    public String setupFacility() {
        return "administration/facility_data";
    }

    @GetMapping("/setup/community-pharmacy")
    public String setupCommunityPharmacy() {
        return "administration/communitypharm_data";
    }

    @GetMapping("/setup/case-manager")
    public String setupCaseManage() {
        return "administration/casemanager_data";
    }



}
