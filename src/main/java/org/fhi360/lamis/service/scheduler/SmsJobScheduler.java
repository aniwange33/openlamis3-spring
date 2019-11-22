/**
 *
 * @author AALOZIE
 *//*

package org.fhi360.lamis.service.scheduler;

import org.fhi360.lamis.utility.PropertyAccessor;
import java.util.Map;
import org.fhi360.lamis.service.sms.ModemGatewayService;
import org.fhi360.lamis.service.sms.SmsService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SmsJobScheduler extends QuartzJobBean {    
    private final ModemGatewayService smsGatewayService = new ModemGatewayService();
    private final SmsService smsService = new SmsService();
    
    @Override
    protected void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        //This job is executed every 4 hours 30 mins triggered by the smsCronTrigger in the applicationContext.xml
        //The startSmsService() method starts sms gateway if a modem is enabled for sending   
        //otherwise the runSchedule() method uses the internet gateway to send sms
        try {
            Map<String, Object> map = new PropertyAccessor().getSystemProperties();
            String modemStatus = (String) map.get("modemStatus");
            if(modemStatus.equals("ON")) {
                if(!smsGatewayService.isStarted()) smsGatewayService.startSmsService();
                //smsGatewayService.readSms(); //Used when modem cannot poll messages
            }
            System.out.println("Running sms service........");
            smsService.runSchedule();
        } 
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
*/
