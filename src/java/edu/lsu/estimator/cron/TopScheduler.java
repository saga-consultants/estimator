/*     */ package edu.lsu.estimator.cron;
/*     */ 
/*     */ import com.kingombo.slf5j.Logger;
/*     */ import com.kingombo.slf5j.LoggerFactory;
/*     */ import edu.lsu.estimator.cron.HelloJob;
/*     */ import java.util.Date;
/*     */ import org.quartz.JobBuilder;
/*     */ import org.quartz.JobDetail;
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
/*     */ public class TopScheduler
/*     */ {
/*  32 */   Logger log = LoggerFactory.getLogger();
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*  37 */     TopScheduler top = new TopScheduler();
/*     */     try {
/*  39 */       top.runonce();
/*  40 */     } catch (Exception e) {
/*  41 */       e.printStackTrace();
/*     */     } 
/*  43 */     top.ever();
/*     */   }
/*     */ 
/*     */   
/*     */   private void ever() {
/*     */     try {
/*  49 */       this.log.info("@@@@@@@@@@@@@@@@@@@ get scheduler @@@@@@@@@@@@@@@@@@@@@@@@@@");
/*  50 */       Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
/*     */ 
/*     */       
/*  53 */       this.log.info("========= start scheduler ===========");
/*  54 */       scheduler.start();
/*     */ 
/*     */ 
/*     */       
/*  58 */       this.log.info("========= define job based on HelloJob class ===========");
/*     */ 
/*     */       
/*  61 */       JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("job1", "group1").build();
/*     */ 
/*     */ 
/*     */       
/*  65 */       this.log.info("========= define trigger, start now and repeat every 2 seconds for ever ===========");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  72 */       Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startNow().withSchedule((ScheduleBuilder)SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever()).build();
/*     */ 
/*     */       
/*  75 */       this.log.info("========= scheduler ties the trigger with the job ===========");
/*  76 */       scheduler.scheduleJob(job, trigger);
/*     */       
/*  78 */       this.log.info("========= scheduler will sleep for 10 seonds =========zzzzzzzzzzzzzzzzzzzzzzz");
/*  79 */       Thread.currentThread(); Thread.sleep(10000L);
/*     */ 
/*     */ 
/*     */       
/*  83 */       this.log.info("========= scheduler will pause the job trigger ===========");
/*     */       
/*  85 */       scheduler.unscheduleJob(trigger.getKey());
/*  86 */       scheduler.pauseTrigger(trigger.getKey());
/*     */       
/*  88 */       this.log.info("========= scheduler will sleep for 6 seonds =========zzzzzzzzzzzzz");
/*  89 */       Thread.currentThread(); Thread.sleep(6000L);
/*     */       
/*  91 */       this.log.info("========= scheduler will resume the job trigger ========");
/*     */       
/*  93 */       scheduler.scheduleJob(job, trigger);
/*     */       
/*  95 */       scheduler.resumeTrigger(trigger.getKey());
/*     */ 
/*     */ 
/*     */       
/*  99 */       this.log.info("========= scheduler will sleep for 10 seonds =========zzzzzzzzzzzzzzzzzzzzzzz");
/* 100 */       Thread.currentThread(); Thread.sleep(10000L);
/*     */       
/* 102 */       this.log.info("========= scheduler will standby and sleep for 10 seonds =========zzzzzzzzzzzzzz");
/* 103 */       scheduler.standby();
/* 104 */       Thread.currentThread(); Thread.sleep(10000L);
/*     */       
/* 106 */       this.log.info("========= scheduler wakes up ? =================");
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
/* 120 */       this.log.info("=========  scheduler shutdown ===========");
/* 121 */       scheduler.shutdown();
/*     */     
/*     */     }
/* 124 */     catch (SchedulerException se) {
/* 125 */       se.printStackTrace();
/* 126 */     } catch (Exception e) {
/* 127 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void runonce() throws Exception {
/* 132 */     this.log.info("------- Initializing ----------------------");
/*     */ 
/*     */     
/* 135 */     StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
/* 136 */     Scheduler sched = stdSchedulerFactory.getScheduler();
/*     */     
/* 138 */     this.log.info("------- Initialization Complete -----------");
/*     */ 
/*     */     
/* 141 */     Date runTime = new Date();
/*     */     
/* 143 */     this.log.info("------- Scheduling Job  -------------------");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 148 */     JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("job1", "group1").build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();
/*     */ 
/*     */     
/* 157 */     sched.scheduleJob(job, trigger);
/* 158 */     this.log.info(job.getKey() + " will run at: " + runTime);
/*     */ 
/*     */     
/* 161 */     sched.start();
/*     */     
/* 163 */     this.log.info("------- Started Scheduler -----------------");
/*     */ 
/*     */ 
/*     */     
/* 167 */     this.log.info("------- Waiting 5 seconds... -------------");
/*     */     
/*     */     try {
/* 170 */       Thread.currentThread(); Thread.sleep(5000L);
/*     */     }
/* 172 */     catch (Exception e) {
/* 173 */       e.printStackTrace();
/*     */     } 
/*     */ 
/*     */     
/* 177 */     this.log.info("------- woke up and Shutting Down ---------------------");
/* 178 */     sched.shutdown(true);
/* 179 */     this.log.info("------- Shutdown Complete -----------------");
/*     */   }
/*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\cron\TopScheduler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */