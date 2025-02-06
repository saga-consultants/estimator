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
import java.util.Date;

/**
 *
 * @author kwang
 */
@Entity
@Table(name = "LOGS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Logs.findAll", query = "SELECT l FROM Logs l"),
    @NamedQuery(name = "Logs.findByType", query = "SELECT l FROM Logs l WHERE l.type = :type"),
    @NamedQuery(name = "Logs.findByCat", query = "SELECT l FROM Logs l WHERE l.cat = :cat"),
    @NamedQuery(name = "Logs.findByLocation", query = "SELECT l FROM Logs l WHERE l.location = :location"),
    @NamedQuery(name = "Logs.findByWhattime", query = "SELECT l FROM Logs l WHERE l.whattime = :whattime"),
    @NamedQuery(name = "Logs.findByWho", query = "SELECT l FROM Logs l WHERE l.who = :who"),
    @NamedQuery(name = "Logs.findByWhat", query = "SELECT l FROM Logs l WHERE l.what = :what"),
    @NamedQuery(name = "Logs.findByResult", query = "SELECT l FROM Logs l WHERE l.result = :result")})
public class Logs implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "TYPE")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CAT")
    private String cat;
    @Size(max = 250)
    @Column(name = "LOCATION")
    private String location;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "WHATTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date whattime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "WHO")
    private String who;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "WHAT")
    private String what;
    @Size(max = 80)
    @Column(name = "RESULT")
    private String result;

    public Logs() {
    }

    public Logs(Date whattime) {
        this.whattime = whattime;
    }

    public Logs(Date whattime, String type, String cat, String who, String what) {
        this.whattime = whattime;
        this.type = type;
        this.cat = cat;
        this.who = who;
        this.what = what;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getWhattime() {
        return whattime;
    }

    public void setWhattime(Date whattime) {
        this.whattime = whattime;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (whattime != null ? whattime.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Logs)) {
            return false;
        }
        Logs other = (Logs) object;
        if ((this.whattime == null && other.whattime != null) || (this.whattime != null && !this.whattime.equals(other.whattime))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.lsu.estimator.Logs[ whattime=" + whattime + " ]";
    }
    
}
