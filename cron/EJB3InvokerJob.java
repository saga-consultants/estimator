/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.cron;


import org.quartz.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;


/** 
 * 
 * @author ola.hamfors@... 
 * @author scottbjer@... - Updated 
 */ 
public class EJB3InvokerJob implements StatefulJob { 
    @SuppressWarnings("unused") 
    private static final String RCS_ID =         "$Id: EJB3InvokerJob.java,v 1.7 2008/06/09 08:04:37 STNO Exp $"; 

    public static final String EJB_JNDI_NAME_KEY = "ejb"; 
    public static final String EJB_METHOD_KEY = "method"; 

    public static final String EJB_ARG_TYPES_KEY = "argTypes"; 
    public static final String EJB_ARGS_KEY = "args"; 

    public static final String INITIAL_CONTEXT_FACTORY =         "java.naming.factory.initial"; 
    public static final String PROVIDER_URL = "java.naming.provider.url"; 
    public static final String PRINCIPAL = "java.naming.security.principal"; 
    public static final String CREDENTIALS =         "java.naming.security.credentials"; 


    public EJB3InvokerJob() { 
        super(); 
    } 

    public void execute(JobExecutionContext context) throws JobExecutionException { 
        JobDetail detail = context.getJobDetail(); 
        JobDataMap dataMap = detail.getJobDataMap(); 
        String ejb = dataMap.getString(EJB_JNDI_NAME_KEY); 
        String method = dataMap.getString(EJB_METHOD_KEY); 
        
        Object[] arguments = (Object[])dataMap.get(EJB_ARGS_KEY); 
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

        
        Class[] argTypes = (Class[])dataMap.get(EJB_ARG_TYPES_KEY); 
        if (argTypes == null) { 
            argTypes = new Class[arguments.length]; 
            for (int x = 0; x < arguments.length; x++) { 
                argTypes[x] = arguments[x].getClass(); 
            } 
        } 

        try { 
            value.getClass().getMethod(method, argTypes).invoke(value, 
                                                                arguments); 
        } catch (IllegalAccessException iae) { 
            throw new JobExecutionException(iae); 
        } catch (InvocationTargetException ite) { 
            throw new JobExecutionException(ite); 
        } catch (NoSuchMethodException e) { 
            throw new JobExecutionException(e); 
        } 
    } 

    @SuppressWarnings("unchecked") 
    private InitialContext getInitialContext(JobDataMap jobDataMap) throws NamingException { 
        Hashtable params = new Hashtable(2); 
        String initialContextFactory =             jobDataMap.getString(INITIAL_CONTEXT_FACTORY); 
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
        if (params.size() == 0) { 
            return new InitialContext(); 
        } else { 
            return new InitialContext(params); 
        } 
    } 


} 