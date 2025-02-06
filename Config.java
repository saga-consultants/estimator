/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author kwang
 */
@Entity
@Table(name = "CONFIG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Config.findAll", query = "SELECT c FROM Config c"),
    @NamedQuery(name = "Config.findByMastername", query = "SELECT c FROM Config c WHERE c.mastername = :mastername"),
    @NamedQuery(name = "Config.findByMasteripv4", query = "SELECT c FROM Config c WHERE c.masteripv4 = :masteripv4"),
    @NamedQuery(name = "Config.findByMasterport", query = "SELECT c FROM Config c WHERE c.masterport = :masterport"),
    @NamedQuery(name = "Config.findByMasterurl", query = "SELECT c FROM Config c WHERE c.masterurl = :masterurl"),
    @NamedQuery(name = "Config.findByRemoteDisableLogin", query = "SELECT c FROM Config c WHERE c.remoteDisableLogin = :remoteDisableLogin"),
    @NamedQuery(name = "Config.findByLiveMode", query = "SELECT c FROM Config c WHERE c.liveMode = :liveMode"),
    @NamedQuery(name = "Config.findByPingInterval", query = "SELECT c FROM Config c WHERE c.pingInterval = :pingInterval"),
    @NamedQuery(name = "Config.findByEnabledInd", query = "SELECT c FROM Config c WHERE c.enabledInd = :enabledInd"),
    @NamedQuery(name = "Config.findByClientFscy", query = "SELECT c FROM Config c WHERE c.clientFscy = :clientFscy"),
    @NamedQuery(name = "Config.findByClientVersion", query = "SELECT c FROM Config c WHERE c.clientVersion = :clientVersion"),
    @NamedQuery(name = "Config.findByClientid", query = "SELECT c FROM Config c WHERE c.clientid = :clientid"),
    @NamedQuery(name = "Config.findByLdapserver", query = "SELECT c FROM Config c WHERE c.ldapserver = :ldapserver"),
    @NamedQuery(name = "Config.findByLdapsurl", query = "SELECT c FROM Config c WHERE c.ldapsurl = :ldapsurl"),
    @NamedQuery(name = "Config.findByMasterecho", query = "SELECT c FROM Config c WHERE c.masterecho = :masterecho")})
public class Config implements Serializable {
    private static final long serialVersionUID = 1L;
    @Size(max = 128)
    @Column(name = "MASTERNAME")
    private String mastername;
    @Size(max = 15)
    @Column(name = "MASTERIPV4")
    private String masteripv4;
    @Size(max = 5)
    @Column(name = "MASTERPORT")
    private String masterport;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "MASTERURL")
    private String masterurl;
    @Basic(optional = false)
    @NotNull
    @Column(name = "REMOTE_DISABLE_LOGIN")
    private short remoteDisableLogin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LIVE_MODE")
    private short liveMode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PING_INTERVAL")
    private short pingInterval;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ENABLED_IND")
    private short enabledInd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CLIENT_FSCY")
    private short clientFscy;
    
    @Size(max = 20)
    @Column(name = "CLIENT_VERSION")
    private String clientVersion;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CLIENTID")
    private Integer clientid;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "LDAPSERVER")
    private String ldapserver;
    
    @NotNull
    @Range(min=80, max=65000)
    @Column(name = "LDAPPORT")
    private Integer ldapport;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "LDAPSURL")
    private String ldapsurl;
    
    @Size(max = 256)
    @Column(name = "MASTERECHO")
    private String masterecho;
    
    //2012-01-18
    @Size(max = 20)
    @Column(name = "MASTER_VERSION")
    private String masterVersion;
    
    //@NotNull
    @Column(name = "POLL_INTERVAL")
    private short pollInterval;
     
    //@NotNull
    @Range(min=0)
    @Column(name = "UPG_VER_IND")
    private Integer upgReqInd;
    
/*    
DOCHK
DOCHKTZ
 */

    
    public Config() {
    }

    public Config(Integer clientid) {
        this.clientid = clientid;
    }

    public Config(Integer clientid, String masterurl, short remoteDisableLogin, short liveMode, short pingInterval, short enabledInd, short clientFscy, String ldapserver, String ldapsurl) {
        this.clientid = clientid;
        this.masterurl = masterurl;
        this.remoteDisableLogin = remoteDisableLogin;
        this.liveMode = liveMode;
        this.pingInterval = pingInterval;
        this.enabledInd = enabledInd;
        this.clientFscy = clientFscy;
        this.ldapserver = ldapserver;
        this.ldapsurl = ldapsurl;
    }

    public String getMastername() {
        return mastername;
    }

    public void setMastername(String mastername) {
        this.mastername = mastername;
    }

    public String getMasteripv4() {
        return masteripv4;
    }

    public void setMasteripv4(String masteripv4) {
        this.masteripv4 = masteripv4;
    }

    public String getMasterport() {
        return masterport;
    }

    public void setMasterport(String masterport) {
        this.masterport = masterport;
    }

    public String getMasterurl() {
        return masterurl;
    }

    public void setMasterurl(String masterurl) {
        this.masterurl = masterurl;
    }

    public short getRemoteDisableLogin() {
        return remoteDisableLogin;
    }

    public void setRemoteDisableLogin(short remoteDisableLogin) {
        this.remoteDisableLogin = remoteDisableLogin;
    }

    public short getLiveMode() {
        return liveMode;
    }

    public void setLiveMode(short liveMode) {
        this.liveMode = liveMode;
    }

    public short getPingInterval() {
        return pingInterval;
    }

    public void setPingInterval(short pingInterval) {
        this.pingInterval = pingInterval;
    }

    public short getEnabledInd() {
        return enabledInd;
    }

    public void setEnabledInd(short enabledInd) {
        this.enabledInd = enabledInd;
    }

    public short getClientFscy() {
        return clientFscy;
    }

    public void setClientFscy(short clientFscy) {
        this.clientFscy = clientFscy;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public Integer getClientid() {
        return clientid;
    }

    public void setClientid(Integer clientid) {
        this.clientid = clientid;
    }

    public String getLdapserver() {
        return ldapserver;
    }

    public void setLdapserver(String ldapserver) {
        this.ldapserver = ldapserver;
    }

    public String getLdapsurl() {
        return ldapsurl;
    }

    public void setLdapsurl(String ldapsurl) {
        this.ldapsurl = ldapsurl;
    }

    public String getMasterecho() {
        return masterecho;
    }

    public void setMasterecho(String masterecho) {
        this.masterecho = masterecho;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientid != null ? clientid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Config)) {
            return false;
        }
        Config other = (Config) object;
        if ((this.clientid == null && other.clientid != null) || (this.clientid != null && !this.clientid.equals(other.clientid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.lsu.estimator.Config[ clientid=" + clientid + " ]";
    }

    public Integer getLdapport() {
        return ldapport;
    }

    public void setLdapport(Integer ldapport) {
        this.ldapport = ldapport;
    }

    public String getMasterVersion() {
        return masterVersion;
    }

    public void setMasterVersion(String masterVersion) {
        this.masterVersion = masterVersion;
    }

    public short getPollInterval() {
        return pollInterval;
    }

    public void setPollInterval(short pollInterval) {
        this.pollInterval = pollInterval;
    }

    public Integer getUpgReqInd() {
        return upgReqInd;
    }

    public void setUpgReqInd(Integer upgReqInd) {
        this.upgReqInd = upgReqInd;
    }
    
}
