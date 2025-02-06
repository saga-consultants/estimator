/*    */ package edu.lsu.estimator;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import javax.enterprise.context.SessionScoped;
/*    */ import javax.inject.Named;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Named
/*    */ @SessionScoped
/*    */ public class LayoutBean
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 20 */   private String theme = "aristo";
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTheme() {
/* 25 */     return this.theme;
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\LayoutBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */