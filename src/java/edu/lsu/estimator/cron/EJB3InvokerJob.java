/*     */ package edu.lsu.estimator.cron;

/*     */
 /*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
/*     */ import org.quartz.JobDataMap;
/*     */ import org.quartz.JobDetail;
/*     */ import org.quartz.JobExecutionContext;
/*     */ import org.quartz.JobExecutionException;
/*     */ import org.quartz.StatefulJob;

/*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */ public class EJB3InvokerJob/*     */ implements StatefulJob /*     */ {

    /*     */ private static final String RCS_ID = "$Id: EJB3InvokerJob.java,v 1.7 2008/06/09 08:04:37 STNO Exp $";
    /*     */ public static final String EJB_JNDI_NAME_KEY = "ejb";
    /*     */ public static final String EJB_METHOD_KEY = "method";
    /*     */ public static final String EJB_ARG_TYPES_KEY = "argTypes";
    /*     */ public static final String EJB_ARGS_KEY = "args";
    /*     */ public static final String INITIAL_CONTEXT_FACTORY = "java.naming.factory.initial";
    /*     */ public static final String PROVIDER_URL = "java.naming.provider.url";
    /*     */ public static final String PRINCIPAL = "java.naming.security.principal";
    /*     */ public static final String CREDENTIALS = "java.naming.security.credentials";

    /*     */
 /*     */ public void execute(JobExecutionContext context) throws JobExecutionException {
        /* 49 */ JobDetail detail = context.getJobDetail();
        /* 50 */ JobDataMap dataMap = detail.getJobDataMap();
        /* 51 */ String ejb = dataMap.getString("ejb");
        /* 52 */ String method = dataMap.getString("method");
        /*     */
 /* 54 */ Object[] arguments = (Object[]) dataMap.get("args");
        /* 55 */ if (arguments == null) {
            /* 56 */ arguments = new Object[0];
            /*     */ }
        /* 58 */ if (ejb == null) {
            /* 59 */ throw new JobExecutionException();
            /*     */ }
        /* 61 */ InitialContext jndiContext = null;
        /*     */
 /*     */ try {
            /* 64 */ jndiContext = getInitialContext(dataMap);
            /* 65 */ } catch (NamingException ne) {
            /* 66 */ throw new JobExecutionException(ne);
            /*     */ }
        /* 68 */ Object value = null;
        /*     */ try {
            /* 70 */ value = jndiContext.lookup(ejb);
            /* 71 */ } catch (NamingException ne) {
            /* 72 */ throw new JobExecutionException(ne);
            /*     */ }
        /*     */
 /*     */
 /* 76 */ Class[] argTypes = (Class[]) dataMap.get("argTypes");
        /* 77 */ if (argTypes == null) {
            /* 78 */ argTypes = new Class[arguments.length];
            /* 79 */ for (int x = 0; x < arguments.length; x++) {
                /* 80 */ argTypes[x] = arguments[x].getClass();
                /*     */ }
            /*     */ }
        /*     */
 /*     */ try {
            /* 85 */ value.getClass().getMethod(method, argTypes).invoke(value, arguments);
            /*     */ } /* 87 */ catch (IllegalAccessException iae) {
            /* 88 */ throw new JobExecutionException(iae);
            /* 89 */ } catch (InvocationTargetException ite) {
            /* 90 */ throw new JobExecutionException(ite);
            /* 91 */ } catch (NoSuchMethodException e) {
            /* 92 */ throw new JobExecutionException(e);
            /*     */ }
        /*     */ }

    /*     */
 /*     */
 /*     */ private InitialContext getInitialContext(JobDataMap jobDataMap) throws NamingException {
        /* 98 */ Hashtable<Object, Object> params = new Hashtable<>(2);
        /* 99 */ String initialContextFactory = jobDataMap.getString("java.naming.factory.initial");
        /* 100 */ if (initialContextFactory != null) {
            /* 101 */ params.put("java.naming.factory.initial", initialContextFactory);
            /*     */ }
        /* 103 */ String providerUrl = jobDataMap.getString("java.naming.provider.url");
        /* 104 */ if (providerUrl != null) {
            /* 105 */ params.put("java.naming.provider.url", providerUrl);
            /*     */ }
        /* 107 */ String principal = jobDataMap.getString("java.naming.security.principal");
        /* 108 */ if (principal != null) {
            /* 109 */ params.put("java.naming.security.principal", principal);
            /*     */ }
        /* 111 */ String credentials = jobDataMap.getString("java.naming.security.credentials");
        /* 112 */ if (credentials != null) {
            /* 113 */ params.put("java.naming.security.credentials", credentials);
            /*     */ }
        /* 115 */ if (params.size() == 0) {
            /* 116 */ return new InitialContext();
            /*     */ }
        /* 118 */ return new InitialContext(params);
        /*     */ }
    /*     */ }

/*
 * Location:
 * D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\
 * estimator\cron\EJB3InvokerJob.class Java compiler version: 7 (51.0) JD-Core
 * Version: 1.1.3
 */
