/*     */ package edu.lsu.estimator.cron;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
/*     */ import javax.rmi.PortableRemoteObject;
/*     */ import org.quartz.Job;
/*     */ import org.quartz.JobDataMap;
/*     */ import org.quartz.JobExecutionContext;
/*     */ import org.quartz.JobExecutionException;
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
/*     */ 
/*     */ public class EJB3InvokerJobPatch
/*     */   implements Job
/*     */ {
/*     */   public static final String EJB_JNDI_NAME_KEY = "ejb";
/*     */   public static final String EJB_INTERFACE_NAME_KEY = "interfaceName";
/*     */   public static final String EJB_METHOD_KEY = "method";
/*     */   public static final String EJB_ARG_TYPES_KEY = "argTypes";
/*     */   public static final String EJB_ARGS_KEY = "args";
/*     */   public static final String INITIAL_CONTEXT_FACTORY = "java.naming.factory.initial";
/*     */   public static final String PROVIDER_URL = "java.naming.provider.url";
/*     */   public static final String PRINCIPAL = "java.naming.security.principal";
/*     */   public static final String CREDENTIALS = "java.naming.security.credentials";
/*  71 */   private InitialContext initialContext = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(JobExecutionContext context) throws JobExecutionException {
/*  82 */     JobDataMap dataMap = context.getMergedJobDataMap();
/*     */     
/*  84 */     String ejbJNDIName = dataMap.getString("ejb");
/*  85 */     String methodName = dataMap.getString("method");
/*  86 */     Object[] arguments = (Object[])dataMap.get("args");
/*     */     
/*  88 */     if (null == ejbJNDIName || ejbJNDIName.length() == 0) {
/*  89 */       throw new JobExecutionException("must specify ejb JNDI name");
/*     */     }
/*     */     
/*  92 */     if (arguments == null) {
/*  93 */       arguments = new Object[0];
/*     */     }
/*     */     
/*  96 */     Object ejb = locateEjb(dataMap);
/*     */     
/*  98 */     Class[] argTypes = (Class[])dataMap.get("argTypes");
/*     */     
/* 100 */     if (argTypes == null) {
/* 101 */       argTypes = new Class[arguments.length];
/* 102 */       for (int i = 0; i < arguments.length; i++) {
/* 103 */         argTypes[i] = arguments[i].getClass();
/*     */       }
/*     */     } 
/*     */     
/*     */     try {
/* 108 */       Method methodToExecute = ejb.getClass().getDeclaredMethod(methodName, argTypes);
/* 109 */       Object returnObj = methodToExecute.invoke(ejb, arguments);
/*     */       
/* 111 */       context.setResult(returnObj);
/* 112 */     } catch (Exception e) {
/* 113 */       throw new JobExecutionException(e);
/*     */ 
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */       
/* 120 */       if (this.initialContext != null) {
/*     */         try {
/* 122 */           this.initialContext.close();
/* 123 */         } catch (NamingException namingException) {}
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> T locateEjb(JobDataMap dataMap) throws JobExecutionException {
/* 135 */     String ejbJNDIName = dataMap.getString("ejb");
/*     */     
/* 137 */     Object object = null;
/*     */     
/*     */     try {
/* 140 */       this.initialContext = getInitialContext(dataMap);
/*     */       
/* 142 */       object = this.initialContext.lookup(ejbJNDIName);
/*     */       
/* 144 */       if (object == null) {
/* 145 */         throw new JobExecutionException("Cannot find " + ejbJNDIName);
/*     */       }
/*     */     }
/* 148 */     catch (NamingException e) {
/* 149 */       throw new JobExecutionException(e);
/*     */     } 
/*     */     
/* 152 */     String ejbInterfaceName = dataMap.getString("interfaceName");
/*     */     
/* 154 */     Class<?> ejbInterface = null;
/*     */     
/*     */     try {
/* 157 */       ejbInterface = Class.forName(ejbInterfaceName);
/* 158 */     } catch (ClassNotFoundException e) {
/* 159 */       throw new JobExecutionException(e);
/*     */     } 
/*     */     
/* 162 */     if (!ejbInterface.isAssignableFrom(object.getClass())) {
/* 163 */       object = PortableRemoteObject.narrow(object, ejbInterface);
/*     */     }
/*     */     
/* 166 */     return (T)object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InitialContext getInitialContext(JobDataMap jobDataMap) throws NamingException {
/* 174 */     Hashtable<String, String> params = new Hashtable<>();
/*     */     
/* 176 */     String initialContextFactory = jobDataMap.getString("java.naming.factory.initial");
/*     */     
/* 178 */     if (initialContextFactory != null) {
/* 179 */       params.put("java.naming.factory.initial", initialContextFactory);
/*     */     }
/*     */     
/* 182 */     String providerUrl = jobDataMap.getString("java.naming.provider.url");
/*     */     
/* 184 */     if (providerUrl != null) {
/* 185 */       params.put("java.naming.provider.url", providerUrl);
/*     */     }
/*     */     
/* 188 */     String principal = jobDataMap.getString("java.naming.security.principal");
/*     */     
/* 190 */     if (principal != null) {
/* 191 */       params.put("java.naming.security.principal", principal);
/*     */     }
/*     */     
/* 194 */     String credentials = jobDataMap.getString("java.naming.security.credentials");
/*     */     
/* 196 */     if (credentials != null) {
/* 197 */       params.put("java.naming.security.credentials", credentials);
/*     */     }
/*     */     
/* 200 */     return (params.size() == 0) ? new InitialContext() : new InitialContext(params);
/*     */   }
/*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\cron\EJB3InvokerJobPatch.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */