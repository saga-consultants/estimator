/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.cron;

import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;
import edu.lsu.estimator.AppReference;
import edu.lsu.estimator.tdo.SyncWorker;
import org.quartz.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

/**
 *
 * @author kwang
 */
@DisallowConcurrentExecution
public class JobSyncEJB3  implements Job{ //extends EJB3InvokerJob
    public static final String EJB_JNDI_NAME_KEY = "java:app/SyncService";
    //public static final String EJB_INTERFACE_NAME_KEY = "interfaceName";
    public static final String EJB_METHOD_KEY = "sync";

    public static final String EJB_ARG_TYPES_KEY = "argTypes";
    public static final String EJB_ARGS_KEY = "args";

    public static final String INITIAL_CONTEXT_FACTORY = "java.naming.factory.initial";
    public static final String PROVIDER_URL = "java.naming.provider.url";
    public static final String PRINCIPAL = "java.naming.security.principal";
    public static final String CREDENTIALS = "java.naming.security.credentials";

    private InitialContext initialContext = null;
        
    private final Logger log = LoggerFactory.getLogger();  //logger = Logger.getLogger(this.getClass());

    SyncWorker sync;
    AppReference ref;
     
    public JobSyncEJB3(){}
    
    @Override
    @SuppressWarnings("unchecked")
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException ex) {
            ;
        }
        //JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
	sync = (SyncWorker)jobExecutionContext.getJobDetail().getJobDataMap().get("sync"); 
        //sync = (SyncWorker)jobExecutionContext.getMergedJobDataMap().get("sync");
        //ref = (AppReference)jobExecutionContext.getJobDetail().getJobDataMap().get("ref"); 
        //sync = (SyncWorker)ref.findBean2("sync"); //facesContext.getApplication() returns NPE in ref
        
        if( sync!=null){        
            log.info("vvvvvvvvvv starting sync job vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            sync.sync();
            String msg = sync.getShowSyncMsg();
            if( msg==null || msg.isEmpty() || msg.startsWith("sync is done")){
                log.info("^^^^^^^^^^^^^^^^^^^^^^^^ sync job is done ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            }else{
                log.info("^^^^^^^^^^^^^^^^^^^^^^^^ sync job failed: %s", msg);
            }
        }else{
            log.info("xxxxxxxxx can not start sync job since SYNC obj is null");
        }
        /*
        String ejbJNDIName = EJB_JNDI_NAME_KEY;//dataMap.getString(EJB_JNDI_NAME_KEY);
        String methodName = EJB_METHOD_KEY;// dataMap.getString(EJB_METHOD_KEY);
        if (null == ejbJNDIName || ejbJNDIName.length() == 0) {
                throw new JobExecutionException("must specify ejb JNDI name");
        }
        
        Object[] arguments = (Object[]) dataMap.get(EJB_ARGS_KEY);
        if (arguments == null) {
                arguments = new Object[0];
        }

        Object ejb = locateEjb(dataMap,    methodName, arguments);
        
        Class[] argTypes = new Class[0];//(Class[]C) dataMap.get(EJB_ARG_TYPES_KEY);
        if (argTypes == null && arguments.length>0) {
                argTypes = new Class[arguments.length];
                for (int i = 0; i < arguments.length; i++) {
                        argTypes[i] = arguments[i].getClass();
                }
        }

        try {   //object.getClass().getMethod(methodName, argTypes).invoke(object,  arguments);
                Method methodToExecute = ejb.getClass().getDeclaredMethod(methodName, argTypes);
                Object returnObj = methodToExecute.invoke(ejb, arguments);

                jobExecutionContext.setResult(returnObj);
                
        }catch (IllegalAccessException iae) { 
            throw new JobExecutionException(iae); 
        } catch (InvocationTargetException ite) { 
            throw new JobExecutionException(ite); 
        } catch (NoSuchMethodException e) { 
            throw new JobExecutionException(e); 
        }finally {
                // Don't close jndiContext until after method execution because
                // WebLogic requires context to be open to keep the user credentials
                // available. See JIRA Issue: QUARTZ-401
                if (initialContext != null) {
                        try {
                                initialContext.close();
                        } catch (NamingException e) {
                                // Ignore any errors closing the initial context
                        }
                }
        }
                        
        */
        
        
        
        /*
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        dataMap.put(EJB_JNDI_NAME_KEY, "java:app/JobService");
        //dataMap.put(EJB_INTERFACE_NAME_KEY, "br.org.cni.pronatec.controller.service.JobServiceLocal");
        dataMap.put(EJB_METHOD_KEY, "buscaSistec");

        Object[] arguments = new Object[1];
        arguments[0] = jobExecutionContext.getTrigger().getStartTime();
        dataMap.put(EJB_ARGS_KEY, arguments);

        Class[] argumentTypes = new Class[1];
        argumentTypes[0] = Date.class;

        dataMap.put(EJB_ARG_TYPES_KEY, argumentTypes);
        super.execute(jobExecutionContext);
            
            
        //A simple solution would be to lookup the EJB via JNDI in the Job implementation.
        try {    
            final Context context = new InitialContext();
            myService= (MyService) context.lookup("java:global/my-app/myejbmodule-ejb/MyService");
        } catch (NamingException ex) {
            java.util.logging.Logger.getLogger(JobSyncEJB3.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    
    
    
	@SuppressWarnings("unchecked")
	private <T> T locateEjb(JobDataMap dataMap,     String methodName, Object[] arguments) throws JobExecutionException {		
		String ejbJNDIName = EJB_JNDI_NAME_KEY;//dataMap.getString(EJB_JNDI_NAME_KEY);
		Object object = null;		
		try {
			initialContext = getInitialContext(dataMap);
			object = initialContext.lookup(ejbJNDIName);			
			if (object == null) {
				throw new JobExecutionException("Cannot find " + ejbJNDIName);
			}			
		} catch (NamingException e) {
			throw new JobExecutionException(e);
		}		
                /*
		String ejbInterfaceName = dataMap.getString(EJB_INTERFACE_NAME_KEY);		
		Class ejbInterface = null;		
		try {
			ejbInterface = Class.forName(ejbInterfaceName);
		} catch (ClassNotFoundException e) {
			throw new JobExecutionException(e);
		}
		
		if (!ejbInterface.isAssignableFrom(object.getClass())) {
			object = PortableRemoteObject.narrow(object, ejbInterface);
		}
		*/
                
                
		return (T) object;
	}
	
	
	
	
	private InitialContext getInitialContext(JobDataMap jobDataMap) throws NamingException {

		Hashtable<String, String> params = new Hashtable<String, String>();
		
		String initialContextFactory = jobDataMap.getString(INITIAL_CONTEXT_FACTORY);

		if (initialContextFactory != null) {
			params.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
		}

		String providerUrl = jobDataMap.getString(PROVIDER_URL);

		if (providerUrl != null) {
			params.put(Context.PROVIDER_URL, providerUrl);
		}

		String principal = jobDataMap.getString(PRINCIPAL);

		if (principal != null) {
			params.put(Context.SECURITY_PRINCIPAL, principal);
		}

		String credentials = jobDataMap.getString(CREDENTIALS);

		if (credentials != null) {
			params.put(Context.SECURITY_CREDENTIALS, credentials);
		}

		return (params.size() == 0) ? new InitialContext() : new InitialContext(params);

	}

    
}


/******* the corresponding EJB is:
 @Stateless
@EJB(name="java:app/JobService", beanInterface=JobServiceLocal.class)
public class JobService implements JobServiceLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Override
    public void buscaSistec(Date dataAgendamento) {
    // Do something
    }
 */