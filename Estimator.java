/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;
import com.rits.cloning.Cloner;
import edu.lsu.estimator.Student;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.hibernate.validator.ClassValidator;  
//import org.hibernate.validator.InvalidValue;  
/**
 *
 * @author kwang
 */
@Named
@SessionScoped
public class Estimator implements Serializable { //collect all info, to cal/recal charges and credits, and show the balance

    private static final long serialVersionUID = 1L; //If you don't define serialVersionUID, the system will make one by hashing most of your class's features. Then if you change anything, the UID will change and Java won't let you reload old data. 

    @Inject
    AppReference ref;
    @Inject
    edu.lsu.estimator.InfoState info;

    private static final Logger log = LoggerFactory.getLogger();
    @PersistenceContext
    private EntityManager em; //for autocomplete
    @Inject
    @RequestScoped
    FacesContext facesContext;    //transient

    @Inject
    private edu.lsu.estimator.Student stud; //default stud, gives default numbers at first ??? --- where to set and keep the default settings????

    @Inject
    private PackFunctions calc;    //Calculator

    @Inject
    Accessor accessor; //POJOaccessor
    @Inject
    Login login;

    private int modflag; //new or mod (load an existing Estimation data ???)
    private Student modStud;

    private int tab_index = 0;
    private String[] tab_titles = {"Personal", "Acdemic", "Financial", "Institutional", "Notes and Adjustment"};
    private String PreTabString;
    private String NextTabString;

//    private List<String> std_merits;
    private String std_merits;
    private boolean std_merit1 = false;
    private boolean std_merit2 = false;
    private boolean std_merit3 = false;

    private String last_merit = null;

    private List<String> std_quas;
    //   private int  std_efc;
    private int std_EA_PERCENT; //div 100 when output

    //DB table has column defined as string
    private boolean std_intl;
    private boolean std_marry;
    private boolean std_sda;
    private boolean std_dorm;
    private boolean std_indept;
    private boolean std_fafsa;

    private HashMap<String, String> std_indepts = new HashMap<>();
    private int std_indept_byhand = 0;

    private String std_sex = "N"; //not in the DB table?
    private boolean std_sex1 = false;
    private boolean std_sex2 = false;
    private boolean std_sex3 = true;

    private boolean std_calgrant;

 //pell_Grant
    private boolean std_pellGrant;
    
    private boolean std_efc;
    private boolean std_ealsu;
    private boolean std_eanonlsu;
    private boolean std_noloans = false;

    private boolean in_subloan = true;
    private boolean in_unsubloan = true;
    private boolean in_fws = true;

    private boolean adjust_calgrantamt_ind;

    private String std_dob;
    static final String DATE_PATTERN_YYYY = "(0?[1-9]|1[012])/?(0?[1-9]|[12][0-9]|3[01])/?((19|20)\\d\\d)";
    static final String DATE_PATTERN_YY = "(0?[1-9]|1[012])/?(0?[1-9]|[12][0-9]|3[01])/?\\d\\d";
    static final String DATE_PATTERN = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)?\\d\\d)";

    @NotNull
    @Min(0)
    private Integer costudents = 0; //family members studying in LSU

    private final int NOTESINIT = 9;//5;
    private int notestot = 9;//ref.getTotal_funds_amt(); // java.lang.NullPointerException	at edu.lsu.estimator.Estimator.<init>(Estimator.java:115)
    private int shownotes = NOTESINIT;//>= ref.getAlone_funds_amt()  ? NOTESINIT: ref.getAlone_funds_amt();
    private int[] notesind = {0, 0, 0, 0, 0, 0, 0, 0, 0}; // name=100, amt=10  notes=1, nothing=0

    private HashMap<String, String> check_inds = new HashMap<>();

    private String fund1extra = "";
    private String fund2extra = "";
    private String fund3extra = "";
    private String fund4extra = "";
    private String fund5extra = "";
    private String fund6extra = "";

    //added by Ken 2012-01-11
    //private  List<String> popup = new ArrayList<>(); //it is a selectmany... item
    //private List<SelectItem> popups = new ArrayList<>();
    private String popind = "0";
    private String popkey = "";

    private List<Student> popups = new ArrayList<>();
    private edu.lsu.estimator.QueryStudModel popDataModel;
    private Student chosenPopStud;

    //2012-01-14, since the popup query needs values of the field, even they are invalid, and failed to replace bean's value.
    private String std_ln;
    private String std_fn;

    //2012-02-15 primefaces ptab misteriously calls/sets null value to fundid7 fundid8 fundid9, even though they DO has value (in std obj)
    private Integer stdo_fundid7;
    private Integer stdo_fundid8;
    private Integer stdo_fundid9;

    private String stdo_sex;
    private boolean stdo_intl;
    private boolean stdo_marry;
    private boolean stdo_sda;
    private boolean stdo_dorm;

    private boolean stdo_indept;

    private String stdo_academic; //studentUAcademic
    /*
    private boolean std_cat1=true;
    private boolean std_cat2=false;
    private boolean std_cat3=false;
    private boolean std_cat4=false;
    private boolean std_cat5=false;
    private boolean std_cat6=false;
    private boolean std_cat7=false;
    private boolean std_cat8=false;
    private boolean std_cat9=false;
    private boolean std_cat10=false;
     */
    private boolean stdo_fafsa;
    private boolean stdo_calgrant;
    private boolean stdo_efc;
    private boolean stdo_ealsu;
    private boolean stdo_eanonlsu;
    private boolean stdo_noloans;

    private boolean stdo_in_subloan;
    private boolean stdo_in_unsubloan;
    private boolean stdo_in_fws;

    private boolean stdo_adjust_calgrantamt_ind;
    //private List<String> stdo_merits;
    private String stdo_merits;
    private HashMap<String, String> stdo_indepts = new HashMap<>();

    private int tbodyrow = 0;
    /*
    private String mirr_merit;
    private String mirr_academic;
    private int    mirr_tabindex;
    
    private String fund7id;
    private String fund8id;
    private String fund9id;
     */

    //@Inject private ResourceBundle resourceBundle;
    //WELD-000054 Producers cannot produce non-serializable instances for injection into non-transient fields of passivating beans
    private int sfund1amt;
    private int sfund2amt;

    //04/19/2012 this funds in dropdown, may have max limit. superuser can override, while ordinary user can reduce
    private int sfund7amt;
    private int sfund8amt;
    private int sfund9amt;

    //04/18/2012 for adding LASU ID to active rec, reuse the checking logic defined earlier
    private String addlasuid = "";

    //2013/01/28 added by Ken according to Esther's new req for 2013
    private boolean return_std_ind;
    private boolean nc_std_ind;
    private boolean stdo_return_ind;
    private boolean stdo_nc_ind;

    //Uncompilable source code - variable log might not have been initialized
    public Estimator() {  //if define log as static:   java.lang.RuntimeException: Uncompilable source code - variable log might not have been initialized. WHY ?????
        this.modflag = 0;
        this.modStud = null;
        tab_index = 0;
        //mirr_tabindex = tab_index;
        std_sex = "N";
        std_sex1 = false;
        std_sex2 = false;
        std_sex3 = true;
    }

    //org.jboss.weld.exceptions.WeldException: WELD-000049 Unable to invoke [method] @PostConstruct public edu.lsu.estimator.Estimator.syncFromStudObj() on edu.lsu.estimator.Estimator@1ea63f5e ???????
    @PostConstruct
    public void syncFromStudObj() { //WELD-000049 Unable to invoke [method] @PostConstruct public edu.lsu.estimator.Estimator.syncFromStudObj()
        stdo_fundid7 = stud.getFund7id();
        stdo_fundid8 = stud.getFund8id();
        stdo_fundid9 = stud.getFund9id();

        std_indepts.clear();

        String str = stud.getStudentLIntlStud();
        this.std_intl = (str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no")) ? false : true;
        stdo_intl = std_intl;

        str = stud.getStudentMMarry();
        this.std_marry = (str == null || str.isEmpty() || str.trim().equalsIgnoreCase("Single") || str.trim().equalsIgnoreCase("No")) ? false : true;
        stdo_marry = std_marry;
        //log.info("syncFromStudObj()---get stud Mmarital=[%s], std_marry=%s", str, std_marry);  //get stud Mmarital=[no], std_marry=true     

        str = stud.getStudentNSda();
        this.std_sda = (str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no")) ? false : true;
        stdo_sda = std_sda;

        str = stud.getStudentWDorm();
        this.std_dorm = (str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no")) ? false : true;
        stdo_dorm = std_dorm;

        str = stud.getStudentXFafsa();
        this.std_fafsa = (str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no")) ? false : true;
        stdo_fafsa = std_fafsa;

        str = stud.getIndEalsu();//.getInd_ealsu();
        this.std_ealsu = (str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no")) ? false : true;
        stdo_ealsu = std_ealsu;

        str = stud.getIndEanonlsu();//.getInd_eanonlsu();
        if (stud.getStudentAgNonlsuAllowrance().intValue() > 0) {
            str = "Yes";
        } else {
            str = "No";
        }
        this.std_eanonlsu = (str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no")) ? false : true;
        stdo_eanonlsu = std_eanonlsu;

        str = stud.getAdjCalgrantInd();//.getInd_acalgrant();
        this.adjust_calgrantamt_ind = (str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no")) ? false : true;
        stdo_adjust_calgrantamt_ind = adjust_calgrantamt_ind;

        str = stud.getIndExcloans();//.getInd_noloans();
        this.std_noloans = (str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no")) ? false : true;
        stdo_noloans = std_noloans;

        str = stud.getIndEfc();//.getInd_efc();
        this.std_efc = (str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no")) ? false : true;
        stdo_efc = std_efc;

        str = stud.getStudentZCalgrant();
        this.std_calgrant = (str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no")) ? false : true;
        stdo_calgrant = std_calgrant;

        str = stud.getStudentApSubLoans();
        this.in_subloan = (str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no")) ? false : true;
        stdo_in_subloan = in_subloan;

        str = stud.getStudentAqUnsubLoans();
        this.in_unsubloan = (str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no")) ? false : true;
        stdo_in_unsubloan = in_unsubloan;

        str = stud.getStudentArFws();
        this.in_fws = (str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no")) ? false : true;
        stdo_in_fws = in_fws;

        str = stud.getStudentYIndept();
        this.std_indept = (str == null || str.isEmpty() || str.trim().equalsIgnoreCase("no")) ? false : true;
        //log.info("syncFromStudObj()---get stud Yindept=[%s], std_indept=%s", str, std_indept);       
        if (std_marry) {
            this.std_indept = true;
            std_indepts.put("marry", "marry");
            //log.info("syncFromStudObj()---get marry==true, set std_indept=true");            
            if (ref.isEmp(str) || str.equalsIgnoreCase("no")) {
                stud.setStudentYIndept("Yes");
                ////log.info("syncFromStudObj()---adjust stud Yindept to YES, since marry==true");                      
            }
        }
        //studentAgNonlsuAllowrance --BigDecimal ???
        //studentAhLsuAllowrance;
        //studentAiEduAllowPer        
        //studentAaCalgrantA studenAbCalgrantB
        if (std_eanonlsu) {
            stud.setStudentAiEduAllowPer(new BigDecimal(calc.getEaNonLsuPercentageByDorm(std_dorm)).divide(new BigDecimal(100)));
        } else {
            stud.setStudentAiEduAllowPer(BigDecimal.ZERO);
        }

        //     stud.setStudentNumb(0); //if( studentNumb==null)studentNumb=0;
        //      stud.setStudentUserName("username");
        stud.setStudentPassword("password");
        stud.setStudentBwProgress(0); //BigDecimal.ZERO  //getStudentBwProgress
        stud.setStudentStudType("FYUG");  //studentStudType

        //some data entries are not in student. temporary ones
        this.std_sex = stud.getSex();//.getStd_sex() ;//  "N";
        if (std_sex == null || std_sex.isEmpty()) {
            std_sex = "N";
            stud.setSex("N");
        }
        std_sex1 = false;
        std_sex2 = false;
        std_sex3 = false;
        switch (std_sex) {
            case "F":
                std_sex1 = true;
                break;
            case "M":
                std_sex2 = true;
                break;
            case "N":
                std_sex3 = true;
                break;
        }
        stdo_sex = std_sex;

        /*
        this.std_merits = new ArrayList<String>();
        String x = stud.getStudentSMerit();
        if( x!=null && !x.isEmpty()){               
            x = x.replaceAll(", ",  ",");
            std_merits = Arrays.asList(x.split(","));
        }else{
            //keep the initilized and empty list
        }*/
        this.std_merits = stud.getStudentSMerit();
        std_merit1 = false;
        std_merit2 = false;
        std_merit3 = false;
        if (!ref.isEmp(std_merits)) {
            switch (std_merits) {
                case "MC":
                    std_merit1 = true;
                    break;
                case "MS":
                    std_merit2 = true;
                    break;
                case "MF":
                    std_merit3 = true;
                    break;
                default:
                    break;
            }
        }
        stdo_merits = std_merits;
// mirr_merit = std_merits;

        stdo_academic = stud.getStudentUAcademic();
        //mirr_academic = stdo_academic;

        //std_dob is string
        //Date 
        String dob = stud.getStudentDDob();
        if (dob != null && dob.trim().length() == 10) { //ref.getDateInputShowStr()='MM/dd/yyyy'
            std_dob = dob;//new SimpleDateFormat( ref.getDateInputShowStr()).format(dob);
//log.info("syncFromStudObj()---get stud Ddob=[%s]", dob);             
            try {
                DateTime dt = ref.parseDateStr(std_dob, ref.getDateInputShowStr());
                DateMidnight dm = new DateMidnight(dt);
                DateMidnight due = new DateMidnight(ref.getIndept_dob());
                if (dm.isBefore(due)) {
                    //if(! std_indepts.containsKey("dob")){
                    std_indepts.put("dob", "dob");
                    //}
                    this.std_indept = true;
                    str = stud.getStudentYIndept();
//log.info("syncFromStudObj()---get stud Ddob before the line, set std_indept=true");                     
                    if (ref.isEmp(str) || str.equalsIgnoreCase("no")) {
                        stud.setStudentYIndept("Yes");
//log.info("syncFromStudObj()---adjust stud Yindept to YES, since dob is before line");                         
                    }
                } else {
                    std_indepts.remove("dob");
                }
            } catch (Exception e) {
                std_indepts.remove("dob");
                e.printStackTrace();
            }

        } else {
            std_dob = null;
            std_indepts.remove("dob");
        }
        stdo_indepts = std_indepts;
        stdo_indept = std_indept;

        std_ln = stud.getStudentBLastname();
        std_fn = stud.getStudentCFirstname();

        this.costudents = stud.getHomecostudies();//.getCostudents(); 
        //  this.std_apt = stud.getHome_addr_apt();

        //2017-12-19  to verify fund status/max_ind in this year, if so, set amt to zero
        int diffyear = 0;
        if (stud.getStudentFisy() != ref.getFiscal_year()) { // ref.getFiscal_year() =init to xxxx,  current YYYY fiscal_year, then comp to seed.getClientFscy()  // ref.getFaid_year="YYYY~YYYY+1"
            //checking the fund status
            diffyear = 1;
        }

        Integer[] prefunds = ref.getFunds_alone();
        int t = 0;
        stud.setStudentAsScholarship1Name(ref.getFundDescById(prefunds[t]));//=  "Church Matching Scholarship (100% match, $1000 max)"; 
        stud.setFund1id(prefunds[t]);
        if (diffyear > 0 && ref.fundMaxOutIndById(prefunds[t]) < 0) {
            stud.setStudentAuScholarship1Amt(0); //sfund1amt        
        }
        fund1extra = ref.getFundExtById(prefunds[t++]);

        //at edu.lsu.estimator.AppReference.getFundDescById(AppReference.java:399) NPE ??????? ##########################################################
        stud.setStudentAvScholarship2Name(ref.getFundDescById(prefunds[t]));//= "Pacific Union Camp Earning (100% match, $2000 max)";
        stud.setFund2id(prefunds[t]);
        if (diffyear > 0 && ref.fundMaxOutIndById(prefunds[t]) < 0) {
            stud.setStudentAxScholarship2Amt(0);
        }
        fund2extra = ref.getFundExtById(prefunds[t++]);

        stud.setStudentAyScholarship3Name(ref.getFundDescById(prefunds[t]));//= "Non-Pacific Union Camp Earning (50% match, $1500 max)"; 
        stud.setFund3id(prefunds[t]);
        if (diffyear > 0 && ref.fundMaxOutIndById(prefunds[t]) < 0) {
            stud.setStudentBaScholarship3Amt(0);
        }
        fund3extra = ref.getFundExtById(prefunds[t++]);

        stud.setStudentBbScholarship4Name(ref.getFundDescById(prefunds[t]));//= "Literature Evangelist Earnings (100% match, $3000 max)";
        stud.setFund4id(prefunds[t]);
        if (diffyear > 0 && ref.fundMaxOutIndById(prefunds[t]) < 0) {
            stud.setStudentBdScholarship4Amt(0);
        }
        fund4extra = ref.getFundExtById(prefunds[t++]);

        stud.setStudentBeScholarship5Name(ref.getFundDescById(prefunds[t]));//= "";
        stud.setFund5id(prefunds[t]);
        if (diffyear > 0 && ref.fundMaxOutIndById(prefunds[t]) < 0) {
            stud.setStudentBgScholarship5Amt(0);
        }
        fund5extra = ref.getFundExtById(prefunds[t++]);

        stud.setStudentBhScholarship6Name(ref.getFundDescById(prefunds[t]));//= "";
        stud.setFund6id(prefunds[t]);
        if (diffyear > 0 && ref.fundMaxOutIndById(prefunds[t]) < 0) {
            stud.setStudentBjScholarship6Amt(0);
        }
        fund6extra = ref.getFundExtById(prefunds[t++]);

        sfund1amt = stud.getStudentAuScholarship1Amt();
        sfund2amt = stud.getStudentAxScholarship2Amt();/*
        sfund3amt    = stud.getStudentBaScholarship3Amt();
        sfund4amt    = stud.getStudentBdScholarship4Amt();
        sfund5amt    = stud.getStudentBgScholarship5Amt();
        sfund6amt    = stud.getStudentBjScholarship6Amt();*/


        t = stud.getFund7id();
        if (t > 0 && diffyear > 0 && ref.fundMaxOutIndById(prefunds[t]) < 0) {
            stud.setStudentBmScholarship7Amt(0);
        }

        t = stud.getFund8id();
        if (t > 0 && diffyear > 0 && ref.fundMaxOutIndById(prefunds[t]) < 0) {
            stud.setStudentBpScholarship8Amt(0);
        }

        t = stud.getFund9id();
        if (t > 0 && diffyear > 0 && ref.fundMaxOutIndById(prefunds[t]) < 0) {
            stud.setStudentBsScholarship9Amt(0);
        }

        sfund7amt = stud.getStudentBmScholarship7Amt();
        sfund8amt = stud.getStudentBpScholarship8Amt();
        sfund9amt = stud.getStudentBsScholarship9Amt();

        stud.setStudentFisy(ref.getFiscal_year());

        //2013/01/28 by Ken
        Integer tio = stud.getReturnStdInd();
        return_std_ind = (tio == null || tio == 0) ? false : true;
        tio = stud.getNcStdInd();
        nc_std_ind = (tio == null || tio == 0) ? false : true;

        calc.setStud(stud);
        /*
        Integer fundid = stud.getFund7id();
        fund7id = fundid==null?"0":fundid.toString();
        fundid  = stud.getFund8id();
        fund8id = fundid==null?"0":fundid.toString();
        fundid  = stud.getFund9id();
        fund9id = fundid==null?"0":fundid.toString();
         */

        notestot = ref.getTotal_funds_amt();
        shownotes = NOTESINIT >= ref.getAlone_funds_amt() ? NOTESINIT : ref.getAlone_funds_amt();

//      log.info("***************************************** Estimator syncFromStudObj()  post construction() invoked. now stud sex=%s", stud.getSex()); //THIS CAUSE ERROR while log is static. why ???????  //NPE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //java.lang.NullPointerException	at edu.lsu.estimator.Estimator.syncFromStudObj(Estimator.java:147)
    }

    public String modEstimate(Student modStudent) {
        if (modStudent == null) {
            return null;
        }
        this.modflag = 1;
        this.modStud = modStudent;

        Cloner cloner = new Cloner();
        //MyClass clone=cloner.deepClone(o);
        this.stud = cloner.shallowClone(modStudent);//.deepClone(modStudent); //// java.lang.IllegalArgumentException: Can not set final org.osgi.framework.BundleContext field org.osgi.util.tracker.ServiceTracker.context to org.osgi.util.tracker.ServiceTracker
        this.stud.setPdfs(new ArrayList<edu.lsu.estimator.Print>()); //null
        this.stud.setPrtTimes(0);// modStudent.getPrtTimes() ); //the print can not be copied

        //this.stud = modStudent;
        calc.setStud(stud);
        syncFromStudObj();

        loadStudInstituteScholarshipAwards();

        //return "estimate-mod?faces-redirect=true";
        tab_index = 0;
        //mirr_tabindex = tab_index;
        return "estimate-new?faces-redirect=true";
    }

    public String newEstimate() {
        stud = new Student();// with default setting done??? //######### why it keeps former student's info ??????????????????????????????????????????
        this.modflag = 0;
        this.modStud = null;
        stud.setPrtTimes(0); ////////////////////////////////
        stud.setPdfs(null);

        stud.setCounselorId(login.getCurrentUser().getUserid());
        stud.setCounselorOrig(login.getCurrentUser().getUserid());
        stud.setStudentBuOrigCounselor(login.getCurrentUser().getUsername());

        //the constants should also be reset. By Ken 2011-10-31
        calc.setStud(stud);

        //estimator also holds some values for the front web page
        syncFromStudObj();

        tab_index = 0;
        //mirr_tabindex = tab_index;
        return "estimate-new?faces-redirect=true";
    }

    public void newPEstimate() {
        stud = new Student();// with default setting done??? //######### why it keeps former student's info ??????????????????????????????????????????
        this.modflag = 0;
        this.modStud = null;
        stud.setPrtTimes(0); ////////////////////////////////
        stud.setPdfs(null);

        stud.setCounselorId(ref.getSys_counselor_id()); //javax.enterprise.context.ContextNotActiveException: FacesContext is not active
        // can set the FacesContext instance for the current thread to an instance of this custom FacesContext class; the FacesContext.setCurrentInstance(...) API method

        stud.setCounselorOrig(ref.getSys_counselor_id());
        stud.setStudentBuOrigCounselor("MyCampus Portal");

        //the constants should also be reset. By Ken 2011-10-31
        calc.setStud(stud);

        //estimator also holds some values for the front web page
        syncFromStudObj();
    }

    public void tabChanged(TabChangeEvent event) {
        //FacesMessage msg = new FacesMessage("Tab Changed", "Active Tab" + event.getTab().getTitle());  //.getId()      
        //FacesContext.getCurrentInstance().addMessage(null, msg);  
        String title = event.getTab().getTitle();
        for (int i = 0; i < tab_titles.length; i++) {
            if (tab_titles[i].equalsIgnoreCase(title)) {
                tab_index = i;
                break;
            }
        }
        //mirr_tabindex=tab_index;

        RequestContext reqContext = RequestContext.getCurrentInstance();
        //context.addCallbackParam("saved", true);    //basic parameter  
        ////context.addCallbackParam("user", user);     //pojo as json  
        setClientCallBack(reqContext);
    }

    private int validateStudBean() {
        /* ==================== obseleted. before hibernate validator 4 ===================
        ClassValidator<Student> myValidator = new ClassValidator<Student>(Student.class);
        InvalidValue[] invalidValues = userValidator.getInvalidValues(stud);  
        for (InvalidValue value : invalidValues) {  
            System.out.println("========");  
            System.out.println(value);  
            System.out.println("message=" + value.getMessage());  
            System.out.println("propertyName=" + value.getPropertyName());  
            System.out.println("propertyPath=" + value.getPropertyPath());  
            System.out.println("value=" + value.getValue());  
        } */
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //HibernateValidatorConfiguration configuration = Validation.byProvider( HibernateValidator.class ).configure();
        //ValidatorFactory factory = configuration.addProperty( "hibernate.validator.fail_fast", "false" ).buildValidatorFactory();
        Validator validator = (Validator) factory.getValidator();

        //HibernateValidatorConfiguration configure = Validation.byProvider(HibernateValidator.class).configure();
        //ResourceBundleLocator defaultResourceBundleLocator = configure.getDefaultResourceBundleLocator(); 
        //ResourceBundleLocator myResourceBundleLocator = new MyCustomResourceBundleLocator(defaultResourceBundleLocator);
        //configure.messageInterpolator(new ResourceBundleMessageInterpolator(myResourceBundleLocator));
        //The Validator interface contains three methods that can be used to either validate entire entities or just a single properties of the entity
        //All three methods return a Set<ConstraintViolation>. The set is empty, if the validation succeeds. Otherwise a ConstraintViolation instance is added for each violated constraint.
        //validateValue() method  checks, whether a single property of a given class can be validated successfully, if the property had the specified value        
        //Set<ConstraintViolation<Student>> constraintViolations = validator.validateValue(Student.class, "studentBLastname", null);
        //Set<ConstraintViolation<Student>> constraintViolations2 = validator.validateProperty(stud, "studentBLastname");
        String[] prop_names = {"studentALsuid", "studentBLastname", "studentCFirstname", "studentDDob", "studentFPhone", "studentEEmail", "studentIState", "studentJZip"};
        String[] uic_ids = {"std_lsuid", "std_ln", "std_fn", "std_dob", "std_phone", "std_email", "std_state", "std_zip", "std_gpa"};

        /*
        Set<ConstraintViolation<Student>> constraintViolations  = validator.validate(stud);
        //assertEquals(0, constraintViolations.size());
        //assertEquals("may not be null", constraintViolations.iterator().next().getMessage());
        for( ConstraintViolation<Student> one: constraintViolations ){
            log.info("validateStudBean found violation. msg=%s, path=%s",one.getMessage(), one.getPropertyPath().toString());
        }*/
        int vios = 0;

        FacesMessage err; //err = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidLSUID");  
        for (int i = 0; i < prop_names.length; i++) {
            String prop = prop_names[i];
            Set<ConstraintViolation<Student>> propconstraintViolations = validator.validateProperty(stud, prop);
            //           log.info("validateStudBean looping property %s (id=%s)", prop, uic_ids[i]);
            if (propconstraintViolations.size() > 0) {
                UIComponent c = getUIComponentById(uic_ids[i]);
                if (c == null) {
                    facesContext.getViewRoot().findComponent(uic_ids[i]);
                }
                UIInput textInput = (UIInput) c;
                log.info("validateStudBean looping property %s (id=%s)  got UIComponent==null? %s", prop, uic_ids[i], c == null);
                String label = "";

                if (textInput != null) {
                    textInput.setValid(false);                  //NPE
                    label = (String) c.getAttributes().get("label"); //NPE
                    label = label + ": ";
                }
                for (ConstraintViolation<Student> one : propconstraintViolations) {
                    log.info("validateStudBean found violation. msg=%s, path=%s", one.getMessage(), one.getPropertyPath().toString());
                    err = new FacesMessage(" ");
                    err.setSeverity(FacesMessage.SEVERITY_ERROR);
                    err.setDetail(label + one.getMessage());
                    err.setSummary(err.getDetail());
                    facesContext.addMessage(null, err); //"auth-sv-ptab-std_lsuid"
                    vios++;
                }
            }
        }

        /*
        MethodValidator mvalidator = Validation.byProvider( HibernateValidator.class )
        .configure()
        .buildValidatorFactory()
        .getValidator()
        .unwrap( MethodValidator.class );
         */
        //Hibernate Validator goes one step further and allows to place contraint annotations also on method parameters and method return values, thus enabling a programming style known as "Programming by Contract".
        return vios;//constraintViolations.size();
    }

    public String toEmail() { //action. return string instead of void
        log.info("==================toEmail()   button clicked ...........................");
        String ln = stud.getStudentBLastname();
        String fn = stud.getStudentCFirstname();
        //Date 
        String dob = stud.getStudentDDob();
        String phone = stud.getStudentFPhone();
        String msg = chkStudBasicInfo();
        log.info("==================toEmail() got chkStudBasicInfo() res msg=%s", msg);
        if (!msg.isEmpty()) {
            return null;
        }

        int vio = validateStudBean();
        log.info("==================toEmail() got validateStudBean() res code=%d", vio);
        if (vio > 0) {
            return null;
        }

        stud.setStudentUserName(compUserName(stud.getStudentALsuid(), ln, fn, dob)); //used to comp when merging dup/conflict among those picked==1 (upload download)
        msg = saveStud(1);

        if (msg == null || msg.isEmpty()) {
            FacesMessage guimsg = ref.facesMessageByKey(FacesMessage.SEVERITY_INFO, "EstimateForm.DataSaved");
            if (facesContext != null) {
                facesContext.addMessage(null, guimsg);
            }
            stud.setStudentNumb((Integer) null);

            //new PDFgen().servePDF(stud);
            PDFgen pdf = new PDFgen();
            Print prt = new Print();
            prt.setSship1ExtAmt(0);
            prt.setSship2ExtAmt(0);
            prt.setSship7ExtAmt(0);
            prt.setSship8ExtAmt(0);
            prt.setSship9ExtAmt(0);

            msg = pdf.genPDF(stud, login, ref, calc, prt, 2);
            if (!msg.startsWith("Failed")) {//                 msg==null||msg.isEmpty()){
                log.info("==================toEmail() gen pdf: pass");
                String pdfname = msg;
                //save prints table
                msg = savePrint(prt, 2);//1); 
                if (msg == null || msg.isEmpty()) {
                    log.info("==================toEmail() save print rec: pass");
                    //try to send pdf
                    msg = sendEmailLASU(pdf.getPdfRoot(), pdfname, stud.getStudentEEmail(), stud.getStudentCFirstname() + " " + stud.getStudentBLastname());

                    if (msg.startsWith("sent estimate")) {// msg!=null && !msg.isEmpty()){
                        log.info("==================toEmail() sent pdf: %s ........", msg);
                        FacesMessage pdfmsg = ref.facesMessageByStr(FacesMessage.SEVERITY_INFO, msg);
                        if (facesContext != null) {
                            facesContext.addMessage(null, pdfmsg);
                        }
                        //update prints downloaded flag ind?
                    } else {
                        FacesMessage pdfmsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                        if (facesContext != null) {
                            facesContext.addMessage(null, pdfmsg);
                        }
                        log.info("==================toEmail() failed to send email: %s ........", msg);
                        return null;
                    }
                    ////           stud.setPrtTimes( stud.getPrtTimes()+1);
                    msg = accessor.updateStudPrtTimes(stud);
                    if (msg != null && !msg.isEmpty()) {
                        log.info("==================toEmail() failed to update stud obj %s with msg: %s", stud.getRecid(), msg);
                    }
                } else {
                    FacesMessage pdfmsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    if (facesContext != null) {
                        facesContext.addMessage(null, pdfmsg);
                    }
                    log.info("==================toEmail() failed to save print rec: %s ........", msg);
                }
            } else {
                log.info("==================toEmail() gen pdf failed: %s ........", msg);
                FacesMessage pdfmsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                if (facesContext != null) {
                    facesContext.addMessage(null, pdfmsg);
                }
            }

        } else if (!msg.equals("processed")) {
            FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
            if (facesContext != null) {
                facesContext.addMessage(null, guimsg);
            }
        }
        return null;
    }

    private String sendEmailLASU(String pdffolder, String pdfname, String emailbox, String receiverName) {
        String smtpServer = "smtp.lasierra.edu";
        int smtpPort = 465; //25;////ssmtp and smtp (25) by smtp. LASU does not has TLS ssmtp port like gmail
        String senderBox = "enroll@lasierra.edu";
        String senderName = "La Sierra University Enrollment Services Office";

        String msg = "";
        if (ref.isEmp(pdfname) || !pdfname.endsWith(".pdf")) {
            msg = "invalid file name. Can not email estimate.";
            return msg;
        }
        //check stud.getStudentEEmail() ???
        log.info("\n sendEmailLASU() is invoked to send PDF fiel to [%s]", emailbox);
        if (ref.isEmp(emailbox)) {
            msg = "invalid E-mail address '" + emailbox + "'. Can not email estimate.";
            return msg;
        }

        try {
            // Create the email message
            HtmlEmail email = new HtmlEmail();

            //email.addTo(stud.getStudentEEmail(),stud.getStudentCFirstname()+" "+ stud.getStudentBLastname());//"kwang@lasierra.edu", "Ken Wang");
            email.addTo(emailbox, receiverName);
            email.setFrom(senderBox, senderName);//"kwang@lasierra.edu", "LASU Ken Wang");
            email.setSubject("La Sierra University Estimate");//Test email with inline image and attachement");
            //           email.addBcc(senderBox, senderName);
            // email.addCc("info@lasierra.edu",  "La Sierra University Enrollment Services Office");
            email.addCc(login.getCurrentUser().getEmail() + "@lasierra.edu", login.getCurrentUser().getUsername()); //org.apache.commons.mail.EmailException: Missing final '@domain'
            email.addReplyTo("info@lasierra.edu", "La Sierra University Enrollment Services Office");

            log.info("==================toEmail()  will embed image from network ...........................");

            // embed the image and get the content id
            URL url = null;
            try {
                url = new URL("http://vhost1.lasierra.edu/live/logo_2c.gif"); //http://www.lasierra.edu/downloadcenter/images
            } catch (MalformedURLException ex) {
                log.info("...toEmail() meets MalformedURLException:", ex);
                msg = ex.getMessage();
                return msg;
            }

            String cid = email.embed(url, "La Sierra University logo");

            log.info("==================toEmail()   will set message ...........................");
            StringBuilder sbHtml = new StringBuilder(512);
            StringBuilder sbText = new StringBuilder(512);
            sbHtml.append("<html><head><title>La Sierra University Estimate</title></head><body>\n");
            sbHtml.append("Dear <b>").append(stud.getStudentCFirstname()).append(' ').append(stud.getStudentBLastname()).append("</b>,<br/><br/>\n");
            sbText.append("Dear ").append(stud.getStudentCFirstname()).append(' ').append(stud.getStudentBLastname()).append(",\n\n");
            sbHtml.append("Thank you for choosing La Sierra University!<br/><br/>Your estimate is based on the data and information you provided.<br/><br/>\n");
            sbText.append("Thank you for choosing La Sierra University!\n\nYour estimate is based on the data and information you provided.\n\n");
            sbHtml.append("Attached is the estimate file in PDF format. <br/>You may need a <a href='http://get.adobe.com/reader/'>reader software</a> to view its content.\n");
            sbText.append("Attached is the estimate file in PDF format. \nTo view its content, you may need a reader software, which you can download free from 'http://get.adobe.com/reader/'.\n");

            sbHtml.append("<br/><br/><br/>  <img src='cid:").append(cid).append("' alt='La Sierra University' /></body></html>");
            sbText.append("\n\nLa Sierra University");

            // set the html message
            email.setHtmlMsg(sbHtml.toString());//"<html>The La Sierra University logo - <img src=\"cid:"+cid+"\"></html>");
            // set the alternative message
            email.setTextMsg(sbText.toString());//"Your email client does not support HTML messages.");

            log.info("==================toEmail()   will attache files ...........................");
            // Create the attachment. can add an unlimited number of attachments either inline or attached. The attachments will be MIME encoded.
            EmailAttachment attachment = new EmailAttachment();
            //for img not on localhost
            /*
            try {
                attachment.setURL(new URL("http://www.apache.org/images/asf_logo_wide.gif"));
            } catch (MalformedURLException ex) {
                java.util.logging.Logger.getLogger(Estimator.class.getName()).log(Level.SEVERE, null, ex);
            }*/

            //     attachment.setPath("/var/estimator-files/"+pdfname);//estimator2012_t.pdf"); //on localhost
            attachment.setPath(pdffolder + "/" + pdfname);//estimator2012_t.pdf");
            attachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachment.setDescription("La Sierra University Estimate in PDF format");
            attachment.setName(pdfname);//"Student_estimate.pdf");
            // add the attachment
            email.attach(attachment);

            log.info("==================toEmail()  will set smtp server info and auth info ...........................");
            //smtp server and authentication info
            email.setHostName(smtpServer);//smtp.lasierra.edu");
            email.setSmtpPort(smtpPort);//465);  //gmail Port for TLS/STARTTLS: 587  Port for SSL: 465
            //  email.setAuthenticator; //gmail required . no exe atatchement, max size=25MB //new DefaultAuthenticator("estimator@lasierra.edu", "lasu3stimate")
            email.setSSL(true);
            email.setTLS(true);

            log.info("==================toEmail() tries to send email ...........................");
            ////           email.setDebug(true);
            // send the email
            //     email.buildMimeMessage();
            //email.isSSL();
            //email.isTLS();
            //email.setBounceAddress("");
            //email.setCc("");
            //       email.setSSL(true);
            //email.setMailSession(null);
            //email.setSocketTimeout(notestot);
            //email.setSocketConnectionTimeout(notestot);            

            email.send();
            log.info("==================toEmail() sent email ...........................");
            msg = "sent estimate file '" + pdfname + "' to server " + smtpServer + " targetting " + emailbox + " (the email server may bounce or reject the email if recipient email box does not exist or is invalid/inactive or reaches capacity limit)";
        } catch (Exception ex) { // java.lang.IllegalStateException: WEB9031: WebappClassLoader unable to load resource [org.apache.commons.mail.EmailException], because it has not yet been started, or was already stopped
            log.info("...toEmail() meets EmailException:", ex);
            msg = "Failed to send estimate to " + emailbox + " since: " + ex.getMessage();
        }
        return msg;
    }

    private String sendEmail(String pdfname, String emailbox, String receiverName) {
        String smtpServer = "smtp.gmail.com";
        int smtpPort = 587;
        String senderBox = "enroll@lasierra.edu";
        String senderName = "La Sierra University Enrollment Services Office";

        String msg = "";
        if (ref.isEmp(pdfname) || !pdfname.endsWith(".pdf")) {
            msg = "invalid file name. Can not email estimate.";
            return msg;
        }
        //check stud.getStudentEEmail() ???
        if (ref.isEmp(emailbox)) {
            msg = "invalid E-mail address '" + emailbox + "'. Can not email estimate.";
            return msg;
        }

        try {
            // Create the email message
            HtmlEmail email = new HtmlEmail();

            //email.addTo(stud.getStudentEEmail(),stud.getStudentCFirstname()+" "+ stud.getStudentBLastname());//"kwang@lasierra.edu", "Ken Wang");
            email.addTo(emailbox, receiverName);
            email.setFrom(senderBox, senderName);//"kwang@lasierra.edu", "LASU Ken Wang");
            email.setSubject("La Sierra University Estimate");//Test email with inline image and attachement");
            //           email.addBcc(senderBox, senderName);
            log.info("==================toEmail()  will embed image from network ...........................");

            // embed the image and get the content id
            URL url = null;
            try {
                url = new URL("http://vhost1.lasierra.edu/live/logo_2c.gif");
            } catch (MalformedURLException ex) {
                log.info("...toEmail() meets MalformedURLException:", ex);
                msg = ex.getMessage();
                return msg;
            }

            String cid = email.embed(url, "La Sierra University logo");

            log.info("==================toEmail()   will set message ...........................");
            StringBuilder sbHtml = new StringBuilder(512);
            StringBuilder sbText = new StringBuilder(512);
            sbHtml.append("<html><head><title>La Sierra University Estimate</title></head><body>\n");
            sbHtml.append("Dear <b>").append(stud.getStudentCFirstname()).append(' ').append(stud.getStudentBLastname()).append("</b>,<br/><br/>\n");
            sbText.append("Dear ").append(stud.getStudentCFirstname()).append(' ').append(stud.getStudentBLastname()).append(",\n\n");
            sbHtml.append("Thank you for choosing La Sierra University!<br/><br/>Your estimate is based on the data and information you provided.<br/><br/>\n");
            sbText.append("Thank you for choosing La Sierra University!\n\nYour estimate is based on the data and information you provided.\n\n");
            sbHtml.append("Attached is the estimate file in PDF format. <br/>You may need a <a href='http://get.adobe.com/reader/'>reader software</a> to view its content.\n");
            sbText.append("Attached is the estimate file in PDF format. \nTo view its content, you may need a reader software, which you can download free from 'http://get.adobe.com/reader/'.\n");

            sbHtml.append("<br/><br/><br/>  <img src='cid:").append(cid).append("' alt='La Sierra University' /></body></html>");
            sbText.append("\n\nLa Sierra University");

            // set the html message
            email.setHtmlMsg(sbHtml.toString());//"<html>The La Sierra University logo - <img src=\"cid:"+cid+"\"></html>");
            // set the alternative message
            email.setTextMsg(sbText.toString());//"Your email client does not support HTML messages.");

            log.info("==================toEmail()   will attache files ...........................");
            // Create the attachment. can add an unlimited number of attachments either inline or attached. The attachments will be MIME encoded.
            EmailAttachment attachment = new EmailAttachment();
            //for img not on localhost
            /*
            try {
                attachment.setURL(new URL("http://www.apache.org/images/asf_logo_wide.gif"));
            } catch (MalformedURLException ex) {
                java.util.logging.Logger.getLogger(Estimator.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            attachment.setPath("/var/estimator-files/" + pdfname);//estimator2012_t.pdf"); //on localhost
            attachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachment.setDescription("La Sierra University Estimate in PDF format");
            attachment.setName(pdfname);//"Student_estimate.pdf");
            // add the attachment
            email.attach(attachment);

            log.info("==================toEmail()  will set smtp server info and auth info ...........................");
            //smtp server and authentication info
            email.setHostName(smtpServer);//smtp.lasierra.edu");
            email.setSmtpPort(smtpPort);//465);  //gmail Port for TLS/STARTTLS: 587  Port for SSL: 465
            //  email.setAuthenticator(new DefaultAuthenticator("kwang@lasierra.edu", "xxxyyyzzz")); //gmail required . no exe atatchement, max size=25MB
            email.setTLS(true);

            log.info("==================toEmail() tries to send email ...........................");
            ////           email.setDebug(true);
            // send the email
            //     email.buildMimeMessage();
            //email.isSSL();
            //email.isTLS();
            //email.setBounceAddress("");
            //email.setCc("");
            //       email.setSSL(true);
            //email.setMailSession(null);
            //email.setSocketTimeout(notestot);
            //email.setSocketConnectionTimeout(notestot);            

            email.send();
            /*

INFO: DEBUG: JavaMail version 1.4.4
INFO: DEBUG: failed to load any providers, using defaults
INFO: DEBUG: Tables of loaded providers
INFO: DEBUG: Providers Listed By Class Name: {com.sun.mail.smtp.SMTPSSLTransport=javax.mail.Provider[TRANSPORT,smtps,com.sun.mail.smtp.SMTPSSLTransport,Sun Microsystems, Inc.,1.4.4], com.sun.mail.smtp.SMTPTransport=javax.mail.Provider[TRANSPORT,smtp,com.sun.mail.smtp.SMTPTransport,Sun Microsystems, Inc.,1.4.4], com.sun.mail.imap.IMAPSSLStore=javax.mail.Provider[STORE,imaps,com.sun.mail.imap.IMAPSSLStore,Sun Microsystems, Inc.,1.4.4], com.sun.mail.pop3.POP3SSLStore=javax.mail.Provider[STORE,pop3s,com.sun.mail.pop3.POP3SSLStore,Sun Microsystems, Inc.,1.4.4], com.sun.mail.imap.IMAPStore=javax.mail.Provider[STORE,imap,com.sun.mail.imap.IMAPStore,Sun Microsystems, Inc.,1.4.4], com.sun.mail.pop3.POP3Store=javax.mail.Provider[STORE,pop3,com.sun.mail.pop3.POP3Store,Sun Microsystems, Inc.,1.4.4]}
INFO: DEBUG: Providers Listed By Protocol: {imaps=javax.mail.Provider[STORE,imaps,com.sun.mail.imap.IMAPSSLStore,Sun Microsystems, Inc.,1.4.4], imap=javax.mail.Provider[STORE,imap,com.sun.mail.imap.IMAPStore,Sun Microsystems, Inc.,1.4.4], smtps=javax.mail.Provider[TRANSPORT,smtps,com.sun.mail.smtp.SMTPSSLTransport,Sun Microsystems, Inc.,1.4.4], pop3=javax.mail.Provider[STORE,pop3,com.sun.mail.pop3.POP3Store,Sun Microsystems, Inc.,1.4.4], smtp=javax.mail.Provider[TRANSPORT,smtp,com.sun.mail.smtp.SMTPTransport,Sun Microsystems, Inc.,1.4.4], pop3s=javax.mail.Provider[STORE,pop3s,com.sun.mail.pop3.POP3SSLStore,Sun Microsystems, Inc.,1.4.4]}
INFO: DEBUG: failed to load address map, using defaults
INFO: DEBUG: getProvider() returning javax.mail.Provider[TRANSPORT,smtp,com.sun.mail.smtp.SMTPTransport,Sun Microsystems, Inc.,1.4.4]
INFO: DEBUG SMTP: useEhlo true, useAuth true
INFO: DEBUG SMTP: useEhlo true, useAuth true
INFO: DEBUG SMTP: trying to connect to host "smtp.lasierra.edu", port 465, isSSL false
INFO: 22:45:19.951 [http-thread-pool-443(3)] INFO  edu.lsu.estimator.Estimator - ...toEmail() meets EmailException:
org.apache.commons.mail.EmailException: Sending the email to the following server failed : smtp.lasierra.edu:465

Caused by: javax.mail.MessagingException: Could not connect to SMTP host: smtp.lasierra.edu, port: 465
	at com.sun.mail.smtp.SMTPTransport.openServer(SMTPTransport.java:1934) ~[javax.mail.jar:1.4.4]
	at com.sun.mail.smtp.SMTPTransport.protocolConnect(SMTPTransport.java:638) ~[javax.mail.jar:1.4.4]
	at javax.mail.Service.connect(Service.java:317) ~[javax.mail.jar:1.4.4]
	at javax.mail.Service.connect(Service.java:176) ~[javax.mail.jar:1.4.4]
	at javax.mail.Service.connect(Service.java:125) ~[javax.mail.jar:1.4.4]
	at javax.mail.Transport.send0(Transport.java:194) ~[javax.mail.jar:1.4.4]
	at javax.mail.Transport.send(Transport.java:124) ~[javax.mail.jar:1.4.4]
	at org.apache.commons.mail.Email.sendMimeMessage(Email.java:1232) ~[commons-email-1.2.jar:1.2]
	... 48 common frames omitted
Caused by: javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
	at sun.security.ssl.Alerts.getSSLException(Alerts.java:192) ~[na:1.7.0_04-ea]
	at sun.security.ssl.SSLSocketImpl.fatal(SSLSocketImpl.java:1868) ~[na:1.7.0_04-ea]
	at sun.security.ssl.Handshaker.fatalSE(Handshaker.java:276) ~[na:1.7.0_04-ea]
	at sun.security.ssl.Handshaker.fatalSE(Handshaker.java:270) ~[na:1.7.0_04-ea]
	at sun.security.ssl.ClientHandshaker.serverCertificate(ClientHandshaker.java:1338) ~[na:1.7.0_04-ea]
	at sun.security.ssl.ClientHandshaker.processMessage(ClientHandshaker.java:154) ~[na:1.7.0_04-ea]
	at sun.security.ssl.Handshaker.processLoop(Handshaker.java:868) ~[na:1.7.0_04-ea]
	at sun.security.ssl.Handshaker.process_record(Handshaker.java:804) ~[na:1.7.0_04-ea]
	at sun.security.ssl.SSLSocketImpl.readRecord(SSLSocketImpl.java:998) ~[na:1.7.0_04-ea]
	at sun.security.ssl.SSLSocketImpl.performInitialHandshake(SSLSocketImpl.java:1294) ~[na:1.7.0_04-ea]
	at sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1321) ~[na:1.7.0_04-ea]
	at sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1305) ~[na:1.7.0_04-ea]
	at com.sun.mail.util.SocketFetcher.configureSSLSocket(SocketFetcher.java:507) ~[javax.mail.jar:1.4.4]
	at com.sun.mail.util.SocketFetcher.getSocket(SocketFetcher.java:238) ~[javax.mail.jar:1.4.4]
	at com.sun.mail.smtp.SMTPTransport.openServer(SMTPTransport.java:1900) ~[javax.mail.jar:1.4.4]
	... 55 common frames omitted
Caused by: sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
	at sun.security.validator.PKIXValidator.doBuild(PKIXValidator.java:385) ~[na:1.7.0_04-ea]
	at sun.security.validator.PKIXValidator.engineValidate(PKIXValidator.java:292) ~[na:1.7.0_04-ea]
	at sun.security.validator.Validator.validate(Validator.java:260) ~[na:1.7.0_04-ea]
	at sun.security.ssl.X509TrustManagerImpl.validate(X509TrustManagerImpl.java:326) ~[na:1.7.0_04-ea]
	at sun.security.ssl.X509TrustManagerImpl.checkTrusted(X509TrustManagerImpl.java:231) ~[na:1.7.0_04-ea]
	at sun.security.ssl.X509TrustManagerImpl.checkServerTrusted(X509TrustManagerImpl.java:126) ~[na:1.7.0_04-ea]
	at sun.security.ssl.ClientHandshaker.serverCertificate(ClientHandshaker.java:1320) ~[na:1.7.0_04-ea]
	... 65 common frames omitted
Caused by: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target

using a workaround.
Disabling all SSL errors

The command-line version is working again when adding :
java.lang.System.setProperty(“sun.security.ssl.allowUnsafeRenegotiation”, “true”);

Or in JVM option add:
“-Dsun.security.ssl.allowUnsafeRenegotiation=true”
* 
* the above still not working
* 
* *****fix step 1 get server public key
openssl s_client -connect smtp.lasierra.edu:smtps
openssl s_client -connect imap.lasierra.edu:imaps

in the output,  Cut and paste the certificate (including BEGIN and END lines) into a local file (eg. imapd.pem).

**** step 2 Import the public key
sudo keytool -import -alias mail.yourcompany.com -keystore $JAVA_HOME/jre/lib/security/cacerts -file imapd.pem
Enter keystore password:  changeit
 
it will import the public key (imapd.pem) into Java's default keystore, and marks it as trusted. 

* 
Restart the app server
Restart, and if everything is correct, your webapp should now connect to the SSL resource without problems.
* 
Java will normally use a system-wide keystore in $JAVA_HOME/jre/lib/security/cacerts, but it is possible to use a different keystore by specifying a parameter,
 -Djavax.net.ssl.trustStore=/path/to/keystore, where '/path/to/keystore' is the absolute file path of the alternative keystore. 
 * 
The openssl commands are very useful for debugging SSL problems. For instance, to print the server's certificate:
~$ openssl s_client -showcerts  -connect localhost:443 2>/dev/null  
             */

            log.info("==================toEmail() sent email ...........................");
            msg = "sent estimate file '" + pdfname + "' to server " + smtpServer + " targetting " + emailbox + " (the email server may bounce or reject the email if recipient email box does not exist or is invalid/inactive or reaches capacity limit)";
        } catch (Exception ex) { // java.lang.IllegalStateException: WEB9031: WebappClassLoader unable to load resource [org.apache.commons.mail.EmailException], because it has not yet been started, or was already stopped
            log.info("...toEmail() meets EmailException:", ex);
            msg = "Faield to send estimate to " + emailbox + " since: " + ex.getMessage();
        }
        return msg;
    }

    //(String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("dirty");
    public String makeuploadprints() {//for saved student record
        //by user 25 only
        List<String> recs = (List<String>) em.createNativeQuery("select a.recid from student a where a.student_fisy=? and  a.dup>0 and not exists ( select '1' from prints b where b.recid = a.recid)")
                .setParameter(1, ref.getFiscal_year())
                .getResultList();
        if (recs == null || recs.size() == 0) {
            return null;
        }

        int prtot = 0, prtgenok = 0, prtgenfail = 0, prtsaveok = 0, prtsavefail = 0;
        edu.lsu.estimator.PDFgen pdf = new edu.lsu.estimator.PDFgen();
        for (String onerecid : recs) { //Integer cannot be cast to java.lang.String
            prtot++;
            Student onestd = em.find(Student.class, onerecid);
            calc.setStud(onestd);

            //estimator also holds some values for the front web page
            //syncFromStudObj(); //will call calc.setStud(stud); //if use this method, should deep copy stud obj, and copy back at the end
            //new PDFgen().servePDF(stud);
            //PDFgen pdf = new PDFgen();
            edu.lsu.estimator.Print prt = new edu.lsu.estimator.Print();
            prt.setSship1ExtAmt(0);
            prt.setSship2ExtAmt(0);
            prt.setSship7ExtAmt(0);
            prt.setSship8ExtAmt(0);
            prt.setSship9ExtAmt(0);

            log.info("vvvvvvvvvvvvvvvvvvvvvvvvvvv  to be generated print has recid=%s ( find by %s)", onestd.getRecid(), onerecid);
            String msg = pdf.genPDF(onestd, login, ref, calc, prt, 3); //will invoke:  calc.init();       calc.initAndShowPellGrantAmt();     calc.refreshCalc(onestd)
            if (!msg.startsWith("Failed")) {//                 msg==null||msg.isEmpty()){
                log.info("==================makeuploadprints() gen pdf: pass");
                prtgenok++;

                prt.setCounselorId(onestd.getCounselorId());
                log.info("vvvvvvvvvvvvvvvvvvvvvvvvvvv  to be saved print has recid=%s ", prt.getRecid());
                msg = savePrint(prt, 3);//0);
                if (msg == null || msg.isEmpty()) {
                    log.info("==================makeuploadprints() save print rec: pass");
                    prtsaveok++;
                } else {
                    log.info("==================makeuploadprints() save print for %s: failed. msg=%s", onerecid, msg);
                    prtsavefail++;
                }
            } else {
                log.info("==================makeuploadprints() gen print for %s: failed. msg=%s", onerecid, msg);
                prtgenfail++;
            }
        }
        FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_INFO, "Targets:" + prtot + "   GenPrints: ok " + prtgenok + "/fail " + prtgenfail + "    SavePrints: ok " + prtsaveok + "/fail " + prtsavefail);
        facesContext.addMessage(null, guimsg);

        calc.setStud(stud);
        calc.refreshCalc(stud);
        return null;
    }

    public String makeprints() { //for saved student record, assuming it is valid
        //check existence first
        //then loop
        List<String> recs = (List<String>) em.createNativeQuery("select a.recid from student a where a.student_fisy=? and a.lost_time=0 and a.pickup_ind=1 and not exists ( select '1' from prints b where b.recid = a.recid)")
                .setParameter(1, ref.getFiscal_year())
                .getResultList();
        if (recs == null || recs.size() == 0) {
            return null;
        }

        int prtot = 0, prtgenok = 0, prtgenfail = 0, prtsaveok = 0, prtsavefail = 0;
        edu.lsu.estimator.PDFgen pdf = new edu.lsu.estimator.PDFgen();
        for (String onerecid : recs) { //Integer cannot be cast to java.lang.String
            prtot++;
            Student onestd = em.find(Student.class, onerecid);
            calc.setStud(onestd);

            //estimator also holds some values for the front web page
            //syncFromStudObj(); //will call calc.setStud(stud); //if use this method, should deep copy stud obj, and copy back at the end
            //new PDFgen().servePDF(stud);
            //PDFgen pdf = new PDFgen();
            edu.lsu.estimator.Print prt = new edu.lsu.estimator.Print();
            prt.setSship1ExtAmt(0);
            prt.setSship2ExtAmt(0);
            prt.setSship7ExtAmt(0);
            prt.setSship8ExtAmt(0);
            prt.setSship9ExtAmt(0);

            log.info("vvvvvvvvvvvvvvvvvvvvvvvvvvv  to be generated print has recid=%s ( find by %s)", onestd.getRecid(), onerecid);
            String msg = pdf.genPDF(onestd, login, ref, calc, prt, 3); //will invoke:  calc.init();       calc.initAndShowPellGrantAmt();     calc.refreshCalc(onestd)
            if (!msg.startsWith("Failed")) {//                 msg==null||msg.isEmpty()){
                log.info("==================makeprints() gen pdf: pass");
                prtgenok++;

                prt.setCounselorId(onestd.getCounselorId());
                log.info("vvvvvvvvvvvvvvvvvvvvvvvvvvv  to be saved print has recid=%s ", prt.getRecid());
                msg = savePrint(prt, 3);//0);
                if (msg == null || msg.isEmpty()) {
                    log.info("==================makeprints() save print rec: pass");
                    prtsaveok++;
                } else {
                    log.info("==================makeprints() save print for %s: failed. msg=%s", onerecid, msg);
                    prtsavefail++;
                }
            } else {
                log.info("==================makeprints() gen print for %s: failed. msg=%s", onerecid, msg);
                prtgenfail++;
            }
        }
        FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_INFO, "Targets:" + prtot + "   GenPrints: ok " + prtgenok + "/fail " + prtgenfail + "    SavePrints: ok " + prtsaveok + "/fail " + prtsavefail);
        facesContext.addMessage(null, guimsg);

        calc.setStud(stud);
        calc.refreshCalc(stud);
        return null;
    }

    public String toPDF() {//it is action.    savestudinfo() is actionlistener which come after all phases of JSF (include update model --where BV lives)
        log.info("==================toPDF() pdf button clicked ...........................");

        String ln = stud.getStudentBLastname();
        String fn = stud.getStudentCFirstname();
        //Date 
        String dob = stud.getStudentDDob();
        String phone = stud.getStudentFPhone();
        String msg = chkStudBasicInfo();
        log.info("==================toPDF() got chkStudBasicInfo() res msg=%s", msg);
        if (!msg.isEmpty()) {
            return null;
        }

        int vio = validateStudBean();
        log.info("==================toPDF() got validateStudBean() res code=%d", vio);
        if (vio > 0) {
            return null;
        }

        /*
        if(ln==null || ln.isEmpty() || fn==null ||fn.isEmpty() || dob==null || phone==null || phone.isEmpty()){
            log.info("!!!!!! toPDF() found ln==null? %s, fn==null? %s, dob==null? %s, phone==null? %s", ln==null || ln.isEmpty(), fn==null ||fn.isEmpty(), dob==null, phone==null || phone.isEmpty());
            
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidData");    
             if(facesContext!=null )facesContext.addMessage(null, msg);   
             //log.info("==================toPDF() invalid stud data:  ...while stud sex=%s", stud.getSex());  
             return ;
        } */
        stud.setStudentUserName(compUserName(stud.getStudentALsuid(), ln, fn, dob)); //used to comp when merging dup/conflict among those picked==1 (upload download)
        msg = saveStud(1);

        log.info("==================toPDF() stud saving: %s ........", msg);
        if (msg == null || msg.isEmpty()) {
            FacesMessage guimsg = ref.facesMessageByKey(FacesMessage.SEVERITY_INFO, "EstimateForm.DataSaved");
            if (facesContext != null) {
                facesContext.addMessage(null, guimsg);
            }
            stud.setStudentNumb((Integer) null);

            //new PDFgen().servePDF(stud);
            PDFgen pdf = new PDFgen();
            Print prt = new Print();
            prt.setSship1ExtAmt(0);
            prt.setSship2ExtAmt(0);
            prt.setSship7ExtAmt(0);
            prt.setSship8ExtAmt(0);
            prt.setSship9ExtAmt(0);

            msg = pdf.genPDF(stud, login, ref, calc, prt, 1);
            if (!msg.startsWith("Failed")) {//                 msg==null||msg.isEmpty()){
                log.info("==================toPDF() gen pdf: pass");
                String pdfname = msg;
                //save prints table
                msg = savePrint(prt, 1);

                if (msg == null || msg.isEmpty()) {
                    log.info("==================toPDF() save print rec: pass");
                    //and download pdf
                    msg = pdf.downloadPDF(pdfname);

                    if (msg != null && !msg.isEmpty()) {
                        log.info("==================toPDF() download pdf: %s ........", msg);
                        //update prints downloaded flag ind?
                    }
                    ////           stud.setPrtTimes( stud.getPrtTimes()+1);
                    msg = accessor.updateStudPrtTimes(stud);
                    if (msg != null && !msg.isEmpty()) {
                        log.info("==================toPDF() failed to update stud obj %s with msg: %s", stud.getRecid(), msg);
                    }
                } else {
                    FacesMessage pdfmsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    if (facesContext != null) {
                        facesContext.addMessage(null, pdfmsg);
                    }
                    log.info("==================toPDF() failed to save print rec: %s ........", msg);
                }
            } else {
                log.info("==================toPDF() gen pdf failed: %s ........", msg);
                FacesMessage pdfmsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                if (facesContext != null) {
                    facesContext.addMessage(null, pdfmsg);
                }
            }

        } else if (!msg.equals("processed")) {
            FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
            if (facesContext != null) {
                facesContext.addMessage(null, guimsg);
            }
        }
        return null;
    }

    private String savePrint(Print pdf, int mode, Integer... p) {
        String msg = "";

        boolean pu = p.length > 0;

        pdf.setPrtNum(null);
        //INFO: ########## Path: fisyPrt ########### FAILED: may not be null
        pdf.setFisy(stud.getStudentFisy()); //which shall get fist from ref, which in turn get from config table during startup process

        pdf.setFisyPrt(ref.getFaid_year()); //2012-2013
        pdf.setClientId(ref.getClientid()); //may be diff from stud client

        if (mode > 0 && mode < 3) {//output
            pdf.setPrtTime(System.currentTimeMillis());

            //pu will not mod old one
            pdf.setCounselorId(login.getCurrentUser().getUserid());
            pdf.setCounselorName(login.getCurrentUser().getUsername());

            pdf.setRecid(stud.getRecid());
        } else {//just save a new or modified record
            pdf.setPrtTime(0);

            if (pu) {
                pdf.setCounselorId(ref.getSys_counselor_id());
            } else {
                pdf.setCounselorId(login.getCurrentUser().getUserid());
            }
        }
        pdf.setPrtTz(ref.getTzSN());

        //  prtId ########### FAILED: may not be null --will be composed by trigger (counselor:client:seqnumb)
        Random rand = new Random();
        int rand_int1 = rand.nextInt(10000);
        pdf.setPrtId("tmpid");
        log.info("vvvvvvvvvvvvvvvvvvvvvvvvvvv  to be saved print has stud recid=%s, counselorid=%d, mode=%d", pdf.getRecid(), pdf.getCounselorId(), mode);

        msg = accessor.savePrt(pdf);        //javax.validation.ConstraintViolationException: Bean Validation constraint(s) violated while executing Automatic Bean Validation on callback event:'prePersist'. Please refer to embedded ConstraintViolations for details.

        if (msg == null || msg.isEmpty()) {
            int seq = info.getNowPrtNumbInQueue();
            pdf.setPrtNum(seq);
            pdf.setPrtId(info.getPrintPrtid(pdf.getCounselorId(), pdf.getClientId(), pdf.getPrtNum()));
            //stud.setStudentNumb(seq);
            //stud.setRecid( info.getStudRecid(stud.getCounselorId(), stud.getClientId(),  stud.getStudentNumb()));
        }
        List<Print> pdfs = stud.getPdfs();
        if (pdfs == null) {
            pdfs = new ArrayList<Print>();
        }
        pdfs.add(pdf);
        stud.setPdfs(pdfs);
        return msg;
    }

    private String checkScholarships(Integer... p) {
        boolean pu = p.length > 0;
        boolean np = p.length == 0;

        String msg = "";
        int miss = 0, err = 0;
        if (stud.getStudentAuScholarship1Amt() > 0) {
            if (ref.fundNotesReqById(stud.getFund1id()) > 0) {
                if (stud.getStudentAtScholarship1Note() == null || stud.getStudentAtScholarship1Note().trim().isEmpty()) {//isEmpty() Returns true if, and only if, length() is 0. W/O trim().
                    msg = "Notes for Scholarship '" + stud.getStudentAsScholarship1Name() + "' is Required.";

                    if (np) {
                        FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                        if (facesContext != null) {
                            facesContext.addMessage(null, guimsg);
                        }
                    }
                    miss++;
                }
            }
            int max = ref.fundMatchTopById(stud.getFund1id());
            int in = stud.getStudentAuScholarship1Amt();
            if (np && (user_over_maxamt(login.getCurrentUser().getSuperuser(), in, sfund1amt, max) < 1)) {
                //if( in>max){
                //UIInput source = (UIInput)event.getSource(); //the object on which the event initially occoured
                //source.setValid(false);
                UIComponent c = getUIComponentById("ship1amt"); //auth-sv-ptab-std_dob
                //if( c==null) facesContext.getViewRoot().findComponent("std_dob");
                UIInput textInput = (UIInput) c;
                textInput.setValid(false);

                FacesMessage guimsg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship1.amtoverflow");
                if (facesContext != null) {
                    facesContext.addMessage(null, guimsg);
                }
                msg = guimsg.getSummary();
                err++;
            }
            if (pu && in > max) {
                msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship1.amtoverflow").getSummary();
                err++;
            }

        } else {
            stud.setStudentAtScholarship1Note("");
        }

        if (stud.getStudentAxScholarship2Amt() > 0) {
            if (ref.fundNotesReqById(stud.getFund2id()) > 0) {
                if (stud.getStudentAwScholarship2Note() == null || stud.getStudentAwScholarship2Note().trim().isEmpty()) {
                    msg = "Notes for Scholarship '" + stud.getStudentAvScholarship2Name() + "' is Required.";
                    if (np) {
                        FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                        if (facesContext != null) {
                            facesContext.addMessage(null, guimsg);
                        }
                    }
                    miss++;
                }
            }
            int max = ref.fundMatchTopById(stud.getFund2id());
            int in = stud.getStudentAxScholarship2Amt();
            if (np && (user_over_maxamt(login.getCurrentUser().getSuperuser(), in, sfund2amt, max) < 1)) {

                //2013-02
                if (login.getCurrentUser().getSuperuser() == 0 && in <= ref.getUniversity_grant_hard_top_limit()) {
                    //allow normal user go beyond limit, but still a hidden limit 30K
                } else {
                    //if( in>max){
                    //UIInput source = (UIInput)event.getSource(); //the object on which the event initially occoured
                    //source.setValid(false);
                    UIComponent c = getUIComponentById("ship2amt"); //auth-sv-ptab-std_dob
                    //if( c==null) facesContext.getViewRoot().findComponent("std_dob");
                    UIInput textInput = (UIInput) c;
                    textInput.setValid(false);

                    FacesMessage guimsg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship2.amtoverflow");
                    if (facesContext != null) {
                        facesContext.addMessage(null, guimsg);
                    }
                    msg = guimsg.getSummary();
                    err++;
                }
            }
            if (pu && in > max) {
                msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship2.amtoverflow").getSummary();
                err++;
            }
        } else {
            stud.setStudentAwScholarship2Note("");
        }

        if (stud.getStudentBaScholarship3Amt() > 0) {
            if (ref.fundNotesReqById(stud.getFund3id()) > 0) {
                if (stud.getStudentAzScholarship3Note() == null || stud.getStudentAzScholarship3Note().trim().isEmpty()) {
                    msg = "Notes for Scholarship '" + stud.getStudentAyScholarship3Name() + "' is Required.";
                    if (np) {
                        FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                        if (facesContext != null) {
                            facesContext.addMessage(null, guimsg);
                        }
                    }
                    miss++;
                }
            }
        } else {
            stud.setStudentAzScholarship3Note("");
        }

        if (stud.getStudentBdScholarship4Amt() > 0) {
            if (ref.fundNotesReqById(stud.getFund4id()) > 0) {
                if (stud.getStudentBcScholarship4Note() == null || stud.getStudentBcScholarship4Note().trim().isEmpty()) {
                    msg = "Notes for Scholarship '" + stud.getStudentBbScholarship4Name() + "' is Required.";
                    if (np) {
                        FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                        if (facesContext != null) {
                            facesContext.addMessage(null, guimsg);
                        }
                    }
                    miss++;
                }
            }
        } else {
            stud.setStudentBcScholarship4Note("");
        }

        if (stud.getStudentBgScholarship5Amt() > 0) {
            if (ref.fundNotesReqById(stud.getFund5id()) > 0) {
                if (stud.getStudentBfScholarship5Note() == null || stud.getStudentBfScholarship5Note().trim().isEmpty()) {
                    msg = "Notes for Scholarship '" + stud.getStudentBeScholarship5Name() + "' is Required.";
                    if (np) {
                        FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                        if (facesContext != null) {
                            facesContext.addMessage(null, guimsg);
                        }
                    }
                    miss++;
                }
            }
        } else {
            stud.setStudentBfScholarship5Note("");
        }

        if (stud.getStudentBjScholarship6Amt() > 0) {
            if (ref.fundNotesReqById(stud.getFund6id()) > 0) {
                if (stud.getStudentBiScholarship6Note() == null || stud.getStudentBiScholarship6Note().trim().isEmpty()) {
                    msg = "Notes for Scholarship '" + stud.getStudentBhScholarship6Name() + "' is Required.";
                    if (np) {
                        FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                        if (facesContext != null) {
                            facesContext.addMessage(null, guimsg);
                        }
                    }
                    miss++;
                }
            }
        } else {
            stud.setStudentBiScholarship6Note("");
        }

        int fundid = 0;
        if (stud.getStudentBmScholarship7Amt() > 0) {
            fundid = stud.getFund7id() == null ? 0 : stud.getFund7id(); //NPE ???????
            if (fundid != stdo_fundid7) {
                stud.setFund7id(stdo_fundid7);
                fundid = stdo_fundid7;
            }
            if (fundid == 0) {
                //stud.setStudentBkScholarship7Name("");
                //stud.setStudentBlScholarship7Note("");
                //stud.setStudentBmScholarship7Amt(0);
                msg = "Scholarship #7 name is not specified.";
                if (np) {
                    FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    if (facesContext != null) {
                        facesContext.addMessage(null, guimsg);
                    }
                }
            } else {
                //              log.info(" scholarship7 needs notes? %s", ref.fundNotesReqById(fundid )); // scholarship7 needs notes? 1
                if (ref.fundNotesReqById(fundid) > 0) {
                    //                  log.info("sshi7 notes=[%s], not supplied? %s, ", stud.getStudentBlScholarship7Note(), ref.isEmp(stud.getStudentBlScholarship7Note()));
                    if (ref.isEmp(stud.getStudentBlScholarship7Note())) {//stud.getStudentBlScholarship7Note() ==null || stud.getStudentBlScholarship7Note().isEmpty()){
                        msg = "Notes for Scholarship #7 '" + stud.getStudentBkScholarship7Name() + "' is Required.";
                        if (np) {
                            FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                            if (facesContext != null) {
                                facesContext.addMessage(null, guimsg);
                            }
//                        log.info("sshi7 notes is not supplied");
                            UIComponent c = getUIComponentById("ship7note");
                            UIInput textInput = (UIInput) c;
                            textInput.setValid(false);
                        }
                        miss++;
                    }
                }
                int max = ref.fundMatchTopById(stud.getFund7id());
                int in = stud.getStudentBmScholarship7Amt();
                //               log.info("sship7 max=%d, in=%d, former=%d, override? %d", max, in, sfund7amt, user_over_maxamt(0, in, sfund7amt, max ));
                //sship7 max=4100, in=4444, override? 1
                if (np && (user_over_maxamt(login.getCurrentUser().getSuperuser(), in, sfund7amt, max) < 1)) { //login.getCurrentUser().getSuperuser()
                    //if( in>max){
                    //UIInput source = (UIInput)event.getSource(); //the object on which the event initially occoured
                    //source.setValid(false);
                    UIComponent c = getUIComponentById("ship7amt"); //auth-sv-ptab-std_dob
                    //if( c==null) facesContext.getViewRoot().findComponent("std_dob");
                    UIInput textInput = (UIInput) c;
                    textInput.setValid(false);

                    FacesMessage guimsg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship7.amtoverflow");
                    if (facesContext != null) {
                        facesContext.addMessage(null, guimsg);
                    }
                    msg = guimsg.getSummary();
                    err++;
                }
                if (pu && in > max) {
                    msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship7.amtoverflow").getSummary();
                    err++;
                }
            }
        } else {
//log.info("/////////////////estimator checkScholarships() reset scholarship #7 id=0 since amt<=0 while stud recid=%s", stud.getRecid());            
            stud.setFund7id(0);
            stud.setStudentBkScholarship7Name("");
            stud.setStudentBlScholarship7Note("");
        }

        if (stud.getStudentBpScholarship8Amt() > 0) {
            fundid = stud.getFund8id() == null ? 0 : stud.getFund8id(); //NPE ???????
            if (fundid != stdo_fundid8) {
                stud.setFund8id(stdo_fundid8);
                fundid = stdo_fundid8;
            }
            if (fundid == 0) {
                //stud.setStudentBnScholarship8Name("");
                //stud.setStudentBoScholarship8Note("");
                //stud.setStudentBpScholarship8Amt(0);
                msg = "Scholarship #8 name is not specified.";
                if (np) {
                    FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    if (facesContext != null) {
                        facesContext.addMessage(null, guimsg);
                    }
                }
            } else {
                log.info(" scholarship8 needs notes? %s", ref.fundNotesReqById(fundid));
                if (ref.fundNotesReqById(stud.getFund8id()) > 0) {
                    if (ref.isEmp(stud.getStudentBoScholarship8Note())) {// stud.getStudentBoScholarship8Note() ==null || stud.getStudentBoScholarship8Note().isEmpty()){
                        msg = "Notes for Scholarship #8 '" + stud.getStudentBnScholarship8Name() + "' is Required.";
                        if (np) {
                            FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                            if (facesContext != null) {
                                facesContext.addMessage(null, guimsg);
                            }
                            UIComponent c = getUIComponentById("ship8note");
                            UIInput textInput = (UIInput) c;
                            textInput.setValid(false);
                        }
                        miss++;
                    }
                }
                int max = ref.fundMatchTopById(stud.getFund8id());
                int in = stud.getStudentBpScholarship8Amt();
                if (np && (user_over_maxamt(login.getCurrentUser().getSuperuser(), in, sfund8amt, max) < 1)) {
                    //if( in>max){
                    //UIInput source = (UIInput)event.getSource(); //the object on which the event initially occoured
                    //source.setValid(false);
                    UIComponent c = getUIComponentById("ship8amt"); //auth-sv-ptab-std_dob
                    //if( c==null) facesContext.getViewRoot().findComponent("std_dob");
                    UIInput textInput = (UIInput) c;
                    textInput.setValid(false);

                    FacesMessage guimsg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship8.amtoverflow");
                    if (facesContext != null) {
                        facesContext.addMessage(null, guimsg);
                    }
                    msg = guimsg.getSummary();
                    err++;
                }
                if (pu && in > max) {
                    msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship8.amtoverflow").getSummary();
                    err++;
                }
            }
        } else {
            stud.setFund8id(0);
            stud.setStudentBnScholarship8Name("");
            stud.setStudentBoScholarship8Note("");
        }

        if (stud.getStudentBsScholarship9Amt() > 0) {
            fundid = stud.getFund9id() == null ? 0 : stud.getFund9id(); //NPE ???????
            if (fundid != stdo_fundid9) {
                stud.setFund9id(stdo_fundid9);
                fundid = stdo_fundid9;
            }

            if (fundid == 0) {
                //stud.setStudentBqScholarship9Name("");
                //stud.setStudentBrScholarship9Note("");
                //stud.setStudentBsScholarship9Amt(0);
                msg = "Scholarship #9 name is not specified.";
                if (np) {
                    FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                    if (facesContext != null) {
                        facesContext.addMessage(null, guimsg);
                    }
                }
            } else {
                log.info(" scholarship9 needs notes? %s", ref.fundNotesReqById(fundid));
                if (ref.fundNotesReqById(stud.getFund9id()) > 0) {
                    if (ref.isEmp(stud.getStudentBrScholarship9Note())) {// stud.getStudentBrScholarship9Note() ==null || stud.getStudentBrScholarship9Note().isEmpty()){
                        msg = "Notes for Scholarship #9 '" + stud.getStudentBqScholarship9Name() + "' is Required.";
                        FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
                        if (np) {
                            if (facesContext != null) {
                                facesContext.addMessage(null, guimsg);
                            }
                            UIComponent c = getUIComponentById("ship9note");
                            UIInput textInput = (UIInput) c;
                            textInput.setValid(false);
                        }
                        miss++;
                    }
                }
                int max = ref.fundMatchTopById(stud.getFund9id());
                int in = stud.getStudentBsScholarship9Amt();
                if (np && (user_over_maxamt(login.getCurrentUser().getSuperuser(), in, sfund9amt, max) < 1)) {
                    //if( in>max){
                    //UIInput source = (UIInput)event.getSource(); //the object on which the event initially occoured
                    //source.setValid(false);
                    UIComponent c = getUIComponentById("ship9amt"); //auth-sv-ptab-std_dob
                    //if( c==null) facesContext.getViewRoot().findComponent("std_dob");
                    UIInput textInput = (UIInput) c;
                    textInput.setValid(false);

                    FacesMessage guimsg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship9.amtoverflow");
                    if (facesContext != null) {
                        facesContext.addMessage(null, guimsg);
                    }
                    msg = guimsg.getSummary();
                    err++;
                }
                if (pu && in > max) {
                    msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship9.amtoverflow").getSummary();
                    err++;
                }
            }
        } else {
            stud.setFund9id(0);
            stud.setStudentBqScholarship9Name("");
            stud.setStudentBrScholarship9Note("");
        }

        int diffs = 0, awdid = 0;
        HashMap<Integer, Integer> map = new HashMap<>(3);
        if ((awdid = stud.getFund7id()) > 0) {
            map.put(awdid, awdid);
            diffs++;
        }
        if ((awdid = stud.getFund8id()) > 0) {
            map.put(awdid, awdid);
            diffs++;
        }
        if ((awdid = stud.getFund9id()) > 0) {
            map.put(awdid, awdid);
            diffs++;
        }
        if (np && (diffs > map.size())) {// map.keySet().size();
            FacesMessage guimsg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarships.dup");
            if (facesContext != null) {
                facesContext.addMessage(null, guimsg);
            }
            msg = guimsg.getSummary();
            log.info(" sssssssssssss dup scholarships. valids=%d, map size=%d, key set size=%d", diffs, map.size(), map.keySet().size());
            for (int k : map.keySet()) {
                log.info(" sssssssssss key=[%d]", k);
            }
        }
        if (pu && diffs > map.size()) {
            msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarships.dup").getSummary();
        }

        if (np && (miss + err > 0 && ref.isEmp(msg))) {
            msg = "Some scholarship needs notes or is over limit";
        }

        return msg;
    }

    private String saveStud(int mode, int... p) {
        //20140303 added vararg
        boolean pu = p.length > 0;

        String msg = "";
        /*chkStudBasicInfo();
        if( !msg.isEmpty()){
            return msg;
        }*/

        //2013-02
//        log.info("XXXXXX stud obj FAFSA attr=%s while checkbox=%s and stdo attr=%s , in saveStud(%d)", stud.getStudentXFafsa(), std_fafsa, stdo_fafsa, mode);

        //it seems PF32 itself automatically and magically changed the checkbox value without user interaction.
        //so reset them to the stdo values kept from eventer lisenter ( of field change event)
        //no if ?
        if (pu == false) {
            stud.setPuser_id(null);

            std_fafsa = stdo_fafsa;
            stud.setStudentXFafsa(std_fafsa ? "Yes" : "No");

            std_calgrant = stdo_calgrant;
            stud.setStudentZCalgrant(std_calgrant ? "Yes" : "No");

            std_ealsu = stdo_ealsu;
            stud.setIndEalsu(std_ealsu ? "yes" : "no");

            std_eanonlsu = stdo_eanonlsu;
            stud.setIndEanonlsu(std_eanonlsu ? "yes" : "no");

            std_efc = stdo_efc;
            stud.setIndEfc(std_efc ? "Yes" : "No");

            std_noloans = stdo_noloans;
            stud.setIndExcloans(std_noloans ? "yes" : "no");

            std_dorm = stdo_dorm;
            stud.setStudentWDorm(std_dorm ? "yes" : "no");

            in_subloan = stdo_in_subloan;
            stud.setStudentApSubLoans(in_subloan ? "Yes" : "No");

            in_unsubloan = stdo_in_unsubloan;
            stud.setStudentAqUnsubLoans(in_unsubloan ? "Yes" : "No");

            in_fws = stdo_in_fws;
            stud.setStudentArFws(in_fws ? "Yes" : "No");

            std_intl = stdo_intl;
            stud.setStudentLIntlStud(std_intl ? "yes" : "no");

            return_std_ind = stdo_return_ind;
            stud.setReturnStdInd(return_std_ind ? 1 : 0);

            nc_std_ind = stdo_nc_ind;
            stud.setNcStdInd(nc_std_ind ? 1 : 0);

            std_marry = stdo_marry;
            stud.setStudentMMarry(std_marry ? "yes" : "no");

            std_sda = stdo_sda;
            stud.setStudentNSda(std_sda ? "yes" : "no");

            std_indept = stdo_indept;
            stud.setStudentYIndept(std_indept ? "yes" : "no");

            std_sex = stdo_sex;
            stud.setSex(std_sex);

            std_merits = stdo_merits;
            stud.setStudentSMerit(std_merits);

            //pf32 to stud, then kept in stdo_academic;
            stud.setStudentUAcademic(stdo_academic);
        }

        stud.setEa_nonlsu_perc(stud.getStudentAiEduAllowPer().multiply(new BigDecimal(100)).intValue());
//        log.info("===$$$$$$$$$$$$$$$$$$$$$$$$$ calc got ea_nonlsu_perc=%f, so perc=%d", stud.getStudentAiEduAllowPer(), stud.getEa_nonlsu_perc());

        if (stud.getStd_transfer_ind() == null) {
            stud.setStd_transfer_ind(-1);
        }
        if (stud.getStd_1st_freshmen() == null) {
            stud.setStd_1st_freshmen(-1);
        }

        if (stud.getNcStdInd() == null) {
            stud.setNcStdInd(0);
        }
        if (stud.getReturnStdInd() == null) {
            stud.setReturnStdInd(0);
        }

        if (pu == false) {
            msg = checkScholarships();
        } else {
            msg = checkScholarships(1);
        }

        if (msg != null && !msg.isEmpty()) {
            log.info("/////////saveStud() got non-empty checking msg from checkScholarships(): %s", msg);
            //return "sships";
            return msg;
        }
        if (stud.getStudentUAcademic() == null) {
            if (ref.isEmp(stdo_academic)) {
                log.info("############################ saveStud() found academic level is null. default to FR");
                stud.setStudentUAcademic("FR");
            } else {
                stud.setStudentUAcademic(stdo_academic);
            }
        }
        stud.setStudentPassword("useless");
        stud.setStudentFisy(ref.getFiscal_year());

        if (pu) {
            stud.setCounselorId(ref.getSys_counselor_id());
        } else {
            stud.setCounselorId(login.getCurrentUser().getUserid());
        }

        stud.setClientId(ref.getClientid());
        stud.setRecid("tmpid");

        stud.setLostTime(0);
        stud.setLostToLocal(null);
        stud.setLostToMaster(null);
        stud.setLostTz(null);

        //if( stud.getPrtTimes()==null)
        stud.setPrtTimes(0);
        stud.setPdfs(new ArrayList<Print>()); //otherwise, the cached obj will carry PDF, and will set/update that Print record's recid=tempid

        if (modStud == null) {   //new created          
            if (pu) {
                stud.setStudentBuOrigCounselor("MYCAMPUS PORTAL");
                //stud.setCounselorOrig( );
            } else {
                stud.setStudentBuOrigCounselor(login.getCurrentUser().getUsername());
                stud.setCounselorOrig(login.getCurrentUser().getUserid());
            }

            stud.setDdoe(System.currentTimeMillis()); //microseconds from 1970-01-01 12:00 AM UTC
            stud.setTzdoe(ref.getTzSN()); //TimeZone.getDefault().getID()==America/Los Angeles   TimeZone.getDefault().getDisplayName()== Pacific Standard Time

            stud.setStudentBzUploaded("No");
            stud.setStudentBvDoe(new Date());//new Timestamp(System.currentTimeMillis())  ); //new Date()            

            stud.setDdom(0);
            stud.setTzdom(null);
            stud.setStudentBxModCounselor(null);
            stud.setModRoot(null);
            stud.setModPre(null);
        } else {   //this doc is a modification based on one existing one, which may be created locally (diff counselor) or downloaded form master  (diff or same counselor from diff client)
            stud.setDdown(0);
            stud.setDup(0);
            //stud.setDdoe(0); still keeps the original doe

            //pu will always create new one 
            stud.setStudentBxModCounselor(login.getCurrentUser().getUsername());
            stud.setDdom(System.currentTimeMillis());
            stud.setTzdom(ref.getTzSN()); // will the app keep running the same tz while OS changes TimeZone ???

            stud.setCounselorMod(login.getCurrentUser().getUserid());
            if (modStud.getModRoot() != null) {
                stud.setModRoot(modStud.getModRoot());
            } else {
                stud.setModRoot(modStud.getRecid());
            }
            stud.setModPre(modStud.getRecid());

        }
        stud.setPickupInd(1);
        stud.setStudentFisy(ref.getFiscal_year());
        stud.setStudentStudType("UGFY");

        stud.setStudentNumb(null);  //#######################  (Integer)null  NPE ###################################################################

        if (stud.getSex() == null) {
            std_sex = "N";
            stud.setSex("N");
        }

        if (modStud != null) {
            modStud.setPickupInd(0);
            // modStud.setLost_to_recid(stud.); //##################### used only when merge/sync with master data
            modStud.setLostTime(System.currentTimeMillis());
            modStud.setLostTz(ref.getTzSN());
        }
        List<Student> matches = queryActiveStudsByIdOrUsername(stud);
        String tz = ref.getTzSN();

//log.info("vvvvvvvvvvvvvvvvvvvvvvvvvvv  to-be-saved student has recid (fake)=%s", stud.getRecid());          
        try {
            if (pu) {
                msg = accessor.saveStudInfo(stud, modStud, matches, tz, 1); //################### EJB3.1 invoke JPA2 in transaction //Transaction marked for rollback??? WTF
            } else {
                msg = accessor.saveStudInfo(stud, modStud, matches, tz);
            }
//EJB3.1 seems exe out of order, shall be (stud, matches, log), but did this:(log, matches, stud), which caused matcheds has "tmpid" as the lost_to_local
//so not update matches's lost_to here, but call accessor.markOldDupStud() after
            if (!msg.isEmpty()) {
                log.info("????????? error when saving matches and mod info: %s", msg);
                return msg;
            }

            int seq = info.getNowStudNumbInQueue();
            stud.setStudentNumb(seq);
            stud.setRecid(info.getStudRecid(stud.getCounselorId(), stud.getClientId(), stud.getStudentNumb()));

//log.info("vvvvvvvvvvvvvvvvvvvvvvvvvvv  saved student has recid (calulated)=%s", stud.getRecid());            
            stud = em.find(Student.class, stud.getRecid());  //find and load from persistence cotext  
//log.info("vvvvvvvvvvvvvvvvvvvvvvvvvvv  find student has recid (calulated)=%s", stud.getRecid());  

            /**
             * * but if add transaction, then saveStudInfo(Accessor.java:70) has
             * error. WTF JPA?????? em.getTransaction().begin();
             * em.refresh(stud); //reload from database
             * //javax.persistence.TransactionRequiredException
             * em.getTransaction().commit();
             */
            if (pu) {
                accessor.markOldDupStud(stud, ref.getTzSN(), 1);
            } else {
                accessor.markOldDupStud(stud, ref.getTzSN());
            }

            if (modStud != null) {
                modStud.setLostToLocal(stud.getRecid());
                //accessor.updateStudInfo(modStud);
            }
            /*
            sfund1amt = stud.getStudentAuScholarship1Amt();
            sfund2amt = stud.getStudentAuScholarship1Amt();
             */

            //found out all rec with same student_username, and override them by pickupind(0), and lost info
            //em.cache clear, so not persis/merge dirty data to database            
        } catch (Exception e) {
            msg = "Error: " + e.getMessage();
            e.printStackTrace();
        }
        return msg;
    }

    private String chkStudBasicInfo(Integer... p) {
        boolean npu = p.length == 0;

        String msg = "";
        //2012-01 stud basic info pop-up, needs hook them to estimator, then to student obj, so the checking shall be there first
        int flag = 0;
        FacesMessage err;
        int chk = this.validateLsuId(stud.getStudentALsuid()); //stud only check max=6
        if (chk < 1 && chk == 0) {
            err = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidLSUID");

            flag++;
            if (npu) {
                UIComponent c = getUIComponentById("std_lsuid"); //auth-sv-ptab-std_dob
                if (c == null) {
                    facesContext.getViewRoot().findComponent("std_lsuid");
                }

                UIInput textInput = (UIInput) c;
                textInput.setValid(false);
                String label = (String) c.getAttributes().get("label") + ": " + err.getDetail();// err.getSummary()
                err.setDetail(label);
                label = (String) c.getAttributes().get("label") + ": " + err.getSummary();
                err.setSummary(label);
                if (facesContext != null) {
                    facesContext.addMessage("auth-sv-ptab-std_lsuid", err);
                }
            }
        }

        chk = this.validateLn(std_ln);
        if (chk != 0) {
            log.info("''''''''chkStudBasicInfo() validateLn(std_ln=%s)==%d", std_ln, chk);
            if (chk < 0) {
                err = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NoLName");
            } else {
                err = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidLName");
            }

            flag++;
            if (npu) {
                UIComponent c = getUIComponentById("std_lname"); //auth-sv-ptab-std_dob
                if (c == null) {
                    facesContext.getViewRoot().findComponent("std_lname");
                }

                UIInput textInput = (UIInput) c;
                textInput.setValid(false); //NPE
                String label = (String) c.getAttributes().get("label") + ": " + err.getDetail();// err.getSummary()
                err.setDetail(label);
                label = (String) c.getAttributes().get("label") + ": " + err.getSummary();
                err.setSummary(label);
                if (facesContext != null) {
                    facesContext.addMessage("auth-sv-ptab-std_lname", err);
                }
            }
        }
        chk = this.validateFn(std_fn);
        if (chk != 0) {
            log.info("''''''''chkStudBasicInfo() validateFn(std_fn=%s)==%d", std_fn, chk);
            if (chk < 0) {
                err = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NoFName");
            } else {
                err = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidFName");
            }

            flag++;
            if (npu) {
                UIComponent c = getUIComponentById("std_fname"); //auth-sv-ptab-std_dob
                if (c == null) {
                    facesContext.getViewRoot().findComponent("std_fname");
                }
                UIInput textInput = (UIInput) c;
                textInput.setValid(false);
                String label = (String) c.getAttributes().get("label") + ": " + err.getDetail();// err.getSummary()
                err.setDetail(label);
                label = (String) c.getAttributes().get("label") + ": " + err.getSummary();
                err.setSummary(label);
                if (facesContext != null) {
                    facesContext.addMessage("auth-sv-ptab-std_fname", err);
                }
            }
        }
        chk = this.validateDobStr(std_dob);
        if (chk != 1) { // chk<1 || chk==2
            if (chk < 0) {
                err = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NoDoB");
            } else if (chk == 0) {
                err = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidDoB");
            } else {
                err = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NotSuitableDoB");
            }
            flag++;

            if (npu) {
                UIComponent c = getUIComponentById("std_dob"); //auth-sv-ptab-std_dob
                if (c == null) {
                    facesContext.getViewRoot().findComponent("std_dob");
                }
                UIInput textInput = (UIInput) c;
                textInput.setValid(false);

                String label = (String) c.getAttributes().get("label") + ": " + err.getDetail();// err.getSummary()
                err.setDetail(label);
                label = (String) c.getAttributes().get("label") + ": " + err.getSummary();
                err.setSummary(label);
                if (facesContext != null) {
                    facesContext.addMessage("auth-sv-ptab-std_dob", err);
                }
            }
            /*
            log.info(">>>>>>>>>>>>> viewRoot viewid=[%s], clientid=%s, id=%s", facesContext.getViewRoot().getViewId(), facesContext.getViewRoot().getClientId(), facesContext.getViewRoot().getId() ); //.toString() viewRoot=[javax.faces.component.UIViewRoot@5ba33bf5]
            Map<String, Object> attrs = facesContext.getViewRoot().getAttributes();
            for(String key: attrs.keySet()){
                 log.info(">>>>>>>>>>>>>viewRoot Attr key=[%s], val=[%s]", key, attrs.get(key).toString());
            }
            
            Map<String, String> attrs2 = facesContext.getViewRoot().getResourceBundleMap();
            for(String key: attrs2.keySet()){
                 log.info(">>>>>>>>>>>>>viewRoot ResourceBundle key=[%s], val=[%s]", key, attrs2.get(key).toString());
            }
            
            List<UIComponent> list = facesContext.getViewRoot().getChildren();
            for(UIComponent key: list){
                 log.info(">>>>>>>>>>>>>ViewRoot Child UIComponent id=[%s], clientid=[%s], class=[%s], canonocal=[%s]", key.getId(), key.getClientId(), key.getClass().getName(), key.getClass().getCanonicalName()); 
                 //((UIInput)key).getClass().getName(), ((UIInput)key).getValue()); //java.lang.ClassCastException: com.sun.faces.facelets.compiler.UIInstructions cannot be cast to javax.faces.component.UIInput
                 attrs = key.getAttributes();
                 for(String k: attrs.keySet()){
                    log.info(">>>>>>>>>>>>>ViewRoot Child UIComponent Attr key=[%s], val=[%s]", k, attrs.get(k).toString());
                 }
                 attrs2 = key.getResourceBundleMap();
                 for(String k: attrs2.keySet()){
                    log.info(">>>>>>>>>>>>>ViewRoot Child UIComponent ResourceBundle key=[%s], val=[%s]", k, attrs2.get(k).toString());
                }
            } 
            
        UIComponent c = getUIComponentById("std_dob"); //auth-sv-ptab-std_dob
        if( c==null) facesContext.getViewRoot().findComponent("std_dob");

        String label = (String)c.getAttributes().get("label");
        log.info("component label attr=[%s]", label);
             
            attrs = c.getAttributes(); //'java.lang.NullPointerException' when invoking action listener '#{estimator.savestudinfo}' for component 'save'
            for(String key: attrs.keySet()){
                 log.info(">>>>>>>>>>>>>UIComponent Attr key=[%s], val=[%s]", key, attrs.get(key).toString());
            }
            attrs2 = c.getResourceBundleMap();
                for(String k: attrs2.keySet()){
                log.info(">>>>>>>>>>>>> UIComponent ResourceBundle key=[%s], val=[%s]", k, attrs2.get(k).toString());
            }
            
            list = c.getChildren();
            for(UIComponent key: list){
                 log.info(">>>>>>>>>>>>>UIComponent Child UIComponent id=[%s], clientid=[%s], class=[%s], canonocal=[%s]", key.getId(), key.getClientId(), key.getClass().getName(), key.getClass().getCanonicalName()); 
                 //((UIInput)key).getClass().getName(), ((UIInput)key).getValue()); //java.lang.ClassCastException: com.sun.faces.facelets.compiler.UIInstructions cannot be cast to javax.faces.component.UIInput
                 attrs = key.getAttributes();
                 for(String k: attrs.keySet()){
                    log.info(">>>>>>>>>>>>>UIComponent Child UIComponent Attr key=[%s], val=[%s]", k, attrs.get(k).toString());
                 }
                 attrs2 = key.getResourceBundleMap();
                 for(String k: attrs2.keySet()){
                    log.info(">>>>>>>>>>>>>UIComponent Child UIComponent ResourceBundle key=[%s], val=[%s]", k, attrs2.get(k).toString());
                }
            } 
            
            //no c.getValue()?                
            //System.out.println(c.getFamily());
            
        UIInput textInput = (UIInput)c;// context.getViewRoot().findComponent(field1Id);
        log.info(">>>>>>UIComponent family=[%s], UIinput family=[%s], clientid=[%s], id=[%s]", c.getFamily(),  textInput.getFamily(), textInput.getClientId(),textInput.getId()  );

        label = (String)textInput.getAttributes().get("label");
        log.info("textInput label attr=[%s]", label);

        //textInput.getValue();
        //textInput.getSubmittedValue();
        textInput.setValid(false);
             
            attrs = c.getAttributes();
            for(String key: attrs.keySet()){
                 log.info(">>>>>>>>>>>>>UIInput Attr key=[%s], val=[%s]", key, attrs.get(key).toString());
            }
            attrs2 = textInput.getResourceBundleMap();
                for(String k: attrs2.keySet()){
                log.info(">>>>>>>>>>>>>UIInput ResourceBundle key=[%s], val=[%s]", k, attrs2.get(k).toString());
            }
            list = textInput.getChildren();
            for(UIComponent key: list){
                 log.info(">>>>>>>>>>>>>UIInput Child UIComponent id=[%s], clientid=[%s], class=[%s], canonocal=[%s]", key.getId(), key.getClientId(), key.getClass().getName(), key.getClass().getCanonicalName()); 
                 //((UIInput)key).getClass().getName(), ((UIInput)key).getValue()); //java.lang.ClassCastException: com.sun.faces.facelets.compiler.UIInstructions cannot be cast to javax.faces.component.UIInput
                 attrs = key.getAttributes();
                 for(String k: attrs.keySet()){
                    log.info(">>>>>>>>>>>>>UIInput Child UIComponent Attr key=[%s], val=[%s]", k, attrs.get(k).toString());
                 }
                 attrs2 = key.getResourceBundleMap();
                 for(String k: attrs2.keySet()){
                    log.info(">>>>>>>>>>>>>UIInput Child UIComponent ResourceBundle key=[%s], val=[%s]", k, attrs2.get(k).toString());
                }
            }*/

        }

        if (flag > 0) {
            msg = "student identity data is not valid or complete";
        } else {
            stud.setStudentBLastname(std_ln.toUpperCase());
            stud.setStudentCFirstname(std_fn.toUpperCase());
            stud.setStudentDDob(std_dob);
        }

        if (ref.isEmp(msg)) {
            msg = validateTransPhoneNumb(stud.getStudentFPhone());
            if (!msg.startsWith("Error:")) {
                stud.setStudentFPhone(msg);
                msg = "";
            } else {
                if (npu) {
                    UIComponent c = getUIComponentById("std_phone"); //auth-sv-ptab-std_dob
                    if (c == null) {
                        facesContext.getViewRoot().findComponent("std_phone");
                    }
                    UIInput textInput = (UIInput) c;
                    textInput.setValid(false);

                    err = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                    String label = (String) c.getAttributes().get("label") + ": " + err.getDetail();// err.getSummary()
                    err.setDetail(label);
                    label = (String) c.getAttributes().get("label") + ": " + err.getSummary();
                    err.setSummary(label);
                    if (facesContext != null) {
                        facesContext.addMessage("auth-sv-ptab-std_phone", err);
                    }
                }
            }
        }
        return msg;
    }

    private List<Student> queryActiveStudsByIdOrUsername(Student stud) {
        boolean nobaseid = ref.isEmp(stud.getStudentALsuid());
        //query = "SELECT s FROM Student s WHERE s.pickupInd = 1 and (s.studentALsuid = :lsuid or s.studentUserName = :username)"),
        //query = "SELECT s FROM Student s WHERE s.pickupInd = 1 and (s.studentALsuid = :lsuid or s.studentUserName = :username)"),

        List<Student> results = em.createNamedQuery("Student.findActiveByLsuidOrUsername")
                .setParameter("lsuid", nobaseid ? "1234567" : stud.getStudentALsuid())
                .setParameter("username", stud.getStudentUserName())
                .setParameter("studentFisy", ref.getFiscal_year())
                .getResultList();
        /*
        List<Student> results = em.createNamedQuery("Student.findActiveByLsuidOrUsernamewofisy")
                           .setParameter("lsuid", nobaseid ?"1234567": stud.getStudentALsuid())
                           .setParameter("username", stud.getStudentUserName())                           
                           .getResultList();
         */
        List<Student> matches = new ArrayList<>();
        for (Student one : results) {
            if (ref.isEmp(one.getStudentALsuid()) == false && nobaseid == false) { //both must match
                if (one.getStudentALsuid().equals(stud.getStudentALsuid())) {
                    matches.add(one);
                }
            } else if (one.getStudentUserName().equalsIgnoreCase(stud.getStudentUserName())) {
                matches.add(one);
            }
        }
        for (Student one : matches) {
            log.info("???????????????? queryActiveStudsByIdOrUsername() found matched/dup active student: (%s, %s) for (%s,%s)", one.getRecid(), one.getStudentUserName(), stud.getRecid(), stud.getStudentUserName());
        }
        return matches;
    }

    public UIComponent getUIComponentById(String componentId) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();

        //final String componentId = "foo";
        UIComponent c = findComponent(root, componentId);
        //return c.getClientId(context);        
        return c;
    }

    /**
     * Finds component with the given id
     */
    private UIComponent findComponent(UIComponent c, String id) {
        if (id.equals(c.getId())) {
            return c;
        }
        Iterator<UIComponent> kids = c.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent found = findComponent(kids.next(), id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public String savepstudinfo() {
        String ln = stud.getStudentBLastname();
        String fn = stud.getStudentCFirstname();
        String dob = stud.getStudentDDob();
        String phone = stud.getStudentFPhone();
        String msg = chkStudBasicInfo(1);
        log.info("???? savestudinfo() get chkStudBasicInfo() msg=%s", msg);
        if (!msg.isEmpty()) {
            return msg;
        }

        stud.setStudentUserName(compUserName(stud.getStudentALsuid(), ln, fn, dob)); //used to comp when merging dup/conflict among those picked==1 (upload download)
        msg = saveStud(0, 1);

        if (msg == null || msg.isEmpty()) {
            FacesMessage guimsg = ref.facesMessageByKey(FacesMessage.SEVERITY_INFO, "EstimateForm.DataSaved");
            //      if(facesContext!=null )facesContext.addMessage(null, guimsg);     
            stud.setStudentNumb((Integer) null); //why ???? . when saved next tine, the DB sequence will gen new number

            //  modEstimate(stud);
            PDFgen pdf = new PDFgen();
            Print prt = new Print();
            prt.setSship1ExtAmt(0);
            prt.setSship2ExtAmt(0);
            prt.setSship7ExtAmt(0);
            prt.setSship8ExtAmt(0);
            prt.setSship9ExtAmt(0);

            msg = pdf.genPDF(stud, login, ref, calc, prt, 0, 1);
            if (!msg.startsWith("Failed")) {//                 msg==null||msg.isEmpty()){
                log.info("==================savestudinfo() gen pdf: pass");
                String pdfname = msg;
                //save prints table
                msg = savePrint(prt, 0, 1);

                if (msg == null || msg.isEmpty()) {
                    log.info("==================savestudinfo() save print rec: pass");
                    return "SAVED";
                } else {
                    log.info("==================savestudinfo() save print rec: fail. msg=%s", msg);
                    return msg;
                }
            } else {
                log.info("==================savestudinfo() gen pdf: fail. msg=%s", msg);
                return msg;
            }

        } else {//if( !msg.equals("sships")) {
            return msg;
        }

    }

    public void savestudinfo(ActionEvent event) {//ActionEvent event){//AjaxBehaviorEvent event){  //ActionEvent event){
        //((Querier)ref.findBean("querier")).saveStudInfo(stud);               
        String ln = stud.getStudentBLastname();
        String fn = stud.getStudentCFirstname();
        String dob = stud.getStudentDDob();
        String phone = stud.getStudentFPhone();
        String msg = chkStudBasicInfo();
        log.info("???? savestudinfo() get chkStudBasicInfo() msg=%s", msg);

        //201602
        if (stud.getStudentAuScholarship1Amt() > 0) {
            stud.setStudentAuScholarship1Amt(0);
        }

        if (!msg.isEmpty()) {
            return;//  null;
        }
        /*
        if(ln==null || ln.isEmpty() || fn==null ||fn.isEmpty() || dob==null || phone==null || phone.isEmpty()){
            log.info("!!!!!! savestudinfo() found ln==null? %s, fn==null? %s, dob==null? %s, phone==null? %s", ln==null || ln.isEmpty(), fn==null ||fn.isEmpty(), dob==null, phone==null || phone.isEmpty());
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidData");    
             if(facesContext!=null )facesContext.addMessage(null, msg);                   
             return;
        }*/

        stud.setStudentUserName(compUserName(stud.getStudentALsuid(), ln, fn, dob)); //used to comp when merging dup/conflict among those picked==1 (upload download)

        msg = saveStud(0);
        /*
        stud.setStudentPassword("useless");
        
        stud.setCounselorId( login.getCurrentUser().getUserid() );
        stud.setClientId(1);
        stud.setRecid("tmpid");
        if( modStud==null){            
            stud.setStudentBuOrigCounselor(login.getCurrentUser().getUsername() );
            stud.setCounselorOrig( login.getCurrentUser().getUserid() );
            stud.setDdoe(System.currentTimeMillis()); //microseconds from 1970-01-01 12:00 AM UTC
            stud.setTzdoe(ref.getTzSN() ); //TimeZone.getDefault().getID()==America/Los Angeles   TimeZone.getDefault().getDisplayName()== Pacific Standard Time
           
            stud.setStudentBzUploaded("No");
            stud.setStudentBvDoe( new Date());//new Timestamp(System.currentTimeMillis())  ); //new Date()
            
            
        }else{             
            stud.setStudentBxModCounselor(login.getCurrentUser().getUsername());
            stud.setDdom(System.currentTimeMillis());
            stud.setTzdom(ref.getTzSN()); // will the app keep running the same tz while OS changes TimeZone ???
            
            stud.setCounselorMod(login.getCurrentUser().getUserid() );
            if( modStud.getModRoot()!=null){
                stud.setModRoot(modStud.getModRoot() );
            }else{
                stud.setModRoot( modStud.getRecid());
            }
            stud.setModPre(modStud.getRecid());
             
            
        }        
        stud.setPickupInd(1);
        stud.setStudentFisy( ref.getFiscal_year());         
        stud.setStudentStudType("UGFY");
                
        stud.setStudentNumb(null);  //#######################  (Integer)null  NPE ###################################################################
        
        
        if( stud.getSex()==null){
            std_sex="N";
            stud.setSex("N");
        }
        
        String msg="";
        
        if( modStud!=null){
            modStud.setPickupInd(0);
            // modStud.setLost_to_recid(stud.); //##################### used only when merge/sync with master data
            modStud.setLostTime(System.currentTimeMillis());
            modStud.setLostTz(ref.getTzSN());
        }
        try{
            msg = accessor.saveStudInfo(stud, modStud);       //################### EJB3.1 invoke JPA2 in transaction
            int seq = info.getNowStudNumbInQueue();                          
            stud.setStudentNumb(seq);
            stud.setRecid( info.getStudRecid(stud.getCounselorId(), stud.getClientId(),  stud.getStudentNumb()));
            
            if( modStud!=null){
                modStud.setLostToLocal(stud.getRecid());
                 accessor.updateStudInfo( modStud); 
            }
            
            
            //found out all rec with same student_username, and override them by pickupind(0), and lost info
            
            //em.cache clear, so not persis/merge dirty data to database
            
            
        }catch(Exception e){
            msg = "Error: "+e.getMessage();
            
        }    
         */

        if (msg == null || msg.isEmpty()) {
            FacesMessage guimsg = ref.facesMessageByKey(FacesMessage.SEVERITY_INFO, "EstimateForm.DataSaved");
            if (facesContext != null) {
                facesContext.addMessage(null, guimsg);
            }
            stud.setStudentNumb((Integer) null); //why ???? . when saved next tine, the DB sequence will gen new number

            //  modEstimate(stud);
            PDFgen pdf = new PDFgen();
            Print prt = new Print();
            prt.setSship1ExtAmt(0);
            prt.setSship2ExtAmt(0);
            prt.setSship7ExtAmt(0);
            prt.setSship8ExtAmt(0);
            prt.setSship9ExtAmt(0);

            msg = pdf.genPDF(stud, login, ref, calc, prt, 0);
            if (!msg.startsWith("Failed")) {//                 msg==null||msg.isEmpty()){
                log.info("==================savestudinfo() gen pdf: pass");
                String pdfname = msg;
                //save prints table
                msg = savePrint(prt, 0);

                if (msg == null || msg.isEmpty()) {
                    log.info("==================savestudinfo() save print rec: pass");
                } else {
                    log.info("==================savestudinfo() save print rec: fail. msg=%s", msg);
                }
            } else {
                log.info("==================savestudinfo() gen pdf: fail. msg=%s", msg);
            }

        } else {//if( !msg.equals("sships")) {
            FacesMessage guimsg = ref.facesMessageByStr(FacesMessage.SEVERITY_ERROR, msg);
            if (facesContext != null) {
                facesContext.addMessage(null, guimsg);
            }
        }
////        tab_index=0;

        RequestContext reqContext = RequestContext.getCurrentInstance();
        //context.addCallbackParam("saved", true);    //basic parameter  
        ////context.addCallbackParam("user", user);     //pojo as json  
        setClientCallBack(reqContext);

        return;//  null;
    }

    public String navSaveStud() {
        return null;
    }

    private String compUserName(String id, String ln, String fn, String dob) { //std_dob  Date dob
        //return new StringBuilder(128).append(id==null?"":id).append('|').append(ln==null?"":ln.toUpperCase()).append('|').append(fn==null? "":fn.toUpperCase()).append('|').append(dob==null? "":std_dob).toString();
        //some have same ID, but name/dob differ, and should be considered as one stud. so taking off ID here
        return new StringBuilder(128).append(ln == null ? "" : ln.toUpperCase()).append('|').append(fn == null ? "" : fn.toUpperCase()).append('|').append(dob == null ? "" : std_dob).toString();
    }

    public String showStudName(int len) {//String str, String str2){
        //   int len = 25;
        //estimator.stud.studentCFirstname, estimator.stud.studentBLastname
        if (len <= 0) {
            return "";
        }
        String str = stud.getStudentCFirstname();//stud.getStudentBLastname();
        int flen = 0;
        String stmp = "";

        if (str != null && !str.isEmpty()) {
            // flen= str==null? 0:str.length();
            flen = str.length();
            flen = flen <= len ? flen : len;
            stmp = str.substring(0, flen);
        }
        flen = len - flen;

        String str2 = stud.getStudentBLastname();
        if (flen > 1 && str2 != null && !str2.isEmpty()) {
            flen--;
            if (str2.length() < flen) {
                flen = str2.length();
            }
            stmp = stmp + " " + str2.substring(0, flen);
        }
        return stmp;
    }

    public void academiclevel_changed(AjaxBehaviorEvent event) {
        log.info("////////////academiclevel_changed() event triggerred. now level code is %s", stud.getStudentUAcademic());
        stdo_academic = stud.getStudentUAcademic();
        calc.refreshCalc(stud);
    }/*
    public void studcat1changed( AjaxBehaviorEvent event){
        if(std_cat1==true){
            setStudCat(1);
        }else{
            std_cat1=true;
            setStudCat(1);
        }
        calc.refreshCalc(stud);
    }
    public void studcat2changed( AjaxBehaviorEvent event){
        if(std_cat2==true){
            setStudCat(2);
        }else{
            std_cat1=true;
            setStudCat(1);
        }
        calc.refreshCalc(stud);
    }
    public void studcat3changed( AjaxBehaviorEvent event){
        if(std_cat3==true){
            setStudCat(3);
        }else{
            std_cat1=true;
            setStudCat(1);
        }
        calc.refreshCalc(stud);
    }
    public void studcat4changed( AjaxBehaviorEvent event){
        if(std_cat4==true){
            setStudCat(4);
        }else{
            std_cat1=true;
            setStudCat(1);
        }
        stdo_academic = stud.getStudentUAcademic();
        calc.refreshCalc(stud);
    }
    public void studcat5changed( AjaxBehaviorEvent event){
        if(std_cat5==true){
            setStudCat(5);
        }else{
            std_cat1=true;
            setStudCat(1);
        }
        calc.refreshCalc(stud);
    }
    public void studcat6changed( AjaxBehaviorEvent event){
        if(std_cat6==true){
            setStudCat(6);
        }else{
            std_cat1=true;
            setStudCat(1);
        }
        calc.refreshCalc(stud);
    }
    public void studcat7changed( AjaxBehaviorEvent event){
        if(std_cat7==true){
            setStudCat(7);
        }else{
            std_cat1=true;
            setStudCat(1);
        }
        calc.refreshCalc(stud);
    }
    public void studcat8changed( AjaxBehaviorEvent event){
        if(std_cat8==true){
            setStudCat(8);
        }else{
            std_cat1=true;
            setStudCat(1);
        }
        calc.refreshCalc(stud);
    }
    public void studcat9changed( AjaxBehaviorEvent event){
        if(std_cat9==true){
            setStudCat(9);
        }else{
            std_cat1=true;
            setStudCat(1);
        }
        calc.refreshCalc(stud);
    }
    public void studcat10changed( AjaxBehaviorEvent event){
        if(std_cat10==true){
            setStudCat(10);
        }else{
            std_cat1=true;
            setStudCat(1);
        }        
        calc.refreshCalc(stud);
    }
    
    private void setStudCat(int cat){
        String[] cats = {"", "FR", "F2", "SO", "JR","SR",    "PB", "MBA", "GR", "CJ", "ESL"};
        std_cat1= false;
        std_cat2= false;
        std_cat3= false;
        std_cat4= false;  
        std_cat5= false;  
        std_cat6= false;  
        std_cat7= false;  
        std_cat8= false;  
        std_cat9= false;  
        std_cat10= false; 
        switch(cat){
            case 1: std_cat1= true;    break;
            case 2: std_cat2= true;    break;
            case 3: std_cat3= true;    break;
            case 4: std_cat4= true;    break;   
            case 5: std_cat5= true;    break;   
            case 6: std_cat6= true;    break;   
            case 7: std_cat7= true;    break;   
            case 8: std_cat8= true;    break;   
            case 9: std_cat9= true;    break; 
            case 10: std_cat10= true;    break;
            default: break;
        }
        stud.setStudentUAcademic(cats[cat]); //boundage checking??
        stdo_academic = cats[cat];//stud.getStudentUAcademic();
        mirr_academic = stdo_academic;
    }*/


    public void maritalchanged(AjaxBehaviorEvent event) {
        stdo_marry = std_marry;
        if (std_marry == true) {
            if (!std_indepts.containsKey("marry")) {
                std_indepts.put("marry", "marry");
            }
            if (this.std_indept == false) {
                this.std_indept = true;
            }
            stud.setStudentMMarry("yes");//Married");

            //calc.setSAVE_STUDENT_M_MARRY("Yes");
            //calc.setSAVE_STUDENT_Y_INDEPT("Yes");
        } else if (std_marry == false) {
            if (std_indepts.containsKey("marry")) {
                std_indepts.remove("marry");
            }
            if (this.std_indept == true) {
                if (std_indepts.size() == 0 && std_indept_byhand == 0) {
                    this.std_indept = false;
                }
            }
            stud.setStudentMMarry("no");//Single");

            //calc.setSAVE_STUDENT_M_MARRY("No");
            //calc.setSAVE_STUDENT_Y_INDEPT("No");
        }
        stud.setStudentYIndept(std_indept ? "Yes" : "No");
        calc.refreshCalc(stud);
        stdo_indept = std_indept;
    }

    public void dormchanged(AjaxBehaviorEvent event) {
        stdo_dorm = std_dorm;
        stud.setStudentWDorm(std_dorm ? "Yes" : "No");
        if (std_eanonlsu) {
            stud.setStudentAiEduAllowPer(new BigDecimal(calc.getEaNonLsuPercentageByDorm(std_dorm)).divide(new BigDecimal(100)));
        } else {
            stud.setStudentAiEduAllowPer(BigDecimal.ZERO);
        }
        calc.refreshCalc(stud);
    }

    public void sdachanged(AjaxBehaviorEvent event) {
        stdo_sda = std_sda;
        stud.setStudentNSda(std_sda ? "Yes" : "No");
        calc.refreshCalc(stud);
    }

    public void indeptclicked(AjaxBehaviorEvent event) { //changed?
        if (this.std_indept == true) {
            std_indept_byhand = 1;
        } else {
            std_indept_byhand = 0;
            if (std_indepts.size() > 0) {
                std_indept = true;
            }
        }
        stud.setStudentYIndept(std_indept ? "Yes" : "No");
        calc.refreshCalc(stud);
        stdo_indept = std_indept;
    }

    public void stdintlchanged(AjaxBehaviorEvent event) {
        // in setStd_Intl()//already done: stud.setStudentLIntlStud(std_intl ? "yes":"no");
        stdo_intl = std_intl;
        if (std_intl) {
            std_fafsa = false;
            //stud.setStudentXFafsa(std_fafsa ? "Yes":"No");
            //          log.info("vvvvvv to set stud obj FAFSA attr, while checkbox=%s, in stdintlchanged()", std_fafsa);
            setStudFAFSA(std_fafsa ? "Yes" : "No");

            //calc.setSAVE_STUDENT_X_FAFSA( stud.getStudentXFafsa());           
            std_calgrant = false;
            stud.setStudentZCalgrant(std_calgrant ? "Yes" : "No");
            //calc.setSAVE_STUDENT_Z_CALGRANT(stud.getStudentZCalgrant());           

            std_efc = false;
            stud.setStudentAfFamilyContrib(PackValues.efcInit);
            stud.setIndEfc(std_efc ? "Yes" : "No");

        }
        stud.setStudentLIntlStud(std_intl ? "Yes" : "No");
        //this.calc.setSAVE_STUDENT_L_INTL_STUD(stud.getStudentLIntlStud());        
        calc.refreshCalc(stud);

        //std_phone processing
        String phone = stud.getStudentFPhone();
        //     stud.setStudentFPhone("");
//        log.info("------international=%s, phone=%s", std_intl, phone );

        //   if( std_intl==true)return;        
        if (std_intl == true || phone == null || phone.isEmpty()) {
            return;
        }

        phone = phone.trim().replaceAll("  ", " ");
        /*
The NANP is administered by the North American Numbering Plan Administration (NANPA).
Current NANP number format can be summed up via the following:
+1-NPA-NXX-xxxx  || +1 NPA NXX xxxx || +1.NPA.NXX.xxxx || 1NPA NXX xxxx || 1NPANXXxxxx || || NPANXXxxxx  || (NPA) NXX-xxxx 

Component	Name	Number ranges	Notes
+1	ITU country calling code     "1" is also the usual trunk code for accessing long-distance service between NANP numbers. In an intra-NANP context, numbers are usually written without the leading "+"
NPA	Numbering Plan Area Code     Allowed ranges: [2-9] for the first digit, and [0-9] for both the second and third digits.[3]	Covers Canada, the United States, parts of the Caribbean Sea, and some Atlantic and Pacific islands. The area code is often enclosed in parentheses.
             *   Not all area codes correspond to a geographical area. Codes 8xx (excluding 811 and 899) with the last two digits matching, such as 800, 888, 877, 866, etc., are reserved for toll-free calls. Code 900 is reserved for premium-rate calls 
             *    Area code 710 is reserved for the United States Government. Area code 600 is reserved for national Canadian services
NXX	Central Office (exchange)    code	Allowed ranges: [2-9] for the first digit, and [0-9] for both the second and third digits.	Often considered part of a subscriber number. The three-digit Central Office codes are assigned to a specific CO serving its customers, but may be physically dispersed by redirection, or forwarding to mobile operators and other services.
xxxx	Subscriber Number	    [0-9] for each of the four digits.	This unique four-digit number is the subscriber number or station code.       
             * 
NPA [2-9]\\d{2}  Area code 555 in the North American Numbering Plan is reserved for Directory Assistance applications
NPA [2-9]\\d{2}  , industry testing codes (generally NXX 958 and 959) and special service codes (such as NXX 950 and 976).  555-0100 through 555-0199 are now specifically reserved for fictional use. Only the 555-01xx range is officially reserved
xxxx \\d{4,5}            
         */
        String phonepattern2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$"; //Examples: Matches following phone numbers:          (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 
        String phoneNumberPattern = "(\\d-)?(\\d{3}-)?\\d{3}-\\d{4}"; //matches 1-999-123-4567 1-123-4567 999-123-4567 123-4567

        //String emailpatern = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  //"^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  
        //“^[\\w\\-]+(\\.[\\w\\-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$” //“^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$”;
        String phonepattern = "^([\\+]?)(([1]?)[-\\. ]?)\\(?(\\d{3})\\)?[-\\. ]?(\\d{3,4})[-\\. ]?(\\d{4})([\\. ]?([xX]\\d{1,5})){0,1}$";
        Pattern pattern = Pattern.compile(phonepattern);
        Matcher matcher = null;
        /*
           String[] phones =new String[]{"(123)456-7890", "123-456-7890", "1234567890x12345","1234567890 x12345",  "4567890 x12345", "4567890x12345", "1234567890", "(123)-456-7890", "(123)456-7890x12345", "123-456-7890x12345", "1234567890x12345", "(123)-456-7890x12345",
               "(123)456-7890 x12345", "123-456-7890 x12345", "7852345","785.2345","785-2345","785 2345","9099372328","909.937.2328","951.785.2000x2208","9517852000x2208", "1234567890 x12345", "(123)-456-7890 x12345","(123)4567890", "1234567890", "1234567890", "123.456.7890", "(123)4567890x12345", 
               "1234567890x12345", "1234567890x12345", "123.456.7890x12345","(123)4567890 x12345", "1234567890 x12345", "1234567890 x12345", "123.456.7890 x12345", "1-999-123-4567", "999-123-4567",
               "123-4567", "1.999.123.4567" , "999.123.4567", "123.4567", "1.999.123.4567x12345", "999.123.4567x12345", "123.4567x12345", "1.999.123.4567",  "999.123.4567",
               "123.4567 x12345", "123.4567 x12345", "19991234567", "19991234567x12345", "19991234567 x12345", "+19991234567 x12345" , "-19991234567 x12345", "123 456 7890", "1 123 456 7890", "12345678", "88812345678"
            };
            for(int i=0; i<  phones.length; i++){                 
                         matcher = pattern.matcher(phones[i]);
                        if(matcher.matches()){                 
                            //System.out.println( " matched: ["+phones[i]+"]" );
                        }else
                             System.out.println( " ------unmatched: ["+phones[i]+"]" );
           }    
            INFO:  ------unmatched: [4567890 x12345]
            INFO:  ------unmatched: [4567890x12345]
            INFO:  ------unmatched: [7852345]
            INFO:  ------unmatched: [785.2345]
            INFO:  ------unmatched: [785-2345]
            INFO:  ------unmatched: [785 2345]
            INFO:  ------unmatched: [123-4567]
            INFO:  ------unmatched: [123.4567]
            INFO:  ------unmatched: [123.4567x12345]
            INFO:  ------unmatched: [123.4567 x12345]
            INFO:  ------unmatched: [123.4567 x12345]
            INFO:  ------unmatched: [12345678]     
         */
        String newphone = phone;
        int len = phone.length();
        int firstxpos = phone.toUpperCase().indexOf("X");
        int lastxpos = phone.toUpperCase().lastIndexOf("X");
        int dotpos = phone.indexOf(".");
        int blkpos = phone.indexOf(" ");
        if (firstxpos < 0) {
            if (len < 7 || (len == 7 && (dotpos > 0 || blkpos > 0)) || (len == 8 && dotpos < 0 && blkpos < 0)) {
                FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                //                  log.info("---phone length is invalid w/o x. len=%d, dotpos=%d, blkpos=%d", len, dotpos, blkpos);                   
                if (facesContext != null) {
                    facesContext.addMessage(null, msg);
                }
                return;
            } else if (len == 7 && dotpos < 0 && blkpos < 0) {
                newphone = "951" + phone;
            } else if (len == 8 && (blkpos > 0 && blkpos == phone.lastIndexOf(" ") || dotpos > 0 && dotpos == phone.lastIndexOf("."))) {
                newphone = "951" + phone;
            }
        } else if (firstxpos < 7 || firstxpos != lastxpos || lastxpos == len - 1) {
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
//               log.info("---phone length is invalid w/x or dual x or empty ext. len=%d, firstxpos=%d, lastxpos=%d, dotpos=%d, blkpos=%d", len, firstxpos, lastxpos, dotpos, blkpos);                      
            if (facesContext != null) {
                facesContext.addMessage(null, msg);
            }
            return;
        } else {
            String subphone = phone.substring(0, firstxpos).trim();
            len = subphone.length();
            dotpos = subphone.indexOf(".");
            blkpos = subphone.indexOf(" ");
            if (len < 7 || (len == 7 && (dotpos > 0 || blkpos > 0)) || (len == 8 && dotpos < 0 && blkpos < 0)) {
                FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                //                 log.info("----phone length is invalid before x. len=%d, dotpos=%d, blkpos=%d", len, dotpos, blkpos);                
                if (facesContext != null) {
                    facesContext.addMessage(null, msg);
                }
                return;
            } else if (len == 7 && dotpos < 0 && blkpos < 0) {
                newphone = "951" + phone;
            } else if (len == 8 && (blkpos > 0 && blkpos == phone.lastIndexOf(" ") || dotpos > 0 && dotpos == phone.lastIndexOf("."))) {
                newphone = "951." + phone;
            }
        }
        matcher = pattern.matcher(newphone);
        if (!matcher.matches()) {
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
            if (log == null) {
                //                System.out.println("---logger==null, and msg is null ? "+(msg==null)+", facesContext is null ? "+ (facesContext==null)); 
            } else {
                //                log.info("---pattern matching failed for phone: [%s]", newphone); 
            }
            if (facesContext != null) {
                facesContext.addMessage(null, msg);
            }
        } else {
            //phone = newphone;
            matcher.reset();
            if (matcher.find()) {
                len = matcher.groupCount();
                //                log.info("=====matcher group=%s, grpcount=%d", matcher.group(), len);
                for (int i = 0; i < len; i++) {
                    //                    log.info("===grp #%d=[%s]", i+1, matcher.group(i));
                }
            }
            StringBuilder sb = new StringBuilder(32);
            String grp = matcher.group(3);
            String grp2 = matcher.group(1);
            if (grp != null && !grp.isEmpty()) {
                if (grp2 != null && !grp2.isEmpty()) {
                    sb.append(grp2);
                }
                sb.append(grp).append(" ");
            }
            grp = matcher.group(4);
            if (grp != null && !grp.isEmpty()) {
                sb.append("(").append(grp).append(") ");
            }
            sb.append(matcher.group(5)).append("-").append(matcher.group(6));
            grp = matcher.group(7);
            if (grp != null && !grp.isEmpty()) {
                sb.append(" ").append(grp.trim());
            }

            stud.setStudentFPhone(sb.toString());
        }
        // }// end if null
    }

    /*
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #1=[9097891234x23]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #2=[]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #3=[]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #4=[]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #5=[909]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #6=[789]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #7=[1234]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #8=[x23]
     * 
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #1=[+1 909 789-1234]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #2=[+]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #3=[1 ]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #4=[1]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #5=[909]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #6=[789]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #7=[1234]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #8=[null] 

INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #1=[+1.(909) 789-1234 x23]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #2=[+]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #3=[1.]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #4=[1]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #5=[909]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #6=[789]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #7=[1234]
INFO: [http-thread-pool-8080(4)] INFO edu.lsu.estimator.Estimator - ===grp #8=[ x23]
     */
    public void valChangedCheckForNullToZero(AjaxBehaviorEvent event) {
        UIInput source = (UIInput) event.getSource();
        String clientfid = source.getClientId(); //String clientsid = source.getId();
        //source.setValid(false);
        Object val = source.getValue();
        log.info(" dob change event called in phase %s, clientid=%s id=%s, value=%s", event.getPhaseId(), clientfid, source.getId(), (val == null ? "null" : val.toString()));
        if (val == null || val.toString().trim().isEmpty() || val.toString().trim().equals("$")) {
            source.setValue("0");
        }
    }

    public Student getStud() {
        return stud;
    }

    public PackFunctions getCalc() { //Calculator
        return calc;
    }

    public Student getModStud() {
        return modStud;
    }

    public void setModStud(Student modStud) {
        this.modStud = modStud;
    }

    public int getModflag() {
        return modflag;
    }

    public void setModflag(int modflag) {
        this.modflag = modflag;
    }

    public String getStd_merits() {
        return std_merits;
    }

    public void setStd_merits(String p_std_merits) {
        std_merits = p_std_merits;
        stud.setStudentSMerit(std_merits);
        stdo_merits = std_merits;
        //mirr_merit = stdo_merits;
    }

    /*
    public List<String> getStd_merits_list() {
//        log.info(" getting estimator merits as >>%s<<", std_merits==null?"":std_merits.toString());
        if(std_merits==null || std_merits.isEmpty()){
            String x = stud.getStudentSMerit();
            if( x!=null && !x.isEmpty()){               
                x = x.replaceAll(", ",  ",");
                std_merits = Arrays.asList(x.split(","));
            }
        }else{
            //keep merits as null or empty?            
        }
        if( std_merits==null)std_merits = new ArrayList<String>();
        return std_merits;
    }
    public void setStd_merits_list(List<String> p_std_merits) {
 //      log.info("  estimator tries to set merits as >>%s<<", p_std_merits==null?"":p_std_merits.toString());
        this.std_merits = p_std_merits;
        if( std_merits==null || std_merits.size()==0){
            stud.setStudentSMerit("");
            last_merit = null;
        }else{ 
            String[] x = (String[]) std_merits.toArray(new String[0]);
            
            //max 50
            String max = Arrays.toString(x);            
            int len = max.length();
            
            int found=0;
            if( x.length==1){
                last_merit = x[0];
//                log.info("estimator gets only one merit = %s", x[0]);
            }else{ //more than one merit chosen, but only allow one---acording to Geoff
                found=-1;
                for(int i=0; i<x.length; i++){
                    if(!x[i].equalsIgnoreCase(last_merit)){
                        found = i;
//                        log.info("estimator gets the first new merit %s  while former option is %s, found=%d",x[found], last_merit==null?"":last_merit , found);
                        last_merit = x[found];
                        break;
                    }
                }                
            }
            if( found >=0){
                    //std_merits = new ArrayList<>(1);
 //                   log.info("estimator resets merits val to %s in both merits string and stud obj", x[found]);
                    try{
                        //std_merits.clear(); // at java.util.AbstractList.clear(AbstractList.java:234) java.lang.UnsupportedOperationException ???###############################
                        std_merits = new ArrayList<>();
                        std_merits.add(x[found]);
                        stud.setStudentSMerit(x[found]);   
                    }catch(Exception e){
                        e.printStackTrace();
                    }finally{
 //                       log.info("estimator saved and double checked stud obj merits string >>%s<<, and merits string=>>%s<<", stud.getStudentSMerit(),  std_merits==null?"":std_merits.toString());
                    }
            }
 //           log.info("===estimator double checkes stud obj merits string >>%s<<", stud.getStudentSMerit());
            return;
                
            
        }
 //       log.info("estimator double checkes stud obj merits string >>%s<<", stud.getStudentSMerit());
    }*/
 /*
            if( len>=2){ //get rid of '[' and ']'
                max = max.substring(1, len-1);
                len -=2;
            }            
            log.info("estimator transfered merits to string >>%s<<, length==%d", max, len);
            
            len = len>50? 50:len;
            stud.setStudentSMerit( max.substring(0, len) );
     */

    public boolean isStd_intl() {
        return std_intl;
    }

    public void setStd_intl(boolean std_intl) {
        this.std_intl = std_intl;
        stud.setStudentLIntlStud(std_intl ? "yes" : "no");
    }

    public boolean isStd_dorm() {
        return std_dorm;
    }

    public void setStd_dorm(boolean std_dorm) {
        this.std_dorm = std_dorm;
        stud.setStudentWDorm(std_dorm ? "yes" : "no");
    }

    public boolean isStd_marry() {
        return std_marry;
    }

    public void setStd_marry(boolean std_marry) {
        this.std_marry = std_marry;
        stud.setStudentMMarry(std_marry ? "yes" : "no");
    }

    public boolean isStd_sda() {
        return std_sda;
    }

    public void setStd_sda(boolean std_sda) {
        this.std_sda = std_sda;
        stud.setStudentNSda(std_sda ? "yes" : "no");
    }

    public boolean isStd_indept() {
        return std_indept;
    }

    public void setStd_indept(boolean std_indept) {
        this.std_indept = std_indept;
        stud.setStudentYIndept(std_indept ? "yes" : "no");
    }

    public boolean isStd_fafsa() {
        return std_fafsa;
    }

    public void setStd_fafsa(boolean in_std_fafsa) {
        this.std_fafsa = in_std_fafsa;

        //       log.info("vvvvvv to set stud obj FAFSA attr, while checkbox=%s, in setStd_fafsa()", in_std_fafsa);
        setStudFAFSA(std_fafsa ? "Yes" : "No");

        //stud.setStudentXFafsa(std_fafsa ? "yes":"no");
    }

    public int getTab_index() {
        return tab_index;
    }

    public void setTab_index(int tab_index) {
        this.tab_index = tab_index;
        //mirr_tabindex = tab_index;
    }

    public String getPreTabString() {
        return PreTabString;
    }

    public String getNextTabString() {
        return NextTabString;
    }

    public String getStd_sex() {
        if (ref.isEmp(std_sex)) {
            std_sex = "N";
            stud.setSex(std_sex);
        }
        return std_sex;
    }

    public void setStd_sex(String std_sex) {
        if (ref.isEmp(std_sex)) {
            this.std_sex = "N";
        } else {
            this.std_sex = std_sex;
        }
        stud.setSex(std_sex);
    }

    public List<String> getStd_quas() {
        return std_quas;
    }

    public void setStd_quas(List<String> std_quas) {
        this.std_quas = std_quas;
    }

    /*
    public int getStd_efc() {
        return std_efc;
    }

    public void setStd_efc(int std_efc) {
        this.std_efc = std_efc;
    }*/

    public int getStd_EA_PERCENT() {
        return std_EA_PERCENT;
    }

    public void setStd_EA_PERCENT(int std_EA_PERCENT) {
        this.std_EA_PERCENT = std_EA_PERCENT;
    }

    public boolean isStd_calgrant() {
        return std_calgrant;
    }

    public void setStd_calgrant(boolean std_calgrant) {
        this.std_calgrant = std_calgrant;
        stud.setStudentZCalgrant(std_calgrant ? "yes" : "no");
    }
    
       public boolean isStd_pellGrant() {
        return std_pellGrant;
    }

    //pell grnt
    public void setStd_pellGrant(boolean std_pellGrant) {
        this.std_pellGrant = std_pellGrant;
    }

    public boolean isStd_efc() {
        return std_efc;
    }

    public void setStd_efc(boolean std_efc) {
        this.std_efc = std_efc;
        stud.setIndEfc(std_efc ? "yes" : "no");
    }

    public boolean isStd_ealsu() {
        return std_ealsu;
    }

    public void setStd_ealsu(boolean std_ealsu) {
        this.std_ealsu = std_ealsu;
        stud.setIndEalsu(std_ealsu ? "yes" : "no");
    }

    public boolean isStd_eanonlsu() {
        return std_eanonlsu;
    }

    public void setStd_eanonlsu(boolean std_eanonlsu) {
        this.std_eanonlsu = std_eanonlsu;
        stud.setIndEanonlsu(std_eanonlsu ? "yes" : "no");
        stud.setStudentAgNonlsuAllowrance(std_eanonlsu ? (new BigDecimal(1)) : (new BigDecimal(0)));
    }

    public boolean isStd_noloans() {
        return std_noloans;
    }

    public void setStd_noloans(boolean std_noloans) {
        this.std_noloans = std_noloans;
        stud.setIndExcloans(std_noloans ? "yes" : "no");
    }

    public String getStd_dob() {
        return std_dob;
    }

    public void setStd_dob(String std_dob) {
        this.std_dob = std_dob;
    }

    public HashMap<String, String> getStd_indepts() {
        return std_indepts;
    }

    public void std_state_changed(AjaxBehaviorEvent event) {
        String state = stud.getStudentIState();//getStudentAjHomeState();
        if (state != null) {//state!=null && !state.isEmpty()){
/*            
            state = state.trim();           
            if( state.length()<=2){
                state = state.toUpperCase();
            }else{
                state = state.replaceAll("  ", " ");
                String[] states = state.split(" ");
                String tmp="";
                StringBuilder sb = new StringBuilder(32);
                for(int i=0; i<states.length; i++){
                    tmp = states[i];
                    if( tmp!=null && !tmp.isEmpty()){
                        sb.append( tmp.substring(0, 1).toUpperCase()).append( tmp.substring(1).toLowerCase()).append(" ");
                    }
                }
                state = sb.toString().trim();
            }
            stud.setStudentIState(state);
             */ state = state.trim();
            stud.setStudentIState(upper(state));
        } else {
            //x  check isEMP()?
        }
    }

    public void std_zip_changed(AjaxBehaviorEvent event) {
        String zip = stud.getStudentJZip();
        if (zip != null) {
            zip = zip.trim();
        }
        if (zip != null && !zip.isEmpty() && std_intl == false) {
            zip = zip.trim().replaceAll("  ", " ");
            //        log.info("  zip str=%s length=%d", zip, zip.length());
            int len = zip.length();
            if (zip.lastIndexOf("-") == len - 1) {
                zip = zip.replaceAll("-", "");
            }
            len = zip.length();
            zip = upper(zip);

            if (len > 5) {
                //len++;
                String e = zip.substring(5, 6);
                boolean q = e.equals(" ") || e.equals("-") || e.equals("X");
                zip = new StringBuilder(16).append(zip.substring(0, 5)).append(q == true ? "" : "-").append(zip.substring(5)).toString();//.substring(0, (len>13? 13:len));
                stud.setStudentJZip(zip);
            }
        }
        if (ref.isEmp(zip)) {
            //x  check if isEMP() ???
        }
    }

    public void phonechanged(AjaxBehaviorEvent event) {
        //       stdintlchanged(event); //##########need to add CSS style , so have to redo/dup the logic here
        UIInput source = (UIInput) event.getSource(); //the object on which the event initially occoured
        String clientfid = source.getClientId();

        String phone = stud.getStudentFPhone();
        stud.setStudentFPhone(upper(phone));

        if (std_intl == true) {
            if (phone == null || phone.isEmpty()) {
                source.setValid(false);
            }
            return;
        }

        if (phone != null && !phone.isEmpty()) {
            source.setValid(false);

            phone = phone.trim().replaceAll("  ", " ");

            String phonepattern2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$"; //Examples: Matches following phone numbers:          (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 
            String phoneNumberPattern = "(\\d-)?(\\d{3}-)?\\d{3}-\\d{4}"; //matches 1-999-123-4567 1-123-4567 999-123-4567 123-4567

            //String emailpatern = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  //"^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  
            //“^[\\w\\-]+(\\.[\\w\\-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$” //“^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$”;
            String phonepattern = "^([\\+]?)(([1]?)[-\\. ]?)\\(?(\\d{3})\\)?[-\\. ]?(\\d{3,4})[-\\. ]?(\\d{4})([\\. ]?([xX]\\d{1,5})){0,1}$";
            Pattern pattern = Pattern.compile(phonepattern);
            Matcher matcher = null;

            String newphone = phone;
            int len = phone.length();
            int firstxpos = phone.toUpperCase().indexOf("X");
            int lastxpos = phone.toUpperCase().lastIndexOf("X");
            int dotpos = phone.indexOf(".");
            int blkpos = phone.indexOf(" ");
            if (firstxpos < 0) {
                if (len < 7 || (len == 7 && (dotpos > 0 || blkpos > 0)) || (len == 8 && dotpos < 0 && blkpos < 0)) {
                    FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                    //                  log.info("---phone length is invalid w/o x. len=%d, dotpos=%d, blkpos=%d", len, dotpos, blkpos);                   
                    if (facesContext != null) {
                        facesContext.addMessage(null, msg);
                    }
                    return;
                } else if (len == 7 && dotpos < 0 && blkpos < 0) {
                    newphone = "951" + phone;
                } else if (len == 8 && (blkpos > 0 && blkpos == phone.lastIndexOf(" ") || dotpos > 0 && dotpos == phone.lastIndexOf("."))) {
                    newphone = "951" + phone;
                }
            } else if (firstxpos < 7 || firstxpos != lastxpos || lastxpos == len - 1) {
                FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
//               log.info("---phone length is invalid w/x or dual x or empty ext. len=%d, firstxpos=%d, lastxpos=%d, dotpos=%d, blkpos=%d", len, firstxpos, lastxpos, dotpos, blkpos);                      
                if (facesContext != null) {
                    facesContext.addMessage(null, msg);
                }
                return;
            } else {
                String subphone = phone.substring(0, firstxpos).trim();
                len = subphone.length();
                dotpos = subphone.indexOf(".");
                blkpos = subphone.indexOf(" ");
                if (len < 7 || (len == 7 && (dotpos > 0 || blkpos > 0)) || (len == 8 && dotpos < 0 && blkpos < 0)) {
                    FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                    //                 log.info("----phone length is invalid before x. len=%d, dotpos=%d, blkpos=%d", len, dotpos, blkpos);                
                    if (facesContext != null) {
                        facesContext.addMessage(null, msg);
                    }
                    return;
                } else if (len == 7 && dotpos < 0 && blkpos < 0) {
                    newphone = "951" + phone;
                } else if (len == 8 && (blkpos > 0 && blkpos == phone.lastIndexOf(" ") || dotpos > 0 && dotpos == phone.lastIndexOf("."))) {
                    newphone = "951." + phone;
                }
            }
            matcher = pattern.matcher(newphone);
            if (!matcher.matches()) {
                FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                if (log == null) {
                    //                System.out.println("---logger==null, and msg is null ? "+(msg==null)+", facesContext is null ? "+ (facesContext==null)); 
                } else {
                    //                log.info("---pattern matching failed for phone: [%s]", newphone); 
                }
                if (facesContext != null) {
                    facesContext.addMessage(null, msg);
                }
            } else {
                //phone = newphone;
                matcher.reset();
                if (matcher.find()) {
                    len = matcher.groupCount();
                    /*              log.info("=====matcher group=%s, grpcount=%d", matcher.group(), len);
                   for(int i=0; i<len; i++){
                       log.info("===grp #%d=[%s]", i+1, matcher.group(i));
                   }     
                     */
                }
                StringBuilder sb = new StringBuilder(32);
                String grp = matcher.group(3);
                String grp2 = matcher.group(1);
                if (grp != null && !grp.isEmpty()) {
                    if (grp2 != null && !grp2.isEmpty()) {
                        sb.append(grp2);
                    }
                    sb.append(grp).append(" ");
                }
                grp = matcher.group(4);
                if (grp != null && !grp.isEmpty()) {
                    sb.append("(").append(grp).append(") ");
                }
                sb.append(matcher.group(5)).append("-").append(matcher.group(6));
                grp = matcher.group(7);
                if (grp != null && !grp.isEmpty()) {
                    sb.append(" ").append(grp.trim());
                }

                stud.setStudentFPhone(sb.toString());
                source.setValid(true);
            }
        }// end if null
    }

    public String validateTransPhoneNumb(String phone) {
        String res = "Error: ";
        if (phone != null && !phone.isEmpty()) {

            phone = phone.trim().replaceAll("  ", " ");

            String phonepattern2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$"; //Examples: Matches following phone numbers:          (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 
            String phoneNumberPattern = "(\\d-)?(\\d{3}-)?\\d{3}-\\d{4}"; //matches 1-999-123-4567 1-123-4567 999-123-4567 123-4567

            //String emailpatern = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  //"^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  
            //“^[\\w\\-]+(\\.[\\w\\-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$” //“^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$”;
            String phonepattern = "^([\\+]?)(([1]?)[-\\. ]?)\\(?(\\d{3})\\)?[-\\. ]?(\\d{3,4})[-\\. ]?(\\d{4})([\\. ]?([xX]\\d{1,5})){0,1}$";
            Pattern pattern = Pattern.compile(phonepattern);
            Matcher matcher = null;

            String newphone = phone;
            int len = phone.length();
            int firstxpos = phone.toUpperCase().indexOf("X");
            int lastxpos = phone.toUpperCase().lastIndexOf("X");
            int dotpos = phone.indexOf(".");
            int blkpos = phone.indexOf(" ");
            if (firstxpos < 0) {
                if (len < 7 || (len == 7 && (dotpos > 0 || blkpos > 0)) || (len == 8 && dotpos < 0 && blkpos < 0)) {
                    FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                    //                  log.info("---phone length is invalid w/o x. len=%d, dotpos=%d, blkpos=%d", len, dotpos, blkpos);                   
                    //                  if(facesContext!=null )facesContext.addMessage(null, msg);                   
                    return res + msg.getSummary();
                } else if (len == 7 && dotpos < 0 && blkpos < 0) {
                    newphone = "951" + phone;
                } else if (len == 8 && (blkpos > 0 && blkpos == phone.lastIndexOf(" ") || dotpos > 0 && dotpos == phone.lastIndexOf("."))) {
                    newphone = "951" + phone;
                }
            } else if (firstxpos < 7 || firstxpos != lastxpos || lastxpos == len - 1) {
                FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
//               log.info("---phone length is invalid w/x or dual x or empty ext. len=%d, firstxpos=%d, lastxpos=%d, dotpos=%d, blkpos=%d", len, firstxpos, lastxpos, dotpos, blkpos);                      
//               if(facesContext!=null )facesContext.addMessage(null, msg);               
                return res + msg.getSummary();
            } else {
                String subphone = phone.substring(0, firstxpos).trim();
                len = subphone.length();
                dotpos = subphone.indexOf(".");
                blkpos = subphone.indexOf(" ");
                if (len < 7 || (len == 7 && (dotpos > 0 || blkpos > 0)) || (len == 8 && dotpos < 0 && blkpos < 0)) {
                    FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                    //                 log.info("----phone length is invalid before x. len=%d, dotpos=%d, blkpos=%d", len, dotpos, blkpos);                
                    //                 if(facesContext!=null )facesContext.addMessage(null, msg);
                    return res + msg.getSummary();
                } else if (len == 7 && dotpos < 0 && blkpos < 0) {
                    newphone = "951" + phone;
                } else if (len == 8 && (blkpos > 0 && blkpos == phone.lastIndexOf(" ") || dotpos > 0 && dotpos == phone.lastIndexOf("."))) {
                    newphone = "951." + phone;
                }
            }
            matcher = pattern.matcher(newphone);
            if (!matcher.matches()) {
                FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidPhoneNumb");
                if (log == null) {
                    //                System.out.println("---logger==null, and msg is null ? "+(msg==null)+", facesContext is null ? "+ (facesContext==null)); 
                } else {
                    //                log.info("---pattern matching failed for phone: [%s]", newphone); 
                }
                //            if(facesContext!=null )facesContext.addMessage(null, msg);
                return res + msg.getSummary();
            } else {
                //phone = newphone;
                matcher.reset();
                if (matcher.find()) {
                    len = matcher.groupCount();
                    /*              log.info("=====matcher group=%s, grpcount=%d", matcher.group(), len);
                   for(int i=0; i<len; i++){
                       log.info("===grp #%d=[%s]", i+1, matcher.group(i));
                   }     
                     */
                }
                StringBuilder sb = new StringBuilder(32);
                String grp = matcher.group(3);
                String grp2 = matcher.group(1);
                if (grp != null && !grp.isEmpty()) {
                    if (grp2 != null && !grp2.isEmpty()) {
                        sb.append(grp2);
                    }
                    sb.append(grp).append(" ");
                }
                grp = matcher.group(4);
                if (grp != null && !grp.isEmpty()) {
                    sb.append("(").append(grp).append(") ");
                }
                sb.append(matcher.group(5)).append("-").append(matcher.group(6));
                grp = matcher.group(7);
                if (grp != null && !grp.isEmpty()) {
                    sb.append(" ").append(grp.trim());
                }

                res = sb.toString();
                return res;
            }
        }// end if null
        else {
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.ReqPhoneNumb");
            return res + msg.getSummary();
        }

    }

    public int getCostudents() {
        return costudents;
    }

    public void setCostudents(int costudents) {
        this.costudents = costudents;
        stud.setHomecostudies(costudents);
    }

    public int getNotestot() {
        return notestot;
    }

    public int getShownotes() {
        return shownotes;
    }

    public int[] getNotesind() {
        return notesind;
    }

    public void setNotesind(int[] notesind) {
        this.notesind = notesind;
    }

    void loadStudInstituteScholarshipAwards() { //invoked by init()
        String[] scholarships = new String[]{stud.getStudentAsScholarship1Name(), stud.getStudentAvScholarship2Name(), stud.getStudentAyScholarship3Name(),
            stud.getStudentBbScholarship4Name(), stud.getStudentBeScholarship5Name(), stud.getStudentBhScholarship6Name(),
            stud.getStudentBkScholarship7Name(), stud.getStudentBnScholarship8Name(), stud.getStudentBqScholarship9Name()};//new String[notestot];
        int[] amts = new int[]{stud.getStudentAuScholarship1Amt(), stud.getStudentAxScholarship2Amt(), stud.getStudentBaScholarship3Amt(),
            stud.getStudentBdScholarship4Amt(), stud.getStudentBgScholarship5Amt(), stud.getStudentBjScholarship6Amt(),
            stud.getStudentBmScholarship7Amt(), stud.getStudentBpScholarship8Amt(), stud.getStudentBsScholarship9Amt()};
        String[] notes = new String[]{stud.getStudentAtScholarship1Note(), stud.getStudentAwScholarship2Note(), stud.getStudentAzScholarship3Note(),
            stud.getStudentBcScholarship4Note(), stud.getStudentBfScholarship5Note(), stud.getStudentBiScholarship6Note(),
            stud.getStudentBlScholarship7Note(), stud.getStudentBoScholarship8Note(), stud.getStudentBrScholarship9Note()};
        ////fundSeq ???

        int filled = 0, shows = 0;
        /*
        for(int i=0; i<notestot; i++){ //notesind = {0,0,0,0, 0,0,0,0, 0}; // name=100, amt=10  notes=1, nothing=0
            filled=0;
            if( scholarships[i]!=null && !scholarships[i].isEmpty()) {
                notesind[i] += 100;
                filled++;
            }             
            if( amts[i]>0){
                notes[i] +=10;
                filled++;
            }
            if( notes[i]!=null && !notes[i].isEmpty()){
                notesind[i] +=1;
                filled++;
            }
            if( filled>0){
                shows++;
            }
        }*/
        for (int i = notestot - 1; i > shownotes; i--) {
            filled = 0;
            //  if( scholarships[i]!=null && !scholarships[i].isEmpty()) {                
            //      filled++;
            //  }             
            //fundid==0 means no selection
            if (amts[i] > 0) {
                filled++;
            }
            if (notes[i] != null && !notes[i].isEmpty()) {
                filled++;
            }
            if (filled > 0) {
                shows = i + 1;
                break;
            }
        }
        if (shows > shownotes) {
            shownotes = shows;
        }
        log.info("===== show notes == %d", shownotes);
    }

    public String ifshowscholar(int i) {
        if (i <= shownotes) {
            return "block;";
        } else {
            return "none;";
        }
    }

    public void clickformorescholar(AjaxBehaviorEvent event) {
        shownotes++;
        tab_index = 3; //without it, the tab will jump to other tab, sometimes. the first tab has index==0
    }

    public void std_fafsachanged(AjaxBehaviorEvent event) {
        //true -> false
        //false -> true
        if (std_fafsa == true) {
            std_intl = false;
            stud.setStudentLIntlStud(std_intl ? "Yes" : "No");
        }
        if (std_fafsa == false) {
            std_efc = false;
            stud.setIndEfc(std_efc ? "Yes" : "No");
        }

//        log.info("vvvvvv to set stud obj FAFSA attr, while checkbox=%s, in std_fafsachanged()", std_fafsa);
        setStudFAFSA(std_fafsa ? "Yes" : "No");
        //stud.setStudentXFafsa(std_fafsa? "Yes":"No");

        calc.refreshCalc(stud);
        stdo_fafsa = std_fafsa;
        stdo_efc = std_efc;
        stdo_intl = std_intl;
    }

    public void std_calgrantchanged(AjaxBehaviorEvent event) {
        if (std_calgrant == true) {
            std_intl = false;
            stud.setStudentLIntlStud(std_intl ? "Yes" : "No");
        } else {
            //set calgrant A B to zero
            this.adjust_calgrantamt_ind = false;
        }
        stud.setStudentZCalgrant(std_calgrant ? "Yes" : "No");

        stud.setAdjCalgrantInd(adjust_calgrantamt_ind ? "Yes" : "No");
        calc.refreshCalc(stud);
        stdo_calgrant = std_calgrant;
        stdo_adjust_calgrantamt_ind = adjust_calgrantamt_ind;
        stdo_intl = std_intl;
    }

    public void efc_indchanged(AjaxBehaviorEvent event) {
        //      log.info("====== efc_ind changed to %s, and efc_amt was %d", std_efc, stud.getStudentAfFamilyContrib());
        if (std_efc == false) {
            stud.setStudentAfFamilyContrib(PackValues.efcInit);
            log.info("====== should reset efc_amt, now is  %d", stud.getStudentAfFamilyContrib());
        } //not zero

        if (std_efc == true) {//enable std_fafsa 

            std_intl = false;

            if (std_fafsa == false) {
                std_fafsa = true;
                //              log.info("vvvvvv to set stud obj FAFSA attr=%s, in efc_indchanged() while efc changed to true", std_fafsa);
                setStudFAFSA(std_fafsa ? "Yes" : "No");
                //stud.setStudentXFafsa(std_fafsa? "Yes":"No");
                stdo_fafsa = std_fafsa;
            }

            stud.setStudentLIntlStud(std_intl ? "Yes" : "No");
        }

        //stud has no efc indicator for EFC, only studentAfFamilyContrib
        stud.setIndEfc(std_efc ? "Yes" : "No");
        calc.refreshCalc(stud);
        stdo_efc = std_efc;
        stdo_intl = std_intl;

    }

    public void efc_amtchanged(AjaxBehaviorEvent event) {
        if (std_efc == false) {
            std_efc = true;
        }
        stud.setIndEfc(std_efc ? "Yes" : "No");
        calc.refreshCalc(stud);
    }

    public boolean hasMsg(String clientId) {
        log.info(" ===hasMsg() invoked with param %s, and return %s", clientId, facesContext.getMessages(clientId).hasNext());
        return facesContext.getMessages(clientId).hasNext();
    }

    public boolean hasErrInd(String clientId) { //nowhere uses this func
        log.info(" ====== hasErrInd() invoked with param %s, and return %s", clientId, check_inds.containsKey(clientId));
        return check_inds.containsKey(clientId);
    }

    public boolean isIn_subloan() {
        return in_subloan;
    }

    public void setIn_subloan(boolean in_subloan) {
        this.in_subloan = in_subloan;
        stud.setStudentApSubLoans(in_subloan ? "yes" : "no");
    }

    public boolean isIn_unsubloan() {
        return in_unsubloan;
    }

    public void setIn_unsubloan(boolean in_unsubloan) {
        this.in_unsubloan = in_unsubloan;
        stud.setStudentAqUnsubLoans(in_unsubloan ? "yes" : "no");
    }

    public boolean isIn_fws() {
        return in_fws;
    }

    public void setIn_fws(boolean in_fws) {
        this.in_fws = in_fws;
        stud.setStudentArFws(in_fws ? "yes" : "no");
    }

    ///############### all additional ajax listeners to recalculate the quote
    //==============Academical, only merits updated to std_merits first, not directly into stud obj
    public void gpachanged(AjaxBehaviorEvent event) {
        calc.refreshCalc(stud);
    }

    public void satmathchanged(AjaxBehaviorEvent event) {
        calc.refreshCalc(stud);
    }

    public void satverbchanged(AjaxBehaviorEvent event) {
        calc.refreshCalc(stud);
    }

    public void actchanged(AjaxBehaviorEvent event) {
        calc.refreshCalc(stud);
    }

    /*
    public void meritschanged(AjaxBehaviorEvent event){
        //Geoff said only can have one merit, and can be unselected
        //std_merits //List<String>   
        //last_merit
        if( std_merits!=null && std_merits.size()>0){
            stud.setStudentSMerit(std_merits.get(0)); //only one merit.
        }else{
            stud.setStudentSMerit("");
        }
        calc.refreshCalc(stud);
        stdo_merits = std_merits;
    }*/
    public void merit1changed(AjaxBehaviorEvent event) {
        if (std_merit1 == true) {
            std_merit2 = false;
            std_merit3 = false;
            std_merits = "MC";
        } else {
            std_merits = "";
        }
        stud.setStudentSMerit(std_merits);
        calc.refreshCalc(stud);
        stdo_merits = std_merits;
        //mirr_merit = stdo_merits;
    }

    public void merit2changed(AjaxBehaviorEvent event) {
        if (std_merit2 == true) {
            std_merit1 = false;
            std_merit3 = false;
            std_merits = "MS";
        } else {
            std_merits = "";
        }
        stud.setStudentSMerit(std_merits);
        calc.refreshCalc(stud);
        stdo_merits = std_merits;
        //mirr_merit = stdo_merits;
    }

    public void merit3changed(AjaxBehaviorEvent event) {
        if (std_merit3 == true) {
            std_merit2 = false;
            std_merit1 = false;
            std_merits = "MF";
        } else {
            std_merits = "";
        }
        stud.setStudentSMerit(std_merits);
        calc.refreshCalc(stud);
        stdo_merits = std_merits;
        //mirr_merit = stdo_merits;
    }

    //=============Financial
    public void ealsuchanged(AjaxBehaviorEvent event) {
        stud.setStudentAhLsuAllowrance(std_ealsu ? "Yes" : "No");
        if (std_ealsu == false) {
            stud.setEa_lsu_perc(0);
        }

        calc.refreshCalc(stud);
        stdo_ealsu = std_ealsu;
    }

    public void ealsuperc_changed(AjaxBehaviorEvent event) {
        if (std_ealsu) {
            calc.refreshCalc(stud);
        }
    }

    public void eanonlsuchanged(AjaxBehaviorEvent event) {
        stud.setStudentAgNonlsuAllowrance(std_eanonlsu ? (new BigDecimal(1)) : (new BigDecimal(0)));
        if (std_eanonlsu) {
            stud.setStudentAiEduAllowPer(new BigDecimal(calc.getEaNonLsuPercentageByDorm(std_dorm)).divide(new BigDecimal(100)));
            /*
            if( std_dorm){
                stud.setStudentAiEduAllowPer( new BigDecimal(calc.getEaNonLsuPercentage()).divide( new BigDecimal(100))); 
            }else{
                stud.setStudentAiEduAllowPer( new BigDecimal(calc.getEaNonLsuPercentage()).divide( new BigDecimal(100)));  
            }*/
        } else {
            stud.setStudentAiEduAllowPer(BigDecimal.ZERO);
        }
        calc.refreshCalc(stud);
        stdo_eanonlsu = std_eanonlsu;
    }

    public void noloanschanged(AjaxBehaviorEvent event) {
        //std_noloans  will effect inc_subloan  inc_unsubloan
        if (std_noloans) {
            in_subloan = false;
            in_unsubloan = false;
            stud.setStudentApSubLoans(in_subloan ? "Yes" : "No");
            stud.setStudentAqUnsubLoans(in_unsubloan ? "Yes" : "No");
        } else { //the loans options are on by default 2012-01-12
            in_subloan = true;
            in_unsubloan = true;
            stud.setStudentApSubLoans(in_subloan ? "Yes" : "No");
            stud.setStudentAqUnsubLoans(in_unsubloan ? "Yes" : "No");
        }
        stud.setIndExcloans(std_noloans ? "Yes" : "No");
        calc.refreshCalc(stud);
        stdo_noloans = std_noloans;
        stdo_in_subloan = in_subloan;
        stdo_in_unsubloan = in_unsubloan;
    }

    public void familymemberschanged(AjaxBehaviorEvent event) {
        calc.refreshCalc(stud);
    }

    public void familyincomechanged(AjaxBehaviorEvent event) {
        calc.refreshCalc(stud);
    }

    public void familyassetchanged(AjaxBehaviorEvent event) {
        calc.refreshCalc(stud);
    }

    ///institutional
    private int user_over_maxamt(int superind, int useramt, int oldamt, int fundmax) {
        if (superind > 0 || useramt <= fundmax) {
            return 1;
        }

        //String[] msgkeys ={"", "EstimateForm.institute.scholarship1.amtoverflow", "EstimateForm.institute.scholarship2.amtoverflow"};
        //if a normal user want to mod over limit amount, and still over limit, then
        return (useramt > oldamt) ? 0 : 1;
    }

    public void lsuawardamtchanged1(AjaxBehaviorEvent event) {
        int max = ref.fundMatchTopById(stud.getFund1id());
        int in = stud.getStudentAuScholarship1Amt();
        if (user_over_maxamt(login.getCurrentUser().getSuperuser(), in, sfund1amt, max) < 1) {
            //if( in>max  ){

            UIInput source = (UIInput) event.getSource(); //the object on which the event initially occoured
            source.setValid(false);
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship1.amtoverflow");
            if (facesContext != null) {
                facesContext.addMessage(null, msg);
            }
            return;
        }

        /*
        String note = stud.getStudentAtScholarship1Note();
        if( note == null || note.trim().isEmpty() ){
            stud.setStudentAtScholarship1Note("Scholarship #1 Note");
        }*/
        calc.refreshCalc(stud);
    }

    public void lsuawardamtchanged2(AjaxBehaviorEvent event) {
        int max = ref.fundMatchTopById(stud.getFund2id());
        int in = stud.getStudentAxScholarship2Amt();
        if (user_over_maxamt(login.getCurrentUser().getSuperuser(), in, sfund2amt, max) < 1) {
            //2013-02: 2013 new rule: normal user shall be able to go beyond the paper limit, but has a hidden top limit
            if (login.getCurrentUser().getSuperuser() == 0 && in <= ref.getUniversity_grant_hard_top_limit()) {
                //allow an over paper limit, but enforce a hidden limit
            } else {
                //if( in>max){
                UIInput source = (UIInput) event.getSource(); //the object on which the event initially occoured
                source.setValid(false);
                FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship2.amtoverflow");
                if (facesContext != null) {
                    facesContext.addMessage(null, msg);
                }
                return;
            }
        }
        /* 
        String note = stud.getStudentAwScholarship2Note();
        if( note == null || note.trim().isEmpty() ){
            stud.setStudentAwScholarship2Note("Scholarship #2 Note");
        }*/
        calc.refreshCalc(stud);
    }

    public void lsuawardamtchanged3(AjaxBehaviorEvent event) {
        /*
        String note = stud.getStudentAzScholarship3Note();
        if( note == null || note.trim().isEmpty() ){
            stud.setStudentAzScholarship3Note("Scholarship #3 Note");
        }*/
        calc.refreshCalc(stud);
    }

    public void lsuawardamtchanged4(AjaxBehaviorEvent event) {
        /*
        String note = stud.getStudentBcScholarship4Note();
        if( note == null || note.trim().isEmpty() ){
            stud.setStudentBcScholarship4Note("Scholarship #4 Note");
        }*/
        calc.refreshCalc(stud);
    }

    public void lsuawardamtchanged5(AjaxBehaviorEvent event) {
        /*  
        String note = stud.getStudentBfScholarship5Note();
        if( note == null || note.trim().isEmpty() ){
            stud.setStudentBfScholarship5Note("Scholarship #5 Note");
        }
        note = stud.getStudentBeScholarship5Name();
        if( note == null || note.trim().isEmpty() ){
            stud.setStudentBeScholarship5Name("Scholarship #5 Name");
        }*/
        calc.refreshCalc(stud);
    }

    public void lsuawardamtchanged6(AjaxBehaviorEvent event) {
        /*
        String note = stud.getStudentBiScholarship6Note();
        if( note == null || note.trim().isEmpty() ){
            stud.setStudentBiScholarship6Note("Scholarship #6 Note");
        }
        note = stud.getStudentBhScholarship6Name();
        if( note == null || note.trim().isEmpty() ){
            stud.setStudentBhScholarship6Name("Scholarship #6 Name");
        }*/
        calc.refreshCalc(stud);
    }

    public void lsuawardamtchanged7(AjaxBehaviorEvent event) {
        /*
        String note = stud.getStudentBlScholarship7Note();
        if( note == null || note.trim().isEmpty() ){
            stud.setStudentBlScholarship7Note("Scholarship #7 Note");
        }
        note = stud.getStudentBkScholarship7Name();
        if( note == null || note.trim().isEmpty() ){
            stud.setStudentBkScholarship7Name("Scholarship #7 Name");
        }*/

        int max = ref.fundMatchTopById(stud.getFund7id());
        int in = stud.getStudentBmScholarship7Amt();
        if (max > 0 && user_over_maxamt(login.getCurrentUser().getSuperuser(), in, sfund7amt, max) < 1) {
            //if( in>max){
            UIInput source = (UIInput) event.getSource(); //the object on which the event initially occoured
            source.setValid(false);
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship7.amtoverflow");
            if (facesContext != null) {
                facesContext.addMessage(null, msg);
            }
            return;
        }

        calc.refreshCalc(stud);
    }

    public void lsuawardamtchanged8(AjaxBehaviorEvent event) {
        /*
        String note = stud.getStudentBoScholarship8Note();
        if( note == null || note.trim().isEmpty() ){
            stud.setStudentBoScholarship8Note("Scholarship #8 Note");
        }
        note = stud.getStudentBnScholarship8Name();
        if( note == null || note.trim().isEmpty() ){
            stud.setStudentBnScholarship8Name("Scholarship #8 Name");
        }*/
        int max = ref.fundMatchTopById(stud.getFund8id());
        int in = stud.getStudentBpScholarship8Amt();
        if (max > 0 && user_over_maxamt(login.getCurrentUser().getSuperuser(), in, sfund8amt, max) < 1) {
            //if( in>max){
            UIInput source = (UIInput) event.getSource(); //the object on which the event initially occoured
            source.setValid(false);
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship8.amtoverflow");
            if (facesContext != null) {
                facesContext.addMessage(null, msg);
            }
            return;
        }

        calc.refreshCalc(stud);
    }

    public void lsuawardamtchanged9(AjaxBehaviorEvent event) {
        /*
        String note = stud.getStudentBrScholarship9Note();
        if( note == null || note.trim().isEmpty() ){
            stud.setStudentBrScholarship9Note("Scholarship #9 Note");
        }
        note = stud.getStudentBqScholarship9Name();
        if( note == null || note.trim().isEmpty() ){
            stud.setStudentBqScholarship9Name("Scholarship #9 Name");
        }*/
        int max = ref.fundMatchTopById(stud.getFund9id());
        int in = stud.getStudentBsScholarship9Amt();
        if (max > 0 && user_over_maxamt(login.getCurrentUser().getSuperuser(), in, sfund9amt, max) < 1) {
            //if( in>max){
            UIInput source = (UIInput) event.getSource(); //the object on which the event initially occoured
            source.setValid(false);
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.institute.scholarship9.amtoverflow");
            if (facesContext != null) {
                facesContext.addMessage(null, msg);
            }
            return;
        }

        calc.refreshCalc(stud);
    }

    public void lsuawardnoteschanged1(AjaxBehaviorEvent event) {
        String notes = stud.getStudentAtScholarship1Note();
        if (notes != null) {//!ref.isEmp(notes)){
            notes = notes.trim().toUpperCase();
            stud.setStudentAtScholarship1Note(notes);
        }
        //calc.refreshCalc(stud);
    }

    public void lsuawardnoteschanged2(AjaxBehaviorEvent event) {
        String notes = stud.getStudentAwScholarship2Note();
        if (notes != null) {//!ref.isEmp(notes)){
            notes = notes.trim().toUpperCase();
            stud.setStudentAwScholarship2Note(notes);
        }
        //calc.refreshCalc(stud);
    }

    public void lsuawardnoteschanged3(AjaxBehaviorEvent event) {
        String notes = stud.getStudentAzScholarship3Note();
        if (notes != null) {//!ref.isEmp(notes)){
            notes = notes.trim().toUpperCase();
            stud.setStudentAzScholarship3Note(notes);
        }
        //calc.refreshCalc(stud);
    }

    public void lsuawardnoteschanged4(AjaxBehaviorEvent event) {
        String notes = stud.getStudentBcScholarship4Note();
        if (notes != null) {//!ref.isEmp(notes)){
            notes = notes.trim().toUpperCase();
            stud.setStudentBcScholarship4Note(notes);
        }
        //calc.refreshCalc(stud);
    }

    public void lsuawardnoteschanged5(AjaxBehaviorEvent event) {
        String notes = stud.getStudentBfScholarship5Note();
        if (notes != null) {//!ref.isEmp(notes)){
            notes = notes.trim().toUpperCase();
            stud.setStudentBfScholarship5Note(notes);
        }
        //calc.refreshCalc(stud);
    }

    public void lsuawardnoteschanged6(AjaxBehaviorEvent event) {
        String notes = stud.getStudentBiScholarship6Note();
        if (notes != null) {//!ref.isEmp(notes)){
            notes = notes.trim().toUpperCase();
            stud.setStudentBiScholarship6Note(notes);
        }
        //calc.refreshCalc(stud);
    }

    public void lsuawardnoteschanged7(AjaxBehaviorEvent event) {
        String notes = stud.getStudentBlScholarship7Note();
        if (notes != null) {//!ref.isEmp(notes)){
            notes = notes.trim().toUpperCase();
            stud.setStudentBlScholarship7Note(notes);
        }
        //calc.refreshCalc(stud);
    }

    public void lsuawardnoteschanged8(AjaxBehaviorEvent event) {
        String notes = stud.getStudentBoScholarship8Note();
        if (notes != null) {//!ref.isEmp(notes)){
            notes = notes.trim().toUpperCase();
            stud.setStudentBoScholarship8Note(notes);
        }
        //calc.refreshCalc(stud);
    }

    public void lsuawardnoteschanged9(AjaxBehaviorEvent event) {
        String notes = stud.getStudentBrScholarship9Note();
        if (notes != null) {//!ref.isEmp(notes)){
            notes = notes.trim().toUpperCase();
            stud.setStudentBrScholarship9Note(notes);
        }
        //calc.refreshCalc(stud);
    }

    //2012-02-15 primefaces ptab misteriously calls/sets null value to fundid7 fundid8 fundid9, even though they DO has value (in std obj)
    public void lsuawardnamechanged7(AjaxBehaviorEvent event) {
        int f = stud.getFund7id();
        stdo_fundid7 = f;

        if (f > 0) {
            stud.setStudentBkScholarship7Name(ref.getFundDescById(f));  //fundid7
        } else {
            stud.setStudentBkScholarship7Name("");
            stud.setStudentBmScholarship7Amt(0);
        }
        calc.refreshCalc(stud);
    }

    public void lsuawardnamechanged8(AjaxBehaviorEvent event) {
        int f = stud.getFund8id();
        stdo_fundid8 = f;

        if (f > 0) {
            stud.setStudentBnScholarship8Name(ref.getFundDescById(f));//  fundid8
        } else {
            stud.setStudentBnScholarship8Name("");
            stud.setStudentBpScholarship8Amt(0);
        }
        calc.refreshCalc(stud);
    }

    public void lsuawardnamechanged9(AjaxBehaviorEvent event) {
        int f = stud.getFund9id();
        stdo_fundid9 = f;

        if (f > 0) {
            stud.setStudentBqScholarship9Name(ref.getFundDescById(f));// fundid9
        } else {
            stud.setStudentBqScholarship9Name("");
            stud.setStudentBsScholarship9Amt(0);
        }
        calc.refreshCalc(stud);
    }

    ////==========adjust
    public void calgrantadjustindchanged(AjaxBehaviorEvent event) {
        //if( adjust_calgrantamt_ind){            
        //}
        calc.setAdjustCalGrantAmtInd(adjust_calgrantamt_ind);
        stud.setAdjCalgrantInd(adjust_calgrantamt_ind ? "Yes" : "No");
        calc.refreshCalc(stud);
        this.stdo_adjust_calgrantamt_ind = this.adjust_calgrantamt_ind;
    }

    public void calgrantaamtchanged(AjaxBehaviorEvent event) {
        if (adjust_calgrantamt_ind) {
            if (stud.getStudentAaCalgrantA() > 0) { //the stud obj validate (min=0)
                stud.setStudentAbCalgrantB(0);
            }
            calc.refreshCalc(stud);
        }
        //stud.setStudentAaCalgrantA(0);               
    }

    public void calgrantbamtchanged(AjaxBehaviorEvent event) {
        if (adjust_calgrantamt_ind) {
            if (stud.getStudentAbCalgrantB() > 0) { //the stud obj validate (min=0)
                stud.setStudentAaCalgrantA(0);
            }
            calc.refreshCalc(stud);
        }
    }

    public void subloanindchanged(AjaxBehaviorEvent event) {   //in_subloan
        stud.setStudentApSubLoans(in_subloan ? "Yes" : "No");
        calc.refreshCalc(stud);
        stdo_in_subloan = in_subloan;
    }

    public void unsubloanindchanged(AjaxBehaviorEvent event) {   //in_unsubloan
        stud.setStudentAqUnsubLoans(in_unsubloan ? "Yes" : "No");
        calc.refreshCalc(stud);
        stdo_in_unsubloan = in_unsubloan;
    }

    public void fwsindchanged(AjaxBehaviorEvent event) {   //in_fws
        stud.setStudentArFws(in_fws ? "Yes" : "No");
        calc.refreshCalc(stud);
        stdo_in_fws = in_fws;
    }

    //// new var to indicate if the cal grant A/B amount is modified/override
    public boolean isAdjust_calgrantamt_ind() {
        return adjust_calgrantamt_ind;
    }

    public void setAdjust_calgrantamt_ind(boolean adjust_calgrantamt_ind) {
        this.adjust_calgrantamt_ind = adjust_calgrantamt_ind;
    }

    //=====================Geoff asked all text input field value to UPPER cased
    private String upper(String str) {
        return str == null ? null : str.toUpperCase(); //" " or "" ???
    }

    public void citychanged(AjaxBehaviorEvent event) {
        String city = stud.getStudentHCity();
        if (city != null) {
            city = city.trim();
            stud.setStudentHCity(upper(city));
        }
    }

    public void aptchanged(AjaxBehaviorEvent event) {
        String str = stud.getHomeAddrApt();
        if (str != null) {
            str = str.trim();
            stud.setHomeAddrApt(upper(str));
        }
    }

    public void addrchanged(AjaxBehaviorEvent event) {
        String str = stud.getStudentGStreet();
        if (str != null) {
            str = str.trim();
            stud.setStudentGStreet(upper(str));
        }
    }

    public void countrychanged(AjaxBehaviorEvent event) {
        String str = stud.getStudentKCountry();
        if (str != null) {
            str = str.trim();
            stud.setStudentKCountry(upper(str));
        }
    }

    public void emailchanged(AjaxBehaviorEvent event) {
        String str = stud.getStudentEEmail();
        if (str != null) {
            str = str.trim();
            stud.setStudentEEmail(upper(str));
        }
    }

    public void highschoolchanged(AjaxBehaviorEvent event) {
        String str = stud.getStudentOLastSchool();
        if (str != null) {
            str = str.trim();
            stud.setStudentOLastSchool(upper(str));
        }
    }

    public void majorchanged(AjaxBehaviorEvent event) {
        String str = stud.getStudentTMajor();
        if (str != null) {
            str = str.trim();
            stud.setStudentTMajor(upper(str));
        }
    }

    public void familyinlsuchanged(AjaxBehaviorEvent event) {
        String str = stud.getStudentVFamily();
        if (str != null) {
            str = str.trim();
            stud.setStudentVFamily(upper(str));
        }
        if (str != null && !str.isEmpty() && costudents == 0) {
            costudents = 1;
        }
        calc.refreshCalc(stud);
    }

    public void noncahelpchanged(AjaxBehaviorEvent event) {
        String str = stud.getStudentAjHomeState();
        //if(str!=null){
        if (!ref.isEmp(str)) {
            str = str.trim();
            stud.setStudentAjHomeState(upper(str));
            calc.refreshCalc(stud);
        } else {
            Integer amt = stud.getStudentAkNoncalGrant();
            if (amt == null || amt == 0) {
                stud.setStudentAjHomeState(null);
            } else {
                UIInput source = (UIInput) event.getSource(); //the object on which the event initially occoured
                source.setValid(false);
                FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.financial.noncagrant.nonamebutamt");
                facesContext.addMessage(null, msg);

                String msgkey = "EstimateForm.financial.noncagrant.defaultnotes";
                stud.setStudentAjHomeState(ref.getJSFMsgByKey(msgkey));// resourceBundle.getString(msgkey ) );
                calc.refreshCalc(stud);
            }
        }
    }

    public void noncahelpamtchanged(AjaxBehaviorEvent event) {
        String str = stud.getStudentAjHomeState();
        Integer amt = stud.getStudentAkNoncalGrant();
        if (amt != null && amt > 0 && ref.isEmp(str)) {
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.financial.noncagrant.nonamebutamt");
            facesContext.addMessage(null, msg);

            String msgkey = "EstimateForm.financial.noncagrant.defaultnotes";
            stud.setStudentAjHomeState(ref.getJSFMsgByKey(msgkey));
        }
        calc.refreshCalc(stud);
    }

    public void nonlsuhelpchanged(AjaxBehaviorEvent event) {
        String str = stud.getStudentAlOutScholarships();
        if (str != null) {
            str = str.trim();
            stud.setStudentAlOutScholarships(upper(str));
        }
    }

    public void outsidehelpamtchanged(AjaxBehaviorEvent event) {
        calc.refreshCalc(stud);
    }

    public String getFund1extra() {
        return fund1extra;
    }

    public String getFund2extra() {
        return fund2extra;
    }

    public String getFund3extra() {
        return fund3extra;
    }

    public String getFund4extra() {
        return fund4extra;
    }

    public String getFund5extra() {
        return fund5extra;
    }

    public String getFund6extra() {
        return fund6extra;
    }

    //new code to handle pop-up autocomplete of stud info==========================================================================
    public void dobchanged_org(AjaxBehaviorEvent event) { // keep it as before pop-up stud info
        //Date dob = stud.getStudentDDob();
        //stored temp in std_dob as string        
        //      log.info(" dob change event called in phase %s while dob=%s", event.getPhaseId(), std_dob); //NPE ???????
        if (this.std_indept == true) {
            if (std_indepts.containsKey("dob")) {
                std_indepts.remove("dob");
                if (std_indepts.size() == 0 && std_indept_byhand == 0) {
                    this.std_indept = false;
                }
            }
            stud.setStudentYIndept(std_indept == true ? "Yes" : "No");
            calc.refreshCalc(stud);
        }

        UIInput source = (UIInput) event.getSource(); //the object on which the event initially occoured
        String clientfid = source.getClientId();
        /*
       String clientsid = source.getId();
       check_inds.put(clientfid, clientfid);
       
       log.info("~~~~ got clientid=%s and set ind=%s, cheecking hasErrInd()...", clientfid, check_inds.containsKey(clientfid) );
       hasMsg(clientfid);
         */

        UIComponent com = event.getComponent();// the component which sent the event
        // log.info("  event occoured on obeject id== %s, clientid==%s , which value=%s", source.getId(), source.getClientId(), source.getValue()); 
        // log.info("  event sent by %s, and the clientID==", com.getId(), com.getClientId(facesContext));       
        //facesContext.isValidationFailed();
//       facesContext.validationFailed();

        source.setValid(false);
        // ((UIInput)com.findComponent("std_dob")).setValid(false);
        //      ((UIInput)com).setValid(false);

        // log.info("  facesContext.isValidationFailed() == %s, obj isValid()==%s", facesContext.isValidationFailed(), source.isValid());         
        // source.setValidatorMessage("  set a valiadator msg. so ??? ");
        if (std_dob == null || std_dob.isEmpty()) { //this will be handled by "required=true JSF validator
            ///           if( source.isRequired()) source.setValid(false);         
            return;
        } else if ((std_dob = std_dob.replaceFirst("^(00+)", "0")).isEmpty()) {
            //FacesMessage msg = new FacesMessage("Invalid Date","");
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NoDoB");
            facesContext.addMessage(clientfid, msg);
            ////          source.setValid(false);  

            //          log.info(" dob is null or empty. msg ???");
        } else {
            ////          source.setValid(false);  //only if its value is valid , then set it back
            /*
            DateTime dt   = new DateTime(dob);
            String strLow  = "01/01/"+(ref.getFiscal_year() - ref.getApp_young_limit()  ) ;
            String strHigh = "01/01/"+(ref.getFiscal_year() - ref.getApp_old_limit() ) ;
            DateTime low   = new DateTime(strLow);
            DateTime high  = new DateTime(strHigh);
             */
            std_dob = std_dob.replaceAll("[^\\d]", "");//std_dob.trim();        //.replaceAll( "[^\\d]", "" ) //.replaceAll("[^a-zA-Z0-9]", "");    
            //log.info(" dob changed. transformed value=[%s]", std_dob);           

            if (std_dob.indexOf("/") < 0) {
                Pattern pattern = Pattern.compile("^\\d*$");
                Matcher matcher = pattern.matcher(std_dob);
                if (matcher.matches()) {
                    //String dobstr = std_dob.replaceFirst("^(00+)", "0");                
                    int len = std_dob.length(); //   2x(x)yy 2x(x)yyyy [10]
                    //1278      12.1978(6)      12378(5)    1231978(7)          0123.78(6)  01231978   
                    //8278      82.1978(6)      82378(5)    8231978(7)          0823.78(6)  08231978 
                    //11278(5)  1121978(7)      1123.78(6)   11231978            1123.78(6)  11231978 
                    //1122.78(6) 11221978(8)    1122.78(6)   11221978            1122.78(6)  11221978 
                    char[] dob = std_dob.toCharArray();
                    switch (len) {
                        case 4:
                            std_dob = "0" + dob[0] + "/0" + dob[1] + "/19" + dob[2] + dob[3];
                            break;
                        case 5:
                            if (dob[0] > '1') {
                                std_dob = "0" + dob[0] + "/" + dob[1] + dob[2] + "/19" + dob[3] + dob[4];
                            } else if (dob[1] <= '2') {
                                std_dob = "" + dob[0] + dob[1] + "/0" + dob[2] + "/19" + dob[3] + dob[4]; // heading dob[0]+dob[1] will give 99
                            } else {
                                std_dob = "0" + dob[0] + "/" + dob[1] + dob[2] + "/19" + dob[3] + dob[4];
                            }
                            break;
                        case 6:
                            if (dob[0] == '0') {
                                std_dob = "" + dob[0] + dob[1] + "/" + dob[2] + dob[3] + "/19" + dob[4] + dob[5];
                            } else if (dob[0] == '1') {
                                if (dob[1] >= '3') {
                                    std_dob = "0" + dob[0] + "/0" + dob[1] + "/" + dob[2] + dob[3] + dob[4] + dob[5];
                                } else {
                                    std_dob = "" + dob[0] + dob[1] + "/" + dob[2] + dob[3] + "/19" + dob[4] + dob[5];
                                }
                            } else if (dob[0] > '1') {
                                std_dob = "0" + dob[0] + "/0" + dob[1] + "/" + dob[2] + dob[3] + dob[4] + dob[5];
                            }
                            break;
                        case 7:
                            if (dob[0] > '1') {
                                std_dob = "0" + dob[0] + "/" + dob[1] + dob[2] + "/" + dob[3] + dob[4] + dob[5] + dob[6];
                            } else if (dob[1] >= '3') {
                                std_dob = "0" + dob[0] + "/" + dob[1] + dob[2] + "/" + dob[3] + dob[4] + dob[5] + dob[6];
                            } else {
                                std_dob = "" + dob[0] + dob[1] + "/0" + dob[2] + "/" + dob[3] + dob[4] + dob[5] + dob[6];
                            }
                            break;
                        case 8:
                            std_dob = "" + dob[0] + dob[1] + "/" + dob[2] + dob[3] + "/" + dob[4] + dob[5] + dob[6] + dob[7];
                            break;
                        default:
                            break;
                    }
                }
            }

            Pattern pattern = Pattern.compile(DATE_PATTERN);
            Matcher matcher = pattern.matcher(std_dob);
            if (!matcher.matches()) {
                FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NoDoB");
                facesContext.addMessage(clientfid, msg);
                //              log.info(" dob is malformed. msg ???");
                return;
            } else {
                matcher.reset();
                if (matcher.find()) {
                    String day = matcher.group(2);
                    if (day.length() == 1) {
                        day = "0" + day;
                    }

                    String month = matcher.group(1);
                    if (month.length() == 1) {
                        month = "0" + month;
                    }

                    int year = Integer.parseInt(matcher.group(3));
                    if (year < 100) {
                        year += (year > 30 ? 1900 : 2000);
                    }
                    // " year="+year+" month="+month+" day="+day;
                    //Feb and Leap year
                    std_dob = month + "/" + day + "/" + year;
                    //                   log.info(" dob parsed as %s", std_dob); //NPE ?????????????
                }
            }

            DateTime dt = ref.parseDateStr(std_dob, ref.getDateInputShowStr());
            if (dt == null) {
                FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidDoB");
                facesContext.addMessage(clientfid, msg);
                //             log.info(" dob is not malformed, but not valid. msg ???");
                return;
            }

            DateMidnight dm = new DateMidnight(dt);  //dob            
            DateMidnight low = new DateMidnight(ref.getFiscal_year() - ref.getApp_young_limit(), 1, 1);
            DateMidnight high = new DateMidnight(ref.getFiscal_year() - ref.getApp_old_limit(), 1, 1);

            if (dm.isAfter(low) || dm.isBefore(high)) {
                FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NotSuitableDoB");
                facesContext.addMessage(clientfid, msg);
//                log.info(" dob is not in suitable range. msg ???");
                return;
            } else {
                DateMidnight due = new DateMidnight(ref.getIndept_dob());

                //if( dm.isBefore(due) ){
                if (!dm.isAfter(due)) {
                    if (!std_indepts.containsKey("dob")) {
                        std_indepts.put("dob", "dob");
                    }
                    this.std_indept = true;
                }
                ///////may calculate age
                //put into stud EJB
                stud.setStudentDDob(std_dob);//dm.toDate() );
                //check_inds.remove(clientfid);
                source.setValid(true);
//                log.info(" dob is good. set indept_ind and DOB of EJB");
            }
        }
    }

    public void chgedid(AjaxBehaviorEvent event) {
        //    log.info("^^^^^^^^^ chgedid()");        
        UIInput source = (UIInput) event.getSource(); //the object on which the event initially occoured
        String clientfid = source.getClientId();

        String lsuid = stud.getStudentALsuid();
        int flag = validateLsuId(lsuid);
        if (flag < 0) { //no value

        } else if (flag > 0) { //valid
            ;
        } else { //invalid
            source.setValid(false);
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidLSUID");
            facesContext.addMessage(clientfid, msg);
        }
        mkPops(flag, "id");
    }

    public int checkInvalidFilePathName(String strname) {
        int invalid = 0;
        char[] invalids = new char[]{'/', '\\', '"', '?', '%', '|', '*', '<', '>', '\''};
        if (strname != null && strname.length() > 0) {
            for (char c : invalids) {
                if (strname.indexOf(c) > -1) {
                    invalid = 1;
                    break;
                }
            }
        }
        return invalid;
    }

    public void lnamechanged(AjaxBehaviorEvent event) {
        //stud.studentBLastname
        String ln = std_ln;//stud.getStudentBLastname();
        //      log.info("$$$$$$$$$$$$$$$$$$ ajax lnamechanged() is invoked. bean value==%s", ln);
        //if( ln!=null && !ln.isEmpty() ){//|| ln.trim().length()>1){
        if (!ref.isEmp(ln)) {
            /*
            int len = ln.length();
            char[] fnames = ln.toCharArray();
            char former = ' ';
            char c;
            for(int i=0; i<len; i++){
                c = fnames[i];
                if( former==' ')fnames[i] = ln.substring(i, i+1).toUpperCase().charAt(0);
                former = fnames[i];
            }
             stud.setStudentBLastname( new String(fnames) );
             */
            ln = upper(ln).trim();
            /*
            int invalid=0;
            if( ln.length()>0){
                char[] invalids = new char[]{'/', '\\', '"', '?', '%', '|', '*', '<', '>' };
                for( char c : invalids){
                    if( ln.indexOf(c)>-1){
                        stud.setStudentBLastname("");
                        UIInput source = (UIInput)event.getSource(); //the object on which the event initially occoured
                        String clientfid = source.getClientId();
                        source.setValid(false);
                        invalid =1;
                        break;
                    }
                }
            }*/
            int invalid = checkInvalidFilePathName(ln);

            if (invalid > 0) {
                stud.setStudentBLastname("");
                UIInput source = (UIInput) event.getSource(); //the object on which the event initially occoured
                String clientfid = source.getClientId();
                source.setValid(false);
            } else {
                stud.setStudentBLastname(ln);
            }
            std_ln = ln;
            /*
            //ln = ln.trim();
            boolean blanktail = ln.equals(ln.trim());
            String[] lnames = ln.split(" ");
            StringBuilder sb = new StringBuilder(64);
            for(int i=0; i<lnames.length; i++ ){
                sb.append(lnames[i].substring(0, 1).toUpperCase()).append(lnames[i].substring(1)).append(" "); //.toLowerCase()
            }
            stud.setStudentBLastname( blanktail? sb.toString() : sb.toString().trim()); */
        } else {
            stud.setStudentBLastname("");
            UIInput source = (UIInput) event.getSource(); //the object on which the event initially occoured
            String clientfid = source.getClientId();
            source.setValid(false);
            /*
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NoLName");            
            facesContext.addMessage(clientfid, msg); 
             */
        }
        mkPops(validateLn(ln), "ln");
    }

    public void fnamechanged(AjaxBehaviorEvent event) {
        //stud.studentCFirstname
        String fn = std_fn;//stud.getStudentCFirstname();
        //   log.info("$$$$$$$$$$$$$$$$$$ ajax fnamechanged() is invoked. bean value==%s", fn);
        //if( fn!=null && !fn.isEmpty()){
        if (!ref.isEmp(fn)) {
            /*            
            int len = fn.length();
            char[] fnames = fn.toCharArray();
            char former = ' ';
            char c;
            for(int i=0; i<len; i++){
                c = fnames[i];
                if( former==' ')fnames[i] = fn.substring(i, i+1).toUpperCase().charAt(0);
                former = fnames[i];
            }
             stud.setStudentCFirstname( new String(fnames) ); //Arrays.toString(fnames)  or fnames.toString()  or String.valueOf()
             */

            fn = upper(fn).trim();

            int invalid = checkInvalidFilePathName(fn);

            if (invalid > 0) {
                stud.setStudentCFirstname("");
                UIInput source = (UIInput) event.getSource(); //the object on which the event initially occoured
                String clientfid = source.getClientId();
                source.setValid(false);
            } else {
                stud.setStudentCFirstname(fn);
            }
            std_fn = fn;
            /*
            //fn = fn.trim();
            boolean blanktail = fn.equals(fn.trim());             
            String[] fnames = fn.split(" ");
            StringBuilder sb = new StringBuilder(64);
            for(int i=0; i<fnames.length; i++ ){
                sb.append(fnames[i].substring(0, 1).toUpperCase()).append(fnames[i].substring(1)).append(" "); //.toLowerCase()
            }
            stud.setStudentCFirstname( blanktail? sb.toString() : sb.toString().trim());              
             */
        } else {
            stud.setStudentCFirstname("");
            UIInput source = (UIInput) event.getSource(); //the object on which the event initially occoured
            String clientfid = source.getClientId();
            source.setValid(false);
            /*
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NoFName");            
            facesContext.addMessage(clientfid, msg); 
            
             */
        }
        mkPops(validateFn(fn), "fn");
    }

    private int validateLsuId(String lsuid) {
        String longid = lsuid;//+"       ";
        int res = 0;
        if (lsuid == null) {
            return -1;
        }
        lsuid = lsuid.trim();
        int len = lsuid.length();
        if (len == 0) {
            stud.setStudentALsuid(null);
            return -1;
        }
        if (len < 6) {
            //stud.setStudentALsuid(longid);            
            return res;
        }
        try {
            len = Integer.parseInt(lsuid);
            if (!lsuid.equals(String.valueOf(Integer.parseInt(lsuid)))) {
                //stud.setStudentALsuid(longid);                
                return res;
            }
        } catch (Exception e) {
            //stud.setStudentALsuid(longid);            
            return res;
        }
        return 1;
    }

    private int validateLn(String ln) {
        if (ref.isEmp(ln)) {
            return -1;
        }
        String str = ln;//.trim();  
        //      stud.setStudentBLastname(str);
        int len = str.length();
        if (len == 0) {
            return -1;
        } else {
            //str = str.replaceAll("%%", "%");
            int found = str.indexOf("%") >= 0 ? 1 : 0 + str.indexOf("_") >= 0 ? 1 : 0;//Math.abs(str.indexOf("%")) + Math.abs( str.indexOf("_")) ;
            if (found > 0) {
                //String[] splits = str.split("%");
                return 1;
            } else {
                return 0; // not invalid, but means no wildcard
            }
        }
    }

    private int validateFn(String fn) {
        if (ref.isEmp(fn)) {
            return -1;
        }
        String str = fn;//fn.trim();  
        //     stud.setStudentCFirstname(str);
        int len = str.length();
        if (len == 0) {
            return -1;
        } else {
            //str = str.replaceAll("%%", "%");
            int found = str.indexOf("%") >= 0 ? 1 : 0 + str.indexOf("_") >= 0 ? 1 : 0;//Math.abs(str.indexOf("%")) + Math.abs( str.indexOf("_")) ; //derby supports "_" as a single  wildcard char
            if (found > 0) {
                //String[] splits = str.split("%");
                return 1;
            } else {
                return 0;
            }
        }
    }

    public void dobchanged(AjaxBehaviorEvent event) {
        //Date dob = stud.getStudentDDob();
        //stored temp in std_dob as string        
        //      log.info(" dob change event called in phase %s while dob=%s", event.getPhaseId(), std_dob); //NPE ???????
        if (this.std_indept == true) {
            if (std_indepts.containsKey("dob")) {
                std_indepts.remove("dob");
                if (std_indepts.size() == 0 && std_indept_byhand == 0) {
                    this.std_indept = false;
                }
            }
            stud.setStudentYIndept(std_indept == true ? "Yes" : "No");
            calc.refreshCalc(stud);
        }

        UIInput source = (UIInput) event.getSource(); //the object on which the event initially occoured
        String clientfid = source.getClientId();
        /*
       String clientsid = source.getId();
       check_inds.put(clientfid, clientfid);
       
       log.info("~~~~ got clientid=%s and set ind=%s, cheecking hasErrInd()...", clientfid, check_inds.containsKey(clientfid) );
       hasMsg(clientfid);
         */
        //      log.info("~~~~ got clientid=%s ...", clientfid); //auth-sv-ptab-std_dob
        UIComponent com = event.getComponent();// the component which sent the event
        // log.info("  event occoured on obeject id== %s, clientid==%s , which value=%s", source.getId(), source.getClientId(), source.getValue()); 
        // log.info("  event sent by %s, and the clientID==", com.getId(), com.getClientId(facesContext));       
        //facesContext.isValidationFailed();
//       facesContext.validationFailed();

////       source.setValid(false);
        // ((UIInput)com.findComponent("std_dob")).setValid(false);
        //      ((UIInput)com).setValid(false);
        // log.info("  facesContext.isValidationFailed() == %s, obj isValid()==%s", facesContext.isValidationFailed(), source.isValid());         
        // source.setValidatorMessage("  set a valiadator msg. so ??? ");
        //   log.info("$$$$$$$$$$$$$$$$$$ ajax dobchanged() is invoked. bean value==%s", std_dob);
        std_dob = std_dob.replaceAll("[^\\d]", "");//std_dob.trim();        //.replaceAll( "[^\\d]", "" ) //.replaceAll("[^a-zA-Z0-9]", "");    
        //log.info(" dob changed. transformed value=[%s]", std_dob); 

        int chk = validateDobStr(std_dob);

        if (chk == 0) { //not valid
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.InvalidDoB");
            facesContext.addMessage(clientfid, msg);
        } else if (chk == -1 || chk == -2) { //transfered to empty
/*           FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NoDoB");            
           facesContext.addMessage(clientfid, msg); */
        } else if (chk == 2) {
            FacesMessage msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "EstimateForm.NotSuitableDoB");
            facesContext.addMessage(clientfid, msg);
//                log.info(" dob is not in suitable range. msg ???");
            //return;
        }
        if (chk == 1) {
            DateTime dt = ref.parseDateStr(std_dob, ref.getDateInputShowStr());
            DateMidnight dm = new DateMidnight(dt);  //dob 
            DateMidnight due = new DateMidnight(ref.getIndept_dob());
            if (dm.isBefore(due)) {
                if (!std_indepts.containsKey("dob")) {
                    std_indepts.put("dob", "dob");
                }
                this.std_indept = true;
            }
            ///////may calculate age
            //put into stud EJB
            stud.setStudentDDob(std_dob);//dm.toDate() );
            //check_inds.remove(clientfid);
////                source.setValid(true); 
//                log.info(" dob is good. set indept_ind and DOB of EJB");             
        }
        mkPops(chk, "dob");
    }

    private int validateDobStr(String dobstr) {
        String strdob = dobstr;

        if (strdob == null || strdob.isEmpty()) { //this will be handled by "required=true JSF validator
            ///           if( source.isRequired()) source.setValid(false);         
            return -1;
        } else if ((strdob = strdob.replaceFirst("^(00+)", "0")).isEmpty()) {
            return -2;
        } else {
            ////          source.setValid(false);  //only if its value is valid , then set it back
            /*
            DateTime dt   = new DateTime(dob);
            String strLow  = "01/01/"+(ref.getFiscal_year() - ref.getApp_young_limit()  ) ;
            String strHigh = "01/01/"+(ref.getFiscal_year() - ref.getApp_old_limit() ) ;
            DateTime low   = new DateTime(strLow);
            DateTime high  = new DateTime(strHigh);
             */
            strdob = strdob.trim();

            if (strdob.indexOf("/") < 0) {
                Pattern pattern = Pattern.compile("^\\d*$");
                Matcher matcher = pattern.matcher(strdob);
                if (matcher.matches()) {
                    //String dobstr = std_dob.replaceFirst("^(00+)", "0");                
                    int len = strdob.length(); //   2x(x)yy 2x(x)yyyy [10]
                    //1278      12.1978(6)      12378(5)    1231978(7)          0123.78(6)  01231978   
                    //8278      82.1978(6)      82378(5)    8231978(7)          0823.78(6)  08231978 
                    //11278(5)  1121978(7)      1123.78(6)   11231978            1123.78(6)  11231978 
                    //1122.78(6) 11221978(8)    1122.78(6)   11221978            1122.78(6)  11221978 
                    char[] dob = strdob.toCharArray();
                    switch (len) {
                        case 4:
                            strdob = "0" + dob[0] + "/0" + dob[1] + "/19" + dob[2] + dob[3];
                            break;
                        case 5:
                            if (dob[0] > '1') {
                                strdob = "0" + dob[0] + "/" + dob[1] + dob[2] + "/19" + dob[3] + dob[4];
                            } else if (dob[1] <= '2') {
                                strdob = "" + dob[0] + dob[1] + "/0" + dob[2] + "/19" + dob[3] + dob[4]; // heading dob[0]+dob[1] will give 99
                            } else {
                                strdob = "0" + dob[0] + "/" + dob[1] + dob[2] + "/19" + dob[3] + dob[4];
                            }
                            break;
                        case 6:
                            if (dob[0] == '0') {
                                strdob = "" + dob[0] + dob[1] + "/" + dob[2] + dob[3] + "/19" + dob[4] + dob[5];
                            } else if (dob[0] == '1') {
                                if (dob[1] >= '3') {
                                    strdob = "0" + dob[0] + "/0" + dob[1] + "/" + dob[2] + dob[3] + dob[4] + dob[5];
                                } else {
                                    strdob = "" + dob[0] + dob[1] + "/" + dob[2] + dob[3] + "/19" + dob[4] + dob[5];
                                }
                            } else if (dob[0] > '1') {
                                strdob = "0" + dob[0] + "/0" + dob[1] + "/" + dob[2] + dob[3] + dob[4] + dob[5];
                            }
                            break;
                        case 7:
                            if (dob[0] > '1') {
                                strdob = "0" + dob[0] + "/" + dob[1] + dob[2] + "/" + dob[3] + dob[4] + dob[5] + dob[6];
                            } else if (dob[1] >= '3') {
                                strdob = "0" + dob[0] + "/" + dob[1] + dob[2] + "/" + dob[3] + dob[4] + dob[5] + dob[6];
                            } else {
                                strdob = "" + dob[0] + dob[1] + "/0" + dob[2] + "/" + dob[3] + dob[4] + dob[5] + dob[6];
                            }
                            break;
                        case 8:
                            strdob = "" + dob[0] + dob[1] + "/" + dob[2] + dob[3] + "/" + dob[4] + dob[5] + dob[6] + dob[7];
                            break;
                        default:
                            break;
                    }
                }
            }

            Pattern pattern = Pattern.compile(DATE_PATTERN);
            Matcher matcher = pattern.matcher(strdob);
            if (!matcher.matches()) {
                //              log.info(" dob is malformed. msg ???");
                return 0;
            } else {
                matcher.reset();
                if (matcher.find()) {
                    String day = matcher.group(2);
                    if (day.length() == 1) {
                        day = "0" + day;
                    }

                    String month = matcher.group(1);
                    if (month.length() == 1) {
                        month = "0" + month;
                    }

                    int year = Integer.parseInt(matcher.group(3));
                    if (year < 100) {
                        year += (year > 30 ? 1900 : 2000);
                    }
                    // " year="+year+" month="+month+" day="+day;
                    //Feb and Leap year
                    std_dob = month + "/" + day + "/" + year;
                    //                   log.info(" dob parsed as %s", std_dob); //NPE ?????????????
                }
            }

            DateTime dt = ref.parseDateStr(strdob, ref.getDateInputShowStr());
            if (dt == null) {
                //  log.info(" dob is not malformed, but not valid. msg ???");
                return 0;
            }
            DateMidnight dm = new DateMidnight(dt);  //dob            
            DateMidnight low = new DateMidnight(ref.getFiscal_year() - ref.getApp_young_limit(), 1, 1);
            DateMidnight high = new DateMidnight(ref.getFiscal_year() - ref.getApp_old_limit(), 1, 1);
            if (dm.isAfter(low) || dm.isBefore(high)) {
                return 2;
            }
            //stud.setStudentDDob();
            std_dob = strdob;
            return 1;
        }
    }

    private void mkPops(int validInd, String src) {
        this.chosenPopStud = null;
        log.info("$$$$$$$$$$$$$$$$$$ change event handler got field=[%s] , ind=[%d]", src, validInd);
        //check if this is invalid input (only for id and dob). if yes, cancel the popupif exists, and no generation
        if ((src.equals("id") && validInd == 0) || (src.equals("dob") && (validInd == 0 || validInd == 2))) {
            //  log.info("$$$$$$$$$$$$$$$$$$ change event handler returns   invalid [%d] of  field [%s]", validInd, src);
            popind = "0";
            return;
        }
        //check other fields valid status ( id or dob), if any is invalid, cancel the popup if exists, and no generation
        int chkId = 2, go = 0, chkLn = 2, chkFn = 2, chkDob = 3;
        switch (src) {
            case "ln":
                chkLn = validInd;
                go++;
            case "fn":
                chkFn = (chkLn == 2 ? validInd : 2);
                go++;
            case "id":
                if (go == 0) {
                    chkId = validInd;
                }
                chkDob = validateDobStr(std_dob);
                if (go == 0) {
                    break;
                }
            case "dob":
                if (chkDob == 3) {
                    chkDob = validInd;
                    chkId = validateLsuId(stud.getStudentALsuid());
                }
                break;
            default:
                break;
        }

        if (chkLn > 1) {
            chkLn = validateLn(std_ln); //stud.getStudentBLastname()
        }
        if (chkFn > 1) {
            chkFn = validateFn(std_fn); //stud.getStudentCFirstname()
        }
        if (chkId > 1) {
            chkId = validateLsuId(stud.getStudentALsuid());
        }

        if (chkId == 0 || chkDob == 0 || chkDob == 2) {
            popind = "0";
            //  log.info("$$$$$$$$$$$$$$$$$$ change event handler returns since ID or DOB field is invalid");
            return;
        }
        if (chkId < 0 && chkDob < 0 && chkLn < 0 && chkFn < 0) {
            popind = "0";
            //  log.info("$$$$$$$$$$$$$$$$$$ change event handler returns since all 4 fields are empty ?");
            return;
        }

        //to generate popups ( if the KEY is diff. KEY = (empty id? '':id)||(empty ln? '':upper(ln))||(empty fn? '':upper(fn))||(empty dob? '':dob), otherwise, only set popind=1
        String key = genPopKey();
        log.info("$$$$$$$$$$$$$$$$$$ change event handler got key=%s, while keptkey=%s", key, popkey);
        if (key.equals(popkey) && popups != null && popups.size() > 0) {
            //popind= ( popups==null || popups.size()==0)? "0":"1";

            //2013-02-11 Ken filtered RECID in the cache, gen by former query
            String onerec = stud.getRecid();
            List<Student> diffs = new ArrayList<>();
            for (Student one : popups) {
                if (one.getRecid().equals(onerec)) {
                    continue;
                }
                diffs.add(one);
            }

            if (diffs.size() > 0) {
                popind = "1";
                popups = diffs;
                //if( popDataModel==null || popDataModel.getWrappedData()==null || popDataModel.getRowCount()==0 ) 
                popDataModel = new QueryStudModel(popups);
                log.info("$$$$$$$$$$$$$$$$$$ change event handler returns since key matches: %s. popind=%s, popups size=%d", key, popind, popups.size());
                return;
            } else {

            }

        }
//@NamedQuery(name = "Student.findPOPWID", query="SELECT s FROM Student s where s.pickupInd=1 and s.studentALsuid= :lsuid and UPPER(s.studentBLastname) LIKE :ln and UPPER(s.studentCFirstname) LIKE :fn and char(s.studentDDob) like :dob ORDER BY s.ddoe desc "),
//@NamedQuery(name = "Student.findPOPWOID", query="SELECT s FROM Student s where s.pickupInd=1 and UPPER(s.studentBLastname) LIKE :ln and UPPER(s.studentCFirstname) LIKE :fn and char(s.studentDDob) like :dob ORDER BY s.ddoe desc "),
//SELECT s  FROM Student s where s.pickupInd=1 and UPPER(s.studentBLastname) LIKE '%' and UPPER(s.studentCFirstname) LIKE '%' and char(s.studentDDob) like '1968-01-02' ORDER BY s.ddoe desc
//SELECT s.* FROM Student s where s.pickup_Ind=1 and UPPER(s.student_B_Lastname) LIKE '%' and UPPER(s.student_C_Firstname) LIKE '%' and char(s.student_D_Dob) like '1968-01-02' ORDER BY s.ddoe desc
        String ln = chkLn < 0 ? "%" : std_ln.toUpperCase(); //stud.getStudentBLastname()
        String fn = chkFn < 0 ? "%" : std_fn.toUpperCase(); //stud.getStudentCFirstname()
        String dob = chkDob < 0 ? "%" : std_dob;//stud.getStudentDDob();//ref.getDerbyFmtStrbyDate(stud.getStudentDDob()); 

        List<Student> pickups = null;
        String onerec = stud.getRecid();
        if (chkId > 0) {//src.equals("id") && validInd>0  ){ //valid lsuid            
            if (onerec != null) {
                pickups = (List<Student>) em.createNamedQuery("Student.findPOPWIDwrecid")
                        .setParameter("lsuid", stud.getStudentALsuid())
                        .setParameter("ln", ln)
                        .setParameter("fn", fn)
                        .setParameter("dob", dob) //ref.isEmp(std_dob)?"%":std_dob)  //derby char(date) === yyyy-mm-dd format
                        .setParameter("recid", onerec)
                        .getResultList();
            } else {
                pickups = (List<Student>) em.createNamedQuery("Student.findPOPWID")
                        .setParameter("lsuid", stud.getStudentALsuid())
                        .setParameter("ln", ln)
                        .setParameter("fn", fn)
                        .setParameter("dob", dob) //ref.isEmp(std_dob)?"%":std_dob)  //derby char(date) === yyyy-mm-dd format                                                    
                        .getResultList();
            }
            //log.info("$$$$$$$$$$$$$$$$$$ change event handler queried db with lsuid(validind=%d)=[%s], ln=[%s], fn=[%s],dob=[%s]. get %d", chkId, stud.getStudentALsuid(), ln, fn, dob, pickups.size());

        } else {
            if (onerec != null) {
                pickups = (List<Student>) em.createNamedQuery("Student.findPOPWOIDwrecid")
                        .setParameter("ln", ln)
                        .setParameter("fn", fn)
                        .setParameter("dob", dob)//ref.isEmp(std_dob)?"%":std_dob)
                        .setParameter("recid", onerec)
                        .getResultList();
            } else {
                pickups = (List<Student>) em.createNamedQuery("Student.findPOPWOID")
                        .setParameter("ln", ln)
                        .setParameter("fn", fn)
                        .setParameter("dob", dob)//ref.isEmp(std_dob)?"%":std_dob)                                                     
                        .getResultList();
            }
            //log.info("$$$$$$$$$$$$$$$$$$ change event handler queried db without lsuid(validind=%d), ln=[%s], fn=[%s],dob=[%s]. get %d", chkId, ln, fn, dob, pickups.size());
            //$$$$$$$$$$$$$$$$$$ change event handler queried db without lsuid, ln=[LAST%], fn=[%],dob=[]. get 0 
        }
        if (popups != null) {
            popups.clear();
        }
        popups = pickups;
        popkey = key;

        if (popups == null) {
            popups = new ArrayList<Student>();
        }
        popDataModel = new QueryStudModel(popups);
        if (pickups == null || pickups.size() == 0) {
            popind = "0";
        } else {
            popind = "1";
        }
        log.info("$$$$$$$$$$$$$$$$$$ change event handler made quey/match, pop amt=%d, marked popind=%s, while key=%s. filter RECID=%s", popups.size(), popind, popkey, stud.getRecid());
    }

    /*    
    private String transformDobStr(){ //MM/dd/yyyy  to yyyy-MM-dd
        //stud.setStudentDDob  OR String dobstr
        return ref.getDerbyFmtStrbyDate(stud.getStudentDDob());
    }*/

    private String genPopKey() {
        StringBuilder sb = new StringBuilder(128);
        /*
        sb.append(ref.isEmp(stud.getStudentALsuid() )? "":stud.getStudentALsuid() ).append("|").append( ref.isEmp(stud.getStudentBLastname())?"":stud.getStudentBLastname().toUpperCase() ).append("|");
        sb.append( ref.isEmp( stud.getStudentCFirstname())? "":stud.getStudentCFirstname().toUpperCase() ).append("|").append( ref.isEmp( std_dob)?"":std_dob ); */
        sb.append(ref.isEmp(stud.getStudentALsuid()) ? "" : stud.getStudentALsuid()).append("|").append(ref.isEmp(std_ln) ? "" : std_ln.toUpperCase()).append("|");
        sb.append(ref.isEmp(std_fn) ? "" : std_fn.toUpperCase()).append("|").append(ref.isEmp(std_dob) ? "" : std_dob);
        return sb.toString();
    }

    public void pickpop(org.primefaces.event.SelectEvent event) {
        //log.info("$$$$$$$$$$$$$$$$$$ pickpop() is invoked.got stud: %s, e.src=%s", chosenPopStud==null? "NUll": chosenPopStud.getRecid(), event.getObject().toString() );
        Student pickup = (Student) event.getObject();
        log.info("$$$$$$$$$$$$$$$$$$ pickpop() got student recid=%s", pickup == null ? "NULL" : pickup.getRecid());
        modEstimate(pickup);

        popind = "0";
    }

    public String getPopind() {
        return popind;
    }

    public void setPopind(String popind) {
        this.popind = popind;
    }

    public QueryStudModel getPopDataModel() {
        return popDataModel;
    }

    public void setPopDataModel(QueryStudModel popDataModel) {
        this.popDataModel = popDataModel;
    }

    public Student getChosenPopStud() {
        return chosenPopStud;
    }

    public void setChosenPopStud(Student chosenPopStud) {
        this.chosenPopStud = chosenPopStud;
    }

    public List<Student> getPopups() {
        return popups;
    }

    public void setPopups(List<Student> popups) {
        this.popups = popups;
    }

    public String getStd_ln() {
        return std_ln;
    }

    public void setStd_ln(String std_ln) {
        this.std_ln = std_ln;
    }

    public String getStd_fn() {
        return std_fn;
    }

    public void setStd_fn(String std_fn) {
        this.std_fn = std_fn;
    }

    public String sumrow(int init) {
        tbodyrow = init - 1;
        return sumrow();
    }

    public String sumrow() {
        tbodyrow++;
        return tbodyrow % 2 == 0 ? "evenrow" : "oddrow";
    }

    public void chgpubnotes(AjaxBehaviorEvent event) {
        String notes = stud.getStudentAnPubNotes();
        if (notes != null) {//!ref.isEmp(notes)){
            notes = notes.trim();
            stud.setStudentAnPubNotes(notes);
        }
    }

    public void chgprinotes(AjaxBehaviorEvent event) {
        String notes = stud.getStudentAoPriNotes();
        if (notes != null) {//!ref.isEmp(notes)){
            notes = notes.trim();
            stud.setStudentAoPriNotes(notes);
        }
    }

    public void chgsex(AjaxBehaviorEvent event) {
        stdo_sex = std_sex;
        stud.setSex(std_sex);
    }

    public void chgsex1(AjaxBehaviorEvent event) {
        if (std_sex1 == true) {
            std_sex = "F";
            std_sex2 = false;
            std_sex3 = false;
        } else if (std_sex2 == false && std_sex3 == false) {
            std_sex3 = true;
            std_sex = "N";
        }
        stdo_sex = std_sex;
        stud.setSex(std_sex);
    }

    public void chgsex2(AjaxBehaviorEvent event) {
        if (std_sex2 == true) {
            std_sex = "M";
            std_sex1 = false;
            std_sex3 = false;
        } else if (std_sex1 == false && std_sex3 == false) {
            std_sex3 = true;
            std_sex = "N";
        }
        stdo_sex = std_sex;
        stud.setSex(std_sex);
    }

    public void chgsex3(AjaxBehaviorEvent event) {
        if (std_sex3 == true) {
            std_sex = "N";
            std_sex2 = false;
            std_sex1 = false;
        } else if (std_sex2 == false && std_sex1 == false) {
            std_sex3 = true;
            std_sex = "N";
        }
        stdo_sex = std_sex;
        stud.setSex(std_sex);
    }

    public HashMap<String, String> getStdo_indepts() {
        return stdo_indepts;
    }

    public Integer getStdo_fundid7() {
        return stdo_fundid7;
    }

    public Integer getStdo_fundid8() {
        return stdo_fundid8;
    }

    public Integer getStdo_fundid9() {
        return stdo_fundid9;
    }

    public String getStdo_sex() {
        return stdo_sex;
    }

    public boolean isStdo_intl() {
        return stdo_intl;
    }

    public boolean isStdo_marry() {
        return stdo_marry;
    }

    public boolean isStdo_sda() {
        return stdo_sda;
    }

    public boolean isStdo_dorm() {
        return stdo_dorm;
    }

    public boolean isStdo_indept() {
        return stdo_indept;
    }

    public boolean isStdo_fafsa() {
        return stdo_fafsa;
    }

    public boolean isStdo_calgrant() {
        return stdo_calgrant;
    }

    public boolean isStdo_efc() {
        return stdo_efc;
    }

    public boolean isStdo_ealsu() {
        return stdo_ealsu;
    }

    public boolean isStdo_eanonlsu() {
        return stdo_eanonlsu;
    }

    public boolean isStdo_noloans() {
        return stdo_noloans;
    }

    public boolean isStdo_in_subloan() {
        return stdo_in_subloan;
    }

    public boolean isStdo_in_unsubloan() {
        return stdo_in_unsubloan;
    }

    public boolean isStdo_in_fws() {
        return stdo_in_fws;
    }

    public boolean isStdo_adjust_calgrantamt_ind() {
        return stdo_adjust_calgrantamt_ind;
    }

    /*
    public List<String> getStdo_merits() {
        return stdo_merits;
    }*/
    public String getStdo_merits() {
        return stdo_merits;
    }

    public void setClientCallBack(RequestContext context) {
        if (context == null) {
            return;
        }
        //context.addCallbackParam("saved", true);    //basic parameter  
        ////context.addCallbackParam("user", user);     //pojo as json  
        context.addCallbackParam("ss_sex", stdo_sex);
        context.addCallbackParam("ss_intl", stdo_intl);
        context.addCallbackParam("ss_marry", stdo_marry);
        context.addCallbackParam("ss_sda", stdo_sda);
        context.addCallbackParam("ss_indept", stdo_indept);// ss_indepts=#{estimator.stdo_indepts} <br/>
        context.addCallbackParam("ss_indepts", stdo_indepts.size()); ///disabled or not

        context.addCallbackParam("ss_merit", stdo_merits);//stdo_merits==null||stdo_merits.size()==0 ? "": stdo_merits.get(0));// stud.getStudentSMerit());// stdo_merits =[] or [x]
        //log.info("-----setClientCallBack() set ss_merit from   stdo_merits==[%s]",   stdo_merits.toString());
        context.addCallbackParam("ss_academic", stdo_academic); //stdo_academic

        context.addCallbackParam("ss_fafsa", stdo_fafsa);
        context.addCallbackParam("ss_calgrant", stdo_calgrant);
        context.addCallbackParam("ss_ealsu", stdo_ealsu);
        context.addCallbackParam("ss_eanonlsu", stdo_eanonlsu);
        context.addCallbackParam("ss_efc", stdo_efc);
        context.addCallbackParam("ss_noloans", stdo_noloans);
        context.addCallbackParam("ss_dorm", stdo_dorm);

        context.addCallbackParam("ss_awd7", stdo_fundid7.intValue());
        context.addCallbackParam("ss_awd8", stdo_fundid8.intValue());
        context.addCallbackParam("ss_awd9", stdo_fundid9.intValue());

        context.addCallbackParam("ss_adjcal", stdo_adjust_calgrantamt_ind);
        context.addCallbackParam("ss_subloan", stdo_in_subloan);
        context.addCallbackParam("ss_unsubloan", stdo_in_unsubloan);
        context.addCallbackParam("ss_fws", stdo_in_fws);

        context.addCallbackParam("ss_return", stdo_return_ind);
        context.addCallbackParam("ss_nc", stdo_nc_ind);

        context.addCallbackParam("ss_ptab", tab_index);
        //querier.selectedStud
        //context.addCallbackParam("select", this.selectedStud!=null);                 

        //execute javascript oncomplete  
        ////context.execute("alert('Hello from the Backing Bean, in method procInfoBtn().');");  
        //context.execute("c=true;");        
        //update panel  
        ////context.addPartialUpdateTarget("form:panel");  
        //add facesmessage  
        ////FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Success", "Success"));  
    }

    public String getStdo_academic() {
        return stdo_academic;
    }

    public boolean isStd_sex1() {
        return std_sex1;
    }

    public void setStd_sex1(boolean std_sex1) {
        this.std_sex1 = std_sex1;
    }

    public boolean isStd_sex2() {
        return std_sex2;
    }

    public void setStd_sex2(boolean std_sex2) {
        this.std_sex2 = std_sex2;
    }

    public boolean isStd_sex3() {
        return std_sex3;
    }

    public void setStd_sex3(boolean std_sex3) {
        this.std_sex3 = std_sex3;
    }

    public boolean isStd_merit1() {
        return std_merit1;
    }

    public void setStd_merit1(boolean std_merit1) {
        this.std_merit1 = std_merit1;
    }

    public boolean isStd_merit2() {
        return std_merit2;
    }

    public void setStd_merit2(boolean std_merit2) {
        this.std_merit2 = std_merit2;
    }

    public boolean isStd_merit3() {
        return std_merit3;
    }

    public void setStd_merit3(boolean std_merit3) {
        this.std_merit3 = std_merit3;
    }

    /*
    public boolean isStd_cat1() {
        return std_cat1;
    }

    public void setStd_cat1(boolean std_cat1) {
        this.std_cat1 = std_cat1;
    }

    public boolean isStd_cat2() {
        return std_cat2;
    }

    public void setStd_cat2(boolean std_cat2) {
        this.std_cat2 = std_cat2;
    }

    public boolean isStd_cat3() {
        return std_cat3;
    }

    public void setStd_cat3(boolean std_cat3) {
        this.std_cat3 = std_cat3;
    }

    public boolean isStd_cat4() {
        return std_cat4;
    }

    public void setStd_cat4(boolean std_cat4) {
        this.std_cat4 = std_cat4;
    }

    public boolean isStd_cat5() {
        return std_cat5;
    }

    public void setStd_cat5(boolean std_cat5) {
        this.std_cat5 = std_cat5;
    }

    public boolean isStd_cat6() {
        return std_cat6;
    }

    public void setStd_cat6(boolean std_cat6) {
        this.std_cat6 = std_cat6;
    }

    public boolean isStd_cat7() {
        return std_cat7;
    }

    public void setStd_cat7(boolean std_cat7) {
        this.std_cat7 = std_cat7;
    }

    public boolean isStd_cat8() {
        return std_cat8;
    }

    public void setStd_cat8(boolean std_cat8) {
        this.std_cat8 = std_cat8;
    }

    public boolean isStd_cat9() {
        return std_cat9;
    }

    public void setStd_cat9(boolean std_cat9) {
        this.std_cat9 = std_cat9;
    }

    public boolean isStd_cat10() {
        return std_cat10;
    }

    public void setStd_cat10(boolean std_cat10) {
        this.std_cat10 = std_cat10;
    }
     */
 /*
    public String getMirr_merit() {
        return mirr_merit;
    }

    public String getMirr_academic() {
        return mirr_academic;
    }

    public int getMirr_tabindex() {
        return mirr_tabindex;
    }

    public void setMirr_merit(String mirr_merit) {
        this.mirr_merit = mirr_merit;
    }

    public void setMirr_academic(String mirr_academic) {
        this.mirr_academic = mirr_academic;
    }

    public void setMirr_tabindex(int mirr_tabindex) {
        this.mirr_tabindex = mirr_tabindex;
    }

    public String getFund7id() {
        return fund7id;
    }

    public void setFund7id(String fund7id) {
        this.fund7id = fund7id==null? "0":fund7id;
        stud.setFund7id(Integer.valueOf(this.fund7id));
    }

    public String getFund8id() {
        return fund8id;
    }

    public void setFund8id(String fund8id) {
        this.fund8id = fund8id==null? "0":fund8id;
        stud.setFund8id(Integer.valueOf(this.fund8id));
    }

    public String getFund9id() {
        return fund9id;
    }

    public void setFund9id(String fund9id) {
        this.fund9id = fund9id==null? "0":fund9id;
        stud.setFund9id(Integer.valueOf(this.fund9id));
    }
     */

    public String getAddlasuid() {
        return addlasuid;
    }

    public void setAddlasuid(String addlasuid) {
        this.addlasuid = addlasuid;
    }

    public void lasuidchged(AjaxBehaviorEvent event) {
        UIInput source = (UIInput) event.getSource(); //the object on which the event initially occoured
        String clientfid = source.getClientId();

        String lsuid = addlasuid;
        int flag = validateLsuId(lsuid); //-1:no value  0: invalid  1:valid
        if (flag < 1) {
            source.setValid(false);
        }

    }

    public int lasuidisok() {
        return validateLsuId(addlasuid);
    }

    public void resetlasuid(AjaxBehaviorEvent event) {
        addlasuid = "";
        log.info(" resetlasuid() clear lasuid");
    }

    public void submitlasuid(ActionEvent event) { //the func is not invoked!!!
        //public String submitlasuid(){ //no calling either. delete "type=button" worked for both, but AjaxBehaviorEvent for listener, ActionEvent for actionLisenter
        log.info(" ////// submitlasuid() is finally revoked.//////");
        FacesMessage msg = null;
        if (validateLsuId(addlasuid) == 1) {
            Querier qurier = ref.findBean2("querier");
            Student chosenstd = qurier.getSelectedStud();  //NPE
            String stdid = chosenstd.getStudentALsuid();

            if (chosenstd.getPickupInd() < 1) {
                msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "QueryForm.InactiveRecordLSUID");
                facesContext.addMessage(null, msg);
            } else {

                if (ref.isEmp(stdid)) {
                    chosenstd.setStudentALsuid(addlasuid);
                    //find dups and update, in EJB31 too
                    chosenstd.setEstmNumb(-1 * login.getCurrentUser().getUserid()); //default 0,   upload sets ( *-1) both sides

                    chosenstd.setTzdid(ref.getTzSN());
                    chosenstd.setDid(System.currentTimeMillis());
                    chosenstd.setDidstr(ref.fmtFullNowTimeWithDefaultZone());

                    accessor.saveSuppliedLasuId(chosenstd);

                    msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "QueryForm.SavedLSUID");
                    facesContext.addMessage(null, msg);
                } else {
                    msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "QueryForm.ExistingLSUID");
                    facesContext.addMessage(null, msg);
                }
            }
        } else {
            msg = ref.facesMessageByKey(FacesMessage.SEVERITY_ERROR, "QueryForm.InvalidSupplyLSUID");
            facesContext.addMessage(null, msg);
        }
        log.info(" submitlasuid() msg: %s  (lasuid=%s)", msg.getSummary(), addlasuid);
        addlasuid = "";
        //return "";
    }

    /**
     * @return the return_std_ind
     */
    public boolean isReturn_std_ind() {
        return return_std_ind;
    }

    /**
     * @param return_std_ind the return_std_ind to set
     */
    public void setReturn_std_ind(boolean return_std_ind) {
        this.return_std_ind = return_std_ind;
    }

    /**
     * @return the nc_std_ind
     */
    public boolean isNc_std_ind() {
        return nc_std_ind;
    }

    /**
     * @param nc_std_ind the nc_std_ind to set
     */
    public void setNc_std_ind(boolean nc_std_ind) {
        this.nc_std_ind = nc_std_ind;
    }

    public void returningIndChanged(AjaxBehaviorEvent event) { //no need recalc
        stud.setReturnStdInd(return_std_ind ? 1 : 0);
        stdo_return_ind = return_std_ind;
    }

    public void ncIndChanged(AjaxBehaviorEvent event) { //no need of recacl
        stud.setNcStdInd(nc_std_ind ? 1 : 0);
        stdo_nc_ind = nc_std_ind;
    }

    /**
     * @return the stdo_return_ind
     */
    public boolean isStdo_return_ind() {
        return stdo_return_ind;
    }

    /**
     * @return the stdo_nc_ind
     */
    public boolean isStdo_nc_ind() {
        return stdo_nc_ind;
    }

    private void setStudFAFSA(String str) {
        stud.setStudentXFafsa(str);
        //       log.info("^^^^^^^ set stud obj FAFSA attr=%s", str);
    }
}

/*
 //    @Inject static final FormatLogger  log;    //static    will  enable restore session while re-deploy: java.lang.ClassCastException: cannot assign instance of java.lang.String to field edu.lsu.estimator.FormatLogger.log of type org.apache.log4j.Logger in instance of edu.lsu.estimator.FormatLogger
    //but @PostConstruct    public void syncFromStudObj(){  will fail. why ???????    
    
 ///////   private static  FormatLogger log = new FormatLogger (Logger.getLogger("edu.lsu.estimator.Estimator"));//this.getClass().getName()));
    //transient
    //static: non-static variable this cannot be referenced from a static context
 */
