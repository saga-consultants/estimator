/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.tdo;
 

import java.io.Serializable;
import java.util.Date;
 

/**
 *
 * @author kwang
 */
public class TDOCounselor implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public TDOCounselor(){}
    
    public String username;     
    public Boolean superuser;
    
    public Integer userid;
    
    public String dept;
    public String lsuid;
        
    public String email;
    public int status;
    
    public long creator;
    public long editor;    
   
    public long doe;    
    public String doetz; 
    public long dom;
    public String domtz;
           
    public long dos; 
    public String dostz;
    
    public Date showDate;
    
    public Integer stdInd;
    /*
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Counselor)) {
            return false;
        }
        Counselor other = (Counselor) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.lsu.estimator.Counselor[ userid=" + userid + " ]";
    }
    */
}
