/*     */ package edu.lsu.estimator;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import javax.persistence.Basic;
/*     */ import javax.persistence.Column;
/*     */ import javax.persistence.Entity;
/*     */ import javax.persistence.Id;
/*     */ import javax.persistence.NamedQueries;
/*     */ import javax.persistence.NamedQuery;
/*     */ import javax.persistence.Table;
/*     */ import javax.validation.constraints.NotNull;
/*     */ import javax.validation.constraints.Size;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import org.hibernate.validator.constraints.NotEmpty;
/*     */ import org.hibernate.validator.constraints.Range;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Entity
/*     */ @Table(name = "COUNSELORS")
/*     */ @XmlRootElement
/*     */ @NamedQueries({@NamedQuery(name = "Counselor.findAll", query = "SELECT c FROM Counselor c"), 
    @NamedQuery(name = "Counselor.findByEmail", query = "SELECT c FROM Counselor c WHERE c.email = :email"), 
    @NamedQuery(name = "Counselor.findByUsername", query = "SELECT c FROM Counselor c WHERE upper(c.username) = upper(:username)"), @NamedQuery(name = "Counselor.findByUsernameAndStatus", query = "SELECT c FROM Counselor c WHERE c.username = :username and c.status = :status"), @NamedQuery(name = "Counselor.findBySuperuser", query = "SELECT c FROM Counselor c WHERE c.superuser = :superuser"), @NamedQuery(name = "Counselor.findByUserid", query = "SELECT c FROM Counselor c WHERE c.userid = :userid"), @NamedQuery(name = "Counselor.findByCreator", query = "SELECT c FROM Counselor c WHERE c.creator = :creator"), @NamedQuery(name = "Counselor.findByStatus", query = "SELECT c FROM Counselor c WHERE c.status = :status"), @NamedQuery(name = "Counselor.findByEditor", query = "SELECT c FROM Counselor c WHERE c.editor = :editor"), @NamedQuery(name = "Counselor.findByDom", query = "SELECT c FROM Counselor c WHERE c.dom = :dom"), @NamedQuery(name = "Counselor.findByDoe", query = "SELECT c FROM Counselor c WHERE c.doe = :doe"), @NamedQuery(name = "Counselor.findByDoetz", query = "SELECT c FROM Counselor c WHERE c.doetz = :doetz"), @NamedQuery(name = "Counselor.findByDomtz", query = "SELECT c FROM Counselor c WHERE c.domtz = :domtz"), @NamedQuery(name = "Counselor.findByShadow", query = "SELECT c FROM Counselor c WHERE c.shadow = :shadow"), @NamedQuery(name = "Counselor.findByLsuid", query = "SELECT c FROM Counselor c WHERE c.lsuid = :lsuid")})
/*     */ public class Counselor
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   @Size(max = 40)
/*     */   @Column(name = "EMAIL")
/*     */   private String email;
/*     */   @Size(max = 30)
/*     */   @Column(name = "USERNAME")
/*     */   private String username;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "SUPERUSER")
/*     */   private Integer superuser;
/*     */   @Id
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "USERID")
/*     */   private Integer userid;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "CREATOR")
/*     */   private int creator;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "STATUS")
/*     */   private int status;
/*     */   @Column(name = "EDITOR")
/*     */   private Integer editor;
/*     */   @Column(name = "DOM")
/*     */   private long dom;
/*     */   @Column(name = "DOE")
/*     */   private long doe;
/*     */   @Size(max = 20)
/*     */   @Column(name = "DOETZ")
/*     */   private String doetz;
/*     */   @Size(max = 20)
/*     */   @Column(name = "DOMTZ")
/*     */   private String domtz;
/*     */   @Column(name = "DOS")
/*     */   private long dos;
/*     */   @Size(max = 20)
/*     */   @Column(name = "DOSTZ")
/*     */   private String dostz;
/*     */   @Size(max = 256)
/*     */   @Column(name = "SHADOW")
/*     */   private String shadow;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Size(min = 1, max = 10)
/*     */   @Column(name = "LSUID")
/*     */   private String lsuid;
/*     */   @Column(name = "DEPTNAME")
/*     */   @NotNull
/*     */   @NotEmpty
/*     */   @Size(max = 30)
/*     */   private String deptName;
/*     */   @Column(name = "STD_IND")
/*     */   @Range(min = 0L, max = 1L)
/*     */   private Integer stdInd;
/*     */   
/*     */   public Counselor() {}
/*     */   
/*     */   public Counselor(Integer userid) {
/* 114 */     this.userid = userid;
/*     */   }
/*     */   
/*     */   public Counselor(Integer userid, Integer superuser, int creator, int status, String lsuid) {
/* 118 */     this.userid = userid;
/* 119 */     this.superuser = superuser;
/* 120 */     this.creator = creator;
/* 121 */     this.status = status;
/* 122 */     this.lsuid = lsuid;
/*     */   }
/*     */   
/*     */   public String getEmail() {
/* 126 */     return this.email;
/*     */   }
/*     */   
/*     */   public void setEmail(String email) {
/* 130 */     this.email = email;
/*     */   }
/*     */   
/*     */   public String getUsername() {
/* 134 */     return this.username;
/*     */   }
/*     */   
/*     */   public void setUsername(String username) {
/* 138 */     this.username = username;
/*     */   }
/*     */   
/*     */   public Integer getSuperuser() {
/* 142 */     return this.superuser;
/*     */   }
/*     */   
/*     */   public void setSuperuser(Integer superuser) {
/* 146 */     this.superuser = superuser;
/*     */   }
/*     */   
/*     */   public Integer getUserid() {
/* 150 */     return this.userid;
/*     */   }
/*     */   
/*     */   public void setUserid(Integer userid) {
/* 154 */     this.userid = userid;
/*     */   }
/*     */   
/*     */   public int getCreator() {
/* 158 */     return this.creator;
/*     */   }
/*     */   
/*     */   public void setCreator(int creator) {
/* 162 */     this.creator = creator;
/*     */   }
/*     */   
/*     */   public int getStatus() {
/* 166 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setStatus(int status) {
/* 170 */     this.status = status;
/*     */   }
/*     */   
/*     */   public Integer getEditor() {
/* 174 */     return this.editor;
/*     */   }
/*     */   
/*     */   public void setEditor(Integer editor) {
/* 178 */     this.editor = editor;
/*     */   }
/*     */   
/*     */   public long getDom() {
/* 182 */     return this.dom;
/*     */   }
/*     */   
/*     */   public void setDom(long dom) {
/* 186 */     this.dom = dom;
/*     */   }
/*     */   
/*     */   public long getDoe() {
/* 190 */     return this.doe;
/*     */   }
/*     */   
/*     */   public void setDoe(long doe) {
/* 194 */     this.doe = doe;
/*     */   }
/*     */   
/*     */   public String getDoetz() {
/* 198 */     return this.doetz;
/*     */   }
/*     */   
/*     */   public void setDoetz(String doetz) {
/* 202 */     this.doetz = doetz;
/*     */   }
/*     */   
/*     */   public String getDomtz() {
/* 206 */     return this.domtz;
/*     */   }
/*     */   
/*     */   public void setDomtz(String domtz) {
/* 210 */     this.domtz = domtz;
/*     */   }
/*     */   
/*     */   public String getShadow() {
/* 214 */     return this.shadow;
/*     */   }
/*     */   
/*     */   public void setShadow(String shadow) {
/* 218 */     this.shadow = shadow;
/*     */   }
/*     */   
/*     */   public String getLsuid() {
/* 222 */     return this.lsuid;
/*     */   }
/*     */   
/*     */   public void setLsuid(String lsuid) {
/* 226 */     this.lsuid = lsuid;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 231 */     int hash = 0;
/* 232 */     hash += (this.userid != null) ? this.userid.hashCode() : 0;
/* 233 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 239 */     if (!(object instanceof edu.lsu.estimator.Counselor)) {
/* 240 */       return false;
/*     */     }
/* 242 */     edu.lsu.estimator.Counselor other = (edu.lsu.estimator.Counselor)object;
/* 243 */     if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
/* 244 */       return false;
/*     */     }
/* 246 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 251 */     return "edu.lsu.estimator.Counselor[ userid=" + this.userid + " ]";
/*     */   }
/*     */   
/*     */   public String getDeptName() {
/* 255 */     return this.deptName;
/*     */   }
/*     */   
/*     */   public void setDeptName(String deptName) {
/* 259 */     this.deptName = deptName;
/*     */   }
/*     */   
/*     */   public long getDos() {
/* 263 */     return this.dos;
/*     */   }
/*     */   
/*     */   public void setDos(long dos) {
/* 267 */     this.dos = dos;
/*     */   }
/*     */   
/*     */   public String getDostz() {
/* 271 */     return this.dostz;
/*     */   }
/*     */   
/*     */   public void setDostz(String dostz) {
/* 275 */     this.dostz = dostz;
/*     */   }
/*     */   
/*     */   public Integer getStdInd() {
/* 279 */     return this.stdInd;
/*     */   }
/*     */   
/*     */   public void setStdInd(Integer stdInd) {
/* 283 */     this.stdInd = stdInd;
/*     */   }
/*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\Counselor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */