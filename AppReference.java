/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;
import edu.lsu.estimator.cron.HeartBeat;
import edu.lsu.estimator.secu.PwdEncryptor;
import org.jasypt.digest.PooledStringDigester;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.*;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 *
 * @author kwang
 */
@ApplicationScoped
@Named("ref")
public class AppReference {    
//global settings, shared among all users/sessions
    private int test_ind = 0;
    private int fiscal_year = 2018;//2012;
    private String faid_year = "2024 ~ 2025";//  "2023 ~ 2024" //"2012 ~ 2013"; // old 2013 ~ 2014
    private String verString = "Version 3.0.0.0";
    private NumberFormat fmtInt = new DecimalFormat("$#,###");
    
    private  static final Logger log = LoggerFactory.getLogger();
    private String PUNAME = "estimator";
    @PersistenceContext(unitName="estimator") private EntityManager em; //(unitName="master3PU")     
    
    private int clientid   = -1; //the global var. if it is 0, means it is not initialized yet.
    private String clientverions = "1.1.1";
    private String fullclientverions = "1.1.1.1"; 
    
    private int lsuid_max_length = 6;
    private int max_popup_items = 10;
    private final int sys_counselor_id = 0;
    
    private NumberFormat moneyFmt = new DecimalFormat("$#,###");
        
    public enum LogType {SYNC, SESSION, ESTIMATE    }
    public enum LogCatagoty {UPLOAD, DOWNLOAD, UPGRADE, SIGNIN, SIGNOUT, CANCEL, AUTOSYNC, HANDSYNC, NEWSTUD, LOADSTUD, SAVESTUD, QUERYSTUD, VIEWSTUD, LISTSTUD, MODSTUD,PRINT, EMAIL, EFILE}
    
    private boolean autosync_ind = true;
    
    @Inject private ResourceBundle resourceBundle;
    
    private String sdateFmtStr = "M/d/yyyy";
    private String mdateFmtStr = "MMMM d, yyyy";
    private String timeFmtStr = "M/d/yyyy H:m:s";
    private String derbyDateFmt="yyyy-MM-dd";
    
    private String timeShowFmtStr = "M/d/yy h:mm a z"; //Tue Oct 18 00:00:00 PDT 2011 ?????  Thu Sep 08 00:00:00 PDT 2011
    
    private String timeShrtShowFmtStr = "MM/dd/yy HH:mm"; //h:mm a
    private String timeInfoShowFmtStr = "EEE, MMM d, h:mm a z, yyyy";
    
    private String dateInputShowStr = "MM/dd/yyyy";
    private String simpleDtFmtStr = "EEE, MMM d, h:mm a z"; //"EEE, MMM d, h:ma, z" -->> Thu, Aug 11, 8:00 AM PDT
    //new SimpleDateFormat( )
    //String stringDate = "Friday, December 31, 2010 11:06:42 AM WST";
    //DateFormat formatter = new SimpleDateFormat("E, dd MMM, yyyy hh:mm:ss a Z");
    
    //@producer w/parameter from injectee
    //fmtDateStr (date, fmtStr)
    
    private int indept_age = 24;//was 23; Esther confirmed it should be 24 for 2013
    private Date indept_dob=null;
    private int app_young_limit=12;
    private int app_old_limit=95;
    
    
    private int optSyncOnLogin=1;
    
    private LinkedHashMap<String, String> studtypes = new LinkedHashMap<>();
    private String[] studtype_vals   = {"ALL", "FR","F2", "SO", "JR", "SR","MBA",  "UD", "UI", "ESL", "CJ", "G", "PG"};
    private String[] studtype_labels = {"All Student Types", "Freshman","Continuing Freshman","Sophomore","Junior","Senior","MBA Student", "UnderGraduate-Domestic", "UnderGraduate-International", "ESL", "CriminalJustice","Graduate", "PostGraduate"};
    private HashMap<String, String> studtypesMap = new HashMap<>();
    
    private List<Fund> funds = new ArrayList<Fund>();
    private Integer[] funds_alone = {125, 122, 123, 120, 124, 121};
    private int alone_funds_amt = funds_alone.length;
    private int total_funds_amt = 9;
    private List<SelectItem> a_funds;
    
    private int fund_name_max = 70;
    private int fund_note_max= 256;
    
    // A managed bean with a public field ("digester") should not declares any scope other than @Dependent.  
    //public static PooledStringDigester digester = null;
    private PooledStringDigester digester = null;
    
    private TimeZone tz;
    private String tzSN;
    
        
    private Config seed;
    //system wide var for client id, read from seed record
//    private int seed_clientid=-1; //0: needs init   -1: no seed  >0: initialized
    
    //system wide indicators 0==not set/not ready  1:set/ready  -1: no properly set
    
    private int sys_clk_ind=1;
    private int sys_blk_ind=1; //blocked?
    private int sys_cfg_ind=0; //configuration validation    
    private int sys_usr_ind=0; //counselors table populated
   // private int sys_pwd_ind=0; //counselor's MD5 hashed password
    private List<Counselor> users ;
    
    private int ldap_up_ind =0;
    private int master_up_ind = 0;
    
    private String sys_clk_msg = "";
    private String sys_blk_msg = "";
    private String sys_cfg_msg = "";
    private String sys_usr_msg = "";
    private static final PwdEncryptor cipher = new PwdEncryptor();
    
    //2012-02-07
    private String js_keepalive_url = null;
    
    //2012-03-14. too late to modify database table column definations
    private String[] tz_ids;
    private String[] tz_snwo; //short name w/o DST, e.g.: PST
    private String[] tz_snwt; //short name w  DST, e.g.: PDT
    private int      tz_amt=0;
    private HashMap<String, String> tz_map = new HashMap<>(800);
    
    @Inject
    SyncWorker sync;
    
    //2013-02
    //if public: WELD-000075 Normal scoped managed bean implementation class has a public field
    private final Integer loan_origination_perc=1069; //1051; //1;  //Esther infomed that the new fee rate is 1.051%  2013-07-25  1.069% at 2017-01-12
    private final Integer university_grant_hard_top_limit = 30000;
    
    //2014-03-13, global string to hold the payment options, which is same for everyone in whole year
    private String strYIA = "";
    private String strQIA = "";
    private String strMPN = "";
    
    
    public AppReference(){
        //SelectItem one =null;
        for(int i=0; i<studtype_vals.length; i++){
            //one = new SelectItem();
            //one.setValue(studtype_vals[i]);
            //one.setLabel(studtype_labels[i]);
           // studtypes.add(one);
            studtypes.put(studtype_labels[i], studtype_vals[i]);
            studtypesMap.put(studtype_vals[i], studtype_labels[i]);
        }
        
        tz = TimeZone.getDefault();
        tzSN = tz.getDisplayName(tz.inDaylightTime(new Date()), TimeZone.SHORT); //TimeZone.getTimeZone() abbrivate name can only be GMT, and for java 1.1. recommended are long full ID
        //initSeed();
        tz_ids = TimeZone.getAvailableIDs();
        tz_amt = tz_ids.length;
        tz_snwo = new String[tz_amt];
        tz_snwt = new String[tz_amt];
        
        TimeZone tztmp=null;
        for( int x=0; x<tz_amt; x++){
            tztmp = TimeZone.getTimeZone(tz_ids[x]);
            tz_snwo[x] = tztmp.getDisplayName(false, TimeZone.SHORT );
            tz_snwt[x] = tztmp.getDisplayName(true,  TimeZone.SHORT );
            tz_map.put(tz_snwo[x], tz_ids[x]);
            tz_map.put(tz_snwt[x], tz_ids[x]);
        }
        //this caused error on new year start
        //Can not access database. The system is not ready for use for FISY 2014.
        //syncFISY();
        
        //2014-03-13
        /*
        List<Config> seeds = (List<Config>)em.createNamedQuery("Config.findAll").getResultList();        
        seed = ( seeds==null || seeds.isEmpty() ) ? null : seeds.get(0);
        if( seed!=null){   
            int year = seed.getClientFscy();
            
        }*/
    }
    
    @PostConstruct // if call initSeed() directly, em will be null. but use this @PostConstruct, em is not null. do not know why.
    public void initSeed(){ /*
        clientid = -1;
        sys_clk_ind = 1;
        sys_blk_ind = 1;
        sys_cfg_ind = 0;
        sys_usr_ind = 0;
        sys_clk_msg = "";
        sys_blk_msg = "";
        sys_cfg_msg = "";
        sys_usr_msg = "";
        
        if( em == null){
            log.error(" ref initSeed() found em was null. stop the world."); 
            sys_blk_ind =1;
            sys_blk_msg += "Can not access database. The system is not ready for use.<br/>";
            return;
        }
        
        //load config seed record
        seed = (Config) em.createNamedQuery("Config.findByClientFscy").setParameter("clientFscy", fiscal_year).getSingleResult();//.getFirstResult(); //NPE
        //seed_clientid = seed==null? -1: seed.getClientid();
        if( seed!=null){             
            clientid =   seed.getClientid();            
            sys_blk_ind = seed.getEnabledInd()>0? (seed.getRemoteDisableLogin()>0? 1: 0) : 0;  
            
            if( isEmp(seed.getMasterurl()) || isEmp(seed.getMastername()) || isEmp(seed.getMasterport()) || isEmp( seed.getLdapserver()) || isEmp(seed.getLdapsurl()) || seed.getLdapport()==null ){
                sys_cfg_ind = 0;
                sys_cfg_msg += "Can not get configuration data about remote server. The system is not ready for use.<br/>";
            }else{
                sys_cfg_ind = 1;
            }
            clientverions = seed.getClientVersion();
        }
        if( clientid>0 && sys_blk_ind>0){            
            if( isEmp(seed.getMasterecho())  ){
                sys_cfg_ind *= -1;
                sys_cfg_msg += "Estimator's identity data is missing. The system is not ready for use.<br/>";
            }
        } 
        
        users = (List<Counselor>)em.createNamedQuery("Counselor.findByStatus").setParameter("status", 1).getResultList();
        sys_usr_ind = users==null? -1: users.size();
        */
        reloadSeed();
        
        if( seed ==null){
            sys_blk_ind =1;
            sys_blk_msg += "Can not access database. The system is not ready for use for FISY "+fiscal_year+".";
            return;
        }
        
        
        HeartBeat ping = new HeartBeat();
        ping.loadConfig(seed);
        
        if( sync==null)sync = new SyncWorker();
        sync.initSyncWorker(seed); //NULL OBJECT
        
        if( ping.schedule(this, sync)>0){
            log.info(" failed to set scheduler. system failed to start.");
            //sys_blk_ind=1; 
            
            //should allow user to login, without remote server
            ldap_up_ind=0;
            master_up_ind=0;
            sys_blk_msg+="The remote server detection task can not be scheduled. You can still try to login. ";            
        }
    }
    
    //WTF "attempting to execute an operation on a closed EntityManagerFactory" while em !=null ?????????? WTF WTF
    
    public static <T> T getSingleResultByTypeOrNull(Query query) {
        query.setMaxResults(1);
        List<?> list = query.getResultList();
        if (list == null || list.size() == 0) {
            return null;
        }
        return (T) list.get(0);
    }
    public static Object getSingleResultOrNull(Query query){
        List results = query.getResultList();
        if (results.isEmpty()) return null;
        else if (results.size() == 1) return results.get(0);
        throw new NonUniqueResultException();
    }
    
    public void reloadSeed(){
        clientid    = -1;
        sys_clk_ind = 0;
        sys_blk_ind = 0;
        sys_cfg_ind = 1;
        sys_usr_ind = 1;
        sys_clk_msg = "";
        sys_blk_msg = "";
        sys_cfg_msg = "";
        sys_usr_msg = "";       
        
        if( em == null){
            log.error("@@@@@@@@@@@@@@@@@@@@@@@@@ ref reloadSeed() found em was null. stop the world.@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"); 
            sys_blk_msg += "Can not access database. The system is not ready for use.<br/>";
            sys_blk_ind =1;
            return;
        }
        wtfClosedEntityManagerFactory(em, log);
        
        //load config seed record //WTF potetial connection pool leaking ????????
        //seed = (Config) em.createNamedQuery("Config.findByClientFscy").setParameter("clientFscy", fiscal_year).getSingleResult();
        
        //List<Config> seeds = (List<Config>)em.createNamedQuery("Config.findByClientFscy").setParameter("clientFscy", fiscal_year).getResultList();
        //2014-01-21 shall get what seeds in the config table
        List<Config> seeds = (List<Config>)em.createNamedQuery("Config.findAll").getResultList();
        
        seed = ( seeds==null || seeds.isEmpty() ) ? null : seeds.get(0);
        
        //either shall get rs to tell number first: 
        //CollectionUtils.isEmpty(elementList ) ? null : elementList.get(0);
        //or by count, 
        //or   catch(NoResultException e){ return null;}
        
        //seed_clientid = seed==null? -1: seed.getClientid();
        if( seed!=null){             
            clientid =   seed.getClientid();  
            
            fiscal_year = seed.getClientFscy();
            faid_year = fiscal_year+" ~ " + (fiscal_year+1);
            
            //sys_blk_ind = seed.getEnabledInd()>0? (seed.getRemoteDisableLogin()>0? 1: 0) : 0; 
            if( seed.getEnabledInd()==0  ){
                sys_blk_msg += "Estimator is not enabled. The system is not ready for use.<br/>";
                sys_blk_ind =1;
            }
            if( seed.getRemoteDisableLogin()>0){
                sys_blk_msg += "Estimator is disabled. The system is not ready for use.<br/>";
                sys_blk_ind =1;
            }
            
            if( isEmp(seed.getMasterurl()) || isEmp(seed.getMastername()) || isEmp(seed.getMasterport()) || isEmp( seed.getLdapserver()) || isEmp(seed.getLdapsurl()) || seed.getLdapport()==null ){
                sys_cfg_ind = 0;
                sys_cfg_msg += "Can not get configuration data about remote server. The system is not ready for use.<br/>";
            }else{
                sys_cfg_ind = 1;
            }
            //clientverions = seed.getClientVersion();
            fullclientverions = seed.getClientVersion();
//            log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&& ref reloadseed, got full client ver=[%s]", fullclientverions);
            int p = fullclientverions.lastIndexOf(".");
            clientverions = fullclientverions.substring(0, p);
//            log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& ref reloadseed, got short client ver=[%s]", clientverions);
        }else{
            log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& ref reloadseed,but seed is null &&&&&&&&&&&&&&&&&&&&&&&&");
        }
        if( clientid>0 ){  //&& sys_blk_ind>0
            if( isEmp(seed.getMasterecho())  ){
                sys_cfg_ind *= -1;
                sys_cfg_msg += "Estimator's identity data is missing >>>>. The system is not ready for use.<br/>";
            }
        }
        
        users = (List<Counselor>)em.createNamedQuery("Counselor.findByStatus").setParameter("status", 1).getResultList();
        //sys_usr_ind = users==null? -1: users.size();       
        if(clientid>0 &&( users==null || users.size()==0)){
            sys_usr_ind=1;
            sys_usr_msg += "System has no defined users, so is not ready for use.<br/>";
        }
        verString = "Version "+ clientverions;//seed.getClientVersion();
  //      log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& ref reloadseed, got  client ver=[%s]", verString);
    }
    
    
    public void syncFISY(){
        SimpleDateFormat d = new java.text.SimpleDateFormat("yyyy") ;
        String strFISY = d.format(new Date());
        int iFISY = Integer.parseInt(strFISY);
        if(fiscal_year < iFISY ){
            fiscal_year = iFISY;
            faid_year = strFISY+" ~ " + (iFISY+1);
        }
    }
    
    public void refreshSeedVersion(){
        fullclientverions = seed.getClientVersion();
        int p = fullclientverions.lastIndexOf(".");
        clientverions = fullclientverions.substring(0, p);
        verString = "Version "+ clientverions;
    }
    
    public void reloadFunds(){
        funds=null;
        this.getFunds();
        a_funds = null;
        this.getA_funds();
    }
    
    public void reloadUsers(){
        users = (List<Counselor>)em.createNamedQuery("Counselor.findByStatus").setParameter("status", 1).getResultList();
        //sys_usr_ind = users==null? -1: users.size();       
        if(clientid>0 &&( users==null || users.size()==0)){
            sys_usr_ind=1;
            sys_usr_msg += "System has no defined users, so is not ready for use.<br/>";
        }
    }
    
    public EntityManager wtfClosedEntityManagerFactory(EntityManager oem, Logger loger){
        Logger logger = loger==null? log:loger;
        if( oem!=null){
            if( oem.getEntityManagerFactory()==null || !oem.getEntityManagerFactory().isOpen()){
                logger.info(" EM is not NULL but the Factory is NULL or Closed. tried to gen new em. ");
                return Persistence.createEntityManagerFactory(PUNAME).createEntityManager();
            }
            logger.info(" EM is NOT NULL,  Factory is not NULL or Closed. reuse em. ");
            return oem;
        }else{
            logger.info(" EM is NULL . tried to regen em. ");
            return Persistence.createEntityManagerFactory(PUNAME).createEntityManager();
        }
    }
    
    
    public String getTimeShowFmtStr() {
        return timeShowFmtStr;
    }

    /**
     * @return the tz
     */
    public TimeZone getTz() {
        return tz; //shall keep the same timezone when the server starts. it will not likely to move/travel cross diff tiemzones when it is up and running.
    }

    /**
     * @return the tzSN
     */
    public String getTzSN() {//TimeZone.getDefault()
        return tzSN   =tz.getDisplayName(tz.inDaylightTime(new Date()), TimeZone.SHORT);  //well, the timezone dispaly name may change if DST happens when date goes by.
    }

    /**
     * @return the digester
     */
    public PooledStringDigester getDigester() {
        return digester;
    }

    public int getClientid() {
        return clientid;
    }

    public void setClientid(int clientid) { 
        this.clientid = clientid;
    }

    public String getClientverions() {
        return clientverions;
    }

    public void setClientverions(String clientverions) {
        this.clientverions = clientverions;
    }
/*
    public int getSeed_clientid() {
        return seed_clientid;
    }

    public void setSeed_clientid(int seed_clientid) {
        this.seed_clientid = seed_clientid;
    }
*/
    public Config getSeed() {
        return seed;
    }

    public void setSeed(Config seed) {
        this.seed = seed;
    }

    public int getSys_cfg_ind() {
        return sys_cfg_ind;
    }

    public int getSys_blk_ind() {
        return sys_blk_ind;
    }

    public int getSys_usr_ind() {
        return sys_usr_ind;
    }
/*
    public int getSys_pwd_ind() {
        return sys_pwd_ind;
    }
*/

    public void setSys_blk_ind(int sys_blk_ind) {
        this.sys_blk_ind = sys_blk_ind;
    }

    public void setSys_cfg_ind(int sys_cfg_ind) {
        this.sys_cfg_ind = sys_cfg_ind;
    }

    public void setSys_usr_ind(int sys_usr_ind) {
        this.sys_usr_ind = sys_usr_ind;
    }

    public List<Counselor> getUsers() {
        return users;
    }

    public void setUsers(List<Counselor> users) {
        this.users = users;
    }

    public int getLdap_up_ind() {
        return ldap_up_ind;
    }

    public void setLdap_up_ind(int ldap_up_ind) {
        this.ldap_up_ind = ldap_up_ind;
    }

    public int getMaster_up_ind() {
        return master_up_ind;
    }

    public void setMaster_up_ind(int master_up_ind) {
        this.master_up_ind = master_up_ind;
   //     log.info("jjjjjjjjjjjjj  ref setMaster_up_ind() set master_up_ind=%d", master_up_ind);
    }

    public String getSys_blk_msg() {
        return sys_blk_msg;
    }

    public void setSys_blk_msg(String sys_blk_msg) {
        this.sys_blk_msg = sys_blk_msg;
    }

    public String getSys_cfg_msg() {
        return sys_cfg_msg;
    }

    public void setSys_cfg_msg(String sys_cfg_msg) {
        this.sys_cfg_msg = sys_cfg_msg;
    }

    public String getSys_usr_msg() {
        return sys_usr_msg;
    }

    public void setSys_usr_msg(String sys_usr_msg) {
        this.sys_usr_msg = sys_usr_msg;
    }

    public String getSys_clk_msg() {
        return sys_clk_msg;
    }

    public void setSys_clk_msg(String sys_clk_msg) {
        this.sys_clk_msg = sys_clk_msg;
    }

    public int getSys_clk_ind() {
        return sys_clk_ind;
    }

    public void setSys_clk_ind(int sys_clk_ind) {
        this.sys_clk_ind = sys_clk_ind;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T findBean(String beanName) {
        FacesContext context = FacesContext.getCurrentInstance();
        return (T) context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", Object.class);
        /*
        FacesContext facesContext = getFacesContext(); 
        Application app = facesContext.getApplication(); 
        ExpressionFactory elFactory = app.getExpressionFactory(); 
        ELContext elContext = facesContext.getELContext(); 
        ValueExpression valueExp =             elFactory.createValueExpression(elContext, expression, Object.class); 
        return valueExp.getValue(elContext); 
         */
    }
    @SuppressWarnings("unchecked")
    public static <T> T findBean2(String beanName) {        
        FacesContext facesContext   = FacesContext.getCurrentInstance();
        Application app             = facesContext.getApplication(); 
        ExpressionFactory elFactory = app.getExpressionFactory(); 
        ELContext elContext         = facesContext.getELContext(); 
        ValueExpression valueExp    =  elFactory.createValueExpression(elContext, "#{" + beanName + "}", Object.class); 
        return (T) valueExp.getValue(elContext);         
    }
    
    public String genLabelsofKeys(String[] keysStr){
        StringBuilder sbTmp = new StringBuilder(128);
        int m = 0, n=0;
        if( keysStr!=null && ((m=keysStr.length)>0)){
            n = m - 1;
            sbTmp.append("(");
            for(int i=0; i<m;  i++){
                sbTmp.append(studtypesMap.get(keysStr[i]) );
                if( i< n) sbTmp.append(", ");
            }
            sbTmp.append(")");
        }else{
            sbTmp.append("none");
        }
        return sbTmp.toString();
    }
    
    public boolean isAutosync_ind() {
        return autosync_ind;
    }
    
    public String getDerbyFmtStrbyDate(Date date){
        SimpleDateFormat d = new java.text.SimpleDateFormat(derbyDateFmt) ;
        return d.format(date);
    }
    
    public String getSimpleFmtStrbyDate(Date date){
        SimpleDateFormat d = new java.text.SimpleDateFormat(simpleDtFmtStr) ;
        return d.format(date);
    }
    public String getSimpleFmtStrNow(){        
        return getSimpleFmtStrbyDate(new Date());
    }
    
    public boolean isDateStrValidByFormater(String dateStr, String formatStr){
        try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(formatStr);
            DateTime date =  fmt.parseDateTime(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public DateTime parseDateStr(String maybeDate, String format) {
          org.joda.time.DateTime date = null;
   //       System.out.println("parseDateStr()---pattern="+format);
          try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(format);
            date =  fmt.parseDateTime(maybeDate);// java.lang.IllegalArgumentException: Invalid format: "01/02/1934" ????????????????????????            
          } catch (Exception e) { 
              e.printStackTrace();
          }
          return date;
    }
    
    
    //@Produces //WELD-001408 Unsatisfied dependencies for type [Severity] with qualifiers [@Default] at injection point [[parameter 1] of [method] @Produces edu.lsu.estimator.AppReference.facesMessageByKey(Severity, String)].
    FacesMessage facesMessageByKey(FacesMessage.Severity level, String key){ //String clientID
        String msg = null;
        try{
            msg = resourceBundle.getString(key);
        }catch(Exception e){
            msg = "message: "+key;
            e.printStackTrace();
        }
        FacesMessage fmsg = new FacesMessage( msg);
        fmsg.setSeverity(level);
        fmsg.setSummary(msg);
        return fmsg;
    }
    
    FacesMessage facesMessageByStr(FacesMessage.Severity level, String str){ //String clientID
        String msg = str;        
        FacesMessage fmsg = new FacesMessage( msg);
        fmsg.setSeverity(level);
        fmsg.setSummary(str);
        //fmsg.setDetail(str);
        return fmsg;
    }
    
    
    public String getSimpleDtFmtStr() {
        return simpleDtFmtStr;
    }
    
    
    @Produces @RequestScoped//@SessionScoped// ---comented out because sessionscopoed obj needs to be serializable, while HttpSession is not.
    public HttpSession getGenHttpSession(){        
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if( session==null) session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        return session;
        //request.getSession(false)
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public String getSdateFmtStr() {
        return sdateFmtStr;
    }

    public String getTimeFmtStr() {
        return timeFmtStr;
    }

    public String getDateInputShowStr() {
        return dateInputShowStr;
    }

    public int getOptSyncOnLogin() {
        return optSyncOnLogin;
    }

    public LinkedHashMap<String, String> getStudtypes() {
        return studtypes;
    }

    public String[] getStudtype_vals() {
        return studtype_vals;
    }

    public String[] getStudtype_labels() {
        return studtype_labels;
    }

    public int getTest_ind() {
        return test_ind;
    }

    public String padStr(String str, String pad, int mod, int indents, int totlen){
        StringBuilder sb = new StringBuilder(64);
        if( str!=null && pad!=null && indents>=0 && totlen>(str.length()+pad.length())){
            switch(mod){
                case -1:
                    for( int i=0; i<indents; i++) sb.append(pad);
                    sb.append(str);
                    for(int i=0; i< (totlen - str.length()-indents ); i++)sb.append(pad);
                    break;
                case 0:
                    int n = (totlen - str.length() ) /2;
                    for( int i=0; i<n; i++) sb.append(pad);
                    sb.append(str);
                    n = totlen - str.length() - n;
                    for( int i=0; i<n; i++) sb.append(pad);
                    break;
                case 1:
                    int m = totlen - str.length() - indents;
                    for( int i=0; i<m; i++) sb.append(pad);
                    sb.append(str);
                    for( int i=0; i<indents; i++) sb.append(pad);
                    break;
                default:                    
            }
        }        
        return sb.toString();
    }

    public int getFiscal_year() {
        return fiscal_year;
    }

    public String getFaid_year() {
        return faid_year;
    }

    public int getIndept_age() {
        return indept_age;
    }

    public Date getIndept_dob() {
        if( indept_dob==null){
            DateMidnight dm = new DateMidnight(this.fiscal_year - this.indept_age, 1, 1);
            /*
            String str = "01/01/"+(this.fiscal_year - this.indept_age) ;
            //SimpleDateFormat sdf = new SimpleDateFormat(sdateFmtStr);
            DateTime dt = new DateTime(str); //Invalid format: "01/01/1990" is malformed at "/01/1990"
            */
            indept_dob = dm.toDate();
            //sdf.format(indept_dob);
        }
        return indept_dob;
    }

    public int getApp_young_limit() {
        return app_young_limit;
    }

    public int getApp_old_limit() {
        return app_old_limit;
    }

    public String getMdateFmtStr() {
        return mdateFmtStr;
    }

    public String getVerString() {
        return verString;
    }
    
    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }
    
    public String getDocCounselorName(String recid){
        if( isEmp(recid))return "";
        List<Student> stds = em.createNamedQuery("Student.findByRecid", Student.class).setParameter("recid", recid).getResultList();
        if( stds==null || stds.size()==0){
            return " N/A <no such doc>";
        }else{
            Integer cid= stds.get(0).getCounselorId();
            //Counselor c = em.createNamedQuery("Counselor.findByUserid", Counselor.class).setParameter("userid", cid).getSingleResult();  
            //return c.getUsername();
            //some sync may fall, does not update counselors list
            List<Counselor> cs = em.createNamedQuery("Counselor.findByUserid", Counselor.class).setParameter("userid", cid).getResultList(); 
            if( cs==null || cs.size()==0){
                return " N/A <stranger>";
            }else{
                return cs.get(0).getUsername();
            }
        }
    }
    
    public String showDoEMDTShort(Student stud, int tm_ind){  //use ddom+tzdom or ddoe+tzdoe
        if( stud==null)return "";
        String stmp = "";
        
        long tm = stud.getDdom();
        String tz = stud.getTzdom();
        if( tm==0){
            tm=stud.getDdoe();
            tz = stud.getTzdoe();
        }
         
        Date date = new Date(tm);        
        //if( date ==null) return "";
        //timeShowFmtStr
        //private String timeShrtShowFmtStr = "MM/dd/yy HH:mm"; //h:mm a
        //private String timeInfoShowFmtStr = "EEE, MMM d, h:mm a z, yyyy";
    
        if( tm_ind>0){ //show in the list, should be the local time/timezone, not the record's  timezone
            SimpleDateFormat d = new java.text.SimpleDateFormat(timeShrtShowFmtStr) ;
            d.setTimeZone(TimeZone.getDefault() );//TimeZone.getTimeZone(getTzIdByShortName(tz)) ); //getTimeZone(tz)
            
            return d.format(date);
        }else{
            SimpleDateFormat d = new java.text.SimpleDateFormat(timeInfoShowFmtStr) ;
            d.setTimeZone( TimeZone.getTimeZone(getTzIdByShortName(tz)) );
            
            stmp = stud.getPuser_id();
            if(stmp!=null ){
                if( stmp.equalsIgnoreCase( stud.getStudentALsuid())){
                    return "SELF@ "+ d.format(date);
                }else{
                    return "ID "+stmp+" @ "+ d.format(date);
                }                
            }else{
                Counselor c = em.createNamedQuery("Counselor.findByUserid", Counselor.class).setParameter("userid", stud.getCounselorId()).getSingleResult();                
                return c.getUsername()+" @ "+ d.format(date);
            }            
            
        }
        
    }
    
    public String getTzIdByShortName(String tz_shortname){
        String res = "";
        if(!isEmp(tz_shortname))  {
            String sn = tz_shortname.trim().toUpperCase();
            /*
            for(int x=0; x<tz_amt; x++){
                if( tz_snwo[x].equals(sn) || tz_snwt[x].equals(sn)){
                    res = tz_ids[x];
                    break;
                }
            }*/
            if( tz_map.containsKey(sn)){
                res = tz_map.get(sn);
            }
        }
        return res;
    }
    
    public String showDTShort(Date date){        
        if( date ==null) return "";
        //timeShowFmtStr
        SimpleDateFormat d = new java.text.SimpleDateFormat(timeShowFmtStr) ; //private String timeShowFmtStr = "M/d/yy h:mm a z"; //Tue Oct 18 00:00:00 PDT 2011 ?????  Thu Sep 08 00:00:00 PDT 2011
        return d.format(date);
    }
    
    public String showAmt(Integer amt){
        return fmtInt.format(amt);
    }
    public String showAmt(BigDecimal big){
        return fmtInt.format(big);
    }
    /*
    public String showMerits(List<String> merits){
        String str = "";
        int i=-1;
        for( String one: merits){
            i++;
            switch(one.toUpperCase()){
                case "MC": str = str + (i>0? "; ":"") +"Recommended"; break;
                case "MS": str = str + (i>0? "; ":"") + "Semifinalist"; break;
                case "MF": str = str + (i>0? "; ":"") + "Finalist"; break;
            }
        }
        return str;
    }*/
    public String showMerits(String merits){
        if(merits==null || merits.isEmpty())return "";
        String str = merits.replaceAll(", ", ",");
        String[] all = str.split(",");
        int i=-1;
        str="";
        for( String one: all){
            i++;
            switch(one.toUpperCase()){
                case "MC": str = str + (i>0? "; ":"") +"Recommended"; break;
                case "MS": str = str + (i>0? "; ":"") + "Semifinalist"; break;
                case "MF": str = str + (i>0? "; ":"") + "Finalist"; break;
            }
        }
        return isEmp(str)? "none":str; 
    }
    
    
    public String showStudAddr(Student one){
        StringBuilder sb = new StringBuilder(128);
        int st=0;
        String str = one.getHomeAddrApt();
        if( !isEmp(str)){
            st++;
            sb.append(str.trim()).append(", ");
        }
        str = one.getStudentGStreet();
        if( !isEmp(str)){
            st++;
            sb.append(str.trim());
        }
        if( st>0){
            sb.append("\n<br/>"); //"&lt;br/&gt;"
        }
        str = one.getStudentHCity();
        if( !isEmp(str)){
            sb.append(str.trim()).append(", ");
        }
        sb.append( showStudArea(one));
        str = one.getStudentKCountry();
        if( !isEmp(str) ){
            str = str.trim().toUpperCase();
            if( !str.equals("USA") && !str.equals("US") && !str.startsWith("U.S.A")&& !str.equals("U.S.")&& !str.startsWith("UNITED STATES") ){                
                sb.append(", ").append(str);
            }
        }
        return sb.toString();
    }
    //<h:outputText value="#{one.homeAddrApt}#{empty one.homeAddrApt? '':', '}#{one.studentGStreet}&lt;br/&gt;
    //#{one.studentHCity}#{empty one.studentHCity? '':', '}#{ref.showStudArea(one)} 
    //#{empty one.studentKCountry?'': (one.studentKCountry eq 'USA'? '':', ')}#{one.studentKCountry eq 'USA'? '':one.studentKCountry}" escape="false" /> 
    
    
    public String showStudArea(Student stud){
        if( stud!=null){
            String zip = stud.getStudentJZip()==null? "": stud.getStudentJZip();            
            int len =  zip.length();
            len = len>5? 5: len;
            return stud.getStudentIState()+" "+zip.substring(0, len);             
        }else{
            return "";
        }
    }
    
    public List<Fund> getFunds() {
        if( funds==null || funds.size()==0){
            funds = em.createNamedQuery("Fund.findByStatus").setParameter("status", "Active").getResultList();
        }
  //      System.out.println(" ######################################################################### ref getFunds() was invoked and execured. funds.size=="+(funds==null? -1:funds.size()) );
        return funds;
    }

    public void setFunds(List<Fund> funds) {
        this.funds = funds;
    }
    
    public String getFundTxtById(int seq){
 //       System.out.println(" ref getFundDescById() got fund id=="+seq);
        String desc="";
        Fund target = null;
        if(funds==null  || funds.size()==0) getFunds();
 //       System.out.println(" ref getFundDescById() was invoked. rechecked funds. funds.size=="+(funds==null? -1:funds.size()) );
        
        for( Fund one:funds){
  //          System.out.println(" ref getFundDescById() looping fund by seq= "+one.getFundseq());
            if(one.getFundId()==seq){
                target = one;
                break;
            }
        }
 //        System.out.println(" ref getFundDescById() "+(target!=null?" found ": " did not find ")+" matching fund by id=="+seq);
        StringBuilder sb = new StringBuilder(64);
        if( target!=null){
            sb.append( target.getReqNoteInd().equalsIgnoreCase("yes")?"needs notes, ":"" );        
            
            sb.append(target.getMatchPerc().compareTo(BigDecimal.ZERO)>0 ? tranPercentage(target.getMatchPerc()):"");
            sb.append(target.getHasMatching().equalsIgnoreCase("yes")? " match":"");
            
            sb.append(target.getMatchTop()>0?" $"+target.getMatchTop()+" max":"");
            
            String res = sb.toString();//sb.append(" ---")
            if( res!=null && !res.isEmpty()){
                res = " ---"+res.replaceAll(", ,", ", ");
            }
            return target.getFundDesc() + res;  //NPE ??????
        }else{
            return "No  fund  by id "+seq;
        }        
    }
    
    public String getFundExtById(int seq){         
        Fund target = null;
        if(funds==null  || funds.size()==0) getFunds(); 
        for( Fund one:funds){  
            if(one.getFundId()==seq){
                target = one;
                break;
            }
        }
 
        StringBuilder sb = new StringBuilder(64);
        if( target!=null){
            sb.append( target.getReqNoteInd().equalsIgnoreCase("yes")?"needs notes, ":"" );        
  //          log.info("]]]]]]]]]]]]]] ref.getFundExtById(%d) target.getMatchPerc()=%.4f%n, gt 0 ? %d", seq, target.getMatchPerc(), target.getMatchPerc().compareTo(BigDecimal.ZERO));
            sb.append(target.getMatchPerc().compareTo(BigDecimal.ZERO)>0 ? tranPercentage(target.getMatchPerc()):"");
            sb.append(target.getHasMatching().equalsIgnoreCase("yes")? " match":"");
            
            sb.append(target.getMatchTop()>0?"  $"+target.getMatchTop()+" max":"");
            
            String res = sb.toString();
            if( res!=null && !res.isEmpty()){
                res = " ---"+res.replaceAll(", ,", ", ");
            }
            return  res;  
        }else{
            return "No  fund  by id "+seq;
        }        
    }
    
    
    public int fundMatchTopById(int seq){
        Fund target = null;
        if(funds==null  || funds.size()==0) getFunds(); 
        for( Fund one:funds){  
            if(one.getFundId()==seq){
                target = one;
                break;
            }
        }
        return target==null? 0:target.getMatchTop();
    }
    
    //2017-12-19 to tell if a fund is disabled, though the prefunds are still "ACTIVE" to be chosen  by query, to meet a fixed layout
    public int fundMaxOutIndById(int seq){
        Fund target = null;
        if(funds==null  || funds.size()==0) getFunds(); 
        for( Fund one:funds){  
            if(one.getFundId()==seq){
                target = one;
                break;
            }
        }
        return target==null? -1:target.getMax_ind(); // 0: default    -1: disabled     1: maxed out, stopping giving out
    }
    
    
    public int fundNotesReqById(int seq){
        Fund target = null;
        if(funds==null  || funds.size()==0) getFunds(); 
        for( Fund one:funds){  
            if(one.getFundId()==seq){
                target = one;
                break;
            }
        }
        return target==null? -1: target.getReqNoteInd().equalsIgnoreCase("yes") ? 1 : 0; //NPE
    }
    
    public String getFundCodeById(int seq){
        String code="";
        Fund target = null;
        if(funds==null  || funds.size()==0) getFunds();
        
        for( Fund one:funds){  
            if(one.getFundId()==seq){
                target = one;
                break;
            }
        }
        if( target!=null) return target.getFundCode();
        return "N/A"; 
    }
    
    
    public String getFundDescById(int seq){
        String desc="";
        Fund target = null;
        if(funds==null  || funds.size()==0) getFunds();
        
        for( Fund one:funds){  
            if(one.getFundId()==seq){
                target = one;
                break;
            }
        }
        if( target!=null) return target.getFundDesc();
        return "No  fund  by id "+seq; 
    }
    
    private String tranPercentage(BigDecimal perc){
        int all = perc.movePointRight(4).intValueExact();
        int wh = all /100;
        int fr = all % 100;
       // log.info("]]]]]]]]]]]]]] tranPercentage() turn bigdecimal %s to %d.%d percent", perc.toString(), wh, fr); // java.util.IllegalFormatConversionException: d != java.math.BigDecimal
        return ""+wh +(fr==0?"":"."+fr)+"%";
    }
     
    
    
    public Integer[] getFunds_alone() {
        return funds_alone;
    }

    public List<SelectItem> getA_funds() {
        if( a_funds==null){
            a_funds = new ArrayList<>();
            /*
            int tot = funds.size() - funds_alone.length;
            for(int i=0; i<tot; i++){                
            } */
            try{
                List<Integer> specials = Arrays.asList(funds_alone);

                SelectItem ns = new SelectItem();
                ns.setLabel( resourceBundle.getString("EstimateForm.institute.fundselectopt")  );
                ns.setValue("0");            
                ns.setNoSelectionOption(true);
                a_funds.add(ns);

                if(funds==null || funds.isEmpty()) getFunds();

                if( funds==null || funds.isEmpty())return a_funds;
                //for(Fund one : funds){
                for(int f=0; f<funds.size(); f++){
                    Fund one = funds.get(f);
                    if( specials.contains(one.getFundId()) )continue;
                    SelectItem opt = new SelectItem();
                    opt.setLabel(getFundTxtById(one.getFundId()));
                    opt.setValue(one.getFundId().toString());
                    a_funds.add(opt);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return a_funds;
    }

    public int getAlone_funds_amt() {
        return alone_funds_amt;
    }

    public int getTotal_funds_amt() {
        return total_funds_amt;
    }
    
    public String fmtFullTimeWithZone(long ms, String tzstr){    
        if( ms==0 || isEmp(tzstr)) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z"); //Z like -0800
    //sdf2.setTimeZone( TimeZone.getTimeZone("UTC") );
        sdf.setTimeZone( TimeZone.getTimeZone(  getTzIdByShortName(  tzstr ) ) ); 
       // DateTime dt = new DateTime(ms);
        return sdf.format( new Date(ms));        
    }
    
    public String fmtFullNowTimeWithDefaultZone(){        
        long ms = System.currentTimeMillis();
        String tzstr = getTzSN();
                
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z"); //Z like -0800
    //sdf2.setTimeZone( TimeZone.getTimeZone("UTC") );
        sdf.setTimeZone( TimeZone.getTimeZone( getTzIdByShortName (tzstr) ) );
       // DateTime dt = new DateTime(ms);
        return sdf.format( new Date(ms));        
    }
    
    
    public int convertCurrencyStrToInt(String str){
        int res=0;
        if( str==null || (str=str.trim()).isEmpty()|| str.equalsIgnoreCase("n/a") ){
            return 0; //-1
        }
      //  log.info(" got %s", str);
        str = str.replaceAll(",","");//.substring(1);
       // log.info(" after removing ',', got %s", str);
        
        str = str.replaceAll("\\$","");
       // log.info(" after removing '$', got %s", str);
        
        return Integer.parseInt(str);
    }
    
    public String fmtMoney(Integer amt){
        return moneyFmt.format(amt);
    }
    
    
    public boolean isEmp(String str){
        return str==null ? true : (str.trim().isEmpty()? true : false);
    }
    
    public boolean reachHostBySocket(String hostname, int port){
        Socket socket = null;
        boolean reachable = false; 
        try {
            //socket = new Socket(hostname, port);//java.net.UnknownHostException: ldap.lasierra.edu
            socket = new Socket();            
            socket.setKeepAlive(false);
            socket.setSoLinger(false, 2); //in seconds //only affect socket close
            
            socket.setTcpNoDelay(true); 
            socket.setSoTimeout(2000);    //applies to read but not connect         
            SocketAddress sockaddr = new InetSocketAddress(hostname, port);
            socket.connect(sockaddr, 2000); //milliseconds
           
            reachable = true;
        } catch(java.net.SocketTimeoutException tx){
            //log.info("", tx);   // equals to e.printStack() 
            log.info("...reachHostBySocket() met SocketTimeoutException with server %s %d", hostname, port);
        } catch (UnknownHostException ex) {
            //log.info("", ex);
            log.info("...reachHostBySocket() met UnknownHostException with server %s %d", hostname, port);
        }catch(java.net.ConnectException ne){ //Connection timed out
            log.info("", ne);
        }catch (IOException ex) {
            log.info("", ex);
        }catch(Exception e){
            log.info("", e);
        }finally {            
            //socket.isClosed();
            //socket.isConnected();
            if (socket != null) try { socket.close(); } catch(Exception e) {}
        }        
        return reachable;
    }
    
    public int pingLdap(){
        //log.info("~~~~~ pingLdap() will invoke ref.reachHostBySocket() with parameter %s %d", seed.getLdapserver(), seed.getLdapport());
        return  reachHostBySocket(seed.getLdapserver(), seed.getLdapport() )? 1 : 0;
    }
    /*
    public static PwdEncryptor getCipher() {
        return cipher;
    }*/
    
    public PwdEncryptor getCipher(){
        return cipher;
    }
    
    public String getVerStr(){ //seed.getClientVersion() is full
        return seed==null? "Version Unknown": "Version: "+ clientverions+ (seed.getClientid()>0? "     ID: "+seed.getClientid(): "");
    }
    
    public String getJavaInfo(){ //java.version
        StringBuilder sb = new StringBuilder(128);
        return sb.append( System.getProperty("java.runtime.version") ).append('/').append(System.getProperty("java.vm.name")).append('/').append(System.getProperty("java.vm.version")).toString();              
    }
    
    public String getOSinfo(){
        StringBuilder sb = new StringBuilder(128);
        String hostname = "host";
        try{
            hostname = InetAddress.getLocalHost().getHostName();
        }catch(Exception e){}
        return sb.append( System.getProperty("user.name") ).append('@').append(hostname).append('/')
                .append(System.getProperty("os.name")).append('/').append(System.getProperty("os.arch")).append('/').append(System.getProperty("os.version")).toString();         
    }
    public String getAddress(){
        return getIP()+"/"+getMAC();
    }
    
    public String getMAC(){
        String os = System.getProperty("os.name");        
        try {
            if(os.startsWith("Windows")) {
                return windowsParseMacAddress(windowsRunIpConfigCommand());
            } else if(os.startsWith("Linux")) {
                return linuxParseMacAddress(linuxRunIfConfigCommand());
            } else {
                //throw new IOException("unknown operating system: " + os);
                return "N/A";
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            //throw new IOException(ex.getMessage());
            return "N/A";
        }    
    }
    
    public String getIP(){
        String os = System.getProperty("os.name");        
        try {
            if(os.startsWith("Windows")) {
                return windowsParseIpAddress(windowsRunIpConfigCommand());
            } else if(os.startsWith("Linux")) {
                return linuxParseIpAddress(linuxRunIfConfigCommand());
            } else {                
                return "N/A";
            }
        } catch(Exception ex) {
            ex.printStackTrace();            
            return "N/A";
        }    
    }
    
    private String linuxParseIpAddress(String ipConfigResponse)  {        
        StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
        String lastMacAddress = null;
        
        while(tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken().trim();
        
            int macAddressPosition = line.indexOf("inet addr:");
            if(macAddressPosition < 0) continue;
            int loe = line.indexOf(" ", macAddressPosition + 10);
            
            String macAddressCandidate = line.substring(macAddressPosition + 10, loe).trim();
            if(linuxIsMacAddress(macAddressCandidate)) {                
                lastMacAddress = ( lastMacAddress==null? "":lastMacAddress+";") + macAddressCandidate;
                continue;
               // return lastMacAddress;
            }
        }
        if( lastMacAddress!=null) return lastMacAddress; else return "N/A";       
    }
    
private String windowsParseIpAddress(String ipConfigResponse) {        
        StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
        String lastMacAddress = null;
        
        while(tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken().trim();                       
            int macAddressPosition = line.indexOf(":");
            if(macAddressPosition <= 0) continue;
            
            if( line.indexOf("IP Address") <0) continue;
            String macAddressCandidate = line.substring(macAddressPosition + 1).trim();
            if(windowsIsMacAddress(macAddressCandidate)) {
                //lastMacAddress = macAddressCandidate;
                lastMacAddress = ( lastMacAddress==null? "":lastMacAddress+";") + macAddressCandidate;
                continue;
            }
        }
         if( lastMacAddress!=null) return lastMacAddress; else return "N/A";         
    }    
    
    
    private String linuxParseMacAddress(String ipConfigResponse) throws ParseException {
        /*String localHost = null;
        try {
            localHost = InetAddress.getLocalHost().getHostAddress();
        } catch(java.net.UnknownHostException ex) {
            ex.printStackTrace();
            throw new ParseException(ex.getMessage(), 0);
        }*/        
        StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
        String lastMacAddress = null;
        
        while(tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken().trim();
        //    boolean containsLocalHost = line.indexOf(localHost) >= 0;
            
            // see if line contains IP address
            /*
            if(containsLocalHost && lastMacAddress != null) {
                return lastMacAddress;
            } */
            
            // see if line contains MAC address
            int macAddressPosition = line.indexOf("HWaddr");
            if(macAddressPosition <= 0) continue;
            
            String macAddressCandidate = line.substring(macAddressPosition + 6).trim();
            if(linuxIsMacAddress(macAddressCandidate)) {                
                lastMacAddress = ( lastMacAddress==null? "":lastMacAddress+";") + macAddressCandidate;
                continue;
               // return lastMacAddress;
            }
        }
        if( lastMacAddress!=null) return lastMacAddress; else return "N/A";
        /*
        ParseException ex = new ParseException("cannot read MAC address for " + localHost + " from [" + ipConfigResponse + "]", 0);
        ex.printStackTrace();
        throw ex; */
    }
    
private boolean linuxIsMacAddress(String macAddressCandidate) {
        // TODO: use a smart regular expression
       // if(macAddressCandidate.length() != 17) return false;
        return macAddressCandidate!=null && macAddressCandidate.trim().length()>7 && !macAddressCandidate.startsWith("127")? true:false;
    }
    
private String linuxRunIfConfigCommand() throws IOException {
        Process p = Runtime.getRuntime().exec("ifconfig");
        InputStream stdoutStream = new BufferedInputStream(p.getInputStream());
        
        StringBuffer buffer= new StringBuffer();
        for (;;) {
            int c = stdoutStream.read();
            if (c == -1) break;
            buffer.append((char)c);
        }
        String outputText = buffer.toString();
        
        stdoutStream.close();
        
        return outputText;
    }
    
private String windowsParseMacAddress(String ipConfigResponse) throws ParseException {
        /*String localHost = null;
        try {
            localHost = InetAddress.getLocalHost().getHostAddress();
        } catch(java.net.UnknownHostException ex) {
            ex.printStackTrace();
            throw new ParseException(ex.getMessage(), 0);
        } */        
        StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
        String lastMacAddress = null;
        
        while(tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken().trim();            
            /*
            // see if line contains IP address
            if(line.endsWith(localHost) && lastMacAddress != null) {
                return lastMacAddress;
            } */
            
            // see if line contains MAC address
            int macAddressPosition = line.indexOf(":");
            if(macAddressPosition <= 0) continue;
            if( line.indexOf("Physical Address") <0) continue;
            String macAddressCandidate = line.substring(macAddressPosition + 1).trim();
            if(windowsIsMacAddress(macAddressCandidate)) {
                //lastMacAddress = macAddressCandidate;
                lastMacAddress = ( lastMacAddress==null? "":lastMacAddress+";") + macAddressCandidate;
                continue;
            }
        }
         if( lastMacAddress!=null) return lastMacAddress; else return "N/A";
         /*
        ParseException ex = new ParseException("cannot read MAC address from [" + ipConfigResponse + "]", 0);
        ex.printStackTrace();
        throw ex; */
    }    
    
    private   boolean windowsIsMacAddress(String macAddressCandidate) {
        // TODO: use a smart regular expression
        //if(macAddressCandidate.length() != 17) return false;        
        return macAddressCandidate!=null && macAddressCandidate.trim().length()>7 && !macAddressCandidate.startsWith("127")? true:false;
    }    
    
    private  String windowsRunIpConfigCommand() throws IOException {
        Process p = Runtime.getRuntime().exec("ipconfig /all");
        InputStream stdoutStream = new BufferedInputStream(p.getInputStream());
        
        StringBuffer buffer= new StringBuffer();
        for (;;) {
            int c = stdoutStream.read();
            if (c == -1) break;
            buffer.append((char)c);
        }
        String outputText = buffer.toString();
        
        stdoutStream.close();
        
        return outputText;
    }
    
    public String genFormTitleByRecid(String recid,  Student one, Student modStud){
        //log.info(" recid=%s, current fisy=%d, record fisy=%d", recid, fiscal_year, modStud.getStudentFisy());
        //#{estimator.stud.recid eq null? 'New':''} Estimate  #{estimator.stud.recid eq null?'':' (Based on doc '}#{estimator.stud.recid}#{estimator.stud.recid eq null?'':')'}
        return new StringBuilder(256).append( isEmp(recid)? "New Estimate ":"Estimate ").append( isEmp(recid)? "":"(Based on doc "+recid+ (genModStudFisy(modStud) ) + ")").toString();
    }

    private String genModStudFisy( Student modStud){
        if( modStud==null ) return "";
        return fiscal_year == modStud.getStudentFisy() ? "": " @"+modStud.getStudentFisy();
    }
    
    public String getFullclientverions() {
        return fullclientverions;
    }

    public void setFullclientverions(String fullclientverions) {
        this.fullclientverions = fullclientverions;
    }

    public String getPUNAME() {
        return PUNAME;
    }

    public String getJs_keepalive_url() {
        if(isEmp(js_keepalive_url )){  
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext ext = facesContext.getExternalContext();
            /*
 log.info("ext req context path=%s",    ext.getRequestContextPath());
 log.info("ext req path info=%s",       ext.getRequestPathInfo());
 log.info("ext req scheme=%s",          ext.getRequestScheme());
 log.info("ext req server=%s",          ext.getRequestServerName());
 log.info("ext req port=%s",            ext.getRequestServerPort());
 log.info("ext req servlet path=%s",    ext.getRequestServletPath());
  */            
            StringBuilder sb = new StringBuilder(256);
            sb.append("http://").append( ext.getRequestServerName()).append( ext.getRequestContextPath()).append("/view/login.jsf");
            //log.info("shall redirect to URL: [%s]", sb.toString());
            js_keepalive_url = sb.toString();
        }
        
        return js_keepalive_url;
    }
    
    
    public void poll2live() {   
        ;  
    } 
    
    /*
    public String try_sync(SyncWorker worker, int userid){
        try{
            Thread.currentThread().sleep(1000);
        }catch(Exception e){
            
        }
        return worker.sync(); //userid
    }
    */
    public String getJSFMsgByKey(String key){
        String res = "";
        if(!isEmp(key) && resourceBundle.containsKey(key)){
            res = resourceBundle.getString(key);
        }
        return res;
    }

    /**
     * @return the loan_origination_perc
     */
    public Integer getLoan_origination_perc() {
        return loan_origination_perc;
    }

    /**
     * @return the university_grant_hard_top_limit
     */
    public Integer getUniversity_grant_hard_top_limit() {
        return university_grant_hard_top_limit;
    }
    
    public void setStdFixInstAids(Student stud){
        
        Integer[] prefunds = getFunds_alone();
        int t=0;
        stud.setStudentAsScholarship1Name( getFundDescById(prefunds[t]) );//=  "Church Matching Scholarship (100% match, $1000 max)"; 
        stud.setFund1id(prefunds[t]);
        t++;
        
        //at edu.lsu.estimator.AppReference.getFundDescById(AppReference.java:399) NPE ??????? ##########################################################
        stud.setStudentAvScholarship2Name(  getFundDescById(prefunds[t]) );//= "Pacific Union Camp Earning (100% match, $2000 max)";
        stud.setFund2id(prefunds[t]);
        t++;
        
        stud.setStudentAyScholarship3Name( getFundDescById(prefunds[t]) );//= "Non-Pacific Union Camp Earning (50% match, $1500 max)"; 
        stud.setFund3id(prefunds[t]);
        t++;
        
        stud.setStudentBbScholarship4Name( getFundDescById(prefunds[t]) );//= "Literature Evangelist Earnings (100% match, $3000 max)";
        stud.setFund4id(prefunds[t]);
        t++;
        
        stud.setStudentBeScholarship5Name( getFundDescById(prefunds[t]) );//= "";
        stud.setFund5id(prefunds[t]);
        t++;
        
        stud.setStudentBhScholarship6Name( getFundDescById(prefunds[t]) );//= "";
        stud.setFund6id(prefunds[t]);
        
    }

    public int getSys_counselor_id() {
        return sys_counselor_id;
    }

    public String getStrYIA() {
        return strYIA;
    }

    public void setStrYIA(String strYIA) {
        this.strYIA = strYIA;
    }

    public String getStrQIA() {
        return strQIA;
    }

    public void setStrQIA(String strQIA) {
        this.strQIA = strQIA;
    }

    public String getStrMPN() {
        return strMPN;
    }

    public void setStrMPN(String strMPN) {
        this.strMPN = strMPN;
    }
     
    
}



/*
 Symbol	Meaning	Type	Example
G	Era	Text	“GG” -> “AD”
y	Year	Number	“yy” -> “03″
“yyyy” -> “2003″
M	Month	Text or Number	“M” -> “7″
“M” -> “12″
“MM” -> “07″
“MMM” -> “Jul”
“MMMM” -> “December”
d	Day in month	Number	“d” -> “3″
“dd” -> “03″
h	Hour (1-12, AM/PM)	Number	“h” -> “3″
“hh” -> “03″
H	Hour (0-23)	Number	“H” -> “15″
“HH” -> “15″
k	Hour (1-24)	Number	“k” -> “3″
“kk” -> “03″
K	Hour (0-11 AM/PM)	Number	“K” -> “15″
“KK” -> “15″
m	Minute	Number	“m” -> “7″
“m” -> “15″
“mm” -> “15″
s	Second	Number	“s” -> “15″
“ss” -> “15″
S	Millisecond (0-999)	Number	“SSS” -> “007″
E	Day in week	Text	“EEE” -> “Tue”
“EEEE” -> “Tuesday”
D	Day in year (1-365 or 1-364)	Number	“D” -> “65″
“DDD” -> “065″
F	Day of week in month (1-5)	Number	“F” -> “1″
w	Week in year (1-53)	Number	“w” -> “7″
W	Week in month (1-5)	Number	“W” -> “3″
a	AM/PM	Text	“a” -> “AM”
“aa” -> “AM”
z	Time zone	Text	“z” -> “EST”
“zzz” -> “EST”
“zzzz” -> “Eastern Standard Time”
‘	Excape for text	Delimiter	“‘hour’ h” -> “hour 9″
”	Single quote	Literal	“ss”SSS” -> “45′876″
 */