/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.naming.*;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

/**
 *
 * @author kwang
 */
public class Hook extends HttpServlet {
    private  static final Logger log = LoggerFactory.getLogger();
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Hook</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Hook at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }

    protected void processGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Estimator signin page for remote program</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<form id='rphook'  name='rphook' method='post' action='https://localhost/estimator/rphook'> ");
            out.println("<input id='user' name='user' type='text' value='' />");
            out.println("<input id='key' name='key' type='password' value='' />");
            out.println("<input id='btn' name='btn' type='submit' value='submit' />");
            out.println("</form>");
            //out.println("<h1>Servlet Hook at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }
    
    protected void processPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();
        FacesContext facesContext = null;
        try {
            String user = request.getParameter("user");
            String key = request.getParameter("key");
            log.info("========= rphook post get user=%s ", user );
            
            if( user==null || user.trim().isEmpty() || key==null || key.trim().isEmpty()){
                processGet(request, response);
                return;
            }
            
            HttpSession session = (HttpSession)request.getSession(false);
            if( session==null ) session = (HttpSession)request.getSession(true);
            
            //session.setMaxInactiveInterval(0);
             facesContext = getFacesContext(request, response);
            //credentials
            Credentials credentials = (Credentials)getManagedBean("credentials", facesContext);
            if( credentials==null){
                log.info(" rphook could not get managed bean credentials. has to exit.");
                return;
            }
            credentials.setUsername(user);
            credentials.setPassword(key);
            
            //login
            Login login = (Login)getManagedBean("login", facesContext);
            if( login==null){
                log.info(" rphook could not get managed bean login. has to exit.");
                return;
            }
            
            String dest = login.login();
            if( dest==null || dest.isEmpty()){
                log.info("rphook authen by login failed.");
                return;
            }
            dest = "/estimator/view/" + dest;
            
            //will be "query?faces-redirect=true";  or "estimator-new?faces-redirect=true"; 
            //########## The requested resource doesn't exist.
            
            UIViewRoot view = facesContext.getApplication().getViewHandler().createView(facesContext,   dest);
            facesContext.setViewRoot(view);                
            
            log.info("========= rphook redirects to estimator page after authenticating user=%s ", user );
            int p = dest.indexOf("?");
            dest = dest.substring(0, p) + ".jsf";
            response.sendRedirect( dest);
             
            /*
            //@@@@ bean forward
            try{                 
                response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
                RequestDispatcher dispatcher = request.getRequestDispatcher( dest  ); //"/process"
                 
                dispatcher.forward(request, response);
                facesContext.responseComplete(); //java.lang.IllegalStateException
            } catch (ServletException ex) {
                log.info("", ex);
            } catch (IOException ex) {
                log.info("", ex);
            }
              
            //@@@@ servlet
            RequestDispatcher dispatcher = request.getRequestDispatcher( dest ); //request.getParameter("target")
            dispatcher.forward(request, response); ////########## The requested resource doesn't exist.
            */
                        
      //      facesContext.getExternalContext().dispatch(dest);
            //There will be no view-root until after the RESTORE_VIEW phase of the JSF lifecycle,
            // faces context returned null viewId, using either the getViewRoot().getViewId(), and Faces api threw NPE.
            
        } finally {            
           // out.close();
           // if(  facesContext !=null) 
     ///           removeFacesContext();
        }
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        processGet(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        processPost(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    
    
     protected FacesContext getFacesContext(HttpServletRequest request, HttpServletResponse response) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {

            FacesContextFactory contextFactory  = (FacesContextFactory)FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
            LifecycleFactory lifecycleFactory = (LifecycleFactory)FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY); 
            Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

            facesContext = contextFactory.getFacesContext(request.getSession().getServletContext(), request, response, lifecycle);

            // Set using our inner class
            InnerFacesContext.setFacesContextAsCurrentInstance(facesContext);

            // set a new viewRoot, otherwise context.getViewRoot returns null
            UIViewRoot view = facesContext.getApplication().getViewHandler().createView(facesContext, "");
            facesContext.setViewRoot(view);                
        }
        return facesContext;
    }
    public void removeFacesContext() {
        InnerFacesContext.setFacesContextAsCurrentInstance(null);
    }
    protected Application getApplication(FacesContext facesContext) {
        return facesContext.getApplication();        
    }
    protected Object getManagedBean(String beanName, FacesContext facesContext) {        
        return getApplication(facesContext).getVariableResolver().resolveVariable(facesContext, beanName);
    }
    
    // You need an inner class to be able to call FacesContext.setCurrentInstance
    // since it's a protected method
    private abstract static class InnerFacesContext extends FacesContext {
        protected static void setFacesContextAsCurrentInstance(FacesContext facesContext) {
            FacesContext.setCurrentInstance(facesContext);
        }
    }   
    
    
    public int authenByLDAPS(String uid, String token, int std_ind){ //mycampus uses uid, should be cn, since cn is used to compose DN)
        int res = -1;
        Hashtable<String, Object> env = new Hashtable<String, Object>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY,    "com.sun.jndi.ldap.LdapCtxFactory");
        
         
        env.put(Context.PROVIDER_URL, "ldaps://ldap.lasierra.edu:636"); // ldaps@port 389: javax.net.ssl.SSLHandshakeException: Remote host closed connection during handshake
        // request the use of SSL via the use of LDAPS URLs.  ldaps means using ldap protocol with ssl
        env.put(Context.SECURITY_PROTOCOL, "ssl"); //makes no diff for ldap and ldaps         
        //env.put(Context.PROVIDER_URL, "ldap://ldap.lasierra.edu:389");
                
        //trace ldap behavior to an instance of java.io.OutputStream
//        env.put("com.sun.jndi.ldap.trace.ber", System.err);
        
        //By default, the LDAP provider first uses version 3 to communicate with the specified LDAP server, then auto downgrade to 2 if v3 not work
        env.put("java.naming.ldap.version", "3"); 
        
        // Authenticate as  User and password  
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        
        // (user.getStdInd()==0 ?
        env.put(Context.SECURITY_PRINCIPAL,  "cn="+uid+  (std_ind ==0 ? ",ou=employee,":",ou=student,")+ "ou=people, dc=lasierra, dc=edu"); //cn=S. User, ou=NewHires, o=JNDITutorial
        env.put(Context.SECURITY_CREDENTIALS, token);
        
        //-Dcom.sun.jndi.ldap.connect.pool.timeout=300000
        //environment property com.sun.jndi.ldap.connect.timeout that sets the timeout for connecting to the server
         env.put("com.sun.jndi.ldap.connect.timeout", "5000");
                  
        //cutoff the LDAP response from the server after the initial connection is established with the server.
        env.put("com.sun.jndi.ldap.read.timeout", "5000"); //microseconds
        try {
            // Create initial context
            DirContext ctx = new InitialDirContext(env); 
            res = 1;
            //System.out.println("authLDAPS() Context Sucessfully Initialized. Binded. User "+who+" is authenticated.");
        
            log.info("################# authenByLDAPS() Context Sucessfully Initialized. Binded. User %s is authenticated.", uid);
            
            //do sth with the conn or ctx
            //System.out.println(ctx.lookup("ou=NewHires"));
            
            // Close the context when we're done
            ctx.close();
        } catch(AuthenticationException aex) { //[LDAP: error code 49 - Invalid Credentials]
            log.info("############## authenByLDAPS() AuthenticationException: %s", aex.getMessage());   
            //aex.printStackTrace();
            res = 0;
        }catch( AuthenticationNotSupportedException anse){
            log.info("############## authenByLDAPS() unsupported auth method: %s ", anse.getMessage());
            res=-2;
        } catch(  CommunicationException  ste){
            log.info("################# authenByLDAPS() Communication Exception : %s", ste.getMessage());
            ste.printStackTrace();
//        }catch(SocketTimeoutException | ConnectException | CommunicationException  ste){
            res = -3;
        } catch (NamingException e) {//if no network: SEVERE: javax.naming.CommunicationException: ldap.lasierra.edu:636 [Root exception is java.net.UnknownHostException: ldap.lasierra.edu]
            log.info("################# authenByLDAPS() naming exception: %s", e.getMessage());
            e.printStackTrace();
            res = 0;        
        }catch( Exception e){//ConnectException ce){//: Connection timed out) 
            log.info("################# authenByLDAPS()  exception: %s", e.getMessage());
            e.printStackTrace();
            res = -4;
        }
        
        //CommunicationException  timeout
        return res;
       
    }
}
