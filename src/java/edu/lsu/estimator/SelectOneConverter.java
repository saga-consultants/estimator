/*    */ package edu.lsu.estimator;
/*    */ 
/*    */ import javax.faces.component.UIComponent;
/*    */ import javax.faces.context.FacesContext;
/*    */ import javax.faces.convert.Converter;
/*    */ import javax.faces.convert.FacesConverter;
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
/*    */ @FacesConverter("lasu.selectone.converter")
/*    */ public class SelectOneConverter
/*    */   implements Converter
/*    */ {
/*    */   public Object getAsObject(FacesContext context, UIComponent component, String value) {
/*    */     try {
/* 26 */       return Integer.valueOf(value);
/* 27 */     } catch (Exception ex) {
/* 28 */       ex.printStackTrace();
/* 29 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAsString(FacesContext context, UIComponent component, Object value) {
/* 36 */     return ((Integer)value).toString();
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\SelectOneConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */