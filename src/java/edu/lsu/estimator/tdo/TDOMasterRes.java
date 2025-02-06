/*    */ package edu.lsu.estimator.tdo;
/*    */ 
/*    */ import edu.lsu.estimator.tdo.TDOStudent;
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TDOMasterRes<T>
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public ArrayList<String> msgs;
/*    */   public int code;
/*    */   public String masterver;
/*    */   public Integer objsversion;
/*    */   public long objs_masterdoe;
/*    */   public String objs_masterdoetz;
/*    */   public Integer objs_masterupdator;
/*    */   public long masterclock;
/*    */   public String mastertz;
/* 36 */   public int remote_clock_ind = 0;
/* 37 */   public int remote_stop_sys_ind = 0;
/* 38 */   public int remote_stop_user_ind = 0;
/*    */   public byte[] aesData;
/*    */   public int clientInitNumb;
/*    */   public List<T> objs;
/*    */   public List<TDOStudent> obj2s;
/*    */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\tdo\TDOMasterRes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */