/*     */ package edu.lsu.estimator;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import javax.persistence.Basic;
/*     */ import javax.persistence.Column;
/*     */ import javax.persistence.Entity;
/*     */ import javax.persistence.GeneratedValue;
/*     */ import javax.persistence.GenerationType;
/*     */ import javax.persistence.Id;
/*     */ import javax.persistence.NamedQueries;
/*     */ import javax.persistence.NamedQuery;
/*     */ import javax.persistence.Table;
/*     */ import javax.persistence.TableGenerator;
/*     */ import javax.validation.constraints.NotNull;
/*     */ import javax.validation.constraints.Size;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
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
/*     */ @Entity
/*     */ @Table(name = "VERSIONS")
/*     */ @XmlRootElement
/*     */ @NamedQueries({@NamedQuery(name = "Version.findAll", query = "SELECT v FROM Version v"), @NamedQuery(name = "Version.findByModule", query = "SELECT v FROM Version v WHERE v.module = :module"), @NamedQuery(name = "Version.findByVersion", query = "SELECT v FROM Version v WHERE v.version = :version"), @NamedQuery(name = "Version.findByDos", query = "SELECT v FROM Version v WHERE v.dos = :dos"), @NamedQuery(name = "Version.findBySrcTime", query = "SELECT v FROM Version v WHERE v.srcTime = :srcTime"), @NamedQuery(name = "Version.findBySrcTz", query = "SELECT v FROM Version v WHERE v.srcTz = :srcTz"), @NamedQuery(name = "Version.findBySrcWho", query = "SELECT v FROM Version v WHERE v.srcWho = :srcWho"), @NamedQuery(name = "Version.findByEffInd", query = "SELECT v FROM Version v WHERE v.effInd = :effInd"), @NamedQuery(name = "Version.findByRecid", query = "SELECT v FROM Version v WHERE v.recid = :recid")})
/*     */ public class Version
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   @Id
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "RECID")
/*     */   @TableGenerator(name = "VERSEQ_Gen", table = "SEQUENCE", pkColumnName = "NAME", valueColumnName = "ID", pkColumnValue = "VER_GEN", initialValue = 1, allocationSize = 1)
/*     */   @GeneratedValue(strategy = GenerationType.TABLE, generator = "VERSEQ_Gen")
/*     */   private Integer recid;
/*     */   @Size(max = 30)
/*     */   @Column(name = "MODULE")
/*     */   private String module;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "VERSION")
/*     */   private int version;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "DOS")
/*     */   private long dos;
/*     */   @Column(name = "DOSTZ")
/*     */   @Size(max = 20)
/*     */   private String dostz;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "SRC_TIME")
/*     */   private long srcTime;
/*     */   @Size(max = 20)
/*     */   @Column(name = "SRC_TZ")
/*     */   private String srcTz;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "SRC_WHO")
/*     */   private int srcWho;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "EFF_IND")
/*     */   private int effInd;
/*     */   
/*     */   public Version() {}
/*     */   
/*     */   public Version(Integer recid) {
/*  78 */     this.recid = recid;
/*     */   }
/*     */   
/*     */   public Version(Integer recid, int version, long dos, long srcTime, int srcWho, int effInd) {
/*  82 */     this.recid = recid;
/*  83 */     this.version = version;
/*  84 */     this.dos = dos;
/*  85 */     this.srcTime = srcTime;
/*  86 */     this.srcWho = srcWho;
/*  87 */     this.effInd = effInd;
/*     */   }
/*     */   
/*     */   public String getModule() {
/*  91 */     return this.module;
/*     */   }
/*     */   
/*     */   public void setModule(String module) {
/*  95 */     this.module = module;
/*     */   }
/*     */   
/*     */   public int getVersion() {
/*  99 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setVersion(int version) {
/* 103 */     this.version = version;
/*     */   }
/*     */   
/*     */   public long getDos() {
/* 107 */     return this.dos;
/*     */   }
/*     */   
/*     */   public void setDos(long dos) {
/* 111 */     this.dos = dos;
/*     */   }
/*     */   
/*     */   public long getSrcTime() {
/* 115 */     return this.srcTime;
/*     */   }
/*     */   
/*     */   public void setSrcTime(long srcTime) {
/* 119 */     this.srcTime = srcTime;
/*     */   }
/*     */   
/*     */   public String getSrcTz() {
/* 123 */     return this.srcTz;
/*     */   }
/*     */   
/*     */   public void setSrcTz(String srcTz) {
/* 127 */     this.srcTz = srcTz;
/*     */   }
/*     */   
/*     */   public int getSrcWho() {
/* 131 */     return this.srcWho;
/*     */   }
/*     */   
/*     */   public void setSrcWho(int srcWho) {
/* 135 */     this.srcWho = srcWho;
/*     */   }
/*     */   
/*     */   public int getEffInd() {
/* 139 */     return this.effInd;
/*     */   }
/*     */   
/*     */   public void setEffInd(int effInd) {
/* 143 */     this.effInd = effInd;
/*     */   }
/*     */   
/*     */   public Integer getRecid() {
/* 147 */     return this.recid;
/*     */   }
/*     */   
/*     */   public void setRecid(Integer recid) {
/* 151 */     this.recid = recid;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 156 */     int hash = 0;
/* 157 */     hash += (this.recid != null) ? this.recid.hashCode() : 0;
/* 158 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 164 */     if (!(object instanceof edu.lsu.estimator.Version)) {
/* 165 */       return false;
/*     */     }
/* 167 */     edu.lsu.estimator.Version other = (edu.lsu.estimator.Version)object;
/* 168 */     if ((this.recid == null && other.recid != null) || (this.recid != null && !this.recid.equals(other.recid))) {
/* 169 */       return false;
/*     */     }
/* 171 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 176 */     return "edu.lsu.estimator.Version[ recid=" + this.recid + " ]";
/*     */   }
/*     */   
/*     */   public String getDostz() {
/* 180 */     return this.dostz;
/*     */   }
/*     */   
/*     */   public void setDostz(String dostz) {
/* 184 */     this.dostz = dostz;
/*     */   }
/*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\Version.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */