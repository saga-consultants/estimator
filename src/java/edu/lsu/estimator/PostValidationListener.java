/*    */ package edu.lsu.estimator;
/*    */ 
/*    */ import javax.faces.component.UIInput;
/*    */ import javax.faces.component.html.HtmlInputText;
/*    */ import javax.faces.event.AbortProcessingException;
/*    */ import javax.faces.event.ListenerFor;
/*    */ import javax.faces.event.PostValidateEvent;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ListenerFor(sourceClass = HtmlInputText.class, systemEventClass = PostValidateEvent.class)
/*    */ public class PostValidationListener
/*    */   implements SystemEventListener
/*    */ {
/*    */   public boolean isListenerForSource(Object source) {
/* 48 */     return true;
/*    */   }
/*    */   
/*    */   public void processEvent(SystemEvent event) throws AbortProcessingException {
/* 52 */     UIInput source = (UIInput)event.getSource();
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
/* 64 */     if (!source.isValid()) {
/* 65 */       String os = (String)source.getAttributes().get("styleClass");
/*    */       
/* 67 */       if (os != null && !os.isEmpty() && !os.equalsIgnoreCase("null") && !os.equalsIgnoreCase("ui-input-invalid")) {
/* 68 */         source.getAttributes().put("ORIG_STYLE_SET", os);
/* 69 */         source.getAttributes().put("styleClass", os + " ui-input-invalid");
/*    */       } else {
/*    */         
/* 72 */         source.getAttributes().put("styleClass", "ui-input-invalid");
/*    */       } 
/*    */     } else {
/*    */       
/* 76 */       String os = (String)source.getAttributes().get("ORIG_STYLE_SET");
/*    */       
/* 78 */       if (os != null && !os.isEmpty() && !os.equalsIgnoreCase("null")) {
/* 79 */         if (os.equalsIgnoreCase("ui-input-invalid")) {
/* 80 */           source.getAttributes().put("styleClass", "");
/*    */         } else {
/*    */           
/* 83 */           source.getAttributes().put("styleClass", os);
/*    */         }
/*    */       
/*    */       } else {
/*    */         
/* 88 */         source.getAttributes().put("styleClass", "");
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\PostValidationListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */