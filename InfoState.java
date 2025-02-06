/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
//import org.apache.log4j.Logger;

/**
 *
 * @author kwang
 */
@Named("info")
@SessionScoped 
public class InfoState implements Serializable{ //keep settings for the current user's session
    private static final long serialVersionUID = 1L;
    
    private int newStudsOfUser = 0;
    private int newStudsAccmOfAll = 0; //several counselors may share/use this PC
    private int studsOfUser=0;
    private int totStuds = 0;
    private int amtUserPCs = 0;//the current user may used other PCs and sync with the master
    
    private int usersNumb = 0;
    private int otherCounselorNumb = 0;
    private int otherCounselorStuds = 0;
            
    private Date lastSyncTime;
    private Date firstUseDate; //maybe first time login, or first new estimation
    private Date staleSinceDate;
    
//    private Date lastUploadcTime;
//    private Date lastDownloadTime;
//    private Date lastPingDate; //ping or check master server
    private int masterLive = 0; //if master is available
    private int masterReady = 0;
    
    //04/10/2012
    private int nonprts = 0;
    private int upprts=0;
    
    @PersistenceContext    private EntityManager em;
    @Inject Login login;
    
    @Inject Accessor accessor;
    
    //@Inject transient  FormatLogger log;
 /////    private transient  FormatLogger log = new FormatLogger (Logger.getLogger(this.getClass().getName()));
     private  static final Logger log = LoggerFactory.getLogger();
   
     @Inject AppReference ref;
     //private int fisy=2013;
     
    public InfoState(){/*
        SimpleDateFormat d = new java.text.SimpleDateFormat("yyyy") ;
        String strFISY = d.format(new Date());
        int iFISY = Integer.parseInt(strFISY);
        if(fisy < iFISY ){
            fisy = iFISY;            
        }*/
    }
    
    //@PostConstruct
    public void sumUp(){ //query database data table and logs and this login, to popular fields and set values
        //s.counselorId
        //"select u from Logs u where u.type='ESTIMATE' and u.cat='NEWSTUD' and u.who = :userid  and u.what='SAVED'  and u.result = 'ok'")
        //############ java.lang.NullPointerException 	at edu.lsu.estimator.Logs_.<clinit>(Logs_.java)
        /*
        List<Student> results = em.createQuery("SELECT s FROM Student s WHERE s.counselorId =:userid and s.pickupInd=1")        
            .setParameter("userid", login.getCurrentUser().getUserid())      
            .getResultList();
        if( results!=null) 
            this.studsOfUser = results.size();
        else
            this.studsOfUser = 0;         
        */
        //can use native query to get the count(*) directly
        this.studsOfUser = (Integer)em.createNativeQuery("select count(*) from student s where s.student_fisy=? and s.counselor_id=? and s.pickup_ind =1  ")
                .setParameter(1, ref.getFiscal_year())
                .setParameter(2, login.getCurrentUser().getUserid()) //only query or named query(which uses OBJ, not table) use parameter name like :userid, and set parameter by name                
                .getSingleResult();
        
        
        List<Date> lastsyncs = em.createNativeQuery("select max(l.whattime) from Logs l where  l.type='SYNC' and l.cat='UPLOAD' and l.result='ok' ").getResultList();
        List<Date> lastsignins = em.createNativeQuery("select min(l.whattime) from Logs l where l.type='SESSION' and l.cat='SIGNIN' and l.result='ok' ").getResultList();
        if( lastsyncs!=null && lastsyncs.size()>0    && lastsyncs.get(0)!=null){ // DERBY query returns null, but adding a max/min, it will return one record of null.
            this.lastSyncTime = lastsyncs.get(0); 
 //           log.info(" === got last sync time %s", lastSyncTime);
        }else if(lastsignins!=null && lastsignins.size()>0   && lastsignins.get(0)!=null){
            this.lastSyncTime = lastsignins.get(0);
 //            log.info(" === got last signin time %s", lastSyncTime);
        }else{
            this.lastSyncTime = new Date();
 //            log.info(" === got current time %s", lastSyncTime);
        }
        
        //or s.student_bz_uploaded='no'  //.setParameter(1, login.getCurrentUser().getUserid())
        int newstuds = (Integer)em.createNativeQuery("select count(*) from student s where s.student_fisy=? and s.pickup_ind=1 and  s.dup=0")
                .setParameter(1,  ref.getFiscal_year())
                .getSingleResult();
        this.newStudsOfUser = newstuds;
        
        int others = (Integer)em.createNativeQuery("select count(*) from (select distinct s.counselor_id from student s where  s.student_fisy=? and s.counselor_id<>? and s.pickup_ind=1  )x")
                .setParameter(1, ref.getFiscal_year())
                .setParameter(2, login.getCurrentUser().getUserid())                
                .getSingleResult(); 
        this.otherCounselorNumb = others;
        
        int otherstuds = (Integer)em.createNativeQuery("select count(*) from student s where  s.student_fisy=? and  s.counselor_id<>? and s.pickup_ind=1  ") 
                .setParameter(1, ref.getFiscal_year())
                .setParameter(2, login.getCurrentUser().getUserid() )                
                .getSingleResult();
        this.otherCounselorStuds = otherstuds;
        
        this.totStuds = studsOfUser + otherCounselorStuds;              
        
        
        //04/10/2012
        this.nonprts = (Integer)em.createNativeQuery("select count(a.recid) from student a where a.student_fisy=? and a.pickup_ind=1 and a.lost_time=0 and not exists ( select '1' from prints b where b.recid = a.recid)")
                .setParameter(1, ref.getFiscal_year()) 
                .getSingleResult();        
                
        this.upprts = (Integer)em.createNativeQuery("select count(a.recid) from student a where a.student_fisy=? and  a.dup>0 and not exists ( select '1' from prints b where b.recid = a.recid)")
                .setParameter(1, ref.getFiscal_year())
                .getSingleResult();
        //derby trigger to update tempid to real one sometimes fails
        //javax.persistence.TransactionRequiredException: executeUpdate is not supported for a Query object obtained through non-transactional access of a container-managed transactional EntityManager
        //em.createNativeQuery(" update prints set prt_id= RTRIM(char(counselor_id))||'.'||RTRIM(char(client_id))||'.'||RTRIM(char(PRT_NUM)) where prt_id='tmpid'").executeUpdate();
        accessor.triggerTmpidPrt();
    }
    
    
    public Integer getNowStudNumbInQueue(){
        int res=0;
        res = (Integer)em.createNativeQuery("select id from sequence where name='STUD_GEN'").getSingleResult();
        return res;
    }
    public String getStudRecid(Integer counselorid, Integer clientid, Integer seqnumb){
        String res="";
        res = (String)em.createNativeQuery("select recid from student where counselor_id=? and client_id=? and student_numb=?")
                .setParameter(1, counselorid)
                .setParameter(2, clientid)
                .setParameter(3, seqnumb)
                .getSingleResult();
        return res;
    }
    
    public Integer getNowPrtNumbInQueue(){
        int res=0;
        res = (Integer)em.createNativeQuery("select id from sequence where name='PRT_GEN'").getSingleResult();
        return res;
    }
    public String getPrintPrtid(Integer counselorid, Integer clientid, Integer seqnumb){
        String res="";
        res = (String)em.createNativeQuery("select prt_id from prints p where  p.counselor_id=? and p.client_id=? and p.prt_num=?")
                .setParameter(1, counselorid)
                .setParameter(2, clientid)
                .setParameter(3, seqnumb)
                .getSingleResult();
        return res;
    }
    
    public int getNewStudsOfUser() {
        return newStudsOfUser;
    }

    public void setNewStudsOfUser(int newStudsOfUser) {
        this.newStudsOfUser = newStudsOfUser;
    }

    public int getNewStudsAccmOfAll() {
        return newStudsAccmOfAll;
    }

    public void setNewStudsAccmOfAll(int newStudsAccmOfAll) {
        this.newStudsAccmOfAll = newStudsAccmOfAll;
    }

    public int getUsersNumb() {
        return usersNumb;
    }

    public void setUsersNumb(int usersNumb) {
        this.usersNumb = usersNumb;
    }

    public Date getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(Date lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public Date getFirstUseDate() {
        return firstUseDate;
    }

    public void setFirstUseDate(Date firstUseDate) {
        this.firstUseDate = firstUseDate;
    }

    public Date getStaleSinceDate() {
        return staleSinceDate;
    }

    public void setStaleSinceDate(Date staleSinceDate) {
        this.staleSinceDate = staleSinceDate;
    }
/*
    public Date getLastUploadcTime() {
        return lastUploadcTime;
    }

    public void setLastUploadcTime(Date lastUploadcTime) {
        this.lastUploadcTime = lastUploadcTime;
    }

    public Date getLastDownloadTime() {
        return lastDownloadTime;
    }

    public void setLastDownloadTime(Date lastDownloadTime) {
        this.lastDownloadTime = lastDownloadTime;
    }

    public Date getLastPingDate() {
        return lastPingDate;
    }

    public void setLastPingDate(Date lastPingDate) {
        this.lastPingDate = lastPingDate;
    }
*/
    public int getMasterLive() {
        return masterLive;
    }

    public void setMasterLive(int masterLive) {
        this.masterLive = masterLive;
    }

    public int getMasterReady() {
        return masterReady;
    }

    public void setMasterReady(int masterReady) {
        this.masterReady = masterReady;
    }

    public int getOtherCounselorNumb() {
        return otherCounselorNumb;
    }

    public void setOtherCounselorNumb(int otherCounselorNumb) {
        this.otherCounselorNumb = otherCounselorNumb;
    }

    public int getOtherCounselorStuds() {
        return otherCounselorStuds;
    }

    public void setOtherCounselorStuds(int otherCounselorStuds) {
        this.otherCounselorStuds = otherCounselorStuds;
    }

    public int getTotStuds() {
        return totStuds;
    }

    public void setTotStuds(int totStuds) {
        this.totStuds = totStuds;
    }

    public int getAmtUserPCs() {
        return amtUserPCs;
    }

    public void setAmtUserPCs(int amtUserPCs) {
        this.amtUserPCs = amtUserPCs;
    }

    public int getStudsOfUser() {
        return studsOfUser;
    }

    public void setStudsOfUser(int studsOfUser) {
        this.studsOfUser = studsOfUser;
    }

    public int getNonprts() {
        return nonprts;
    }

    public int getUpprts() {
        return upprts;
    }
    
}
