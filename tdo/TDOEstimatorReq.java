/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.tdo;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author kwang
 */
public class TDOEstimatorReq implements Serializable { //TDOEstimatorReq
    private static final long serialVersionUID = 1L;
    
    public TDOEstimatorReq(){}
    
    public Map<String, String> c_info; // c_last_download, c_last_downloaded_count?
    
    public SecretKey c_key;     
    public byte[] aesData; //clientkey    
    
    public int fisy;
    public int clientid;
    public int counselorid;
    public long clientclock;
    public String clienttz;
    
    public String clientversions; //server will check if new version exists, and if the client can continue
    
    public long last_stud_dod; //from server, stored in updownload table , and the max (ddou) and successful one, including cnt==0 records // all pickup_ind==1 or prints>0
    //pickup_ind==1 and clientid<>id and dou > stud_dod    
    
    public long last_prt_dod;  //from server, used by server to filter new prints from other clients since last download   
    //clientid<>id and dou > stud_dod 
    
    public String clientaddress;
    public String clientjava;
    public String clienthostname;  
}
