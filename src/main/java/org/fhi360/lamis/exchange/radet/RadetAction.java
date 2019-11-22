/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.radet;


import org.fhi360.lamis.config.ApplicationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;

@RestController
public class RadetAction {
    private HashMap parameterMap = new HashMap();
    private final ApplicationProperties applicationProperties;
    private final RadetService radetService;

    public RadetAction(ApplicationProperties applicationProperties, RadetService radetService) {
        this.applicationProperties = applicationProperties;
        this.radetService = radetService;
    }


    @GetMapping("/parse-file")
    public String parseRadetFile(HttpSession session) {
        radetService.clearClientList(session);


        int fileUploaded = 0;
        if (session.getAttribute("fileUploaded") != null) {
            fileUploaded = (Integer) session.getAttribute("fileUploaded");
        }
        if (fileUploaded == 1) {
            //new RadetService().parseFile((String) request.getSession().getAttribute("fileName"));
        }
        return "SUCCESS";
    }

    @GetMapping("/build-client-list")
    public String buildClientList(HttpSession session) {
        radetService.buildClientList(session);
        radetService.retrieveClientList(session);
        return "SUCCESS";
    }

    @GetMapping("/radet-grid")
    public String radetGrid(HttpSession session) {
        radetService.retrieveClientList(session);
        return "SUCCESS";
    }

    @GetMapping("/radet-report")
    public String radetReport(@RequestParam String reportType, HttpSession session) {
        radetService.radetReport(reportType, session);
        if (reportType.equals("1")) parameterMap.put("reportTitle", "Clients with no ARV pickup");
        if (reportType.equals("2")) parameterMap.put("reportTitle", "Clients with no Regimen at ART start");
        if (reportType.equals("3")) parameterMap.put("reportTitle", "Clients who are Lost to Follow Up");
        if (reportType.equals("4")) parameterMap.put("reportTitle", "Clients who are Due for Viral Load Test");
        return "SUCCESS";
    }

    public String dispatcher(HttpSession session) {
        String contextPath = applicationProperties.getContextPath();
        // new RadetConverter().convertExcel();
        return "SUCCESS";
    }

//        Thread thread = new Thread(){
//            public void run(){
//              System.out.println("Thread Running");
//              setFileName(new RadetConverter().convertExcel());
//            }
//        };
//        thread.start();
//        return SUCCESS;   
//        }

    public void performRadet() {
        try {
            System.out.println("Started From the Radet script at: " + new Date());
            //   RadetAsyncAction radetAsyncAction = new RadetAsyncAction("Thread: "+new Date().getTime() );
            //  radetAsyncAction.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
