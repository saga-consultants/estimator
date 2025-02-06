/*    */ package edu.lsu.estimator;
/*    */ 
/*    */ import edu.lsu.estimator.FormatLogger;
/*    */ import java.io.Serializable;
/*    */ import javax.enterprise.context.ContextNotActiveException;
/*    */ import javax.enterprise.context.RequestScoped;
/*    */ import javax.enterprise.inject.Produces;
/*    */ import javax.enterprise.inject.spi.InjectionPoint;
/*    */ import javax.faces.context.FacesContext;
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
/*    */ public class utilFactory
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   @Produces
/*    */   FormatLogger createLogger(InjectionPoint injectionPoint) {
/* 36 */     return null;
/*    */   }
/*    */   @Produces
/*    */   @RequestScoped
/*    */   public FacesContext getFacesContext() {
/* 41 */     FacesContext ctx = FacesContext.getCurrentInstance();
/* 42 */     if (ctx == null)
/* 43 */       throw new ContextNotActiveException("FacesContext is not active"); 
/* 44 */     return ctx;
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimato\\utilFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */