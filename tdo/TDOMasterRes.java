/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.tdo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kwang
 */
public class TDOMasterRes<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    //In Java, generics are compile-time only data, which are lost at run time.
    public TDOMasterRes(){}
    
    //public Map<String, String> map; //code|msg, reason.  
    public ArrayList<String> msgs;    
    public int code; //  e.g: -1: need initialization 0:ok  1: exception  >1: not valid
    
    public String masterver;
    public Integer objsversion;
    public long objs_masterdoe;
    public String objs_masterdoetz;
    public Integer objs_masterupdator;
    
    //2012-01-18  to let client user master time to mark data's dou and dos ( dod shall be done by master)
    public long masterclock;
    public String mastertz;
    
    public int remote_clock_ind=0; //1: error  -1: warning   
    public int remote_stop_sys_ind =0; //code >0, and if inactive, or is blocked
    public int remote_stop_user_ind =0; //code >0, and if user is inactive, or is blocked
    
    public byte[] aesData;  //used when initialize client, holding its client key, enceypted by client's AES key
    public int clientInitNumb;////used when initialize client, holding its new and init clientid ( can be encrypted as byte[])
    
    public   List<T> objs; //can be funds, counselors, costs,    students and prints
    
    public   List<TDOStudent> obj2s; //special for sync new prints, to hold studs
    //private Class<T> returnedClass;
    /*
     public Class returnedClass() {
      return returnedClass;
    }
     */
}
