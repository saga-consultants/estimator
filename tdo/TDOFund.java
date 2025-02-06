/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.tdo;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 *
 * @author kwang
 */
public class TDOFund implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public TDOFund() {    }
    
    public Integer fundId;
     
    public String fundCode;
     
    public String fundDesc;
     
    public String reqNoteInd;
     
    public String hasMatching;
     
    public BigDecimal matchPerc;
     
    public Integer matchTop;
     
    public String instCapExcept;
     
    public int priority;
     
    public String status;
     
    public Integer creator;     
    public Integer updator;    
    public Integer syncor;
    
    
    public long doe;    
    public String doetz; 
    public long dom;
    public String domtz;
           
    public long dos; 
    public String dostz;
    
    public Integer auto_ind;     
    public Integer max_ind;
    
    public String earningsMatch;
      
}

    
    
/*     
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
    */
 
