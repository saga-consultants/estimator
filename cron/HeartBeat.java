/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.cron;

import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;
import edu.lsu.estimator.AppReference;
import edu.lsu.estimator.Config;
import edu.lsu.estimator.tdo.SyncWorker;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.Serializable;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
/**
 *
 * @author kwang
 * uses  scheduled program to to routine check:
 * > network availability
 * >> client configuration info availability (if no, means it is not initialized from master yet)
 * > LDAP server availability
 * > MASTER server availability
 * > MASTER time diff comp
 * > MASTER version check
 * 
 * --if logged in
 * > MASTER counselor comp
 * > MASTER data comp
 */
@Stateful
@Named("ping")  //java.lang.RuntimeException: Uncompilable source code - javax.inject.Named is not an annotation type ????????????????????????????
//@ApplicationScoped //Uncompilable source code - Erroneous sym type: edu.lsu.estimator.cron.HeartBeat.schedule
@Singleton
public class HeartBeat implements Serializable {
    private static final long serialVersionUID = 1L;
        
    private Logger log = LoggerFactory.getLogger();      
    
    //@Inject 
    AppReference o_ref;
    //@Inject 
    SyncWorker o_sync;
    
    private int clientcfg_flag;  // 1: ldap and master cfg are OK  0: wrong cfg
    private int clientinit_flag;  //1: no clientID or ID=C000, needs to be initialized, 0: done
    
//    private int net_flag;
    private int master_on_flag;  //master availability
    private int ldap_on_flag;    //ladp server availability
    private int ldap_local_flag; //if local has ldap's auth last suceed track
    
    private int clientver_flag;  //keep version string: a.b.c.x.y.z
    private int clientver_block_flag;
    
    private int clienttime_diff; //keep client's second diff from master
    private int clienttime_block_flag; //keep client's lock ind based on time diff comparing
    
    private int clientable_flag; //keep master config if the client is active or not
    private int clientchg_flag;  //keep client side new or mod or print student record
    
    private int masterver_flag; //holder for fought masrter side versions: a.b.c.x.x.x
    private int masterfscy_flag; //holder for   masrter side  fscy
    
    private int mastercnt_last; //holder for master's data record count number, so for next time comp
    private int masterchg_flag; //holder for master's data record change ind, based on count comparing
        
    private int beat_interval; //frequency in minutes?
    
    /******** shall be in another program for login controlling *************
    private int failure_times_lock; //max continue failures to sign in as one user
    private int failures_lock_col; //once locked, how long to unlock (able to try login again)
    //show last login (success or not)?
    */
    Scheduler sched = null;
    private Config seed = null;
    
//    @PostConstruct //this one needs: (void return type, and empty/zero parameter)
    public int loadConfig(Config seed){
        int res=1;
        beat_interval = seed.getPingInterval();
        
        //Query sql = em.createNativeQuery("select * from config");        
        return res;
    }    
    
    
    public int reloadConfig(Config seed){ //log.info("~~~~~============########### exec() check ref==NULL ? %s", ref==null);
        int res=1;
        beat_interval = seed.getPingInterval();
        return res;
    }
    
    public int schedule(AppReference ref, SyncWorker sync){//AppReference ref  throws Exception {
       // this.seed = seed;
        //if( sched!=null) return 0; //ref iniseed() called not the 1st time, no need to reschedule the same jobs
        
        log.info("------- schedule() is Initializing ----------------------");
        //get a reference to a scheduler
        SchedulerFactory sf = new StdSchedulerFactory();
        
        try {
            if( sched==null) sched = sf.getScheduler();
        } catch (SchedulerException ex) {
            log.info("", ex);            
            //throw ex;
            return 1;
        }
        log.info("------- schedule() Initialization Complete -----------");        
        log.info("------- schedule() is setting job parameter in context object -------------------");

        // define the job and tie it to our HelloJob class
        /*
        JobDetail jobmaster = newJob(JobChkMaster.class)
            .withIdentity("master", "group1")
            .build();
        */        
        JobDataMap jobDataMap = new JobDataMap();  
        jobDataMap.put("ref", ref);  
        jobDataMap.put("sync", sync);
        
        log.info("------- schedule() is creating Job -------------------");
        
        JobKey masterKey = new JobKey("master", "group1");       
        JobDetail jobmaster = JobBuilder.newJob(edu.lsu.estimator.cron.JobChkMaster.class)
            .withIdentity(masterKey)
            .usingJobData(jobDataMap)
            .build(); 
        //Map masterMap = jobmaster.getJobDataMap();
    	//masterMap.put("ref", ref);
        
        /*
        JobDetail jobldap = newJob(JobChkLdap.class)
            .withIdentity("ldap", "group2")
            .build();
        */        
        JobDetail jobldap = JobBuilder.newJob(edu.lsu.estimator.cron.JobChkLdap.class)
            .withIdentity("ldap", "group2")
            .usingJobData(jobDataMap)
            .build(); 
        
        JobKey syncKey = new JobKey("sync", "group3"); 
        JobDetail jobsync = JobBuilder.newJob(edu.lsu.estimator.cron.JobSyncEJB3.class)
            .withIdentity(syncKey)//.withIdentity("sync", "group3")
            .usingJobData(jobDataMap) //jobDataMap2
            .build();         
                
        
        log.info("------- schedule() is creating Trigger -------------------");
        
        Trigger triggermaster = newTrigger()
            .withIdentity("trigger1", "group1")
            .startNow()
            .withSchedule(simpleSchedule()
                    .withIntervalInSeconds(beat_interval * 60)
                    .repeatForever())            
            .build();
        
        
        // no method like: trigger1.setJobDataMap(jobData1);
        //triggermaster.getJobDataMap().put(dataMap, dataMap);  this will overwrite job map if use jobContext.getMergedJobDataMap()
        
        Trigger triggerldap = newTrigger()
            .withIdentity("trigger2", "group2")
            .startNow()
            .withSchedule(simpleSchedule()
                    .withIntervalInSeconds(beat_interval * 60)
                    .repeatForever())            
            .build();
        
        Trigger triggersync = newTrigger()
            .withIdentity("trigger3", "group3")             
            .startNow()
            .withSchedule(simpleSchedule()
                    .withIntervalInSeconds(beat_interval * 60  )
                    .repeatForever())            
            .build();
        
/*      Trigger the job to run on the next round minute  
        Trigger trigger = newTrigger()
            .withIdentity("trigger1", "group1")
            .startAt(runTime)
            .build();
*/        
        
        log.info("------- schedule() Scheduling Job  -------------------");
        try {
            // Tell quartz to schedule the job using our trigger
            sched.scheduleJob(jobmaster, triggermaster);
            sched.scheduleJob(jobldap, triggerldap);
            sched.scheduleJob(jobsync, triggersync);
        } catch (SchedulerException ex) {
            log.info("" , ex);
            return 2;
        }
        
/*
        scheduler.unscheduleJob(trigger.getKey());
        scheduler.pauseTrigger(trigger.getKey());
        scheduler.resumeTrigger(trigger.getKey());
        scheduler.standby();
 */
        
        //log.info(job.getKey() + " will run at: " + runTime);  
        log.info("------- schedule() Scheduled Job  -------------------");
        try {
            // Start up the scheduler (nothing can actually run until the  scheduler has been started)
            sched.start();
        } catch (SchedulerException ex) {
            log.info("", ex);
            return 3;
        }
        log.info("------- schedule() Scheduler Started -----------------");
        return 0;
        
 /*       
        // wait long enough so that the scheduler has an opportunity to   run the job!
        log.info("------- Waiting 5 seconds... -------------");
        try {
            // wait 5 seconds to show job
            Thread.currentThread().sleep(5 * 1000L); 
            // executing...
        } catch (Exception e) {
            e.printStackTrace();
        }

         
        log.info("------- woke up and Shutting Down ---------------------");
        sched.shutdown(true);
        log.info("------- Shutdown Complete -----------------");
 */     
        
    }
    
    @PreDestroy
    public void unschedule() { 
        try {
            //your application will not terminate until you call scheduler.shutdown(), because there will be active threads.
            if( sched!=null && (!sched.isShutdown() || sched.isStarted())){
                sched.shutdown(true);  //false: shutdown without waiting for the executing jobs to finish
                log.info("------- unschedule() Shutdown scheduler -----------------");
            }else{
                log.info("------- unschedule() fonud no valid scheduler  ---------------------");
            }
        } catch (SchedulerException ex) {
            log.info(" unschedule() exception ", ex);
        }
    }

    public Config getSeed() {
        return seed;
    }

    public void setSeed(Config seed) {
        this.seed = seed;
    }
    
    
    
}
 
/*a basic quartz.properties looks something like this: (place the quartz.properties file in the WEB-INF/classes folder of a web app )

org.quartz.scheduler.instanceName = MyScheduler
org.quartz.threadPool.threadCount = 3
org.quartz.jobStore.class = org.quartz.simpl.RAMJobStore
org.quartz.scheduler.skipUpdateCheck = true
 */
 