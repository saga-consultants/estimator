/*    */ package edu.lsu.estimator;
/*    */ 
/*    */ import javax.enterprise.context.RequestScoped;
/*    */ import javax.inject.Named;
/*    */ import javax.validation.constraints.NotNull;
/*    */ import org.hibernate.validator.constraints.Length;
/*    */ import org.hibernate.validator.constraints.NotEmpty;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Named("credentials")
/*    */ @RequestScoped
/*    */ public class Credentials
/*    */ {
           private String initialTextPlaceHolder="Enter your username ";
           private String initialPasswordPlaceHolder="Enter your password ";
/* 21 */   private String username = "";
/* 22 */   private String password = ""; @NotNull
/*    */   @Length(min = 3, max = 64)
/*    */   @NotEmpty(message = "{testBVmsg}")
/*    */   public String getUsername() {
/* 26 */     return this.username;
/*    */   }
/* 28 */   public void setUsername(String username) { this.username = username; } @NotNull
/*    */   @Length(min = 3, max = 64)
/*    */   @NotEmpty
/*    */   public String getPassword() {
/* 32 */     return this.password;
/*    */   } public void setPassword(String password) {
/* 34 */     this.password = password;
/*    */   }
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\Credentials.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */