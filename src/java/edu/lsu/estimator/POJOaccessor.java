/*    */ package edu.lsu.estimator;
/*    */ 
/*    */ import edu.lsu.estimator.Student;
/*    */ import java.io.Serializable;
/*    */ import javax.enterprise.context.Dependent;
/*    */ import javax.inject.Named;
/*    */ import javax.persistence.EntityManager;
/*    */ import javax.persistence.PersistenceContext;
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
/*    */ @Named("pojoaccessor")
/*    */ @Dependent
/*    */ public class POJOaccessor
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   @PersistenceContext
/*    */   private EntityManager em;
/*    */   
/*    */   public void saveStudInfo(Student stud) {
/*    */     try {
/* 30 */       this.em.getTransaction().begin();
/* 31 */       this.em.persist(stud);
/* 32 */       this.em.getTransaction().commit();
/* 33 */     } catch (Exception ex) {
/* 34 */       ex.printStackTrace();
/*    */ 
/*    */ 
/*    */       
/* 38 */       throw ex;
/*    */     } finally {}
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\POJOaccessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */