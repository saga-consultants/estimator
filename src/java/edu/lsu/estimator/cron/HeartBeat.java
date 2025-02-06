/*     */ package edu.lsu.estimator.cron;

/*     */
 /*     */ import com.kingombo.slf5j.Logger;
/*     */ import com.kingombo.slf5j.LoggerFactory;
/*     */ import edu.lsu.estimator.AppReference;
/*     */ import edu.lsu.estimator.Config;
import edu.lsu.estimator.SyncWorker;
/*     */ import edu.lsu.estimator.cron.JobChkLdap;
/*     */ import edu.lsu.estimator.cron.JobChkMaster;
/*     */ import edu.lsu.estimator.cron.JobSyncEJB3;
/*     */ import java.io.Serializable;
/*     */ import javax.annotation.PreDestroy;
/*     */ import javax.ejb.Singleton;
/*     */ import javax.ejb.Stateful;
/*     */ import javax.inject.Named;
/*     */ import org.quartz.JobBuilder;
/*     */ import org.quartz.JobDataMap;
/*     */ import org.quartz.JobDetail;
/*     */ import org.quartz.JobKey;
/*     */ import org.quartz.ScheduleBuilder;
/*     */ import org.quartz.Scheduler;
/*     */ import org.quartz.SchedulerException;
/*     */ import org.quartz.SimpleScheduleBuilder;
/*     */ import org.quartz.Trigger;
/*     */ import org.quartz.TriggerBuilder;
/*     */ import org.quartz.impl.StdSchedulerFactory;

/*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */ @Stateful
/*     */ @Named("ping")
/*     */ @Singleton
/*     */ public class HeartBeat
        /*     */ implements Serializable /*     */ {

    /*     */ private static final long serialVersionUID = 1L;
    /*  66 */    private Logger log = LoggerFactory.getLogger();
    /*     */
 /*     */    AppReference o_ref;
    /*     */
 /*     */    SyncWorker o_sync;
    /*     */
 /*     */    private int clientcfg_flag;
    /*     */
 /*     */    private int clientinit_flag;
    /*     */
 /*     */    private int master_on_flag;
    /*     */
 /*     */    private int ldap_on_flag;
    /*     */
 /*     */    private int ldap_local_flag;
    /*     */
 /*     */    private int clientver_flag;
    /*     */
 /*     */    private int clientver_block_flag;
    /*     */
 /*     */    private int clienttime_diff;
    /*     */
 /*     */    private int clienttime_block_flag;
    /*     */
 /*     */    private int clientable_flag;
    /*     */
 /*     */    private int clientchg_flag;
    /*     */
 /*     */    private int masterver_flag;
    /*     */
 /*     */    private int masterfscy_flag;
    /*     */
 /*     */    private int mastercnt_last;
    /*     */
 /*     */    private int masterchg_flag;
    /*     */
 /*     */    private int beat_interval;
    /* 103 */    Scheduler sched = null;
    /* 104 */    private Config seed = null;

    /*     */
 /*     */
 /*     */ public int loadConfig(Config seed) {
        /* 108 */ int res = 1;
        /* 109 */ this.beat_interval = seed.getPingInterval();
        /*     */
 /*     */
 /* 112 */ return res;
        /*     */    }

    /*     */
 /*     */
 /*     */ public int reloadConfig(Config seed) {
        /* 117 */ int res = 1;
        /* 118 */ this.beat_interval = seed.getPingInterval();
        /* 119 */ return res;
        /*     */    }

    /*     */
 /*     */
 /*     */
 /*     */
 /*     */ public int schedule(AppReference ref, SyncWorker sync) {
        /* 126 */ this.log.info("------- schedule() is Initializing ----------------------");
        /*     */
 /* 128 */ StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        /*     */
 /*     */ try {
            /* 131 */ if (this.sched == null) {
                this.sched = stdSchedulerFactory.getScheduler();
            }
            /* 132 */        } catch (SchedulerException ex) {
            /* 133 */ this.log.info("", (Throwable) ex);
            /*     */
 /* 135 */ return 1;
            /*     */        }
        /* 137 */ this.log.info("------- schedule() Initialization Complete -----------");
        /* 138 */ this.log.info("------- schedule() is setting job parameter in context object -------------------");
        /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /* 146 */ JobDataMap jobDataMap = new JobDataMap();
        /* 147 */ jobDataMap.put("ref", ref);
        /* 148 */ jobDataMap.put("sync", sync);
        /*     */
 /* 150 */ this.log.info("------- schedule() is creating Job -------------------");
        /*     */
 /* 152 */ JobKey masterKey = new JobKey("master", "group1");
        /*     */
 /*     */
 /*     */
 /* 156 */ JobDetail jobmaster = JobBuilder.newJob(JobChkMaster.class).withIdentity(masterKey).usingJobData(jobDataMap).build();
        /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /* 168 */ JobDetail jobldap = JobBuilder.newJob(JobChkLdap.class).withIdentity("ldap", "group2").usingJobData(jobDataMap).build();
        /*     */
 /* 170 */ JobKey syncKey = new JobKey("sync", "group3");
        /*     */
 /*     */
 /*     */
 /* 174 */ JobDetail jobsync = JobBuilder.newJob(JobSyncEJB3.class).withIdentity(syncKey).usingJobData(jobDataMap).build();
        /*     */
 /*     */
 /* 177 */ this.log.info("------- schedule() is creating Trigger -------------------");
        /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /* 185 */ Trigger triggermaster = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startNow().withSchedule((ScheduleBuilder) SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(this.beat_interval * 60).repeatForever()).build();
        /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /* 197 */ Trigger triggerldap = TriggerBuilder.newTrigger().withIdentity("trigger2", "group2").startNow().withSchedule((ScheduleBuilder) SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(this.beat_interval * 60).repeatForever()).build();
        /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /* 205 */ Trigger triggersync = TriggerBuilder.newTrigger().withIdentity("trigger3", "group3").startNow().withSchedule((ScheduleBuilder) SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(this.beat_interval * 60).repeatForever()).build();
        /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /* 214 */ this.log.info("------- schedule() Scheduling Job  -------------------");
        /*     */
 /*     */ try {
            /* 217 */ this.sched.scheduleJob(jobmaster, triggermaster);
            /* 218 */ this.sched.scheduleJob(jobldap, triggerldap);
            /* 219 */ this.sched.scheduleJob(jobsync, triggersync);
            /* 220 */        } catch (SchedulerException ex) {
            /* 221 */ this.log.info("", (Throwable) ex);
            /* 222 */ return 2;
            /*     */        }
        /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /* 233 */ this.log.info("------- schedule() Scheduled Job  -------------------");
        /*     */
 /*     */ try {
            /* 236 */ this.sched.start();
            /* 237 */        } catch (SchedulerException ex) {
            /* 238 */ this.log.info("", (Throwable) ex);
            /* 239 */ return 3;
            /*     */        }
        /* 241 */ this.log.info("------- schedule() Scheduler Started -----------------");
        /* 242 */ return 0;
        /*     */    }

    /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */ @PreDestroy
    /*     */ public void unschedule() {
        /*     */ try {
            /* 267 */ if (this.sched != null && (!this.sched.isShutdown() || this.sched.isStarted())) {
                /* 268 */ this.sched.shutdown(true);
                /* 269 */ this.log.info("------- unschedule() Shutdown scheduler -----------------");
                /*     */            } else {
                /* 271 */ this.log.info("------- unschedule() fonud no valid scheduler  ---------------------");
                /*     */            }
            /* 273 */        } catch (SchedulerException ex) {
            /* 274 */ this.log.info(" unschedule() exception ", (Throwable) ex);
            /*     */        }
        /*     */    }

    /*     */
 /*     */ public Config getSeed() {
        /* 279 */ return this.seed;
        /*     */    }

    /*     */
 /*     */ public void setSeed(Config seed) {
        /* 283 */ this.seed = seed;
        /*     */    }
    /*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\cron\HeartBeat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
