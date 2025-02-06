/*    */ package edu.lsu.estimator.secu;
/*    */ 
/*    */ import org.jasypt.salt.SaltGenerator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SaltGen
/*    */   implements SaltGenerator
/*    */ {
/* 14 */   byte[] salt8 = new byte[] { -61, 94, 35, -91, Byte.MAX_VALUE, -7, -23, -103 };
/*    */ 
/*    */ 
/*    */   
/* 18 */   byte[] salt16 = new byte[] { -61, 94, 35, -91, Byte.MAX_VALUE, -7, -23, -103, -9, 19, -122, 98, 65, 55, -44, -80 };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] generateSalt(int lengthBytes) {
/* 27 */     if (lengthBytes == 8) return this.salt8; 
/* 28 */     return this.salt16;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean includePlainSaltInEncryptionResults() {
/* 34 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\secu\SaltGen.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */