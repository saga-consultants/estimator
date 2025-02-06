/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

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
@Table(name = "VERSIONS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Version.findAll", query = "SELECT v FROM Version v"),
    @NamedQuery(name = "Version.findByModule", query = "SELECT v FROM Version v WHERE v.module = :module"),
    @NamedQuery(name = "Version.findByVersion", query = "SELECT v FROM Version v WHERE v.version = :version"),
    @NamedQuery(name = "Version.findByDos", query = "SELECT v FROM Version v WHERE v.dos = :dos"),
    @NamedQuery(name = "Version.findBySrcTime", query = "SELECT v FROM Version v WHERE v.srcTime = :srcTime"),
    @NamedQuery(name = "Version.findBySrcTz", query = "SELECT v FROM Version v WHERE v.srcTz = :srcTz"),
    @NamedQuery(name = "Version.findBySrcWho", query = "SELECT v FROM Version v WHERE v.srcWho = :srcWho"),
    @NamedQuery(name = "Version.findByEffInd", query = "SELECT v FROM Version v WHERE v.effInd = :effInd"),
    @NamedQuery(name = "Version.findByRecid", query = "SELECT v FROM Version v WHERE v.recid = :recid")})
public class Version implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "RECID")    
    @TableGenerator(name = "VERSEQ_Gen", table = "SEQUENCE", pkColumnName="NAME",valueColumnName="ID", pkColumnValue="VER_GEN", initialValue = 1, allocationSize = 1 )    
    @GeneratedValue(strategy = GenerationType.TABLE, generator="VERSEQ_Gen")
    private Integer recid;
    
    @Size(max = 30)
    @Column(name = "MODULE")
    private String module;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VERSION")
    private int version;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "DOS")
    private long dos;
    
    @Column(name = "DOSTZ")
    @Size(max = 20)
    private String dostz;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "SRC_TIME")
    private long srcTime;
    @Size(max = 20)
    @Column(name = "SRC_TZ")
    private String srcTz;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SRC_WHO")
    private int srcWho;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EFF_IND")
    private int effInd;

    public Version() {
    }

    public Version(Integer recid) {
        this.recid = recid;
    }

    public Version(Integer recid, int version, long dos, long srcTime, int srcWho, int effInd) {
        this.recid = recid;
        this.version = version;
        this.dos = dos;
        this.srcTime = srcTime;
        this.srcWho = srcWho;
        this.effInd = effInd;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getDos() {
        return dos;
    }

    public void setDos(long dos) {
        this.dos = dos;
    }

    public long getSrcTime() {
        return srcTime;
    }

    public void setSrcTime(long srcTime) {
        this.srcTime = srcTime;
    }

    public String getSrcTz() {
        return srcTz;
    }

    public void setSrcTz(String srcTz) {
        this.srcTz = srcTz;
    }

    public int getSrcWho() {
        return srcWho;
    }

    public void setSrcWho(int srcWho) {
        this.srcWho = srcWho;
    }

    public int getEffInd() {
        return effInd;
    }

    public void setEffInd(int effInd) {
        this.effInd = effInd;
    }

    public Integer getRecid() {
        return recid;
    }

    public void setRecid(Integer recid) {
        this.recid = recid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recid != null ? recid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Version)) {
            return false;
        }
        Version other = (Version) object;
        if ((this.recid == null && other.recid != null) || (this.recid != null && !this.recid.equals(other.recid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.lsu.estimator.Version[ recid=" + recid + " ]";
    }

    public String getDostz() {
        return dostz;
    }

    public void setDostz(String dostz) {
        this.dostz = dostz;
    }
    
}
