/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;


import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;
import edu.lsu.estimator.secu.DarkPlain;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author kwang
 */

//@Model is only request scoped
@Named("login")
@SessionScoped
public class Login implements Serializable {
    private static final long serialVersionUID = 1L;
    
   /////@Inject static FormatLogger  log;    
    private  static final Logger log = LoggerFactory.getLogger();
    
   @Inject FacesContext facesContext;
      
   @Inject
   edu.lsu.estimator.Credentials credentials;
  // @Inject @UserDatabase    EntityManager em;
   
   @PersistenceContext    private EntityManager em;
   @Inject
   edu.lsu.estimator.Accessor accessor;
   @Inject DarkPlain darkPlain;
   
   @Inject
   edu.lsu.estimator.AppReference ref;
   
   @Inject @RequestScoped HttpSession session; //@SessionScoped
   //lazy to initiate(or inject) when is invoked/used in the code.
   //WELD-000053 Producers cannot declare passivating scope and return a non-serializable class:  [method] @Produces @SessionScoped public edu.lsu.estimator.utilFactory.getGenHttpSession()
      
   private Counselor user;
    
   private String signintimestr;
   
   private boolean loggedin = false;
   //#{login.loggedin} always cause
   //The class 'edu.lsu.estimator.org$jboss$weld$bean-web-ManagedBean-class_edu$lsu$estimator$Login_$$_WeldClientProxy' does not have the property 'loggedin'.
   
   
   private boolean no_autosync;
   
   public Login(){
       //no_autosync = !ref.isAutosync_ind(); //java.lang.NullPointerException
   }
   
   @PostConstruct
    public void setAttrFromLazyInjects(){
        no_autosync = !ref.isAutosync_ind(); 
        // WELD-000049 Unable to invoke [method] @PostConstruct public edu.lsu.estimator.AppReference.initSeed() on edu.lsu.estimator.AppReference@3e4492 
    }
   
    public String login() {
        //if( user.getId().equals( "test")){
        Logs login = new Logs( new java.util.Date(), "SESSION", "SIGNIN", (credentials.getUsername()==null 
                || credentials.getUsername().isEmpty()? "n/a":credentials.getUsername()) , "TRIED" );
        HttpServletRequest request = (HttpServletRequest)facesContext.getExternalContext().getRequest();
        String stmp = request.getHeader("user-agent");
        if( stmp!=null && stmp.length()>250) stmp = stmp.substring(0, 250); //DB column limit
        login.setLocation( stmp );
        
        stmp = login.getWhat();
        stmp = stmp + "@"+request.getRemoteHost()+"("+ request.getRemoteAddr()+")";
        if( stmp!=null && stmp.length()>250) stmp.substring(0, 250);
        login.setWhat(stmp);
    
        int auth = 0;  
        Counselor found =null;
        ref.reloadSeed();
        List<Counselor> users = ref.getUsers();
        if( users!=null && users.size()>0){             
            for( Counselor one: users){ 
                if( one.getEmail().equalsIgnoreCase( credentials.getUsername())){
                    found = one;
                    break;
                } 
            }
        }else{
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "LoginForm.UsersDataNotReady"); 
            facesContext.addMessage(null, msg); //no UIcomponent ID, so it will be a global message
            log.info("login() can not auth user %s since the counselors table is empty", credentials.getUsername());
            return null;
        }
        
        //shall not use sys counselor id
        if( found !=null){
            if( found.getUserid()==ref.getSys_counselor_id()){
                log.info("login() found user %s as system counselor in counselors table ", credentials.getUsername());
                FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "LoginForm.NoSuchUser"); 
                facesContext.addMessage(null, msg); //no UIcomponent ID, so it will be a global message
                return null;
            }
        }
        
        if( found ==null){
            log.info("login() found user %s since the user is not in active counselors table ", credentials.getUsername());
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "LoginForm.NoSuchUser"); 
            facesContext.addMessage(null, msg); //no UIcomponent ID, so it will be a global message
            return null;
        } 
            

        auth=1;
        if( auth ==1){            
            user = found;//results.get(0);
            //user.setSigninDate(new Date());
            session.setAttribute("logintime", ref.getSimpleFmtStrNow());
            login.setResult("ok");  
            
 accessor.saveLog(login);
 

 
            if( user.getDeptName().equalsIgnoreCase("ENRL")){
                return "estimate-new?faces-redirect=true";
             
            }else{
                return "query?faces-redirect=true";     
                //return sb.append("query.xhtml?faces-redirect=true&includeViewParams=true").toString(); same page
                
            }            
            
        }else{
            if( auth==0){
                FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "LoginForm.NoMatch"); 
                //java.util.MissingResourceException: Can't find bundle for base name msg, locale en_US            
                facesContext.addMessage(null, msg); //no UIcomponent ID, so it will be a global message
            }
            
            session.removeAttribute("logintime");
            login.setResult("failed"); 
            accessor.saveLog(login);
            return null;
        }
        

    }
    
    
    
    
    
    public String logout() {
        user = null;
        if( session!=null)session.invalidate();
        return "signout?faces-redirect=true";
    }
     
    public boolean chkLoggedIn() { //"#{login.chkLoggedin()}": Method chkLoggedin not found
        return user != null;
    } 
    public boolean isLoggedIn() {
        return user != null;
    } 
    
    @Produces @LoggedIn Counselor getCurrentUser() {
        return user;
    }
    
    
    public String getSignintimestr() {
        if( user==null){
            return "";
        }else{
            return (String)session.getAttribute(signintimestr);
        }        
    }

    public boolean isNo_autosync() {
        return no_autosync;
    }

    public void setNo_autosync(boolean no_autosync) {
        this.no_autosync = no_autosync;
    }

}

//@LoggedIn and @UserDatabase are custom qualifier annota
