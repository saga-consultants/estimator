/*    */ package edu.lsu.estimator;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
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
/*    */ public class SplitReplaceTester
/*    */ {
/*    */   public static void main(String[] args) {}
/*    */   
/*    */   private void test() {
/* 23 */     String str = "";
/* 24 */     List<String> std_merits = null;
/*    */     
/* 26 */     String x = str;
/* 27 */     if (x != null && !x.isEmpty())
/* 28 */     { x = x.replaceAll(", ", ",");
/* 29 */       std_merits = Arrays.asList(x.split(","));
/*    */        }
/*    */     
/* 32 */     else if (std_merits == null) { std_merits = new ArrayList<>(); }
/*    */   
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\SplitReplaceTester.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */