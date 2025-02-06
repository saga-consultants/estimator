/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.cron;


import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;
import edu.lsu.estimator.AppReference;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kwang
 */
@DisallowConcurrentExecution 
public class JobChkLdap  implements Job {
    private static Logger log = LoggerFactory.getLogger();
    
    //@Inject 
            AppReference ref;
    @PersistenceContext private EntityManager em;
    
    private int ldapUp = 0;
    
    public JobChkLdap(){}    
    /*
    public static void main(String[] args){
        JobChkLdap worker = new JobChkLdap();
        try{                        
            //test obj up and down between c/s
            worker.pingLdap();
        }catch(Exception e){
            e.printStackTrace();
        }
    } */
    
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        //throw new UnsupportedOperationException("Not supported yet.");
      // Say Hello to the World and display the date/time
 //       log.info("...... JobChkLdap exec() says: Hello World!   " );//+ new Date());
        ref = (AppReference)jec.getJobDetail().getJobDataMap().get("ref"); 
        pingLdap();
        ref.setLdap_up_ind(ldapUp);
    }
    
    private void pingLdap(){
 //       log.info("~~~~~ pingLdap() will invoke ref.reachHostBySocket() with parameter %s %d", ref.getSeed().getLdapserver(), ref.getSeed().getLdapport());
        ldapUp = ref.reachHostBySocket(ref.getSeed().getLdapserver(), ref.getSeed().getLdapport() )? 1 : 0;
    }
}
