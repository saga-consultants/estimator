/*    */ package edu.lsu.estimator.cron;
/*    */ 
/*    */ import com.kingombo.slf5j.Logger;
/*    */ import com.kingombo.slf5j.LoggerFactory;

import edu.lsu.estimator.AppReference;
/*    */
/*    */ import javax.persistence.EntityManager;
/*    */ import javax.persistence.PersistenceContext;
/*    */ import org.quartz.DisallowConcurrentExecution;
/*    */ import org.quartz.Job;
/*    */ import org.quartz.JobExecutionContext;
/*    */ import org.quartz.JobExecutionException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @DisallowConcurrentExecution
/*    */ public class JobChkLdap
/*    */   implements Job
/*    */ {
/* 27 */   private static Logger log = LoggerFactory.getLogger();
/*    */   
/*    */   AppReference ref;
/*    */   
/*    */   @PersistenceContext
/*    */   private EntityManager em;
/* 33 */   private int ldapUp = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(JobExecutionContext jec) throws JobExecutionException {
/* 51 */     this.ref = (AppReference)jec.getJobDetail().getJobDataMap().get("ref");
/* 52 */     pingLdap();
/* 53 */     this.ref.setLdap_up_ind(this.ldapUp);
/*    */   }
/*    */ 
/*    */   
/*    */   private void pingLdap() {
    
    log.info("LDAP sss "+this.ref.getSeed().getLdapport() );
/* 58 */     this.ldapUp = this.ref.reachHostBySocket(this.ref.getSeed().getLdapserver(), this.ref.getSeed().getLdapport().intValue()) ? 1 : 0;
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\cron\JobChkLdap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */