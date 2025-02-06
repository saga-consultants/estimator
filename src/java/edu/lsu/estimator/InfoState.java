/*     */ package edu.lsu.estimator;
/*     */ 
/*     */ import com.kingombo.slf5j.Logger;
/*     */ import com.kingombo.slf5j.LoggerFactory;
/*     */ import edu.lsu.estimator.Accessor;
/*     */ import edu.lsu.estimator.AppReference;
/*     */ import edu.lsu.estimator.Login;
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.enterprise.context.SessionScoped;
/*     */ import javax.inject.Inject;
/*     */ import javax.inject.Named;
/*     */ import javax.persistence.EntityManager;
/*     */ import javax.persistence.PersistenceContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Named("info")
/*     */ @SessionScoped
/*     */ public class InfoState
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  30 */   private int newStudsOfUser = 0;
/*  31 */   private int newStudsAccmOfAll = 0;
/*  32 */   private int studsOfUser = 0;
/*  33 */   private int totStuds = 0;
/*  34 */   private int amtUserPCs = 0;
/*     */   
/*  36 */   private int usersNumb = 0;
/*  37 */   private int otherCounselorNumb = 0;
/*  38 */   private int otherCounselorStuds = 0;
/*     */ 
/*     */   
/*     */   private Date lastSyncTime;
/*     */   
/*     */   private Date firstUseDate;
/*     */   
/*     */   private Date staleSinceDate;
/*     */   
/*  47 */   private int masterLive = 0;
/*  48 */   private int masterReady = 0;
/*     */ 
/*     */   
/*  51 */   private int nonprts = 0;
/*  52 */   private int upprts = 0;
/*     */   
/*     */   @PersistenceContext
/*     */   private EntityManager em;
/*     */   
/*     */   @Inject
/*     */   Login login;
/*     */   @Inject
/*     */   Accessor accessor;
/*  61 */   private static final Logger log = LoggerFactory.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Inject
/*     */   AppReference ref;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sumUp() {
/*  90 */    
 long data_0=((long)this.em.createNativeQuery("select count(*) from student s where s.student_fisy=? and s.counselor_id=? and s.pickup_ind =1  ")
/*  91 */       .setParameter(1, Integer.valueOf(this.ref.getFiscal_year()))
/*  92 */       .setParameter(2, this.login.getCurrentUser().getUserid())
/*  93 */       .getSingleResult());


this.studsOfUser =(int) data_0;
/*     */ 
/*     */     
/*  96 */     List<Date> lastsyncs = this.em.createNativeQuery("select max(l.whattime) from Logs l where  l.type='SYNC' and l.cat='UPLOAD' and l.result='ok' ").getResultList();
/*  97 */     List<Date> lastsignins = this.em.createNativeQuery("select min(l.whattime) from Logs l where l.type='SESSION' and l.cat='SIGNIN' and l.result='ok' ").getResultList();
/*  98 */     if (lastsyncs != null && lastsyncs.size() > 0 && lastsyncs.get(0) != null) {
    System.out.println(" Sara lastsyncs.get(0) " + lastsyncs.get(0));
/*  99 */       this.lastSyncTime = lastsyncs.get(0);
/*     */     }
/* 101 */     else if (lastsignins != null && lastsignins.size() > 0 && lastsignins.get(0) != null) {
/* 102 */       this.lastSyncTime = lastsignins.get(0);
/*     */     } else {
/*     */       
/* 105 */       this.lastSyncTime = new Date();
/*     */     } 
/*     */ 
/*     */ 
/*     */     long data_=(long)(this.em.createNativeQuery("select count(*) from student s where s.student_fisy=? and s.pickup_ind=1 and  s.dup=0")
/* 111 */       .setParameter(1, Integer.valueOf(this.ref.getFiscal_year()))
/* 112 */       .getSingleResult());
/* 110 */     int newstuds =(int)data_;
/* 113 */     this.newStudsOfUser = newstuds;
/*     */     
              long data_1=(long)this.em.createNativeQuery("select count(*) from (select distinct s.counselor_id from student s where  s.student_fisy=? and s.counselor_id<>? and s.pickup_ind=1  )x")
/* 116 */       .setParameter(1, Integer.valueOf(this.ref.getFiscal_year()))
/* 117 */       .setParameter(2, this.login.getCurrentUser().getUserid())
/* 118 */       .getSingleResult();
/* 115 */     int others =(int)data_1;
/* 119 */     this.otherCounselorNumb = others;
/*     */     
              long data_2=((long)this.em.createNativeQuery("select count(*) from student s where  s.student_fisy=? and  s.counselor_id<>? and s.pickup_ind=1  ")
/* 122 */       .setParameter(1, Integer.valueOf(this.ref.getFiscal_year()))
/* 123 */       .setParameter(2, this.login.getCurrentUser().getUserid())
/* 124 */       .getSingleResult());
/* 121 */     int otherstuds =(int)data_2 ;
/* 125 */     this.otherCounselorStuds = otherstuds;
/*     */     
/* 127 */     this.totStuds = this.studsOfUser + this.otherCounselorStuds;
/*     */ 
/*     */ 
/*     */     long data_3=(long)this.em.createNativeQuery("select count(a.recid) from student a where a.student_fisy=? and a.pickup_ind=1 and a.lost_time=0 and not exists ( select '1' from prints b where b.recid = a.recid)")
/* 132 */       .setParameter(1, Integer.valueOf(this.ref.getFiscal_year()))
/* 133 */       .getSingleResult();
/* 131 */     this.nonprts = (int)data_3;
/*     */     
long data_4=(long)this.em.createNativeQuery("select count(a.recid) from student a where a.student_fisy=? and  a.dup>0 and not exists ( select '1' from prints b where b.recid = a.recid)")
/* 136 */       .setParameter(1, Integer.valueOf(this.ref.getFiscal_year()))
/* 137 */       .getSingleResult();
/* 135 */     this.upprts = (int)data_4;
/*     */ 
/*     */ 
/*     */     
/* 141 */     this.accessor.triggerTmpidPrt();
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getNowStudNumbInQueue() {
/* 146 */     int res = 0;
/* 147 */     res = ((Integer)this.em.createNativeQuery("select id from sequence where name='STUD_GEN'").getSingleResult()).intValue();
/* 148 */     return Integer.valueOf(res);
/*     */   }
/*     */   public String getStudRecid(Integer counselorid, Integer clientid, Integer seqnumb) {
/* 151 */     String res = "";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 156 */     res = (String)this.em.createNativeQuery("select recid from student where counselor_id=? and client_id=? and student_numb=?").setParameter(1, counselorid).setParameter(2, clientid).setParameter(3, seqnumb).getSingleResult();
/* 157 */     return res;
/*     */   }
/*     */   
/*     */   public Integer getNowPrtNumbInQueue() {
/* 161 */     int res = 0;
/* 162 */     res = ((Integer)this.em.createNativeQuery("select id from sequence where name='PRT_GEN'").getSingleResult()).intValue();
/* 163 */     return Integer.valueOf(res);
/*     */   }
/*     */   public String getPrintPrtid(Integer counselorid, Integer clientid, Integer seqnumb) {
/* 166 */     String res = "";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     res = (String)this.em.createNativeQuery("select prt_id from prints p where  p.counselor_id=? and p.client_id=? and p.prt_num=?").setParameter(1, counselorid).setParameter(2, clientid).setParameter(3, seqnumb).getSingleResult();
/* 172 */     return res;
/*     */   }
/*     */   
/*     */   public int getNewStudsOfUser() {
/* 176 */     return this.newStudsOfUser;
/*     */   }
/*     */   
/*     */   public void setNewStudsOfUser(int newStudsOfUser) {
/* 180 */     this.newStudsOfUser = newStudsOfUser;
/*     */   }
/*     */   
/*     */   public int getNewStudsAccmOfAll() {
/* 184 */     return this.newStudsAccmOfAll;
/*     */   }
/*     */   
/*     */   public void setNewStudsAccmOfAll(int newStudsAccmOfAll) {
/* 188 */     this.newStudsAccmOfAll = newStudsAccmOfAll;
/*     */   }
/*     */   
/*     */   public int getUsersNumb() {
/* 192 */     return this.usersNumb;
/*     */   }
/*     */   
/*     */   public void setUsersNumb(int usersNumb) {
/* 196 */     this.usersNumb = usersNumb;
/*     */   }
/*     */   
/*     */   public Date getLastSyncTime() {
/* 200 */     return this.lastSyncTime;
/*     */   }
/*     */   
/*     */   public void setLastSyncTime(Date lastSyncTime) {
/* 204 */     this.lastSyncTime = lastSyncTime;
/*     */   }
/*     */   
/*     */   public Date getFirstUseDate() {
/* 208 */     return this.firstUseDate;
/*     */   }
/*     */   
/*     */   public void setFirstUseDate(Date firstUseDate) {
/* 212 */     this.firstUseDate = firstUseDate;
/*     */   }
/*     */   
/*     */   public Date getStaleSinceDate() {
/* 216 */     return this.staleSinceDate;
/*     */   }
/*     */   
/*     */   public void setStaleSinceDate(Date staleSinceDate) {
/* 220 */     this.staleSinceDate = staleSinceDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMasterLive() {
/* 248 */     return this.masterLive;
/*     */   }
/*     */   
/*     */   public void setMasterLive(int masterLive) {
/* 252 */     this.masterLive = masterLive;
/*     */   }
/*     */   
/*     */   public int getMasterReady() {
/* 256 */     return this.masterReady;
/*     */   }
/*     */   
/*     */   public void setMasterReady(int masterReady) {
/* 260 */     this.masterReady = masterReady;
/*     */   }
/*     */   
/*     */   public int getOtherCounselorNumb() {
/* 264 */     return this.otherCounselorNumb;
/*     */   }
/*     */   
/*     */   public void setOtherCounselorNumb(int otherCounselorNumb) {
/* 268 */     this.otherCounselorNumb = otherCounselorNumb;
/*     */   }
/*     */   
/*     */   public int getOtherCounselorStuds() {
/* 272 */     return this.otherCounselorStuds;
/*     */   }
/*     */   
/*     */   public void setOtherCounselorStuds(int otherCounselorStuds) {
/* 276 */     this.otherCounselorStuds = otherCounselorStuds;
/*     */   }
/*     */   
/*     */   public int getTotStuds() {
/* 280 */     return this.totStuds;
/*     */   }
/*     */   
/*     */   public void setTotStuds(int totStuds) {
/* 284 */     this.totStuds = totStuds;
/*     */   }
/*     */   
/*     */   public int getAmtUserPCs() {
/* 288 */     return this.amtUserPCs;
/*     */   }
/*     */   
/*     */   public void setAmtUserPCs(int amtUserPCs) {
/* 292 */     this.amtUserPCs = amtUserPCs;
/*     */   }
/*     */   
/*     */   public int getStudsOfUser() {
/* 296 */     return this.studsOfUser;
/*     */   }
/*     */   
/*     */   public void setStudsOfUser(int studsOfUser) {
/* 300 */     this.studsOfUser = studsOfUser;
/*     */   }
/*     */   
/*     */   public int getNonprts() {
/* 304 */     return this.nonprts;
/*     */   }
/*     */   
/*     */   public int getUpprts() {
/* 308 */     return this.upprts;
/*     */   }
/*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\InfoState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */