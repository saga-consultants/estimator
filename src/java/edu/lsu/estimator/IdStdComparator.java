/*    */ package edu.lsu.estimator;
/*    */ 
/*    */ import edu.lsu.estimator.AppReference;
/*    */ import edu.lsu.estimator.Student;
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
/*    */ @ApplicationScoped
/*    */ @Named("IdStdComparator")
/*    */ public class IdStdComparator
/*    */   implements Comparator<Student>
/*    */ {
/*    */   @Inject
/*    */   AppReference ref;
/*    */   
/*    */   public int compare(Student std1, Student std2) {
/* 26 */     int res = 0;
/*    */     
/* 28 */     String id1 = std1.getStudentALsuid();
/* 29 */     id1 = this.ref.isEmp(id1) ? "null" : id1.trim();
/* 30 */     String id2 = std2.getStudentALsuid();
/* 31 */     id2 = this.ref.isEmp(id2) ? "null" : id2.trim();
/*    */     
/* 33 */     res = id1.compareToIgnoreCase(id2);
/* 34 */     if (res == 0) {
/*    */       
/* 36 */       long dom1 = std1.getDdom();
/* 37 */       long doe1 = std1.getDdoe();
/* 38 */       long dom2 = std1.getDdom();
/* 39 */       long doe2 = std1.getDdoe();
/* 40 */       long last1 = (dom1 > 0L) ? dom1 : doe1;
/* 41 */       long last2 = (dom2 > 0L) ? dom2 : doe2;
/* 42 */       res = (int)(last1 - last2);
/* 43 */       if (res != 0) res = (res > 0) ? 1 : -1; 
/*    */     } 
/* 45 */     return res;
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\IdStdComparator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */