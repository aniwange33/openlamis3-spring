/**
 * @author AALOZIE
 */
package org.fhi360.lamis.service.scheduler;

import org.fhi360.lamis.service.SyncService;
import org.fhi360.lamis.utility.PropertyAccessor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
@Component
public class DataSyncJobScheduler extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        //This job is executed every 3 hours triggered by the syncCronTrigger in the applicationContext.xml
        //The startTransaction() method loops through all available upload folders, check if the webservice has 
        //transfered xml files into the folder and locked the folder.
        //This job is executed only on the server 
        try {
            Map<String, Object> map = new PropertyAccessor().getSystemProperties();
            String appInstance = (String) map.get("appInstance");
            String enableSync = (String) map.get("enableSync");
            if (appInstance.equalsIgnoreCase("local") && enableSync.equalsIgnoreCase("0")) {
                Date threadDate = new Date();
                System.out.println("Started From the job scheduler at: " + threadDate);
                SyncService syncService = new SyncService();
                syncService.startTransaction();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
