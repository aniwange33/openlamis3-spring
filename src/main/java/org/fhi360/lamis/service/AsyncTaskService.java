/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service;

/**
 *
 * @author user1
 */

import org.fhi360.lamis.utility.PropertyAccessor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class AsyncTaskService {
    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);

    public void doPerformanceAnalyzer() throws InterruptedException, ExecutionException {
//        PerformanceAnalyzer task1 = new PerformanceAnalyzer();
//        Future future = threadpool.submit(task1);
//        System.out.println("Performance analyser task is submitted");
    }

    public void doFingerPrintService() throws InterruptedException, ExecutionException {
//        FingerPrintService task2 = new FingerPrintService();
//        Future future = threadpool.submit(task2);
//        System.out.println("Finger print task is submitted");
    }

    private static class PerformanceAnalyzer implements Callable {
        @Override
        public String call() {

            Map<String, Object> map = new PropertyAccessor().getSystemProperties();
            String appInstance = (String) map.get("appInstance");
            if(appInstance.equalsIgnoreCase("local")) {
                // if(new PropertyAccessor().getDateLastAsyncTask() == 0) new FacilityPerformanceAnalyzer().analyze();
            }
            return "Done";
        }
    }

    private static class FingerPrintService implements Callable {
        @Override
        public String call() {
            //new Enrollment().enroll();
            return "Done";
        }
    }

}
