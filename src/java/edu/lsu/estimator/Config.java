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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Entity
/*     */ @Table(name = "CONFIG")
/*     */ @XmlRootElement
/*     */ @NamedQueries({@NamedQuery(name = "Config.findAll", query = "SELECT c FROM Config c"), @NamedQuery(name = "Config.findByMastername", query = "SELECT c FROM Config c WHERE c.mastername = :mastername"), @NamedQuery(name = "Config.findByMasteripv4", query = "SELECT c FROM Config c WHERE c.masteripv4 = :masteripv4"), @NamedQuery(name = "Config.findByMasterport", query = "SELECT c FROM Config c WHERE c.masterport = :masterport"), @NamedQuery(name = "Config.findByMasterurl", query = "SELECT c FROM Config c WHERE c.masterurl = :masterurl"), @NamedQuery(name = "Config.findByRemoteDisableLogin", query = "SELECT c FROM Config c WHERE c.remoteDisableLogin = :remoteDisableLogin"), @NamedQuery(name = "Config.findByLiveMode", query = "SELECT c FROM Config c WHERE c.liveMode = :liveMode"), @NamedQuery(name = "Config.findByPingInterval", query = "SELECT c FROM Config c WHERE c.pingInterval = :pingInterval"), @NamedQuery(name = "Config.findByEnabledInd", query = "SELECT c FROM Config c WHERE c.enabledInd = :enabledInd"), @NamedQuery(name = "Config.findByClientFscy", query = "SELECT c FROM Config c WHERE c.clientFscy = :clientFscy"), @NamedQuery(name = "Config.findByClientVersion", query = "SELECT c FROM Config c WHERE c.clientVersion = :clientVersion"), @NamedQuery(name = "Config.findByClientid", query = "SELECT c FROM Config c WHERE c.clientid = :clientid"), @NamedQuery(name = "Config.findByLdapserver", query = "SELECT c FROM Config c WHERE c.ldapserver = :ldapserver"), @NamedQuery(name = "Config.findByLdapsurl", query = "SELECT c FROM Config c WHERE c.ldapsurl = :ldapsurl"), @NamedQuery(name = "Config.findByMasterecho", query = "SELECT c FROM Config c WHERE c.masterecho = :masterecho")})
/*     */ public class Config
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   @Size(max = 128)
/*     */   @Column(name = "MASTERNAME")
/*     */   private String mastername;
/*     */   @Size(max = 15)
/*     */   @Column(name = "MASTERIPV4")
/*     */   private String masteripv4;
/*     */   @Size(max = 5)
/*     */   @Column(name = "MASTERPORT")
/*     */   private String masterport;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Size(min = 1, max = 128)
/*     */   @Column(name = "MASTERURL")
/*     */   private String masterurl;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "REMOTE_DISABLE_LOGIN")
/*     */   private short remoteDisableLogin;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "LIVE_MODE")
/*     */   private short liveMode;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "PING_INTERVAL")
/*     */   private short pingInterval;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "ENABLED_IND")
/*     */   private short enabledInd;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "CLIENT_FSCY")
/*     */   private short clientFscy;
/*     */   @Size(max = 20)
/*     */   @Column(name = "CLIENT_VERSION")
/*     */   private String clientVersion;
/*     */   @Id
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Column(name = "CLIENTID")
/*     */   private Integer clientid;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Size(min = 1, max = 128)
/*     */   @Column(name = "LDAPSERVER")
/*     */   private String ldapserver;
/*     */   @NotNull
/*     */   @Range(min = 80L, max = 65000L)
/*     */   @Column(name = "LDAPPORT")
/*     */   private Integer ldapport;
/*     */   @Basic(optional = false)
/*     */   @NotNull
/*     */   @Size(min = 1, max = 128)
/*     */   @Column(name = "LDAPSURL")
/*     */   private String ldapsurl;
/*     */   @Size(max = 256)
/*     */   @Column(name = "MASTERECHO")
/*     */   private String masterecho;
/*     */   @Size(max = 20)
/*     */   @Column(name = "MASTER_VERSION")
/*     */   private String masterVersion;
/*     */   @Column(name = "POLL_INTERVAL")
/*     */   private short pollInterval;
/*     */   @Range(min = 0L)
/*     */   @Column(name = "UPG_VER_IND")
/*     */   private Integer upgReqInd;
/*     */   
/*     */   public Config() {}
/*     */   
/*     */   public Config(Integer clientid) {
/* 130 */     this.clientid = clientid;
/*     */   }
/*     */   
/*     */   public Config(Integer clientid, String masterurl, short remoteDisableLogin, short liveMode, short pingInterval, short enabledInd, short clientFscy, String ldapserver, String ldapsurl) {
/* 134 */     this.clientid = clientid;
/* 135 */     this.masterurl = masterurl;
/* 136 */     this.remoteDisableLogin = remoteDisableLogin;
/* 137 */     this.liveMode = liveMode;
/* 138 */     this.pingInterval = pingInterval;
/* 139 */     this.enabledInd = enabledInd;
/* 140 */     this.clientFscy = clientFscy;
/* 141 */     this.ldapserver = ldapserver;
/* 142 */     this.ldapsurl = ldapsurl;
/*     */   }
/*     */   
/*     */   public String getMastername() {
/* 146 */     return this.mastername;
/*     */   }
/*     */   
/*     */   public void setMastername(String mastername) {
/* 150 */     this.mastername = mastername;
/*     */   }
/*     */   
/*     */   public String getMasteripv4() {
/* 154 */     return this.masteripv4;
/*     */   }
/*     */   
/*     */   public void setMasteripv4(String masteripv4) {
/* 158 */     this.masteripv4 = masteripv4;
/*     */   }
/*     */   
/*     */   public String getMasterport() {
/* 162 */     return this.masterport;
/*     */   }
/*     */   
/*     */   public void setMasterport(String masterport) {
/* 166 */     this.masterport = masterport;
/*     */   }
/*     */   
/*     */   public String getMasterurl() {
/* 170 */     return this.masterurl;
/*     */   }
/*     */   
/*     */   public void setMasterurl(String masterurl) {
/* 174 */     this.masterurl = masterurl;
/*     */   }
/*     */   
/*     */   public short getRemoteDisableLogin() {
/* 178 */     return this.remoteDisableLogin;
/*     */   }
/*     */   
/*     */   public void setRemoteDisableLogin(short remoteDisableLogin) {
/* 182 */     this.remoteDisableLogin = remoteDisableLogin;
/*     */   }
/*     */   
/*     */   public short getLiveMode() {
/* 186 */     return this.liveMode;
/*     */   }
/*     */   
/*     */   public void setLiveMode(short liveMode) {
/* 190 */     this.liveMode = liveMode;
/*     */   }
/*     */   
/*     */   public short getPingInterval() {
/* 194 */     return this.pingInterval;
/*     */   }
/*     */   
/*     */   public void setPingInterval(short pingInterval) {
/* 198 */     this.pingInterval = pingInterval;
/*     */   }
/*     */   
/*     */   public short getEnabledInd() {
/* 202 */     return this.enabledInd;
/*     */   }
/*     */   
/*     */   public void setEnabledInd(short enabledInd) {
/* 206 */     this.enabledInd = enabledInd;
/*     */   }
/*     */   
/*     */   public short getClientFscy() {
/* 210 */     return this.clientFscy;
/*     */   }
/*     */   
/*     */   public void setClientFscy(short clientFscy) {
/* 214 */     this.clientFscy = clientFscy;
/*     */   }
/*     */   
/*     */   public String getClientVersion() {
/* 218 */     return this.clientVersion;
/*     */   }
/*     */   
/*     */   public void setClientVersion(String clientVersion) {
/* 222 */     this.clientVersion = clientVersion;
/*     */   }
/*     */   
/*     */   public Integer getClientid() {
/* 226 */     return this.clientid;
/*     */   }
/*     */   
/*     */   public void setClientid(Integer clientid) {
/* 230 */     this.clientid = clientid;
/*     */   }
/*     */   
/*     */   public String getLdapserver() {
/* 234 */     return this.ldapserver;
/*     */   }
/*     */   
/*     */   public void setLdapserver(String ldapserver) {
/* 238 */     this.ldapserver = ldapserver;
/*     */   }
/*     */   
/*     */   public String getLdapsurl() {
/* 242 */     return this.ldapsurl;
/*     */   }
/*     */   
/*     */   public void setLdapsurl(String ldapsurl) {
/* 246 */     this.ldapsurl = ldapsurl;
/*     */   }
/*     */   
/*     */   public String getMasterecho() {
/* 250 */     return this.masterecho;
/*     */   }
/*     */   
/*     */   public void setMasterecho(String masterecho) {
/* 254 */     this.masterecho = masterecho;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 259 */     int hash = 0;
/* 260 */     hash += (this.clientid != null) ? this.clientid.hashCode() : 0;
/* 261 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 267 */     if (!(object instanceof edu.lsu.estimator.Config)) {
/* 268 */       return false;
/*     */     }
/* 270 */     edu.lsu.estimator.Config other = (edu.lsu.estimator.Config)object;
/* 271 */     if ((this.clientid == null && other.clientid != null) || (this.clientid != null && !this.clientid.equals(other.clientid))) {
/* 272 */       return false;
/*     */     }
/* 274 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 279 */     return "edu.lsu.estimator.Config[ clientid=" + this.clientid + " ]";
/*     */   }
/*     */   
/*     */   public Integer getLdapport() {
/* 283 */     return this.ldapport;
/*     */   }
/*     */   
/*     */   public void setLdapport(Integer ldapport) {
/* 287 */     this.ldapport = ldapport;
/*     */   }
/*     */   
/*     */   public String getMasterVersion() {
/* 291 */     return this.masterVersion;
/*     */   }
/*     */   
/*     */   public void setMasterVersion(String masterVersion) {
/* 295 */     this.masterVersion = masterVersion;
/*     */   }
/*     */   
/*     */   public short getPollInterval() {
/* 299 */     return this.pollInterval;
/*     */   }
/*     */   
/*     */   public void setPollInterval(short pollInterval) {
/* 303 */     this.pollInterval = pollInterval;
/*     */   }
/*     */   
/*     */   public Integer getUpgReqInd() {
/* 307 */     return this.upgReqInd;
/*     */   }
/*     */   
/*     */   public void setUpgReqInd(Integer upgReqInd) {
/* 311 */     this.upgReqInd = upgReqInd;
/*     */   }
/*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\Config.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */