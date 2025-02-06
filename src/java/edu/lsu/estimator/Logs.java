/*     */ package edu.lsu.estimator;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ import javax.persistence.Basic;
/*     */ import javax.persistence.Column;
/*     */ import javax.persistence.Entity;
/*     */ import javax.persistence.Id;
/*     */ import javax.persistence.NamedQueries;
/*     */ import javax.persistence.NamedQuery;
/*     */ import javax.persistence.Table;
/*     */ import javax.persistence.Temporal;
/*     */ import javax.persistence.TemporalType;
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
/*     */ @Entity
/*     */ @Table(name = "LOGS")
/*     */ @XmlRootElement
/*     */ @NamedQueries({@NamedQuery(name = "Logs.findAll", query = "SELECT l FROM Logs l"), @NamedQuery(name = "Logs.findByType", query = "SELECT l FROM Logs l WHERE l.type = :type"), @NamedQuery(name = "Logs.findByCat", query = "SELECT l FROM Logs l WHERE l.cat = :cat"), @NamedQuery(name = "Logs.findByLocation", query = "SELECT l FROM Logs l WHERE l.location = :location"), @NamedQuery(name = "Logs.findByWhattime", query = "SELECT l FROM Logs l WHERE l.whattime = :whattime"), @NamedQuery(name = "Logs.findByWho", query = "SELECT l FROM Logs l WHERE l.who = :who"), @NamedQuery(name = "Logs.findByWhat", query = "SELECT l FROM Logs l WHERE l.what = :what"), @NamedQuery(name = "Logs.findByResult", query = "SELECT l FROM Logs l WHERE l.result = :result")})
/*     */ public class Logs
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Size(min = 1, max = 50)
/*     */   @Column(name = "TYPE")
/*     */   private String type;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Size(min = 1, max = 50)
/*     */   @Column(name = "CAT")
/*     */   private String cat;
/*     */   @Size(max = 250)
/*     */   @Column(name = "LOCATION")
/*     */   private String location;
/*     */   @Id
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "WHATTIME")
/*     */   @Temporal(TemporalType.TIMESTAMP)
/*     */   private Date whattime;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Size(min = 1, max = 40)
/*     */   @Column(name = "WHO")
/*     */   private String who;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Size(min = 1, max = 250)
/*     */   @Column(name = "WHAT")
/*     */   private String what;
/*     */   @Size(max = 80)
/*     */   @Column(name = "RESULT")
/*     */   private String result;
/*     */   
/*     */   public Logs() {}
/*     */   
/*     */   public Logs(Date whattime) {
/*  69 */     this.whattime = whattime;
/*     */   }
/*     */   
/*     */   public Logs(Date whattime, String type, String cat, String who, String what) {
/*  73 */     this.whattime = whattime;
/*  74 */     this.type = type;
/*  75 */     this.cat = cat;
/*  76 */     this.who = who;
/*  77 */     this.what = what;
/*     */   }
/*     */   
/*     */   public String getType() {
/*  81 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/*  85 */     this.type = type;
/*     */   }
/*     */   
/*     */   public String getCat() {
/*  89 */     return this.cat;
/*     */   }
/*     */   
/*     */   public void setCat(String cat) {
/*  93 */     this.cat = cat;
/*     */   }
/*     */   
/*     */   public String getLocation() {
/*  97 */     return this.location;
/*     */   }
/*     */   
/*     */   public void setLocation(String location) {
/* 101 */     this.location = location;
/*     */   }
/*     */   
/*     */   public Date getWhattime() {
/* 105 */     return this.whattime;
/*     */   }
/*     */   
/*     */   public void setWhattime(Date whattime) {
/* 109 */     this.whattime = whattime;
/*     */   }
/*     */   
/*     */   public String getWho() {
/* 113 */     return this.who;
/*     */   }
/*     */   
/*     */   public void setWho(String who) {
/* 117 */     this.who = who;
/*     */   }
/*     */   
/*     */   public String getWhat() {
/* 121 */     return this.what;
/*     */   }
/*     */   
/*     */   public void setWhat(String what) {
/* 125 */     this.what = what;
/*     */   }
/*     */   
/*     */   public String getResult() {
/* 129 */     return this.result;
/*     */   }
/*     */   
/*     */   public void setResult(String result) {
/* 133 */     this.result = result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 138 */     int hash = 0;
/* 139 */     hash += (this.whattime != null) ? this.whattime.hashCode() : 0;
/* 140 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 146 */     if (!(object instanceof edu.lsu.estimator.Logs)) {
/* 147 */       return false;
/*     */     }
/* 149 */     edu.lsu.estimator.Logs other = (edu.lsu.estimator.Logs)object;
/* 150 */     if ((this.whattime == null && other.whattime != null) || (this.whattime != null && !this.whattime.equals(other.whattime))) {
/* 151 */       return false;
/*     */     }
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 158 */     return "edu.lsu.estimator.Logs[ whattime=" + this.whattime + " ]";
/*     */   }
/*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\Logs.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */