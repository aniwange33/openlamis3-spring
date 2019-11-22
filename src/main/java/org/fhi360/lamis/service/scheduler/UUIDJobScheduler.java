/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.scheduler;

import java.util.Map;
import org.fhi360.lamis.service.TableUUIDGenerator;
import org.fhi360.lamis.utility.PropertyAccessor;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author User10
 */
public class UUIDJobScheduler extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jec) {
        Map<String, Object> map = new PropertyAccessor().getSystemProperties();
        String appInstance = (String) map.get("appInstance");
        if (appInstance.equalsIgnoreCase("local")) {
            TableUUIDGenerator generator = new TableUUIDGenerator();
            generator.init();
        }
    }
}
