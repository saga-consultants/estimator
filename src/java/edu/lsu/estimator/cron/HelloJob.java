/*    */ package edu.lsu.estimator.cron;
/*    */ 
/*    */ import com.kingombo.slf5j.Logger;
/*    */ import com.kingombo.slf5j.LoggerFactory;
/*    */ import java.util.Date;
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
/*    */ 
/*    */ 
/*    */ public class HelloJob
/*    */   implements Job
/*    */ {
/* 25 */   private static Logger _log = LoggerFactory.getLogger(edu.lsu.estimator.cron.HelloJob.class);
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
/* 41 */     _log.info("###### HelloJob exec() says: Hello World! - " + new Date());
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\cron\HelloJob.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */