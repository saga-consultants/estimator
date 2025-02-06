/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.tdo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;

/**
 *
 * @author kwang
 * ######################################### client should ensure only one exe unit is running at one time (synchronized or executor or global/appscoped indicator? )###########################
 */
public interface SPIEstimatorMaster {
    
    //client pack its info(fisy, clientid[0=to init], counselorid[0=sys task], clientkey[null=to-init or wrong client], client_versions, clienttime+tz, last_time_download, last_time_download_count) into c_info, 
    //send to server, and get response("", "", "time diff"  ) in another map obj
    
    //-------------- regular routine check (validation, and new changes )-----------
    public TDOMasterRes<Object> check( TDOEstimatorReq req, Boolean... fromClientInd); ////all request will go through this first, or directly by the client
    //if it is regular req from client, then after passing all checks, do:
    //get TDOEstimatorReq (clientversions+counselor_ver+ last_stud_dod+ last_prt_dod)  Map<String, String> c_info:  
    //send TDOMasterRes<T> ArrayList<String> msgs ---> new_xxx_rule ===versionabc  new_stud_amt==x  new_prt_amt==y )
    
    
    //----------------- init, download all rules ----------------------------------
    
    public TDOMasterRes<TDOCounselor> init( TDOEstimatorReq req); //returns all active counselors for init
    public TDOMasterRes<TDOFund> getInitFunds(TDOEstimatorReq req); //returns all active funds
    
    public TDOMasterRes<TDOFund> getAllFunds( TDOEstimatorReq req); //ALL
    public TDOMasterRes<TDOCounselor> getAllCounselors( TDOEstimatorReq req);//ALL
    
    //----------------- init, download all studs and prints-----------------
    //########### reuse the normal stud/prt upload download funct, but the uploaded set is null, and last_dou=0    
    
    
    
    
    
    //------------------- sync, if new rules exist (verion or comp dos) ----------
    //master versions(mod ver doe eff recid) if( doe>last_dod and eff>0) get the max ver numb
    
    //goto counselors or funds table, get all dom>last_dod as delta, mark dod in the obj, and put into res to return
    //INIT funds and counselor has master set DOS time.
    public TDOMasterRes<TDOFund> getNewFunds(TDOEstimatorReq req, long last_fund_dos);
    
    
    public TDOMasterRes<TDOCounselor> getNewCounselors( TDOEstimatorReq req, long last_counselor_dos);
    
    //VERSIONS are only for (counselors, funds, awards, and cost)
    
    
    //-------------------client DATA: UPLOAD FIRST, THEN DOWNLOAD--------------------------------
    //collect all three sets at once with timestamp. upload student, who is new and picked up and dou==0, set dou/tz, 
    //download loosers of them(update dou/pickupind to client db), and winners from other clients (set dodown to db)
    public TDOMasterRes<TDOStudent> syncNewPickedStuds(TDOEstimatorReq req, List<TDOStudent> c_students);    
    //upload printed non-pickup stud (pick=0, dou=0, prts>0), which has prints (>=1)
    public TDOMasterRes<Object> uploadNewPrtsUnpickedStuds(TDOEstimatorReq req, List<TDOStudent> c_counselors);
    //upload prints (both sets of studs), which has FK to students, so loaded after students. set (dou/tz, last last_dou) before upload(even null), download those dou>=last_dod and diff client (both prints and studs), 
    //and set studs(with dou) and prints (both sets) with dou to db
    public TDOMasterRes<TDOPrint> syncNewPrts(TDOEstimatorReq req, List<TDOPrint> c_counselors);
   
    
    //download stud
    //public List<CounselorTDO> downloadStuds(TDOEstimatorReq req, List<CounselorTDO> c_counselors);
    
    //download prints
    //public List<CounselorTDO> downloadPrints(TDOEstimatorReq req, List<CounselorTDO> c_counselors);
    
    //download prints
    //public List<CounselorTDO> downloadPrintsStuds(TDOEstimatorReq req, List<CounselorTDO> c_counselors);
    
    
    //test to show the ability of uploading and downloading
    public TDOMasterRes test(TDOEstimatorReq req, List<TDOCounselor> c_counselors); //List<TDOCounselor>
    
    
}