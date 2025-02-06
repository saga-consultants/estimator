/*    */ package edu.lsu.estimator;
/*    */ 
/*    */ import edu.lsu.estimator.AppReference;
/*    */ import java.util.Comparator;
/*    */ import javax.enterprise.context.ApplicationScoped;
/*    */ import javax.inject.Inject;
/*    */ import javax.inject.Named;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ApplicationScoped
/*    */ @Named("IdComparator")
/*    */ public class IdComparator
/*    */   implements Comparator<String>
/*    */ {
/*    */   @Inject
/*    */   AppReference ref;
/*    */   
/*    */   public int compare(String id1, String id2) {
/* 26 */     int res = 0;
/*    */ 
/*    */     
/* 29 */     id1 = this.ref.isEmp(id1) ? "null" : id1.trim();
/* 30 */     id2 = this.ref.isEmp(id2) ? "null" : id2.trim();
/*    */ 
/*    */     
/* 33 */     if (id1.equals("null") && !id2.equals("null")) {
/* 34 */       res = -1;
/* 35 */     } else if (!id1.equals("null") && id2.equals("null")) {
/* 36 */       res = 1;
/*    */     } else {
/* 38 */       res = id1.compareToIgnoreCase(id2);
/* 39 */       if (res != 0) res = (res > 0) ? 1 : -1; 
/*    */     } 
/* 41 */     return res;
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\IdComparator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */