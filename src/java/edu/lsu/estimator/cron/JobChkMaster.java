/*     */ package edu.lsu.estimator.cron;
/*     */ 
/*     */ import com.caucho.hessian.client.HessianProxyFactory;
/*     */ import com.kingombo.slf5j.Logger;
/*     */ import com.kingombo.slf5j.LoggerFactory;

import edu.lsu.estimator.AppReference;
import edu.lsu.estimator.tdo.SPIEstimatorMaster;

/*     */ 
/*     */ import javax.crypto.KeyGenerator;
/*     */ import javax.crypto.SecretKey;
/*     */ import org.quartz.DisallowConcurrentExecution;
/*     */ import org.quartz.Job;
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
/*     */ public class JobChkMaster
/*     */   implements Job
/*     */ {
/*  41 */   private static Logger log = LoggerFactory.getLogger();
/*     */   
/*  43 */   private SecretKey aesKey = null;
/*  44 */   private SPIEstimatorMaster hook = null;
/*  45 */   private int masterUp = 0;
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
/*     */   AppReference ref;
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
/*     */   public void execute(JobExecutionContext jec) throws JobExecutionException {
/*  86 */     this.ref = (AppReference)jec.getJobDetail().getJobDataMap().get("ref");
/*     */ 
/*     */ 
/*     */     
/*  90 */     pingMaster();
/*  91 */     hookMaster();
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
/*     */   private void pingMaster() {
/* 108 */     this.masterUp = this.ref.reachHostBySocket(this.ref.getSeed().getMastername(), Integer.parseInt(this.ref.getSeed().getMasterport())) ? 1 : 0;
/* 109 */     this.ref.setMaster_up_ind(this.masterUp);
/*     */   }
/*     */   
/*     */   private void hookMaster() {
/* 113 */     if (this.masterUp <= 0) {
/* 114 */       this.hook = null;
/* 115 */       log.error("~~~~~ hookMaster() got master up ind=%d. quit.", new Object[] { Integer.valueOf(this.masterUp) });
/*     */       
/*     */       return;
/*     */     } 
/* 119 */     String url = this.ref.getSeed().getMasterurl();
/* 120 */     HessianProxyFactory factory = new HessianProxyFactory();
/*     */     try {
/* 122 */       this.hook = (SPIEstimatorMaster)factory.create(SPIEstimatorMaster.class, url);
/* 123 */     } catch (Exception e) {
/* 124 */       this.hook = null;
/* 125 */       this.ref.setMaster_up_ind(0);
/*     */       
/* 127 */       e.printStackTrace();
/*     */     } 
/* 129 */     if (this.aesKey == null)
/*     */       try {
/* 131 */         KeyGenerator keygen = KeyGenerator.getInstance("AES");
/* 132 */         this.aesKey = keygen.generateKey();
/* 133 */       } catch (Exception e) {
/* 134 */         this.aesKey = null;
/* 135 */         e.printStackTrace();
/*     */       }  
/*     */   }
/*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\cron\JobChkMaster.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */