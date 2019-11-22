/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.scheduler;

import java.util.Map;
import org.fhi360.lamis.service.SyncService;
import org.fhi360.lamis.utility.PropertyAccessor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
//import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 *
 * @author user10
 */
@Component
public class LockSyncFolderJobScheduler  {
    private final SyncService syncService = new SyncService();
//extends QuartzJobBean
    //@Override
    protected void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        //This job is executed every 3 hours triggered by the syncCronTrigger in the applicationContext.xml
        try {
            Map<String, Object> map = new PropertyAccessor().getSystemProperties();
            String appInstance = (String) map.get("appInstance");
            if(appInstance.equalsIgnoreCase("server")) {
                syncService.lockSyncFolder();
            }
        } 
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
}
