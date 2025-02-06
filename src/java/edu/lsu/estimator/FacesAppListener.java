/*    */ package edu.lsu.estimator;
/*    */ 
/*    */ import javax.faces.event.AbortProcessingException;
/*    */ import javax.faces.event.SystemEvent;
/*    */ import javax.faces.event.SystemEventListener;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FacesAppListener
/*    */   implements SystemEventListener
/*    */ {
/*    */   public void processEvent(SystemEvent event) throws AbortProcessingException {
/* 19 */     if (event instanceof javax.faces.event.PostConstructApplicationEvent) {
/* 20 */       System.out.println("PostConstructApplicationEvent is Called");
/*    */     }
/*    */     
/* 23 */     if (event instanceof javax.faces.event.PreDestroyApplicationEvent) {
/* 24 */       System.out.println("PreDestroyApplicationEvent is Called");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isListenerForSource(Object source) {
/* 32 */     return source instanceof javax.faces.application.Application;
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\FacesAppListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */