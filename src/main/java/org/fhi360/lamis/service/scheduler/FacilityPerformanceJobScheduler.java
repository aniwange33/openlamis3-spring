/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.scheduler;

import org.fhi360.lamis.service.indicator.FacilityPerformanceService;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 *
 * @author user10
 */
@Component
public class FacilityPerformanceJobScheduler  extends QuartzJobBean {
    private final FacilityPerformanceService facilityPerformanceService;

    public FacilityPerformanceJobScheduler(FacilityPerformanceService facilityPerformanceService) {
        this.facilityPerformanceService = facilityPerformanceService;
    }

    @Override
    protected void executeInternal(JobExecutionContext jec){
        facilityPerformanceService.process();
    }
}
