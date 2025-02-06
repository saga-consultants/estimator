/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author kwang
 */
@Entity
@Table(name = "FUNDS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fund.findAll", query = "SELECT f FROM Fund f"),
    @NamedQuery(name = "Fund.findByFundCode", query = "SELECT f FROM Fund f WHERE f.fundCode = :fundCode"),
    @NamedQuery(name = "Fund.findByFundDesc", query = "SELECT f FROM Fund f WHERE f.fundDesc = :fundDesc"),
    @NamedQuery(name = "Fund.findByReqNoteInd", query = "SELECT f FROM Fund f WHERE f.reqNoteInd = :reqNoteInd"),
    @NamedQuery(name = "Fund.findByHasMatching", query = "SELECT f FROM Fund f WHERE f.hasMatching = :hasMatching"),
    @NamedQuery(name = "Fund.findByMatchPerc", query = "SELECT f FROM Fund f WHERE f.matchPerc = :matchPerc"),
    @NamedQuery(name = "Fund.findByMatchTop", query = "SELECT f FROM Fund f WHERE f.matchTop = :matchTop"),
    @NamedQuery(name = "Fund.findByInstCapExcept", query = "SELECT f FROM Fund f WHERE f.instCapExcept = :instCapExcept"),
    @NamedQuery(name = "Fund.findByPriority", query = "SELECT f FROM Fund f WHERE f.priority = :priority"),
  @NamedQuery(name = "Fund.findByStatus", query = "SELECT f FROM Fund f WHERE f.status = :status order by f.fundDesc"),
    @NamedQuery(name = "Fund.findByCreator", query = "SELECT f FROM Fund f WHERE f.creator = :creator"),
    @NamedQuery(name = "Fund.findByUpdator", query = "SELECT f FROM Fund f WHERE f.updator = :updator"),
    @NamedQuery(name = "Fund.findBySyncor", query = "SELECT f FROM Fund f WHERE f.syncor = :syncor"),
    @NamedQuery(name = "Fund.findByEarningsMatch", query = "SELECT f FROM Fund f WHERE f.earningsMatch = :earningsMatch"),
    
    @NamedQuery(name = "Fund.findByFundId", query = "SELECT f FROM Fund f WHERE f.fundId = :fundId")})
public class Fund implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "FUND_ID")
    private Integer fundId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "FUND_CODE")
    private String fundCode;
    
    @Size(max = 70)
    @Column(name = "FUND_DESC")
    private String fundDesc;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "REQ_NOTE_IND")
    private String reqNoteInd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "HAS_MATCHING")
    private String hasMatching;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "MATCH_PERC")
    private BigDecimal matchPerc;
    @Column(name = "MATCH_TOP")
    private Integer matchTop;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "INST_CAP_EXCEPT")
    private String instCapExcept;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRIORITY")
    private int priority;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "STATUS")
    private String status;
    
    @Basic(optional = false)
    @NotNull
     
    @Column(name = "CREATOR")
    private Integer creator;
     
    @Column(name = "UPDATOR")
    private Integer updator;
     
    @Column(name = "SYNCOR")
    private Integer syncor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "EARNINGS_MATCH")
    private String earningsMatch;
    
     
    @Column(name = "DOM")
    private long dom;
    @Column(name = "DOE")
    private long doe;
    @Size(max = 20)
    @Column(name = "DOETZ")
    private String doetz;
    @Size(max = 20)
    @Column(name = "DOMTZ")
    private String domtz;
    
    
    @Column(name = "DOS")
    private long dos;    
    @Size(max = 20)
    @Column(name = "DOSTZ")
    private String dostz;
     
    @NotNull
    @Min(0)
    @Max(1)
    @Column( name="AUTO_IND")//, columnDefinition="SMALLINT DEFAULT 0", nullable = false)  // insertable=false  will let JPA totally ignore this field when persisting
    private Integer auto_ind = 0;
    
    @NotNull
    @Min(-1)
    @Max(1)
    @Column( name="MAX_IND")//, columnDefinition="SMALLINT DEFAULT 0", nullable = false)
    private Integer max_ind = 0;
    
    //JDO allows it in its ORM definition, so JPA needs catch up.
    
    //columnDefinition,  you won't get the database default value after an insert in your entity, have to refresh to get value from table
    
    // ensure that default is set for non primitive types (setting null for example). Using @PrePersist and @PreUpdate
    //e.g.  
    /*
    @PrePersist
    void preInsert() {
       createdt = new Date();
    }
    */

    public Fund() {
    }

    public Fund(Integer fundId) {
        this.fundId = fundId;
    }

    public Fund(Integer fundId, String fundCode, String reqNoteInd, String hasMatching, String instCapExcept, int priority, String status, Integer creator, String earningsMatch, long doe) {
        this.fundId = fundId;
        this.fundCode = fundCode;
        this.reqNoteInd = reqNoteInd;
        this.hasMatching = hasMatching;
        this.instCapExcept = instCapExcept;
        this.priority = priority;
        this.status = status;
        this.creator = creator;
        this.earningsMatch = earningsMatch;
        this.doe = doe;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundDesc() {
        return fundDesc;
    }

    public void setFundDesc(String fundDesc) {
        this.fundDesc = fundDesc;
    }

    public String getReqNoteInd() {
        return reqNoteInd;
    }

    public void setReqNoteInd(String reqNoteInd) {
        this.reqNoteInd = reqNoteInd;
    }

    public String getHasMatching() {
        return hasMatching;
    }

    public void setHasMatching(String hasMatching) {
        this.hasMatching = hasMatching;
    }

    public BigDecimal getMatchPerc() {
        return matchPerc;
    }

    public void setMatchPerc(BigDecimal matchPerc) {
        this.matchPerc = matchPerc;
    }

    public Integer getMatchTop() {
        return matchTop;
    }

    public void setMatchTop(Integer matchTop) {
        this.matchTop = matchTop;
    }

    public String getInstCapExcept() {
        return instCapExcept;
    }

    public void setInstCapExcept(String instCapExcept) {
        this.instCapExcept = instCapExcept;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Integer getUpdator() {
        return updator;
    }

    public void setUpdator(Integer updator) {
        this.updator = updator;
    }

    public Integer getSyncor() {
        return syncor;
    }

    public void setSyncor(Integer syncor) {
        this.syncor = syncor;
    }

    public String getEarningsMatch() {
        return earningsMatch;
    }

    public void setEarningsMatch(String earningsMatch) {
        this.earningsMatch = earningsMatch;
    }



    public Integer getFundId() {
        return fundId;
    }

    public void setFundId(Integer fundId) {
        this.fundId = fundId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fundId != null ? fundId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fund)) {
            return false;
        }
        Fund other = (Fund) object;
        if ((this.fundId == null && other.fundId != null) || (this.fundId != null && !this.fundId.equals(other.fundId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.lsu.estimator.Fund[ fundId=" + fundId + " ]";
    }

    public long getDom() {
        return dom;
    }

    public void setDom(long dom) {
        this.dom = dom;
    }

    public long getDoe() {
        return doe;
    }

    public void setDoe(long doe) {
        this.doe = doe;
    }

    public String getDoetz() {
        return doetz;
    }

    public void setDoetz(String doetz) {
        this.doetz = doetz;
    }

    public String getDomtz() {
        return domtz;
    }

    public void setDomtz(String domtz) {
        this.domtz = domtz;
    }

    public long getDos() {
        return dos;
    }

    public void setDos(long dos) {
        this.dos = dos;
    }

    public String getDostz() {
        return dostz;
    }

    public void setDostz(String dostz) {
        this.dostz = dostz;
    }

    public Integer getAuto_ind() {
        return auto_ind;
    }

    public void setAuto_ind(Integer auto_ind) {
        this.auto_ind = auto_ind;
    }

    public Integer getMax_ind() {
        return max_ind;
    }

    public void setMax_ind(Integer max_ind) {
        this.max_ind = max_ind;
    }
    
}
