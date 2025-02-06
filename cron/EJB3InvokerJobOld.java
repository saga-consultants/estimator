/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.cron;

import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;
import org.quartz.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
/** 
 * 
 * @author [EMAIL PROTECTED] 
 * 
 */ 
public class EJB3InvokerJobOld implements StatefulJob{ 
        @SuppressWarnings("unused") 
        private final static String RCS_ID = "$Id: EJB3InvokerJob.java,v 1.6 2006/08/18 08:04:37 STNO Exp $"; 

        public static final String EJB_JNDI_NAME_KEY = "ejb"; 

        public static final String EJB_METHOD_KEY = "method"; 

        public static final String EJB_INTERFACE_CLASS = "interfaceClass";

        public static final String EJB_ARG_TYPES_KEY = "argTypes"; 

        public static final String EJB_ARGS_KEY = "args"; 

        public static final String INITIAL_CONTEXT_FACTORY = "java.naming.factory.initial"; 

        public static final String PROVIDER_URL = "java.naming.provider.url"; 

        Logger logger = LoggerFactory.getLogger();//getLogger(getClass().getName()); 
        
        public EJB3InvokerJobOld() { 
                super(); 
        } 

        public void execute(JobExecutionContext context) 
                        throws JobExecutionException { 
                JobDetail detail = context.getJobDetail(); 
                JobDataMap dataMap = detail.getJobDataMap(); 
                String ejb = dataMap.getString(EJB_JNDI_NAME_KEY); 
                String method = dataMap.getString(EJB_METHOD_KEY); 
                String interfaceClass = dataMap.getString(EJB_INTERFACE_CLASS); 
                logger.debug("Trying to execute {ejb=" + ejb + ",method=" 
                                + method + ",intefaceClass=" + interfaceClass + "}"); 
                Object[] arguments = (Object[]) dataMap.get(EJB_ARGS_KEY); 
                if (arguments == null) { 
                        arguments = new Object[0]; 
                } 
                if (ejb == null) { 
                        throw new JobExecutionException(); 
                } 
                InitialContext jndiContext = null; 

                try { 
                        jndiContext = getInitialContext(dataMap); 
                } catch (NamingException ne) { 
                        throw new JobExecutionException(ne); 
                } 
                Object value = null; 
                try { 
                        value = jndiContext.lookup(ejb); 
                } catch (NamingException ne) { 
                        throw new JobExecutionException(ne); 
                } 
                Class beanClass; 
                try { 
                        beanClass = Class.forName(interfaceClass); 
                } catch (ClassNotFoundException e) { 
                        throw new JobExecutionException(e); 
                } 

                Method methodExecute = null; 
                Class[] argTypes = (Class[]) dataMap.get(EJB_ARG_TYPES_KEY); 
                if (argTypes == null) { 
                        argTypes = new Class[arguments.length]; 
                        for (int i = 0; i < arguments.length; i++) { 
                                argTypes[i] = arguments.getClass(); 
                        } 
                } 
                // get all interfaces 
                Class[] interfaces = beanClass.getInterfaces(); 
                for (int i = 0; i < interfaces.length; i++) { 
                        try { 
                                // try to find the method and use the first interface if we do 
  //                              methodExecute = interfaces.getDeclaredMethod(method,    argTypes[i]); 
                             methodExecute = null;
                                break; 
                        } catch (Exception nsme) { 
                                // do nothing 
                        } 
                        throw new JobExecutionException("No interface with method " 
                                        + method + " declaired. Your Bean {" + beanClass.getName()  + "} has to implement the Local interface"); 
                } 

                try { 
                        methodExecute.invoke(value, arguments); 
                } catch (IllegalAccessException iae) { 
                        throw new JobExecutionException(iae); 
                } catch (InvocationTargetException ite) { 
                        throw new JobExecutionException(ite); 
                } 
              //[i][b]Replaced with 
           // value.getClass().getMethod(method, argTypes).invoke(value, 
           //                                                     arguments);[/b][/i] 
        } 

        @SuppressWarnings("unchecked") 
        private InitialContext getInitialContext(JobDataMap jobDataMap) 
                        throws NamingException { 
                Hashtable params = new Hashtable(2); 
                String initialContextFactory = jobDataMap 
                                .getString(INITIAL_CONTEXT_FACTORY); 
                if (initialContextFactory != null) { 
                        params.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory); 
                } 
                String providerUrl = jobDataMap.getString(PROVIDER_URL); 
                if (providerUrl != null) { 
                        params.put(Context.PROVIDER_URL, providerUrl); 
                } 
                return new InitialContext(params); 
        } 

} 