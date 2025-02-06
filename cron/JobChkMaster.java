/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.cron;


import com.caucho.hessian.client.HessianProxyFactory;
import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;
import edu.lsu.estimator.AppReference;
import edu.lsu.estimator.tdo.SPIEstimatorMaster;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author kwang
 */
@DisallowConcurrentExecution 
public class JobChkMaster  implements Job {  //Job classes that Quartz uses are stateless. 
    private static Logger log = LoggerFactory.getLogger();
    
    private SecretKey aesKey = null;
    private SPIEstimatorMaster  hook = null;
    private int masterUp = 0;
    
    
    //@Inject 
            AppReference ref; //not enabled eligible for injection beans
    //@Inject HeartBeat ping;
 
            
    
    public JobChkMaster(){}
    
    /*
    @PostConstruct //never executed
    public void isref(){  
        log.info("~~~~~============############################# JobChkMaster postconstruct check ref==NULL ? %s", ref==null);        
        if( ref==null){
            FacesContext context = FacesContext.getCurrentInstance();
            //return (T) context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", Object.class);
            ref = (AppReference) context.getApplication().evaluateExpressionGet(context, "#{ref}", Object.class);
        }       
    }     
    */
    
    /*
    public static void main(String[] args){
        JobChkMaster worker = new JobChkMaster();
        try{
            //test obj up and down between c/s
            worker.pingMaster();
            worker.hookMaster();
            worker.testReqRes();
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        //throw new UnsupportedOperationException("Not supported yet.");
      // Say Hello to the World and display the date/time
 //       log.info("###### JobChkMaster exec() says: Hello World! - ");// + new Date());        
        ref = (AppReference)jec.getJobDetail().getJobDataMap().get("ref"); 
 
        
        //test obj up and down between c/s
        pingMaster();
        hookMaster();
        
        
        //testReqRes(); //sync is null
    }
    
    private void pingMaster(){
  //      log.info("~~~~~ pingMaster() check ref==NULL ? %s", ref==null);
         
        /*
        if( ref==null){
            FacesContext context = FacesContext.getCurrentInstance();
            //return (T) context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", Object.class);
            ref = (AppReference) context.getApplication().evaluateExpressionGet(context, "#{ref}", Object.class); //NPE
        }*/                     
                
 //       log.info("~~~~~ pingMaster() will invoke ref.reachHostBySocket() with parameter %s %s", ref.getSeed().getMastername(), ref.getSeed().getMasterport());       
        masterUp = ref.reachHostBySocket(ref.getSeed().getMastername(), Integer.parseInt( ref.getSeed().getMasterport()) )? 1 : 0;
        ref.setMaster_up_ind(masterUp);
    }
    
    private void hookMaster(){
        if( masterUp <=0){
            this.hook = null;
            log.error("~~~~~ hookMaster() got master up ind=%d. quit.", masterUp);
            return;
        }
        
        String url = ref.getSeed().getMasterurl();//;//"http://localhost:8080/master3/req"; // RPCWEBEJB   tunneled via 8080, so works better, than the standard IIOP way.
        HessianProxyFactory factory = new HessianProxyFactory();
        try{
            this.hook = (SPIEstimatorMaster) factory.create(SPIEstimatorMaster.class, url);
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
  //      assertNotNull(timeService);
    }
    
    /*
    private void testReqRes(){
        if( hook ==null){
            log.error(" ***** testReqRes() can not get master handler through Hessian factory. quit. ********* ");
            return;            
        }
        if( aesKey == null ){
            log.error(" ***** testReqRes() can not get AES key for secure communication. quit. ********* ");
            return;  
        }
        //collect client into
        TDOEstimatorReq req = new TDOEstimatorReq();
        HashMap<String, String> map = new HashMap<>();
        req.fisy = 2012;
        req.clientid=0;
        req.c_key = aesKey;
        req.clientclock = System.currentTimeMillis();
        req.clienttz = TimeZone.getDefault().getDisplayName( TimeZone.getDefault().inDaylightTime(new Date()), TimeZone.SHORT);
        req.clientversions = null; //"1.1.1.1"
        req.counselorid = 0;
        req.aesData = null; //byte[] for client cert, encrypted by aes encryptor
        req.last_stud_dod = 0L;
        req.last_prt_dod = 0L;
        
        TDOMasterRes<TDOCounselor> res = hook.init(req);
        
        if( res==null){
            log.info(" --- got empty master res.");
        }else{
        
            log.info(" --- got master res. code=%d, new clientid=%d",res.code,  res.clientInitNumb);
            if( res.clientInitNumb >0){
                log.info(" --- got master counselors. numb=%d, first one's name=%s", res.objs.size(), res.objs.get(0).username);
                log.info(" --- got master assigned client key=%s", sync.javaAESdecryptor(aesKey, res.aesData)); //NPE
            }
            if( res.msgs!=null){
                for(String msg : res.msgs)
                log.info(" --- got master res msg = %s", msg);
            }
        }         
    } */
    
    
}
