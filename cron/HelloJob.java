/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.cron;


import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 *
 * @author kwang
 *  a simple job that says "Hello" to the world.
 */
public class HelloJob implements Job {

    private static Logger _log = LoggerFactory.getLogger(HelloJob.class);
    
    /*Quartz requires a public empty constructor so that the
     * scheduler can instantiate the class whenever it needs.    
     */
    public HelloJob() {    }
    
    /**Called by the  org.quartz.Scheduler when a org.quartz.Trigger fires that is associated with  the Job.
     *
     * @throws JobExecutionException
     *     if there is an exception while executing the job.
     */    
 //   @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        //throw new UnsupportedOperationException("Not supported yet.");
      // Say Hello to the World and display the date/time
        _log.info("###### HelloJob exec() says: Hello World! - " + new Date());
    }
    
}
