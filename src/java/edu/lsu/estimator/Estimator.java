/*      */ package edu.lsu.estimator;

/*      */
 /*      */ import com.kingombo.slf5j.Logger;
/*      */ import com.kingombo.slf5j.LoggerFactory;
/*      */ import com.rits.cloning.Cloner;
/*      */ import edu.lsu.estimator.Accessor;
/*      */ import edu.lsu.estimator.AppReference;
/*      */ import edu.lsu.estimator.InfoState;
/*      */ import edu.lsu.estimator.Login;
/*      */ import edu.lsu.estimator.PDFgen;
/*      */ import edu.lsu.estimator.PackFunctions;
/*      */ import edu.lsu.estimator.Print;
/*      */ import edu.lsu.estimator.Querier;
/*      */ import edu.lsu.estimator.QueryStudModel;
/*      */ import edu.lsu.estimator.Student;
           import java.io.File;
/*      */ import java.io.Serializable;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
           import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.annotation.PostConstruct;
/*      */ import javax.enterprise.context.RequestScoped;
/*      */ import javax.enterprise.context.SessionScoped;
/*      */ import javax.faces.application.FacesMessage;
/*      */ import javax.faces.component.UIComponent;
/*      */ import javax.faces.component.UIInput;
/*      */ import javax.faces.component.UIViewRoot;
/*      */ import javax.faces.context.FacesContext;
/*      */ import javax.faces.event.ActionEvent;
/*      */ import javax.faces.event.AjaxBehaviorEvent;
/*      */ import javax.inject.Inject;
/*      */ import javax.inject.Named;
/*      */ import javax.mail.Authenticator;
/*      */ import javax.persistence.EntityManager;
/*      */ import javax.persistence.PersistenceContext;
/*      */ import javax.validation.ConstraintViolation;
/*      */ import javax.validation.Validation;
/*      */ import javax.validation.Validator;
/*      */ import javax.validation.ValidatorFactory;
/*      */ import javax.validation.constraints.Min;
/*      */ import javax.validation.constraints.NotNull;
/*      */ import org.apache.commons.mail.DefaultAuthenticator;
/*      */ import org.apache.commons.mail.EmailAttachment;
/*      */ import org.apache.commons.mail.HtmlEmail;
/*      */ import org.joda.time.DateMidnight;
/*      */ import org.joda.time.DateTime;
/*      */ import org.joda.time.ReadableInstant;
/*      */ import org.primefaces.context.RequestContext;
/*      */ import org.primefaces.event.SelectEvent;
/*      */ import org.primefaces.event.TabChangeEvent;
import org.apache.commons.beanutils.BeanUtils;

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
 /*      */ @Named
/*      */ @SessionScoped
/*      */ public class Estimator
        /*      */ implements Serializable /*      */ {

    /*      */ private static final long serialVersionUID = 1L;
    /*      */    @Inject
    /*      */ AppReference ref;
    /*      */    @Inject
    /*      */ InfoState info;
    /*   80 */    private static final Logger log = LoggerFactory.getLogger();
    @PersistenceContext
    /*      */ private EntityManager em;
    /*      */    @Inject
    /*      */    @RequestScoped
    /*      */ FacesContext facesContext;
    /*      */    @Inject
    /*      */ private Student stud;
    /*      */    @Inject
    /*      */ private PackFunctions calc;
    /*      */    @Inject
    /*      */ Accessor accessor;
    /*      */    @Inject
    /*      */ Login login;
    /*      */    private int modflag;
    /*      */    private Student modStud;
    /*   95 */    private int tab_index = 0;
    /*   96 */    private String[] tab_titles = new String[]{"Personal", "Acdemic", "Financial", "Institutional", "Notes and Adjustment"};
    /*      */
 /*      */    private String PreTabString;
    /*      */
 /*      */    private String NextTabString;
    /*      */
 /*      */    private String std_merits;
    /*      */    private boolean std_merit1 = false;
    /*      */    private boolean std_merit2 = false;
    /*      */    private boolean std_merit3 = false;
    /*  106 */    private String last_merit = null;
    /*      */
 /*      */    private List<String> std_quas;
    /*      */
 /*      */    private int std_EA_PERCENT;
    /*      */
 /*      */    private boolean std_intl;
               private boolean std_tf;
    /*      */private boolean std_ftfr;
 /*      */    private boolean std_marry;
    /*      */
 /*      */    private boolean std_sda;
    /*      */    private boolean std_dorm;
    /*      */    private boolean std_indept;
    /*      */    private boolean std_fafsa;
    /*  120 */    private HashMap<String, String> std_indepts = new HashMap<>();
    /*  121 */    private int std_indept_byhand = 0;
    /*      */
 /*  123 */    private String std_sex = "N";
    /*      */
 /*      */    private boolean std_sex1 = false;
    /*      */
 /*      */    private boolean std_sex2 = false;
    /*      */
 /*      */    private boolean std_sex3 = true;
    /*      */
 /*      */    private boolean std_calgrant;
    /*      */
 /*      */    private boolean std_efc;
    /*      */    private boolean std_ealsu;
    /*      */    private boolean std_eanonlsu;
    /*      */    private boolean std_noloans = false;
    /*      */    private boolean in_subloan = true;
    /*      */    private boolean in_unsubloan = true;
    /*      */    private boolean in_fws = true;
    /*      */    private boolean adjust_calgrantamt_ind;
    /*      */    private String std_dob;
    /*      */    static final String DATE_PATTERN_YYYY = "(0?[1-9]|1[012])/?(0?[1-9]|[12][0-9]|3[01])/?((19|20)\\d\\d)";
    /*      */    static final String DATE_PATTERN_YY = "(0?[1-9]|1[012])/?(0?[1-9]|[12][0-9]|3[01])/?\\d\\d";
    /*      */    static final String DATE_PATTERN = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)?\\d\\d)";
    /*      */    @NotNull
    /*      */    @Min(0L)
    /*  147 */ private Integer costudents = Integer.valueOf(0);
    /*      */
 /*  149 */    private final int NOTESINIT = 9;
    /*  150 */    private int notestot = 9;
    /*  151 */    private int shownotes = 9;
    /*  152 */    private int[] notesind = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    /*      */
 /*  154 */    private HashMap<String, String> check_inds = new HashMap<>();
    /*      */
 /*      */
 /*  157 */    private String fund1extra = "";
    /*  158 */    private String fund2extra = "";
    /*  159 */    private String fund3extra = "";
    /*  160 */    private String fund4extra = "";
    /*  161 */    private String fund5extra = "";
    /*  162 */    private String fund6extra = "";
    /*      */
 /*      */
 /*      */
 /*      */
 /*  167 */    private String popind = "0";
    /*  168 */    private String popkey = "";
    /*      */
 /*  170 */    private List<Student> popups = new ArrayList<>();
    /*      */
 /*      */    private QueryStudModel popDataModel;
    /*      */
 /*      */    private Student chosenPopStud;
    /*      */
 /*      */    private String std_ln;
    /*      */
 /*      */    private String std_fn;
    /*      */
 /*      */    private Integer stdo_fundid7;
    /*      */
 /*      */    private Integer stdo_fundid8;
    /*      */
 /*      */    private Integer stdo_fundid9;
    /*      */
 /*      */    private String stdo_sex;
    /*      */
 /*      */    private boolean stdo_intl;
    /*      */
    private boolean stdo_tf;
    private boolean stdo_ftfr;


    private boolean std_pellGrant;

    
    private int std_pellGrant_value;
    /*      */    private boolean stdo_marry;
    /*      */
 /*      */    private boolean stdo_sda;
    /*      */
 /*      */    private boolean stdo_dorm;
    /*      */
 /*      */    private boolean stdo_indept;
    /*      */
 /*      */    private String stdo_academic;
    /*      */
 /*      */    private boolean stdo_fafsa;
    /*      */
 /*      */    private boolean stdo_calgrant;
    /*      */
 /*      */    private boolean stdo_efc;
    /*      */
 /*      */    private boolean stdo_ealsu;
    /*      */
 /*      */    private boolean stdo_eanonlsu;
    /*      */
 /*      */    private boolean stdo_noloans;
    /*      */
 /*      */    private boolean stdo_in_subloan;
    /*      */
 /*      */    private boolean stdo_in_unsubloan;
    /*      */    private boolean stdo_in_fws;
    /*      */    private boolean stdo_adjust_calgrantamt_ind;
    /*      */    private String stdo_merits;
    /*  218 */    private HashMap<String, String> stdo_indepts = new HashMap<>();
    /*      */
 /*  220 */    private int tbodyrow = 0;
    /*      */
 /*      */
 /*      */
 /*      */    private int sfund1amt = 0;
    /*      */
 /*      */
 /*      */
 /*      */    private int sfund2amt = 0;
    /*      */
 /*      */
 /*      */
 /*      */    private int sfund7amt;
    /*      */
 /*      */
 /*      */
 /*      */    private int sfund8amt;
    /*      */
 /*      */
 /*      */    private int sfund9amt;
    /*      */
 /*      */
 /*  242 */    private String addlasuid = "";
    /*      */
 /*      */    private boolean return_std_ind;
    /*      */
 /*      */    private boolean nc_std_ind;
    /*      */
 /*      */    private boolean stdo_return_ind;
    /*      */
 /*      */    private boolean stdo_nc_ind;

    /*      */
 /*      */ public Estimator() {
        /*  253 */ this.modflag = 0;
        /*  254 */ this.modStud = null;
        /*  255 */ this.tab_index = 0;
        /*      */
 /*  257 */ this.std_sex = "N";
        /*  258 */ this.std_sex1 = false;
        /*  259 */ this.std_sex2 = false;
        /*  260 */ this.std_sex3 = true;
        /*      */    }

    /*      */
 /*      */
 /*      */ @PostConstruct
    /*      */ public void syncFromStudObj() {
        /*  266 */ this.stdo_fundid7 = this.stud.getFund7id();
        /*  267 */ this.stdo_fundid8 = this.stud.getFund8id();
        /*  268 */ this.stdo_fundid9 = this.stud.getFund9id();
        /*      */
 /*      */
 /*  271 */ this.std_indepts.clear();
        /*      */
 /*  273 */ String str = this.stud.getStudentLIntlStud();
        /*  274 */ this.std_intl = !(str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no"));
        /*  275 */ this.stdo_intl = this.std_intl;
        /*      */
//add the TF 
        int _i = this.stud.getStd_transfer_ind();
        this.std_tf = _i == 1 ? true : false;
        this.stdo_tf = this.std_tf;
        
        int _j= this.stud.getStd_1st_freshmen();
        this.std_ftfr = _j == 1 ? true: false;
        this.stdo_ftfr=this.std_ftfr;

        /*  277 */ str = this.stud.getStudentMMarry();
        /*  278 */ this.std_marry = !(str == null || str.isEmpty() || str.trim().equalsIgnoreCase("Single") || str.trim().equalsIgnoreCase("No"));
        /*  279 */ this.stdo_marry = this.std_marry;
        /*      */
 /*      */
 /*  282 */ str = this.stud.getStudentNSda();
        /*  283 */ this.std_sda = !(str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no"));
        /*  284 */ this.stdo_sda = this.std_sda;
        /*      */
 /*  286 */ str = this.stud.getStudentWDorm();
        /*  287 */ this.std_dorm = !(str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no"));
        /*  288 */ this.stdo_dorm = this.std_dorm;
        /*      */
 /*  290 */ str = this.stud.getStudentXFafsa();
        /*  291 */ this.std_fafsa = !(str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no"));
        /*  292 */ this.stdo_fafsa = this.std_fafsa;
        /*      */
 /*  294 */ str = this.stud.getIndEalsu();
        /*  295 */ this.std_ealsu = !(str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no"));
        /*  296 */ this.stdo_ealsu = this.std_ealsu;
        /*      */
 /*  298 */ str = this.stud.getIndEanonlsu();
        /*  299 */ if (this.stud.getStudentAgNonlsuAllowrance() != null && this.stud.getStudentAgNonlsuAllowrance().intValue() > 0) {
            /*  300 */ str = "Yes";
            /*      */        } else {
            /*  302 */ str = "No";
            /*      */        }
        /*  304 */ this.std_eanonlsu = !(str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no"));
        /*  305 */ this.stdo_eanonlsu = this.std_eanonlsu;
        /*      */
 /*  307 */ str = this.stud.getAdjCalgrantInd();
        /*  308 */ this.adjust_calgrantamt_ind = !(str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no"));
        /*  309 */ this.stdo_adjust_calgrantamt_ind = this.adjust_calgrantamt_ind;
        /*      */
 /*  311 */ str = this.stud.getIndExcloans();
        /*  312 */ this.std_noloans = !(str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no"));
        /*  313 */ this.stdo_noloans = this.std_noloans;
        /*      */
 /*  315 */ str = this.stud.getIndEfc();
        /*  316 */ this.std_efc = !(str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no"));
        /*  317 */ this.stdo_efc = this.std_efc;
        /*      */
 /*  319 */ str = this.stud.getStudentZCalgrant();
        /*  320 */ this.std_calgrant = !(str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no"));
        /*  321 */ this.stdo_calgrant = this.std_calgrant;
        /*      */
 /*  323 */ str = this.stud.getStudentApSubLoans();
        /*  324 */ this.in_subloan = !(str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no"));
        /*  325 */ this.stdo_in_subloan = this.in_subloan;
        /*      */
 /*  327 */ str = this.stud.getStudentAqUnsubLoans();
        /*  328 */ this.in_unsubloan = !(str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no"));
        /*  329 */ this.stdo_in_unsubloan = this.in_unsubloan;
        /*      */
 /*  331 */ str = this.stud.getStudentArFws();
        /*  332 */ this.in_fws = !(str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no"));
        /*  333 */ this.stdo_in_fws = this.in_fws;
        /*      */
 /*  335 */ str = this.stud.getStudentYIndept();
        /*  336 */ this.std_indept = !(str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no"));
        /*      */
 /*  338 */ if (this.std_marry) {
            /*  339 */ this.std_indept = true;
            /*  340 */ this.std_indepts.put("marry", "marry");
            /*      */
 /*  342 */ if (this.ref.isEmp(str) || str.equalsIgnoreCase("no")) {
                /*  343 */ this.stud.setStudentYIndept("Yes");
                /*      */            }
            /*      */        }
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  351 */ if (this.std_eanonlsu) {
            /*  352 */ this.stud.setStudentAiEduAllowPer((new BigDecimal(this.calc.getEaNonLsuPercentageByDorm(this.std_dorm))).divide(new BigDecimal(100)));
            /*      */        } else {
            /*  354 */ this.stud.setStudentAiEduAllowPer(BigDecimal.ZERO);
            /*      */        }
        /*      */
 /*      */
 /*      */
 /*      */
 /*  360 */ this.stud.setStudentPassword("password");
        /*  361 */ this.stud.setStudentBwProgress(Integer.valueOf(0));
        /*  362 */ this.stud.setStudentStudType("FYUG");
        /*      */
 /*      */
 /*      */
 /*  366 */ this.std_sex = this.stud.getSex();
        /*  367 */ if (this.std_sex == null || this.std_sex.isEmpty()) {
            /*  368 */ this.std_sex = "N";
            /*  369 */ this.stud.setSex("N");
            /*      */        }
        /*  371 */ this.std_sex1 = false;
        this.std_sex2 = false;
        this.std_sex3 = false;
        /*  372 */ switch (this.std_sex) {
            case "F":
                /*  373 */ this.std_sex1 = true;
                break;
            /*  374 */ case "M":
                this.std_sex2 = true;
                break;
            /*  375 */ case "N":
                this.std_sex3 = true;
                break;
        }
        /*      */
 /*  377 */ this.stdo_sex = this.std_sex;
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
 /*  388 */ this.std_merits = this.stud.getStudentSMerit();
        /*  389 */ this.std_merit1 = false;
        this.std_merit2 = false;
        this.std_merit3 = false;
        /*  390 */ if (!this.ref.isEmp(this.std_merits)) /*  391 */ {
            switch (this.std_merits) {
                case "MC":
                    /*  392 */ this.std_merit1 = true;
                    break;
                /*  393 */ case "MS":
                    this.std_merit2 = true;
                    break;
                /*  394 */ case "MF":
                    this.std_merit3 = true;
                    /*      */ break;
            }
        }
        /*      */
 /*  397 */ this.stdo_merits = this.std_merits;
        /*      */
 /*      */
 /*  400 */ this.stdo_academic = this.stud.getStudentUAcademic();
        /*      */
 /*      */
 /*      */
 /*      */
 /*  405 */ String dob = this.stud.getStudentDDob();
        /*  406 */ if (dob != null && dob.trim().length() == 10) {
            /*  407 */ this.std_dob = dob;
            /*      */
 /*      */ try {
                /*  410 */ DateTime dt = this.ref.parseDateStr(this.std_dob, this.ref.getDateInputShowStr());
                /*  411 */ DateMidnight dm = new DateMidnight(dt);
                /*  412 */ DateMidnight due = new DateMidnight(this.ref.getIndept_dob());
                /*  413 */ if (dm.isBefore((ReadableInstant) due)) {
                    /*      */
 /*  415 */ this.std_indepts.put("dob", "dob");
                    /*      */
 /*  417 */ this.std_indept = true;
                    /*  418 */ str = this.stud.getStudentYIndept();
                    /*      */
 /*  420 */ if (this.ref.isEmp(str) || str.equalsIgnoreCase("no")) {
                        /*  421 */ this.stud.setStudentYIndept("Yes");
                        /*      */                    }
                    /*      */                } else {
                    /*      */
 /*  425 */ this.std_indepts.remove("dob");
                    /*      */                }
                /*  427 */            } catch (Exception e) {
                /*  428 */ this.std_indepts.remove("dob");
                /*  429 */ e.printStackTrace();
                /*      */            }
            /*      */        } else {
            /*      */
 /*  433 */ this.std_dob = null;
            /*  434 */ this.std_indepts.remove("dob");
            /*      */        }
        /*  436 */ this.stdo_indepts = this.std_indepts;
        /*  437 */ this.stdo_indept = this.std_indept;
        /*      */
 /*  439 */ this.std_ln = this.stud.getStudentBLastname();
        /*  440 */ this.std_fn = this.stud.getStudentCFirstname();
        /*      */
 /*      */
 /*  443 */ this.costudents = this.stud.getHomecostudies();
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  449 */ int diffyear = 0;
        /*  450 */ if (this.stud.getStudentFisy() != this.ref.getFiscal_year()) /*      */ {
            /*  452 */ diffyear = 1;
            /*      */        }
        /*      */
 /*  455 */ Integer[] prefunds = this.ref.getFunds_alone();
        /*  456 */ int t = 0;
        /*  457 */ this.stud.setStudentAsScholarship1Name(this.ref.getFundDescById(prefunds[t].intValue()));
        /*  458 */ this.stud.setFund1id(prefunds[t]);
        /*  459 */ if (diffyear > 0 && this.ref.fundMaxOutIndById(prefunds[t].intValue()) < 0) {
            this.stud.setStudentAuScholarship1Amt(Integer.valueOf(0));
        }
        /*  460 */ this.fund1extra = this.ref.getFundExtById(prefunds[t++].intValue());
        /*      */
 /*      */
 /*  463 */ this.stud.setStudentAvScholarship2Name(this.ref.getFundDescById(prefunds[t].intValue()));
        /*  464 */ this.stud.setFund2id(prefunds[t]);
        /*  465 */ if (diffyear > 0 && this.ref.fundMaxOutIndById(prefunds[t].intValue()) < 0) {
            this.stud.setStudentAxScholarship2Amt(Integer.valueOf(0));
        }
        /*  466 */ this.fund2extra = this.ref.getFundExtById(prefunds[t++].intValue());
        /*      */
 /*  468 */ this.stud.setStudentAyScholarship3Name(this.ref.getFundDescById(prefunds[t].intValue()));
        /*  469 */ this.stud.setFund3id(prefunds[t]);
        /*  470 */ if (diffyear > 0 && this.ref.fundMaxOutIndById(prefunds[t].intValue()) < 0) {
            this.stud.setStudentBaScholarship3Amt(Integer.valueOf(0));
        }
        /*  471 */ this.fund3extra = this.ref.getFundExtById(prefunds[t++].intValue());
        /*      */
 /*  473 */ this.stud.setStudentBbScholarship4Name(this.ref.getFundDescById(prefunds[t].intValue()));
        /*  474 */ this.stud.setFund4id(prefunds[t]);
        /*  475 */ if (diffyear > 0 && this.ref.fundMaxOutIndById(prefunds[t].intValue()) < 0) {
            this.stud.setStudentBdScholarship4Amt(Integer.valueOf(0));
        }
        /*  476 */ this.fund4extra = this.ref.getFundExtById(prefunds[t++].intValue());
        /*      */
 /*  478 */ this.stud.setStudentBeScholarship5Name(this.ref.getFundDescById(prefunds[t].intValue()));
        /*  479 */ this.stud.setFund5id(prefunds[t]);
        /*  480 */ if (diffyear > 0 && this.ref.fundMaxOutIndById(prefunds[t].intValue()) < 0) {
            this.stud.setStudentBgScholarship5Amt(Integer.valueOf(0));
        }
        /*  481 */ this.fund5extra = this.ref.getFundExtById(prefunds[t++].intValue());
        /*      */
 /*  483 */ this.stud.setStudentBhScholarship6Name(this.ref.getFundDescById(prefunds[t].intValue()));
        /*  484 */ this.stud.setFund6id(prefunds[t]);
        /*  485 */ if (diffyear > 0 && this.ref.fundMaxOutIndById(prefunds[t].intValue()) < 0) {
            this.stud.setStudentBjScholarship6Amt(Integer.valueOf(0));
        }
        /*  486 */ this.fund6extra = this.ref.getFundExtById(prefunds[t++].intValue());
        /*      */
 /*      */
 /*      */
 /*  490 */ this.sfund1amt = this.stud.getStudentAuScholarship1Amt() != null ? this.stud.getStudentAuScholarship1Amt().intValue() : this.sfund1amt;
        /*  491 */ this.sfund2amt = this.stud.getStudentAxScholarship2Amt() != null ? this.stud.getStudentAxScholarship2Amt().intValue() : this.sfund2amt;
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  498 */ t = this.stud.getFund7id() != null ? this.stud.getFund7id().intValue() : 0;
        /*  499 */ if (t > 0 && diffyear > 0 && this.ref.fundMaxOutIndById(prefunds[t].intValue()) < 0) {
            this.stud.setStudentBmScholarship7Amt(Integer.valueOf(0));
        }
        /*      */
 /*  501 */ t = this.stud.getFund8id() != null ? this.stud.getFund8id().intValue() : 0;
        /*  502 */ if (t > 0 && diffyear > 0 && this.ref.fundMaxOutIndById(prefunds[t].intValue()) < 0) {
            this.stud.setStudentBpScholarship8Amt(Integer.valueOf(0));
        }
        /*      */
 /*  504 */ t = this.stud.getFund9id() != null ? this.stud.getFund9id().intValue() : 0;
        /*  505 */ if (t > 0 && diffyear > 0 && this.ref.fundMaxOutIndById(prefunds[t].intValue()) < 0) {
            this.stud.setStudentBsScholarship9Amt(Integer.valueOf(0));
        }
        /*      */
 /*  507 */ this.sfund7amt = this.stud.getStudentBmScholarship7Amt() != null ? this.stud.getStudentBmScholarship7Amt().intValue() : 0;
        /*  508 */ this.sfund8amt = this.stud.getStudentBpScholarship8Amt() != null ? this.stud.getStudentBpScholarship8Amt().intValue() : 0;
        /*  509 */ this.sfund9amt = this.stud.getStudentBsScholarship9Amt() != null ? this.stud.getStudentBsScholarship9Amt().intValue() : 0;
        /*      */
 /*  511 */ this.stud.setStudentFisy(this.ref.getFiscal_year());
        /*      */
 /*      */
 /*  514 */ Integer tio = this.stud.getReturnStdInd();
        /*  515 */ this.return_std_ind = !(tio == null || tio.intValue() == 0);
        /*  516 */ tio = this.stud.getNcStdInd();
        /*  517 */ this.nc_std_ind = !(tio == null || tio.intValue() == 0);
        /*      */
 /*      */
 /*  520 */ this.calc.setStud(this.stud);
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  530 */ this.notestot = this.ref.getTotal_funds_amt();
        /*  531 */ this.shownotes = (9 >= this.ref.getAlone_funds_amt()) ? 9 : this.ref.getAlone_funds_amt();
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String modEstimate(Student modStudent) {
        /*  538 */ if (modStudent == null) {
            return null;
        }
        /*  539 */ this.modflag = 1;
        /*  540 */ this.modStud = modStudent;
        /*      */
 /*  542 */ Cloner cloner = new Cloner();
        /*      */
 /*  544 */ this.stud = (Student) cloner.shallowClone(modStudent);
        /*  545 */ this.stud.setPdfs(new ArrayList());
        /*  546 */ this.stud.setPrtTimes(Integer.valueOf(0));
        /*      */
 /*      */
 /*  549 */ this.calc.setStud(this.stud);
        /*  550 */ syncFromStudObj();
        /*      */
 /*  552 */ loadStudInstituteScholarshipAwards();
        /*      */
 /*      */
 /*  555 */ this.tab_index = 0;
        /*      */
 /*  557 */ return "estimate-new?faces-redirect=true";
        /*      */    }

    /*      */
 /*      */ public String newEstimate() {
        /*  561 */ this.stud = new Student();
        /*  562 */ this.modflag = 0;
        /*  563 */ this.modStud = null;
        /*  564 */ this.stud.setPrtTimes(Integer.valueOf(0));
        /*  565 */ this.stud.setPdfs(null);
        /*      */
 /*  567 */ this.stud.setCounselorId(this.login.getCurrentUser().getUserid());
        /*  568 */ this.stud.setCounselorOrig(this.login.getCurrentUser().getUserid());
        /*  569 */ this.stud.setStudentBuOrigCounselor(this.login.getCurrentUser().getUsername());
        /*      */
 /*      */
 /*  572 */ this.calc.setStud(this.stud);
        /*      */
 /*      */
 /*  575 */ syncFromStudObj();
        /*      */
 /*  577 */ this.tab_index = 0;
        /*      */
 /*  579 */ return "estimate-new?faces-redirect=true";
        /*      */    }

    /*      */
 /*      */
 /*      */ public void newPEstimate() {
        /*  584 */ this.stud = new Student();
        /*  585 */ this.modflag = 0;
        /*  586 */ this.modStud = null;
        /*  587 */ this.stud.setPrtTimes(Integer.valueOf(0));
        /*  588 */ this.stud.setPdfs(null);
        /*      */
 /*  590 */ this.stud.setCounselorId(Integer.valueOf(this.ref.getSys_counselor_id()));
        /*      */
 /*      */
 /*  593 */ this.stud.setCounselorOrig(Integer.valueOf(this.ref.getSys_counselor_id()));
        /*  594 */ this.stud.setStudentBuOrigCounselor("MyCampus Portal");
        /*      */
 /*      */
 /*  597 */ this.calc.setStud(this.stud);
        /*      */
 /*      */
 /*  600 */ syncFromStudObj();
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public void tabChanged(TabChangeEvent event) {
        /*  606 */ String title = event.getTab().getTitle();
        /*  607 */ for (int i = 0; i < this.tab_titles.length; i++) {
            /*  608 */ if (this.tab_titles[i].equalsIgnoreCase(title)) {
                /*  609 */ this.tab_index = i;
                /*      */
 /*      */ break;
                /*      */            }
            /*      */        }
        /*      */
 /*  615 */ RequestContext reqContext = RequestContext.getCurrentInstance();
        /*      */
 /*      */
 /*  618 */ setClientCallBack(reqContext);
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
 /*      */ private int validateStudBean() {
        /*  634 */ ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        /*      */
 /*      */
 /*  637 */ Validator validator = factory.getValidator();

        /*  653 */ String[] prop_names = {"studentALsuid", "studentBLastname", "studentCFirstname", "studentDDob", "studentFPhone", "studentEEmail", "studentIState", "studentJZip"};
        /*  654 */ String[] uic_ids = {"std_lsuid", "std_ln", "std_fn", "std_dob", "std_phone", "std_email", "std_state", "std_zip", "std_gpa"};
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  664 */ int vios = 0;
        /*      */
 /*      */
 /*  667 */ for (int i = 0; i < prop_names.length; i++) {
            /*  668 */ String prop = prop_names[i];
            /*  669 */ Set<ConstraintViolation<Student>> propconstraintViolations = validator.validateProperty(this.stud, prop, new Class[0]);
            /*      */
 /*  671 */ if (propconstraintViolations.size() > 0) {
                /*  672 */ UIComponent c = getUIComponentById(uic_ids[i]);
                /*  673 */ if (c == null) {
                    this.facesContext.getViewRoot().findComponent(uic_ids[i]);
                }
                /*  674 */ UIInput textInput = (UIInput) c;
                /*  675 */ log.info("validateStudBean looping property %s (id=%s)  got UIComponent==null? %s", new Object[]{prop, uic_ids[i], Boolean.valueOf((c == null))});
                /*  676 */ String label = "";
                /*      */
 /*  678 */ if (textInput != null) {
                    /*  679 */ textInput.setValid(false);
                    /*  680 */ label = (String) c.getAttributes().get("label");
                    /*  681 */ label = label + ": ";
                    /*      */                }
                /*  683 */ for (ConstraintViolation<Student> one : propconstraintViolations) {
                    /*  684 */ log.info("validateStudBean found violation. msg=%s, path=%s", new Object[]{one.getMessage(), one.getPropertyPath().toString()});
                    /*  685 */ FacesMessage err = new FacesMessage(" ");
                    /*  686 */ err.setSeverity(FacesMessage.SEVERITY_ERROR);
                    /*  687 */ err.setDetail(label + one.getMessage());
                    /*  688 */ err.setSummary(err.getDetail());
                    /*  689 */ this.facesContext.addMessage(null, err);
                    /*  690 */ vios++;
                    /*      */                }
                /*      */            }
            /*      */        }
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  703 */ return vios;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String toEmail() {
        /*  708 */ log.info("==================toEmail()   button clicked ...........................");
        /*  709 */ String ln = this.stud.getStudentBLastname();
        /*  710 */ String fn = this.stud.getStudentCFirstname();
        /*      */ String recid = this.stud.getRecid();
        /*  712 */ String dob = this.stud.getStudentDDob();
        /*  713 */ String phone = this.stud.getStudentFPhone();
        /*  714 */ String msg = chkStudBasicInfo(new Integer[0]);
        /*  715 */ log.info("==================toEmail() got chkStudBasicInfo() res msg=%s", new Object[]{msg});
        /*  716 */ if (!msg.isEmpty()) {
            return null;
        }
        /*      */
 /*  718 */ int vio = validateStudBean();
        /*  719 */ log.info("==================toEmail() got validateStudBean() res code=%d", new Object[]{Integer.valueOf(vio)});
        /*  720 */ if (vio > 0) {
            /*  721 */ return null;
            /*      */        }
        /*      */
 /*  724 */ this.stud.setStudentUserName(compUserName(this.stud.getStudentALsuid(), ln, fn, dob));
        /*  725 */   // if(!recid.equals("tmpid"))
        msg = saveStud(1, new int[0]);
        if (msg.contains("Transaction aborted")) {
            msg = "Error: Student data exists already.";;
        }

        /*      */
 /*      */
 /*  728 */ if ((msg == null || msg.isEmpty())) {
            /*  729 */ FacesMessage guimsg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_INFO, "EstimateForm.DataSaved");
            /*  730 */ if (this.facesContext != null) {
                this.facesContext.addMessage(null, guimsg);
            }
            /*  731 */ this.stud.setStudentNumb((Integer) null);
            /*      */
 /*      */
 /*  734 */ PDFgen pdf = new PDFgen();
            /*  735 */ Print prt = new Print();
            /*  736 */ prt.setSship1ExtAmt(Integer.valueOf(0));
            /*  737 */ prt.setSship2ExtAmt(Integer.valueOf(0));
            /*  738 */ prt.setSship7ExtAmt(Integer.valueOf(0));
            /*  739 */ prt.setSship8ExtAmt(Integer.valueOf(0));
            /*  740 */ prt.setSship9ExtAmt(Integer.valueOf(0));
            /*      */
 /*      */
 /*  743 */ msg = pdf.genPDF(this.stud, this.login, this.ref, this.calc, prt, 2, new Integer[0]);
            /*  744 */ if (!msg.startsWith("Failed")) {
                /*  745 */ log.info("==================toEmail() gen pdf: pass");
                /*  746 */ String pdfname = msg;
                /*      */
 /*  748 */ msg = savePrint(prt, 2, new Integer[0]);
                /*  749 */ if (msg == null || msg.isEmpty()) {
                    /*  750 */ log.info("==================toEmail() save print rec: pass");
                    /*      */
 /*  752 */ msg = sendEmailLASU(pdf.getPdfRoot(), pdfname, this.stud.getStudentEEmail(), this.stud.getStudentCFirstname() + " " + this.stud.getStudentBLastname());
                    /*      */
 /*  754 */ if (msg.startsWith("sent estimate")) {
                        /*  755 */ log.info("==================toEmail() sent pdf: %s ........", new Object[]{msg});
                        /*  756 */ FacesMessage pdfmsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_INFO, msg);
                        /*  757 */ if (this.facesContext != null) {
                            this.facesContext.addMessage(null, pdfmsg);
                        }
                        /*      */
 /*      */                    } else {
                        /*  760 */ FacesMessage pdfmsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                        /*  761 */ if (this.facesContext != null) {
                            this.facesContext.addMessage(null, pdfmsg);
                        }
                        /*  762 */ log.info("==================toEmail() failed to send email: %s ........", new Object[]{msg});
                        /*  763 */ return null;
                        /*      */                    }
                    /*      */
 /*  766 */ msg = this.accessor.updateStudPrtTimes(this.stud);
                    /*  767 */ if (msg != null && !msg.isEmpty()) {
                        /*  768 */ log.info("==================toEmail() failed to update stud obj %s with msg: %s", new Object[]{this.stud.getRecid(), msg});
                        /*      */                    }
                    /*      */                } else {
                    /*  771 */ FacesMessage pdfmsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    /*  772 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, pdfmsg);
                    }
                    /*  773 */ log.info("==================toEmail() failed to save print rec: %s ........", new Object[]{msg});
                    /*      */                }
                /*      */            } else {
                /*  776 */ log.info("==================toEmail() gen pdf failed: %s ........", new Object[]{msg});
                /*  777 */ FacesMessage pdfmsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                /*  778 */ if (this.facesContext != null) {
                    this.facesContext.addMessage(null, pdfmsg);
                }
                /*      */
 /*      */            }
            /*  781 */        } else if (!msg.equals("processed")) {
            /*  782 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
            /*  783 */ if (this.facesContext != null) {
                this.facesContext.addMessage(null, guimsg);
            }
            /*      */        }
        /*  785 */ return null;
        /*      */    }

    /*      */
 /*      */ private String sendEmailLASU(String pdffolder, String pdfname, String emailbox, String receiverName) {
        /*  789 */ String smtpServer = "smtp.gmail.com";
        /*  790 */ int smtpPort = 465;
        /*  791 */ String senderBox = "enroll@lasierra.edu";
        /*  792 */ String senderName = "La Sierra University Enrollment Services Office";
        /*      */
 /*  794 */ String msg = "";
        /*  795 */ if (this.ref.isEmp(pdfname) || !pdfname.endsWith(".pdf")) {
            /*  796 */ msg = "invalid file name. Can not email estimate.";
            /*  797 */ return msg;
            /*      */        }
        /*      */
 /*  800 */ log.info("\n sendEmailLASU() is invoked to send PDF fiel to [%s]", new Object[]{emailbox});
        /*  801 */ if (this.ref.isEmp(emailbox)) {
            /*  802 */ msg = "invalid E-mail address '" + emailbox + "'. Can not email estimate.";
            /*  803 */ return msg;
            /*      */        }
        /*      */
 /*      */
 /*      */ try {
            /*  808 */ HtmlEmail email = new HtmlEmail();
            /*      */
 /*      */
 /*  811 */ email.addTo(emailbox, receiverName);
            /*  812 */ email.setFrom(senderBox, senderName);
            /*  813 */ email.setSubject("La Sierra University Estimate");
            /*      */
 /*      */
 /*  816 */ email.addCc(this.login.getCurrentUser().getEmail() + "@lasierra.edu", this.login.getCurrentUser().getUsername());
            /*  817 */ email.addReplyTo("info@lasierra.edu", "La Sierra University Enrollment Services Office");
            /*      */
 /*      */
 /*  820 */ log.info("==================toEmail()  will embed image from network ...........................");
            /*      */
 /*      */File img =null;
 /*  823 */ URL url = null;
            /*      */ try {
                         String img_=new PDFgen().getEstimaterLogoPath();
                            img = new File(img_);
                /*  825 */ //url = new URL("http://vhost1.lasierra.edu/live/logo_2c.gif");
                /*  826 */            } catch (Exception ex) {
                /*  827 */ log.info("...toEmail() meets MalformedURLException:", ex);
                /*  828 */ msg = ex.getMessage();
                /*  829 */ return msg;
                /*      */            }
            /*      */
 /*  832 */ String cid = email.embed(img, "La Sierra University logo");
            /*      */
 /*  834 */ log.info("==================toEmail()   will set message ...........................");
            /*  835 */ StringBuilder sbHtml = new StringBuilder(512);
            /*  836 */ StringBuilder sbText = new StringBuilder(512);
            /*  837 */ sbHtml.append("<html><head><title>La Sierra University Estimate</title></head><body>\n");
            /*  838 */ sbHtml.append("Dear <b>").append(this.stud.getStudentCFirstname()).append(' ').append(this.stud.getStudentBLastname()).append("</b>,<br/><br/>\n");
            /*  839 */ sbText.append("Dear ").append(this.stud.getStudentCFirstname()).append(' ').append(this.stud.getStudentBLastname()).append(",\n\n");
            /*  840 */ sbHtml.append("Thank you for choosing La Sierra University!<br/><br/>Your estimate is based on the data and information you provided.<br/><br/>\n");
            /*  841 */ sbText.append("Thank you for choosing La Sierra University!\n\nYour estimate is based on the data and information you provided.\n\n");
            /*  842 */ sbHtml.append("Attached is the estimate file in PDF format. <br/>You may need a <a href='http://get.adobe.com/reader/'>reader software</a> to view its content.\n");
            /*  843 */ sbText.append("Attached is the estimate file in PDF format. \nTo view its content, you may need a reader software, which you can download free from 'http://get.adobe.com/reader/'.\n");
            /*      */
 /*  845 */ // sbHtml.append("<br/><br/><br/>  <img src='cid:").append(cid).append("' alt='La Sierra University' /></body></html>");
            /*  846 */ sbText.append("\n\nLa Sierra University");
            /*      */
 /*      */
 /*  849 */ email.setHtmlMsg(sbHtml.toString());
            /*      */
 /*  851 */ email.setTextMsg(sbText.toString());
            /*      */
 /*      */
 /*  854 */ log.info("==================toEmail()   will attache files ...........................");
            /*      */
 /*  856 */ EmailAttachment attachment = new EmailAttachment();
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  866 */ attachment.setPath(pdffolder + "/" + pdfname);
            /*  867 */ attachment.setDisposition("attachment");
            /*  868 */ attachment.setDescription("La Sierra University Estimate in PDF format");
            /*  869 */ attachment.setName(pdfname);
            /*      */
 /*  871 */ email.attach(attachment);
            /*      */
 /*  873 */ log.info("==================toEmail()  will set smtp server info and auth info ...........................");
            /*      */
 /*  875 */ email.setHostName(smtpServer);
            /*  876 */ email.setSmtpPort(smtpPort);
            /*  877 */ email.setAuthenticator((Authenticator) new DefaultAuthenticator("estimator@lasierra.edu", "lasu3stimate"));
            /*  878 */ email.setSSL(true);
            /*  879 */ email.setTLS(true);
            /*      */
 /*      */
 /*  882 */ log.info("==================toEmail() tries to send email ...........................");
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
 /*  895 */ email.send();
            /*  896 */ log.info("==================toEmail() sent email ...........................");
            /*  897 */ msg = "sent estimate file '" + pdfname + "' to server " + smtpServer + " targetting " + emailbox + " (the email server may bounce or reject the email if recipient email box does not exist or is invalid/inactive or reaches capacity limit)";
            /*  898 */        } catch (Exception ex) {
            /*  899 */ log.info("...toEmail() meets EmailException:", ex);
            /*  900 */ msg = "Failed to send estimate to " + emailbox + " since: " + ex.getMessage();
            /*      */        }
        /*  902 */ return msg;
        /*      */    }

    /*      */
 /*      */
 /*      */ private String sendEmail(String pdfname, String emailbox, String receiverName) {
        /*  907 */ String smtpServer = "smtp.gmail.com";
        /*  908 */ int smtpPort = 587;
        /*  909 */ String senderBox = "enroll@lasierra.edu";
        /*  910 */ String senderName = "La Sierra University Enrollment Services Office";
        /*      */
 /*  912 */ String msg = "";
        /*  913 */ if (this.ref.isEmp(pdfname) || !pdfname.endsWith(".pdf")) {
            /*  914 */ msg = "invalid file name. Can not email estimate.";
            /*  915 */ return msg;
            /*      */        }
        /*      */
 /*  918 */ if (this.ref.isEmp(emailbox)) {
            /*  919 */ msg = "invalid E-mail address '" + emailbox + "'. Can not email estimate.";
            /*  920 */ return msg;
            /*      */        }
        /*      */
 /*      */
 /*      */ try {
            /*  925 */ HtmlEmail email = new HtmlEmail();
            /*      */
 /*      */
 /*  928 */ email.addTo(emailbox, receiverName);
            /*  929 */ email.setFrom(senderBox, senderName);
            /*  930 */ email.setSubject("La Sierra University Estimate");
            /*      */
 /*  932 */ log.info("==================toEmail()  will embed image from network ...........................");
            /*      */
 /*      */
 /*  935 */ URL url = null;
            /*      */ try {
                /*  937 */ url = new URL("http://vhost1.lasierra.edu/live/logo_2c.gif");
                /*  938 */            } catch (MalformedURLException ex) {
                /*  939 */ log.info("...toEmail() meets MalformedURLException:", ex);
                /*  940 */ msg = ex.getMessage();
                /*  941 */ return msg;
                /*      */            }
            /*      */
 /*  944 */// String cid = email.embed(url, "La Sierra University logo");
            /*      */
 /*  946 */ log.info("==================toEmail()   will set message ...........................");
            /*  947 */ StringBuilder sbHtml = new StringBuilder(512);
            /*  948 */ StringBuilder sbText = new StringBuilder(512);
            /*  949 */ sbHtml.append("<html><head><title>La Sierra University Estimate</title></head><body>\n");
            /*  950 */ sbHtml.append("Dear <b>").append(this.stud.getStudentCFirstname()).append(' ').append(this.stud.getStudentBLastname()).append("</b>,<br/><br/>\n");
            /*  951 */ sbText.append("Dear ").append(this.stud.getStudentCFirstname()).append(' ').append(this.stud.getStudentBLastname()).append(",\n\n");
            /*  952 */ sbHtml.append("Thank you for choosing La Sierra University!<br/><br/>Your estimate is based on the data and information you provided.<br/><br/>\n");
            /*  953 */ sbText.append("Thank you for choosing La Sierra University!\n\nYour estimate is based on the data and information you provided.\n\n");
            /*  954 */ sbHtml.append("Attached is the estimate file in PDF format. <br/>You may need a <a href='http://get.adobe.com/reader/'>reader software</a> to view its content.\n");
            /*  955 */ sbText.append("Attached is the estimate file in PDF format. \nTo view its content, you may need a reader software, which you can download free from 'http://get.adobe.com/reader/'.\n");
            /*      */
 /*  957 */// sbHtml.append("<br/><br/><br/>  <img src='cid:").append(cid).append("' alt='La Sierra University' /></body></html>");
            /*  958 */ sbText.append("\n\nLa Sierra University");
            /*      */
 /*      */
 /*  961 */ email.setHtmlMsg(sbHtml.toString());
            /*      */
 /*  963 */ email.setTextMsg(sbText.toString());
            /*      */
 /*      */
 /*  966 */ log.info("==================toEmail()   will attache files ...........................");
            /*      */
 /*  968 */ EmailAttachment attachment = new EmailAttachment();
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*  976 */ attachment.setPath("/var/estimator-files/" + pdfname);
            /*  977 */ attachment.setDisposition("attachment");
            /*  978 */ attachment.setDescription("La Sierra University Estimate in PDF format");
            /*  979 */ attachment.setName(pdfname);
            /*      */
 /*  981 */ email.attach(attachment);
            /*      */
 /*  983 */ log.info("==================toEmail()  will set smtp server info and auth info ...........................");
            /*      */
 /*  985 */ email.setHostName(smtpServer);
            /*  986 */ email.setSmtpPort(smtpPort);
            /*  987 */ email.setAuthenticator((Authenticator) new DefaultAuthenticator("kwang@lasierra.edu", "xxxyyyzzz"));
            /*  988 */ email.setTLS(true);
            /*      */
 /*  990 */ log.info("==================toEmail() tries to send email ...........................");

            /* 1003 */ email.send();

            /* 1091 */ log.info("==================toEmail() sent email ...........................");
            /* 1092 */ msg = "sent estimate file '" + pdfname + "' to server " + smtpServer + " targetting " + emailbox + " (the email server may bounce or reject the email if recipient email box does not exist or is invalid/inactive or reaches capacity limit)";
            /* 1093 */        } catch (Exception ex) {
            /* 1094 */ log.info("...toEmail() meets EmailException:", ex);
            /* 1095 */ msg = "Faield to send estimate to " + emailbox + " since: " + ex.getMessage();
            /*      */        }
        /* 1097 */ return msg;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String makeuploadprints() {
        /* 1108 */ List<String> recs = this.em.createNativeQuery("select a.recid from student a where a.student_fisy=? and  a.dup>0 and not exists ( select '1' from prints b where b.recid = a.recid)").setParameter(1, Integer.valueOf(this.ref.getFiscal_year())).getResultList();
        /* 1109 */ if (recs == null || recs.size() == 0) {
            return null;
        }
        /*      */
 /* 1111 */ int prtot = 0, prtgenok = 0, prtgenfail = 0, prtsaveok = 0, prtsavefail = 0;
        /* 1112 */ PDFgen pdf = new PDFgen();
        /* 1113 */ for (String onerecid : recs) {
            /* 1114 */ prtot++;
            /* 1115 */ Student onestd = (Student) this.em.find(Student.class, onerecid);
            /* 1116 */ this.calc.setStud(onestd);
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1123 */ Print prt = new Print();
            /* 1124 */ prt.setSship1ExtAmt(Integer.valueOf(0));
            /* 1125 */ prt.setSship2ExtAmt(Integer.valueOf(0));
            /* 1126 */ prt.setSship7ExtAmt(Integer.valueOf(0));
            /* 1127 */ prt.setSship8ExtAmt(Integer.valueOf(0));
            /* 1128 */ prt.setSship9ExtAmt(Integer.valueOf(0));
            /*      */
 /* 1130 */ log.info("vvvvvvvvvvvvvvvvvvvvvvvvvvv  to be generated print has recid=%s ( find by %s)", new Object[]{onestd.getRecid(), onerecid});
            /* 1131 */ String msg = pdf.genPDF(onestd, this.login, this.ref, this.calc, prt, 3, new Integer[0]);
            /* 1132 */ if (!msg.startsWith("Failed")) {
                /* 1133 */ log.info("==================makeuploadprints() gen pdf: pass");
                /* 1134 */ prtgenok++;
                /*      */
 /* 1136 */ prt.setCounselorId(onestd.getCounselorId());
                /* 1137 */ log.info("vvvvvvvvvvvvvvvvvvvvvvvvvvv  to be saved print has recid=%s ", new Object[]{prt.getRecid()});
                /* 1138 */ msg = savePrint(prt, 3, new Integer[0]);
                /* 1139 */ if (msg == null || msg.isEmpty()) {
                    /* 1140 */ log.info("==================makeuploadprints() save print rec: pass");
                    /* 1141 */ prtsaveok++;
                    continue;
                    /*      */                }
                /* 1143 */ log.info("==================makeuploadprints() save print for %s: failed. msg=%s", new Object[]{onerecid, msg});
                /* 1144 */ prtsavefail++;
                /*      */ continue;
                /*      */            }
            /* 1147 */ log.info("==================makeuploadprints() gen print for %s: failed. msg=%s", new Object[]{onerecid, msg});
            /* 1148 */ prtgenfail++;
            /*      */        }
        /*      */
 /* 1151 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_INFO, "Targets:" + prtot + "   GenPrints: ok " + prtgenok + "/fail " + prtgenfail + "    SavePrints: ok " + prtsaveok + "/fail " + prtsavefail);
        /* 1152 */ this.facesContext.addMessage(null, guimsg);
        /*      */
 /* 1154 */ this.calc.setStud(this.stud);
        /* 1155 */ this.calc.refreshCalc(this.stud);
        /* 1156 */ return null;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String makeprints() {
        /* 1164 */ List<String> recs = this.em.createNativeQuery("select a.recid from student a where a.student_fisy=? and a.lost_time=0 and a.pickup_ind=1 and not exists ( select '1' from prints b where b.recid = a.recid)").setParameter(1, Integer.valueOf(this.ref.getFiscal_year())).getResultList();
        /* 1165 */ if (recs == null || recs.size() == 0) {
            return null;
        }
        /*      */
 /* 1167 */ int prtot = 0, prtgenok = 0, prtgenfail = 0, prtsaveok = 0, prtsavefail = 0;
        /* 1168 */ PDFgen pdf = new PDFgen();
        /* 1169 */ for (String onerecid : recs) {
            /* 1170 */ prtot++;
            /* 1171 */ Student onestd = (Student) this.em.find(Student.class, onerecid);
            /* 1172 */ this.calc.setStud(onestd);
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1179 */ Print prt = new Print();
            /* 1180 */ prt.setSship1ExtAmt(Integer.valueOf(0));
            /* 1181 */ prt.setSship2ExtAmt(Integer.valueOf(0));
            /* 1182 */ prt.setSship7ExtAmt(Integer.valueOf(0));
            /* 1183 */ prt.setSship8ExtAmt(Integer.valueOf(0));
            /* 1184 */ prt.setSship9ExtAmt(Integer.valueOf(0));
            /*      */
 /* 1186 */ log.info("vvvvvvvvvvvvvvvvvvvvvvvvvvv  to be generated print has recid=%s ( find by %s)", new Object[]{onestd.getRecid(), onerecid});
            /* 1187 */ String msg = pdf.genPDF(onestd, this.login, this.ref, this.calc, prt, 3, new Integer[0]);
            /* 1188 */ if (!msg.startsWith("Failed")) {
                /* 1189 */ log.info("==================makeprints() gen pdf: pass");
                /* 1190 */ prtgenok++;
                /*      */
 /* 1192 */ prt.setCounselorId(onestd.getCounselorId());
                /* 1193 */ log.info("vvvvvvvvvvvvvvvvvvvvvvvvvvv  to be saved print has recid=%s ", new Object[]{prt.getRecid()});
                /* 1194 */ msg = savePrint(prt, 3, new Integer[0]);
                /* 1195 */ if (msg == null || msg.isEmpty()) {
                    /* 1196 */ log.info("==================makeprints() save print rec: pass");
                    /* 1197 */ prtsaveok++;
                    continue;
                    /*      */                }
                /* 1199 */ log.info("==================makeprints() save print for %s: failed. msg=%s", new Object[]{onerecid, msg});
                /* 1200 */ prtsavefail++;
                /*      */ continue;
                /*      */            }
            /* 1203 */ log.info("==================makeprints() gen print for %s: failed. msg=%s", new Object[]{onerecid, msg});
            /* 1204 */ prtgenfail++;
            /*      */        }
        /*      */
 /* 1207 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_INFO, "Targets:" + prtot + "   GenPrints: ok " + prtgenok + "/fail " + prtgenfail + "    SavePrints: ok " + prtsaveok + "/fail " + prtsavefail);
        /* 1208 */ this.facesContext.addMessage(null, guimsg);
        /*      */
 /* 1210 */ this.calc.setStud(this.stud);
        /* 1211 */ this.calc.refreshCalc(this.stud);
        /* 1212 */ return null;
        /*      */    }

    /*      */
 /*      */ public String toPDF() {
        /* 1216 */ log.info("==================toPDF() pdf button clicked ...........................");
        /*      */
 /* 1218 */ String ln = this.stud.getStudentBLastname();
        /* 1219 */ String fn = this.stud.getStudentCFirstname();
        /*      */ String recid = this.stud.getRecid();
        /* 1221 */ String dob = this.stud.getStudentDDob();
        /* 1222 */ String phone = this.stud.getStudentFPhone();
        /* 1223 */ String msg = chkStudBasicInfo(new Integer[0]);
        /* 1224 */ log.info("==================toPDF() got chkStudBasicInfo() res msg=%s", new Object[]{msg});
        /* 1225 */ if (!msg.isEmpty()) {
            return null;
        }
        /*      */
 /* 1227 */ int vio = validateStudBean();
        /* 1228 */ log.info("==================toPDF() got validateStudBean() res code=%d", new Object[]{Integer.valueOf(vio)});
        /* 1229 */ if (vio > 0) {
            /* 1230 */ return null;
            /*      */        }
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
 /* 1242 */ this.stud.setStudentUserName(compUserName(this.stud.getStudentALsuid(), ln, fn, dob));
        /* 1243 */   // 
        msg = saveStud(1, new int[0]);
        if (msg.contains("Transaction aborted")) {
            msg = "Error: Student data exists already.";
        }

        /*      */
 /* 1245 */ log.info("==================toPDF() stud saving: %s ........", new Object[]{msg});
        /* 1246 */ if ((msg == null || msg.isEmpty())) {
            /* 1247 */ FacesMessage guimsg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_INFO, "EstimateForm.DataSaved");
            /* 1248 */ if (this.facesContext != null) {
                this.facesContext.addMessage(null, guimsg);
            }
            /* 1249 */ this.stud.setStudentNumb((Integer) null);
            /*      */
 /*      */
 /* 1252 */ PDFgen pdf = new PDFgen();
            /* 1253 */ Print prt = new Print();
            /* 1254 */ prt.setSship1ExtAmt(Integer.valueOf(0));
            /* 1255 */ prt.setSship2ExtAmt(Integer.valueOf(0));
            /* 1256 */ prt.setSship7ExtAmt(Integer.valueOf(0));
            /* 1257 */ prt.setSship8ExtAmt(Integer.valueOf(0));
            /* 1258 */ prt.setSship9ExtAmt(Integer.valueOf(0));
            /*      */
 /*      */
 /* 1261 */ msg = pdf.genPDF(this.stud, this.login, this.ref, this.calc, prt, 1, new Integer[0]);
            /* 1262 */ if (!msg.startsWith("Failed")) {
                /* 1263 */ log.info("==================toPDF() gen pdf: pass");
                /* 1264 */ String pdfname = msg;
                /*      */

                //dont have tmpid here 
                /* 1266 */ msg = savePrint(prt, 1, new Integer[0]);
                /*      *///
                /* 1268 */ if (msg == null || msg.isEmpty()) {
                    /* 1269 */ log.info("==================toPDF() save print rec: pass");
                    /*      */
 /* 1271 */ msg = pdf.downloadPDF(pdfname);
                    /*      */
 /* 1273 */ if (msg != null && !msg.isEmpty()) {
                        /* 1274 */ log.info("==================toPDF() download pdf: %s ........", new Object[]{msg});
                        /*      */                    }

                    /*      */
                    //
                    /* 1278 */// msg = this.accessor.updateStudPrtTimes(this.stud);
                    /* 1279 */ if (msg != null && !msg.isEmpty()) {
                        /* 1280 */ log.info("==================toPDF() failed to update stud obj %s with msg: %s", new Object[]{this.stud.getRecid(), msg});
                        /*      */                    }
                    /*      */                } else {
                    /* 1283 */ FacesMessage pdfmsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    /* 1284 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, pdfmsg);
                    }
                    /* 1285 */ log.info("==================toPDF() failed to save print rec: %s ........", new Object[]{msg});
                    /*      */                }
                /*      */            } else {
                /* 1288 */ log.info("==================toPDF() gen pdf failed: %s ........", new Object[]{msg});
                /* 1289 */ FacesMessage pdfmsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                /* 1290 */ if (this.facesContext != null) {
                    this.facesContext.addMessage(null, pdfmsg);
                }
                /*      */
 /*      */            }
            /* 1293 */        } else if (!msg.equals("processed")) {
            /* 1294 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
            /* 1295 */ if (this.facesContext != null) {
                this.facesContext.addMessage(null, guimsg);
            }
            /*      */        }
//        FacesMessage guimsg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_INFO, "EstimatorForm.DatSavedPrinted");
//        /* 1248 */ if (this.facesContext != null) {
//            this.facesContext.addMessage(null, guimsg);
//            System.out.println("Successfully saved and pdf generated ");
//        }
        /* 1297 */ return null;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ private String savePrint(Print pdf, int mode, Integer... p) {
        /* 1303 */ String msg = "";
        /*      */
 /* 1305 */ boolean pu = (p.length > 0);
        /*      */
 /* 1307 */ pdf.setPrtNum(null);
        /*      *///
        /* 1309 */ pdf.setFisy(Integer.valueOf(this.stud.getStudentFisy()));
        /*      */
 /*      */
 /* 1312 */ pdf.setFisyPrt(this.ref.getFaid_year());
        /* 1313 */ pdf.setClientId(this.ref.getClientid());
        /*      */
 /*      */
 /* 1316 */ if (mode > 0 && mode < 3) {
            /* 1317 */ pdf.setPrtTime(System.currentTimeMillis());
            /*      */
 /*      */
 /* 1320 */ pdf.setCounselorId(this.login.getCurrentUser().getUserid());
            /* 1321 */ pdf.setCounselorName(this.login.getCurrentUser().getUsername());
            /*      */
 /* 1323 */ pdf.setRecid(this.stud.getRecid());
            /*      */        } else {
            /* 1325 */ pdf.setPrtTime(0L);
            /*      */
 /* 1327 */ if (pu) {
                /* 1328 */ pdf.setCounselorId(Integer.valueOf(this.ref.getSys_counselor_id()));
                /*      */            } else {
                /* 1330 */ pdf.setCounselorId(this.login.getCurrentUser().getUserid());
                /*      */            }
            /* 1332 */        }
        pdf.setPrtTz(this.ref.getTzSN());
        /*      */
 /*      */
        //it should not be tmpid 

        /* 1335 */ //pdf.setPrtId("tmpid");
        /* 1336 */ log.info("vvvvvvvvvvvvvvvvvvvvvvvvvvv  to be saved print has stud recid=%s, counselorid=%d, mode=%d", new Object[]{pdf.getRecid(), pdf.getCounselorId(), Integer.valueOf(mode)});
        /*      */
 /*      */
 /* 1339 */
        Print pdf_ = null;
        int seq = this.info.getNowPrtNumbInQueue().intValue();
        /* 1343 */

        String real_prt_id_ = this.stud.getRecid();// pdf.getCounselorId() + "." + pdf.getClientId() + "." + seq;
        int prntNumb = Integer.valueOf(real_prt_id_.split("\\.")[2]) + 1;
        pdf.setPrtNum(Integer.valueOf(prntNumb));
        pdf_ = (Print) this.em.find(Print.class, this.stud.getRecid());

        if (pdf_ == null) {

            /* 1344 */ pdf.setPrtId(real_prt_id_);
            msg = this.accessor.savePrt(pdf);
            /*      */
        } else {

            /* 1344 */ pdf.setPrtId(real_prt_id_);
            msg = this.accessor.updatePrint(pdf, pdf_);
        }
        /* 1341 */ if (msg == null || msg.isEmpty()) {
            /* 1342 */// int seq = this.info.getNowPrtNumbInQueue().intValue();
            /* 1343 */ //pdf.setPrtNum(Integer.valueOf(seq));
            /* 1344 */ //pdf.setPrtId(this.info.getPrintPrtid(pdf.getCounselorId(), Integer.valueOf(pdf.getClientId()), pdf.getPrtNum()));
            /*      */        }
        /*      */
 /*      */
 /* 1348 */ List<Print> pdfs = this.stud.getPdfs();
        /* 1349 */ if (pdfs == null) {
            pdfs = new ArrayList<>();
        }
        /* 1350 */ pdfs.add(pdf);
        /* 1351 */ this.stud.setPdfs(pdfs);
        /* 1352 */ return msg;
        /*      */    }

    /*      */
 /*      */
 /*      */ private String checkScholarships(Integer... p) {
        /* 1357 */ boolean pu = (p.length > 0);
        /* 1358 */ boolean np = (p.length == 0);
        /*      */
 /* 1360 */ String msg = "";
        /* 1361 */ int miss = 0, err = 0;
        /* 1362 */ if (this.stud.getStudentAuScholarship1Amt() != null && this.stud.getStudentAuScholarship1Amt().intValue() > 0) {
            /* 1363 */ if (this.ref.fundNotesReqById(this.stud.getFund1id().intValue()) > 0 && ( /* 1364 */this.stud.getStudentAtScholarship1Note() == null || this.stud.getStudentAtScholarship1Note().trim().isEmpty())) {
                /* 1365 */ msg = "Notes for Scholarship '" + this.stud.getStudentAsScholarship1Name() + "' is Required.";
                /*      */
 /* 1367 */ if (np) {
                    /* 1368 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    /* 1369 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, guimsg);
                    }
                    /*      */                }
                /* 1371 */ miss++;
                /*      */            }
            /*      */
 /* 1374 */ int max = this.ref.fundMatchTopById(this.stud.getFund1id().intValue());
            /* 1375 */ int in = this.stud.getStudentAuScholarship1Amt().intValue();
            /* 1376 */ if (np && user_over_maxamt(this.login.getCurrentUser().getSuperuser().intValue(), in, this.sfund1amt, max) < 1) {
                /*      */
 /*      */
 /*      */
 /* 1380 */ UIComponent c = getUIComponentById("ship1amt");
                /*      */
 /* 1382 */ UIInput textInput = (UIInput) c;
                /* 1383 */ textInput.setValid(false);
                /*      */
 /* 1385 */ FacesMessage guimsg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship1.amtoverflow");
                /* 1386 */ if (this.facesContext != null) {
                    this.facesContext.addMessage(null, guimsg);
                }
                /* 1387 */ msg = guimsg.getSummary();
                /* 1388 */ err++;
                /*      */            }
            /* 1390 */ if (pu && in > max) {
                /* 1391 */ msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship1.amtoverflow").getSummary();
                /* 1392 */ err++;
                /*      */            }
            /*      */        } else {
            /*      */
 /* 1396 */ this.stud.setStudentAtScholarship1Note("");
            /*      */        }
        /*      */
 /* 1399 */ if (this.stud.getStudentAxScholarship2Amt() != null && this.stud.getStudentAxScholarship2Amt().intValue() > 0) {
            /* 1400 */ if (this.ref.fundNotesReqById(this.stud.getFund2id().intValue()) > 0 && ( /* 1401 */this.stud.getStudentAwScholarship2Note() == null || this.stud.getStudentAwScholarship2Note().trim().isEmpty())) {
                /* 1402 */ msg = "Notes for Scholarship '" + this.stud.getStudentAvScholarship2Name() + "' is Required.";
                /* 1403 */ if (np) {
                    /* 1404 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    /* 1405 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, guimsg);
                    }
                    /*      */                }
                /* 1407 */ miss++;
                /*      */            }
            /*      */
 /* 1410 */ int max = this.ref.fundMatchTopById(this.stud.getFund2id().intValue());
            /* 1411 */ int in = this.stud.getStudentAxScholarship2Amt().intValue();
            /* 1412 */ if (np && user_over_maxamt(this.login.getCurrentUser().getSuperuser().intValue(), in, this.sfund2amt, max) < 1) /*      */ {
                /*      */
 /* 1415 */ if (this.login.getCurrentUser().getSuperuser().intValue() != 0 || in > this.ref.getUniversity_grant_hard_top_limit().intValue()) {
                    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 1421 */ UIComponent c = getUIComponentById("ship2amt");
                    /*      */
 /* 1423 */ UIInput textInput = (UIInput) c;
                    /* 1424 */ textInput.setValid(false);
                    /*      */
 /* 1426 */ FacesMessage guimsg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship2.amtoverflow");
                    /* 1427 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, guimsg);
                    }
                    /* 1428 */ msg = guimsg.getSummary();
                    /* 1429 */ err++;
                    /*      */                }
                /*      */            }
            /* 1432 */ if (pu && in > max) {
                /* 1433 */ msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship2.amtoverflow").getSummary();
                /* 1434 */ err++;
                /*      */            }
            /*      */        } else {
            /* 1437 */ this.stud.setStudentAwScholarship2Note("");
            /*      */        }
        /*      */
 /* 1440 */ if (this.stud.getStudentBaScholarship3Amt() != null && this.stud.getStudentBaScholarship3Amt().intValue() > 0) {
            /* 1441 */ if (this.ref.fundNotesReqById(this.stud.getFund3id().intValue()) > 0 && ( /* 1442 */this.stud.getStudentAzScholarship3Note() == null || this.stud.getStudentAzScholarship3Note().trim().isEmpty())) {
                /* 1443 */ msg = "Notes for Scholarship '" + this.stud.getStudentAyScholarship3Name() + "' is Required.";
                /* 1444 */ if (np) {
                    /* 1445 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    /* 1446 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, guimsg);
                    }
                    /*      */                }
                /* 1448 */ miss++;
                /*      */            }
            /*      */        } else {
            /*      */
 /* 1452 */ this.stud.setStudentAzScholarship3Note("");
            /*      */        }
        /*      */
 /* 1455 */ if (this.stud.getStudentBdScholarship4Amt() != null && this.stud.getStudentBdScholarship4Amt().intValue() > 0) {
            /* 1456 */ if (this.ref.fundNotesReqById(this.stud.getFund4id().intValue()) > 0 && ( /* 1457 */this.stud.getStudentBcScholarship4Note() == null || this.stud.getStudentBcScholarship4Note().trim().isEmpty())) {
                /* 1458 */ msg = "Notes for Scholarship '" + this.stud.getStudentBbScholarship4Name() + "' is Required.";
                /* 1459 */ if (np) {
                    /* 1460 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    /* 1461 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, guimsg);
                    }
                    /*      */                }
                /* 1463 */ miss++;
                /*      */            }
            /*      */        } else {
            /*      */
 /* 1467 */ this.stud.setStudentBcScholarship4Note("");
            /*      */        }
        /*      */
 /* 1470 */ if (this.stud.getStudentBgScholarship5Amt() != null && this.stud.getStudentBgScholarship5Amt().intValue() > 0) {
            /* 1471 */ if (this.ref.fundNotesReqById(this.stud.getFund5id().intValue()) > 0 && ( /* 1472 */this.stud.getStudentBfScholarship5Note() == null || this.stud.getStudentBfScholarship5Note().trim().isEmpty())) {
                /* 1473 */ msg = "Notes for Scholarship '" + this.stud.getStudentBeScholarship5Name() + "' is Required.";
                /* 1474 */ if (np) {
                    /* 1475 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    /* 1476 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, guimsg);
                    }
                    /*      */                }
                /* 1478 */ miss++;
                /*      */            }
            /*      */        } else {
            /*      */
 /* 1482 */ this.stud.setStudentBfScholarship5Note("");
            /*      */        }
        /*      */
 /* 1485 */ if (this.stud.getStudentBjScholarship6Amt() != null && this.stud.getStudentBjScholarship6Amt().intValue() > 0) {
            /* 1486 */ if (this.ref.fundNotesReqById(this.stud.getFund6id().intValue()) > 0 && ( /* 1487 */this.stud.getStudentBiScholarship6Note() == null || this.stud.getStudentBiScholarship6Note().trim().isEmpty())) {
                /* 1488 */ msg = "Notes for Scholarship '" + this.stud.getStudentBhScholarship6Name() + "' is Required.";
                /* 1489 */ if (np) {
                    /* 1490 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    /* 1491 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, guimsg);
                    }
                    /*      */                }
                /* 1493 */ miss++;
                /*      */            }
            /*      */        } else {
            /*      */
 /* 1497 */ this.stud.setStudentBiScholarship6Note("");
            /*      */        }
        /*      */
 /*      */
 /*      */
 /* 1502 */ int fundid = 0;
        /* 1503 */ if (this.stud.getStudentBmScholarship7Amt() != null && this.stud.getStudentBmScholarship7Amt().intValue() > 0) {
            /* 1504 */ fundid = (this.stud.getFund7id() == null) ? 0 : this.stud.getFund7id().intValue();
            /* 1505 */ if (fundid != this.stdo_fundid7.intValue()) {
                /* 1506 */ this.stud.setFund7id(this.stdo_fundid7);
                /* 1507 */ fundid = this.stdo_fundid7.intValue();
                /*      */            }
            /* 1509 */ if (fundid == 0) {
                /*      */
 /*      */
 /*      */
 /* 1513 */ msg = "Scholarship #7 name is not specified.";
                /* 1514 */ if (np) {
                    /* 1515 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    /* 1516 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, guimsg);
                    }
                    /*      */
 /*      */                }
                /*      */            } else {
                /* 1520 */ if (this.ref.fundNotesReqById(fundid) > 0) /*      */ {
                    /* 1522 */ if (this.ref.isEmp(this.stud.getStudentBlScholarship7Note())) {
                        /* 1523 */ msg = "Notes for Scholarship #7 '" + this.stud.getStudentBkScholarship7Name() + "' is Required.";
                        /* 1524 */ if (np) {
                            /* 1525 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                            /* 1526 */ if (this.facesContext != null) {
                                this.facesContext.addMessage(null, guimsg);
                            }
                            /*      */
 /* 1528 */ UIComponent c = getUIComponentById("ship7note");
                            /* 1529 */ UIInput textInput = (UIInput) c;
                            /* 1530 */ textInput.setValid(false);
                            /*      */                        }
                        /* 1532 */ miss++;
                        /*      */                    }
                    /*      */                }
                /* 1535 */ int max = this.ref.fundMatchTopById(this.stud.getFund7id().intValue());
                /* 1536 */ int in = this.stud.getStudentBmScholarship7Amt().intValue();
                /*      */
 /*      */
 /* 1539 */ if (np && user_over_maxamt(this.login.getCurrentUser().getSuperuser().intValue(), in, this.sfund7amt, max) < 1) {
                    /*      */
 /*      */
 /*      */
 /* 1543 */ UIComponent c = getUIComponentById("ship7amt");
                    /*      */
 /* 1545 */ UIInput textInput = (UIInput) c;
                    /* 1546 */ textInput.setValid(false);
                    /*      */
 /* 1548 */ FacesMessage guimsg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship7.amtoverflow");
                    /* 1549 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, guimsg);
                    }
                    /* 1550 */ msg = guimsg.getSummary();
                    /* 1551 */ err++;
                    /*      */                }
                /* 1553 */ if (pu && in > max) {
                    /* 1554 */ msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship7.amtoverflow").getSummary();
                    /* 1555 */ err++;
                    /*      */                }
                /*      */            }
            /*      */        } else {
            /*      */
 /* 1560 */ this.stud.setFund7id(Integer.valueOf(0));
            /* 1561 */ this.stud.setStudentBkScholarship7Name("");
            /* 1562 */ this.stud.setStudentBlScholarship7Note("");
            /*      */        }
        /*      */
 /*      */
 /*      */
 /* 1567 */ if (this.stud.getStudentBpScholarship8Amt() != null && this.stud.getStudentBpScholarship8Amt().intValue() > 0) {
            /* 1568 */ fundid = (this.stud.getFund8id() == null) ? 0 : this.stud.getFund8id().intValue();
            /* 1569 */ if (fundid != this.stdo_fundid8.intValue()) {
                /* 1570 */ this.stud.setFund8id(this.stdo_fundid8);
                /* 1571 */ fundid = this.stdo_fundid8.intValue();
                /*      */            }
            /* 1573 */ if (fundid == 0) {
                /*      */
 /*      */
 /*      */
 /* 1577 */ msg = "Scholarship #8 name is not specified.";
                /* 1578 */ if (np) {
                    /* 1579 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    /* 1580 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, guimsg);
                    }
                    /*      */                }
                /*      */            } else {
                /* 1583 */ log.info(" scholarship8 needs notes? %s", new Object[]{Integer.valueOf(this.ref.fundNotesReqById(fundid))});
                /* 1584 */ if (this.ref.fundNotesReqById(this.stud.getFund8id().intValue()) > 0
                        && /* 1585 */ this.ref.isEmp(this.stud.getStudentBoScholarship8Note())) {
                    /* 1586 */ msg = "Notes for Scholarship #8 '" + this.stud.getStudentBnScholarship8Name() + "' is Required.";
                    /* 1587 */ if (np) {
                        /* 1588 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                        /* 1589 */ if (this.facesContext != null) {
                            this.facesContext.addMessage(null, guimsg);
                        }
                        /* 1590 */ UIComponent c = getUIComponentById("ship8note");
                        /* 1591 */ UIInput textInput = (UIInput) c;
                        /* 1592 */ textInput.setValid(false);
                        /*      */                    }
                    /* 1594 */ miss++;
                    /*      */                }
                /*      */
 /* 1597 */ int max = this.ref.fundMatchTopById(this.stud.getFund8id().intValue());
                /* 1598 */ int in = this.stud.getStudentBpScholarship8Amt().intValue();
                /* 1599 */ if (np && user_over_maxamt(this.login.getCurrentUser().getSuperuser().intValue(), in, this.sfund8amt, max) < 1) {
                    /*      */
 /*      */
 /*      */
 /* 1603 */ UIComponent c = getUIComponentById("ship8amt");
                    /*      */
 /* 1605 */ UIInput textInput = (UIInput) c;
                    /* 1606 */ textInput.setValid(false);
                    /*      */
 /* 1608 */ FacesMessage guimsg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship8.amtoverflow");
                    /* 1609 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, guimsg);
                    }
                    /* 1610 */ msg = guimsg.getSummary();
                    /* 1611 */ err++;
                    /*      */                }
                /* 1613 */ if (pu && in > max) {
                    /* 1614 */ msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship8.amtoverflow").getSummary();
                    /* 1615 */ err++;
                    /*      */                }
                /*      */            }
            /*      */        } else {
            /* 1619 */ this.stud.setFund8id(Integer.valueOf(0));
            /* 1620 */ this.stud.setStudentBnScholarship8Name("");
            /* 1621 */ this.stud.setStudentBoScholarship8Note("");
            /*      */        }
        /*      */
 /*      */
 /* 1625 */ if (this.stud.getStudentBsScholarship9Amt() != null && this.stud.getStudentBsScholarship9Amt().intValue() > 0) {
            /* 1626 */ fundid = (this.stud.getFund9id() == null) ? 0 : this.stud.getFund9id().intValue();
            /* 1627 */ if (fundid != this.stdo_fundid9.intValue()) {
                /* 1628 */ this.stud.setFund9id(this.stdo_fundid9);
                /* 1629 */ fundid = this.stdo_fundid9.intValue();
                /*      */            }
            /*      */
 /* 1632 */ if (fundid == 0) {
                /*      */
 /*      */
 /*      */
 /* 1636 */ msg = "Scholarship #9 name is not specified.";
                /* 1637 */ if (np) {
                    /* 1638 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    /* 1639 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, guimsg);
                    }
                    /*      */                }
                /*      */            } else {
                /* 1642 */ log.info(" scholarship9 needs notes? %s", new Object[]{Integer.valueOf(this.ref.fundNotesReqById(fundid))});
                /* 1643 */ if (this.ref.fundNotesReqById(this.stud.getFund9id().intValue()) > 0
                        && /* 1644 */ this.ref.isEmp(this.stud.getStudentBrScholarship9Note())) {
                    /* 1645 */ msg = "Notes for Scholarship #9 '" + this.stud.getStudentBqScholarship9Name() + "' is Required.";
                    /* 1646 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    /* 1647 */ if (np) {
                        /* 1648 */ if (this.facesContext != null) {
                            this.facesContext.addMessage(null, guimsg);
                        }
                        /* 1649 */ UIComponent c = getUIComponentById("ship9note");
                        /* 1650 */ UIInput textInput = (UIInput) c;
                        /* 1651 */ textInput.setValid(false);
                        /*      */                    }
                    /* 1653 */ miss++;
                    /*      */                }
                /*      */
 /* 1656 */ int max = this.ref.fundMatchTopById(this.stud.getFund9id().intValue());
                /* 1657 */ int in = this.stud.getStudentBsScholarship9Amt().intValue();
                int value_max = (this.login.getCurrentUser() != null && this.login.getCurrentUser().getSuperuser() != null) ? this.login.getCurrentUser().getSuperuser().intValue() : 0;
                /* 1658 */ if (np && user_over_maxamt(value_max, in, this.sfund9amt, max) < 1) {
                    /*      */
 /*      */
 /*      */
 /* 1662 */ UIComponent c = getUIComponentById("ship9amt");
                    /*      */
 /* 1664 */ UIInput textInput = (UIInput) c;
                    /* 1665 */ textInput.setValid(false);
                    /*      */
 /* 1667 */ FacesMessage guimsg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship9.amtoverflow");
                    /* 1668 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, guimsg);
                    }
                    /* 1669 */ msg = guimsg.getSummary();
                    /* 1670 */ err++;
                    /*      */                }
                /* 1672 */ if (pu && in > max) {
                    /* 1673 */ msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship9.amtoverflow").getSummary();
                    /* 1674 */ err++;
                    /*      */                }
                /*      */            }
            /*      */        } else {
            /* 1678 */ this.stud.setFund9id(Integer.valueOf(0));
            /* 1679 */ this.stud.setStudentBqScholarship9Name("");
            /* 1680 */ this.stud.setStudentBrScholarship9Note("");
            /*      */        }
        /*      */
 /*      */
 /* 1684 */ int diffs = 0, awdid = 0;
        /* 1685 */ HashMap<Integer, Integer> map = new HashMap<>(3);
        /* 1686 */ if ((awdid = this.stud.getFund7id().intValue()) > 0) {
            /* 1687 */ map.put(Integer.valueOf(awdid), Integer.valueOf(awdid));
            /* 1688 */ diffs++;
            /*      */        }
        /* 1690 */ if ((awdid = this.stud.getFund8id().intValue()) > 0) {
            /* 1691 */ map.put(Integer.valueOf(awdid), Integer.valueOf(awdid));
            /* 1692 */ diffs++;
            /*      */        }
        /* 1694 */ if ((awdid = this.stud.getFund9id().intValue()) > 0) {
            /* 1695 */ map.put(Integer.valueOf(awdid), Integer.valueOf(awdid));
            /* 1696 */ diffs++;
            /*      */        }
        /* 1698 */ if (np && diffs > map.size()) {
            /* 1699 */ FacesMessage guimsg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarships.dup");
            /* 1700 */ if (this.facesContext != null) {
                this.facesContext.addMessage(null, guimsg);
            }
            /* 1701 */ msg = guimsg.getSummary();
            /* 1702 */ log.info(" sssssssssssss dup scholarships. valids=%d, map size=%d, key set size=%d", new Object[]{Integer.valueOf(diffs), Integer.valueOf(map.size()), Integer.valueOf(map.keySet().size())});
            /* 1703 */ for (Iterator<Integer> iterator = map.keySet().iterator(); iterator.hasNext();) {
                int k = ((Integer) iterator.next()).intValue();
                /* 1704 */ log.info(" sssssssssss key=[%d]", new Object[]{Integer.valueOf(k)});
            }
            /*      */
 /*      */        }
        /* 1707 */ if (pu && diffs > map.size()) {
            /* 1708 */ msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarships.dup").getSummary();
            /*      */        }
        /*      */
 /* 1711 */ if (np && miss + err > 0 && this.ref.isEmp(msg)) {
            /* 1712 */ msg = "Some scholarship needs notes or is over limit";
            /*      */        }
        /*      */
 /* 1715 */ return msg;
        /*      */    }

    /*      */
 /*      */
 /*      */ private String saveStud(int mode, int... p) {
        /* 1720 */ boolean pu = (p.length > 0);
        /*      */
 /* 1722 */ String msg = "";

        /* 1734 */ if (!pu) {
            /* 1735 */ this.stud.setPuser_id(null);
            /*      */
 /* 1737 */ this.std_fafsa = this.stdo_fafsa;
            /* 1738 */ this.stud.setStudentXFafsa(this.std_fafsa ? "Yes" : "No");
            /*      */
 /* 1740 */ this.std_calgrant = this.stdo_calgrant;
            /* 1741 */ this.stud.setStudentZCalgrant(this.std_calgrant ? "Yes" : "No");
            /*      */
 /* 1743 */ this.std_ealsu = this.stdo_ealsu;
            /* 1744 */ this.stud.setIndEalsu(this.std_ealsu ? "yes" : "no");
            /*      */
 /* 1746 */ this.std_eanonlsu = this.stdo_eanonlsu;
            /* 1747 */ this.stud.setIndEanonlsu(this.std_eanonlsu ? "yes" : "no");
            /*      */
 /* 1749 */ this.std_efc = this.stdo_efc;
            /* 1750 */ this.stud.setIndEfc(this.std_efc ? "Yes" : "No");
            /*      */
 /* 1752 */ this.std_noloans = this.stdo_noloans;
            /* 1753 */ this.stud.setIndExcloans(this.std_noloans ? "yes" : "no");
            /*      */
 /* 1755 */ this.std_dorm = this.stdo_dorm;
            /* 1756 */ this.stud.setStudentWDorm(this.std_dorm ? "yes" : "no");
            /*      */
 /*      */
 /* 1759 */ this.in_subloan = this.stdo_in_subloan;
            /* 1760 */ this.stud.setStudentApSubLoans(this.in_subloan ? "Yes" : "No");
            /*      */
 /* 1762 */ this.in_unsubloan = this.stdo_in_unsubloan;
            /* 1763 */ this.stud.setStudentAqUnsubLoans(this.in_unsubloan ? "Yes" : "No");
            /*      */
 /* 1765 */ this.in_fws = this.stdo_in_fws;
            /* 1766 */ this.stud.setStudentArFws(this.in_fws ? "Yes" : "No");
            /*      */
 /* 1768 */ this.std_intl = this.stdo_intl;
            /* 1769 */ this.stud.setStudentLIntlStud(this.std_intl ? "yes" : "no");
            /*      */
            this.std_tf = this.stdo_tf;
            this.stud.setStd_transfer_ind(this.std_tf ? 1 : 0);
            
           this.std_ftfr=this.stdo_ftfr;
           this.stud.setStd_1st_freshmen(this.std_ftfr ?1:0);
           
            /* 1771 */ this.return_std_ind = this.stdo_return_ind;
            /* 1772 */ this.stud.setReturnStdInd(Integer.valueOf(this.return_std_ind ? 1 : 0));
            /*      */
 /* 1774 */ this.nc_std_ind = this.stdo_nc_ind;
            /* 1775 */ this.stud.setNcStdInd(Integer.valueOf(this.nc_std_ind ? 1 : 0));
            /*      */
 /* 1777 */ this.std_marry = this.stdo_marry;
            /* 1778 */ this.stud.setStudentMMarry(this.std_marry ? "yes" : "no");
            /*      */
 /* 1780 */ this.std_sda = this.stdo_sda;
            /* 1781 */ this.stud.setStudentNSda(this.std_sda ? "yes" : "no");
            /*      */
 /* 1783 */ this.std_indept = this.stdo_indept;
            /* 1784 */ this.stud.setStudentYIndept(this.std_indept ? "yes" : "no");
            /*      */
 /* 1786 */ this.std_sex = this.stdo_sex;
            /* 1787 */ this.stud.setSex(this.std_sex);
            /*      */
 /* 1789 */ this.std_merits = this.stdo_merits;
            /* 1790 */ this.stud.setStudentSMerit(this.std_merits);
            /*      */
 /*      */
 /* 1793 */ this.stud.setStudentUAcademic(this.stdo_academic);
            /*      */        }
        /*      */
 /* 1796 */ this.stud.setEa_nonlsu_perc(Integer.valueOf(this.stud.getStudentAiEduAllowPer().multiply(new BigDecimal(100)).intValue()));
        /*      */
 /*      */
 /* 1799 */ if (this.stud.getStd_transfer_ind() == null) {
            /* 1800 */ this.stud.setStd_transfer_ind(Integer.valueOf(-1));
            /*      */        }
        /* 1802 */ if (this.stud.getStd_1st_freshmen() == null) {
            /* 1803 */ this.stud.setStd_1st_freshmen(Integer.valueOf(-1));
            /*      */        }
        /*      */
 /*      */
 /* 1807 */ if (this.stud.getNcStdInd() == null) {
            this.stud.setNcStdInd(Integer.valueOf(0));
        }
        /* 1808 */ if (this.stud.getReturnStdInd() == null) {
            this.stud.setReturnStdInd(Integer.valueOf(0));
        }
        /*      */
 /*      */
 /* 1811 */ if (!pu) {
            /* 1812 */ msg = checkScholarships(new Integer[0]);
            /*      */        } else {
            /* 1814 */ msg = checkScholarships(new Integer[]{Integer.valueOf(1)});
            /*      */        }
        /* 1816 */ if (msg != null && !msg.isEmpty()) {
            /* 1817 */ log.info("/////////saveStud() got non-empty checking msg from checkScholarships(): %s", new Object[]{msg});
            /*      */
 /* 1819 */ return msg;
            /*      */        }
        /* 1821 */ if (this.stud.getStudentUAcademic() == null) {
            /* 1822 */ if (this.ref.isEmp(this.stdo_academic)) {
                /* 1823 */ log.info("############################ saveStud() found academic level is null. default to FR");
                /* 1824 */ this.stud.setStudentUAcademic("FR");
                /*      */            } else {
                /* 1826 */ this.stud.setStudentUAcademic(this.stdo_academic);
                /*      */            }
            /*      */        }
        /* 1829 */ this.stud.setStudentPassword("useless");
        /* 1830 */ this.stud.setStudentFisy(this.ref.getFiscal_year());
        /*      */
 /* 1832 */ if (pu) {
            /* 1833 */ this.stud.setCounselorId(Integer.valueOf(this.ref.getSys_counselor_id()));
            /*      */        } else {
            int counsellor_id = this.login.getCurrentUser() != null && this.login.getCurrentUser().getUserid() != null ? this.login.getCurrentUser().getUserid() : 0;
            /* 1835 */ this.stud.setCounselorId(counsellor_id);
            /*      */        }
        /* 1837 */ this.stud.setClientId(Integer.valueOf(this.ref.getClientid()));
        /* 1838 */     //this.stud.setRecid("tmpid");
        int seq_ = this.info.getNowStudNumbInQueue().intValue();
        List<Integer> seqList = this.em.createNamedQuery("Student.findStudNumb").getResultList();//this.info.getNowStudNumbInQueue().intValue();this.em.createNamedQuery("");
        if (seqList != null && !seqList.isEmpty()) {
            Collections.sort(seqList, Collections.reverseOrder());
            seq_ = seqList.get(0);
            seq_ = seq_ + 1;
        }
        int nextStudentNumber = seq_ + 1;
        /* 1840 */ this.stud.setLostTime(0L);
        /* 1841 */ this.stud.setLostToLocal(null);
        /* 1842 */ this.stud.setLostToMaster(null);
        /* 1843 */ this.stud.setLostTz(null);
        /*      */
 /*      */
 /* 1846 */ this.stud.setPrtTimes(Integer.valueOf(0));
        /* 1847 */ this.stud.setPdfs(new ArrayList());
        /*      */
 /* 1849 */ if (this.modStud == null) {
            /* 1850 */ if (pu) {
                /* 1851 */ this.stud.setStudentBuOrigCounselor("MYCAMPUS PORTAL");
                /*      */            } else {
                /*      */
 /* 1854 */ this.stud.setStudentBuOrigCounselor(this.login.getCurrentUser().getUsername());
                /* 1855 */ this.stud.setCounselorOrig(this.login.getCurrentUser().getUserid());
                /*      */            }
            /*      */
 /* 1858 */ this.stud.setDdoe(System.currentTimeMillis());
            /* 1859 */ this.stud.setTzdoe(this.ref.getTzSN());
            /*      */
 /* 1861 */ this.stud.setStudentBzUploaded("No");
            /* 1862 */ this.stud.setStudentBvDoe(new Date());
            /*      */
 /* 1864 */ this.stud.setDdom(0L);
            /* 1865 */ this.stud.setTzdom(null);
            /* 1866 */ this.stud.setStudentBxModCounselor(null);
            /* 1867 */ this.stud.setModRoot(null);
            /* 1868 */ this.stud.setModPre(null);
            /*      */        } else {
            /* 1870 */ this.stud.setDdown(0L);
            /* 1871 */ this.stud.setDup(0L);
            /*      */
 /*      */
 /*      */
 /* 1875 */ this.stud.setStudentBxModCounselor(this.login.getCurrentUser().getUsername());
            /* 1876 */ this.stud.setDdom(System.currentTimeMillis());
            /* 1877 */ this.stud.setTzdom(this.ref.getTzSN());
            /*      */
 /* 1879 */ this.stud.setCounselorMod(this.login.getCurrentUser().getUserid());
            /* 1880 */ if (this.modStud.getModRoot() != null) {
                /* 1881 */ this.stud.setModRoot(this.modStud.getModRoot());
                /*      */            } else {
                /* 1883 */ this.stud.setModRoot(this.modStud.getRecid());
                /*      */            }
            /* 1885 */ this.stud.setModPre(this.modStud.getRecid());
            /*      */        }
        /*      */
 /* 1888 */ this.stud.setPickupInd(1);
        /* 1889 */ this.stud.setStudentFisy(this.ref.getFiscal_year());
        /* 1890 */ this.stud.setStudentStudType("UGFY");
        /*      */
 /* 1892 */ this.stud.setStudentNumb(null);
        /*      */
 /* 1894 */ if (this.stud.getSex() == null) {
            /* 1895 */ this.std_sex = "N";
            /* 1896 */ this.stud.setSex("N");
            /*      */        }
        /*      */
 /* 1899 */ if (this.modStud != null) {
            /* 1900 */ this.modStud.setPickupInd(0);
            /*      */
 /* 1902 */ this.modStud.setLostTime(System.currentTimeMillis());
            /* 1903 */ this.modStud.setLostTz(this.ref.getTzSN());
            /*      */        }
        /* 1905 */ List<Student> matches = queryActiveStudsByIdOrUsername(this.stud);
        /* 1906 */ String tz = this.ref.getTzSN();
        /*      */
 /*      */
 /*      */ try {

            // we have to check this.stud should be greater than this.modStud

            /* 1910 */ if (pu) {
                //int nextStudNos_=seq_;
                String real_rec_id_ = this.stud.getCounselorId() + "." + this.stud.getClientId() + "." + seq_;
                String old_rec_id = this.stud.getRecid();
                if (this.stud.getRecid() == null && this.modStud == null) {/* 1911 */
                    //save the data 
                    this.stud.setStudentNumb(nextStudentNumber);
                    this.stud.setRecid(real_rec_id_);
                    msg = this.accessor.saveStudInfo(this.stud, this.modStud, matches, tz, new Integer[]{Integer.valueOf(1)});
                } else {
                    //update the data here thats it
                    this.stud.setLostToLocal(this.stud.getRecid());
                    this.stud.setRecid(real_rec_id_);
                    this.stud.setStudentNumb(nextStudentNumber);
                    this.accessor.updateStudInfo(this.stud);
                }
                /*      */            } else {
                String real_rec_id_ = this.stud.getCounselorId() + "." + this.stud.getClientId() + "." + seq_;
                String old_rec_id = this.stud.getRecid();
                if ((this.stud.getRecid() == null || this.stud.getRecid().equalsIgnoreCase("tmpid"))) {
                    this.stud.setRecid(real_rec_id_);
                    this.stud.setStudentNumb(nextStudentNumber);
                    msg = this.accessor.saveStudInfo(this.stud, this.modStud, matches, tz, new Integer[0]);
                } else {
                    //update the data here thats it
                    this.stud.setLostToLocal(this.stud.getRecid());

                    if (old_rec_id.equalsIgnoreCase(real_rec_id_) || (this.stud.getRecid() != null && Integer.valueOf(this.stud.getRecid().split("\\.")[2]) >= Integer.valueOf(real_rec_id_.split("\\.")[2]))) {

                        seq_ = Integer.valueOf(this.stud.getRecid().split("\\.")[2]) + 1;
                        real_rec_id_ = this.stud.getCounselorId() + "." + this.stud.getClientId() + "." + seq_;
                    }

                    if (this.modStud != null) {

                        updateModStudent();

                        this.modStud.setRecid(real_rec_id_);
                        this.stud.setRecid(real_rec_id_);
                        this.stud.setStudentNumb(nextStudentNumber);
                        this.modStud.setStudentNumb(nextStudentNumber);
                        this.accessor.updateStudInfo(this.modStud);

                    } else {

                        this.stud.setStudentNumb(nextStudentNumber);
                        this.stud.setRecid(real_rec_id_);
                        this.accessor.updateStudInfo(this.stud);
                    }
                    this.accessor.updateStudentPickUpInd(old_rec_id);
                }
                /* 1913 */
 /*      */            }
            /*      */
 /* 1916 */ if (!msg.isEmpty()) {
                /* 1917 */ log.info("????????? error when saving matches and mod info: %s", new Object[]{msg});
                /* 1918 */ return msg;
                /*      */            }

            /*      */        } /* 1952 */ catch (Exception e) {
            /* 1953 */ msg = "Error: " + e.getMessage();
            if (e.getMessage() == null) {

            }
            /* 1954 */ e.printStackTrace();
            /*      */        }

        //have success msg thrown here 
        /* 1956 */ return msg;
        /*      */    }

    /*      */
 /*      */ private String chkStudBasicInfo(Integer... p) {
        /* 1960 */ boolean npu = (p.length == 0);
        /*      */
 /* 1962 */ String msg = "";
        /*      */
 /* 1964 */ int flag = 0;
        /*      */
 /* 1966 */ int chk = validateLsuId(this.stud.getStudentALsuid());
        /* 1967 */ if (chk < 1 && chk == 0) {
            /* 1968 */ FacesMessage err = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidLSUID");
            /*      */
 /* 1970 */ flag++;
            /* 1971 */ if (npu) {
                /* 1972 */ UIComponent c = getUIComponentById("std_lsuid");
                /* 1973 */ if (c == null) {
                    this.facesContext.getViewRoot().findComponent("std_lsuid");
                }
                /*      */
 /* 1975 */ UIInput textInput = (UIInput) c;
                /* 1976 */ textInput.setValid(false);
                /* 1977 */ String label = (String) c.getAttributes().get("label") + ": " + err.getDetail();
                /* 1978 */ err.setDetail(label);
                /* 1979 */ label = (String) c.getAttributes().get("label") + ": " + err.getSummary();
                /* 1980 */ err.setSummary(label);
                /* 1981 */ if (this.facesContext != null) {
                    this.facesContext.addMessage("auth-sv-ptab-std_lsuid", err);
                }
                /*      */
 /*      */            }
            /*      */        }
        /* 1985 */ chk = validateLn(this.std_ln);
        /* 1986 */ if (chk != 0) {
            /* 1987 */ FacesMessage err;
            log.info("''''''''chkStudBasicInfo() validateLn(std_ln=%s)==%d", new Object[]{this.std_ln, Integer.valueOf(chk)});
            /* 1988 */ if (chk < 0) {
                /* 1989 */ err = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NoLName");
                /*      */            } else {
                /* 1991 */ err = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidLName");
                /*      */            }
            /*      */
 /* 1994 */ flag++;
            /* 1995 */ if (npu) {
                /* 1996 */ UIComponent c = getUIComponentById("std_lname");
                /* 1997 */ if (c == null) {
                    this.facesContext.getViewRoot().findComponent("std_lname");
                }
                /*      */
 /* 1999 */ UIInput textInput = (UIInput) c;
                /* 2000 */ textInput.setValid(false);
                /* 2001 */ String label = (String) c.getAttributes().get("label") + ": " + err.getDetail();
                /* 2002 */ err.setDetail(label);
                /* 2003 */ label = (String) c.getAttributes().get("label") + ": " + err.getSummary();
                /* 2004 */ err.setSummary(label);
                /* 2005 */ if (this.facesContext != null) {
                    this.facesContext.addMessage("auth-sv-ptab-std_lname", err);
                }
                /*      */            }
            /*      */        }
        /* 2008 */ chk = validateFn(this.std_fn);
        /* 2009 */ if (chk != 0) {
            /* 2010 */ FacesMessage err;
            log.info("''''''''chkStudBasicInfo() validateFn(std_fn=%s)==%d", new Object[]{this.std_fn, Integer.valueOf(chk)});
            /* 2011 */ if (chk < 0) {
                /* 2012 */ err = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NoFName");
                /*      */            } else {
                /* 2014 */ err = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidFName");
                /*      */            }
            /*      */
 /* 2017 */ flag++;
            /* 2018 */ if (npu) {
                /* 2019 */ UIComponent c = getUIComponentById("std_fname");
                /* 2020 */ if (c == null) {
                    this.facesContext.getViewRoot().findComponent("std_fname");
                }
                /* 2021 */ UIInput textInput = (UIInput) c;
                /* 2022 */ textInput.setValid(false);
                /* 2023 */ String label = (String) c.getAttributes().get("label") + ": " + err.getDetail();
                /* 2024 */ err.setDetail(label);
                /* 2025 */ label = (String) c.getAttributes().get("label") + ": " + err.getSummary();
                /* 2026 */ err.setSummary(label);
                /* 2027 */ if (this.facesContext != null) {
                    this.facesContext.addMessage("auth-sv-ptab-std_fname", err);
                }
                /*      */            }
            /*      */        }
        /* 2030 */ chk = validateDobStr(this.std_dob);
        /* 2031 */ if (chk != 1) {
            /* 2032 */ FacesMessage err;
            if (chk < 0) {
                /* 2033 */ err = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NoDoB");
                /* 2034 */            } else if (chk == 0) {
                /* 2035 */ err = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidDoB");
                /*      */            } else {
                /* 2037 */ err = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NotSuitableDoB");
                /*      */            }
            /* 2039 */ flag++;
            /*      */
 /* 2041 */ if (npu) {
                /* 2042 */ UIComponent c = getUIComponentById("std_dob");
                /* 2043 */ if (c == null) {
                    this.facesContext.getViewRoot().findComponent("std_dob");
                }
                /* 2044 */ UIInput textInput = (UIInput) c;
                /* 2045 */ textInput.setValid(false);
                /*      */
 /* 2047 */ String label = (String) c.getAttributes().get("label") + ": " + err.getDetail();
                /* 2048 */ err.setDetail(label);
                /* 2049 */ label = (String) c.getAttributes().get("label") + ": " + err.getSummary();
                /* 2050 */ err.setSummary(label);
                /* 2051 */ if (this.facesContext != null) {
                    this.facesContext.addMessage("auth-sv-ptab-std_dob", err);
                }
                /*      */
 /*      */            }
            /*      */        }
        /*      */

 /* 2145 */ if (flag > 0) {
            /* 2146 */ msg = "student identity data is not valid or complete";
            /*      */        } else {
            /* 2148 */ this.stud.setStudentBLastname(this.std_ln.toUpperCase());
            /* 2149 */ this.stud.setStudentCFirstname(this.std_fn.toUpperCase());
            /* 2150 */ this.stud.setStudentDDob(this.std_dob);
            /*      */        }
        /*      */
 /* 2153 */ if (this.ref.isEmp(msg)) {
            /* 2154 */ msg = validateTransPhoneNumb(this.stud.getStudentFPhone());
            /* 2155 */ if (!msg.startsWith("Error:")) {
                /* 2156 */ this.stud.setStudentFPhone(msg);
                /* 2157 */ msg = "";
                /*      */            } /* 2159 */ else if (npu) {
                /* 2160 */ UIComponent c = getUIComponentById("std_phone");
                /* 2161 */ if (c == null) {
                    this.facesContext.getViewRoot().findComponent("std_phone");
                }
                /* 2162 */ UIInput textInput = (UIInput) c;
                /* 2163 */ textInput.setValid(false);
                /*      */
 /* 2165 */ FacesMessage err = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                /* 2166 */ String label = (String) c.getAttributes().get("label") + ": " + err.getDetail();
                /* 2167 */ err.setDetail(label);
                /* 2168 */ label = (String) c.getAttributes().get("label") + ": " + err.getSummary();
                /* 2169 */ err.setSummary(label);
                /* 2170 */ if (this.facesContext != null) {
                    this.facesContext.addMessage("auth-sv-ptab-std_phone", err);
                }
                /*      */
 /*      */            }
            /*      */        }
        /* 2174 */ return msg;
        /*      */    }

    /*      */
 /*      */ private List<Student> queryActiveStudsByIdOrUsername(Student stud) {
        /* 2178 */ boolean nobaseid = this.ref.isEmp(stud.getStudentALsuid());
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 2186 */ List<Student> results = this.em.createNamedQuery("Student.findActiveByLsuidOrUsername").setParameter("lsuid", nobaseid ? "1234567" : stud.getStudentALsuid()).setParameter("username", stud.getStudentUserName()).setParameter("studentFisy", Integer.valueOf(this.ref.getFiscal_year())).getResultList();
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 2193 */ List<Student> matches = new ArrayList<>();
        /* 2194 */ for (Student one : results) {
            /* 2195 */ if (!this.ref.isEmp(one.getStudentALsuid()) && !nobaseid) {
                /* 2196 */ if (one.getStudentALsuid().equals(stud.getStudentALsuid())) /* 2197 */ {
                    matches.add(one);
                }
                continue;
                /*      */            }
            /* 2199 */ if (one.getStudentUserName().equalsIgnoreCase(stud.getStudentUserName())) {
                /* 2200 */ matches.add(one);
                /*      */            }
            /*      */        }
        /* 2203 */ for (Student one : matches) {
            /* 2204 */ log.info("???????????????? queryActiveStudsByIdOrUsername() found matched/dup active student: (%s, %s) for (%s,%s)", new Object[]{one.getRecid(), one.getStudentUserName(), stud.getRecid(), stud.getStudentUserName()});
            /*      */        }
        /* 2206 */ return matches;
        /*      */    }

    /*      */
 /*      */ public UIComponent getUIComponentById(String componentId) {
        /* 2210 */ FacesContext context = FacesContext.getCurrentInstance();
        /* 2211 */ UIViewRoot root = context.getViewRoot();
        /*      */
 /*      */
 /* 2214 */ UIComponent c = findComponent((UIComponent) root, componentId);
        /*      */
 /* 2216 */ return c;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ private UIComponent findComponent(UIComponent c, String id) {
        /* 2223 */ if (id.equals(c.getId())) {
            /* 2224 */ return c;
            /*      */        }
        /* 2226 */ Iterator<UIComponent> kids = c.getFacetsAndChildren();
        /* 2227 */ while (kids.hasNext()) {
            /* 2228 */ UIComponent found = findComponent(kids.next(), id);
            /* 2229 */ if (found != null) {
                /* 2230 */ return found;
                /*      */            }
            /*      */        }
        /* 2233 */ return null;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String savepstudinfo() {
        /* 2238 */ String ln = this.stud.getStudentBLastname();
        /* 2239 */ String fn = this.stud.getStudentCFirstname();
        /* 2240 */ String dob = this.stud.getStudentDDob();
        /* 2241 */ String phone = this.stud.getStudentFPhone();
        /* 2242 */ String msg = chkStudBasicInfo(new Integer[]{Integer.valueOf(1)});
        /* 2243 */ log.info("???? savestudinfo() get chkStudBasicInfo() msg=%s", new Object[]{msg});
        /* 2244 */ if (!msg.isEmpty()) {
            return msg;
        }
        /*      */
 /* 2246 */ this.stud.setStudentUserName(compUserName(this.stud.getStudentALsuid(), ln, fn, dob));
        /* 2247 */ msg = saveStud(0, new int[]{1});
        /*      */
 /* 2249 */ if (msg == null || msg.isEmpty()) {
            /* 2250 */ FacesMessage guimsg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_INFO, "EstimateForm.DataSaved");
            /*      */
 /* 2252 */ this.stud.setStudentNumb((Integer) null);
            /*      */
 /*      */
 /* 2255 */ PDFgen pdf = new PDFgen();
            /* 2256 */ Print prt = new Print();
            /* 2257 */ prt.setSship1ExtAmt(Integer.valueOf(0));
            /* 2258 */ prt.setSship2ExtAmt(Integer.valueOf(0));
            /* 2259 */ prt.setSship7ExtAmt(Integer.valueOf(0));
            /* 2260 */ prt.setSship8ExtAmt(Integer.valueOf(0));
            /* 2261 */ prt.setSship9ExtAmt(Integer.valueOf(0));
            /*      */
 /* 2263 */ msg = pdf.genPDF(this.stud, this.login, this.ref, this.calc, prt, 0, new Integer[]{Integer.valueOf(1)});
            /* 2264 */ if (!msg.startsWith("Failed")) {
                /* 2265 */ log.info("==================savestudinfo() gen pdf: pass");
                /* 2266 */ String pdfname = msg;
                /*      */
 /* 2268 */ msg = savePrint(prt, 0, new Integer[]{Integer.valueOf(1)});
                /*      */
 /* 2270 */ if (msg == null || msg.isEmpty()) {
                    /* 2271 */ log.info("==================savestudinfo() save print rec: pass");
                    /* 2272 */ return "SAVED";
                    /*      */                }
                /* 2274 */ log.info("==================savestudinfo() save print rec: fail. msg=%s", new Object[]{msg});
                /* 2275 */ return msg;
                /*      */            }
            /*      */
 /* 2278 */ log.info("==================savestudinfo() gen pdf: fail. msg=%s", new Object[]{msg});
            /* 2279 */ return msg;
            /*      */        }
        /*      */
 /*      */
 /* 2283 */ return msg;
        /*      */    }

    public void updateModStudent() {
        try {
            BeanUtils.copyProperties(this.modStud, this.stud);
        } catch (Exception ex) {
        }
    }

    /*      */ public void savestudinfo(ActionEvent event) {
        /* 2290 */ String ln = this.stud.getStudentBLastname();
        /* 2291 */ String fn = this.stud.getStudentCFirstname();
        /* 2292 */ String dob = this.stud.getStudentDDob();
        /* 2293 */ String phone = this.stud.getStudentFPhone();
        /* 2294 */ String msg = chkStudBasicInfo(new Integer[0]);
        /* 2295 */ log.info("???? savestudinfo() get chkStudBasicInfo() msg=%s", new Object[]{msg});
        /*      */
 /*      */
 /* 2298 */ if (this.stud.getStudentAuScholarship1Amt().intValue() > 0) {
            this.stud.setStudentAuScholarship1Amt(Integer.valueOf(0));
        }
        /*      */
 /*      */
 /* 2301 */ if (!msg.isEmpty()) {
            /*      */ return;
            /*      */        }

        /* 2310 */ this.stud.setStudentUserName(compUserName(this.stud.getStudentALsuid(), ln, fn, dob));
        /*      */
 /* 2312 */ msg = saveStud(0, new int[0]);

        /* 2387 */ if (msg == null || msg.isEmpty()) {
            /* 2388 */ FacesMessage guimsg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_INFO, "EstimateForm.DataSaved");
            /* 2389 */ if (this.facesContext != null) {
                this.facesContext.addMessage(null, guimsg);
            }
            /* 2390 */ this.stud.setStudentNumb((Integer) null);
            /*      */
 /*      */
 /* 2393 */ PDFgen pdf = new PDFgen();
            /* 2394 */ Print prt = new Print();
            /* 2395 */ prt.setSship1ExtAmt(Integer.valueOf(0));
            /* 2396 */ prt.setSship2ExtAmt(Integer.valueOf(0));
            /* 2397 */ prt.setSship7ExtAmt(Integer.valueOf(0));
            /* 2398 */ prt.setSship8ExtAmt(Integer.valueOf(0));
            /* 2399 */ prt.setSship9ExtAmt(Integer.valueOf(0));
            /*      */
 /* 2401 */ msg = pdf.genPDF(this.stud, this.login, this.ref, this.calc, prt, 0, new Integer[0]);
            /* 2402 */ if (!msg.startsWith("Failed")) {
                /* 2403 */ log.info("==================savestudinfo() gen pdf: pass");
                /* 2404 */ String pdfname = msg;
                /*      */
 /* 2406 */ msg = savePrint(prt, 0, new Integer[0]);
                /*      */
 /* 2408 */ if (msg == null || msg.isEmpty()) {
                    /* 2409 */ log.info("==================savestudinfo() save print rec: pass");

                    /*      */                } else {
                    /* 2411 */ log.info("==================savestudinfo() save print rec: fail. msg=%s", new Object[]{msg});
                    /*      */                }
                /*      */            } else {
                /* 2414 */ log.info("==================savestudinfo() gen pdf: fail. msg=%s", new Object[]{msg});
                /*      */            }
            /*      */        } else {
            /*      */
 /* 2418 */ FacesMessage guimsg = this.ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
            /* 2419 */ if (this.facesContext != null) {
                this.facesContext.addMessage(null, guimsg);
            }
            /*      */
 /*      */        }
        /*      */
 /* 2423 */ RequestContext reqContext = RequestContext.getCurrentInstance();
        /*      */
 /*      */
 /* 2426 */ setClientCallBack(reqContext);
        /*      */    }

    /*      */
 /*      */
 /*      */ public String navSaveStud() {
        /* 2431 */ return null;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ private String compUserName(String id, String ln, String fn, String dob) {
        /* 2437 */ return (new StringBuilder(128)).append((ln == null) ? "" : ln.toUpperCase()).append('|').append((fn == null) ? "" : fn.toUpperCase()).append('|').append((dob == null) ? "" : this.std_dob).toString();
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public String showStudName(int len) {
        /* 2443 */ if (len <= 0) {
            return "";
        }
        /* 2444 */ String str = this.stud.getStudentCFirstname();
        /* 2445 */ int flen = 0;
        /* 2446 */ String stmp = "";
        /*      */
 /* 2448 */ if (str != null && !str.isEmpty()) {
            /*      */
 /* 2450 */ flen = str.length();
            /* 2451 */ flen = (flen <= len) ? flen : len;
            /* 2452 */ stmp = str.substring(0, flen);
            /*      */        }
        /* 2454 */ flen = len - flen;
        /*      */
 /* 2456 */ String str2 = this.stud.getStudentBLastname();
        /* 2457 */ if (flen > 1 && str2 != null && !str2.isEmpty()) {
            /* 2458 */ flen--;
            /* 2459 */ if (str2.length() < flen) {
                flen = str2.length();
            }
            /* 2460 */ stmp = stmp + " " + str2.substring(0, flen);
            /*      */        }
        /* 2462 */ return stmp;
        /*      */    }

    /*      */
 /*      */
 /*      */ public void academiclevel_changed(AjaxBehaviorEvent event) {
        /* 2467 */ log.info("////////////academiclevel_changed() event triggerred. now level code is %s", new Object[]{this.stud.getStudentUAcademic()});
        /* 2468 */ this.stdo_academic = this.stud.getStudentUAcademic();
        /* 2469 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */

 /*      */ public void maritalchanged(AjaxBehaviorEvent event) {
        /* 2597 */ this.stdo_marry = this.std_marry;
        /* 2598 */ if (this.std_marry == true) {
            /* 2599 */ if (!this.std_indepts.containsKey("marry")) {
                /* 2600 */ this.std_indepts.put("marry", "marry");
                /*      */            }
            /* 2602 */ if (!this.std_indept) {
                /* 2603 */ this.std_indept = true;
                /*      */            }
            /* 2605 */ this.stud.setStudentMMarry("yes");
            /*      */
 /*      */
 /*      */
 /*      */        } /* 2610 */ else if (!this.std_marry) {
            /* 2611 */ if (this.std_indepts.containsKey("marry")) {
                /* 2612 */ this.std_indepts.remove("marry");
                /*      */            }
            /* 2614 */ if (this.std_indept == true
                    && /* 2615 */ this.std_indepts.size() == 0 && this.std_indept_byhand == 0) {
                this.std_indept = false;
            }
            /*      */
 /* 2617 */ this.stud.setStudentMMarry("no");
            /*      */        }
        /*      */
 /*      */
 /*      */
 /* 2622 */ this.stud.setStudentYIndept(this.std_indept ? "Yes" : "No");
        /* 2623 */ this.calc.refreshCalc(this.stud);
        /* 2624 */ this.stdo_indept = this.std_indept;
        /*      */    }

    /*      */
 /*      */ public void dormchanged(AjaxBehaviorEvent event) {
        /* 2628 */ this.stdo_dorm = this.std_dorm;
        /* 2629 */ this.stud.setStudentWDorm(this.std_dorm ? "Yes" : "No");
        /* 2630 */ if (this.std_eanonlsu) {
            /* 2631 */ this.stud.setStudentAiEduAllowPer((new BigDecimal(this.calc.getEaNonLsuPercentageByDorm(this.std_dorm))).divide(new BigDecimal(100)));
            /*      */        } else {
            /* 2633 */ this.stud.setStudentAiEduAllowPer(BigDecimal.ZERO);
            /*      */        }
        /* 2635 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */
 /*      */ public void sdachanged(AjaxBehaviorEvent event) {
        /* 2639 */ this.stdo_sda = this.std_sda;
        /* 2640 */ this.stud.setStudentNSda(this.std_sda ? "Yes" : "No");
        /* 2641 */ this.calc.refreshCalc(this.stud);
//tf_Ch();
/*      */    }

    /*      */
 /*      */ public void indeptclicked(AjaxBehaviorEvent event) {
        /* 2645 */ if (this.std_indept == true) {
            /* 2646 */ this.std_indept_byhand = 1;
            /*      */        } else {
            /* 2648 */ this.std_indept_byhand = 0;
            /* 2649 */ if (this.std_indepts.size() > 0) {
                this.std_indept = true;
            }
            /*      */        }
        /* 2651 */ this.stud.setStudentYIndept(this.std_indept ? "Yes" : "No");
        /* 2652 */ this.calc.refreshCalc(this.stud);
        /* 2653 */ this.stdo_indept = this.std_indept;
        /*      */    }

    /*      */
    public void tf_Ch() {
        this.stdo_tf = this.std_tf;
        this.stud.setStd_transfer_ind(this.std_tf ? 1 : 0);
    }
      public void ftfr_Ch() {
        this.stdo_ftfr = this.std_ftfr;
        this.stud.setStd_1st_freshmen(this.std_ftfr ? 1 : 0);
    }

    /*      */ public void stdtfchanged(AjaxBehaviorEvent event) {
        this.stdo_tf = this.std_tf;
        this.stud.setStd_transfer_ind(this.std_tf ? 1 : 0);
        /*      */     //this.stdo_academic = this.stud.getStudentUAcademic();
/* 2469 */ this.calc.refreshCalc(this.stud);

    }
 public void stdftfrchanged(AjaxBehaviorEvent event) {
        this.stdo_ftfr = this.std_ftfr;
        this.stud.setStd_1st_freshmen(this.std_ftfr ? 1 : 0);
        /*      */     //this.stdo_academic = this.stud.getStudentUAcademic();
/* 2469 */ this.calc.refreshCalc(this.stud);

    }
    
    /*      */
 /*      */ public void stdintlchanged(AjaxBehaviorEvent event) {
        /* 2659 */ this.stdo_intl = this.std_intl;
        /* 2660 */ if (this.std_intl) {
            /* 2661 */ this.std_fafsa = false;
            /*      */
 /*      */
 /* 2664 */ setStudFAFSA(this.std_fafsa ? "Yes" : "No");
            /*      */
 /*      */
 /*      */
 /*      */
 /* 2669 */ this.std_calgrant = false;
            /* 2670 */ this.stud.setStudentZCalgrant(this.std_calgrant ? "Yes" : "No");
            /*      */
 /*      */
 /* 2673 */ this.std_efc = false;
            /* 2674 */ this.stud.setStudentAfFamilyContrib(Integer.valueOf(99999));
            /* 2675 */ this.stud.setIndEfc(this.std_efc ? "Yes" : "No");
            /*      */        }
        /*      */
 /* 2678 */ this.stud.setStudentLIntlStud(this.std_intl ? "Yes" : "No");
        /*      */
 /* 2680 */ this.calc.refreshCalc(this.stud);
        /*      */
 /*      */
 /* 2683 */ String phone = this.stud.getStudentFPhone();
        /*      */
 /*      */
 /*      */
 /*      */
 /* 2688 */ if (this.std_intl == true || phone == null || phone.isEmpty()) /*      */ {
            return;
        }
        /* 2690 */ phone = phone.trim().replaceAll("  ", " ");
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
 /*      */
 /*      */
 /*      */
 /* 2708 */ String phonepattern2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
        /* 2709 */ String phoneNumberPattern = "(\\d-)?(\\d{3}-)?\\d{3}-\\d{4}";
        /*      */
 /*      */
 /*      */
 /* 2713 */ String phonepattern = "^([\\+]?)(([1]?)[-\\. ]?)\\(?(\\d{3})\\)?[-\\. ]?(\\d{3,4})[-\\. ]?(\\d{4})([\\. ]?([xX]\\d{1,5})){0,1}$";
        /* 2714 */ Pattern pattern = Pattern.compile(phonepattern);
        /* 2715 */ Matcher matcher = null;
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
 /* 2743 */ String newphone = phone;
        /* 2744 */ int len = phone.length();
        /* 2745 */ int firstxpos = phone.toUpperCase().indexOf("X");
        /* 2746 */ int lastxpos = phone.toUpperCase().lastIndexOf("X");
        /* 2747 */ int dotpos = phone.indexOf(".");
        /* 2748 */ int blkpos = phone.indexOf(" ");
        /* 2749 */ if (firstxpos < 0) /* 2750 */ {
            if (len < 7 || (len == 7 && (dotpos > 0 || blkpos > 0)) || (len == 8 && dotpos < 0 && blkpos < 0)) {
                /* 2751 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                /*      */
 /* 2753 */ if (this.facesContext != null) {
                    this.facesContext.addMessage(null, msg);
                }
                return;
                /*      */            }
            /* 2755 */ if (len == 7 && dotpos < 0 && blkpos < 0) {
                /* 2756 */ newphone = "951" + phone;
                /* 2757 */            } else if (len == 8 && ((blkpos > 0 && blkpos == phone.lastIndexOf(" ")) || (dotpos > 0 && dotpos == phone.lastIndexOf(".")))) {
                /* 2758 */ newphone = "951" + phone;
                /*      */            }
        } /* 2760 */ else {
            if (firstxpos < 7 || firstxpos != lastxpos || lastxpos == len - 1) {
                /* 2761 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                /*      */
 /* 2763 */ if (this.facesContext != null) {
                    this.facesContext.addMessage(null, msg);
                }
                /*      */ return;
                /*      */            }
            /* 2766 */ String subphone = phone.substring(0, firstxpos).trim();
            /* 2767 */ len = subphone.length();
            /* 2768 */ dotpos = subphone.indexOf(".");
            /* 2769 */ blkpos = subphone.indexOf(" ");
            /* 2770 */ if (len < 7 || (len == 7 && (dotpos > 0 || blkpos > 0)) || (len == 8 && dotpos < 0 && blkpos < 0)) {
                /* 2771 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                /*      */
 /* 2773 */ if (this.facesContext != null) {
                    this.facesContext.addMessage(null, msg);
                }
                return;
                /*      */            }
            /* 2775 */ if (len == 7 && dotpos < 0 && blkpos < 0) {
                /* 2776 */ newphone = "951" + phone;
                /* 2777 */            } else if (len == 8 && ((blkpos > 0 && blkpos == phone.lastIndexOf(" ")) || (dotpos > 0 && dotpos == phone.lastIndexOf(".")))) {
                /* 2778 */ newphone = "951." + phone;
                /*      */            }
        }
        /*      */
 /* 2781 */ matcher = pattern.matcher(newphone);
        /* 2782 */ if (!matcher.matches()) {
            /* 2783 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
            /* 2784 */ if (log == null);
            /*      */
 /*      */
 /*      */
 /*      */
 /* 2789 */ if (this.facesContext != null) {
                this.facesContext.addMessage(null, msg);
            }
            /*      */
 /*      */        } else {
            /* 2792 */ matcher.reset();
            /* 2793 */ if (matcher.find()) {
                /* 2794 */ len = matcher.groupCount();
                /*      */
 /* 2796 */ for (int i = 0; i < len; i++);
                /*      */            }
            /*      */
 /*      */
 /* 2800 */ StringBuilder sb = new StringBuilder(32);
            /* 2801 */ String grp = matcher.group(3);
            /* 2802 */ String grp2 = matcher.group(1);
            /* 2803 */ if (grp != null && !grp.isEmpty()) {
                /* 2804 */ if (grp2 != null && !grp2.isEmpty()) {
                    /* 2805 */ sb.append(grp2);
                    /*      */                }
                /* 2807 */ sb.append(grp).append(" ");
                /*      */            }
            /* 2809 */ grp = matcher.group(4);
            /* 2810 */ if (grp != null && !grp.isEmpty()) {
                /* 2811 */ sb.append("(").append(grp).append(") ");
                /*      */            }
            /* 2813 */ sb.append(matcher.group(5)).append("-").append(matcher.group(6));
            /* 2814 */ grp = matcher.group(7);
            /* 2815 */ if (grp != null && !grp.isEmpty()) {
                /* 2816 */ sb.append(" ").append(grp.trim());
                /*      */            }
            /*      */
 /* 2819 */ this.stud.setStudentFPhone(sb.toString());
            /*      */        }
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
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void valChangedCheckForNullToZero(AjaxBehaviorEvent event) {
        /* 2856 */ UIInput source = (UIInput) event.getSource();
        /* 2857 */ String clientfid = source.getClientId();
        /*      */
 /* 2859 */ Object val = source.getValue();
        /* 2860 */ log.info(" dob change event called in phase %s, clientid=%s id=%s, value=%s", new Object[]{event.getPhaseId(), clientfid, source.getId(), (val == null) ? "null" : val.toString()});
        /* 2861 */ if (val == null || val.toString().trim().isEmpty() || val.toString().trim().equals("$")) {
            /* 2862 */ source.setValue("0");
            /*      */        }
        /*      */    }

    /*      */
 /*      */
 /*      */ public Student getStud() {
        /* 2868 */ return this.stud;
        /*      */    }

    /*      */
 /*      */ public PackFunctions getCalc() {
        /* 2872 */ return this.calc;
        /*      */    }

    /*      */
 /*      */ public Student getModStud() {
        /* 2876 */ return this.modStud;
        /*      */    }

    /*      */
 /*      */ public void setModStud(Student modStud) {
        /* 2880 */ this.modStud = modStud;
        /*      */    }

    /*      */
 /*      */ public int getModflag() {
        /* 2884 */ return this.modflag;
        /*      */    }

    /*      */
 /*      */ public void setModflag(int modflag) {
        /* 2888 */ this.modflag = modflag;
        /*      */    }

    /*      */
 /*      */ public String getStd_merits() {
        /* 2892 */ return this.std_merits;
        /*      */    }

    /*      */ public void setStd_merits(String p_std_merits) {
        /* 2895 */ this.std_merits = p_std_merits;
        /* 2896 */ this.stud.setStudentSMerit(this.std_merits);
        /* 2897 */ this.stdo_merits = this.std_merits;
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
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public boolean isStd_intl() {
        /* 2976 */ return this.std_intl;
        /*      */    }

    public boolean isStd_tf() {
        /* 2976 */ return this.std_tf;
        /*      */    }

    /*      */ public void setStd_tf(boolean std_tf) {
        this.std_tf = std_tf;
        /* 2981 */ this.stud.setStd_transfer_ind(this.std_tf ? 1 : 0);
    }

     public boolean isStd_ftfr() {
        /* 2976 */ return this.std_ftfr;
        /*      */    }

    /*      */ public void setStd_ftfr(boolean std_ftfr) {
        this.std_ftfr = std_ftfr;
        /* 2981 */ this.stud.setStd_1st_freshmen(this.std_ftfr ? 1 : 0);
    }
    
    /*      */ public void setStd_intl(boolean std_intl) {
        /* 2980 */ this.std_intl = std_intl;
        /* 2981 */ this.stud.setStudentLIntlStud(std_intl ? "yes" : "no");
        /*      */    }

    /*      */
 /*      */ public boolean isStd_dorm() {
        /* 2985 */ return this.std_dorm;
        /*      */    }

    /*      */
 /*      */ public void setStd_dorm(boolean std_dorm) {
        /* 2989 */ this.std_dorm = std_dorm;
        /* 2990 */ this.stud.setStudentWDorm(std_dorm ? "yes" : "no");
        /*      */    }

    /*      */
 /*      */ public boolean isStd_marry() {
        /* 2994 */ return this.std_marry;
        /*      */    }

    /*      */
 /*      */ public void setStd_marry(boolean std_marry) {
        /* 2998 */ this.std_marry = std_marry;
        /* 2999 */ this.stud.setStudentMMarry(std_marry ? "yes" : "no");
        /*      */    }

    /*      */
 /*      */ public boolean isStd_sda() {
        /* 3003 */ return this.std_sda;
        /*      */    }

    /*      */
 /*      */ public void setStd_sda(boolean std_sda) {
        /* 3007 */ this.std_sda = std_sda;
        /* 3008 */ this.stud.setStudentNSda(std_sda ? "yes" : "no");
        /*      */    }

    /*      */
 /*      */ public boolean isStd_indept() {
        /* 3012 */ return this.std_indept;
        /*      */    }

    /*      */
 /*      */ public void setStd_indept(boolean std_indept) {
        /* 3016 */ this.std_indept = std_indept;
        /* 3017 */ this.stud.setStudentYIndept(std_indept ? "yes" : "no");
        /*      */    }

    /*      */
 /*      */ public boolean isStd_fafsa() {
        /* 3021 */ return this.std_fafsa;
        /*      */    }

    /*      */
 /*      */ public void setStd_fafsa(boolean in_std_fafsa) {
        /* 3025 */ this.std_fafsa = in_std_fafsa;
        /*      */
 /*      */
 /* 3028 */ setStudFAFSA(this.std_fafsa ? "Yes" : "No");
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public int getTab_index() {
        /* 3034 */ return this.tab_index;
        /*      */    }

    /*      */
 /*      */ public void setTab_index(int tab_index) {
        /* 3038 */ this.tab_index = tab_index;
        /*      */    }

    /*      */
 /*      */
 /*      */ public String getPreTabString() {
        /* 3043 */ return this.PreTabString;
        /*      */    }

    /*      */
 /*      */ public String getNextTabString() {
        /* 3047 */ return this.NextTabString;
        /*      */    }

    /*      */
 /*      */ public String getStd_sex() {
        /* 3051 */ if (this.ref.isEmp(this.std_sex)) {
            /* 3052 */ this.std_sex = "N";
            /* 3053 */ this.stud.setSex(this.std_sex);
            /*      */        }
        /* 3055 */ return this.std_sex;
        /*      */    }

    /*      */
 /*      */ public void setStd_sex(String std_sex) {
        /* 3059 */ if (this.ref.isEmp(std_sex)) {
            /* 3060 */ this.std_sex = "N";
            /*      */        } else {
            /* 3062 */ this.std_sex = std_sex;
            /*      */        }
        /* 3064 */ this.stud.setSex(std_sex);
        /*      */    }

    /*      */
 /*      */ public List<String> getStd_quas() {
        /* 3068 */ return this.std_quas;
        /*      */    }

    /*      */
 /*      */ public void setStd_quas(List<String> std_quas) {
        /* 3072 */ this.std_quas = std_quas;
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
 /*      */ public int getStd_EA_PERCENT() {
        /* 3084 */ return this.std_EA_PERCENT;
        /*      */    }

    /*      */
 /*      */ public void setStd_EA_PERCENT(int std_EA_PERCENT) {
        /* 3088 */ this.std_EA_PERCENT = std_EA_PERCENT;
        /*      */    }

    /*      */
 /*      */ public boolean isStd_calgrant() {
        /* 3092 */ return this.std_calgrant;
        /*      */    }

    /*      */
 /*      */ public void setStd_calgrant(boolean std_calgrant) {
        /* 3096 */ this.std_calgrant = std_calgrant;
        /* 3097 */ this.stud.setStudentZCalgrant(std_calgrant ? "yes" : "no");
        /*      */    }

    /*      */
 /*      */ public boolean isStd_efc() {
        /* 3101 */ return this.std_efc;
        /*      */    }

    /*      */
 /*      */ public void setStd_efc(boolean std_efc) {
        /* 3105 */ this.std_efc = std_efc;
        /* 3106 */ this.stud.setIndEfc(std_efc ? "yes" : "no");
        /*      */    }

    /*      */
 /*      */ public boolean isStd_ealsu() {
        /* 3110 */ return this.std_ealsu;
        /*      */    }

    /*      */
 /*      */ public void setStd_ealsu(boolean std_ealsu) {
        /* 3114 */ this.std_ealsu = std_ealsu;
        /* 3115 */ this.stud.setIndEalsu(std_ealsu ? "yes" : "no");
        /*      */    }

    /*      */
 /*      */ public boolean isStd_eanonlsu() {
        /* 3119 */ return this.std_eanonlsu;
        /*      */    }

    /*      */
 /*      */ public void setStd_eanonlsu(boolean std_eanonlsu) {
        /* 3123 */ this.std_eanonlsu = std_eanonlsu;
        /* 3124 */ this.stud.setIndEanonlsu(std_eanonlsu ? "yes" : "no");
        /* 3125 */ this.stud.setStudentAgNonlsuAllowrance(std_eanonlsu ? new BigDecimal(1) : new BigDecimal(0));
        /*      */    }

    /*      */
 /*      */ public boolean isStd_noloans() {
        /* 3129 */ return this.std_noloans;
        /*      */    }

    /*      */
 /*      */ public void setStd_noloans(boolean std_noloans) {
        /* 3133 */ this.std_noloans = std_noloans;
        /* 3134 */ this.stud.setIndExcloans(std_noloans ? "yes" : "no");
        /*      */    }

    /*      */
 /*      */ public String getStd_dob() {
        /* 3138 */ return this.std_dob;
        /*      */    }

    /*      */
 /*      */ public void setStd_dob(String std_dob) {
        /* 3142 */ this.std_dob = std_dob;
        /*      */    }

    /*      */
 /*      */ public HashMap<String, String> getStd_indepts() {
        /* 3146 */ return this.std_indepts;
        /*      */    }

    /*      */
 /*      */ public void std_state_changed(AjaxBehaviorEvent event) {
        /* 3150 */ String state = this.stud.getStudentIState();
        /* 3151 */ if (state != null) {
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
 /*      */
 /*      */
 /*      */
 /*      */
 /* 3170 */ state = state.trim();
            /* 3171 */ this.stud.setStudentIState(upper(state));
            /*      */        }
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public void std_zip_changed(AjaxBehaviorEvent event) {
        /* 3178 */ String zip = this.stud.getStudentJZip();
        /* 3179 */ if (zip != null) {
            zip = zip.trim();
        }
        /* 3180 */ if (zip != null && !zip.isEmpty() && !this.std_intl) {
            /* 3181 */ zip = zip.trim().replaceAll("  ", " ");
            /*      */
 /* 3183 */ int len = zip.length();
            /* 3184 */ if (zip.lastIndexOf("-") == len - 1) {
                zip = zip.replaceAll("-", "");
            }
            /* 3185 */ len = zip.length();
            /* 3186 */ zip = upper(zip);
            /*      */
 /* 3188 */ if (len > 5) {
                /*      */
 /* 3190 */ String e = zip.substring(5, 6);
                /* 3191 */ boolean q = (e.equals(" ") || e.equals("-") || e.equals("X"));
                /* 3192 */ zip = (new StringBuilder(16)).append(zip.substring(0, 5)).append((q == true) ? "" : "-").append(zip.substring(5)).toString();
                /* 3193 */ this.stud.setStudentJZip(zip);
                /*      */            }
            /*      */        }
        /* 3196 */ if (this.ref.isEmp(zip));
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void phonechanged(AjaxBehaviorEvent event) {
        /* 3203 */ UIInput source = (UIInput) event.getSource();
        /* 3204 */ String clientfid = source.getClientId();
        /*      */
 /* 3206 */ String phone = this.stud.getStudentFPhone();
        /* 3207 */ this.stud.setStudentFPhone(upper(phone));
        /*      */
 /* 3209 */ if (this.std_intl == true) {
            /* 3210 */ if (phone == null || phone.isEmpty()) {
                source.setValid(false);
            }
            /*      */
 /*      */ return;
            /*      */        }
        /* 3214 */ if (phone != null && !phone.isEmpty()) {
            /* 3215 */ source.setValid(false);
            /*      */
 /* 3217 */ phone = phone.trim().replaceAll("  ", " ");
            /*      */
 /* 3219 */ String phonepattern2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
            /* 3220 */ String phoneNumberPattern = "(\\d-)?(\\d{3}-)?\\d{3}-\\d{4}";
            /*      */
 /*      */
 /*      */
 /* 3224 */ String phonepattern = "^([\\+]?)(([1]?)[-\\. ]?)\\(?(\\d{3})\\)?[-\\. ]?(\\d{3,4})[-\\. ]?(\\d{4})([\\. ]?([xX]\\d{1,5})){0,1}$";
            /* 3225 */ Pattern pattern = Pattern.compile(phonepattern);
            /* 3226 */ Matcher matcher = null;
            /*      */
 /* 3228 */ String newphone = phone;
            /* 3229 */ int len = phone.length();
            /* 3230 */ int firstxpos = phone.toUpperCase().indexOf("X");
            /* 3231 */ int lastxpos = phone.toUpperCase().lastIndexOf("X");
            /* 3232 */ int dotpos = phone.indexOf(".");
            /* 3233 */ int blkpos = phone.indexOf(" ");
            /* 3234 */ if (firstxpos < 0) /* 3235 */ {
                if (len < 7 || (len == 7 && (dotpos > 0 || blkpos > 0)) || (len == 8 && dotpos < 0 && blkpos < 0)) {
                    /* 3236 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                    /*      */
 /* 3238 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, msg);
                    }
                    return;
                    /*      */                }
                /* 3240 */ if (len == 7 && dotpos < 0 && blkpos < 0) {
                    /* 3241 */ newphone = "951" + phone;
                    /* 3242 */                } else if (len == 8 && ((blkpos > 0 && blkpos == phone.lastIndexOf(" ")) || (dotpos > 0 && dotpos == phone.lastIndexOf(".")))) {
                    /* 3243 */ newphone = "951" + phone;
                    /*      */                }
            } /* 3245 */ else {
                if (firstxpos < 7 || firstxpos != lastxpos || lastxpos == len - 1) {
                    /* 3246 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                    /*      */
 /* 3248 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, msg);
                    }
                    /*      */ return;
                    /*      */                }
                /* 3251 */ String subphone = phone.substring(0, firstxpos).trim();
                /* 3252 */ len = subphone.length();
                /* 3253 */ dotpos = subphone.indexOf(".");
                /* 3254 */ blkpos = subphone.indexOf(" ");
                /* 3255 */ if (len < 7 || (len == 7 && (dotpos > 0 || blkpos > 0)) || (len == 8 && dotpos < 0 && blkpos < 0)) {
                    /* 3256 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                    /*      */
 /* 3258 */ if (this.facesContext != null) {
                        this.facesContext.addMessage(null, msg);
                    }
                    return;
                    /*      */                }
                /* 3260 */ if (len == 7 && dotpos < 0 && blkpos < 0) {
                    /* 3261 */ newphone = "951" + phone;
                    /* 3262 */                } else if (len == 8 && ((blkpos > 0 && blkpos == phone.lastIndexOf(" ")) || (dotpos > 0 && dotpos == phone.lastIndexOf(".")))) {
                    /* 3263 */ newphone = "951." + phone;
                    /*      */                }
            }
            /*      */
 /* 3266 */ matcher = pattern.matcher(newphone);
            /* 3267 */ if (!matcher.matches()) {
                /* 3268 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                /* 3269 */ if (log == null);
                /*      */
 /*      */
 /*      */
 /*      */
 /* 3274 */ if (this.facesContext != null) {
                    this.facesContext.addMessage(null, msg);
                }
                /*      */
 /*      */            } else {
                /* 3277 */ matcher.reset();
                /* 3278 */ if (matcher.find()) {
                    /* 3279 */ len = matcher.groupCount();
                    /*      */                }
                /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 3286 */ StringBuilder sb = new StringBuilder(32);
                /* 3287 */ String grp = matcher.group(3);
                /* 3288 */ String grp2 = matcher.group(1);
                /* 3289 */ if (grp != null && !grp.isEmpty()) {
                    /* 3290 */ if (grp2 != null && !grp2.isEmpty()) {
                        /* 3291 */ sb.append(grp2);
                        /*      */                    }
                    /* 3293 */ sb.append(grp).append(" ");
                    /*      */                }
                /* 3295 */ grp = matcher.group(4);
                /* 3296 */ if (grp != null && !grp.isEmpty()) {
                    /* 3297 */ sb.append("(").append(grp).append(") ");
                    /*      */                }
                /* 3299 */ sb.append(matcher.group(5)).append("-").append(matcher.group(6));
                /* 3300 */ grp = matcher.group(7);
                /* 3301 */ if (grp != null && !grp.isEmpty()) {
                    /* 3302 */ sb.append(" ").append(grp.trim());
                    /*      */                }
                /*      */
 /* 3305 */ this.stud.setStudentFPhone(sb.toString());
                /* 3306 */ source.setValid(true);
                /*      */            }
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public String validateTransPhoneNumb(String phone) {
        /* 3312 */ String res = "Error: ";
        /* 3313 */ if (phone != null && !phone.isEmpty()) {
            /*      */
 /* 3315 */ phone = phone.trim().replaceAll("  ", " ");
            /*      */
 /* 3317 */ String phonepattern2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
            /* 3318 */ String phoneNumberPattern = "(\\d-)?(\\d{3}-)?\\d{3}-\\d{4}";
            /*      */
 /*      */
 /*      */
 /* 3322 */ String phonepattern = "^([\\+]?)(([1]?)[-\\. ]?)\\(?(\\d{3})\\)?[-\\. ]?(\\d{3,4})[-\\. ]?(\\d{4})([\\. ]?([xX]\\d{1,5})){0,1}$";
            /* 3323 */ Pattern pattern = Pattern.compile(phonepattern);
            /* 3324 */ Matcher matcher = null;
            /*      */
 /* 3326 */ String newphone = phone;
            /* 3327 */ int len = phone.length();
            /* 3328 */ int firstxpos = phone.toUpperCase().indexOf("X");
            /* 3329 */ int lastxpos = phone.toUpperCase().lastIndexOf("X");
            /* 3330 */ int dotpos = phone.indexOf(".");
            /* 3331 */ int blkpos = phone.indexOf(" ");
            /* 3332 */ if (firstxpos < 0) /* 3333 */ {
                if (len < 7 || (len == 7 && (dotpos > 0 || blkpos > 0)) || (len == 8 && dotpos < 0 && blkpos < 0)) {
                    /* 3334 */ FacesMessage facesMessage = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                    /*      */
 /*      */
 /* 3337 */ return res + facesMessage.getSummary();
                    /* 3338 */                }
                if (len == 7 && dotpos < 0 && blkpos < 0) {
                    /* 3339 */ newphone = "951" + phone;
                    /* 3340 */                } else if (len == 8 && ((blkpos > 0 && blkpos == phone.lastIndexOf(" ")) || (dotpos > 0 && dotpos == phone.lastIndexOf(".")))) {
                    /* 3341 */ newphone = "951" + phone;
                    /*      */                }
            } /* 3343 */ else {
                if (firstxpos < 7 || firstxpos != lastxpos || lastxpos == len - 1) {
                    /* 3344 */ FacesMessage facesMessage = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                    /*      */
 /*      */
 /* 3347 */ return res + facesMessage.getSummary();
                    /*      */                }
                /* 3349 */ String subphone = phone.substring(0, firstxpos).trim();
                /* 3350 */ len = subphone.length();
                /* 3351 */ dotpos = subphone.indexOf(".");
                /* 3352 */ blkpos = subphone.indexOf(" ");
                /* 3353 */ if (len < 7 || (len == 7 && (dotpos > 0 || blkpos > 0)) || (len == 8 && dotpos < 0 && blkpos < 0)) {
                    /* 3354 */ FacesMessage facesMessage = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                    /*      */
 /*      */
 /* 3357 */ return res + facesMessage.getSummary();
                    /* 3358 */                }
                if (len == 7 && dotpos < 0 && blkpos < 0) {
                    /* 3359 */ newphone = "951" + phone;
                    /* 3360 */                } else if (len == 8 && ((blkpos > 0 && blkpos == phone.lastIndexOf(" ")) || (dotpos > 0 && dotpos == phone.lastIndexOf(".")))) {
                    /* 3361 */ newphone = "951." + phone;
                    /*      */                }
            }
            /*      */
 /* 3364 */ matcher = pattern.matcher(newphone);
            /* 3365 */ if (!matcher.matches()) {
                /* 3366 */ FacesMessage facesMessage = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                /* 3367 */ if (log == null);
                /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 3373 */ return res + facesMessage.getSummary();
                /*      */            }
            /*      */
 /* 3376 */ matcher.reset();
            /* 3377 */ if (matcher.find()) {
                /* 3378 */ len = matcher.groupCount();
                /*      */            }
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 3385 */ StringBuilder sb = new StringBuilder(32);
            /* 3386 */ String grp = matcher.group(3);
            /* 3387 */ String grp2 = matcher.group(1);
            /* 3388 */ if (grp != null && !grp.isEmpty()) {
                /* 3389 */ if (grp2 != null && !grp2.isEmpty()) {
                    /* 3390 */ sb.append(grp2);
                    /*      */                }
                /* 3392 */ sb.append(grp).append(" ");
                /*      */            }
            /* 3394 */ grp = matcher.group(4);
            /* 3395 */ if (grp != null && !grp.isEmpty()) {
                /* 3396 */ sb.append("(").append(grp).append(") ");
                /*      */            }
            /* 3398 */ sb.append(matcher.group(5)).append("-").append(matcher.group(6));
            /* 3399 */ grp = matcher.group(7);
            /* 3400 */ if (grp != null && !grp.isEmpty()) {
                /* 3401 */ sb.append(" ").append(grp.trim());
                /*      */            }
            /*      */
 /* 3404 */ res = sb.toString();
            /* 3405 */ return res;
            /*      */        }
        /*      */
 /*      */
 /* 3409 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.ReqPhoneNumb");
        /* 3410 */ return res + msg.getSummary();
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public int getCostudents() {
        /* 3416 */ return this.costudents.intValue();
        /*      */    }

    /*      */
 /*      */ public void setCostudents(int costudents) {
        /* 3420 */ this.costudents = Integer.valueOf(costudents);
        /* 3421 */ this.stud.setHomecostudies(Integer.valueOf(costudents));
        /*      */    }

    /*      */
 /*      */ public int getNotestot() {
        /* 3425 */ return this.notestot;
        /*      */    }

    /*      */
 /*      */ public int getShownotes() {
        /* 3429 */ return this.shownotes;
        /*      */    }

    /*      */
 /*      */ public int[] getNotesind() {
        /* 3433 */ return this.notesind;
        /*      */    }

    /*      */
 /*      */ public void setNotesind(int[] notesind) {
        /* 3437 */ this.notesind = notesind;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */    void loadStudInstituteScholarshipAwards() {
        /* 3443 */ String[] scholarships = {this.stud.getStudentAsScholarship1Name(), this.stud.getStudentAvScholarship2Name(), this.stud.getStudentAyScholarship3Name(), this.stud.getStudentBbScholarship4Name(), this.stud.getStudentBeScholarship5Name(), this.stud.getStudentBhScholarship6Name(), this.stud.getStudentBkScholarship7Name(), this.stud.getStudentBnScholarship8Name(), this.stud.getStudentBqScholarship9Name()};
        /*      */
        int value_1 = this.stud.getStudentAuScholarship1Amt() != null ? this.stud.getStudentAuScholarship1Amt().intValue() : 0;
        int value_2 = this.stud.getStudentAxScholarship2Amt() != null ? this.stud.getStudentAxScholarship2Amt().intValue() : 0;
        int value_3 = this.stud.getStudentBaScholarship3Amt() != null ? this.stud.getStudentBaScholarship3Amt().intValue() : 0;
        /*      */ int value_4 = this.stud.getStudentBdScholarship4Amt() != null ? this.stud.getStudentBdScholarship4Amt().intValue() : 0;
        int value_5 = this.stud.getStudentBgScholarship5Amt() != null ? this.stud.getStudentBgScholarship5Amt().intValue() : 0;
        int value_6 = this.stud.getStudentBjScholarship6Amt() != null ? this.stud.getStudentBjScholarship6Amt().intValue() : 0;
        int value_7 = this.stud.getStudentBmScholarship7Amt() != null ? this.stud.getStudentBmScholarship7Amt().intValue() : 0;
        int value_8 = this.stud.getStudentBpScholarship8Amt() != null ? this.stud.getStudentBpScholarship8Amt().intValue() : 0;
        int value_9 = this.stud.getStudentBsScholarship9Amt() != null ? this.stud.getStudentBsScholarship9Amt().intValue() : 0;
        /* 3446 */ int[] amts = {value_1, value_2, value_3, value_4, value_5, value_6, value_7, value_8, value_9};
        /*      */
 /*      */
 /* 3449 */ String[] notes = {this.stud.getStudentAtScholarship1Note(), this.stud.getStudentAwScholarship2Note(), this.stud.getStudentAzScholarship3Note(), this.stud.getStudentBcScholarship4Note(), this.stud.getStudentBfScholarship5Note(), this.stud.getStudentBiScholarship6Note(), this.stud.getStudentBlScholarship7Note(), this.stud.getStudentBoScholarship8Note(), this.stud.getStudentBrScholarship9Note()};
        /*      */
 /*      */
 /* 3452 */ int filled = 0, shows = 0;
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
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 3472 */ for (int i = this.notestot - 1; i > this.shownotes; i--) {
            /* 3473 */ filled = 0;
            /*      */
 /*      */
 /*      */
 /*      */
 /* 3478 */ if (amts[i] > 0) {
                /* 3479 */ filled++;
                /*      */            }
            /* 3481 */ if (notes[i] != null && !notes[i].isEmpty()) {
                /* 3482 */ filled++;
                /*      */            }
            /* 3484 */ if (filled > 0) {
                /* 3485 */ shows = i + 1;
                /*      */ break;
                /*      */            }
            /*      */        }
        /* 3489 */ if (shows > this.shownotes) {
            this.shownotes = shows;
        }
        /* 3490 */ log.info("===== show notes == %d", new Object[]{Integer.valueOf(this.shownotes)});
        /*      */    }

    /*      */
 /*      */ public String ifshowscholar(int i) {
        /* 3494 */ if (i <= this.shownotes) {
            return "block;";
        }
        /* 3495 */ return "none;";
        /*      */    }

    /*      */
 /*      */ public void clickformorescholar(AjaxBehaviorEvent event) {
        /* 3499 */ this.shownotes++;
        /* 3500 */ this.tab_index = 3;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public void std_fafsachanged(AjaxBehaviorEvent event) {
        /* 3506 */ if (this.std_fafsa == true) {
            /* 3507 */ this.std_intl = false;
            /* 3508 */ this.stud.setStudentLIntlStud(this.std_intl ? "Yes" : "No");
            /*      */        }
        /* 3510 */ if (!this.std_fafsa) {
            /* 3511 */ this.std_efc = false;
            /* 3512 */ this.stud.setIndEfc(this.std_efc ? "Yes" : "No");
            /*      */        }
        /*      */
 /*      */
 /* 3516 */ setStudFAFSA(this.std_fafsa ? "Yes" : "No");
        /*      */
 /*      */
 /* 3519 */ this.calc.refreshCalc(this.stud);
        /* 3520 */ this.stdo_fafsa = this.std_fafsa;
        /* 3521 */ this.stdo_efc = this.std_efc;
        /* 3522 */ this.stdo_intl = this.std_intl;
        /*      */    }

    /*      */
 /*      */ public void std_calgrantchanged(AjaxBehaviorEvent event) {
    
        /* 3526 */ if (this.std_calgrant == true) {
            /* 3527 */ this.std_intl = false;
            /* 3528 */ this.stud.setStudentLIntlStud(this.std_intl ? "Yes" : "No");
            /*      */        } else {
            /*      */
 /* 3531 */ this.adjust_calgrantamt_ind = false;
            /*      */        }
        /* 3533 */ this.stud.setStudentZCalgrant(this.std_calgrant ? "Yes" : "No");
        /*      */
 /* 3535 */ this.stud.setAdjCalgrantInd(this.adjust_calgrantamt_ind ? "Yes" : "No");
        /* 3536 */ this.calc.refreshCalc(this.stud);
        /* 3537 */ this.stdo_calgrant = this.std_calgrant;
        /* 3538 */ this.stdo_adjust_calgrantamt_ind = this.adjust_calgrantamt_ind;
        /* 3539 */ this.stdo_intl = this.std_intl;
     
        /*      */    }

    /*      */
 /*      */
 
 
      public void std_pellgrantchanged(AjaxBehaviorEvent event) {
        this.stud.setStudentFPellGrant(this.std_pellGrant ? "Yes" : "No");
        /*      */     //this.stdo_academic = this.stud.getStudentUAcademic();
/* 2469 */ this.calc.refreshCalc(this.stud);
System.out.println("this.calc.getActor().getPellGrant() "+ this.calc.getActor().getPellGrant());
           this.setStd_pellGrant_value(this.calc.getActor().getPellGrant());

    }
 
 /*      */ public void efc_indchanged(AjaxBehaviorEvent event) {
        /* 3544 */ if (!this.std_efc) {
            /* 3545 */ this.stud.setStudentAfFamilyContrib(Integer.valueOf(99999));
            /* 3546 */ log.info("====== should reset efc_amt, now is  %d", new Object[]{this.stud.getStudentAfFamilyContrib()});
            /*      */        }
        /*      */
 /*      */
 /* 3550 */ if (this.std_efc == true) {
            /*      */
 /* 3552 */ this.std_intl = false;
            /*      */
 /* 3554 */ if (!this.std_fafsa) {
                /* 3555 */ this.std_fafsa = true;
                /*      */
 /* 3557 */ setStudFAFSA(this.std_fafsa ? "Yes" : "No");
                /*      */
 /* 3559 */ this.stdo_fafsa = this.std_fafsa;
                /*      */            }
            /*      */
 /* 3562 */ this.stud.setStudentLIntlStud(this.std_intl ? "Yes" : "No");
            /*      */        }
        /*      */
 /*      */
 /* 3566 */ this.stud.setIndEfc(this.std_efc ? "Yes" : "No");
        /* 3567 */ this.calc.refreshCalc(this.stud);
        /* 3568 */ this.stdo_efc = this.std_efc;
        /* 3569 */ this.stdo_intl = this.std_intl;
        /*      */    }

    /*      */
 /*      */ public void efc_amtchanged(AjaxBehaviorEvent event) {
        /* 3573 */ if (!this.std_efc) {
            this.std_efc = true;
        }
        /* 3574 */ this.stud.setIndEfc(this.std_efc ? "Yes" : "No");
                  if(this.stud.getStudentAfFamilyContrib()<-1500)
                  {
                  this.stud.setStudentAfFamilyContrib(0);
                  }
        /* 3575 */ this.calc.refreshCalc(this.stud);
        this.setStd_pellGrant_value(this.calc.getActor().getPellGrant());
        /*      */    }

    /*      */
 /*      */
 /*      */ public boolean hasMsg(String clientId) {
        /* 3580 */ log.info(" ===hasMsg() invoked with param %s, and return %s", new Object[]{clientId, Boolean.valueOf(this.facesContext.getMessages(clientId).hasNext())});
        /* 3581 */ return this.facesContext.getMessages(clientId).hasNext();
        /*      */    }

    /*      */
 /*      */ public boolean hasErrInd(String clientId) {
        /* 3585 */ log.info(" ====== hasErrInd() invoked with param %s, and return %s", new Object[]{clientId, Boolean.valueOf(this.check_inds.containsKey(clientId))});
        /* 3586 */ return this.check_inds.containsKey(clientId);
        /*      */    }

    /*      */
 /*      */ public boolean isIn_subloan() {
        /* 3590 */ return this.in_subloan;
        /*      */    }

    /*      */
 /*      */ public void setIn_subloan(boolean in_subloan) {
        /* 3594 */ this.in_subloan = in_subloan;
        /* 3595 */ this.stud.setStudentApSubLoans(in_subloan ? "yes" : "no");
        /*      */    }

    /*      */
 /*      */ public boolean isIn_unsubloan() {
        /* 3599 */ return this.in_unsubloan;
        /*      */    }

    /*      */
 /*      */ public void setIn_unsubloan(boolean in_unsubloan) {
        /* 3603 */ this.in_unsubloan = in_unsubloan;
        /* 3604 */ this.stud.setStudentAqUnsubLoans(in_unsubloan ? "yes" : "no");
        /*      */    }

    /*      */
 /*      */ public boolean isIn_fws() {
        /* 3608 */ return this.in_fws;
        /*      */    }

    /*      */
 /*      */ public void setIn_fws(boolean in_fws) {
        /* 3612 */ this.in_fws = in_fws;
        /* 3613 */ this.stud.setStudentArFws(in_fws ? "yes" : "no");
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public void gpachanged(AjaxBehaviorEvent event) {
        /* 3619 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */ public void satmathchanged(AjaxBehaviorEvent event) {
        /* 3622 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */ public void satverbchanged(AjaxBehaviorEvent event) {
        /* 3625 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */ public void actchanged(AjaxBehaviorEvent event) {
        /* 3628 */ this.calc.refreshCalc(this.stud);
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
 /*      */
 /*      */ public void merit1changed(AjaxBehaviorEvent event) {
        /* 3646 */ if (this.std_merit1 == true) {
            /* 3647 */ this.std_merit2 = false;
            /* 3648 */ this.std_merit3 = false;
            /* 3649 */ this.std_merits = "MC";
            /*      */        } else {
            /* 3651 */ this.std_merits = "";
            /*      */        }
        /* 3653 */ this.stud.setStudentSMerit(this.std_merits);
        /* 3654 */ this.calc.refreshCalc(this.stud);
        /* 3655 */ this.stdo_merits = this.std_merits;
        /*      */    }

    /*      */
 /*      */ public void merit2changed(AjaxBehaviorEvent event) {
        /* 3659 */ if (this.std_merit2 == true) {
            /* 3660 */ this.std_merit1 = false;
            /* 3661 */ this.std_merit3 = false;
            /* 3662 */ this.std_merits = "MS";
            /*      */        } else {
            /* 3664 */ this.std_merits = "";
            /*      */        }
        /* 3666 */ this.stud.setStudentSMerit(this.std_merits);
        /* 3667 */ this.calc.refreshCalc(this.stud);
        /* 3668 */ this.stdo_merits = this.std_merits;
        /*      */    }

    /*      */
 /*      */ public void merit3changed(AjaxBehaviorEvent event) {
        /* 3672 */ if (this.std_merit3 == true) {
            /* 3673 */ this.std_merit2 = false;
            /* 3674 */ this.std_merit1 = false;
            /* 3675 */ this.std_merits = "MF";
            /*      */        } else {
            /* 3677 */ this.std_merits = "";
            /*      */        }
        /* 3679 */ this.stud.setStudentSMerit(this.std_merits);
        /* 3680 */ this.calc.refreshCalc(this.stud);
        /* 3681 */ this.stdo_merits = this.std_merits;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void ealsuchanged(AjaxBehaviorEvent event) {
        /* 3688 */ this.stud.setStudentAhLsuAllowrance(this.std_ealsu ? "Yes" : "No");
        /* 3689 */ if (!this.std_ealsu) {
            /* 3690 */ this.stud.setEa_lsu_perc(Integer.valueOf(0));
            /*      */        }
        /*      */
 /* 3693 */ this.calc.refreshCalc(this.stud);
        /* 3694 */ this.stdo_ealsu = this.std_ealsu;
        /*      */    }

    /*      */
 /*      */ public void ealsuperc_changed(AjaxBehaviorEvent event) {
        /* 3698 */ if (this.std_ealsu) {
            /* 3699 */ this.calc.refreshCalc(this.stud);
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public void eanonlsuchanged(AjaxBehaviorEvent event) {
        /* 3704 */ this.stud.setStudentAgNonlsuAllowrance(this.std_eanonlsu ? new BigDecimal(1) : new BigDecimal(0));
        /* 3705 */ if (this.std_eanonlsu) {
            /* 3706 */ this.stud.setStudentAiEduAllowPer((new BigDecimal(this.calc.getEaNonLsuPercentageByDorm(this.std_dorm))).divide(new BigDecimal(100)));
            /*      */
 /*      */
 /*      */        } /*      */ else {
            /*      */
 /*      */
 /*      */
 /* 3714 */ this.stud.setStudentAiEduAllowPer(BigDecimal.ZERO);
            /*      */        }
        /* 3716 */ this.calc.refreshCalc(this.stud);
        /* 3717 */ this.stdo_eanonlsu = this.std_eanonlsu;
        /*      */    }

    /*      */
 /*      */ public void noloanschanged(AjaxBehaviorEvent event) {
        /* 3721 */ if (this.std_noloans) {
            /* 3722 */ this.in_subloan = false;
            /* 3723 */ this.in_unsubloan = false;
            /* 3724 */ this.stud.setStudentApSubLoans(this.in_subloan ? "Yes" : "No");
            /* 3725 */ this.stud.setStudentAqUnsubLoans(this.in_unsubloan ? "Yes" : "No");
            /*      */        } else {
            /* 3727 */ this.in_subloan = true;
            /* 3728 */ this.in_unsubloan = true;
            /* 3729 */ this.stud.setStudentApSubLoans(this.in_subloan ? "Yes" : "No");
            /* 3730 */ this.stud.setStudentAqUnsubLoans(this.in_unsubloan ? "Yes" : "No");
            /*      */        }
        /* 3732 */ this.stud.setIndExcloans(this.std_noloans ? "Yes" : "No");
        /* 3733 */ this.calc.refreshCalc(this.stud);
        /* 3734 */ this.stdo_noloans = this.std_noloans;
        /* 3735 */ this.stdo_in_subloan = this.in_subloan;
        /* 3736 */ this.stdo_in_unsubloan = this.in_unsubloan;
        /*      */    }

    /*      */
 /*      */
 /*      */ public void familymemberschanged(AjaxBehaviorEvent event) {
        /* 3741 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */ public void familyincomechanged(AjaxBehaviorEvent event) {
        /* 3744 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */ public void familyassetchanged(AjaxBehaviorEvent event) {
        /* 3747 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ private int user_over_maxamt(int superind, int useramt, int oldamt, int fundmax) {
        /* 3753 */ if (superind > 0 || useramt <= fundmax) {
            return 1;
        }
        /*      */
 /*      */
 /*      */
 /* 3757 */ return (useramt > oldamt) ? 0 : 1;
        /*      */    }

    /*      */
 /*      */ public void lsuawardamtchanged1(AjaxBehaviorEvent event) {
        /* 3761 */ int max = this.ref.fundMatchTopById(this.stud.getFund1id().intValue());
        /* 3762 */ int in = this.stud.getStudentAuScholarship1Amt().intValue();
        /* 3763 */ if (user_over_maxamt(this.login.getCurrentUser().getSuperuser().intValue(), in, this.sfund1amt, max) < 1) {
            /*      */
 /*      */
 /* 3766 */ UIInput source = (UIInput) event.getSource();
            /* 3767 */ source.setValid(false);
            /* 3768 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship1.amtoverflow");
            /* 3769 */ if (this.facesContext != null) {
                this.facesContext.addMessage(null, msg);
            }
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */ return;
            /*      */        }
        /*      */
 /*      */
 /* 3778 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */ public void lsuawardamtchanged2(AjaxBehaviorEvent event) {
        /* 3781 */ int max = this.ref.fundMatchTopById(this.stud.getFund2id().intValue());
        /* 3782 */ int in = this.stud.getStudentAxScholarship2Amt().intValue();
        /* 3783 */ if (user_over_maxamt(this.login.getCurrentUser().getSuperuser().intValue(), in, this.sfund2amt, max) < 1) /*      */ {
            /* 3785 */ if (this.login.getCurrentUser().getSuperuser().intValue() != 0 || in > this.ref.getUniversity_grant_hard_top_limit().intValue()) {
                /*      */
 /*      */
 /*      */
 /* 3789 */ UIInput source = (UIInput) event.getSource();
                /* 3790 */ source.setValid(false);
                /* 3791 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship2.amtoverflow");
                /* 3792 */ if (this.facesContext != null) {
                    this.facesContext.addMessage(null, msg);
                }
                /*      */
 /*      */
 /*      */
 /*      */ return;
                /*      */            }
            /*      */        }
        /*      */
 /*      */
 /* 3801 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void lsuawardamtchanged3(AjaxBehaviorEvent event) {
        /* 3808 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void lsuawardamtchanged4(AjaxBehaviorEvent event) {
        /* 3815 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void lsuawardamtchanged5(AjaxBehaviorEvent event) {
        /* 3826 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void lsuawardamtchanged6(AjaxBehaviorEvent event) {
        /* 3837 */ this.calc.refreshCalc(this.stud);
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
 /*      */ public void lsuawardamtchanged7(AjaxBehaviorEvent event) {
        /* 3849 */ int max = this.ref.fundMatchTopById(this.stud.getFund7id().intValue());
        /* 3850 */ int in = this.stud.getStudentBmScholarship7Amt().intValue();
        /* 3851 */ if (max > 0 && user_over_maxamt(this.login.getCurrentUser().getSuperuser().intValue(), in, this.sfund7amt, max) < 1) {
            /*      */
 /* 3853 */ UIInput source = (UIInput) event.getSource();
            /* 3854 */ source.setValid(false);
            /* 3855 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship7.amtoverflow");
            /* 3856 */ if (this.facesContext != null) {
                this.facesContext.addMessage(null, msg);
            }
            /*      */
 /*      */ return;
            /*      */        }
        /* 3860 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void lsuawardamtchanged8(AjaxBehaviorEvent event) {
        /* 3871 */ int max = this.ref.fundMatchTopById(this.stud.getFund8id().intValue());
        /* 3872 */ int in = this.stud.getStudentBpScholarship8Amt().intValue();
        /* 3873 */ if (max > 0 && user_over_maxamt(this.login.getCurrentUser().getSuperuser().intValue(), in, this.sfund8amt, max) < 1) {
            /*      */
 /* 3875 */ UIInput source = (UIInput) event.getSource();
            /* 3876 */ source.setValid(false);
            /* 3877 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship8.amtoverflow");
            /* 3878 */ if (this.facesContext != null) {
                this.facesContext.addMessage(null, msg);
            }
            /*      */
 /*      */ return;
            /*      */        }
        /* 3882 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void lsuawardamtchanged9(AjaxBehaviorEvent event) {
        /* 3893 */ int max = this.ref.fundMatchTopById(this.stud.getFund9id().intValue());
        /* 3894 */ int in = this.stud.getStudentBsScholarship9Amt().intValue();
        /* 3895 */ if (max > 0 && user_over_maxamt(this.login.getCurrentUser().getSuperuser().intValue(), in, this.sfund9amt, max) < 1) {
            /*      */
 /* 3897 */ UIInput source = (UIInput) event.getSource();
            /* 3898 */ source.setValid(false);
            /* 3899 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship9.amtoverflow");
            /* 3900 */ if (this.facesContext != null) {
                this.facesContext.addMessage(null, msg);
            }
            /*      */
 /*      */ return;
            /*      */        }
        /* 3904 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */
 /*      */ public void lsuawardnoteschanged1(AjaxBehaviorEvent event) {
        /* 3908 */ String notes = this.stud.getStudentAtScholarship1Note();
        /* 3909 */ if (notes != null) {
            /* 3910 */ notes = notes.trim().toUpperCase();
            /* 3911 */ this.stud.setStudentAtScholarship1Note(notes);
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public void lsuawardnoteschanged2(AjaxBehaviorEvent event) {
        /* 3916 */ String notes = this.stud.getStudentAwScholarship2Note();
        /* 3917 */ if (notes != null) {
            /* 3918 */ notes = notes.trim().toUpperCase();
            /* 3919 */ this.stud.setStudentAwScholarship2Note(notes);
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public void lsuawardnoteschanged3(AjaxBehaviorEvent event) {
        /* 3924 */ String notes = this.stud.getStudentAzScholarship3Note();
        /* 3925 */ if (notes != null) {
            /* 3926 */ notes = notes.trim().toUpperCase();
            /* 3927 */ this.stud.setStudentAzScholarship3Note(notes);
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public void lsuawardnoteschanged4(AjaxBehaviorEvent event) {
        /* 3932 */ String notes = this.stud.getStudentBcScholarship4Note();
        /* 3933 */ if (notes != null) {
            /* 3934 */ notes = notes.trim().toUpperCase();
            /* 3935 */ this.stud.setStudentBcScholarship4Note(notes);
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public void lsuawardnoteschanged5(AjaxBehaviorEvent event) {
        /* 3940 */ String notes = this.stud.getStudentBfScholarship5Note();
        /* 3941 */ if (notes != null) {
            /* 3942 */ notes = notes.trim().toUpperCase();
            /* 3943 */ this.stud.setStudentBfScholarship5Note(notes);
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public void lsuawardnoteschanged6(AjaxBehaviorEvent event) {
        /* 3948 */ String notes = this.stud.getStudentBiScholarship6Note();
        /* 3949 */ if (notes != null) {
            /* 3950 */ notes = notes.trim().toUpperCase();
            /* 3951 */ this.stud.setStudentBiScholarship6Note(notes);
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public void lsuawardnoteschanged7(AjaxBehaviorEvent event) {
        /* 3956 */ String notes = this.stud.getStudentBlScholarship7Note();
        /* 3957 */ if (notes != null) {
            /* 3958 */ notes = notes.trim().toUpperCase();
            /* 3959 */ this.stud.setStudentBlScholarship7Note(notes);
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public void lsuawardnoteschanged8(AjaxBehaviorEvent event) {
        /* 3964 */ String notes = this.stud.getStudentBoScholarship8Note();
        /* 3965 */ if (notes != null) {
            /* 3966 */ notes = notes.trim().toUpperCase();
            /* 3967 */ this.stud.setStudentBoScholarship8Note(notes);
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public void lsuawardnoteschanged9(AjaxBehaviorEvent event) {
        /* 3972 */ String notes = this.stud.getStudentBrScholarship9Note();
        /* 3973 */ if (notes != null) {
            /* 3974 */ notes = notes.trim().toUpperCase();
            /* 3975 */ this.stud.setStudentBrScholarship9Note(notes);
            /*      */        }
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void lsuawardnamechanged7(AjaxBehaviorEvent event) {
        /* 3985 */ int f = this.stud.getFund7id().intValue();
        /* 3986 */ this.stdo_fundid7 = Integer.valueOf(f);
        /*      */
 /* 3988 */ if (f > 0) {
            /* 3989 */ this.stud.setStudentBkScholarship7Name(this.ref.getFundDescById(f));
            /*      */        } else {
            /* 3991 */ this.stud.setStudentBkScholarship7Name("");
            /* 3992 */ this.stud.setStudentBmScholarship7Amt(Integer.valueOf(0));
            /*      */        }
        /* 3994 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */ public void lsuawardnamechanged8(AjaxBehaviorEvent event) {
        /* 3997 */ int f = this.stud.getFund8id().intValue();
        /* 3998 */ this.stdo_fundid8 = Integer.valueOf(f);
        /*      */
 /* 4000 */ if (f > 0) {
            /* 4001 */ this.stud.setStudentBnScholarship8Name(this.ref.getFundDescById(f));
            /*      */        } else {
            /* 4003 */ this.stud.setStudentBnScholarship8Name("");
            /* 4004 */ this.stud.setStudentBpScholarship8Amt(Integer.valueOf(0));
            /*      */        }
        /* 4006 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */ public void lsuawardnamechanged9(AjaxBehaviorEvent event) {
        /* 4009 */ int f = this.stud.getFund9id().intValue();
        /* 4010 */ this.stdo_fundid9 = Integer.valueOf(f);
        /*      */
 /* 4012 */ if (f > 0) {
            /* 4013 */ this.stud.setStudentBqScholarship9Name(this.ref.getFundDescById(f));
            /*      */        } else {
            /* 4015 */ this.stud.setStudentBqScholarship9Name("");
            /* 4016 */ this.stud.setStudentBsScholarship9Amt(Integer.valueOf(0));
            /*      */        }
        /* 4018 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void calgrantadjustindchanged(AjaxBehaviorEvent event) {
        /* 4026 */ this.calc.setAdjustCalGrantAmtInd(this.adjust_calgrantamt_ind);
        /* 4027 */ this.stud.setAdjCalgrantInd(this.adjust_calgrantamt_ind ? "Yes" : "No");
        /* 4028 */ this.calc.refreshCalc(this.stud);
        /* 4029 */ this.stdo_adjust_calgrantamt_ind = this.adjust_calgrantamt_ind;
        /*      */    }

    /*      */ public void calgrantaamtchanged(AjaxBehaviorEvent event) {
        /* 4032 */ if (this.adjust_calgrantamt_ind) {
            /* 4033 */ if (this.stud.getStudentAaCalgrantA().intValue() > 0) {
                /* 4034 */ this.stud.setStudentAbCalgrantB(Integer.valueOf(0));
                /*      */            }
            /* 4036 */ this.calc.refreshCalc(this.stud);
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public void calgrantbamtchanged(AjaxBehaviorEvent event) {
        /* 4041 */ if (this.adjust_calgrantamt_ind) {
            /* 4042 */ if (this.stud.getStudentAbCalgrantB().intValue() > 0) {
                /* 4043 */ this.stud.setStudentAaCalgrantA(Integer.valueOf(0));
                /*      */            }
            /* 4045 */ this.calc.refreshCalc(this.stud);
            /*      */        }
        /*      */    }

    /*      */ public void subloanindchanged(AjaxBehaviorEvent event) {
        /* 4049 */ this.stud.setStudentApSubLoans(this.in_subloan ? "Yes" : "No");
        /* 4050 */ this.calc.refreshCalc(this.stud);
        /* 4051 */ this.stdo_in_subloan = this.in_subloan;
        /*      */    }

    /*      */ public void unsubloanindchanged(AjaxBehaviorEvent event) {
        /* 4054 */ this.stud.setStudentAqUnsubLoans(this.in_unsubloan ? "Yes" : "No");
        /* 4055 */ this.calc.refreshCalc(this.stud);
        /* 4056 */ this.stdo_in_unsubloan = this.in_unsubloan;
        /*      */    }

    /*      */ public void fwsindchanged(AjaxBehaviorEvent event) {
        /* 4059 */ this.stud.setStudentArFws(this.in_fws ? "Yes" : "No");
        /* 4060 */ this.calc.refreshCalc(this.stud);
        /* 4061 */ this.stdo_in_fws = this.in_fws;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public boolean isAdjust_calgrantamt_ind() {
        /* 4067 */ return this.adjust_calgrantamt_ind;
        /*      */    }

    /*      */
 /*      */ public void setAdjust_calgrantamt_ind(boolean adjust_calgrantamt_ind) {
        /* 4071 */ this.adjust_calgrantamt_ind = adjust_calgrantamt_ind;
        /*      */    }

    /*      */
 /*      */
 /*      */ private String upper(String str) {
        /* 4076 */ return (str == null) ? null : str.toUpperCase();
        /*      */    }

    /*      */
 /*      */ public void citychanged(AjaxBehaviorEvent event) {
        /* 4080 */ String city = this.stud.getStudentHCity();
        /* 4081 */ if (city != null) {
            /* 4082 */ city = city.trim();
            /* 4083 */ this.stud.setStudentHCity(upper(city));
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public void aptchanged(AjaxBehaviorEvent event) {
        /* 4088 */ String str = this.stud.getHomeAddrApt();
        /* 4089 */ if (str != null) {
            /* 4090 */ str = str.trim();
            /* 4091 */ this.stud.setHomeAddrApt(upper(str));
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public void addrchanged(AjaxBehaviorEvent event) {
        /* 4096 */ String str = this.stud.getStudentGStreet();
        /* 4097 */ if (str != null) {
            /* 4098 */ str = str.trim();
            /* 4099 */ this.stud.setStudentGStreet(upper(str));
            /*      */        }
        /*      */    }

    /*      */ public void countrychanged(AjaxBehaviorEvent event) {
        /* 4103 */ String str = this.stud.getStudentKCountry();
        /* 4104 */ if (str != null) {
            /* 4105 */ str = str.trim();
            /* 4106 */ this.stud.setStudentKCountry(upper(str));
            /*      */        }
        /*      */    }

    /*      */ public void emailchanged(AjaxBehaviorEvent event) {
        /* 4110 */ String str = this.stud.getStudentEEmail();
        /* 4111 */ if (str != null) {
            /* 4112 */ str = str.trim();
            /* 4113 */ this.stud.setStudentEEmail(upper(str));
            /*      */        }
        /*      */    }

    /*      */ public void highschoolchanged(AjaxBehaviorEvent event) {
        /* 4117 */ String str = this.stud.getStudentOLastSchool();
        /* 4118 */ if (str != null) {
            /* 4119 */ str = str.trim();
            /* 4120 */ this.stud.setStudentOLastSchool(upper(str));
            /*      */        }
        /*      */    }

    /*      */ public void majorchanged(AjaxBehaviorEvent event) {
        /* 4124 */ String str = this.stud.getStudentTMajor();
        /* 4125 */ if (str != null) {
            /* 4126 */ str = str.trim();
            /* 4127 */ this.stud.setStudentTMajor(upper(str));
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public void familyinlsuchanged(AjaxBehaviorEvent event) {
        /* 4132 */ String str = this.stud.getStudentVFamily();
        /* 4133 */ if (str != null) {
            /* 4134 */ str = str.trim();
            /* 4135 */ this.stud.setStudentVFamily(upper(str));
            /*      */        }
        /* 4137 */ if (str != null && !str.isEmpty() && this.costudents.intValue() == 0) {
            /* 4138 */ this.costudents = Integer.valueOf(1);
            /*      */        }
        /* 4140 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */ public void noncahelpchanged(AjaxBehaviorEvent event) {
        /* 4143 */ String str = this.stud.getStudentAjHomeState();
        /*      */
 /* 4145 */ if (!this.ref.isEmp(str)) {
            /* 4146 */ str = str.trim();
            /* 4147 */ this.stud.setStudentAjHomeState(upper(str));
            /* 4148 */ this.calc.refreshCalc(this.stud);
            /*      */        } else {
            /* 4150 */ Integer amt = this.stud.getStudentAkNoncalGrant();
            /* 4151 */ if (amt == null || amt.intValue() == 0) {
                /* 4152 */ this.stud.setStudentAjHomeState(null);
                /*      */            } else {
                /* 4154 */ UIInput source = (UIInput) event.getSource();
                /* 4155 */ source.setValid(false);
                /* 4156 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.financial.noncagrant.nonamebutamt");
                /* 4157 */ this.facesContext.addMessage(null, msg);
                /*      */
 /* 4159 */ String msgkey = "EstimateForm.financial.noncagrant.defaultnotes";
                /* 4160 */ this.stud.setStudentAjHomeState(this.ref.getJSFMsgByKey(msgkey));
                /* 4161 */ this.calc.refreshCalc(this.stud);
                /*      */            }
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public void noncahelpamtchanged(AjaxBehaviorEvent event) {
        /* 4167 */ String str = this.stud.getStudentAjHomeState();
        /* 4168 */ Integer amt = this.stud.getStudentAkNoncalGrant();
        /* 4169 */ if (amt != null && amt.intValue() > 0 && this.ref.isEmp(str)) {
            /* 4170 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.financial.noncagrant.nonamebutamt");
            /* 4171 */ this.facesContext.addMessage(null, msg);
            /*      */
 /* 4173 */ String msgkey = "EstimateForm.financial.noncagrant.defaultnotes";
            /* 4174 */ this.stud.setStudentAjHomeState(this.ref.getJSFMsgByKey(msgkey));
            /*      */        }
        /* 4176 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */
 /*      */ public void nonlsuhelpchanged(AjaxBehaviorEvent event) {
        /* 4180 */ String str = this.stud.getStudentAlOutScholarships();
        /* 4181 */ if (str != null) {
            /* 4182 */ str = str.trim();
            /* 4183 */ this.stud.setStudentAlOutScholarships(upper(str));
            /*      */        }
        /*      */    }

    /*      */ public void outsidehelpamtchanged(AjaxBehaviorEvent event) {
        /* 4187 */ this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */
 /*      */ public String getFund1extra() {
        /* 4191 */ return this.fund1extra;
        /*      */    }

    /*      */
 /*      */ public String getFund2extra() {
        /* 4195 */ return this.fund2extra;
        /*      */    }

    /*      */
 /*      */ public String getFund3extra() {
        /* 4199 */ return this.fund3extra;
        /*      */    }

    /*      */
 /*      */ public String getFund4extra() {
        /* 4203 */ return this.fund4extra;
        /*      */    }

    /*      */
 /*      */ public String getFund5extra() {
        /* 4207 */ return this.fund5extra;
        /*      */    }

    /*      */
 /*      */ public String getFund6extra() {
        /* 4211 */ return this.fund6extra;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void dobchanged_org(AjaxBehaviorEvent event) {
        /* 4222 */ if (this.std_indept == true) {
            /* 4223 */ if (this.std_indepts.containsKey("dob")) {
                /* 4224 */ this.std_indepts.remove("dob");
                /* 4225 */ if (this.std_indepts.size() == 0 && this.std_indept_byhand == 0) {
                    this.std_indept = false;
                }
                /*      */            }
            /* 4227 */ this.stud.setStudentYIndept((this.std_indept == true) ? "Yes" : "No");
            /* 4228 */ this.calc.refreshCalc(this.stud);
            /*      */        }
        /*      */
 /* 4231 */ UIInput source = (UIInput) event.getSource();
        /* 4232 */ String clientfid = source.getClientId();
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 4241 */ UIComponent com = event.getComponent();
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 4247 */ source.setValid(false);
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 4254 */ if (this.std_dob == null || this.std_dob.isEmpty()) {
            /*      */ return;
            /*      */        }
        /* 4257 */ if ((this.std_dob = this.std_dob.replaceFirst("^(00+)", "0")).isEmpty()) {
            /*      */
 /* 4259 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NoDoB");
            /* 4260 */ this.facesContext.addMessage(clientfid, msg);
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */        } /*      */ else {
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 4273 */ this.std_dob = this.std_dob.replaceAll("[^\\d]", "");
            /*      */
 /*      */
 /* 4276 */ if (this.std_dob.indexOf("/") < 0) {
                /* 4277 */ Pattern pattern1 = Pattern.compile("^\\d*$");
                /* 4278 */ Matcher matcher1 = pattern1.matcher(this.std_dob);
                /* 4279 */ if (matcher1.matches()) {
                    /*      */
 /* 4281 */ int len = this.std_dob.length();
                    /*      */
 /*      */
 /*      */
 /*      */
 /* 4286 */ char[] dob = this.std_dob.toCharArray();
                    /* 4287 */ switch (len) {
                        case 4:
                            /* 4288 */ this.std_dob = "0" + dob[0] + "/0" + dob[1] + "/19" + dob[2] + dob[3];
                            break;
                        /*      */ case 5:
                            /* 4290 */ if (dob[0] > '1') {
                                this.std_dob = "0" + dob[0] + "/" + dob[1] + dob[2] + "/19" + dob[3] + dob[4];
                                break;
                            }
                            /* 4291 */ if (dob[1] <= '2') {
                                this.std_dob = "" + dob[0] + dob[1] + "/0" + dob[2] + "/19" + dob[3] + dob[4];
                                break;
                            }
                            /* 4292 */ this.std_dob = "0" + dob[0] + "/" + dob[1] + dob[2] + "/19" + dob[3] + dob[4];
                            break;
                        /*      */ case 6:
                            /* 4294 */ if (dob[0] == '0') {
                                /* 4295 */ this.std_dob = "" + dob[0] + dob[1] + "/" + dob[2] + dob[3] + "/19" + dob[4] + dob[5];
                                break;
                                /* 4296 */                            }
                            if (dob[0] == '1') {
                                /* 4297 */ if (dob[1] >= '3') {
                                    this.std_dob = "0" + dob[0] + "/0" + dob[1] + "/" + dob[2] + dob[3] + dob[4] + dob[5];
                                    break;
                                }
                                /* 4298 */ this.std_dob = "" + dob[0] + dob[1] + "/" + dob[2] + dob[3] + "/19" + dob[4] + dob[5];
                                break;
                                /* 4299 */                            }
                            if (dob[0] > '1') /* 4300 */ {
                                this.std_dob = "0" + dob[0] + "/0" + dob[1] + "/" + dob[2] + dob[3] + dob[4] + dob[5];
                            }
                            /*      */ break;
                        /*      */ case 7:
                            /* 4303 */ if (dob[0] > '1') {
                                this.std_dob = "0" + dob[0] + "/" + dob[1] + dob[2] + "/" + dob[3] + dob[4] + dob[5] + dob[6];
                                break;
                            }
                            /* 4304 */ if (dob[1] >= '3') {
                                /* 4305 */ this.std_dob = "0" + dob[0] + "/" + dob[1] + dob[2] + "/" + dob[3] + dob[4] + dob[5] + dob[6];
                                break;
                                /* 4306 */                            }
                            this.std_dob = "" + dob[0] + dob[1] + "/0" + dob[2] + "/" + dob[3] + dob[4] + dob[5] + dob[6];
                            break;
                        /*      */ case 8:
                            /* 4308 */ this.std_dob = "" + dob[0] + dob[1] + "/" + dob[2] + dob[3] + "/" + dob[4] + dob[5] + dob[6] + dob[7];
                            /*      */ break;
                    }
                    /*      */
 /*      */
 /*      */
 /*      */
 /*      */                }
                /*      */            }
            /* 4316 */ Pattern pattern = Pattern.compile("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)?\\d\\d)");
            /* 4317 */ Matcher matcher = pattern.matcher(this.std_dob);
            /* 4318 */ if (!matcher.matches()) {
                /* 4319 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NoDoB");
                /* 4320 */ this.facesContext.addMessage(clientfid, msg);
                /*      */
 /*      */ return;
                /*      */            }
            /* 4324 */ matcher.reset();
            /* 4325 */ if (matcher.find()) {
                /* 4326 */ String day = matcher.group(2);
                /* 4327 */ if (day.length() == 1) {
                    day = "0" + day;
                }
                /*      */
 /* 4329 */ String month = matcher.group(1);
                /* 4330 */ if (month.length() == 1) {
                    month = "0" + month;
                }
                /*      */
 /* 4332 */ int year = Integer.parseInt(matcher.group(3));
                /* 4333 */ if (year < 100) {
                    /* 4334 */ year += (year > 30) ? 1900 : 2000;
                    /*      */                }
                /*      */
 /*      */
 /* 4338 */ this.std_dob = month + "/" + day + "/" + year;
                /*      */            }
            /*      */
 /*      */
 /*      */
 /* 4343 */ DateTime dt = this.ref.parseDateStr(this.std_dob, this.ref.getDateInputShowStr());
            /* 4344 */ if (dt == null) {
                /* 4345 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidDoB");
                /* 4346 */ this.facesContext.addMessage(clientfid, msg);
                /*      */
 /*      */ return;
                /*      */            }
            /*      */
 /* 4351 */ DateMidnight dm = new DateMidnight(dt);
            /* 4352 */ DateMidnight low = new DateMidnight(this.ref.getFiscal_year() - this.ref.getApp_young_limit(), 1, 1);
            /* 4353 */ DateMidnight high = new DateMidnight(this.ref.getFiscal_year() - this.ref.getApp_old_limit(), 1, 1);
            /*      */
 /* 4355 */ if (dm.isAfter((ReadableInstant) low) || dm.isBefore((ReadableInstant) high)) {
                /* 4356 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NotSuitableDoB");
                /* 4357 */ this.facesContext.addMessage(clientfid, msg);
                /*      */
 /*      */ return;
                /*      */            }
            /* 4361 */ DateMidnight due = new DateMidnight(this.ref.getIndept_dob());
            /*      */
 /*      */
 /* 4364 */ if (!dm.isAfter((ReadableInstant) due)) {
                /* 4365 */ if (!this.std_indepts.containsKey("dob")) {
                    /* 4366 */ this.std_indepts.put("dob", "dob");
                    /*      */                }
                /* 4368 */ this.std_indept = true;
                /*      */            }
            /*      */
 /*      */
 /* 4372 */ this.stud.setStudentDDob(this.std_dob);
            /*      */
 /* 4374 */ source.setValid(true);
            /*      */        }
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void chgedid(AjaxBehaviorEvent event) {
        /* 4382 */ UIInput source = (UIInput) event.getSource();
        /* 4383 */ String clientfid = source.getClientId();
        /*      */
 /* 4385 */ String lsuid = this.stud.getStudentALsuid();
        /* 4386 */ int flag = validateLsuId(lsuid);
        /* 4387 */ if (flag >= 0) /*      */ {
            /* 4389 */ if (flag <= 0) {
                /*      */
 /*      */
 /* 4392 */ source.setValid(false);
                /* 4393 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidLSUID");
                /* 4394 */ this.facesContext.addMessage(clientfid, msg);
                /*      */            }
        }
        /* 4396 */ mkPops(flag, "id");
        /*      */    }

    /*      */
 /*      */ public int checkInvalidFilePathName(String strname) {
        /* 4400 */ int invalid = 0;
        /* 4401 */ char[] invalids = {'/', '\\', '"', '?', '%', '|', '*', '<', '>', '\''};
        /* 4402 */ if (strname != null && strname.length() > 0) {
            /* 4403 */ for (char c : invalids) {
                /* 4404 */ if (strname.indexOf(c) > -1) {
                    /* 4405 */ invalid = 1;
                    /*      */ break;
                    /*      */                }
                /*      */            }
            /*      */        }
        /* 4410 */ return invalid;
        /*      */    }

    /*      */
 /*      */
 /*      */ public void lnamechanged(AjaxBehaviorEvent event) {
        /* 4415 */ String ln = this.std_ln;
        /*      */
 /*      */
 /* 4418 */ if (!this.ref.isEmp(ln)) {
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
 /* 4431 */ ln = upper(ln).trim();
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
 /*      */
 /* 4447 */ int invalid = checkInvalidFilePathName(ln);
            /*      */
 /* 4449 */ if (invalid > 0) {
                /* 4450 */ this.stud.setStudentBLastname("");
                /* 4451 */ UIInput source = (UIInput) event.getSource();
                /* 4452 */ String clientfid = source.getClientId();
                /* 4453 */ source.setValid(false);
                /*      */            } else {
                /* 4455 */ this.stud.setStudentBLastname(ln);
                /*      */            }
            /* 4457 */ this.std_ln = ln;
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */        } /*      */ else {
            /*      */
 /*      */
 /*      */
 /*      */
 /* 4468 */ this.stud.setStudentBLastname("");
            /* 4469 */ UIInput source = (UIInput) event.getSource();
            /* 4470 */ String clientfid = source.getClientId();
            /* 4471 */ source.setValid(false);
            /*      */        }
        /*      */
 /*      */
 /*      */
 /*      */
 /* 4477 */ mkPops(validateLn(ln), "ln");
        /*      */    }

    /*      */
 /*      */ public void fnamechanged(AjaxBehaviorEvent event) {
        /* 4481 */ String fn = this.std_fn;
        /*      */
 /*      */
 /* 4484 */ if (!this.ref.isEmp(fn)) {
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
 /* 4498 */ fn = upper(fn).trim();
            /*      */
 /* 4500 */ int invalid = checkInvalidFilePathName(fn);
            /*      */
 /* 4502 */ if (invalid > 0) {
                /* 4503 */ this.stud.setStudentCFirstname("");
                /* 4504 */ UIInput source = (UIInput) event.getSource();
                /* 4505 */ String clientfid = source.getClientId();
                /* 4506 */ source.setValid(false);
                /*      */            } else {
                /* 4508 */ this.stud.setStudentCFirstname(fn);
                /*      */            }
            /* 4510 */ this.std_fn = fn;
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */        } /*      */ else {
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 4522 */ this.stud.setStudentCFirstname("");
            /* 4523 */ UIInput source = (UIInput) event.getSource();
            /* 4524 */ String clientfid = source.getClientId();
            /* 4525 */ source.setValid(false);
            /*      */        }
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 4532 */ mkPops(validateFn(fn), "fn");
        /*      */    }

    /*      */ private int validateLsuId(String lsuid) {
        /* 4535 */ String longid = lsuid;
        /* 4536 */ int res = 0;
        /* 4537 */ if (lsuid == null) {
            return -1;
        }
        /* 4538 */ lsuid = lsuid.trim();
        /* 4539 */ int len = lsuid.length();
        /* 4540 */ if (len == 0) {
            /* 4541 */ this.stud.setStudentALsuid(null);
            /* 4542 */ return -1;
            /*      */        }
        /* 4544 */ if (len < 6) /*      */ {
            /* 4546 */ return res;
            /*      */        }
        /*      */ try {
            /* 4549 */ len = Integer.parseInt(lsuid);
            /* 4550 */ if (!lsuid.equals(String.valueOf(Integer.parseInt(lsuid)))) /*      */ {
                /* 4552 */ return res;
                /*      */            }
            /* 4554 */        } catch (Exception e) {
            /*      */
 /* 4556 */ return res;
            /*      */        }
        /* 4558 */ return 1;
        /*      */    }

    /*      */
 /*      */ private int validateLn(String ln) {
        /* 4562 */ if (this.ref.isEmp(ln)) {
            return -1;
        }
        /* 4563 */ String str = ln;
        /*      */
 /* 4565 */ int len = str.length();
        /* 4566 */ if (len == 0) {
            /* 4567 */ return -1;
            /*      */        }
        /*      */
 /* 4570 */ int found = (str.indexOf("%") >= 0) ? 1 : ((0 + str.indexOf("_") >= 0) ? 1 : 0);
        /* 4571 */ if (found > 0) /*      */ {
            /* 4573 */ return 1;
            /*      */        }
        /* 4575 */ return 0;
        /*      */    }

    /*      */
 /*      */
 /*      */ private int validateFn(String fn) {
        /* 4580 */ if (this.ref.isEmp(fn)) {
            return -1;
        }
        /* 4581 */ String str = fn;
        /*      */
 /* 4583 */ int len = str.length();
        /* 4584 */ if (len == 0) {
            /* 4585 */ return -1;
            /*      */        }
        /*      */
 /* 4588 */ int found = (str.indexOf("%") >= 0) ? 1 : ((0 + str.indexOf("_") >= 0) ? 1 : 0);
        /* 4589 */ if (found > 0) /*      */ {
            /* 4591 */ return 1;
            /*      */        }
        /* 4593 */ return 0;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void dobchanged(AjaxBehaviorEvent event) {
        /* 4602 */ if (this.std_indept == true) {
            /* 4603 */ if (this.std_indepts.containsKey("dob")) {
                /* 4604 */ this.std_indepts.remove("dob");
                /* 4605 */ if (this.std_indepts.size() == 0 && this.std_indept_byhand == 0) {
                    this.std_indept = false;
                }
                /*      */            }
            /* 4607 */ this.stud.setStudentYIndept((this.std_indept == true) ? "Yes" : "No");
            /* 4608 */ this.calc.refreshCalc(this.stud);
            /*      */        }
        /*      */
 /* 4611 */ UIInput source = (UIInput) event.getSource();
        /* 4612 */ String clientfid = source.getClientId();
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 4621 */ UIComponent com = event.getComponent();
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
 /* 4636 */ this.std_dob = this.std_dob.replaceAll("[^\\d]", "");
        /*      */
 /*      */
 /* 4639 */ int chk = validateDobStr(this.std_dob);
        /*      */
 /* 4641 */ if (chk == 0) {
            /* 4642 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidDoB");
            /* 4643 */ this.facesContext.addMessage(clientfid, msg);
            /* 4644 */        } else if (chk != -1 && chk != -2) {
            /*      */
 /*      */
 /* 4647 */ if (chk == 2) {
                /* 4648 */ FacesMessage msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NotSuitableDoB");
                /* 4649 */ this.facesContext.addMessage(clientfid, msg);
                /*      */            }
            /*      */        }
        /*      */
 /* 4653 */ if (chk == 1) {
            /* 4654 */ DateTime dt = this.ref.parseDateStr(this.std_dob, this.ref.getDateInputShowStr());
            /* 4655 */ DateMidnight dm = new DateMidnight(dt);
            /* 4656 */ DateMidnight due = new DateMidnight(this.ref.getIndept_dob());
            /* 4657 */ if (dm.isBefore((ReadableInstant) due)) {
                /* 4658 */ if (!this.std_indepts.containsKey("dob")) {
                    /* 4659 */ this.std_indepts.put("dob", "dob");
                    /*      */                }
                /* 4661 */ this.std_indept = true;
                /*      */            }
            /*      */
 /*      */
 /* 4665 */ this.stud.setStudentDDob(this.std_dob);
            /*      */        }
        /*      */
 /*      */
 /*      */
 /* 4670 */ mkPops(chk, "dob");
        /*      */    }

    /*      */
 /*      */ private int validateDobStr(String dobstr) {
        /* 4674 */ String strdob = dobstr;
        /*      */
 /* 4676 */ if (strdob == null || strdob.isEmpty()) /*      */ {
            /* 4678 */ return -1;
        }
        /* 4679 */ if ((strdob = strdob.replaceFirst("^(00+)", "0")).isEmpty()) {
            /* 4680 */ return -2;
            /*      */        }
        /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 4690 */ strdob = strdob.trim();
        /*      */
 /* 4692 */ if (strdob.indexOf("/") < 0) {
            /* 4693 */ Pattern pattern1 = Pattern.compile("^\\d*$");
            /* 4694 */ Matcher matcher1 = pattern1.matcher(strdob);
            /* 4695 */ if (matcher1.matches()) {
                /*      */
 /* 4697 */ int len = strdob.length();
                /*      */
 /*      */
 /*      */
 /*      */
 /* 4702 */ char[] dob = strdob.toCharArray();
                /* 4703 */ switch (len) {
                    case 4:
                        /* 4704 */ strdob = "0" + dob[0] + "/0" + dob[1] + "/19" + dob[2] + dob[3];
                        break;
                    /*      */ case 5:
                        /* 4706 */ if (dob[0] > '1') {
                            strdob = "0" + dob[0] + "/" + dob[1] + dob[2] + "/19" + dob[3] + dob[4];
                            break;
                        }
                        /* 4707 */ if (dob[1] <= '2') {
                            strdob = "" + dob[0] + dob[1] + "/0" + dob[2] + "/19" + dob[3] + dob[4];
                            break;
                        }
                        /* 4708 */ strdob = "0" + dob[0] + "/" + dob[1] + dob[2] + "/19" + dob[3] + dob[4];
                        break;
                    /*      */ case 6:
                        /* 4710 */ if (dob[0] == '0') {
                            /* 4711 */ strdob = "" + dob[0] + dob[1] + "/" + dob[2] + dob[3] + "/19" + dob[4] + dob[5];
                            break;
                            /* 4712 */                        }
                        if (dob[0] == '1') {
                            /* 4713 */ if (dob[1] >= '3') {
                                strdob = "0" + dob[0] + "/0" + dob[1] + "/" + dob[2] + dob[3] + dob[4] + dob[5];
                                break;
                            }
                            /* 4714 */ strdob = "" + dob[0] + dob[1] + "/" + dob[2] + dob[3] + "/19" + dob[4] + dob[5];
                            break;
                            /* 4715 */                        }
                        if (dob[0] > '1') /* 4716 */ {
                            strdob = "0" + dob[0] + "/0" + dob[1] + "/" + dob[2] + dob[3] + dob[4] + dob[5];
                        }
                        /*      */ break;
                    /*      */ case 7:
                        /* 4719 */ if (dob[0] > '1') {
                            strdob = "0" + dob[0] + "/" + dob[1] + dob[2] + "/" + dob[3] + dob[4] + dob[5] + dob[6];
                            break;
                        }
                        /* 4720 */ if (dob[1] >= '3') {
                            /* 4721 */ strdob = "0" + dob[0] + "/" + dob[1] + dob[2] + "/" + dob[3] + dob[4] + dob[5] + dob[6];
                            break;
                            /* 4722 */                        }
                        strdob = "" + dob[0] + dob[1] + "/0" + dob[2] + "/" + dob[3] + dob[4] + dob[5] + dob[6];
                        break;
                    /*      */ case 8:
                        /* 4724 */ strdob = "" + dob[0] + dob[1] + "/" + dob[2] + dob[3] + "/" + dob[4] + dob[5] + dob[6] + dob[7];
                        /*      */ break;
                }
                /*      */
 /*      */
 /*      */
 /*      */
 /*      */            }
            /*      */        }
        /* 4732 */ Pattern pattern = Pattern.compile("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)?\\d\\d)");
        /* 4733 */ Matcher matcher = pattern.matcher(strdob);
        /* 4734 */ if (!matcher.matches()) /*      */ {
            /* 4736 */ return 0;
            /*      */        }
        /* 4738 */ matcher.reset();
        /* 4739 */ if (matcher.find()) {
            /* 4740 */ String day = matcher.group(2);
            /* 4741 */ if (day.length() == 1) {
                day = "0" + day;
            }
            /*      */
 /* 4743 */ String month = matcher.group(1);
            /* 4744 */ if (month.length() == 1) {
                month = "0" + month;
            }
            /*      */
 /* 4746 */ int year = Integer.parseInt(matcher.group(3));
            /* 4747 */ if (year < 100) {
                /* 4748 */ year += (year > 30) ? 1900 : 2000;
                /*      */            }
            /*      */
 /*      */
 /* 4752 */ this.std_dob = month + "/" + day + "/" + year;
            /*      */        }
        /*      */
 /*      */
 /*      */
 /* 4757 */ DateTime dt = this.ref.parseDateStr(strdob, this.ref.getDateInputShowStr());
        /* 4758 */ if (dt == null) /*      */ {
            /* 4760 */ return 0;
            /*      */        }
        /* 4762 */ DateMidnight dm = new DateMidnight(dt);
        /* 4763 */ DateMidnight low = new DateMidnight(this.ref.getFiscal_year() - this.ref.getApp_young_limit(), 1, 1);
        /* 4764 */ DateMidnight high = new DateMidnight(this.ref.getFiscal_year() - this.ref.getApp_old_limit(), 1, 1);
        /* 4765 */ if (dm.isAfter((ReadableInstant) low) || dm.isBefore((ReadableInstant) high)) {
            /* 4766 */ return 2;
            /*      */        }
        /*      */
 /* 4769 */ this.std_dob = strdob;
        /* 4770 */ return 1;
        /*      */    }

    /*      */
 /*      */
 /*      */ private void mkPops(int validInd, String src) {
        /* 4775 */ this.chosenPopStud = null;
        /* 4776 */ log.info("$$$$$$$$$$$$$$$$$$ change event handler got field=[%s] , ind=[%d]", new Object[]{src, Integer.valueOf(validInd)});
        /*      */
 /* 4778 */ if ((src.equals("id") && validInd == 0) || (src.equals("dob") && (validInd == 0 || validInd == 2))) {
            /*      */
 /* 4780 */ this.popind = "0";
            /*      */
 /*      */ return;
            /*      */        }
        /* 4784 */ int chkId = 2, go = 0, chkLn = 2, chkFn = 2, chkDob = 3;
        /* 4785 */ switch (src) {
            case "ln":
                /* 4786 */ chkLn = validInd;
                go++;
            /* 4787 */ case "fn":
                chkFn = (chkLn == 2) ? validInd : 2;
                go++;
            /* 4788 */ case "id":
                if (go == 0) {
                    chkId = validInd;
                }
                chkDob = validateDobStr(this.std_dob);
                if (go == 0) /* 4789 */ {
                    break;
                }
            case "dob":
                if (chkDob == 3) {
                    chkDob = validInd;
                    chkId = validateLsuId(this.stud.getStudentALsuid());
                }
                /*      */
 /*      */ break;
        }
        /*      */
 /*      */
 /* 4794 */ if (chkLn > 1) {
            chkLn = validateLn(this.std_ln);
        }
        /* 4795 */ if (chkFn > 1) {
            chkFn = validateFn(this.std_fn);
        }
        /* 4796 */ if (chkId > 1) {
            chkId = validateLsuId(this.stud.getStudentALsuid());
        }
        /*      */
 /* 4798 */ if (chkId == 0 || chkDob == 0 || chkDob == 2) {
            /* 4799 */ this.popind = "0";
            /*      */
 /*      */ return;
            /*      */        }
        /* 4803 */ if (chkId < 0 && chkDob < 0 && chkLn < 0 && chkFn < 0) {
            /* 4804 */ this.popind = "0";
            /*      */
 /*      */
 /*      */ return;
            /*      */        }
        /*      */
 /*      */
 /* 4811 */ String key = genPopKey();
        /* 4812 */ log.info("$$$$$$$$$$$$$$$$$$ change event handler got key=%s, while keptkey=%s", new Object[]{key, this.popkey});
        /* 4813 */ if (key.equals(this.popkey) && this.popups != null && this.popups.size() > 0) {
            /*      */
 /*      */
 /*      */
 /* 4817 */ String str = this.stud.getRecid();
            /* 4818 */ List<Student> diffs = new ArrayList<>();
            /* 4819 */ for (Student one : this.popups) {
                /* 4820 */ if (one.getRecid().equals(str)) {
                    /*      */ continue;
                    /*      */                }
                /* 4823 */ diffs.add(one);
                /*      */            }
            /*      */
 /* 4826 */ if (diffs.size() > 0) {
                /* 4827 */ this.popind = "1";
                /* 4828 */ this.popups = diffs;
                /*      */
 /* 4830 */ this.popDataModel = new QueryStudModel(this.popups);
                /* 4831 */ log.info("$$$$$$$$$$$$$$$$$$ change event handler returns since key matches: %s. popind=%s, popups size=%d", new Object[]{key, this.popind, Integer.valueOf(this.popups.size())});
                /*      */
 /*      */
 /*      */
 /*      */
 /*      */ return;
                /*      */            }
            /*      */        }
        /*      */
 /*      */
 /*      */
 /* 4842 */ String ln = (chkLn < 0) ? "%" : this.std_ln.toUpperCase();
        /* 4843 */ String fn = (chkFn < 0) ? "%" : this.std_fn.toUpperCase();
        /* 4844 */ String dob = (chkDob < 0) ? "%" : this.std_dob;
        /*      */
 /* 4846 */ List<Student> pickups = null;
        /* 4847 */ String onerec = this.stud.getRecid();
        /* 4848 */ if (chkId > 0) {
            /* 4849 */ if (onerec != null) /*      */ {
                /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 4856 */ pickups = this.em.createNamedQuery("Student.findPOPWIDwrecid").setParameter("lsuid", this.stud.getStudentALsuid()).setParameter("ln", ln).setParameter("fn", fn).setParameter("dob", dob).setParameter("recid", onerec).getResultList();
                /*      */
 /*      */
 /*      */            } /*      */ else /*      */ {
                /*      */
 /* 4863 */ pickups = this.em.createNamedQuery("Student.findPOPWID").setParameter("lsuid", this.stud.getStudentALsuid()).setParameter("ln", ln).setParameter("fn", fn).setParameter("dob", dob).getResultList();
                /*      */            }
            /*      */
 /*      */        } /* 4867 */ else if (onerec != null) {
            /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /* 4873 */ pickups = this.em.createNamedQuery("Student.findPOPWOIDwrecid").setParameter("ln", ln).setParameter("fn", fn).setParameter("dob", dob).setParameter("recid", onerec).getResultList();
            /*      */
 /*      */        } /*      */ else {
            /*      */
 /*      */
 /* 4879 */ pickups = this.em.createNamedQuery("Student.findPOPWOID").setParameter("ln", ln).setParameter("fn", fn).setParameter("dob", dob).getResultList();
            /*      */        }
        /*      */
 /*      */
 /* 4883 */ if (this.popups != null) {
            /* 4884 */ this.popups.clear();
            /*      */        }
        /* 4886 */ this.popups = pickups;
        /* 4887 */ this.popkey = key;
        /*      */
 /* 4889 */ if (this.popups == null) {
            this.popups = new ArrayList<>();
        }
        /* 4890 */ this.popDataModel = new QueryStudModel(this.popups);
        /* 4891 */ if (pickups == null || pickups.size() == 0) {
            /* 4892 */ this.popind = "0";
            /*      */        } else {
            /* 4894 */ this.popind = "1";
            /*      */        }
        /* 4896 */ log.info("$$$$$$$$$$$$$$$$$$ change event handler made quey/match, pop amt=%d, marked popind=%s, while key=%s. filter RECID=%s", new Object[]{Integer.valueOf(this.popups.size()), this.popind, this.popkey, this.stud.getRecid()});
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ private String genPopKey() {
        /* 4905 */ StringBuilder sb = new StringBuilder(128);
        /*      */
 /*      */
 /* 4908 */ sb.append(this.ref.isEmp(this.stud.getStudentALsuid()) ? "" : this.stud.getStudentALsuid()).append("|").append(this.ref.isEmp(this.std_ln) ? "" : this.std_ln.toUpperCase()).append("|");
        /* 4909 */ sb.append(this.ref.isEmp(this.std_fn) ? "" : this.std_fn.toUpperCase()).append("|").append(this.ref.isEmp(this.std_dob) ? "" : this.std_dob);
        /* 4910 */ return sb.toString();
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */ public void pickpop(SelectEvent event) {
        /* 4916 */ Student pickup = (Student) event.getObject();
        /* 4917 */ log.info("$$$$$$$$$$$$$$$$$$ pickpop() got student recid=%s", new Object[]{(pickup == null) ? "NULL" : pickup.getRecid()});
        /* 4918 */ modEstimate(pickup);
        /*      */
 /*      */
 /* 4921 */ this.popind = "0";
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String getPopind() {
        /* 4928 */ return this.popind;
        /*      */    }

    /*      */
 /*      */ public void setPopind(String popind) {
        /* 4932 */ this.popind = popind;
        /*      */    }

    /*      */
 /*      */ public QueryStudModel getPopDataModel() {
        /* 4936 */ return this.popDataModel;
        /*      */    }

    /*      */
 /*      */ public void setPopDataModel(QueryStudModel popDataModel) {
        /* 4940 */ this.popDataModel = popDataModel;
        /*      */    }

    /*      */
 /*      */ public Student getChosenPopStud() {
        /* 4944 */ return this.chosenPopStud;
        /*      */    }

    /*      */
 /*      */ public void setChosenPopStud(Student chosenPopStud) {
        /* 4948 */ this.chosenPopStud = chosenPopStud;
        /*      */    }

    /*      */
 /*      */ public List<Student> getPopups() {
        /* 4952 */ return this.popups;
        /*      */    }

    /*      */
 /*      */ public void setPopups(List<Student> popups) {
        /* 4956 */ this.popups = popups;
        /*      */    }

    /*      */
 /*      */ public String getStd_ln() {
        /* 4960 */ return this.std_ln;
        /*      */    }

    /*      */
 /*      */ public void setStd_ln(String std_ln) {
        /* 4964 */ this.std_ln = std_ln;
        /*      */    }

    /*      */
 /*      */ public String getStd_fn() {
        /* 4968 */ return this.std_fn;
        /*      */    }

    /*      */
 /*      */ public void setStd_fn(String std_fn) {
        /* 4972 */ this.std_fn = std_fn;
        /*      */    }

    /*      */
 /*      */ public String sumrow(int init) {
        /* 4976 */ this.tbodyrow = init - 1;
        /* 4977 */ return sumrow();
        /*      */    }

    /*      */ public String sumrow() {
        /* 4980 */ this.tbodyrow++;
        /* 4981 */ return (this.tbodyrow % 2 == 0) ? "evenrow" : "oddrow";
        /*      */    }

    /*      */
 /*      */ public void chgpubnotes(AjaxBehaviorEvent event) {
        /* 4985 */ String notes = this.stud.getStudentAnPubNotes();
        /* 4986 */ if (notes != null) {
            /* 4987 */ notes = notes.trim();
            /* 4988 */ this.stud.setStudentAnPubNotes(notes);
            /*      */        }
        /*      */    }

    /*      */ public void chgprinotes(AjaxBehaviorEvent event) {
        /* 4992 */ String notes = this.stud.getStudentAoPriNotes();
        /* 4993 */ if (notes != null) {
            /* 4994 */ notes = notes.trim();
            /* 4995 */ this.stud.setStudentAoPriNotes(notes);
            /*      */        }
        /*      */    }

    /*      */
 /*      */ public void chgsex(AjaxBehaviorEvent event) {
        /* 5000 */ this.stdo_sex = this.std_sex;
        /* 5001 */ this.stud.setSex(this.std_sex);
        /*      */    }

    /*      */ public void chgsex1(AjaxBehaviorEvent event) {
        /* 5004 */ if (this.std_sex1 == true) {
            /* 5005 */ this.std_sex = "F";
            /* 5006 */ this.std_sex2 = false;
            /* 5007 */ this.std_sex3 = false;
            /* 5008 */        } else if (!this.std_sex2 && !this.std_sex3) {
            /* 5009 */ this.std_sex3 = true;
            /* 5010 */ this.std_sex = "N";
            /*      */        }
        /* 5012 */ this.stdo_sex = this.std_sex;
        /* 5013 */ this.stud.setSex(this.std_sex);
        /*      */    }

    /*      */ public void chgsex2(AjaxBehaviorEvent event) {
        /* 5016 */ if (this.std_sex2 == true) {
            /* 5017 */ this.std_sex = "M";
            /* 5018 */ this.std_sex1 = false;
            /* 5019 */ this.std_sex3 = false;
            /* 5020 */        } else if (!this.std_sex1 && !this.std_sex3) {
            /* 5021 */ this.std_sex3 = true;
            /* 5022 */ this.std_sex = "N";
            /*      */        }
        /* 5024 */ this.stdo_sex = this.std_sex;
        /* 5025 */ this.stud.setSex(this.std_sex);
        /*      */    }

    /*      */ public void chgsex3(AjaxBehaviorEvent event) {
        /* 5028 */ if (this.std_sex3 == true) {
            /* 5029 */ this.std_sex = "N";
            /* 5030 */ this.std_sex2 = false;
            /* 5031 */ this.std_sex1 = false;
            /* 5032 */        } else if (!this.std_sex2 && !this.std_sex1) {
            /* 5033 */ this.std_sex3 = true;
            /* 5034 */ this.std_sex = "N";
            /*      */        }
        /* 5036 */ this.stdo_sex = this.std_sex;
        /* 5037 */ this.stud.setSex(this.std_sex);
        /*      */    }

    /*      */
 /*      */ public HashMap<String, String> getStdo_indepts() {
        /* 5041 */ return this.stdo_indepts;
        /*      */    }

    /*      */
 /*      */ public Integer getStdo_fundid7() {
        /* 5045 */ return this.stdo_fundid7;
        /*      */    }

    /*      */
 /*      */ public Integer getStdo_fundid8() {
        /* 5049 */ return this.stdo_fundid8;
        /*      */    }

    /*      */
 /*      */ public Integer getStdo_fundid9() {
        /* 5053 */ return this.stdo_fundid9;
        /*      */    }

    /*      */
 /*      */ public String getStdo_sex() {
        /* 5057 */ return this.stdo_sex;
        /*      */    }

    /*      */
 /*      */ public boolean isStdo_intl() {
        /* 5061 */ return this.stdo_intl;
        /*      */    }

    /*      */
 /*      */ public boolean isStdo_marry() {
        /* 5065 */ return this.stdo_marry;
        /*      */    }

    /*      */
 /*      */ public boolean isStdo_sda() {
        /* 5069 */ return this.stdo_sda;
        /*      */    }

    /*      */
 /*      */ public boolean isStdo_dorm() {
        /* 5073 */ return this.stdo_dorm;
        /*      */    }

    /*      */
 /*      */ public boolean isStdo_indept() {
        /* 5077 */ return this.stdo_indept;
        /*      */    }

    /*      */
 /*      */ public boolean isStdo_fafsa() {
        /* 5081 */ return this.stdo_fafsa;
        /*      */    }

    /*      */
 /*      */ public boolean isStdo_calgrant() {
        /* 5085 */ return this.stdo_calgrant;
        /*      */    }

    /*      */
 /*      */ public boolean isStdo_efc() {
        /* 5089 */ return this.stdo_efc;
        /*      */    }

    /*      */
 /*      */ public boolean isStdo_ealsu() {
        /* 5093 */ return this.stdo_ealsu;
        /*      */    }

    /*      */
 /*      */ public boolean isStdo_eanonlsu() {
        /* 5097 */ return this.stdo_eanonlsu;
        /*      */    }

    /*      */
 /*      */ public boolean isStdo_noloans() {
        /* 5101 */ return this.stdo_noloans;
        /*      */    }

    /*      */
 /*      */ public boolean isStdo_in_subloan() {
        /* 5105 */ return this.stdo_in_subloan;
        /*      */    }

    /*      */
 /*      */ public boolean isStdo_in_unsubloan() {
        /* 5109 */ return this.stdo_in_unsubloan;
        /*      */    }

    /*      */
 /*      */ public boolean isStdo_in_fws() {
        /* 5113 */ return this.stdo_in_fws;
        /*      */    }

    /*      */
 /*      */ public boolean isStdo_adjust_calgrantamt_ind() {
        /* 5117 */ return this.stdo_adjust_calgrantamt_ind;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String getStdo_merits() {
        /* 5124 */ return this.stdo_merits;
        /*      */    }

    /*      */
 /*      */ public void setClientCallBack(RequestContext context) {
        /* 5128 */ if (context == null) {
            /*      */ return;
            /*      */        }
        /*      */
 /*      */
 /* 5133 */ context.addCallbackParam("ss_sex", this.stdo_sex);
        /* 5134 */ context.addCallbackParam("ss_intl", Boolean.valueOf(this.stdo_intl));
        /* 5135 */ context.addCallbackParam("ss_marry", Boolean.valueOf(this.stdo_marry));
        /* 5136 */ context.addCallbackParam("ss_sda", Boolean.valueOf(this.stdo_sda));
        /* 5137 */ context.addCallbackParam("ss_indept", Boolean.valueOf(this.stdo_indept));
        /* 5138 */ context.addCallbackParam("ss_indepts", Integer.valueOf(this.stdo_indepts.size()));
        /*      */
 /* 5140 */ context.addCallbackParam("ss_merit", this.stdo_merits);
        /*      */
 /* 5142 */ context.addCallbackParam("ss_academic", this.stdo_academic);
        /*      */
 /* 5144 */ context.addCallbackParam("ss_fafsa", Boolean.valueOf(this.stdo_fafsa));
        /* 5145 */ context.addCallbackParam("ss_calgrant", Boolean.valueOf(this.stdo_calgrant));
        /* 5146 */ context.addCallbackParam("ss_ealsu", Boolean.valueOf(this.stdo_ealsu));
        /* 5147 */ context.addCallbackParam("ss_eanonlsu", Boolean.valueOf(this.stdo_eanonlsu));
        /* 5148 */ context.addCallbackParam("ss_efc", Boolean.valueOf(this.stdo_efc));
        /* 5149 */ context.addCallbackParam("ss_noloans", Boolean.valueOf(this.stdo_noloans));
        /* 5150 */ context.addCallbackParam("ss_dorm", Boolean.valueOf(this.stdo_dorm));
        /*      */
 /* 5152 */ context.addCallbackParam("ss_awd7", Integer.valueOf(this.stdo_fundid7.intValue()));
        /* 5153 */ context.addCallbackParam("ss_awd8", Integer.valueOf(this.stdo_fundid8.intValue()));
        /* 5154 */ context.addCallbackParam("ss_awd9", Integer.valueOf(this.stdo_fundid9.intValue()));
        /*      */
 /* 5156 */ context.addCallbackParam("ss_adjcal", Boolean.valueOf(this.stdo_adjust_calgrantamt_ind));
        /* 5157 */ context.addCallbackParam("ss_subloan", Boolean.valueOf(this.stdo_in_subloan));
        /* 5158 */ context.addCallbackParam("ss_unsubloan", Boolean.valueOf(this.stdo_in_unsubloan));
        /* 5159 */ context.addCallbackParam("ss_fws", Boolean.valueOf(this.stdo_in_fws));
        /*      */
 /* 5161 */ context.addCallbackParam("ss_return", Boolean.valueOf(this.stdo_return_ind));
        /* 5162 */ context.addCallbackParam("ss_nc", Boolean.valueOf(this.stdo_nc_ind));
        /*      */
 /* 5164 */ context.addCallbackParam("ss_ptab", Integer.valueOf(this.tab_index));
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
 /*      */ public String getStdo_academic() {
        /* 5180 */ return this.stdo_academic;
        /*      */    }

    /*      */
 /*      */ public boolean isStd_sex1() {
        /* 5184 */ return this.std_sex1;
        /*      */    }

    /*      */
 /*      */ public void setStd_sex1(boolean std_sex1) {
        /* 5188 */ this.std_sex1 = std_sex1;
        /*      */    }

    /*      */
 /*      */ public boolean isStd_sex2() {
        /* 5192 */ return this.std_sex2;
        /*      */    }

    /*      */
 /*      */ public void setStd_sex2(boolean std_sex2) {
        /* 5196 */ this.std_sex2 = std_sex2;
        /*      */    }

    /*      */
 /*      */ public boolean isStd_sex3() {
        /* 5200 */ return this.std_sex3;
        /*      */    }

    /*      */
 /*      */ public void setStd_sex3(boolean std_sex3) {
        /* 5204 */ this.std_sex3 = std_sex3;
        /*      */    }

    /*      */
 /*      */ public boolean isStd_merit1() {
        /* 5208 */ return this.std_merit1;
        /*      */    }

    /*      */
 /*      */ public void setStd_merit1(boolean std_merit1) {
        /* 5212 */ this.std_merit1 = std_merit1;
        /*      */    }

    /*      */
 /*      */ public boolean isStd_merit2() {
        /* 5216 */ return this.std_merit2;
        /*      */    }

    /*      */
 /*      */ public void setStd_merit2(boolean std_merit2) {
        /* 5220 */ this.std_merit2 = std_merit2;
        /*      */    }

    /*      */
 /*      */ public boolean isStd_merit3() {
        /* 5224 */ return this.std_merit3;
        /*      */    }

    /*      */
 /*      */ public void setStd_merit3(boolean std_merit3) {
        /* 5228 */ this.std_merit3 = std_merit3;
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
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public String getAddlasuid() {
        /* 5365 */ return this.addlasuid;
        /*      */    }

    /*      */
 /*      */ public void setAddlasuid(String addlasuid) {
        /* 5369 */ this.addlasuid = addlasuid;
        /*      */    }

    /*      */
 /*      */ public void lasuidchged(AjaxBehaviorEvent event) {
        /* 5373 */ UIInput source = (UIInput) event.getSource();
        /* 5374 */ String clientfid = source.getClientId();
        /*      */
 /* 5376 */ String lsuid = this.addlasuid;
        /* 5377 */ int flag = validateLsuId(lsuid);
        /* 5378 */ if (flag < 1) {
            /* 5379 */ source.setValid(false);
            /*      */        }
        /*      */    }

    /*      */
 /*      */
 /*      */ public int lasuidisok() {
        /* 5385 */ return validateLsuId(this.addlasuid);
        /*      */    }

    /*      */
 /*      */ public void resetlasuid(AjaxBehaviorEvent event) {
        /* 5389 */ this.addlasuid = "";
        /* 5390 */ log.info(" resetlasuid() clear lasuid");
        /*      */    }

    /*      */
 /*      */
 /*      */ public void submitlasuid(ActionEvent event) {
        /* 5395 */ log.info(" ////// submitlasuid() is finally revoked.//////");
        /* 5396 */ FacesMessage msg = null;
        /* 5397 */ if (validateLsuId(this.addlasuid) == 1) {
            /* 5398 */ Querier qurier = (Querier) AppReference.findBean2("querier");
            /* 5399 */ Student chosenstd = qurier.getSelectedStud();
            /* 5400 */ String stdid = chosenstd.getStudentALsuid();
            /*      */
 /* 5402 */ if (chosenstd.getPickupInd() < 1) {
                /* 5403 */ msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "QueryForm.InactiveRecordLSUID");
                /* 5404 */ this.facesContext.addMessage(null, msg);
                /*      */
 /*      */            } /* 5407 */ else if (this.ref.isEmp(stdid)) {
                /* 5408 */ chosenstd.setStudentALsuid(this.addlasuid);
                /*      */
 /* 5410 */ chosenstd.setEstmNumb(-1 * this.login.getCurrentUser().getUserid().intValue());
                /*      */
 /* 5412 */ chosenstd.setTzdid(this.ref.getTzSN());
                /* 5413 */ chosenstd.setDid(System.currentTimeMillis());
                /* 5414 */ chosenstd.setDidstr(this.ref.fmtFullNowTimeWithDefaultZone());
                /*      */
 /* 5416 */ this.accessor.saveSuppliedLasuId(chosenstd);
                /*      */
 /*      */
 /*      */
 /* 5420 */ msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "QueryForm.SavedLSUID");
                /* 5421 */ this.facesContext.addMessage(null, msg);
                /*      */            } else {
                /* 5423 */ msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "QueryForm.ExistingLSUID");
                /* 5424 */ this.facesContext.addMessage(null, msg);
                /*      */            }
            /*      */        } else {
            /*      */
 /* 5428 */ msg = this.ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "QueryForm.InvalidSupplyLSUID");
            /* 5429 */ this.facesContext.addMessage(null, msg);
            /*      */        }
        /* 5431 */ log.info(" submitlasuid() msg: %s  (lasuid=%s)", new Object[]{msg.getSummary(), this.addlasuid});
        /* 5432 */ this.addlasuid = "";
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public boolean isReturn_std_ind() {
        /* 5440 */ return this.return_std_ind;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setReturn_std_ind(boolean return_std_ind) {
        /* 5447 */ this.return_std_ind = return_std_ind;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public boolean isNc_std_ind() {
        /* 5454 */ return this.nc_std_ind;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public void setNc_std_ind(boolean nc_std_ind) {
        /* 5461 */ this.nc_std_ind = nc_std_ind;
        /*      */    }

    /*      */
 /*      */
 /*      */ public void returningIndChanged(AjaxBehaviorEvent event) {
        /* 5466 */ this.stud.setReturnStdInd(Integer.valueOf(this.return_std_ind ? 1 : 0));
        /* 5467 */ this.stdo_return_ind = this.return_std_ind;
                   this.calc.refreshCalc(this.stud);
        /*      */    }

    /*      */
 /*      */
 /*      */ public void ncIndChanged(AjaxBehaviorEvent event) {
        /* 5472 */ this.stud.setNcStdInd(Integer.valueOf(this.nc_std_ind ? 1 : 0));
        /* 5473 */ this.stdo_nc_ind = this.nc_std_ind;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public boolean isStdo_return_ind() {
        /* 5480 */ return this.stdo_return_ind;
        /*      */    }

    /*      */
 /*      */
 /*      */
 /*      */
 /*      */ public boolean isStdo_nc_ind() {
        /* 5487 */ return this.stdo_nc_ind;
        /*      */    }

    /*      */
 /*      */ private void setStudFAFSA(String str) {
        /* 5491 */ this.stud.setStudentXFafsa(str);
        /*      */    }
 
     public boolean isStd_pellGrant() {
        return std_pellGrant;
    }

    //pell_Grant
    public void setStd_pellGrant(boolean std_pellGrant) {
        this.std_pellGrant = std_pellGrant;
    }
    public int getStd_pellGrant_value() {
        return std_pellGrant_value;
    }

    public void setStd_pellGrant_value(int std_pellGrant_value) {
        this.std_pellGrant_value = std_pellGrant_value;
    }
    /*      */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\Estimator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
