/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.scheduler;

import org.fhi360.lamis.service.indicator.IncidenceIndicatorService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 *
 * @author user10
 */
@Component
public class IncidenceJobScheduler extends QuartzJobBean {
    private final IncidenceIndicatorService incidenceIndicatorService;

    public IncidenceJobScheduler(IncidenceIndicatorService incidenceIndicatorService) {
        this.incidenceIndicatorService = incidenceIndicatorService;
    }

    @Override
    protected void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        incidenceIndicatorService.process();
    }
}
