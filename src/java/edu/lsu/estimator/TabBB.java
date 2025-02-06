/*    */ package edu.lsu.estimator;
/*    */ 
/*    */ import javax.faces.bean.ManagedBean;
/*    */ import javax.faces.bean.SessionScoped;
/*    */ import javax.faces.context.ExternalContext;
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
/*    */ @ManagedBean
/*    */ @SessionScoped
/*    */ public class TabBB
/*    */ {
/* 20 */   private int tabIndex = 1;
/*    */   
/*    */   public boolean handleTabChange() {
/* 23 */     ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
/* 24 */     String index = (String)externalContext.getRequestParameterMap().get("tabIndex");
/* 25 */     setTabIndex(Integer.parseInt(index));
/* 26 */     return true;
/*    */   }
/*    */   
/*    */   public int getTabIndex() {
/* 30 */     return this.tabIndex;
/*    */   }
/*    */   public void setTabIndex(int tabIndex) {
/* 33 */     this.tabIndex = tabIndex;
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\TabBB.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */