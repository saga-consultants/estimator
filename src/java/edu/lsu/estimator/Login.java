/*     */ package edu.lsu.estimator;

/*     */
 /*     */ import com.kingombo.slf5j.Logger;
/*     */ import com.kingombo.slf5j.LoggerFactory;
/*     */ import edu.lsu.estimator.Accessor;
/*     */ import edu.lsu.estimator.AppReference;
/*     */ import edu.lsu.estimator.Counselor;
/*     */ import edu.lsu.estimator.Credentials;
/*     */ import edu.lsu.estimator.LoggedIn;
/*     */ import edu.lsu.estimator.Logs;
/*     */ import edu.lsu.estimator.secu.DarkPlain;
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.enterprise.context.RequestScoped;
/*     */ import javax.enterprise.context.SessionScoped;
/*     */ import javax.enterprise.inject.Produces;
/*     */ import javax.faces.application.FacesMessage;
/*     */ import javax.faces.context.FacesContext;
/*     */ import javax.inject.Inject;
/*     */ import javax.inject.Named;
/*     */ import javax.persistence.EntityManager;
/*     */ import javax.persistence.PersistenceContext;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpSession;
          import javax.ws.rs.Path;
/*     */  import javax.ws.rs.GET;

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
 /*     */@Named("login")
/*     */ @SessionScoped
@Path("/login")
/*     */ public class Login
        /*     */     implements Serializable   /*     */ {

    /*     */ private static final long serialVersionUID = 1L;
    /*  49 */    private static final Logger log = LoggerFactory.getLogger();
    /*     */
 /*     */    @Inject
    /*     */ FacesContext facesContext;
    /*     */
 /*     */    @Inject
    /*     */ Credentials credentials;
    /*     */
 /*     */    @PersistenceContext
    /*     */ private EntityManager em;
    /*     */
 /*     */    @Inject
    /*     */ Accessor accessor;
    /*     */
 /*     */    @Inject
    /*     */ DarkPlain darkPlain;
    /*     */
 /*     */    @Inject
    /*     */ AppReference ref;
    /*     */
 /*     */    @Inject
    /*     */    @RequestScoped
    /*     */ HttpSession session;
    /*     */
 /*     */    private Counselor user;
    /*     */
 /*     */    private String signintimestr;
    /*     */
 /*     */    private boolean loggedin = false;
    /*     */
 /*     */    private boolean no_autosync;
             
              private boolean guestUser;
    /*     */
 /*     */ @PostConstruct
    /*     */ public void setAttrFromLazyInjects() {
        /*  83 */ this.no_autosync = !this.ref.isAutosync_ind();
        /*     */    }

    /*     */
 /*     */
 /*     */
        @GET
       @Path("/test")
        @javax.ws.rs.Produces("text/plain")
    public int test()
    {
    return 0;
    }

 /*     */ public String login() {
     
        /*  89 */ Logs login = new Logs(new Date(), "SESSION", "SIGNIN", (this.credentials.getUsername() == null || 
                this.credentials.getUsername().isEmpty()) ? "n/a" : this.credentials.getUsername(), "TRIED");
        /*  90 */ HttpServletRequest request = (HttpServletRequest) this.facesContext.getExternalContext().getRequest();
        /*  91 */ String stmp = request.getHeader("user-agent");
        /*  92 */ if (stmp != null && stmp.length() > 250) {
            stmp = stmp.substring(0, 250);
        }
        /*  93 */ login.setLocation(stmp);
        /*     */
 /*  95 */ stmp = login.getWhat();
        /*  96 */ stmp = stmp + "@" + request.getRemoteHost() + "(" + request.getRemoteAddr() + ")";
        /*  97 */ if (stmp != null && stmp.length() > 250) {
            stmp.substring(0, 250);
        }
        /*  98 */ login.setWhat(stmp);
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
 /* 113 */ int auth = 0;
        /* 114 */ Counselor found = null;
        /* 115 */ this.ref.reloadSeed();
        /* 116 */ List<Counselor> users = this.ref.getUsers();
        /* 117 */ if (users != null && users.size() > 0) {
            /* 118 */ for (Counselor one : users) {
                /* 119 */ if (one.getEmail().equalsIgnoreCase(this.credentials.getUsername())) {
                    /* 120 */ found = one;
                    /*     */ break;
                    /*     */                }
                /*     */            }
            /*     */        } else {
            /* 125 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "LoginForm.UsersDataNotReady");
            /* 126 */ this.facesContext.addMessage(null, msg);
            /* 127 */ log.info("login() can not auth user %s since the counselors table is empty", new Object[]{this.credentials.getUsername()});
            /* 128 */ return null;
            /*     */        }
        /*     */
 /*     */
 /* 132 */ if (found != null
                && /* 133 */ found.getUserid().intValue() == this.ref.getSys_counselor_id()) {
            /* 134 */ log.info("login() found user %s as system counselor in counselors table ", new Object[]{this.credentials.getUsername()});
            /* 135 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "LoginForm.NoSuchUser");
            /* 136 */ this.facesContext.addMessage(null, msg);
            /* 137 */ return null;
            /*     */        }
        /*     */
 /*     */
 /* 141 */ if (found == null) {
            /* 142 */ log.info("login() found user %s since the user is not in active counselors table ", new Object[]{this.credentials.getUsername()});
            /* 143 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "LoginForm.NoSuchUser");
            /* 144 */ this.facesContext.addMessage(null, msg);
            /* 145 */ return null;
            /*     */        }
        /*     */
 /*     */
 /* 149 */ if (this.ref.pingLdap() > 0) {
           /* 150 */ if (this.darkPlain == null) {
                /* 151 */ log.info("login() can not authenticate user %s since ldap program is not ready.", new Object[]{this.credentials.getUsername()});
                /* 152 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "LoginForm.NoAuthSrc");
                /*     */
 /*     */
 /* 155 */ this.facesContext.addMessage(null, msg);
                /* 156 */ return null;
                /*     */            }
            /*     */
 /*     */ try {
     //Enable or Disable LDAP here Sara
                /* 160 */ auth=this.darkPlain.authLDAPS(this.credentials.getUsername(), this.credentials.getPassword(), found); 
                          //auth = 1;
                /* 161 */            } catch (Exception ste) {
                /* 162 */ auth = 0;
                /*     */
 /* 164 */ FacesMessage msg = new FacesMessage();
                /* 165 */ msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                /* 166 */ msg.setDetail(ste.getMessage());
                /* 167 */ msg.setSummary(ste.getMessage());
                /*     */
 /*     */
 /*     */
 /* 171 */ this.facesContext.addMessage(null, msg);
                /* 172 */ return null;
                /*     */            }
            /* 174 */ log.info("login() got LDAP auth code == %d", new Object[]{Integer.valueOf(auth)});
            /*     */
 /* 176 */ if (auth == 1) {
                /* 177 */ log.info("@@@ login() tried to update/save shadow for identified user %s", new Object[]{this.credentials.getUsername()});
                /*     */
 /* 179 */ String hashPwd = this.ref.getCipher().hashString(this.credentials.getPassword());
                /* 180 */ found.setShadow(hashPwd);
                /*     */
 /* 182 */ String str = this.accessor.updateCounselorPwd(found);
                /* 183 */ log.info("@@@ login() tried to update/save shadow, msg=%s @@@@@@@@@@@@@@", new Object[]{str});
                /* 184 */ this.ref.reloadSeed();
                /*     */            }
            /*     */
 /*     */        } /*     */ else {
            /*     */
 /* 190 */ log.info("@@@ login() can not authenticate user %s with remote server since ldap server is not up. will try local source", new Object[]{this.credentials.getUsername()});            /* 191 */ String shadow = found.getShadow();
            /*     */
 /* 193 */ if (!this.ref.isEmp(shadow)) {
                /*     */
 /*     */
 /* 196 */ auth = this.ref.getCipher().hashString(this.credentials.getPassword()).equals(shadow) ? 1 : 0;
                /* 197 */ log.info("@@@ login() comp user %s local shadow and pwd digest, result=%d", new Object[]{this.credentials.getUsername(), Integer.valueOf(auth)});
                /*     */
 /*     */
 /*     */
 /* 201 */ if (auth == 0) {
                    /* 202 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "LoginForm.UseLocalShadowAndNoMatch");
                    /* 203 */ this.facesContext.addMessage(null, msg);
                    /* 204 */ auth = -1;
                    /*     */                }
                /*     */            } else {
                /* 207 */ log.info("@@@ login() can not authenticate user %s since local source has no shadow", new Object[]{this.credentials.getUsername()});
                /* 208 */ auth = 0;
                /*     */            }
            /*     */        }
      
        //auth = 1;
        /* 214 */ if (auth == 1 ) { //LDAP enable Sarav
            /* 215 */ this.user = found;
            /*     */
 /* 217 */ this.session.setAttribute("logintime", this.ref.getSimpleFmtStrNow());
            /* 218 */ login.setResult("ok");
            /*     */
 /* 220 */// this.accessor.saveLog(login);
 
 /* 244 */ if (this.user.getDeptName().equalsIgnoreCase("ENRL")) {
                /* 245 */ return "estimate-new?faces-redirect=true";
                /*     */            
 }
 /* 257 */ //return "query?faces-redirect=true"; actual
 return "query_new?faces-redirect=true";
            /*     */        }
        /*     */
 /*     */
 /*     */
 /*     */
 /* 263 */ if (auth == 0) {
         FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "LoginForm.NoMatch");
         
          this.facesContext.addMessage(null, msg);
            /*     */        }
 
 /*     */
 /* 263 */ if (auth < 0) {
         FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "LoginForm.NoAuthSrc");
         
          this.facesContext.addMessage(null, msg);
            /*     */        }
 /*     */
 /* 269 */ this.session.removeAttribute("logintime");
        /* 270 */ login.setResult("failed");
        /* 271 */ this.accessor.saveLog(login);
        /* 272 */ return null;
        /*     */    }

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
 /*     */
 /*     */
 /*     */ public String logout() {
        /* 324 */ this.user = null;
        /* 325 */ if (this.session != null) {
            this.session.invalidate();
        }
        /* 326 */ return "signout?faces-redirect=true";
        /*     */    }

    /*     */
 /*     */ public boolean chkLoggedIn() {
        /* 330 */ return (this.user != null);
        /*     */    }

    /*     */ public boolean isLoggedIn() {
       
        /* 333 */ return (this.user != null);
        /*     */    }
    
     public boolean isGuestLoggedIn() {
         
       
        /* 333 */ return (this.user != null);
        /*     */    }
//                  public boolean isGuestUser()
//    {
//   this.guestUser=true;
//   return true;//"estimate-new?faces-redirect=true";
//    }

    @Produces
    /*     */    @LoggedIn
    /*     */ Counselor getCurrentUser() {
        /* 337 */ return this.user;
        /*     */    }

    /*     */
 /*     */
 /*     */ public String getSignintimestr() {
        /* 342 */ if (this.user == null) {
            /* 343 */ return "";
            /*     */        }
        /* 345 */ return (String) this.session.getAttribute(this.signintimestr);
        /*     */    }

    /*     */
 /*     */
 /*     */ public boolean isNo_autosync() {
        /* 350 */ return this.no_autosync;
        /*     */    }

    /*     */
 /*     */ public void setNo_autosync(boolean no_autosync) {
        /* 354 */ this.no_autosync = no_autosync;
        /*     */    }
    /*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\Login.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
