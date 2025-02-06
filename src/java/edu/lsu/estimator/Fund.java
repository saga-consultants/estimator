/*     */ package edu.lsu.estimator;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.math.BigDecimal;
/*     */ import javax.persistence.Basic;
/*     */ import javax.persistence.Column;
/*     */ import javax.persistence.Entity;
/*     */ import javax.persistence.Id;
/*     */ import javax.persistence.NamedQueries;
/*     */ import javax.persistence.NamedQuery;
/*     */ import javax.persistence.Table;
/*     */ import javax.validation.constraints.Max;
/*     */ import javax.validation.constraints.Min;
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
/*     */ @Table(name = "FUNDS")
/*     */ @XmlRootElement
/*     */ @NamedQueries({@NamedQuery(name = "Fund.findAll", query = "SELECT f FROM Fund f"), @NamedQuery(name = "Fund.findByFundCode", query = "SELECT f FROM Fund f WHERE f.fundCode = :fundCode"), @NamedQuery(name = "Fund.findByFundDesc", query = "SELECT f FROM Fund f WHERE f.fundDesc = :fundDesc"), @NamedQuery(name = "Fund.findByReqNoteInd", query = "SELECT f FROM Fund f WHERE f.reqNoteInd = :reqNoteInd"), @NamedQuery(name = "Fund.findByHasMatching", query = "SELECT f FROM Fund f WHERE f.hasMatching = :hasMatching"), @NamedQuery(name = "Fund.findByMatchPerc", query = "SELECT f FROM Fund f WHERE f.matchPerc = :matchPerc"), @NamedQuery(name = "Fund.findByMatchTop", query = "SELECT f FROM Fund f WHERE f.matchTop = :matchTop"), @NamedQuery(name = "Fund.findByInstCapExcept", query = "SELECT f FROM Fund f WHERE f.instCapExcept = :instCapExcept"), @NamedQuery(name = "Fund.findByPriority", query = "SELECT f FROM Fund f WHERE f.priority = :priority"), @NamedQuery(name = "Fund.findByStatus", query = "SELECT f FROM Fund f WHERE f.status = :status order by f.fundDesc"), @NamedQuery(name = "Fund.findByCreator", query = "SELECT f FROM Fund f WHERE f.creator = :creator"), @NamedQuery(name = "Fund.findByUpdator", query = "SELECT f FROM Fund f WHERE f.updator = :updator"), @NamedQuery(name = "Fund.findBySyncor", query = "SELECT f FROM Fund f WHERE f.syncor = :syncor"), @NamedQuery(name = "Fund.findByEarningsMatch", query = "SELECT f FROM Fund f WHERE f.earningsMatch = :earningsMatch"), @NamedQuery(name = "Fund.findByFundId", query = "SELECT f FROM Fund f WHERE f.fundId = :fundId")})
/*     */ public class Fund
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   @Id
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "FUND_ID")
/*     */   private Integer fundId;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Size(min = 1, max = 50)
/*     */   @Column(name = "FUND_CODE")
/*     */   private String fundCode;
/*     */   @Size(max = 70)
/*     */   @Column(name = "FUND_DESC")
/*     */   private String fundDesc;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Size(min = 1, max = 10)
/*     */   @Column(name = "REQ_NOTE_IND")
/*     */   private String reqNoteInd;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Size(min = 1, max = 3)
/*     */   @Column(name = "HAS_MATCHING")
/*     */   private String hasMatching;
/*     */   @Column(name = "MATCH_PERC")
/*     */   private BigDecimal matchPerc;
/*     */   @Column(name = "MATCH_TOP")
/*     */   private Integer matchTop;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Size(min = 1, max = 3)
/*     */   @Column(name = "INST_CAP_EXCEPT")
/*     */   private String instCapExcept;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "PRIORITY")
/*     */   private int priority;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Size(min = 1, max = 10)
/*     */   @Column(name = "STATUS")
/*     */   private String status;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "CREATOR")
/*     */   private Integer creator;
/*     */   @Column(name = "UPDATOR")
/*     */   private Integer updator;
/*     */   @Column(name = "SYNCOR")
/*     */   private Integer syncor;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Size(min = 1, max = 3)
/*     */   @Column(name = "EARNINGS_MATCH")
/*     */   private String earningsMatch;
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
/*     */   @NotNull
/*     */   @Min(0L)
/*     */   @Max(1L)
/*     */   @Column(name = "AUTO_IND")
/* 130 */   private Integer auto_ind = Integer.valueOf(0);
/*     */   
/*     */   @NotNull
/*     */   @Min(-1L)
/*     */   @Max(1L)
/*     */   @Column(name = "MAX_IND")
/* 136 */   private Integer max_ind = Integer.valueOf(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Fund() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Fund(Integer fundId) {
/* 155 */     this.fundId = fundId;
/*     */   }
/*     */   
/*     */   public Fund(Integer fundId, String fundCode, String reqNoteInd, String hasMatching, String instCapExcept, int priority, String status, Integer creator, String earningsMatch, long doe) {
/* 159 */     this.fundId = fundId;
/* 160 */     this.fundCode = fundCode;
/* 161 */     this.reqNoteInd = reqNoteInd;
/* 162 */     this.hasMatching = hasMatching;
/* 163 */     this.instCapExcept = instCapExcept;
/* 164 */     this.priority = priority;
/* 165 */     this.status = status;
/* 166 */     this.creator = creator;
/* 167 */     this.earningsMatch = earningsMatch;
/* 168 */     this.doe = doe;
/*     */   }
/*     */   
/*     */   public String getFundCode() {
/* 172 */     return this.fundCode;
/*     */   }
/*     */   
/*     */   public void setFundCode(String fundCode) {
/* 176 */     this.fundCode = fundCode;
/*     */   }
/*     */   
/*     */   public String getFundDesc() {
/* 180 */     return this.fundDesc;
/*     */   }
/*     */   
/*     */   public void setFundDesc(String fundDesc) {
/* 184 */     this.fundDesc = fundDesc;
/*     */   }
/*     */   
/*     */   public String getReqNoteInd() {
/* 188 */     return this.reqNoteInd;
/*     */   }
/*     */   
/*     */   public void setReqNoteInd(String reqNoteInd) {
/* 192 */     this.reqNoteInd = reqNoteInd;
/*     */   }
/*     */   
/*     */   public String getHasMatching() {
/* 196 */     return this.hasMatching;
/*     */   }
/*     */   
/*     */   public void setHasMatching(String hasMatching) {
/* 200 */     this.hasMatching = hasMatching;
/*     */   }
/*     */   
/*     */   public BigDecimal getMatchPerc() {
/* 204 */     return this.matchPerc;
/*     */   }
/*     */   
/*     */   public void setMatchPerc(BigDecimal matchPerc) {
/* 208 */     this.matchPerc = matchPerc;
/*     */   }
/*     */   
/*     */   public Integer getMatchTop() {
/* 212 */     return this.matchTop;
/*     */   }
/*     */   
/*     */   public void setMatchTop(Integer matchTop) {
/* 216 */     this.matchTop = matchTop;
/*     */   }
/*     */   
/*     */   public String getInstCapExcept() {
/* 220 */     return this.instCapExcept;
/*     */   }
/*     */   
/*     */   public void setInstCapExcept(String instCapExcept) {
/* 224 */     this.instCapExcept = instCapExcept;
/*     */   }
/*     */   
/*     */   public int getPriority() {
/* 228 */     return this.priority;
/*     */   }
/*     */   
/*     */   public void setPriority(int priority) {
/* 232 */     this.priority = priority;
/*     */   }
/*     */   
/*     */   public String getStatus() {
/* 236 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setStatus(String status) {
/* 240 */     this.status = status;
/*     */   }
/*     */   
/*     */   public Integer getCreator() {
/* 244 */     return this.creator;
/*     */   }
/*     */   
/*     */   public void setCreator(Integer creator) {
/* 248 */     this.creator = creator;
/*     */   }
/*     */   
/*     */   public Integer getUpdator() {
/* 252 */     return this.updator;
/*     */   }
/*     */   
/*     */   public void setUpdator(Integer updator) {
/* 256 */     this.updator = updator;
/*     */   }
/*     */   
/*     */   public Integer getSyncor() {
/* 260 */     return this.syncor;
/*     */   }
/*     */   
/*     */   public void setSyncor(Integer syncor) {
/* 264 */     this.syncor = syncor;
/*     */   }
/*     */   
/*     */   public String getEarningsMatch() {
/* 268 */     return this.earningsMatch;
/*     */   }
/*     */   
/*     */   public void setEarningsMatch(String earningsMatch) {
/* 272 */     this.earningsMatch = earningsMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getFundId() {
/* 278 */     return this.fundId;
/*     */   }
/*     */   
/*     */   public void setFundId(Integer fundId) {
/* 282 */     this.fundId = fundId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 287 */     int hash = 0;
/* 288 */     hash += (this.fundId != null) ? this.fundId.hashCode() : 0;
/* 289 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 295 */     if (!(object instanceof edu.lsu.estimator.Fund)) {
/* 296 */       return false;
/*     */     }
/* 298 */     edu.lsu.estimator.Fund other = (edu.lsu.estimator.Fund)object;
/* 299 */     if ((this.fundId == null && other.fundId != null) || (this.fundId != null && !this.fundId.equals(other.fundId))) {
/* 300 */       return false;
/*     */     }
/* 302 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 307 */     return "edu.lsu.estimator.Fund[ fundId=" + this.fundId + " ]";
/*     */   }
/*     */   
/*     */   public long getDom() {
/* 311 */     return this.dom;
/*     */   }
/*     */   
/*     */   public void setDom(long dom) {
/* 315 */     this.dom = dom;
/*     */   }
/*     */   
/*     */   public long getDoe() {
/* 319 */     return this.doe;
/*     */   }
/*     */   
/*     */   public void setDoe(long doe) {
/* 323 */     this.doe = doe;
/*     */   }
/*     */   
/*     */   public String getDoetz() {
/* 327 */     return this.doetz;
/*     */   }
/*     */   
/*     */   public void setDoetz(String doetz) {
/* 331 */     this.doetz = doetz;
/*     */   }
/*     */   
/*     */   public String getDomtz() {
/* 335 */     return this.domtz;
/*     */   }
/*     */   
/*     */   public void setDomtz(String domtz) {
/* 339 */     this.domtz = domtz;
/*     */   }
/*     */   
/*     */   public long getDos() {
/* 343 */     return this.dos;
/*     */   }
/*     */   
/*     */   public void setDos(long dos) {
/* 347 */     this.dos = dos;
/*     */   }
/*     */   
/*     */   public String getDostz() {
/* 351 */     return this.dostz;
/*     */   }
/*     */   
/*     */   public void setDostz(String dostz) {
/* 355 */     this.dostz = dostz;
/*     */   }
/*     */   
/*     */   public Integer getAuto_ind() {
/* 359 */     return this.auto_ind;
/*     */   }
/*     */   
/*     */   public void setAuto_ind(Integer auto_ind) {
/* 363 */     this.auto_ind = auto_ind;
/*     */   }
/*     */   
/*     */   public Integer getMax_ind() {
/* 367 */     return this.max_ind;
/*     */   }
/*     */   
/*     */   public void setMax_ind(Integer max_ind) {
/* 371 */     this.max_ind = max_ind;
/*     */   }
/*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\Fund.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */