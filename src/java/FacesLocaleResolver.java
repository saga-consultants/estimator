
/*    */ 
/*    */ import java.util.Locale;
/*    */ import javax.enterprise.inject.Produces;
/*    */ import javax.faces.context.FacesContext;
/*    */ import javax.inject.Inject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FacesLocaleResolver
/*    */ {
/*    */   @Inject
/*    */   FacesContext facesContext;
/*    */   
/*    */   public boolean isActive() {
/* 22 */     return (this.facesContext != null && this.facesContext.getCurrentPhaseId() != null);
/*    */   }
/*    */   
/*    */   @Produces
/*    */   public Locale getLocale() {
/* 27 */     if (this.facesContext.getViewRoot() != null) {
/* 28 */       return this.facesContext.getViewRoot().getLocale();
/*    */     }
/* 30 */     return this.facesContext.getApplication().getViewHandler().calculateLocale(this.facesContext);
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator_latest\Estimator2\estimator1920.war!\WEB-INF\classes\FacesLocaleResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */