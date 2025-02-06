/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.cron;

import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 *
 * @author kwang
 */
public class TopScheduler {
    Logger log = LoggerFactory.getLogger();//(SimpleExample.class);
    
    public TopScheduler(){}
    
    public static void main(String[] args){
        TopScheduler top = new TopScheduler();
        try{
            top.runonce();
        }catch(Exception e){
            e.printStackTrace();
        }
        top.ever();
    }
    
    private void ever(){
        try {
            // Grab the Scheduler instance from the Factory 
            log.info("@@@@@@@@@@@@@@@@@@@ get scheduler @@@@@@@@@@@@@@@@@@@@@@@@@@");
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // and start it off
            log.info("========= start scheduler ===========");
            scheduler.start();
            
            
            // define the job and tie it to our HelloJob class
            log.info("========= define job based on HelloJob class ===========");
            JobDetail job = newJob(HelloJob.class)
                .withIdentity("job1", "group1")
                .build();

            
            // Trigger the job to run now, and then repeat every 40 seconds
            log.info("========= define trigger, start now and repeat every 2 seconds for ever ===========");
            Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(2)
                        .repeatForever())            
                .build();
 
            // Tell quartz to schedule the job using our trigger
            log.info("========= scheduler ties the trigger with the job ===========");
            scheduler.scheduleJob(job, trigger);
            
            log.info("========= scheduler will sleep for 10 seonds =========zzzzzzzzzzzzzzzzzzzzzzz");
            Thread.currentThread().sleep(2*1000*5L);
            
            
            //scheduler.getTrigger(trigger.getKey());
            log.info("========= scheduler will pause the job trigger ==========="); 
        ////    scheduler.pauseJob(job.getKey()); //######### the jobs accumulated will exec later at first resume time once
            scheduler.unscheduleJob(trigger.getKey());
            scheduler.pauseTrigger(trigger.getKey());
            
            log.info("========= scheduler will sleep for 6 seonds =========zzzzzzzzzzzzz");
            Thread.currentThread().sleep(2*1000*3L);
            
            log.info("========= scheduler will resume the job trigger ========");
        ////    scheduler.resumeJob(job.getKey());
            scheduler.scheduleJob(job, trigger);
      ///       scheduler.rescheduleJob(trigger.getKey(), trigger);
            scheduler.resumeTrigger(trigger.getKey());
       ///     scheduler.triggerJob(job.getKey());
            
            
            log.info("========= scheduler will sleep for 10 seonds =========zzzzzzzzzzzzzzzzzzzzzzz");
            Thread.currentThread().sleep(2*1000*5L);
            
            log.info("========= scheduler will standby and sleep for 10 seonds =========zzzzzzzzzzzzzz");
            scheduler.standby();
            Thread.currentThread().sleep(2*1000*5L);
            
             log.info("========= scheduler wakes up ? =================");
            /*
            scheduler.getCurrentlyExecutingJobs();
          scheduler.interrupt(job.getKey());
            scheduler.isInStandbyMode();
            scheduler.isStarted();
                        
            scheduler.standby();
            scheduler.unscheduleJob(trigger.getKey());
            scheduler.triggerJob(null);
            */
            
            //your application will not terminate until you call scheduler.shutdown(), because there will be active threads.
            
            log.info("=========  scheduler shutdown ===========");
            scheduler.shutdown();
            
            //If you have not set up logging, all logs will be sent to the console 
        } catch (SchedulerException se) {
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void runonce()  throws Exception {        
        log.info("------- Initializing ----------------------");

        // First we must get a reference to a scheduler
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        log.info("------- Initialization Complete -----------");

        // computer a time that is on the next round minute
        Date runTime = new Date();//;evenMinuteDate(new Date());

        log.info("------- Scheduling Job  -------------------");

        // define the job and tie it to our HelloJob class
        JobDetail job = newJob(HelloJob.class)
            .withIdentity("job1", "group1")
            .build();
        
        // Trigger the job to run on the next round minute
        Trigger trigger = newTrigger()
            .withIdentity("trigger1", "group1")
            .startAt(runTime)
            .build();
        
        // Tell quartz to schedule the job using our trigger
        sched.scheduleJob(job, trigger);
        log.info(job.getKey() + " will run at: " + runTime);  

        // Start up the scheduler (nothing can actually run until the  scheduler has been started)
        sched.start();

        log.info("------- Started Scheduler -----------------");

        // wait long enough so that the scheduler as an opportunity to 
        // run the job!
        log.info("------- Waiting 5 seconds... -------------");
        try {
            // wait 65 seconds to show job
            Thread.currentThread().sleep(5 * 1000L); 
            // executing...
        } catch (Exception e) {
            e.printStackTrace();
        }

        // shut down the scheduler
        log.info("------- woke up and Shutting Down ---------------------");
      sched.shutdown(true);
        log.info("------- Shutdown Complete -----------------");
    }

}

/*
 a basic quartz.properties looks something like this: (place the quartz.properties file in the WEB-INF/classes folder of a web app )

org.quartz.scheduler.instanceName = MyScheduler
org.quartz.threadPool.threadCount = 3
org.quartz.jobStore.class = org.quartz.simpl.RAMJobStore
org.quartz.scheduler.skipUpdateCheck = true
 */