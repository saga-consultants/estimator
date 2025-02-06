/*     */ package edu.lsu.estimator.cron;
/*     */ 
/*     */ import com.kingombo.slf5j.Logger;
/*     */ import com.kingombo.slf5j.LoggerFactory;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
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
/*     */ public class EJB3InvokerJobOld
/*     */   implements StatefulJob
/*     */ {
/*     */   private static final String RCS_ID = "$Id: EJB3InvokerJob.java,v 1.6 2006/08/18 08:04:37 STNO Exp $";
/*     */   public static final String EJB_JNDI_NAME_KEY = "ejb";
/*     */   public static final String EJB_METHOD_KEY = "method";
/*     */   public static final String EJB_INTERFACE_CLASS = "interfaceClass";
/*     */   public static final String EJB_ARG_TYPES_KEY = "argTypes";
/*     */   public static final String EJB_ARGS_KEY = "args";
/*     */   public static final String INITIAL_CONTEXT_FACTORY = "java.naming.factory.initial";
/*     */   public static final String PROVIDER_URL = "java.naming.provider.url";
/*  46 */   Logger logger = LoggerFactory.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(JobExecutionContext context) throws JobExecutionException {
/*     */     Class<?> beanClass;
/*  54 */     JobDetail detail = context.getJobDetail();
/*  55 */     JobDataMap dataMap = detail.getJobDataMap();
/*  56 */     String ejb = dataMap.getString("ejb");
/*  57 */     String method = dataMap.getString("method");
/*  58 */     String interfaceClass = dataMap.getString("interfaceClass");
/*  59 */     this.logger.debug("Trying to execute {ejb=" + ejb + ",method=" + method + ",intefaceClass=" + interfaceClass + "}");
/*     */     
/*  61 */     Object[] arguments = (Object[])dataMap.get("args");
/*  62 */     if (arguments == null) {
/*  63 */       arguments = new Object[0];
/*     */     }
/*  65 */     if (ejb == null) {
/*  66 */       throw new JobExecutionException();
/*     */     }
/*  68 */     InitialContext jndiContext = null;
/*     */     
/*     */     try {
/*  71 */       jndiContext = getInitialContext(dataMap);
/*  72 */     } catch (NamingException ne) {
/*  73 */       throw new JobExecutionException(ne);
/*     */     } 
/*  75 */     Object value = null;
/*     */     try {
/*  77 */       value = jndiContext.lookup(ejb);
/*  78 */     } catch (NamingException ne) {
/*  79 */       throw new JobExecutionException(ne);
/*     */     } 
/*     */     
/*     */     try {
/*  83 */       beanClass = Class.forName(interfaceClass);
/*  84 */     } catch (ClassNotFoundException e) {
/*  85 */       throw new JobExecutionException(e);
/*     */     } 
/*     */     
/*  88 */     Method methodExecute = null;
/*  89 */     Class[] argTypes = (Class[])dataMap.get("argTypes");
/*  90 */     if (argTypes == null) {
/*  91 */       argTypes = new Class[arguments.length];
/*  92 */       for (int j = 0; j < arguments.length; j++) {
/*  93 */         argTypes[j] = arguments.getClass();
/*     */       }
/*     */     } 
/*     */     
/*  97 */     Class[] interfaces = beanClass.getInterfaces();
/*  98 */     int i = 0; if (i < interfaces.length) {
/*     */       
/*     */       try {
/*     */         
/* 102 */         methodExecute = null;
/*     */       }
/* 104 */       catch (Exception exception) {
/*     */ 
/*     */         
/* 107 */         throw new JobExecutionException("No interface with method " + method + " declaired. Your Bean {" + beanClass
/* 108 */             .getName() + "} has to implement the Local interface");
/*     */       } 
/*     */     }
/*     */     try {
/* 112 */       methodExecute.invoke(value, arguments);
/* 113 */     } catch (IllegalAccessException iae) {
/* 114 */       throw new JobExecutionException(iae);
/* 115 */     } catch (InvocationTargetException ite) {
/* 116 */       throw new JobExecutionException(ite);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InitialContext getInitialContext(JobDataMap jobDataMap) throws NamingException {
/* 126 */     Hashtable<Object, Object> params = new Hashtable<>(2);
/*     */     
/* 128 */     String initialContextFactory = jobDataMap.getString("java.naming.factory.initial");
/* 129 */     if (initialContextFactory != null) {
/* 130 */       params.put("java.naming.factory.initial", initialContextFactory);
/*     */     }
/* 132 */     String providerUrl = jobDataMap.getString("java.naming.provider.url");
/* 133 */     if (providerUrl != null) {
/* 134 */       params.put("java.naming.provider.url", providerUrl);
/*     */     }
/* 136 */     return new InitialContext(params);
/*     */   }
/*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\cron\EJB3InvokerJobOld.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */