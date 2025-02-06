/*     */ package edu.lsu.estimator;
/*     */ 
/*     */ import com.kingombo.slf5j.Logger;
/*     */ import com.kingombo.slf5j.LoggerFactory;
/*     */ import edu.lsu.estimator.Credentials;
/*     */ import edu.lsu.estimator.Login;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ import javax.faces.FactoryFinder;
/*     */ import javax.faces.application.Application;
/*     */ import javax.faces.component.UIViewRoot;
/*     */ import javax.faces.context.FacesContext;
import javax.faces.*;
/*     */ import javax.faces.context.FacesContextFactory;
/*     */ import javax.faces.lifecycle.Lifecycle;
/*     */ import javax.faces.lifecycle.LifecycleFactory;
/*     */ import javax.naming.AuthenticationException;
/*     */ import javax.naming.AuthenticationNotSupportedException;
/*     */ import javax.naming.CommunicationException;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.InitialDirContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Hook
/*     */   extends HttpServlet
/*     */ {
/*  38 */   private static final Logger log = LoggerFactory.getLogger();
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
/*     */   protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*  51 */     response.setContentType("text/html;charset=UTF-8");
/*  52 */     PrintWriter out = response.getWriter();
/*     */     
/*     */     try {
/*  55 */       out.println("<html>");
/*  56 */       out.println("<head>");
/*  57 */       out.println("<title>Servlet Hook</title>");
/*  58 */       out.println("</head>");
/*  59 */       out.println("<body>");
/*  60 */       out.println("<h1>Servlet Hook at " + request.getContextPath() + "</h1>");
/*  61 */       out.println("</body>");
/*  62 */       out.println("</html>");
/*     */     } finally {
/*  64 */       out.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*  70 */     response.setContentType("text/html;charset=UTF-8");
/*  71 */     PrintWriter out = response.getWriter();
/*     */     
/*     */     try {
/*  74 */       out.println("<html>");
/*  75 */       out.println("<head>");
/*  76 */       out.println("<title>Estimator signin page for remote program</title>");
/*  77 */       out.println("</head>");
/*  78 */       out.println("<body>");
/*  79 */       out.println("<form id='rphook'  name='rphook' method='post' action='https://localhost/estimator/rphook'> ");
/*  80 */       out.println("<input id='user' name='user' type='text' value='' />");
/*  81 */       out.println("<input id='key' name='key' type='password' value='' />");
/*  82 */       out.println("<input id='btn' name='btn' type='submit' value='submit' />");
/*  83 */       out.println("</form>");
/*     */       
/*  85 */       out.println("</body>");
/*  86 */       out.println("</html>");
/*     */     } finally {
/*  88 */       out.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*  96 */     FacesContext facesContext = null;
/*     */     
/*  98 */     String user = request.getParameter("user");
/*  99 */     String key = request.getParameter("key");
/* 100 */     log.info("========= rphook post get user=%s ", new Object[] { user });
/*     */     
/* 102 */     if (user == null || user.trim().isEmpty() || key == null || key.trim().isEmpty()) {
/* 103 */       processGet(request, response);
/*     */       
/*     */       return;
/*     */     } 
/* 107 */     HttpSession session = request.getSession(false);
/* 108 */     if (session == null) session = request.getSession(true);
/*     */ 
/*     */     
/* 111 */     facesContext = getFacesContext(request, response);
/*     */     
/* 113 */     Credentials credentials = (Credentials)getManagedBean("credentials", facesContext);
/* 114 */     if (credentials == null) {
/* 115 */       log.info(" rphook could not get managed bean credentials. has to exit.");
/*     */       return;
/*     */     } 
/* 118 */     credentials.setUsername(user);
/* 119 */     credentials.setPassword(key);
/*     */ 
/*     */     
/* 122 */     Login login = (Login)getManagedBean("login", facesContext);
/* 123 */     if (login == null) {
/* 124 */       log.info(" rphook could not get managed bean login. has to exit.");
/*     */       
/*     */       return;
/*     */     } 
/* 128 */     String dest = login.login();
/* 129 */     if (dest == null || dest.isEmpty()) {
/* 130 */       log.info("rphook authen by login failed.");
/*     */       return;
/*     */     } 
/* 133 */     dest = "/estimator/view/" + dest;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     UIViewRoot view = facesContext.getApplication().getViewHandler().createView(facesContext, dest);
/* 139 */     facesContext.setViewRoot(view);
/*     */     
/* 141 */     log.info("========= rphook redirects to estimator page after authenticating user=%s ", new Object[] { user });
/* 142 */     int p = dest.indexOf("?");
/* 143 */     dest = dest.substring(0, p) + ".jsf";
/* 144 */     response.sendRedirect(dest);
/*     */   }
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
/*     */   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/* 191 */     processGet(request, response);
/*     */   }
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
/*     */   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/* 207 */     processPost(request, response);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServletInfo() {
/* 217 */     return "Short description";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FacesContext getFacesContext(HttpServletRequest request, HttpServletResponse response) {
/* 223 */     FacesContext facesContext = FacesContext.getCurrentInstance();
/* 224 */     if (facesContext == null) {
/*     */       
/* 226 */       FacesContextFactory contextFactory = (FacesContextFactory)FactoryFinder.getFactory("javax.faces.context.FacesContextFactory");
/* 227 */       LifecycleFactory lifecycleFactory = (LifecycleFactory)FactoryFinder.getFactory("javax.faces.lifecycle.LifecycleFactory");
/* 228 */       Lifecycle lifecycle = lifecycleFactory.getLifecycle("DEFAULT");
/*     */       
/* 230 */       facesContext = contextFactory.getFacesContext(request.getSession().getServletContext(), request, response, lifecycle);
/*     */ 
/*     */       
/* 233 */ //facesContext.setCurrentInstance(facesContext);
/*     */ 
/*     */       
/* 236 */       UIViewRoot view = facesContext.getApplication().getViewHandler().createView(facesContext, "");
/* 237 */       facesContext.setViewRoot(view);
/*     */     } 
/* 239 */     return facesContext;
/*     */   }
/*     */   public void removeFacesContext() {
/* 242 */    // InnerFacesContext.setFacesContextAsCurrentInstance(null);
/*     */   }
/*     */   protected Application getApplication(FacesContext facesContext) {
/* 245 */     return facesContext.getApplication();
/*     */   }
/*     */   protected Object getManagedBean(String beanName, FacesContext facesContext) {
/* 248 */     return getApplication(facesContext).getVariableResolver().resolveVariable(facesContext, beanName);
/*     */   }
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
/*     */   public int authenByLDAPS(String uid, String token, int std_ind) {
/* 261 */     int res = -1;
/* 262 */     Hashtable<String, Object> env = new Hashtable<>(11);
/* 263 */     env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
/*     */ 
/*     */     
/* 266 */     env.put("java.naming.provider.url", "ldaps://ldap.lasierra.edu:636");
/*     */     
/* 268 */     env.put("java.naming.security.protocol", "ssl");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 275 */     env.put("java.naming.ldap.version", "3");
/*     */ 
/*     */     
/* 278 */     env.put("java.naming.security.authentication", "simple");
/*     */ 
/*     */     
/* 281 */     env.put("java.naming.security.principal", "cn=" + uid + ((std_ind == 0) ? ",ou=employee," : ",ou=student,") + "ou=people, dc=lasierra, dc=edu");
/* 282 */     env.put("java.naming.security.credentials", token);
/*     */ 
/*     */ 
/*     */     
/* 286 */     env.put("com.sun.jndi.ldap.connect.timeout", "5000");
/*     */ 
/*     */     
/* 289 */     env.put("com.sun.jndi.ldap.read.timeout", "5000");
/*     */     
/*     */     try {
/* 292 */       DirContext ctx = new InitialDirContext(env);
/* 293 */       res = 1;
/*     */ 
/*     */       
/* 296 */       log.info("################# authenByLDAPS() Context Sucessfully Initialized. Binded. User %s is authenticated.", new Object[] { uid });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 302 */       ctx.close();
/* 303 */     } catch (AuthenticationException aex) {
/* 304 */       log.info("############## authenByLDAPS() AuthenticationException: %s", new Object[] { aex.getMessage() });
/*     */       
/* 306 */       res = 0;
/* 307 */     } catch (AuthenticationNotSupportedException anse) {
/* 308 */       log.info("############## authenByLDAPS() unsupported auth method: %s ", new Object[] { anse.getMessage() });
/* 309 */       res = -2;
/* 310 */     } catch (CommunicationException ste) {
/* 311 */       log.info("################# authenByLDAPS() Communication Exception : %s", new Object[] { ste.getMessage() });
/* 312 */       ste.printStackTrace();
/*     */       
/* 314 */       res = -3;
/* 315 */     } catch (NamingException e) {
/* 316 */       log.info("################# authenByLDAPS() naming exception: %s", new Object[] { e.getMessage() });
/* 317 */       e.printStackTrace();
/* 318 */       res = 0;
/* 319 */     } catch (Exception e) {
/* 320 */       log.info("################# authenByLDAPS()  exception: %s", new Object[] { e.getMessage() });
/* 321 */       e.printStackTrace();
/* 322 */       res = -4;
/*     */     } 
/*     */ 
/*     */     
/* 326 */     return res;
/*     */   }
/*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\Hook.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */