/*      */ package edu.lsu.estimator;

/*      */
 /*      */ import com.kingombo.slf5j.Logger;
/*      */ import com.kingombo.slf5j.LoggerFactory;
/*      */ import edu.lsu.estimator.Config;
/*      */ import edu.lsu.estimator.Counselor;
/*      */ import edu.lsu.estimator.Fund;
/*      */ import edu.lsu.estimator.Student;
/*      */ import edu.lsu.estimator.SyncWorker;
/*      */ import edu.lsu.estimator.cron.HeartBeat;
/*      */ import edu.lsu.estimator.secu.PwdEncryptor;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.ConnectException;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.Socket;
/*      */ import java.net.SocketAddress;
/*      */ import java.net.SocketTimeoutException;
/*      */ import java.net.UnknownHostException;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.NumberFormat;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TimeZone;
/*      */ import javax.annotation.PostConstruct;
/*      */ import javax.el.ELContext;
/*      */ import javax.el.ExpressionFactory;
/*      */ import javax.el.ValueExpression;
/*      */ import javax.enterprise.context.ApplicationScoped;
/*      */ import javax.enterprise.context.RequestScoped;
/*      */ import javax.enterprise.inject.Produces;
/*      */ import javax.faces.application.Application;
/*      */ import javax.faces.application.FacesMessage;
/*      */ import javax.faces.context.ExternalContext;
/*      */ import javax.faces.context.FacesContext;
/*      */ import javax.faces.model.SelectItem;
/*      */ import javax.inject.Inject;
/*      */ import javax.inject.Named;
/*      */ import javax.persistence.EntityManager;
/*      */ import javax.persistence.NonUniqueResultException;
/*      */ import javax.persistence.Persistence;
/*      */ import javax.persistence.PersistenceContext;
/*      */ import javax.persistence.Query;
/*      */ import javax.servlet.http.HttpSession;
/*      */ import org.jasypt.digest.PooledStringDigester;
/*      */ import org.joda.time.DateMidnight;
/*      */ import org.joda.time.DateTime;
/*      */ import org.joda.time.format.DateTimeFormat;
/*      */ import org.joda.time.format.DateTimeFormatter;

/*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ @ApplicationScoped
/*      */ @Named("ref")
/*      */ public class AppReference /*      */ {

    /*   73 */ private int test_ind = 0;
    /*   74 */    private int fiscal_year = 2024;
    /*   75 */    private String faid_year = "2024 ~ 2025";
    /*   76 */    private String verString = "Version 3.0.0.0";
    /*   77 */    private NumberFormat fmtInt = new DecimalFormat("$#,###");
    /*      */
 /*   79 */    private static final Logger log = LoggerFactory.getLogger();
    /*   80 */ private String PUNAME = "estimator";
    /*      */
    @PersistenceContext(unitName = "estimator")
    /*      */ private EntityManager em;
    /*   83 */    private int clientid = -1;
    /*   84 */    private String clientverions = "1.1.1";
    /*   85 */    private String fullclientverions = "1.1.1.1";
    /*      */
 /*   87 */    private int lsuid_max_length = 6;
    /*   88 */    private int max_popup_items = 10;
    /*   89 */    private final int sys_counselor_id = 0;
    /*      */
 /*   91 */    private NumberFormat moneyFmt = new DecimalFormat("$#,###");
    /*      */
 /*      */
 /*      */    private boolean autosync_ind = true;
    /*      */
 /*      */
 /*      */    @Inject
    /*      */ private ResourceBundle resourceBundle;
    /*      */
 /*  100 */    private String sdateFmtStr = "M/d/yyyy";
    /*  101 */    private String mdateFmtStr = "MMMM d, yyyy";
    /*  102 */    private String timeFmtStr = "M/d/yyyy H:m:s";
    /*  103 */    private String derbyDateFmt = "yyyy-MM-dd";
    /*      */
 /*  105 */    private String timeShowFmtStr = "M/d/yy h:mm a z";
    /*      */
 /*  107 */    private String timeShrtShowFmtStr = "MM/dd/yy HH:mm";
    /*  108 */    private String timeInfoShowFmtStr = "EEE, MMM d, h:mm a z, yyyy";
    /*      */
 /*  110 */    private String dateInputShowStr = "MM/dd/yyyy";
    /*  111 */    private String simpleDtFmtStr = "EEE, MMM d, h:mm a z";
    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  119 */    private int indept_age = 24;
    /*  120 */    private Date indept_dob = null;
    /*  121 */    private int app_young_limit = 12;
    /*  122 */    private int app_old_limit = 95;
    /*      */
 /*      */
 /*  125 */    private int optSyncOnLogin = 1;
    /*      */
 /*  127 */    private LinkedHashMap<String, String> studtypes = new LinkedHashMap<>();
    /*  128 */    private String[] studtype_vals = new String[]{"ALL", "FR", "F2", "SO", "JR", "SR", "MBA", "UD", "UI", "ESL", "CJ", "G", "PG"};
    /*  129 */    private String[] studtype_labels = new String[]{"All Student Types", "Freshman", "Continuing Freshman", "Sophomore", "Junior", "Senior", "MBA Student", "UnderGraduate-Domestic", "UnderGraduate-International", "ESL", "CriminalJustice", "Graduate", "PostGraduate"};
    /*  130 */    private HashMap<String, String> studtypesMap = new HashMap<>();
    /*      */
 /*  132 */    private List<Fund> funds = new ArrayList<>();
    /*  133 */    private Integer[] funds_alone = new Integer[]{Integer.valueOf(125), Integer.valueOf(122), Integer.valueOf(123), Integer.valueOf(120), Integer.valueOf(124), Integer.valueOf(121)};
    /*  134 */    private int alone_funds_amt = this.funds_alone.length;
    /*  135 */    private int total_funds_amt = 9;
    /*      */
 /*      */    private List<SelectItem> a_funds;
    /*  138 */    private int fund_name_max = 70;
    /*  139 */    private int fund_note_max = 256;
    /*      */
 /*      */
 /*      */
 /*  143 */    private PooledStringDigester digester = null;
    /*      */
 /*      */
 /*      */    private TimeZone tz;
    /*      */
 /*      */
 /*      */    private String tzSN;
    /*      */
 /*      */
 /*      */    private Config seed;
    /*      */
 /*      */
 /*  155 */    private int sys_clk_ind = 1;
    /*  156 */    private int sys_blk_ind = 1;
    /*  157 */    private int sys_cfg_ind = 0;
    /*  158 */    private int sys_usr_ind = 0;
    /*      */
 /*      */    private List<Counselor> users;
    /*      */
 /*  162 */    private int ldap_up_ind = 0;
    /*  163 */    private int master_up_ind = 0;
    /*      */
 /*  165 */    private String sys_clk_msg = "";
    /*  166 */    private String sys_blk_msg = "";
    /*  167 */    private String sys_cfg_msg = "";
    /*  168 */    private String sys_usr_msg = "";
    /*  169 */    private static final PwdEncryptor cipher = new PwdEncryptor();
    /*      */
 /*      */
 /*  172 */    private String js_keepalive_url = null;
    /*      */
 /*      */    private String[] tz_ids;
    /*      */
 /*      */    private String[] tz_snwo;
    /*      */    private String[] tz_snwt;
    /*  178 */    private int tz_amt = 0;
    /*  179 */    private HashMap<String, String> tz_map = new HashMap<>(800);
    /*      */
 /*      */
 /*      */    @Inject
    /*      */ SyncWorker sync;
    /*      */
 /*  185 */    private final Integer loan_origination_perc = Integer.valueOf(1069);
    /*  186 */    private final Integer university_grant_hard_top_limit = Integer.valueOf(30000);
    /*      */
 /*      */
 /*  189 */    private String strYIA = "";
    /*  190 */    private String strQIA = "";
    /*  191 */    private String strMPN = "";

    /*      */
 /*      */
 /*      */
 /*      */ public AppReference() {
        /*  196 */ for (int i = 0; i < this.studtype_vals.length; i++) {
            /*      */
 /*      */
 /*      */
 /*      */
 /*  201 */ this.studtypes.put(this.studtype_labels[i], this.studtype_vals[i]);
            /*  202 */ this.studtypesMap.put(this.studtype_vals[i], this.studtype_labels[i]);
            /*      */        }
        /*      */
 /*  205 */ this.tz = TimeZone.getDefault();
        /*  206 */ this.tzSN = this.tz.getDisplayName(this.tz.inDaylightTime(new Date()), 0);
        /*      */
 /*  208 */ this.tz_ids = TimeZone.getAvailableIDs();
        /*  209 */ this.tz_amt = this.tz_ids.length;
        /*  210 */ this.tz_snwo = new String[this.tz_amt];
        /*  211 */ this.tz_snwt = new String[this.tz_amt];
        /*      */
 /*  213 */ TimeZone tztmp = null;
        /*  214 */ for (int x = 0; x < this.tz_amt; x++) {
            /*  215 */ tztmp = TimeZone.getTimeZone(this.tz_ids[x]);
            /*  216 */ this.tz_snwo[x] = tztmp.getDisplayName(false, 0);
            /*  217 */ this.tz_snwt[x] = tztmp.getDisplayName(true, 0);
            /*  218 */ this.tz_map.put(this.tz_snwo[x], this.tz_ids[x]);
            /*  219 */ this.tz_map.put(this.tz_snwt[x], this.tz_ids[x]);
            /*      */        }
        /*      */    }


 /*      */ @PostConstruct
    /*      */ public void initSeed() {
        /*  279 */ reloadSeed();
        /*      */
 /*  281 */ if (this.seed == null) {
            /*  282 */ this.sys_blk_ind = 1;
            /*  283 */ this.sys_blk_msg += "Can not access database. The system is not ready for use for FISY " + this.fiscal_year + ".";
            /*      */
 /*      */ return;
            /*      */        }
        /*      */
 /*  288 */ HeartBeat ping = new HeartBeat();
        /*  289 */ ping.loadConfig(this.seed);
        /*      */
 /*  291 */ if (this.sync == null) {
            this.sync = new SyncWorker();
        }
        /*  292 */ this.sync.initSyncWorker(this.seed);
        /*      */
 /*  294 */ if (ping.schedule(this, this.sync) > 0) {
            /*  295 */ log.info(" failed to set scheduler. system failed to start.");
            /*      */
 /*      */
 /*      */
 /*  299 */ this.ldap_up_ind = 0;
            /*  300 */ this.master_up_ind = 0;
            /*  301 */ this.sys_blk_msg += "The remote server detection task can not be scheduled. You can still try to login. ";
            /*      */        }
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public static <T> T getSingleResultByTypeOrNull(Query query) {
        /*  308 */ query.setMaxResults(1);
        /*  309 */ List<?> list = query.getResultList();
        /*  310 */ if (list == null || list.size() == 0) {
            /*  311 */ return null;
            /*      */        }
        /*  313 */ return (T) list.get(0);
        /*      */    }

    /*      */ public static Object getSingleResultOrNull(Query query) {
        /*  316 */ List results = query.getResultList();
        /*  317 */ if (results.isEmpty()) {
            return null;
        }
        /*  318 */ if (results.size() == 1) {
            return results.get(0);
        }
        /*  319 */ throw new NonUniqueResultException();
        /*      */    }

    /*      */
 /*      */ public void reloadSeed() {
        /*  323 */ this.clientid = -1;
        /*  324 */ this.sys_clk_ind = 0;
        /*  325 */ this.sys_blk_ind = 0;
        /*  326 */ this.sys_cfg_ind = 1;
        /*  327 */ this.sys_usr_ind = 1;
        /*  328 */ this.sys_clk_msg = "";
        /*  329 */ this.sys_blk_msg = "";
        /*  330 */ this.sys_cfg_msg = "";
        /*  331 */ this.sys_usr_msg = "";
        /*      */
 /*  333 */ if (this.em == null) {
            /*  334 */ log.error("@@@@@@@@@@@@@@@@@@@@@@@@@ ref reloadSeed() found em was null. stop the world.@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            /*  335 */ this.sys_blk_msg += "Can not access database. The system is not ready for use.<br/>";
            /*  336 */ this.sys_blk_ind = 1;
            /*      */ return;
            /*      */        }
        /*  339 */ wtfClosedEntityManagerFactory(this.em, log);
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  346 */ List<Config> seeds = this.em.createNamedQuery("Config.findAll").getResultList();
        /*      */
 /*  348 */ this.seed = (seeds == null || seeds.isEmpty()) ? null : seeds.get(0);

 /*  356 */ if (this.seed != null) {
            /*  357 */ this.clientid = this.seed.getClientid().intValue();
            /*      */
 /*  359 */ this.fiscal_year = this.seed.getClientFscy();
            /*  360 */ this.faid_year = this.fiscal_year + " ~ " + (this.fiscal_year + 1);
            /*      */
 /*      */
 /*  363 */ if (this.seed.getEnabledInd() == 0) {
                /*  364 */ this.sys_blk_msg += "Estimator is not enabled. The system is not ready for use.<br/>";
                /*  365 */ this.sys_blk_ind = 1;
                /*      */            }
            /*  367 */ if (this.seed.getRemoteDisableLogin() > 0) {
                /*  368 */ this.sys_blk_msg += "Estimator is disabled. The system is not ready for use.<br/>";
                /*  369 */ this.sys_blk_ind = 1;
                /*      */            }
            /*      */
 /*  372 */ if (isEmp(this.seed.getMasterurl()) || isEmp(this.seed.getMastername())
                    || isEmp(this.seed.getMasterport()) || isEmp(this.seed.getLdapserver()) || isEmp(this.seed.getLdapsurl())
                    || this.seed.getLdapport() == null) {
                /*  373 */ this.sys_cfg_ind = 0;
                /*  374 */ this.sys_cfg_msg += "Can not get configuration data about remote server. The system is not ready for use.<br/>";
                /*      */            } else {
                /*  376 */ this.sys_cfg_ind = 1;
                /*      */            }
            /*      */
 /*  379 */ this.fullclientverions = this.seed.getClientVersion();
            /*      */
 /*  381 */ int p = this.fullclientverions.lastIndexOf(".");
            /*  382 */ this.clientverions = this.fullclientverions.substring(0, p);
            /*      */        } else {
            /*      */
 /*  385 */ log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& ref reloadseed,but seed is null &&&&&&&&&&&&&&&&&&&&&&&&");
            /*      */        }
        /*  387 */ if (this.clientid > 0
                && /*  388 */ isEmp(this.seed.getMasterecho())) {
            /*  389 */ this.sys_cfg_ind *= -1;
            /*  390 */ this.sys_cfg_msg += "Estimator's identity data is missing ??????. The system is not ready for use.<br/>";
            /*      */        }
        /*      */
 /*      */

 /*  394 */ this.users = this.em.createNamedQuery("Counselor.findByStatus").setParameter("status", 1).getResultList();
        /*      */
        log.info("rrrrrrrrrrrrrrrrrrrr ref reloadseed,but seed is null &&&&&&&&&&&&&&&&&&&&&& " + this.users.size());
        /*  396 */ if (this.clientid > 0 && (this.users == null || this.users.size() == 0)) {
            /*  397 */ this.sys_usr_ind = 1;
            /*  398 */ this.sys_usr_msg += "System has no defined users, so is not ready for use.<br/>";
            /*      */        }
        /*  400 */ this.verString = "Version " + this.clientverions;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public void syncFISY() {
        /*  406 */ SimpleDateFormat d = new SimpleDateFormat("yyyy");
        /*  407 */ String strFISY = d.format(new Date());
        /*  408 */ int iFISY = Integer.parseInt(strFISY);
        /*  409 */ if (this.fiscal_year < iFISY) {
            /*  410 */ this.fiscal_year = iFISY;
            /*  411 */ this.faid_year = strFISY + " ~ " + (iFISY + 1);
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public void refreshSeedVersion() {
        /*  416 */ this.fullclientverions = this.seed.getClientVersion();
        /*  417 */ int p = this.fullclientverions.lastIndexOf(".");
        /*  418 */ this.clientverions = this.fullclientverions.substring(0, p);
        /*  419 */ this.verString = "Version " + this.clientverions;
        /*      */    }

    /*      */
 /*      */ public void reloadFunds() {
        /*  423 */ this.funds = null;
        /*  424 */ getFunds();
        /*  425 */ this.a_funds = null;
        /*  426 */ getA_funds();
        /*      */    }

    /*      */
 /*      */ public void reloadUsers() {
        /*  430 */ this.users = this.em.createNamedQuery("Counselor.findByStatus").setParameter("status", Integer.valueOf(1)).getResultList();
        /*      */
 /*  432 */ if (this.clientid > 0 && (this.users == null || this.users.size() == 0)) {
            /*  433 */ this.sys_usr_ind = 1;
            /*  434 */ this.sys_usr_msg += "System has no defined users, so is not ready for use.<br/>";
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public EntityManager wtfClosedEntityManagerFactory(EntityManager oem, Logger loger) {
        /*  439 */ Logger logger = (loger == null) ? log : loger;
        /*  440 */ if (oem != null) {
            /*  441 */ if (oem.getEntityManagerFactory() == null || !oem.getEntityManagerFactory().isOpen()) {
                /*  442 */ logger.info(" EM is not NULL but the Factory is NULL or Closed. tried to gen new em. ");
                /*  443 */ return Persistence.createEntityManagerFactory(this.PUNAME).createEntityManager();
                /*      */            }
            /*  445 */ logger.info(" EM is NOT NULL,  Factory is not NULL or Closed. reuse em. ");
            /*  446 */ return oem;
            /*      */        }
        /*  448 */ logger.info(" EM is NULL . tried to regen em. ");
        /*  449 */ return Persistence.createEntityManagerFactory(this.PUNAME).createEntityManager();
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public String getTimeShowFmtStr() {
        /*  455 */ return this.timeShowFmtStr;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public TimeZone getTz() {
        /*  462 */ return this.tz;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String getTzSN() {
        /*  469 */ return this.tzSN = this.tz.getDisplayName(this.tz.inDaylightTime(new Date()), 0);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public PooledStringDigester getDigester() {
        /*  476 */ return this.digester;
        /*      */    }

    /*      */
 /*      */ public int getClientid() {
        /*  480 */ return this.clientid;
        /*      */    }

    /*      */
 /*      */ public void setClientid(int clientid) {
        /*  484 */ this.clientid = clientid;
        /*      */    }

    /*      */
 /*      */ public String getClientverions() {
        /*  488 */ return this.clientverions;
        /*      */    }

    /*      */
 /*      */ public void setClientverions(String clientverions) {
        /*  492 */ this.clientverions = clientverions;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public Config getSeed() {
        /*  504 */ return this.seed;
        /*      */    }

    /*      */
 /*      */ public void setSeed(Config seed) {
        /*  508 */ this.seed = seed;
        /*      */    }

    /*      */
 /*      */ public int getSys_cfg_ind() {
        /*  512 */ return this.sys_cfg_ind;
        /*      */    }

    /*      */
 /*      */ public int getSys_blk_ind() {
        /*  516 */ return this.sys_blk_ind;
        /*      */    }

    /*      */
 /*      */ public int getSys_usr_ind() {
        /*  520 */ return this.sys_usr_ind;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setSys_blk_ind(int sys_blk_ind) {
        /*  529 */ this.sys_blk_ind = sys_blk_ind;
        /*      */    }

    /*      */
 /*      */ public void setSys_cfg_ind(int sys_cfg_ind) {
        /*  533 */ this.sys_cfg_ind = sys_cfg_ind;
        /*      */    }

    /*      */
 /*      */ public void setSys_usr_ind(int sys_usr_ind) {
        /*  537 */ this.sys_usr_ind = sys_usr_ind;
        /*      */    }

    /*      */
 /*      */ public List<Counselor> getUsers() {
        /*  541 */ return this.users;
        /*      */    }

    /*      */
 /*      */ public void setUsers(List<Counselor> users) {
        /*  545 */ this.users = users;
        /*      */    }

    /*      */
 /*      */ public int getLdap_up_ind() {
        /*  549 */ return this.ldap_up_ind;
        /*      */    }

    /*      */
 /*      */ public void setLdap_up_ind(int ldap_up_ind) {
        /*  553 */ this.ldap_up_ind = ldap_up_ind;
        /*      */    }

    /*      */
 /*      */ public int getMaster_up_ind() {
        /*  557 */ return this.master_up_ind;
        /*      */    }

    /*      */
 /*      */ public void setMaster_up_ind(int master_up_ind) {
        /*  561 */ this.master_up_ind = master_up_ind;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String getSys_blk_msg() {
        /*  566 */ return this.sys_blk_msg;
        /*      */    }

    /*      */
 /*      */ public void setSys_blk_msg(String sys_blk_msg) {
        /*  570 */ this.sys_blk_msg = sys_blk_msg;
        /*      */    }

    /*      */
 /*      */ public String getSys_cfg_msg() {
        /*  574 */ return this.sys_cfg_msg;
        /*      */    }

    /*      */
 /*      */ public void setSys_cfg_msg(String sys_cfg_msg) {
        /*  578 */ this.sys_cfg_msg = sys_cfg_msg;
        /*      */    }

    /*      */
 /*      */ public String getSys_usr_msg() {
        /*  582 */ return this.sys_usr_msg;
        /*      */    }

    /*      */
 /*      */ public void setSys_usr_msg(String sys_usr_msg) {
        /*  586 */ this.sys_usr_msg = sys_usr_msg;
        /*      */    }

    /*      */
 /*      */ public String getSys_clk_msg() {
        /*  590 */ return this.sys_clk_msg;
        /*      */    }

    /*      */
 /*      */ public void setSys_clk_msg(String sys_clk_msg) {
        /*  594 */ this.sys_clk_msg = sys_clk_msg;
        /*      */    }

    /*      */
 /*      */ public int getSys_clk_ind() {
        /*  598 */ return this.sys_clk_ind;
        /*      */    }

    /*      */
 /*      */ public void setSys_clk_ind(int sys_clk_ind) {
        /*  602 */ this.sys_clk_ind = sys_clk_ind;
        /*      */    }

    /*      */
 /*      */
 /*      */ public static <T> T findBean(String beanName) {
        /*  607 */ FacesContext context = FacesContext.getCurrentInstance();
        /*  608 */ return (T) context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", Object.class);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public static <T> T findBean2(String beanName) {
        /*  620 */ FacesContext facesContext = FacesContext.getCurrentInstance();
        /*  621 */ Application app = facesContext.getApplication();
        /*  622 */ ExpressionFactory elFactory = app.getExpressionFactory();
        /*  623 */ ELContext elContext = facesContext.getELContext();
        /*  624 */ ValueExpression valueExp = elFactory.createValueExpression(elContext, "#{" + beanName + "}", Object.class);
        /*  625 */ return (T) valueExp.getValue(elContext);
        /*      */    }

    /*      */
 /*      */ public String genLabelsofKeys(String[] keysStr) {
        /*  629 */ StringBuilder sbTmp = new StringBuilder(128);
        /*  630 */ int m = 0, n = 0;
        /*  631 */ if (keysStr != null && (m = keysStr.length) > 0) {
            /*  632 */ n = m - 1;
            /*  633 */ sbTmp.append("(");
            /*  634 */ for (int i = 0; i < m; i++) {
                /*  635 */ sbTmp.append(this.studtypesMap.get(keysStr[i]));
                /*  636 */ if (i < n) {
                    sbTmp.append(", ");
                }
                /*      */            }
            /*  638 */ sbTmp.append(")");
            /*      */        } else {
            /*  640 */ sbTmp.append("none");
            /*      */        }
        /*  642 */ return sbTmp.toString();
        /*      */    }

    /*      */
 /*      */ public boolean isAutosync_ind() {
        /*  646 */ return this.autosync_ind;
        /*      */    }

    /*      */
 /*      */ public String getDerbyFmtStrbyDate(Date date) {
        /*  650 */ SimpleDateFormat d = new SimpleDateFormat(this.derbyDateFmt);
        /*  651 */ return d.format(date);
        /*      */    }

    /*      */
 /*      */ public String getSimpleFmtStrbyDate(Date date) {
        /*  655 */ SimpleDateFormat d = new SimpleDateFormat(this.simpleDtFmtStr);
        /*  656 */ return d.format(date);
        /*      */    }

    /*      */ public String getSimpleFmtStrNow() {
        /*  659 */ return getSimpleFmtStrbyDate(new Date());
        /*      */    }

    /*      */
 /*      */ public boolean isDateStrValidByFormater(String dateStr, String formatStr) {
        /*      */ try {
            /*  664 */ DateTimeFormatter fmt = DateTimeFormat.forPattern(formatStr);
            /*  665 */ DateTime date = fmt.parseDateTime(dateStr);
            /*  666 */ return true;
            /*  667 */        } catch (Exception e) {
            /*  668 */ return false;
            /*      */        }
        /*      */    }

    /*      */ public DateTime parseDateStr(String maybeDate, String format) {
        /*  672 */ DateTime date = null;
        /*      */
 /*      */ try {
            /*  675 */ DateTimeFormatter fmt = DateTimeFormat.forPattern(format);
            /*  676 */ date = fmt.parseDateTime(maybeDate);
            /*  677 */        } catch (Exception e) {
            /*  678 */ e.printStackTrace();
            /*      */        }
        /*  680 */ return date;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */    FacesMessage facesMessageByKey(FacesMessage.Severity level, String key) {
        /*  686 */ String msg = null;
        /*      */ try {
            /*  688 */ msg = this.resourceBundle.getString(key);
            /*  689 */        } catch (Exception e) {
            /*  690 */ msg = "message: " + key;
            /*  691 */ e.printStackTrace();
            /*      */        }
        /*  693 */ FacesMessage fmsg = new FacesMessage(msg);
        /*  694 */ fmsg.setSeverity(level);
        /*  695 */ fmsg.setSummary(msg);
        /*  696 */ return fmsg;
        /*      */    }

    /*      */
 /*      */    FacesMessage facesMessageByStr(FacesMessage.Severity level, String str) {
        /*  700 */ String msg = str;
        /*  701 */ FacesMessage fmsg = new FacesMessage(msg);
        /*  702 */ fmsg.setSeverity(level);
        /*  703 */ fmsg.setSummary(str);
        /*      */
 /*  705 */ return fmsg;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String getSimpleDtFmtStr() {
        /*  710 */ return this.simpleDtFmtStr;
        /*      */    }

    /*      */
 /*      */ @Produces
    /*      */    @RequestScoped
    /*      */ public HttpSession getGenHttpSession() {
        /*  716 */ HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        /*  717 */ if (session == null) {
            session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        }
        /*  718 */ return session;
        /*      */    }

    /*      */
 /*      */
 /*      */ public ResourceBundle getResourceBundle() {
        /*  723 */ return this.resourceBundle;
        /*      */    }

    /*      */
 /*      */ public String getSdateFmtStr() {
        /*  727 */ return this.sdateFmtStr;
        /*      */    }

    /*      */
 /*      */ public String getTimeFmtStr() {
        /*  731 */ return this.timeFmtStr;
        /*      */    }

    /*      */
 /*      */ public String getDateInputShowStr() {
        /*  735 */ return this.dateInputShowStr;
        /*      */    }

    /*      */
 /*      */ public int getOptSyncOnLogin() {
        /*  739 */ return this.optSyncOnLogin;
        /*      */    }

    /*      */
 /*      */ public LinkedHashMap<String, String> getStudtypes() {
        /*  743 */ return this.studtypes;
        /*      */    }

    /*      */
 /*      */ public String[] getStudtype_vals() {
        /*  747 */ return this.studtype_vals;
        /*      */    }

    /*      */
 /*      */ public String[] getStudtype_labels() {
        /*  751 */ return this.studtype_labels;
        /*      */    }

    /*      */
 /*      */ public int getTest_ind() {
        /*  755 */ return this.test_ind;
        /*      */    }

    /*      */
 /*      */ public String padStr(String str, String pad, int mod, int indents, int totlen) {
        /*  759 */ StringBuilder sb = new StringBuilder(64);
        /*  760 */ if (str != null && pad != null && indents >= 0 && totlen > str.length() + pad.length()) {
            /*  761 */ int i;
            int n;
            int j;
            int m;
            int k;
            switch (mod) {
                /*      */ case -1:
                    /*  763 */ for (i = 0; i < indents;) {
                        sb.append(pad);
                        i++;
                    }
                    /*  764 */ sb.append(str);
                    /*  765 */ for (i = 0; i < totlen - str.length() - indents;) {
                        sb.append(pad);
                        i++;
                    }
                    /*      */ break;
                /*      */ case 0:
                    /*  768 */ n = (totlen - str.length()) / 2;
                    /*  769 */ for (j = 0; j < n;) {
                        sb.append(pad);
                        j++;
                    }
                    /*  770 */ sb.append(str);
                    /*  771 */ n = totlen - str.length() - n;
                    /*  772 */ for (j = 0; j < n;) {
                        sb.append(pad);
                        j++;
                    }
                    /*      */ break;
                /*      */ case 1:
                    /*  775 */ m = totlen - str.length() - indents;
                    /*  776 */ for (k = 0; k < m;) {
                        sb.append(pad);
                        k++;
                    }
                    /*  777 */ sb.append(str);
                    /*  778 */ for (k = 0; k < indents;) {
                        sb.append(pad);
                        k++;
                    }
                    /*      */
 /*      */ break;
                /*      */            }
            /*      */        }
        /*  783 */ return sb.toString();
        /*      */    }

    /*      */
 /*      */ public int getFiscal_year() {
        /*  787 */ return this.fiscal_year;
        /*      */    }

    /*      */
 /*      */ public String getFaid_year() {
        /*  791 */ return this.faid_year;
        /*      */    }

    /*      */
 /*      */ public int getIndept_age() {
        /*  795 */ return this.indept_age;
        /*      */    }

    /*      */
 /*      */ public Date getIndept_dob() {
        /*  799 */ if (this.indept_dob == null) {
            /*  800 */ DateMidnight dm = new DateMidnight(this.fiscal_year - this.indept_age, 1, 1);
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  806 */ this.indept_dob = dm.toDate();
            /*      */        }
        /*      */
 /*  809 */ return this.indept_dob;
        /*      */    }

    /*      */
 /*      */ public int getApp_young_limit() {
        /*  813 */ return this.app_young_limit;
        /*      */    }

    /*      */
 /*      */ public int getApp_old_limit() {
        /*  817 */ return this.app_old_limit;
        /*      */    }

    /*      */
 /*      */ public String getMdateFmtStr() {
        /*  821 */ return this.mdateFmtStr;
        /*      */    }

    /*      */
 /*      */ public String getVerString() {
        /*  825 */ return this.verString;
        /*      */    }

    /*      */
 /*      */ public TimeZone getTimeZone() {
        /*  829 */ return TimeZone.getDefault();
        /*      */    }

    /*      */
 /*      */ public String getDocCounselorName(String recid) {
        /*  833 */ if (isEmp(recid)) {
            return "";
        }
        /*  834 */ List<Student> stds = this.em.createNamedQuery("Student.findByRecid", Student.class).setParameter("recid", recid).getResultList();
        /*  835 */ if (stds == null || stds.size() == 0) {
            /*  836 */ return " N/A <no such doc>";
            /*      */        }
        /*  838 */ Integer cid = ((Student) stds.get(0)).getCounselorId();
        /*      */
 /*      */
 /*      */
 /*  842 */ List<Counselor> cs = this.em.createNamedQuery("Counselor.findByUserid", Counselor.class).setParameter("userid", cid).getResultList();
        /*  843 */ if (cs == null || cs.size() == 0) {
            /*  844 */ return " N/A <stranger>";
            /*      */        }
        /*  846 */ return ((Counselor) cs.get(0)).getUsername();
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public String showDoEMDTShort(Student stud, int tm_ind) {
        /*  852 */ if (stud == null) {
            return "";
        }
        /*  853 */ String stmp = "";
        /*      */
 /*  855 */ long tm = stud.getDdom();
        /*  856 */ String tz = stud.getTzdom();
        /*  857 */ if (tm == 0L) {
            /*  858 */ tm = stud.getDdoe();
            /*  859 */ tz = stud.getTzdoe();
            /*      */        }
        /*      */
 /*  862 */ Date date = new Date(tm);
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  868 */ if (tm_ind > 0) {
            /*  869 */ SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.timeShrtShowFmtStr);
            /*  870 */ simpleDateFormat.setTimeZone(TimeZone.getDefault());
            /*      */
 /*  872 */ return simpleDateFormat.format(date);
            /*      */        }
        /*  874 */ SimpleDateFormat d = new SimpleDateFormat(this.timeInfoShowFmtStr);
        /*  875 */ d.setTimeZone(TimeZone.getTimeZone(getTzIdByShortName(tz)));
        /*      */
 /*  877 */ stmp = stud.getPuser_id();
        /*  878 */ if (stmp != null) {
            /*  879 */ if (stmp.equalsIgnoreCase(stud.getStudentALsuid())) {
                /*  880 */ return "SELF@ " + d.format(date);
                /*      */            }
            /*  882 */ return "ID " + stmp + " @ " + d.format(date);
            /*      */        }
        /*      */
 /*  885 */ Counselor c = (Counselor) this.em.createNamedQuery("Counselor.findByUserid", Counselor.class).setParameter("userid", stud.getCounselorId()).getSingleResult();
        /*  886 */ return c.getUsername() + " @ " + d.format(date);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String getTzIdByShortName(String tz_shortname) {
        /*  894 */ String res = "";
        /*  895 */ if (!isEmp(tz_shortname)) {
            /*  896 */ String sn = tz_shortname.trim().toUpperCase();
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  904 */ if (this.tz_map.containsKey(sn)) {
                /*  905 */ res = this.tz_map.get(sn);
                /*      */            }
            /*      */        }
        /*  908 */ return res;
        /*      */    }

    /*      */
 /*      */ public String showDTShort(Date date) {
        /*  912 */ if (date == null) {
            return "";
        }
        /*      */
 /*  914 */ SimpleDateFormat d = new SimpleDateFormat(this.timeShowFmtStr);
        /*  915 */ return d.format(date);
        /*      */    }

    /*      */
 /*      */ public String showAmt(Integer amt) {
        /*  919 */ return this.fmtInt.format(amt);
        /*      */    }

    /*      */ public String showAmt(BigDecimal big) {
        /*  922 */ return this.fmtInt.format(big);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String showMerits(String merits) {
        /*  939 */ if (merits == null || merits.isEmpty()) {
            return "";
        }
        /*  940 */ String str = merits.replaceAll(", ", ",");
        /*  941 */ String[] all = str.split(",");
        /*  942 */ int i = -1;
        /*  943 */ str = "";
        /*  944 */ for (String one : all) {
            /*  945 */ i++;
            /*  946 */ switch (one.toUpperCase()) {
                case "MC":
                    /*  947 */ str = str + ((i > 0) ? "; " : "") + "Recommended";
                    break;
                /*  948 */ case "MS":
                    str = str + ((i > 0) ? "; " : "") + "Semifinalist";
                    break;
                /*  949 */ case "MF":
                    str = str + ((i > 0) ? "; " : "") + "Finalist";
                    break;
            }
            /*      */
 /*      */        }
        /*  952 */ return isEmp(str) ? "none" : str;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String showStudAddr(Student one) {
        /*  957 */ StringBuilder sb = new StringBuilder(128);
        /*  958 */ int st = 0;
        /*  959 */ String str = one.getHomeAddrApt();
        /*  960 */ if (!isEmp(str)) {
            /*  961 */ st++;
            /*  962 */ sb.append(str.trim()).append(", ");
            /*      */        }
        /*  964 */ str = one.getStudentGStreet();
        /*  965 */ if (!isEmp(str)) {
            /*  966 */ st++;
            /*  967 */ sb.append(str.trim());
            /*      */        }
        /*  969 */ if (st > 0) {
            /*  970 */ sb.append("\n<br/>");
            /*      */        }
        /*  972 */ str = one.getStudentHCity();
        /*  973 */ if (!isEmp(str)) {
            /*  974 */ sb.append(str.trim()).append(", ");
            /*      */        }
        /*  976 */ sb.append(showStudArea(one));
        /*  977 */ str = one.getStudentKCountry();
        /*  978 */ if (!isEmp(str)) {
            /*  979 */ str = str.trim().toUpperCase();
            /*  980 */ if (!str.equals("USA") && !str.equals("US") && !str.startsWith("U.S.A") && !str.equals("U.S.") && !str.startsWith("UNITED STATES")) {
                /*  981 */ sb.append(", ").append(str);
                /*      */            }
            /*      */        }
        /*  984 */ return sb.toString();
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String showStudArea(Student stud) {
        /*  992 */ if (stud != null) {
            /*  993 */ String zip = (stud.getStudentJZip() == null) ? "" : stud.getStudentJZip();
            /*  994 */ int len = zip.length();
            /*  995 */ len = (len > 5) ? 5 : len;
            /*  996 */ return stud.getStudentIState() + " " + zip.substring(0, len);
            /*      */        }
        /*  998 */ return "";
        /*      */    }

    /*      */
 /*      */
 /*      */ public List<Fund> getFunds() {
        /* 1003 */ if (this.funds == null || this.funds.size() == 0) {
            /* 1004 */ this.funds = this.em.createNamedQuery("Fund.findByStatus").setParameter("status", "Active").getResultList();
            /*      */        }
        /*      */
 /* 1007 */ return this.funds;
        /*      */    }

    /*      */
 /*      */ public void setFunds(List<Fund> funds) {
        /* 1011 */ this.funds = funds;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String getFundTxtById(int seq) {
        /* 1016 */ String desc = "";
        /* 1017 */ Fund target = null;
        /* 1018 */ if (this.funds == null || this.funds.size() == 0) {
            getFunds();
        }
        /*      */
 /*      */
 /* 1021 */ for (Fund one : this.funds) {
            /*      */
 /* 1023 */ if (one.getFundId().intValue() == seq) {
                /* 1024 */ target = one;
                /*      */
 /*      */ break;
                /*      */            }
            /*      */        }
        /* 1029 */ StringBuilder sb = new StringBuilder(64);
        /* 1030 */ if (target != null) {
            /* 1031 */ sb.append(target.getReqNoteInd().equalsIgnoreCase("yes") ? "needs notes, " : "");
            /*      */
 /* 1033 */ sb.append((target.getMatchPerc().compareTo(BigDecimal.ZERO) > 0) ? tranPercentage(target.getMatchPerc()) : "");
            /* 1034 */ sb.append(target.getHasMatching().equalsIgnoreCase("yes") ? " match" : "");
            /*      */
 /* 1036 */ sb.append((target.getMatchTop().intValue() > 0) ? (" $" + target.getMatchTop() + " max") : "");
            /*      */
 /* 1038 */ String res = sb.toString();
            /* 1039 */ if (res != null && !res.isEmpty()) {
                /* 1040 */ res = " ---" + res.replaceAll(", ,", ", ");
                /*      */            }
            /* 1042 */ return target.getFundDesc() + res;
            /*      */        }
        /* 1044 */ return "No  fund  by id " + seq;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String getFundExtById(int seq) {
        /* 1049 */ Fund target = null;
        /* 1050 */ if (this.funds == null || this.funds.size() == 0) {
            getFunds();
        }
        /* 1051 */ for (Fund one : this.funds) {
            /* 1052 */ if (one.getFundId().intValue() == seq) {
                /* 1053 */ target = one;
                /*      */
 /*      */ break;
                /*      */            }
            /*      */        }
        /* 1058 */ StringBuilder sb = new StringBuilder(64);
        /* 1059 */ if (target != null) {
            /* 1060 */ sb.append(target.getReqNoteInd().equalsIgnoreCase("yes") ? "needs notes, " : "");
            /*      */
 /* 1062 */ sb.append((target.getMatchPerc().compareTo(BigDecimal.ZERO) > 0) ? tranPercentage(target.getMatchPerc()) : "");
            /* 1063 */ sb.append(target.getHasMatching().equalsIgnoreCase("yes") ? " match" : "");
            /*      */
 /* 1065 */ sb.append((target.getMatchTop().intValue() > 0) ? ("  $" + target.getMatchTop() + " max") : "");
            /*      */
 /* 1067 */ String res = sb.toString();
            /* 1068 */ if (res != null && !res.isEmpty()) {
                /* 1069 */ res = " ---" + res.replaceAll(", ,", ", ");
                /*      */            }
            /* 1071 */ return res;
            /*      */        }
        /* 1073 */ return "No  fund  by id " + seq;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public int fundMatchTopById(int seq) {
        /* 1079 */ Fund target = null;
        /* 1080 */ if (this.funds == null || this.funds.size() == 0) {
            getFunds();
        }
        /* 1081 */ for (Fund one : this.funds) {
            /* 1082 */ if (one.getFundId().intValue() == seq) {
                /* 1083 */ target = one;
                /*      */ break;
                /*      */            }
            /*      */        }
        /* 1087 */ return (target == null) ? 0 : target.getMatchTop().intValue();
        /*      */    }

    /*      */
 /*      */
 /*      */ public int fundMaxOutIndById(int seq) {
        /* 1092 */ Fund target = null;
        /* 1093 */ if (this.funds == null || this.funds.size() == 0) {
            getFunds();
        }
        /* 1094 */ for (Fund one : this.funds) {
            /* 1095 */ if (one.getFundId().intValue() == seq) {
                /* 1096 */ target = one;
                /*      */ break;
                /*      */            }
            /*      */        }
        log.info("MAXXXXXXXXXXXXXXXXXXX  ", target.getMax_ind());
        /* 1100 */ return (target == null) ? -1 : target.getMax_ind().intValue();
        /*      */    }

    /*      */
 /*      */
 /*      */ public int fundNotesReqById(int seq) {
        /* 1105 */ Fund target = null;
        /* 1106 */ if (this.funds == null || this.funds.size() == 0) {
            getFunds();
        }
        /* 1107 */ for (Fund one : this.funds) {
            /* 1108 */ if (one.getFundId().intValue() == seq) {
                /* 1109 */ target = one;
                /*      */ break;
                /*      */            }
            /*      */        }
        /* 1113 */ return (target == null) ? -1 : (target.getReqNoteInd().equalsIgnoreCase("yes") ? 1 : 0);
        /*      */    }

    /*      */
 /*      */ public String getFundCodeById(int seq) {
        /* 1117 */ String code = "";
        /* 1118 */ Fund target = null;
        /* 1119 */ if (this.funds == null || this.funds.size() == 0) {
            getFunds();
        }
        /*      */
 /* 1121 */ for (Fund one : this.funds) {
            /* 1122 */ if (one.getFundId().intValue() == seq) {
                /* 1123 */ target = one;
                /*      */ break;
                /*      */            }
            /*      */        }
        /* 1127 */ if (target != null) {
            return target.getFundCode();
        }
        /* 1128 */ return "N/A";
        /*      */    }

    /*      */
 /*      */
 /*      */ public String getFundDescById(int seq) {
        /* 1133 */ String desc = "";
        /* 1134 */ Fund target = null;
        /* 1135 */ if (this.funds == null || this.funds.size() == 0) {
            getFunds();
        }
        /*      */
 /* 1137 */ for (Fund one : this.funds) {
            /* 1138 */ if (one.getFundId().intValue() == seq) {
                /* 1139 */ target = one;
                /*      */ break;
                /*      */            }
            /*      */        }
        /* 1143 */ if (target != null) {
            return target.getFundDesc();
        }
        /* 1144 */ return "No  fund  by id " + seq;
        /*      */    }

    /*      */
 /*      */ private String tranPercentage(BigDecimal perc) {
        /* 1148 */ int all = perc.movePointRight(4).intValueExact();
        /* 1149 */ int wh = all / 100;
        /* 1150 */ int fr = all % 100;
        /*      */
 /* 1152 */ return "" + wh + ((fr == 0) ? "" : ("." + fr)) + "%";
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public Integer[] getFunds_alone() {
        /* 1158 */ return this.funds_alone;
        /*      */    }

    /*      */
 /*      */ public List<SelectItem> getA_funds() {
        /* 1162 */ if (this.a_funds == null) {
            /* 1163 */ this.a_funds = new ArrayList<>();
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */ try {
                /* 1169 */ List<Integer> specials = Arrays.asList(this.funds_alone);
                /*      */
 /* 1171 */ SelectItem ns = new SelectItem();
                /* 1172 */ ns.setLabel(this.resourceBundle.getString("EstimateForm.institute.fundselectopt"));
                /* 1173 */ ns.setValue("0");
                /* 1174 */ ns.setNoSelectionOption(true);
                /* 1175 */ this.a_funds.add(ns);
                /*      */
 /* 1177 */ if (this.funds == null || this.funds.isEmpty()) {
                    getFunds();
                }
                /*      */
 /* 1179 */ if (this.funds == null || this.funds.isEmpty()) {
                    return this.a_funds;
                }
                /*      */
 /* 1181 */ for (int f = 0; f < this.funds.size(); f++) {
                    /* 1182 */ Fund one = this.funds.get(f);
                    /* 1183 */ if (!specials.contains(one.getFundId())) /* 1184 */ {
                        SelectItem opt = new SelectItem();
                        /* 1185 */ opt.setLabel(getFundTxtById(one.getFundId().intValue()));
                        /* 1186 */ opt.setValue(one.getFundId().toString());
                        /* 1187 */ this.a_funds.add(opt);
                    }
                    /*      */                }
                /* 1189 */            } catch (Exception e) {
                /* 1190 */ e.printStackTrace();
                /*      */            }
            /*      */        }
        /* 1193 */ return this.a_funds;
        /*      */    }

    /*      */
 /*      */ public int getAlone_funds_amt() {
        /* 1197 */ return this.alone_funds_amt;
        /*      */    }

    /*      */
 /*      */ public int getTotal_funds_amt() {
        /* 1201 */ return this.total_funds_amt;
        /*      */    }

    /*      */
 /*      */ public String fmtFullTimeWithZone(long ms, String tzstr) {
        /* 1205 */ if (ms == 0L || isEmp(tzstr)) {
            return "";
        }
        /* 1206 */ SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");
        /*      */
 /* 1208 */ sdf.setTimeZone(TimeZone.getTimeZone(getTzIdByShortName(tzstr)));
        /*      */
 /* 1210 */ return sdf.format(new Date(ms));
        /*      */    }

    /*      */
 /*      */ public String fmtFullNowTimeWithDefaultZone() {
        /* 1214 */ long ms = System.currentTimeMillis();
        /* 1215 */ String tzstr = getTzSN();
        /*      */
 /* 1217 */ SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");
        /*      */
 /* 1219 */ sdf.setTimeZone(TimeZone.getTimeZone(getTzIdByShortName(tzstr)));
        /*      */
 /* 1221 */ return sdf.format(new Date(ms));
        /*      */    }

    /*      */
 /*      */
 /*      */ public int convertCurrencyStrToInt(String str) {
        /* 1226 */ int res = 0;
        /* 1227 */ if (str == null || (str = str.trim()).isEmpty() || str.equalsIgnoreCase("n/a")) {
            /* 1228 */ return 0;
            /*      */        }
        /*      */
 /* 1231 */ str = str.replaceAll(",", "");
        /*      */
 /*      */
 /* 1234 */ str = str.replaceAll("\\$", "");
        /*      */
 /*      */
 /* 1237 */ return Integer.parseInt(str);
        /*      */    }

    /*      */
 /*      */ public String fmtMoney(Integer amt) {
        /* 1241 */ return this.moneyFmt.format(amt);
        /*      */    }

    /*      */
 /*      */
 /*      */ public boolean isEmp(String str) {
        /* 1246 */ return (str == null) ? true : (str.trim().isEmpty());
        /*      */    }

    /*      */
 /*      */ public boolean reachHostBySocket(String hostname, int port) {
        /* 1250 */ Socket socket = null;
        /* 1251 */ boolean reachable = false;
        /*      */
 /*      */ try {
            /* 1254 */ socket = new Socket();
            /* 1255 */ socket.setKeepAlive(false);
            /* 1256 */ socket.setSoLinger(false, 2);
            /*      */
 /* 1258 */ socket.setTcpNoDelay(true);
            /* 1259 */ socket.setSoTimeout(2000);
            /* 1260 */ SocketAddress sockaddr = new InetSocketAddress(hostname, port);
            /* 1261 */ socket.connect(sockaddr, 2000);
            /*      */
 /* 1263 */ reachable = true;
            /* 1264 */        } catch (SocketTimeoutException tx) {
            /*      */
 /* 1266 */ log.info("...reachHostBySocket() met SocketTimeoutException with server %s %d", new Object[]{hostname, Integer.valueOf(port)});
            /* 1267 */        } catch (UnknownHostException ex) {
            /*      */
 /* 1269 */ log.info("...reachHostBySocket() met UnknownHostException with server %s %d", new Object[]{hostname, Integer.valueOf(port)});
            /* 1270 */        } catch (ConnectException ne) {
            /* 1271 */ log.info("", ne);
            /* 1272 */        } catch (IOException ex) {
            /* 1273 */ log.info("", ex);
            /* 1274 */        } catch (Exception e) {
            /* 1275 */ log.info("", e);
            /*      */        } /*      */ finally {
            /*      */
 /* 1279 */ if (socket != null) try {
                socket.close();
            } catch (Exception exception) {
            }
            /*      */        }
        /* 1281 */ return reachable;
        /*      */    }

    /*      */
 /*      */
 /*      */ public int pingLdap() {
        /* 1286 */ return reachHostBySocket(this.seed.getLdapserver(), this.seed.getLdapport().intValue()) ? 1 : 0;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public PwdEncryptor getCipher() {
        /* 1294 */ return cipher;
        /*      */    }

    /*      */
 /*      */ public String getVerStr() {
        /* 1298 */ return (this.seed == null) ? "Version Unknown" : ("Version: " + this.clientverions + ((this.seed.getClientid().intValue() > 0) ? ("     ID: " + this.seed.getClientid()) : ""));
        /*      */    }

    /*      */
 /*      */ public String getJavaInfo() {
        /* 1302 */ StringBuilder sb = new StringBuilder(128);
        /* 1303 */ return sb.append(System.getProperty("java.runtime.version")).append('/').append(System.getProperty("java.vm.name")).append('/').append(System.getProperty("java.vm.version")).toString();
        /*      */    }

    /*      */
 /*      */ public String getOSinfo() {
        /* 1307 */ StringBuilder sb = new StringBuilder(128);
        /* 1308 */ String hostname = "host";
        /*      */ try {
            /* 1310 */ hostname = InetAddress.getLocalHost().getHostName();
            /* 1311 */        } catch (Exception exception) {
        }
        /* 1312 */ return sb.append(System.getProperty("user.name")).append('@').append(hostname).append('/')
                /* 1313 */.append(System.getProperty("os.name")).append('/').append(System.getProperty("os.arch")).append('/').append(System.getProperty("os.version")).toString();
        /*      */    }

    /*      */ public String getAddress() {
        /* 1316 */ return getIP() + "/" + getMAC();
        /*      */    }

    /*      */
 /*      */ public String getMAC() {
        /* 1320 */ String os = System.getProperty("os.name");
        /*      */ try {
            /* 1322 */ if (os.startsWith("Windows")) /* 1323 */ {
                return windowsParseMacAddress(windowsRunIpConfigCommand());
            }
            /* 1324 */ if (os.startsWith("Linux")) {
                /* 1325 */ return linuxParseMacAddress(linuxRunIfConfigCommand());
                /*      */            }
            /*      */
 /* 1328 */ return "N/A";
            /*      */        } /* 1330 */ catch (Exception ex) {
            /* 1331 */ ex.printStackTrace();
            /*      */
 /* 1333 */ return "N/A";
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public String getIP() {
        /* 1338 */ String os = System.getProperty("os.name");
        /*      */ try {
            /* 1340 */ if (os.startsWith("Windows")) /* 1341 */ {
                return windowsParseIpAddress(windowsRunIpConfigCommand());
            }
            /* 1342 */ if (os.startsWith("Linux")) {
                /* 1343 */ return linuxParseIpAddress(linuxRunIfConfigCommand());
                /*      */            }
            /* 1345 */ return "N/A";
            /*      */        } /* 1347 */ catch (Exception ex) {
            /* 1348 */ ex.printStackTrace();
            /* 1349 */ return "N/A";
            /*      */        }
        /*      */    }

    /*      */
 /*      */ private String linuxParseIpAddress(String ipConfigResponse) {
        /* 1354 */ StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
        /* 1355 */ String lastMacAddress = null;
        /*      */
 /* 1357 */ while (tokenizer.hasMoreTokens()) {
            /* 1358 */ String line = tokenizer.nextToken().trim();
            /*      */
 /* 1360 */ int macAddressPosition = line.indexOf("inet addr:");
            /* 1361 */ if (macAddressPosition < 0) /* 1362 */ {
                continue;
            }
            int loe = line.indexOf(" ", macAddressPosition + 10);
            /*      */
 /* 1364 */ String macAddressCandidate = line.substring(macAddressPosition + 10, loe).trim();
            /* 1365 */ if (linuxIsMacAddress(macAddressCandidate)) {
                /* 1366 */ lastMacAddress = ((lastMacAddress == null) ? "" : (lastMacAddress + ";")) + macAddressCandidate;
                /*      */            }
            /*      */        }
        /*      */
 /*      */
 /* 1371 */ return (lastMacAddress != null) ? lastMacAddress : "N/A";
        /*      */    }

    /*      */
 /*      */ private String windowsParseIpAddress(String ipConfigResponse) {
        /* 1375 */ StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
        /* 1376 */ String lastMacAddress = null;
        /*      */
 /* 1378 */ while (tokenizer.hasMoreTokens()) {
            /* 1379 */ String line = tokenizer.nextToken().trim();
            /* 1380 */ int macAddressPosition = line.indexOf(":");
            /* 1381 */ if (macAddressPosition <= 0) /*      */ {
                continue;
            }
            /* 1383 */ if (line.indexOf("IP Address") < 0) /* 1384 */ {
                continue;
            }
            String macAddressCandidate = line.substring(macAddressPosition + 1).trim();
            /* 1385 */ if (windowsIsMacAddress(macAddressCandidate)) /*      */ {
                /* 1387 */ lastMacAddress = ((lastMacAddress == null) ? "" : (lastMacAddress + ";")) + macAddressCandidate;
                /*      */            }
            /*      */        }
        /*      */
 /* 1391 */ return (lastMacAddress != null) ? lastMacAddress : "N/A";
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ private String linuxParseMacAddress(String ipConfigResponse) throws ParseException {
        /* 1403 */ StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
        /* 1404 */ String lastMacAddress = null;
        /*      */
 /* 1406 */ while (tokenizer.hasMoreTokens()) {
            /* 1407 */ String line = tokenizer.nextToken().trim();
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1417 */ int macAddressPosition = line.indexOf("HWaddr");
            /* 1418 */ if (macAddressPosition <= 0) /*      */ {
                continue;
            }
            /* 1420 */ String macAddressCandidate = line.substring(macAddressPosition + 6).trim();
            /* 1421 */ if (linuxIsMacAddress(macAddressCandidate)) {
                /* 1422 */ lastMacAddress = ((lastMacAddress == null) ? "" : (lastMacAddress + ";")) + macAddressCandidate;
                /*      */            }
            /*      */        }
        /*      */
 /*      */
 /* 1427 */ return (lastMacAddress != null) ? lastMacAddress : "N/A";
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ private boolean linuxIsMacAddress(String macAddressCandidate) {
        /* 1437 */ return (macAddressCandidate != null && macAddressCandidate.trim().length() > 7 && !macAddressCandidate.startsWith("127"));
        /*      */    }

    /*      */
 /*      */ private String linuxRunIfConfigCommand() throws IOException {
        /* 1441 */ Process p = Runtime.getRuntime().exec("ifconfig");
        /* 1442 */ InputStream stdoutStream = new BufferedInputStream(p.getInputStream());
        /*      */
 /* 1444 */ StringBuffer buffer = new StringBuffer();
        /*      */ while (true) {
            /* 1446 */ int c = stdoutStream.read();
            /* 1447 */ if (c == -1) /* 1448 */ {
                break;
            }
            buffer.append((char) c);
            /*      */        }
        /* 1450 */ String outputText = buffer.toString();
        /*      */
 /* 1452 */ stdoutStream.close();
        /*      */
 /* 1454 */ return outputText;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ private String windowsParseMacAddress(String ipConfigResponse) throws ParseException {
        /* 1465 */ StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
        /* 1466 */ String lastMacAddress = null;
        /*      */
 /* 1468 */ while (tokenizer.hasMoreTokens()) {
            /* 1469 */ String line = tokenizer.nextToken().trim();
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1477 */ int macAddressPosition = line.indexOf(":");
            /* 1478 */ if (macAddressPosition <= 0
                    || /* 1479 */ line.indexOf("Physical Address") < 0) /* 1480 */ {
                continue;
            }
            String macAddressCandidate = line.substring(macAddressPosition + 1).trim();
            /* 1481 */ if (windowsIsMacAddress(macAddressCandidate)) /*      */ {
                /* 1483 */ lastMacAddress = ((lastMacAddress == null) ? "" : (lastMacAddress + ";")) + macAddressCandidate;
                /*      */            }
            /*      */        }
        /*      */
 /* 1487 */ return (lastMacAddress != null) ? lastMacAddress : "N/A";
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ private boolean windowsIsMacAddress(String macAddressCandidate) {
        /* 1497 */ return (macAddressCandidate != null && macAddressCandidate.trim().length() > 7 && !macAddressCandidate.startsWith("127"));
        /*      */    }

    /*      */
 /*      */ private String windowsRunIpConfigCommand() throws IOException {
        /* 1501 */ Process p = Runtime.getRuntime().exec("ipconfig /all");
        /* 1502 */ InputStream stdoutStream = new BufferedInputStream(p.getInputStream());
        /*      */
 /* 1504 */ StringBuffer buffer = new StringBuffer();
        /*      */ while (true) {
            /* 1506 */ int c = stdoutStream.read();
            /* 1507 */ if (c == -1) /* 1508 */ {
                break;
            }
            buffer.append((char) c);
            /*      */        }
        /* 1510 */ String outputText = buffer.toString();
        /*      */
 /* 1512 */ stdoutStream.close();
        /*      */
 /* 1514 */ return outputText;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public String genFormTitleByRecid(String recid, Student one, Student modStud) {
        /* 1520 */ return (new StringBuilder(256)).append(isEmp(recid) ? "New Estimate " : "Estimate ").append(isEmp(recid) ? "" : ("(Based on doc " + recid + genModStudFisy(modStud) + ")")).toString();
        /*      */    }

    /*      */
 /*      */ private String genModStudFisy(Student modStud) {
        /* 1524 */ if (modStud == null) {
            return "";
        }
        /* 1525 */ return (this.fiscal_year == modStud.getStudentFisy()) ? "" : (" @" + modStud.getStudentFisy());
        /*      */    }

    /*      */
 /*      */ public String getFullclientverions() {
        /* 1529 */ return this.fullclientverions;
        /*      */    }

    /*      */
 /*      */ public void setFullclientverions(String fullclientverions) {
        /* 1533 */ this.fullclientverions = fullclientverions;
        /*      */    }

    /*      */
 /*      */ public String getPUNAME() {
        /* 1537 */ return this.PUNAME;
        /*      */    }

    /*      */
 /*      */ public String getJs_keepalive_url() {
        /* 1541 */ if (isEmp(this.js_keepalive_url)) {
            /* 1542 */ FacesContext facesContext = FacesContext.getCurrentInstance();
            /* 1543 */ ExternalContext ext = facesContext.getExternalContext();
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1552 */ StringBuilder sb = new StringBuilder(256);
            /* 1553 */ sb.append("http://").append(ext.getRequestServerName()).append(ext.getRequestContextPath()).append("/view/login.jsf");
            /*      */
 /* 1555 */ this.js_keepalive_url = sb.toString();
            /*      */        }
        /*      */
 /* 1558 */ return this.js_keepalive_url;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void poll2live() {
    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String getJSFMsgByKey(String key) {
        /* 1577 */ String res = "";
        /* 1578 */ if (!isEmp(key) && this.resourceBundle.containsKey(key)) {
            /* 1579 */ res = this.resourceBundle.getString(key);
            /*      */        }
        /* 1581 */ return res;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public Integer getLoan_origination_perc() {
        /* 1588 */ return this.loan_origination_perc;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public Integer getUniversity_grant_hard_top_limit() {
        /* 1595 */ return this.university_grant_hard_top_limit;
        /*      */    }

    /*      */
 /*      */
 /*      */ public void setStdFixInstAids(Student stud) {
        /* 1600 */ Integer[] prefunds = getFunds_alone();
        /* 1601 */ int t = 0;
        /* 1602 */ stud.setStudentAsScholarship1Name(getFundDescById(prefunds[t].intValue()));
        /* 1603 */ stud.setFund1id(prefunds[t]);
        /* 1604 */ t++;
        /*      */
 /*      */
 /* 1607 */ stud.setStudentAvScholarship2Name(getFundDescById(prefunds[t].intValue()));
        /* 1608 */ stud.setFund2id(prefunds[t]);
        /* 1609 */ t++;
        /*      */
 /* 1611 */ stud.setStudentAyScholarship3Name(getFundDescById(prefunds[t].intValue()));
        /* 1612 */ stud.setFund3id(prefunds[t]);
        /* 1613 */ t++;
        /*      */
 /* 1615 */ stud.setStudentBbScholarship4Name(getFundDescById(prefunds[t].intValue()));
        /* 1616 */ stud.setFund4id(prefunds[t]);
        /* 1617 */ t++;
        /*      */
 /* 1619 */ stud.setStudentBeScholarship5Name(getFundDescById(prefunds[t].intValue()));
        /* 1620 */ stud.setFund5id(prefunds[t]);
        /* 1621 */ t++;
        /*      */
 /* 1623 */ stud.setStudentBhScholarship6Name(getFundDescById(prefunds[t].intValue()));
        /* 1624 */ stud.setFund6id(prefunds[t]);
        /*      */    }

    /*      */
 /*      */
 /*      */ public int getSys_counselor_id() {
        /* 1629 */ return 0;
        /*      */    }

    /*      */
 /*      */ public String getStrYIA() {
        /* 1633 */ return this.strYIA;
        /*      */    }

    /*      */
 /*      */ public void setStrYIA(String strYIA) {
        /* 1637 */ this.strYIA = strYIA;
        /*      */    }

    /*      */
 /*      */ public String getStrQIA() {
        /* 1641 */ return this.strQIA;
        /*      */    }

    /*      */
 /*      */ public void setStrQIA(String strQIA) {
        /* 1645 */ this.strQIA = strQIA;
        /*      */    }

    /*      */
 /*      */ public String getStrMPN() {
        /* 1649 */ return this.strMPN;
        /*      */    }

    /*      */
 /*      */ public void setStrMPN(String strMPN) {
        /* 1653 */ this.strMPN = strMPN;
        /*      */    }
    /*      */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\AppReference.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
