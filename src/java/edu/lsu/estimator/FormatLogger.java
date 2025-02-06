/*    */ package edu.lsu.estimator;
/*    */ 
/*    */ import ch.qos.logback.classic.Level;
/*    */ import com.kingombo.slf5j.Logger;
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FormatLogger
/*    */   implements Serializable
/*    */ {
/*    */   private Logger log;
/*    */   private String srcClass;
/*    */   
/*    */   public FormatLogger(Logger log) {
/* 32 */     this.log = log;
/*    */   }
/*    */   public FormatLogger(Logger log, String ClassName) {
/* 35 */     this.log = log;
/* 36 */     this.srcClass = ClassName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void debug(String formatter, Object... args) {
/* 41 */     log(Level.DEBUG, formatter, args);
/*    */   }
/*    */ 
/*    */   
/*    */   public void info(String formatter, Object... args) {
/* 46 */     log(Level.INFO, formatter, args);
/*    */   }
/*    */   public void warn(String formatter, Object... args) {
/* 49 */     log(Level.WARN, formatter, args);
/*    */   }
/*    */   public void err(String formatter, Object... args) {
/* 52 */     log(Level.ERROR, formatter, args);
/*    */   }
/*    */   
/*    */   public void log(Level level, String formatter, Object... args) {}
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\FormatLogger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */