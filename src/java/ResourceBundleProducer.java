
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.ResourceBundle;
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
/*    */ public class ResourceBundleProducer
/*    */ {
/*    */   @Inject
/*    */   public Locale locale;
/*    */   @Inject
/*    */   public FacesContext facesContext;
/*    */   
/*    */   @Produces
/*    */   public ResourceBundle getResourceBundle() {
/* 26 */     return ResourceBundle.getBundle("/JSFmessages", this.facesContext.getViewRoot().getLocale());
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator_latest\Estimator2\estimator1920.war!\WEB-INF\classes\ResourceBundleProducer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */