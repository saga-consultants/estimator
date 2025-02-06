/*     */ package edu.lsu.estimator.cron;
/*     */ 
/*     */ import com.kingombo.slf5j.Logger;
/*     */ import com.kingombo.slf5j.LoggerFactory;

import edu.lsu.estimator.AppReference;
import edu.lsu.estimator.SyncWorker;

/*    
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
/*     */ import org.quartz.DisallowConcurrentExecution;
/*     */ import org.quartz.Job;
/*     */ import org.quartz.JobDataMap;
/*     */ import org.quartz.JobExecutionContext;
/*     */ import org.quartz.JobExecutionException;
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
/*     */ @DisallowConcurrentExecution
/*     */ public class JobSyncEJB3
/*     */   implements Job
/*     */ {
/*     */   public static final String EJB_JNDI_NAME_KEY = "java:app/SyncService";
/*     */   public static final String EJB_METHOD_KEY = "sync";
/*     */   public static final String EJB_ARG_TYPES_KEY = "argTypes";
/*     */   public static final String EJB_ARGS_KEY = "args";
/*     */   public static final String INITIAL_CONTEXT_FACTORY = "java.naming.factory.initial";
/*     */   public static final String PROVIDER_URL = "java.naming.provider.url";
/*     */   public static final String PRINCIPAL = "java.naming.security.principal";
/*     */   public static final String CREDENTIALS = "java.naming.security.credentials";
/*  40 */   private InitialContext initialContext = null;
/*     */   
/*  42 */   private final Logger log = LoggerFactory.getLogger();
/*     */ 
/*     */   
/*     */   SyncWorker sync;
/*     */ 
/*     */   
/*     */   AppReference ref;
/*     */ 
/*     */   
/*     */   public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
/*     */     try {
/*  53 */       Thread.currentThread(); Thread.sleep(2000L);
/*  54 */     } catch (InterruptedException interruptedException) {}
/*     */ 
/*     */ 
/*     */     
/*  58 */     this.sync = (SyncWorker)jobExecutionContext.getJobDetail().getJobDataMap().get("sync");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     if (this.sync != null) {
/*  64 */       this.log.info("vvvvvvvvvv starting sync job vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
/*  65 */       this.sync.sync();
/*  66 */       String msg = this.sync.getShowSyncMsg();
/*  67 */       if (msg == null || msg.isEmpty() || msg.startsWith("sync is done")) {
/*  68 */         this.log.info("^^^^^^^^^^^^^^^^^^^^^^^^ sync job is done ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
/*     */       } else {
/*  70 */         this.log.info("^^^^^^^^^^^^^^^^^^^^^^^^ sync job failed: %s", new Object[] { msg });
/*     */       } 
/*     */     } else {
/*  73 */       this.log.info("xxxxxxxxx can not start sync job since SYNC obj is null");
/*     */     } 
/*     */   }
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
/*     */   private <T> T locateEjb(JobDataMap dataMap, String methodName, Object[] arguments) throws JobExecutionException {
/* 157 */     String ejbJNDIName = "java:app/SyncService";
/* 158 */     Object object = null;
/*     */     try {
/* 160 */       this.initialContext = getInitialContext(dataMap);
/* 161 */       object = this.initialContext.lookup(ejbJNDIName);
/* 162 */       if (object == null) {
/* 163 */         throw new JobExecutionException("Cannot find " + ejbJNDIName);
/*     */       }
/* 165 */     } catch (NamingException e) {
/* 166 */       throw new JobExecutionException(e);
/*     */     } 
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
/* 183 */     return (T)object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InitialContext getInitialContext(JobDataMap jobDataMap) throws NamingException {
/* 191 */     Hashtable<String, String> params = new Hashtable<>();
/*     */     
/* 193 */     String initialContextFactory = jobDataMap.getString("java.naming.factory.initial");
/*     */     
/* 195 */     if (initialContextFactory != null) {
/* 196 */       params.put("java.naming.factory.initial", initialContextFactory);
/*     */     }
/*     */     
/* 199 */     String providerUrl = jobDataMap.getString("java.naming.provider.url");
/*     */     
/* 201 */     if (providerUrl != null) {
/* 202 */       params.put("java.naming.provider.url", providerUrl);
/*     */     }
/*     */     
/* 205 */     String principal = jobDataMap.getString("java.naming.security.principal");
/*     */     
/* 207 */     if (principal != null) {
/* 208 */       params.put("java.naming.security.principal", principal);
/*     */     }
/*     */     
/* 211 */     String credentials = jobDataMap.getString("java.naming.security.credentials");
/*     */     
/* 213 */     if (credentials != null) {
/* 214 */       params.put("java.naming.security.credentials", credentials);
/*     */     }
/*     */     
/* 217 */     return (params.size() == 0) ? new InitialContext() : new InitialContext(params);
/*     */   }
/*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\cron\JobSyncEJB3.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */