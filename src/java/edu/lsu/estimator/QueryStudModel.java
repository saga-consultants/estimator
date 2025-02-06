/*    */ package edu.lsu.estimator;
/*    */ 
/*    */ import edu.lsu.estimator.Student;
/*    */ import java.io.Serializable;
/*    */ import java.util.List;
/*    */ import javax.faces.model.ListDataModel;
/*    */ import org.primefaces.model.SelectableDataModel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class QueryStudModel
/*    */   extends ListDataModel<Student>
/*    */   implements SelectableDataModel<Student>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public QueryStudModel() {}
/*    */   
/*    */   public QueryStudModel(List<Student> list) {
/* 24 */     super(list);
/* 25 */     setWrappedData(list);
/*    */   }
/*    */ 
/*    */   
/*    */   public Student getRowData(String rowKey) {
/* 30 */     List<Student> students = (List<Student>)getWrappedData();
/* 31 */     StringBuilder sb = new StringBuilder(128);
/* 32 */     for (Student student : students) {
/* 33 */       sb.setLength(0);
/* 34 */       sb.append(student.getStudentFisy()).append('.').append(student.getCounselorId()).append('.').append(student.getStudentNumb());
/* 35 */       if (sb.toString().equalsIgnoreCase(rowKey)) {
/* 36 */         return student;
/*    */       }
/*    */     } 
/* 39 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getRowKey(Student stud) {
/* 44 */     StringBuilder sb = new StringBuilder(128);
/* 45 */     return sb.append(stud.getStudentFisy()).append('.').append(stud.getCounselorId()).append('.').append(stud.getStudentNumb()).toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public int isAliveFilled() {
/* 50 */     if (isRowAvailable()) return 1;
/*    */     
/* 52 */     List<Student> students = (List<Student>)getWrappedData();
/* 53 */     if (students == null) return -1; 
/* 54 */     return students.size();
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\QueryStudModel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */