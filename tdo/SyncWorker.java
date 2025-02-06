/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.tdo;

import com.caucho.hessian.client.HessianProxyFactory;
import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;
import edu.lsu.estimator.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.ejb.*;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * @author kwang
 * to detect master is alive and the lisenter program is hookable
 * to perform uploading (non-guest, and non-empty new records) to master and downloading all others (includes same counselor from diff PC) to the local DB
 * EJB timer service needs full profile ( add EJB full and glassfish CMP component)
 * or some lib like Quartz or java.util.Timer(TimerTask)?
 * may use singleton stateless EJB to perf the action...
 *
 * The best place to put a timer is to make it a static field of a servlet
 * that has load-on-startup set to non-zero value and to place timer
 * instantiation into the init() method of such a servlet. This method
 * guaranteed to be called once at server startup.
 */
@Stateful
@Named("sync")
@Singleton
//@EJB(name="java:app/SyncService", beanInterface=Serializable.class)
@ApplicationScoped //WELD-000072 Managed bean declaring a passivating scope must be passivation capable.solution: implements Serializable interface
@ApplicationException(rollback=true) //or context.setRollbackOnly() when exception is caught//javax.ejb.TransactionRequiredLocalException
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SyncWorker implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private static Logger log = LoggerFactory.getLogger();
    private static  SecretKey aesKey = null;//KeyGenerator.getInstance("AES").generateKey(); 
    @Inject
    AppReference ref;
    @Inject
    Accessor accessor;
    @Inject
    Login login;
    //@Inject InfoState info;//sessionscope(small) can not be injected into applciationscoped (big) range
    @PersistenceContext(unitName="estimator") private EntityManager em; //(unitName=" xxx")
    
    private String init_step_msg="";
    private int init_on_ind=0;
      
    private int masterUp=0;
    private edu.lsu.estimator.tdo.SPIEstimatorMaster hook = null;
    
    edu.lsu.estimator.tdo.TDOEstimatorReq req =null;
    edu.lsu.estimator.tdo.TDOMasterRes<edu.lsu.estimator.tdo.TDOCounselor> res = null;
    edu.lsu.estimator.tdo.TDOMasterRes<edu.lsu.estimator.tdo.TDOFund> res_fund = null;
    
    private String showSyncMsg=""; //msg['MasterIsConnected'] |...  for regular check, and croned sync (both synchronized)
    
    //no getter
    private String showBaseMsg="";
    private String showReadyMsg="";
    
    private Integer init_lock=0;
    private Integer sync_lock=0;
    
    public SyncWorker(){
        /*
         FacesContext facesContext = FacesContext.getCurrentInstance();
        String messageBundleName = facesContext.getApplication().getMessageBundle();
        Locale locale = facesContext.getViewRoot().getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle(messageBundleName, locale);
         */
    }
//    @PostConstruct
    public void initSyncWorker(Config seed){
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg"); //NPE ??????
        String message = bundle.getString("MasterIsConnected");
  //      log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& got msg bundle [%s]", message);
        showBaseMsg= message;
        log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& initSyncWorker() got seed getPingInterval [%d]", seed==null? -1: seed.getPingInterval()); //ref.getSeed()
        //why can not invoke ref.getseed()????
        //WELD-000049 Unable to invoke [method] @PostConstruct public edu.lsu.estimator.AppReference.initSeed() on edu.lsu.estimator.AppReference@4e5ce39d
        
        showReadyMsg=showBaseMsg;//+" | will auto sync every "+ seed.getPingInterval()+" minutes"; //ref.getSeed()
        //showSyncMsg = showReadyMsg;
        showSyncMsg = "will auto sync every "+ seed.getPingInterval()+" minutes";
       
    }
    
    
    
    public String sync(){ //int counselorid
        if( sync_lock>0){
            log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& sync() returns since counselor %d is doing the task", sync_lock);
            return "";
        }        
        
        //if( 1==1)return "";
        
        synchronized( sync_lock){ /*Do not synchronize on non final field on synchronized block in Java. 
         * because reference of non final field may change any time and then different thread might synchronizing on different objects i.e. no synchronization at all.
         
         *  java synchronized synchronizes the whole of thread memory with main memory
         * cannot apply java synchronized keyword with variables and can not use java volatile keyword with method.
         */
                
            if(sync_lock == 0 ){//not being used by other user/session (diff session may from same user)
                
                Integer counselorid = new Integer(999);
                sync_lock =counselorid;
                try{
                    //tried to delay the ajax call so the page shall show first, but this still block the whole page and showed a blank page
                    //Thread.currentThread().sleep( 2000);
                    ; 
                }catch(Exception e){}
                
                Logs synclog = new Logs( new java.util.Date(), "SYNC", "UPLOAD", "SYS" , "TRIED" ); //login.getCurrentUser().getUsername()
                try{
                    //for demo of the estimator alone, disable the sync temporarily
                    dosync(counselorid);
                    ;
                    
                    synclog.setResult("ok");
                }catch(Exception e){
                    e.printStackTrace();
                    showSyncMsg = "Failed to sync by Exception";//+e.toString();
                    synclog.setResult("fail");
                    log.info("Failed to sync: excpetion while syncing:", e);
                }finally{
                    sync_lock=0;
                    accessor.saveLog(synclog);
                }
            }else{
                log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& gained the lock, but sync() returns since SYS is doing the task");//counselor %d  , sync_lock
            }
        }
        return "";
    }
    
    private int foundMatch(String[] all, String str){
        //for(String one:all){
        for( int i=0; i<all.length; i++){
            if( all[i].equalsIgnoreCase(str)){
                return i;
            }
        }
        return 0;
    }
    
    private List<Student> queryActiveStudsByIdOrUsername(Student stud){
        boolean nobaseid = ref.isEmp(stud.getStudentALsuid());
        List<Student> results = em.createNamedQuery("Student.findActiveByLsuidOrUsername")
                           .setParameter("lsuid", nobaseid ?"1234567": stud.getStudentALsuid())
                           .setParameter("username", stud.getStudentUserName())
                           .setParameter("studentFisy", ref.getSeed().getClientFscy())
                           .getResultList();
        List<Student> matches = new ArrayList<>();
        for(Student one : results){
            if( ref.isEmp(one.getStudentALsuid()) ==false && nobaseid==false){ //both must match
                if( one.getStudentALsuid().equals(stud.getStudentALsuid())){
                    matches.add(one);                    
                }
            }else if(one.getStudentUserName().equalsIgnoreCase( stud.getStudentUserName())){
                matches.add(one);
            }
        }
        return matches;        
    }
    
    private synchronized void dosync(int counselorid){
        log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& dosync() is invoked , lock=%d (0 to quit)",  sync_lock); //by counselor %d  counselorid,
        if( sync_lock==0)return;
        
        if(hook==null )hookMaster();
        if(hook==null ){
            log.info("XXXXXXXXXXXXXXXXXXXX dosync() has to return since can not get Hessian hook");
            showSyncMsg="failed to get Hessian Hook";
            return ;
        }          
        
        showSyncMsg="";
try{        
        //CHECK IF ANY MASTER SIDE CHANGES              
        // java.lang.ClassCastException: java.math.BigDecimal( derby has numeric(18,0)) cannot be cast to java.lang.Long
//WHY ######################################## A potential statement leak detected for connection pool DerbyPool.???????
        long last_std_dod = ((BigDecimal)em.createNativeQuery("select coalesce(max(dup), 0) from student   where student_fisy=?")
                .setParameter(1, ref.getSeed().getClientFscy())
                .getSingleResult())
                .longValue();
        long last_prt_dod = ((BigDecimal)em.createNativeQuery("select coalesce(max(dup), 0) from prints  where fisy=?")
                .setParameter(1, ref.getSeed().getClientFscy())
                .getSingleResult())
                .longValue();
 log.info("XXXXXXXXXXXXXXXXXXXX dosync() got  last_std_dod=%d, last_prt_dod=%d", last_std_dod, last_prt_dod);        
//javax.resource.ResourceException: This Managed Connection is not valid as the physical connection is not usable
        
        String[] master_names = {"", "impl", "costs", "awards", "funds", "counselors", "std", "prt"};
        //enum MASTER {IMPL(1), COSTS(0), AWARDS(2), FUNDS(3), COUNSELORS(4), STD(5), PRT(6)};
        prepReq(counselorid);
        if( req==null){
            log.info("XXXXXXXXXXXXXXXXXXXX dosync() has to return since can not gen req obj");
            showSyncMsg="failed to get Request Object";
            return;
        }   
        req.last_prt_dod=last_prt_dod;
        req.last_stud_dod = last_std_dod;
 
log.info(">>>>>>>>>>>>>>>>>>>>>>>>> dosync() comm with master check() >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");        
        TDOMasterRes<Object> res_chk = hook.check(req, true);
 log.info("<<<<<<<<<<<<<<<<<<<<<<<  dosync() get reply code %d for check(), msg=%s <<<<<<<<<<<<<", res_chk==null? -99999:res_chk.code, res_chk.msgs);         
        
        int m_impl=0,m_costs=0,m_awards=0,m_funds=0,m_counselors=0,m_std=0,m_prt=0;
        if( res_chk.msgs!=null){
            int i=0;
            for( String msg: res_chk.msgs){
                log.info("  dosync() get reply master msg: %s" ,msg);
                i = foundMatch(master_names, msg);
                showSyncMsg = msg;
                switch (i){                    
                   case 1: m_impl++; break;
                   case 2: m_costs++; break;
                   case 3: m_awards++; break;
                   case 4: m_funds++; break;
                   case 5: m_counselors++; break;
                   case 6: m_std++; break;
                   case 7: m_prt++; break;
                   default:break;
                }
            }
        }
        
        //2012-12-13  to handle 888 code for UPDATE_FISY_TO_xxxx
        if(  res_chk.code>2000 ){//==888){
            //update config fisy to ...
            log.info("########################## updated FISY [%s] ##############################",  res_chk.code  );            
            ref.getSeed().setClientFscy((short)res_chk.code);//.setClientVersion( sbVer.toString()); //NPE //getClientFscy
            em.merge(ref.getSeed());
            
            log.info("########################## seed fisy=%d", ref.getSeed().getClientFscy());
            ref.refreshSeedVersion();               
            showSyncMsg="updated FISY. scheduled next run.";
            
            ref.reloadSeed();
            log.info("########################## after reloadSeed(), seed fisy=%d", ref.getSeed().getClientFscy());
            log.info("########################## after reloadSeed(), ref fisy=%d", ref.getFiscal_year());
            log.info("########################## after reloadSeed(), ref fasy=%s", ref.getFaid_year());
             
            ref.getSeed().setClientFscy((short)res_chk.code);
            
            return;
        }
        
        
        if( res_chk.code!=0){ //may need to force logout the current counselor or lock the whole system (time diff)             
            if(showSyncMsg==null || showSyncMsg.isEmpty()) showSyncMsg="failed to check master changes";
            return;
        }
                
        
        //get last_std_dod and new/mod students
        //public TDOMasterRes<TDOStudent> syncNewPickedStuds(TDOEstimatorReq req, List<TDOStudent> c_students);         
        List<TDOStudent> cup_students = new ArrayList<>();
        //only picked & doup==0 & same_clientid & ddown==0
        //@NamedQuery(name = "Student.findActiveStudentByFisyNupNdownDiff", 
        //query = "SELECT s FROM Student s WHERE s.studentFisy = :studentFisy and s.pickupInd=1 and s.dup=0 and s.ddown=0 and s.clientId = :clientId"),
 //query = "SELECT s FROM Student s WHERE s.studentFisy = :studentFisy and s.pickupInd=1 and ((s.dup=0 and s.ddown=0 and s.clientId = :clientId) or (s.estmNumb<0))   "),
       
        List<Student> students = em.createNamedQuery("Student.findActiveStudentByFisyNupNdownDiff", Student.class)
                .setParameter("studentFisy", ref.getFiscal_year())
                .setParameter("clientId", ref.getClientid())
                .getResultList();
        //2012-03-13 add??? s.studentALsuid !=null
        
        if( m_std>0 || students.size()>0){ ////if need to query master and upload
            for( Student one : students){
                Integer prts = (Integer)em.createNativeQuery("select count(*) from prints where recid=?")// and prt_time")
                        .setParameter(1, one.getRecid())
                        .getSingleResult();
                if( prts>0){
                    TDOStudent tdo = genTDOStudFromClientStud(one);
                    //if( tdo.estmNumb<0)tdo.estmNumb = tdo.estmNumb*-1;
                    cup_students.add(tdo);
                }else{
                    log.info("XXXXXXXXXXXXXXXXXXXX dosync() filters non-printed record: recid=%s",one.getRecid());
                }
            }
        }//even there is no new studs here, there maybe new students on the master, should have a try and fetch them back if any
        log.info(">>>>>> estimator got %d new local student to upload or sync to master", cup_students.size());
        
            //req = new TDOEstimatorReq();
            prepReq(counselorid);                
            req.last_stud_dod = last_std_dod;
            TDOMasterRes<TDOStudent> res_std = hook.syncNewPickedStuds(req, cup_students); //######### even no local upload, there might be master side changes from others
            if( res_std.code!=0){
                log.info("XXXXXXXXXXXXXXXXXXXX dosync() get reply code %d for syncNewStuds", res_std.code);
                if( res_std.msgs!=null){
                    for( String msg: res_std.msgs) log.info("  master msg: %s" ,msg);
                }
                showSyncMsg="failed to sync new students";
                return;
            }
            Map<String, String> losts = new HashMap<>();
            if( res_std.objs!=null && res_std.objs.size()>0 ){ //may have looser uploaded from this estimator. though not for init. plus new students uploaded from other estimators
                Student std = null;
                //for( TDOStudent tdo:res_std.objs){
                int diffs = res_std.objs.size();
                for(int x=0; x<diffs; x++){
                    TDOStudent tdo = res_std.objs.get(x);
                    if( tdo.estmNumb<0) tdo.estmNumb = tdo.estmNumb*-1;
                    
                    Student one = genClientStudFromTdo(tdo);
                    if( tdo.clientId == req.clientid ){ //only can be looser uploaded from this restimator
                        /*std = em.find(Student.class, one.getRecid());
                        if( std==null)em.persist(one);
                        else em.merge(one); //mark looser and dodown
                        */
                        one.setDup( res_std.masterclock);
                        one.setTzup( res_std.mastertz);
                        em.merge(one);
                        losts.put(one.getRecid(), "lost");
                        log.info("master returns diff set: #%d of %d: update the looser record from master [recid=%s]", x+1, diffs, tdo.recid);
                    }else{ // master may have winner who won over this estimator's uploaded stud record
                        List<Student> matches = queryActiveStudsByIdOrUsername(one);
                        //if( matches!=null && matches.size()>0 ){
                        log.info("master returns diff set: #%d of %d: insert the new record from master [recid=%s], and updates %d matched ones.", x+1, diffs, tdo.recid, matches==null?0:matches.size());
                            if( !accessor.saveMasterStudInfo(one, matches, showSyncMsg).isEmpty() ){ //insert stud, update matches
                                showSyncMsg="failed to save student data from master";
                                log.info("master returns diff set: #%d of %d: new record from master [recid=%s] FAILED.", x+1, diffs, tdo.recid);
                                return;
                            }
                        //}
                        //em.persist(one);
                        //master has set down and tz   on one
                    }
                }
                
            }//res_std !=null   
            
         //set those stud obj with doup=res_std.masterclock
                for( Student one : students){
                    if( !losts.containsKey(one.getRecid())){ //loosers are already updated
                        one.setDup( res_std.masterclock);
                        one.setTzup( res_std.mastertz);
                        if( one.getEstmNumb()<0)one.setEstmNumb( one.getEstmNumb()*-1);
                        em.merge(one);
                        log.info("estimator updating dup of uploaded and winner record [recid=%s]", one.getRecid());
                    }
                }
                
        
        //upload lost stud, but has new prints
        //public TDOMasterRes<Object> uploadNewPrtsUnpickedStuds(TDOEstimatorReq req, List<TDOStudent> c_students);
        //@NamedQuery(name = "Student.findInactiveStudentByFisyNupNdownDiff", query = "SELECT s FROM Student s WHERE s.studentFisy = :studentFisy and s.pickupInd=0 and s.dup=0 and s.ddown=0 and s.clientId = :clientId and s.prtTimes>0"),
        List<TDOStudent> c_students = new ArrayList<>();
        students = em.createNamedQuery("Student.findInactiveStudentByFisyNupNdownDiff", Student.class)
                .setParameter("studentFisy", ref.getFiscal_year())
                .setParameter("clientId", ref.getClientid()) // delete and s.clientId = :clientId  
                //shall monitor all prints of all students. but now prt will always gen new stud data
                .getResultList();
        //2012-03-13 add? : and s.studentALsuid != null  ??
        
        if( students.size()>0){
            for( Student one : students){
                TDOStudent tdo = genTDOStudFromClientStud(one);
                c_students.add(tdo);
            }
            prepReq(counselorid);
            TDOMasterRes<Object> res_up = hook.uploadNewPrtsUnpickedStuds(req, c_students);
            log.info(">>>>>> dosync() get reply code %d for uploadPrtStuds", res_up.code);
            //if needs to check response code, then put here
            if( res_up.code!=0){                
                if( res_up.msgs!=null){
                    for( String msg: res_up.msgs) log.info("  master msg: %s" ,msg);
                }
                showSyncMsg="failed to sync printed students";
                return;
            }
            for( Student one : students){
                if( one.getEstmNumb()<0)one.setEstmNumb( one.getEstmNumb()*-1);
                one.setDup( res_up.masterclock);
                one.setTzup( res_up.mastertz);
                em.merge(one);
            }
        }
        
        
        //get last_prt_dod, and upload new prints, and download master new prints and corresponding students        
        //public TDOMasterRes<TDOPrint> syncNewPrts(TDOEstimatorReq req, List<TDOPrint> c_prints);
        //@NamedQuery(name = "Print.findByClientIdNupNdownFisy", query = "SELECT o FROM Print o WHERE o.clientId = :clientId and o.dou=0 and o.dod=0 and o.fisy=:fisy   and o.prtTime>0"), 
        List<TDOPrint> c_prints = new ArrayList<>();
        List<Print> prints = em.createNamedQuery("Print.findByClientIdNupNdownFisy", Print.class)
                .setParameter("clientId", ref.getClientid())
                .setParameter("fisy", ref.getFiscal_year())
                .getResultList();
        
        if( m_prt>0 || prints.size()>0){
            for( Print one : prints){
                TDOPrint tdo = genTDOPrintFromClientPrt(one);
                c_prints.add(tdo);
            }
        }
        
        //========================###################### 04/10/2012, add the prints of those newstud in step1 ##################################################################
        //query = "SELECT o FROM Print o WHERE o.clientId = :clientId and o.dou=0 and o.dod=0 and o.fisy=:fisy and o.prtTime=0"
        log.info(">>>>>> dosync() collected newprts, size=%d.  will also check those non-prts for new students, whose size=%d", prints.size(), cup_students.size());
        for( TDOStudent onenewtdo : cup_students){
            prints = em.createNamedQuery("Print.findByClientIdNupNdownFisyNonPrt", Print.class)
                .setParameter("clientId", ref.getClientid())
                .setParameter("fisy", ref.getFiscal_year())
                .setParameter("recid", onenewtdo.recid)
                .getResultList();
            for( Print one : prints){
                TDOPrint tdo = genTDOPrintFromClientPrt(one);
                c_prints.add(tdo);
            }
            log.info(">>>>>> dosync() collected extra prts for new/pick std %s, size=%d", onenewtdo.recid, prints.size());
        }
        
        //==================################## also check those uploaded student's prints ####################################        
        //query = "SELECT s FROM Student s WHERE s.studentFisy = :studentFisy and s.dup>0 and s.ddown=0  and s.clientId = :clientId  and s.prtTimes=0 "
        /*
        students = em.createNamedQuery("Student.findUploadedNonPrtStudentByFisy", Student.class)
                .setParameter("studentFisy", ref.getFiscal_year())
                .setParameter("clientId", ref.getClientid()) // delete and s.clientId = :clientId  
                //shall monitor all prints of all students. but now prt will always gen new stud data
                .getResultList();
        //find the dup=0 prints (print_time=0)
        log.info(">>>>>> dosync() collected non-prts for uploaded students whose prttimes=0, size=%d", students.size());
        for(Student one : students){
            //query = "SELECT o FROM Print o WHERE o.clientId = :clientId and o.dou=0 and o.dod=0 and o.fisy=:fisy and o.prtTime=0"
            List<Print> nprts = em.createNamedQuery("Print.findByClientIdNupNdownFisyNonPrt", Print.class)
                .setParameter("clientId", ref.getClientid())
                .setParameter("fisy", ref.getFiscal_year())
                .setParameter("recid", one.getRecid())
                .getResultList();
            for( Print prt : nprts){
                TDOPrint tdo = genTDOPrintFromClientPrt(prt);
                c_prints.add(tdo);
            }
            log.info(">>>>>> dosync() collected non-prts for new/pick std %s, size=%d", one.getRecid(), nprts.size());
        }       */
        List<String> recs = (List<String>)em.createNativeQuery("SELECT s.recid FROM Student s WHERE s.student_Fisy = ? and s.dup>0 and s.ddown=0  and s.client_Id = ?  and s.prt_Times=0 and  exists( select '1' from Prints o  where o.dup=0 and o.ddown=0 and o.prt_Time=0 and o.recid= s.recid)")
                                            .setParameter(1, ref.getFiscal_year())
                                            .setParameter(2, ref.getClientid())
                                            .getResultList();
        if( recs!=null && recs.size()>0){
            for( String rec : recs){
                List<Print> nprts = em.createNamedQuery("Print.findByClientIdNupNdownFisyNonPrt", Print.class)
                    .setParameter("clientId", ref.getClientid())
                    .setParameter("fisy", ref.getFiscal_year())
                    .setParameter("recid", rec)
                    .getResultList();
                for( Print prt : nprts){
                    TDOPrint tdo = genTDOPrintFromClientPrt(prt);
                    c_prints.add(tdo);
                }
                log.info(">>>>>> dosync() collected non-prts for new/pick std %s, size=%d", rec, nprts.size());
            }
        }
        
        
        
        
        
        //======= also check those uploaded students, with prt_times>0 but no prints records(prt_time>0)
        List<String> fakeprtimes = (List<String>)em.createNativeQuery("select a.recid from student a where a.prt_times>0 and a.dup>0 and not exists ( select '1' from prints b where b.recid = a.recid and b.prt_time>0)").getResultList();
        if( fakeprtimes!=null && fakeprtimes.size()>0){
            for( String onerecid : fakeprtimes){
                //query = "SELECT o FROM Print o WHERE o.clientId = :clientId and o.dou=0 and o.dod=0 and o.fisy=:fisy and o.prtTime=0"
                List<Print> nprts = em.createNamedQuery("Print.findByClientIdNupNdownFisyNonPrt", Print.class)
                    .setParameter("clientId", ref.getClientid())
                    .setParameter("fisy", ref.getFiscal_year())
                    .setParameter("recid", onerecid)
                    .getResultList();
                for( Print prt : nprts){
                    TDOPrint tdo = genTDOPrintFromClientPrt(prt);
                    c_prints.add(tdo);
                }
                log.info(">>>>>> dosync() collected fake-prts for new/pick std %s, size=%d", onerecid, nprts.size());
            }
        }
        
        
        
        log.info(">>>>>> dosync() collected total prts size=%d", c_prints.size());
        
            prepReq(counselorid);        
            req.last_prt_dod = last_prt_dod;
            TDOMasterRes<TDOPrint> res_prt = hook.syncNewPrts(req, c_prints);
            log.info(">>>>>> dosync() get reply code %d for syncNewPrts", res_prt.code);
            if( res_prt.code!=0){                
                if( res_prt.msgs!=null){
                    for( String msg: res_prt.msgs) log.info("  master msg : %s",msg);
                }
                showSyncMsg="failed to sync prints";
                return ;
            }
            
            int diffs=0;
            if( res_prt.objs!=null && (diffs=res_prt.objs.size())>0){
                //save master students whose PDF has been download/prtined/emailed
                if( res_prt.obj2s!=null && res_prt.obj2s.size()>0){
                    Student std = null;
                    for( TDOStudent tdo: res_prt.obj2s){ //no order to keep                        
                        Student one = genClientStudFromTdo(tdo);
                        if( one.getEstmNumb()<0)one.setEstmNumb( one.getEstmNumb()*-1);
                         //IT may happen that other client printed estimate based stud data uploaded(original or last time), so need to find out id the stud rec exists
                        if( tdo.clientId == req.clientid ){                            
                            std = em.find(Student.class, tdo.recid);
                            if( std==null){
                                log.info(">> dosync() will save new std of same client since can not find that obj with pk=%s", tdo.recid);
                                em.persist(one);
                            }else{  ////should never happen
                                log.info(">> dosync() will merge std of same client since find obj with pk=%s", tdo.recid);
                                em.merge(one); ////should never happen
                            } 
                        }else{
                            log.info(">> dosync() will save new std of diff client %s  with pk=%s", tdo.clientId, tdo.recid);
                            em.persist(one);
                        }                        
                        //em.persist(one);
                    }
                }                
                
                Print prt = null;
                //for( TDOPrint tdo:res_prt.objs){
                for(int x=0; x<diffs; x++){
                    TDOPrint tdo = res_prt.objs.get(x);
                    
                    if( tdo.clientId != req.clientid ){
                        Print one = genClientPrintFromTdo(tdo);
                        one.setDou(res_prt.masterclock);
                        one.setDouTz( res_prt.mastertz);
                        
                        if( one.getRecid()==null){
                            log.info(">> dosync() found new PRT obj with null recid.");
                            if( tdo.recid !=null){
                                log.info(">> dosync() reset new PRT obj recid from TDO");
                                one.setRecid( tdo.recid);
                            }else{
                                log.info(">> dosync() can not set new PRT obj recid from TDO either. skipping....");
                                continue;
                            }
                        }
                        
                        //if( one.getEstmNumb()<0)one.setEstmNumb( one.getEstmNumb()*-1);
                        log.info(">> dosync() will save new PRT of diff client %s  with pk=%s of recid=%s", tdo.clientId, tdo.prtId, tdo.recid);
                        em.persist(one); //try to find dup first???  ok, use clientid to filter and skip.
                    }
                }                
            }         
        /*    
        if( prints!=null && prints.size()>0){
            for( Print one : prints){
                one.setDou( res_prt.masterclock);
                one.setDouTz(res_prt.mastertz);
                em.merge(one);
            }
        }
        */
        if( c_prints!=null && c_prints.size()>0){
            for( TDOPrint tdo : c_prints){
                Print one = em.find(Print.class, tdo.prtId);
                one.setDou( res_prt.masterclock);
                one.setDouTz(res_prt.mastertz);
                if( one.getRecid()==null){
                    log.info(">> dosync() found PRT obj with null recid.");
                    if( tdo.recid !=null){
                        log.info(">> dosync() reset PRT obj recid from TDO");
                        one.setRecid( tdo.recid);
                    }else{
                        log.info(">> dosync() can not set PRT obj recid from TDO either. skipping....");
                        continue;
                    }
                }
                log.info(">> dosync() will merge  PRT (old recid=%s)   with pk=%s of recid=%s", one.getRecid(),  tdo.prtId, tdo.recid);
                em.merge(one);
            }
        }    
        
        //====================================================================
        log.info(">>>>>> ready to sync new rules. m_funds=%d, m_counselors=%d", m_funds, m_counselors );
        if( m_funds>0){
           // long last_fund_dos = (long)em.createNativeQuery("select coalesce(max(dos), 0) from funds").getSingleResult(); // java.lang.ClassCastException: java.math.BigDecimal cannot be cast to java.lang.Long
            //can ((BigDecimal)s.createSQLQuery(sql).uniqueResult()).longValue()
            //or Long.parseLong(  x.toString())
            long last_fund_dos = Long.valueOf(em.createNativeQuery("select coalesce(max(dos), 0) from funds").getSingleResult().toString());
            prepReq(counselorid);        
            TDOMasterRes<TDOFund> res_fund = hook.getNewFunds(req, last_fund_dos);
            if( res_fund.code!=0){
                log.info("XXXXXXXXXXXXXXXXXXXX dosync() get reply code %d for syncNewFunds", res_fund.code);
                if( res_fund.msgs!=null){
                    for( String msg: res_fund.msgs) log.info("  master msg : %s",msg);
                }
                showSyncMsg="failed to sync new funds";
                return ;
            } 
            if(res_fund.objs!=null && res_fund.objs.size()>0){
                for( TDOFund tdo : res_fund.objs){
                    tdo.updator = 0;
                    Fund one = genClientFundFromTDO(tdo);
                    Fund old = em.find(Fund.class, one.getFundId());
                    if( old!=null){
                        em.merge(one);
                        log.info("===== ====== updated fund of ID=%s from master, max=%d, auto=%d, new status=%s", one.getFundId(), one.getMax_ind(), one.getAuto_ind(), one.getStatus());
                    }else{
                        em.persist(one);
                        log.info("===== inserted fund of ID=%s from master", one.getFundId());
                    }
                }
                log.info("=====get master funds version [%d]", res_fund.objsversion);
            }
            //04/19/2012
            
            if( res_fund.objs!=null && res_fund.objs.size()>0 && res_fund.objsversion>0){       //NPE ?????         
                
                Version fundver = new Version();
                fundver.setDos( System.currentTimeMillis());
                fundver.setDostz(ref.getTzSN());
                fundver.setModule("funds");
                fundver.setVersion( res_fund.objsversion);
                fundver.setEffInd(1);           
                fundver.setSrcTime( 0);//res_fund.objs_masterdoe);
                fundver.setSrcTz( "");//res_fund.objs_masterdoetz);
                fundver.setSrcWho(0);// res_fund.objs_masterupdator); //NPE                

                saveUserVer(fundver);
                log.info("+++++++++++ saved fund module version");

                String over = ref.getSeed().getClientVersion();                
                String[] overs = over.split("\\.");
                String[] rule_names = {"impl", "costs", "awards", "funds", "counselors"};
                String old = overs[3];
                if( !old.equals( String.valueOf( res_fund.objsversion) ) ){
                    StringBuilder sbVer = new StringBuilder(16);
                    int p=0;
                    for( p=0; p<3; p++){
                        sbVer.append(overs[p]).append(".");
                    }
                    sbVer.append(res_fund.objsversion);
                    for(int q=p+1; q<overs.length; q++ ){
                        sbVer.append(".").append(overs[q]);
                    }

                    log.info("+++++++++++ got client funds version [%s], master funds version [%s], and shall save [%s]", overs[3], res_fund.objsversion, sbVer.toString()  );            
                    ref.getSeed().setClientVersion( sbVer.toString()); //NPE
                    em.merge(ref.getSeed());
                    log.info("+++++++++++ config updated...");
                    ref.refreshSeedVersion();
                    log.info("+++++++++++ config refreshed..., ver=%s", ref.getSeed().getClientVersion());
                    
                }
                ref.reloadFunds();
                log.info("+++++++++++ fund reloaded ...");//, UG max=%d", ref.getFunds().get(122).getMatchTop());                
                
            }
        }
        
        if( m_counselors>0){
            //long last_user_dos = (long)em.createNativeQuery("select coalesce(max(dos), 0) from counselors").getSingleResult();
            long last_user_dos = Long.valueOf(em.createNativeQuery("select coalesce(max(dos), 0) from counselors").getSingleResult().toString());
            prepReq(counselorid);        
            TDOMasterRes<TDOCounselor> res_user = hook.getNewCounselors(req, last_user_dos);
            if( res_user.code!=0){
                log.info("XXXXXXXXXXXXXXXXXXXX dosync() get reply code %d for syncNewCounselors", res_user.code);
                if( res_user.msgs!=null){
                    for( String msg: res_user.msgs) log.info("  master msg : %s",msg);
                }
                showSyncMsg="failed to sync users";
                return ;
            }
            if(res_user.objs!=null && res_user.objs.size()>0){
                for( TDOCounselor tdo : res_user.objs){
                    Counselor one = genClientCounselorFromTDO(tdo);
                    Counselor old = em.find(Counselor.class, one.getUserid());
                    if( old!=null)em.merge(one);
                    else em.persist(one);
                }
            }            
            if( res_user.objs!=null && res_user.objs.size()>0 && res_user.objsversion>0){
                Version userver = new Version();
                userver.setDos( System.currentTimeMillis());
                userver.setDostz(ref.getTzSN());
                userver.setModule("counselors");
                userver.setVersion( res_user.objsversion); //NPE
                userver.setEffInd(1);           
                userver.setSrcTime(0);// res.objs_masterdoe);
                userver.setSrcTz( "");//res.objs_masterdoetz);
                userver.setSrcWho(0);// res.objs_masterupdator);
                saveUserVer(userver);

                String over = ref.getSeed().getClientVersion();            
                
                int p=over.lastIndexOf(".")+1;
                String old = over.substring(p);
                
                
                if( !old.equals( String.valueOf( res_user.objsversion) ) ){
                    StringBuilder sbVer = new StringBuilder(16);
                    sbVer.append(over.substring(0, p));
                    sbVer.append(res_user.objsversion);
                
                    log.info("+++++++++++ master user version [%s], and shall save [%s]", res_user.objsversion, sbVer.toString()  );            
                    ref.getSeed().setClientVersion( sbVer.toString()); //NPE
                    
                    em.merge(ref.getSeed());
                    log.info("+++++++++++ updated config record!!!");
                    ref.refreshSeedVersion();
                }
                
                ref.reloadUsers();
                log.info("+++++++++++ reloaded users");
            }
            
            
        }
        
        showSyncMsg="sync is done. scheduled next run.";
        
        //info.lastSyncTime
        //info.newStudsOfUser
        //info.sumUp();// org.jboss.weld.context.ContextNotActiveException: WELD-001303 No active contexts for scope type javax.enterprise.context.SessionScoped
        
        }catch(Exception e){
            e.printStackTrace();
            showSyncMsg="Failed to sync by Exceptions";//+e.toString();
            return ;
        }finally{
            log.info("+++++++++++ sync return msg=[%s]", showSyncMsg);
        }    
    }
    
    private int pollmaster(){
        return 1;
    }
    
    //private int 
    
    
    //==================================================================================================================================
    //public void init(ActionEvent event){ //for button actionListener, return void
    public String init(){
        if( init_lock>0)return null;
        synchronized( init_lock){
            if(init_lock == 0 ){//not being used by other user/session (diff session may from same user)
                init_lock =1;
                doinit();
                init_lock=0;
            }
        }
        return null;
    }
    public String doinit(){ //for button action, return url
        String url = null;
        StringBuilder sb = new StringBuilder(512);
        int ind = 0;
        try {
            //for actionListsener
            init_on_ind  =1;
            sb.append( "\n<br/>starting initialization ...");
            log.info("starting initialization ...");
            //Thread.currentThread().sleep(1000L); //InterruptedException

            //step 1: check clientid>0 ?
            sb.append( "done\n<br/>").append( "checking Estimator status ...");    
            log.info("checking Estimator status ...");
            if( ref.getClientid()>0)return url;

            //step 2: check master_up
            sb.append( "done\n<br/>").append( "checking master status ...");
            log.info( "checking master status ...");
            masterUp = ref.reachHostBySocket(ref.getSeed().getMastername(), Integer.parseInt( ref.getSeed().getMasterport()) )? 1 : 0;            
            if( masterUp==0)return url;
            
            //step 3: hook master
            sb.append( "done\n<br/>").append("shaking hands with master ...");   
            log.info("shaking hands with master ..."); 
            hookMaster();
            if(hook==null )return url;            
            
            //step 4: TDOEstimatorReq sent to master
            sb.append( "done\n<br/>").append( "sending initial request to master and waiting for response ...");  
            log.info( "sending initial request to master and waiting for response ...");   
            initReqRes(); //###########################################################################################// 1st,
            if(res==null )return url;   
            
            //step 5: check what master responded
            sb.append( "done\n<br/>").append( "checking response data ...");
            log.info( "checking response data ...");
            if( res.code!=0 || res.clientInitNumb==0){
                ind = -1;
                sb.append("failed");
                if( res.msgs!=null){
                    for( String msg: res.msgs){
                        sb.append("\n<br/>&nbsp;&nbsp;master: ").append(msg);
                        log.info("master msg: %s",msg);
                    }
                }
                //if client is disabled ???                
                return url;
            }
            
            //step 6: get clientid and client key from the response, and save to seed record
            sb.append( "done\n<br/>").append( "saving client identity ...");
            log.info( "saving client identity ...");
            Config seed = ref.getSeed();            
            
            if( res.remote_clock_ind!=0){
                ref.setSys_clk_msg ("Estimator clock has "+Math.abs(res.remote_clock_ind)+" seonds offset from Master."+(res.remote_clock_ind>0? " You have to fix the issue.":"")) ;
                ref.setSys_clk_ind( res.remote_clock_ind);
            }
            if( res.remote_stop_sys_ind >0){
                ref.setSys_blk_msg ("Master has disabled this Estimator.");
                ref.setSys_blk_ind( res.remote_stop_sys_ind);
            }
            if( res.remote_stop_user_ind>0){
                ref.setSys_usr_msg ("Master has denied you. You can not sign in or operate.");
                ref.setSys_usr_ind( res.remote_stop_user_ind);
            }
            
            if( ref.getSys_blk_ind()>0 || ref.getSys_clk_ind()>0){
                log.info("master set blk or clk ind. quit");
                return url; //no need to proceed, even in init.
            }
            
            String over = seed.getClientVersion();            
            String mver = res.masterver;//.versions;           
            log.info("+++++++++++ got client init version [%s], master latest version [%s], and shall save [%s]", over, mver, over.substring(0, over.indexOf(".")) + mver.substring(mver.indexOf("."))  );            
          seed.setClientVersion( over.substring(0, over.indexOf(".")) + mver.substring(mver.indexOf("."))); //NPE
            Version userver = new Version();
            userver.setDos( System.currentTimeMillis());
            userver.setDostz(ref.getTzSN());
            userver.setModule("counselors");
            userver.setVersion( res.objsversion);
            userver.setEffInd(1);           
            userver.setSrcTime( res.objs_masterdoe);
            userver.setSrcTz( res.objs_masterdoetz);
            userver.setSrcWho( res.objs_masterupdator);
            
 //           saveInitCfg(seed); //last step, in case others fail
            
            //step 7: get Counselor from the response, save to client table
            sb.append( "done\n<br/>").append( "checking and saving  users/counselors data ...");
            log.info( "checking and saving  users/counselors data ...");
            if( res.objs==null || res.objs.size()==0){
                ind = -1;
                sb.append("failed");
                return url;
            }
            List<TDOCounselor> users = res.objs;
            saveInitUsers(users);
            //ref.setUsers(users);
            //ref.setSys_usr_ind( users.size());
 //           ref.reloadSeed(); //moved to last
            
            
            
            //step 8: TDOEstimatorReq sent to master, to get funds
            sb.append( "done\n<br/>").append( "sending second request to master and waiting for response ...");
            log.info( "sending second request to master and waiting for response ...");
            secdReqRes();//###########################################################################################// 2nd,
            if( res_fund ==null)return url;
            if( res_fund.code!=0 ){
                ind = -1;
                sb.append("failed");
                if( res_fund.msgs!=null){
                    for( String msg: res_fund.msgs){
                        sb.append("\n<br/>&nbsp;&nbsp;  master: ").append(msg);
                        log.info(" master msg: %s", msg);
                    }
                }
                return url;
            }
            
            
            //step 9: get funds from the response, save to client table
            sb.append( "done\n<br/>").append( "checking and saving funds data ...");
            log.info( "checking and saving funds data ...");
            List<TDOFund> funds = res_fund.objs;
            saveInitFunds(funds);
            sb.append( "done\n<br/>");            
            Version fundver = new Version();
            fundver.setDos( System.currentTimeMillis());
            fundver.setDostz(ref.getTzSN());
            fundver.setModule("funds");
            fundver.setVersion( res_fund.objsversion);
            fundver.setEffInd(1);           
            fundver.setSrcTime( res_fund.objs_masterdoe);
            fundver.setSrcTz( res_fund.objs_masterdoetz);
            fundver.setSrcWho( res_fund.objs_masterupdator);
            //ref.setFunds(funds);
            
            saveUserVer(userver);
            saveUserVer(fundver);
            seed.setClientid( res.clientInitNumb);
            ref.setClientid(res.clientInitNumb);
            seed.setMasterecho(javaAESdecryptor(aesKey, res.aesData) );
            
            
            
            //2012-01-26 also get/fetch all master students and prints data, other estimators may have done some work
            //got clientid, but no counselorid at this time (init sync data)
            //get studs by calling: public TDOMasterRes<TDOStudent> syncNewPickedStuds(TDOEstimatorReq req, List<TDOStudent> c_students);  
            sb.append( "downloading all active students from master ......");
            log.info( "downloading all active students from master ......");
            req.clientclock = System.currentTimeMillis();
            req.clienttz = ref.getTzSN();
            req.last_stud_dod=0;
            
            //2012-12-13: only fetch for specified fisy
            req.fisy=ref.getSeed().getClientFscy();
            log.info("doinit() ---- ----- set req fisy from seed=%d", req.fisy);
            
            TDOMasterRes<TDOStudent> res_std = hook.syncNewPickedStuds(req, null);
            //although it is init, still need to check existence before persistence, in case of failure of last init
            if( res_std.code!=0){                
                ind = -1;
                sb.append("failed");
                if( res_std.msgs!=null){
                    for( String msg: res_std.msgs) {
                        sb.append("\n<br/>&nbsp;&nbsp;  master: ").append(msg);
                        log.info("master msg: %s", msg);
                    }
                }
                return url;
            }
            sb.append( "done\n<br/>");
            if( res_std.objs!=null && res_std.objs.size()>0 ){ //may have looser uploaded from this estimator. though not for init
                Student std = null;
                for( TDOStudent tdo:res_std.objs){
                    Student one = genClientStudFromTdo(tdo);
                    if( tdo.clientId == req.clientid ){
                        std = em.find(Student.class, one.getRecid());
                        if( std==null)em.persist(one);
                        else em.merge(one);
                    }else{
                        em.persist(one);
                    }
                }
            }
            
            //no lost stud w/prints
            //get prints by calling: public TDOMasterRes<TDOPrint> syncNewPrts(TDOEstimatorReq req, List<TDOPrint> c_counselors);
            req.clientclock = System.currentTimeMillis();
            req.clienttz = ref.getTzSN();
            req.last_prt_dod=0;
            TDOMasterRes<TDOPrint> res_prt = hook.syncNewPrts(req, null);
            sb.append( "downloading all print records and corresponding students from master ......");
            log.info( "downloading all print records and corresponding students from master ......");
            if( res_prt.code!=0){                
                ind = -1;
                sb.append("failed");
                if( res_prt.msgs!=null){
                    for( String msg: res_prt.msgs){
                        sb.append("\n<br/>&nbsp;&nbsp;  master: ").append(msg);
                        log.info("master msg: %s", msg);
                    }
                }
                return url;
            }
            if( res_prt.objs!=null && res_prt.objs.size()>0){
                Print prt = null;
                for( TDOPrint tdo:res_prt.objs){
                    Print one = genClientPrintFromTdo(tdo);
                    em.persist(one); //try to find first???
                }
                if( res_prt.obj2s!=null && res_prt.obj2s.size()>0){
                    Student std = null;
                    for( TDOStudent tdo: res_prt.obj2s){
                        Student one = genClientStudFromTdo(tdo);
                        if( tdo.clientId == req.clientid ){
                            std = em.find(Student.class, one.getRecid());
                            if( std==null)em.persist(one);
                            else em.merge(one);
                        }else{
                            em.persist(one);
                        }
                    }
                }
                
            }            
            sb.append( "done\n<br/>");
            log.info("done fetching student and prints data from master for init");
            
            
            saveInitCfg(seed);             
            ref.reloadSeed();
            ref.getFunds();
            ind = 1;
            log.info("done init. saved clientid and seed record");
            
            //return url;  //refresh page, to show login form
        }catch (Exception ex) {
            log.info("", ex);
            init_step_msg = "Error: "+ex.getMessage();
        }finally{
            init_on_ind = 0;
            if( ind==0)sb.append("failed\n<br/>");
            if( ind==1){
                sb.append("\n<br/><br/>Initialization is performed.");
                log.info("Initialization is performed.");
            }else{
                sb.append("\n<br/><br/>Initialization is not performed.");
                log.info("Initialization is not performed.");
            }
            init_step_msg = sb.toString();
        }
        return url;        
    }
    
    private void hookMaster(){
        masterUp = ref.getMaster_up_ind();
        log.error("~~~~~~~~~~~~~~~~~~~ hookMaster() got master_up_ind=%d. ", masterUp);
        if( masterUp <=0){
            this.hook = null;            
            return;
        }
        
        String url = ref.getSeed().getMasterurl();// "http://localhost:8080/master3/req"; // RPCWEBEJB   tunneled via 8080, so works better, than the standard IIOP way.
        HessianProxyFactory factory = new HessianProxyFactory();
        try{
            this.hook = (SPIEstimatorMaster) factory.create(SPIEstimatorMaster.class, url);
            //factory.setConnectTimeout(serialVersionUID);
            //factory.setReadTimeout(serialVersionUID);
            
        }catch(Exception e){
            this.hook = null;
            ref.setMaster_up_ind(0);//.setSys_blk_ind(1);
            
            e.printStackTrace();
        }
        if( aesKey == null){
            try{
                KeyGenerator keygen = KeyGenerator.getInstance("AES");
                aesKey = keygen.generateKey();
            }catch(Exception e){
                aesKey = null;
                e.printStackTrace();
            }
        }
        log.info("hookMaster() hooked master at url=[%s]", url);
  //      assertNotNull(timeService);
    }
    
    private void prepReq(int counselorid){ //for regular sync
  //      log.info(" prepReq() called with counselorid=%s ", counselorid);
        if( req==null){
            req = new TDOEstimatorReq();
            HashMap<String, String> map = new HashMap<>();
            req.fisy = ref.getSeed().getClientFscy();//2012; from table config
            log.info(" prepreq()---- ----- set req fisy from seed=%d", req.fisy);
            
            req.clientid=ref.getClientid();
            
            if( aesKey==null) {
                try{
                    KeyGenerator keygen = KeyGenerator.getInstance("AES");
                    aesKey = keygen.generateKey();
                }catch(Exception e){
                    aesKey = null;
                    e.printStackTrace();
                    req=null;
                    return;
                }
            }
            req.aesData = this.javaAESencryptor( ref.getSeed().getMasterecho());
            req.c_key = aesKey;            
            
            req.clientversions = ref.getFullclientverions(); //"3.1.1.1"
            
            req.counselorid = counselorid;
        
            req.clienthostname = ref.getOSinfo();
            req.clientaddress = ref.getAddress();
            req.clientjava = ref.getJavaInfo();            
  //          log.info(" prepReq() init req with client host[%s], addr[%s], java[%s] ", req.clienthostname, req.clientaddress, req.clientjava);
            
            req.last_stud_dod = 0L;
            req.last_prt_dod = 0L;
            
            req.clienttz = ref.getTzSN();// TimeZone.getDefault().getDisplayName( TimeZone.getDefault().inDaylightTime(new Date()), TimeZone.SHORT);
            req.clientclock = System.currentTimeMillis();
        }else{            
            req.clientversions = ref.getFullclientverions();            
            req.counselorid = counselorid;                        
            req.last_stud_dod = 0L;
            req.last_prt_dod = 0L;
            
            req.clienttz = ref.getTzSN();
            req.clientclock = System.currentTimeMillis();
  //          log.info(" prepReq() reset req ");
        }
  //      log.info(" prepReq() done with counselorid=%s ", counselorid);
    }
    
    private void initReqRes(){
        res = null;
        if( hook ==null){
            log.error(" ***** testReqRes() can not get master handler through Hessian factory. quit. ********* ");
            return;            
        }
        if( aesKey == null ){
            log.error(" ***** testReqRes() can not get AES key for secure communication. quit. ********* ");
            return;  
        }
        //collect client into
        req = new TDOEstimatorReq();
        HashMap<String, String> map = new HashMap<>();
        req.fisy = ref.getSeed().getClientFscy();//2012; from table config
        log.info("initreqres()---- ----- set req fisy from seed=%d", req.fisy);
        
        req.clientid=0;
        req.c_key = aesKey;
        req.clientclock = System.currentTimeMillis();
        req.clienttz = ref.getTzSN();// TimeZone.getDefault().getDisplayName( TimeZone.getDefault().inDaylightTime(new Date()), TimeZone.SHORT);
        req.clientversions = null; //"3.1.1.1"
        req.counselorid = 0;
        req.aesData = null; //byte[] for client cert, encrypted by aes encryptor
        req.last_stud_dod = 0L;
        req.last_prt_dod = 0L;
        
        req.clienthostname = ref.getOSinfo();
        req.clientaddress = ref.getAddress();
        req.clientjava = ref.getJavaInfo();
        
        //TDOMasterRes<TDOCounselor> 
                res = hook.init(req); //java.lang.ClassNotFoundException: edu.lsu.estimator.Client ????? BY Hessian ????
        
        if( res==null){
            log.info(" --- got empty master init res.");
        }else{        
            log.info(" --- got master res. code=%d, new clientid=%d",res.code,  res.clientInitNumb);
            if( res.code==0 ){// res.clientInitNumb >0){
                log.info(" --- got master counselors. numb=%d, first one's name=%s", res.objs.size(), res.objs.get(0).username);
                log.info(" --- got master assigned client key=%s", javaAESdecryptor(aesKey, res.aesData));
            }
            if( res.msgs!=null){
                for(String msg : res.msgs)
                log.info(" --- got master res msg = %s", msg);
            }
        }         
    }
    
    private void secdReqRes(){        
        req.clientid = res.clientInitNumb;
        req.aesData = res.aesData;
        req.clientclock = System.currentTimeMillis();
        req.clientversions = ref.getFullclientverions();//.getSeed().getClientVersion();
        req.c_key = aesKey;
        req.aesData = res.aesData; //has not been saved into db yet
        
        res_fund = hook.getInitFunds(req);
        if( res_fund==null){
            log.info(" --- got empty master funds res.");
        }else if( res_fund.code!=0){
            log.info(" --- got master res. code=%d", res_fund.code);
            if( res_fund.msgs!=null){
                for(String msg : res_fund.msgs)
                log.info(" --- got master funds res msg = %s", msg);
            }
        }        
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void saveInitCfg(Config seed){ //'UPDATOR'  cannot accept a NULL value.
        //em.merge(seed);
        em.createNativeQuery("update config set clientid=?, masterecho=?, client_version=? where clientid=0")
                .setParameter(1, seed.getClientid())
                .setParameter(2, seed.getMasterecho())
                .setParameter(3, seed.getClientVersion())
                .executeUpdate();
        //em.flush(); //The attribute [clientid] of class [edu.lsu.estimator.Config] is mapped to a primary key column in the database. Updates are not allowed.
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void saveInitUsers(List<TDOCounselor> users){
        em.createNativeQuery("delete from counselors").executeUpdate();
        for(TDOCounselor one:users){
            Counselor ndo = new Counselor();
            
            ndo.setUserid( one.userid);
            ndo.setUsername( one.username);            
            ndo.setSuperuser(one.superuser? 1: 0); 
            ndo.setStatus(one.status);
            ndo.setDeptName( one.dept);
            ndo.setLsuid( one.lsuid);
            ndo.setCreator((int)one.creator);
            ndo.setDoe(one.doe);
            ndo.setDoetz(one.doetz);
            ndo.setEditor((int)one.editor);
            ndo.setDom(one.dom);
            ndo.setDomtz(one.domtz);
            ndo.setEmail(one.email);
            
            ndo.setDos(one.dos);
            ndo.setDostz(one.dostz);
            
            try{
                em.persist(ndo);   //   javax.validation.ConstraintViolationException: Bean Validation constraint(s) violated while executing Automatic Bean Validation on callback event:'prePersist'. Please refer to embedded ConstraintViolations for details.      
/*

INFO: ########## Path: deptName ########### FAILED: may not be null
INFO: ########## Path: deptName ########### FAILED: may not be empty
INFO: ########## Path: lsuid ########### FAILED: may not be null
 */
            }catch(javax.validation.ConstraintViolationException ve){
  //            ve.printStackTrace();
              for( ConstraintViolation cv :  ve.getConstraintViolations()){
                   System.out.println( "########## Path: "+cv.getPropertyPath().toString()+" ########### FAILED: "+cv.getMessage() );/*
                   System.out.println( cv.getInvalidValue().toString() ); //NPE
                   System.out.println( cv.getConstraintDescriptor().toString() );
                   System.out.println( cv.getPropertyPath().toString() );
                   System.out.println( cv.getLeafBean().toString() );
                   System.out.println( cv.getLeafBean() );              

                   System.out.println( cv.getRootBean().toString() );
                   System.out.println( cv.getRootBeanClass().getCanonicalName() );
                   System.out.println( cv.getRootBeanClass().getSimpleName() ); */
               } 
               //return  msg = ve.toString(); 
            }catch(Exception e){
                e.printStackTrace();
                //return msg = e.getMessage();
            }
        }  
//        em.flush();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void saveInitFunds(List<TDOFund> funds){
        em.createNativeQuery("delete from funds").executeUpdate();
        for(TDOFund one:funds){
            Fund ndo = new Fund();
            ndo.setFundId( one.fundId);
            ndo.setFundCode(one.fundCode);
            ndo.setFundDesc(one.fundDesc);
            
            ndo.setHasMatching(one.hasMatching);
            ndo.setMatchPerc(one.matchPerc);            ///always 0 ????????????????????????????????????????????????????????
            ndo.setMatchTop(one.matchTop);
            ndo.setReqNoteInd(one.reqNoteInd);
            
            ndo.setStatus(one.status);
            ndo.setEarningsMatch(one.earningsMatch);
            ndo.setPriority(one.priority);
            ndo.setCreator(one.creator); //str
            ndo.setDoe( one.doe); //date
            ndo.setDoetz( one.doetz);
            
            ndo.setUpdator(one.updator==null? 0: one.updator);//str
            ndo.setDom( one.dom);//date
            ndo.setDomtz( one.domtz);
            
            ndo.setSyncor(0);// init sync, no user identified
            ndo.setDos(one.dos);//date
            ndo.setDostz( one.dostz);
            
            ndo.setAuto_ind( one.auto_ind);
            ndo.setMax_ind(one.max_ind);
            
            ndo.setInstCapExcept(one.instCapExcept);              
            try{
                em.persist(ndo);            
            }catch(javax.validation.ConstraintViolationException ve){
  //            ve.printStackTrace();
              for( ConstraintViolation cv :  ve.getConstraintViolations()){
                   System.out.println( "########## Path: "+cv.getPropertyPath().toString()+" ########### FAILED: "+cv.getMessage() );
              }
            }catch(Exception e){
                e.printStackTrace();
            }
        }  
//        em.flush();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void saveUserVer(Version ver){
        em.createNativeQuery("update versions set eff_ind=0 where eff_ind=1 and module=?").setParameter(1, ver.getModule()).executeUpdate();        
        em.persist(ver);  
        log.info("------ saveUserVer()  updated versions eff_ind to 0 for module=%s", ver.getModule());
    }
    
    
    public void timedTask(){ //if timeout or exception. Timer and interval and re-try limit?
        //ping server
        //conn socket of the listener
    }
    
    public String handsync(){
        return "sync?faces-redirect=true";
    }
    
    
    public String getInit_step_msg() {
        return init_step_msg;
    }

    public void setInit_step_msg(String init_step_msg) {
        this.init_step_msg = init_step_msg;
    }

    public int getInit_on_ind() {
        return init_on_ind;
    }

    public void setInit_on_ind(int init_on_ind) {
        this.init_on_ind = init_on_ind;
    }
    
    
    
    public String javaAESdecryptor(SecretKey key, byte[] cipherStr){
            if( cipherStr==null || cipherStr.length==0)return "";
            try{
                //To create an AES key, we have to instantiate a KeyGenerator for AES. We do not specify a provider, because we do not care about a particular AES key generation implementation.
                //Since we do not initialize the KeyGenerator, a system-provided source of randomness and a default keysize will be used to create the AES key:
                KeyGenerator keygen = KeyGenerator.getInstance("AES");
    //           SecretKey aesKey = keygen.generateKey();                
                
                //create a Cipher instance
                Cipher aesCipher;    
                // Create the cipher
                aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                
                //use the generated aesKey from above to initialize the Cipher object for encryption:
                
/*                
                // Initialize the cipher for encryption
                aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);

                // Our cleartext
                byte[] cleartext = clearStr.getBytes();//"This is just an example".getBytes();
                // Encrypt the cleartext
                byte[] ciphertext = aesCipher.doFinal(cleartext);
*/                
                
                // Initialize the same cipher for decryption
                aesCipher.init(Cipher.DECRYPT_MODE, key);
                // Decrypt the ciphertext
                byte[] cleartext1 = aesCipher.doFinal(cipherStr);//.getBytes()); //Input length must be multiple of 16 when decrypting with padded cipher
                //cleartext and cleartext1 are identical.//////
                
                
                System.out.println("client javaAESdecryptor() deciphered text="+new String(cleartext1));
                return new String(cleartext1);
            }catch(Exception e){
                e.printStackTrace();
            }
            return "client AES decryptor  not working";
        }    
    
    
        public byte[] javaAESencryptor(String clearStr){
            if( clearStr==null || clearStr.isEmpty()) return "".getBytes();
            try{
                //To create an AES key, we have to instantiate a KeyGenerator for AES. We do not specify a provider, because we do not care about a particular AES key generation implementation.
                //Since we do not initialize the KeyGenerator, a system-provided source of randomness and a default keysize will be used to create the AES key:
                KeyGenerator keygen = KeyGenerator.getInstance("AES");
     //           SecretKey aesKey = keygen.generateKey();
                if( aesKey == null){
                    aesKey = keygen.generateKey();
                }
                
                //create a Cipher instance
                Cipher aesCipher;    
                // Create the cipher
                aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                
                //use the generated aesKey from above to initialize the Cipher object for encryption:
                // Initialize the cipher for encryption
                aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);

                // Our cleartext
                byte[] cleartext = clearStr.getBytes();//"This is just an example".getBytes();
                // Encrypt the cleartext
                byte[] ciphertext = aesCipher.doFinal(cleartext);
                
      /*          
                // Initialize the same cipher for decryption
                aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
                // Decrypt the ciphertext
                byte[] cleartext1 = aesCipher.doFinal(ciphertext);
                //cleartext and cleartext1 are identical.//////
        */
                
                //return new String(ciphertext);
                return ciphertext;
            }catch(Exception e){
                e.printStackTrace();
            }
            return "client AES encryptor not working".getBytes();
        }

    public String getShowSyncMsg() {
        return showSyncMsg;
    }

    public void setShowSyncMsg(String showSyncMsg) {
        this.showSyncMsg = showSyncMsg;
    }
        
        
    //====================================== Rule OBJ from TDO ================================
    private Fund genClientFundFromTDO(TDOFund tdo){//for rules, client only receives, and master only sends---unidirection transform. clients has no auto-sequence
            Fund one = new Fund();
            one.setCreator(         tdo.creator            );                                                                              
            one.setDoe(             tdo.doe                );                                                                              
            one.setDoetz(           tdo.doetz              );                                                                              
            one.setUpdator(         tdo.updator            );                                                                              
            one.setDom(             tdo.dom                );                                                                              
            one.setDomtz(           tdo.domtz              );                                                                              

            one.setFundId(          tdo.fundId             );                                                                              
            one.setFundCode(        tdo.fundCode           );                                                                              
            one.setFundDesc(        tdo.fundDesc           );                                                                              
            one.setEarningsMatch(   tdo.earningsMatch      );                                                                              
            one.setHasMatching(     tdo.hasMatching        );                                                                              
            one.setMatchPerc(       tdo.matchPerc          );                                                                              
            one.setMatchTop(        tdo.matchTop           );                                                                              
            one.setInstCapExcept(   tdo.instCapExcept      );                                                                              
            one.setPriority(        tdo.priority           );                                                                              
            one.setReqNoteInd(      tdo.reqNoteInd         );                                                                              
            one.setStatus(          tdo.status             );                                                                             
            
            one.setSyncor(          tdo.syncor             );
            one.setDos(             tdo.dos                );
            one.setDostz(           tdo.dostz              );  
            
            //
            //log.info("~~~~~~~ new FUND POJO (from master TDO) MAX_IND=%d", one.getMax_ind());
            //log.info("~~~~~~~ new FUND POJO (from master TDO) AUTO_IND=%d", one.getAuto_ind());
            one.setAuto_ind(        tdo.auto_ind           );
            one.setMax_ind(         tdo.max_ind            );

            return one;
    }
    private Counselor genClientCounselorFromTDO(TDOCounselor tdo){ //for rules, client only receives, and master only sends
        Counselor one = new Counselor();
        one.setUserid(     tdo.userid     );
        one.setUsername(   tdo.username   );
        one.setStatus(     tdo.status     );
        one.setSuperuser(  tdo.superuser? 1:0  );
        one.setCreator((int)tdo.creator    );
        one.setDoe(        tdo.doe        );
        one.setDoetz(      tdo.doetz      );
        one.setEditor((int)tdo.editor     );
        one.setDom(        tdo.dom        );
        one.setDomtz(      tdo.domtz      );
        one.setLsuid(      tdo.lsuid      );
        one.setDeptName(   tdo.dept       );
        one.setEmail(      tdo.email      );
        
        one.setDos(        tdo.dos        );
        one.setDostz(      tdo.dostz      );
        one.setStdInd(     tdo.stdInd);
        return one;
    }
    
    //====================================== print Data OBJ to/from TDO ================================
    private TDOPrint genTDOPrintFromClientPrt(Print one){//for uploading
        TDOPrint tdo = new TDOPrint();
        //the dou will be set by master using master's time. . client needs to get that time from res to save/merge back to db (if res.code==0)
        tdo.dou             = one.getDou();//.getDup();
        tdo.douTz           = one.getDouTz();//.getDupTz();
        
        tdo.dod             = one.getDod();
        tdo.dodTz           = one.getDodTz();
        
        tdo.calGrantA       = one.getCalGrantA();//.getCalGranta();
        tdo.calGrantB       = one.getCalGrantB();//.getCalGrantb();
        tdo.clientId        = one.getClientId();
        tdo.coa             = one.getCoa();
        tdo.counselorId     = one.getCounselorId();
        tdo.counselorName   = one.getCounselorName();
        
        tdo.due             = one.getDue();
        tdo.earnings        = one.getEarnings();
        tdo.efc             = one.getEfc();
        tdo.familyDisct     = one.getFamilyDisct();
        tdo.fisy            = one.getFisy();
        tdo.fisyPrt         = one.getFisyPrt();
        tdo.fws             = one.getFws();
        tdo.lsuEa           = one.getLsuEa();
        tdo.lsuPerf         = one.getLsuPerf();
        tdo.lsuSship        = one.getLsuSship();
        tdo.maxNeed         = one.getMaxNeed();
        tdo.monthOpt        = one.getMonthOpt();
        tdo.natlMerit       = one.getNatlMerit();
        tdo.nonLsuEa        = one.getNonLsuEa();
        tdo.nonLsuSship     = one.getNonLsuSship();
        tdo.noncalGrant     = one.getNoncalGrant();
        tdo.otherExpenses   = one.getOtherExpenses();
        tdo.pell            = one.getPell();
        tdo.perkins         = one.getPerkins();
       tdo.prtId           = one.getPrtId();
        tdo.prtNum          = one.getPrtNum();
        tdo.prtTime         = one.getPrtTime();
        tdo.prtTz           = one.getPrtTz();
        tdo.pseog           = one.getPseog();
        tdo.quarterOpt      = one.getQuarterOpt();
       tdo.recid           = one.getRecid();
        tdo.roomBoard       = one.getRoomBoard();
        tdo.sda             = one.getSda();
        tdo.sship1ExtAmt    = one.getSship1ExtAmt();
        tdo.sship1LsuAmt    = one.getSship1LsuAmt();
        tdo.sship1Name      = one.getSship1Name();
        tdo.sship2ExtAmt    = one.getSship2ExtAmt();
        tdo.sship2LsuAmt    = one.getSship2LsuAmt();
        tdo.sship2Name      = one.getSship2Name();
        tdo.sship3ExtAmt    = one.getSship3ExtAmt();
        tdo.sship3LsuAmt    = one.getSship3LsuAmt();
        tdo.sship3Name      = one.getSship3Name();
        tdo.sship4ExtAmt    = one.getSship4ExtAmt();
        tdo.sship4LsuAmt    = one.getSship4LsuAmt();
        tdo.sship4Name      = one.getSship4Name();
        tdo.sship5ExtAmt    = one.getSship5ExtAmt();
        tdo.sship5LsuAmt    = one.getSship5LsuAmt();
        tdo.sship5Name      = one.getSship5Name();
        tdo.sship6ExtAmt    = one.getSship6ExtAmt();
        tdo.sship6LsuAmt    = one.getSship6LsuAmt();
        tdo.sship6Name      = one.getSship6Name();
        tdo.sship7ExtAmt    = one.getSship7ExtAmt();
        tdo.sship7LsuAmt    = one.getSship7LsuAmt();
        tdo.sship7Name      = one.getSship7Name();
        tdo.sship8ExtAmt    = one.getSship8ExtAmt();
        tdo.sship8LsuAmt    = one.getSship8LsuAmt();
        tdo.sship8Name      = one.getSship8Name();
        tdo.sship9ExtAmt    = one.getSship9ExtAmt();
        tdo.sship9LsuAmt    = one.getSship9LsuAmt();
        tdo.sship9Name      = one.getSship9Name();
        
        tdo.needGrant       = one.getNeedGrant();
        tdo.achieveAward    = one.getAchieveAward();
        tdo.renew4y         = one.getRenew4y();
        //201602
        tdo.lsugrant        = one.getLsugrant();
        
        tdo.subloan         = one.getSubloan();
        tdo.totAid          = one.getTotAid();
        tdo.totAidWoWork    = one.getTotAidWoWork();
        tdo.totCharges      = one.getTotCharges();
        tdo.totLoan         = one.getTotLoan();
        tdo.tuitionFee      = one.getTuitionFee();
        tdo.unsubloan       = one.getUnsubloan();
        tdo.versions        = one.getVersions();//.getVerstr();
        tdo.yearOpt         = one.getYearOpt();
        return tdo;
    }
    
    private Print genClientPrintFromTdo(TDOPrint tdo){//, long mastertime, String tz){
        Print one = new Print();
        one.setDod(               tdo.dod          ); //.setDdown(
        one.setDodTz(             tdo.dodTz        ); //.setDdownTz(       
        
        one.setDou(               tdo.dou          );             
        one.setDouTz(             tdo.douTz        );        
        one.setCalGrantA(         tdo.calGrantA    );             
        one.setCalGrantB(         tdo.calGrantB    );             
        one.setClientId(          tdo.clientId     );             
        one.setCoa(               tdo.coa          );             
        one.setCounselorId(       tdo.counselorId  );             
        one.setCounselorName(     tdo.counselorName);             
                    
        one.setDue(               tdo.due          );             
        one.setEarnings(          tdo.earnings     );             
        one.setEfc(               tdo.efc          );             
        one.setFamilyDisct(       tdo.familyDisct  );             
        one.setFisy(              tdo.fisy         );             
        one.setFisyPrt(           tdo.fisyPrt      );             
        one.setFws(               tdo.fws          );             
        one.setLsuEa(             tdo.lsuEa        );             
        one.setLsuPerf(           tdo.lsuPerf      );             
        one.setLsuSship(          tdo.lsuSship     );             
        one.setMaxNeed(           tdo.maxNeed      );             
        one.setMonthOpt(          tdo.monthOpt     );             
        one.setNatlMerit(         tdo.natlMerit    );             
        one.setNonLsuEa(          tdo.nonLsuEa     );             
        one.setNonLsuSship(       tdo.nonLsuSship  );             
        one.setNoncalGrant(       tdo.noncalGrant  );             
        one.setOtherExpenses(     tdo.otherExpenses);             
        one.setPell(              tdo.pell         );             
        one.setPerkins(           tdo.perkins      );             
        one.setPrtId(             tdo.prtId        );             
        one.setPrtNum(            tdo.prtNum       );             
        one.setPrtTime(           tdo.prtTime      );             
        one.setPrtTz(             tdo.prtTz        );             
        one.setPseog(             tdo.pseog        );             
        one.setQuarterOpt(        tdo.quarterOpt   );             
        one.setRecid(             tdo.recid        );             
        one.setRoomBoard(         tdo.roomBoard    );             
        one.setSda(               tdo.sda          );             
        one.setSship1ExtAmt(      tdo.sship1ExtAmt );             
        one.setSship1LsuAmt(      tdo.sship1LsuAmt );             
        one.setSship1Name(        tdo.sship1Name   );             
        one.setSship2ExtAmt(      tdo.sship2ExtAmt );             
        one.setSship2LsuAmt(      tdo.sship2LsuAmt );             
        one.setSship2Name(        tdo.sship2Name   );             
        one.setSship3ExtAmt(      tdo.sship3ExtAmt );             
        one.setSship3LsuAmt(      tdo.sship3LsuAmt );             
        one.setSship3Name(        tdo.sship3Name   );             
        one.setSship4ExtAmt(      tdo.sship4ExtAmt );             
        one.setSship4LsuAmt(      tdo.sship4LsuAmt );             
        one.setSship4Name(        tdo.sship4Name   );             
        one.setSship5ExtAmt(      tdo.sship5ExtAmt );             
        one.setSship5LsuAmt(      tdo.sship5LsuAmt );             
        one.setSship5Name(        tdo.sship5Name   );             
        one.setSship6ExtAmt(      tdo.sship6ExtAmt );             
        one.setSship6LsuAmt(      tdo.sship6LsuAmt );             
        one.setSship6Name(        tdo.sship6Name   );             
        one.setSship7ExtAmt(      tdo.sship7ExtAmt );             
        one.setSship7LsuAmt(      tdo.sship7LsuAmt );             
        one.setSship7Name(        tdo.sship7Name   );             
        one.setSship8ExtAmt(      tdo.sship8ExtAmt );             
        one.setSship8LsuAmt(      tdo.sship8LsuAmt );             
        one.setSship8Name(        tdo.sship8Name   );             
        one.setSship9ExtAmt(      tdo.sship9ExtAmt );             
        one.setSship9LsuAmt(      tdo.sship9LsuAmt );             
        one.setSship9Name(        tdo.sship9Name   );             
        
        
        one.setNeedGrant(         tdo.needGrant    );
        one.setAchieveAward(      tdo.achieveAward );
        one.setRenew4y(           tdo.renew4y      ); 
        //201602
        one.setLsugrant(          tdo.lsugrant     );
        
        one.setSubloan(           tdo.subloan      );             
        one.setTotAid(            tdo.totAid       );             
        one.setTotAidWoWork(      tdo.totAidWoWork );             
        one.setTotCharges(        tdo.totCharges   );             
        one.setTotLoan(           tdo.totLoan      );             
        one.setTuitionFee(        tdo.tuitionFee   );             
        one.setUnsubloan(         tdo.unsubloan    );             
        one.setVersions(          tdo.versions     );             
        one.setYearOpt(           tdo.yearOpt      );   
         

        return one;
    }
    
    //====================================== stud Data OBJ to/from TDO ================================
    private TDOStudent genTDOStudFromClientStud(Student one){
        TDOStudent tdo = new TDOStudent();
        tdo.dup                 = one.getDup();
        tdo.tzup                = one.getTzup();
        
        tdo.ddown               = one.getDdown();
        tdo.tzdown              = one.getTzdown();
        
        tdo.recid               = one.getRecid();       
        tdo.clientId            = one.getClientId();
        tdo.counselorId         = one.getCounselorId();
        tdo.studentNumb         = one.getStudentNumb();
        tdo.studentUserName     = one.getStudentUserName();
        tdo.studentALsuid       = one.getStudentALsuid();
        tdo.studentBLastname    = one.getStudentBLastname();
        tdo.studentCFirstname   = one.getStudentCFirstname();
        tdo.studentDDob         = one.getStudentDDob();
        tdo.pickupInd           = one.getPickupInd(); //some are uploaded just because prints
        
        tdo.sex                 = one.getSex();
        tdo.counselorMod        = one.getCounselorMod();
        tdo.counselorOrig       = one.getCounselorOrig();        
        tdo.estmNumb            = one.getEstmNumb();
        tdo.fund1id             = one.getFund1id();
        tdo.fund2id             = one.getFund2id();
        tdo.fund3id             = one.getFund3id();
        tdo.fund4id             = one.getFund4id();
        tdo.fund5id             = one.getFund5id();
        tdo.fund6id             = one.getFund6id();
        tdo.fund7id             = one.getFund7id();
        tdo.fund8id             = one.getFund8id();
        tdo.fund9id             = one.getFund9id();
        tdo.homeAddrApt         = one.getHomeAddrApt();
        tdo.homecostudies       = one.getHomecostudies();
        tdo.indEalsu            = one.getIndEalsu();
        tdo.indEanonlsu         = one.getIndEanonlsu();
        tdo.indEfc              = one.getIndEfc();
        tdo.indExcloans         = one.getIndExcloans();
        
        tdo.adjCalgrantInd      = one.getAdjCalgrantInd();        
        
        tdo.studentAaCalgrantA          = one.getStudentAaCalgrantA();
        tdo.studentAbCalgrantB  	= one.getStudentAbCalgrantB();
        tdo.studentAcFamilySize 	= one.getStudentAcFamilySize();
        tdo.studentAdFamilyIncome       = one.getStudentAdFamilyIncome();
        tdo.studentAeFamilyAsset        = one.getStudentAeFamilyAsset();
        tdo.studentAfFamilyContrib      = one.getStudentAfFamilyContrib();
        tdo.studentAgNonlsuAllowrance   = one.getStudentAgNonlsuAllowrance();
        tdo.studentAiEduAllowPer        = one.getStudentAiEduAllowPer();
        tdo.studentAjHomeState          = one.getStudentAjHomeState();
        tdo.studentAkNoncalGrant        = one.getStudentAkNoncalGrant();
        tdo.studentAlOutScholarships    = one.getStudentAlOutScholarships();
        tdo.studentAmOutScholarshipAmt  = one.getStudentAmOutScholarshipAmt();
        tdo.studentAnPubNotes           = one.getStudentAnPubNotes();
        tdo.studentAoPriNotes           = one.getStudentAoPriNotes();
        tdo.studentApSubLoans           = one.getStudentApSubLoans();
        tdo.studentAqUnsubLoans         = one.getStudentAqUnsubLoans();
        tdo.studentArFws                = one.getStudentArFws();
        tdo.studentAsScholarship1Name   = one.getStudentAsScholarship1Name();
        tdo.studentAtScholarship1Note   = one.getStudentAtScholarship1Note();
        tdo.studentAuScholarship1Amt    = one.getStudentAuScholarship1Amt();
        tdo.studentAvScholarship2Name   = one.getStudentAvScholarship2Name();
        tdo.studentAwScholarship2Note   = one.getStudentAwScholarship2Note();
        tdo.studentAxScholarship2Amt    = one.getStudentAxScholarship2Amt();
        tdo.studentAyScholarship3Name   = one.getStudentAyScholarship3Name();
        tdo.studentAzScholarship3Note   = one.getStudentAzScholarship3Note();
        tdo.studentBaScholarship3Amt    = one.getStudentBaScholarship3Amt();
        tdo.studentBbScholarship4Name   = one.getStudentBbScholarship4Name();
        tdo.studentBcScholarship4Note   = one.getStudentBcScholarship4Note();
        tdo.studentBdScholarship4Amt    = one.getStudentBdScholarship4Amt();
        tdo.studentBeScholarship5Name   = one.getStudentBeScholarship5Name();
        tdo.studentBfScholarship5Note   = one.getStudentBfScholarship5Note();
        tdo.studentBgScholarship5Amt    = one.getStudentBgScholarship5Amt();
        tdo.studentBhScholarship6Name   = one.getStudentBhScholarship6Name();
        tdo.studentBiScholarship6Note   = one.getStudentBiScholarship6Note();
        tdo.studentBjScholarship6Amt    = one.getStudentBjScholarship6Amt();
        tdo.studentBkScholarship7Name   = one.getStudentBkScholarship7Name();
        tdo.studentBlScholarship7Note   = one.getStudentBlScholarship7Note();
        tdo.studentBmScholarship7Amt    = one.getStudentBmScholarship7Amt();
        tdo.studentBnScholarship8Name   = one.getStudentBnScholarship8Name();
        tdo.studentBoScholarship8Note   = one.getStudentBoScholarship8Note();
        tdo.studentBpScholarship8Amt    = one.getStudentBpScholarship8Amt();
        tdo.studentBqScholarship9Name   = one.getStudentBqScholarship9Name();
        tdo.studentBrScholarship9Note   = one.getStudentBrScholarship9Note();
        tdo.studentBsScholarship9Amt    = one.getStudentBsScholarship9Amt();
                                                                           
        tdo.studentBtSupercounselor     = one.getStudentBtSupercounselor();
                                                                           
        tdo.studentBwProgress           = one.getStudentBwProgress();
        tdo.studentCbBanner             = one.getStudentCbBanner();
                                                                           
        tdo.studentEEmail               = one.getStudentEEmail();
        tdo.studentFPhone               = one.getStudentFPhone();
        tdo.studentFisy                 = one.getStudentFisy();
        tdo.studentGStreet              = one.getStudentGStreet();
        tdo.studentHCity                = one.getStudentHCity();
        tdo.studentIState               = one.getStudentIState();
        tdo.studentJZip                 = one.getStudentJZip();
        tdo.studentKCountry             = one.getStudentKCountry();
        tdo.studentLIntlStud            = one.getStudentLIntlStud();
        tdo.studentMMarry               = one.getStudentMMarry();
        tdo.studentNSda                 = one.getStudentNSda();
                                                                           
        tdo.studentOLastSchool          = one.getStudentOLastSchool();
        tdo.studentPGpa                 = one.getStudentPGpa();
        tdo.studentPassword             = one.getStudentPassword();
        tdo.studentQSat                 = one.getStudentQSat();
        tdo.studentQSatV                = one.getStudentQSatV();
        tdo.studentRAct                 = one.getStudentRAct();
        tdo.studentSMerit               = one.getStudentSMerit();
        tdo.studentStudType             = one.getStudentStudType();
        tdo.studentTMajor               = one.getStudentTMajor();
        tdo.studentTermEnd              = one.getStudentTermEnd();
        tdo.studentTermStart            = one.getStudentTermStart();
        tdo.studentUAcademic            = one.getStudentUAcademic();        
        tdo.studentVFamily              = one.getStudentVFamily();
        tdo.studentWDorm                = one.getStudentWDorm();
        tdo.studentXFafsa               = one.getStudentXFafsa();
        tdo.studentYIndept              = one.getStudentYIndept();
        tdo.studentZCalgrant            = one.getStudentZCalgrant();
        
        tdo.lostTime                = one.getLostTime();
        tdo.lostToLocal             = one.getLostToLocal(); //lostToLocal
        tdo.lostTz                  = one.getLostTz();
        tdo.studentBuOrigCounselor  = String.valueOf( one.getCounselorOrig());
        tdo.studentBvDoe            = new Date( one.getDdoe() );        
        tdo.studentBxModCounselor   = String.valueOf( one.getCounselorMod() );
        tdo.studentByDom            = new Date(one.getDdom());
        tdo.studentBzUploaded       = one.getStudentBzUploaded(); /////when set dou
        
        tdo.modPre                  = one.getModPre();
        tdo.modRoot                 = one.getModRoot();        
        
        tdo.ddoe                    = one.getDdoe();
        tdo.ddom                    = one.getDdom();
        
        tdo.tzdoe                   = one.getTzdoe();
        tdo.tzdom                   = one.getTzdom(); 
        
        tdo.prtTimes                = one.getPrtTimes();
        
        tdo.did                     = one.getDid();
        tdo.didstr                  = one.getDidstr();
        tdo.tzdid                   = one.getTzdid();
        
        tdo.returnStdInd            = one.getReturnStdInd();
        tdo.ncStdInd                = one.getNcStdInd();
        
        tdo.terms                   = one.getTerms();
        tdo.term_code1              = one.getTerm_code1();
        tdo.term_code2              = one.getTerm_code2();
        tdo.term_code3              = one.getTerm_code3();
        tdo.term_code4              = one.getTerm_code4();
        tdo.term_load1              = one.getTerm_load1();
        tdo.term_load2              = one.getTerm_load2();
        tdo.term_load3              = one.getTerm_load3();
        tdo.term_load4              = one.getTerm_load4();
        tdo.term_prog1              = one.getTerm_prog1();
        tdo.term_prog2              = one.getTerm_prog2();
        tdo.term_prog3              = one.getTerm_prog3();
        tdo.term_prog4              = one.getTerm_prog4();
        tdo.term_unit1              = one.getTerm_unit1();
        tdo.term_unit2              = one.getTerm_unit2();
        tdo.term_unit3              = one.getTerm_unit3();
        tdo.term_unit4              = one.getTerm_unit4();
        tdo.puser_id                = one.getPuser_id();
        tdo.ea_lsu_perc             = one.getEa_lsu_perc();
        tdo.ea_nonlsu_perc          = one.getEa_nonlsu_perc();
        tdo.std_1st_freshmen        = one.getStd_1st_freshmen();
        tdo.std_transfer_ind        = one.getStd_transfer_ind();
        
        
        return tdo;
    }
    private Student genClientStudFromTdo(TDOStudent tdo){
        Student one = new Student();
        one.setDdown(               tdo.ddown );
        one.setTzdown(              tdo.tzdown );

        one.setDup(                 tdo.dup);
        one.setTzup(                tdo.tzup);

        one.setRecid(               tdo.recid);
        one.setClientId(            tdo.clientId );
        one.setCounselorId(         tdo.counselorId);

        one.setPickupInd(           tdo.pickupInd);

        one.setSex(                 tdo.sex);
        if( tdo.counselorMod!=null)one.setCounselorMod(        tdo.counselorMod );
        one.setCounselorOrig(       tdo.counselorOrig);
        one.setEstmNumb(            tdo.estmNumb );

        one.setHomeAddrApt(         tdo.homeAddrApt);
        one.setHomecostudies(       tdo.homecostudies);
        one.setIndEalsu(            tdo.indEalsu );
        one.setIndEanonlsu(         tdo.indEanonlsu);
        one.setIndEfc(              tdo.indEfc );
        one.setIndExcloans(         tdo.indExcloans);
                                                                                                     
        one.setStudentNumb               (tdo.studentNumb                  );                
        one.setStudentUserName           (tdo.studentUserName              );                
        one.setStudentALsuid             (tdo.studentALsuid                );                
        one.setStudentBLastname          (tdo.studentBLastname             );                
        one.setStudentCFirstname         (tdo.studentCFirstname            );                
        one.setStudentDDob               (tdo.studentDDob                  );                
        one.setFund1id                   (tdo.fund1id                      );                
        one.setFund2id                   (tdo.fund2id                      );                
        one.setFund3id                   (tdo.fund3id                      );                
        one.setFund4id                   (tdo.fund4id                      );                
        one.setFund5id                   (tdo.fund5id                      );                
        one.setFund6id                   (tdo.fund6id                      );                
        one.setFund7id                   (tdo.fund7id                      );                
        one.setFund8id                   (tdo.fund8id                      );                
        one.setFund9id                   (tdo.fund9id                      );                
                                                                                             
        one.setAdjCalgrantInd            (tdo.adjCalgrantInd               );                
                                                                                             
        one.setStudentAaCalgrantA        (tdo.studentAaCalgrantA           );                
        one.setStudentAbCalgrantB        (tdo.studentAbCalgrantB           );                
        one.setStudentAcFamilySize       (tdo.studentAcFamilySize          );                
        one.setStudentAdFamilyIncome     (tdo.studentAdFamilyIncome        );                
        one.setStudentAeFamilyAsset      (tdo.studentAeFamilyAsset         );                
        one.setStudentAfFamilyContrib    (tdo.studentAfFamilyContrib       );                
        one.setStudentAgNonlsuAllowrance (tdo.studentAgNonlsuAllowrance    );                
        one.setStudentAiEduAllowPer      (tdo.studentAiEduAllowPer         );                
        one.setStudentAjHomeState        (tdo.studentAjHomeState           );                
        one.setStudentAkNoncalGrant      (tdo.studentAkNoncalGrant         );                
        one.setStudentAlOutScholarships  (tdo.studentAlOutScholarships     );                
        one.setStudentAmOutScholarshipAmt(tdo.studentAmOutScholarshipAmt   );                
        one.setStudentAnPubNotes         (tdo.studentAnPubNotes            );                
        one.setStudentAoPriNotes         (tdo.studentAoPriNotes            );                
        one.setStudentApSubLoans         (tdo.studentApSubLoans            );                
        one.setStudentAqUnsubLoans       (tdo.studentAqUnsubLoans          );                
        one.setStudentArFws              (tdo.studentArFws                 );                
        one.setStudentAsScholarship1Name (tdo.studentAsScholarship1Name    );                
        one.setStudentAtScholarship1Note (tdo.studentAtScholarship1Note    );                
        one.setStudentAuScholarship1Amt  (tdo.studentAuScholarship1Amt     );                
        one.setStudentAvScholarship2Name (tdo.studentAvScholarship2Name    );                
        one.setStudentAwScholarship2Note (tdo.studentAwScholarship2Note    );                
        one.setStudentAxScholarship2Amt  (tdo.studentAxScholarship2Amt     );                
        one.setStudentAyScholarship3Name (tdo.studentAyScholarship3Name    );                
        one.setStudentAzScholarship3Note (tdo.studentAzScholarship3Note    );                
        one.setStudentBaScholarship3Amt  (tdo.studentBaScholarship3Amt     );                
        one.setStudentBbScholarship4Name (tdo.studentBbScholarship4Name    );                
        one.setStudentBcScholarship4Note (tdo.studentBcScholarship4Note    );                
        one.setStudentBdScholarship4Amt  (tdo.studentBdScholarship4Amt     );                
        one.setStudentBeScholarship5Name (tdo.studentBeScholarship5Name    );                
        one.setStudentBfScholarship5Note (tdo.studentBfScholarship5Note    );                
        one.setStudentBgScholarship5Amt  (tdo.studentBgScholarship5Amt     );                
        one.setStudentBhScholarship6Name (tdo.studentBhScholarship6Name    );                
        one.setStudentBiScholarship6Note (tdo.studentBiScholarship6Note    );                
        one.setStudentBjScholarship6Amt  (tdo.studentBjScholarship6Amt     );                
        one.setStudentBkScholarship7Name (tdo.studentBkScholarship7Name    );                
        one.setStudentBlScholarship7Note (tdo.studentBlScholarship7Note    );                
        one.setStudentBmScholarship7Amt  (tdo.studentBmScholarship7Amt     );                
        one.setStudentBnScholarship8Name (tdo.studentBnScholarship8Name    );                
        one.setStudentBoScholarship8Note (tdo.studentBoScholarship8Note    );                
        one.setStudentBpScholarship8Amt  (tdo.studentBpScholarship8Amt     );                
        one.setStudentBqScholarship9Name (tdo.studentBqScholarship9Name    );                
        one.setStudentBrScholarship9Note (tdo.studentBrScholarship9Note    );                
        one.setStudentBsScholarship9Amt  (tdo.studentBsScholarship9Amt     );                
                                                                                             
        one.setStudentBtSupercounselor   (tdo.studentBtSupercounselor      );                
                                                                                             
        one.setStudentBwProgress         (tdo.studentBwProgress            );                
        one.setStudentCbBanner           (tdo.studentCbBanner              );                
                                                                                             
        one.setStudentEEmail             (tdo.studentEEmail                );                
        one.setStudentFPhone             (tdo.studentFPhone                );                
                                                                                             
        one.setStudentGStreet            (tdo.studentGStreet               );                
        one.setStudentHCity              (tdo.studentHCity                 );                
        one.setStudentIState             (tdo.studentIState                );                
        one.setStudentJZip               (tdo.studentJZip                  );                
        one.setStudentKCountry           (tdo.studentKCountry              );                
        one.setStudentLIntlStud          (tdo.studentLIntlStud             );                
        one.setStudentMMarry             (tdo.studentMMarry                );                
        one.setStudentNSda               (tdo.studentNSda                  );                
                                                                                             
        one.setStudentOLastSchool        (tdo.studentOLastSchool           );                
        one.setStudentPGpa               (tdo.studentPGpa                  );                
        one.setStudentPassword           (tdo.studentPassword              );                
        one.setStudentQSat               (tdo.studentQSat                  );                
        one.setStudentQSatV              (tdo.studentQSatV                 );                
        one.setStudentRAct               (tdo.studentRAct                  );                
        one.setStudentSMerit             (tdo.studentSMerit                );                
        one.setStudentStudType           (tdo.studentStudType              );                
        one.setStudentTMajor             (tdo.studentTMajor                );                
        one.setStudentTermEnd            (tdo.studentTermEnd               );                
        one.setStudentTermStart          (tdo.studentTermStart             );                
        one.setStudentUAcademic          (tdo.studentUAcademic             );                
        one.setStudentVFamily            (tdo.studentVFamily               );                
        one.setStudentWDorm              (tdo.studentWDorm                 );                
        one.setStudentXFafsa             (tdo.studentXFafsa                );                
        one.setStudentYIndept            (tdo.studentYIndept               );                
        one.setStudentZCalgrant          (tdo.studentZCalgrant             );                


        one.setLostTime(            tdo.lostTime );
        one.setLostToMaster(        tdo.lostToMaster );
        one.setLostTz(              tdo.lostTz );

        one.setStudentBzUploaded(   tdo.studentBzUploaded);
        one.setModPre(              tdo.modPre );
        one.setModRoot(             tdo.modRoot);

        one.setDdoe(                tdo.ddoe );
        one.setTzdoe(               tdo.tzdoe);
        one.setDdom(                tdo.ddom );        
        one.setTzdom(               tdo.tzdom);

        one.setPrtTimes(            tdo.prtTimes );        

        one.setCounselorOrig(tdo.counselorOrig);// Integer.valueOf( tdo.studentBuOrigCounselor ));
        if( tdo.counselorMod!=null)one.setCounselorMod(tdo.counselorMod);// Integer.valueOf(tdo.studentBxModCounselor) ); //.counselorMod
                          
        one.setDid( tdo.did);
        one.setDidstr( tdo.didstr);
        one.setTzdid( tdo.tzdid);
        
        one.setReturnStdInd( tdo.returnStdInd);
        one.setNcStdInd(  tdo.ncStdInd);
        
        one.setTerms(tdo.terms                    );
        one.setTerm_code1(tdo.term_code1               );
        one.setTerm_code2(tdo.term_code2               );
        one.setTerm_code3(tdo.term_code3               );
        one.setTerm_code4(tdo.term_code4               );
        one.setTerm_load1(tdo.term_load1               );
        one.setTerm_load2(tdo.term_load2               );
        one.setTerm_load3(tdo.term_load3               );
        one.setTerm_load4(tdo.term_load4               );
        one.setTerm_prog1(tdo.term_prog1               );
        one.setTerm_prog2(tdo.term_prog2               );
        one.setTerm_prog3(tdo.term_prog3               );
        one.setTerm_prog4(tdo.term_prog4               );
        one.setTerm_unit1(tdo.term_unit1               );
        one.setTerm_unit2(tdo.term_unit2               );
        one.setTerm_unit3(tdo.term_unit3               );
        one.setTerm_unit4(tdo.term_unit4               );
        one.setPuser_id(tdo.puser_id                   );
        one.setEa_lsu_perc(tdo.ea_lsu_perc              );
        one.setEa_nonlsu_perc(tdo.ea_nonlsu_perc           );
        one.setStd_1st_freshmen(tdo.std_1st_freshmen         );
        one.setStd_transfer_ind(tdo.std_transfer_ind         );
        
        return one;
    }

    public String getShowBaseMsg() {
        if( ref.getMaster_up_ind()>0)
            return showBaseMsg + " | " + showSyncMsg;
        else
            return ref.getJSFMsgByKey("MasterIsNotAvailable"); //msg['MasterIsNotAvailable']}
        
    }
    
}
