/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.scheduler;

import org.fhi360.lamis.service.ExcelConverterService;
import org.fhi360.lamis.utility.PropertyAccessor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author BRAINERGY SOLUTIONS
 */
@Component
@Profile("server")
public class ExportJobScheduler extends QuartzJobBean {
    private ExcelConverterService excelConverterService = new ExcelConverterService();

    @Override
    protected void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        excelConverterService.startConversion();
    }
}
